package parser;

import parser.exceptions.LexerUnknownLexemeException;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

public class Lexer {
    final int NOT_STARTED = -2;
    int current;
    private Reader reader;
    private Map<Character, LexemeType> stringToTypeMap = new HashMap<>();

    public Lexer(Reader reader) {
        this.reader = reader;
        stringToTypeMap.put('(', LexemeType.OPEN_BRACKET);
        stringToTypeMap.put(')', LexemeType.CLOSE_BRACKET);
        stringToTypeMap.put('+', LexemeType.PLUS);
        stringToTypeMap.put('-', LexemeType.MINUS);
        stringToTypeMap.put('*', LexemeType.MULTIPLY);
        stringToTypeMap.put('^', LexemeType.POWER);
        stringToTypeMap.put('/', LexemeType.DIVIDE);
        current = NOT_STARTED;
    }


    public Lexeme getNextLexeme() throws IOException {
        if (current == NOT_STARTED)
            current = reader.read();
        while (current == ' ')
            current = reader.read();
        if (current == '\n' || current == '\r')
            return new Lexeme("eof",LexemeType.EOF);
        if (stringToTypeMap.containsKey((char) current)) {
            char tmp = (char) current;
            current = reader.read();
            return new Lexeme(Character.toString(tmp), stringToTypeMap.get(tmp));
        }
        if (current == -1) {
            current = reader.read();
            return new Lexeme("eof", LexemeType.EOF);
        }
        if (Character.isDigit(current))
            return getNumberLexeme();
        throw new LexerUnknownLexemeException();
    }

    private Lexeme getNumberLexeme() throws IOException {
        StringBuilder value = new StringBuilder();
        while (Character.isDigit(current)) {
            value.append((char) current);
            current = reader.read();
        }
        return new Lexeme(value.toString(), LexemeType.VALUE);
    }
}
