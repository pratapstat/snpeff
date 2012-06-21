package ca.mcgill.mcb.pcingola.snpSql;

import java.util.Collection;

import ca.mcgill.mcb.pcingola.fileIterator.VcfFileIterator;
import ca.mcgill.mcb.pcingola.snpSql.db.DbUtil;
import ca.mcgill.mcb.pcingola.snpSql.db.Effect;
import ca.mcgill.mcb.pcingola.snpSql.db.Entry;
import ca.mcgill.mcb.pcingola.snpSql.db.Tuple;
import ca.mcgill.mcb.pcingola.snpSql.db.TupleFloat;
import ca.mcgill.mcb.pcingola.snpSql.db.TupleInt;
import ca.mcgill.mcb.pcingola.util.Gpr;
import ca.mcgill.mcb.pcingola.util.Timer;
import ca.mcgill.mcb.pcingola.vcf.VcfEffect;
import ca.mcgill.mcb.pcingola.vcf.VcfEntry;
import ca.mcgill.mcb.pcingola.vcf.VcfInfo;

/**
 * Create a database using a VCF file
 * 
 * @author pablocingolani
 */
public class SnpSqlCmdCreate extends SnpSql {

	public static int FLUSH_EVERY = 100;

	String databasePath;
	String database;
	String vcfFileName;

	/**
	 * Add the whole VCF file to the database
	 * @return
	 */
	boolean addVcfFile() {
		boolean ok = false;
		Timer.showStdErr("Reading data from file: '" + vcfFileName + "', database name '" + database + "', databse path: '" + databasePath + "'");

		// Add info from VCF
		VcfFileIterator vcfFile = new VcfFileIterator(vcfFileName);
		Collection<VcfInfo> vcfInfos = null;
		int count = 1;
		for (VcfEntry vcfEntry : vcfFile) {
			if (vcfInfos == null) {
				vcfInfos = vcfFile.getVcfInfo();
			}

			// Add entry
			Entry vdb = new Entry(vcfEntry);
			vdb.save();

			// Add effects
			for (VcfEffect veff : vcfEntry.parseEffects()) {
				Effect effect = new Effect(veff);
				vdb.add(effect);
				effect.save();
			}

			// Add info fields
			for (VcfInfo vcfInfo : vcfInfos) {
				if (!vcfInfo.getId().startsWith("EFF")) {
					String name = vcfInfo.getId();
					String value = vcfEntry.getInfo(name);

					switch (vcfInfo.getVcfInfoType()) {
					case STRING:
					case CHARACTER:
						if (value != null) {
							Tuple tuple = new Tuple(name, value);
							vdb.add(tuple);
							tuple.save();
						}
						break;
					case INTEGER:
						if (value != null) {
							long valInt = vcfEntry.getInfoInt(name);
							TupleInt tuple = new TupleInt(name, valInt);
							vdb.add(tuple);
							tuple.save();
						}
						break;
					case FLOAT:
						if (value != null) {
							double valFloat = vcfEntry.getInfoFloat(name);
							TupleFloat tuple = new TupleFloat(name, valFloat);
							vdb.add(tuple);
							tuple.save();
						}
						break;
					case FLAG:
						value = "true";
						Tuple tuple = new Tuple(name, value);
						vdb.add(tuple);
						tuple.save();
						break;
					}
				}
			}

			// Commit every now and then
			count++;
			if (count % FLUSH_EVERY == 0) {
				DbUtil.getCurrentSession().flush();
				DbUtil.getCurrentSession().clear();
				Timer.showStdErr("Commit: " + count + "\t" + vdb);
			}
		}

		Timer.showStdErr("Done.");
		return ok;
	}

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
		database = Gpr.baseName(vcfFileName);
		databasePath = Gpr.dirName(vcfFileName) + "/" + database;
	}

	@Override
	public boolean run() {
		boolean ok = false;

		// Create and start a new server
		DbUtil.create(database, databasePath, verbose);

		try {
			// Connect to database
			DbUtil.beginTransaction();

			// Add data
			ok = addVcfFile();

			// Close connection
			DbUtil.commit();
		} catch (Throwable t) {
			DbUtil.rollback();
		}

		// Stop server
		DbUtil.get().stop();
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
