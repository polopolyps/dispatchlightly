package com.polopoly.ps.dispatchlightly.render;

import static com.polopoly.util.Require.require;

import java.io.Writer;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.context.Context;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.tools.view.context.ChainedContext;
import org.apache.velocity.tools.view.context.ViewContext;

import com.polopoly.ps.dispatchlightly.ChainedModelContext;
import com.polopoly.ps.dispatchlightly.Model;
import com.polopoly.ps.dispatchlightly.ModelContext;
import com.polopoly.ps.dispatchlightly.ModelFactory;
import com.polopoly.ps.dispatchlightly.exception.ModelFactoryException;
import com.polopoly.ps.dispatchlightly.exception.NoContextAvailableException;
import com.polopoly.ps.dispatchlightly.exception.NoModelClassAvailableException;
import com.polopoly.ps.dispatchlightly.exception.RenderException;

public class Renderer {
	public static final String MODEL_KEY_IN_CONTEXT = "lm";
	public static final String CONTEXT_KEY_IN_CONTEXT = "lightContext";

	private RenderRequest request;
	private Context parentVelocityContext;

	public Renderer(RenderRequest request) {
		this.request = require(request);
	}

	public void render(Writer writer) throws RenderException {
		ModelContext parentContext;

		try {
			parentContext = request.getParentContext();
		} catch (NoContextAvailableException e) {
			throw new RenderException(e);
		}

		ModelContext childContext = new ChainedModelContext(parentContext);

		request.populateChildContext(childContext);

		Model model = createModel(childContext);

		Template template;

		try {
			template = new TemplateResolver().resolveTemplate(request);
		} catch (NoModelClassAvailableException e) {
			throw new RenderException(e);
		}

		VelocityContext velocityContext;

		// The #render directive of Polopoly expects the context to be a
		// ChainedContext so do our best to retrieve one from the parent
		// context.

		if (parentVelocityContext instanceof InternalContextAdapter) {
			parentVelocityContext = ((InternalContextAdapter) parentVelocityContext).getInternalUserContext();
		}

		if (parentVelocityContext != null) {
			if (parentVelocityContext instanceof ViewContext) {
				ViewContext viewContext = (ViewContext) parentVelocityContext;

				velocityContext = new ChainedContext(parentVelocityContext, viewContext.getVelocityEngine(),
						viewContext.getRequest(), viewContext.getResponse(), viewContext.getServletContext());
			} else {
				velocityContext = new VelocityContext(parentVelocityContext);
			}
		} else {
			velocityContext = new VelocityContext();
		}

		velocityContext.put(MODEL_KEY_IN_CONTEXT, model);
		velocityContext.put(CONTEXT_KEY_IN_CONTEXT, childContext);

		try {
			template.merge(velocityContext, writer);
		} catch (Exception e) {
			String modelClass;
			try {
				modelClass = request.getModelClass().getName();
			} catch (NoModelClassAvailableException e1) {
				modelClass = "n/a";
			}

			throw new RenderException("While rendering " + template + " using model " + model + " of type "
					+ modelClass + " using context " + childContext + ": " + e.getMessage(), e);
		}
	}

	private Model createModel(ModelContext modelContext) throws RenderException {
		try {
			return new ModelFactory(request.getModelClass(), modelContext).create();
		} catch (ModelFactoryException e) {
			throw new RenderException(e.getMessage());
		} catch (NoModelClassAvailableException e) {
			throw new RenderException(e.getMessage());
		}
	}

	public void setParentVelocityContext(Context parentVelocityContext) {
		this.parentVelocityContext = parentVelocityContext;
	}
}
