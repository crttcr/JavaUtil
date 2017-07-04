package xivvic.util.identity;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class SecureRandomIdentifierTest
{

	private SecureRandomIdentifier subject;

	@Before
	public void before()
	{
		subject = new SecureRandomIdentifier();

	}

	@Test
	public void onCall_thenReturnRandomString()
	{
		String id = subject.nextId();

		assertNotNull(id);


		System.out.println(id);
	}

	@Test
	public void onSequentialCalls_thenReturnRandomString()
	{
		String a = subject.nextId();
		String b = subject.nextId();
		String c = subject.nextId();

		assertNotNull(a);
		assertNotNull(b);
		assertNotNull(c);

		assertFalse(a.equals(b));
		assertFalse(a.equals(c));
		assertFalse(b.equals(c));

	}
}
