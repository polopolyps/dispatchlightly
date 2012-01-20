package com.polopoly.ps.dispatchlightly;

import com.polopoly.ps.dispatchlightly.exception.NoSuchModelContextObjectException;

public interface ModelContext {

	<T> T get(Class<T> klass) throws NoSuchModelContextObjectException;

	void put(Object object);

}
