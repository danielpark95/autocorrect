package main;

import java.util.List;
import java.util.Map;
import java.util.Set;
/*
 * Interface for Autocorrect.
 * Actual implementation in BasicAutocorrect.java and AdvancedAutocorrect.java
 */
public interface Autocorrect {
	List<String> removeOneLetter(String s);
	List<String> addOneLetter(String s);
	List<String> replaceOneLetter(String s);
	List<String> switchTwoLetters(String s);
	
	List<List<String>> combineMethods(List<List<String>> l);
	List<List<String>> filterValidWords(List<List<String>> l);
	Set<String> getUniqueSuggestions(List<List<String>> l);
	List<String> sortSuggestions(Set<String> s, Map<String, Double> m);

	String suggestOne(String s);
	String getResult(String s);
}