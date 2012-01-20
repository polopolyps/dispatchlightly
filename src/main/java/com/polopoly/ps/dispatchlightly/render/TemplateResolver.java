package com.polopoly.ps.dispatchlightly.render;

import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import com.polopoly.ps.dispatchlightly.exception.CannotResolveTemplateException;
import com.polopoly.ps.dispatchlightly.exception.NoModelClassAvailableException;
import com.polopoly.ps.dispatchlightly.polopoly.RenderMode;
import com.polopoly.ps.pcmd.util.CamelCase;

public class TemplateResolver {

	public Template resolveTemplate(RenderRequest request)
			throws CannotResolveTemplateException,
			NoModelClassAvailableException {
		String modeSuffix;

		if (request.getMode().equals(RenderMode.DEFAULT)) {
			modeSuffix = "";
		} else {
			modeSuffix = "." + request.getMode();
		}

		String velocityFile = new CamelCase().fromCamelCase(request
				.getModelClass().getSimpleName()) + modeSuffix + ".vm";

		// Get velocity template
		org.apache.velocity.Template velocityTemplate;

		try {
			velocityTemplate = Velocity.getTemplate(velocityFile);

			return new Template(velocityTemplate, velocityFile);
		} catch (ResourceNotFoundException e) {
			throw new CannotResolveTemplateException(
					"Could not find the Velocity template for model "
							+ request.getModelClass() + " and mode "
							+ request.getMode() + ". Assuming it is called "
							+ velocityFile + ".", e);
		} catch (ParseErrorException e) {
			throw new CannotResolveTemplateException("While fetching template "
					+ velocityFile + ": " + e.getMessage(), e);
		} catch (Exception e) {
			throw new CannotResolveTemplateException("While fetching template "
					+ velocityFile + ": " + e.getMessage(), e);
		}
	}
}
