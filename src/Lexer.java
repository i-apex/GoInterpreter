
// Lexer class to generate tokens from input
class Lexer {
    private String text;
    private int pos;
    private char currentChar;

    public Lexer(String text) {
        this.text = text;
        this.pos = 0;
        this.currentChar = text.charAt(pos);
    }

    public void advance() {
        pos++;
        if (pos < text.length()) {
            currentChar = text.charAt(pos);
        } else {
            currentChar = '\0'; // '\0' represents end of input
        }
    }

    public Token getNextToken() {
        while (currentChar != '\0') {
            if (Character.isLetter(currentChar) || currentChar == '_') {
                return identifier();
            } else if (currentChar == '=') {
                advance();
                return new Token(TokenType.ASSIGN, "=");
            } else if (Character.isDigit(currentChar)) {
                return integer();
            } else if (currentChar == '+') {
                advance();
                return new Token(TokenType.PLUS, "+");
            } else if (currentChar == '-') {
                advance();
                return new Token(TokenType.MINUS, "-");
            } else if (currentChar == '*') {
                advance();
                return new Token(TokenType.MUL, "*");
            } else if (currentChar == '/') {
                advance();
                return new Token(TokenType.DIV, "/");
            } else if (currentChar == '(') {
                advance();
                return new Token(TokenType.LPAREN, "(");
            } else if (currentChar == ')') {
                advance();
                return new Token(TokenType.RPAREN, ")");
            } else if (currentChar == ';') {
                advance();
                return new Token(TokenType.SEMICOLON, ";");
            } else {
                // Ignore whitespace
                if (Character.isWhitespace(currentChar)) {
                    advance();
                    continue;
                }
                throw new RuntimeException("Invalid character");
            }
        }
        return new Token(TokenType.EOF, null);
    }

    private char peek() {
        int peekPos = pos + 1;
        return peekPos < text.length() ? text.charAt(peekPos) : '\0';
    }
    private Token identifier() {
        StringBuilder result = new StringBuilder();
        while (currentChar != '\0' && (Character.isLetterOrDigit(currentChar) || currentChar == '_' || currentChar == '.')) {
            result.append(currentChar);
            advance();
        }
        String identifier = result.toString();
        if (identifier.equals("fmt.Println")) {
            return new Token(TokenType.PRINTLN, identifier);
        } else {
            return new Token(TokenType.IDENTIFIER, identifier);
        }
    }

    private Token integer() {
        StringBuilder result = new StringBuilder();
        while (currentChar != '\0' && Character.isDigit(currentChar)) {
            result.append(currentChar);
            advance();
        }
        return new Token(TokenType.INT, result.toString());
    }
}