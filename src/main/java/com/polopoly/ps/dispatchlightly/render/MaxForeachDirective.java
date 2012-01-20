package com.polopoly.ps.dispatchlightly.render;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.directive.Foreach;
import org.apache.velocity.runtime.parser.node.Node;

/**
 * Extends the normal foreach to read a variable called foreachIterations that
 * describes the number of iterations after which the loop will break.
 */
public class MaxForeachDirective extends Foreach {
	private static final String MAX_ITERATIONS_VARIABLE_NAME = "foreachIterations";
	private static final Logger LOGGER = Logger
			.getLogger(MaxForeachDirective.class.getName());

	@Override
	public String getName() {
		return "foreach";
	}

	@Override
	public boolean render(InternalContextAdapter context, Writer writer,
			Node node) throws IOException, MethodInvocationException,
			ResourceNotFoundException, ParseErrorException {
		Object maxIterationsObject = context.get(MAX_ITERATIONS_VARIABLE_NAME);

		if (maxIterationsObject instanceof Integer) {
			setMaxIterations((Integer) maxIterationsObject);
		}

		boolean result = super.render(context, writer, node);

		return result;
	}

	private void setMaxIterations(Integer maxIterations) {
		// use introspection since the field is private.

		try {
			Field field = Foreach.class.getDeclaredField("maxNbrLoops");
			field.setAccessible(true);
			field.set(this, maxIterations);
		} catch (Exception e) {
			LOGGER.log(Level.WARNING,
					"While setting maxIterations: " + e.getMessage(), e);
		}
	}
}
