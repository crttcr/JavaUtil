package xivvic.util.time;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.function.Function;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TimeUtil
{
	public static Function<String, Long> textToNanosFunction()
	{
		Function<String, Long> function = (s) ->
		{
			if (s == null)
			{
				log.warn("Null time");
				return null;
			}

			Long result = null;
			try
			{
				DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_TIME;
				LocalTime              time = LocalTime.parse(s, formatter);

				result = time.toNanoOfDay();
			}
			catch(DateTimeParseException e)
			{
				String msg = String.format("Parse error for time value: [%s]", s);
				log.warn(msg);
				return null;
			}

			return result;
		};

		return function;
	}

	// Converts a text object to a date, using the supplied formatter
	//
	public static Function<String, Long> text2EpochDaysFunction(final DateTimeFormatter fmt)
	{
		if (fmt == null)
		{
			return null;
		}

		Function<String, Long> function = (s) ->
		{
			if (s == null)
			{
				return null;
			}

			Long result = null;
			try
			{
				LocalDate              date = LocalDate.parse(s, fmt);

				result = date.toEpochDay();
			}
			catch(DateTimeParseException e)
			{
				String msg = String.format("Parse error for date value: [%s]", s);
				log.warn(msg);
				return null;
			}

			return result;
		};

		return function;
	}

	// Converts a text object in the form of ISO_DATE to a number of days in the Unix epoch.
	//
	public static Function<String, Long> text2EpochDaysFunction()
	{
		final DateTimeFormatter fmt = DateTimeFormatter.ISO_LOCAL_DATE;
		return text2EpochDaysFunction(fmt);
	}
}
