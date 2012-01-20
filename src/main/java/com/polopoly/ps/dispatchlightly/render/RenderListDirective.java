package com.polopoly.ps.dispatchlightly.render;

import java.io.IOException;
import java.io.Writer;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.parser.node.Node;

import com.polopoly.util.CheckedCast;
import com.polopoly.util.CheckedClassCastException;

public class RenderListDirective extends Directive {

	@Override
	public String getName() {
		return "renderlist";
	}

	@Override
	public int getType() {
		return LINE;
	}

	@Override
	public boolean render(InternalContextAdapter context, Writer writer, Node node) throws IOException,
			ResourceNotFoundException, ParseErrorException, MethodInvocationException {

		if (node.jjtGetNumChildren() != 1) {
			throw new ParseErrorException("The " + getName()
					+ " directive accepts a single parameter which is a "
					+ ListRenderRequest.class.getSimpleName() + ". Got " + node.jjtGetNumChildren()
					+ " parameters.");
		}

		Object parameter = node.jjtGetChild(0).value(context);

		ListRenderRequest listRequest;

		try {
			listRequest = CheckedCast.cast(parameter, ListRenderRequest.class, "Argument "
					+ node.jjtGetChild(0).literal() + " to " + getName());
		} catch (CheckedClassCastException rr) {
			throw new ParseErrorException(rr.getMessage());
		}

		for (RenderRequest request : listRequest) {
			RenderLightlyDirective.render(request, writer, context);
		}

		return true;
	}

}
