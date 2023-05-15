class a?


end


def sum(a,b)
    return a+b
end

def multiply(a,b); a*b; end

puts 5 . + 5

((Math.tan(2*x)) * (Math.cos(2*x))**(-1) - (Math.tan(2*c + x)) * (Math.cos(2*x))**(-2)).to_f/((Math.cos(2*x))**(-1) + (Math.cos(3*x))**(-2)) + (1 + (Math.cos(n*x))**(1.to_f/c))/(2*x + (Math.sin(3*x))**2)

puts sum(5,5)



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
