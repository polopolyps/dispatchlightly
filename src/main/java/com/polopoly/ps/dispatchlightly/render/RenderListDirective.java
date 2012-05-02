package com.polopoly.ps.dispatchlightly.render;

import java.io.IOException;
import java.io.Writer;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.parser.node.Node;

import com.polopoly.ps.dispatchlightly.polopoly.RenderMode;
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

		if (node.jjtGetNumChildren() == 0 || node.jjtGetNumChildren() > 2) {
			throw invalidParameters(node, "Got " + node.jjtGetNumChildren() + " parameters.");
		}

		Object parameter = node.jjtGetChild(0).value(context);

		ListRenderRequest listRequest;

		try {
			listRequest = CheckedCast.cast(parameter, ListRenderRequest.class);
		} catch (CheckedClassCastException rr) {
			RenderMode mode;

			if (node.jjtGetNumChildren() == 2) {
				try {
					mode = new RenderMode(CheckedCast.cast(node.jjtGetChild(1).value(context), String.class));
				} catch (CheckedClassCastException e) {
					throw invalidParameters(node, "The second parameter was not a string (the render mode).");
				}
			} else {
				mode = RenderMode.DEFAULT;
			}

			try {
				Iterable<?> objectsToRender = CheckedCast.cast(parameter, Iterable.class);

				listRequest = new DefaultListRenderRequest(null, objectsToRender, mode);
			} catch (CheckedClassCastException e) {
				throw invalidParameters(node, "Argument " + node.jjtGetChild(0).literal()
						+ " should be either a ListRenderRequest or an Iterable.");
			}
		}

		for (RenderRequest request : listRequest) {
			RenderLightlyDirective.render(request, writer, context);
		}

		return true;
	}

	protected ParseErrorException invalidParameters(Node node, String error) {
		return new ParseErrorException("The " + getName() + " directive accepts either a single parameter which is a "
				+ ListRenderRequest.class.getSimpleName() + " or one or two parameters where the first is "
				+ "an Iterable of objects to render and the second (optional) is the render mode as a string. " + error);
	}

}
