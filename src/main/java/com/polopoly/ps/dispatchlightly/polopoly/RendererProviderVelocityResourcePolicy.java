package com.polopoly.ps.dispatchlightly.polopoly;

import java.io.InputStream;

import javax.servlet.ServletContext;

import com.polopoly.cm.client.CMException;
import com.polopoly.siteengine.mvc.view.renderer.RendererProviderVelocityFilePolicy;

public class RendererProviderVelocityResourcePolicy extends RendererProviderVelocityFilePolicy {
	public InputStream getVelocityTemplate(ServletContext context) throws CMException {
		String fileName = getFileName(this);

		InputStream result = getClass().getResourceAsStream(fileName);

		if (result == null) {
			result = super.getVelocityTemplate(context);
		}

		return result;
	}

}
