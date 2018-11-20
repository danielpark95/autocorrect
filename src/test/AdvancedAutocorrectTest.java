package test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import main.AdvancedAutocorrect;
import main.Data;

public class AdvancedAutocorrectTest {
	@Test 
	public void testGetMinMax() {
		Data data = Data.getInstance();
		AdvancedAutocorrect ac = new AdvancedAutocorrect(data);
		
		Set<String> test1 = new HashSet<String>(Arrays.asList("the","of","and"));
		double[] actual1 = ac.getMinMax(test1);
		double min1 = actual1[0];
		double max1 = actual1[1];
		assertTrue(min1 > Double.MIN_VALUE);
		assertTrue(min1 < Double.MAX_VALUE);
		assertTrue(min1 < max1);
		assertTrue(max1 > Double.MIN_VALUE);
		assertTrue(max1 < Double.MAX_VALUE);
	}
	
}