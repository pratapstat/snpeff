package ca.mcgill.mcb.pcingola;

import ca.mcgill.mcb.pcingola.probablility.FisherExactTest;

public class Zzz {

	public static void main(String[] args) {
		int k = 0;
		int N = 12791;
		int D = 6385;
		int n = 0;
		double pup = FisherExactTest.get().fisherExactTestUp(k, N, D, n);
		double pdown = FisherExactTest.get().fisherExactTestDown(k, N, D, n);

		System.out.println("Up: " + pup);
		System.out.println("Down: " + pdown);
	}

}
