package ua.drovolskyi.compilers.lab1.lexer;

public class CharStreamStringImpl implements CharStream {
    private String string;
    private Integer currentIndex;

    public CharStreamStringImpl(String string) {
        this.string = string;
        this.currentIndex = 0;
    }

    @Override
    public void consume(){
        currentIndex++;
    }

    @Override
    public void consumeCharacters(Integer count){
        currentIndex += count;
    }


    @Override
    public void returnCharacter(){
        currentIndex--;
    }

    @Override
    public void returnCharacters(Integer count){
        currentIndex -= count;
    }

    @Override
    public Character getCurrentSymbol(){
        return string.charAt(currentIndex);
    }

    @Override
    public Integer getCurrentIndex(){
        return currentIndex;
    }

    @Override
    public Boolean isLastSymbol(){
        return currentIndex.equals(string.length() - 1);
    }

    @Override
    public Boolean isEnded(){
        return currentIndex.compareTo(string.length()) >= 0;
    }

    // returns {numberOfSymbols} symbols, starting from symbol with index {currentIndex + 1}
    @Override
    public String lookahead(Integer numberOfCharacters){
        return string.substring(currentIndex + 1, (currentIndex + 1) + numberOfCharacters);
    }

    // returns number of character available after currentCharacter
    @Override
    public Integer availableCharacters(){
        return string.length() - (currentIndex + 1);
    }

    @Override
    public String getString(Integer startIndex, Integer endIndex){
        return string.substring(startIndex, endIndex);
    }
}
