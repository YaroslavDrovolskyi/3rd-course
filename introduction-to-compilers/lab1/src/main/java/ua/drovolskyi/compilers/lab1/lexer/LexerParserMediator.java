package ua.drovolskyi.compilers.lab1.lexer;

import ua.drovolskyi.compilers.Token;
import ua.drovolskyi.compilers.lab2.parser.TokenStream;
import ua.drovolskyi.compilers.lab2.parser.TokenStreamListImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

/**
 * <ul>
 *     Prepare output of Lexer to input for Parser:
 *     <li>Removes COMMENT, ERROR, (WHITESPACE && non-terminator)-tokens from list of tokens</li>
 *     <li>If SIGIL goes exactly before IDENTIFIER, it will be added to IDENTIFIER lexeme.
 *     SIGIL will be removed as a token.
 *     Cases with sigil-started tokens as names of functions will be handled by Parser.</li>
 *     <li>If '?'(or '!') goes exactly after IDENTIFIER, it will be added to IDENTIFIER lexeme.
 *     '?'(or '!') will be removed as a token.
 *     Cases with '?'(or '!')'-ended tokens as name of variables will be handled by Parser</li>
 * </ul>
 * <p>To perform all this work call .prepareForParser() method.</p>
 */
public class LexerParserMediator {
    private static final List<String> SIGILS = Arrays.asList("$", "@@", "@");
    private static final List<String> SPECIAL_MARKS = Arrays.asList("!", "?");

    public static TokenStream prepareForParser(List<Token> tokens){
        List<Token> preparedTokens = new ArrayList<>(); // need to fill this list by prepared tokens

        ListIterator<Token> it = tokens.listIterator();
        while(it.hasNext()){
            Token currentToken = it.next();

            // skip odd tokens
            if(currentToken.getType() == Token.Type.COMMENT){
                continue;
            }
            if(currentToken.getType() == Token.Type.WHITESPACE &&
                    !isTerminator(currentToken)){
                continue;
            }


            if(currentToken.getType() == Token.Type.IDENTIFIER){
                if(it.hasNext()){
                    Token nextToken = it.next();
                    if(SPECIAL_MARKS.contains(nextToken.getValue())){ // check for [IDENTIFIER]{? | !}
                        preparedTokens.add(
                                new Token(Token.Type.IDENTIFIER, currentToken.getValue() + nextToken.getValue()));
                    }
                    else{
                        preparedTokens.add(new Token(currentToken.getType(), currentToken.getValue()));
                        it.previous();
                    }
                }
                else{
                    preparedTokens.add(new Token(currentToken.getType(), currentToken.getValue()));
                }
            }
            else if(currentToken.getType() == Token.Type.SIGIL){
                if(it.hasNext()){
                    Token nextToken = it.next();
                    if(nextToken.getType() == Token.Type.IDENTIFIER){ // check for [SIGIL][IDENTIFIER]
                        preparedTokens.add(
                                new Token(Token.Type.IDENTIFIER, currentToken.getValue() + nextToken.getValue()));
                    }
                    else{
                        preparedTokens.add(new Token(currentToken.getType(), currentToken.getValue()));
                        it.previous();
                    }
                }
                else{
                    preparedTokens.add(new Token(currentToken.getType(), currentToken.getValue()));
                }
            }
            else{
                preparedTokens.add(new Token(currentToken.getType(), currentToken.getValue()));
            }
        }

        return new TokenStreamListImpl(preparedTokens);
    }

    // check if token is terminating string (\n or ;)
    private static Boolean isTerminator(Token t){
        return t.getValue().equals("\n") ||
                t.getValue().equals(";");
    }
}
