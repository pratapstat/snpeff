package ca.mcgill.mcb.pcingola.fileIterator;

import ca.mcgill.mcb.pcingola.interval.SeqChange.ChangeType;

/**
 * Needleman-Wunsch (global sequence alignment) algorithm for sequence  alignment (short strings, since it's not memory optimized)
 * 
 * @author pcingola
 */
public class VcfRefAltAlign extends NeedlemanWunsch {

	public static final int MAX_SIZE = 10000; // 1024 * 1024;

	String stringA, stringB;
	ChangeType changeType;

	public VcfRefAltAlign(String a, String b) {
		super(a, b);
		stringA = a;
		stringB = b;
	}

	@Override
	public String align() {
		try {
			if (simpleAlign()) {
				// OK Nothing else to do 
			} else {
				// Perform alignment only of sequences are not too long (we don't want an 'out of memory' issue)
				long size = ((long) stringA.length()) * stringB.length();
				if ((size > 0) && (size < MAX_SIZE)) {
					scoreMatrix();
					calcAlignment();

					if (stringB.length() > stringA.length()) {
						if (alignment.startsWith("-")) {
							changeType = ChangeType.DEL;
							return alignment;
						}
					} else if (stringB.length() < stringA.length()) {
						if (alignment.startsWith("+")) {
							changeType = ChangeType.INS;
							return alignment;
						}
					}
				}

				// Not an InDel? Then it's a substitution
				substitution();
			}
		} catch (Throwable t) {
			throw new RuntimeException("Error aligning sequences:\n\tSequence 1: " + new String(a) + "\n\tSequence 2: " + new String(b), t);
		}

		return alignment;
	}

	public ChangeType getChangeType() {
		return changeType;
	}

	public void setChangeType(ChangeType changeType) {
		this.changeType = changeType;
	}

	/**
	 * Simplified alignment
	 * @return
	 */
	boolean simpleAlign() {
		if (stringA.length() < stringB.length()) {
			// A has a deletion respect to B
			int idx = stringB.indexOf(stringA);
			if (idx >= 0) {
				changeType = ChangeType.DEL;
				offset = stringA.length();
				alignment = "-" + stringB.substring(stringA.length(), stringB.length());
				return true;
			}
		}

		if (stringA.length() > stringB.length()) {
			// A has an insertion respect to B
			int idx = stringA.indexOf(stringB);
			if (idx >= 0) {
				changeType = ChangeType.INS;
				offset = stringB.length();
				alignment = "+" + stringA.substring(stringB.length(), stringA.length());
				return true;
			}
		}

		return false;

	}

	/**
	 * If it is not a trivial alignment, then it's a mixed variant (a.k.a subtitution)
	 */
	void substitution() {
		changeType = ChangeType.MIXED;

		// Offset
		// Note: There must be a difference, otherwise this would be an InDel, captured in 'simpleAlign() method
		int min = Math.min(stringA.length(), stringB.length());
		for (int i = 0; i < min; i++)
			if (stringA.charAt(i) == stringB.charAt(i)) offset = i;
			else break;
	}
}
