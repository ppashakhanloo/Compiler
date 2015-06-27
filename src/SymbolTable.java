import java.util.ArrayList;

public class SymbolTable {
	static ArrayList<Entry> symtab = new ArrayList<Entry>();

	public static void put(String id, Type type, boolean isDecleration,
			int scope) {
		if (isDecleration) {
			symtab.add(new Entry(id, type, scope));
		} else {
			if (foundInSymtab(id) != -1) {
				symtab.add(new Entry(id, type, scope));
			}
		}
	}

	public static void setIsStatic(String id, boolean isStatic) {
		int index = indexInSymtab(id);
		symtab.get(index).isStatic = isStatic;
	}

	public static void setAdrs(String id, int adrs) {
		int index = indexInSymtab(id);
		symtab.get(index).adrs = adrs;
	}

	public static void setParent(String id, String id_parent) {
		int index = indexInSymtab(id);
		int index_parent = indexInSymtab(id_parent);
		symtab.get(index).parent = symtab.get(index_parent);
	}

	public static int indexInSymtab(String id) {
		int index = symtab.size() - 1;
		while (symtab.get(index).scope >= 2 && index >= 0) {
			if (symtab.get(index).id.equals(id)) {
				return index;
			}
			index--;
		}
		if (index < 0) {
			return -1;
		}

		while (index >= 0 && symtab.get(index).scope > 1) {
			if (symtab.get(index).scope == 2) {
				if (symtab.get(index).id.equals(id)) {
					return index;
				}
				index--;
			}
		}
		if (index < 0) {
			return -1;
		}

		Entry parent = symtab.get(index).parent;
		while (parent != null) {
			index--;
			if (index < 0) {
				return -1;
			}
			while (!symtab.get(index).equals(parent) && index >= 0) {
				index--;
			}
			if (index < 0) {
				return -1;
			}

			parent = symtab.get(index).parent;
			index++;
			while (symtab.get(index).scope != 1) {
				if (symtab.get(index).scope == 2) {
					if (symtab.get(index).id.equals(id)) {
						return index;
					}
				}
				index++;
			}
		}

		return -1;
	}

	public static int foundInSymtab(String id) {
		int index = symtab.size() - 1;
		while (symtab.get(index).scope >= 2 && index >= 0) {
			if (symtab.get(index).id.equals(id)) {
				return symtab.get(index).adrs;
			}
			index--;
		}
		if (index < 0) {
			return -1;
		}

		while (index >= 0 && symtab.get(index).scope > 1) {
			if (symtab.get(index).scope == 2) {
				if (symtab.get(index).id.equals(id)) {
					return symtab.get(index).adrs;
				}
				index--;
			}
		}
		if (index < 0) {
			return -1;
		}

		Entry parent = symtab.get(index).parent;
		while (parent != null) {
			index--;
			if (index < 0) {
				return -1;
			}
			while (!symtab.get(index).equals(parent) && index >= 0) {
				index--;
			}
			if (index < 0) {
				return -1;
			}

			parent = symtab.get(index).parent;
			index++;
			while (symtab.get(index).scope != 1) {
				if (symtab.get(index).scope == 2) {
					if (symtab.get(index).id.equals(id)) {
						return symtab.get(index).adrs;
					}
				}
				index++;
			}
		}

		return -1;
	}

}

enum Type {
	INT, BOOLEAN, CLASS, METHOD;
}

class Entry {
	public Type type;
	public String id = "";
	public int adrs;
	public int scope;
	public Entry parent = null;
	public boolean isStatic = false;
	public boolean isArgument = false;

	public Entry(String id, Type type, int scope) {
		this.id = id;
		this.type = type;
		this.scope = scope;
	}

	@Override
	public boolean equals(Object o) {
		Entry entry = (Entry) o;
		return this.type.equals(entry.type) && this.id.equals(entry.id)
				&& this.adrs == entry.adrs && this.scope == entry.scope
				&& this.parent == entry.parent
				&& this.isStatic == entry.isStatic
				&& this.isArgument == entry.isArgument;
	}
}