package edu.oswego.siskande.minijava.ast;

import edu.oswego.siskande.minijava.visitor.Visitor;

public class Formal implements ASTNode {
    public Type type;
    public Identifier identifier;
    public int line;

    public Formal(Type type, Identifier identifier, int line) {
        this.type = type;
        this.identifier = identifier;
        this.line = line;
    }

    public void accept(Visitor v) {v.visit(this);}
}
