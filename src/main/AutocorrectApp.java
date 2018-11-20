package main;
import java.util.Scanner;

/*
 * Application to run Basic and Advanced Autocorrect.
 */
public class AutocorrectApp {
	public static void main(String[] args) {
		Data data = Data.getInstance();
		BasicAutocorrect basicAC = new BasicAutocorrect(data);
		AdvancedAutocorrect advancedAC = new AdvancedAutocorrect(data);
		
		System.out.println("Autocorrect Demo\n");
		System.out.println("Type \"quit\" to end program.\n");

		Scanner scanner = new Scanner(System.in);
		while (true) {
			System.out.print("Please enter a word: ");
			String word = scanner.nextLine().toLowerCase().trim();
			if (word.equals("quit")) {
				break;
			} else {
				System.out.println("\tBasic " + basicAC.getResult(word));
				System.out.println("\tAdvanced " + advancedAC.getResult(word) + "\n");
			}
		}
		System.out.println("Program ended. Bye!");
		scanner.close();
	}
}