package xivvic.util.arg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;

public class ArgUtilStructuredParamTest
{
	@Test
	public void testNoDelimiter()
	{
		String                s = "Willard was an interesting individual.";
		Map<String, String> map = ArgUtil.structuredArgument2Map(s);
		String                v = map.get(s);

		assertNotNull(map);
		assertNull(v);
		assertEquals(0, map.size());
	}


	@Test
	public void testSinglePair()
	{
		String                s = "a:b";
		Map<String, String> map = ArgUtil.structuredArgument2Map(s);
		String                v = map.get("a");

		assertNotNull(map);
		assertNotNull(v);
		assertEquals(1, map.size());
		assertTrue(v.equals("b"));
	}

	@Test
	public void testSingleBracedPairNoSpaces()
	{
		String                s = "a:{b}";
		Map<String, String> map = ArgUtil.structuredArgument2Map(s);

		assertNotNull(map);
		assertEquals(1, map.size());

		String b = map.get("a");

		assertNotNull(b);

		assertTrue(b.equals("b"));
	}

	@Test
	public void testSingleBracedPairWithSpaces()
	{
		String                s = "a:{b c d}";
		Map<String, String> map = ArgUtil.structuredArgument2Map(s);

		assertNotNull(map);
		assertEquals(1, map.size());
		String b = map.get("a");

		assertNotNull(b);

		assertTrue(b.equals("b c d"));
	}

	@Test
	public void testMultipleBracedPairs()
	{
		String                s = "a:{b c d} x:y spot:{M x}";
		Map<String, String> map = ArgUtil.structuredArgument2Map(s);

		assertNotNull(map);
		assertEquals(3, map.size());

		String a = map.get("a");
		String b = map.get("x");
		String c = map.get("spot");

		assertNotNull(a);
		assertNotNull(b);
		assertNotNull(c);

		assertTrue(a.equals("b c d"));
		assertTrue(b.equals("y"));
		assertTrue(c.equals("M x"));
	}

	@Test
	public void testSimplePairs()
	{
		String                s = "fname:Winston lname:Churchill dob:32939";
		Map<String, String> map = ArgUtil.structuredArgument2Map(s);

		assertNotNull(map);
		assertEquals(3, map.size());

		String fn = map.get("fname");
		String ln = map.get("lname");
		String db = map.get("dob");

		assertNotNull(fn);
		assertNotNull(ln);
		assertNotNull(db);

		assertTrue(db.equals("32939"));
	}
}
