package xivvic.util.identity;

/**
 * This is not as secure as SecureRandomIdentifier, but it's 
 * fast and creates variable length strings.
 * 
 */
import java.util.Random;
import java.util.function.Supplier;

public class RandomString
{
	private static final char[] symbols;
	private final Random		  random = new Random();
	private final char[]		  buf;

	static
	{
		StringBuilder tmp = new StringBuilder();

		// Change here to include more or different characters
		//
		// Note: This character set excludes characters that get confused
		//
		// 0 1 L O l o
		//
		for (char ch = '2'; ch <= '9'; ++ch)
			tmp.append(ch);
		for (char ch = 'A'; ch <= 'K'; ++ch)
			tmp.append(ch);
		for (char ch = 'M'; ch <= 'N'; ++ch)
			tmp.append(ch);
		for (char ch = 'P'; ch <= 'Z'; ++ch)
			tmp.append(ch);
		for (char ch = 'a'; ch <= 'k'; ++ch)
			tmp.append(ch);
		for (char ch = 'm'; ch <= 'n'; ++ch)
			tmp.append(ch);
		for (char ch = 'p'; ch <= 'z'; ++ch)
			tmp.append(ch);

		symbols = tmp.toString().toCharArray();
	}

	// Node, this is not the best way to call this functions multiple times within a heavily
	// spinning loop.  Create your own instance variable in that case.
	//
	public final static Supplier<String> RandomStringSupplier(int length)
	{
		return () -> { RandomString rs = new RandomString(length); return rs.nextString(); };
	}
	
	public RandomString(int length)
	{
		if (length < 1)
			throw new IllegalArgumentException("length < 1: " + length);

		buf = new char[length];
	}

	public String nextString()
	{
		for (int idx = 0; idx < buf.length; ++idx)
		{
			buf[idx] = symbols[random.nextInt(symbols.length)];
		}

		return new String(buf);
	}
}