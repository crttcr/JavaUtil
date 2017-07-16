package xivvic.util.io;

import java.lang.InstantiationException;

public class TerminalText {
	
	private TerminalText() throws InstantiationException { 
		throw new InstantiationException(); 
	}
	
	public static String red(String text) 			{ return format(Code.RED, text); }
	public static String blue(String text)			{ return format(Code.BLUE, text); }
	public static String green(String text) 		{ return format(Code.GREEN, text); }
	public static String darkGray(String text) 	{ return format(Code.DARK_GRAY, text); }
	public static String lightGray(String text) 	{ return format(Code.LIGHT_GRAY, text); }
	public static String bold(String text) 		{ return format(Code.BOLD, text); }
	public static String underline(String text) 	{ return format(Code.UNDERLINE, text); }
	
	private enum Code {
		RED    		("\033[31m", "\033[39m"),
		BLUE    	   ("\033[34m", "\033[39m"),
		GREEN		   ("\033[32m", "\033[39m"),
		LIGHT_GRAY	("\033[37m", "\033[39m"),
		DARK_GRAY   ("\033[38m", "\033[39m"),
		GRAY		   ("\033[37m", "\033[39m"),
		BOLD   		("\033[1m",   "\033[0m"),  // reset *all* attributes; reset bold not supported on BSD
		UNDERLINE 	("\033[4m",  "\033[24m");
		
		protected String code, reset;
		
		Code(String code, String reset) {
			this.code = code;
			this.reset = reset;
		}
		
		public String toString() {
			return code;
		}
	}
	
	private static String format(Code code, String text) {
		return code.code + text + code.reset;
	}
}	