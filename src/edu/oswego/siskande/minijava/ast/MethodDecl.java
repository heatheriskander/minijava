package edu.oswego.siskande.minijava.ast;

import edu.oswego.siskande.minijava.MiniJava;
import edu.oswego.siskande.minijava.types.MiniJavaType;
import edu.oswego.siskande.minijava.visitor.Visitor;

import java.util.List;
import java.util.Map;

public class MethodDecl implements ASTNode {
    public Type type;
    public Identifier identifier;
    public List<Formal> formalList;
    public List<VarDecl> varDeclList;
    public List<Statement> statementList;
    public Exp ret;
    public int line;
    public String containingClass;
    public Map<String, MiniJavaType> symbols;
    public String entryString;

    public MethodDecl(Type type, Identifier identifier, List<Formal> formalList, List<VarDecl> varDeclList, List<Statement> statementList, Exp exp, int line, String containingClass) {
        this.type = type;
        this.identifier = identifier;
        this.formalList = formalList;
        this.varDeclList = varDeclList;
        this.statementList = statementList;
        this.ret = exp;
        this.line = line;
        this.containingClass = containingClass;
    }

    public void accept(Visitor v) {v.visit(this);}}
