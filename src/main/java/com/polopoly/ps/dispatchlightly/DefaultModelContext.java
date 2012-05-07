package com.polopoly.ps.dispatchlightly;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.polopoly.ps.dispatchlightly.exception.NoSuchModelContextObjectException;

public class DefaultModelContext implements ModelContext {
	private static final Logger LOGGER = Logger
			.getLogger(DefaultModelContext.class.getName());

	private List<Object> contextObjects = new ArrayList<Object>();

	@SuppressWarnings("unchecked")
	public <T> T get(Class<T> klass) throws NoSuchModelContextObjectException {
		List<Object> candidates = new ArrayList<Object>();

		if (klass.isAssignableFrom(getClass())) {
			return (T) this;
		}

		for (Object o : contextObjects) {
			if (klass.isAssignableFrom(o.getClass())) {
				candidates.add(o);
			}
		}

		if (candidates.isEmpty()) {
			throw new NoSuchModelContextObjectException(klass);
		}

		for (int i = 0; i < candidates.size(); i++) {
			for (int j = i + 1; j < candidates.size(); j++) {

				Object a = candidates.get(i);
				Object b = candidates.get(j);

				if (a.getClass().equals(b.getClass())) {
					// tie.
					continue;
				}

				if (a.getClass().isAssignableFrom(b.getClass())) {
					candidates.remove(i);
				} else if (b.getClass().isAssignableFrom(a.getClass())) {
					candidates.remove(j);
				}
			}
		}

		return (T) candidates.get(0);
	}

	public void put(Object object) {
		if (object == null) {
			// creating exception to be able to trace who the culprit was.
			LOGGER.log(Level.WARNING,
					"Attempt to put a null object into the model.",
					new Exception());
			return;
		}

		// we add it first so in case of ambiguity the last added object
		// is the one returned by get.
		contextObjects.add(0, object);
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
