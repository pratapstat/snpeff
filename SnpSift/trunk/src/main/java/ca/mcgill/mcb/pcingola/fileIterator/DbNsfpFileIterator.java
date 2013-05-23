package ca.mcgill.mcb.pcingola.fileIterator;

import gnu.trove.map.hash.TObjectIntHashMap;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * DbNSFP database: 
 * Reference	https://sites.google.com/site/jpopgen/dbNSFP
 * 
 * @author lletourn
 */
public class DbNsfpFileIterator extends MarkerFileIterator<DbNsfpEntry> {

	@SuppressWarnings("unchecked")
	static private class StringTokenizer {

		String string = null;
		int tokens = 0;
		int[] separatorPosition = new int[1000];

		StringTokenizer(String value, char delim) {
			string = value;
			// Loop on the characters counting the separators and remembering
			// their positions
			StringCharacterIterator sci = new StringCharacterIterator(string);
			char c = sci.first();
			while (c != CharacterIterator.DONE) {
				if (c == delim) {
					// Remember its position
					separatorPosition[tokens] = sci.getIndex();
					tokens++;

					// Resize the position array if needed
					if (tokens >= separatorPosition.length) {
						int[] copy = new int[separatorPosition.length * 10];
						System.arraycopy(separatorPosition, 0, copy, 0, separatorPosition.length);
						separatorPosition = copy;
					}
				}
				c = sci.next();
			}
			// Add one token: tokens = separatorCount + 1
			tokens++;
		}

		<T> T[] tokens(Class<T> componentType) {
			T[] r = (T[]) Array.newInstance(componentType, tokens);
			Constructor<T> ctor;
			try {
				ctor = componentType.getConstructor(String.class);
			} catch (NoSuchMethodException e) {
				throw new RuntimeException("Cannot create an array of type [" + componentType + "] from an array of String. The type [" + componentType.getSimpleName() + "] must define a single arg constructor that takes a String.class instance.");
			}

			String currentValue = null;
			int i = 0;
			try {
				int start = 0;
				for (i = 0; i < tokens; i++) {
					// Index of the token's last character (exclusive)
					int nextStart = separatorPosition[i];
					// Special case for the last token
					if (i == tokens - 1) nextStart = string.length();

					// Calculate the size of the token
					int length = nextStart - start;
					if (length > 0) {
						currentValue = string.substring(start, nextStart);
						r[i] = ctor.newInstance(currentValue);
					}
					start = nextStart + 1;
				}
			} catch (Exception e) {
				throw new RuntimeException("Cannot create an instance of type [" + componentType + "] from the " + i + "th string value [" + currentValue + "].", e);
			}
			return r;
		}
	}

	public static final String COLUMN_CHR_NAME = "chr";
	public static final String COLUMN_POS_NAME = "pos(1-coor)";

	private final TObjectIntHashMap<String> columnNames = new TObjectIntHashMap<String>();
	private int chromosomeIdx;
	private int startIdx;

	/**
	 * Splits a separated string into an array of <code>String</code> tokens. If
	 * the input string is null, this method returns null.
	 * 
	 * <p/>
	 * Implementation note: for performance reasons, this implementation uses
	 * neither StringTokenizer nor String.split(). StringTokenizer does not
	 * return all tokens for strings of the form "1,2,,3," unless you use an
	 * instance that returns the separator. By doing so, our code would need to
	 * modify the token string which would create another temporary object and
	 * would make this method very slow. <br/>
	 * String.split does not return all tokens for strings of the form
	 * "1,2,3,,,". We simply cannot use this method.
	 * <p/>
	 * The result is a custom String splitter algorithm which performs well for
	 * large Strings.
	 * 
	 * @param value
	 *            the string value to split into tokens
	 * @return an array of String Objects or null if the string value is null
	 */
	static public String[] split(final String value, char delim) {
		if (value == null) return null;
		StringTokenizer st = new StringTokenizer(value, delim);
		return st.tokens(String.class);
	}

	public DbNsfpFileIterator(BufferedReader reader) {
		super(reader, 1);
	}

	public Set<String> getFieldNames() {
		if (columnNames.size() == 0) parseHeader(null);
		return columnNames.keySet();
	}

	/**
	 * Do we have a column 'colName'?
	 * @param filedName
	 * @return
	 */
	public boolean hasField(String filedName) {
		if (columnNames.size() == 0) parseHeader(null);
		return columnNames.containsKey(filedName);
	}

	private DbNsfpEntry parseEntry(List<String[]> linesForEntry) {
		Map<String, Map<String, String>> values = new HashMap<String, Map<String, String>>();

		String chromosome = null;
		int start = -1;
		for (String[] altAlleleValues : linesForEntry) {
			Map<String, String> entryValues = new HashMap<String, String>();
			chromosome = altAlleleValues[chromosomeIdx];
			start = parsePosition(altAlleleValues[startIdx]);
			for (String columnName : columnNames.keySet()) {
				entryValues.put(columnName, altAlleleValues[columnNames.get(columnName)]);
			}
			values.put(entryValues.get("alt"), entryValues);
		}

		DbNsfpEntry entry = new DbNsfpEntry(getChromosome(chromosome), start, values);
		return entry;
	}

	/**
	 * Parse header line
	 * @param line
	 */
	void parseHeader(String line) {
		// No line provided? Read one
		if (line == null) {
			try {
				if (ready()) line = readLine();
				else throw new RuntimeException("Error reading (parsing header) from file '" + fileName + "'.");
			} catch (IOException e) {
				throw new RuntimeException("Error reading file '" + fileName + "'. Line ignored:\n\tLine (" + lineNum + "):\t'" + line + "'");
			}
		}

		// Parse column names
		line = line.substring(1); // Remove first '#' character
		String values[] = split(line, '\t');

		// Add all column names to hash
		for (int idx = 0; idx < values.length; idx++) {
			columnNames.put(values[idx].trim(), idx);

			// Chromosome and position column numbers
			if (values[idx].equals(COLUMN_CHR_NAME)) chromosomeIdx = idx;
			else if (values[idx].equals(COLUMN_POS_NAME)) startIdx = idx;
		}

		// Errors?
		if (chromosomeIdx == -1 || startIdx == -1) throw new RuntimeException("Missing '" + COLUMN_CHR_NAME + "' and/or '" + COLUMN_POS_NAME + "' columns in dbNSFP file");
	}

	@Override
	protected DbNsfpEntry readNext() {
		// Read another entry from the file
		try {
			List<String[]> linesForEntry = new ArrayList<String[]>();
			while (ready()) {
				line = readLine();
				if (line == null) return null; // End of file?

				// Do we need to parse header?
				if (columnNames.size() == 0) {
					parseHeader(line);
					continue;
				}

				// Parse data
				String values[] = split(line, '\t');
				if (linesForEntry.size() > 0) {
					String currentValues[] = linesForEntry.get(0);
					if (!currentValues[chromosomeIdx].equals(values[chromosomeIdx]) || !currentValues[startIdx].equals(values[startIdx])) {
						nextLine = line;
						break;
					}
				}

				linesForEntry.add(values);
			}

			if (linesForEntry.size() == 0) return null;

			DbNsfpEntry entry = parseEntry(linesForEntry);
			return entry;

		} catch (IOException e) {
			throw new RuntimeException("Error reading file '" + fileName + "'. Line ignored:\n\tLine (" + lineNum + "):\t'" + line + "'");
		}
	}

	@Override
	public void seek(long pos) throws IOException {
		super.seek(pos);
		nextLine = null;
	}

}
