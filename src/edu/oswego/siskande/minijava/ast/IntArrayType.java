package edu.oswego.siskande.minijava.ast;

import edu.oswego.siskande.minijava.visitor.Visitor;

public class IntArrayType extends Type{
    public void accept(Visitor v) {v.visit(this);}

    @Override
    public String name() {
        return "int[]";
    }
}
