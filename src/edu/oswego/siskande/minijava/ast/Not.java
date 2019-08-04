package edu.oswego.siskande.minijava.ast;


import edu.oswego.siskande.minijava.visitor.Visitor;

public class Not extends Exp {
    public Exp exp;

    public Not(Exp exp) {
        this.exp = exp;
    }

    public void accept(Visitor v) {v.visit(this);}}
