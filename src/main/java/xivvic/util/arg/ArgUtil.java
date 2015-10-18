package xivvic.util.arg;

import java.util.HashMap;
import java.util.Map;

public class ArgUtil
{
	public static final String N_V_DELIMITER = ":";
	public static final String REGEX_PATTERN_WHITESPACE = "\\s+";
	
	
	/**
	 * Processes an argument as a string of Name:Value pairs separated
	 * by whitespace.  Does not respect whitespace internal to a name
	 * or to a value. Also requires no space between the end of the argument
	 * name and the delimiter and between the delimiter and the start of the
	 * value.
	 * 
	 * Handles multiple values or the same name by replacement, so there is 
	 * only the last value in the list in the returned map.
	 * 
	 * If the parameter is null, the result of this function is null.
	 * 
	 * @param param set of name:value pairs, separated by spaces
	 * 
	 * @return a map containing the names mapping to their values.
	 * 
	 */
	public static Map<String, String> object2ArgumentMap(Object param)
	{
		if (param == null)
			return null;
		
		if (! (param instanceof String))
			return null;
		
		String       s = (String) param;
		s              = s.trim();
		String[] pairs = s.split(REGEX_PATTERN_WHITESPACE);
		
		Map<String, String> result = new HashMap<>();
		for (String pair : pairs)
		{
			String[] parts = pair.split(N_V_DELIMITER, 2);
			
			if (parts == null || parts.length == 0)
				continue;
			
			String value = parts.length == 1 ? null : parts[1];
			result.put(parts[0], value);
		}
		
		return result;
	}
	
	/**
	 * Processes an argument as a string of Name:Value pairs.
	 * In this case, the Value component can have embedded white space
	 * enclosed in curly braces like this:
	 * 
	 * name_1:{This value has spaces}    name_2:ThisValueDoesNot
	 * 
	 * The curly braces will be removed from the value
	 * 
	 * IF THERE ARE NO NAME:VALUE pairs, an empty map will be returned.
	 * 
	 * 
	 * Handles multiple values or the same name by replacement, so there is 
	 * only the last value in the list in the returned map.
	 * 
	 * If the parameter is null, the result of this function is null.
	 * 
	 * @param param set of name:value or name:{Spaced Value} pairs, separated by whitespace
	 * 
	 * @return a map containing the names mapping to their values.
	 * 
	 */
	public static Map<String, String> structuredObjectArgumentMap(Object param)
	{
		if (param == null)
			return null;
		
		if (! (param instanceof String))
			return null;
		
		String       s = (String) param;
		s              = s.trim();

		Map<String, String> result = new HashMap<>();
		int             name_start = 0;
		int             next_delim = s.indexOf(N_V_DELIMITER);

		while (next_delim != -1)
		{
			int    name_end = next_delim;
			int value_start = next_delim + 1;
			char         ch = s.charAt(value_start);
			int   value_end = value_start + 1;
			
			if (ch == '{')
			{
				value_start++;
				int closing_curly = s.indexOf('}', value_start);
				if (closing_curly == -1)
					break;
				value_end  = closing_curly;
			}
			else
			{
				while (value_end < (s.length() - 1) && s.charAt(value_end) != ' ')
					value_end++;
				
				if (value_end == s.length() -1)
					value_end++;
				
				next_delim = value_end;
			}
			
			String     name = s.substring(name_start, name_end);
			String    value = s.substring(value_start, value_end);
			result.put(name, value);
			
			name_start = value_end + 1;
			while (name_start < (s.length() - 1) && s.charAt(name_start) == ' ')
				name_start++;

			next_delim = s.indexOf(N_V_DELIMITER, name_start);
		}

		return result;
	}

}
