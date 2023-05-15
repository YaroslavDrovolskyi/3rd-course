package ua.drovolskyi.compilers.lab1.parser;

// import ua.drovolskyi.compilers.lab1.parser.AbstractSyntaxTree.Node;

import ua.drovolskyi.compilers.lab1.Token;

import java.util.*;

import static java.util.Map.entry;

public class Parser {
    private static final List<String> UNARY_OPERATORS = Arrays.asList("-", "!", "~", "+");
    private static final List<String> BINARY_OPERATORS = // describe math and logical operators
            Arrays.asList("+", "-", "*", "**", "/", "%", "==", "!=", "^", "%=", "/=", "-=", "+=", "*=",
                    ">", "<", ">=", "<=", ".", "===", "<=>", "**=", "...", "..,",
                    "<<", ">>", "=~", "!~", "?", ":", "defined?",
                    "and", "or", "not", "&&", "||", "|");
    private static final List<String> LOGICAL_OPERATORS =
            Arrays.asList();
    private static final List<String> SIGILS = Arrays.asList("$", "@@", "@");

    private final Integer LOWEST_PRECEDENCE = 0;
    private final Integer MAX_PRECEDENCE = 19;

    // operators with bigger precedence should be executed first
    private final Map<String, Integer> operatorsPrecedence = Map.ofEntries(
            entry("(", 20),

            entry(".", 18), // calling method is also operator

            entry("!", 17),
            entry("~", 17),

            entry("**", 16),

            entry("*", 15),
            entry("/", 15),
            entry("%", 15),

            entry("+", 14),
            entry("-", 14),

            entry("<<", 13),
            entry(">>", 13),

            entry("&", 12),

            entry("|", 11),
            entry("^", 11),

            entry(">", 10),
            entry(">=", 10),
            entry("<", 10),
            entry("<=", 10),

            entry("<=>", 9),
            entry("==", 9),
            entry("===", 9),
            entry("!=", 9),
            entry("=~", 9),
            entry("!~", 9),

            entry("&&", 8),

            entry("||", 7),

            entry("..,", 6),
            entry("...", 6),

            entry("?", 5),
            entry(":", 5),

            entry("%=", 4),
            entry("/=", 4),
            entry("-=", 4),
            entry("+=", 4),
            entry("*=", 4),
            entry("**=", 4),

            entry("defined?", 3),

            entry("not", 2),

            entry("or", 1),
            entry("and", 1)
    );

        //new TreeMap<>(String::compareTo);





    private final TokenStream tokenStream;

    public Parser(TokenStream tokenStream){
        this.tokenStream = tokenStream;
    }

    ////////////////////////////////////// Tasks
    // + in lexer transform SIGIL + IDENTIFIER => IDENTIFIER
    // + in lexer transform (IDENTIFIER + !) or (IDENTIFIER + ?) => IDENTIFIER
    // + remove (WHITESPACE && non-terminator) and COMMENT from TokenStream
    // + Problem: child do not added to nodes
    // + add unless structure
    // + if variable found, check if its value does not end with ? or '!'
    // + in function definition check if identifier does not start from SIGIL
    // + add operator of method call (make it operation with highest priority)
    // add FUNCTION_BODY
    // + add value for UNARY_OPERTAOR


    public AbstractSyntaxTree parse(){
        AstNode root =
                new AstNode(AstNode.Type.PROGRAM, null);

        while(!tokenStream.isEnded()){
            tokenStream.consume();
            // here, currentToken is start of new expression

            AstNode expression = null;
            if(!tokenStream.isEnded()){
                expression = parseExpressionRecursively(LOWEST_PRECEDENCE);
            }

            if(expression != null){
                root.addChild(expression);
            }
        }
        return new AbstractSyntaxTree(root);
    }

    // precedence is precedence of parent node
    private AstNode parseExpressionRecursively(Integer precedence){
        AstNode expr = parseSimpleExpression();
        if(expr == null){
            // here unrecognized token error (is already reported) or \n or ; reached
            return null;
        }

        while(tokenStream.availableTokens() >= 1 &&
                !nextIsTerminator() && precedence < nextPrecedence()){
            String nextLexeme = tokenStream.lookahead(1).getValue();

            if(BINARY_OPERATORS.contains(nextLexeme) ||
                    LOGICAL_OPERATORS.contains(nextLexeme)){
                tokenStream.consume();
                expr = parseBinaryOperator(expr);
            }
            else if (nextLexeme.equals("(")){
                tokenStream.consume();
                expr = parseFunctionCall(expr.getValue());
            }
            else{
                return expr;
            }
        }

        return expr;
    }

