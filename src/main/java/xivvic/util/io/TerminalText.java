package xivvic.util.io;

public class TerminalText {

	private TerminalText() throws InstantiationException
	{
		throw new InstantiationException();
	}

	public static String black(String text)        { return Code.BLACK         .wrap(text); }
	public static String red(String text)          { return Code.RED           .wrap(text); }
	public static String redBold(String text)      { return Code.RED_BOLD      .wrap(text); }
	public static String green(String text)        { return Code.GREEN         .wrap(text); }
	public static String greenBold(String text)    { return Code.GREEN_BOLD    .wrap(text); }
	public static String yellow(String text)       { return Code.YELLOW        .wrap(text); }
	public static String yellowBold(String text)   { return Code.YELLOW_BOLD   .wrap(text); }
	public static String blue(String text)         { return Code.BLUE          .wrap(text); }
	public static String blueBold(String text)     { return Code.BLUE_BOLD     .wrap(text); }
	public static String magenta(String text)      { return Code.MAGENTA       .wrap(text); }
	public static String magentaBold(String text)  { return Code.MAGENTA_BOLD  .wrap(text); }
	public static String cyan(String text)         { return Code.CYAN          .wrap(text); }
	public static String cyanBold(String text)     { return Code.CYAN_BOLD     .wrap(text); }
	public static String gray(String text)         { return Code.GRAY          .wrap(text); }
	public static String grayBold(String text)     { return Code.GRAY_BOLD     .wrap(text); }
	public static String darkGray(String text)     { return Code.DARK_GRAY     .wrap(text); }
	public static String darkGrayBold(String text) { return Code.DARK_GRAY_BOLD.wrap(text); }
	public static String underline(String text)    { return Code.UNDERLINE     .wrap(text); }
	public static String bold(String text)         { return Code.BOLD          .wrap(text); }

	public enum Code
	{
		BLACK          ("\033[30m",   "\033[39m"),
		RED            ("\033[31m",   "\033[39m"),
		RED_BOLD       ("\033[31;1m", "\033[39m"),
		GREEN		      ("\033[32m",   "\033[39m"),
		GREEN_BOLD     ("\033[32;1m", "\033[39m"),
		YELLOW	      ("\033[33m",   "\033[39m"),
		YELLOW_BOLD	   ("\033[33;1m", "\033[39m"),
		BLUE    	      ("\033[34m",   "\033[39m"),
		BLUE_BOLD      ("\033[34;1m", "\033[39m"),
		MAGENTA    	   ("\033[35m",   "\033[39m"),
		MAGENTA_BOLD   ("\033[35;1m", "\033[39m"),
		CYAN    	      ("\033[36m",   "\033[39m"),
		CYAN_BOLD      ("\033[36;1m", "\033[39m"),
		GRAY		      ("\033[37m",   "\033[39m"),
		GRAY_BOLD	   ("\033[37;1m", "\033[39m"),
		DARK_GRAY      ("\033[38m",   "\033[39m"),
		DARK_GRAY_BOLD ("\033[38;1m", "\033[39m"),
		UNDERLINE 	   ("\033[4m",    "\033[24m"),
		BOLD   		   ("\033[1m",    "\033[0m"), // reset *all* attributes; reset bold not supported on BSD
		;

		protected String code, reset;

		Code(String code, String reset)
		{
			this.code = code;
			this.reset = reset;
		}

		public String wrap(String text)
		{
			if (text == null || text.length() == 0)
			{
				return "";
			}

			return code + text + reset;
		}
		@Override
		public String toString()
		{
			return code;
		}
	}
}