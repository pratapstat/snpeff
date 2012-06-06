//-----------------------------------------------------------------------------
//
// Filter parsing grammar
//			Pablo Cingolani
//
//-----------------------------------------------------------------------------
grammar VcfFilter;

options {
  // We're going to output an AST.
  output = AST;
}

// Tokens (reserved words)
tokens {
	CONDITION;
	OP_BINARY;
	OP_UNARY;	
	VAR_FIELD;
	VAR_SUBFIELD;
	VAR_GENOTYPE;
	VAR_GENOTYPE_SUB;
	FUNCTION;
	LITERAL_NUMBER;
	LITERAL_STRING;
}

@lexer::header {
package ca.mcgill.mcb.pcingola.vcfEtc.antlr;
}

@header {
package ca.mcgill.mcb.pcingola.vcfEtc.antlr;
}


//-----------------------------------------------------------------------------
// Lexer
//-----------------------------------------------------------------------------

// Send runs of space and tab characters to the hidden channel.        
WS		: (' ' | '\t')+ { $channel = HIDDEN; };

// Treat runs of newline characters as a single NEWLINE token.
// On some platforms, newlines are represented by a \n character.
// On others they are represented by a \r and a \n character.
NEWLINE		: ('\r'? '\n')+ { $channel=HIDDEN; };

	
// A number is a set of digits
fragment NUMBER	: (DIGIT)+;

// A DIGIT
fragment DIGIT	: '0'..'9' ;

// A letter
fragment LETTER	: LOWER | UPPER;
fragment LOWER	: 'a'..'z';
fragment UPPER	: 'A'..'Z';

// Letter or digit
fragment ALPHANUM 	:	LETTER | DIGIT;

// 'C' style single line comments
COMMENT_SL : '//' ~('\r' | '\n')* NEWLINE	{ $channel=HIDDEN; };

// FLOAT number (float/double) without any signNUMBER
FLOAT  :   ('+'|'-')? NUMBER ( '.' NUMBER )? (('e'|'E') ('+'|'-')? NUMBER)? ;

// A string literal
 STRING: '\'' ~( '\n' | '\r' | '\'' )* '\'' { setText(getText().substring( 1, getText().length()-1 ) ); } ;

// An identifier.
ID : LETTER (ALPHANUM | '_')*;

//-----------------------------------------------------------------------------
// Parser
//-----------------------------------------------------------------------------

// FCL file may contain several funcion blocks
main		:	f=condition -> ^(CONDITION $f);

condition	:	subcondition (boolOperator^ subcondition)*;
subcondition	:	('!'^)? (bare | paren);
bare		:	binaryExpr | unaryExpr;
paren 		:	'('! condition ')'!;

// Operations always are in parenthesis
binaryExpr	:	l=expression o=binOperator r=expression 		-> ^(OP_BINARY $o $l $r);
unaryExpr	:	o=uniOperator e=expression				-> ^(OP_UNARY $o $e);

// All these return a boolean
boolOperator  	:	'&' | '|';
binOperator  	:	'='  | '>='  | '>' | '<=' | '<'  | '!=' | '=~' | '!~';
uniOperator  	: 	'!' | 'na' | 'exists';				

// Variables, functions or literals (these are values
expression	:	var 
			| function 
			| literalFloat 
			| literalString;


literalFloat	:	f=FLOAT							->	^(LITERAL_NUMBER $f);
literalString	:	s=STRING						->	^(LITERAL_STRING $s);
	
// Variables
var 		:	varField | varSubfield | varGenotype | varGenotypeSub ;
varField	:	i=ID							-> ^(VAR_FIELD $i);
varSubfield	:	i=ID '[' n=index ']'					-> ^(VAR_SUBFIELD $i $n);
varGenotype	:	'GEN' '[' g=index ']' '.' i=ID				-> ^(VAR_GENOTYPE $g $i);
varGenotypeSub	:	'GEN' '[' g=index ']' '.' i=ID  '[' n=FLOAT ']'		-> ^(VAR_GENOTYPE_SUB $g $i $n);

// Functions calculated based on genotype information
function	:	f=functionName '(' ')'		-> ^(FUNCTION $f);
functionName	:	'countHom' | 'countHet' | 'countVariant' | 'countRef';

// You can use '*' for 'any'
index 		:	FLOAT | '*';
