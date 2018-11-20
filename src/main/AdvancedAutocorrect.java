package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
/*
 * Advanced implementation of AutoCorrect that
 * 	1) extends BasicAutocorrect
 * 	2) takes into account word frequency in final ranking
 * 	3) weighs equal-length words returned from replaceOneLetter() and switchTwoLetters() more
 * 	   than addOneLetter() and removeOneLetter()
 *     (e.g. "esting" could be "eating" or "testing", but "eating" will be prioritized) 
 */
public class AdvancedAutocorrect extends BasicAutocorrect{
	private Set<String> wordSet;
	private Map<String, String> neighborMap;
	private String letters;
	private Map<String, Integer> rankMap;
	
	public AdvancedAutocorrect(Data data) {
		super(data);
		wordSet = data.getWordSet();
		neighborMap = data.getNeighborMap();
		letters = data.getLetters();
		rankMap = data.getRankMap();
		super.verbose = false;
	}
	boolean verbose = false;
	
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
	
	// rankMap contains the ranks of words in 
	public Map<String,Double> normalizeRankMap(Set<String> combinedSet, Map<String, Double> combinedMap) {
		double[] minMax = getMinMax(combinedSet);
		double min = minMax[0];
		double max = minMax[1];
		double floor = 1.0;
		double ceiling = 1.3;
		Map<String,Double> logMap = new HashMap<>();
		
		Map<String, Double> rankMap2 = new HashMap<>();
		for (String word : combinedSet) {
			if (!rankMap.containsKey(word)) {
				rankMap2.put(word, -1.0);
			} else {
				rankMap2.put(word, 0.0 + rankMap.get(word));
			}
		}
		if (verbose) {
			System.out.println("\trankMap = " + rankMap2);	
		}
		
		for (String word : combinedSet) {
			if (!rankMap.containsKey(word)) {
				logMap.put(word, 0.9);
			} else {
				double rank = rankMap.get(word);
				// min,max normalization of word rank (logged for smaller spread)
				double z = (Math.log10(rank) - Math.log10(max)) / (Math.log10(min) - Math.log10(max));
				z = (z * (ceiling - floor)) + floor;
				logMap.put(word, z);
			}
		}
		if (verbose) {
			System.out.println("\tlogMap = " + logMap);
		}
		return logMap;
	}
	
	
	public Map<String, Double> updateMap (Set<String> combinedSet, Map<String,Double> combinedMap){
		Map<String, Double> logMap = normalizeRankMap(combinedSet, combinedMap);
		for (String word : combinedSet) {
			combinedMap.put(word, combinedMap.get(word) * logMap.get(word));
		}
		return combinedMap;
	}
	
	//Overloading parent method to include weights as a parameter
	public List<String> combineAndSort(String s, Double[] weights) {
		System.out.print("\tAdvanced ");
		Set<String> combinedSet = new HashSet<String>();
		Map<String, Double> combinedMap = new HashMap<String, Double>();
		
		List<List<String>> validCombinedList = new ArrayList<>();
		
		List<List<String>> combinedList = new ArrayList<>();
		combinedList.add(removeOneLetter(s));
		combinedList.add(addOneLetter(s));
		combinedList.add(replaceOneLetter(s));
		combinedList.add(switchTwoLetters(s));

		int runCount = 0;
		boolean foundWord = false;
		for (List<String> combList : combinedList) {
			List<String> validCombList = new ArrayList<>();
			for (String comb : combList) {
				if (wordSet.contains(comb)) {
					validCombList.add(comb);
					foundWord = true;
				}
			}
			validCombinedList.add(validCombList);
		}
		
		while (runCount < 3 && !foundWord) {		
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
			
			for (List<String> combList : combinedAgainList) {
				List<String> validCombList = new ArrayList<>();
				for (String comb : combList) {
					if (wordSet.contains(comb)) {
						validCombList.add(comb);
						foundWord = true;
					}
				}
				validCombinedList.add(validCombList);
			}	
			runCount++;
		}
		
		for (int i = 0; i < 4; i++) {
			for (String word : validCombinedList.get(4*runCount + i)) {
				if (combinedSet.contains(word)) {
					combinedMap.put(word, combinedMap.get(word) + weights[4*runCount + i]);
				} else {
					combinedSet.add(word);
					combinedMap.put(word, weights[4*runCount + i]);
				}
			}
		}
		
		Map<String, Double> finalMap = updateMap(combinedSet, combinedMap);
		List<String> resultList = sortWords(combinedSet, finalMap);
		return resultList;
	}

	// Suggest the most frequently occurring word in combined
	@Override
	public String makeGuess(String s) {
		Double[] weights = {1.0,1.0,1.5,1.5,0.5,0.5,0.5,0.75,0.75};
		List<String> sortedWords = combineAndSort(s, weights);
		if (sortedWords.size() > 0) {
			return sortedWords.get(0);
		} else {
			return "N/A";
		}
	}
}