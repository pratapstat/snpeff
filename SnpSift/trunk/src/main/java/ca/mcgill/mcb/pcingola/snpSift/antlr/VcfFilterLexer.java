// $ANTLR 3.4 /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g 2012-06-06 15:10:52

package ca.mcgill.mcb.pcingola.snpSift.antlr;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked"})
public class VcfFilterLexer extends Lexer {
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
    public static final int T__61=61;
    public static final int T__62=62;
    public static final int T__63=63;
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
    public String getGrammarFileName() { return "/Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g"; }

    // $ANTLR start "T__30"
    public final void mT__30() throws RecognitionException {
        try {
            int _type = T__30;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:6:7: ( '!' )
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:6:9: '!'
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
    // $ANTLR end "T__30"

    // $ANTLR start "T__31"
    public final void mT__31() throws RecognitionException {
        try {
            int _type = T__31;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:7:7: ( '!=' )
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:7:9: '!='
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
    // $ANTLR end "T__31"

    // $ANTLR start "T__32"
    public final void mT__32() throws RecognitionException {
        try {
            int _type = T__32;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:8:7: ( '!~' )
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:8:9: '!~'
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
    // $ANTLR end "T__32"

    // $ANTLR start "T__33"
    public final void mT__33() throws RecognitionException {
        try {
            int _type = T__33;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:9:7: ( '&' )
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:9:9: '&'
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
    // $ANTLR end "T__33"

    // $ANTLR start "T__34"
    public final void mT__34() throws RecognitionException {
        try {
            int _type = T__34;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:10:7: ( '(' )
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:10:9: '('
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
    // $ANTLR end "T__34"

    // $ANTLR start "T__35"
    public final void mT__35() throws RecognitionException {
        try {
            int _type = T__35;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:11:7: ( ')' )
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:11:9: ')'
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
    // $ANTLR end "T__35"

    // $ANTLR start "T__36"
    public final void mT__36() throws RecognitionException {
        try {
            int _type = T__36;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:12:7: ( '*' )
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:12:9: '*'
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
    // $ANTLR end "T__36"

    // $ANTLR start "T__37"
    public final void mT__37() throws RecognitionException {
        try {
            int _type = T__37;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:13:7: ( '.' )
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:13:9: '.'
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
    // $ANTLR end "T__37"

    // $ANTLR start "T__38"
    public final void mT__38() throws RecognitionException {
        try {
            int _type = T__38;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:14:7: ( '<' )
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:14:9: '<'
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
    // $ANTLR end "T__38"

    // $ANTLR start "T__39"
    public final void mT__39() throws RecognitionException {
        try {
            int _type = T__39;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:15:7: ( '<=' )
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:15:9: '<='
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
    // $ANTLR end "T__39"

    // $ANTLR start "T__40"
    public final void mT__40() throws RecognitionException {
        try {
            int _type = T__40;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:16:7: ( '=' )
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:16:9: '='
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
    // $ANTLR end "T__40"

    // $ANTLR start "T__41"
    public final void mT__41() throws RecognitionException {
        try {
            int _type = T__41;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:17:7: ( '=~' )
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:17:9: '=~'
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
    // $ANTLR end "T__41"

    // $ANTLR start "T__42"
    public final void mT__42() throws RecognitionException {
        try {
            int _type = T__42;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:18:7: ( '>' )
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:18:9: '>'
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
    // $ANTLR end "T__42"

    // $ANTLR start "T__43"
    public final void mT__43() throws RecognitionException {
        try {
            int _type = T__43;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:19:7: ( '>=' )
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:19:9: '>='
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
    // $ANTLR end "T__43"

    // $ANTLR start "T__44"
    public final void mT__44() throws RecognitionException {
        try {
            int _type = T__44;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:20:7: ( '?' )
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:20:9: '?'
            {
            match('?'); 

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
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:21:7: ( 'ALL' )
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:21:9: 'ALL'
            {
            match("ALL"); 



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
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:22:7: ( 'ANY' )
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:22:9: 'ANY'
            {
            match("ANY"); 



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
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:23:7: ( 'EFF' )
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:23:9: 'EFF'
            {
            match("EFF"); 



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
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:24:7: ( 'GEN' )
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:24:9: 'GEN'
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
    // $ANTLR end "T__48"

    // $ANTLR start "T__49"
    public final void mT__49() throws RecognitionException {
        try {
            int _type = T__49;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:25:7: ( 'SET' )
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:25:9: 'SET'
            {
            match("SET"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__49"

    // $ANTLR start "T__50"
    public final void mT__50() throws RecognitionException {
        try {
            int _type = T__50;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:26:7: ( '[' )
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:26:9: '['
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
    // $ANTLR end "T__50"

    // $ANTLR start "T__51"
    public final void mT__51() throws RecognitionException {
        try {
            int _type = T__51;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:27:7: ( ']' )
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:27:9: ']'
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
    // $ANTLR end "T__51"

    // $ANTLR start "T__52"
    public final void mT__52() throws RecognitionException {
        try {
            int _type = T__52;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:28:7: ( 'countHet' )
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:28:9: 'countHet'
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
    // $ANTLR end "T__52"

    // $ANTLR start "T__53"
    public final void mT__53() throws RecognitionException {
        try {
            int _type = T__53;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:29:7: ( 'countHom' )
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:29:9: 'countHom'
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
    // $ANTLR end "T__53"

    // $ANTLR start "T__54"
    public final void mT__54() throws RecognitionException {
        try {
            int _type = T__54;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:30:7: ( 'countRef' )
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:30:9: 'countRef'
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
    // $ANTLR end "T__54"

    // $ANTLR start "T__55"
    public final void mT__55() throws RecognitionException {
        try {
            int _type = T__55;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:31:7: ( 'countVariant' )
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:31:9: 'countVariant'
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
    // $ANTLR end "T__55"

    // $ANTLR start "T__56"
    public final void mT__56() throws RecognitionException {
        try {
            int _type = T__56;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:32:7: ( 'exists' )
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:32:9: 'exists'
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
    // $ANTLR end "T__56"

    // $ANTLR start "T__57"
    public final void mT__57() throws RecognitionException {
        try {
            int _type = T__57;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:33:7: ( 'in' )
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:33:9: 'in'
            {
            match("in"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__57"

    // $ANTLR start "T__58"
    public final void mT__58() throws RecognitionException {
        try {
            int _type = T__58;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:34:7: ( 'isHet' )
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:34:9: 'isHet'
            {
            match("isHet"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__58"

    // $ANTLR start "T__59"
    public final void mT__59() throws RecognitionException {
        try {
            int _type = T__59;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:35:7: ( 'isHom' )
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:35:9: 'isHom'
            {
            match("isHom"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__59"

    // $ANTLR start "T__60"
    public final void mT__60() throws RecognitionException {
        try {
            int _type = T__60;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:36:7: ( 'isRef' )
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:36:9: 'isRef'
            {
            match("isRef"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__60"

    // $ANTLR start "T__61"
    public final void mT__61() throws RecognitionException {
        try {
            int _type = T__61;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:37:7: ( 'isVariant' )
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:37:9: 'isVariant'
            {
            match("isVariant"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__61"

    // $ANTLR start "T__62"
    public final void mT__62() throws RecognitionException {
        try {
            int _type = T__62;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:38:7: ( 'na' )
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:38:9: 'na'
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
    // $ANTLR end "T__62"

    // $ANTLR start "T__63"
    public final void mT__63() throws RecognitionException {
        try {
            int _type = T__63;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:39:7: ( '|' )
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:39:9: '|'
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
    // $ANTLR end "T__63"

    // $ANTLR start "WS"
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:46:5: ( ( ' ' | '\\t' )+ )
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:46:7: ( ' ' | '\\t' )+
            {
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:46:7: ( ' ' | '\\t' )+
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
            	    // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:
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
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:51:10: ( ( ( '\\r' )? '\\n' )+ )
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:51:12: ( ( '\\r' )? '\\n' )+
            {
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:51:12: ( ( '\\r' )? '\\n' )+
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
            	    // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:51:13: ( '\\r' )? '\\n'
            	    {
            	    // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:51:13: ( '\\r' )?
            	    int alt2=2;
            	    int LA2_0 = input.LA(1);

            	    if ( (LA2_0=='\r') ) {
            	        alt2=1;
            	    }
            	    switch (alt2) {
            	        case 1 :
            	            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:51:13: '\\r'
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
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:55:17: ( ( DIGIT )+ )
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:55:19: ( DIGIT )+
            {
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:55:19: ( DIGIT )+
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
            	    // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:
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
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:58:16: ( '0' .. '9' )
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:
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
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:61:17: ( LOWER | UPPER )
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:
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
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:62:16: ( 'a' .. 'z' )
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:
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
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:63:16: ( 'A' .. 'Z' )
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:
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
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:66:20: ( LETTER | DIGIT )
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:
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
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:69:12: ( '//' (~ ( '\\r' | '\\n' ) )* NEWLINE )
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:69:14: '//' (~ ( '\\r' | '\\n' ) )* NEWLINE
            {
            match("//"); 



            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:69:19: (~ ( '\\r' | '\\n' ) )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( ((LA5_0 >= '\u0000' && LA5_0 <= '\t')||(LA5_0 >= '\u000B' && LA5_0 <= '\f')||(LA5_0 >= '\u000E' && LA5_0 <= '\uFFFF')) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:
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
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:72:8: ( ( '+' | '-' )? NUMBER ( '.' NUMBER )? ( ( 'e' | 'E' ) ( '+' | '-' )? NUMBER )? )
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:72:12: ( '+' | '-' )? NUMBER ( '.' NUMBER )? ( ( 'e' | 'E' ) ( '+' | '-' )? NUMBER )?
            {
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:72:12: ( '+' | '-' )?
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0=='+'||LA6_0=='-') ) {
                alt6=1;
            }
            switch (alt6) {
                case 1 :
                    // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:
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


            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:72:30: ( '.' NUMBER )?
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0=='.') ) {
                alt7=1;
            }
            switch (alt7) {
                case 1 :
                    // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:72:32: '.' NUMBER
                    {
                    match('.'); 

                    mNUMBER(); 


                    }
                    break;

            }


            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:72:46: ( ( 'e' | 'E' ) ( '+' | '-' )? NUMBER )?
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0=='E'||LA9_0=='e') ) {
                alt9=1;
            }
            switch (alt9) {
                case 1 :
                    // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:72:47: ( 'e' | 'E' ) ( '+' | '-' )? NUMBER
                    {
                    if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                        input.consume();
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;
                    }


                    // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:72:57: ( '+' | '-' )?
                    int alt8=2;
                    int LA8_0 = input.LA(1);

                    if ( (LA8_0=='+'||LA8_0=='-') ) {
                        alt8=1;
                    }
                    switch (alt8) {
                        case 1 :
                            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:
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
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:75:7: ( '\\'' (~ ( '\\n' | '\\r' | '\\'' ) )* '\\'' )
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:75:9: '\\'' (~ ( '\\n' | '\\r' | '\\'' ) )* '\\''
            {
            match('\''); 

            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:75:14: (~ ( '\\n' | '\\r' | '\\'' ) )*
            loop10:
            do {
                int alt10=2;
                int LA10_0 = input.LA(1);

                if ( ((LA10_0 >= '\u0000' && LA10_0 <= '\t')||(LA10_0 >= '\u000B' && LA10_0 <= '\f')||(LA10_0 >= '\u000E' && LA10_0 <= '&')||(LA10_0 >= '(' && LA10_0 <= '\uFFFF')) ) {
                    alt10=1;
                }


                switch (alt10) {
            	case 1 :
            	    // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:
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
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:78:4: ( LETTER ( ALPHANUM | '_' | '.' )* )
            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:78:6: LETTER ( ALPHANUM | '_' | '.' )*
            {
            mLETTER(); 


            // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:78:13: ( ALPHANUM | '_' | '.' )*
            loop11:
            do {
                int alt11=2;
                int LA11_0 = input.LA(1);

                if ( (LA11_0=='.'||(LA11_0 >= '0' && LA11_0 <= '9')||(LA11_0 >= 'A' && LA11_0 <= 'Z')||LA11_0=='_'||(LA11_0 >= 'a' && LA11_0 <= 'z')) ) {
                    alt11=1;
                }


                switch (alt11) {
            	case 1 :
            	    // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:
            	    {
            	    if ( input.LA(1)=='.'||(input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
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
        // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:1:8: ( T__30 | T__31 | T__32 | T__33 | T__34 | T__35 | T__36 | T__37 | T__38 | T__39 | T__40 | T__41 | T__42 | T__43 | T__44 | T__45 | T__46 | T__47 | T__48 | T__49 | T__50 | T__51 | T__52 | T__53 | T__54 | T__55 | T__56 | T__57 | T__58 | T__59 | T__60 | T__61 | T__62 | T__63 | WS | NEWLINE | COMMENT_SL | FLOAT | STRING | ID )
        int alt12=40;
        alt12 = dfa12.predict(input);
        switch (alt12) {
            case 1 :
                // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:1:10: T__30
                {
                mT__30(); 


                }
                break;
            case 2 :
                // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:1:16: T__31
                {
                mT__31(); 


                }
                break;
            case 3 :
                // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:1:22: T__32
                {
                mT__32(); 


                }
                break;
            case 4 :
                // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:1:28: T__33
                {
                mT__33(); 


                }
                break;
            case 5 :
                // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:1:34: T__34
                {
                mT__34(); 


                }
                break;
            case 6 :
                // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:1:40: T__35
                {
                mT__35(); 


                }
                break;
            case 7 :
                // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:1:46: T__36
                {
                mT__36(); 


                }
                break;
            case 8 :
                // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:1:52: T__37
                {
                mT__37(); 


                }
                break;
            case 9 :
                // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:1:58: T__38
                {
                mT__38(); 


                }
                break;
            case 10 :
                // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:1:64: T__39
                {
                mT__39(); 


                }
                break;
            case 11 :
                // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:1:70: T__40
                {
                mT__40(); 


                }
                break;
            case 12 :
                // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:1:76: T__41
                {
                mT__41(); 


                }
                break;
            case 13 :
                // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:1:82: T__42
                {
                mT__42(); 


                }
                break;
            case 14 :
                // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:1:88: T__43
                {
                mT__43(); 


                }
                break;
            case 15 :
                // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:1:94: T__44
                {
                mT__44(); 


                }
                break;
            case 16 :
                // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:1:100: T__45
                {
                mT__45(); 


                }
                break;
            case 17 :
                // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:1:106: T__46
                {
                mT__46(); 


                }
                break;
            case 18 :
                // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:1:112: T__47
                {
                mT__47(); 


                }
                break;
            case 19 :
                // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:1:118: T__48
                {
                mT__48(); 


                }
                break;
            case 20 :
                // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:1:124: T__49
                {
                mT__49(); 


                }
                break;
            case 21 :
                // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:1:130: T__50
                {
                mT__50(); 


                }
                break;
            case 22 :
                // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:1:136: T__51
                {
                mT__51(); 


                }
                break;
            case 23 :
                // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:1:142: T__52
                {
                mT__52(); 


                }
                break;
            case 24 :
                // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:1:148: T__53
                {
                mT__53(); 


                }
                break;
            case 25 :
                // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:1:154: T__54
                {
                mT__54(); 


                }
                break;
            case 26 :
                // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:1:160: T__55
                {
                mT__55(); 


                }
                break;
            case 27 :
                // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:1:166: T__56
                {
                mT__56(); 


                }
                break;
            case 28 :
                // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:1:172: T__57
                {
                mT__57(); 


                }
                break;
            case 29 :
                // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:1:178: T__58
                {
                mT__58(); 


                }
                break;
            case 30 :
                // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:1:184: T__59
                {
                mT__59(); 


                }
                break;
            case 31 :
                // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:1:190: T__60
                {
                mT__60(); 


                }
                break;
            case 32 :
                // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:1:196: T__61
                {
                mT__61(); 


                }
                break;
            case 33 :
                // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:1:202: T__62
                {
                mT__62(); 


                }
                break;
            case 34 :
                // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:1:208: T__63
                {
                mT__63(); 


                }
                break;
            case 35 :
                // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:1:214: WS
                {
                mWS(); 


                }
                break;
            case 36 :
                // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:1:217: NEWLINE
                {
                mNEWLINE(); 


                }
                break;
            case 37 :
                // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:1:225: COMMENT_SL
                {
                mCOMMENT_SL(); 


                }
                break;
            case 38 :
                // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:1:236: FLOAT
                {
                mFLOAT(); 


                }
                break;
            case 39 :
                // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:1:242: STRING
                {
                mSTRING(); 


                }
                break;
            case 40 :
                // /Users/pablocingolani/Documents/workspace/SnpSift/antlr/VcfFilter.g:1:249: ID
                {
                mID(); 


                }
                break;

        }

    }


    protected DFA12 dfa12 = new DFA12(this);
    static final String DFA12_eotS =
        "\1\uffff\1\36\5\uffff\1\40\1\42\1\44\1\uffff\4\33\2\uffff\4\33\20"+
        "\uffff\7\33\1\66\1\33\1\72\1\73\1\74\1\75\1\76\1\77\2\33\1\uffff"+
        "\3\33\6\uffff\10\33\1\120\1\121\1\122\4\33\1\130\3\uffff\5\33\1"+
        "\uffff\1\33\1\137\1\140\1\141\2\33\3\uffff\1\33\1\145\1\33\1\uffff"+
        "\1\33\1\150\1\uffff";
    static final String DFA12_eofS =
        "\151\uffff";
    static final String DFA12_minS =
        "\1\11\1\75\5\uffff\1\75\1\176\1\75\1\uffff\1\114\1\106\2\105\2\uffff"+
        "\1\157\1\170\1\156\1\141\20\uffff\1\114\1\131\1\106\1\116\1\124"+
        "\1\165\1\151\1\56\1\110\6\56\1\156\1\163\1\uffff\2\145\1\141\6\uffff"+
        "\3\164\1\155\1\146\1\162\1\110\1\163\3\56\1\151\2\145\1\141\1\56"+
        "\3\uffff\1\141\1\164\1\155\1\146\1\162\1\uffff\1\156\3\56\1\151"+
        "\1\164\3\uffff\1\141\1\56\1\156\1\uffff\1\164\1\56\1\uffff";
    static final String DFA12_maxS =
        "\1\174\1\176\5\uffff\1\75\1\176\1\75\1\uffff\1\116\1\106\2\105\2"+
        "\uffff\1\157\1\170\1\163\1\141\20\uffff\1\114\1\131\1\106\1\116"+
        "\1\124\1\165\1\151\1\172\1\126\6\172\1\156\1\163\1\uffff\1\157\1"+
        "\145\1\141\6\uffff\3\164\1\155\1\146\1\162\1\126\1\163\3\172\1\151"+
        "\1\157\1\145\1\141\1\172\3\uffff\1\141\1\164\1\155\1\146\1\162\1"+
        "\uffff\1\156\3\172\1\151\1\164\3\uffff\1\141\1\172\1\156\1\uffff"+
        "\1\164\1\172\1\uffff";
    static final String DFA12_acceptS =
        "\2\uffff\1\4\1\5\1\6\1\7\1\10\3\uffff\1\17\4\uffff\1\25\1\26\4\uffff"+
        "\1\42\1\43\1\44\1\45\1\46\1\47\1\50\1\2\1\3\1\1\1\12\1\11\1\14\1"+
        "\13\1\16\1\15\21\uffff\1\34\3\uffff\1\41\1\20\1\21\1\22\1\23\1\24"+
        "\20\uffff\1\35\1\36\1\37\5\uffff\1\33\6\uffff\1\27\1\30\1\31\3\uffff"+
        "\1\40\2\uffff\1\32";
    static final String DFA12_specialS =
        "\151\uffff}>";
    static final String[] DFA12_transitionS = {
            "\1\26\1\27\2\uffff\1\27\22\uffff\1\26\1\1\4\uffff\1\2\1\32\1"+
            "\3\1\4\1\5\1\31\1\uffff\1\31\1\6\1\30\12\31\2\uffff\1\7\1\10"+
            "\1\11\1\12\1\uffff\1\13\3\33\1\14\1\33\1\15\13\33\1\16\7\33"+
            "\1\17\1\uffff\1\20\3\uffff\2\33\1\21\1\33\1\22\3\33\1\23\4\33"+
            "\1\24\14\33\1\uffff\1\25",
            "\1\34\100\uffff\1\35",
            "",
            "",
            "",
            "",
            "",
            "\1\37",
            "\1\41",
            "\1\43",
            "",
            "\1\45\1\uffff\1\46",
            "\1\47",
            "\1\50",
            "\1\51",
            "",
            "",
            "\1\52",
            "\1\53",
            "\1\54\4\uffff\1\55",
            "\1\56",
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
            "\1\57",
            "\1\60",
            "\1\61",
            "\1\62",
            "\1\63",
            "\1\64",
            "\1\65",
            "\1\33\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\32\33",
            "\1\67\11\uffff\1\70\3\uffff\1\71",
            "\1\33\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\32\33",
            "\1\33\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\32\33",
            "\1\33\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\32\33",
            "\1\33\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\32\33",
            "\1\33\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\32\33",
            "\1\33\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\32\33",
            "\1\100",
            "\1\101",
            "",
            "\1\102\11\uffff\1\103",
            "\1\104",
            "\1\105",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\106",
            "\1\107",
            "\1\110",
            "\1\111",
            "\1\112",
            "\1\113",
            "\1\114\11\uffff\1\115\3\uffff\1\116",
            "\1\117",
            "\1\33\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\32\33",
            "\1\33\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\32\33",
            "\1\33\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\32\33",
            "\1\123",
            "\1\124\11\uffff\1\125",
            "\1\126",
            "\1\127",
            "\1\33\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\32\33",
            "",
            "",
            "",
            "\1\131",
            "\1\132",
            "\1\133",
            "\1\134",
            "\1\135",
            "",
            "\1\136",
            "\1\33\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\32\33",
            "\1\33\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\32\33",
            "\1\33\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\32\33",
            "\1\142",
            "\1\143",
            "",
            "",
            "",
            "\1\144",
            "\1\33\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\32\33",
            "\1\146",
            "",
            "\1\147",
            "\1\33\1\uffff\12\33\7\uffff\32\33\4\uffff\1\33\1\uffff\32\33",
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
            return "1:1: Tokens : ( T__30 | T__31 | T__32 | T__33 | T__34 | T__35 | T__36 | T__37 | T__38 | T__39 | T__40 | T__41 | T__42 | T__43 | T__44 | T__45 | T__46 | T__47 | T__48 | T__49 | T__50 | T__51 | T__52 | T__53 | T__54 | T__55 | T__56 | T__57 | T__58 | T__59 | T__60 | T__61 | T__62 | T__63 | WS | NEWLINE | COMMENT_SL | FLOAT | STRING | ID );";
        }
    }
 

}