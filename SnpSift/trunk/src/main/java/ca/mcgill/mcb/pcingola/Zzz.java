package ca.mcgill.mcb.pcingola;

import java.io.File;
import java.io.IOException;

import ca.mcgill.mcb.pcingola.util.Gpr;
import ca.mcgill.mcb.pcingola.util.Timer;

public class Zzz {

	public static String findPhastConsFile(String dirName, String regex) {
		try {
			File dir = new File(dirName);
			for (File f : dir.listFiles()) {
				String fname = f.getCanonicalPath();
				System.out.println(fname);
				if (fname.matches(regex)) {
					System.out.println("FOUND: " + fname);
					return fname;
				}
			}
		} catch (IOException e) {
			// Do nothing
		}

		Timer.showStdErr("Cannot find any file in directory '" + dirName + "' matching regular expression '" + regex + "'");
		return null;
	}

	public static void main(String[] args) {

		String phastConsDir = Gpr.HOME + "/snpEff/db/phastCons";
		String chromo = "2";

		// Find a file that matches a phastCons name
		String wigFile = findPhastConsFile(phastConsDir, ".*/chr" + chromo + ".*wigFix.*");

		if ((wigFile == null) || !Gpr.exists(wigFile)) {
			if (wigFile != null) Timer.showStdErr("Cannot open PhastCons file '" + wigFile + "' for chromosome '" + chromo + "'");
		}

	}
}
