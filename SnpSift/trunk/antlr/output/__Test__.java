import java.io.*;
import org.antlr.runtime.*;
import org.antlr.runtime.debug.DebugEventSocketProxy;

import ca.mcgill.mcb.pcingola.snpSift.antlr.*;


public class __Test__ {

    public static void main(String args[]) throws Exception {
        VcfFilterLexer lex = new VcfFilterLexer(new ANTLRFileStream("/Users/pablocingolani/Documents/workspace/SnpSift/antlr/output/__Test___input.txt", "UTF8"));
        CommonTokenStream tokens = new CommonTokenStream(lex);

        VcfFilterParser g = new VcfFilterParser(tokens, 49100, null);
        try {
            g.main();
        } catch (RecognitionException e) {
            e.printStackTrace();
        }
    }
}