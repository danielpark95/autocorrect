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
 * BasicAutocorrect can only return a suggestion that's one character away from input.
 */
public class BasicAutocorrect implements Autocorrect{
	private Set<String> wordSet;
	private Map<String, String> neighborMap;
	private String letters;
	
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
		List<String> addedList = new ArrayList<String>();
		for (int i = 0 ; i < letters.length(); i++){
			char letter = letters.charAt(i);
			for (int j = 0; j <= s.length(); j++) {
				if (j < s.length()) {
					String substring = s.substring(0, j) + letter + s.substring(j,s.length());
					addedList.add(substring);
				} else {
					String substring = s + letter;
					addedList.add(substring);
				}
			}
		}
		return addedList;
	}
	
	// Return all Strings that can be formed by replacing one letter with its keyboard neighbor
	// (e.g. "mt" -> {"nt","kt","jt","mr","mf","mg","mh","my"})
	public List<String> replaceOneLetter(String s){
		List<String> replacedList = new ArrayList<String>();
		for (int i = 0 ; i < s.length(); i ++) {
			String neighbors = neighborMap.get(s.charAt(i)+"");
			for (int j = 0 ; j < neighbors.length(); j++) {
				String replaced = s.substring(0,i) + neighbors.charAt(j) + s.subSequence(i+1, s.length());
				replacedList.add(replaced);
			}
		}
		return replacedList;
	}
	
	// Return all valid words that can be formed by switching two adjacent letters
	// (e.g. "ehllo" -> {"hello", "elhlo", "ehllo", "ehlol"})
	public List<String> switchTwoLetters(String s){
		List<String> switchedList = new ArrayList<String>();
		for (int i = 0 ; i < s.length()-1; i++) {
			String switched = s.substring(0,i) + s.charAt(i+1) + s.charAt(i) + s.substring(i+2,s.length());
			switchedList.add(switched);
		}
		return switchedList;
	}
	
	// Return a list containing the results of 4 methods
	public List<String> combineMethods(String s) {
		List<String> combinedList = new ArrayList<>();
		combinedList.addAll(removeOneLetter(s));
		combinedList.addAll(addOneLetter(s));
		combinedList.addAll(replaceOneLetter(s));
		combinedList.addAll(switchTwoLetters(s));
		return combinedList;	
	}
	
	// Return only the words found in wordSet
	public List<String> filterValidWords(List<String> combinedList) {
		List<String> validCombinedList = new ArrayList<>();
		for (String comb : combinedList) {
			if (wordSet.contains(comb)) {
				validCombinedList.add(comb);
			}
		}
		return validCombinedList;
	}
	
	// Assign weight to suggestions
	public Map<String,Double> getSuggestionMap(Set<String> uniqueSuggestions) {
		Map<String, Double> suggestionMap = new HashMap<>();
		for (String word : uniqueSuggestions) {
			if (suggestionMap.containsKey(word)) {
				suggestionMap.put(word, suggestionMap.get(word) + 1.0);
			} else {
				suggestionMap.put(word, 1.0);
			}
		}
		return suggestionMap;
	}
	
	// Sort unique suggestions based on values in map with custom comparator
	public List<String> sortSuggestions(Set<String> uniqueSuggestions, Map<String, Double> combinedMap) {
		List<String> sortedSuggestions = new ArrayList<>();
		sortedSuggestions.addAll(uniqueSuggestions);
		sortedSuggestions.sort((w1,w2) -> combinedMap.get(w2).compareTo(combinedMap.get(w1)));
		return sortedSuggestions;
	}

	// Suggest the most frequently occurring word in combined
	public String makeGuess (String s) {
		List<String> combinedList = combineMethods(s);
		List<String> suggestions = filterValidWords(combinedList);
		Set<String> uniqueSuggestions = new HashSet<String>(suggestions);
		Map<String,Double> suggestionMap = getSuggestionMap(uniqueSuggestions);
		List<String> sortedSuggestions = sortSuggestions(uniqueSuggestions, suggestionMap);
		
		if (sortedSuggestions.size() > 0) {
			return sortedSuggestions.get(0);
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
