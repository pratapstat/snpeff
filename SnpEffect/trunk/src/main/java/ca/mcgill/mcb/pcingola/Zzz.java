package ca.mcgill.mcb.pcingola;

import ca.mcgill.mcb.pcingola.fileIterator.SmithWaterman;

public class Zzz {

	public static void main(String[] args) {
		String seq1 = "LPCKRCRPSVYSLSYIKRGKTRNYLYPIWSPYAYYLYCYKYRITLREKMLPRCYKSITYKEEEDLTLQPRSCLQCS*VPSRPPGGQKHRAG*PRPA*RTVFGWELDGAGY*PPAPPGPSTVRLSLPPYLPDLCPLARPSL*PSTLLGYPISGLGSRQPGKFDCNPGVRGREDKCGLCVCELPE*SERQRCPAAGLQLHGL*GGQTRSPCPPSLGQCHLYGVSP*KGCWPPAQ*ASL";
		String seq2 = "MPCKRCRPSVYSLSYIKRGKTRNYLYPIWSPYAYYLYCYKYRITLREKMLPRCYKSITYKEEEDLTLQPRSCLQCSESLVGLQEGKSTEQGNHDQLKELYSAGNLTVLATDPLLHQDPVQLDFHFRLTSQTSAHWHGLLCDRRLFLDIPYQALDQGNRESLTATLEYVEEKTNVDSVFVNFQNDRNDRGALLRAFSYMGFEVVRPDHPALPPLDNVIFMVYPLERDVGHLPSEPP";

		SmithWaterman sw = new SmithWaterman(seq1, seq2);
		sw.align();
		System.out.println(sw);

		//		for (int i = seq1.length() - 1; i > 0; i--) {
		//			String s = seq1.substring(0, i);
		//			System.out.println(i + "\tIndex: " + seq2.indexOf(s));
		//		}
	}

	public Zzz(String genome) {
	}

	public void run() {
	}
}
