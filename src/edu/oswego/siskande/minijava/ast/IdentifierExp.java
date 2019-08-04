package edu.oswego.siskande.minijava.ast;

import edu.oswego.siskande.minijava.visitor.Visitor;

public class IdentifierExp extends Exp {
    public String name;

    public IdentifierExp(String name) {
        this.name = name;
    }

    public void accept(Visitor v) {v.visit(this);}
}
