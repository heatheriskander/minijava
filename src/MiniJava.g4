grammar MiniJava;

goal: mainClass (classDeclaration)* EOF;

mainClass: 'class' identifier '{' 'public' 'static' 'void' 'main' '(' 'String' '[' ']' identifier ')' '{' statement '}' '}';

varDeclaration: type identifier ';';

classDeclaration
    : 'class' identifier '{' (varDeclaration)* (methodDeclaration)* '}' # ClassDeclarationSimple
    | 'class' identifier  'extends' identifier '{' (varDeclaration)* (methodDeclaration)* '}' # ClassDeclarationExtends
    ;

methodDeclaration: 'public' type identifier '(' (type identifier (',' type identifier)*)? ')' '{' (varDeclaration)* (statement)* 'return' expression ';' '}';

type
    : 'int' '[' ']' # IntArrayType
    | 'boolean' # BooleanType
    | 'int' # IntType
    | identifier # IdentifierType
    ;

statement
    : '{' (statement)* '}' # Block
    | 'if' '(' expression ')' statement 'else' statement # If
    | 'while' '(' expression ')' statement # While
    | 'System.out.println' '(' expression ')' ';' # Print
    | identifier '=' expression ';' # Assign
    | identifier '[' expression ']' '=' expression ';' # ArrayAssign;

expression
    : '(' expression ')' # Parens
    | expression '*' expression # Times
    | expression (PLUS | MINUS) expression # PlusMinus
    | expression '<' expression # LessThan
    | expression '&&' expression # And
    | expression '[' expression ']' # ArrayLookup
    | expression '.' 'length' # ArrayLength
    | expression '.' identifier '(' (expression (',' expression)*)? ')' # Call
    | INTEGER_LITERAL # IntegerLiteral
    | 'true' # True
    | 'false' # False
    | identifier # IdentifierExp
    | 'this' # This
    | 'new' 'int' '[' expression ']' # NewArray
    | 'new' identifier '(' ')' # NewObject
    | '!' expression # Not
    ;

identifier: IDENTIFIER;

PLUS: '+';
MINUS: '-';
IDENTIFIER: [_a-zA-Z][_a-zA-Z0-9]*;
INTEGER_LITERAL: '0' | [1-9][0-9]*;
WHITESPACE: [ \n\t\r]+ -> skip;
COMMENT: '/*' .*? '*/' -> skip;
LINE_COMMENT: '//' ~[\r\n]* -> skip;