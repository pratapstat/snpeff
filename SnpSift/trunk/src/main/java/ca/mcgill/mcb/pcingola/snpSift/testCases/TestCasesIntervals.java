package ca.mcgill.mcb.pcingola.snpSift.testCases;

import java.util.List;

import junit.framework.TestCase;
import ca.mcgill.mcb.pcingola.snpSift.SnpSiftCmdIntervals;
import ca.mcgill.mcb.pcingola.vcf.VcfEntry;

/**
 * SnpSIft intervals
 * 
 * @author pcingola
 */
public class TestCasesIntervals extends TestCase {

	/**
	 * Filter VCF entries 
	 */
	public void test_01() {
		// Run command
		String args[] = { "-v" // Verbose
				, "-i", "test/annotate_5.vcf" // This file has a few VCF entries before 1,000,000 and a few after
				, "test/interval.bed" // BED file intervals cover chr1:1-1,000,000 
		};
		SnpSiftCmdIntervals cmd = new SnpSiftCmdIntervals(args);
		List<VcfEntry> results = cmd.run(true);

		// Check results
		for (VcfEntry ve : results)
			if (ve.getStart() > 1000000) throw new RuntimeException("This entry should be filtered out!\n\t" + ve);

	}

	/**
	 * Filter VCF entries 
	 */
	public void test_02() {
		// Run command
		String args[] = { "-v" // Verbose
				, "-x" // Exclude entries in the BED file intervals
				, "-i", "test/annotate_5.vcf" // This file has a few VCF entries before 1,000,000 and a few after
				, "test/interval.bed" // BED file intervals cover chr1:1-1,000,000 
		};
		SnpSiftCmdIntervals cmd = new SnpSiftCmdIntervals(args);
		List<VcfEntry> results = cmd.run(true);

		// Check results
		for (VcfEntry ve : results)
			if (ve.getStart() < 1000000) throw new RuntimeException("This entry should be filtered out!\n\t" + ve);

	}
}
