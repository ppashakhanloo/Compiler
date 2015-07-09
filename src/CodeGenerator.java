import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

public class CodeGenerator {
	static ArrayList<ThreeAdrsCode> PB = new ArrayList<ThreeAdrsCode>();
	static Stack<Object> semanticStack = new Stack<Object>();
	public static int TEMP_ADDRESS = Compiler.TEMP_ADDRESS;

	// static Stack<String> callStack = new Stack<String>();
	public static ArrayList<Method> methodArrayList = new ArrayList<Method>();

	public static void printStack() {
		System.out.println(Arrays.toString(semanticStack.toArray()));
	}

	public static void SAVE() {
		semanticStack.push("" + PB.size());
		PB.add(new ThreeAdrsCode());
	}

	public static void START_OF_MAIN() {
		int index = Integer.parseInt((String) semanticStack.pop());
		PB.get(index).set("JP", "#" + (PB.size()), "", "");
	}

	public static void PUSH() {
		semanticStack.push(LexicalAnalyzer.old_lexeme);
	}

	public static void POP() {
		semanticStack.pop();
	}

	public static void SET_STATIC() {
		SymbolTable.setIsStatic(LexicalAnalyzer.old_lexeme, true);
	}

	public static void SET_METHOD_ADRS() {
		SymbolTable.symtab.get(SymbolTable.indexInSymtab((String) semanticStack
				.peek())).adrs = PB.size();
	}

	public static void SET_DECLERATION_TO_FALSE() {
		LexicalAnalyzer.currentScope--;
		LexicalAnalyzer.isDecleration = false;
	}

	public static void CHECK_RETURN_TYPE() throws CompilerException {
		String expression_type = (String) semanticStack.pop();
		String result = (String) semanticStack.pop();
		String method_id = (String) semanticStack.pop();
		String method_type = (String) semanticStack.pop();

		if (!expression_type.equals("int") && method_type.equals("int")) {
			throw new CompilerException(
					"Type mismatch: return type must be integer ["
							+ LexicalAnalyzer.lineNumber + "].");
		}
		if (!expression_type.equals("boolean") && method_type.equals("boolean")) {
			throw new CompilerException(
					"Type mismatch: return type must be boolean ["
							+ LexicalAnalyzer.lineNumber + "].");
		}

		// find index of method
		int index = SymbolTable.symtab.size() - 1;
		while (index >= 0) {
			if (SymbolTable.symtab.get(index).scope == 2
					&& SymbolTable.symtab.get(index).type.equals(Type.METHOD)) {
				break;
			}
			index--;
		}
		// find index of parent
		index--;
		while (index >= 0) {
			if (SymbolTable.symtab.get(index).scope == 1
					&& SymbolTable.symtab.get(index).type.equals(Type.CLASS)) {
				break;
			}
			index--;
		}

		String class_id = SymbolTable.symtab.get(index).id;

		PB.add(new ThreeAdrsCode("JP", "@" + Compiler.RETURN_ADDRESS, "", ""));
		methodArrayList.add(new Method(expression_type, result, method_id,
				class_id));
	}

