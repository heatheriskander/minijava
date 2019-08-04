package edu.oswego.siskande.minijava.ast;

import edu.oswego.siskande.minijava.visitor.Visitor;

public class VarDecl implements ASTNode {
    public Type type;
    public Identifier identifier;
    public int line;

    public VarDecl(Type type, Identifier identifier, int line) {
        this.type = type;
        this.identifier = identifier;
        this.line = line;
    }

    public void accept(Visitor v) {v.visit(this);}
}
