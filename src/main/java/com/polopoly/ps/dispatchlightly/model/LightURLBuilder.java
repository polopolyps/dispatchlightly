package com.polopoly.ps.dispatchlightly.model;

import java.util.Map;

import com.polopoly.cm.ContentId;
import com.polopoly.cm.servlet.URLBuilder;

/**
 * Simplification of the {@link URLBuilder} class to eliminate servlet
 * dependencies. If a URLBuilder has been put in the request by the request
 * preparator, an instance wrapping it will be put into the context model to
 * enable models to build URLs.
 */
public interface LightURLBuilder {

	/**
	 * Build a link to a file in a content object.
	 */
	String createFileUrl(ContentId cId, String filePath);

	/**
	 * Build a URL with the specified request parameters (GET).
	 */
	String createUrl(ContentId[] path, Map<String, String> parametersMap);

	/**
	 * Prepends the current context path to the URL.
	 */
	String getStaticAsset(String assetUriRelativeToRoot);
}