package edu.oswego.siskande.minijava.visitor;

import edu.oswego.siskande.minijava.MiniJava;
import edu.oswego.siskande.minijava.ast.*;
import edu.oswego.siskande.minijava.types.Class;
import edu.oswego.siskande.minijava.types.Method;
import edu.oswego.siskande.minijava.types.MiniJavaType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TypeCheckVisitor implements Visitor {
    private Map<String, MiniJavaType> types;
    private Set<String> errorSet = new HashSet<>();
    private Class currentClass;
    private Method currentMethod;

    public TypeCheckVisitor(Map<String, MiniJavaType> types) {
        this.types = types;
    }

    private boolean conformsTo(MiniJavaType a, MiniJavaType b) {
        if(a == null) return true;
        if (a.equals(b)) return true;
        if(a.getName().equals("int") && b.getName().equals("boolean")) return true;

        while (a.getParent() != null) {
            a = types.get(a.getParent());
            if (a.equals(b)) return true;
        }

        return false;
    }

    private MiniJavaType resolveIdType(String id) {
        int i = currentMethod.paramNames.indexOf(id);
        if (i >= 0) return types.get(currentMethod.paramTypes.get(i));
        if (currentMethod.node.symbols.containsKey(id)) return currentMethod.node.symbols.get(id);
        if (currentClass.getFields().containsKey(id)) return types.get(currentClass.getFields().get(id));

        Class curr = currentClass;
        while (curr.getParent() != null) {
            curr = (Class) types.get(curr.getParent());
            if (curr.getFields().containsKey(id)) return types.get(curr.getFields().get(id));
        }

        return null;
    }

    @Override
    public void visit(Program n) {
        n.mainClass.accept(this);
        n.classDeclList.forEach(c -> c.accept(this));
        errorSet.forEach(e -> System.err.println("Error: " + e));
    }

    @Override
    public void visit(MainClass n) {
        n.statement.accept(this);
    }

    @Override
    public void visit(ClassDeclSimple n) {
        n.symbols = new HashMap<>();
        n.varDeclList.forEach(v -> n.symbols.put(v.identifier.name, types.get(v.type.name())));
        currentClass = (Class) types.get(n.identifier.name);
        n.methodDeclList.forEach(m -> m.accept(this));
    }

    @Override
    public void visit(ClassDeclExtends n) {
        n.symbols = new HashMap<>();
        n.varDeclList.forEach(v -> n.symbols.put(v.identifier.name, types.get(v.type.name())));
        n.methodDeclList.forEach(m -> m.accept(this));
    }

    @Override
    public void visit(VarDecl n) {

    }

    @Override
    public void visit(MethodDecl n) {
        n.symbols = new HashMap<>();
//        n.formalList.forEach(f -> {
//            if (n.symbols.containsKey(f.identifier.name)) {
//                errorSet.add(String.format("Multiple declaration of parameter '%s' (%d)", f.identifier.name, n.line));
//            } else {
//                if (types.containsKey(f.type.name()))
//                    n.symbols.put(f.identifier.name, types.get(f.type.name()));
//                else
//                    errorSet.add(String.format("Undeclared type '%s' (%d)", f.type.name(), n.line));
//            }
//        });
        n.varDeclList.forEach(v -> {
            if (n.symbols.containsKey(v.identifier.name)) {
                errorSet.add(String.format("Multiple declaration of local variable '%s' (%d)", v.identifier.name, n.line));
            } else {
                if (types.containsKey(v.type.name()))
                    n.symbols.put(v.identifier.name, types.get(v.type.name()));
                else
                    errorSet.add(String.format("Undeclared type '%s' (%d)", v.type.name(), n.line));
            }
        });

        currentMethod = currentClass.getMethods().get(n.entryString);
        n.statementList.forEach(s -> s.accept(this));
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
        n.statementList.forEach(s -> s.accept(this));
    }

    @Override
    public void visit(If n) {
        n.condition.accept(this);
        if(!conformsTo(n.condition.type, types.get("boolean"))) {
            errorSet.add(String.format("Condition must be boolean (%d)", n.line));
        }

        n.s1.accept(this);
        n.s2.accept(this);
    }

    @Override
    public void visit(While n) {
        n.condition.accept(this);
        if(!conformsTo(n.condition.type, types.get("boolean"))) {
            errorSet.add(String.format("Condition must be boolean (%d)", n.line));
        }

        n.statement.accept(this);
    }

    @Override
    public void visit(Print n) {
        n.exp.accept(this);
    }

    @Override
    public void visit(Assign n) {
        n.exp.accept(this);

        MiniJavaType idType = resolveIdType(n.identifier.name);
        if (idType == null) {
            errorSet.add(String.format("Undeclared variable '%s' (%d)", n.identifier.name, n.line));
            return;
        }

        if (!conformsTo(n.exp.type, idType)) errorSet.add(String.format("Incompatible types (%d)", n.line));
    }

    @Override
    public void visit(ArrayAssign n) {
        n.index.accept(this);
        n.val.accept(this);

        MiniJavaType idType = resolveIdType(n.array.name);
        if (idType == null) {
            errorSet.add(String.format("Undeclared variable '%s' (%d)", n.array.name, n.line));
            return;
        }
        if (!conformsTo(idType, types.get("IntArray"))) {
            errorSet.add(String.format("Not an array: '%s' (%d)", n.array.name, n.line));
        }

        if (!conformsTo(n.index.type, types.get("int"))) {
            errorSet.add(String.format("Array index must be int (%d)", n.line));
        }

        if (!conformsTo(n.val.type, types.get("int"))) {
            errorSet.add(String.format("Incompatible types (%d)", n.line));
        }
    }

    @Override
    public void visit(And n) {
        n.e1.accept(this);
        n.e2.accept(this);

        if (!conformsTo(n.e1.type, types.get("boolean")) || !conformsTo(n.e2.type, types.get("boolean"))) {
            errorSet.add(String.format("Arguments must be boolean (%d)", n.line));
        }

        n.type = types.get("boolean");
    }

    @Override
    public void visit(LessThan n) {
        n.e1.accept(this);
        n.e2.accept(this);

        if (!conformsTo(n.e1.type, types.get("int")) || !conformsTo(n.e2.type, types.get("int"))) {
            errorSet.add(String.format("Arguments must be int (%d)", n.line));
        }

        n.type = types.get("boolean");
    }

    @Override
    public void visit(Plus n) {
        n.e1.accept(this);
        n.e2.accept(this);

        if (!conformsTo(n.e1.type, types.get("int")) || !conformsTo(n.e2.type, types.get("int"))) {
            errorSet.add(String.format("Arguments must be int (%d)", n.line));
        }

        n.type = types.get("int");
    }

    @Override
    public void visit(Minus n) {
        n.e1.accept(this);
        n.e2.accept(this);

        if (!conformsTo(n.e1.type, types.get("int")) || !conformsTo(n.e2.type, types.get("int"))) {
            errorSet.add(String.format("Arguments must be int (%d)", n.line));
        }

        n.type = types.get("int");
    }

    @Override
    public void visit(Times n) {
        n.e1.accept(this);
        n.e2.accept(this);

        if (!conformsTo(n.e1.type, types.get("int")) || !conformsTo(n.e2.type, types.get("int"))) {
            errorSet.add(String.format("Arguments must be int (%d)", n.line));
        }

        n.type = types.get("int");
    }

    @Override
    public void visit(ArrayLookup n) {
    }

    @Override
    public void visit(ArrayLength n) {
        n.type = types.get("int");
    }

    @Override
    public void visit(Call n) {
        n.obj.accept(this);
        n.method.accept(this);
        n.args.forEach(a -> a.accept(this));

        var objClass = (Class) n.obj.type;

        StringBuilder methodSignature = new StringBuilder()
                .append(n.method.name)
                .append("(");
        if(!n.args.isEmpty()) {
            n.args.forEach(a -> methodSignature.append(a.type.getName() + ", "));
            methodSignature.delete(methodSignature.length() - 2, methodSignature.length());
        }
        methodSignature.append(")");

        var method = objClass.getMethods().get(methodSignature.toString());
        if (method == null) errorSet.add(String.format("Undeclared method '%s.%s' (%d)", objClass.getName(), methodSignature.toString(), n.line));
        else n.type = types.get(method.returnType);
    }

    @Override
    public void visit(IntegerLiteral n) {
        n.type = types.get("int");
    }

    @Override
    public void visit(True n) {
        n.type = types.get("boolean");
    }

    @Override
    public void visit(False n) {
        n.type = types.get("boolean");
    }

    @Override
    public void visit(IdentifierExp n) {
        n.type = resolveIdType(n.name);
        if (n.type == null) {
            errorSet.add(String.format("Undeclared variable '%s' (%d)", n.name, n.line));
        }
    }

    @Override
    public void visit(This n) {
        n.type = currentClass;
    }

    @Override
    public void visit(NewArray n) {
        n.type = types.get("IntArray");

        n.length.accept(this);
        if (!conformsTo(n.length.type, types.get("int"))) {
            errorSet.add(String.format("Array size must be int (%d)", n.line));
        }
    }

    @Override
    public void visit(NewObject n) {
        n.type = types.get(n.identifier.name);
    }

    @Override
    public void visit(Not n) {
        n.exp.accept(this);
        if(!conformsTo(n.exp.type, types.get("boolean"))) {
            errorSet.add(String.format("Argument must be boolean (%d)", n.line));
        }

        n.type = types.get("boolean");
    }

    @Override
    public void visit(Identifier n) {

    }
}
