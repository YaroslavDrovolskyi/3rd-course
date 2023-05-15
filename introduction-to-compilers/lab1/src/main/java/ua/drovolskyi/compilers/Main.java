package ua.drovolskyi.compilers;

import ua.drovolskyi.compilers.lab1.lexer.CharStreamStringImpl;
import ua.drovolskyi.compilers.lab1.lexer.Lexer;
import ua.drovolskyi.compilers.lab1.lexer.LexerParserMediator;
import ua.drovolskyi.compilers.lab2.parser.AbstractSyntaxTree;
import ua.drovolskyi.compilers.lab2.parser.AstVisualizer;
import ua.drovolskyi.compilers.lab2.parser.Parser;
import ua.drovolskyi.compilers.lab2.parser.TokenStream;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        String path = "src/main/resources/";
        String correctFilename = "test_parser_correct";
        String incorrectFilename = "test_parser_incorrect_1";

        tokenizeAndParse(path, correctFilename, "rb");
    }

    public static void tokenizeAndParse(String path, String filename, String fileExtension) throws IOException {
        String code = new String(Files.readAllBytes(Paths.get(path + filename + "." + fileExtension)));

        // lexer
        Lexer lexer = new Lexer(new CharStreamStringImpl(code));
        List<Token> tokens = lexer.tokenize();

        // mediator
        TokenStream tokenStream = LexerParserMediator.prepareForParser(tokens);

        // parser
        Parser parser = new Parser(tokenStream);
        AbstractSyntaxTree ast = parser.parse();

        AstVisualizer visualizer = new AstVisualizer();
        visualizer.visualize(ast, path + filename + ".png");
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