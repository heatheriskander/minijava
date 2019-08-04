package edu.oswego.siskande.minijava.ast;

import edu.oswego.siskande.minijava.visitor.Visitor;

public class While extends Statement {
    public Exp condition;
    public Statement statement;

    public While(Exp condition, Statement statement, int line) {
        this.condition = condition;
        this.statement = statement;
        this.line = line;
    }

    public void accept(Visitor v) {v.visit(this);}
}
