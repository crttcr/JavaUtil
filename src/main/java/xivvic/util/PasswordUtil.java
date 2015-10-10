package xivvic.util;

import java.security.SecureRandom;

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
	private static final String            ALGO = "PBKDF2WithHmacSHA1";
	private static final int         ITERATIONS = 20 * 1000;
	private static final int        SALT_LENGTH = 32;
	private static final int DESIRED_KEY_LENGTH = 256;

	/**
	 * Computes a salted PBKDF2 hash of given plaintext password suitable for 
	 * storing in a database. Empty passwords are not supported.
	 */
	public static String getSaltedHash(String password) 
		throws Exception
	{
		String rng_algo = "SHA1PRNG";
		SecureRandom sr = SecureRandom.getInstance(rng_algo);
		byte[]     salt = sr.generateSeed(SALT_LENGTH);

		// store the salt with the password
		//
		return Base64.encodeBase64String(salt) + "$" + hash(password, salt);
	}


	/**
	 * Checks whether given plaintext password corresponds to a stored salted hash of the password.
	 */
	public static boolean check(String password, String stored) 
		throws Exception
	{
		String[] saltAndPass = stored.split("\\$");
		if (saltAndPass.length != 2)
		{
			throw new IllegalStateException("The stored password have the form 'salt$hash'");
		}

		String hashOfInput = hash(password, Base64.decodeBase64(saltAndPass[0]));
		return hashOfInput.equals(saltAndPass[1]);
	}

	// using PBKDF2 from Sun, an alternative is https://github.com/wg/scrypt
	// cf. http://www.unlimitednovelty.com/2012/03/dont-use-bcrypt.html
	//
	public static String hash(String password, byte[] salt) throws Exception
	{
		if (password == null || password.length() == 0)
			throw new IllegalArgumentException( "Empty passwords are not supported.");

		SecretKeyFactory f = SecretKeyFactory.getInstance(ALGO);
		char[]       chars = password.toCharArray();
		PBEKeySpec    spec = new PBEKeySpec(chars, salt, ITERATIONS, DESIRED_KEY_LENGTH);
		SecretKey      key = f.generateSecret(spec);
		byte[]       bytes = key.getEncoded();

		return Base64.encodeBase64String(bytes);
	}
}
