package com.polopoly.ps.dispatchlightly;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Comparator;

import com.polopoly.ps.dispatchlightly.annotation.Optional;
import com.polopoly.ps.dispatchlightly.exception.FatalDispatchingException;
import com.polopoly.ps.dispatchlightly.exception.ModelFactoryException;
import com.polopoly.ps.dispatchlightly.exception.NoSuchModelContextObjectException;
import com.polopoly.util.Require;

public class ModelFactory {

	private Class<? extends Model> modelClass;
	private ModelContext modelContext;

	public ModelFactory(Class<? extends Model> modelClass,
			ModelContext modelContext) {
		this.modelClass = Require.require(modelClass);
		this.modelContext = Require.require(modelContext);
	}

	public Model create() throws ModelFactoryException {
		Constructor<?>[] constructors = modelClass.getConstructors();

		if (constructors.length == 0) {
			throw new ModelFactoryException("The model class "
					+ modelClass.getName() + " had no public constructors.");
		}

		Arrays.sort(constructors, new Comparator<Constructor<?>>() {
			@Override
			public int compare(Constructor<?> o1, Constructor<?> o2) {
				return new Integer(o1.getParameterTypes().length)
						.compareTo(new Integer(o2.getParameterTypes().length));
			}
		});

		NoSuchModelContextObjectException lastException = null;

		for (int i = constructors.length - 1; i >= 0; i--) {
			Constructor<?> longestConstructor = constructors[i];

			try {
				return (Model) construct(longestConstructor);
			} catch (NoSuchModelContextObjectException e) {
				lastException = e;

				continue;
			}
		}

		throw new ModelFactoryException(
				"The model context did not contain objects of all required types to construct a model of type "
						+ modelClass.getName()
						+ ". An example of a missing object for one of the constructors is "
						+ lastException.getMissingClass().getName()
						+ " (for the constructor "
						+ constructors[0]
						+ "). The context was: " + modelContext + ".");
	}

	private Object construct(Constructor<?> constructor)
			throws ModelFactoryException, NoSuchModelContextObjectException {
		Class<?>[] parameterTypes = constructor.getParameterTypes();

		Object[] arguments = new Object[parameterTypes.length];

		for (int i = 0; i < parameterTypes.length; i++) {
			try {
				arguments[i] = modelContext.get(parameterTypes[i]);
			} catch (NoSuchModelContextObjectException e) {
				if (!isParameterOptional(constructor, i)) {
					throw e;
				}
			}
		}

		try {
			return constructor.newInstance(arguments);
		} catch (Exception e) {
			throw new FatalDispatchingException(
					"While trying to instantiate model class " + modelClass
							+ " using constructor " + constructor
							+ " with arguments " + Arrays.toString(arguments)
							+ ": " + e.getMessage(), e);
		}
	}

	private boolean isParameterOptional(Constructor<?> constructor,
			int parameterIndex) {
		Annotation[] annotations = constructor.getParameterAnnotations()[parameterIndex];

		for (Annotation annotation : annotations) {
			if (annotation instanceof Optional) {
				return true;
			}
		}

		return false;
	}
}
