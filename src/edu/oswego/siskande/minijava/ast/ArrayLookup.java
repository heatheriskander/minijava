package edu.oswego.siskande.minijava.ast;

import edu.oswego.siskande.minijava.visitor.Visitor;

public class ArrayLookup extends Exp {
    public Exp array, index;

    public ArrayLookup(Exp array, Exp index) {
        this.array = array;
        this.index = index;
    }

    public void accept(Visitor v) {v.visit(this);}
}
