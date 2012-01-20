package com.polopoly.ps.dispatchlightly.render;

import java.io.IOException;
import java.io.Writer;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import com.polopoly.util.Require;

/**
 * Wraps the Velocity template to include the file name.
 */
public class Template {
	private org.apache.velocity.Template template;
	private String resourceName;

	Template(org.apache.velocity.Template template, String resourceName) {
		this.template = Require.require(template);
		this.resourceName = Require.require(resourceName);
	}

	public String toString() {
		return resourceName;
	}

	public void merge(VelocityContext context, Writer writer) throws ResourceNotFoundException,
			ParseErrorException, MethodInvocationException, IOException {
		template.merge(context, writer);
	}
}
