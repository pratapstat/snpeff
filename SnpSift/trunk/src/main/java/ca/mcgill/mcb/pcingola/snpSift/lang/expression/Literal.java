package ca.mcgill.mcb.pcingola.snpSift.lang.expression;

import ca.mcgill.mcb.pcingola.vcf.VcfEntry;
import ca.mcgill.mcb.pcingola.vcf.VcfInfoType;

public class Literal extends Expression {

	String str;
	double d = Double.NaN;
	long l = 0;

	public Literal(String str) {
		this.str = str;
		returnType = VcfInfoType.STRING;
	}

	public Literal(String str, boolean asNumber) {
		this.str = str;
		try {
			returnType = VcfInfoType.INTEGER;
			l = Long.parseLong(str);
			d = l;
		} catch (Exception e) {
			returnType = VcfInfoType.FLOAT;
			d = Double.parseDouble(str);
			l = (long) d;
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Comparable get(VcfEntry vcfEntry) {
		switch (returnType) {
		case INTEGER:
			return l;
		case FLOAT:
			return d;
		case STRING:
			return str;
		default:
			throw new RuntimeException("Unknwon return type '" + returnType + "'");
		}
	}

	@Override
	public String toString() {
		switch (returnType) {
		case STRING:
			return "'" + str + "'";
		case FLOAT:
			return "" + d;
		case INTEGER:
			return "" + l;
		default:
			throw new RuntimeException("Unknown type '" + returnType + "'");
		}
	}
}
