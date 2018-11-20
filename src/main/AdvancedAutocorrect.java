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
	private Map<String, Integer> rankMap;
	
	public AdvancedAutocorrect(Data data) {
		super(data);
		rankMap = data.getRankMap();
	}
	boolean verbose = false;
	
	public double[] getMinMax(Set<String> combinedSet, Map<String, Double> combinedMap) {
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
	
	public Map<String,Double> normalizeRankMap(Set<String> combinedSet, Map<String, Double> combinedMap) {
		double[] minMax = getMinMax(combinedSet,combinedMap);
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
		System.out.println("Advanced Autocorrect");
		//System.out.println("\tinput = \""  + s + "\"");
		Set<String> combinedSet = new HashSet<String>();
		Map<String, Double> combinedMap = new HashMap<String, Double>();
		
		List<List<String>> combinedList = new ArrayList<>();
		combinedList.add(removeOneLetter(s));
		combinedList.add(addOneLetter(s));
		combinedList.add(replaceOneLetter(s));
		combinedList.add(switchTwoLetters(s));
		
		for (int i = 0 ; i < 4; i++) {
			for (String word : combinedList.get(i)) {
				if (combinedSet.contains(word)) {
					combinedMap.put(word, combinedMap.get(word) + weights[i]);
				} else {
					combinedSet.add(word);
					combinedMap.put(word, weights[i]);
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
		Double[] weights = {1.0,1.0,1.5,1.5};
		List<String> sortedWords = combineAndSort(s, weights);
		if (sortedWords.size() > 0) {
			return sortedWords.get(0);
		} else {
			return "N/A";
		}
	}
}