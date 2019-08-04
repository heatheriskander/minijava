package edu.oswego.siskande.minijava.ast;

import edu.oswego.siskande.minijava.visitor.Visitor;

public class NewArray extends Exp {
    public Exp length;

    public NewArray(Exp length) {
        this.length = length;
    }

    public void accept(Visitor v) {v.visit(this);}
}
