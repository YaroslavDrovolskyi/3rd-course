package ua.drovolskyi.compilers.lab1.lexer;

public interface CharStream {
    String getString(Integer startIndex, Integer endIndex);
    void consume();
    void consumeCharacters(Integer count);
    void returnCharacter();
    void returnCharacters(Integer count);
    Character getCurrentSymbol();
    Integer getCurrentIndex();
    Boolean isLastSymbol();
    Boolean isEnded();
    String lookahead(Integer numberOfCharacters);
    Integer availableCharacters();
}
