package ua.drovolskyi.compilers.lab2.parser;

import ua.drovolskyi.compilers.Token;

// before first call of .consume() current index is -1 and calling get-methods would be invalid
public interface TokenStream {
    void consume();
    void consumeTokens(Integer count);
    void returnToken();
    void returnTokens(Integer count);
    Token getCurrentToken();
    Integer getCurrentIndex();
    Boolean isLastToken();
    Boolean isEnded();
    Token lookahead(Integer offset);
    Integer availableTokens();

    /**
     * Returns number of line where current token is located
     * @return
     */
    Integer currentLine();
}
