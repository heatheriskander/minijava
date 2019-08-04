package edu.oswego.siskande.minijava.ast;

import edu.oswego.siskande.minijava.visitor.Visitor;

import java.util.List;

public class ClassDeclExtends extends ClassDecl {
    public Identifier parent;


    public ClassDeclExtends(Identifier identifier, Identifier parent, List<VarDecl> varDeclList, List<MethodDecl> methodDeclList, int line) {
        this.identifier = identifier;
        this.parent = parent;
        this.varDeclList = varDeclList;
        this.methodDeclList = methodDeclList;
        this.line = line;
    }

    public void accept(Visitor v) {v.visit(this);}
}
