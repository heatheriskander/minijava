package edu.oswego.siskande.minijava.ast;

import edu.oswego.siskande.minijava.visitor.Visitor;

import java.util.List;

public class Program implements ASTNode {
    public MainClass mainClass;
    public List<ClassDecl> classDeclList;

    public Program(MainClass mainClass, List<ClassDecl> classDeclList) {
        this.mainClass = mainClass;
        this.classDeclList = classDeclList;
    }

    public void accept(Visitor v) {v.visit(this);}
}
