package edu.oswego.siskande.minijava.ast;

import edu.oswego.siskande.minijava.visitor.Visitor;

import java.util.List;

public class ClassDeclSimple extends ClassDecl {

    public ClassDeclSimple(Identifier identifier, List<VarDecl> varDeclList, List<MethodDecl> methodDeclList) {
        this.identifier = identifier;
        this.varDeclList = varDeclList;
        this.methodDeclList = methodDeclList;
    }

    public void accept(Visitor v) {v.visit(this);}
}
