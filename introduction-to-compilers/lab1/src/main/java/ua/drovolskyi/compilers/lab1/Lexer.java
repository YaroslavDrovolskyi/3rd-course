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
    private static final String oneCharacterOperators = "!+-~*/%&|^><=";

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
            return new Token(Token.Type.OPERATOR, c + charStream.lookahead(2));
            charStream.consume();
            charStream.consume();
            charStream.consume();
        }
        // two-characters operators
        else if(twoCharacterOperators.contains(c + charStream.lookahead(1))){
            return new Token(Token.Type.OPERATOR, c + charStream.lookahead(1));
            charStream.consume();
            charStream.consume();
        }
        // one-character operators
        else if(oneCharacterOperators.contains(String.valueOf(c))){
            charStream.consume();
            return new Token(Token.Type.OPERATOR, String.valueOf(c));
        }
        // one-character punctuation
        else if(punctuation.contains(String.valueOf(c))){
            charStream.consume();
            return new Token(Token.Type.PUNCTUATION, String.valueOf(c));
        }
        else if(c.equals('.') || Character.isDigit(c)){ // this will also handle '.' operator
            return createNumberToken(); /////////////////////////////////
        }
        // check for three-character lexems
        // check for two-character lexems
        // check for one-character lexems
        // string
        // numbers ('_' in number are valid) (second dot in number is not valid)
        // words (keywords + identifiers) (+ 'not', 'and', 'or' operators)
        // '.' operator (check if number starts or not)
        // 'abs?' is valid identifier
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
}
