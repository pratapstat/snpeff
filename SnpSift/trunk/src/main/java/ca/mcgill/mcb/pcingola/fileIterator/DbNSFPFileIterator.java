package ca.mcgill.mcb.pcingola.fileIterator;

import gnu.trove.map.hash.TObjectIntHashMap;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.mcgill.mcb.pcingola.interval.Genome;

public class DbNSFPFileIterator extends MarkerFileIterator<DbNSFPEntry> {
	private TObjectIntHashMap<String> columnNames;
	private int chromosomeIdx;
	private int startIdx;

	public DbNSFPFileIterator(BufferedReader reader) {
		super(reader, 1);
	}

	public DbNSFPFileIterator(String fileName, Genome genome) {
		super(fileName, genome, 1);
	}

	public DbNSFPFileIterator(String fileName) {
		super(fileName, 1);
	}

	private DbNSFPEntry parseEntry(List<String[]> linesForEntry) {
		Map<String, Map<String, String>> values = new HashMap<String, Map<String, String>>();

		String chromosome = null;
		int start = -1;
		for (String[] altAlleleValues : linesForEntry) {
			Map<String, String> entryValues = new HashMap<String, String>();
			chromosome = altAlleleValues[chromosomeIdx];
			start = parsePosition(altAlleleValues[startIdx]);
			for (String columnName : columnNames.keySet()) {
				entryValues.put(columnName,
						altAlleleValues[columnNames.get(columnName)]);
			}
			values.put(entryValues.get("alt"), entryValues);
		}

		DbNSFPEntry entry = new DbNSFPEntry(getChromosome(chromosome), start,
				values);
		return entry;
	}

	@Override
	protected DbNSFPEntry readNext() {
		// Read another entry from the file
		try {
			List<String[]> linesForEntry = new ArrayList<String[]>();
			while (ready()) {
				line = readLine();
				if (line == null)
					return null; // End of file?

				if (columnNames.size() == 0) {
					line = line.substring(1);
					String values[] = split(line, '\t');
					for (int idx = 0; idx < values.length; idx++) {
						columnNames.put(values[idx], idx);
						if (values[idx].equals("chr"))
							chromosomeIdx = idx;
						else if (values[idx].equals("pos"))
							startIdx = idx;
					}
					continue;
				}
				String values[] = split(line, '\t');
				if (linesForEntry.size() > 0) {
					String currentValues[] = linesForEntry.get(0);
					if (currentValues[chromosomeIdx] != values[chromosomeIdx]
							|| currentValues[startIdx] != values[startIdx]) {
						nextLine = line;
						break;
					}
				}

				linesForEntry.add(values);
			}

			if (linesForEntry.size() == 0)
				return null;

			DbNSFPEntry entry = parseEntry(linesForEntry);
			return entry;

		} catch (IOException e) {
			throw new RuntimeException("Error reading file '" + fileName
					+ "'. Line ignored:\n\tLine (" + lineNum + "):\t'" + line
					+ "'");
		}
	}

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
		if (value == null)
			return null;
		StringTokenizer st = new StringTokenizer(value, delim);
		return st.tokens(String.class);
	}

	@SuppressWarnings("unchecked")
	static private class StringTokenizer {

		String string = null;
		int tokens = 0;
		int[] separatorPosition = new int[1000];

		StringTokenizer(String value, char delim) {
			this.string = value;
			// Loop on the characters counting the separators and remembering
			// their positions
			StringCharacterIterator sci = new StringCharacterIterator(string);
			char c = sci.first();
			while (c != StringCharacterIterator.DONE) {
				if (c == delim) {
					// Remember its position
					separatorPosition[tokens] = sci.getIndex();
					tokens++;

					// Resize the position array if needed
					if (tokens >= separatorPosition.length) {
						int[] copy = new int[separatorPosition.length * 10];
						System.arraycopy(separatorPosition, 0, copy, 0,
								separatorPosition.length);
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
				throw new RuntimeException(
						"Cannot create an array of type ["
								+ componentType
								+ "] from an array of String. The type ["
								+ componentType.getSimpleName()
								+ "] must define a single arg constructor that takes a String.class instance.");
			}

			String currentValue = null;
			int i = 0;
			try {
				int start = 0;
				for (i = 0; i < tokens; i++) {
					// Index of the token's last character (exclusive)
					int nextStart = separatorPosition[i];
					// Special case for the last token
					if (i == tokens - 1)
						nextStart = string.length();

					// Calculate the size of the token
					int length = nextStart - start;
					if (length > 0) {
						currentValue = string.substring(start, nextStart);
						r[i] = ctor.newInstance(currentValue);
					}
					start = nextStart + 1;
				}
			} catch (Exception e) {
				throw new RuntimeException(
						"Cannot create an instance of type [" + componentType
								+ "] from the " + i + "th string value ["
								+ currentValue + "].", e);
			}
			return r;
		}
	}

}
