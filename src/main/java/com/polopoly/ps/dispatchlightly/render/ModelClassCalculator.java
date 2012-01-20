package com.polopoly.ps.dispatchlightly.render;

import com.polopoly.ps.dispatchlightly.Model;
import com.polopoly.ps.dispatchlightly.annotation.ModelClass;
import com.polopoly.ps.dispatchlightly.exception.NoModelClassAvailableException;

public class ModelClassCalculator {

	public Class<? extends Model> deriveModelFromAnnotation(Object object)
			throws NoModelClassAvailableException {
		Class<? extends Object> objectClass = object.getClass();

		ModelClass annotation = objectClass.getAnnotation(ModelClass.class);

		if (annotation != null) {
			return annotation.value();
		}

		Class<?> atClass = objectClass;

		while (!atClass.equals(Object.class)) {
			try {
				return scanInterfaces(objectClass);
			} catch (NoModelClassAvailableException e) {
				// try next.
				atClass = atClass.getSuperclass();
			}
		}

		throw new NoModelClassAvailableException("The class " + objectClass
				+ " was not annotated with @ModelClass. Could not calculate what model class to render "
				+ object + " with.");

	}

	private Class<? extends Model> scanInterfaces(Class<?> klass) throws NoModelClassAvailableException {
		for (Class<?> interfaceClass : klass.getInterfaces()) {
			ModelClass annotation = interfaceClass.getAnnotation(ModelClass.class);

			if (annotation != null) {
				return annotation.value();
			}
		}

		throw new NoModelClassAvailableException();
	}

}
