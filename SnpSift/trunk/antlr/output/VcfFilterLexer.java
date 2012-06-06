// $ANTLR 3.4 /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g 2011-08-26 12:10:28

package ca.mcgill.mcb.pcingola.vcfEtc.antlr;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked"})
public class VcfFilterLexer extends Lexer {
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
    // delegators
    public Lexer[] getDelegates() {
        return new Lexer[] {};
    }

    public VcfFilterLexer() {} 
    public VcfFilterLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public VcfFilterLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);
    }
    public String getGrammarFileName() { return "/Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g"; }

    // $ANTLR start "T__26"
    public final void mT__26() throws RecognitionException {
        try {
            int _type = T__26;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:6:7: ( '!' )
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:6:9: '!'
            {
            match('!'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__26"

    // $ANTLR start "T__27"
    public final void mT__27() throws RecognitionException {
        try {
            int _type = T__27;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:7:7: ( '!=' )
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:7:9: '!='
            {
            match("!="); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__27"

    // $ANTLR start "T__28"
    public final void mT__28() throws RecognitionException {
        try {
            int _type = T__28;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:8:7: ( '!~' )
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:8:9: '!~'
            {
            match("!~"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__28"

    // $ANTLR start "T__29"
    public final void mT__29() throws RecognitionException {
        try {
            int _type = T__29;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:9:7: ( '&' )
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:9:9: '&'
            {
            match('&'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__29"

    // $ANTLR start "T__30"
    public final void mT__30() throws RecognitionException {
        try {
            int _type = T__30;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:10:7: ( '(' )
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:10:9: '('
            {
            match('('); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__30"

    // $ANTLR start "T__31"
    public final void mT__31() throws RecognitionException {
        try {
            int _type = T__31;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:11:7: ( ')' )
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:11:9: ')'
            {
            match(')'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__31"

    // $ANTLR start "T__32"
    public final void mT__32() throws RecognitionException {
        try {
            int _type = T__32;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:12:7: ( '*' )
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:12:9: '*'
            {
            match('*'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__32"

    // $ANTLR start "T__33"
    public final void mT__33() throws RecognitionException {
        try {
            int _type = T__33;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:13:7: ( '.' )
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:13:9: '.'
            {
            match('.'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__33"

    // $ANTLR start "T__34"
    public final void mT__34() throws RecognitionException {
        try {
            int _type = T__34;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:14:7: ( '<' )
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:14:9: '<'
            {
            match('<'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__34"

    // $ANTLR start "T__35"
    public final void mT__35() throws RecognitionException {
        try {
            int _type = T__35;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:15:7: ( '<=' )
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:15:9: '<='
            {
            match("<="); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__35"

    // $ANTLR start "T__36"
    public final void mT__36() throws RecognitionException {
        try {
            int _type = T__36;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:16:7: ( '=' )
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:16:9: '='
            {
            match('='); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__36"

    // $ANTLR start "T__37"
    public final void mT__37() throws RecognitionException {
        try {
            int _type = T__37;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:17:7: ( '=~' )
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:17:9: '=~'
            {
            match("=~"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__37"

    // $ANTLR start "T__38"
    public final void mT__38() throws RecognitionException {
        try {
            int _type = T__38;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:18:7: ( '>' )
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:18:9: '>'
            {
            match('>'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__38"

    // $ANTLR start "T__39"
    public final void mT__39() throws RecognitionException {
        try {
            int _type = T__39;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:19:7: ( '>=' )
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:19:9: '>='
            {
            match(">="); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__39"

    // $ANTLR start "T__40"
    public final void mT__40() throws RecognitionException {
        try {
            int _type = T__40;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:20:7: ( 'GEN' )
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:20:9: 'GEN'
            {
            match("GEN"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__40"

    // $ANTLR start "T__41"
    public final void mT__41() throws RecognitionException {
        try {
            int _type = T__41;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:21:7: ( '[' )
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:21:9: '['
            {
            match('['); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__41"

    // $ANTLR start "T__42"
    public final void mT__42() throws RecognitionException {
        try {
            int _type = T__42;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:22:7: ( ']' )
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:22:9: ']'
            {
            match(']'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__42"

    // $ANTLR start "T__43"
    public final void mT__43() throws RecognitionException {
        try {
            int _type = T__43;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:23:7: ( 'countHet' )
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:23:9: 'countHet'
            {
            match("countHet"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__43"

    // $ANTLR start "T__44"
    public final void mT__44() throws RecognitionException {
        try {
            int _type = T__44;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:24:7: ( 'countHom' )
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:24:9: 'countHom'
            {
            match("countHom"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__44"

    // $ANTLR start "T__45"
    public final void mT__45() throws RecognitionException {
        try {
            int _type = T__45;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:25:7: ( 'countRef' )
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:25:9: 'countRef'
            {
            match("countRef"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__45"

    // $ANTLR start "T__46"
    public final void mT__46() throws RecognitionException {
        try {
            int _type = T__46;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:26:7: ( 'countVariant' )
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:26:9: 'countVariant'
            {
            match("countVariant"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__46"

    // $ANTLR start "T__47"
    public final void mT__47() throws RecognitionException {
        try {
            int _type = T__47;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:27:7: ( 'exists' )
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:27:9: 'exists'
            {
            match("exists"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__47"

    // $ANTLR start "T__48"
    public final void mT__48() throws RecognitionException {
        try {
            int _type = T__48;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:28:7: ( 'na' )
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:28:9: 'na'
            {
            match("na"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__48"

    // $ANTLR start "T__49"
    public final void mT__49() throws RecognitionException {
        try {
            int _type = T__49;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:29:7: ( '|' )
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:29:9: '|'
            {
            match('|'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__49"

    // $ANTLR start "WS"
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:42:5: ( ( ' ' | '\\t' )+ )
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:42:7: ( ' ' | '\\t' )+
            {
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:42:7: ( ' ' | '\\t' )+
            int cnt1=0;
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0=='\t'||LA1_0==' ') ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:
            	    {
            	    if ( input.LA(1)=='\t'||input.LA(1)==' ' ) {
            	        input.consume();
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt1 >= 1 ) break loop1;
                        EarlyExitException eee =
                            new EarlyExitException(1, input);
                        throw eee;
                }
                cnt1++;
            } while (true);


             _channel = HIDDEN; 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "WS"

    // $ANTLR start "NEWLINE"
    public final void mNEWLINE() throws RecognitionException {
        try {
            int _type = NEWLINE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:47:10: ( ( ( '\\r' )? '\\n' )+ )
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:47:12: ( ( '\\r' )? '\\n' )+
            {
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:47:12: ( ( '\\r' )? '\\n' )+
            int cnt3=0;
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( (LA3_0=='\n'||LA3_0=='\r') ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:47:13: ( '\\r' )? '\\n'
            	    {
            	    // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:47:13: ( '\\r' )?
            	    int alt2=2;
            	    int LA2_0 = input.LA(1);

            	    if ( (LA2_0=='\r') ) {
            	        alt2=1;
            	    }
            	    switch (alt2) {
            	        case 1 :
            	            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:47:13: '\\r'
            	            {
            	            match('\r'); 

            	            }
            	            break;

            	    }


            	    match('\n'); 

            	    }
            	    break;

            	default :
            	    if ( cnt3 >= 1 ) break loop3;
                        EarlyExitException eee =
                            new EarlyExitException(3, input);
                        throw eee;
                }
                cnt3++;
            } while (true);


             _channel=HIDDEN; 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "NEWLINE"

    // $ANTLR start "NUMBER"
    public final void mNUMBER() throws RecognitionException {
        try {
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:51:17: ( ( DIGIT )+ )
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:51:19: ( DIGIT )+
            {
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:51:19: ( DIGIT )+
            int cnt4=0;
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( ((LA4_0 >= '0' && LA4_0 <= '9')) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:
            	    {
            	    if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
            	        input.consume();
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt4 >= 1 ) break loop4;
                        EarlyExitException eee =
                            new EarlyExitException(4, input);
                        throw eee;
                }
                cnt4++;
            } while (true);


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "NUMBER"

    // $ANTLR start "DIGIT"
    public final void mDIGIT() throws RecognitionException {
        try {
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:54:16: ( '0' .. '9' )
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:
            {
            if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "DIGIT"

    // $ANTLR start "LETTER"
    public final void mLETTER() throws RecognitionException {
        try {
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:57:17: ( LOWER | UPPER )
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:
            {
            if ( (input.LA(1) >= 'A' && input.LA(1) <= 'Z')||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "LETTER"

    // $ANTLR start "LOWER"
    public final void mLOWER() throws RecognitionException {
        try {
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:58:16: ( 'a' .. 'z' )
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:
            {
            if ( (input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "LOWER"

    // $ANTLR start "UPPER"
    public final void mUPPER() throws RecognitionException {
        try {
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:59:16: ( 'A' .. 'Z' )
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:
            {
            if ( (input.LA(1) >= 'A' && input.LA(1) <= 'Z') ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "UPPER"

    // $ANTLR start "ALPHANUM"
    public final void mALPHANUM() throws RecognitionException {
        try {
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:62:20: ( LETTER | DIGIT )
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:
            {
            if ( (input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'Z')||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "ALPHANUM"

    // $ANTLR start "COMMENT_SL"
    public final void mCOMMENT_SL() throws RecognitionException {
        try {
            int _type = COMMENT_SL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:65:12: ( '//' (~ ( '\\r' | '\\n' ) )* NEWLINE )
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:65:14: '//' (~ ( '\\r' | '\\n' ) )* NEWLINE
            {
            match("//"); 



            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:65:19: (~ ( '\\r' | '\\n' ) )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( ((LA5_0 >= '\u0000' && LA5_0 <= '\t')||(LA5_0 >= '\u000B' && LA5_0 <= '\f')||(LA5_0 >= '\u000E' && LA5_0 <= '\uFFFF')) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:
            	    {
            	    if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '\t')||(input.LA(1) >= '\u000B' && input.LA(1) <= '\f')||(input.LA(1) >= '\u000E' && input.LA(1) <= '\uFFFF') ) {
            	        input.consume();
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    break loop5;
                }
            } while (true);


            mNEWLINE(); 


             _channel=HIDDEN; 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "COMMENT_SL"

    // $ANTLR start "FLOAT"
    public final void mFLOAT() throws RecognitionException {
        try {
            int _type = FLOAT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:68:8: ( ( '+' | '-' )? NUMBER ( '.' NUMBER )? ( ( 'e' | 'E' ) ( '+' | '-' )? NUMBER )? )
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:68:12: ( '+' | '-' )? NUMBER ( '.' NUMBER )? ( ( 'e' | 'E' ) ( '+' | '-' )? NUMBER )?
            {
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:68:12: ( '+' | '-' )?
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0=='+'||LA6_0=='-') ) {
                alt6=1;
            }
            switch (alt6) {
                case 1 :
                    // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:
                    {
                    if ( input.LA(1)=='+'||input.LA(1)=='-' ) {
                        input.consume();
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;
                    }


                    }
                    break;

            }


            mNUMBER(); 


            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:68:30: ( '.' NUMBER )?
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0=='.') ) {
                alt7=1;
            }
            switch (alt7) {
                case 1 :
                    // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:68:32: '.' NUMBER
                    {
                    match('.'); 

                    mNUMBER(); 


                    }
                    break;

            }


            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:68:46: ( ( 'e' | 'E' ) ( '+' | '-' )? NUMBER )?
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0=='E'||LA9_0=='e') ) {
                alt9=1;
            }
            switch (alt9) {
                case 1 :
                    // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:68:47: ( 'e' | 'E' ) ( '+' | '-' )? NUMBER
                    {
                    if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                        input.consume();
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;
                    }


                    // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:68:57: ( '+' | '-' )?
                    int alt8=2;
                    int LA8_0 = input.LA(1);

                    if ( (LA8_0=='+'||LA8_0=='-') ) {
                        alt8=1;
                    }
                    switch (alt8) {
                        case 1 :
                            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:
                            {
                            if ( input.LA(1)=='+'||input.LA(1)=='-' ) {
                                input.consume();
                            }
                            else {
                                MismatchedSetException mse = new MismatchedSetException(null,input);
                                recover(mse);
                                throw mse;
                            }


                            }
                            break;

                    }


                    mNUMBER(); 


                    }
                    break;

            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "FLOAT"

    // $ANTLR start "STRING"
    public final void mSTRING() throws RecognitionException {
        try {
            int _type = STRING;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:71:7: ( '\\'' (~ ( '\\n' | '\\r' | '\\'' ) )* '\\'' )
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:71:9: '\\'' (~ ( '\\n' | '\\r' | '\\'' ) )* '\\''
            {
            match('\''); 

            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:71:14: (~ ( '\\n' | '\\r' | '\\'' ) )*
            loop10:
            do {
                int alt10=2;
                int LA10_0 = input.LA(1);

                if ( ((LA10_0 >= '\u0000' && LA10_0 <= '\t')||(LA10_0 >= '\u000B' && LA10_0 <= '\f')||(LA10_0 >= '\u000E' && LA10_0 <= '&')||(LA10_0 >= '(' && LA10_0 <= '\uFFFF')) ) {
                    alt10=1;
                }


                switch (alt10) {
            	case 1 :
            	    // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:
            	    {
            	    if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '\t')||(input.LA(1) >= '\u000B' && input.LA(1) <= '\f')||(input.LA(1) >= '\u000E' && input.LA(1) <= '&')||(input.LA(1) >= '(' && input.LA(1) <= '\uFFFF') ) {
            	        input.consume();
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    break loop10;
                }
            } while (true);


            match('\''); 

             setText(getText().substring( 1, getText().length()-1 ) ); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "STRING"

    // $ANTLR start "ID"
    public final void mID() throws RecognitionException {
        try {
            int _type = ID;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:74:4: ( LETTER ( ALPHANUM | '_' )* )
            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:74:6: LETTER ( ALPHANUM | '_' )*
            {
            mLETTER(); 


            // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:74:13: ( ALPHANUM | '_' )*
            loop11:
            do {
                int alt11=2;
                int LA11_0 = input.LA(1);

                if ( ((LA11_0 >= '0' && LA11_0 <= '9')||(LA11_0 >= 'A' && LA11_0 <= 'Z')||LA11_0=='_'||(LA11_0 >= 'a' && LA11_0 <= 'z')) ) {
                    alt11=1;
                }


                switch (alt11) {
            	case 1 :
            	    // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:
            	    {
            	    if ( (input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
            	        input.consume();
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    break loop11;
                }
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "ID"

    public void mTokens() throws RecognitionException {
        // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:1:8: ( T__26 | T__27 | T__28 | T__29 | T__30 | T__31 | T__32 | T__33 | T__34 | T__35 | T__36 | T__37 | T__38 | T__39 | T__40 | T__41 | T__42 | T__43 | T__44 | T__45 | T__46 | T__47 | T__48 | T__49 | WS | NEWLINE | COMMENT_SL | FLOAT | STRING | ID )
        int alt12=30;
        alt12 = dfa12.predict(input);
        switch (alt12) {
            case 1 :
                // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:1:10: T__26
                {
                mT__26(); 


                }
                break;
            case 2 :
                // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:1:16: T__27
                {
                mT__27(); 


                }
                break;
            case 3 :
                // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:1:22: T__28
                {
                mT__28(); 


                }
                break;
            case 4 :
                // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:1:28: T__29
                {
                mT__29(); 


                }
                break;
            case 5 :
                // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:1:34: T__30
                {
                mT__30(); 


                }
                break;
            case 6 :
                // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:1:40: T__31
                {
                mT__31(); 


                }
                break;
            case 7 :
                // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:1:46: T__32
                {
                mT__32(); 


                }
                break;
            case 8 :
                // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:1:52: T__33
                {
                mT__33(); 


                }
                break;
            case 9 :
                // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:1:58: T__34
                {
                mT__34(); 


                }
                break;
            case 10 :
                // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:1:64: T__35
                {
                mT__35(); 


                }
                break;
            case 11 :
                // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:1:70: T__36
                {
                mT__36(); 


                }
                break;
            case 12 :
                // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:1:76: T__37
                {
                mT__37(); 


                }
                break;
            case 13 :
                // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:1:82: T__38
                {
                mT__38(); 


                }
                break;
            case 14 :
                // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:1:88: T__39
                {
                mT__39(); 


                }
                break;
            case 15 :
                // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:1:94: T__40
                {
                mT__40(); 


                }
                break;
            case 16 :
                // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:1:100: T__41
                {
                mT__41(); 


                }
                break;
            case 17 :
                // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:1:106: T__42
                {
                mT__42(); 


                }
                break;
            case 18 :
                // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:1:112: T__43
                {
                mT__43(); 


                }
                break;
            case 19 :
                // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:1:118: T__44
                {
                mT__44(); 


                }
                break;
            case 20 :
                // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:1:124: T__45
                {
                mT__45(); 


                }
                break;
            case 21 :
                // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:1:130: T__46
                {
                mT__46(); 


                }
                break;
            case 22 :
                // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:1:136: T__47
                {
                mT__47(); 


                }
                break;
            case 23 :
                // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:1:142: T__48
                {
                mT__48(); 


                }
                break;
            case 24 :
                // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:1:148: T__49
                {
                mT__49(); 


                }
                break;
            case 25 :
                // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:1:154: WS
                {
                mWS(); 


                }
                break;
            case 26 :
                // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:1:157: NEWLINE
                {
                mNEWLINE(); 


                }
                break;
            case 27 :
                // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:1:165: COMMENT_SL
                {
                mCOMMENT_SL(); 


                }
                break;
            case 28 :
                // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:1:176: FLOAT
                {
                mFLOAT(); 


                }
                break;
            case 29 :
                // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:1:182: STRING
                {
                mSTRING(); 


                }
                break;
            case 30 :
                // /Users/pablocingolani/workspace/VcfEtc/antlr/VcfFilter.g:1:189: ID
                {
                mID(); 


                }
                break;

        }

    }


    protected DFA12 dfa12 = new DFA12(this);
    static final String DFA12_eotS =
        "\1\uffff\1\31\5\uffff\1\33\1\35\1\37\1\26\2\uffff\3\26\20\uffff"+
        "\3\26\1\47\1\50\2\26\2\uffff\7\26\1\65\4\26\1\uffff\1\72\1\73\1"+
        "\74\1\26\3\uffff\3\26\1\101\1\uffff";
    static final String DFA12_eofS =
        "\102\uffff";
    static final String DFA12_minS =
        "\1\11\1\75\5\uffff\1\75\1\176\1\75\1\105\2\uffff\1\157\1\170\1\141"+
        "\20\uffff\1\116\1\165\1\151\2\60\1\156\1\163\2\uffff\2\164\1\110"+
        "\1\163\2\145\1\141\1\60\1\164\1\155\1\146\1\162\1\uffff\3\60\1\151"+
        "\3\uffff\1\141\1\156\1\164\1\60\1\uffff";
    static final String DFA12_maxS =
        "\1\174\1\176\5\uffff\1\75\1\176\1\75\1\105\2\uffff\1\157\1\170\1"+
        "\141\20\uffff\1\116\1\165\1\151\2\172\1\156\1\163\2\uffff\2\164"+
        "\1\126\1\163\1\157\1\145\1\141\1\172\1\164\1\155\1\146\1\162\1\uffff"+
        "\3\172\1\151\3\uffff\1\141\1\156\1\164\1\172\1\uffff";
    static final String DFA12_acceptS =
        "\2\uffff\1\4\1\5\1\6\1\7\1\10\4\uffff\1\20\1\21\3\uffff\1\30\1\31"+
        "\1\32\1\33\1\34\1\35\1\36\1\2\1\3\1\1\1\12\1\11\1\14\1\13\1\16\1"+
        "\15\7\uffff\1\27\1\17\14\uffff\1\26\4\uffff\1\22\1\23\1\24\4\uffff"+
        "\1\25";
    static final String DFA12_specialS =
        "\102\uffff}>";
    static final String[] DFA12_transitionS = {
            "\1\21\1\22\2\uffff\1\22\22\uffff\1\21\1\1\4\uffff\1\2\1\25\1"+
            "\3\1\4\1\5\1\24\1\uffff\1\24\1\6\1\23\12\24\2\uffff\1\7\1\10"+
            "\1\11\2\uffff\6\26\1\12\23\26\1\13\1\uffff\1\14\3\uffff\2\26"+
            "\1\15\1\26\1\16\10\26\1\17\14\26\1\uffff\1\20",
            "\1\27\100\uffff\1\30",
            "",
            "",
            "",
            "",
            "",
            "\1\32",
            "\1\34",
            "\1\36",
            "\1\40",
            "",
            "",
            "\1\41",
            "\1\42",
            "\1\43",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\44",
            "\1\45",
            "\1\46",
            "\12\26\7\uffff\32\26\4\uffff\1\26\1\uffff\32\26",
            "\12\26\7\uffff\32\26\4\uffff\1\26\1\uffff\32\26",
            "\1\51",
            "\1\52",
            "",
            "",
            "\1\53",
            "\1\54",
            "\1\55\11\uffff\1\56\3\uffff\1\57",
            "\1\60",
            "\1\61\11\uffff\1\62",
            "\1\63",
            "\1\64",
            "\12\26\7\uffff\32\26\4\uffff\1\26\1\uffff\32\26",
            "\1\66",
            "\1\67",
            "\1\70",
            "\1\71",
            "",
            "\12\26\7\uffff\32\26\4\uffff\1\26\1\uffff\32\26",
            "\12\26\7\uffff\32\26\4\uffff\1\26\1\uffff\32\26",
            "\12\26\7\uffff\32\26\4\uffff\1\26\1\uffff\32\26",
            "\1\75",
            "",
            "",
            "",
            "\1\76",
            "\1\77",
            "\1\100",
            "\12\26\7\uffff\32\26\4\uffff\1\26\1\uffff\32\26",
            ""
    };

    static final short[] DFA12_eot = DFA.unpackEncodedString(DFA12_eotS);
    static final short[] DFA12_eof = DFA.unpackEncodedString(DFA12_eofS);
    static final char[] DFA12_min = DFA.unpackEncodedStringToUnsignedChars(DFA12_minS);
    static final char[] DFA12_max = DFA.unpackEncodedStringToUnsignedChars(DFA12_maxS);
    static final short[] DFA12_accept = DFA.unpackEncodedString(DFA12_acceptS);
    static final short[] DFA12_special = DFA.unpackEncodedString(DFA12_specialS);
    static final short[][] DFA12_transition;

    static {
        int numStates = DFA12_transitionS.length;
        DFA12_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA12_transition[i] = DFA.unpackEncodedString(DFA12_transitionS[i]);
        }
    }

    class DFA12 extends DFA {

        public DFA12(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 12;
            this.eot = DFA12_eot;
            this.eof = DFA12_eof;
            this.min = DFA12_min;
            this.max = DFA12_max;
            this.accept = DFA12_accept;
            this.special = DFA12_special;
            this.transition = DFA12_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( T__26 | T__27 | T__28 | T__29 | T__30 | T__31 | T__32 | T__33 | T__34 | T__35 | T__36 | T__37 | T__38 | T__39 | T__40 | T__41 | T__42 | T__43 | T__44 | T__45 | T__46 | T__47 | T__48 | T__49 | WS | NEWLINE | COMMENT_SL | FLOAT | STRING | ID );";
        }
    }
 

}