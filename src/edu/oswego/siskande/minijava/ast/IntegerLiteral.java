package edu.oswego.siskande.minijava.ast;

import edu.oswego.siskande.minijava.visitor.Visitor;

public class IntegerLiteral extends Exp {
    public int val;

    public IntegerLiteral(int val) {
        this.val = val;
    }

    public void accept(Visitor v) {v.visit(this);}
}
