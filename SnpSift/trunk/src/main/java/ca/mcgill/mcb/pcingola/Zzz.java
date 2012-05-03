package ca.mcgill.mcb.pcingola;

import ca.mcgill.mcb.pcingola.fileIterator.microCosm.MicroCosmEntry;
import ca.mcgill.mcb.pcingola.fileIterator.microCosm.MicroCosmFileIterator;
import ca.mcgill.mcb.pcingola.interval.Genome;

public class Zzz {

	public static void main(String[] args) {
		MicroCosmFileIterator mfi = new MicroCosmFileIterator("/Users/pcingola/snpEff/miRna/microCosm.v5.homo_sapiens.txt");

		Genome genome = new Genome("test");
		for (MicroCosmEntry me : mfi) {
			System.out.println(me.getMarker(genome));
		}
	}
}
