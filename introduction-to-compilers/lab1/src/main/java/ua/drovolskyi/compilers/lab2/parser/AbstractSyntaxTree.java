package ua.drovolskyi.compilers.lab2.parser;

public class AbstractSyntaxTree {
    private AstNode root; // is a program

    public AbstractSyntaxTree(AstNode root){
        this.root = root;
    }

    public AstNode getRoot(){
        return this.root;
    }


}
