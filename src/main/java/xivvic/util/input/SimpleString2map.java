package xivvic.util.input;

import java.util.Map;
import java.util.Objects;

/**
 * Extensible class for processing a String input
 *
 * @author Reid
 *
 */
public class SimpleString2map
extends String2mapBase
{
	private final String key;
	private final boolean isRequired;
	private final String defaultValue;

	public SimpleString2map(String key, boolean required)
	{
		this.key = Objects.requireNonNull(key);
		this.isRequired = required;
		this.defaultValue = null;
	}

	public SimpleString2map(String key, String defaultValue)
	{
		super();
		this.key = Objects.requireNonNull(key);
		this.defaultValue = defaultValue;
		this.isRequired = false;
	}

	@Override
	public Map<String, Object> process(String param)
	{
		if (param == null)
		{
			if (defaultValue != null)
			{
				result.put(key, defaultValue);
				return result;
			}

			if (! isRequired)
			{
				return result;
			}
			else
			{
				return null;
			}
		}

		String s = param.trim();
		result.put(key, s);

		return result;
	}

}
