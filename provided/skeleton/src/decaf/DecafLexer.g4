lexer grammar DecafLexer;

@header {
package decaf;
}

options
{
  language=Java;
}

tokens
{
  TK_class
}

CLASSE : 'class Program';

INT : 'int';

LCURLY : '{';
RCURLY : '}';
COLE : '[';
COLD : ']';

LPAR : '(';
RPAR : ')';

NUME : NUM(NUM)*;
BOOLEAN : 'boolean';
BOOLEANLITERAL : 'true'|'false';
BREAK : 'break';
CALLOUT: 'callout';
CONTINUE : 'continue';
ELSE : 'else';
FOR: 'for';
IF : 'if';
RETURN : 'return';
VOID : 'void';
AND : '&&';
OR : '||';
EXCL : '!';
DEFINE: '=';
ADD : '+=';
SUBTRACT : '-=';
EQ : '==';
NEQ : '!=';
GT : '>';
GE : '>=';
LT : '<';
LE : '<=';
PLUS : '+';
MINUS : '-';
MUL : '*';
DIV : '/';
MOD : '%';
COMMA : ',';
COLON : ':';
SEMICOLON : ';';

ID : (LET|'_')(LET|'_'|NUM)*;

WS_ : (' '|'\t'|'\n') -> skip;

SL_COMMENT : '//' (~'\n')* '\n' -> skip;

CHAR : '\'' (ESC|LETRAS|NUM) '\'';
STRING : '\"' (WS_|PO|ESC|LETRAS|NUM)* '\"';
HEX : ZERO(HEXA|NUM)+|NUM(NUM)+|NUM;
CLASS : 'class program';
LET : ('A'..'Z')|('a'..'z');

fragment
ZERO: '0''x';

fragment
HEXA : ('A'..'F')|('a'..'f');

fragment
PO : ('.'|','|'\\\''|'\\\"'|':'|'?');

fragment
NUM : ('0'..'9');

fragment
LETRAS : ('A'..'Z')|('a'..'z')|'%'|','|'\\n';

fragment
ESC :  '\\' ('n'|'"'|'t'|'\\' );

fragment
OP : ('\\\"' |'.'|'?'|'\\\''|':'|'%');

fragment
KW : ('boolean'|'callout'|'class'|'else'|'int'|'return'|'void'|'for'|'break'|'continue');

