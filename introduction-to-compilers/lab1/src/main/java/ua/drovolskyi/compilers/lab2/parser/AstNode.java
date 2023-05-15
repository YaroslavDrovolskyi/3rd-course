package ua.drovolskyi.compilers.lab2.parser;

import java.util.ArrayList;
import java.util.List;

// is node of AbstractSyntaxTree
public class AstNode {
    private Type type;
    private String value;
    private List<AstNode> childs;


    public AstNode(Type type, String value) {
        this.type = type;
        this.value = value;
        this.childs = new ArrayList<>();
    }

    public AstNode(Type type, String value, List<AstNode> childs) {
        this(type, value);
        this.childs.addAll(childs);
    }

    public void addChild(AstNode newChild) {
        this.childs.add(newChild);
    }

    public void addChilds(List<AstNode> newChilds) {
        this.childs.addAll(newChilds);
    }

    public String getValue(){
        return this.value;
    }

    public Type getType(){
        return this.type;
    }

    public List<AstNode> getChilds(){
        return this.childs;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();

        sb.append("AstNode{").append(this.type);
        if(this.value != null){
            sb.append(", ").append(this.value);
        }
        sb.append("}");

        return sb.toString();
    }

    public enum Type {
        PROGRAM("PROGRAM"),
        VAR_BINDING("VAR_BINDING"),
        CLASS_DEFINITION("CLASS_DEFINITION"),
        FUNCTION_DEFINITION("FUNCTION_DEFINITION"),
        BINARY_OPERATOR("BINARY_OPERATOR"),
        FUNCTION_CALL("FUNCTION_CALL"),
        LITERAL_NUMBER("LITERAL_NUMBER"),
        LITERAL_STRING("LITERAL_STRING"),
        LITERAL_BOOLEAN("LITERAL_BOOLEAN"),
        NIL("NIL"),
        IDENTIFIER("IDENTIFIER"),
        RETURN("RETURN"),
        FUNCTION_PARAMETERS("FUNCTION_PARAMETERS"),
        BLOCK("BLOCK"),
        CONDITIONAL("CONDITIONAL"),
        CONDITION("CONDITION"),
        REPETITION("REPETITION"),
        EXPRESSION("EXPRESSION"),
        UNARY_OPERATOR("UNARY_OPERATOR");

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
