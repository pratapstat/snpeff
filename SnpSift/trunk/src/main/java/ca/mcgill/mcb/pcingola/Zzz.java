package ca.mcgill.mcb.pcingola;

import java.util.List;

import junit.framework.Assert;
import ca.mcgill.mcb.pcingola.snpSift.SnpSiftCmdFilter;
import ca.mcgill.mcb.pcingola.vcf.VcfEntry;

public class Zzz {

	static boolean debug = true;

	public static void main(String[] args) throws Exception {
		// Filter data
		SnpSiftCmdFilter vcfFilter = new SnpSiftCmdFilter();
		String expression = "EFF[0].EFFECT = 'SYNONYMOUS_CODING'";
		List<VcfEntry> list = vcfFilter.filter("test/test03.vcf", expression, true);

		// Check that it satisfies the condition
		System.out.println("Expression: '" + expression + "'");
		for (VcfEntry vcfEntry : list) {
			System.out.println("\t" + vcfEntry);

			String eff = vcfEntry.getInfo("EFF").split("\\(")[0];
			Assert.assertEquals(eff, "SYNONYMOUS_CODING");
		}
	}

}
