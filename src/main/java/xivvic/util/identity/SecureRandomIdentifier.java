package xivvic.util.identity;

import java.math.BigInteger;
import java.security.SecureRandom;

public final class SecureRandomIdentifier
{
	private static final SecureRandom random = new SecureRandom();

	public String nextId() 
	{
		return new BigInteger(130, random).toString(32);
	}
}
