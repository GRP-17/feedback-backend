package com.group17.util;

public class CommonException extends RuntimeException {
	private int errorCode;
	private String errorMessage;

	public CommonException(String errorMessage, int errorCode) {
		super(errorMessage);
		this.errorMessage = errorMessage;
		this.errorCode = errorCode;
	}

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
