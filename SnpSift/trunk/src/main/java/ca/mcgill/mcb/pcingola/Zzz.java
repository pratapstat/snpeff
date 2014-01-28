package ca.mcgill.mcb.pcingola;

import java.io.IOException;

import ca.mcgill.mcb.pcingola.fileIterator.VcfRefAltAlign;

public class Zzz {

	public static void main(String[] args) throws IOException {
		VcfRefAltAlign va = new VcfRefAltAlign("TCG", "TG");
		va.align();
		System.out.println("Offset:" + va.getOffset());
		System.out.println("Type:" + va.getChangeType());
		System.out.println("Align:" + va.getAlignment());
	}
}
