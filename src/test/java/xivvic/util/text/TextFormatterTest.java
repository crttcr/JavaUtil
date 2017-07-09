package xivvic.util.text;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class TextFormatterTest
{

	@Test
	public void onBoundedSubstrings_withNullInput_thenReturnEmptyArray()
	{
		assertEquals(0, TextFormatter.boundedSubstrings(null, 10, false).length);
	}

	@Test(expected = IllegalArgumentException.class)
	public void onBoundedSubstrings_withNegtiveLength_thenThrowException()
	{
		assertEquals(0, TextFormatter.boundedSubstrings("Mr. Magoo", -1, false).length);
	}

	@Test(expected = IllegalArgumentException.class)
	public void onBoundedSubstrings_withZeroLength_thenThrowException()
	{
		assertEquals(0, TextFormatter.boundedSubstrings("Mr. Magoo", 0, false).length);
	}

	@Test
	public void onBoundedSubstrings_withLengthOne_thenReturnCorrectArray()
	{
		// Arrange
		//
		String text = "Frank is alive.";

		// Act
		//
		String[] rt = TextFormatter.boundedSubstrings(text, 1, true);
		String[] rf = TextFormatter.boundedSubstrings(text, 1, false);

		// Assert
		//
		assertNotNull(rt);
		assertNotNull(rf);
		assertEquals(text.length(), rt.length);
		assertEquals(text.length(), rf.length);
		for (int i = 0; i < text.length(); i++) {
			assertEquals(text.charAt(i), rt[i].charAt(0));
			assertEquals(text.charAt(i), rf[i].charAt(0));
		}
	}

	@Test
	public void onBoundedSubstrings_withOneLongWordAndTruncate_theReturnSinglePrefix()
	{
		// Arrange
		//
		int     len = 6;
		String text = "Bulletin";

		// Act
		//
		String[] result = TextFormatter.boundedSubstrings(text, len, true);

		// Assert
		//
		assertNotNull(result);
		assertEquals(1, result.length);
		assertEquals("Bullet", result[0]);
	}

	@Test
	public void onBoundedSubstrings_withOneLongWordAndNoTruncate_theReturnTheWord()
	{
		// Arrange
		//
		int     len = 6;
		String text = "Bulletin";

		// Act
		//
		String[] result = TextFormatter.boundedSubstrings(text, len, false);

		// Assert
		//
		assertNotNull(result);
		assertEquals(1, result.length);
		assertEquals(text, result[0]);
	}


	@Test
	public void onBoundedSubstrings_withLongWordAtTheEndAndTruncate_theReturnCorrectArray()
	{
		// Arrange
		//
		int     len = 6;
		String text = "Horse Bulletin";

		// Act
		//
		String[] result = TextFormatter.boundedSubstrings(text, len, true);

		// Assert
		//
		assertNotNull(result);
		assertEquals(2, result.length);
		assertEquals("Horse ", result[0]);
		assertEquals("Bullet", result[1]);
	}

	@Test
	public void onBoundedSubstrings_withLongWordAtTheEndAndNoTruncate_theReturnCorrectArray()
	{
		// Arrange
		//
		int     len = 6;
		String text = "Horse Bulletin";

		// Act
		//
		String[] result = TextFormatter.boundedSubstrings(text, len, false);

		// Assert
		//
		assertNotNull(result);
		assertEquals(2, result.length);
		assertEquals("Horse ", result[0]);
		assertEquals("Bulletin", result[1]);
	}

	@Test
	public void onBES_withNullString_thenReturnEmptyString()
	{
		assertEquals("", TextFormatter.boundedElipsisSubstring(null, 10, '.'));
	}

	@Test
	public void onBES_withZeroLength_thenReturnEmptyString()
	{
		assertEquals("", TextFormatter.boundedElipsisSubstring("French fries are good", 0, '.'));
	}

	@Test(expected = IllegalArgumentException.class)
	public void onBES_withNegativeLength_thenThrowException()
	{
		assertEquals("", TextFormatter.boundedElipsisSubstring("French fries are good", -1, '.'));
	}

	@Test
	public void onBES_withLengthOf3_thenReturnElipses()
	{
		assertEquals("...", TextFormatter.boundedElipsisSubstring("French fries are good", 3, '.'));
	}

	@Test
	public void onBES_withLengthOf4_thenReturnFirstCharacterWithElipses()
	{
		assertEquals("F~~~", TextFormatter.boundedElipsisSubstring("French fries are good", 4, '~'));
	}

	@Test
	public void onJoin_withNullArray_theReturnEmptyString()
	{
		assertEquals("", TextFormatter.join(null, ":"));
	}

	@Test
	public void onJoin_withZeroLengthArray_theReturnEmptyString()
	{
		assertEquals("", TextFormatter.join(new String[0], ":"));
	}

	@Test
	public void onJoin_withOneElement_thenReturnThatElement()
	{
		assertEquals("Pancake", TextFormatter.join(new String[] {"Pancake"}, ":"));
	}

	@Test
	public void onJoin_withTwoElements_theReturnThemJoined()
	{
		assertEquals("A:B", TextFormatter.join(new String[] {"A", "B"}, ":"));
	}

	@Test
	public void onJoin_withNullSeparator_theReturnWithEmptySeparator()
	{
		assertEquals("AB", TextFormatter.join(new String[] {"A", "B"}, null));
	}

	@Test
	public void onCoalesceWhitespece_withNull_thenReturnEmptyString()
	{
		assertEquals("", TextFormatter.coalesceWhitespace(null));
	}

	@Test
	public void onCoalesceWhitespece_withEmptyString_thenReturnEmptyString()
	{
		assertEquals("", TextFormatter.coalesceWhitespace(""));
	}

	@Test
	public void onCoalesceWhitespece_withNoWhitespace_thenReturnOriginalString()
	{
		assertEquals("CowsAreCool", TextFormatter.coalesceWhitespace("CowsAreCool"));
	}

	@Test
	public void onCoalesceWhitespece_withSingleSpaces_thenReturnOriginalString()
	{
		assertEquals("Cows Are Cool", TextFormatter.coalesceWhitespace("Cows Are Cool"));
	}

	@Test
	public void onCoalesceWhitespece_withConsecutiveSpaces_thenReturnCoalescedString()
	{
		assertEquals("Cows Are Cool", TextFormatter.coalesceWhitespace("Cows   Are    Cool"));
	}

	@Test
	public void onEdgedLine_withNegativeInteriorLength_thenReturnTwoEdgeCharacters()
	{
		assertEquals("**", TextFormatter.edgedLine('*', '-', -1));
	}

	@Test
	public void onEdgedLine_withZeroInteriorLength_thenReturnTwoEdgeCharacters()
	{
		assertEquals("**", TextFormatter.edgedLine('*', '-', -1));
	}

	@Test
	public void onEdgedLine_withPositiveInteriorLength_thenReturnTheLine()
	{
		assertEquals("#----#", TextFormatter.edgedLine('#', '-', 4));
	}

}