    /**
     * It is called if prevExpression.priority < currentToken.priority.
     * prevExpression will be parent of currentToken. <br/>
     * Current token is some binary math or binary logic operation <br/>
     * Parse binary operation up to end of line
     */
    private AstNode parseBinaryOperator(AstNode leftChild){
        AstNode operation = new AstNode(AstNode.Type.BINARY_OPERATOR,
                tokenStream.getCurrentToken().getValue(), Arrays.asList(leftChild));

        if(!tokenStream.isLastToken()){
            if(!nextIsTerminator()){
                Integer operationPrecedence = currentPrecedence();
                tokenStream.consume();
                operation.addChild(parseExpressionRecursively(operationPrecedence));
            }
            else{
                reportUnexpectedToken("end of line", "right operand");
            }
        }
        else{
            reportUnexpectedEndOfFile("right operand");
        }

        return operation;
    }

    /**
     * Current token is '('
     */
    private AstNode parseFunctionCall(String identifier){
        return new AstNode(AstNode.Type.FUNCTION_CALL, identifier, parseFunctionCallArgs());
    }

    /**
     * Current token is '(' <br/>
     * Current token after execution is ')'
     */
    private List<AstNode> parseFunctionCallArgs(){
        List<AstNode> args = new ArrayList<>();

        tokenStream.consume();

        if(reportIfUnexpectedEnd()){
            return args;
        }

        // function call without arguments
        if(tokenStream.getCurrentToken().getValue().equals(")")){
            return args;
        }

        // function has arguments
        args.add(parseExpressionRecursively(LOWEST_PRECEDENCE));
        while(tokenStream.availableTokens() >= 2
                && tokenStream.lookahead(1).getValue().equals(",")){
            tokenStream.consumeTokens(2);
            args.add(parseExpressionRecursively(LOWEST_PRECEDENCE));
        }

        if(reportIfUnexpectedEnd()){
            return args;
        }

        if(tokenStream.isLastToken()){
            reportUnexpectedEndOfFile("')'");
            return args;
        }

        // found matching ')'
        if(tokenStream.lookahead(1).getValue().equals(")")){
            tokenStream.consume();
            return args;
        }

        reportUnexpectedToken(tokenStream.lookahead(1).getValue(), "')'");
        return args;
    }

    private AstNode parseSimpleExpression(){
        if(tokenStream.isEnded() || isTerminator(tokenStream.getCurrentToken())){
            return null;
        }

        Token t = tokenStream.getCurrentToken();
        if(t.getType() == Token.Type.LITERAL_NUMBER){
            return new AstNode(AstNode.Type.LITERAL_NUMBER, t.getValue());
        }
        else if(t.getType() == Token.Type.LITERAL_STRING){
            return new AstNode(AstNode.Type.LITERAL_STRING, t.getValue());
        }
        else if(t.getValue().equals("true") ||
                t.getValue().equals("false")){
            return new AstNode(AstNode.Type.LITERAL_BOOLEAN, t.getValue());
        }
        else if(t.getValue().equals("nil")){
            return new AstNode(AstNode.Type.NIL, t.getValue());
        }
        else if(t.getType() == Token.Type.IDENTIFIER){ // parse variable or variable binding
            if(t.getValue().endsWith("?") || t.getValue().endsWith("!")){
                reportError("Variable name can't end with '?' or '!'");
            }
            return parseIdentifier();
        }
        else if (t.getValue().equals("return")){
            return parseReturn();
        }
        else if(t.getValue().equals("class")){
            return parseClassDefinition();
        }
        else if(t.getValue().equals("def")){
            return parseFunctionDefinition();
        }
        else if(t.getValue().equals("if") || t.getValue().equals("unless")){
            return parseConditional();
        }
        else if(t.getValue().equals("while") || t.getValue().equals("until")){
            return parseRepetition();
        }
        else if(t.getValue().equals("(")){
            return parseGroupedExpression();
        }
        else if(UNARY_OPERATORS.contains(t.getValue())){
            return parseUnaryOperator();
        }

        reportUnexpectedToken("'" + t.getValue() + "'");
        return null;
    }

