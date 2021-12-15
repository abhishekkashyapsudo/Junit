package com.nagp.ebroker.exception;

public class InsufficientEquityAvailableException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1226508902444186580L;
	public static final String EXCEPTION_MESSAGE = "Insufficient Equity available with the trader.";
	public InsufficientEquityAvailableException() {
		super(EXCEPTION_MESSAGE);
	}
}
