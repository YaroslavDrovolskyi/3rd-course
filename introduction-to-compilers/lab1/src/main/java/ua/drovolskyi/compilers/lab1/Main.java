package ua.drovolskyi.compilers.lab1;

import ua.drovolskyi.compilers.lab1.lexer.CharStreamStringImpl;
import ua.drovolskyi.compilers.lab1.lexer.Lexer;
import ua.drovolskyi.compilers.lab1.lexer.LexerParserMediator;
import ua.drovolskyi.compilers.lab1.parser.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        String correct = "src/main/resources/test_parser_correct.rb";
        String incorrect = "src/main/resources/test_parser_incorrect_1.rb";
        String filename = incorrect;
        String code = new String(Files.readAllBytes(Paths.get(filename)));

        // lexer
        Lexer lexer = new Lexer(new CharStreamStringImpl(code));
        List<Token> tokens = lexer.tokenize();

        // mediator
        TokenStream tokenStream = LexerParserMediator.prepareForParser(tokens);

        // parser
        Parser parser = new Parser(tokenStream);
        AbstractSyntaxTree ast = parser.parse();

        AstVisualizer visualizer = new AstVisualizer();
        visualizer.visualize(ast, "src/main/resources/test_parser_incorrect_1.png");

//        System.out.println("Tokens:");
//        printTokensList(tokens);
//        printTokensListWithoutWhitespaces(tokens);
    }



    private static void printTokensList(List<Token> tokens){
        if(tokens.isEmpty()){
            System.out.println("TOKENS LIST IS EMPTY");
        }

        for(Token token : tokens){
            String lexeme = token.getValue();
            String lexemeToPrint = lexeme;
            if(lexeme.equals("\n")){
                lexemeToPrint = "\\n";
            }
            else if(lexeme.equals(" ")){
                lexemeToPrint = "[SPACE]";
            }
            else if(lexeme.equals("\t")){
                lexemeToPrint = "\\t";
            }
            else if(lexeme.equals("\r")){
                lexemeToPrint = "\\r";
            }
            System.out.println(token.getType() + "\t\t\t\t\t" + lexemeToPrint);
        }
    }

    private static void printTokensListWithoutWhitespaces(List<Token> tokens){
        if(tokens.isEmpty()){
            System.out.println("TOKENS LIST IS EMPTY");
        }

        for(Token token : tokens){
            if(token.getType() != Token.Type.WHITESPACE){
                System.out.println(token.getType() + "\t\t\t\t\t" + token.getValue());
            }
        }
    }
}