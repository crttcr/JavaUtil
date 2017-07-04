package xivvic.util.time;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.function.Function;

public class TimeUtil
{
	public static Function<String, Object> textToNanosFunction()
	{
		Function<String, Object> function = (s) ->
		{
			if (s == null)
			{
				return "null time";
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
				return msg;
			}

			return result;
		};

		return function;
	}

	// Converts a text object to a date, using the supplied formatter
	//
	public static Function<String, Object> text2EpochFunction(final DateTimeFormatter fmt)
	{
		if (fmt == null)
		{
			return null;
		}

		Function<String, Object> function = (s) ->
		{
			if (s == null)
			{
				return "null";
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
				return msg;
			}

			return result;
		};

		return function;
	}

	// Converts a text object in the form of ISO_DATE to a number of days in the Unix epoch.
	//
	public static Function<String, Object> text2EpochFunction()
	{
		Function<String, Object> function = (s) ->
		{
			if (s == null)
			{
				return "null";
			}

			Long result = null;
			try
			{
				DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
				LocalDate              date = LocalDate.parse(s, formatter);

				result = date.toEpochDay();
			}
			catch(DateTimeParseException e)
			{
				String msg = String.format("Parse error for date value: [%s]", s);
				return msg;
			}

			return result;
		};

		return function;
	}

	// Converts a text object in the form of ISO_DATE to a number of days in the Unix epoch.
	//
	public static Function<String, Long> text2EpochFunctionLong()
	{
		Function<String, Long> function = (s) ->
		{
			if (s == null)
			{
				return null;
			}

			Long result = null;
			try
			{
				DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
				LocalDate              date = LocalDate.parse(s, formatter);

				result = date.toEpochDay();
			}
			catch(DateTimeParseException e)
			{
				String msg = String.format("Parse error for date value: [%s]", s);
				System.err.println(msg);
				return null;
			}

			return result;
		};

		return function;
	}

}
