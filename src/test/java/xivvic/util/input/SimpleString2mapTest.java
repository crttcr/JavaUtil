package xivvic.util.input;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class SimpleString2mapTest
{
	private SimpleString2map subject;

	@Before
	public void before()
	{
		subject = new SimpleString2map("AKey", false);
	}

	@Test
	public void onProcess_withNull_whenNotRequired_thenReturnEmptyMap()
	{
		assertTrue(subject.process(null).isEmpty());
	}

	@Test
	public void onProcess_withNull_whenRequired_thenReturnNull()
	{
		subject = new SimpleString2map("AKey", true);
		assertNull(subject.process(null));
	}

	@Test
	public void onProcess_withEmptyString_whenNotRequired_thenReturnMapWithOneKey()
	{
		// Arrange
		//
		String empty = "";
		String key = "AKey";
		subject = new SimpleString2map(key, false);

		// Act
		//
		Map<String, Object> result = subject.process("");

		// Assert
		//
		assertNotNull(result);
		assertFalse(result.isEmpty());
		assertTrue(result.containsKey(key));
		assertEquals(empty, result.get(key));
	}

	@Test
	public void onProcess_withEmptyString_whenRequired_thenMapWithOneKey()
	{
		// Arrange
		//
		String empty = "";
		String key = "AKey";
		subject = new SimpleString2map(key, true);

		// Act
		//
		Map<String, Object> result = subject.process("");

		// Assert
		//
		assertNotNull(result);
		assertFalse(result.isEmpty());
		assertTrue(result.containsKey(key));
		assertEquals(empty, result.get(key));
	}

}
