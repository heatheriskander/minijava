package edu.oswego.siskande.minijava.ast;

import edu.oswego.siskande.minijava.visitor.Visitor;

import java.util.List;

public class Block extends Statement {
    public List<Statement> statementList;

    public Block(List<Statement> statementList) {
        this.statementList = statementList;
    }

    public void accept(Visitor v) {v.visit(this);}
}
