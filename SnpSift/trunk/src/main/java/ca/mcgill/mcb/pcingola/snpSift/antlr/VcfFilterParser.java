// $ANTLR 3.4 /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g 2011-11-16 20:24:40

package ca.mcgill.mcb.pcingola.snpSift.antlr;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

import org.antlr.runtime.tree.*;


@SuppressWarnings({"all", "warnings", "unchecked"})
public class VcfFilterParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "ALPHANUM", "COMMENT_SL", "CONDITION", "DIGIT", "FLOAT", "FUNCTION_BOOL_GENOTYPE", "FUNCTION_BOOL_SET", "FUNCTION_ENTRY", "ID", "LETTER", "LITERAL_NUMBER", "LITERAL_STRING", "LOWER", "NEWLINE", "NUMBER", "OP_BINARY", "OP_UNARY", "STRING", "UPPER", "VAR_EFF_SUB", "VAR_FIELD", "VAR_GENOTYPE", "VAR_GENOTYPE_SUB", "VAR_GENOTYPE_SUB_ARRAY", "VAR_SUBFIELD", "WS", "'!'", "'!='", "'!~'", "'&'", "'('", "')'", "'*'", "'.'", "'<'", "'<='", "'='", "'=~'", "'>'", "'>='", "'EFF'", "'GEN'", "'SET'", "'['", "']'", "'countHet'", "'countHom'", "'countRef'", "'countVariant'", "'exists'", "'in'", "'isHet'", "'isHom'", "'isRef'", "'isVariant'", "'na'", "'|'"
    };

    public static final int EOF=-1;
    public static final int T__30=30;
    public static final int T__31=31;
    public static final int T__32=32;
    public static final int T__33=33;
    public static final int T__34=34;
    public static final int T__35=35;
    public static final int T__36=36;
    public static final int T__37=37;
    public static final int T__38=38;
    public static final int T__39=39;
    public static final int T__40=40;
    public static final int T__41=41;
    public static final int T__42=42;
    public static final int T__43=43;
    public static final int T__44=44;
    public static final int T__45=45;
    public static final int T__46=46;
    public static final int T__47=47;
    public static final int T__48=48;
    public static final int T__49=49;
    public static final int T__50=50;
    public static final int T__51=51;
    public static final int T__52=52;
    public static final int T__53=53;
    public static final int T__54=54;
    public static final int T__55=55;
    public static final int T__56=56;
    public static final int T__57=57;
    public static final int T__58=58;
    public static final int T__59=59;
    public static final int T__60=60;
    public static final int ALPHANUM=4;
    public static final int COMMENT_SL=5;
    public static final int CONDITION=6;
    public static final int DIGIT=7;
    public static final int FLOAT=8;
    public static final int FUNCTION_BOOL_GENOTYPE=9;
    public static final int FUNCTION_BOOL_SET=10;
    public static final int FUNCTION_ENTRY=11;
    public static final int ID=12;
    public static final int LETTER=13;
    public static final int LITERAL_NUMBER=14;
    public static final int LITERAL_STRING=15;
    public static final int LOWER=16;
    public static final int NEWLINE=17;
    public static final int NUMBER=18;
    public static final int OP_BINARY=19;
    public static final int OP_UNARY=20;
    public static final int STRING=21;
    public static final int UPPER=22;
    public static final int VAR_EFF_SUB=23;
    public static final int VAR_FIELD=24;
    public static final int VAR_GENOTYPE=25;
    public static final int VAR_GENOTYPE_SUB=26;
    public static final int VAR_GENOTYPE_SUB_ARRAY=27;
    public static final int VAR_SUBFIELD=28;
    public static final int WS=29;

    // delegates
    public Parser[] getDelegates() {
        return new Parser[] {};
    }

    // delegators


    public VcfFilterParser(TokenStream input) {
        this(input, new RecognizerSharedState());
    }
    public VcfFilterParser(TokenStream input, RecognizerSharedState state) {
        super(input, state);
    }

protected TreeAdaptor adaptor = new CommonTreeAdaptor();

