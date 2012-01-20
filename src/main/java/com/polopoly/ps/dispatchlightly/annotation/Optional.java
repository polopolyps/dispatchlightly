package com.polopoly.ps.dispatchlightly.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a constructor parameter of a model as optional. It will still be set if
 * it is available in the model but if it is not, the constructor will be called
 * with the parameter set to null.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Optional {

}
