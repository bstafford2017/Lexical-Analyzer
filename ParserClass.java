import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Scanner;
import java.util.Stack;

public class ParserClass {

	public static ArrayList<String> tokens = new ArrayList<>();
	public static int counter = 0;
	
	public static void main(String[] args) {
		LinkedList<Node> list = new LinkedList<>();
		load(args[0], list);
		checkPar();
		if(program()) {
			System.out.println("Sucessful compilation!");
		} else {
			System.out.println("Error: " + tokens.get(counter));
			System.exit(0);
		}
	}
	
	public static void checkPar() {
		ListIterator<String> it = tokens.listIterator();
		Stack<String> stack = new Stack<>();
		while(it.hasNext()) {
			String line = it.next();
			if(line.equals("<lparen>"))
				stack.push(line);
			try {
				if(line.equals("<rparen>"))
					stack.pop();
			} catch (Exception e) {
				System.out.println("Error: <rparen>");
				System.exit(0);
			}
		}
		if(stack.empty()) {
			return;
		} else { 
			System.out.println("Error: " + stack.pop());
			System.exit(0);
		}
	}
	
	public static boolean program() {
		if(stmt_list()) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean stmt_list() {
		if(stmt()) {
			if(counter == tokens.size()) return true;
			if(stmt_list()) {
				return true;
			} else {
				return false;
			}
		} else if (counter == tokens.size()){
			return true;
		} else {
			return true;
		}
	}
	
	public static boolean stmt(){
		try {
			if(assign_stmt()) {
				return true;
			} else if(tokens.get(counter).equals("<read>")) {
				counter += 2;
				if(tokens.get(counter).equals("<id>")) {
					counter += 2;
					return true;
				} else {
					System.out.println("Error: " + tokens.get(counter));
					System.exit(0);
					return false; // (ignore)
				}
			} else if(tokens.get(counter).equals("<print>")) {
				counter +=2;
				if(expr()) {
					counter += 2;
					return true;
				} else {
					System.out.println("Error: " + tokens.get(counter));
					System.exit(0);
					return false; // (ignore)
				}
			} else if(tokens.get(counter).equals("<if_stmt>")) {
				counter += 2;
				if(tokens.get(counter).equals("<lparen>")) {
					counter += 2;
					if(expr()) {
						if(tokens.get(counter).equals("<rparen>")) {
							counter += 2;
							if(stmt()) {
								return true;
							} else {
								return false;
							}
						} else {
							System.out.println("Error: " + tokens.get(counter));
							System.exit(0);
							return false; // (ignore)
						}
					} else {
						return false;
					}
				} else {
					System.out.println("Error: " + tokens.get(counter));
					System.exit(0);
					return false; // (ignore)
				}
			} else {
				return false;
			}
		} catch (Exception e) {
			System.out.println("Error: " + tokens.get(counter - 2) + " " + tokens.get(counter - 1));
			System.exit(0);
			return false; // (ignore)
		}
	}
	
	public static boolean if_stmt() {
		if(stmt()) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean assign_stmt() {
		try {
			if(tokens.get(counter).equals("<id>")) {
				counter += 2;
				if(tokens.get(counter).equals("<assign>")) {
					counter += 2;
					if(expr()) {
						return true;
					} else {
						return false;
					}
				} else {
					return false;
				}
			} else {
				return false;
			}
		} catch (Exception e) {
			System.out.println("Error: " + tokens.get(counter - 2) + " " + tokens.get(counter - 1));
			System.exit(0);
			return false; // (ignore)
		}
	}
	
	public static boolean expr() {
		if(term()) {
			if(term_tail()) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	public static boolean term_tail() {
		if(add_op()) {
			if(term()) {
				if(term_tail()) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else if(counter == tokens.size()) {
			System.out.println("Successful compilation");
			System.exit(0);
			return true; // (ignore)
		}
		else {
			return true;
		}
	}
	
	public static boolean term() {
		if(factor()) {
			if(fact_tail()) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	public static boolean fact_tail() {
		if(mult_op()) {
			if(factor()) {
				if(fact_tail()) {
					return true;
				} else { 
					return false;
				}
			} else {
				return false;
			}
		} else if(counter == tokens.size()) {
			System.out.println("Successful compilation");
			System.exit(0);
			return true; // (ignore)
		} else {
			return true;
		}
	}
	
	public static boolean factor() {
		try {
			if(tokens.get(counter).equals("<id>")) {
				counter += 2;
				return true;
			} else if(tokens.get(counter).equals("<number>")) {
				counter += 2;
				return true;
			} else if(tokens.get(counter).equals("<lparen>")) {
				counter += 2;
				if(expr()) {
					if(tokens.get(counter).equals("<rparen>")) {
						counter += 2;
						return true;
					}  else if (tokens.get(counter).equals("<lparen>")) {
						counter += 2;
						return true;
					} else {
						System.out.println("Error: " + tokens.get(counter));
						System.exit(0);
						return false; // (ignore)
					}
				} else {
					System.out.println("Error: " + tokens.get(counter));
					System.exit(0);
					return false; // (ignore)
				}
			} else {
				System.out.println("Error: " + tokens.get(counter));
				System.exit(0);
				return false; // (ignore>
			}
		} catch (Exception e) {
			System.out.println("Error: " + tokens.get(counter - 2) + " " + tokens.get(counter - 1));
			System.exit(0);
			return false; // (ignore)
		}
	}
	
	public static boolean add_op() {
		try {
			if(tokens.get(counter + 1).equals("+") || tokens.get(counter + 1).equals("-")) {
				counter += 2;
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			System.out.println("Error: " + tokens.get(counter - 2) + " " + tokens.get(counter - 1));
			System.exit(0);
			return false; // (ignore)
		}
	}
	
	public static boolean mult_op() {
		try {
			if(tokens.get(counter + 1).equals("*") || tokens.get(counter + 1).equals("/")) {
				counter += 2;
				return true;
			} else if(tokens.get(counter + 1).equals("//") || tokens.get(counter + 1).equals("%")) {
				counter += 2;
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			System.out.println("Error: " + tokens.get(counter - 2) + " " + tokens.get(counter - 1));
			System.exit(0);
			return false; // (ignore)
		}
	}
	
	public static void load(String filename, LinkedList<Node> list) {
		File file = new File(filename);
		Scanner sc = null;
		try {
			sc = new Scanner(file);
		} catch(FileNotFoundException e) {
			System.out.println("File not found");
			System.exit(0);
		}
		while(sc.hasNextLine()) {
			String line = sc.nextLine();
			if(line.equals("$$$")) break;
			String[] delim = line.split(", ");
			if(delim[0].equals("")) continue;
			if(delim[0].contains("<comment>")) continue;
			tokens.add(delim[0]);
			tokens.add(delim[1]);
			if(delim[0].equals("<id>") && !listContains(delim[1], list)) {
				Node addNode = new Node(delim[1]);
				list.add(addNode);
			}
		}
		sc.close();
	}
	
	public static boolean listContains(String find, LinkedList<Node> list) {
		ListIterator<Node> it = list.listIterator();
		while(it.hasNext()) {
			Node node = it.next();
			if(node.getName().equals(find)) return true;
		}
		return false;
	}
}
