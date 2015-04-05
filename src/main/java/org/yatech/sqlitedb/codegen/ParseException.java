package org.yatech.sqlitedb.codegen;

public class ParseException extends Exception {

	private static final long serialVersionUID = 228374008966549030L;
	
	public ParseException(String message) {
		super(message);
	}
	
	public ParseException(String message, Throwable cause) {
		super(message, cause);
	}

}
