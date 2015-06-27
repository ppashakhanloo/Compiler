import java.util.ArrayList;
import java.util.Stack;

public class CodeGenerator {
	static ArrayList<ThreeAdrsCode> PB = new ArrayList<ThreeAdrsCode>();
	static Stack<Object> semanticStack = new Stack<Object>();
	public static int TEMP_ADDRESS = Compiler.TEMP_ADDRESS;

	public static void SAVE() {
		semanticStack.push(new Integer(PB.size()));
		PB.add(new ThreeAdrsCode());
	}

	public static void START_OF_MAIN() {
		int index = (Integer) semanticStack.pop();
		PB.get(index).set("JP", Integer.toString(PB.size()), "", "");
	}

	public static void PUSH() {
		semanticStack.push(LexicalAnalyzer.lexeme);
	}

	public static void POP() {
		semanticStack.pop();
	}

	public static void SET_STATIC() {
		SymbolTable.setIsStatic(LexicalAnalyzer.lexeme, true);
	}

	public static void SET_METHOD_ADRS() {
		SymbolTable.symtab.get(SymbolTable.indexInSymtab((String) semanticStack
				.pop())).adrs = PB.size();
	}

	public static void SET_DECLERATION_TO_FALSE() {
		LexicalAnalyzer.currentScope--;
		LexicalAnalyzer.isDecleration = false;
	}

	public static void CHECK_RETURN_TYPE() throws CompilerException {
		semanticStack.push(semanticStack.peek());
		boolean isInt = semanticStack.pop() instanceof Integer;
		boolean isBoolean = semanticStack.pop() instanceof Boolean;
		String type = (String) semanticStack.pop();
		if (type.equals("int") && !isInt) {
			throw new CompilerException(
					"Type mismatch: return type must be integer ["
							+ LexicalAnalyzer.lineNumber + "].");
		}
		if (type.equals("boolean") && !isBoolean) {
			throw new CompilerException(
					"Type mismatch: return type must be boolean ["
							+ LexicalAnalyzer.lineNumber + "].");
		}
		PB.add(new ThreeAdrsCode("JP", "@" + Compiler.RETURN_ADDRESS, "", ""));
	}

	public static void CALL() throws CompilerException {
		if (((ArrayList<Entry>) semanticStack.peek()).size() > 0)
			throw new CompilerException(
					"The number of arguments is more than expected ["
							+ LexicalAnalyzer.lineNumber + "]");
		semanticStack.pop();
		PB.add(new ThreeAdrsCode("ASSIGN", Integer.toString(PB.size() + 2),
				Integer.toString(Compiler.RETURN_ADDRESS), ""));
		PB.add(new ThreeAdrsCode("JP", (String) semanticStack.pop(), "", ""));
	}

	public static void SET_ARGUMENT() {
		int index = SymbolTable.indexInSymtab((String) semanticStack.peek());
		int argIndex = findMethodArg(LexicalAnalyzer.lexeme, index);
		if (argIndex == -1)
			return;
		SymbolTable.symtab.get(argIndex).isArgument = true;
	}

	private static int findMethodArg(String lexeme, int index) {
		index++;
		while (SymbolTable.symtab.get(index).scope > 2) {
			if (SymbolTable.symtab.get(index).id.equals(lexeme))
				return index;
			index++;
		}
		return -1;
	}

	private static ArrayList<Entry> getArguments(int index)
			throws CompilerException {
		ArrayList<Entry> out = new ArrayList<Entry>();
		if (!SymbolTable.symtab.get(index).type.equals(Type.METHOD)) {
			throw new CompilerException("Undefined method ["
					+ LexicalAnalyzer.lineNumber + "].");
		} else {
			index++;
			while (SymbolTable.symtab.get(index).isArgument == true
					&& SymbolTable.symtab.get(index).scope == 3) {
				out.add(SymbolTable.symtab.get(index));
			}
		}

		return out;
	}

	public static void FACTOR_INDEX() throws CompilerException {
		int index = SymbolTable.indexInSymtab((String) semanticStack.pop());
		index++;
		boolean found = false;
		while (SymbolTable.symtab.get(index).scope > 1
				&& index < SymbolTable.symtab.size()) {
			if (SymbolTable.symtab.get(index).id.equals(LexicalAnalyzer.lexeme)
					&& SymbolTable.symtab.get(index).type.equals(Type.METHOD)) {
				found = true;
				break;
			}
			index++;
		}
		if (found) {
			semanticStack.push(SymbolTable.symtab.get(index).adrs);
			ArrayList<Entry> args = getArguments(index);
			semanticStack.push(args);
		} else {
			throw new CompilerException("Undefined method ["
					+ LexicalAnalyzer.lineNumber + "].");
		}
	}

	public static void FACTOR_ADRS() throws CompilerException {
		int index = SymbolTable.indexInSymtab((String) semanticStack.pop());
		index++;
		if (index >= SymbolTable.symtab.size())
			throw new CompilerException("Undefined static variable ["
					+ LexicalAnalyzer.lineNumber + "].");
		boolean found = false;
		while (SymbolTable.symtab.get(index).scope > 1
				&& index < SymbolTable.symtab.size()) {
			if (SymbolTable.symtab.get(index).id.equals(LexicalAnalyzer.lexeme)
					&& (SymbolTable.symtab.get(index).type.equals(Type.INT) || SymbolTable.symtab
							.get(index).type.equals(Type.BOOLEAN))
					&& SymbolTable.symtab.get(index).isStatic) {
				found = true;
				break;
			}
			index++;
		}

		if (found) {
			semanticStack.push(SymbolTable.symtab.get(index).adrs);
		} else {
			throw new CompilerException("Undefined static variable ["
					+ LexicalAnalyzer.lineNumber + "].");
		}
	}

