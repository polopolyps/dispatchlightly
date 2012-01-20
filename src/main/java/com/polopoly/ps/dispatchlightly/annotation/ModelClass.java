package com.polopoly.ps.dispatchlightly.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.polopoly.ps.dispatchlightly.Model;

/**
 * Can be used to point out the model class to use for a certain model object or
 * interface implemented by a model object. If an explicit model class is
 * provided when constructing the render request this annotation is not
 * required.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ModelClass {
	Class<? extends Model> value();
}
