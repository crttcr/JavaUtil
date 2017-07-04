package xivvic.util.arg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;

public class ArgUtilTest
{
	@Test
	public void onObject2Map_withNull_thenReturnEmptyMap()
	{
		Map<String, String> result = ArgUtil.object2ArgumentMap(null);
		assertNotNull(result);
		assertTrue(result.isEmpty());
	}

	@Test
	public void onObject2Map_withNonString_thenReturnEmptyMap()
	{
		Map<String, String> result = ArgUtil.object2ArgumentMap(new Object());
		assertNotNull(result);
		assertTrue(result.isEmpty());
	}

	@Test
	public void onObject2Map_withEmptyString_thenReturnEmptyMap()
	{
		Map<String, String> result = ArgUtil.object2ArgumentMap("");
		assertNotNull(result);
		assertTrue(result.isEmpty());
	}

	@Test
	public void onObject2Map_withNoDelimiter_thenReturnEmptyMap()
	{
		// Arrange
		//
		String                s = "Willard";

		// Act
		//
		Map<String, String> map = ArgUtil.object2ArgumentMap(s);

		// Assert
		//
		assertNotNull(map);
		assertTrue(map.isEmpty());
	}

	@Test
	public void onObject2Map_withNoValue_thenReturnMapOfKeyToEmptyString()
	{
		// Arrange
		//
		String                s = "Willard:";
		String                t = s.substring(0, s.length() - 1);
		System.out.println(t);

		// Act
		//
		Map<String, String> map = ArgUtil.object2ArgumentMap(s);
		String                v = map.get(t);

		// Assert
		//
		assertNotNull(map);
		assertEquals(1, map.size());
		assertEquals("", v);
	}

	@Test
	public void onObject2Map_withSinglePair_thenReturnResultingMap()
	{
		// Arrange
		//
		String pair = "fruit:apple";

		// Act
		//
		Map<String, String> result = ArgUtil.object2ArgumentMap(pair);

		// Assert
		//
		assertNotNull(result);
		assertEquals("apple", result.get("fruit"));
	}

	@Test
	public void onObject2Map_withThreePair_thenReturnResultingMap()
	{
		// Arrange
		//
		String pair = "fruit:apple veg:onion\nsweet:candy";

		// Act
		//
		Map<String, String> result = ArgUtil.object2ArgumentMap(pair);

		// Assert
		//
		assertNotNull(result);
		assertEquals("apple", result.get("fruit"));
		assertEquals("onion", result.get("veg"));
		assertEquals("candy", result.get("sweet"));
	}

	@Test
	public void onStructuredArg2Map_withNull_thenReturnEmptyMap()
	{
		Map<String, String> result = ArgUtil.structuredArgument2Map(null);
		assertNotNull(result);
		assertTrue(result.isEmpty());
	}

	@Test
	public void onStructuredArg2Map_withNonString_thenReturnEmptyMap()
	{
		Map<String, String> result = ArgUtil.structuredArgument2Map(new Object());
		assertNotNull(result);
		assertTrue(result.isEmpty());
	}

	@Test
	public void onStructuredArg2Map_withEmptyString_thenReturnEmptyMap()
	{
		Map<String, String> result = ArgUtil.structuredArgument2Map("");
		assertNotNull(result);
		assertTrue(result.isEmpty());
	}

	@Test
	public void onStructuredArg2Map_withMultipleBracedPairsAndExtraSpace_thenReturnValidMap()
	{
		// Arrange
		//
		String                s = "a:{b  c  d}   x:y   spot:{M x}  ";

		// Act
		//
		Map<String, String> map = ArgUtil.structuredArgument2Map(s);
		String a = map.get("a");
		String b = map.get("x");
		String c = map.get("spot");

		// Assert
		//
		assertNotNull(map);
		assertEquals(3, map.size());

		assertNotNull(a);
		assertNotNull(b);
		assertNotNull(c);

		assertTrue(a.equals("b  c  d"));
		assertTrue(b.equals("y"));
		assertTrue(c.equals("M x"));
	}

	@Test
	public void onStructuredArg2Map_withSinglePair_thenReturnResultingMap()
	{
		// Arrange
		//
		String pair = "fruit:apple";

		// Act
		//
		Map<String, String> result = ArgUtil.structuredArgument2Map(pair);

		// Assert
		//
		assertNotNull(result);
		assertEquals("apple", result.get("fruit"));
	}

	@Test
	public void onStructuredArg2Map_withThreePair_thenReturnResultingMap()
	{
		// Arrange
		//
		String pair = "fruit:{apple pie} veg:{onion tart}  sweet:candy";

		// Act
		//
		Map<String, String> result = ArgUtil.structuredArgument2Map(pair);

		// Assert
		//
		assertNotNull(result);
		assertEquals("apple pie", result.get("fruit"));
		assertEquals("onion tart", result.get("veg"));
		assertEquals("candy", result.get("sweet"));
	}

}
