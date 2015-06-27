import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class LexicalAnalyzer {
	// Used for testing
	public static void main(String[] args) throws Exception {
		LexicalAnalyzer a = new LexicalAnalyzer("test.java");

		while (true) {
			try {
				int s = a.NextToken();
				if (s != 12)
					System.out.println(s + "\t\t" + lexeme);
				else
					break;
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}

	public static int DATA_ADDRESS = Compiler.DATA_ADDRESS;
	public static Type type;
	public static boolean isDecleration;
	public static int currentScope = 1;
	public static String lexeme;
	public static int lineNumber = 1;
	FileInputStream fileInput;

	LexicalAnalyzer(String filename) throws Exception {
		File f = new File(filename);
		if (!f.exists())
			throw new Exception("File does not exist: " + f);
		if (!f.isFile())
			throw new Exception("Should not be a directory: " + f);
		if (!f.canRead())
			throw new Exception("Can not read input file: " + f);
		fileInput = new FileInputStream(filename);

		r = fileInput.read();
		c = (char) r;
	}

	public Character c = '\0';
	public int r = 0;

	/*
	 * IMPORTANT NOTES: "EOF" means file has finished. When an error occurs, an
	 * exception is thrown and the rest of the file is scanned in order to
	 * report any additional errors. If error occurs inside a string or
	 * character, scanner goes to next line
	 */
	public int NextToken() throws Exception {
		OUTER: while (true) {

			// file ended
			if (r == -1)
				// return "$";
				return 35;

			// first iteration
			// if (r == 0) {
			// r = fileInput.read();
			// c = (char) r;
			// }

			// detect line numbers
			if (c == '\n') {
				lineNumber++;
				readChar();
				continue OUTER;
			}

			// ignore whitespace
			if (c == ' ' || c == '\t' || c == '\r') {
				readChar();
				continue OUTER;
			}

			// detect ordinary chars in scanner: + - ( ) { } , ; *
			else if (c == '+' || c == ')' || c == '(' || c == '}' || c == '{'
					|| c == ',' || c == ';' || c == '*' || c == '-') {
				// return faceOrdinaryCharSymbols(c);
				switch (c) {
				case '+':
					readChar();
					return 4;
				case ')':
					readChar();
					return 2;
				case '(':
					readChar();
					return 1;
				case '}':
					currentScope--;
					readChar();
					return 34;
				case '{':
					currentScope++;
					readChar();
					return 33;
				case ',':
					readChar();
					return 5;
				case ';':
					isDecleration = false;
					readChar();
					return 9;
				case '*':
					readChar();
					return 3;
				case '-':
					readChar();
					return 6;
				}

			}

			// detect // ------ and /* ---- */
			else if (c == '/') {
				readChar();
				if (c == '*') {
					readCommentedSequence();
				} else if (c == '/') {
					readChar();
					while (c != '\n' && r != -1) {
						readChar();
					}
					continue OUTER;
				}
			}

			else if (c == '.') {
				readChar();
				return 7;
			}

			// detect 1234
			else if (Character.isDigit(c))
				return faceDigitSymbol();

			// detect keyword, id
			else if (isAlpha_(c))
				return faceAlpha_Symbol();

			// detect &&
			else if (c == '&') {

				readChar();
				if (c == '&') {
					readChar();
					return 0;
				} else
					throw new InvalidTokenException("Invalid Token at line ",
							lineNumber, "&");

			}
			// return faceConditionalSign(c);

			// detect = and == and <
			else if (c == '=' || c == '<') {
				if (c == '=') {
					readChar();
					if (c == '=') {
						readChar();
						// return "==";
						return 32;
					} else
						// return "=";
						return 11;
				}
				if (c == '<') {
					readChar();
					// return "<";
					return 10;
				}
			}
			// return faceRelationalSign(c);

			// detect unknown symbol
			else
				faceUnknownSymbol();
		}

	}

	// ERROR HANDLER ROUTINE
	private void faceUnknownSymbol() throws CompilerException, IOException {
		readChar();
		throw new CompilerException("Invalid token at line " + lineNumber + ".");
	}

	private int faceAlpha_Symbol() throws IOException {
		String token = "" + c;
		readChar();
		String temp1 = readAlpha_Num();
		token += temp1;

		lexeme = token;

		if (isKeyword(token)) {
			if (token.equals("EOF"))
				return 12;
			if (token.equals("class")) {
				isDecleration = true;
				type = Type.CLASS;
				return 14;
			}
			if (token.equals("public")) {
				type = Type.METHOD;
				isDecleration = true;
				return 13;
			}
			if (token.equals("static"))
				return 16;
			if (token.equals("void"))
				return 17;
			if (token.equals("main"))
				return 18;
			if (token.equals("extends"))
				return 19;
			if (token.equals("return"))
				return 20;
			if (token.equals("boolean")) {
				type = Type.BOOLEAN;
				isDecleration = true;
				return 21;
			}
			if (token.equals("int")) {
				type = Type.INT;
				isDecleration = true;
				return 22;
			}
			if (token.equals("if"))
				return 23;
			if (token.equals("else"))
				return 24;
			if (token.equals("while"))
				return 25;
			if (token.equals("System"))
				return 26;
			if (token.equals("out"))
				return 27;
			if (token.equals("println"))
				return 28;
			if (token.equals("true"))
				return 29;
			if (token.equals("false"))
				return 30;
			//
			// return token;
		}

		// return "id";
		SymbolTable.put(lexeme, type, isDecleration, currentScope);

		if (type.equals(Type.BOOLEAN) || type.equals(Type.INT)) {
			SymbolTable.setAdrs(lexeme, DATA_ADDRESS);
			DATA_ADDRESS += 4;
		}

		return 15;

	}

	private int faceDigitSymbol() throws IOException, InvalidTokenException {
		String token = "";
		if (c == '0') {
			readChar();
			token = "0" + readIntSequence();
			lexeme = cleanUpDecimal(token);
			// return "int_literal";
			return 31;
		} else {
			String temp = readIntSequence();
			lexeme = cleanUpDecimal(temp);
			// return "int_literal";
			return 31;
		}

	}

	private String cleanUpDecimal(String str) {
		return String.valueOf(Integer.valueOf(str));
	}

	private void readCommentedSequence() throws IOException {
		char c1 = readChar();
		char c2 = readChar();
		while (!(c1 == '*' && c2 == '/')) {
			if (c == '\n')
				lineNumber++;
			c1 = c2;
			c2 = readChar();
		}
		readChar();
	}

	private String readIntSequence() throws IOException {
		String result = "";
		while (Character.isDigit(c)) {
			result += c;
			readChar();
		}
		return result;
	}

	private String readAlpha_Num() throws IOException {
		String result = "";
		while (isAlpha_Num(c)) {
			result += c;
			readChar();
		}
		return result;
	}

	private boolean isAlpha_(char c) {
		if (Character.isAlphabetic(c) || c == '_')
			return true;
		return false;
	}

	private boolean isAlpha_Num(char c) {
		if (isAlpha_(c) || Character.isDigit(c))
			return true;
		return false;
	}

	public char readChar() throws IOException {
		r = fileInput.read();
		c = (char) r;
		return c;
	}

	private boolean isKeyword(String test) {
		ArrayList<String> keywords = new ArrayList<String>(Arrays.asList("EOF",
				"class", "public", "static", "void", "main", "extends",
				"return", "boolean", "int", "if", "else", "System", "out",
				"println", "true", "false", "while"));
		if (keywords.contains(test))
			return true;
		return false;
	}
}

@SuppressWarnings("serial")
class InvalidTokenException extends Exception {
	public InvalidTokenException(String msg, int lineNumber, String invalidToken) {
		super(msg + lineNumber + " = " + invalidToken);
	}
}
