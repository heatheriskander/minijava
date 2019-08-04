package edu.oswego.siskande.minijava.ast;

import edu.oswego.siskande.minijava.visitor.Visitor;

public interface ASTNode {
    void accept(Visitor v);
}
