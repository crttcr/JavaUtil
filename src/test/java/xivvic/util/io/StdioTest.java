package xivvic.util.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

// import static org.junit.Assert.*;
// import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class StdioTest
{
	private static final double EPSILON = 0.0001;

	private ByteArrayOutputStream out_bytes;
	private PrintStream  printstream;
	private InputStream yes_input;
	private InputStream  no_input;

	@Mock private InputStream mock_input;

	private Stdio subject;

	@Before
	public void before()
	{
		yes_input = inputStreamForString("yes\n");
		no_input  = inputStreamForString("no\n");

		out_bytes = new ByteArrayOutputStream();
		printstream = new PrintStream(out_bytes);

		subject = new Stdio(yes_input, printstream);
	}

	@After
	public void after()
	{
		yes_input = null;
		no_input = null;
		out_bytes = null;
		printstream = null;
	}

	@Test(expected = NullPointerException.class)
	public void onCreate_withNullInputStream_thenThrowException() {
		new Stdio(null, System.out);
	}

	@Test(expected = NullPointerException.class)
	public void onCreate_withNullPrintstream_thenThrowException() {
		new Stdio(System.in, null);
	}

	@Test
	public void onGetString_whenInputStreamThrows_thenReturnNull() {
		// Arrange
		//
		configureMockInputStreamToThrowOnRead();
		subject = new Stdio(mock_input, System.out);

		// Act
		//
		String s = subject.getString();

		// Assert
		//
		assertNull(s);
	}

	@Test
	public void onGetString_withEmptyInput_thenReturnNull() {
		// Arrange
		//
		// An empty input stream will return 0 bytes, causing an exception which his handled
		// by the getString() method.
		//
		InputStream is = inputStreamForString("");
		subject = new Stdio(is, printstream);
		subject = new Stdio(mock_input, System.out);

		// Act
		//
		String s = subject.getString();

		// Assert
		//
		assertNull(s);
	}

	@Test
	public void onGetString_withNewlineInput_thenReturnNull() {
		// Arrange
		//
		InputStream is = inputStreamForString("\n");
		subject = new Stdio(is, printstream);
		subject = new Stdio(mock_input, System.out);

		// Act
		//
		String s = subject.getString();

		// Assert
		//
		assertNull(s);
	}

	@Test
	public void onGetString_withSingleTerminatedLine_thenReturnLine() {
		// Arrange
		//
		String    line = "";
		InputStream is = inputStreamForString(line + "\n");
		subject        = new Stdio(is, printstream);

		// Act
		//
		String s = subject.getString();

		// Assert
		//
		assertNotNull(s);
		assertEquals(line, s);
	}

	@Test
	public void onGetString_withSingleUnterminatedTerminatedLine_thenReturnLine() {
		// Arrange
		//
		String    line = "Elephants are cool.";
		InputStream is = inputStreamForString(line);
		subject        = new Stdio(is, printstream);

		// Act
		//
		String s = subject.getString();

		// Assert
		//
		assertNotNull(s);
		assertEquals(line, s);
	}

	@Test
	public void onGetString_withTwoLinesAndTwoCalls_thenReturnEachLine() {
		// Arrange
		//
		String       a = "Now is the time";
		String       b = "Four score and";
		String    line = a + "\n" + b;
		InputStream is = inputStreamForString(line);
		subject        = new Stdio(is, printstream);

		// Act
		//
		String s = subject.getString();
		String t = subject.getString();

		// Assert
		//
		assertNotNull(s);
		assertNotNull(t);
		assertEquals(a, s);
		assertEquals(b, t);
	}

	@Test
	public void onConfirm_withNoPrompt_thenPromptIsAreYouSure() throws Exception
	{
		// Arrange
		//
		String prompt = null;

		// Act
		//
		subject.confirm(prompt, true);

		// Assert
		//
		assertEquals(Stdio.DEFAULT_CONFIRM_PROMPT, out_bytes.toString());
	}

	@Test
	public void onConfirm_withNoAnswer_thenReturnFalse()
	{
		// Arrange
		//
		String prompt = "Steady on Jack. You're sure? ";
		subject = new Stdio(no_input, printstream);


		// Act
		//
		boolean b = subject.confirm(prompt, true);

		// Assert
		//
		assertFalse(b);
	}


	@Test
	public void onConfirm_withYesAnswer_thenReturnTrue()
	{
		// Arrange
		//
		String prompt = "Steady on Jack. You're sure? ";
		yes_input = inputStreamForString("y\n");

		subject = new Stdio(yes_input, printstream);


		// Act
		//
		boolean b = subject.confirm(prompt, true);

		// Assert
		//
		assertTrue(b);
	}

	@Test
	public void onConfirm_withReadExceptionDefaultingTrue_thenReturnDefault() throws Exception
	{
		// Arrange
		//
		String prompt = "Steady on Jack. You're sure? ";
		yes_input = inputStreamForString("");

		subject = new Stdio(yes_input, printstream);


		// Act
		//
		boolean b = subject.confirm(prompt, true);

		// Assert
		//
		assertTrue(b);
	}


	@Test
	public void onConfirm_withReadExceptionDefaultingFalse_thenReturnDefault() throws Exception
	{
		// Arrange
		//
		String prompt = "Steady on Jack. You're sure? ";
		yes_input = inputStreamForString("");

		subject = new Stdio(yes_input, printstream);


		// Act
		//
		boolean b = subject.confirm(prompt, false);

		// Assert
		//
		assertFalse(b);
	}
	@Test
	public void onGetString_withNewline_thenReturnEmptyString() throws Exception
	{
		// Arrange
		//
		InputStream is = inputStreamForString("\n");
		subject = new Stdio(is, printstream);

		// Act
		//
		String s = subject.getString();

		// Assert
		//
		assertNotNull(s);
		assertEquals("", s);
	}

	@Test
	public void onGetString_withRegularString_thenReturnThatString() throws Exception
	{
		// Arrange
		//
		String s = "This is a regular string.";;
		InputStream is = inputStreamForString(s + "\n");
		subject = new Stdio(is, printstream);

		// Act
		//
		String result = subject.getString();

		// Assert
		//
		assertNotNull(result);
		assertEquals(s, result);
	}

	@Test
	public void onGetInteger_withIntegerString_thenReturnThatInteger() throws Exception
	{
		// Arrange
		//
		String s = "-10";
		InputStream is = inputStreamForString(s + "\n");
		subject = new Stdio(is, printstream);

		// Act
		//
		int result = subject.getInt();

		// Assert
		//
		assertEquals(-10, result);
	}

	@Test
	public void onGetDouble_withDoubleString_thenReturnThatInteger() throws Exception
	{
		// Arrange
		//
		String s = "1.654\n";
		InputStream is = inputStreamForString(s);
		subject = new Stdio(is, printstream);

		// Act
		//
		double result = subject.getDouble();

		// Assert
		//
		assertEquals(1.654, result, EPSILON);
	}

	@Test(expected = NullPointerException.class)
	public void onGSFL_withNullChoices_thenThrowException()
	{
		subject.getStringFromList(null, "Hello");
	}

	@Test
	public void onGSFL_withEmptyChoices_thenReturnNull()
	{
		List<String> empty = Arrays.asList();
		assertNull(subject.getStringFromList(empty, "Please choose"));
	}


	@Test
	public void onGSFL_withValidStringResponse_thenReturnChoice()
	{
		// Arrange
		//
		String target = "ape";
		List<String> choices = Arrays.asList("apple", "app", target, "ant");
		InputStream is = inputStreamForString(target);
		subject = new Stdio(is, printstream);

		// Act
		//
		String choice = subject.getStringFromList(choices, "Please choose");

		// Assert
		//
		assertEquals(target, choice);
	}

	@Test
	public void onGSFL_withEventualValidStringResponse_thenReturnChoice()
	{
		// Arrange
		//
		String target = "ape";
		List<String> choices = Arrays.asList("apple", "app", target, "ant");
		InputStream is = inputStreamForString("blast\nape\n");
		subject = new Stdio(is, printstream);

		// Act
		//
		String choice = subject.getStringFromList(choices, "Please choose");

		// Assert
		//
		assertEquals(target, choice);
	}

	@Test
	public void onGSFL_withEventualValidIndexResponse_thenReturnChoice()
	{
		// Arrange
		//
		String target = "ant";
		List<String> choices = Arrays.asList("apple", "app", "ale", target, "ant");
		InputStream is = inputStreamForString("blast\n3\n");
		subject = new Stdio(is, printstream);

		// Act
		//
		String choice = subject.getStringFromList(choices, "Please choose");

		// Assert
		//
		assertEquals(target, choice);
	}

	@Test(expected = NullPointerException.class)
	public void onGSFLWD_withNullChoices_thenThrowException()
	{
		subject.getStringFromListWithDefault(null, "Hello", 0);
	}

	@Test
	public void onGSFLWD_withEmptyChoices_thenReturnNull()
	{
		List<String> empty = Arrays.asList();
		assertNull(subject.getStringFromListWithDefault(empty, "Please choose", 0));
	}

	@Test
	public void onGSFLWD_withNegativeDefault_thenReturnNull()
	{
		List<String> choices = Arrays.asList("a");
		assertNull(subject.getStringFromListWithDefault(choices, "Please choose", -1));
	}

	@Test
	public void onGSFLWD_withDefaultOutOfRange_thenReturnNull()
	{
		List<String> choices = Arrays.asList("a");
		assertNull(subject.getStringFromListWithDefault(choices, "Please choose", 3));
	}

	@Test
	public void onGSFLWD_withEmptyResponse_thenChooseDefault()
	{
		// Arrange
		//
		int dv = 1;
		List<String> choices = Arrays.asList("a", "b", "c");
		InputStream is = inputStreamForString("\n");
		subject = new Stdio(is, printstream);

		// Act
		//
		String choice = subject.getStringFromListWithDefault(choices, "Please choose", dv);

		// Assert
		//
		assertEquals(choices.get(dv), choice);
	}

	@Test
	public void onGSFLWD_withValidStringResponse_thenReturnChoice()
	{
		// Arrange
		//
		int dv = 1;
		String target = "ape";
		List<String> choices = Arrays.asList("apple", "app", target, "ant");
		InputStream is = inputStreamForString("ape\n");
		subject = new Stdio(is, printstream);

		// Act
		//
		String choice = subject.getStringFromListWithDefault(choices, "Please choose", dv);

		// Assert
		//
		assertEquals(target, choice);
	}

	@Test
	public void onGSFLWD_withEventualValidStringResponse_thenReturnChoice()
	{
		// Arrange
		//
		int dv = 1;
		String target = "ape";
		List<String> choices = Arrays.asList("apple", "app", target, "ant");
		InputStream is = inputStreamForString("blast\nape\n");
		subject = new Stdio(is, printstream);

		// Act
		//
		String choice = subject.getStringFromListWithDefault(choices, "Please choose", dv);

		// Assert
		//
		assertEquals(target, choice);
	}

	@Test
	public void onGSFLWD_withEventualValidIndexResponse_thenReturnChoice()
	{
		// Arrange
		//
		int dv = 1;
		String target = "ape";
		List<String> choices = Arrays.asList("apple", "app", target, "ant");
		InputStream is = inputStreamForString("blast\n2\n");
		subject = new Stdio(is, printstream);

		// Act
		//
		String choice = subject.getStringFromListWithDefault(choices, "Please choose", dv);

		// Assert
		//
		assertEquals(target, choice);
	}


	@Test(expected = NullPointerException.class)
	public void onPFEVWD_withNullDefault_thenThrowException()
	{
		subject.promptForEnumValueWithDefault("Pick one", null);
	}

	@Test
	public void onPFEVWD_withNullPrompt_thenUseStandardPrompt()
	{
		// Arrange
		//
		TimeUnit    dv = TimeUnit.DAYS;
		String  prompt = "Pick a time unit";
		InputStream is = inputStreamForString("SECONDS\n");
		subject        = new Stdio(is, printstream);

		// Act
		//
		TimeUnit result = subject.promptForEnumValueWithDefault(prompt, dv);

		// Assert
		//
		assertEquals(TimeUnit.SECONDS, result);
		assertTrue(out_bytes.toString().contains("Select"));
	}

	@Test(expected = NullPointerException.class)
	public void onPFE_withNullEnum_thenThrowException()
	{
		assertNull(subject.promptForEnumValue("Pick", null));
	}

	@Test
	public void onPFEV_withNullPrompt_thenUseStandardPrompt()
	{
		// Arrange
		//
		String  prompt = "Pick a time unit";
		InputStream is = inputStreamForString("SECONDS\n");
		subject        = new Stdio(is, printstream);

		// Act
		//
		TimeUnit result = subject.promptForEnumValue(prompt, TimeUnit.class);

		// Assert
		//
		assertEquals(TimeUnit.SECONDS, result);
		assertTrue(out_bytes.toString().contains("Select"));
	}

	@Test
	public void onPFEV_withEventualGoodAnswer_thenReturnNull()
	{
		// Arrange
		//
		String  prompt = "Pick a time unit";
		InputStream is = inputStreamForString("CATS\nDOGS\nHOURS");
		subject        = new Stdio(is, printstream);

		// Act
		//
		TimeUnit result = subject.promptForEnumValue(prompt, TimeUnit.class);

		// Assert
		//
		assertNotNull(result);
		assertEquals(TimeUnit.HOURS, result);
	}

	@Test(expected = NullPointerException.class)
	public void onPFFMP_withNullPath_thenThrowException()
	{
		subject.promptForFileMatchingPattern("Pick", null, "*.txt");
	}


	///////////////////////////////
	// Helpers                   //
	///////////////////////////////

	private InputStream inputStreamForString(String s)
	{
		Objects.requireNonNull(s);

		byte[]   bytes = s.getBytes(StandardCharsets.UTF_8);
		InputStream rv = new ByteArrayInputStream(bytes);

		return rv;
	}

	private void configureMockInputStreamToThrowOnRead()
	{
		IOException ex = new IOException("You blew it");

		try
		{
			//			when(mock_input.read()).thenThrow(ex);
			//			when(mock_input.read(any())).thenThrow(ex);
			when(mock_input.read(any(), anyInt(), anyInt())).thenThrow(ex);
		}
		catch (IOException e)
		{
			fail("Configuring mock threw an exception. But it should not have happened");
		}
	}
}
