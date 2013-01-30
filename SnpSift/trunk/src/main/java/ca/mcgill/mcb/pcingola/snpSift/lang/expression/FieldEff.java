package ca.mcgill.mcb.pcingola.snpSift.lang.expression;

import java.util.List;

import ca.mcgill.mcb.pcingola.snpSift.lang.expression.FieldIterator.IteratorType;
import ca.mcgill.mcb.pcingola.vcf.VcfEffect;
import ca.mcgill.mcb.pcingola.vcf.VcfEffect.FormatVersion;
import ca.mcgill.mcb.pcingola.vcf.VcfEntry;

/**
 * An 'EFF' field form SnpEff:
 * 
 * E.g.:  'EFF[2].GENE'
 * 
 * @author pablocingolani
 */
public class FieldEff extends FieldSub {

	int fieldNum = -1;
	FormatVersion formatVersion = null;

	public FieldEff(String name, int index) {
		super("EFF." + name, index); // Add an 'EFF.' at the beginning
	}

	/**
	 * Get field number by name
	 * @param name
	 * @return
	 */
	int fieldNum(String name, VcfEntry vcfEntry) {
		FormatVersion formatVersion = vcfEntry.effectFormatVersion();
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
		// Get all effects
		List<VcfEffect> effects = vcfEntry.parseEffects(formatVersion);
		if (effects.size() <= 0) return null;

		// Find field
		if (index >= effects.size()) return null;

		// Is this field 'iterable'?
		int idx = index;
		if (index < 0) {
			FieldIterator.get().setMax(IteratorType.EFFECT, effects.size() - 1);
			FieldIterator.get().setType(index);
			idx = FieldIterator.get().get(IteratorType.EFFECT);
		}

		// Find sub-field
		String eff = effects.get(idx);
		eff = eff.replace('(', ' '); // Replace all chars by spaces
		eff = eff.replace('|', ' ');
		eff = eff.replace(')', ' ');
		String subField[] = eff.split("\\s");

		// Field number not set? Try to guess it
		if (fieldNum < 0) {
			fieldNum = fieldNum(this.name, vcfEntry);
			if (fieldNum < 0) throw new RuntimeException("No such EFF subfield '" + name + "'");
		}

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
