package ua.drovolskyi.compilers.lab1;

public class Token {
    private final Type type;
    private final String value;

    public Token(Type type, String value){
        this.type = type;
        this.value = value;
    }

    public Type getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public static enum Type{
        KEYWORD,
        OPERATOR,
        LITERAL_NUMBER,
        LITERAL_STRING,
        IDENTIFIER,
        PUNCTUATION,
        COMMENT,
        WHITESPACE,
        ERROR
    }
}
