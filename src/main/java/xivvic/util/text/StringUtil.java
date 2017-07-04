package xivvic.util.text;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

public class StringUtil
{
	public static final String[] EMPTY_ARRAY = {};
	public static final Predicate<String> TRUE = new Predicate<String>()
	{
		@Override
		public boolean test(String t)
		{
			return true;
		}
	};

	public static final Predicate<String> FALSE = TRUE.negate();

	public static Predicate<String> anyPredicate(String[] pool, Predicate<String> predicate)
	{
		if (pool == null)
		{
			return FALSE;
		}

		if (predicate == null)
		{
			return FALSE;
		}

		final String[] copy = pool.clone();

		Predicate<String> p = new Predicate<String>()
		{
			@Override
			public boolean test(String t)
			{
				for (String s: copy)
				{
					if (predicate.test(s))
					{
						return true;
					}
				}

				return false;
			}
		};

		return p;
	}

	/**
	 * This expression matches all the major ASCII newline conventions
	 * See:  http://en.wikipedia.org/wiki/Newline for details
	 *
	 *  \n   Decimal 10    -- 0x0A       -- LineFeed         --  Unix
	 *  \r   Decimal 13    -- 0x0D       -- Carriage Return  --  Commodore, Mac OS, Apple II, 0S-9
	 *  \r\n Decimal 13 10 -- 0x0D 0x0A  -- CRLF             --  Brain-dead Windows
	 */
	public static final String REGEX_GENERIC_LINE_END = "\\n|\\r|\\r\\n";

	/**
	 * findMatches looks for the substring "sub" in the array of strings
	 * and returns an array of those containing the substring.  Returns an
	 * empty array if none are found or if either argument is blank.
	 *
	 */
	public static String[] findMatches(String sub, String[] array)
	{
		if (sub == null || sub.length() == 0)
		{
			return new String[0];
		}

		if (array == null || array.length == 0)
		{
			return new String[0];
		}


		List<String> list = new LinkedList<>();
		for (String item : array)
		{
			if (! item.contains(sub))
			{
				continue;
			}

			list.add(item);
		}

		return list.toArray(new String[0]);
	}

	/**
	 * Process a String array separating the contents into groups based on the value
	 * of a prefix.  Given the following input
	 *
	 * String input[] = { "", "apple", "car:make", "house:rooms", "house:address", "car:color", "car:model" }
	 *
	 * List<String[]> groups = StringUtil.groupByPrefix(input, ":");
	 *
	 * will return a list of the following arrays
	 *
	 * { "", "apple" }
	 * { "car:make", "car:color", "car:model" }
	 * { "house:rooms", "house:address" }
	 *
	 * @param commands
	 * @param string
	 * @return
	 */
	public static List<String[]> groupByPrefix(String[] input, String string)
	{
		List<String[]> result = new LinkedList<>();

		if (input == null || input.length == 0)
		{
			return result;
		}

		if (string == null || string.length() == 0)
		{
			return result;
		}

		Map<String, List<String>> groups = new HashMap<>();

		for (String item : input)
		{
			String key = "";
			int  index = item.indexOf(string);
			if (index != -1)
			{
				key = item.substring(0, index);
			}

			List<String> bucket = groups.get(key);
			if (bucket == null)
			{
				bucket = new LinkedList<>();
				groups.put(key, bucket);
			}

			bucket.add(item);
		}

		Set<String>  prefixes = groups.keySet();
		Iterator<String> it = prefixes.iterator();
		while (it.hasNext())
		{
			String       prefix = it.next();
			List<String> values = groups.get(prefix);
			String[] strings = new String[values.size()];
			int i = 0;
			for (String s : values)
			{
				strings[i++] = s;
			}

			result.add(strings);
		}

		return result;
	}

	/**
	 * Converts a string to be a template target.
	 * Example:
	 *
	 * person.name -> {{person.name}}
	 *
	 * @param inside the content of the target.
	 * @return the string formatted to be a template target.
	 */
	public static String convertToTemplateTarget(String inside)
	{
		if (inside == null)
		{
			inside = "";
		}

		return "{{" + inside + "}}";
	}

	/**
	 * Convert a String into an input stream.  Uses the optional encoding.  If the encoding
	 * is null then, it uses "UTF-8" encoding.  Handles null text input by converting it to
	 * the empty string.  This should produce an InputStream that immediately returns end of
	 * buffer.
	 *
	 *
	 * @param text that is to be placed in the input stream
	 * @param encoding that will be used to convert the String's bytes
	 * @return InputStream containing the characters of the stream
	 */
	public static InputStream string2inputStream(String text, Charset charset)
	{
		if (text == null)
		{
			text = "";
		}

		if (charset == null)
		{
			charset = StandardCharsets.UTF_8;
		}

		byte[] bytes = text.getBytes(charset);
		InputStream is = new ByteArrayInputStream(bytes);

		return is;
	}

}
