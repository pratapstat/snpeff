package ca.mcgill.mcb.pcingola.snpSift;

import java.util.List;

import ca.mcgill.mcb.pcingola.fileIterator.NeedlemanWunsch;
import ca.mcgill.mcb.pcingola.fileIterator.VcfFileIterator;
import ca.mcgill.mcb.pcingola.interval.SeqChange;
import ca.mcgill.mcb.pcingola.vcf.VcfEntry;

/**
 * Simplify indels
 * 
 * E.g.
 * 		Instead of 
 * 
 * 		Chr1 13204566 . AATATATATATATATATATATGTATATATATAT AATATATATATATATATATGTATATATATAT
 * 
 * 		Writes
 * 
 * 		Chr1 13204585 . TAT T
 * 
 * 	Notice the real difference:
 * 		AATATATATATATATATATATGTATATATATAT 
 * 		AATATATATATATATATAT--GTATATATATAT
 * 
 * 
 * @author pablocingolani
 */
public class SnpSiftCmdSimplifyIndels extends SnpSift {

	String vcfFileName;

	public SnpSiftCmdSimplifyIndels(String[] args) {
		super(args, "SimplifyIndels");
	}

	/**
	 * Parse command line arguments
	 * @param args
	 */
	@Override
	public void parse(String[] args) {
		for (int i = 0; i < args.length; i++) {
			String arg = args[i];

			// Argument starts with '-'?
			if (arg.startsWith("-")) {
				usage("Unknown command line option '" + arg + "'");
			} else {
				if (vcfFileName == null) vcfFileName = arg;
				else usage("Unknown argunment '" + arg + "'");
			}
		}

		// Sanity check
		if (vcfFileName == null) usage("Missing VCF file");
	}

	/**
	 * Run algorithm
	 */
	@Override
	public void run() {
		VcfFileIterator vcf = new VcfFileIterator(vcfFileName);
		for (VcfEntry ve : vcf) {
			if (vcf.isHeadeSection()) System.out.println(vcf.getVcfHeader());

			if (ve.isInDel()) {
				// Simplify the indel, if possible
				List<SeqChange> seqChanges = ve.seqChanges();

				// We can only simplify when there is only one InDel 
				if (ve.isMultipleAlts()) {
					// Multi-allelic InDels? I'm too lazy to simplify it...(show as it is)
					System.out.println(ve);
				} else {
					// Simplify & Print
					System.out.println(simplifyInDel(ve));
				}
			} else {
				// Not an indel? Cannot simplify (show as it is)
				System.out.println(ve);
			}
		}
	}

	/**
	 * Simplify and print
	 * @param ve
	 * @param seqChange
	 * @return
	 */
	String simplifyInDel(VcfEntry ve) {
		String line = ve.toString(); // Create the real line

		// Align
		NeedlemanWunsch nw = new NeedlemanWunsch(ve.getRef(), ve.getAltsStr());
		nw.align();
		int startDiff = nw.getOffset();
		String ch = nw.getAlignment();
		if (!(ch.startsWith("-") || ch.startsWith("+"))) {
			// VCF entry could not be properly aligned.
			// Probably a mixed variant: Nothing to do
			return ve.toString();
		}

		// Calculate new fields
		int pos = ve.getStart() + startDiff;
		String ref = "", alt = "";
		if (ve.getRef().length() > ve.getAltsStr().length()) {
			if (startDiff > 0) ref = "" + ve.getRef().charAt(startDiff - 1);
			alt = ref + nw.getAlignment().substring(1);
		} else {
			if (startDiff > 0) alt = "" + ve.getRef().charAt(startDiff - 1);
			ref = alt + nw.getAlignment().substring(1);
		}

		// Replace VCF fields using "simplified ones"
		String fields[] = line.split("\t");
		fields[1] = "" + pos; // Replace POS coordinate
		fields[3] = ref; // Replace REF field
		fields[4] = alt; // Replace ALT field

		// Put all the fields in one line
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < fields.length; i++)
			sb.append((i > 0 ? "\t" : "") + fields[i]);

		return sb.toString();
	}

	/**
	* Show usage message
	* @param msg
	*/
	@Override
	public void usage(String msg) {
		if (msg != null) {
			System.err.println("Error: " + msg);
			showCmd();
		}

		showVersion();

		System.err.println("Usage: java -jar " + SnpSift.class.getSimpleName() + ".jar SimplifyIndels [options] file.vcf");
		System.exit(1);
	}
}
