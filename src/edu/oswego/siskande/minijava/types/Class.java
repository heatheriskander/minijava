package edu.oswego.siskande.minijava.types;

import edu.oswego.siskande.minijava.ast.ClassDecl;

import java.util.Map;

public class Class extends MiniJavaType {
    private Map<String, String> fields;
    private Map<String, Method> methods;
    private ClassDecl node;

    public Class(String name, Map<String, String> fields, Map<String, Method> methods, ClassDecl node) {
        super(name);
        this.fields = fields;
        this.methods = methods;
        this.node = node;
    }

    public Class(String name, String parent, Map<String, String> fields, Map<String, Method> methods, ClassDecl node) {
        super(name, parent);
        this.fields = fields;
        this.methods = methods;
        this.node = node;
    }

    public Map<String, String> getFields() {
        return fields;
    }

    public Map<String, Method> getMethods() {
        return methods;
    }

    public ClassDecl getNode() {
        return node;
    }

    public void setFields(Map<String, String> fields) {
        this.fields = fields;
    }

    public void setMethods(Map<String, Method> methods) {
        this.methods = methods;
    }
}
