package edu.oswego.siskande.minijava.ast;

import edu.oswego.siskande.minijava.visitor.Visitor;

public class Plus extends Exp {
    public Exp e1, e2;

    public Plus(Exp e1, Exp e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    public void accept(Visitor v) {v.visit(this);}
}
