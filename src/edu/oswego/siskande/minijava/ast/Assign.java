package edu.oswego.siskande.minijava.ast;

import edu.oswego.siskande.minijava.visitor.Visitor;

public class Assign extends Statement {
    public Identifier identifier;
    public Exp exp;

    public Assign(Identifier identifier, Exp exp, int line) {
        this.identifier = identifier;
        this.exp = exp;
        this.line = line;
    }

    public void accept(Visitor v) {v.visit(this);}
}
