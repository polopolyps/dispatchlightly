package com.polopoly.ps.dispatchlightly;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.polopoly.ps.dispatchlightly.exception.NoSuchModelContextObjectException;

public class DefaultModelContext implements ModelContext {
	private static final Logger LOGGER = Logger.getLogger(DefaultModelContext.class.getName());

	private Set<Object> contextObjects = new HashSet<Object>();

	@SuppressWarnings("unchecked")
	public <T> T get(Class<T> klass) throws NoSuchModelContextObjectException {
		List<Object> candidates = new ArrayList<Object>();

		for (Object o : contextObjects) {
			if (klass.isAssignableFrom(o.getClass())) {
				candidates.add(o);
			}
		}

		if (candidates.isEmpty()) {
			throw new NoSuchModelContextObjectException(klass);
		}

		while (candidates.size() > 1) {
			Object a = candidates.get(0);
			Object b = candidates.get(1);

			if (a.getClass().isAssignableFrom(b.getClass())) {
				candidates.remove(0);
				continue;
			}

			else if (b.getClass().isAssignableFrom(a.getClass())) {
				candidates.remove(1);
				continue;
			} else {
				LOGGER.log(Level.WARNING,
						"There were a least two instances in the model compatble with the class " + klass
								+ ", one of type " + a.getClass() + " and one of type " + b.getClass()
								+ ". Randomly picking " + b.getClass() + ".");
				candidates.remove(0);
			}
		}

		return (T) candidates.get(0);
	}

	public void put(Object object) {
		if (object == null) {
			// creating exception to be able to trace who the culprit was.
			LOGGER.log(Level.WARNING, "Attempt to put a null object into the model.", new Exception());
			return;
		}

		contextObjects.add(object);
	}

	public String toString() {
		StringBuffer result = new StringBuffer(100);

		if (contextObjects.isEmpty()) {
			return "empty";
		}

		for (Object o : contextObjects) {
			if (result.length() > 0) {
				result.append(", ");
			}

			String string;

			if (o instanceof ModelContext) {
				// we can't do toString of contexts since they are likely to be
				// ourselves or something chaining to ourselves.
				string = "<a context>";
			} else {
				string = o.toString();
			}

			result.append(string + " (" + o.getClass().getSimpleName() + ")");
		}

		return result.toString();
	}
}
