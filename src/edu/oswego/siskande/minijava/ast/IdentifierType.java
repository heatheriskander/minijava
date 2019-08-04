package edu.oswego.siskande.minijava.ast;

import edu.oswego.siskande.minijava.visitor.Visitor;

public class IdentifierType extends Type{
    String name;
    public IdentifierType(String name) {
        this.name = name;
    }


    public void accept(Visitor v) {v.visit(this);}

    @Override
    public String name() {
        return name;
    }
}
