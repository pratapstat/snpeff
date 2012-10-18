package ca.mcgill.mcb.pcingola;

import ca.mcgill.mcb.pcingola.snpSift.lang.LangFactory;
import ca.mcgill.mcb.pcingola.snpSift.lang.expression.Field;
import ca.mcgill.mcb.pcingola.util.Gpr;

public class Zzz {

	static boolean debug = true;

	public static void main(String[] args) throws Exception {
		String fieldStr = "GEN[0].GT";

		LangFactory lf = new LangFactory();
		Field field = lf.parseField(fieldStr);
		Gpr.debug("Field: " + field);
	}

}
