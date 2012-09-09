package ca.mcgill.mcb.pcingola.snpSql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;

import ca.mcgill.mcb.pcingola.fileIterator.VcfFileIterator;
import ca.mcgill.mcb.pcingola.snpSql.db.DbUtil;
import ca.mcgill.mcb.pcingola.snpSql.db.Effect;
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

	public static boolean debug = false;
	public static int BATCH_SIZE = 100;
	public static int SHOW_EVERY = 100 * BATCH_SIZE;

	String vcfFileName;
	long entryId = 0, effectId = 0, tupleId = 0, tupleIntId = 0, tupleFloatId = 0;
	int batchSizeEntry = 0, batchSizeEff = 0, batchSizeTuple = 0, batchSizeTupleInt = 0, batchSizeTupleFloat = 0;
	PreparedStatement pstmtEntry, pstmtEff, pstmtTuple, pstmtTupleInt, pstmtTupleFloat;
	VcfEffect.FormatVersion formatVersion = VcfEffect.FormatVersion.FORMAT_SNPEFF_2;

	/**
	 * Add VCF effect
	 * @param vcfEffect
	 */
	void addEffect(long entryId, VcfEffect veff) {
		if (debug) Gpr.debug("Add Effect: " + veff);

		// Prepapre statement
		try {
			Effect eff = new Effect(veff);

			pstmtEff.setLong(1, effectId);
			pstmtEff.setString(2, eff.getEffect());
			pstmtEff.setString(3, eff.getImpact());
			pstmtEff.setString(4, eff.getFunClass());
			pstmtEff.setString(5, eff.getCodon());
			pstmtEff.setString(6, eff.getAa());
			pstmtEff.setInt(7, eff.getAaLen());
			pstmtEff.setString(8, eff.getGene());
			pstmtEff.setString(9, eff.getBioType());
			pstmtEff.setString(10, eff.getCoding());
			pstmtEff.setString(11, eff.getTranscriptId());
			pstmtEff.setString(12, eff.getExonId());
			pstmtEff.setLong(13, entryId);

			batchSizeEff++;
			effectId++;
			pstmtEff.addBatch();
		} catch (SQLException e) {
			throw new RuntimeException("Error trying to insert Effect\n\tRecord number " + effectId + "\n\rEffect:\t" + veff, e);
		}
	}

	/**
	 * Add VCF entry
	 * @param id
	 * @param vcfEntry
	 */
	void addEntry(VcfEntry vcfEntry) {
		if (debug) Gpr.debug("Add Entry: " + vcfEntry);

		// Prepapre statement
		try {
			pstmtEntry.setLong(1, entryId);
			pstmtEntry.setString(2, vcfEntry.getChromosomeName());
			pstmtEntry.setInt(3, vcfEntry.getStart());
			pstmtEntry.setString(4, vcfEntry.getId());
			pstmtEntry.setString(5, vcfEntry.getRef());
			pstmtEntry.setString(6, vcfEntry.getAltsStr());
			pstmtEntry.setDouble(7, vcfEntry.getQuality());
			pstmtEntry.setString(8, vcfEntry.getFilterPass());

			batchSizeEntry++;
			entryId++;
			pstmtEntry.addBatch();
		} catch (SQLException e) {
			throw new RuntimeException("Error trying to insert Entry.\n\tRecord number " + entryId + "\n\tVcfEntry:\t" + vcfEntry, e);
		}
	}

	/**
	 * Add info fields
	 */
	void addInfo(long entryId, VcfEntry vcfEntry, Collection<VcfInfo> vcfInfos) {
		try {
			for (VcfInfo vcfInfo : vcfInfos) {
				if (!vcfInfo.getId().startsWith("EFF")) {
					String name = vcfInfo.getId();
					String value = vcfEntry.getInfo(name);

					switch (vcfInfo.getVcfInfoType()) {
					case Flag:
						value = "true";
					case String:
					case Character:
						if (value != null) {
							if (debug) Gpr.debug("Add Tuple: " + name + " = " + value);
							pstmtTuple.setLong(1, tupleId++);
							pstmtTuple.setString(2, name);
							pstmtTuple.setString(3, value);
							pstmtTuple.setLong(4, entryId);

							batchSizeTuple++;
							pstmtTuple.addBatch();
						}
						break;
					case Integer:
						if (value != null) {
							long valInt = vcfEntry.getInfoInt(name);
							if (debug) Gpr.debug("Add TupleInt: " + name + " = " + valInt);
							pstmtTupleInt.setLong(1, tupleIntId++);
							pstmtTupleInt.setString(2, name);
							pstmtTupleInt.setLong(3, valInt);
							pstmtTupleInt.setLong(4, entryId);

							batchSizeTupleInt++;
							pstmtTupleInt.addBatch();
						}
						break;
					case Float:
						if (value != null) {
							double valFloat = vcfEntry.getInfoFloat(name);
							if (debug) Gpr.debug("Add TupleFloat: " + name + " = " + valFloat);
							pstmtTupleFloat.setLong(1, tupleFloatId++);
							pstmtTupleFloat.setString(2, name);
							pstmtTupleFloat.setDouble(3, valFloat);
							pstmtTupleFloat.setLong(4, entryId);

							batchSizeTupleFloat++;
							pstmtTupleFloat.addBatch();
						}
						break;
					default:
						Gpr.debug("Unknonw type: " + vcfInfo.getVcfInfoType());
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error trying to insert Info.\n\tVcfEntry:\t" + vcfEntry, e);
		}
	}

	/**
	 * Add the whole VCF file to the database
	 * @return
	 */
	boolean addVcfFile() {
		boolean ok = false;
		Timer.showStdErr("Reading data from file: '" + vcfFileName + "', database name '" + database + "', databse path: '" + databasePath + "'");

		try {
			//---
			// Prepapre statements
			//---
			Connection con = DbUtil.get().getConnection();
			prepareStetements(con);

			//---
			// Parse VCF file and add to database
			//---
			VcfFileIterator vcfFile = new VcfFileIterator(vcfFileName);
			Collection<VcfInfo> vcfInfos = null;
			Timer t = new Timer();
			for (VcfEntry vcfEntry : vcfFile) {
				if (debug) Gpr.debug(vcfEntry);
				if (vcfInfos == null) vcfInfos = vcfFile.getVcfHeader().getVcfInfo();

				// Add VcfEntry
				long eid = entryId; // It is incremented after the insert operation
				addEntry(vcfEntry);

				// Add effects
				for (VcfEffect veff : vcfEntry.parseEffects(formatVersion))
					addEffect(eid, veff);

				// Add Info fields
				addInfo(eid, vcfEntry, vcfInfos);

				// Send batch every now and then
				if (entryId % BATCH_SIZE == 0) sendBatch();

				//---
				// Show timer
				//---
				if (entryId % SHOW_EVERY == 0) {
					Timer.showStdErr("VCF entries: " + entryId + "\tEffects: " + effectId + "\tInfo fields: " + (tupleId + tupleIntId + tupleFloatId) + "\tElapsed: " + t);
					t.start();
				}
			}

			Timer.showStdErr("VCF entries: " + entryId + "\tEffects: " + effectId + "\tInfo fields: " + (tupleId + tupleIntId + tupleFloatId) + "\tElapsed: " + t);
			sendBatch(); // Send last batch

			Timer.showStdErr("Done.");
			ok = true;
		} catch (Exception e) {
			ok = false;
			e.printStackTrace();
		}
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

	/**
	 * Prepare insert statements
	 * @param con
	 */
	void prepareStetements(Connection con) {
		try {
			pstmtEntry = con.prepareStatement("INSERT INTO Entry (id, chrom, pos, vcfId, ref, alt, qual, filter) VALUES ( ?,   ?,   ?,     ?,   ?,   ?,    ?,      ?);");
			pstmtEff = con.prepareStatement("INSERT INTO Effect (id, effect, impact, funClass, codon, aa, aaLen, gene, bioType, coding, transcriptId, exonId, entry_id) VALUES (?,       ?,      ?,        ?,     ?,  ?,     ?,    ?,       ?,      ?,            ?,      ?,        ?);");
			pstmtTuple = con.prepareStatement("INSERT INTO Tuple (id, name, value, entry_id) VALUES ( ?,    ?,     ?,        ?);");
			pstmtTupleInt = con.prepareStatement("INSERT INTO TupleInt (id, name, value, entry_id) VALUES ( ?,    ?,     ?,        ?);");
			pstmtTupleFloat = con.prepareStatement("INSERT INTO TupleFloat (id, name, value, entry_id) VALUES ( ?,    ?,     ?,        ?);");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error preparing statement", e);
		}
	}

	@Override
	public boolean run() {
		boolean ok = false;

		// Create and start a new server
		if (verbose) Timer.showStdErr("Creating database '" + database + "', path '" + databasePath + "'");
		DbUtil.create(database, databasePath, true, true, verbose);

		try {
			// Connect to database. Even if we don't use Hibernate, this is done in order to get the database created.
			DbUtil.beginTransaction();

			// Add data
			ok = addVcfFile();

			DbUtil.commit();
		} catch (Throwable t) {
			t.printStackTrace();
			DbUtil.rollback();
			ok = false;
		}

		// Stop server
		if (verbose) Timer.showStdErr("Closing database.");
		DbUtil.get().stop();
		if (verbose) Timer.showStdErr("Done.");
		return ok;
	}

	/**
	 * Build database
	 * @param vcStringfFielName
	 * @return
	 */
	public boolean run(String vcStringfFielName) {
		this.vcfFileName = vcStringfFielName;
		setDatabseFomVcf(vcfFileName);
		return run();
	}

	void sendBatch() {
		try {
			// Timer.showStdErr("Batch:" + id);
			if (batchSizeEntry > 0) pstmtEntry.executeBatch();
			if (batchSizeEff > 0) pstmtEff.executeBatch();
			if (batchSizeTuple > 0) pstmtTuple.executeBatch();
			if (batchSizeTupleInt > 0) pstmtTupleInt.executeBatch();
			if (batchSizeTupleFloat > 0) pstmtTupleFloat.executeBatch();

			batchSizeEntry = batchSizeEff = batchSizeTuple = batchSizeTupleInt = batchSizeTupleFloat = 0;
		} catch (SQLException e) {
			throw new RuntimeException("Error sending batch to database: " + entryId, e);
		}

	}

	@Override
	public void usage(String message) {
		if (message != null) System.err.println("Error: " + message + "\n");
		System.err.println("SnpSql version " + VERSION);
		System.err.println("Usage: SnpSql [create] file.vcf");
		System.exit(-1);
	}

}
