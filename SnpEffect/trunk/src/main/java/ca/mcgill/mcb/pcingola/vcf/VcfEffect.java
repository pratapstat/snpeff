package ca.mcgill.mcb.pcingola.vcf;

import ca.mcgill.mcb.pcingola.snpEffect.ChangeEffect;
import ca.mcgill.mcb.pcingola.util.Gpr;

/**
 * An 'EFF' entry in a vcf line
 * 
 * @author pablocingolani
 */
public class VcfEffect {

	/**
	 * VcfFields in SnpEff version 2.X have a different format than 3.X 
	 */
	public enum FormatVersion {
		FORMAT_SNPEFF_2, FORMAT_SNPEFF_3
	};

	String effectString;
	String effectStrings[];
	FormatVersion formatVersion;
	ChangeEffect.EffectType effect;
	ChangeEffect.EffectImpact impact;
	ChangeEffect.FunctionalClass funClass;
	String codon;
	String aa;
	int aaLen;
	String gene;
	String bioType;
	ChangeEffect.Coding coding;
	String transcriptId;
	String exonId;

	/**
	 * Convert from field name to field number
	 * @param name
	 * @param formatVersion
	 * @return
	 */
	public static int fieldNum(String name, FormatVersion formatVersion) {
		int fieldNum = 0;

		if (name.equals("EFF.EFFECT")) return fieldNum;
		fieldNum++;

		if (name.equals("EFF.IMPACT")) return fieldNum;
		fieldNum++;

		if (name.equals("EFF.FUNCLASS")) return fieldNum;
		fieldNum++;

		if (name.equals("EFF.CODON")) return fieldNum;
		fieldNum++;

		if (name.equals("EFF.AA")) return fieldNum;
		fieldNum++;

		if (formatVersion != FormatVersion.FORMAT_SNPEFF_2) {
			if (name.equals("EFF.AA_LEN")) return fieldNum;
			fieldNum++;
		}

		if (name.equals("EFF.GENE")) return fieldNum;
		fieldNum++;

		if (name.equals("EFF.BIOTYPE")) return fieldNum;
		fieldNum++;

		if (name.equals("EFF.CODING")) return fieldNum;
		fieldNum++;

		if (name.equals("EFF.TRID")) return fieldNum;
		fieldNum++;

		if (name.equals("EFF.EXID")) return fieldNum;
		fieldNum++;

		return -1;
	}

	/**
	 * Split a 'effect' string to an array of strings
	 * @param eff
	 * @return
	 */
	public static String[] split(String eff) {
		eff = eff.replace('(', '\t'); // Replace all chars by spaces
		eff = eff.replace('|', '\t');
		eff = eff.replace(')', '\t');
		String effs[] = eff.split("\t", -1); // Negative number means "use trailing empty as well"
		return effs;
	}

	/**
	 * Constructor: Guess format version
	 * @param effStr
	 * @param formatVersion
	 */
	public VcfEffect(String effectString) {
		formatVersion = null; // Force guess
		this.effectString = effectString;
		parse();
	}

	/**
	 * Constructor: Force format version
	 * @param effStr
	 * @param formatVersion : If null, will try to guess it
	 */
	public VcfEffect(String effectString, FormatVersion formatVersion) {
		this.formatVersion = formatVersion;
		this.effectString = effectString;
		parse();
	}

	/**
	 * Guess effect format version
	 * @return
	 */
	public FormatVersion formatVersion() {
		// Already set?
		if (formatVersion != null) return formatVersion;

		// OK, guess format version
		if (effectStrings == null) effectStrings = split(effectString);
		Gpr.debug("effectStrings.len: " + effectStrings.length);

		if (effectStrings.length <= 12) return FormatVersion.FORMAT_SNPEFF_2;
		return FormatVersion.FORMAT_SNPEFF_3;
	}

	/**
	 * Get a subfield as an index
	 * @param index
	 * @return
	 */
	public String get(int index) {
		if (index >= effectStrings.length) return null;
		return effectStrings[index];
	}

	public String getAa() {
		return aa;
	}

	public int getAaLen() {
		return aaLen;
	}

	public String getBioType() {
		return bioType;
	}

	public ChangeEffect.Coding getCoding() {
		return coding;
	}

	public String getCodon() {
		return codon;
	}

