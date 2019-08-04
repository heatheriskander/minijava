package edu.oswego.siskande.minijava.visitor;

import edu.oswego.siskande.minijava.ast.*;
import edu.oswego.siskande.minijava.types.Method;
import edu.oswego.siskande.minijava.types.MiniJavaType;
import edu.oswego.siskande.minijava.types.Class;

import java.util.*;

public class TypeVisitor implements Visitor {

    private Map<String, MiniJavaType> types;

    private Map<String, String> currentFieldMap = null;
    private Map<String, Method> currentMethodMap = null;
    private String currentClassName = null;

    private Set<String> errorSet = new HashSet<>();

    public TypeVisitor(Map<String, MiniJavaType> types) {
        this.types = types;
    }

    @Override
    public void visit(Program n) {
        n.classDeclList.forEach(c -> c.accept(this));

        types.values().forEach(t -> {
            if (t.getParent() != null && !types.containsKey(t.getParent()))
                errorSet.add(String.format("Class '%s' extends nonexistent type '%s'", t.getName(), t.getParent()));
        });

        if(!errorSet.isEmpty()) {
            errorSet.forEach(e -> System.err.println("Error: " + e));
            types.clear();
        }
    }

    @Override
    public void visit(MainClass n) {

    }

    @Override
    public void visit(ClassDeclSimple n) {
        if (types.containsKey(n.identifier.name)) {
            errorSet.add(String.format("Multiple declaration of class '%s' (%d)", n.identifier.name, n.line));
            return;
        }

        var newClass = new Class(n.identifier.name, new HashMap<>(), new HashMap<>(), n);
        types.put(n.identifier.name, newClass);

        Map<String, String> fields = new HashMap<>();
        Map<String, Method> methods = new HashMap<>();

        currentFieldMap = fields;
        currentMethodMap = methods;
        currentClassName = n.identifier.name;

        n.varDeclList.forEach(v -> v.accept(this));
        n.methodDeclList.forEach(m -> m.accept(this));

        newClass.setFields(fields);
        newClass.setMethods(methods);
    }

    @Override
    public void visit(ClassDeclExtends n) {
        if (types.containsKey(n.identifier.name)) {
            errorSet.add(String.format("Multiple declaration of class '%s' (%d)", n.identifier.name, n.line));
            return;
        }

        var newClass = new Class(n.identifier.name, n.parent.name, null, null, n);
        types.put(n.identifier.name, newClass);

        Map<String, String> fields = new HashMap<>();
        Map<String, Method> methods = new HashMap<>();

        currentFieldMap = fields;
        currentMethodMap = methods;
        currentClassName = n.identifier.name;

        n.varDeclList.forEach(v -> v.accept(this));
        n.methodDeclList.forEach(m -> m.accept(this));

        newClass.setFields(fields);
        newClass.setMethods(methods);
    }

    @Override
    public void visit(VarDecl n) {
        if (currentFieldMap.containsKey(n.identifier.name)) {
            errorSet.add(String.format("Multiple declaration of field '%s' in class '%s' (%d)", n.identifier.name, currentClassName, n.line));
            return;
        }

        currentFieldMap.put(n.identifier.name, n.type.name());
    }

    @Override
    public void visit(MethodDecl n) {
        String returnType = n.type.name();
        var paramNames = new ArrayList<String>();
        var paramTypes = new ArrayList<String>();
        n.formalList.forEach(f -> {
            paramNames.add(f.identifier.name);
            paramTypes.add(f.type.name());
        });
        var method = new Method(n.identifier.name, returnType, paramNames, paramTypes, n);

        if (currentMethodMap.containsKey(method.toString())) {
            errorSet.add(String.format("Multiple declaration of method '%s' (%d)", method.toString(), n.line));
            return;
        }

        currentMethodMap.put(method.toString(), method);
    }

    @Override
    public void visit(Formal n) {

    }

    @Override
    public void visit(IntArrayType n) {

    }

    @Override
    public void visit(BooleanType n) {

    }

    @Override
    public void visit(IntType n) {

    }

    @Override
    public void visit(IdentifierType n) {

    }

    @Override
    public void visit(Block n) {

    }

    @Override
    public void visit(If n) {

    }

    @Override
    public void visit(While n) {

    }

    @Override
    public void visit(Print n) {

    }

    @Override
    public void visit(Assign n) {

    }

    @Override
    public void visit(ArrayAssign n) {

    }

    @Override
    public void visit(And n) {

    }

    @Override
    public void visit(LessThan n) {

    }

    @Override
    public void visit(Plus n) {

    }

    @Override
    public void visit(Minus n) {

    }

    @Override
    public void visit(Times n) {

    }

    @Override
    public void visit(ArrayLookup n) {

    }

    @Override
    public void visit(ArrayLength n) {

    }

    @Override
    public void visit(Call n) {

    }

    @Override
    public void visit(IntegerLiteral n) {

    }

    @Override
    public void visit(True n) {

    }

    @Override
    public void visit(False n) {

    }

    @Override
    public void visit(IdentifierExp n) {

    }

    @Override
    public void visit(This n) {

    }

    @Override
    public void visit(NewArray n) {

    }

    @Override
    public void visit(NewObject n) {

    }

    @Override
    public void visit(Not n) {

    }

    @Override
    public void visit(Identifier n) {

    }
}
