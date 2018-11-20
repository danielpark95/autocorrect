package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
/*
 * Advanced implementation of Autocorrect that
 * 	1) favors suggestions that are common words over uncommon words
 * 	2) favors suggestions with length similar to input
 * 	3) finds suggestions up to 3 letters different from input
 */
public class AdvancedAutocorrect extends BasicAutocorrect{
	// Top 100k english words, ordered by frequency
	// "the" -> 1, "of" -> 2, "and" -> 3, etc.
	private Map<String, Integer> rankMap;
	
	public AdvancedAutocorrect(Data data) {
		super(data);
		rankMap = data.getRankMap();
	}
	
	// Get min,max rank of all the words in combined set. Helper function for normalizeRankMap
	public double[] getMinMax(Set<String> combinedSet) {
		double min = Double.MAX_VALUE;
		double max = Double.MIN_VALUE;
		for (String word : combinedSet) {
			if (rankMap.containsKey(word)) {
				double rank = rankMap.get(word);
				if (rank > 0) {
					if (rank > max) {
						max = rank;
					} else if (rank < min) {
						min = rank;
					}
				}
			}
		}
		double[] minMax = {min, max};
		return minMax;
	}
	
	// Create a weight system that gives the most uncommon word in a set 0.8 multiplier,
	// and the most common word a 1.3 multiplier. If a word is not found in the top 100k
	// dataset, it will get a 0.7 multiplier.
	public Map<String,Double> normalizeRankMap(Set<String> combinedSet){
		double[] minMax = getMinMax(combinedSet);
		double min = minMax[0];
		double max = minMax[1];
		Map<String,Double> logMap = new HashMap<>();

		double floor = 0.8;		// Weight multiplier for least frequent word in set
		double ceiling = 1.3;	// Weight multiplier for most frequent word in set
		for (String word : combinedSet) {
			if (!rankMap.containsKey(word)) {
				logMap.put(word, 0.7);
			} else {
				double rank = rankMap.get(word);
				// min,max normalization of word rank (logged for smaller spread)
				double z = (Math.log10(rank) - Math.log10(max)) / (Math.log10(min) - Math.log10(max));
				z = (z * (ceiling - floor)) + floor;
				logMap.put(word, z);
			}
		}
		return logMap;
	}
	
	// Combine the word frequency weight system with word-length weight system
	public Map<String, Double> combineMaps (Set<String> combinedSet, Map<String,Double> normalizedRankMap, Map<String,Double> combinedMap){
		for (String word : combinedSet) {
			combinedMap.put(word, combinedMap.get(word) * normalizedRankMap.get(word));
		}
		return combinedMap;
	}
	
	// Suggested words from replaceOneLetter and switchTwoLetters are assigned a multiplier of 1.5,
	// while remove and add are assigned 1. This is to favor suggestions that are more similar to 
	// input.
	public Map<String,Double> getSuggestionMap(List<List<String>> suggestionsList, Double[] weights, int runCount) {
		Map<String, Double> suggestionMap = new HashMap<>();
		for (int i = 0; i < 4; i++) {
			for (String word : suggestionsList.get(i)) {
				if (suggestionMap.containsKey(word)) {
					suggestionMap.put(word, suggestionMap.get(word) + weights[i] / (runCount + 1));
				} else {
					suggestionMap.put(word, weights[i] / (runCount + 1));
				}
			}
		}
		return suggestionMap;
	}
	
	// Suggest the most frequently occurring word in combined
	@Override
	public String suggestOne(String s) {
		Double[] weights = {1.0,1.0,1.5,1.5};
		int runCount = 0;
		List<List<String>> l1 = new ArrayList<>();
		l1.add(Arrays.asList(s));
		List<List<String>> combinedList = combineMethods(l1);
		List<List<String>> suggestionsList = filterValidWords(combinedList);
		Set<String> uniqueSuggestions = getUniqueSuggestions(suggestionsList);
		
		while (++runCount < 2 && uniqueSuggestions.isEmpty()) {
			combinedList = combineMethods(combinedList);
			suggestionsList = filterValidWords(combinedList);
			uniqueSuggestions = getUniqueSuggestions(suggestionsList);
		}
		
		Map<String, Double> suggestionMap = getSuggestionMap(suggestionsList,weights,runCount);
		Map<String, Double> normalizedRankMap = normalizeRankMap(uniqueSuggestions);
		Map<String, Double> finalMap = combineMaps(uniqueSuggestions, suggestionMap, normalizedRankMap);
		List<String> sortedSuggestions = sortSuggestions(uniqueSuggestions, finalMap);
		
		if (sortedSuggestions.size() > 0) {
			return sortedSuggestions.get(0);
		} else {
			return "N/A";
		}
	}
}