import java.io.File;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Queue;
import java.util.Scanner;

public class AdHocScanner {

	public static void main(String[] args) {

		if(args.length != 1) {
			System.out.println("Invalid command line arguments!\nMust include filename.");
			System.exit(0);
		}
		
		// Setup data structures
		LinkedList<Node> list = new LinkedList<>();
		Queue<String> queue = new LinkedList<>();
		
		// Load queue from file
		loadQueue(queue, args[0]);
		
		while(!queue.isEmpty()) {
			if(queue.peek().equals("read")) {
				// Remove "read"
				queue.poll();
				String pattern = "([a-zA-Z])+([a-zA-Z]|\\d)*";
				
				// Checks if value variable name
				if(queue.peek().matches(pattern)) {
					System.out.println("<read>, read");
					String temp = queue.poll();
					list.add(new Node(temp));
					System.out.println("<id>, " + temp);
				} else {
					System.out.println("<error>, " + queue.poll());
					System.exit(0);
				}
			} else if(queue.peek().equals("write")) {
				// Remove "write"
				queue.poll();
				System.out.println("<write>, write");
				String temp = queue.poll();
				System.out.println("<id>, " + temp);
			} else if(queue.peek().matches("if")) {
				System.out.println("<if>, " + queue.poll());
				while(!queue.peek().equals("\n")) {
					queue.poll();
				}
			} else if(queue.peek().matches("\n")) {
				queue.poll();
			} else if(queue.peek().matches("([a-zA-Z])+([a-zA-Z]|\\d)*")){
				// Remove assignment variable
				String temp = queue.poll();
				temp = temp.trim();
				System.out.println("<id>, " + temp);
				
				// Checks there is only one variable with assignment
				if(queue.peek().equals("=")) {
					System.out.println("<assign>, " + queue.poll());
					while(!queue.peek().equals("\n"))
						arithmetic(queue, list);
				} else {
					System.out.println("<error>, " + queue.poll());
					System.exit(0);
				}
			} else {
				System.out.println("<error>, " + queue.poll());
				System.exit(0);
			}
		}
	}
	
	public static void loadQueue(Queue<String> queue, String filename) {
		filename = filename.trim();
		Scanner sc = null;
		try {
			sc = new Scanner(new File(filename));
		} catch (Exception e) {
			System.out.println("File \"" + filename + "\" does not exit!");
			System.exit(0);
		}
		while(sc.hasNextLine()) {
			String line = sc.nextLine();
			
			// Add ' ' before and after comment
			String[] splitHashtag = line.split("#");
			String addSpaceToHashtag = "";
			for(int i = 0; i < splitHashtag.length; i++) {
				splitHashtag[i] = splitHashtag[i].concat(" # ");
				addSpaceToHashtag = addSpaceToHashtag.concat(splitHashtag[i]);
			}
			
			// Add "\n" to  the end of the array
			String[] splitSpace = addSpaceToHashtag.split("(\\s++)");
			String[] addNewLine = new String[splitSpace.length + 1];
			for(int i = 0; i < addNewLine.length; i++){
				if(i == splitSpace.length){
					addNewLine[i] = "\n";
					break;
				} else {
					addNewLine[i] = splitSpace[i];
				}				
			}

			// Delete comments
			for(int i = 0; i < addNewLine.length; i++) {
				if(addNewLine[i].equals("#")) {
					while(!addNewLine[i].equals("\n")) {
						addNewLine[i] = "";
						i++;
					}
				}
			}
			
			// Add space between all non key words
			// to split up arithemic operations
			for(int i = 0; i < addNewLine.length; i++) {
				if(addNewLine[i].equals("read")) {
					// ignore
				} else if(addNewLine[i].equals("write")) {
					// ignore
				} else if(addNewLine[i].equals("if")) {
					// ignore
				} else if(addNewLine[i].matches("([a-zA-Z])+([a-zA-Z]|\\\\d)*")){
					// ignore
				} else if(addNewLine[i].equals("")) {
					// ignore
				} else if(addNewLine[i].equals("\n")) {
					// ignore
				} else if(addNewLine[i].matches("[0-9]+(\\.\\d+)?")) {
					// ignore
				} else {
					// Overwrite current string with all spaced out strings
					String addSpaceToOp = "";
					for(int j = 0; j < addNewLine[i].length(); j++)
						if(j == addNewLine[i].length() - 1)
							addSpaceToOp = addSpaceToOp.concat(addNewLine[i].charAt(j) + "");
						else 
							addSpaceToOp = addSpaceToOp.concat(addNewLine[i].charAt(j) + " ");
					addNewLine[i] = addSpaceToOp;
				}
			}
	
			// Concat addNewLine to create full string
			String temp = "";
			for(int i = 0; i < addNewLine.length; i++) {
				temp = temp.concat(addNewLine[i] + " ");
			}
			
			// Split full string according to spaces
			// in order to separate nonspaced math operations 
			String[] last = temp.split(" ");	
			
			// Add all 'valid' elements into queue 
			for(String value : last)
				if(!value.equals(""))
					queue.add(value);
		}
	}
	
	public static boolean listContains(LinkedList<Node> list, String find) {
		ListIterator<Node> it = list.listIterator();
		while(it.hasNext()) {
			Node temp = it.next();
			if(find.equals(temp.getName()))
				return true;
		}
		return false;
	}
	
	public static void arithmetic(Queue<String> queue, LinkedList<Node> list) {
		if(queue.peek().equals("+") || queue.peek().equals("-")) {
			System.out.println("<add_op>, " + queue.poll());
		} else if(queue.peek().equals("*") || queue.peek().equals("/") || queue.peek().equals("%")) {
			System.out.println("<mult_op>, " + queue.poll());
		} else if(queue.peek().equals("(")){
			System.out.println("<lparen>, " + queue.poll());
		} else if(queue.peek().equals(")")){
			System.out.println("<rparen>, " + queue.poll());
		} else if(listContains(list, queue.peek())) {
			System.out.println("<id>, " + queue.poll());
		} else if(queue.peek().matches("[0-9]+(\\.\\d+)?")){
			System.out.println("<number>, " + queue.poll());
		} else if(queue.peek().matches("> | >= | < | <= | == | != ")) {
			System.out.println("<rel_op>, " + queue.poll());
		}
		else {
			System.out.println("<error>, " + queue.poll());
			System.exit(0);
		}
	}
}