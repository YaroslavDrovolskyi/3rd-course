package ua.drovolskyi.compilers.lab1.parser;

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
        return value;
    }

    public enum Type {
        PROGRAM,
        VAR_BINDING,
        CLASS_DEFINITION,
        FUNCTION_DEFINITION,
        BINARY_OPERATOR,
        FUNCTION_CALL,
        LITERAL_NUMBER,
        LITERAL_STRING,
        LITERAL_BOOLEAN,
        NIL,
        IDENTIFIER,
        RETURN,
        FUNCTION_PARAMETERS,
        BLOCK,
        CONDITIONAL,
        CONDITION
    }
}
