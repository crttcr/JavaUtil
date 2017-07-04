package xivvic.util.password;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PasswordUtilTest
{
	@Test(expected=IllegalArgumentException.class)
	public void onGetSaltedHash_withNullPassword_thenThrowException()
	{
		PasswordUtil.getSaltedHash(null);
	}

	@Test(expected=IllegalArgumentException.class)
	public void onGetSaltedHash_withEmptyPassword_thenThrowException()
	{
		PasswordUtil.getSaltedHash("");
	}

	@Test
	public void onGetSaltedHash_withValidArguments_thenReturnHashedPassword()
	{
		// Arrange
		//
		String pwd  = "39459fk49fk392*23&!DKvieke*2)";

		// Act
		//
		String encoded = PasswordUtil.getSaltedHash(pwd);

		// Assert
		//
		System.out.println(encoded);
		assertNotNull(encoded);
	}


	@Test(expected=IllegalArgumentException.class)
	public void onHash_withNullPassword_thenThrowException()
	{
		byte[] salt = {0x45, 0x23 };
		PasswordUtil.hash(null, salt);
	}

	@Test(expected=IllegalArgumentException.class)
	public void onHash_withEmptyPassword_thenThrowException()
	{
		byte[] salt = {0x45, 0x23 };
		PasswordUtil.hash("", salt);
	}

	@Test(expected=IllegalArgumentException.class)
	public void onHash_withNullSalt_thenThrowException()
	{
		PasswordUtil.hash("39459fk49fk392*23&!DKvieke*2)", null);
	}

	@Test(expected=IllegalArgumentException.class)
	public void onHash_withEmptySalt_thenThrowException()
	{
		byte[] salt = {};
		PasswordUtil.hash("39459fk49fk392*23&!DKvieke*2)", salt);
	}

	@Test
	public void onHash_withValidArguments_thenReturnHashedPassword()
	{
		// Arrange
		//
		byte[] salt = {0x23, 0x33, 0x45, 0x23, 0x39, 0x11, 0x54, 0x56};
		String pwd  = "39459fk49fk392*23&!DKvieke*2)";

		// Act
		//
		String encoded = PasswordUtil.hash(pwd, salt);

		// Assert
		//
		assertNotNull(encoded);
	}

	@Test(expected=IllegalArgumentException.class)
	public void onCheck_withNullPassword_thenThrowException()
	{
		String expected = "Ky1gQZuuQOvPqac4r1FpO03koCCTsuWOE63gwkDmkjA=";
		PasswordUtil.check(null, expected);
	}

	@Test(expected=IllegalArgumentException.class)
	public void onCheck_withEmptyPassword_thenThrowException()
	{
		String expected = "Ky1gQZuuQOvPqac4r1FpO03koCCTsuWOE63gwkDmkjA=";
		PasswordUtil.check("", expected);
	}

	@Test(expected=IllegalArgumentException.class)
	public void onCheck_withNullEncoded_thenThrowException()
	{
		PasswordUtil.check("39459fk49fk392*23&!DKvieke*2)", null);
	}

	@Test(expected=IllegalArgumentException.class)
	public void onCheck_withEmptyEncoded_thenThrowException()
	{
		PasswordUtil.check("39459fk49fk392*23&!DKvieke*2)", "");
	}

	@Test(expected = IllegalArgumentException.class)
	public void onCheck_withEncodedFormMissingDelimiter_thenThrowException()
	{
		String pwd  = "39459fk49fk392*23&!DKvieke*2)";
		String expected = "this_is_salt.Ky1gQZuuQOvPqac4r1FpO03koCCTsuWOE63gwkDmkjA=";
		PasswordUtil.check(pwd, expected);
	}

	@Test
	public void onCheck_withCorrectHash_thenReturnTrue()
	{
		// Arrange
		//
		String pwd  = "39459fk49fk392*23&!DKvieke*2)";
		String expected = "snEFNpq9TLRvvN7NGqYKemUFaaHyEv2m9Z9QZYcRm7g=$rvU9l/fsnPUa/w56A0rfTTVfdbfnrtl46G/MkUVaLeE=";

		// Act
		//
		boolean b = PasswordUtil.check(pwd, expected);

		// Assert
		//
		assertTrue(b);
	}

	@Test
	public void onCheck_withCorrectHash_thenReturnFalse()
	{
		// Arrange
		//
		String pwd  = "39459fk49fk392*23&!DKvieke*2)";
		String good = "snEFNpq9TLRvvN7NGqYKemUFaaHyEv2m9Z9QZYcRm7g=$rvU9l/fsnPUa/w56A0rfTTVfdbfnrtl46G/MkUVaLeE=";
		String bad  = good.substring(1);

		// Act
		//
		boolean b = PasswordUtil.check(pwd, bad);

		// Assert
		//
		assertFalse(b);
	}
}
