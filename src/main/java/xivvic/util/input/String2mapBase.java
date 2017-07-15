package xivvic.util.input;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.extern.slf4j.Slf4j;

/**
 * Base class for processing action inputs.
 *
 * @author Reid
 *
 */
@Slf4j
public abstract class String2mapBase
implements String2map
{
	protected final static Logger LOG = LoggerFactory.getLogger(String2mapBase.class.getName());

	/**
	 * Keys that must be found in the input or the processing fails
	 */
	protected final Set<String> required;

	/**
	 * Functions that convert property values for a specific key to a different object or value
	 */
	protected final Map<String, Function<String, ?>> converters;

	/**
	 * Synthetic keys to generate for a given property
	 */
	protected final Map<String, Supplier<String>> synthetics;

	/**
	 * The resulting map of values to return from processing input.
	 */
	protected final Map<String, Object> result;

	public String2mapBase()
	{
		this(null, null, null, null);
	}

	public String2mapBase(Map<String, Object> map, Set<String> required)
	{
		this(map, required, null, null);
	}


	public String2mapBase(Map<String, Object> map, Set<String> required, Map<String, Function<String, ?>> converters)
	{
		this(map, required, converters, null);
	}

	public String2mapBase(
	Map<String, Object> map,
	Set<String> required,
	Map<String, Function<String, ?>> converters,
	Map<String, Supplier<String>> synthetics
	)
	{
		this.result     = map        == null ? new HashMap<>() : new HashMap<>(map);
		this.required   = required   == null ? new HashSet<>() : new HashSet<>(required);
		this.converters = converters == null ? new HashMap<>() : new HashMap<>(converters);
		this.synthetics = synthetics == null ? new HashMap<>() : new HashMap<>(synthetics);
	}

	public final void registerSynthesizer(String key, Supplier<String> function)
	{
		if (key == null)
		{
			return;
		}

		if (function == null)
		{
			return;
		}

		synthetics.put(key, function);
	}

	/**
	 * Register a function to convert a particular input item using custom logic
	 *
	 * @param key the key which identifies the input, if null this method has no effect.
	 * @param converter the function that will be used to convert the input value, if null removes the converter
	 */
	public final void registerConverter(String key, Function<String, ?> converter)
	{
		if (key == null)
		{
			return;
		}

		if (converter == null)
		{
			converters.remove(key);
		}
		else
		{
			converters.put(key, converter);
		}
	}

	public boolean isRequired(String key)
	{
		if (key == null)
		{
			log.warn("isRequired called with null key.");
			return false;
		}

		return required.contains(key);
	}
}
