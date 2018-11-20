package main;
import java.util.ArrayList;
import java.util.Arrays;
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
	
//	// Return a list containing the results of 4 methods
//	public List<List<String>> combineMethods(String s) {
//		List<List<String>> combinedList = new ArrayList<>();
//		combinedList.add(removeOneLetter(s));
//		combinedList.add(addOneLetter(s));
//		combinedList.add(replaceOneLetter(s));
//		combinedList.add(switchTwoLetters(s));
//		return combinedList;	
//	}
//	
//	
	// Combine methods again on invalid words if there are no valid words
	public List<List<String>> combineMethods (List<List<String>> combinedList) {
		List<List<String>> combinedAgainList = new ArrayList<>();
		
		List<String> addedAgainList = new ArrayList<>();
		for (List<String> comb : combinedList) {
			for (String word : comb) {
				addedAgainList.addAll(addOneLetter(word));
			}
		}
		combinedAgainList.add(addedAgainList);

		List<String> removedAgainList = new ArrayList<>();
		for (List<String> comb : combinedList) {
			for (String word : comb) {
				removedAgainList.addAll(removeOneLetter(word));
			}
		}
		combinedAgainList.add(removedAgainList);
		
		List<String> replacedAgainList = new ArrayList<>();
		for (List<String> comb : combinedList) {
			for (String word : comb) {
				replacedAgainList.addAll(replaceOneLetter(word));
			}
		}
		combinedAgainList.add(replacedAgainList);
		
		List<String> switchedAgainList = new ArrayList<>();
		for (List<String> comb : combinedList) {
			for (String word : comb) {
				switchedAgainList.addAll(switchTwoLetters(word));
			}
		}
		combinedAgainList.add(switchedAgainList);
		
		return combinedAgainList;
		
	}
	
	// Return only the words found in wordSet
	public List<List<String>> filterValidWords(List<List<String>> combinedList) {
		List<List<String>> validCombinedList = new ArrayList<>();
		for (List<String> combList : combinedList) {
			List<String> validList = new ArrayList<>();
			for (String comb : combList) {
				if (wordSet.contains(comb)) {
					validList.add(comb);
				}
			}
			validCombinedList.add(validList);
		}
		return validCombinedList;
	}
	
	// Assign weight of 1 to all suggestions
	public Map<String,Double> getSuggestionMap(List<List<String>> suggestionsList) {
		Map<String, Double> suggestionMap = new HashMap<>();
		for (List<String> suggestions : suggestionsList) {
			for (String word : suggestions) {
				if (suggestionMap.containsKey(word)) {
					suggestionMap.put(word, suggestionMap.get(word) + 1.0);
				} else {
					suggestionMap.put(word, 1.0);
				}
			}
		}
		return suggestionMap;
	}
	
	// Remove duplicates and return set
	public Set<String> getUniqueSuggestions(List<List<String>> suggestionsList) {
		Set<String> uniqueSuggestions = new HashSet<String>();
		for (List<String> suggestions : suggestionsList) {
			uniqueSuggestions.addAll(suggestions);
		}
		return uniqueSuggestions;
	}
	
	// Sort unique suggestions based on values in map with custom comparator
	public List<String> sortSuggestions(Set<String> uniqueSuggestions, Map<String, Double> combinedMap) {
		List<String> sortedSuggestions = new ArrayList<>();
		sortedSuggestions.addAll(uniqueSuggestions);
		sortedSuggestions.sort((w1,w2) -> combinedMap.get(w2).compareTo(combinedMap.get(w1)));
		return sortedSuggestions;
	}

	// Give a suggestion
	public String suggestOne (String s) {
		List<List<String>> l1 = new ArrayList<>();
		l1.add(Arrays.asList(s));
		
		List<List<String>> combinedList = combineMethods(l1);
		List<List<String>> suggestionsList = filterValidWords(combinedList);
		Map<String,Double> suggestionMap = getSuggestionMap(suggestionsList);
		Set<String> uniqueSuggestions = getUniqueSuggestions(suggestionsList);
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
			return "Guess: " + suggestOne(s);
		}
	}

}
