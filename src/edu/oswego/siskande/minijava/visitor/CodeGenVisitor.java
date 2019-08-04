package edu.oswego.siskande.minijava.visitor;

import edu.oswego.siskande.minijava.MiniJava;
import edu.oswego.siskande.minijava.ast.*;
import edu.oswego.siskande.minijava.types.Class;
import edu.oswego.siskande.minijava.types.MiniJavaType;

import java.io.PrintWriter;
import java.util.Map;

public class CodeGenVisitor implements Visitor {
    private PrintWriter out;
    private Map<String, MiniJavaType> types;

    private int currLbl;
    private int currTemp;

    private String nextLbl() { return "lbl" + currLbl++; }
    private String nextTemp() { return "t" + currTemp++; }

    public CodeGenVisitor(PrintWriter out, Map<String, MiniJavaType> types) {
        this.out = out;
        this.types = types;
    }

    @Override
    public void visit(Program n) {
        out.println("#include \"runtime/minijava.h\";\n");
        n.classDeclList.forEach(c -> out.printf("typedef struct %s_s* %s_t;\n", c.identifier.name, c.identifier.name));
        out.println();
        types.values().forEach(t -> {
            if (t instanceof Class) {
                ((Class) t).getMethods().values().forEach(m -> {
                    StringBuilder methodSignature = new StringBuilder()
                            .append(m.returnType)
                            .append("_t ")
                            .append(t.getName())
                            .append("_")
                            .append(m.name)
                            .append("(")
                            .append(t.getName())
                            .append("_t, ");
                    m.paramTypes.forEach(p -> methodSignature.append(p).append("_t, "));
                    methodSignature.delete(methodSignature.length() - 2, methodSignature.length());
                    methodSignature.append(");");
                    out.println(methodSignature.toString());
                });
            }
        });

        out.println();
        n.classDeclList.forEach(c -> c.accept(this));
        n.mainClass.accept(this);

        out.flush();
    }

    @Override
    public void visit(MainClass n) {
        currTemp = 0;
        out.print("int main()\n{\n");
        n.statement.accept(this);
        out.println("}");
    }

    @Override
    public void visit(ClassDeclSimple n) {
        out.printf("struct %s_s\n{\n", n.identifier.name);
        n.symbols.forEach((name, type) -> {
            out.printf("%s_t __%s;\n", type.getName(), name);
        });
        out.printf("};\n\n");

        n.methodDeclList.forEach(m ->  {
            out.printf("%s_t %s_%s(%s_t __self", m.type.name(), n.identifier.name, m.identifier.name, n.identifier.name);
            m.accept(this);
        });
    }

    @Override
    public void visit(ClassDeclExtends n) {
        out.printf("struct %s_s\n{\n", n.identifier.name);
        n.symbols.forEach((name, type) -> {
            out.printf("%s_t __%s;\n", type.getName(), name);
        });
        out.printf("};\n\n");

        n.methodDeclList.forEach(m ->  {
            out.printf("%s_t %s_%s(%s_t __self", m.type.name(), n.identifier.name, m.identifier.name, n.identifier.name);
            m.accept(this);
        });
    }

    @Override
    public void visit(VarDecl n) {
        out.printf("%s_t __%s;\n", n.type.name(), n.identifier.name);
    }

    @Override
    public void visit(MethodDecl n) {
        currLbl = 0;
        if(!n.formalList.isEmpty()) {
            n.formalList.forEach(f -> {
                out.printf(", ");
                out.printf("%s_t __%s", f.type.name(), f.identifier.name);
            });
        }
        out.printf(")\n{\n");

        n.varDeclList.forEach(v -> v.accept(this));
        out.println();

        n.statementList.forEach(s -> s.accept(this));
        out.println();

        out.printf("%s_t __%s;\n", n.type.name(), n.ret.symbol = nextTemp());
        n.ret.accept(this);
        out.printf("return __%s;\n", n.ret.symbol);

        out.printf("}\n\n");
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
        String ifLbl = nextLbl();
        String endLbl = nextLbl();
        out.printf("%s_t __%s;\n", n.condition.type.getName(), n.condition.symbol = nextTemp());
        n.condition.accept(this);
        out.printf("if (__%s) goto %s;\n", n.condition.symbol, ifLbl);
        n.s2.accept(this);
        out.printf("goto %s;\n%s:;\n", endLbl, ifLbl);
        n.s1.accept(this);
        out.printf("%s:;\n", endLbl);
    }

