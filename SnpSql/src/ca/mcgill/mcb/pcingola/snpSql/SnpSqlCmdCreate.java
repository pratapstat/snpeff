package ca.mcgill.mcb.pcingola.snpSql;

import java.util.Collection;

import ca.mcgill.mcb.pcingola.fileIterator.VcfFileIterator;
import ca.mcgill.mcb.pcingola.snpSql.db.Effect;
import ca.mcgill.mcb.pcingola.snpSql.db.Entry;
import ca.mcgill.mcb.pcingola.snpSql.db.HibernateUtil;
import ca.mcgill.mcb.pcingola.snpSql.db.Tuple;
import ca.mcgill.mcb.pcingola.snpSql.db.TupleFloat;
import ca.mcgill.mcb.pcingola.snpSql.db.TupleInt;
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

	public static int COMMIT_EVERY = 100;

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
			if (count % COMMIT_EVERY == 0) {
				Timer.showStdErr("Commit: " + count + "\t" + vdb);
				HibernateUtil.commit();
				HibernateUtil.beginTransaction();
			}
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
