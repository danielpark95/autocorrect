package main;

// Given Strings s1 and s2, find the Levenshtein (edit) Distance
// (e.g. "testing" and "testing" have LD of 0)
// (e.g. "teestng" and "testing" have LD of 2)
public class Levenshtein {

	// Find the Levenshtein Distance between two strings using dynamic programming
	public int computeLD(String s1, String s2) {
		int m = s1.length();
		int n = s2.length();
		
		// If either string is empty, return the length of the other
		if (m == 0) return n;
		if (n == 0) return m;
		
		// dp[i][j] will contain the LD for s1.substring(0,i) and s2.substring(0,j)
		int[][] dp = new int[m+1][n+1];
		
		// Initialize the table for empty s1 and s2
		for (int i = 1; i <= m; i++) dp[i][0] = i;
		for (int j = 1; j <= n; j++) dp[0][j] = j;
		
		// Fill table in bottom-up manner
		for (int i = 1; i <= m; i++) {
			for (int j = 1; j <= n; j++) {
				int sameLetter = s1.charAt(i-1) == s2.charAt(j-1) ? 0 : 1;
				dp[i][j] = Math.min(dp[i-1][j] + 1, Math.min(dp[i][j-1] + 1, dp[i-1][j-1] + 1 * sameLetter));
			}
		}
		
		return dp[m][n]; 
	}
	
	public static void main (String[] args) {
		String s1 = "sitting";
		String s2 = "kitten";
		
		Levenshtein l = new Levenshtein();
		
		int ld = l.computeLD(s1, s2);
		System.out.println("Levenshtein distance for \"" + s1 + "\", \"" + s2 + "\" is: " + ld);
	}
}
