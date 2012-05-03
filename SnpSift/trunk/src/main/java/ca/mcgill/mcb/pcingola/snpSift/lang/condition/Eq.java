package ca.mcgill.mcb.pcingola.snpSift.lang.condition;

import ca.mcgill.mcb.pcingola.snpSift.lang.expression.Expression;
import ca.mcgill.mcb.pcingola.vcf.VcfEntry;

/**
 * Equal 
 * 
 * @author pcingola
 */
public class Eq extends OpBinary {

	public Eq(Expression left, Expression right) {
		super(left, right, "=");
	}

	@Override
	public boolean eval(VcfEntry vcfEntry) {
		boolean retVal = (left.compareTo(right, vcfEntry) == 0);
		return negated ? !retVal : retVal;
	}

}
