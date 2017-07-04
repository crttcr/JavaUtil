package xivvic.util.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.function.Function;

import org.junit.Test;


public class TimeUtilTest
{
	@Test
	public void onText2nanos_withNull_thenReturnNull()
	{
		Function<String, Long> f = TimeUtil.textToNanosFunction();
		assertNull(f.apply(null));
	}

	@Test
	public void onText2nanos_withEmptyString_thenReturnNull()
	{
		Function<String, Long> f = TimeUtil.textToNanosFunction();
		assertNull(f.apply(""));
	}

	@Test
	public void onText2nanos_withBadFormat_thenReturnNull()
	{
		Function<String, Long> f = TimeUtil.textToNanosFunction();
		assertNull(f.apply("09:40abc"));
	}

	@Test
	public void testTextToNanosFunctionHM()
	{
		// Arrange
		//
		String s ="00:01";
		Function<String, Long> converter = TimeUtil.textToNanosFunction();

		// Act
		//
		Long out = converter.apply(s);

		// Assert
		//
		assertNotNull(out);
		assertEquals(60000000000L, out.longValue());
	}

	@Test
	public void testTextToNanosFunctionHMS()
	{
		// Arrange
		//
		String s ="00:01:03";
		Function<String, Long> converter = TimeUtil.textToNanosFunction();

		// Act
		//
		Long out = converter.apply(s);

		// Assert
		//
		assertNotNull(out);
		assertEquals(63000000000L, out.longValue());
	}

	@Test
	public void onText2epochLong_withNull_thenReturnNull()
	{
		Function<String, Long> f = TimeUtil.text2EpochDaysFunction();
		assertNull(f.apply(null));
	}

	@Test
	public void onText2epochLong_withEmptyString_thenReturnNull()
	{
		Function<String, Long> f = TimeUtil.text2EpochDaysFunction();
		assertNull(f.apply(""));
	}

	@Test
	public void onText2epochLong_withBadFormat_thenReturnNull()
	{
		Function<String, Long> f = TimeUtil.text2EpochDaysFunction();
		assertNull(f.apply("2017x03x01"));
	}

	@Test
	public void onText2epochLong_withT0_thenReturnZero()
	{
		// Arrange
		//
		String epoch = "1970-01-01";
		Function<String, Long> f = TimeUtil.text2EpochDaysFunction();

		// Act
		//
		Long result = f.apply(epoch);

		// Assert
		//
		assertNotNull(result);
		assertTrue(0L == result);
	}

	@Test
	public void onText2epochLong_withNow_thenReturnSecondsSinceEpoch()
	{
		// Arrange
		//
		LocalDate today = LocalDate.now();
		Long expected = today.toEpochDay();
		String s = today.toString();
		Function<String, Long> f = TimeUtil.text2EpochDaysFunction();

		// Act
		//
		Long result = f.apply(s);

		// Assert
		//
		assertNotNull(result);
		assertEquals(expected.longValue(), result.longValue());
	}

}
