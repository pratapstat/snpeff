package ca.mcgill.mcb.pcingola.snpSift;

import java.util.ArrayList;

import ca.mcgill.mcb.pcingola.fileIterator.VcfFileIterator;
import ca.mcgill.mcb.pcingola.snpSift.lang.expression.Field;
import ca.mcgill.mcb.pcingola.vcf.VcfEntry;

/**
 * Extract fields from VCF file to a TXT (tab separated) format
 * 
 * @author pablocingolani
 */
public class SnpSiftCmdExtractFields extends SnpSift {

	public static final int SHOW = 10000;

	String vcfFile;
	ArrayList<String> fieldNames;
	ArrayList<Field> fields;

	/**
	 * Main
	 * @param args
	 */
	public static void main(String[] args) {
		SnpSiftCmdExtractFields vcfExtractFields = new SnpSiftCmdExtractFields(args);
		vcfExtractFields.run();
	}

	public SnpSiftCmdExtractFields(String args[]) {
		super(args, "extractFields");
	}

	/**
	 * Parse command line arguments
	 */
	@Override
	public void parse(String[] args) {
		int argNum = 0;
		if (args.length == 0) usage(null);

		if (args.length >= argNum) vcfFile = args[argNum++];
		else usage("Missing 'file.vcf'");

		// Read all field named
		fieldNames = new ArrayList<String>();
		for (int i = argNum; i < args.length; i++)
			fieldNames.add(args[i]);

		if (fieldNames.isEmpty()) usage("Missing field names");
	}

	/**
	 * Run main algorithm
	 */
	@Override
	public void run() {
		String sep = "#";

		// Create fields and show title
		fields = new ArrayList<Field>();
		for (String fieldName : fieldNames) {
			Field field = new Field(fieldName);
			field.setExceptionIfNotFound(false); // Otherwise exceptions are thrown
			fields.add(field);

			System.out.print(sep + fieldName);
			sep = "\t";
		}
		System.out.println("");

		// Iterate on file
		VcfFileIterator vcf = new VcfFileIterator(vcfFile);
		for (VcfEntry ve : vcf) {
			sep = "";
			for (Field f : fields) {
				String val = f.getFieldString(ve);
				if (val == null) val = "";

				System.out.print(sep + val);
				sep = "\t";
			}

			System.out.println("");
		}
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

		System.err.println("Usage: java -jar " + SnpSift.class.getSimpleName() + ".jar extractFields file.vcf filedName1 filedName2 ... filedNameN > tabFile.txt");
		System.exit(1);
	}
}
