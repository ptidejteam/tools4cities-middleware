package ca.concordia.encs.citydata;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class VarsTest {

	// Test for valid steps
	@Test
	public void test1() throws Exception {
		String targetSubstring = "p";
		String sensitive = System.getenv("CITYDATA_BLA");
		String nonSensitive = System.getenv("CITYDATA_NOT_SENSITIVE");

		assertEquals(nonSensitive, "abc123");
		assertTrue(sensitive.contains(targetSubstring), "Expected the string to contain '" + targetSubstring + "'");

	}

}
