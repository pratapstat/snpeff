// $ANTLR 3.4 /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g 2011-08-26 12:10:28

package ca.mcgill.mcb.pcingola.vcfEtc.antlr;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

import org.antlr.runtime.tree.*;


@SuppressWarnings({"all", "warnings", "unchecked"})
public class VcfFilterParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "ALPHANUM", "COMMENT_SL", "CONDITION", "DIGIT", "FLOAT", "FUNCTION", "ID", "LETTER", "LITERAL_NUMBER", "LITERAL_STRING", "LOWER", "NEWLINE", "NUMBER", "OP_BINARY", "OP_UNARY", "STRING", "UPPER", "VAR_FIELD", "VAR_GENOTYPE", "VAR_GENOTYPE_SUB", "VAR_SUBFIELD", "WS", "'!'", "'!='", "'!~'", "'&'", "'('", "')'", "'*'", "'.'", "'<'", "'<='", "'='", "'=~'", "'>'", "'>='", "'GEN'", "'['", "']'", "'countHet'", "'countHom'", "'countRef'", "'countVariant'", "'exists'", "'na'", "'|'"
    };

    public static final int EOF=-1;
    public static final int T__26=26;
    public static final int T__27=27;
    public static final int T__28=28;
    public static final int T__29=29;
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
    public static final int ALPHANUM=4;
    public static final int COMMENT_SL=5;
    public static final int CONDITION=6;
    public static final int DIGIT=7;
    public static final int FLOAT=8;
    public static final int FUNCTION=9;
    public static final int ID=10;
    public static final int LETTER=11;
    public static final int LITERAL_NUMBER=12;
    public static final int LITERAL_STRING=13;
    public static final int LOWER=14;
    public static final int NEWLINE=15;
    public static final int NUMBER=16;
    public static final int OP_BINARY=17;
    public static final int OP_UNARY=18;
    public static final int STRING=19;
    public static final int UPPER=20;
    public static final int VAR_FIELD=21;
    public static final int VAR_GENOTYPE=22;
    public static final int VAR_GENOTYPE_SUB=23;
    public static final int VAR_SUBFIELD=24;
    public static final int WS=25;

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
    public String getGrammarFileName() { return "/Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g"; }


    public static class main_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "main"
    // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:81:1: main : f= condition -> ^( CONDITION $f) ;
    public final VcfFilterParser.main_return main() throws RecognitionException {
        VcfFilterParser.main_return retval = new VcfFilterParser.main_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        VcfFilterParser.condition_return f =null;


        RewriteRuleSubtreeStream stream_condition=new RewriteRuleSubtreeStream(adaptor,"rule condition");
        try {
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:81:7: (f= condition -> ^( CONDITION $f) )
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:81:9: f= condition
            {
            pushFollow(FOLLOW_condition_in_main348);
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
            // 81:21: -> ^( CONDITION $f)
            {
                // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:81:24: ^( CONDITION $f)
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
    // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:83:1: condition : subcondition ( boolOperator ^ subcondition )* ;
    public final VcfFilterParser.condition_return condition() throws RecognitionException {
        VcfFilterParser.condition_return retval = new VcfFilterParser.condition_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        VcfFilterParser.subcondition_return subcondition1 =null;

        VcfFilterParser.boolOperator_return boolOperator2 =null;

        VcfFilterParser.subcondition_return subcondition3 =null;



        try {
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:83:11: ( subcondition ( boolOperator ^ subcondition )* )
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:83:13: subcondition ( boolOperator ^ subcondition )*
            {
            root_0 = (Object)adaptor.nil();


            pushFollow(FOLLOW_subcondition_in_condition365);
            subcondition1=subcondition();

            state._fsp--;

            adaptor.addChild(root_0, subcondition1.getTree());

            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:83:26: ( boolOperator ^ subcondition )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==29||LA1_0==49) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:83:27: boolOperator ^ subcondition
            	    {
            	    pushFollow(FOLLOW_boolOperator_in_condition368);
            	    boolOperator2=boolOperator();

            	    state._fsp--;

            	    root_0 = (Object)adaptor.becomeRoot(boolOperator2.getTree(), root_0);

            	    pushFollow(FOLLOW_subcondition_in_condition371);
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
    // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:84:1: subcondition : ( '!' ^)? ( bare | paren ) ;
    public final VcfFilterParser.subcondition_return subcondition() throws RecognitionException {
        VcfFilterParser.subcondition_return retval = new VcfFilterParser.subcondition_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token char_literal4=null;
        VcfFilterParser.bare_return bare5 =null;

        VcfFilterParser.paren_return paren6 =null;


        Object char_literal4_tree=null;

        try {
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:84:14: ( ( '!' ^)? ( bare | paren ) )
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:84:16: ( '!' ^)? ( bare | paren )
            {
            root_0 = (Object)adaptor.nil();


            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:84:16: ( '!' ^)?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==26) ) {
                switch ( input.LA(2) ) {
                    case ID:
                        {
                        int LA2_3 = input.LA(3);

                        if ( (LA2_3==41) ) {
                            int LA2_9 = input.LA(4);

                            if ( (LA2_9==FLOAT||LA2_9==32) ) {
                                int LA2_12 = input.LA(5);

                                if ( (LA2_12==42) ) {
                                    int LA2_15 = input.LA(6);

                                    if ( ((LA2_15 >= 27 && LA2_15 <= 28)||(LA2_15 >= 34 && LA2_15 <= 39)) ) {
                                        alt2=1;
                                    }
                                }
                            }
                        }
                        else if ( ((LA2_3 >= 27 && LA2_3 <= 28)||(LA2_3 >= 34 && LA2_3 <= 39)) ) {
                            alt2=1;
                        }
                        }
                        break;
                    case 40:
                        {
                        int LA2_4 = input.LA(3);

                        if ( (LA2_4==41) ) {
                            int LA2_10 = input.LA(4);

                            if ( (LA2_10==FLOAT||LA2_10==32) ) {
                                int LA2_13 = input.LA(5);

                                if ( (LA2_13==42) ) {
                                    int LA2_16 = input.LA(6);

                                    if ( (LA2_16==33) ) {
                                        int LA2_17 = input.LA(7);

                                        if ( (LA2_17==ID) ) {
                                            int LA2_18 = input.LA(8);

                                            if ( (LA2_18==41) ) {
                                                int LA2_19 = input.LA(9);

                                                if ( (LA2_19==FLOAT) ) {
                                                    int LA2_20 = input.LA(10);

                                                    if ( (LA2_20==42) ) {
                                                        int LA2_21 = input.LA(11);

                                                        if ( ((LA2_21 >= 27 && LA2_21 <= 28)||(LA2_21 >= 34 && LA2_21 <= 39)) ) {
                                                            alt2=1;
                                                        }
                                                    }
                                                }
                                            }
                                            else if ( ((LA2_18 >= 27 && LA2_18 <= 28)||(LA2_18 >= 34 && LA2_18 <= 39)) ) {
                                                alt2=1;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        }
                        break;
                    case 43:
                    case 44:
                    case 45:
                    case 46:
                        {
                        int LA2_5 = input.LA(3);

                        if ( (LA2_5==30) ) {
                            int LA2_11 = input.LA(4);

                            if ( (LA2_11==31) ) {
                                int LA2_14 = input.LA(5);

                                if ( ((LA2_14 >= 27 && LA2_14 <= 28)||(LA2_14 >= 34 && LA2_14 <= 39)) ) {
                                    alt2=1;
                                }
                            }
                        }
                        }
                        break;
                    case FLOAT:
                        {
                        int LA2_6 = input.LA(3);

                        if ( ((LA2_6 >= 27 && LA2_6 <= 28)||(LA2_6 >= 34 && LA2_6 <= 39)) ) {
                            alt2=1;
                        }
                        }
                        break;
                    case STRING:
                        {
                        int LA2_7 = input.LA(3);

                        if ( ((LA2_7 >= 27 && LA2_7 <= 28)||(LA2_7 >= 34 && LA2_7 <= 39)) ) {
                            alt2=1;
                        }
                        }
                        break;
                    case 26:
                    case 30:
                    case 47:
                    case 48:
                        {
                        alt2=1;
                        }
                        break;
                }

            }
            switch (alt2) {
                case 1 :
                    // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:84:17: '!' ^
                    {
                    char_literal4=(Token)match(input,26,FOLLOW_26_in_subcondition381); 
                    char_literal4_tree = 
                    (Object)adaptor.create(char_literal4)
                    ;
                    root_0 = (Object)adaptor.becomeRoot(char_literal4_tree, root_0);


                    }
                    break;

            }


            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:84:24: ( bare | paren )
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==FLOAT||LA3_0==ID||LA3_0==STRING||LA3_0==26||LA3_0==40||(LA3_0 >= 43 && LA3_0 <= 48)) ) {
                alt3=1;
            }
            else if ( (LA3_0==30) ) {
                alt3=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 3, 0, input);

                throw nvae;

            }
            switch (alt3) {
                case 1 :
                    // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:84:25: bare
                    {
                    pushFollow(FOLLOW_bare_in_subcondition387);
                    bare5=bare();

                    state._fsp--;

                    adaptor.addChild(root_0, bare5.getTree());

                    }
                    break;
                case 2 :
                    // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:84:32: paren
                    {
                    pushFollow(FOLLOW_paren_in_subcondition391);
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
    // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:85:1: bare : ( binaryExpr | unaryExpr );
    public final VcfFilterParser.bare_return bare() throws RecognitionException {
        VcfFilterParser.bare_return retval = new VcfFilterParser.bare_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        VcfFilterParser.binaryExpr_return binaryExpr7 =null;

        VcfFilterParser.unaryExpr_return unaryExpr8 =null;



        try {
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:85:7: ( binaryExpr | unaryExpr )
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==FLOAT||LA4_0==ID||LA4_0==STRING||LA4_0==40||(LA4_0 >= 43 && LA4_0 <= 46)) ) {
                alt4=1;
            }
            else if ( (LA4_0==26||(LA4_0 >= 47 && LA4_0 <= 48)) ) {
                alt4=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 4, 0, input);

                throw nvae;

            }
            switch (alt4) {
                case 1 :
                    // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:85:9: binaryExpr
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_binaryExpr_in_bare400);
                    binaryExpr7=binaryExpr();

                    state._fsp--;

                    adaptor.addChild(root_0, binaryExpr7.getTree());

                    }
                    break;
                case 2 :
                    // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:85:22: unaryExpr
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_unaryExpr_in_bare404);
                    unaryExpr8=unaryExpr();

                    state._fsp--;

                    adaptor.addChild(root_0, unaryExpr8.getTree());

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
    // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:86:1: paren : '(' ! condition ')' !;
    public final VcfFilterParser.paren_return paren() throws RecognitionException {
        VcfFilterParser.paren_return retval = new VcfFilterParser.paren_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token char_literal9=null;
        Token char_literal11=null;
        VcfFilterParser.condition_return condition10 =null;


        Object char_literal9_tree=null;
        Object char_literal11_tree=null;

        try {
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:86:9: ( '(' ! condition ')' !)
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:86:11: '(' ! condition ')' !
            {
            root_0 = (Object)adaptor.nil();


            char_literal9=(Token)match(input,30,FOLLOW_30_in_paren413); 

            pushFollow(FOLLOW_condition_in_paren416);
            condition10=condition();

            state._fsp--;

            adaptor.addChild(root_0, condition10.getTree());

            char_literal11=(Token)match(input,31,FOLLOW_31_in_paren418); 

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
    // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:89:1: binaryExpr : l= expression o= binOperator r= expression -> ^( OP_BINARY $o $l $r) ;
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
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:89:12: (l= expression o= binOperator r= expression -> ^( OP_BINARY $o $l $r) )
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:89:14: l= expression o= binOperator r= expression
            {
            pushFollow(FOLLOW_expression_in_binaryExpr430);
            l=expression();

            state._fsp--;

            stream_expression.add(l.getTree());

            pushFollow(FOLLOW_binOperator_in_binaryExpr434);
            o=binOperator();

            state._fsp--;

            stream_binOperator.add(o.getTree());

            pushFollow(FOLLOW_expression_in_binaryExpr438);
            r=expression();

            state._fsp--;

            stream_expression.add(r.getTree());

            // AST REWRITE
            // elements: o, r, l
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
            // 89:56: -> ^( OP_BINARY $o $l $r)
            {
                // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:89:59: ^( OP_BINARY $o $l $r)
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
    // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:90:1: unaryExpr : o= uniOperator e= expression -> ^( OP_UNARY $o $e) ;
    public final VcfFilterParser.unaryExpr_return unaryExpr() throws RecognitionException {
        VcfFilterParser.unaryExpr_return retval = new VcfFilterParser.unaryExpr_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        VcfFilterParser.uniOperator_return o =null;

        VcfFilterParser.expression_return e =null;


        RewriteRuleSubtreeStream stream_expression=new RewriteRuleSubtreeStream(adaptor,"rule expression");
        RewriteRuleSubtreeStream stream_uniOperator=new RewriteRuleSubtreeStream(adaptor,"rule uniOperator");
        try {
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:90:11: (o= uniOperator e= expression -> ^( OP_UNARY $o $e) )
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:90:13: o= uniOperator e= expression
            {
            pushFollow(FOLLOW_uniOperator_in_unaryExpr464);
            o=uniOperator();

            state._fsp--;

            stream_uniOperator.add(o.getTree());

            pushFollow(FOLLOW_expression_in_unaryExpr468);
            e=expression();

            state._fsp--;

            stream_expression.add(e.getTree());

            // AST REWRITE
            // elements: o, e
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
            // 90:43: -> ^( OP_UNARY $o $e)
            {
                // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:90:46: ^( OP_UNARY $o $e)
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
    // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:93:1: boolOperator : ( '&' | '|' );
    public final VcfFilterParser.boolOperator_return boolOperator() throws RecognitionException {
        VcfFilterParser.boolOperator_return retval = new VcfFilterParser.boolOperator_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token set12=null;

        Object set12_tree=null;

        try {
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:93:16: ( '&' | '|' )
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:
            {
            root_0 = (Object)adaptor.nil();


            set12=(Token)input.LT(1);

            if ( input.LA(1)==29||input.LA(1)==49 ) {
                input.consume();
                adaptor.addChild(root_0, 
                (Object)adaptor.create(set12)
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
    // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:94:1: binOperator : ( '=' | '>=' | '>' | '<=' | '<' | '!=' | '=~' | '!~' );
    public final VcfFilterParser.binOperator_return binOperator() throws RecognitionException {
        VcfFilterParser.binOperator_return retval = new VcfFilterParser.binOperator_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token set13=null;

        Object set13_tree=null;

        try {
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:94:15: ( '=' | '>=' | '>' | '<=' | '<' | '!=' | '=~' | '!~' )
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:
            {
            root_0 = (Object)adaptor.nil();


            set13=(Token)input.LT(1);

            if ( (input.LA(1) >= 27 && input.LA(1) <= 28)||(input.LA(1) >= 34 && input.LA(1) <= 39) ) {
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
    // $ANTLR end "binOperator"


    public static class uniOperator_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "uniOperator"
    // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:95:1: uniOperator : ( '!' | 'na' | 'exists' );
    public final VcfFilterParser.uniOperator_return uniOperator() throws RecognitionException {
        VcfFilterParser.uniOperator_return retval = new VcfFilterParser.uniOperator_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token set14=null;

        Object set14_tree=null;

        try {
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:95:15: ( '!' | 'na' | 'exists' )
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:
            {
            root_0 = (Object)adaptor.nil();


            set14=(Token)input.LT(1);

            if ( input.LA(1)==26||(input.LA(1) >= 47 && input.LA(1) <= 48) ) {
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
    // $ANTLR end "uniOperator"


    public static class expression_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "expression"
    // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:98:1: expression : ( var | function | literalFloat | literalString );
    public final VcfFilterParser.expression_return expression() throws RecognitionException {
        VcfFilterParser.expression_return retval = new VcfFilterParser.expression_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        VcfFilterParser.var_return var15 =null;

        VcfFilterParser.function_return function16 =null;

        VcfFilterParser.literalFloat_return literalFloat17 =null;

        VcfFilterParser.literalString_return literalString18 =null;



        try {
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:98:12: ( var | function | literalFloat | literalString )
            int alt5=4;
            switch ( input.LA(1) ) {
            case ID:
            case 40:
                {
                alt5=1;
                }
                break;
            case 43:
            case 44:
            case 45:
            case 46:
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
                    // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:98:14: var
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_var_in_expression569);
                    var15=var();

                    state._fsp--;

                    adaptor.addChild(root_0, var15.getTree());

                    }
                    break;
                case 2 :
                    // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:99:6: function
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_function_in_expression577);
                    function16=function();

                    state._fsp--;

                    adaptor.addChild(root_0, function16.getTree());

                    }
                    break;
                case 3 :
                    // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:100:6: literalFloat
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_literalFloat_in_expression585);
                    literalFloat17=literalFloat();

                    state._fsp--;

                    adaptor.addChild(root_0, literalFloat17.getTree());

                    }
                    break;
                case 4 :
                    // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:101:6: literalString
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_literalString_in_expression593);
                    literalString18=literalString();

                    state._fsp--;

                    adaptor.addChild(root_0, literalString18.getTree());

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
    // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:104:1: literalFloat : f= FLOAT -> ^( LITERAL_NUMBER $f) ;
    public final VcfFilterParser.literalFloat_return literalFloat() throws RecognitionException {
        VcfFilterParser.literalFloat_return retval = new VcfFilterParser.literalFloat_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token f=null;

        Object f_tree=null;
        RewriteRuleTokenStream stream_FLOAT=new RewriteRuleTokenStream(adaptor,"token FLOAT");

        try {
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:104:14: (f= FLOAT -> ^( LITERAL_NUMBER $f) )
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:104:16: f= FLOAT
            {
            f=(Token)match(input,FLOAT,FOLLOW_FLOAT_in_literalFloat604);  
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
            // 104:30: -> ^( LITERAL_NUMBER $f)
            {
                // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:104:33: ^( LITERAL_NUMBER $f)
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
    // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:105:1: literalString : s= STRING -> ^( LITERAL_STRING $s) ;
    public final VcfFilterParser.literalString_return literalString() throws RecognitionException {
        VcfFilterParser.literalString_return retval = new VcfFilterParser.literalString_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token s=null;

        Object s_tree=null;
        RewriteRuleTokenStream stream_STRING=new RewriteRuleTokenStream(adaptor,"token STRING");

        try {
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:105:15: (s= STRING -> ^( LITERAL_STRING $s) )
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:105:17: s= STRING
            {
            s=(Token)match(input,STRING,FOLLOW_STRING_in_literalString628);  
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
            // 105:31: -> ^( LITERAL_STRING $s)
            {
                // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:105:34: ^( LITERAL_STRING $s)
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
    // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:108:1: var : ( varField | varSubfield | varGenotype | varGenotypeSub );
    public final VcfFilterParser.var_return var() throws RecognitionException {
        VcfFilterParser.var_return retval = new VcfFilterParser.var_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        VcfFilterParser.varField_return varField19 =null;

        VcfFilterParser.varSubfield_return varSubfield20 =null;

        VcfFilterParser.varGenotype_return varGenotype21 =null;

        VcfFilterParser.varGenotypeSub_return varGenotypeSub22 =null;



        try {
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:108:7: ( varField | varSubfield | varGenotype | varGenotypeSub )
            int alt6=4;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==ID) ) {
                int LA6_1 = input.LA(2);

                if ( (LA6_1==41) ) {
                    alt6=2;
                }
                else if ( (LA6_1==EOF||(LA6_1 >= 27 && LA6_1 <= 29)||LA6_1==31||(LA6_1 >= 34 && LA6_1 <= 39)||LA6_1==49) ) {
                    alt6=1;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 6, 1, input);

                    throw nvae;

                }
            }
            else if ( (LA6_0==40) ) {
                int LA6_2 = input.LA(2);

                if ( (LA6_2==41) ) {
                    int LA6_5 = input.LA(3);

                    if ( (LA6_5==FLOAT||LA6_5==32) ) {
                        int LA6_6 = input.LA(4);

                        if ( (LA6_6==42) ) {
                            int LA6_7 = input.LA(5);

                            if ( (LA6_7==33) ) {
                                int LA6_8 = input.LA(6);

                                if ( (LA6_8==ID) ) {
                                    int LA6_9 = input.LA(7);

                                    if ( (LA6_9==41) ) {
                                        alt6=4;
                                    }
                                    else if ( (LA6_9==EOF||(LA6_9 >= 27 && LA6_9 <= 29)||LA6_9==31||(LA6_9 >= 34 && LA6_9 <= 39)||LA6_9==49) ) {
                                        alt6=3;
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
                            new NoViableAltException("", 6, 5, input);

                        throw nvae;

                    }
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 6, 2, input);

                    throw nvae;

                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 6, 0, input);

                throw nvae;

            }
            switch (alt6) {
                case 1 :
                    // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:108:9: varField
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_varField_in_var654);
                    varField19=varField();

                    state._fsp--;

                    adaptor.addChild(root_0, varField19.getTree());

                    }
                    break;
                case 2 :
                    // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:108:20: varSubfield
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_varSubfield_in_var658);
                    varSubfield20=varSubfield();

                    state._fsp--;

                    adaptor.addChild(root_0, varSubfield20.getTree());

                    }
                    break;
                case 3 :
                    // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:108:34: varGenotype
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_varGenotype_in_var662);
                    varGenotype21=varGenotype();

                    state._fsp--;

                    adaptor.addChild(root_0, varGenotype21.getTree());

                    }
                    break;
                case 4 :
                    // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:108:48: varGenotypeSub
                    {
                    root_0 = (Object)adaptor.nil();


                    pushFollow(FOLLOW_varGenotypeSub_in_var666);
                    varGenotypeSub22=varGenotypeSub();

                    state._fsp--;

                    adaptor.addChild(root_0, varGenotypeSub22.getTree());

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
    // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:109:1: varField : i= ID -> ^( VAR_FIELD $i) ;
    public final VcfFilterParser.varField_return varField() throws RecognitionException {
        VcfFilterParser.varField_return retval = new VcfFilterParser.varField_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token i=null;

        Object i_tree=null;
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");

        try {
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:109:10: (i= ID -> ^( VAR_FIELD $i) )
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:109:12: i= ID
            {
            i=(Token)match(input,ID,FOLLOW_ID_in_varField676);  
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
            // 109:23: -> ^( VAR_FIELD $i)
            {
                // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:109:26: ^( VAR_FIELD $i)
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
    // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:110:1: varSubfield : i= ID '[' n= index ']' -> ^( VAR_SUBFIELD $i $n) ;
    public final VcfFilterParser.varSubfield_return varSubfield() throws RecognitionException {
        VcfFilterParser.varSubfield_return retval = new VcfFilterParser.varSubfield_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token i=null;
        Token char_literal23=null;
        Token char_literal24=null;
        VcfFilterParser.index_return n =null;


        Object i_tree=null;
        Object char_literal23_tree=null;
        Object char_literal24_tree=null;
        RewriteRuleTokenStream stream_42=new RewriteRuleTokenStream(adaptor,"token 42");
        RewriteRuleTokenStream stream_41=new RewriteRuleTokenStream(adaptor,"token 41");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_index=new RewriteRuleSubtreeStream(adaptor,"rule index");
        try {
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:110:13: (i= ID '[' n= index ']' -> ^( VAR_SUBFIELD $i $n) )
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:110:15: i= ID '[' n= index ']'
            {
            i=(Token)match(input,ID,FOLLOW_ID_in_varSubfield700);  
            stream_ID.add(i);


            char_literal23=(Token)match(input,41,FOLLOW_41_in_varSubfield702);  
            stream_41.add(char_literal23);


            pushFollow(FOLLOW_index_in_varSubfield706);
            n=index();

            state._fsp--;

            stream_index.add(n.getTree());

            char_literal24=(Token)match(input,42,FOLLOW_42_in_varSubfield708);  
            stream_42.add(char_literal24);


            // AST REWRITE
            // elements: n, i
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
            // 110:40: -> ^( VAR_SUBFIELD $i $n)
            {
                // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:110:43: ^( VAR_SUBFIELD $i $n)
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
    // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:111:1: varGenotype : 'GEN' '[' g= index ']' '.' i= ID -> ^( VAR_GENOTYPE $g $i) ;
    public final VcfFilterParser.varGenotype_return varGenotype() throws RecognitionException {
        VcfFilterParser.varGenotype_return retval = new VcfFilterParser.varGenotype_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token i=null;
        Token string_literal25=null;
        Token char_literal26=null;
        Token char_literal27=null;
        Token char_literal28=null;
        VcfFilterParser.index_return g =null;


        Object i_tree=null;
        Object string_literal25_tree=null;
        Object char_literal26_tree=null;
        Object char_literal27_tree=null;
        Object char_literal28_tree=null;
        RewriteRuleTokenStream stream_42=new RewriteRuleTokenStream(adaptor,"token 42");
        RewriteRuleTokenStream stream_41=new RewriteRuleTokenStream(adaptor,"token 41");
        RewriteRuleTokenStream stream_40=new RewriteRuleTokenStream(adaptor,"token 40");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_33=new RewriteRuleTokenStream(adaptor,"token 33");
        RewriteRuleSubtreeStream stream_index=new RewriteRuleSubtreeStream(adaptor,"rule index");
        try {
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:111:13: ( 'GEN' '[' g= index ']' '.' i= ID -> ^( VAR_GENOTYPE $g $i) )
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:111:15: 'GEN' '[' g= index ']' '.' i= ID
            {
            string_literal25=(Token)match(input,40,FOLLOW_40_in_varGenotype731);  
            stream_40.add(string_literal25);


            char_literal26=(Token)match(input,41,FOLLOW_41_in_varGenotype733);  
            stream_41.add(char_literal26);


            pushFollow(FOLLOW_index_in_varGenotype737);
            g=index();

            state._fsp--;

            stream_index.add(g.getTree());

            char_literal27=(Token)match(input,42,FOLLOW_42_in_varGenotype739);  
            stream_42.add(char_literal27);


            char_literal28=(Token)match(input,33,FOLLOW_33_in_varGenotype741);  
            stream_33.add(char_literal28);


            i=(Token)match(input,ID,FOLLOW_ID_in_varGenotype745);  
            stream_ID.add(i);


            // AST REWRITE
            // elements: g, i
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
            // 111:49: -> ^( VAR_GENOTYPE $g $i)
            {
                // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:111:52: ^( VAR_GENOTYPE $g $i)
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(
                (Object)adaptor.create(VAR_GENOTYPE, "VAR_GENOTYPE")
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
    // $ANTLR end "varGenotype"


    public static class varGenotypeSub_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "varGenotypeSub"
    // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:112:1: varGenotypeSub : 'GEN' '[' g= index ']' '.' i= ID '[' n= FLOAT ']' -> ^( VAR_GENOTYPE_SUB $g $i $n) ;
    public final VcfFilterParser.varGenotypeSub_return varGenotypeSub() throws RecognitionException {
        VcfFilterParser.varGenotypeSub_return retval = new VcfFilterParser.varGenotypeSub_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token i=null;
        Token n=null;
        Token string_literal29=null;
        Token char_literal30=null;
        Token char_literal31=null;
        Token char_literal32=null;
        Token char_literal33=null;
        Token char_literal34=null;
        VcfFilterParser.index_return g =null;


        Object i_tree=null;
        Object n_tree=null;
        Object string_literal29_tree=null;
        Object char_literal30_tree=null;
        Object char_literal31_tree=null;
        Object char_literal32_tree=null;
        Object char_literal33_tree=null;
        Object char_literal34_tree=null;
        RewriteRuleTokenStream stream_FLOAT=new RewriteRuleTokenStream(adaptor,"token FLOAT");
        RewriteRuleTokenStream stream_42=new RewriteRuleTokenStream(adaptor,"token 42");
        RewriteRuleTokenStream stream_41=new RewriteRuleTokenStream(adaptor,"token 41");
        RewriteRuleTokenStream stream_40=new RewriteRuleTokenStream(adaptor,"token 40");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_33=new RewriteRuleTokenStream(adaptor,"token 33");
        RewriteRuleSubtreeStream stream_index=new RewriteRuleSubtreeStream(adaptor,"rule index");
        try {
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:112:16: ( 'GEN' '[' g= index ']' '.' i= ID '[' n= FLOAT ']' -> ^( VAR_GENOTYPE_SUB $g $i $n) )
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:112:18: 'GEN' '[' g= index ']' '.' i= ID '[' n= FLOAT ']'
            {
            string_literal29=(Token)match(input,40,FOLLOW_40_in_varGenotypeSub767);  
            stream_40.add(string_literal29);


            char_literal30=(Token)match(input,41,FOLLOW_41_in_varGenotypeSub769);  
            stream_41.add(char_literal30);


            pushFollow(FOLLOW_index_in_varGenotypeSub773);
            g=index();

            state._fsp--;

            stream_index.add(g.getTree());

            char_literal31=(Token)match(input,42,FOLLOW_42_in_varGenotypeSub775);  
            stream_42.add(char_literal31);


            char_literal32=(Token)match(input,33,FOLLOW_33_in_varGenotypeSub777);  
            stream_33.add(char_literal32);


            i=(Token)match(input,ID,FOLLOW_ID_in_varGenotypeSub781);  
            stream_ID.add(i);


            char_literal33=(Token)match(input,41,FOLLOW_41_in_varGenotypeSub784);  
            stream_41.add(char_literal33);


            n=(Token)match(input,FLOAT,FOLLOW_FLOAT_in_varGenotypeSub788);  
            stream_FLOAT.add(n);


            char_literal34=(Token)match(input,42,FOLLOW_42_in_varGenotypeSub790);  
            stream_42.add(char_literal34);


            // AST REWRITE
            // elements: i, g, n
            // token labels: n, i
            // rule labels: g, retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleTokenStream stream_n=new RewriteRuleTokenStream(adaptor,"token n",n);
            RewriteRuleTokenStream stream_i=new RewriteRuleTokenStream(adaptor,"token i",i);
            RewriteRuleSubtreeStream stream_g=new RewriteRuleSubtreeStream(adaptor,"rule g",g!=null?g.tree:null);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 112:67: -> ^( VAR_GENOTYPE_SUB $g $i $n)
            {
                // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:112:70: ^( VAR_GENOTYPE_SUB $g $i $n)
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(
                (Object)adaptor.create(VAR_GENOTYPE_SUB, "VAR_GENOTYPE_SUB")
                , root_1);

                adaptor.addChild(root_1, stream_g.nextTree());

                adaptor.addChild(root_1, stream_i.nextNode());

                adaptor.addChild(root_1, stream_n.nextNode());

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


    public static class function_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "function"
    // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:115:1: function : f= functionName '(' ')' -> ^( FUNCTION $f) ;
    public final VcfFilterParser.function_return function() throws RecognitionException {
        VcfFilterParser.function_return retval = new VcfFilterParser.function_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token char_literal35=null;
        Token char_literal36=null;
        VcfFilterParser.functionName_return f =null;


        Object char_literal35_tree=null;
        Object char_literal36_tree=null;
        RewriteRuleTokenStream stream_30=new RewriteRuleTokenStream(adaptor,"token 30");
        RewriteRuleTokenStream stream_31=new RewriteRuleTokenStream(adaptor,"token 31");
        RewriteRuleSubtreeStream stream_functionName=new RewriteRuleSubtreeStream(adaptor,"rule functionName");
        try {
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:115:10: (f= functionName '(' ')' -> ^( FUNCTION $f) )
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:115:12: f= functionName '(' ')'
            {
            pushFollow(FOLLOW_functionName_in_function817);
            f=functionName();

            state._fsp--;

            stream_functionName.add(f.getTree());

            char_literal35=(Token)match(input,30,FOLLOW_30_in_function819);  
            stream_30.add(char_literal35);


            char_literal36=(Token)match(input,31,FOLLOW_31_in_function821);  
            stream_31.add(char_literal36);


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
            // 115:36: -> ^( FUNCTION $f)
            {
                // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:115:39: ^( FUNCTION $f)
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(
                (Object)adaptor.create(FUNCTION, "FUNCTION")
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
    // $ANTLR end "function"


    public static class functionName_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "functionName"
    // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:116:1: functionName : ( 'countHom' | 'countHet' | 'countVariant' | 'countRef' );
    public final VcfFilterParser.functionName_return functionName() throws RecognitionException {
        VcfFilterParser.functionName_return retval = new VcfFilterParser.functionName_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token set37=null;

        Object set37_tree=null;

        try {
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:116:14: ( 'countHom' | 'countHet' | 'countVariant' | 'countRef' )
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:
            {
            root_0 = (Object)adaptor.nil();


            set37=(Token)input.LT(1);

            if ( (input.LA(1) >= 43 && input.LA(1) <= 46) ) {
                input.consume();
                adaptor.addChild(root_0, 
                (Object)adaptor.create(set37)
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
    // $ANTLR end "functionName"


    public static class index_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "index"
    // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:119:1: index : ( FLOAT | '*' );
    public final VcfFilterParser.index_return index() throws RecognitionException {
        VcfFilterParser.index_return retval = new VcfFilterParser.index_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token set38=null;

        Object set38_tree=null;

        try {
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:119:9: ( FLOAT | '*' )
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:
            {
            root_0 = (Object)adaptor.nil();


            set38=(Token)input.LT(1);

            if ( input.LA(1)==FLOAT||input.LA(1)==32 ) {
                input.consume();
                adaptor.addChild(root_0, 
                (Object)adaptor.create(set38)
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


 

    public static final BitSet FOLLOW_condition_in_main348 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_subcondition_in_condition365 = new BitSet(new long[]{0x0002000020000002L});
    public static final BitSet FOLLOW_boolOperator_in_condition368 = new BitSet(new long[]{0x0001F90044080500L});
    public static final BitSet FOLLOW_subcondition_in_condition371 = new BitSet(new long[]{0x0002000020000002L});
    public static final BitSet FOLLOW_26_in_subcondition381 = new BitSet(new long[]{0x0001F90044080500L});
    public static final BitSet FOLLOW_bare_in_subcondition387 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_paren_in_subcondition391 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_binaryExpr_in_bare400 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_unaryExpr_in_bare404 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_30_in_paren413 = new BitSet(new long[]{0x0001F90044080500L});
    public static final BitSet FOLLOW_condition_in_paren416 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_31_in_paren418 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_binaryExpr430 = new BitSet(new long[]{0x000000FC18000000L});
    public static final BitSet FOLLOW_binOperator_in_binaryExpr434 = new BitSet(new long[]{0x0000790000080500L});
    public static final BitSet FOLLOW_expression_in_binaryExpr438 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_uniOperator_in_unaryExpr464 = new BitSet(new long[]{0x0000790000080500L});
    public static final BitSet FOLLOW_expression_in_unaryExpr468 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_var_in_expression569 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_function_in_expression577 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_literalFloat_in_expression585 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_literalString_in_expression593 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FLOAT_in_literalFloat604 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_in_literalString628 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_varField_in_var654 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_varSubfield_in_var658 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_varGenotype_in_var662 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_varGenotypeSub_in_var666 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_varField676 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_varSubfield700 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_41_in_varSubfield702 = new BitSet(new long[]{0x0000000100000100L});
    public static final BitSet FOLLOW_index_in_varSubfield706 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_42_in_varSubfield708 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_40_in_varGenotype731 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_41_in_varGenotype733 = new BitSet(new long[]{0x0000000100000100L});
    public static final BitSet FOLLOW_index_in_varGenotype737 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_42_in_varGenotype739 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_33_in_varGenotype741 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_ID_in_varGenotype745 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_40_in_varGenotypeSub767 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_41_in_varGenotypeSub769 = new BitSet(new long[]{0x0000000100000100L});
    public static final BitSet FOLLOW_index_in_varGenotypeSub773 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_42_in_varGenotypeSub775 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_33_in_varGenotypeSub777 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_ID_in_varGenotypeSub781 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_41_in_varGenotypeSub784 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_FLOAT_in_varGenotypeSub788 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_42_in_varGenotypeSub790 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_functionName_in_function817 = new BitSet(new long[]{0x0000000040000000L});
    public static final BitSet FOLLOW_30_in_function819 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_31_in_function821 = new BitSet(new long[]{0x0000000000000002L});

}