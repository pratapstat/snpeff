package ca.mcgill.mcb.pcingola.snpSql;

import java.util.Collection;

import ca.mcgill.mcb.pcingola.fileIterator.VcfFileIterator;
import ca.mcgill.mcb.pcingola.snpSql.db.DbUtil;
import ca.mcgill.mcb.pcingola.snpSql.db.Effect;
import ca.mcgill.mcb.pcingola.snpSql.db.Entry;
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

	public static int FLUSH_EVERY = 100;

	String vcfFileName;

	/**
	 * Add info fields
	 */
	void addInfo(Entry entry, VcfEntry vcfEntry, Collection<VcfInfo> vcfInfos) {
		for (VcfInfo vcfInfo : vcfInfos) {
			if (!vcfInfo.getId().startsWith("EFF")) {
				String name = vcfInfo.getId();
				String value = vcfEntry.getInfo(name);

				switch (vcfInfo.getVcfInfoType()) {
				case STRING:
				case CHARACTER:
					if (value != null) {
						Tuple tuple = new Tuple(name, value);
						entry.add(tuple);
						tuple.save();
					}
					break;
				case INTEGER:
					if (value != null) {
						long valInt = vcfEntry.getInfoInt(name);
						TupleInt tuple = new TupleInt(name, valInt);
						entry.add(tuple);
						tuple.save();
					}
					break;
				case FLOAT:
					if (value != null) {
						double valFloat = vcfEntry.getInfoFloat(name);
						TupleFloat tuple = new TupleFloat(name, valFloat);
						entry.add(tuple);
						tuple.save();
					}
					break;
				case FLAG:
					value = "true";
					Tuple tuple = new Tuple(name, value);
					entry.add(tuple);
					tuple.save();
					break;
				}
			}
		}
	}

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
			Entry entry = new Entry(vcfEntry);
			entry.save();

			// Add effects
			for (VcfEffect veff : vcfEntry.parseEffects()) {
				Effect effect = new Effect(veff);
				entry.add(effect);
				effect.save();
			}

			// Add info field
			//addInfo(entry, vcfEntry, vcfInfos);

			// Commit every now and then
			count++;
			if (count % FLUSH_EVERY == 0) {
				DbUtil.getCurrentSession().flush();
				DbUtil.getCurrentSession().clear();
				Timer.showStdErr("Commit: " + count + "\t" + entry);
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

		setDatabseFomVcf(vcfFileName);
	}

	@Override
	public boolean run() {
		boolean ok = false;

		// Create and start a new server
		if (verbose) Timer.showStdErr("Creating database '" + database + "', path '" + databasePath + "'");
		DbUtil.create(database, databasePath, true, verbose);

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
		if (verbose) Timer.showStdErr("Closing database.");
		DbUtil.get().stop();
		if (verbose) Timer.showStdErr("Done.");
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
