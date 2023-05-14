package ua.drovolskyi.compilers.lab1;

public class Token {
    private final Type type;
    private String value;

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

    public void setValue(String value){
        this.value = value;
    }

    public static enum Type{
        KEYWORD("KEYWORD"),
        OPERATOR("OPERATOR"),
        LITERAL_NUMBER("LITERAL_NUMBER"),
        LITERAL_STRING("LITERAL_STRING"),
        IDENTIFIER("IDENTIFIER"),
        PUNCTUATION("PUNCTUATION"),
        COMMENT("COMMENT"),
        WHITESPACE("WHITESPACE"),
        SIGIL("SIGIL"),
        ERROR("ERROR");

        private final String label;

        private Type(String label){
            this.label = label;
        }

        @Override
        public String toString(){
            return this.label;
        }
    }
}