	public ChangeEffect.EffectType getEffect() {
		return effect;
	}

	public String getExonId() {
		return exonId;
	}

	public ChangeEffect.FunctionalClass getFunClass() {
		return funClass;
	}

	public String getGene() {
		return gene;
	}

	public ChangeEffect.EffectImpact getImpact() {
		return impact;
	}

	public String getTranscriptId() {
		return transcriptId;
	}

	void parse() {
		effectStrings = split(effectString);

		// Guess format, if not given
		if (formatVersion == null) formatVersion = formatVersion();

		try {
			// Parse each sub field
			int index = 0;
			if (effectStrings[index].startsWith("REGULATION")) effect = ChangeEffect.EffectType.REGULATION;
			else effect = ChangeEffect.EffectType.valueOf(effectStrings[index]);
			index++;

			if ((effectStrings.length > index) && !effectStrings[index].isEmpty()) impact = ChangeEffect.EffectImpact.valueOf(effectStrings[index]);
			index++;

			if ((effectStrings.length > index) && !effectStrings[index].isEmpty()) funClass = ChangeEffect.FunctionalClass.valueOf(effectStrings[index]);
			index++;

			if ((effectStrings.length > index) && !effectStrings[index].isEmpty()) codon = effectStrings[index];
			index++;

			if ((effectStrings.length > index) && !effectStrings[index].isEmpty()) aa = effectStrings[index];
			index++;

			if (formatVersion != FormatVersion.FORMAT_SNPEFF_2) {
				if ((effectStrings.length > index) && !effectStrings[index].isEmpty()) aaLen = Gpr.parseIntSafe(effectStrings[index]);
				else aaLen = 0;
				index++;
			}

			if ((effectStrings.length > index) && !effectStrings[index].isEmpty()) gene = effectStrings[index];
			index++;

			if ((effectStrings.length > index) && !effectStrings[index].isEmpty()) bioType = effectStrings[index];
			index++;

			if ((effectStrings.length > index) && !effectStrings[index].isEmpty()) coding = ChangeEffect.Coding.valueOf(effectStrings[index]);
			index++;

			if ((effectStrings.length > index) && !effectStrings[index].isEmpty()) transcriptId = effectStrings[index];
			index++;

			if ((effectStrings.length > index) && !effectStrings[index].isEmpty()) exonId = effectStrings[index];
			index++;
		} catch (Exception e) {
			String fields = "";
			for (int i = 0; i < effectStrings.length; i++)
				fields += "\t" + i + " : '" + effectStrings[i] + "'\n";
			throw new RuntimeException("Error parsing: '" + effectString + "' (formatVersion='" + formatVersion + "')\n" + fields, e);
		}
	}

	public void setAa(String aa) {
		this.aa = aa;
	}

	public void setAaLen(int aaLen) {
		this.aaLen = aaLen;
	}

	public void setBioType(String bioType) {
		this.bioType = bioType;
	}

	public void setCoding(ChangeEffect.Coding coding) {
		this.coding = coding;
	}

	public void setCodon(String codon) {
		this.codon = codon;
	}

	public void setEffect(ChangeEffect.EffectType effect) {
		this.effect = effect;
	}

	public void setExonId(String exonId) {
		this.exonId = exonId;
	}

	public void setFunClass(ChangeEffect.FunctionalClass funClass) {
		this.funClass = funClass;
	}

	public void setGene(String gene) {
		this.gene = gene;
	}

	public void setImpact(ChangeEffect.EffectImpact impact) {
		this.impact = impact;
	}

	public void setTranscriptId(String transcriptId) {
		this.transcriptId = transcriptId;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(effect);
		sb.append("(");

		if (impact != null) sb.append(impact);
		sb.append("|");

		if (funClass != null) sb.append(funClass);
		sb.append("|");

		sb.append(codon);
		sb.append("|");

		sb.append(aa);
		sb.append("|");

		if (aaLen > 0) sb.append(aaLen);
		sb.append("|");

		sb.append(gene);
		sb.append("|");

		sb.append(bioType);
		sb.append("|");

		if (coding != null) sb.append(coding);
		sb.append("|");

		sb.append(transcriptId);
		sb.append("|");

		sb.append(exonId);

		sb.append(")");

		return sb.toString();
	}
}
