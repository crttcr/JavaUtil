package xivvic.util.io;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TerminalTextTest
{
	@Test
	public void onWrap_withEnumeratedValue_thenDisplayTheRightColor() throws Exception
	{
		TerminalText.Code[] codes = TerminalText.Code.values();

		for (TerminalText.Code c : codes)
		{
			String name = c.name();
			String text = c.wrap(name);
			System.out.println(text);
		}
	}

}