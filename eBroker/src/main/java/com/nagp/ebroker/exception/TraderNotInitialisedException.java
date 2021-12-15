package com.nagp.ebroker.exception;

public class TraderNotInitialisedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1226508902444186580L;
	public static final String EXCEPTION_MESSAGE = "Trader not initialized in the database";
	public TraderNotInitialisedException() {
		super(EXCEPTION_MESSAGE);
	}
}
