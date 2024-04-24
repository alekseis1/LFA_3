import java.util.ArrayList;
import java.util.List;

// Token types
enum TokenType {
    IDENTIFIER,
    INTEGER,
    STRING,
    CHAR,
    PLUS,
    MINUS,
    MULTIPLY,
    DIVIDE,
    ASSIGN,
    LPAREN,
    RPAREN,
    COLON,
    INCREMENT,
    EQUAL,
    LESS,
    GREATER,
    LESS_EQUAL,
    GREATER_EQUAL,
    FOR,
    IN,
    RANGE,
    IF,
    ELSE,
    PRINT,
    SPACE,
    NEWLINE,
    EOF
}

// Token class
class Token {
    TokenType type;
    String value;

    Token(TokenType type, String value) {
        this.type = type;
        this.value = value;
    }

    public String toString() {
        return String.format("Token(%s, %s)", type, value);
    }
}

// Lexer class
class Lexer {
    private final String text;
    private int pos;

    public Lexer(String text) {
        this.text = text;
        this.pos = 0;
    }

    private void skipWhitespace() {
        while (pos < text.length() && Character.isWhitespace(text.charAt(pos))) {
            pos++;
        }
    }

    private Token getNextToken() {
        if (pos >= text.length()) {
            return new Token(TokenType.EOF, null);
        }

        // Match identifiers (variables and keywords)
        if (Character.isLetter(text.charAt(pos))) {
            StringBuilder identifier = new StringBuilder();
            while (pos < text.length() && (Character.isLetterOrDigit(text.charAt(pos)) || text.charAt(pos) == '_')) {
                identifier.append(text.charAt(pos));
                pos++;
            }
            String identifierStr = identifier.toString();
            return switch (identifierStr) {
                case "for" -> new Token(TokenType.FOR, "for");
                case "in" -> new Token(TokenType.IN, "in");
                case "range" -> new Token(TokenType.RANGE, "range");
                case "if" -> new Token(TokenType.IF, "if");
                case "else" -> new Token(TokenType.ELSE, "else");
                case "print" -> new Token(TokenType.PRINT, "print");
                default -> new Token(TokenType.IDENTIFIER, identifierStr);
            };
        }

        // Match integers
        if (Character.isDigit(text.charAt(pos))) {
            StringBuilder integer = new StringBuilder();
            while (pos < text.length() && Character.isDigit(text.charAt(pos))) {
                integer.append(text.charAt(pos));
                pos++;
            }
            return new Token(TokenType.INTEGER, integer.toString());
        }

        // Match operators and other single-character tokens
        switch (text.charAt(pos)) {
            case '+': pos++; return new Token(TokenType.PLUS, "+");
            case '-': pos++; return new Token(TokenType.MINUS, "-");
            case '*': pos++; return new Token(TokenType.MULTIPLY, "*");
            case '/': pos++; return new Token(TokenType.DIVIDE, "/");
            case '=': pos++; return new Token(TokenType.ASSIGN, "=");
            case '(': pos++; return new Token(TokenType.LPAREN, "(");
            case ')': pos++; return new Token(TokenType.RPAREN, ")");
            case ':': pos++; return new Token(TokenType.COLON, ":");
            case '<':
                if (pos + 1 < text.length() && text.charAt(pos + 1) == '=') {
                    pos += 2;
                    return new Token(TokenType.LESS_EQUAL, "<=");
                }
                pos++;
                return new Token(TokenType.LESS, "<");
            case '>':
                if (pos + 1 < text.length() && text.charAt(pos + 1) == '=') {
                    pos += 2;
                    return new Token(TokenType.GREATER_EQUAL, ">=");
                }
                pos++;
                return new Token(TokenType.GREATER, ">");
            case '\n':
                pos++;
                return new Token(TokenType.NEWLINE, "\n");
            default:
                pos++;
                return new Token(TokenType.EOF, null); // In case of unknown character
        }
    }

    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();
        Token token;
        do {
            skipWhitespace();
            token = getNextToken();
            tokens.add(token);
        } while (token.type != TokenType.EOF);
        return tokens;
    }
}

public class Main {
    public static void main(String[] args) {
        String input = "i = 0\n while i < 10: print(i)\n i += 1\n if i == 10: print('Hello')\n else: print('World')\n";
        Lexer lexer = new Lexer(input);
        List<Token> tokens = lexer.tokenize();
        for (Token token : tokens) {
            System.out.println(token);
        }
        System.out.println("------");
        String input2 = "i = 5\n for i in range(10): print(i)\n i +=1\n if i == 10: print(Hello)\n else: print(World)";
        Lexer lexer2 = new Lexer(input2);
        List<Token> tokens2 = lexer2.tokenize();
        for (Token token : tokens2){
            System.out.println(token);
        }
    }
}