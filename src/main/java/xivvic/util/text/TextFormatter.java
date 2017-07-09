/* FILE:      TextFormatter.java
 * CREATED:   Nov 8, 2003
 * CREATOR:   reid
 *
 * Copyright (c) 2003 Emroo.com
 *
 */
package xivvic.util.text;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TextFormatter provides methods that are useful for producing
 * special purpose text transformations that can't be found in a
 * well-supported open source library.
 */

public class TextFormatter
{
	private TextFormatter() {}

	/** boundedSubstrings breaks an input string, the text parameter, into an array
	 * of substrings. Provided that none of the individual words are longer
	 * than the specified length, each line returned will have a length less than
	 * or equal to the length argument.
	 *
	 * When the length of the desired output strings is less than the word length,
	 * then longer words will form an output string longer than the length, unless
	 * the truncate flag is set to true in which case they will be truncated to the
	 * desired length.
	 *
	 * Any newline characters in the input text will be replaced
	 * with a single space.
	 *
	 * Null text will result in a zero-length array being returned.
	 *
	 * If length < 1, this function throws an IllegalArgumentException.
	 *
	 * @throws IllegalArgumentException when length < 1
	 *
	 */
	public static String[] boundedSubstrings(String text, int length, boolean truncate)
	{
		if (text == null)
		{
			return new String[0];
		}

		if (length < 1)
		{
			throw new IllegalArgumentException("length must be a positive integer.");
		}

		if (length == 1)
		{
			return text.split("");
		}

		text = text.replaceAll("\r?\n", " ");
		if (text.length() <= length)
		{
			String[] array = new String[1];
			array[0] = text;
			return array;
		}

		List<String> list = new ArrayList<>();
		BreakIterator  it = BreakIterator.getLineInstance();
		it.setText(text);

		int start = it.first();
		int end   = it.next();
		StringBuilder sb = new StringBuilder();

		while (end != BreakIterator.DONE)
		{
			String word = text.substring(start, end);
			if (sb.length() + word.length() > length)
			{
				if (sb.length() == 0)
				{
					String toAdd = truncate ? word.substring(0, length) : word;
					list.add(toAdd);
				}
				else
				{
					list.add(sb.toString());
					if (word.length() >= length)
					{
						String toAdd = truncate ? word.substring(0, length) : word;
						list.add(toAdd);
					}
					else
					{
						sb = new StringBuilder(word);
					}
				}
			}
			else
			{
				sb.append(word);
			}
			start = end;
			end = it.next();
		}

		return list.toArray(new String[0]);
	}

	/** This method produces a substring of the given string that is suitable for putting
	 * into a fixed length display.  If the string is shorter than the desired length, it is
	 * returned in its entirety.  If it is too long, for the indicated length,
	 * this method returns a substring with three elipsis characters at the end.
	 *
	 * @param String to receive the formatting.
	 * @param length of the returned string
	 * @param the character used to add elispses to long strings.
	 *
	 * @return the formatted string with the indicated width.
	 *
	 * @throws IllegalArgumentException if the length parameter is negative.
	 */
	public static String boundedElipsisSubstring(String string, int length, char elipsisChar)
	{
		if (string == null)
		{
			return "";
		}

		if (string.length() <= length)
		{
			return string;
		}

		if (length  < 0)
		{
			throw new IllegalArgumentException("length < 0 does not make sense for this function.");
		}

		StringBuffer sb = new StringBuffer();
		if (length < 4)
		{
			for (int i = 0; i < length; i++)
			{
				sb.append(elipsisChar);
			}

			return sb.toString();
		}

		sb.append(string.substring(0, length - 3));
		sb.append(elipsisChar);
		sb.append(elipsisChar);
		sb.append(elipsisChar);

		return sb.toString();
	}


	/**
	 * join() combines an array of strings into a single string, with an
	 * optional separator between each.
	 *
	 * @param array of strings to be joined together into a single string.
	 * @param separator is placed between each pair of strings.
	 * @return
	 */
	public static String join(String[] strings, String separator)
	{
		if (strings == null || strings.length == 0)
		{
			return "";
		}

		StringJoiner sj = new StringJoiner(separator == null ? "" : separator);

		for (String s: strings)
		{
			sj.add(s == null ? "" : s);
		}

		return sj.toString();
	}


	/**
	 * coalesceWhitespace() converts all contiguous ranges of whitespace characters
	 * into a single space, this includes converting tabs and newlines to spaces.
	 * If the input is null or an empty string, the result is an empty string.
	 *
	 * Note, this function compiles a regex for each call, so for performance intensive
	 * needs, use a different approach.
	 *
	 * @param text to be modified
	 * @return string that has consecutive whitespace characters collapsed into single spaces.
	 */
	public static String coalesceWhitespace(String text)
	{
		if (text == null || text.length() == 0)
		{
			return "";
		}

		Pattern pattern = Pattern.compile("\\s+");
		Matcher matcher = pattern.matcher(text);
		return matcher.replaceAll(" ");
	}


	/**
	 * Create a string that is capped on the edges by an "edge" character
	 * and filled with "interior" character.
	 *
	 * When the interiorLength parameter is not a positive number, the resulting
	 * string will consist of two "edge" characters.
	 *
	 * <code>
	 * String s = edgedLine('*', '-', 10);
	 * String t =  "*----------*";
	 * assert (s.equals(t));
	 * </code>
	 *
	 * @param edge the character to place on the edges of the result string
	 * @param interior the character to place in the interior of the result string
	 * @param interiorLength how many characters should be inserted into the middle
	 * @return a string as described
	 */
	public static String edgedLine(char edge, char interior, int interiorLength)
	{
		int count = interiorLength > 0 ? interiorLength : 0;

		StringBuffer sb = new StringBuffer(count + 2);
		sb.append(edge);
		if (count > 0)
		{
			for (int i = 0; i < count; i++)
			{
				sb.append(interior);
			}
		}

		sb.append(edge);
		return sb.toString();
	}

}
