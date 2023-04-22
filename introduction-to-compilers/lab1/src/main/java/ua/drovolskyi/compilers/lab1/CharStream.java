package ua.drovolskyi.compilers.lab1;

public interface CharStream {
    String getString(Integer startIndex, Integer endIndex);
    void consume();
    Character getCurrentSymbol();
    Integer getCurrentIndex();
    Boolean isLastSymbol();
    Boolean isEnded();
    String lookahead(Integer numberOfCharacters);
}
