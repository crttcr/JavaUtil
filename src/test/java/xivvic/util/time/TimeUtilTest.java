package xivvic.util.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.function.Function;

import org.junit.Test;

public class TimeUtilTest
{
	@Test
	public void testTextToNanosFunctionHM()
	{
		// Arrange
		//
		String s ="00:01";
		Function<String, Object> converter = TimeUtil.textToNanosFunction();

		// Act
		//
		Object out = converter.apply(s);

		// Assert
		//
		assertNotNull(out);
		assertTrue(out instanceof Long);
		assertEquals(60000000000L, out);
	}

	@Test
	public void testTextToNanosFunctionHMS()
	{
		// Arrange
		//
		String s ="00:01:03";
		Function<String, Object> converter = TimeUtil.textToNanosFunction();

		// Act
		//
		Object out = converter.apply(s);

		// Assert
		//
		assertNotNull(out);
		assertTrue(out instanceof Long);
		assertEquals(63000000000L, out);
	}

	@Test
	public void testText2EpochFunctionNull()
	{
		// Arrange
		//
		String s = null;
		Function<String, Object> converter = TimeUtil.text2EpochFunction();

		// Act
		//
		Object out = converter.apply(s);

		// Assert
		//
		assertNotNull(out);
		assertTrue(out instanceof String);
		assertEquals("null", out);
	}

	@Test
	public void testText2EpochFunction()
	{
		// Arrange
		//
		String s ="2015-12-25";
		Function<String, Object> converter = TimeUtil.text2EpochFunction();

		// Act
		//
		Object out = converter.apply(s);

		// Assert
		//
		assertNotNull(out);
		assertTrue(out instanceof Long);
		assertEquals(16794L, out);
	}

	@Test
	public void testText2EpochFunctionSingleDigits()
	{
		// Arrange
		//
		String s ="2016-1-2";
		Function<String, Object> converter = TimeUtil.text2EpochFunction();

		// Act
		//
		Object out = converter.apply(s);

		// Assert
		//
		assertNotNull(out);
		assertTrue(out instanceof String);
		String t = (String) out;
		assertTrue(t.startsWith("Parse error"));
	}

}
