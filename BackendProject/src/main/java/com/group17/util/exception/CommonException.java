package com.group17.util.exception;

/**
 * An exception which carries custom information needed in the handlers.
 * <p>
 * This is used as a multipurpose exception and can hold any error code, 
 * along with a respective message.
 */
public class CommonException extends RuntimeException {
	/** The HTTP status code of the error. */
	private int errorCode;
	/** A useful message to help the request sender understand what caused the error */
	private String errorMessage;

	/**
	 * Construct a CommonException with a message and code.
	 * 
	 * @param errorMessage a message about the error to help the request sender
	 * @param errorCode a HTTP status code which defines the type of error it is
	 */
	public CommonException(String errorMessage, int errorCode) {
		super(errorMessage);
		this.errorMessage = errorMessage;
		this.errorCode = errorCode;
	}

	/**
	 * Construct a CommonException without a message nor code.
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
