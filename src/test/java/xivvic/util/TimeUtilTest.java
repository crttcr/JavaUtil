package xivvic.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.function.Function;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import xivvic.util.time.TimeUtil;

public class TimeUtilTest
{

	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{
	}

	@Before
	public void setUp() throws Exception
	{
	}

	@After
	public void tearDown() throws Exception
	{
	}

	@Test
	public void testTextToNanosFunctionHM()
	{
		String s ="00:01";
		Function<String, Object> converter = TimeUtil.textToNanosFunction();
		Object out = converter.apply(s);

		System.out.println(out);
		assertNotNull(out);
		assertTrue(out instanceof Long);
		assertEquals(60000000000L, out);
	}

	@Test
	public void testTextToNanosFunctionHMS()
	{
		String s ="00:01:03";
		Function<String, Object> converter = TimeUtil.textToNanosFunction();
		Object out = converter.apply(s);

		System.out.println(out);
		assertNotNull(out);
		assertTrue(out instanceof Long);
		assertEquals(63000000000L, out);
	}

	@Test
	public void testText2EpochFunctionNull()
	{
		String s = null;
		Function<String, Object> converter = TimeUtil.text2EpochFunction();
		Object out = converter.apply(s);
		
		assertNotNull(out);
		assertTrue(out instanceof String);
		assertEquals("null", out);
	}

	@Test
	public void testText2EpochFunction()
	{
		String s ="2015-12-25";
		Function<String, Object> converter = TimeUtil.text2EpochFunction();
		Object out = converter.apply(s);

		System.out.println(out);
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
