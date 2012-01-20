package com.polopoly.ps.dispatchlightly.exception;

import com.polopoly.util.Require;

public class NoSuchModelContextObjectException extends Exception {

	private Class<?> missingClass;

	public NoSuchModelContextObjectException(Class<?> missingClass) {
		super("No object of type " + missingClass + " in context.");

		this.missingClass = Require.require(missingClass);
	}

	public Class<?> getMissingClass() {
		return missingClass;
	}

}