    /**
     * Current token is '(' <br/>
     * Current token after execution is ')'
     * @return
     */
    private AstNode parseGroupedExpression(){
        tokenStream.consume();
        AstNode expr = parseExpressionRecursively(LOWEST_PRECEDENCE);

        if(!tokenStream.isEnded() &&
                tokenStream.lookahead(1).getValue().equals(")")){
            tokenStream.consume();
            return expr;
        }

        return null;
    }

    /**
     * Current token is unary operator
     * @return
     */
    private AstNode parseUnaryOperator(){
        AstNode node = new AstNode(AstNode.Type.UNARY_OPERATOR, tokenStream.getCurrentToken().getValue());
        tokenStream.consume();
        node.addChild(parseExpressionRecursively(MAX_PRECEDENCE));

        return node;
    }


    /**
     * Current token is 'while' or 'until' <br/>
     * Current token after execution is 'end'
     */
    private AstNode parseRepetition(){
        AstNode repetitionNode = new AstNode(AstNode.Type.REPETITION, tokenStream.getCurrentToken().getValue());
        tokenStream.consume();

        if(tokenStream.isEnded()){
            reportUnexpectedEndOfFile("condition");
            return null;
        }
        if(tokenStream.getCurrentToken().getValue().equals("\n")){
            reportUnexpectedToken("new line", "condition");
            return null;
        }

        // condition of repetition
        AstNode condition = new AstNode(AstNode.Type.CONDITION, null);
        condition.addChild(parseExpressionRecursively(LOWEST_PRECEDENCE));
        repetitionNode.addChild(condition);
        // here current tokens is last token of condition expression

        tokenStream.consume();
        if(tokenStream.isEnded()){
            reportUnexpectedEndOfFile("new line");
            return null;
        }
        repetitionNode.addChild(parseBlock());

        if(tokenStream.isEnded()){
            reportUnexpectedEndOfFile("'end'");
        }
        else if(!tokenStream.getCurrentToken().getValue().equals("end")){
            reportUnexpectedToken("'" + tokenStream.getCurrentToken().getValue() + "'", "'end'");
        }

        return repetitionNode;
    }

    /**
     * Class is sequence of methods
     * Current token is 'class' <br/>
     * Current token after execution is 'end' of class
     */
    private AstNode parseClassDefinition(){
        if(!tokenStream.isLastToken()){
            tokenStream.consume();
            Token currentToken = tokenStream.getCurrentToken();

            if(currentToken.getType() == Token.Type.IDENTIFIER){
                String lexeme = currentToken.getValue();
                if(!lexeme.endsWith("?") && !lexeme.endsWith("!") &&
                        !lexeme.startsWith("$") && !lexeme.startsWith("@") && !lexeme.startsWith("@@")){
                    AstNode classNode = new AstNode(AstNode.Type.CLASS_DEFINITION, lexeme);

                    if(tokenStream.isLastToken()){
                        reportUnexpectedEndOfFile("new line");
                        return classNode;
                    }
                    else if(!isTerminator(tokenStream.lookahead(1))){
                        reportUnexpectedToken(tokenStream.lookahead(1).getValue(), "new line");
                        return classNode;
                    }

                    tokenStream.consume();

                    // skip \n
                    while(!tokenStream.isLastToken() && isTerminator(tokenStream.lookahead(1))){
                        tokenStream.consume();
                    }

                    while(!tokenStream.isLastToken() && tokenStream.lookahead(1).getValue().equals("def")){
                        tokenStream.consume();
                        classNode.addChild(parseFunctionDefinition());
                        // here current token is 'end' of function

                        while(!tokenStream.isLastToken() && isTerminator(tokenStream.lookahead(1))){
                            tokenStream.consume();
                        }
                    }

                    if(tokenStream.isLastToken()){
                        reportUnexpectedEndOfFile("'end'");
                    }
                    else if(tokenStream.lookahead(1).getValue().equals("end")){
                        tokenStream.consume();
                    }
                    else{
                        reportUnexpectedToken(tokenStream.lookahead(1).getValue(), "'end'");
                    }
                    return classNode;
                }
                else{
                    reportUnexpectedToken(lexeme, "valid class name");
                }
            }
            else{
                reportUnexpectedToken(currentToken.getValue(), "identifier");
            }
        }
        else{
            reportUnexpectedEndOfFile("class name");
        }

        return null;
    }


