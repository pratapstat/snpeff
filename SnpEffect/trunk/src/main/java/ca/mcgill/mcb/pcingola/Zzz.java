package ca.mcgill.mcb.pcingola;

import ca.mcgill.mcb.pcingola.snpEffect.Config;

public class Zzz {

	public static void main(String[] args) {
		Config config = new Config("hg19");
		System.out.println("Config file length: " + config.toString().length());
	}

}
