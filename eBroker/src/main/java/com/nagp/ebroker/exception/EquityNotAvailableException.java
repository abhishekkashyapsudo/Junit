package com.nagp.ebroker.exception;

public class EquityNotAvailableException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1226508902444186580L;
	public static final String EXCEPTION_MESSAGE = "Equity not available with the trader.";
	public EquityNotAvailableException() {
		super(EXCEPTION_MESSAGE);
	}
}
