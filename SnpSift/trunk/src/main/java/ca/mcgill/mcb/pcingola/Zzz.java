package ca.mcgill.mcb.pcingola;

import ca.mcgill.mcb.pcingola.fileIterator.VcfFileIterator;
import ca.mcgill.mcb.pcingola.util.Gpr;
import ca.mcgill.mcb.pcingola.vcf.VcfEntry;

public class Zzz {

	public static void main(String[] args) {
		int maxLen = 10 * 1024 * 1024;

		String vcfFileName = Gpr.HOME + "/z.vcf";
		String vcfOutFileName = Gpr.HOME + "/z.cut.vcf";
		VcfFileIterator vcf = new VcfFileIterator(vcfFileName);

		StringBuilder sb = new StringBuilder();
		for (VcfEntry ve : vcf) {
			if (vcf.isHeadeSection()) sb.append(vcf.getVcfHeader() + "\n");

			String str = ve.getChromosomeName() //
					+ "\t" + (ve.getStart() + 1) // 
					+ "\t" + ve.getId() // 
					+ "\t" + ve.getRef().substring(0, maxLen) // 
					+ "\t" + ve.getAltsStr() // 
					+ "\t" + ve.getFilterPass() // 
					+ "\t" + ve.getInfoStr() //
			;

			sb.append(str + "\n");
		}

		Gpr.toFile(vcfOutFileName, sb);
		Gpr.debug("Done!");
	}
}