    @Override
    public void visit(While n) {

    }

    @Override
    public void visit(Print n) {
        n.exp.accept(this);
        out.printf("printf(\"%s\\n\", __%s);\n", "%d", n.exp.symbol);
    }

    @Override
    public void visit(Assign n) {
        out.printf("%s_t __%s;\n", n.exp.type.getName(), n.exp.symbol = nextTemp());
        n.exp.accept(this);
        out.printf("__%s = __%s;\n", n.identifier.name, n.exp.symbol);
    }

    @Override
    public void visit(ArrayAssign n) {

    }

    @Override
    public void visit(And n) {

    }

    @Override
    public void visit(LessThan n) {
        out.printf("int_t __%s, __%s;\n", n.e1.symbol = nextTemp(), n.e2.symbol = nextTemp());
        n.e1.accept(this);
        n.e2.accept(this);
        out.printf("__%s = __%s < __%s;\n", n.symbol, n.e1.symbol, n.e2.symbol);
    }

    @Override
    public void visit(Plus n) {
        out.printf("int_t __%s, __%s;\n", n.e1.symbol = nextTemp(), n.e2.symbol = nextTemp());
        n.e1.accept(this);
        n.e2.accept(this);
        out.printf("__%s = __%s + __%s;\n", n.symbol, n.e1.symbol, n.e2.symbol);
    }

    @Override
    public void visit(Minus n) {
        out.printf("int_t __%s, __%s;\n", n.e1.symbol = nextTemp(), n.e2.symbol = nextTemp());
        n.e1.accept(this);
        n.e2.accept(this);
        out.printf("__%s = __%s - __%s;\n", n.symbol, n.e1.symbol, n.e2.symbol);
    }

    @Override
    public void visit(Times n) {
        out.printf("int_t __%s, __%s;\n", n.e1.symbol = nextTemp(), n.e2.symbol = nextTemp());
        n.e1.accept(this);
        n.e2.accept(this);
        out.printf("__%s = __%s * __%s;\n", n.symbol, n.e1.symbol, n.e2.symbol);
    }

    @Override
    public void visit(ArrayLookup n) {

    }

    @Override
    public void visit(ArrayLength n) {
        out.printf("IntArray_t __%s;\n", n.array.symbol = nextTemp());
        n.array.accept(this);

        out.printf("__%s = __%s->size;\n", n.symbol, n.array.symbol);
    }

    @Override
    public void visit(Call n) {
        out.printf("%s_t __%s;\n", n.type.getName(), n.symbol = nextTemp());
        out.printf("%s_t __%s;\n", n.obj.type.getName(), n.obj.symbol = nextTemp());
        n.obj.accept(this);
        n.args.forEach(a -> {
            out.printf("%s_t __%s;\n", a.type.getName(), a.symbol = nextTemp());
            a.accept(this);
        });
        out.printf("__%s = %s_%s(__%s", n.symbol, n.obj.type.getName(), n.method.name, n.obj.symbol);
        n.args.forEach(a -> out.printf(", __%s", a.symbol));
        out.printf(");\n");
    }

    @Override
    public void visit(IntegerLiteral n) {
        out.printf("__%s = %d;\n", n.symbol, n.val);
    }

    @Override
    public void visit(True n) {
        out.printf("__%s = 1;\n", n.symbol);
    }

    @Override
    public void visit(False n) {
        out.printf("__%s = 0;\n", n.symbol);
    }

    @Override
    public void visit(IdentifierExp n) {
        out.printf("__%s = __%s;\n", n.symbol, n.name);
    }

    @Override
    public void visit(This n) {
        out.printf("__%s = __self;\n", n.symbol);
    }

    @Override
    public void visit(NewArray n) {

    }

    @Override
    public void visit(NewObject n) {
        out.printf("__%s = malloc(sizeof(struct %s_s));\n", n.symbol, n.identifier.name);
    }

    @Override
    public void visit(Not n) {
        out.printf("boolean_t __%s;\n", n.exp.symbol = nextTemp());
        n.exp.accept(this);

        out.printf("__%s = !__%s;\n", n.symbol, n.exp.symbol);
    }

    @Override
    public void visit(Identifier n) {

    }
}
