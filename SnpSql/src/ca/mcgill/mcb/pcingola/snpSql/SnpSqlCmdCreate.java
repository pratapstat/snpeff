package ca.mcgill.mcb.pcingola.snpSql;

import ca.mcgill.mcb.pcingola.fileIterator.VcfFileIterator;
import ca.mcgill.mcb.pcingola.snpSql.db.HibernateUtil;
import ca.mcgill.mcb.pcingola.snpSql.db.VcfEntryDb;
import ca.mcgill.mcb.pcingola.util.Timer;
import ca.mcgill.mcb.pcingola.vcf.VcfEffect;
import ca.mcgill.mcb.pcingola.vcf.VcfEntry;

/**
 * Create a database using a VCF file
 * 
 * @author pablocingolani
 */
public class SnpSqlCmdCreate extends SnpSql {

	String database;
	String vcfFileName;

	@Override
	public void parseArgs(String[] args) {
		this.args = args;
		for (int i = 0; i < args.length; i++) {

			// Argument starts with '-'?
			if (args[i].startsWith("-")) {
				if (args[i].equals("-v") || args[i].equalsIgnoreCase("-verbose")) {
					verbose = true;
				}
			} else {
				if (vcfFileName == null) vcfFileName = args[i];
			}
		}

		// Sanity checks
		if (vcfFileName == null) usage("Missing vcf file.");
	}

	@Override
	public boolean run() {
		boolean ok = false;
		Timer.showStdErr("Creating database from file: '" + vcfFileName + "'");

		// Connect to database
		HibernateUtil.beginTransaction();

		// Add info from VCF
		VcfFileIterator vcfFile = new VcfFileIterator(vcfFileName);
		for (VcfEntry vcfEntry : vcfFile) {
			VcfEntryDb vdb = new VcfEntryDb(vcfEntry);
			vdb.save();

			for (VcfEffect veff : vcfEntry.parseEffects()) {
				System.out.println(veff);
			}
			//			String eff = ve.getInfo("EFF");
			//			if (eff != null) {
			//				System.out.println(eff);
			//				Tuple teff = new Tuple("EFF", eff.substring(0, 30));
			//				vdb.add(teff);
			//				teff.save();
			//			}

		}

		// Close connection
		HibernateUtil.commit();
		HibernateUtil.close();

		Timer.showStdErr("Done.");
		return ok;
	}

	@Override
	public void usage(String message) {
		if (message != null) System.err.println("Error: " + message + "\n");
		System.err.println("SnpSql version " + VERSION);
		System.err.println("Usage: SnpSql [create] file.vcf");
		System.exit(-1);
	}

}
