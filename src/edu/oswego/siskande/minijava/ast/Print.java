package edu.oswego.siskande.minijava.ast;

import edu.oswego.siskande.minijava.visitor.Visitor;

public class Print extends Statement {
    public Exp exp;

    public Print(Exp exp, int line) {
        this.exp = exp;
        this.line = line;
    }

    public void accept(Visitor v) {v.visit(this);}
}
