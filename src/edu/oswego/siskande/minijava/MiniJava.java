package edu.oswego.siskande.minijava;

import edu.oswego.siskande.minijava.ast.*;
import org.antlr.v4.runtime.*;
import edu.oswego.siskande.minijava.antlr.*;
import edu.oswego.siskande.minijava.visitor.*;
import edu.oswego.siskande.minijava.types.MiniJavaType;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;


public class MiniJava {
    public static void main (String[] args) {
        if (args.length != 1) {
            System.err.println("Error: compiler must be given a single file as an argument.");
            System.exit(1);
        }

        MiniJavaParser parser = null;
        try {
            var lexer = new MiniJavaLexer(CharStreams.fromFileName(args[0]));
            lexer.removeErrorListeners();
            lexer.addErrorListener(MiniJavaErrorListener.INSTANCE);

            parser = new MiniJavaParser(new CommonTokenStream(lexer));
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
            System.exit(1);
        }

        var context = parser.goal();
        var ast = new ASTBuildVisitor().visitGoal(context);

        Map<String, MiniJavaType> types = new HashMap<>();
        var typeVisitor = new TypeVisitor(types);
        typeVisitor.visit((Program) ast);
        if(!types.isEmpty()) {
            types.put("boolean", new MiniJavaType("boolean"));
            types.put("int", new MiniJavaType("int"));
            var intArrayType = new MiniJavaType("IntArray");
            types.put("IntArray", intArrayType);
            types.put("int[]", intArrayType);
        }

        var typeCheckVisitor = new TypeCheckVisitor(types);
        typeCheckVisitor.visit((Program) ast);

        try {
            var out = new PrintWriter(new BufferedWriter(new FileWriter(args[0] + ".c")));
            var codeGenVisitor = new CodeGenVisitor(out, types);
            ast.accept(codeGenVisitor);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}