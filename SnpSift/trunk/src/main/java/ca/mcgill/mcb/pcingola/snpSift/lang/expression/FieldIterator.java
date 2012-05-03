package ca.mcgill.mcb.pcingola.snpSift.lang.expression;

/**
 * Iterates on fields / sub-fields
 * It's a singleton
 * 
 * @author pcingola
 */
public class FieldIterator {

	public enum IteratorType {
		VAR, EFFECT, GENOTYPE, GENOTYPE_VAR
	}

	private static final FieldIterator fieldIterator = new FieldIterator();

	SimpleIterator var = new SimpleIterator();
	SimpleIterator gentype = new SimpleIterator();
	SimpleIterator effect = new SimpleIterator();
	SimpleIterator gentypeVar = new SimpleIterator();

	public static FieldIterator get() {
		return fieldIterator;
	}

	/**
	 * Set 'max' parameter for an iterator
	 * @param starType
	 * @param max
	 */
	public int get(IteratorType iterType) {
		switch(iterType) {
			case VAR:
				return var.current;

			case GENOTYPE:
				return gentype.current;

			case GENOTYPE_VAR:
				return gentypeVar.current;

			case EFFECT:
				return effect.current;

			default:
				throw new RuntimeException("Unknown iterator type '" + iterType + "'");
		}
	}

	/**
	 * Is there a 'next'
	 * @return
	 */
	public boolean hasNext() {
		return var.hasNext() || effect.hasNext() || gentype.hasNext() || gentypeVar.hasNext();
	}

	/**
	 * Next in iteration
	 */
	public void next() {
		if( gentypeVar.hasNext() ) {
			gentypeVar.next();
			return;
		}

		if( gentype.hasNext() ) {
			gentypeVar.reset();
			gentype.next();
			return;
		}

		if( effect.hasNext() ) {
			gentypeVar.reset();
			gentype.reset();
			effect.next();
			return;
		}

		if( var.hasNext() ) {
			gentypeVar.reset();
			gentype.reset();
			effect.reset();
			var.next();
			return;
		}

		throw new RuntimeException("Cannot go beyond this count: " + this);
	}

	/**
	 * Reset all counters
	 */
	public void reset() {
		gentypeVar.reset();
		gentype.reset();
		effect.reset();
		var.reset();
	}

	/**
	 * Set 'max' parameter for an iterator
	 * @param starType
	 * @param max
	 */
	public void setMax(IteratorType iterType, int max) {
		switch(iterType) {
			case VAR:
				var.max = Math.max(max, var.max);
				break;

			case GENOTYPE:
				gentype.max = Math.max(max, gentype.max);
				break;

			case GENOTYPE_VAR:
				gentypeVar.max = Math.max(max, gentypeVar.max);
				break;

			case EFFECT:
				effect.max = Math.max(max, effect.max);
				break;

			default:
				throw new RuntimeException("Unknown iterator type '" + iterType + "'");
		}
	}

	@Override
	public String toString() {
		return "[ " + var.current + " | " + effect.current + " | " + gentype.current + " | " + gentypeVar.current + " ]";
	}
}

/**
 * Iterate on one variable
 * @author pcingola
 *
 */
class SimpleIterator {

	int min = 0, max = 0, current = 0;

	boolean hasNext() {
		return current < max;
	}

	void next() {
		current++;
	}

	void reset() {
		current = min;
		max = 0;
	}
}
