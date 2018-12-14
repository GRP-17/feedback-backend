package com.group17.util;

/**
 * An exception which carries custom information needed in the handlers
 * used as a multipurpose exception can hold any error code and message
 */
public class CommonException extends RuntimeException {
	/** the HTTP status code of the error */
	private int errorCode;
	/** a useful message to help the request sender understand what the error was caused by */
	private String errorMessage;

	/**
	 * constructor
	 * @param errorMessage a message about the error to help the request sender
	 * @param errorCode a HTTP status code which defines the type of error it is
	 */
	public CommonException(String errorMessage, int errorCode) {
		super(errorMessage);
		this.errorMessage = errorMessage;
		this.errorCode = errorCode;
	}

	/**
	 * default constructor
	 */
	public CommonException() {
		super();
	}

	public int getErrorCode() {
		return errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
}
