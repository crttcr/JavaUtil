package xivvic.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import xivvic.util.arg.ArgUtil;

public class ArgUtilTest
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
	public void testNullParam()
	{
		Object o = ArgUtil.object2ArgumentMap(null);
		
		assertNull(o);
	}

	@Test
	public void testNonStringParam()
	{
		Integer i = new Integer(4);
		Object  o = ArgUtil.object2ArgumentMap(i);
		
		assertNull(o);
	}

	@Test
	public void testNoDelimiter()
	{
		String                s = "Willard";
		Map<String, String> map = ArgUtil.object2ArgumentMap(s);
		String                v = map.get(s);
		
		assertNotNull(map);
		assertNull(v);
		assertEquals(1, map.size());
	}


	@Test
	public void testSinglePair()
	{
		String                s = "a:b";
		Map<String, String> map = ArgUtil.object2ArgumentMap(s);
		String                v = map.get("a");
		
		assertNotNull(map);
		assertNotNull(v);
		assertEquals(1, map.size());
		assertTrue(v.equals("b"));
	}
	
	@Test
	public void testSingleMultiplePairs()
	{
		String                s = "fname:Reid lname:Churchill dob:32939";
		Map<String, String> map = ArgUtil.object2ArgumentMap(s);
		
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
