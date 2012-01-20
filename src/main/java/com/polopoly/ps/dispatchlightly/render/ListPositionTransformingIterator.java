package com.polopoly.ps.dispatchlightly.render;

import java.util.Iterator;

import com.polopoly.ps.dispatchlightly.model.ListPosition;

public abstract class ListPositionTransformingIterator<T, U> implements Iterator<U> {
	private Iterator<T> delegate;
	private boolean first = true;

	public ListPositionTransformingIterator(Iterator<T> delegate) {
		this.delegate = delegate;
	}

	public boolean hasNext() {
		return delegate.hasNext();
	}

	public U next() {
		T next = delegate.next();
		ListPosition position;

		if (first) {
			position = ListPosition.FIRST;
			first = false;
		} else if (delegate.hasNext()) {
			position = ListPosition.MIDDLE;
		} else {
			position = ListPosition.LAST;
		}

		return transform(next, position);
	}

	protected abstract U transform(T next, ListPosition position);

	public void remove() {
		delegate.remove();
	}

	@Override
	public String toString() {
		return delegate.toString();
	}
}
