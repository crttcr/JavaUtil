package xivvic.util.input;

import java.util.Map;

/**
 * Extendable class for processing an integer input
 *
 * @author Reid
 *
 */
public class SingleIntegerString2map
extends String2mapBase
{
	public static final String KEY = "aipsi.key";

	private boolean isRequired = false;
	private Integer defaultValue;


	public SingleIntegerString2map(boolean required)
	{
		super();
		this.isRequired = required;
	}

	public SingleIntegerString2map(int defaultValue)
	{
		super();
		this.defaultValue = defaultValue;
	}

	@Override
	public Map<String, Object> process(String params)
	{
		if (params == null)
		{
			if (defaultValue != null)
			{
				result.put(KEY, defaultValue);
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

		String s = params.trim();

		try
		{
			int i = Integer.parseInt(s);
			result.put(KEY, i);
		}
		catch (NumberFormatException e)
		{
			if (defaultValue != null)
			{
				result.put(KEY, defaultValue);
			}
			else
			{
				if (isRequired)
				{
					return null;
				}
			}
		}

		return result;
	}

}
