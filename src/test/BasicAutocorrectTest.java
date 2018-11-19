package test;
import main.*;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import java.util.List;

public class BasicAutocorrectTest {
	Data data = Data.getInstance();
	String[] correct = {"on","any","hello", "world", "oracle", "testing", "example"};
	boolean verbose = true; // Set true to print all test cases
	
	@Test
	public void testAddOneLetter() { 
		Autocorrect ac = new BasicAutocorrect(data);
		String[][] misspelled = {
				{"n","o"},
				{"ny","ay","an"},
				{"ello", "hllo", "helo","hell"},
				{"orld", "wrld", "wold", "word", "worl"},
				{"racle", "oacle", "orcle", "orale", "orace", "oracl"},
				{"esting", "tsting", "teting", "tesing", "testng", "testig", "testin"},
				{"xample", "eample", "exmple", "exaple","examle", "exampe", "exampl"}
		};
		if (verbose) {
			System.out.println(String.format("%-7s %-22s %s","*****", "testAddOneLetter()", "*****"));
		}
		for (int i = 0; i < misspelled.length; i++) {
			if (verbose)
				System.out.println((i+1) + ". (" + correct[i] + ")");
			for (int j = 0; j < misspelled[i].length; j++) {
				List<String> res = ac.addOneLetter(misspelled[i][j]);
				if (verbose) {
					System.out.println(String.format("   %-10s %-3s %s", "\"" + misspelled[i][j] + "\"", "->",  res));}
				assertTrue(res.contains(correct[i]));
			}
		}
	}

	@Test
	public void testRemoveOneLetter() {
		Autocorrect ac = new BasicAutocorrect(data);
		String[][] misspelled = {
				{"yon","ozn", "onq"},
				{"aany","anmy","anyy"},
				{"fhello","hfello","heello","helzlo","hellqo","hellox"},
				{"qworld","wsorld","woorld","worrld","worldd"},
				{"poracle","otracle","oraacle","oraclle","oracloe","oraclee"},
				{"ftesting","tzesting","tes2ting","test5ing","test9ing","testinng","testingg"},
				{"rexample","eexample","exxample","examnple","exampple","examplee","exampleq"}
		};
		if (verbose) {
			System.out.println(String.format("\n%-7s %-22s %s","*****", "testRemoveOneLetter()", "*****"));
		}
		for (int i = 0; i < misspelled.length; i++) {
			if (verbose) {
				System.out.println((i+1) + ". (" + correct[i] + ")");}
			for (int j = 0; j < misspelled[i].length; j++) {
				List<String> res = ac.removeOneLetter(misspelled[i][j]);
				if (verbose) {
					System.out.println(String.format("   %-10s %-3s %s", "\"" + misspelled[i][j] + "\"", "->",  res));}
				assertTrue(res.contains(correct[i]));
			}
		}
	}

	@Test
	public void testReplaceOneLetter() {
		Autocorrect ac = new BasicAutocorrect(data);
		String[][] misspelled = {
				{"pn","om"},
				{"zny","amy","anu"},
				{"nello","hrllo","heklo","helpo","hellp"},
				{"eorld","wprld","wotld","workd","worlc"},
				{"pracle","otacle","orzcle","oradle","oracoe","oraclf"},
				{"yesting","trsting","tezting","tesring","testong","testimg","testinf"},
				{"3xample","ezample","exxmple","exa ple","exam[le","exampke","examplr"}
		};
		if (verbose) {
			System.out.println(String.format("\n%-7s %-22s %s","*****", "testReplaceOneLetter()", "*****"));
		}
		for (int i = 0; i < misspelled.length; i++) {
			if (verbose) {
				System.out.println((i+1) + ". (" + correct[i] + ")");}
			for (int j = 0; j < misspelled[i].length; j++) {
				List<String> res = ac.replaceOneLetter(misspelled[i][j]);
				if (verbose) {
					System.out.println(String.format("   %-10s %-3s %s", "\"" + misspelled[i][j] + "\"", "->",  res));}
				assertTrue(res.contains(correct[i]));
			}
		}
	}

	@Test
	public void testSwitchTwoLetters() {
		Autocorrect ac = new BasicAutocorrect(data);
		String[][] misspelled = {
				{"no"},
				{"nay","ayn"},
				{"ehllo","hlelo","hello","helol"},
				{"owrld","wrold","wolrd","wordl"},
				{"roacle","oarcle","orcale","oralce","oracel"},
				{"etsting","tseting","tetsing","tesitng","testnig","testign"},
				{"xeample","eaxmple","exmaple","exapmle","examlpe","exampel"}
		};
		if (verbose) {
			System.out.println(String.format("\n%-7s %-22s %s","*****", "testSwitchTwoLetters()", "*****"));
		}
		for (int i = 0; i < misspelled.length; i++) {
			if (verbose) {
				System.out.println((i+1) + ". (" + correct[i] + ")");}
			for (int j = 0; j < misspelled[i].length; j++) {
				List<String> res = ac.switchTwoLetters(misspelled[i][j]);
				if (verbose) {
					System.out.println(String.format("   %-10s %-3s %s", "\"" + misspelled[i][j] + "\"", "->",  res));}
				assertTrue(res.contains(correct[i]));
			}
		}
	}
}