package xivvic.util.identity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import xivvic.util.identity.RandomString;

public class RandomStringTest
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
	public void testBasicFunction()
	{
		RandomString rs = new RandomString(16);
		String        s = rs.nextString();
		
		assertNotNull(s);
		assertEquals(16, s.length());
		System.out.println(s);
	}

}
