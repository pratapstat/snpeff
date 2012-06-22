package ca.mcgill.mcb.pcingola.snpSql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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

	public static boolean debug = false;
	public static int BATCH_SIZE = 100;
	public static int SHOW_EVERY = 100 * BATCH_SIZE;

	long entryId = 0, effectId = 0;
	String vcfFileName;
	PreparedStatement pstmtEntry, pstmtEff;

	/**
	 * Add VCF effect
	 * @param vcfEffect
	 */
	void addEffect(VcfEffect veff) {
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

			pstmtEff.addBatch();
			effectId++;
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

			entryId++;
			pstmtEntry.addBatch();
		} catch (SQLException e) {
			throw new RuntimeException("Error trying to insert Entry.\n\tRecord number " + entryId + "\n\tVcfEntry:\t" + vcfEntry, e);
		}
	}

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
		boolean batchPrepapred = false;
		for (VcfEntry vcfEntry : vcfFile) {
			if (debug) Gpr.debug(vcfEntry);
			if (vcfInfos == null) vcfInfos = vcfFile.getVcfInfo();

			// Add VcfEntry
			addEntry(vcfEntry);

			// Add effects
			for (VcfEffect veff : vcfEntry.parseEffects())
				addEffect(veff);

			batchPrepapred = true;

			//---
			// Send batch
			//---
			try {
				if (entryId % BATCH_SIZE == 0) {
					// Timer.showStdErr("Batch:" + id);
					pstmtEntry.executeBatch();
					pstmtEff.executeBatch();
					batchPrepapred = false;
				}
			} catch (SQLException e) {
				throw new RuntimeException("Error sending batch to database: " + entryId, e);
			}

			//---
			// Show timer
			//---
			if (entryId % SHOW_EVERY == 0) {
				Timer.showStdErr("EntryId: " + entryId + "\tEffectId: " + effectId + "\tElapsed: " + t);
				t.start();
			}
		}

		//---
		// Send last batch
		//---
		try {
			if (batchPrepapred) {
				Timer.showStdErr("EntryId: " + entryId + "\tEffectId: " + effectId + "\tElapsed: " + t);
				pstmtEntry.executeBatch();
				pstmtEff.executeBatch();
				batchPrepapred = false;
			}
		} catch (SQLException e) {
			throw new RuntimeException("Error sending batch to database: " + entryId, e);
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

	/**
	 * Prepare insert statements
	 * @param con
	 */
	void prepareStetements(Connection con) {
		try {
			pstmtEntry = con.prepareStatement("INSERT INTO Entry (id, chr, pos, vcfId, ref, alt, qual, filter) VALUES " //
					+ "                                          ( ?,   ?,   ?,     ?,   ?,   ?,    ?,      ?);" //
			);
			pstmtEff = con.prepareStatement("INSERT INTO Effect (id, effect, impact, funClass, codon, aa, aaLen, gene, bioType, coding, transcriptId, exonId) VALUES " //
					+ "                                         (?,       ?,      ?,        ?,     ?,  ?,     ?,    ?,       ?,      ?,            ?,      ?);" //
			);
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
