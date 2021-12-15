package com.nagp.ebroker.exception;

public class InsufficientFundsException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1226508902444186580L;
	public static final String EXCEPTION_MESSAGE = "Insufficient Funds to buy this equity";
	public InsufficientFundsException() {
		super(EXCEPTION_MESSAGE);
	}
	
	

}
