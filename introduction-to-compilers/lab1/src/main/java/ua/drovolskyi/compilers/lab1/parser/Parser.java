package ua.drovolskyi.compilers.lab1.parser;

// import ua.drovolskyi.compilers.lab1.parser.AbstractSyntaxTree.Node;

import ua.drovolskyi.compilers.lab1.Token;

import java.util.*;

public class Parser {
    private static final List<String> UNARY_OPERATORS = Arrays.asList("-", "!");
    private static final List<String> BINARY_OPERATORS =
            Arrays.asList("+", "-", "*", "/", "==", "!=", ">", "<", ">=", "<=");
    private static final List<String> LOGICAL_OPERATORS =
            Arrays.asList("and", "or", "not", "&&", "||", "!");
    private static final List<String> SIGILS = Arrays.asList("$", "@@", "@");

    private final Integer LOWEST_PRECEDENCE = 0;
    private final Integer MAX_PRECEDENCE = 7;

    // operators with bigger precedence should be executed first
    private final Map<String, Integer> operatorsPrecedence = new TreeMap<>(String::compareTo);




    private final TokenStream tokenStream;

    public Parser(TokenStream tokenStream){
        this.tokenStream = tokenStream;

        // init operators precedences
        operatorsPrecedence.put("or", 1);
        operatorsPrecedence.put("and", 2);
        operatorsPrecedence.put("==", 3);
        operatorsPrecedence.put("!=", 3);
        operatorsPrecedence.put(">", 4);
        operatorsPrecedence.put("<", 4);
        operatorsPrecedence.put(">=", 4);
        operatorsPrecedence.put("<=", 4);
        operatorsPrecedence.put("+", 5);
        operatorsPrecedence.put("-", 5);
        operatorsPrecedence.put("*", 6);
        operatorsPrecedence.put("/", 6);
        operatorsPrecedence.put(".", 6); // calling method is also operator

        operatorsPrecedence.put("(", 8);
    }

    ////////////////////////////////////// Tasks
    // in lexer transform SIGIL + IDENTIFIER => IDENTIFIER
    // in lexer transform (IDENTIFIER + !) or (IDENTIFIER + ?) => IDENTIFIER
    // remove (WHITESPACE && non-terminator) and COMMENT from TokenStream
    // + Problem: child do not added to nodes
    // + add unless structure
    // if variable found, check if its value does not end with ? or '!'
    // in function definition check if identifier does not start from SIGIL
    // add operator of method call (make it operation with highest priority)


