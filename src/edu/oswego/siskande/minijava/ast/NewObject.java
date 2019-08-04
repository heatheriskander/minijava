package edu.oswego.siskande.minijava.ast;

import edu.oswego.siskande.minijava.visitor.Visitor;

public class NewObject extends Exp {
    public Identifier identifier;

    public NewObject(Identifier identifier) {
        this.identifier = identifier;
    }

    public void accept(Visitor v) {v.visit(this);}
}
