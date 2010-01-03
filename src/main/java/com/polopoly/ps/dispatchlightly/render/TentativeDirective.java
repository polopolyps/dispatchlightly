package com.polopoly.ps.dispatchlightly.render;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.parser.node.ASTBlock;
import org.apache.velocity.runtime.parser.node.Node;

import com.polopoly.util.CheckedCast;
import com.polopoly.util.CheckedClassCastException;

/**
 * Will catch an exception of a specified class in the block it contains. This
 * is useful for optional sections when there is a getter throwing a checked
 * exception if some data is missing in which case the section should not be
 * displayed.
 */
public class TentativeDirective extends Directive {
	private static final Logger LOGGER = Logger.getLogger(TentativeDirective.class.getName());

	private static class DontRenderBodyException extends Exception {

	}

	private String exceptionClassName = "";

	@Override
	public String getName() {
		return "tentative";
	}

	@Override
	public int getType() {
		return BLOCK;
	}

	@Override
	public boolean render(InternalContextAdapter context, Writer writer, Node node) throws IOException,
			ResourceNotFoundException, ParseErrorException, MethodInvocationException {
		if (node.jjtGetNumChildren() < 2) {
			throw new ParseErrorException("The " + getName() + " directive accepts a single parameter which is a "
					+ RenderRequest.class.getSimpleName() + ". Got " + node.jjtGetNumChildren() + " parameters.");
		}

		try {
			StringWriter content = new StringWriter();

			for (int i = 0; i < node.jjtGetNumChildren(); i++) {
				Node child = node.jjtGetChild(i);

				if (child instanceof ASTBlock) {
					parseBodyBlock(context, child, content);
				} else if (child != null) {
					parseParameter(context, child, i);
				}
			}

			writer.write(content.getBuffer().toString());
		} catch (DontRenderBodyException e) {
			// the specified exception was thrown; nothing to render.
		}

		return true;
	}

	protected void parseBodyBlock(InternalContextAdapter context, Node child, StringWriter content) throws IOException,
			DontRenderBodyException {
		try {
			child.render(context, content);
		} catch (RuntimeException e) {
			if (isSpecifiedExceptionClass(e)) {
				throw new DontRenderBodyException();
			} else {
				throw e;
			}
		}
	}

	private boolean isSpecifiedExceptionClass(Exception e) {
		// not specifying an exception class means catch all exceptions
		if ("".equals(exceptionClassName)) {
			return true;
		}

		Throwable at = e;
		Throwable last = null;

		List<String> examined = new ArrayList<String>();

		do {
			if (at.getClass().getName().contains(exceptionClassName)) {
				return true;
			}

			examined.add(at.getClass().getName());

			last = at;
			at = at.getCause();
		} while (at != null && at != last);

		LOGGER.log(Level.INFO, "The exception " + e
				+ " thrown inside a tentative block did not match the specified exception class " + exceptionClassName
				+ " (checked if any of the following contained the exception class name: " + examined
				+ "). Re-throwing it.");

		return false;
	}

	protected void parseParameter(InternalContextAdapter context, Node child, int i) {
		if (i > 1) {
			throw new ParseErrorException("The " + getName()
					+ " directive accepts a single optional parameter. Remove the second parameter " + child.literal()
					+ ".");
		}

		try {
			exceptionClassName = CheckedCast.cast(child.value(context), String.class, child.literal());
		} catch (CheckedClassCastException e) {
			throw new ParseErrorException("The " + getName() + " directive accepts a single optional parameter which "
					+ "is a String specifying the exception class name: " + e.getMessage());
		}
	}

}
