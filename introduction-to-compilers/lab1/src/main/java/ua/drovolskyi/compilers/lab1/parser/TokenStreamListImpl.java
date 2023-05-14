package ua.drovolskyi.compilers.lab1.parser;

import ua.drovolskyi.compilers.lab1.Token;

import java.util.List;

public class TokenStreamListImpl implements TokenStream{
    private final List<Token> tokens;
    private Integer currentIndex;

    public TokenStreamListImpl(List<Token> tokens) {
        this.tokens = tokens;
        this.currentIndex = -1;
    }

    @Override
    public void consume(){
        currentIndex++;
    }

    @Override
    public void consumeTokens(Integer count){
        currentIndex += count;
    }

    @Override
    public void returnToken(){
        currentIndex--;
    }

    @Override
    public void returnTokens(Integer count){
        currentIndex -= count;
    }

    @Override
    public Token getCurrentToken(){
        return tokens.get(currentIndex);
    }

    @Override
    public Integer getCurrentIndex(){
        return currentIndex;
    }

    @Override
    public Boolean isLastToken(){
        return currentIndex.equals(tokens.size() - 1);
    }

    @Override
    public Boolean isEnded(){
        return currentIndex.compareTo(tokens.size()) >= 0;
    }

    @Override
    public Token lookahead(Integer offset){
        return tokens.get(currentIndex + offset);
    }

    // returns number of tokens available after currentToken
    @Override
    public Integer availableTokens(){
        return tokens.size() - (currentIndex + 1);
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();

        sb.append("TokenStream{");
        if(this.currentIndex.equals(-1)){
            sb.append("NOT STARTED");
        }
        if(isEnded()){
            sb.append("ENDED");
        }
        else{
            sb.append("current: ").append(getCurrentToken()).append(", ");
            sb.append("next: ");
            if(!isLastToken()){
                sb.append(tokens.get(currentIndex + 1));
            }
            else{
                sb.append("NO NEXT TOKEN");
            }
        }
        sb.append("}");

        return sb.toString();
    }
}
