package com.polopoly.ps.dispatchlightly.render;

import static com.polopoly.ps.dispatchlightly.render.Renderer.CONTEXT_KEY_IN_CONTEXT;

import java.io.IOException;
import java.io.Writer;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.parser.node.Node;

import com.polopoly.ps.dispatchlightly.ModelContext;
import com.polopoly.ps.dispatchlightly.exception.CannotResolveTemplateException;
import com.polopoly.ps.dispatchlightly.exception.NoContextAvailableException;
import com.polopoly.ps.dispatchlightly.exception.RenderException;
import com.polopoly.util.CheckedCast;
import com.polopoly.util.CheckedClassCastException;

public class RenderLightlyDirective extends Directive {

	@Override
	public String getName() {
		return "renderlightly";
	}

	@Override
	public int getType() {
		return LINE;
	}

	@Override
	public boolean render(InternalContextAdapter context, Writer writer,
			Node node) throws IOException, ResourceNotFoundException,
			ParseErrorException, MethodInvocationException {
		if (node.jjtGetNumChildren() != 1) {
			throw new ParseErrorException("The " + getName()
					+ " directive accepts a single parameter which is a "
					+ RenderRequest.class.getSimpleName() + ". Got "
					+ node.jjtGetNumChildren() + " parameters.");
		}

		Object parameter = node.jjtGetChild(0).value(context);

		RenderRequest request;

		try {
			request = CheckedCast.cast(parameter, RenderRequest.class,
					"Argument " + node.jjtGetChild(0).literal() + " to "
							+ getName());
		} catch (CheckedClassCastException rr) {
			throw new ParseErrorException(rr.getMessage());
		}

		render(request, writer, context);

		return true;
	}

	protected static void render(RenderRequest request, Writer writer,
			InternalContextAdapter context) {
		try {
			request.getParentContext();
		} catch (NoContextAvailableException e1) {
			request = buildRequestFromContextlessRequest(context, request);
		}

		try {
			Renderer renderer = new Renderer(request);

			renderer.setParentVelocityContext(context);

			renderer.render(writer);
		} catch (CannotResolveTemplateException e) {
			throw new ResourceNotFoundException("While rendering " + request
					+ ": " + e.getMessage(), e);
		} catch (RenderException e) {
			throw new ResourceNotFoundException("While rendering " + request
					+ ": " + e.getMessage(), e);
		}
	}

	private static RenderRequest buildRequestFromContextlessRequest(
			final InternalContextAdapter context,
			RenderRequest incompleteRequest) {
		try {
			return new DelegatingRenderRequest(incompleteRequest) {
				ModelContext modelContext = CheckedCast.cast(
						context.get(CONTEXT_KEY_IN_CONTEXT),
						ModelContext.class, "$" + CONTEXT_KEY_IN_CONTEXT);

				@Override
				public ModelContext getParentContext()
						throws NoContextAvailableException {
					return modelContext;
				}
			};
		} catch (CheckedClassCastException e) {
			throw new ParseErrorException(
					"The current template evaluation is not within the light model so there is no context available. "
							+ "The render request "
							+ incompleteRequest
							+ " must therefore include a context: "
							+ e.getMessage());
		}
	}
}
