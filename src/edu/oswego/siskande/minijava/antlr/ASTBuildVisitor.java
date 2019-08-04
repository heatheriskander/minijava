package edu.oswego.siskande.minijava.antlr;

import edu.oswego.siskande.minijava.ast.*;
import org.antlr.v4.runtime.RuleContext;

import java.beans.Expression;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ASTBuildVisitor extends MiniJavaBaseVisitor<ASTNode> {
    String currentClass = null;

    @Override
    public ASTNode visitGoal(MiniJavaParser.GoalContext ctx) {
        MainClass mainClass = (MainClass) visitMainClass(ctx.mainClass());
        List<ClassDecl> classDeclList = new ArrayList<>();
        for(MiniJavaParser.ClassDeclarationContext c : ctx.classDeclaration()) {
            classDeclList.add((ClassDecl) c.accept(this));
        }

        return new Program(mainClass, classDeclList);
    }

    @Override
    public ASTNode visitMainClass(MiniJavaParser.MainClassContext ctx) {
        Identifier className = (Identifier) visitIdentifier(ctx.identifier(0));
        Identifier argsName = (Identifier) visitIdentifier(ctx.identifier(1));
        Statement statement = (Statement) ctx.statement().accept(this);

        return new MainClass(className, argsName, statement);
    }

    @Override
    public ASTNode visitVarDeclaration(MiniJavaParser.VarDeclarationContext ctx) {
        Type type = (Type) ctx.type().accept(this);
        Identifier identifier = (Identifier) visitIdentifier(ctx.identifier());

        return new VarDecl(type, identifier, ctx.start.getLine());
    }


    @Override
    public ASTNode visitClassDeclarationSimple(MiniJavaParser.ClassDeclarationSimpleContext ctx) {
        Identifier identifier = (Identifier) ctx.identifier().accept(this);
        currentClass = identifier.name;
        List<VarDecl> varDeclList = new ArrayList<>();
        for(MiniJavaParser.VarDeclarationContext c : ctx.varDeclaration())
            varDeclList.add((VarDecl) visitVarDeclaration(c));
        List<MethodDecl> methodDeclList = new ArrayList<>();
        for(MiniJavaParser.MethodDeclarationContext c : ctx.methodDeclaration())
            methodDeclList.add((MethodDecl) visitMethodDeclaration(c));

        return new ClassDeclSimple(identifier, varDeclList, methodDeclList);
    }

    @Override
    public ASTNode visitClassDeclarationExtends(MiniJavaParser.ClassDeclarationExtendsContext ctx) {
        Identifier identifier = (Identifier) ctx.identifier(0).accept(this);
        currentClass = identifier.name;
        Identifier parent = (Identifier) ctx.identifier(1).accept(this);
        List<VarDecl> varDeclList = new ArrayList<>();
        for(MiniJavaParser.VarDeclarationContext c : ctx.varDeclaration())
            varDeclList.add((VarDecl) visitVarDeclaration(c));
        List<MethodDecl> methodDeclList = new ArrayList<>();
        for(MiniJavaParser.MethodDeclarationContext c : ctx.methodDeclaration())
            methodDeclList.add((MethodDecl) visitMethodDeclaration(c));

        return new ClassDeclExtends(identifier, parent, varDeclList, methodDeclList, ctx.start.getLine());
    }

    @Override
    public ASTNode visitMethodDeclaration(MiniJavaParser.MethodDeclarationContext ctx) {
        Type type = (Type) ctx.type(0).accept(this);
        Identifier identifier = (Identifier) ctx.identifier(0).accept(this);
        List<Formal> formalList = new ArrayList<>();
        for(int i = 1; i < ctx.type().size() && i < ctx.identifier().size(); i++) {
            formalList.add(new Formal((Type) ctx.type(i).accept(this), (Identifier) ctx.identifier(i).accept(this), ctx.start.getLine()));
        }
        List<VarDecl> varDeclList = new ArrayList<>();
        for(MiniJavaParser.VarDeclarationContext c : ctx.varDeclaration())
            varDeclList.add((VarDecl) visitVarDeclaration(c));
        List<Statement> statementList = new ArrayList<>();
        for(MiniJavaParser.StatementContext c : ctx.statement())
            statementList.add((Statement) c.accept(this));
        Exp ret = (Exp) ctx.expression().accept(this);

        return new MethodDecl(type, identifier, formalList, varDeclList, statementList, ret, ctx.start.getLine(), currentClass);
    }

    @Override
    public ASTNode visitIntArrayType(MiniJavaParser.IntArrayTypeContext ctx) {
        return new IntArrayType();
    }

    @Override
    public ASTNode visitBooleanType(MiniJavaParser.BooleanTypeContext ctx) {
        return new BooleanType();
    }

    @Override
    public ASTNode visitIntType(MiniJavaParser.IntTypeContext ctx) {
        return new IntType();
    }

    @Override
    public ASTNode visitIdentifierType(MiniJavaParser.IdentifierTypeContext ctx) {
        Identifier identifier = (Identifier) ctx.identifier().accept(this);
        return new IdentifierType(identifier.name);
    }

    @Override
    public ASTNode visitBlock(MiniJavaParser.BlockContext ctx) {
        List<Statement> statementList = ctx.statement().stream()
                .map(c -> (Statement) c.accept(this))
                .collect(Collectors.toCollection(ArrayList::new));

        var res =  new Block(statementList);
        res.line = ctx.start.getLine();
        return res;
    }

    @Override
    public ASTNode visitIf(MiniJavaParser.IfContext ctx) {
        Exp condition = (Exp) ctx.expression().accept(this);
        Statement s1 = (Statement) ctx.statement(0).accept(this);
        Statement s2 = (Statement) ctx.statement(1).accept(this);

        var res =  new If(condition, s1, s2, ctx.start.getLine());
        res.line = ctx.start.getLine();
        return res;
    }

    @Override
    public ASTNode visitWhile(MiniJavaParser.WhileContext ctx) {
        Exp condition = (Exp) ctx.expression().accept(this);
        Statement statement = (Statement) ctx.statement().accept(this);

        var res = new While(condition, statement, ctx.start.getLine());
        res.line = ctx.start.getLine();
        return res;
    }

    @Override
    public ASTNode visitPrint(MiniJavaParser.PrintContext ctx) {
        Exp exp = (Exp) ctx.expression().accept(this);

        var res = new Print(exp, ctx.start.getLine());
        res.line = ctx.start.getLine();
        return res;
    }

    @Override
    public ASTNode visitAssign(MiniJavaParser.AssignContext ctx) {
        Identifier identifier = (Identifier) ctx.identifier().accept(this);
        Exp exp = (Exp) ctx.expression().accept(this);

        return new Assign(identifier, exp, ctx.start.getLine());
    }

    @Override
    public ASTNode visitArrayAssign(MiniJavaParser.ArrayAssignContext ctx) {
        Identifier array = (Identifier) ctx.identifier().accept(this);
        Exp index = (Exp) ctx.expression(0).accept(this);
        Exp val = (Exp) ctx.expression(0).accept(this);

        return new ArrayAssign(array, index, val, ctx.start.getLine());
    }

    @Override
    public ASTNode visitIdentifierExp(MiniJavaParser.IdentifierExpContext ctx) {
        Identifier identifier = (Identifier) ctx.identifier().accept(this);
        var res = new IdentifierExp(identifier.name);
        res.line = ctx.start.getLine();
        return res;
    }

    @Override
    public ASTNode visitCall(MiniJavaParser.CallContext ctx) {
        List<Exp> expList = ctx.expression().stream()
                .map(c -> (Exp) c.accept(this))
                .collect(Collectors.toCollection(ArrayList::new));
        Identifier method = (Identifier) ctx.identifier().accept(this);

        var res = new Call(expList.remove(0), method, expList);
        res.line = ctx.start.getLine();
        return res;
    }

    @Override
    public ASTNode visitNewObject(MiniJavaParser.NewObjectContext ctx) {
        Identifier identifier = (Identifier) ctx.identifier().accept(this);
        var res = new NewObject(identifier);
        res.line = ctx.start.getLine();
        return res;
    }

    @Override
    public ASTNode visitParens(MiniJavaParser.ParensContext ctx) {
        return ctx.expression().accept(this);
    }

    @Override
    public ASTNode visitTrue(MiniJavaParser.TrueContext ctx) {
        var res =  new True();
        res.line = ctx.start.getLine();
        return res;
    }

    @Override
    public ASTNode visitArrayLength(MiniJavaParser.ArrayLengthContext ctx) {
        Exp array = (Exp) ctx.expression().accept(this);

        var res = new ArrayLength(array);
        res.line = ctx.start.getLine();
        return res;
    }

    @Override
    public ASTNode visitFalse(MiniJavaParser.FalseContext ctx) {
        return new False();
    }

    @Override
    public ASTNode visitPlusMinus(MiniJavaParser.PlusMinusContext ctx) {
        Exp e1 = (Exp) ctx.expression(0).accept(this);
        Exp e2 = (Exp) ctx.expression(1).accept(this);

        if(ctx.PLUS() != null) {
            var res = new Plus(e1, e2);
            res.line = ctx.start.getLine();
            return res;
        }

        var res = new Minus(e1, e2);
        res.line = ctx.start.getLine();
        return res;
    }

    @Override
    public ASTNode visitArrayLookup(MiniJavaParser.ArrayLookupContext ctx) {
        Exp array = (Exp) ctx.expression(0).accept(this);
        Exp index = (Exp) ctx.expression(1).accept(this);

        var res =  new ArrayLookup(array, index);
        res.line = ctx.start.getLine();
        return res;
    }

    @Override
    public ASTNode visitNewArray(MiniJavaParser.NewArrayContext ctx) {
        Exp length = (Exp) ctx.expression().accept(this);

        var res = new NewArray(length);
        res.line = ctx.start.getLine();
        return res;
    }

    @Override
    public ASTNode visitNot(MiniJavaParser.NotContext ctx) {
        Exp exp = (Exp) ctx.expression().accept(this);

        var res = new Not(exp);
        res.line = ctx.start.getLine();
        return res;
    }

    @Override
    public ASTNode visitLessThan(MiniJavaParser.LessThanContext ctx) {
        Exp e1 = (Exp) ctx.expression(0).accept(this);
        Exp e2 = (Exp) ctx.expression(1).accept(this);

        var res = new LessThan(e1, e2);
        res.line = ctx.start.getLine();
        return res;
    }

    @Override
    public ASTNode visitAnd(MiniJavaParser.AndContext ctx) {
        Exp e1 = (Exp) ctx.expression(0).accept(this);
        Exp e2 = (Exp) ctx.expression(1).accept(this);

        var res =  new And(e1, e2);
        res.line = ctx.start.getLine();
        return res;
    }

    @Override
    public ASTNode visitTimes(MiniJavaParser.TimesContext ctx) {
        Exp e1 = (Exp) ctx.expression(0).accept(this);
        Exp e2 = (Exp) ctx.expression(1).accept(this);

        var res = new Times(e1, e2);
        res.line = ctx.start.getLine();
        return res;
    }

    @Override
    public ASTNode visitThis(MiniJavaParser.ThisContext ctx) {
        var res = new This();
        res.line = ctx.start.getLine();
        return res;
    }

    @Override
    public ASTNode visitIntegerLiteral(MiniJavaParser.IntegerLiteralContext ctx) {
        return new IntegerLiteral(Integer.parseInt(ctx.INTEGER_LITERAL().getText()));
    }

    @Override
    public ASTNode visitIdentifier(MiniJavaParser.IdentifierContext ctx) {
        return new Identifier(ctx.getText());
    }
}
