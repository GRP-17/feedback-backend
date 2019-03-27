package com.group17.exception;

import org.springframework.http.HttpStatus;

public class NoDashboardIdException extends CommonException {
	
	public enum Type { PARAMETER; }
	
	public NoDashboardIdException(Type type) {
		super("No dashboardId specified in " + type.toString(),
		      HttpStatus.BAD_REQUEST.value());
	}

}
