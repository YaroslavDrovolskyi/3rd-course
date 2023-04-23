package ua.drovolskyi.compilers.lab1;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Lexer {
    private CharStream charStream;
    private static final String whitespaces = " \t\n";
    private static final String punctuation = "()[]{},;|";
    private static final List<String> threeCharacterOperators = Arrays.asList(
        "===", "<=>", "**=", "..."
    );
    private static final List<String> twoCharacterOperators = Arrays.asList(
            "**", "<<", ">>", "==", "!=", ">=", "<=", "%=", "/=", "-=", "+=", "*=", "..", "?:", "&&", "||"
    );
    private static final String oneCharacterOperators = "!+-~*/%&|^><=.";

    public Lexer(CharStream charStream){
        this.charStream = charStream;
    }

    public List<Token> tokenize(){
        List<Token> tokens = new LinkedList<>();
        while(!charStream.isEnded()){
            tokens.add(extractToken());
        }
        return tokens;
    }

    // start of next token must be the currentSymbol
    private Token extractToken(){
        Character c = charStream.getCurrentSymbol();

        if(whitespaces.indexOf(c) != -1){
            return new Token(Token.Type.WHITESPACE, String.valueOf(c));
        }
        else if(c.equals('#')){
            return createCommentToken(); // return comment token to the end of line or EOF
        }
        // three-characters operators
        else if(threeCharacterOperators.contains(c + charStream.lookahead(2))){
            Token token = new Token(Token.Type.OPERATOR, c + charStream.lookahead(2));
            charStream.consume();
            charStream.consume();
            charStream.consume();
            return token;
        }
        // two-characters operators
        else if(twoCharacterOperators.contains(c + charStream.lookahead(1))){
            Token token = new Token(Token.Type.OPERATOR, c + charStream.lookahead(1));
            charStream.consume();
            charStream.consume();
            return token;
        }
        // one-character operators
        else if(oneCharacterOperators.contains(String.valueOf(c))){
            Token token = new Token(Token.Type.OPERATOR, String.valueOf(c));
            charStream.consume();

        }
        // one-character punctuation
        else if(punctuation.contains(String.valueOf(c))){
            Token token = new Token(Token.Type.PUNCTUATION, String.valueOf(c));
            charStream.consume();
            return token;
        }
        else if(Character.isDigit(c)){
            return createNumberToken();
        }

        ///////////////////// NEED TO remake createCommentToken() to make start of next token currentCharacter
        // check for three-character lexems
        // check for two-character lexems
        // check for one-character lexems
        // string
        // numbers ('_' in number are valid) (second dot in number is not valid)
        // words (keywords + identifiers) (+ 'not', 'and', 'or' operators)
        // '.' operator (check if number starts or not)
        // 'abs?' or 'abs!' are valid identifiers
    }

    // returns comment token to the end of line or end of charStream
    private Token createCommentToken(){
        // currentCharacter == '#'
        Integer tokenStartIndex = charStream.getCurrentIndex();
        while(!charStream.isLastSymbol() && !charStream.getCurrentSymbol().equals('\n')){
            charStream.consume();
        }
        Integer tokenEndIndex = charStream.getCurrentIndex();

        return new Token(Token.Type.COMMENT, charStream.getString(tokenStartIndex, tokenEndIndex));
    }

    private Token createNumberToken(){
        // currentCharacter is digit

        StringBuilder lexeme = new StringBuilder();
        lexeme.append(charStream.getCurrentSymbol());
        charStream.consume();

        Boolean dotOccurred = false;

        while(!charStream.isEnded()){
            Character c = charStream.getCurrentSymbol();

            if(c.equals('.')){
                if(dotOccurred.equals(true) ||
                        lexeme.lastIndexOf("_") == lexeme.length() - 1){
                    return createNumberTokenImpl(lexeme);
                }
                else{
                    dotOccurred = true;
                }
            }

            if(c.equals('_')){
                if(lexeme.lastIndexOf("_") == lexeme.length() - 1 ||
                        lexeme.lastIndexOf(".") == lexeme.length() - 1){
                    return createNumberTokenImpl(lexeme);
                }
            }

            if(!Character.isDigit(c)){
                return createNumberTokenImpl(lexeme);
            }

            lexeme.append(c);
            charStream.consume();
        }

        return createNumberTokenImpl(lexeme);
    }

    // call this method when currentCharacter don't satisfy you,
    // incorrect character is consumed, but not added to lexeme
    // put currentIndex on start of next token
    private Token createNumberTokenImpl(StringBuilder lexeme){
        // if '_' or '.' is last character of lexeme
        if(lexeme.lastIndexOf("_") == lexeme.length() - 1 ||
                lexeme.lastIndexOf(".") == lexeme.length() - 1){
            charStream.returnCharacter();
            lexeme.delete(lexeme.length() - 1, lexeme.length());
        }

        return new Token(Token.Type.LITERAL_NUMBER, lexeme.toString());
    }
}
