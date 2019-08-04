package edu.oswego.siskande.minijava.ast;

import edu.oswego.siskande.minijava.visitor.Visitor;

public class False extends Exp {
    public void accept(Visitor v) {v.visit(this);}
}
