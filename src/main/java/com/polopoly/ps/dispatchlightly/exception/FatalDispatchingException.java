package com.polopoly.ps.dispatchlightly.exception;

public class FatalDispatchingException extends RuntimeException {

	public FatalDispatchingException() {
		super();
	}

	public FatalDispatchingException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public FatalDispatchingException(String arg0) {
		super(arg0);
	}

	public FatalDispatchingException(Throwable arg0) {
		super(arg0);
	}

}
