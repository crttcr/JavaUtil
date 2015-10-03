package xivvic.util;

import java.util.Arrays;
import java.util.function.Predicate;

public class StringUtil
{
	public static final Predicate<String> TRUE = new Predicate<String>() 
	{
		@Override
		public boolean test(String t)
		{
			return true;
		}
	};

	public static final Predicate<String> FALSE = new Predicate<String>() 
	{
		@Override
		public boolean test(String t)
		{
			return false;
		}
	};

	public static Predicate<String> anyPredicate(String[] pool, Predicate<String> predicate)
	{
		if (pool == null)
			return FALSE;

		if (predicate == null)
			return FALSE;
		
		final String[] copy = Arrays.copyOf(pool, pool.length);
		
		Predicate<String> result = new Predicate<String>()
		{
			@Override
			public boolean test(String t)
			{
				for (String s: copy)
				{
					if (predicate.test(s))
						return true;
				}

				return false;
			}
		};

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
			inside = "";
		
		return "{{" + inside + "}}";
	}
}
