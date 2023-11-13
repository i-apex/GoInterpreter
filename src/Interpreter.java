import java.util.HashMap;
import java.util.Map;

// Interpreter class to interpret tokens
class Interpreter {
    private Lexer lexer;
    private Token currentToken;
    private Map<String, Symbol> symbolTable;

    public Interpreter(Lexer lexer) {
        this.lexer = lexer;
        this.currentToken = lexer.getNextToken();
        this.symbolTable = new HashMap<>();
    }

    private void eat(TokenType tokenType) {
        if (currentToken.type == tokenType) {
            currentToken = lexer.getNextToken();
        } else {
            throw new RuntimeException("Invalid syntax");
        }
    }

    public void interpret() {
        while (currentToken.type != TokenType.EOF) {
            statement();
        }
    }

    private void statement() {
        if (currentToken.type == TokenType.IDENTIFIER) {
            declarationOrAssignment();
        } else if (currentToken.type == TokenType.PRINTLN) {
            printStatement();
        } else {
            throw new RuntimeException("Invalid statement");
        }
    }

    private void declarationOrAssignment() {
        Token variableToken = currentToken;
        eat(TokenType.IDENTIFIER);

        if (currentToken.type == TokenType.ASSIGN) {
            eat(TokenType.ASSIGN);

            int value = expr(); // Change here: evaluate the expression

            Symbol symbol = new Symbol(variableToken.value, value);
            symbolTable.put(variableToken.value, symbol);

            //System.out.println("Variable: " + variableToken.value + " Value: " + value);
        }
        if (currentToken.type == TokenType.SEMICOLON){
            eat(TokenType.SEMICOLON); // Just a declaration without assignment, so consume semicolon
        }
    }

    private void printStatement() {
        eat(TokenType.PRINTLN);
        eat(TokenType.LPAREN);

        if (currentToken.type == TokenType.IDENTIFIER) {
            // fmt.Println(x)
            String variableName = currentToken.value;
            eat(TokenType.IDENTIFIER);

            if (symbolTable.containsKey(variableName)) {
                System.out.println(symbolTable.get(variableName).value);
            } else {
                throw new RuntimeException("Variable '" + variableName + "' not declared");
            }
        } else {
            // fmt.Println(expression)
            int value = expr();
            System.out.println(value);
        }

        eat(TokenType.RPAREN);
        eat(TokenType.SEMICOLON);
    }

    private void ifstatement() {
        eat(TokenType.IF);
        eat(TokenType.LPAREN);

        if(currentToken.type == TokenType.IDENTIFIER){
            String variable1 = currentToken.value;
            eat(TokenType.ASSIGN);
            eat(TokenType.ASSIGN);
            String variable2;
            if(currentToken.type == TokenType.IDENTIFIER) {
                variable2 = currentToken.value;
                if(variable1==variable2){
                    eat(TokenType.LCURL);
                    
                }
            }


        }
    }
    private int expr() {
        int result = term();
        while (currentToken.type == TokenType.PLUS || currentToken.type == TokenType.MINUS) {
            Token operator = currentToken;
            if (operator.type == TokenType.PLUS) {
                eat(TokenType.PLUS);
                result += term();
            } else if (operator.type == TokenType.MINUS) {
                eat(TokenType.MINUS);
                result -= term();
            }
        }
        return result;
    }

    private int term() {
        int result = factor();
        while (currentToken.type == TokenType.MUL || currentToken.type == TokenType.DIV) {
            Token operator = currentToken;
            if (operator.type == TokenType.MUL) {
                eat(TokenType.MUL);
                result *= factor();
            } else if (operator.type == TokenType.DIV) {
                eat(TokenType.DIV);
                int divisor = factor();
                if (divisor != 0) {
                    result /= divisor;
                } else {
                    throw new RuntimeException("Division by zero");
                }
            }
        }
        return result;
    }

    private int factor() {
        if (currentToken.type == TokenType.INT) {
            int value = Integer.parseInt(currentToken.value);
            eat(TokenType.INT);
            return value;
        } else if (currentToken.type == TokenType.IDENTIFIER) {
            String variableName = currentToken.value;
            eat(TokenType.IDENTIFIER);

            if (symbolTable.containsKey(variableName)) {
                return symbolTable.get(variableName).value;
            } else {
                throw new RuntimeException("Variable '" + variableName + "' not declared");
            }
        } else if (currentToken.type == TokenType.LPAREN) {
            eat(TokenType.LPAREN);
            int result = expr();
            eat(TokenType.RPAREN);
            return result;
        } else {
            throw new RuntimeException("Invalid factor");
        }
    }
}