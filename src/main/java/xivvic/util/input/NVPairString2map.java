package xivvic.util.input;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import lombok.extern.slf4j.Slf4j;
import xivvic.util.arg.ArgUtil;

/**
 * Class for processing inputs structured as name value pairs.
 *
 * @author Reid
 */
@Slf4j
public class NVPairString2map
extends String2mapBase
{
	public static String2map   DEFAULT = new NVPairString2map(null, null, null);

	public NVPairString2map(Map<String, Object> map, Set<String> required, Map<String, Function<String, ?>> converters)
	{
		super(map, required, converters);
	}

	public NVPairString2map(
	Map<String, Object> map,
	Set<String> required,
	Map<String, Function<String, ?>> converters,
	Map<String, Supplier<String>> synthetics
	)
	{
		super(map, required, converters, synthetics);
	}

	// Convert the string param into a map(String -> String) and make sure
	// that all required keys are present, returning null if they are not.
	//
	@Override
	public Map<String, Object> process(String params)
	{
		final Map<String, String> args = ArgUtil.structuredArgument2Map(params);
		if (args == null)
		{
			String msg = String.format("Failed to retrieve arg map from parameter: [%s]", params);
			log.warn(msg);
			return null;
		}

		// Here we are placing in synthetic values where they don't exist
		//
		BiConsumer<String, Supplier<String>> call_and_map = (k, v) -> { if (! args.containsKey(k))
		{
			args.put(k, v.get());
		} };
		synthetics.forEach(call_and_map);


		Predicate<String>   key_exists = s -> args.containsKey(s);
		boolean              all_there = required.stream().allMatch(key_exists);

		if (! all_there)
		{
			String msg = String.format("Not all args required [%s] args present", required);
			log.warn(msg);
			return null;
		}

		// Then apply an argument processor to each key in the map where there is a corresponding
		// key in the converters collection for this processor.
		//
		for(Entry<String, String> entry : args.entrySet())
		{
			String                 key = entry.getKey();
			String               value = entry.getValue();
			Function<String, ?>      f = converters.get(key);
			Object              object = value;

			if (f != null)
			{
				object = f.apply(value);
			}

			result.put(key, object);
		}


		return result;
	}

}
