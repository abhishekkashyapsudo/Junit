package com.nagp.ebroker.exception;

public class InvalidQuantityException extends Exception {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1226508902444186580L;
	public static final String EXCEPTION_MESSAGE = "Quantity should be greater than 0";
	public InvalidQuantityException() {
		super(EXCEPTION_MESSAGE);
	}

}
