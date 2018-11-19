package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

//Singleton class for loading data 
public class Data {
	private static Data instance = null;
	private Map<String, Integer> rankMap;
	private Set<String> wordSet;
	private Map<String, String> neighborMap;
	private String letters;
	
	private Data() {
		rankMap = loadRankMap();
		wordSet = loadWordSet();
		neighborMap = loadNeighborMap();
		letters = loadLetters();
	}

	// Thread-safe singleton for loading data from file
	public static synchronized Data getInstance() {
		if (instance == null) {
			instance = new Data();
		}
		return instance;
	}
	
	public Map<String, Integer> getRankMap() {
		return rankMap;
	}
	public Set<String> getWordSet() {
		return wordSet;
	}
	public Map<String, String> getNeighborMap() {
		return neighborMap;
	}
	public String getLetters() {
		return letters;
	}

	// Map of 100,000 English words to their frequency rank
	// (e.g. "the" -> 1, "of" -> 2, "and" -> 3, etc.
	private static Map<String, Integer> loadRankMap(){
		Map<String, Integer> rankedMap = new HashMap<>();
		try {
			int i = 0;
			Scanner file = new Scanner(new File("100k_ranked.txt"));
			file.useDelimiter("\n");
			while (file.hasNext()) {
				String word = file.next().toLowerCase().trim();
				if (word.startsWith("#")) {
					continue;
				} else {
					rankedMap.put(word,++i);
				}
			}
			file.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return rankedMap;
	}

	// HashSet of 170,000 English words for O(1) lookup time
	private static Set<String> loadWordSet(){
		Set<String> wordSet = new HashSet<>();
		try {
			Scanner file = new Scanner(new File("english_corpus.txt"));
			while (file.hasNext()) {
				wordSet.add(file.next().toLowerCase().trim());
			}
			file.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return wordSet;
	}
	
	// Map of each letter ("a") to its physical neighbors ("zxswq") on the keyboard
	// Used to find likely letters of for a mistyped letter
	private static Map<String, String> loadNeighborMap(){
		Map<String, String> neighborMap = new HashMap<>();
		try {
			Scanner file = new Scanner(new File("nearby_keys.txt"));
			file.useDelimiter("\n");
			int numLines = Integer.parseInt(file.next());
			for (int i = 0 ; i < numLines/2; i++) {
				String key = file.next().toLowerCase();
				String values = file.next().toLowerCase().trim();
				neighborMap.put(key, values);
			}
			file.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return neighborMap;
	}
	
	// Letters in the alphabet 
	private static String loadLetters() {
		String letters = "";
		try {
			Scanner file = new Scanner(new File("letters.txt"));
			file.useDelimiter("\n");
			letters = file.next().toLowerCase().trim();
			file.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return letters;
	}
}
