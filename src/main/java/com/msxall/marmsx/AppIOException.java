package com.msxall.marmsx;

public class AppIOException extends RuntimeException {
	public AppIOException() {
		super();
	}

	public AppIOException(String message) {
		super(message);
	}

	public AppIOException(String message, Throwable cause) {
		super(message, cause);
	}

	public AppIOException(Throwable cause) {
		super(cause);
	}

	protected AppIOException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
