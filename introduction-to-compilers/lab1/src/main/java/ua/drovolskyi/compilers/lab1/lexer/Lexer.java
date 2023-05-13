package ua.drovolskyi.compilers.lab1.lexer;

import ua.drovolskyi.compilers.lab1.Token;
import ua.drovolskyi.compilers.lab1.lexer.CharStream;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Lexer {
    private CharStream charStream;
    private static final String allowedCharacters = "!?+-~*/%&|^><=.:()[]{},;|$@" +
            "0123456789 \t\n\rabcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String whitespaces = " \t\n\r";
    private static final String punctuation = "()[]{},;|";
    private static final List<String> threeCharacterOperators = Arrays.asList(
        "===", "<=>", "**=", "..."
    );
    private static final List<String> twoCharacterOperators = Arrays.asList(
            "**", "<<", ">>", "==", "!=", ">=", "<=", "%=", "/=", "-=", "+=", "*=", "..", "?:", "&&", "||", "::"
    );
    private static final String oneCharacterOperators = "!?+-~*/%&|^><=.:";
    private static final List<String> keywords = Arrays.asList(
            "alias", "and", "BEGIN", "begin", "break", "case",
            "class", "def", "defined?", "do", "else", "elsif",
            "END", "end", "ensure", "false", "for", "if",
            "in", "module", "next", "nil", "not", "or",
            "redo", "rescue", "retry", "return", "self", "super",
            "then", "true", "undef", "unless", "until", "when",
            "while", "yield"
    );

    private static final List<String> sigils = Arrays.asList("$", "@@", "@");

    private final StringBuilder errorLexeme = new StringBuilder();

    public Lexer(CharStream charStream){
        this.charStream = charStream;
    }

    public List<Token> tokenize(){
        List<Token> tokens = new LinkedList<>();
        while(!charStream.isEnded()){
            Token newToken = extractToken();
            if(newToken != null){
                tokens.add(newToken);
            }
        }
        return tokens;
    }

    // start of next token must be the currentSymbol
    // this function always returns token except when it is forbidden character
    private Token extractToken(){
        Character c = charStream.getCurrentSymbol();

        // if current character is allowed
        // check if previous character was forbidden
        if(allowedCharacters.contains(String.valueOf(c))){
            if(!errorLexeme.isEmpty()){
                String lexeme = errorLexeme.toString();
                errorLexeme.setLength(0);
                return new Token(Token.Type.ERROR, errorLexeme.toString());
            }
        }

        if(whitespaces.contains(String.valueOf(c))){
            Token token = new Token(Token.Type.WHITESPACE, String.valueOf(c));
            charStream.consume();
            return token;
        }
        else if(c.equals('#')){
            return createCommentToken(); // return comment token to the end of line or EOF
        }
        // three-characters operators
        else if(charStream.availableCharacters() >= 2 &&
                threeCharacterOperators.contains(c + charStream.lookahead(2))){
            Token token = new Token(Token.Type.OPERATOR, c + charStream.lookahead(2));
            charStream.consumeCharacters(3);
            return token;
        }
        // two-characters operators
        else if(charStream.availableCharacters() >= 1 &&
                twoCharacterOperators.contains(c + charStream.lookahead(1))){
            Token token = new Token(Token.Type.OPERATOR, c + charStream.lookahead(1));
            charStream.consumeCharacters(2);
            return token;
        }
        // one-character operators
        else if(oneCharacterOperators.contains(String.valueOf(c))){
            Token token = new Token(Token.Type.OPERATOR, String.valueOf(c));
            charStream.consume();
            return token;
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
        else if(c.equals('\"') || c.equals('\'')){
            return createLiteralStringToken(c);
        }
        else if(charStream.availableCharacters() >= 1 &&
                (c + charStream.lookahead(1)).equals("@@")){
            charStream.consumeCharacters(2);
            return new Token(Token.Type.SIGIL, "@@");
        }
        else if(c.equals('@') || c.equals('$')){
            charStream.consume();
            return new Token(Token.Type.SIGIL, String.valueOf(c));
        }
        else {
            Token token = tryCreateKeywordToken();

            // token is keyword
            if(token != null){
                return token;
            }

            // if current character is start of identifier
            if(Character.isAlphabetic(c) ||
                    c.equals('_')){ //
                return createIdentifierToken();
            }
            else{
                errorLexeme.append(c);
                charStream.consume();
                if(charStream.isEnded()){
                    String lexeme = errorLexeme.toString();
                    errorLexeme.setLength(0);
                    return new Token(Token.Type.ERROR, lexeme);
                }
                else{
                    return null;
                }

            }
        }

        // + when we make lookahead, check if we don't go out of bounds
        // + errors
        // + check for three-character lexems
        // + check for two-character lexems
        // + check for one-character lexems
        // + string (ADD possibility for '' and "" strings)
        // + numbers ('_' in number are valid) (second dot in number is not valid)
        // + words (keywords + identifiers) (+ 'not', 'and', 'or' operators)
        // + 'abs?' or 'abs!' are valid identifiers
        // + sigils
    }

    private Token createIdentifierToken(){
        // current character is alphabetic or _

        StringBuilder lexeme = new StringBuilder();
        lexeme.append(charStream.getCurrentSymbol());
        charStream.consume();

        while(!charStream.isEnded()){
            Character c = charStream.getCurrentSymbol();

            if(!Character.isDigit(c) &&
                    !Character.isAlphabetic(c) &&
                    !c.equals('_')){
                return new Token(Token.Type.IDENTIFIER, lexeme.toString());
            }

            lexeme.append(c);
            charStream.consume();
        }

        return new Token(Token.Type.IDENTIFIER, lexeme.toString());
    }

    private Token createLiteralStringToken(Character terminatingCharacter){
        // currentCharacter is " or '

        StringBuilder lexeme = new StringBuilder();
        lexeme.append(charStream.getCurrentSymbol());
        charStream.consume();

        while(!charStream.isEnded()){
            Character c = charStream.getCurrentSymbol();

            if(c.equals(terminatingCharacter)){
                // if before terminatingCharacter we haven't \ (check for escape sequence \")
                if(lexeme.lastIndexOf("\\") != lexeme.length() - 1){
                    lexeme.append(c);
                    charStream.consume();
                    return new Token(Token.Type.LITERAL_STRING, lexeme.toString());
                }
            }

            lexeme.append(c);
            charStream.consume();
        }

        // stream is ended, but matching terminatingCharacter is not found
        return new Token(Token.Type.ERROR, lexeme.toString());
    }

    // returns comment token to the end of line or end of charStream
    // \n is not included into token
    private Token createCommentToken(){
        // currentCharacter is #

        StringBuilder lexeme = new StringBuilder();
        lexeme.append(charStream.getCurrentSymbol());
        charStream.consume();

        while(!charStream.isEnded()){
            Character c = charStream.getCurrentSymbol();

            if(c.equals('\n')){
                return new Token(Token.Type.COMMENT, lexeme.toString());
            }

            lexeme.append(c);
            charStream.consume();
        }

        return new Token(Token.Type.COMMENT, lexeme.toString());
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
            else if(c.equals('_')){
                if(lexeme.lastIndexOf("_") == lexeme.length() - 1 ||
                        lexeme.lastIndexOf(".") == lexeme.length() - 1){
                    return createNumberTokenImpl(lexeme);
                }
            }
            else if(!Character.isDigit(c)){
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

    private Token tryCreateKeywordToken(){
        Character c = charStream.getCurrentSymbol();
        for(String keyword : keywords){
            if(charStream.availableCharacters() >= keyword.length() - 1 &&
                    keyword.equals(c + charStream.lookahead(keyword.length() - 1))){
                charStream.consumeCharacters(keyword.length());

                // if it is not a keyword, but a start of identifier
                if(!charStream.isEnded()){
                    Character afterKeywordCharacter = charStream.getCurrentSymbol();
                    if(Character.isDigit(afterKeywordCharacter) ||
                            Character.isAlphabetic(afterKeywordCharacter) ||
                            afterKeywordCharacter.equals('_')) {
                        charStream.returnCharacters(keyword.length());
                        return null;
                    }
                }

                return new Token(Token.Type.KEYWORD, keyword);
            }
        }
        return null;
    }
}
