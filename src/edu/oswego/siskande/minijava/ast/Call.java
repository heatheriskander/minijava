package edu.oswego.siskande.minijava.ast;

import edu.oswego.siskande.minijava.visitor.Visitor;

import java.util.List;

public class Call extends Exp {
    public Exp obj;
    public Identifier method;
    public List<Exp> args;

    public Call(Exp obj, Identifier method, List<Exp> args) {
        this.obj = obj;
        this.method = method;
        this.args = args;
    }

    public void accept(Visitor v) {v.visit(this);}
}