    public AbstractSyntaxTree parse(){
        AstNode root =
                new AstNode(AstNode.Type.PROGRAM, null);

        while(!tokenStream.isEnded()){
            tokenStream.consume();
            // here, currentToken is start of new expression

            AstNode expression = null;
            if(!tokenStream.isEnded()){
                expression = parseExpressionRecursively(currentPrecedence());
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

        while(!tokenStream.isLastToken() &&
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

    // it is called if leftChild.priority < currentToken.priority
    // current token is some binary math or binary logic operation
    private AstNode parseBinaryOperator(AstNode leftChild){
        AstNode operation = new AstNode(AstNode.Type.BINARY_OPERATOR,
                tokenStream.getCurrentToken().getValue(), Arrays.asList(leftChild));

        if(!tokenStream.isLastToken()){
            Integer operationPrecedence = currentPrecedence();
            tokenStream.consume();
            operation.addChild(parseExpressionRecursively(operationPrecedence));
        }
        else{
            ///////////////////////////////////// here NEED to report about unexpected file end
        }

        return operation;
    }

    // current token is '('
    private AstNode parseFunctionCall(String identifier){
        return new AstNode(AstNode.Type.FUNCTION_CALL, identifier, parseFunctionCallArgs());
    }

    // current token is '('
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
        args.add(parseExpressionRecursively(currentPrecedence()));
        while(tokenStream.availableTokens() >= 2
                && tokenStream.lookahead(1).getValue().equals(",")){
            tokenStream.consumeTokens(2);
            args.add(parseExpressionRecursively(currentPrecedence()));
        }

        if(reportIfUnexpectedEnd()){
            return args;
        }

        // found matching )
        if(tokenStream.lookahead(1).getValue().equals(")")){
            tokenStream.consume();
            return args;
        }

        /////////////////////////////////////// here NEED report about unexpected token error
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
                ///////////////////////////////////////////////// NEED to report about incorrect ? ot ! at the end of token
            }
            return parseIdentifier();
        }
        else if (t.getValue().equals("return")){
            return parseReturn();
        }
        else if(t.getValue().equals("def")){
            return parseFunctionDefinition();
        }
        else if(t.getValue().equals("if") || t.getValue().equals("unless")){
            return parseConditional();
        }
        else if(t.getValue().equals("while")){
            return parseRepetition();
        }
        else if(t.getValue().equals("(")){
            return parseGroupedExpression();
        }
        else if(UNARY_OPERATORS.contains(t.getValue())){
            return parseUnaryOperator();
        }

        /////////////////////////////////////////// here NEED report about unrecognized token error
        return null;
    }

    /**
     * Current token is '('
     * @return
     */
    private AstNode parseGroupedExpression(){
        tokenStream.consume();
        AstNode expr = parseExpressionRecursively(currentPrecedence());

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
        AstNode node = new AstNode(AstNode.Type.UNARY_OPERATOR, null);
        tokenStream.consume();
        node.addChild(parseExpressionRecursively(MAX_PRECEDENCE));

        return node;
    }


    // current token is 'while'
    private AstNode parseRepetition(){
        AstNode repetitionNode = new AstNode(AstNode.Type.REPETITION, null);
        tokenStream.consume();

        // condition of repetition
        AstNode condition = new AstNode(AstNode.Type.CONDITION, null);
        condition.addChild(parseExpressionRecursively(currentPrecedence()));
        repetitionNode.addChild(condition);

        if(!reportIfUnexpectedEnd()){
            tokenStream.consume();
        }
        else{
            return null;
        }

        // block (repetition's body)
        AstNode block = new AstNode(AstNode.Type.BLOCK, null);
        block.addChild(parseBlock());
        repetitionNode.addChild(block);

        return repetitionNode;
    }


    // current token is 'if'
    private AstNode parseConditional(){
        AstNode conditional = new AstNode(AstNode.Type.CONDITIONAL, tokenStream.getCurrentToken().getValue());

        tokenStream.consume();

        AstNode condition = new AstNode(AstNode.Type.CONDITION, null);
        condition.addChild(parseExpressionRecursively(currentPrecedence()));
        conditional.addChild(condition);

        if(!tokenStream.isLastToken()){
            if(isTerminator(tokenStream.lookahead(1))){
                AstNode block = new AstNode(AstNode.Type.BLOCK, null);
                block.addChild(parseBlock());
                conditional.addChild(block);
                // current token is last token of parsed block

                if(tokenStream.getCurrentToken().getValue().equals("else")){
                    AstNode elseBlock = new AstNode(AstNode.Type.BLOCK, null);
                    elseBlock.addChild(parseBlock());
                    conditional.addChild(elseBlock);
                }

                // else unexpected toke is already reported
                return conditional;
            }
            else{
                /////////////////////////// NEED to report about unexpected token
                return null;
            }

        }
        else{
            ////////////////////////////////// NEED report about unexpected end of file
            return null;
        }


    }


    /**
     * Current token is 'def'
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
                        tokenStream.consume();
                        if(tokenStream.isEnded()){
                            ////////////////////////////////// NEED report about unexpected end of file
                            return functionNode;
                        }
                        if(!isTerminator(tokenStream.getCurrentToken())){
                            ///////////////////////////////// NEED report about unexpected token
                            return functionNode;
                        }

                        functionNode.addChild(parseBlock()); // add body
                        // here current token is 'end' or out of bounds of tokenStream

                        return functionNode;
                    }
                    else{
                        ///////////////////////////////////////// NEED report about unexpected token
                        return functionNode;
                    }
                }
                else{
                    return functionNode;
                }
            }
        }

        /////////////////////////////////// NEED to report about unexpected end of file
        return new AstNode(AstNode.Type.FUNCTION_DEFINITION, null);
    }

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
     * Current symbol is token that is previous to first token of block
     * @return
     */
    private AstNode parseBlock(){
        tokenStream.consume();
        AstNode block = new AstNode(AstNode.Type.BLOCK, null);

        while(!tokenStream.isEnded() &&
                !tokenStream.getCurrentToken().getValue().equals("end") &&
                !tokenStream.getCurrentToken().getValue().equals("else")){
            AstNode expr = parseExpressionRecursively(currentPrecedence());
            if(expr != null){
                block.addChild(expr);
            }
            tokenStream.consume();
        }

        if(tokenStream.isEnded()){
            //////////////////////////// NEED to report about unexpected end of file
        }
        // else we reached 'end'

        return block;
    }

    /**
     * Current token is '('
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
                    /////////////////////////// NEED to report about unexpected token
                    return params;
                }

                if(reportIfUnexpectedEnd()){
                    return params;
                }
            }
            else{
                /////////////////////////// NEED to report about unexpected end of file
                return params;
            }
        }
        tokenStream.consume();

        reportIfUnexpectedEnd();

        if(!tokenStream.getCurrentToken().getValue().equals(")")){
            /////////////////////////////////////// need to report about unexpected end of file
        }
        return params;
    }

    /**
     * Current token is 'return'
     */
    private AstNode parseReturn(){
        tokenStream.consume();

        AstNode arg = null;
        if(!reportIfUnexpectedEnd()){
            arg = parseExpressionRecursively(currentPrecedence());
        }

        return new AstNode(AstNode.Type.RETURN, null, Arrays.asList(arg));
    }


    // current token is identifier
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

            //////////////////////////////////////////////// here NEED to report about unexpected token
        }
        return identifierNode;
    }

    // current token is identifier, next token is =
    private AstNode parseVarBinding(){
        AstNode identifier = new AstNode(AstNode.Type.IDENTIFIER, tokenStream.getCurrentToken().getValue());
        AstNode rightSide = null;

        tokenStream.consumeTokens(2);

        if(!reportIfUnexpectedEnd()){
            rightSide = parseExpressionRecursively(currentPrecedence());
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
                //////////////////////////////// here NEED to report about unexpected end of statement
                return true;
            }
        }
        else{
            /////////////////////////////////// here NEED to report about unexpected end of file
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
}
