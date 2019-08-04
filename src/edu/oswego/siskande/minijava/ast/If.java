package edu.oswego.siskande.minijava.ast;

import edu.oswego.siskande.minijava.visitor.Visitor;

public class If extends Statement{
    public Exp condition;
    public Statement s1, s2;

    public If(Exp condition, Statement s1, Statement s2, int line) {
        this.condition = condition;
        this.s1 = s1;
        this.s2 = s2;
        this.line = line;
    }

    public void accept(Visitor v) {v.visit(this);}
}
