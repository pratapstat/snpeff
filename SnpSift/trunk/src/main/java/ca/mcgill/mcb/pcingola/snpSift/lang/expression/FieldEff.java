package ca.mcgill.mcb.pcingola.snpSift.lang.expression;

import ca.mcgill.mcb.pcingola.snpSift.lang.expression.FieldIterator.IteratorType;
import ca.mcgill.mcb.pcingola.vcf.VcfEffect;
import ca.mcgill.mcb.pcingola.vcf.VcfEntry;

/**
 * A field form SnpEff:
 * 
 * E.g.:  'EFF[2].GENE'
 * 
 * @author pablocingolani
 */
public class FieldEff extends FieldSub {

	int fieldNum = -1;
	VcfEffect.FormatVersion formatVersion;

	public FieldEff(String name, int index, VcfEffect.FormatVersion formatVersion) {
		super("EFF." + name, index); // Add an 'EFF.' at the beginning
		this.formatVersion = formatVersion;
		fieldNum = fieldNum(this.name);
		if (fieldNum < 0) throw new RuntimeException("No such EFF subfield '" + name + "'");
	}

	/**
	 * Get field number by name
	 * @param name
	 * @return
	 */
	int fieldNum(String name) {
		return VcfEffect.fieldNum(name, formatVersion);
	}

	/**
	 * Get a field from VcfEntry
	 * @param vcfEntry
	 * @param field
	 * @return
	 */
	@Override
	public String getFieldString(VcfEntry vcfEntry) {
		// Genotype field => Look for genotype and then field
		String effStr = vcfEntry.getInfo("EFF");
		if (effStr == null) return null;

		// Find field
		String effects[] = effStr.split(",");
		if (index >= effects.length) return null;

		// Is this field 'iterable'?
		int idx = index;
		if (index < 0) {
			FieldIterator.get().setMax(IteratorType.EFFECT, effects.length - 1);
			FieldIterator.get().setType(index);
			idx = FieldIterator.get().get(IteratorType.EFFECT);
		}

		// Find sub-field
		String eff = effects[idx];
		eff = eff.replace('(', ' '); // Replace all chars by spaces
		eff = eff.replace('|', ' ');
		eff = eff.replace(')', ' ');
		String subField[] = eff.split("\\s");

		if (fieldNum >= subField.length) return null;
		return subField[fieldNum];
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "EFF[" + indexStr(index) + "]." + name;
	}
}