public void setTreeAdaptor(TreeAdaptor adaptor) {
    this.adaptor = adaptor;
}
public TreeAdaptor getTreeAdaptor() {
    return adaptor;
}
    public String[] getTokenNames() { return VcfFilterParser.tokenNames; }
    public String getGrammarFileName() { return "/home/pcingola/workspace/SnpSift/antlr/VcfFilter.g"; }


    public static class main_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "main"
    // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:85:1: main : f= condition -> ^( CONDITION $f) ;
    public final VcfFilterParser.main_return main() throws RecognitionException {
        VcfFilterParser.main_return retval = new VcfFilterParser.main_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        VcfFilterParser.condition_return f =null;


        RewriteRuleSubtreeStream stream_condition=new RewriteRuleSubtreeStream(adaptor,"rule condition");
        try {
            // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:85:7: (f= condition -> ^( CONDITION $f) )
            // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:85:9: f= condition
            {
            pushFollow(FOLLOW_condition_in_main369);
            f=condition();

            state._fsp--;

            stream_condition.add(f.getTree());

            // AST REWRITE
            // elements: f
            // token labels: 
            // rule labels: f, retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_f=new RewriteRuleSubtreeStream(adaptor,"rule f",f!=null?f.tree:null);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 85:21: -> ^( CONDITION $f)
            {
                // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:85:24: ^( CONDITION $f)
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(
                (Object)adaptor.create(CONDITION, "CONDITION")
                , root_1);

                adaptor.addChild(root_1, stream_f.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;

            }

            retval.stop = input.LT(-1);


            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "main"


    public static class condition_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "condition"
    // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:87:1: condition : subcondition ( boolOperator ^ subcondition )* ;
    public final VcfFilterParser.condition_return condition() throws RecognitionException {
        VcfFilterParser.condition_return retval = new VcfFilterParser.condition_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        VcfFilterParser.subcondition_return subcondition1 =null;

        VcfFilterParser.boolOperator_return boolOperator2 =null;

        VcfFilterParser.subcondition_return subcondition3 =null;



        try {
            // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:87:11: ( subcondition ( boolOperator ^ subcondition )* )
            // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:87:13: subcondition ( boolOperator ^ subcondition )*
            {
            root_0 = (Object)adaptor.nil();


            pushFollow(FOLLOW_subcondition_in_condition386);
            subcondition1=subcondition();

            state._fsp--;

            adaptor.addChild(root_0, subcondition1.getTree());

            // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:87:26: ( boolOperator ^ subcondition )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==33||LA1_0==60) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:87:27: boolOperator ^ subcondition
            	    {
            	    pushFollow(FOLLOW_boolOperator_in_condition389);
            	    boolOperator2=boolOperator();

            	    state._fsp--;

            	    root_0 = (Object)adaptor.becomeRoot(boolOperator2.getTree(), root_0);

            	    pushFollow(FOLLOW_subcondition_in_condition392);
            	    subcondition3=subcondition();

            	    state._fsp--;

            	    adaptor.addChild(root_0, subcondition3.getTree());

            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "condition"


    public static class subcondition_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "subcondition"
    // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:88:1: subcondition : ( '!' ^)? ( bare | paren ) ;
    public final VcfFilterParser.subcondition_return subcondition() throws RecognitionException {
        VcfFilterParser.subcondition_return retval = new VcfFilterParser.subcondition_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token char_literal4=null;
        VcfFilterParser.bare_return bare5 =null;

        VcfFilterParser.paren_return paren6 =null;


        Object char_literal4_tree=null;

        try {
            // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:88:14: ( ( '!' ^)? ( bare | paren ) )
            // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:88:16: ( '!' ^)? ( bare | paren )
            {
            root_0 = (Object)adaptor.nil();


            // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:88:16: ( '!' ^)?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==30) ) {
                switch ( input.LA(2) ) {
                    case 30:
                    case 34:
                    case 53:
                    case 55:
                    case 56:
                    case 57:
                    case 58:
                    case 59:
                        {
                        alt2=1;
                        }
                        break;
                    case ID:
                        {
                        int LA2_4 = input.LA(3);

                        if ( (LA2_4==47) ) {
                            int LA2_10 = input.LA(4);

                            if ( (LA2_10==FLOAT||LA2_10==36) ) {
                                int LA2_14 = input.LA(5);

                                if ( (LA2_14==48) ) {
                                    int LA2_18 = input.LA(6);

                                    if ( ((LA2_18 >= 31 && LA2_18 <= 32)||(LA2_18 >= 38 && LA2_18 <= 43)||LA2_18==54) ) {
                                        alt2=1;
                                    }
                                }
                            }
                        }
                        else if ( ((LA2_4 >= 31 && LA2_4 <= 32)||(LA2_4 >= 38 && LA2_4 <= 43)||LA2_4==54) ) {
                            alt2=1;
                        }
                        }
                        break;
                    case 45:
                        {
                        int LA2_5 = input.LA(3);

                        if ( (LA2_5==47) ) {
                            int LA2_11 = input.LA(4);

                            if ( (LA2_11==FLOAT||LA2_11==36) ) {
                                int LA2_15 = input.LA(5);

                                if ( (LA2_15==48) ) {
                                    int LA2_19 = input.LA(6);

                                    if ( (LA2_19==37) ) {
                                        int LA2_21 = input.LA(7);

                                        if ( (LA2_21==ID) ) {
                                            int LA2_23 = input.LA(8);

                                            if ( (LA2_23==47) ) {
                                                int LA2_25 = input.LA(9);

                                                if ( (LA2_25==FLOAT||LA2_25==36) ) {
                                                    int LA2_26 = input.LA(10);

                                                    if ( (LA2_26==48) ) {
                                                        int LA2_27 = input.LA(11);

                                                        if ( ((LA2_27 >= 31 && LA2_27 <= 32)||(LA2_27 >= 38 && LA2_27 <= 43)||LA2_27==54) ) {
                                                            alt2=1;
                                                        }
                                                    }
                                                }
                                            }
                                            else if ( ((LA2_23 >= 31 && LA2_23 <= 32)||(LA2_23 >= 38 && LA2_23 <= 43)||LA2_23==54) ) {
                                                alt2=1;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        }
                        break;
                    case 44:
                        {
                        int LA2_6 = input.LA(3);

                        if ( (LA2_6==47) ) {
                            int LA2_12 = input.LA(4);

                            if ( (LA2_12==FLOAT||LA2_12==36) ) {
                                int LA2_16 = input.LA(5);

                                if ( (LA2_16==48) ) {
                                    int LA2_20 = input.LA(6);

                                    if ( (LA2_20==37) ) {
                                        int LA2_22 = input.LA(7);

                                        if ( (LA2_22==ID) ) {
                                            int LA2_24 = input.LA(8);

                                            if ( ((LA2_24 >= 31 && LA2_24 <= 32)||(LA2_24 >= 38 && LA2_24 <= 43)||LA2_24==54) ) {
                                                alt2=1;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        }
                        break;
                    case 49:
                    case 50:
                    case 51:
                    case 52:
                        {
                        int LA2_7 = input.LA(3);

                        if ( (LA2_7==34) ) {
                            int LA2_13 = input.LA(4);

                            if ( (LA2_13==35) ) {
                                int LA2_17 = input.LA(5);

                                if ( ((LA2_17 >= 31 && LA2_17 <= 32)||(LA2_17 >= 38 && LA2_17 <= 43)||LA2_17==54) ) {
                                    alt2=1;
                                }
                            }
                        }
                        }
                        break;
                    case FLOAT:
                        {
                        int LA2_8 = input.LA(3);

                        if ( ((LA2_8 >= 31 && LA2_8 <= 32)||(LA2_8 >= 38 && LA2_8 <= 43)||LA2_8==54) ) {
                            alt2=1;
                        }
                        }
                        break;
                    case STRING:
                        {
                        int LA2_9 = input.LA(3);

                        if ( ((LA2_9 >= 31 && LA2_9 <= 32)||(LA2_9 >= 38 && LA2_9 <= 43)||LA2_9==54) ) {
                            alt2=1;
                        }
                        }
                        break;
                }

            }
            switch (alt2) {
                case 1 :
                    // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:88:17: '!' ^
                    {
                    char_literal4=(Token)match(input,30,FOLLOW_30_in_subcondition402); 
                    char_literal4_tree = 
                    (Object)adaptor.create(char_literal4)
                    ;
                    root_0 = (Object)adaptor.becomeRoot(char_literal4_tree, root_0);


                    }
                    break;

            }


            // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:88:24: ( bare | paren )
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==FLOAT||LA3_0==ID||LA3_0==STRING||LA3_0==30||(LA3_0 >= 44 && LA3_0 <= 45)||(LA3_0 >= 49 && LA3_0 <= 53)||(LA3_0 >= 55 && LA3_0 <= 59)) ) {
                alt3=1;
            }
            else if ( (LA3_0==34) ) {
                alt3=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 3, 0, input);

                throw nvae;

            }
            switch (alt3) {
                case 1 :
                    // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:88:25: bare
                    {
                    pushFollow(FOLLOW_bare_in_subcondition408);
                    bare5=bare();

                    state._fsp--;

                    adaptor.addChild(root_0, bare5.getTree());

                    }
                    break;
                case 2 :
                    // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:88:32: paren
                    {
                    pushFollow(FOLLOW_paren_in_subcondition412);
                    paren6=paren();

                    state._fsp--;

                    adaptor.addChild(root_0, paren6.getTree());

                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);


            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "subcondition"


    public static class bare_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "bare"
    // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:89:1: bare : ( unaryExpr | binaryExpr | functionBoolean );
    public final VcfFilterParser.bare_return bare() throws RecognitionException {
        VcfFilterParser.bare_return retval = new VcfFilterParser.bare_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        VcfFilterParser.unaryExpr_return unaryExpr7 =null;

        VcfFilterParser.binaryExpr_return binaryExpr8 =null;

        VcfFilterParser.functionBoolean_return functionBoolean9 =null;



        try {
            // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:89:7: ( unaryExpr | binaryExpr | functionBoolean )
            int alt4=3;
            switch ( input.LA(1) ) {
            case 30:
            case 53:
            case 59:
                {
                alt4=1;
                }
                break;
            case ID:
                {
                switch ( input.LA(2) ) {
                case 47:
                    {
                    int LA4_9 = input.LA(3);

                    if ( (LA4_9==FLOAT||LA4_9==36) ) {
                        int LA4_14 = input.LA(4);

                        if ( (LA4_14==48) ) {
                            int LA4_18 = input.LA(5);

                            if ( ((LA4_18 >= 31 && LA4_18 <= 32)||(LA4_18 >= 38 && LA4_18 <= 43)) ) {
                                alt4=2;
                            }
                            else if ( (LA4_18==54) ) {
                                alt4=3;
                            }
                            else {
                                NoViableAltException nvae =
                                    new NoViableAltException("", 4, 18, input);

                                throw nvae;

                            }
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("", 4, 14, input);

                            throw nvae;

                        }
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 4, 9, input);

                        throw nvae;

                    }
                    }
                    break;
                case 31:
                case 32:
                case 38:
                case 39:
                case 40:
                case 41:
                case 42:
                case 43:
                    {
                    alt4=2;
                    }
                    break;
                case 54:
                    {
                    alt4=3;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 4, 2, input);

                    throw nvae;

                }

                }
                break;
            case 45:
                {
                int LA4_3 = input.LA(2);

                if ( (LA4_3==47) ) {
                    int LA4_11 = input.LA(3);

                    if ( (LA4_11==FLOAT||LA4_11==36) ) {
                        int LA4_15 = input.LA(4);

                        if ( (LA4_15==48) ) {
                            int LA4_19 = input.LA(5);

                            if ( (LA4_19==37) ) {
                                int LA4_21 = input.LA(6);

                                if ( (LA4_21==ID) ) {
                                    switch ( input.LA(7) ) {
                                    case 47:
                                        {
                                        int LA4_25 = input.LA(8);

                                        if ( (LA4_25==FLOAT||LA4_25==36) ) {
                                            int LA4_26 = input.LA(9);

                                            if ( (LA4_26==48) ) {
                                                int LA4_27 = input.LA(10);

                                                if ( ((LA4_27 >= 31 && LA4_27 <= 32)||(LA4_27 >= 38 && LA4_27 <= 43)) ) {
                                                    alt4=2;
                                                }
                                                else if ( (LA4_27==54) ) {
                                                    alt4=3;
                                                }
                                                else {
                                                    NoViableAltException nvae =
                                                        new NoViableAltException("", 4, 27, input);

                                                    throw nvae;

                                                }
                                            }
                                            else {
                                                NoViableAltException nvae =
                                                    new NoViableAltException("", 4, 26, input);

                                                throw nvae;

                                            }
                                        }
                                        else {
                                            NoViableAltException nvae =
                                                new NoViableAltException("", 4, 25, input);

                                            throw nvae;

                                        }
                                        }
                                        break;
                                    case 31:
                                    case 32:
                                    case 38:
                                    case 39:
                                    case 40:
                                    case 41:
                                    case 42:
                                    case 43:
                                        {
                                        alt4=2;
                                        }
                                        break;
                                    case 54:
                                        {
                                        alt4=3;
                                        }
                                        break;
                                    default:
                                        NoViableAltException nvae =
                                            new NoViableAltException("", 4, 23, input);

                                        throw nvae;

                                    }

                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("", 4, 21, input);

                                    throw nvae;

                                }
                            }
                            else {
                                NoViableAltException nvae =
                                    new NoViableAltException("", 4, 19, input);

                                throw nvae;

                            }
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("", 4, 15, input);

                            throw nvae;

                        }
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 4, 11, input);

                        throw nvae;

                    }
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 4, 3, input);

                    throw nvae;

                }
                }
                break;
            case 44:
                {
                int LA4_4 = input.LA(2);

                if ( (LA4_4==47) ) {
                    int LA4_12 = input.LA(3);

                    if ( (LA4_12==FLOAT||LA4_12==36) ) {
                        int LA4_16 = input.LA(4);

                        if ( (LA4_16==48) ) {
                            int LA4_20 = input.LA(5);

                            if ( (LA4_20==37) ) {
                                int LA4_22 = input.LA(6);

                                if ( (LA4_22==ID) ) {
                                    int LA4_24 = input.LA(7);

                                    if ( ((LA4_24 >= 31 && LA4_24 <= 32)||(LA4_24 >= 38 && LA4_24 <= 43)) ) {
                                        alt4=2;
                                    }
                                    else if ( (LA4_24==54) ) {
                                        alt4=3;
                                    }
                                    else {
                                        NoViableAltException nvae =
                                            new NoViableAltException("", 4, 24, input);

                                        throw nvae;

                                    }
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("", 4, 22, input);

                                    throw nvae;

                                }
                            }
                            else {
                                NoViableAltException nvae =
                                    new NoViableAltException("", 4, 20, input);

                                throw nvae;

                            }
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("", 4, 16, input);

                            throw nvae;

                        }
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 4, 12, input);

                        throw nvae;

                    }
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 4, 4, input);

                    throw nvae;

                }
                }
                break;
            case 49:
            case 50:
            case 51:
            case 52:
                {
                int LA4_5 = input.LA(2);

                if ( (LA4_5==34) ) {
                    int LA4_13 = input.LA(3);

                    if ( (LA4_13==35) ) {
                        int LA4_17 = input.LA(4);

                        if ( ((LA4_17 >= 31 && LA4_17 <= 32)||(LA4_17 >= 38 && LA4_17 <= 43)) ) {
                            alt4=2;
                        }
                        else if ( (LA4_17==54) ) {
                            alt4=3;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("", 4, 17, input);

                            throw nvae;

                        }
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 4, 13, input);

                        throw nvae;

                    }
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 4, 5, input);

                    throw nvae;

                }
                }
                break;
            case FLOAT:
                {
                int LA4_6 = input.LA(2);

                if ( ((LA4_6 >= 31 && LA4_6 <= 32)||(LA4_6 >= 38 && LA4_6 <= 43)) ) {
                    alt4=2;
                }
                else if ( (LA4_6==54) ) {
                    alt4=3;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 4, 6, input);

                    throw nvae;

                }
                }
                break;
            case STRING:
                {
                int LA4_7 = input.LA(2);

                if ( ((LA4_7 >= 31 && LA4_7 <= 32)||(LA4_7 >= 38 && LA4_7 <= 43)) ) {
                    alt4=2;
                }
                else if ( (LA4_7==54) ) {
                    alt4=3;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 4, 7, input);

                    throw nvae;

                }
                }
                break;
            case 55:
            case 56:
            case 57:
            case 58:
                {
                alt4=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 4, 0, input);

                throw nvae;

            }

            switch (alt4) {
                case 1 :
                    // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:89:9: unaryExpr
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_unaryExpr_in_bare421);
                    unaryExpr7=unaryExpr();

                    state._fsp--;

                    adaptor.addChild(root_0, unaryExpr7.getTree());

                    }
                    break;
                case 2 :
                    // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:89:21: binaryExpr
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_binaryExpr_in_bare425);
                    binaryExpr8=binaryExpr();

                    state._fsp--;

                    adaptor.addChild(root_0, binaryExpr8.getTree());

                    }
                    break;
                case 3 :
                    // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:89:34: functionBoolean
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_functionBoolean_in_bare429);
                    functionBoolean9=functionBoolean();

                    state._fsp--;

                    adaptor.addChild(root_0, functionBoolean9.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);


            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "bare"


    public static class paren_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "paren"
    // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:90:1: paren : '(' ! condition ')' !;
    public final VcfFilterParser.paren_return paren() throws RecognitionException {
        VcfFilterParser.paren_return retval = new VcfFilterParser.paren_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token char_literal10=null;
        Token char_literal12=null;
        VcfFilterParser.condition_return condition11 =null;


        Object char_literal10_tree=null;
        Object char_literal12_tree=null;

        try {
            // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:90:9: ( '(' ! condition ')' !)
            // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:90:11: '(' ! condition ')' !
            {
            root_0 = (Object)adaptor.nil();


            char_literal10=(Token)match(input,34,FOLLOW_34_in_paren439); 

            pushFollow(FOLLOW_condition_in_paren442);
            condition11=condition();

            state._fsp--;

            adaptor.addChild(root_0, condition11.getTree());

            char_literal12=(Token)match(input,35,FOLLOW_35_in_paren444); 

            }

            retval.stop = input.LT(-1);


            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "paren"


    public static class binaryExpr_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "binaryExpr"
    // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:93:1: binaryExpr : l= expression o= binOperator r= expression -> ^( OP_BINARY $o $l $r) ;
    public final VcfFilterParser.binaryExpr_return binaryExpr() throws RecognitionException {
        VcfFilterParser.binaryExpr_return retval = new VcfFilterParser.binaryExpr_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        VcfFilterParser.expression_return l =null;

        VcfFilterParser.binOperator_return o =null;

        VcfFilterParser.expression_return r =null;


        RewriteRuleSubtreeStream stream_expression=new RewriteRuleSubtreeStream(adaptor,"rule expression");
        RewriteRuleSubtreeStream stream_binOperator=new RewriteRuleSubtreeStream(adaptor,"rule binOperator");
        try {
            // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:93:12: (l= expression o= binOperator r= expression -> ^( OP_BINARY $o $l $r) )
            // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:93:14: l= expression o= binOperator r= expression
            {
            pushFollow(FOLLOW_expression_in_binaryExpr456);
            l=expression();

            state._fsp--;

            stream_expression.add(l.getTree());

            pushFollow(FOLLOW_binOperator_in_binaryExpr460);
            o=binOperator();

            state._fsp--;

            stream_binOperator.add(o.getTree());

            pushFollow(FOLLOW_expression_in_binaryExpr464);
            r=expression();

            state._fsp--;

            stream_expression.add(r.getTree());

            // AST REWRITE
            // elements: o, l, r
            // token labels: 
            // rule labels: retval, r, o, l
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_r=new RewriteRuleSubtreeStream(adaptor,"rule r",r!=null?r.tree:null);
            RewriteRuleSubtreeStream stream_o=new RewriteRuleSubtreeStream(adaptor,"rule o",o!=null?o.tree:null);
            RewriteRuleSubtreeStream stream_l=new RewriteRuleSubtreeStream(adaptor,"rule l",l!=null?l.tree:null);

            root_0 = (Object)adaptor.nil();
            // 93:57: -> ^( OP_BINARY $o $l $r)
            {
                // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:93:60: ^( OP_BINARY $o $l $r)
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(
                (Object)adaptor.create(OP_BINARY, "OP_BINARY")
                , root_1);

                adaptor.addChild(root_1, stream_o.nextTree());

                adaptor.addChild(root_1, stream_l.nextTree());

                adaptor.addChild(root_1, stream_r.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;

            }

            retval.stop = input.LT(-1);


            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "binaryExpr"


    public static class unaryExpr_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "unaryExpr"
    // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:94:1: unaryExpr : o= uniOperator e= expression -> ^( OP_UNARY $o $e) ;
    public final VcfFilterParser.unaryExpr_return unaryExpr() throws RecognitionException {
        VcfFilterParser.unaryExpr_return retval = new VcfFilterParser.unaryExpr_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        VcfFilterParser.uniOperator_return o =null;

        VcfFilterParser.expression_return e =null;


        RewriteRuleSubtreeStream stream_expression=new RewriteRuleSubtreeStream(adaptor,"rule expression");
        RewriteRuleSubtreeStream stream_uniOperator=new RewriteRuleSubtreeStream(adaptor,"rule uniOperator");
        try {
            // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:94:11: (o= uniOperator e= expression -> ^( OP_UNARY $o $e) )
            // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:94:13: o= uniOperator e= expression
            {
            pushFollow(FOLLOW_uniOperator_in_unaryExpr491);
            o=uniOperator();

            state._fsp--;

            stream_uniOperator.add(o.getTree());

            pushFollow(FOLLOW_expression_in_unaryExpr495);
            e=expression();

            state._fsp--;

            stream_expression.add(e.getTree());

            // AST REWRITE
            // elements: e, o
            // token labels: 
            // rule labels: retval, e, o
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_e=new RewriteRuleSubtreeStream(adaptor,"rule e",e!=null?e.tree:null);
            RewriteRuleSubtreeStream stream_o=new RewriteRuleSubtreeStream(adaptor,"rule o",o!=null?o.tree:null);

            root_0 = (Object)adaptor.nil();
            // 94:44: -> ^( OP_UNARY $o $e)
            {
                // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:94:47: ^( OP_UNARY $o $e)
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(
                (Object)adaptor.create(OP_UNARY, "OP_UNARY")
                , root_1);

                adaptor.addChild(root_1, stream_o.nextTree());

                adaptor.addChild(root_1, stream_e.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;

            }

            retval.stop = input.LT(-1);


            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "unaryExpr"


    public static class boolOperator_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "boolOperator"
    // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:97:1: boolOperator : ( '&' | '|' );
    public final VcfFilterParser.boolOperator_return boolOperator() throws RecognitionException {
        VcfFilterParser.boolOperator_return retval = new VcfFilterParser.boolOperator_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token set13=null;

        Object set13_tree=null;

        try {
            // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:97:16: ( '&' | '|' )
            // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:
            {
            root_0 = (Object)adaptor.nil();


            set13=(Token)input.LT(1);

            if ( input.LA(1)==33||input.LA(1)==60 ) {
                input.consume();
                adaptor.addChild(root_0, 
                (Object)adaptor.create(set13)
                );
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);


            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "boolOperator"


    public static class binOperator_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "binOperator"
    // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:98:1: binOperator : ( '=' | '>=' | '>' | '<=' | '<' | '!=' | '=~' | '!~' );
    public final VcfFilterParser.binOperator_return binOperator() throws RecognitionException {
        VcfFilterParser.binOperator_return retval = new VcfFilterParser.binOperator_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token set14=null;

        Object set14_tree=null;

        try {
            // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:98:15: ( '=' | '>=' | '>' | '<=' | '<' | '!=' | '=~' | '!~' )
            // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:
            {
            root_0 = (Object)adaptor.nil();


            set14=(Token)input.LT(1);

            if ( (input.LA(1) >= 31 && input.LA(1) <= 32)||(input.LA(1) >= 38 && input.LA(1) <= 43) ) {
                input.consume();
                adaptor.addChild(root_0, 
                (Object)adaptor.create(set14)
                );
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);


            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "binOperator"


    public static class uniOperator_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "uniOperator"
    // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:99:1: uniOperator : ( '!' | 'na' | 'exists' );
    public final VcfFilterParser.uniOperator_return uniOperator() throws RecognitionException {
        VcfFilterParser.uniOperator_return retval = new VcfFilterParser.uniOperator_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token set15=null;

        Object set15_tree=null;

        try {
            // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:99:15: ( '!' | 'na' | 'exists' )
            // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:
            {
            root_0 = (Object)adaptor.nil();


            set15=(Token)input.LT(1);

            if ( input.LA(1)==30||input.LA(1)==53||input.LA(1)==59 ) {
                input.consume();
                adaptor.addChild(root_0, 
                (Object)adaptor.create(set15)
                );
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);


            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "uniOperator"


    public static class expression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "expression"
    // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:102:1: expression : ( var | functionEntry | literalFloat | literalString );
    public final VcfFilterParser.expression_return expression() throws RecognitionException {
        VcfFilterParser.expression_return retval = new VcfFilterParser.expression_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        VcfFilterParser.var_return var16 =null;

        VcfFilterParser.functionEntry_return functionEntry17 =null;

        VcfFilterParser.literalFloat_return literalFloat18 =null;

        VcfFilterParser.literalString_return literalString19 =null;



        try {
            // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:102:12: ( var | functionEntry | literalFloat | literalString )
            int alt5=4;
            switch ( input.LA(1) ) {
            case ID:
            case 44:
            case 45:
                {
                alt5=1;
                }
                break;
            case 49:
            case 50:
            case 51:
            case 52:
                {
                alt5=2;
                }
                break;
            case FLOAT:
                {
                alt5=3;
                }
                break;
            case STRING:
                {
                alt5=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 5, 0, input);

                throw nvae;

            }

            switch (alt5) {
                case 1 :
                    // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:102:14: var
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_var_in_expression598);
                    var16=var();

                    state._fsp--;

                    adaptor.addChild(root_0, var16.getTree());

                    }
                    break;
                case 2 :
                    // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:103:6: functionEntry
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_functionEntry_in_expression606);
                    functionEntry17=functionEntry();

                    state._fsp--;

                    adaptor.addChild(root_0, functionEntry17.getTree());

                    }
                    break;
                case 3 :
                    // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:104:6: literalFloat
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_literalFloat_in_expression613);
                    literalFloat18=literalFloat();

                    state._fsp--;

                    adaptor.addChild(root_0, literalFloat18.getTree());

                    }
                    break;
                case 4 :
                    // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:105:6: literalString
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_literalString_in_expression621);
                    literalString19=literalString();

                    state._fsp--;

                    adaptor.addChild(root_0, literalString19.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);


            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "expression"


    public static class literalFloat_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "literalFloat"
    // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:108:1: literalFloat : f= FLOAT -> ^( LITERAL_NUMBER $f) ;
    public final VcfFilterParser.literalFloat_return literalFloat() throws RecognitionException {
        VcfFilterParser.literalFloat_return retval = new VcfFilterParser.literalFloat_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token f=null;

        Object f_tree=null;
        RewriteRuleTokenStream stream_FLOAT=new RewriteRuleTokenStream(adaptor,"token FLOAT");

        try {
            // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:108:14: (f= FLOAT -> ^( LITERAL_NUMBER $f) )
            // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:108:16: f= FLOAT
            {
            f=(Token)match(input,FLOAT,FOLLOW_FLOAT_in_literalFloat632);  
            stream_FLOAT.add(f);


            // AST REWRITE
            // elements: f
            // token labels: f
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleTokenStream stream_f=new RewriteRuleTokenStream(adaptor,"token f",f);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 108:31: -> ^( LITERAL_NUMBER $f)
            {
                // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:108:34: ^( LITERAL_NUMBER $f)
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(
                (Object)adaptor.create(LITERAL_NUMBER, "LITERAL_NUMBER")
                , root_1);

                adaptor.addChild(root_1, stream_f.nextNode());

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;

            }

            retval.stop = input.LT(-1);


            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "literalFloat"


    public static class literalString_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "literalString"
    // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:109:1: literalString : s= STRING -> ^( LITERAL_STRING $s) ;
    public final VcfFilterParser.literalString_return literalString() throws RecognitionException {
        VcfFilterParser.literalString_return retval = new VcfFilterParser.literalString_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token s=null;

        Object s_tree=null;
        RewriteRuleTokenStream stream_STRING=new RewriteRuleTokenStream(adaptor,"token STRING");

        try {
            // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:109:15: (s= STRING -> ^( LITERAL_STRING $s) )
            // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:109:17: s= STRING
            {
            s=(Token)match(input,STRING,FOLLOW_STRING_in_literalString657);  
            stream_STRING.add(s);


            // AST REWRITE
            // elements: s
            // token labels: s
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleTokenStream stream_s=new RewriteRuleTokenStream(adaptor,"token s",s);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 109:32: -> ^( LITERAL_STRING $s)
            {
                // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:109:35: ^( LITERAL_STRING $s)
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(
                (Object)adaptor.create(LITERAL_STRING, "LITERAL_STRING")
                , root_1);

                adaptor.addChild(root_1, stream_s.nextNode());

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;

            }

            retval.stop = input.LT(-1);


            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "literalString"


    public static class var_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "var"
    // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:112:1: var : ( varField | varSubfield | varGenotypeSub | varGenotypeSubArray | varEffSub );
    public final VcfFilterParser.var_return var() throws RecognitionException {
        VcfFilterParser.var_return retval = new VcfFilterParser.var_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        VcfFilterParser.varField_return varField20 =null;

        VcfFilterParser.varSubfield_return varSubfield21 =null;

        VcfFilterParser.varGenotypeSub_return varGenotypeSub22 =null;

        VcfFilterParser.varGenotypeSubArray_return varGenotypeSubArray23 =null;

        VcfFilterParser.varEffSub_return varEffSub24 =null;



        try {
            // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:112:8: ( varField | varSubfield | varGenotypeSub | varGenotypeSubArray | varEffSub )
            int alt6=5;
            switch ( input.LA(1) ) {
            case ID:
                {
                int LA6_1 = input.LA(2);

                if ( (LA6_1==47) ) {
                    alt6=2;
                }
                else if ( (LA6_1==EOF||(LA6_1 >= 31 && LA6_1 <= 33)||LA6_1==35||(LA6_1 >= 38 && LA6_1 <= 43)||LA6_1==54||LA6_1==60) ) {
                    alt6=1;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 6, 1, input);

                    throw nvae;

                }
                }
                break;
            case 45:
                {
                int LA6_2 = input.LA(2);

                if ( (LA6_2==47) ) {
                    int LA6_6 = input.LA(3);

                    if ( (LA6_6==FLOAT||LA6_6==36) ) {
                        int LA6_7 = input.LA(4);

                        if ( (LA6_7==48) ) {
                            int LA6_8 = input.LA(5);

                            if ( (LA6_8==37) ) {
                                int LA6_9 = input.LA(6);

                                if ( (LA6_9==ID) ) {
                                    int LA6_10 = input.LA(7);

                                    if ( (LA6_10==47) ) {
                                        alt6=4;
                                    }
                                    else if ( (LA6_10==EOF||(LA6_10 >= 31 && LA6_10 <= 33)||LA6_10==35||(LA6_10 >= 38 && LA6_10 <= 43)||LA6_10==54||LA6_10==60) ) {
                                        alt6=3;
                                    }
                                    else {
                                        NoViableAltException nvae =
                                            new NoViableAltException("", 6, 10, input);

                                        throw nvae;

                                    }
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("", 6, 9, input);

                                    throw nvae;

                                }
                            }
                            else {
                                NoViableAltException nvae =
                                    new NoViableAltException("", 6, 8, input);

                                throw nvae;

                            }
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("", 6, 7, input);

                            throw nvae;

                        }
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 6, 6, input);

                        throw nvae;

                    }
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 6, 2, input);

                    throw nvae;

                }
                }
                break;
            case 44:
                {
                alt6=5;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 6, 0, input);

                throw nvae;

            }

            switch (alt6) {
                case 1 :
                    // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:112:10: varField
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_varField_in_var685);
                    varField20=varField();

                    state._fsp--;

                    adaptor.addChild(root_0, varField20.getTree());

                    }
                    break;
                case 2 :
                    // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:112:21: varSubfield
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_varSubfield_in_var689);
                    varSubfield21=varSubfield();

                    state._fsp--;

                    adaptor.addChild(root_0, varSubfield21.getTree());

                    }
                    break;
                case 3 :
                    // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:112:35: varGenotypeSub
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_varGenotypeSub_in_var693);
                    varGenotypeSub22=varGenotypeSub();

                    state._fsp--;

                    adaptor.addChild(root_0, varGenotypeSub22.getTree());

                    }
                    break;
                case 4 :
                    // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:112:52: varGenotypeSubArray
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_varGenotypeSubArray_in_var697);
                    varGenotypeSubArray23=varGenotypeSubArray();

                    state._fsp--;

                    adaptor.addChild(root_0, varGenotypeSubArray23.getTree());

                    }
                    break;
                case 5 :
                    // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:112:74: varEffSub
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_varEffSub_in_var701);
                    varEffSub24=varEffSub();

                    state._fsp--;

                    adaptor.addChild(root_0, varEffSub24.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);


            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "var"


    public static class varField_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "varField"
    // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:113:1: varField : i= ID -> ^( VAR_FIELD $i) ;
    public final VcfFilterParser.varField_return varField() throws RecognitionException {
        VcfFilterParser.varField_return retval = new VcfFilterParser.varField_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token i=null;

        Object i_tree=null;
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");

        try {
            // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:113:11: (i= ID -> ^( VAR_FIELD $i) )
            // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:113:13: i= ID
            {
            i=(Token)match(input,ID,FOLLOW_ID_in_varField712);  
            stream_ID.add(i);


            // AST REWRITE
            // elements: i
            // token labels: i
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleTokenStream stream_i=new RewriteRuleTokenStream(adaptor,"token i",i);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 113:24: -> ^( VAR_FIELD $i)
            {
                // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:113:27: ^( VAR_FIELD $i)
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(
                (Object)adaptor.create(VAR_FIELD, "VAR_FIELD")
                , root_1);

                adaptor.addChild(root_1, stream_i.nextNode());

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;

            }

            retval.stop = input.LT(-1);


            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "varField"


    public static class varSubfield_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "varSubfield"
    // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:114:1: varSubfield : i= ID '[' n= index ']' -> ^( VAR_SUBFIELD $i $n) ;
    public final VcfFilterParser.varSubfield_return varSubfield() throws RecognitionException {
        VcfFilterParser.varSubfield_return retval = new VcfFilterParser.varSubfield_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token i=null;
        Token char_literal25=null;
        Token char_literal26=null;
        VcfFilterParser.index_return n =null;


        Object i_tree=null;
        Object char_literal25_tree=null;
        Object char_literal26_tree=null;
        RewriteRuleTokenStream stream_48=new RewriteRuleTokenStream(adaptor,"token 48");
        RewriteRuleTokenStream stream_47=new RewriteRuleTokenStream(adaptor,"token 47");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_index=new RewriteRuleSubtreeStream(adaptor,"rule index");
        try {
            // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:114:14: (i= ID '[' n= index ']' -> ^( VAR_SUBFIELD $i $n) )
            // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:114:16: i= ID '[' n= index ']'
            {
            i=(Token)match(input,ID,FOLLOW_ID_in_varSubfield737);  
            stream_ID.add(i);


            char_literal25=(Token)match(input,47,FOLLOW_47_in_varSubfield739);  
            stream_47.add(char_literal25);


            pushFollow(FOLLOW_index_in_varSubfield743);
            n=index();

            state._fsp--;

            stream_index.add(n.getTree());

            char_literal26=(Token)match(input,48,FOLLOW_48_in_varSubfield745);  
            stream_48.add(char_literal26);


            // AST REWRITE
            // elements: i, n
            // token labels: i
            // rule labels: retval, n
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleTokenStream stream_i=new RewriteRuleTokenStream(adaptor,"token i",i);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_n=new RewriteRuleSubtreeStream(adaptor,"rule n",n!=null?n.tree:null);

            root_0 = (Object)adaptor.nil();
            // 114:41: -> ^( VAR_SUBFIELD $i $n)
            {
                // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:114:44: ^( VAR_SUBFIELD $i $n)
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(
                (Object)adaptor.create(VAR_SUBFIELD, "VAR_SUBFIELD")
                , root_1);

                adaptor.addChild(root_1, stream_i.nextNode());

                adaptor.addChild(root_1, stream_n.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;

            }

            retval.stop = input.LT(-1);


            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "varSubfield"


    public static class varGenotype_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "varGenotype"
    // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:115:1: varGenotype : 'GEN' '[' g= index ']' -> ^( VAR_GENOTYPE $g) ;
    public final VcfFilterParser.varGenotype_return varGenotype() throws RecognitionException {
        VcfFilterParser.varGenotype_return retval = new VcfFilterParser.varGenotype_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token string_literal27=null;
        Token char_literal28=null;
        Token char_literal29=null;
        VcfFilterParser.index_return g =null;


        Object string_literal27_tree=null;
        Object char_literal28_tree=null;
        Object char_literal29_tree=null;
        RewriteRuleTokenStream stream_48=new RewriteRuleTokenStream(adaptor,"token 48");
        RewriteRuleTokenStream stream_45=new RewriteRuleTokenStream(adaptor,"token 45");
        RewriteRuleTokenStream stream_47=new RewriteRuleTokenStream(adaptor,"token 47");
        RewriteRuleSubtreeStream stream_index=new RewriteRuleSubtreeStream(adaptor,"rule index");
        try {
            // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:115:14: ( 'GEN' '[' g= index ']' -> ^( VAR_GENOTYPE $g) )
            // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:115:16: 'GEN' '[' g= index ']'
            {
            string_literal27=(Token)match(input,45,FOLLOW_45_in_varGenotype769);  
            stream_45.add(string_literal27);


            char_literal28=(Token)match(input,47,FOLLOW_47_in_varGenotype771);  
            stream_47.add(char_literal28);


            pushFollow(FOLLOW_index_in_varGenotype775);
            g=index();

            state._fsp--;

            stream_index.add(g.getTree());

            char_literal29=(Token)match(input,48,FOLLOW_48_in_varGenotype777);  
            stream_48.add(char_literal29);


            // AST REWRITE
            // elements: g
            // token labels: 
            // rule labels: g, retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_g=new RewriteRuleSubtreeStream(adaptor,"rule g",g!=null?g.tree:null);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 115:43: -> ^( VAR_GENOTYPE $g)
            {
                // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:115:46: ^( VAR_GENOTYPE $g)
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(
                (Object)adaptor.create(VAR_GENOTYPE, "VAR_GENOTYPE")
                , root_1);

                adaptor.addChild(root_1, stream_g.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;

            }

            retval.stop = input.LT(-1);


            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "varGenotype"


    public static class varGenotypeSub_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "varGenotypeSub"
    // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:116:1: varGenotypeSub : 'GEN' '[' g= index ']' '.' i= ID -> ^( VAR_GENOTYPE_SUB $g $i) ;
    public final VcfFilterParser.varGenotypeSub_return varGenotypeSub() throws RecognitionException {
        VcfFilterParser.varGenotypeSub_return retval = new VcfFilterParser.varGenotypeSub_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token i=null;
        Token string_literal30=null;
        Token char_literal31=null;
        Token char_literal32=null;
        Token char_literal33=null;
        VcfFilterParser.index_return g =null;


        Object i_tree=null;
        Object string_literal30_tree=null;
        Object char_literal31_tree=null;
        Object char_literal32_tree=null;
        Object char_literal33_tree=null;
        RewriteRuleTokenStream stream_48=new RewriteRuleTokenStream(adaptor,"token 48");
        RewriteRuleTokenStream stream_45=new RewriteRuleTokenStream(adaptor,"token 45");
        RewriteRuleTokenStream stream_47=new RewriteRuleTokenStream(adaptor,"token 47");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_37=new RewriteRuleTokenStream(adaptor,"token 37");
        RewriteRuleSubtreeStream stream_index=new RewriteRuleSubtreeStream(adaptor,"rule index");
        try {
            // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:116:17: ( 'GEN' '[' g= index ']' '.' i= ID -> ^( VAR_GENOTYPE_SUB $g $i) )
            // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:116:19: 'GEN' '[' g= index ']' '.' i= ID
            {
            string_literal30=(Token)match(input,45,FOLLOW_45_in_varGenotypeSub799);  
            stream_45.add(string_literal30);


            char_literal31=(Token)match(input,47,FOLLOW_47_in_varGenotypeSub801);  
            stream_47.add(char_literal31);


            pushFollow(FOLLOW_index_in_varGenotypeSub805);
            g=index();

            state._fsp--;

            stream_index.add(g.getTree());

            char_literal32=(Token)match(input,48,FOLLOW_48_in_varGenotypeSub807);  
            stream_48.add(char_literal32);


            char_literal33=(Token)match(input,37,FOLLOW_37_in_varGenotypeSub809);  
            stream_37.add(char_literal33);


            i=(Token)match(input,ID,FOLLOW_ID_in_varGenotypeSub813);  
            stream_ID.add(i);


            // AST REWRITE
            // elements: i, g
            // token labels: i
            // rule labels: g, retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleTokenStream stream_i=new RewriteRuleTokenStream(adaptor,"token i",i);
            RewriteRuleSubtreeStream stream_g=new RewriteRuleSubtreeStream(adaptor,"rule g",g!=null?g.tree:null);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 116:53: -> ^( VAR_GENOTYPE_SUB $g $i)
            {
                // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:116:56: ^( VAR_GENOTYPE_SUB $g $i)
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(
                (Object)adaptor.create(VAR_GENOTYPE_SUB, "VAR_GENOTYPE_SUB")
                , root_1);

                adaptor.addChild(root_1, stream_g.nextTree());

                adaptor.addChild(root_1, stream_i.nextNode());

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;

            }

            retval.stop = input.LT(-1);


            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "varGenotypeSub"


    public static class varGenotypeSubArray_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "varGenotypeSubArray"
    // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:117:1: varGenotypeSubArray : 'GEN' '[' g= index ']' '.' i= ID '[' n= index ']' -> ^( VAR_GENOTYPE_SUB_ARRAY $g $i $n) ;
    public final VcfFilterParser.varGenotypeSubArray_return varGenotypeSubArray() throws RecognitionException {
        VcfFilterParser.varGenotypeSubArray_return retval = new VcfFilterParser.varGenotypeSubArray_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token i=null;
        Token string_literal34=null;
        Token char_literal35=null;
        Token char_literal36=null;
        Token char_literal37=null;
        Token char_literal38=null;
        Token char_literal39=null;
        VcfFilterParser.index_return g =null;

        VcfFilterParser.index_return n =null;


        Object i_tree=null;
        Object string_literal34_tree=null;
        Object char_literal35_tree=null;
        Object char_literal36_tree=null;
        Object char_literal37_tree=null;
        Object char_literal38_tree=null;
        Object char_literal39_tree=null;
        RewriteRuleTokenStream stream_48=new RewriteRuleTokenStream(adaptor,"token 48");
        RewriteRuleTokenStream stream_45=new RewriteRuleTokenStream(adaptor,"token 45");
        RewriteRuleTokenStream stream_47=new RewriteRuleTokenStream(adaptor,"token 47");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_37=new RewriteRuleTokenStream(adaptor,"token 37");
        RewriteRuleSubtreeStream stream_index=new RewriteRuleSubtreeStream(adaptor,"rule index");
        try {
            // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:117:21: ( 'GEN' '[' g= index ']' '.' i= ID '[' n= index ']' -> ^( VAR_GENOTYPE_SUB_ARRAY $g $i $n) )
            // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:117:23: 'GEN' '[' g= index ']' '.' i= ID '[' n= index ']'
            {
            string_literal34=(Token)match(input,45,FOLLOW_45_in_varGenotypeSubArray835);  
            stream_45.add(string_literal34);


            char_literal35=(Token)match(input,47,FOLLOW_47_in_varGenotypeSubArray837);  
            stream_47.add(char_literal35);


            pushFollow(FOLLOW_index_in_varGenotypeSubArray841);
            g=index();

            state._fsp--;

            stream_index.add(g.getTree());

            char_literal36=(Token)match(input,48,FOLLOW_48_in_varGenotypeSubArray843);  
            stream_48.add(char_literal36);


            char_literal37=(Token)match(input,37,FOLLOW_37_in_varGenotypeSubArray845);  
            stream_37.add(char_literal37);


            i=(Token)match(input,ID,FOLLOW_ID_in_varGenotypeSubArray849);  
            stream_ID.add(i);


            char_literal38=(Token)match(input,47,FOLLOW_47_in_varGenotypeSubArray852);  
            stream_47.add(char_literal38);


            pushFollow(FOLLOW_index_in_varGenotypeSubArray856);
            n=index();

            state._fsp--;

            stream_index.add(n.getTree());

            char_literal39=(Token)match(input,48,FOLLOW_48_in_varGenotypeSubArray858);  
            stream_48.add(char_literal39);


            // AST REWRITE
            // elements: n, g, i
            // token labels: i
            // rule labels: g, retval, n
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleTokenStream stream_i=new RewriteRuleTokenStream(adaptor,"token i",i);
            RewriteRuleSubtreeStream stream_g=new RewriteRuleSubtreeStream(adaptor,"rule g",g!=null?g.tree:null);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_n=new RewriteRuleSubtreeStream(adaptor,"rule n",n!=null?n.tree:null);

            root_0 = (Object)adaptor.nil();
            // 117:72: -> ^( VAR_GENOTYPE_SUB_ARRAY $g $i $n)
            {
                // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:117:75: ^( VAR_GENOTYPE_SUB_ARRAY $g $i $n)
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(
                (Object)adaptor.create(VAR_GENOTYPE_SUB_ARRAY, "VAR_GENOTYPE_SUB_ARRAY")
                , root_1);

                adaptor.addChild(root_1, stream_g.nextTree());

                adaptor.addChild(root_1, stream_i.nextNode());

                adaptor.addChild(root_1, stream_n.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;

            }

            retval.stop = input.LT(-1);


            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "varGenotypeSubArray"


    public static class varEffSub_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "varEffSub"
    // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:118:1: varEffSub : 'EFF' '[' g= index ']' '.' i= ID -> ^( VAR_EFF_SUB $g $i) ;
    public final VcfFilterParser.varEffSub_return varEffSub() throws RecognitionException {
        VcfFilterParser.varEffSub_return retval = new VcfFilterParser.varEffSub_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token i=null;
        Token string_literal40=null;
        Token char_literal41=null;
        Token char_literal42=null;
        Token char_literal43=null;
        VcfFilterParser.index_return g =null;


        Object i_tree=null;
        Object string_literal40_tree=null;
        Object char_literal41_tree=null;
        Object char_literal42_tree=null;
        Object char_literal43_tree=null;
        RewriteRuleTokenStream stream_48=new RewriteRuleTokenStream(adaptor,"token 48");
        RewriteRuleTokenStream stream_44=new RewriteRuleTokenStream(adaptor,"token 44");
        RewriteRuleTokenStream stream_47=new RewriteRuleTokenStream(adaptor,"token 47");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_37=new RewriteRuleTokenStream(adaptor,"token 37");
        RewriteRuleSubtreeStream stream_index=new RewriteRuleSubtreeStream(adaptor,"rule index");
        try {
            // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:118:12: ( 'EFF' '[' g= index ']' '.' i= ID -> ^( VAR_EFF_SUB $g $i) )
            // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:118:14: 'EFF' '[' g= index ']' '.' i= ID
            {
            string_literal40=(Token)match(input,44,FOLLOW_44_in_varEffSub882);  
            stream_44.add(string_literal40);


            char_literal41=(Token)match(input,47,FOLLOW_47_in_varEffSub884);  
            stream_47.add(char_literal41);


            pushFollow(FOLLOW_index_in_varEffSub888);
            g=index();

            state._fsp--;

            stream_index.add(g.getTree());

            char_literal42=(Token)match(input,48,FOLLOW_48_in_varEffSub890);  
            stream_48.add(char_literal42);


            char_literal43=(Token)match(input,37,FOLLOW_37_in_varEffSub892);  
            stream_37.add(char_literal43);


            i=(Token)match(input,ID,FOLLOW_ID_in_varEffSub896);  
            stream_ID.add(i);


            // AST REWRITE
            // elements: i, g
            // token labels: i
            // rule labels: g, retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleTokenStream stream_i=new RewriteRuleTokenStream(adaptor,"token i",i);
            RewriteRuleSubtreeStream stream_g=new RewriteRuleSubtreeStream(adaptor,"rule g",g!=null?g.tree:null);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 118:48: -> ^( VAR_EFF_SUB $g $i)
            {
                // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:118:51: ^( VAR_EFF_SUB $g $i)
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(
                (Object)adaptor.create(VAR_EFF_SUB, "VAR_EFF_SUB")
                , root_1);

                adaptor.addChild(root_1, stream_g.nextTree());

                adaptor.addChild(root_1, stream_i.nextNode());

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;

            }

            retval.stop = input.LT(-1);


            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "varEffSub"


    public static class functionEntry_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "functionEntry"
    // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:121:1: functionEntry : f= functionEntryName '(' ')' -> ^( FUNCTION_ENTRY $f) ;
    public final VcfFilterParser.functionEntry_return functionEntry() throws RecognitionException {
        VcfFilterParser.functionEntry_return retval = new VcfFilterParser.functionEntry_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token char_literal44=null;
        Token char_literal45=null;
        VcfFilterParser.functionEntryName_return f =null;


        Object char_literal44_tree=null;
        Object char_literal45_tree=null;
        RewriteRuleTokenStream stream_35=new RewriteRuleTokenStream(adaptor,"token 35");
        RewriteRuleTokenStream stream_34=new RewriteRuleTokenStream(adaptor,"token 34");
        RewriteRuleSubtreeStream stream_functionEntryName=new RewriteRuleSubtreeStream(adaptor,"rule functionEntryName");
        try {
            // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:121:16: (f= functionEntryName '(' ')' -> ^( FUNCTION_ENTRY $f) )
            // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:121:18: f= functionEntryName '(' ')'
            {
            pushFollow(FOLLOW_functionEntryName_in_functionEntry923);
            f=functionEntryName();

            state._fsp--;

            stream_functionEntryName.add(f.getTree());

            char_literal44=(Token)match(input,34,FOLLOW_34_in_functionEntry925);  
            stream_34.add(char_literal44);


            char_literal45=(Token)match(input,35,FOLLOW_35_in_functionEntry927);  
            stream_35.add(char_literal45);


            // AST REWRITE
            // elements: f
            // token labels: 
            // rule labels: f, retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_f=new RewriteRuleSubtreeStream(adaptor,"rule f",f!=null?f.tree:null);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 121:49: -> ^( FUNCTION_ENTRY $f)
            {
                // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:121:52: ^( FUNCTION_ENTRY $f)
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(
                (Object)adaptor.create(FUNCTION_ENTRY, "FUNCTION_ENTRY")
                , root_1);

                adaptor.addChild(root_1, stream_f.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;

            }

            retval.stop = input.LT(-1);


            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "functionEntry"


    public static class functionEntryName_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "functionEntryName"
    // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:122:1: functionEntryName : ( 'countHom' | 'countHet' | 'countVariant' | 'countRef' );
    public final VcfFilterParser.functionEntryName_return functionEntryName() throws RecognitionException {
        VcfFilterParser.functionEntryName_return retval = new VcfFilterParser.functionEntryName_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token set46=null;

        Object set46_tree=null;

        try {
            // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:122:19: ( 'countHom' | 'countHet' | 'countVariant' | 'countRef' )
            // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:
            {
            root_0 = (Object)adaptor.nil();


            set46=(Token)input.LT(1);

            if ( (input.LA(1) >= 49 && input.LA(1) <= 52) ) {
                input.consume();
                adaptor.addChild(root_0, 
                (Object)adaptor.create(set46)
                );
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);


            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "functionEntryName"


    public static class functionBoolean_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "functionBoolean"
    // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:125:1: functionBoolean : ( functionGenotypeBool | functionBooleanSet );
    public final VcfFilterParser.functionBoolean_return functionBoolean() throws RecognitionException {
        VcfFilterParser.functionBoolean_return retval = new VcfFilterParser.functionBoolean_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        VcfFilterParser.functionGenotypeBool_return functionGenotypeBool47 =null;

        VcfFilterParser.functionBooleanSet_return functionBooleanSet48 =null;



        try {
            // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:125:18: ( functionGenotypeBool | functionBooleanSet )
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( ((LA7_0 >= 55 && LA7_0 <= 58)) ) {
                alt7=1;
            }
            else if ( (LA7_0==FLOAT||LA7_0==ID||LA7_0==STRING||(LA7_0 >= 44 && LA7_0 <= 45)||(LA7_0 >= 49 && LA7_0 <= 52)) ) {
                alt7=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 7, 0, input);

                throw nvae;

            }
            switch (alt7) {
                case 1 :
                    // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:125:20: functionGenotypeBool
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_functionGenotypeBool_in_functionBoolean968);
                    functionGenotypeBool47=functionGenotypeBool();

                    state._fsp--;

                    adaptor.addChild(root_0, functionGenotypeBool47.getTree());

                    }
                    break;
                case 2 :
                    // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:126:7: functionBooleanSet
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_functionBooleanSet_in_functionBoolean977);
                    functionBooleanSet48=functionBooleanSet();

                    state._fsp--;

                    adaptor.addChild(root_0, functionBooleanSet48.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);


            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "functionBoolean"


    public static class functionBooleanSet_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "functionBooleanSet"
    // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:130:1: functionBooleanSet : e= expression f= 'in' 'SET' '[' i= index ']' -> ^( FUNCTION_BOOL_SET $f $i $e) ;
    public final VcfFilterParser.functionBooleanSet_return functionBooleanSet() throws RecognitionException {
        VcfFilterParser.functionBooleanSet_return retval = new VcfFilterParser.functionBooleanSet_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token f=null;
        Token string_literal49=null;
        Token char_literal50=null;
        Token char_literal51=null;
        VcfFilterParser.expression_return e =null;

        VcfFilterParser.index_return i =null;


        Object f_tree=null;
        Object string_literal49_tree=null;
        Object char_literal50_tree=null;
        Object char_literal51_tree=null;
        RewriteRuleTokenStream stream_48=new RewriteRuleTokenStream(adaptor,"token 48");
        RewriteRuleTokenStream stream_47=new RewriteRuleTokenStream(adaptor,"token 47");
        RewriteRuleTokenStream stream_46=new RewriteRuleTokenStream(adaptor,"token 46");
        RewriteRuleTokenStream stream_54=new RewriteRuleTokenStream(adaptor,"token 54");
        RewriteRuleSubtreeStream stream_expression=new RewriteRuleSubtreeStream(adaptor,"rule expression");
        RewriteRuleSubtreeStream stream_index=new RewriteRuleSubtreeStream(adaptor,"rule index");
        try {
            // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:130:20: (e= expression f= 'in' 'SET' '[' i= index ']' -> ^( FUNCTION_BOOL_SET $f $i $e) )
            // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:130:22: e= expression f= 'in' 'SET' '[' i= index ']'
            {
            pushFollow(FOLLOW_expression_in_functionBooleanSet993);
            e=expression();

            state._fsp--;

            stream_expression.add(e.getTree());

            f=(Token)match(input,54,FOLLOW_54_in_functionBooleanSet997);  
            stream_54.add(f);


            string_literal49=(Token)match(input,46,FOLLOW_46_in_functionBooleanSet999);  
            stream_46.add(string_literal49);


            char_literal50=(Token)match(input,47,FOLLOW_47_in_functionBooleanSet1001);  
            stream_47.add(char_literal50);


            pushFollow(FOLLOW_index_in_functionBooleanSet1005);
            i=index();

            state._fsp--;

            stream_index.add(i.getTree());

            char_literal51=(Token)match(input,48,FOLLOW_48_in_functionBooleanSet1007);  
            stream_48.add(char_literal51);


            // AST REWRITE
            // elements: e, f, i
            // token labels: f
            // rule labels: retval, e, i
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleTokenStream stream_f=new RewriteRuleTokenStream(adaptor,"token f",f);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_e=new RewriteRuleSubtreeStream(adaptor,"rule e",e!=null?e.tree:null);
            RewriteRuleSubtreeStream stream_i=new RewriteRuleSubtreeStream(adaptor,"rule i",i!=null?i.tree:null);

            root_0 = (Object)adaptor.nil();
            // 130:66: -> ^( FUNCTION_BOOL_SET $f $i $e)
            {
                // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:130:69: ^( FUNCTION_BOOL_SET $f $i $e)
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(
                (Object)adaptor.create(FUNCTION_BOOL_SET, "FUNCTION_BOOL_SET")
                , root_1);

                adaptor.addChild(root_1, stream_f.nextNode());

                adaptor.addChild(root_1, stream_i.nextTree());

                adaptor.addChild(root_1, stream_e.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;

            }

            retval.stop = input.LT(-1);


            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "functionBooleanSet"


    public static class functionGenotypeBool_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "functionGenotypeBool"
    // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:133:1: functionGenotypeBool : f= functionGenotypeBoolName '(' g= varGenotype ')' -> ^( FUNCTION_BOOL_GENOTYPE $f $g) ;
    public final VcfFilterParser.functionGenotypeBool_return functionGenotypeBool() throws RecognitionException {
        VcfFilterParser.functionGenotypeBool_return retval = new VcfFilterParser.functionGenotypeBool_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token char_literal52=null;
        Token char_literal53=null;
        VcfFilterParser.functionGenotypeBoolName_return f =null;

        VcfFilterParser.varGenotype_return g =null;


        Object char_literal52_tree=null;
        Object char_literal53_tree=null;
        RewriteRuleTokenStream stream_35=new RewriteRuleTokenStream(adaptor,"token 35");
        RewriteRuleTokenStream stream_34=new RewriteRuleTokenStream(adaptor,"token 34");
        RewriteRuleSubtreeStream stream_functionGenotypeBoolName=new RewriteRuleSubtreeStream(adaptor,"rule functionGenotypeBoolName");
        RewriteRuleSubtreeStream stream_varGenotype=new RewriteRuleSubtreeStream(adaptor,"rule varGenotype");
        try {
            // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:133:22: (f= functionGenotypeBoolName '(' g= varGenotype ')' -> ^( FUNCTION_BOOL_GENOTYPE $f $g) )
            // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:133:24: f= functionGenotypeBoolName '(' g= varGenotype ')'
            {
            pushFollow(FOLLOW_functionGenotypeBoolName_in_functionGenotypeBool1035);
            f=functionGenotypeBoolName();

            state._fsp--;

            stream_functionGenotypeBoolName.add(f.getTree());

            char_literal52=(Token)match(input,34,FOLLOW_34_in_functionGenotypeBool1037);  
            stream_34.add(char_literal52);


            pushFollow(FOLLOW_varGenotype_in_functionGenotypeBool1041);
            g=varGenotype();

            state._fsp--;

            stream_varGenotype.add(g.getTree());

            char_literal53=(Token)match(input,35,FOLLOW_35_in_functionGenotypeBool1043);  
            stream_35.add(char_literal53);


            // AST REWRITE
            // elements: f, g
            // token labels: 
            // rule labels: f, g, retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_f=new RewriteRuleSubtreeStream(adaptor,"rule f",f!=null?f.tree:null);
            RewriteRuleSubtreeStream stream_g=new RewriteRuleSubtreeStream(adaptor,"rule g",g!=null?g.tree:null);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 133:73: -> ^( FUNCTION_BOOL_GENOTYPE $f $g)
            {
                // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:133:76: ^( FUNCTION_BOOL_GENOTYPE $f $g)
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(
                (Object)adaptor.create(FUNCTION_BOOL_GENOTYPE, "FUNCTION_BOOL_GENOTYPE")
                , root_1);

                adaptor.addChild(root_1, stream_f.nextTree());

                adaptor.addChild(root_1, stream_g.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;

            }

            retval.stop = input.LT(-1);


            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "functionGenotypeBool"


    public static class functionGenotypeBoolName_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "functionGenotypeBoolName"
    // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:134:1: functionGenotypeBoolName : ( 'isHom' | 'isHet' | 'isVariant' | 'isRef' );
    public final VcfFilterParser.functionGenotypeBoolName_return functionGenotypeBoolName() throws RecognitionException {
        VcfFilterParser.functionGenotypeBoolName_return retval = new VcfFilterParser.functionGenotypeBoolName_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token set54=null;

        Object set54_tree=null;

        try {
            // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:134:26: ( 'isHom' | 'isHet' | 'isVariant' | 'isRef' )
            // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:
            {
            root_0 = (Object)adaptor.nil();


            set54=(Token)input.LT(1);

            if ( (input.LA(1) >= 55 && input.LA(1) <= 58) ) {
                input.consume();
                adaptor.addChild(root_0, 
                (Object)adaptor.create(set54)
                );
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);


            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "functionGenotypeBoolName"


    public static class index_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "index"
    // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:137:1: index : ( FLOAT | '*' );
    public final VcfFilterParser.index_return index() throws RecognitionException {
        VcfFilterParser.index_return retval = new VcfFilterParser.index_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token set55=null;

        Object set55_tree=null;

        try {
            // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:137:9: ( FLOAT | '*' )
            // /home/pcingola/workspace/SnpSift/antlr/VcfFilter.g:
            {
            root_0 = (Object)adaptor.nil();


            set55=(Token)input.LT(1);

            if ( input.LA(1)==FLOAT||input.LA(1)==36 ) {
                input.consume();
                adaptor.addChild(root_0, 
                (Object)adaptor.create(set55)
                );
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);


            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "index"

    // Delegated rules


 

    public static final BitSet FOLLOW_condition_in_main369 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_subcondition_in_condition386 = new BitSet(new long[]{0x1000000200000002L});
    public static final BitSet FOLLOW_boolOperator_in_condition389 = new BitSet(new long[]{0x0FBE300440201100L});
    public static final BitSet FOLLOW_subcondition_in_condition392 = new BitSet(new long[]{0x1000000200000002L});
    public static final BitSet FOLLOW_30_in_subcondition402 = new BitSet(new long[]{0x0FBE300440201100L});
    public static final BitSet FOLLOW_bare_in_subcondition408 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_paren_in_subcondition412 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_unaryExpr_in_bare421 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_binaryExpr_in_bare425 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_functionBoolean_in_bare429 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_34_in_paren439 = new BitSet(new long[]{0x0FBE300440201100L});
    public static final BitSet FOLLOW_condition_in_paren442 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_35_in_paren444 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_binaryExpr456 = new BitSet(new long[]{0x00000FC180000000L});
    public static final BitSet FOLLOW_binOperator_in_binaryExpr460 = new BitSet(new long[]{0x001E300000201100L});
    public static final BitSet FOLLOW_expression_in_binaryExpr464 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_uniOperator_in_unaryExpr491 = new BitSet(new long[]{0x001E300000201100L});
    public static final BitSet FOLLOW_expression_in_unaryExpr495 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_var_in_expression598 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_functionEntry_in_expression606 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_literalFloat_in_expression613 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_literalString_in_expression621 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FLOAT_in_literalFloat632 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_in_literalString657 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_varField_in_var685 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_varSubfield_in_var689 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_varGenotypeSub_in_var693 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_varGenotypeSubArray_in_var697 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_varEffSub_in_var701 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_varField712 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_varSubfield737 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_47_in_varSubfield739 = new BitSet(new long[]{0x0000001000000100L});
    public static final BitSet FOLLOW_index_in_varSubfield743 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_48_in_varSubfield745 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_45_in_varGenotype769 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_47_in_varGenotype771 = new BitSet(new long[]{0x0000001000000100L});
    public static final BitSet FOLLOW_index_in_varGenotype775 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_48_in_varGenotype777 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_45_in_varGenotypeSub799 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_47_in_varGenotypeSub801 = new BitSet(new long[]{0x0000001000000100L});
    public static final BitSet FOLLOW_index_in_varGenotypeSub805 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_48_in_varGenotypeSub807 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_37_in_varGenotypeSub809 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_ID_in_varGenotypeSub813 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_45_in_varGenotypeSubArray835 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_47_in_varGenotypeSubArray837 = new BitSet(new long[]{0x0000001000000100L});
    public static final BitSet FOLLOW_index_in_varGenotypeSubArray841 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_48_in_varGenotypeSubArray843 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_37_in_varGenotypeSubArray845 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_ID_in_varGenotypeSubArray849 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_47_in_varGenotypeSubArray852 = new BitSet(new long[]{0x0000001000000100L});
    public static final BitSet FOLLOW_index_in_varGenotypeSubArray856 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_48_in_varGenotypeSubArray858 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_44_in_varEffSub882 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_47_in_varEffSub884 = new BitSet(new long[]{0x0000001000000100L});
    public static final BitSet FOLLOW_index_in_varEffSub888 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_48_in_varEffSub890 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_37_in_varEffSub892 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_ID_in_varEffSub896 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_functionEntryName_in_functionEntry923 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_34_in_functionEntry925 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_35_in_functionEntry927 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_functionGenotypeBool_in_functionBoolean968 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_functionBooleanSet_in_functionBoolean977 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_functionBooleanSet993 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_54_in_functionBooleanSet997 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_46_in_functionBooleanSet999 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_47_in_functionBooleanSet1001 = new BitSet(new long[]{0x0000001000000100L});
    public static final BitSet FOLLOW_index_in_functionBooleanSet1005 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_48_in_functionBooleanSet1007 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_functionGenotypeBoolName_in_functionGenotypeBool1035 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_34_in_functionGenotypeBool1037 = new BitSet(new long[]{0x0000200000000000L});
    public static final BitSet FOLLOW_varGenotype_in_functionGenotypeBool1041 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_35_in_functionGenotypeBool1043 = new BitSet(new long[]{0x0000000000000002L});

}