package com.polopoly.ps.dispatchlightly;

import com.polopoly.ps.dispatchlightly.exception.NoSuchModelContextObjectException;

public class ChainedModelContext implements ModelContext {
	private DefaultModelContext localContext = new DefaultModelContext();
	private ModelContext parentContext;

	public ChainedModelContext(ModelContext parentContext) {
		this.parentContext = parentContext;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(Class<T> klass) throws NoSuchModelContextObjectException {
		if (klass.isAssignableFrom(getClass())) {
			return (T) this;
		}

		try {
			return localContext.get(klass);
		} catch (NoSuchModelContextObjectException e) {
			return parentContext.get(klass);
		}
	}

	@Override
	public void put(Object object) {
		localContext.put(object);
	}

	public String toString() {
		return localContext + " <parent: " + parentContext + ">";
	}
}
