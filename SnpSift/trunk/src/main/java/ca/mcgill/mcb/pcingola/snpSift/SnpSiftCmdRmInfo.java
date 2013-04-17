package ca.mcgill.mcb.pcingola.snpSift;

import java.util.HashSet;

import ca.mcgill.mcb.pcingola.fileIterator.VcfFileIterator;
import ca.mcgill.mcb.pcingola.util.Timer;
import ca.mcgill.mcb.pcingola.vcf.VcfEntry;

/**
 * Removes INFO fields
 * 
 * @author pablocingolani
 */
public class SnpSiftCmdRmInfo extends SnpSift {

	String vcfFileName;
	HashSet<String> infos;

	public SnpSiftCmdRmInfo(String[] args) {
		super(args, "rmInfo");
	}

	@Override
	public void parse(String[] args) {
		infos = new HashSet<String>();
		if (args.length == 0) usage(null);

		for (String arg : args) {
			if (isOpt(arg)) usage("Unknown option " + arg);
			else if (vcfFileName == null) vcfFileName = arg;
			else infos.add(arg);
		}

		// Sanity check
		if (infos.size() <= 0) usage("No INFO field names provided.");
	}

	/**
	 * Analyze the file
	 */
	@Override
	public void run() {
		Timer.showStdErr("Reading STDIN");
		VcfFileIterator vcfFile = new VcfFileIterator(vcfFileName);

		// Read all vcfEntries
		int entryNum = 1;
		for (VcfEntry vcfEntry : vcfFile) {
			// Show header?
			if (entryNum == 1) {
				String headerStr = vcfFile.getVcfHeader().toString();
				if (!headerStr.isEmpty()) System.out.println(headerStr);
			}

			for (String info : infos) {
				vcfEntry.rmInfo(info);
			}
			// Show entry
			System.out.println(vcfEntry);
			entryNum++;
		}

		Timer.showStdErr("Done");
	}

	/**
	 * Show usage and exit
	 */
	@Override
	public void usage(String errMsg) {
		if (errMsg != null) System.err.println("Error: " + errMsg);
		System.err.println("Usage: java -jar " + SnpSift.class.getSimpleName() + "" + ".jar rmInfo [options] file.vcf infoField_1 infoField_2 ... infoField_N > file_out.vcf");
		System.exit(1);
	}
}
