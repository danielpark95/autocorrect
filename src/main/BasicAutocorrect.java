package main;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/*
 * Basic implementation of Autocorrect that returns a suggestion for a misspelled word.
 * 
 * Given a String s, makeGuess(s) checks if a valid word can be formed by
 * 	1) adding one letter
 * 	2) removing one letter
 * 	3) replacing one letter
 * 	4) switching two adjacent letters
 * 
 * In case of multiple suggestions, the answer with highest count will be returned.
 */
public class BasicAutocorrect implements Autocorrect{
	private Set<String> wordSet;
	private Map<String, String> neighborMap;
	private String letters;
	public boolean verbose = false;
	
	public BasicAutocorrect(Data data) {
		wordSet = data.getWordSet();
		neighborMap = data.getNeighborMap();
		letters = data.getLetters();
	}
	
	// Return all Strings that can be formed by removing one letter from input
	// (e.g. "datax" -> {"atax", "dtax", "daax", "datx","data"})
	public List<String> removeOneLetter(String s) {
		List<String> removedList = new ArrayList<String>();
		for (int i = 0 ; i < s.length(); i++) {
			String removed = s.substring(0, i) + s.substring(i+1, s.length());
			removedList.add(removed);
		}
		return removedList;
	}
	
	// Return all Strings that can be formed by adding one letter to input
	// (e.g. "t" -> {"at", "bt", ... , "yt", "zt", "ta", "tb" , ... , "ty", "tz"})
	public List<String> addOneLetter(String s) {
		List<String> res = new ArrayList<String>();
		for (int i = 0 ; i < letters.length(); i++){
			char letter = letters.charAt(i);
			for (int j = 0; j <= s.length(); j++) {
				if (j < s.length()) {
					String substring = s.substring(0, j) + letter + s.substring(j,s.length());
					res.add(substring);
				} else {
					String substring = s + letter;
					res.add(substring);
				}
			}
		}
		return res;
	}
	
	// Return all Strings that can be formed by replacing one letter with its keyboard neighbor
	public List<String> replaceOneLetter(String s){
		List<String> res = new ArrayList<String>();
		for (int i = 0 ; i < s.length(); i ++) {
			String neighbors = neighborMap.get(s.charAt(i)+"");
			for (int j = 0 ; j < neighbors.length(); j++) {
				String replaced = s.substring(0,i) + neighbors.charAt(j) + s.subSequence(i+1, s.length());
				res.add(replaced);
			}
		}
		return res;
	}
	
	// Return all valid words that can be formed by switching two adjacent letters
	public List<String> switchTwoLetters(String s){
		List<String> res = new ArrayList<String>();
		for (int i = 0 ; i < s.length()-1; i++) {
			String switched = s.substring(0,i) + s.charAt(i+1) + s.charAt(i) + s.substring(i+2,s.length());
			res.add(switched);
		}
		return res;
	}

	// Combine 4 methods and rank words 
	public List<String> combineAndSort(String s) {
		System.out.print ("\n\tBasic ");
		Set<String> combinedSet = new HashSet<String>();
		Map<String, Double> combinedMap = new HashMap<String, Double>();
		
		List<String> combinedList = new ArrayList<>();
		combinedList.addAll(removeOneLetter(s));
		combinedList.addAll(addOneLetter(s));
		combinedList.addAll(replaceOneLetter(s));
		combinedList.addAll(switchTwoLetters(s));
		
		List<String> validCombinedList = new ArrayList<>();
		for (String comb : combinedList) {
			if (wordSet.contains(comb)) {
				validCombinedList.add(comb);
			}
		}
		
		for (String word : validCombinedList) {
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
