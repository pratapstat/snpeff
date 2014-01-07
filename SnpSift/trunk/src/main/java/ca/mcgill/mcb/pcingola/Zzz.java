package ca.mcgill.mcb.pcingola;

import ca.mcgill.mcb.pcingola.fileIterator.VcfFileIterator;
import ca.mcgill.mcb.pcingola.interval.SeqChange;
import ca.mcgill.mcb.pcingola.vcf.VcfEntry;

public class Zzz {

	public static void main(String[] args) {
		String vcfFileName = "/home/pcingola/workspace/SnpEff/tests/test.no_change.vcf"; // Gpr.HOME + "/snpEff/z.vcf";
		VcfFileIterator vcf = new VcfFileIterator(vcfFileName);

		for (VcfEntry ve : vcf) {
			System.out.println(ve.getStart() + "\t" + ve);
			for (SeqChange sc : ve.seqChanges()) {
				System.out.println("\t" + sc);
			}
		}
	}
}
