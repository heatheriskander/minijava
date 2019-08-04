package edu.oswego.siskande.minijava.ast;

import edu.oswego.siskande.minijava.visitor.Visitor;

public class ArrayLength extends Exp {
    public Exp array;

    public ArrayLength(Exp array) {
        this.array = array;
    }

    public void accept(Visitor v) {v.visit(this);}
}
