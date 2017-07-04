package xivvic.util.text;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import org.junit.Test;

public class StringUtilTest
{

	@Test
	public void onAnyTrue_withNullInput_thenReturnTrue() throws Exception
	{
		// Arrange
		//
		String[] pool = {"The", "Big", "Train", "Could", "Not", "Stop"};
		Predicate<String> any = StringUtil.anyPredicate(pool, StringUtil.TRUE);

		// Act
		//
		boolean b = any.test(null);

		// Assert
		//
		assertTrue(b);
	}

	@Test
	public void onAnyFalse_withNullInput_thenReturnFalse() throws Exception
	{
		// Arrange
		//
		String[] pool = {"The", "Big", "Train", "Could", "Not", "Stop"};
		Predicate<String> any = StringUtil.anyPredicate(pool, StringUtil.FALSE);

		// Act
		//
		boolean b = any.test(null);

		// Assert
		//
		assertFalse(b);
	}

	@Test
	public void onFindMatches_withNullArray_thenReturnEmptyArray() throws Exception
	{
		assertEquals(0, StringUtil.findMatches("abc", null).length);
	}

	@Test
	public void onFindMatches_withEmptyArray_thenReturnEmptyArray() throws Exception
	{
		String[] array = {};
		assertEquals(0, StringUtil.findMatches("abc", array).length);
	}

	@Test
	public void onFindMatches_withNullSubstring_thenReturnEmptyArray() throws Exception
	{
		String[] array = {"Pony", "Puppy", "Plankton"};
		assertEquals(0, StringUtil.findMatches(null, array).length);
	}

	@Test
	public void onFindMatches_withEmptySubstring_thenReturnEmptyArray() throws Exception
	{
		String[] array = {"Pony", "Puppy", "Plankton"};
		assertEquals(0, StringUtil.findMatches("", array).length);
	}

	@Test
	public void onFindMatches_withSingleLetterSubstring_thenReturnMatches() throws Exception
	{
		// Arrange
		//
		String[] array = {"The", "Big", "Train", "Could", "Not", "Stop"};
		String     sub = "o";

		// Act
		//
		String[] result = StringUtil.findMatches(sub, array);

		// Assert
		//
		assertNotNull(result);
		assertEquals(3, result.length);

		for (int i = 0; i < result.length; i++)
		{
			assertEquals(array[i + 3], result[i]);
		}
	}

	@Test
	public void onFindMatches_withLowercaseSubstring_thenReturnMatches() throws Exception
	{
		// Arrange
		//
		String[] array = {"The", "THE", "the", "them", "Theory", "Bother"};
		String     sub = "the";

		// Act
		//
		String[] result = StringUtil.findMatches(sub, array);

		// Assert
		//
		assertNotNull(result);
		assertEquals(3, result.length);

		assertEquals(array[2], result[0]);
		assertEquals(array[3], result[1]);
		assertEquals(array[5], result[2]);
	}

	@Test
	public void onGroupByPrefix_withNoneWithPrefix_thenReturnListWithOneArray()
	{
		// Arrange
		//
		String input[] = { "", "apple"};

		// Act
		//
		List<String[]> groups = StringUtil.groupByPrefix(input, ":");

		// Assert
		//
		assertNotNull(groups);
		assertEquals(1, groups.size());
		assertEquals("", groups.get(0)[0]);
		assertEquals("apple", groups.get(0)[1]);
	}

	@Test
	public void onGroupByPrefix_withAllWithPrefix_thenReturnListWithOneArray()
	{
		// Arrange
		//
		String input[] = { "cattle", "cataract", "catenary"};
		String     sub = "cat";

		// Act
		//
		List<String[]> groups = StringUtil.groupByPrefix(input, sub);

		// Assert
		//
		assertNotNull(groups);
		assertEquals(1, groups.size());
		assertEquals(3, groups.get(0).length);
		assertEquals("cattle",   groups.get(0)[0]);
		assertEquals("cataract", groups.get(0)[1]);
		assertEquals("catenary", groups.get(0)[2]);
	}

	@Test
	public void onGroupByPrefix_withSingleCharacterPrefix_thenReturnCorrectResult()
	{
		// Arrange
		//
		String    input[] = { "", "apple", "car:make", "house:rooms", "house:address", "car:color", "car:model"};
		String expect_a[] = {"", "apple"};
		String expect_b[] = {"car:make", "car:color", "car:model"};
		String expect_c[] = {"house:rooms", "house:address"};

		// Act
		//
		List<String[]> groups = StringUtil.groupByPrefix(input, ":");

		// Assert
		//
		assertNotNull(groups);
		assertEquals(3, groups.size());
		assertListContainsArray(groups, 	expect_a);
		assertListContainsArray(groups, 	expect_b);
		assertListContainsArray(groups, 	expect_c);
	}

	@Test
	public void onConvertToTemplateTarget_withNull_thenReturnEmptyTemplate()
	{
		assertEquals("{{}}", StringUtil.convertToTemplateTarget(null));
	}

	@Test
	public void onConvertToTemplateTarget_withEmptyString_thenReturnEmptyTemplate()
	{
		assertEquals("{{}}", StringUtil.convertToTemplateTarget(""));
	}

	@Test
	public void onConvertToTemplateTarget_withEmbeddedWhitespace_thenReturnEmptyTemplate()
	{
		String input = "a\tb\nc";
		String expected = "{{" + input + "}}";
		assertEquals(expected, StringUtil.convertToTemplateTarget(input));
	}

	@Test
	public void onConvertToTemplateTarget_withSimpleString_thenReturnEmptyTemplate()
	{
		String input = "user.name";
		String expected = "{{" + input + "}}";
		assertEquals(expected, StringUtil.convertToTemplateTarget(input));
	}

	@Test
	public void OnString2inputStream_withNullString_thenReturnValidEmptyInputStream() throws Exception
	{
		// Arrange
		//
		String input = null;
		Charset   cs = StandardCharsets.UTF_8;

		// Act
		//
		InputStream is = StringUtil.string2inputStream(input, cs);
		int read = is.read();

		// Assert
		//
		assertNotNull(is);
		assertEquals(-1, read);
	}

	@Test
	public void OnString2inputStream_withNullEncodeing_thenReturnValidInputStream() throws Exception
	{
		// Arrange
		//
		String input = "This is a string";
		Charset   cs = null;
		int    expect = 'T';

		// Act
		//
		InputStream is = StringUtil.string2inputStream(input, cs);
		int read = is.read();

		// Assert
		//
		assertNotNull(is);
		assertEquals(expect, read);
	}


	/////////////////////////////////////
	// Helper Methods                  //
	/////////////////////////////////////

	private void assertListContainsArray(List<String[]> groups, String[] expected)
	{
		for (String[] array: groups)
		{
			if (Arrays.equals(array, expected))
			{
				return;
			}
		}

		String msg = String.format("Unable to find %s, in list of strings %s", Arrays.toString(expected), groups);
		fail(msg);
	}


}
