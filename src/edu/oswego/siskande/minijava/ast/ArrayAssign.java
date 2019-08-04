package edu.oswego.siskande.minijava.ast;

import edu.oswego.siskande.minijava.visitor.Visitor;

public class ArrayAssign extends Statement {
    public Identifier array;
    public Exp index, val;

    public ArrayAssign(Identifier array, Exp index, Exp val, int line) {
        this.array = array;
        this.index = index;
        this.val = val;
        this.line = line;
    }

    public void accept(Visitor v) {v.visit(this);}
}
