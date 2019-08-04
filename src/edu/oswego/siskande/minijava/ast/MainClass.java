package edu.oswego.siskande.minijava.ast;

import edu.oswego.siskande.minijava.visitor.Visitor;

public class MainClass implements ASTNode {
    public Identifier className, argsName;
    public Statement statement;

    public MainClass(Identifier className, Identifier argsName, Statement statement) {
        this.className = className;
        this.argsName = argsName;
        this.statement = statement;
    }

    public void accept(Visitor v) {v.visit(this);}
}
