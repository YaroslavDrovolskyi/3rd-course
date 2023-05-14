package ua.drovolskyi.compilers.lab1.parser;

import ua.drovolskyi.compilers.lab1.Token;

import java.util.List;

public class TokenStreamListImpl implements TokenStream{
    private final List<Token> tokens;
    private Integer currentIndex;
    private Integer currentLine = 1;

    public TokenStreamListImpl(List<Token> tokens) {
        this.tokens = tokens;
        this.currentIndex = -1;
    }

    @Override
    public void consume(){
        if(currentIndex > -1){
            if(tokens.get(currentIndex).getValue().equals("\n")){
                currentLine++;
            }
        }
        currentIndex++;
    }

    @Override
    public void consumeTokens(Integer count){
        for(int i = 0; i < count; i++){
            consume();
        }
    }

    @Override
    public void returnToken(){
        if(currentIndex < tokens.size()){
            if(tokens.get(currentIndex).getValue().equals("\n")){
                currentLine--;
            }
        }

        currentIndex--;
    }

    @Override
    public Integer currentLine(){
        return currentLine;
    }

    @Override
    public void returnTokens(Integer count){
        for(int i = 0; i < count; i++){
            returnToken();
        }
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
