package parser;

import parser.exceptions.ParserIncorrectInputException;

import java.io.IOException;

public class Parser {
    private Lexer lexer;
    private Lexeme currentLexeme = null;
    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }
    private void clearState() {
        lexer.current = lexer.NOT_STARTED;
        currentLexeme = null;
    }
    public int calculate() {
        int answer;
        try {
             answer = parseExpression();
        }
        catch (ParserIncorrectInputException exc) {
            clearState();
            System.out.println("Invalid input");
            return calculate();
        }
        catch (IOException exc) {
            System.out.println("IO Error");
            clearState();
            return calculate();
        }
        catch (ArithmeticException exc) {
            System.out.println("Division by zero");
            clearState();
            return calculate();
        }
        catch (NumberFormatException exc) {
            System.out.println("The number was too large");
            clearState();
            return calculate();
        }
        if (currentLexeme.type!=LexemeType.EOF) {
            System.out.println("Incorrect input");
            clearState();
            return calculate();
        }
        clearState();
        return answer;
    }

    private int parseExpression() throws IOException {
        if (currentLexeme == null)
            currentLexeme = lexer.getNextLexeme();
        int tmp = parseTerm();
        while (currentLexeme.type ==LexemeType.MINUS || currentLexeme.type == LexemeType.PLUS) {
            if (currentLexeme.type == LexemeType.MINUS) {
                currentLexeme = lexer.getNextLexeme();
                tmp -= parseTerm();
            }
            if (currentLexeme.type == LexemeType.PLUS) {
                currentLexeme = lexer.getNextLexeme();
                tmp += parseTerm();
            }
        }
        return tmp;
    }

    private int parseTerm() throws IOException {
        int factor = parseFactor();
        while (currentLexeme.type == LexemeType.DIVIDE || currentLexeme.type == LexemeType.MULTIPLY) {
            if (currentLexeme.type == LexemeType.DIVIDE) {
                currentLexeme = lexer.getNextLexeme();
                factor /= parseFactor();
            }
            if (currentLexeme.type == LexemeType.MULTIPLY) {
                currentLexeme = lexer.getNextLexeme();
                factor *= parseFactor();
            }
        }
        return factor;
    }

    private int parsePower() throws IOException {
        if (currentLexeme.type == LexemeType.MINUS){
            currentLexeme = lexer.getNextLexeme();
            return -parseAtom();
        }
        else return parseAtom();
    }

    private int parseFactor() throws IOException {
        int power = parsePower();
        if (currentLexeme.type==LexemeType.POWER){
            currentLexeme = lexer.getNextLexeme();
            return (int)Math.pow(power,parseFactor());
        }
        else return power;
    }

    private int parseAtom() throws IOException {
        if (currentLexeme.type == LexemeType.VALUE) {
            Lexeme tmpLexeme = currentLexeme;
            currentLexeme = lexer.getNextLexeme();
            return Integer.valueOf(tmpLexeme.data);
        }
        if (currentLexeme.type == LexemeType.OPEN_BRACKET) {
            currentLexeme = lexer.getNextLexeme();
            int tmp = parseExpression();
            if (currentLexeme.type == LexemeType.CLOSE_BRACKET) {
                currentLexeme = lexer.getNextLexeme();
                return tmp;
            }
        }
        throw new ParserIncorrectInputException();
    }
}
