
package axm170039;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

//Driver program for red black tree implementation.

public class RedBlackTreeDriver {
	public static void main(String[] args) throws FileNotFoundException {
		Scanner sc;

		File file = new File("E:\\Github_Repo\\LP3\\axm170039\\test-lp3\\sk-t02.txt");
	    sc = new Scanner(file);
		// if (args.length > 0) {
		// 	File file = new File(args[0]);
		// 	sc = new Scanner(file);
		// } else {
		// 	sc = new Scanner(System.in);
		// }
		String operation = "";
		long operand = 0;
		int modValue = 999983;
		long result = 0;
		RedBlackTree<Long> redBlackTree = new RedBlackTree<>();
		// Initialize the timer
		Timer timer = new Timer();

		while (!((operation = sc.next()).equals("End"))) {
			switch (operation) {
				case "Add": {
					operand = sc.nextLong();
					if(redBlackTree.add(operand)) {
						result = (result + 1) % modValue;
					}
					break;
				}
				case "Remove": {
					operand = sc.nextLong();
					if (redBlackTree.remove(operand) != null) {
						result = (result + 1) % modValue;
					}
					break;
				}
				case "Contains":{
					operand = sc.nextLong();
					if (redBlackTree.contains(operand)) {
						result = (result + 1) % modValue;
					}
					break;
				}
			}
		}

		// End Time
		timer.end();

		System.out.println(result);
		System.out.println(timer);
	}
}
