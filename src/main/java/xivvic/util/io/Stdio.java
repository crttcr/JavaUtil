package xivvic.util.io;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Stdio
{
	public static String DEFAULT_CONFIRM_PROMPT = "Are you sure? ";

	private final BufferedReader in;
	private final PrintStream out;
	public Stdio(InputStream in, PrintStream out)
	{
		Objects.requireNonNull(in);
		Objects.requireNonNull(out);

		this.in  = new BufferedReader(new InputStreamReader(in));
		this.out = out;
	}

	public String getString()
	{
		try
		{
			String line = in.readLine();
			return line;
		}
		catch (Exception e)
		{
			String msg = String.format("Error encountered reading from (buffered) InputStream [%s]: %s", in, e.getMessage());
			log.error(msg);
			return null;
		}
	}

	public String getString(String prompt)
	{
		Objects.requireNonNull(prompt);

		if (prompt.trim().length() == 0)
		{
			String msg = String.format("A blank string [%s] does not make for an adequate prompt.", prompt);
			log.error(msg);
			return null;
		}

		prompt = prompt.endsWith(" ") ? prompt : prompt + " ";

		try
		{
			out.print(prompt);
			String line = in.readLine();
			return line;
		}
		catch (Exception e)
		{
			String msg = String.format("Error encountered reading from (buffered) InputStream [%s]: %s", in, e.getMessage());
			log.error(msg);
			return null;
		}
	}

	public String getStringWithDefault(String prompt, String dv)
	{
		Objects.requireNonNull(prompt);
		Objects.requireNonNull(dv);

		if (prompt.trim().length() == 0)
		{
			String msg = String.format("A blank string [%s] does not make for an adequate prompt.", prompt);
			log.error(msg);
			return null;
		}

		prompt = prompt.endsWith(" ") ? prompt : prompt + " ";

		try
		{
			out.print(prompt);
			String line = in.readLine();
			if (line == null || line.trim().length() == 0)
			{
				return dv;
			}
			return line;
		}
		catch (Exception e)
		{
			String msg = String.format("Error encountered reading from (buffered) InputStream [%s]: %s", in, e.getMessage());
			log.error(msg);
			return null;
		}
	}

	public String getStringFromList(List<String> choices, String prompt)
	{
		Objects.requireNonNull(choices);

		if (choices.size() == 0)
		{
			return null;
		}

		if (prompt == null || prompt.trim().length() == 0)
		{
			prompt = "Chose among the following items: ";
		}

		String fmt = "%2d -- %s\n";

		while (true)
		{
			out.print(prompt);
			for (int i = 0; i < choices.size(); i++)
			{
				out.printf(fmt, i, choices.get(i));
			}

			out.print("Selection --> ");

			String choice = getString();
			Integer index = attemptToRecognizeUserInputAsChoice(choices, choice);
			if (index != null)
			{
				return choices.get(index);
			}

			out.printf("Response [%s] is not valid. Choose another item or its index", choice);
		}
	}

	public String getStringFromListWithDefault(List<String> choices, String prompt, int def)
	{
		Objects.requireNonNull(choices);

		if (def < 0 || def >= choices.size())
		{
			return null;
		}

		if (choices.size() == 0)
		{
			return null;
		}

		if (prompt == null || prompt.trim().length() == 0)
		{
			prompt = "Chose among the following items: ";
		}

		String dv = choices.get(def);
		String fmt = "%2d -- %s\n";

		while (true)
		{
			out.printf("%s [%s]", prompt, dv);
			for (int i = 0; i < choices.size(); i++)
			{
				out.printf(fmt, i, choices.get(i));
			}

			out.print("Selection --> ");

			String choice = getString();
			if (choice == null || choice.trim().length() == 0)
			{
				return dv;
			}

			Integer index = attemptToRecognizeUserInputAsChoice(choices, choice);
			if (index != null)
			{
				return choices.get(index);
			}

			out.printf("Response [%s] is not valid. Choose another item or its index", choice);
		}
	}



	/**
	 * Confirms a user action, returning true if the response is "y" or "yes"
	 *
	 */
	public boolean confirm(String prompt, boolean def)
	{
		if (prompt == null || prompt.length() == 0)
		{
			prompt = DEFAULT_CONFIRM_PROMPT;
		}

		try
		{
			out.print(prompt);
			String input = in.readLine();
			if (input == null || input.length() == 0)
			{
				return def;
			}

			String lower = input.trim().toLowerCase();

			if (lower.equals("y") || lower.equals("yes"))
			{
				return true;
			}

			return false;
		}
		catch (Exception e)
		{
			out.println("Exception confirming user action. Returning false.");
			return false;
		}
	}

	public String promptString(String prompt, String def)
	{
		if (prompt == null || prompt.length() == 0)
		{
			prompt = "input? ";
		}

		try
		{
			System.out.print(prompt);
			String input = in.readLine();
			if (input == null || input.length() == 0)
			{
				return def == null ? "" : def;
			}
			return input;
		}
		catch (Exception e)
		{
			System.out.println("getString() exception, returning empty string");
			return "";
		}
	}

	// Read a char from standard system input
	//
	public char getChar()
	{
		String s = getString();
		if (s.length() >= 1)
		{
			return s.charAt(0);
		}
		else
		{
			return '\n';
		}
	}

	public Number getNumber()
	{
		String s = getString();
		try
		{
			String  trim = s.trim();
			String upper = trim.toUpperCase();

			return NumberFormat.getInstance().parse(upper);
		}
		catch (Exception e)
		{
			out.println("getNumber() exception, returning 0");
			return null;
		}
	}

	public Integer getInt()
	{
		Number n = getNumber();
		if (n == null)
		{
			return null;
		}

		return n.intValue();
	}

	public Integer getInt(String prompt)
	{
		// FIXME PROMPT USER FOR INTEGER VALUE
		//
		Number n = getNumber();
		if (n == null)
		{
			return null;
		}

		return n.intValue();
	}

	public Float getFloat()
	{
		Number n = getNumber();
		if (n == null)
		{
			return null;
		}

		return n.floatValue();
	}

	public Double getDouble()
	{
		Number n = getNumber();
		if (n == null)
		{
			return null;
		}

		return n.doubleValue();
	}

	public <E extends Enum<E>> E promptForEnumValueWithDefault(String prompt, E dv)
	{
		Objects.requireNonNull(dv);

		prompt = isBlank(prompt) ? "Select one of the enumerated values: " : prompt;

		E[]         values = dv.getDeclaringClass().getEnumConstants();
		List<String> names = new ArrayList<>(values.length);

		int defaultPosition = 0;
		for (int i = 0; i < values.length; i++)
		{
			E item = values[i];
			names.add(item.name());
			if (dv == item)
			{
				defaultPosition = i;
			}
		}

		String answer = getStringFromListWithDefault(names, prompt, defaultPosition);

		for (int i = 0; i < values.length; i++)
		{
			E item = values[i];
			if (answer.equals(item.name()))
			{
				return item;
			}
		}

		// Should never exit the loop above without finding the chosen item.
		//
		return dv;
	}

	// TODO: Test this method
	//
	public <E extends Enum<E>> E promptForEnumValue(String prompt, Class<E> enumClass)
	{
		Objects.requireNonNull(enumClass);

		prompt = isBlank(prompt) ? "Select one of the enumerated values: " : prompt;

		E[] values = enumClass.getEnumConstants();
		List<String> names = new ArrayList<>(values.length);

		for (int i = 0; i < values.length; i++)
		{
			E item = values[i];
			names.add(item.name());
		}

		String answer = getStringFromList(names, prompt);

		for (int i = 0; i < values.length; i++)
		{
			E item = values[i];
			if (answer.equals(item.name()))
			{
				return item;
			}
		}

		// Should never exit the loop above without finding the chosen item.
		//
		String msg = String.format("Failed to match choice [%s] to one of the values of the [%s] enumerated type", answer, enumClass.getName());
		throw new RuntimeException(msg);
	}

	private boolean isBlank(String s)
	{
		if (s == null || s.trim().length() == 0)
		{
			return true;
		}

		return false;
	}

	private Integer attemptToRecognizeUserInputAsChoice(List<String> choices, String choice)
	{
		if (choices == null || choices.size() == 0)
		{
			return null;
		}

		if (choice == null || choice.length() == 0)
		{
			return null;
		}

		try
		{
			int i = Integer.parseInt(choice);
			if (i >= 0 || i < choices.size())
			{
				return i;
			}
		}
		catch (NumberFormatException e)
		{
			// Not an integer.
			// Fall through and try to match choice to one of the items in the list.
		}

		int pos = choices.indexOf(choice);
		if (pos == -1)
		{
			return null;
		}

		return pos;
	}


}