	public static void PUSH_INT() {
		semanticStack.push("#" + LexicalAnalyzer.lexeme);
	}

	public static void INC_SCOPE() {
		LexicalAnalyzer.currentScope++;
	}

	public static void CHECK_ARG() throws CompilerException {
		Object temp = semanticStack.pop();
		Type type;
		if (temp instanceof Boolean)
			type = Type.BOOLEAN;
		else {
			type = Type.INT;
			try {
				if (!((ArrayList<Entry>) semanticStack.peek()).get(0).type
						.equals(type)) {
					throw new CompilerException(
							"Argument type must be "
									+ ((ArrayList<Entry>) semanticStack.peek())
											.get(0).type + " instead of "
									+ type + LexicalAnalyzer.lineNumber + "].");
				} else {
					PB.add(new ThreeAdrsCode("ASSIGN", (String) temp,
							Integer.toString(((ArrayList<Entry>) (semanticStack
									.peek())).get(0).adrs), ""));
					((ArrayList<Entry>) (semanticStack.peek())).remove(0);
				}
			} catch (Exception e) {
				throw new CompilerException(
						"Number of arguments is less than expected ["
								+ LexicalAnalyzer.lineNumber + "]");
			}
		}
	}

	public static void EXTENSION() {
		int index = SymbolTable.indexInSymtab((String) semanticStack.pop());
		int index_parent = SymbolTable.indexInSymtab(LexicalAnalyzer.lexeme);
		SymbolTable.symtab.get(index).parent = SymbolTable.symtab
				.get(index_parent);
	}

	public static void PID() {
		semanticStack.push(SymbolTable.foundInSymtab(LexicalAnalyzer.lexeme));
	}

	public static void WHILE() {
		PB.get((Integer) semanticStack.pop()).set("JPF",
				(String) semanticStack.pop(), Integer.toString(PB.size() + 1),
				"");
		PB.add(new ThreeAdrsCode("JP", (String) (semanticStack.pop()), "", ""));
	}

	public static void JPF_SAVE() {
		PB.get((Integer) semanticStack.pop()).set("JPF",
				(String) semanticStack.pop(), Integer.toString(PB.size() + 1),
				"");
		semanticStack.push(PB.size());
		semanticStack.add(new ThreeAdrsCode());
	}

	public static void LABEL() {
		semanticStack.push(PB.size());
	}

	public static void JP() {
		PB.get((Integer) semanticStack.pop()).set("JP",
				Integer.toString(PB.size()), "", "");
	}

	public static void PRINT() {
		PB.add(new ThreeAdrsCode("PRINT", (String) semanticStack.pop(), "", ""));
	}

	public static void ASSIGN() {
		PB.add(new ThreeAdrsCode("ASSIGN", (String) semanticStack.pop(),
				(String) semanticStack.pop(), ""));
	}

	public static void ADD() {
		int t = getTemp();
		String first = (String) semanticStack.pop();
		PB.add(new ThreeAdrsCode("ADD", (String) semanticStack.pop(), first,
				Integer.toString(t)));
		semanticStack.push(t);
	}

	public static void MULT() {
		int t = getTemp();
		String first = (String) semanticStack.pop();
		PB.add(new ThreeAdrsCode("MULT", (String) semanticStack.pop(), first,
				Integer.toString(t)));
		semanticStack.push(t);
	}

	public static void SUB() {
		int t = getTemp();
		String first = (String) semanticStack.pop();
		PB.add(new ThreeAdrsCode("SUB", (String) semanticStack.pop(), first,
				Integer.toString(t)));
		semanticStack.push(t);
	}

	public static void AND() {
		int t = getTemp();
		String first = (String) semanticStack.pop();
		PB.add(new ThreeAdrsCode("AND", (String) semanticStack.pop(), first,
				Integer.toString(t)));
		semanticStack.push(t);
	}

	public static void EQ() {
		int t = getTemp();
		String first = (String) semanticStack.pop();
		PB.add(new ThreeAdrsCode("EQ", (String) semanticStack.pop(), first,
				Integer.toString(t)));
		semanticStack.push(t);
	}

	public static void LT() {
		int t = getTemp();
		String first = (String) semanticStack.pop();
		PB.add(new ThreeAdrsCode("LT", (String) semanticStack.pop(), first,
				Integer.toString(t)));
		semanticStack.push(t);
	}

	private static int getTemp() {
		TEMP_ADDRESS += 4;
		return TEMP_ADDRESS;
	}

}

class ThreeAdrsCode {
	String first;
	String second;
	String third;
	String fourth;

	public ThreeAdrsCode(String first, String second, String third,
			String fourth) {
		this.first = first;
		this.second = second;
		this.third = third;
		this.fourth = fourth;
	}

	public void set(String first, String second, String third, String fourth) {
		this.first = first;
		this.second = second;
		this.third = third;
		this.fourth = fourth;
	}

	public ThreeAdrsCode() {

	}

	@Override
	public String toString() {
		return "(" + this.first + ", " + this.second + ", " + this.third + ", "
				+ this.fourth + ")";
	}
}