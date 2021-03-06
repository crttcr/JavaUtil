package xivvic.util.password;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.apache.commons.codec.binary.Base64;

public class PasswordUtil
{

	// The higher the number of ITERATIONS the more
	// expensive computing the hash is for us and
	// also for an attacker.
	//
	private static final String     RANDOM_ALGORITHM = "SHA1PRNG";
	private static final String SECRET_KEY_ALGORITHM = "PBKDF2WithHmacSHA1";
	private static final int         	     ITERATIONS = 20_000;
	private static final int             SALT_LENGTH = 32;
	private static final int      	DESIRED_KEY_LENGTH = 256;

	/**
	 * Computes a salted PBKDF2 hash of given plaintext password suitable for
	 * storing in a database. Empty passwords are not supported.
	 */
	public static String getSaltedHash(String password)
	{
		if (password == null || password.length() == 0)
		{
			throw new IllegalArgumentException( "Empty passwords are not supported.");
		}

		SecureRandom sr;
		try
		{
			sr = SecureRandom.getInstance(RANDOM_ALGORITHM);
		}
		catch (NoSuchAlgorithmException e)
		{
			throw new RuntimeException("Standard RANDOM ALGO not available: " + e.getLocalizedMessage());
		}

		byte[]    salt = sr.generateSeed(SALT_LENGTH);
		String    hash = hash(password, salt);
		String encoded = Base64.encodeBase64String(salt);

		// Store the salt with the password
		//
		String rv = encoded + "$" + hash;
		return rv;
	}


	/**
	 * Checks whether given plaintext password corresponds to a stored salted hash of the password.
	 */
	public static boolean check(String password, String stored)
	{
		if (password == null || password.length() == 0)
		{
			throw new IllegalArgumentException( "Empty passwords are not supported.");
		}

		if (stored == null || stored.length() == 0)
		{
			throw new IllegalArgumentException( "Stored value cannot be blank.");
		}

		String[] saltAndPass = stored.split("\\$");
		if (saltAndPass.length != 2)
		{
			throw new IllegalArgumentException("The stored password have the form 'salt$hash'");
		}

		String hashOfInput = hash(password, Base64.decodeBase64(saltAndPass[0]));
		return hashOfInput.equals(saltAndPass[1]);
	}

	// using PBKDF2 from Sun, an alternative is https://github.com/wg/scrypt
	// cf. http://www.unlimitednovelty.com/2012/03/dont-use-bcrypt.html
	//
	public static String hash(String password, byte[] salt)
	{
		if (password == null || password.length() == 0)
		{
			throw new IllegalArgumentException( "Empty passwords are not supported.");
		}

		if (salt == null || salt.length == 0)
		{
			throw new IllegalArgumentException( "Degenerate salt not supported.");
		}

		try
		{
			SecretKeyFactory kf = SecretKeyFactory.getInstance(SECRET_KEY_ALGORITHM);
			char[]        chars = password.toCharArray();
			PBEKeySpec     spec = new PBEKeySpec(chars, salt, ITERATIONS, DESIRED_KEY_LENGTH);
			SecretKey       key = kf.generateSecret(spec);
			byte[]        bytes = key.getEncoded();

			return Base64.encodeBase64String(bytes);
		}
		catch (NoSuchAlgorithmException | InvalidKeySpecException e )
		{
			throw new RuntimeException(e);
		}
	}
}
