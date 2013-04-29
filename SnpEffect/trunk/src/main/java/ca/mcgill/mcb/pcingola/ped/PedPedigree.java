package ca.mcgill.mcb.pcingola.ped;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import ca.mcgill.mcb.pcingola.util.Gpr;

/**
 * A pedigree of PedEntries
 * 
 * @author pcingola
 */
public class PedPedigree implements Iterable<TfamEntry> {

	boolean verbose = false;
	HashMap<String, TfamEntry> tfamEntryById = new HashMap<String, TfamEntry>();
	List<TfamEntry> tfamEntries;
	PlinkMap plinkMap;

	public PedPedigree() {
		tfamEntryById = new HashMap<String, TfamEntry>();
		tfamEntries = new ArrayList<TfamEntry>();
	}

	public PedPedigree(String tfamFileName) {
		tfamEntryById = new HashMap<String, TfamEntry>();
		tfamEntries = new ArrayList<TfamEntry>();
		loadTfam(tfamFileName);
	}

	/**
	 * Add an entry to this pedigree
	 * @param tfamEntry
	 */
	public void add(TfamEntry tfamEntry) {
		tfamEntryById.put(tfamEntry.getId(), tfamEntry);
		tfamEntries.add(tfamEntry);
	}

	public TfamEntry get(String id) {
		return tfamEntryById.get(id);
	}

	public PlinkMap getPlinkMap() {
		return plinkMap;
	}

	/**
	 * Get a list of sample IDs
	 * @return
	 */
	public List<String> getSampleIds() {
		ArrayList<String> sampleIds = new ArrayList<String>();
		for (TfamEntry te : tfamEntries)
			sampleIds.add(te.id);
		return sampleIds;
	}

	@Override
	public Iterator<TfamEntry> iterator() {
		return tfamEntries.iterator();
	}

	public Set<String> keySet() {
		return tfamEntryById.keySet();
	}

	/**
	 * Load a pedigree from a PED and MAP file pair
	 * 
	 * @param pedFileName
	 */
	public void load(String pedFileName) {
		String pedBaseFileName = Gpr.removeExt(pedFileName);
		String mapFile = pedBaseFileName + ".map";

		PedFileIterator pedFile = new PedFileIterator(pedFileName, mapFile);

		// Load all entries for this family
		int count = 1;
		for (PedEntry pe : pedFile) {
			if (verbose) Gpr.showMarkStderr(count++, 1);
			add(pe);
		}

		plinkMap = pedFile.getPlinkMap();
	}

	/**
	 * Load a TFAM file
	 * @param tfamFileName
	 */
	public void loadTfam(String tfamFileName) {
		String tfamStr = Gpr.readFile(tfamFileName);
		if (tfamStr.isEmpty()) throw new RuntimeException("Cannot read file '" + tfamFileName + "'");

		for (String line : tfamStr.split("\n")) {
			TfamEntry te = new TfamEntry(line);
			add(te);
		}
	}

	/**
	 * Save pedigree as a TFAM file
	 * @param fileName
	 */
	public void saveTfam(String fileName) {
		StringBuilder sb = new StringBuilder();
		for (TfamEntry te : this)
			sb.append(te + "\n");

		Gpr.toFile(fileName, sb.toString());
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	public int size() {
		return tfamEntryById.size();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (TfamEntry te : this)
			sb.append(te.toString() + "\n");
		return sb.toString();
	}

	public Collection<TfamEntry> values() {
		return tfamEntryById.values();
	}
}
