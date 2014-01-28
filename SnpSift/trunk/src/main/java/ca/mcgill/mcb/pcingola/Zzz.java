package ca.mcgill.mcb.pcingola;

import java.io.IOException;
import java.util.Iterator;

import ca.mcgill.mcb.pcingola.fileIterator.DbNsfpEntry;
import ca.mcgill.mcb.pcingola.fileIterator.DbNsfpFileIterator;
import ca.mcgill.mcb.pcingola.util.Gpr;

public class Zzz implements Iterable<String> {

	public static void main(String[] args) throws IOException {
		String txtFileName = Gpr.HOME + "/snpEff/db/dbNSFP/z.txt.gz";

		//		TabixReader tabix = new TabixReader(txtFileName);
		//		tabix.query("1:9009444");
		//		for (String line : tabix)
		//			System.out.println(line);

		DbNsfpFileIterator db = new DbNsfpFileIterator(txtFileName);
		for (DbNsfpEntry dbe : db) {
			System.out.println(dbe);
		}

		Gpr.debug("Done!");
	}

	@Override
	public Iterator<String> iterator() {
		// TODO Auto-generated method stub
		return null;
	}
}