    /**
     * Current token is 'if' <br/>
     * Current token after execution is 'end' of parsed conditional
     */
    private AstNode parseConditional(){
        if(tokenStream.isLastToken()){
            reportUnexpectedEndOfFile("condition");
            return null;
        }

        AstNode conditional = new AstNode(AstNode.Type.CONDITIONAL, null);

        AstNode condition = new AstNode(AstNode.Type.CONDITION, "if");
        tokenStream.consume();

        if(tokenStream.getCurrentToken().getValue().equals("\n")){
            reportUnexpectedToken("end of line","condition");
            return null;
        }

        // add condition itself for 'if' statement as its left child
        condition.addChild(parseExpressionRecursively(LOWEST_PRECEDENCE));

        // add execution block for 'if' statement as its right child
        if(!tokenStream.isLastToken()){
            if(isTerminator(tokenStream.lookahead(1))){
                condition.addChild(parseBlock());
            }
            else{
                reportUnexpectedToken(tokenStream.lookahead(1).getValue(), "new line");
            }
        }
        else{
            reportUnexpectedEndOfFile("new line");
            return conditional;
        }
        conditional.addChild(condition);

        // here current token is 'end' or 'else' or 'elif'

        while(!tokenStream.isEnded() && tokenStream.getCurrentToken().getValue().equals("elsif")){
            condition = new AstNode(AstNode.Type.CONDITION, "elsif");
            tokenStream.consume();

            if(tokenStream.isEnded()){
                reportUnexpectedEndOfFile("condition");
                return null;
            }

            if(tokenStream.getCurrentToken().getValue().equals("\n")){
                reportUnexpectedToken("end of line","condition");
                return null;
            }

            // add condition itself for 'elsif' statement as its left child
            condition.addChild(parseExpressionRecursively(LOWEST_PRECEDENCE));

            // add execution block for 'elsif' statement as its right child
            if(!tokenStream.isLastToken()){
                if(isTerminator(tokenStream.lookahead(1))){
                    condition.addChild(parseBlock());
                }
                else{
                    reportUnexpectedToken(tokenStream.lookahead(1).getValue(), "new line");
                }
            }
            else{
                reportUnexpectedEndOfFile("new line");
            }

            conditional.addChild(condition);
        }

        if(!tokenStream.isEnded() && tokenStream.getCurrentToken().getValue().equals("else")){
            condition = new AstNode(AstNode.Type.CONDITION, "else");

            // add execution block for 'else' statement as its only child
            if(!tokenStream.isLastToken()){
                if(isTerminator(tokenStream.lookahead(1))){
                    condition.addChild(parseBlock());
                }
                else{
                    reportUnexpectedToken(tokenStream.lookahead(1).getValue(), "new line");
                }
            }
            else{
                reportUnexpectedEndOfFile("new line");
            }

            conditional.addChild(condition);
        }

        return conditional;
    }