	public static void CALL() throws CompilerException {
		if (((ArrayList<Entry>) semanticStack.peek()).size() > 0)
			throw new CompilerException(
					"The number of arguments is less than expected ["
							+ LexicalAnalyzer.lineNumber + "]");
		semanticStack.pop(); // pop array list
		PB.add(new ThreeAdrsCode("ASSIGN", "#"
				+ Integer.toString(PB.size() + 2), Integer
				.toString(Compiler.RETURN_ADDRESS), ""));
		String method_adrs = (String) semanticStack.pop();
		PB.add(new ThreeAdrsCode("JP", "#" + method_adrs, "", ""));

		// find name of method
		int index = -1;
		for (int i = 0; i < SymbolTable.symtab.size(); i++) {
			if (SymbolTable.symtab.get(i).type.equals(Type.METHOD)
					&& SymbolTable.symtab.get(i).adrs == Integer
							.parseInt(method_adrs)) {
				index = i;
				break;
			}
		}

		String method_id = SymbolTable.symtab.get(index).id;
		int array_list_index = -1;
		String className = (String) semanticStack.pop();
		for (int i = 0; i < methodArrayList.size(); i++) {
			if (methodArrayList.get(i).methodID.equals(method_id)
					&& methodArrayList.get(i).className.equals(className)) {
				array_list_index = i;
				break;
			}
		}

		semanticStack.push(methodArrayList.get(array_list_index).expression);
		semanticStack.push(methodArrayList.get(array_list_index).returnType);
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
				index++;
			}
		}
		return out;
	}

	public static void FACTOR_INDEX() throws CompilerException {
		int index = SymbolTable.indexOfClass((String) semanticStack.pop());
		int index2 = index;
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
			String class_name = SymbolTable.symtab.get(index2).id;
			semanticStack.push(class_name);
			semanticStack
					.push(Integer.toString(SymbolTable.symtab.get(index).adrs));
			ArrayList<Entry> args = getArguments(index);
			semanticStack.push(args);
		} else {
			Entry parent = SymbolTable.symtab.get(index2).parent;
			Entry old_parent = parent;
			while (parent != null) {
				old_parent = parent;
				index2 = SymbolTable.symtab.indexOf(parent);
				if (index2 < 0) {
					found = false;
					break;
				}

				parent = SymbolTable.symtab.get(index2).parent;
				index2++;
				while (SymbolTable.symtab.get(index2).scope != 1) {
					if (SymbolTable.symtab.get(index2).scope == 2) {
						if (SymbolTable.symtab.get(index2).id
								.equals(LexicalAnalyzer.old_lexeme)) {
							found = true;
							break;
						}
					}
					index2++;
				}
			}
			if (found) {
				semanticStack.push(old_parent.id);
				semanticStack.push(Integer.toString(SymbolTable.symtab
						.get(index2).adrs));
				ArrayList<Entry> args = getArguments(index2);
				semanticStack.push(args);
			} else {
				throw new CompilerException("Undefined static method ["
						+ LexicalAnalyzer.lineNumber + "].");
			}
		}
	}

	public static void FACTOR_ADRS() throws CompilerException {
		int index = SymbolTable.indexOfClass((String) semanticStack.pop());
		int index2 = index;
		index++;
		if (index >= SymbolTable.symtab.size())
			throw new CompilerException("Undefined static variable ["
					+ LexicalAnalyzer.lineNumber + "].");
		boolean found = false;
		while (SymbolTable.symtab.get(index).scope > 1
				&& index < SymbolTable.symtab.size()) {
			if (SymbolTable.symtab.get(index).id
					.equals(LexicalAnalyzer.old_lexeme)
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
			if (SymbolTable.symtab.get(index).type.equals(Type.BOOLEAN))
				semanticStack.push("boolean");
			if (SymbolTable.symtab.get(index).type.equals(Type.INT))
				semanticStack.push("int");
		} else {

			// /////////////////////////////////////////////////////
			Entry parent = SymbolTable.symtab.get(index2).parent;
			while (parent != null) {
				index2--;
				if (index2 < 0) {
					found = false;
					break;
				}
				while (!SymbolTable.symtab.get(index2).equals(parent)
						&& index2 >= 0) {
					index2--;
				}
				if (index2 < 0) {
					found = false;
					break;

				}

				parent = SymbolTable.symtab.get(index2).parent;
				index2++;
				while (SymbolTable.symtab.get(index2).scope != 1) {
					if (SymbolTable.symtab.get(index2).scope == 2) {
						if (SymbolTable.symtab.get(index2).id
								.equals(LexicalAnalyzer.old_lexeme)) {
							found = true;
							break;
						}
					}
					index2++;
				}
			}
			// ////////////////////////////////////////////////////////

			if (found) {
				semanticStack.push(SymbolTable.symtab.get(index2).adrs);
				if (SymbolTable.symtab.get(index2).type.equals(Type.BOOLEAN))
					semanticStack.push("boolean");
				if (SymbolTable.symtab.get(index2).type.equals(Type.INT))
					semanticStack.push("int");
			} else {
				throw new CompilerException("Undefined static variable ["
						+ LexicalAnalyzer.lineNumber + "].");
			}
		}
	}

	public static void PUSH_BOOLEAN() {
		semanticStack.push(LexicalAnalyzer.old_lexeme);
		semanticStack.push("boolean");
	}

	public static void PUSH_INT() {
		// semanticStack.push("#" + LexicalAnalyzer.lexeme);
		semanticStack.push("#" + LexicalAnalyzer.old_lexeme);
		semanticStack.push("int");
	}

	public static void INC_SCOPE() {
		LexicalAnalyzer.currentScope++;
		int index = SymbolTable.indexInSymtab((String) semanticStack.peek());
		SymbolTable.symtab.get(index).type = Type.METHOD;
	}

	public static void CHECK_ARG() throws CompilerException {
		String type_str = "" + semanticStack.pop();
		Type type;
		String arg_value = "" + semanticStack.pop();

		if (type_str.equals("boolean"))
			type = Type.BOOLEAN;
		else
			type = Type.INT;

		try {
			if (!((ArrayList<Entry>) semanticStack.peek()).get(0).type
					.equals(type)) {
				throw new CompilerException("Argument type must be "
						+ ((ArrayList<Entry>) semanticStack.peek()).get(0).type
						+ " instead of " + type + LexicalAnalyzer.lineNumber
						+ "].");
			} else {
				PB.add(new ThreeAdrsCode("ASSIGN", (String) arg_value, Integer
						.toString(((ArrayList<Entry>) (semanticStack.peek()))
								.get(0).adrs), ""));
				((ArrayList<Entry>) (semanticStack.peek())).remove(0);

			}
		} catch (Exception e) {
			throw new CompilerException(
					"Number of arguments is more than expected ["
							+ LexicalAnalyzer.lineNumber + "]");
		}

	}

	public static void EXTENSION() throws CompilerException {
		int index = SymbolTable.indexOfClass((String) semanticStack.pop());
		int index_parent = SymbolTable.indexOfClass(LexicalAnalyzer.lexeme);

		if (index_parent > index)
			throw new CompilerException("Undefined class "
					+ LexicalAnalyzer.lexeme);

		SymbolTable.symtab.get(index).parent = SymbolTable.symtab
				.get(index_parent);
	}

	public static void PID() throws CompilerException {
		if (SymbolTable.indexInSymtab(LexicalAnalyzer.old_lexeme) < 0)
			throw new CompilerException("Undefined Variable ["
					+ LexicalAnalyzer.lineNumber + "].");

		semanticStack.push(Integer.toString(SymbolTable.symtab.get(SymbolTable
				.indexInSymtab(LexicalAnalyzer.old_lexeme)).adrs));

		if (SymbolTable.symtab.get(SymbolTable
				.indexInSymtab(LexicalAnalyzer.old_lexeme)).type
				.equals(Type.INT))
			semanticStack.push("int");
		if (SymbolTable.symtab.get(SymbolTable
				.indexInSymtab(LexicalAnalyzer.old_lexeme)).type
				.equals(Type.BOOLEAN))
			semanticStack.push("boolean");
	}

	public static void WHILE() {
		int index = Integer.parseInt(semanticStack.pop() + "");
		semanticStack.pop();
		PB.get(index).set("JPF", (String) semanticStack.pop(),
				Integer.toString(PB.size() + 1), "");
		PB.add(new ThreeAdrsCode("JP", "#" + (semanticStack.pop() + ""), "", ""));
	}

	public static void JPF_SAVE() {
		int index = Integer.parseInt("" + semanticStack.pop());
		semanticStack.pop();

		PB.get(index).set("JPF", "" + semanticStack.pop(),
				Integer.toString(PB.size() + 1), "");

		semanticStack.push(PB.size());
		PB.add(new ThreeAdrsCode());
	}

	public static void LABEL() {
		semanticStack.push(PB.size());
	}

	public static void JP() {
		PB.get(Integer.parseInt("" + semanticStack.pop())).set("JP",
				"#" + PB.size(), "", "");

	}

	public static void PRINT() {
		semanticStack.pop();
		PB.add(new ThreeAdrsCode("PRINT", ((String) semanticStack.pop()), "",
				""));
	}

	public static void ASSIGN() {
		semanticStack.pop();
		String first = "" + (semanticStack.pop());
		semanticStack.pop();

		PB.add(new ThreeAdrsCode("ASSIGN", first, semanticStack.pop() + "", ""));
	}

	public static void ADD() {
		int t = getTemp();
		semanticStack.pop();
		String first = ("" + semanticStack.pop());
		semanticStack.pop();
		PB.add(new ThreeAdrsCode("ADD", "" + semanticStack.pop(), first,
				Integer.toString(t)));
		semanticStack.push(Integer.toString(t));
		semanticStack.push("int");
	}

	public static void MULT() {
		int t = getTemp();
		semanticStack.pop();
		String first = ("" + semanticStack.pop());
		semanticStack.pop();
		PB.add(new ThreeAdrsCode("MULT", "" + semanticStack.pop(), first,
				Integer.toString(t)));
		semanticStack.push(Integer.toString(t));
		semanticStack.push("int");
	}

	public static void SUB() {
		int t = getTemp();
		semanticStack.pop();
		String first = ("" + semanticStack.pop());
		semanticStack.pop();
		PB.add(new ThreeAdrsCode("SUB", "" + semanticStack.pop(), first,
				Integer.toString(t)));
		semanticStack.push(Integer.toString(t));
		semanticStack.push("int");
	}

	public static void AND() {
		int t = getTemp();
		semanticStack.pop();
		String first = ("" + semanticStack.pop());
		semanticStack.pop();
		PB.add(new ThreeAdrsCode("AND", "" + semanticStack.pop(), first,
				Integer.toString(t)));
		semanticStack.push(Integer.toString(t));
		semanticStack.push("boolean");
	}

	public static void EQ() {
		int t = getTemp();
		semanticStack.pop();
		String first = ("" + semanticStack.pop());
		semanticStack.pop();
		PB.add(new ThreeAdrsCode("EQ", "" + semanticStack.pop(), first, Integer
				.toString(t)));
		semanticStack.push(Integer.toString(t));
		semanticStack.push("boolean");
	}

	public static void LT() {
		int t = getTemp();
		semanticStack.pop();
		String first = ("" + semanticStack.pop());
		semanticStack.pop();
		PB.add(new ThreeAdrsCode("LT", "" + semanticStack.pop(), first, Integer
				.toString(t)));
		semanticStack.push(Integer.toString(t));
		semanticStack.push("boolean");
	}

	private static int getTemp() {
		TEMP_ADDRESS += 4;
		return TEMP_ADDRESS;
	}
}

class Method {
	String returnType = "";
	String expression = "";
	String methodID = "";
	String className = "";

	public Method(String returnType, String expression, String methodID,
			String className) {
		this.returnType = returnType;
		this.expression = expression;
		this.methodID = methodID;
		this.className = className;
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