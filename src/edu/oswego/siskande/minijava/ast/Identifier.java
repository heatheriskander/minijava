package edu.oswego.siskande.minijava.ast;

import edu.oswego.siskande.minijava.visitor.Visitor;

public class Identifier implements ASTNode {
    public String name;

    public Identifier(String name) {
        this.name = name;
    }

    public void accept(Visitor v) {v.visit(this);}
}
