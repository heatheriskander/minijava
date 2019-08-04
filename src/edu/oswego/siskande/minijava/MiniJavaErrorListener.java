package edu.oswego.siskande.minijava;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import edu.oswego.siskande.minijava.antlr.*;


public class MiniJavaErrorListener extends BaseErrorListener {

    public static final MiniJavaErrorListener INSTANCE = new MiniJavaErrorListener();

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        var expected = ((Parser) recognizer).getExpectedTokens();
        if(!expected.isNil() && expected.size() != MiniJavaParser.VOCABULARY.getMaxTokenType()) {
            System.err.printf("[%d:%d] Expecting %s\n", line, charPositionInLine, expected.toString(MiniJavaParser.VOCABULARY));
        }
        else {
            System.err.printf("[%d:%d] %s\n", line, charPositionInLine, msg);
        }
    }
}