    /**
     * Current token is 'def' <br/>
     * Current token after execution is 'end' of parsed function
     * @return
     */
    private AstNode parseFunctionDefinition(){
        if(!tokenStream.isLastToken()){
            Token nextToken = tokenStream.lookahead(1);
            if(isValidFunctionName(nextToken)){
                tokenStream.consume();
                AstNode functionNode = new AstNode(AstNode.Type.FUNCTION_DEFINITION, nextToken.getValue());
                tokenStream.consume();

                if(!reportIfUnexpectedEnd()){
                    if(tokenStream.getCurrentToken().getValue().equals("(")){
                        functionNode.addChild(parseFunctionParams()); // add parameters

                        // here current token is ')' or ',' or out of bounds of tokenStream
                        if(tokenStream.isEnded()){
//                            reportUnexpectedEndOfFile("new line");
                            return functionNode;
                        }
                        tokenStream.consume();
                        if(tokenStream.isEnded()){
                            reportUnexpectedEndOfFile("new line");
                            return functionNode;
                        }
                        if(!isTerminator(tokenStream.getCurrentToken())){
                            reportUnexpectedToken(tokenStream.getCurrentToken().getValue(), "new line");
                            return functionNode;
                        }

                        functionNode.addChild(parseBlock()); // add body
                        // here current token is 'end' or out of bounds of tokenStream

                        return functionNode;
                    }
                    else{
                        reportUnexpectedToken(tokenStream.getCurrentToken().getValue(), "'('");
                        return functionNode;
                    }
                }
                else{
                    return functionNode;
                }
            }
            else{
                reportUnexpectedToken(nextToken.getValue(), "valid function name");
                return null;
            }
        }

        reportUnexpectedEndOfFile("function name");
        return new AstNode(AstNode.Type.FUNCTION_DEFINITION, null);
    }

    /**
     * Check if t.getValue() is valid function name.
     * Valid function name is binary operator, or identifier that does not start with sigil
     * @param t is function name
     * @return
     */
    private Boolean isValidFunctionName(Token t){
        if(t.getType() == Token.Type.IDENTIFIER){
            for(String sigil : SIGILS){
                if (t.getValue().startsWith(sigil)) {
                    return false; // @abs can't be function name
                }
            }
            return true;
        }
        else if (BINARY_OPERATORS.contains(t.getValue())){
            return true;
        }
        return false;
    }

    /**
     * Parse block of code that must end with 'end' <br/>
     * Current token is token that is previous to first token of block <br/>
     * Current token after execution is 'end' or 'elsif' or 'else'
     * @return
     */
    private AstNode parseBlock(){
        tokenStream.consume();
        AstNode block = new AstNode(AstNode.Type.BLOCK, null);

        while(!tokenStream.isEnded() &&
                !tokenStream.getCurrentToken().getValue().equals("end") &&
                !tokenStream.getCurrentToken().getValue().equals("else") &&
                !tokenStream.getCurrentToken().getValue().equals("elsif")){
            AstNode expr = parseExpressionRecursively(LOWEST_PRECEDENCE);
            if(expr != null){
                block.addChild(expr);
            }
            tokenStream.consume();
        }

        if(tokenStream.isEnded()){
            reportUnexpectedEndOfFile("'end' or 'else' or 'elif'");
        }
        // else we reached 'end' or 'else' or 'elsif'

        return block;
    }

    /**
     * Current token is '(' <br/>
     * Current token after execution is ')'
     * @return
     */
    private AstNode parseFunctionParams(){
        AstNode params = new AstNode(AstNode.Type.FUNCTION_PARAMETERS, null);

        tokenStream.consume();

        if(reportIfUnexpectedEnd()){
            return params;
        }

        // function definition without arguments
        if(tokenStream.getCurrentToken().getValue().equals(")")){
            return params;
        }

        // function has arguments
        params.addChild(new AstNode(AstNode.Type.IDENTIFIER, tokenStream.getCurrentToken().getValue()));
        // here current token is identifier
        while(!tokenStream.isLastToken() && tokenStream.lookahead(1).getValue().equals(",")){
            tokenStream.consume();
            // current token is ','

            if(!tokenStream.isLastToken()){
                if(tokenStream.lookahead(1).getType() == Token.Type.IDENTIFIER){
                    tokenStream.consume();
                    params.addChild(new AstNode(AstNode.Type.IDENTIFIER, tokenStream.getCurrentToken().getValue()));
                }
                else {
                    reportUnexpectedToken(tokenStream.lookahead(1).getValue(), "identifier");
                    return params;
                }

                if(reportIfUnexpectedEnd()){
                    return params;
                }
            }
            else{
                reportUnexpectedEndOfFile("identifier");
                return params;
            }
        }
        tokenStream.consume();

        if(reportIfUnexpectedEnd()){
            return null;
        }

        if(!tokenStream.getCurrentToken().getValue().equals(")")){
            reportUnexpectedToken(tokenStream.getCurrentToken().getValue(), "')'");
        }
        return params;
    }

