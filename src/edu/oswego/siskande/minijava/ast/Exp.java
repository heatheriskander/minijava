package edu.oswego.siskande.minijava.ast;

import edu.oswego.siskande.minijava.types.MiniJavaType;

public abstract class Exp implements ASTNode {
    public int line;
    public MiniJavaType type;
    public String symbol = "NULL";
}
