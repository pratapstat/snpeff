package ca.mcgill.mcb.pcingola.snpSift.testCases;

import java.io.IOException;

import junit.framework.Assert;
import junit.framework.TestCase;
import ca.mcgill.mcb.pcingola.fileIterator.VcfFileIterator;
import ca.mcgill.mcb.pcingola.snpSift.SnpSiftCmdAnnotateSorted;
import ca.mcgill.mcb.pcingola.vcf.VcfEntry;

/**
 * Annotate test case
 * 
 * @author pcingola
 */
public class TestCasesAnnotate extends TestCase {

	public static boolean verbose = false;

	/**
	 * Re-annotate a file and check that the new annotation matches the previous one
	 */
	public void annotateTest(String dbFileName, String fileName) {
		System.out.println("Annotate: " + dbFileName + "\t" + fileName);
		// Create command line
		String args[] = { "-q", dbFileName, fileName };

		// Iterate over VCF entries
		try {
			SnpSiftCmdAnnotateSorted vcfAnnotate = new SnpSiftCmdAnnotateSorted(args);
			vcfAnnotate.setSuppressOutput(true);
			vcfAnnotate.initAnnotate();

			VcfFileIterator vcfFile = new VcfFileIterator(fileName);
			for( VcfEntry vcf : vcfFile ) {
				vcfAnnotate.annotate(vcf);

				// We expect the same annotation twice 
				String idstr = vcf.getId();
				if( idstr.length() > 1 ) {
					String id[] = idstr.split(";");
					Assert.assertEquals(2, id.length); // Two annotations are expected  
					Assert.assertEquals(id[0], id[1]); // And both of them should be equal  
				}
			}

			vcfAnnotate.endAnnotate();
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void test_01() {
		String dbFileName = "./test/db_test_1.vcf";
		String fileName = "./test/annotate_1.vcf";
		annotateTest(dbFileName, fileName);
	}

	public void test_02() {
		String dbFileName = "./test/db_test_10.vcf";
		String fileName = "./test/annotate_10.vcf";
		annotateTest(dbFileName, fileName);
	}

	public void test_03() {
		String dbFileName = "./test/db_test_2.vcf";
		String fileName = "./test/annotate_2.vcf";
		annotateTest(dbFileName, fileName);
	}

	public void test_04() {
		String dbFileName = "./test/db_test_large.vcf";
		String fileName = "./test/annotate_large.vcf";
		annotateTest(dbFileName, fileName);
	}

}
