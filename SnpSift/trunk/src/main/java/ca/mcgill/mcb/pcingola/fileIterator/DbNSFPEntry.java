package ca.mcgill.mcb.pcingola.fileIterator;

import java.util.HashMap;
import java.util.Map;

import ca.mcgill.mcb.pcingola.interval.Marker;

/**
 * DbNSFP database entry: 
 * Reference	https://sites.google.com/site/jpopgen/dbNSFP
 * 
 * @author lletourn
 */
public class DbNSFPEntry extends Marker {
	private static final long serialVersionUID = -3275792763917755927L;
	Map<String, Map<String, String>> values = new HashMap<String, Map<String, String>>();

	public DbNSFPEntry(Marker parent, int start, Map<String, Map<String, String>> values) {
		super(parent, start, start, 1, "");
		this.values = values;
	}

	public Map<String, String> getAltAlelleValues(String allele) {
		return values.get(allele);
	}

	/**
	 * Merge the values of 2 DbNSFPEntry.
	 * 
	 * @param entry
	 * @return true if they were merged. False if they are not the same entry.
	 */
	public boolean mergeAndReplace(DbNSFPEntry entry) {
		if (compareTo(entry) != 0) return false;

		for (String altAllele : entry.values.keySet())
			values.put(altAllele, entry.values.get(altAllele));

		return true;
	}
}
