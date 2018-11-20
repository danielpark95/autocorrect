package main;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/*
 * Basic implementation of Autocorrect that returns the most frequently suggested word 
 * after running 4 methods (add one letter, remove one letter, replace one letter, switch
 * two adjacent letters).
 */
public class BasicAutocorrect implements Autocorrect{
	private Set<String> wordSet;
	private Map<String, String> neighborMap;
	private String letters;
	boolean verbose = false;
	
	public BasicAutocorrect(Data data) {
		wordSet = data.getWordSet();
		neighborMap = data.getNeighborMap();
		letters = data.getLetters();
	}
	
	// Return all valid words that can be formed by dropping one letter from misspelled input
	public List<String> removeOneLetter(String s) {
		List<String> res = new ArrayList<String>();
		for (int i = 0 ; i < s.length(); i++) {
			String dropped = s.substring(0, i) + s.substring(i+1, s.length());
			if (wordSet.contains(dropped)) {
				res.add(dropped);
			}
		}
		if (verbose) { 
			System.out.println("\tremoveOneLetter = " + res );	
		}
		return res;
	}
	
	// Return all valid words that can be formed by adding one letter to input
	public List<String> addOneLetter(String s) {
		List<String> res = new ArrayList<String>();
		
		for (int i = 0 ; i < letters.length(); i++){
			char letter = letters.charAt(i);
			for (int j = 0; j <= s.length(); j++) {
				if (j < s.length()) {
					String substring = s.substring(0, j) + letter + s.substring(j,s.length());
					if (wordSet.contains(substring)) {
						res.add(substring);
					}
				} else {
					String substring = s + letter;
					if (wordSet.contains(substring)) {
						res.add(substring);
					}
				}
			}
		}
		if (verbose) { 
			System.out.println("\taddOneLetter = " + res );	
		}
		return res;
	}
	
	// Return all valid words that can be formed by replacing one letter with its keyboard neighbor
	public List<String> replaceOneLetter(String s){
		List<String> res = new ArrayList<String>();
		for (int i = 0 ; i < s.length(); i ++) {
			String neighbors = neighborMap.get(s.charAt(i)+"");
			for (int j = 0 ; j < neighbors.length(); j++) {
				String replaced = s.substring(0,i) + neighbors.charAt(j) + s.subSequence(i+1, s.length());
				if (wordSet.contains(replaced)) {
					res.add(replaced);}
			}
		}
		if (verbose) { 
			System.out.println("\treplaceOneLetter = " + res );	
		}
		return res;
	}
	
	// Return all valid words that can be formed by switching two adjacent letters
	public List<String> switchTwoLetters(String s){
		List<String> res = new ArrayList<String>();
		for (int i = 0 ; i < s.length()-1; i++) {
			String switched = s.substring(0,i) + s.charAt(i+1) + s.charAt(i) + s.substring(i+2,s.length());
			if (wordSet.contains(switched)) {
				res.add(switched);}
		}
		if (verbose) { 
			System.out.println("\tswitchTwoLetters = " + res );	
		}
		return res;
	}

	// Combine 4 methods and rank words 
	public List<String> combineAndSort(String s) {
		System.out.print ("\n\tBasic ");
		//System.out.println("\tinput = \""  + s + "\"");
		Set<String> combinedSet = new HashSet<String>();
		Map<String, Double> combinedMap = new HashMap<String, Double>();
		
		List<String> combinedList = new ArrayList<>();
		combinedList.addAll(removeOneLetter(s));
		combinedList.addAll(addOneLetter(s));
		combinedList.addAll(replaceOneLetter(s));
		combinedList.addAll(switchTwoLetters(s));
		
		for (String word : combinedList) {
			if (combinedSet.contains(word)) {
				combinedMap.put(word, combinedMap.get(word) + 1.0);
			} else {
				combinedSet.add(word);
				combinedMap.put(word, 1.0);
			}
		}
	
		List<String> rankedWords = sortWords(combinedSet, combinedMap);
		return rankedWords;	
	}
	
	// Sort words from set based on values in map with custom comparator
	public List<String> sortWords(Set<String> combinedSet, Map<String, Double> combinedMap) {
		List<String> resultList = new ArrayList<>();
		resultList.addAll(combinedSet);
		if (verbose) {
			System.out.println("\tcombined set = " + resultList);
			System.out.println("\tcombined map = " + combinedMap);			
		}
		resultList.sort((w1,w2) -> combinedMap.get(w2).compareTo(combinedMap.get(w1)));
		if (verbose) {
			System.out.println("\tafter sort = " + resultList);	
		}
		return resultList;
	}

	// Suggest the most frequently occurring word in combined
	public String makeGuess (String s) {
		List<String> sortedWords = combineAndSort(s);
		if (sortedWords.size() > 0) {
			return sortedWords.get(0);
		} else {
			return "N/A";
		}
	}
	
	// Access point for application to return result
	public String getResult(String s) {
		if (wordSet.contains(s)) {
			return "Found: " + s;
		} else {
			return "Guess: " + makeGuess(s);
		}
	}
}