    /**
     * Current token is 'return'. Parse expression up to line end
     */
    private AstNode parseReturn(){
        tokenStream.consume();

        AstNode arg = null;
        if(!reportIfUnexpectedEnd()){
            arg = parseExpressionRecursively(LOWEST_PRECEDENCE);
        }

        return new AstNode(AstNode.Type.RETURN, null, Arrays.asList(arg));
    }


    /**
     * Current token is identifier
     */
    private AstNode parseIdentifier(){
        AstNode identifierNode = new AstNode(AstNode.Type.IDENTIFIER, tokenStream.getCurrentToken().getValue());

        if(!tokenStream.isLastToken()){
            Token nextToken = tokenStream.lookahead(1);

            if(nextToken.getValue().equals("=")){ // next token is '='
                return parseVarBinding();
            }

            if(isTerminator(nextToken)){ // next token is '\n' or ';'
                return identifierNode;
            }

            if(BINARY_OPERATORS.contains(nextToken.getValue()) ||
                    LOGICAL_OPERATORS.contains(nextToken.getValue())){ // if next token is operation
                // return because operation expression will be parsed above in parseExpressionRecursively()
                return identifierNode;
            }
        }
        return identifierNode;
    }

    /**
     * Current token is identifier, next token is '='
     */
    private AstNode parseVarBinding(){
        AstNode identifier = new AstNode(AstNode.Type.IDENTIFIER, tokenStream.getCurrentToken().getValue());
        AstNode rightSide = null;

        tokenStream.consumeTokens(2);

        if(!reportIfUnexpectedEnd()){
            rightSide = parseExpressionRecursively(LOWEST_PRECEDENCE);
        }

        return new AstNode(AstNode.Type.VAR_BINDING, null,
                Arrays.asList(identifier, rightSide));
    }

    /**
     * Reports about unexpected end of expression / file. <br/>
     * Current token must be next to known part of currently parsed expression
     * @return true if error occurred, false otherwise
     */
    private Boolean reportIfUnexpectedEnd(){
        if(!tokenStream.isEnded()){
            if(isTerminator(tokenStream.getCurrentToken())){
                reportError("unexpected end of line");
                return true;
            }
        }
        else{
            reportUnexpectedEndOfFile();
            return true;
        }
        return false;
    }

    private Boolean nextIsTerminator(){
        if(tokenStream.isEnded() || tokenStream.isLastToken()){
            throw new RuntimeException("No available tokens");
        }
        return isTerminator(tokenStream.lookahead(1));
    }


    // check if token is terminating string (\n or ;)
    private Boolean isTerminator(Token t){
        return t.getValue().equals("\n") ||
                t.getValue().equals(";");
    }

    private Integer currentPrecedence(){
        if(tokenStream.isEnded()){
            return LOWEST_PRECEDENCE;
        }
        return getPrecedence(tokenStream.getCurrentToken());
    }

    // next token must exist
    private Integer nextPrecedence(){
        return getPrecedence(tokenStream.lookahead(1));
    }

    private Integer getPrecedence(Token t){
        if(operatorsPrecedence.containsKey(t.getValue())){
            return operatorsPrecedence.get(t.getValue());
        }
        return LOWEST_PRECEDENCE;
    }

    private void reportUnexpectedEndOfFile(String expectedLexeme){
        System.out.println("Line " + tokenStream.currentLine() + ": " +
                expectedLexeme + " expected, but EOF found");
    }

    private void reportUnexpectedEndOfFile(){
        System.out.println("Line " + tokenStream.currentLine() + ": " +
                "unexpected EOF found");
    }

    private void reportUnexpectedToken(String unexpectedLexeme, String expectedLexeme){
        if(unexpectedLexeme.equals("\n")){
            unexpectedLexeme = "end of line";
        }

        System.out.println("Line " + tokenStream.currentLine() + ": " +
                expectedLexeme + " expected, but " + unexpectedLexeme + " found");
    }

    private void reportUnexpectedToken(String unexpectedLexeme){
        System.out.println("Line " + tokenStream.currentLine() + ": " +
                "unexpected token " + unexpectedLexeme);
    }

    private void reportError(String err){
        System.out.println("Line " + tokenStream.currentLine() + ": " + err);
    }
}
