package ca.mcgill.mcb.pcingola.snpEffect.testCases;

import junit.framework.TestCase;

/**
 * Test case where VCF entries are huge (e.g. half chromosome deleted)
 * 
 * @author pcingola
 */
public class TestCasesHugeVariants extends TestCase {

	public TestCasesHugeVariants() {
		super();
	}

	public void test_01() {
		throw new RuntimeException("Unimplemented! We cannot deal with this huge variants!!!");

		//		String args[] = { "testHg3766Chr1", "./tests/huge_deletion.vcf.gz" };
		//
		//		SnpEffCmdEff snpEffCmdEff = new SnpEffCmdEff();
		//		snpEffCmdEff.parseArgs(args);
		//		snpEffCmdEff.setVerbose(true);
		//		List<VcfEntry> vcfEntries = snpEffCmdEff.run(true);
		//
		//		for (VcfEntry ve : vcfEntries) {
		//			System.out.println(ve);
		//			for (VcfEffect veff : ve.parseEffects()) {
		//				EffectImpact imp = veff.getImpact();
		//				System.out.println(ve.getChromosomeName() + ":" + ve.getStart() + "-" + ve.getEnd() + "\t" + imp);
		//				Assert.assertEquals(true, hasWarning);
		//			}
		//		}
		//
	}
}
