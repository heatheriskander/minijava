package edu.oswego.siskande.minijava.types;

import edu.oswego.siskande.minijava.ast.MethodDecl;

import java.util.List;

public class Method {
    public final String name;
    public final String returnType;
    public List<String> paramNames;
    public List<String> paramTypes;
    public final String paramStr;
    public final MethodDecl node;

    public Method(String name, String returnType, List<String> paramNames, List<String> paramTypes, MethodDecl node) {
        this.name = name;
        this.returnType = returnType;
        this.paramNames = paramNames;
        this.paramTypes = paramTypes;
        this.node = node;

        var paramStringBuilder = new StringBuilder();
        paramTypes.forEach(p -> paramStringBuilder.append(p).append(", "));
        paramStr = paramStringBuilder.length() > 0? paramStringBuilder.substring(0, paramStringBuilder.length() - 2): "";
        node.entryString = this.toString();
    }

    @Override
    public String toString() {
        return String.format("%s(%s)", name, paramStr);
    }
}
