package com.polopoly.ps.dispatchlightly;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ModelContextInitializerRetriever {
	private static final Logger LOGGER = Logger.getLogger(ModelContextInitializerRetriever.class.getName());

	private static List<ModelContextInitializer> cachedInitializers;

	public List<ModelContextInitializer> getInitializers() {
		if (cachedInitializers != null) {
			return cachedInitializers;
		}

		cachedInitializers = reallyGetInitializers();

		return cachedInitializers;
	}

	private ArrayList<ModelContextInitializer> reallyGetInitializers() {
		Iterator<ModelContextInitializer> initializerIterator = ServiceLoader.load(
				ModelContextInitializer.class).iterator();

		ArrayList<ModelContextInitializer> initializers = new ArrayList<ModelContextInitializer>();

		while (initializerIterator.hasNext()) {
			try {
				initializers.add(initializerIterator.next());
			} catch (Error e) {
				LOGGER.log(Level.WARNING, "While loading model context initializers: " + e.getMessage(), e);
			}
		}

		return initializers;
	}
}
