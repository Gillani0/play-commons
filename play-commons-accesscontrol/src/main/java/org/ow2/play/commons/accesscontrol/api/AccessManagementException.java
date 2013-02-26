package org.ow2.play.commons.accesscontrol.api;

public class AccessManagementException extends Exception {

	private static final long serialVersionUID = -6710177068878876856L;

	public AccessManagementException() {
		super();
	}

	public AccessManagementException(String message) {
		super(message);
	}

	public AccessManagementException(Throwable cause) {
		super(cause);
	}

	public AccessManagementException(String message, Throwable cause) {
		super(message, cause);
	}

	public AccessManagementException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
