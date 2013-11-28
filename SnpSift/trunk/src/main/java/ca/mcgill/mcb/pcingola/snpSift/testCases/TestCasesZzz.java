package ca.mcgill.mcb.pcingola.snpSift.testCases;

import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;
import ca.mcgill.mcb.pcingola.snpSift.SnpSiftCmdFilter;
import ca.mcgill.mcb.pcingola.vcf.VcfEntry;

/**
 * Try test cases in this class before adding them to long test cases
 * 
 * @author pcingola
 */
public class TestCasesZzz extends TestCase {

	public static boolean verbose = false;

	/**
	 * Remove filter option '-rmFilter'
	 * Bug reported by Jim Johnson
	 */
	public void test_41() {
		verbose = true;

		// Filter data
		String expression = "( DP < 5 )";
		String vcfFile = "test/test_rmfilter.vcf";
		String args[] = { "-f", vcfFile, "--rmFilter", "DP_OK", expression }; // Remove 'PASS' if there is not enough depth  
		SnpSiftCmdFilter vcfFilter = new SnpSiftCmdFilter(args);
		List<VcfEntry> list = vcfFilter.filter(vcfFile, expression, true);

		// Check that it satisfies the condition
		System.out.println("Expression: '" + expression + "'");
		Assert.assertNotNull(list);
		Assert.assertTrue(list.size() == 2);

		// Check result (hould be only one entry)
		VcfEntry vcfEntry = list.get(0);
		if (verbose) System.out.println(vcfEntry.getFilterPass() + "\t" + vcfEntry);

		if (vcfEntry.getStart() == 219134261) Assert.assertEquals("OTHER", vcfEntry.getFilterPass());
		if (vcfEntry.getStart() == 219134272) Assert.assertEquals("DP_OK;OTHER", vcfEntry.getFilterPass());
	}

}
