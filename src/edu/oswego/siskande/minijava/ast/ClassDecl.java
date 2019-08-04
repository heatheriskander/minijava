package edu.oswego.siskande.minijava.ast;

import edu.oswego.siskande.minijava.MiniJava;
import edu.oswego.siskande.minijava.types.MiniJavaType;

import java.util.List;
import java.util.Map;

public abstract class ClassDecl implements ASTNode {
   public int line;
   public Identifier identifier;
   public List<VarDecl> varDeclList;
   public List<MethodDecl> methodDeclList;
   public Map<String, MiniJavaType> symbols;
}
