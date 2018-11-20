package test;

import main.Data;
import main.BasicAutocorrect;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import java.util.List;

public class BasicAutocorrectTest {
	@Test
	public void testAddOneLetter() { 
		Data data = Data.getInstance();
		BasicAutocorrect ac = new BasicAutocorrect(data);
		
		List<String> res1 = ac.addOneLetter("");
		String[] actual1 = new String[res1.size()];
		actual1 = res1.toArray(actual1);
		String[] expected1 = {
			"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q",
			"r","s","t","u","v","w","x","y","z"
		};
		assertArrayEquals(actual1, expected1);
		
		List<String> res2 = ac.addOneLetter("t");
		String[] actual2 = new String[res2.size()];
		actual2 = res2.toArray(actual2);
		String[] expected2 = {
			"at","ta","bt","tb","ct","tc","dt","td","et","te","ft","tf","gt","tg",
			"ht","th","it","ti","jt","tj","kt","tk","lt","tl","mt","tm","nt","tn",
			"ot","to","pt","tp","qt","tq","rt","tr","st","ts","tt","tt","ut","tu",
			"vt","tv","wt","tw","xt","tx","yt","ty","zt","tz"
		};
		assertArrayEquals(actual2, expected2);
		
		List<String> res3 = ac.addOneLetter("to");
		String[] actual3 = new String[res3.size()];
		actual3 = res3.toArray(actual3);
		String[] expected3 = {
			"ato","tao","toa","bto","tbo","tob","cto","tco","toc","dto","tdo","tod",
			"eto","teo","toe","fto","tfo","tof","gto","tgo","tog","hto","tho","toh",
			"ito","tio","toi","jto","tjo","toj","kto","tko","tok","lto","tlo","tol",
			"mto","tmo","tom","nto","tno","ton","oto","too","too","pto","tpo","top",
			"qto","tqo","toq","rto","tro","tor","sto","tso","tos","tto","tto","tot",
			"uto","tuo","tou","vto","tvo","tov","wto","two","tow","xto","txo","tox",
			"yto","tyo","toy","zto","tzo","toz"
		};
		assertArrayEquals(actual3, expected3);
	}

	@Test
	public void testRemoveOneLetter() {
		Data data = Data.getInstance();
		BasicAutocorrect ac = new BasicAutocorrect(data);
		List<String> res1 = ac.removeOneLetter("p");
		String[] actual1 = new String[res1.size()];
		actual1 = res1.toArray(actual1);
		String[] expected1 = {""};
		assertArrayEquals(actual1, expected1);
		
		List<String> res2 = ac.removeOneLetter("aq");
		String[] actual2 = new String[res2.size()];
		actual2 = res2.toArray(actual2);
		String[] expected2 = {"q","a"};
		assertArrayEquals(actual2, expected2);
		
		List<String> res3 = ac.removeOneLetter("hello");
		String[] actual3 = new String[res3.size()];
		actual3 = res3.toArray(actual3);
		String[] expected3 = {"ello","hllo","helo","helo","hell"};
		assertArrayEquals(actual3, expected3);
	}

	@Test
	public void testReplaceOneLetter() {
		Data data = Data.getInstance();
		BasicAutocorrect ac = new BasicAutocorrect(data);
		
		List<String> res1 = ac.replaceOneLetter("a");
		String[] actual1 = new String[res1.size()];
		actual1 = res1.toArray(actual1);
		String[] expected1 = {"z","x","s","w","q"};
		assertArrayEquals(actual1, expected1);
		
		List<String> res2 = ac.replaceOneLetter("mt");
		String[] actual2 = new String[res2.size()];
		actual2 = res2.toArray(actual2);
		String[] expected2 = {"nt","kt","jt","mr","mf","mg","mh","my"};
		assertArrayEquals(actual2, expected2);
	}

	@Test
	public void testSwitchTwoLetters() {
		Data data = Data.getInstance();
		BasicAutocorrect ac = new BasicAutocorrect(data);
		
		List<String> res1 = ac.switchTwoLetters("a");
		String[] actual1 = new String[res1.size()];
		actual1 = res1.toArray(actual1);
		String[] expected1 = {};
		assertArrayEquals(actual1, expected1);
		
		List<String> res2 = ac.switchTwoLetters("mt");
		String[] actual2 = new String[res2.size()];
		actual2 = res2.toArray(actual2);
		String[] expected2 = {"tm"};
		assertArrayEquals(actual2, expected2);
		
		
		List<String> res3 = ac.switchTwoLetters("tpo");
		String[] actual3 = new String[res3.size()];
		actual3 = res3.toArray(actual3);
		String[] expected3 = {"pto","top"};
		assertArrayEquals(actual3, expected3);
		
		List<String> res4 = ac.switchTwoLetters("ehllo");
		String[] actual4 = new String[res4.size()];
		actual4 = res4.toArray(actual4);
		String[] expected4 = {"hello","elhlo","ehllo","ehlol"};
		assertArrayEquals(actual4, expected4);	
	}
}