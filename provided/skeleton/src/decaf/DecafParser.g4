parser grammar DecafParser;

@header {
package decaf;
}

options
{
  language=Java;
  tokenVocab=DecafLexer;
}

type_id: type ID;
type_id2: type? ID;

program : CLASSE LCURLY fild_declaration* method_declaration* RCURLY EOF;

fild_declaration : (type ID | type_id COLE int_literal COLD) (COMMA type_id2 | COMMA type_id2 COLE int_literal COLD)* SEMICOLON;

method_declaration : (type | VOID) ID LPAR ((type_id | type_id COLE int_literal COLD) (COMMA type_id2 | COMMA type_id2 COLE int_literal COLD)*)* RPAR block;

block : LCURLY var_declaration* statment* RCURLY ;

var_declaration : type ID (COMMA type? ID)* SEMICOLON; 

type : INT | BOOLEAN;


statment : location assign_op expr SEMICOLON
	| method_call SEMICOLON
	| IF LPAR expr RPAR block (ELSE block)?
	| FOR ID DEFINE expr COMMA expr block 
	| RETURN expr? SEMICOLON 
	| BREAK SEMICOLON
	| CONTINUE SEMICOLON
	| block;

assign_op : DEFINE | ADD | SUBTRACT;

method_call : method_name LPAR ((expr) (COMMA expr)*)? RPAR | CALLOUT LPAR string_literal  (COMMA callout_org)* RPAR;

method_name : ID;

location : ID
	| ID COLE expr COLD;

expr : location
	| method_call
	| literal
	| expr bin_op expr
	| MINUS expr
	| EXCL expr
	| LPAR expr RPAR;

callout_org : expr | string_literal;

bin_op : arith_op | rel_op | eq_op | cond_op;

arith_op : PLUS | MINUS | MUL | DIV | MOD;

rel_op : LT | GT | LE | GE;

eq_op : EQ | NEQ;

cond_op : AND | OR;

literal : int_literal | char_literal | bool_literal ;

alpha_num : alpha | NUME;

alpha: LET;

digit: NUME;

hex_digit : digit | LET+;

int_literal : decimal_literal | hex_literal;

decimal_literal : NUME;

hex_literal : HEX;

bool_literal : BOOLEANLITERAL;

char_literal : CHAR;

string_literal : STRING;

