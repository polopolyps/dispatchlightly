package com.polopoly.ps.dispatchlightly.model;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import com.polopoly.cm.ContentId;
import com.polopoly.cm.client.CMException;
import com.polopoly.cm.servlet.URLBuilder;
import com.polopoly.util.Require;

public class LightURLBuilderWrapper implements LightURLBuilder {
	private static final Logger LOGGER = Logger.getLogger(LightURLBuilderWrapper.class.getName());

	protected URLBuilder urlBuilder;
	protected HttpServletRequest request;

	public LightURLBuilderWrapper(URLBuilder urlBuilder, HttpServletRequest request) {
		this.urlBuilder = Require.require(urlBuilder);
		this.request = Require.require(request);
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	@Override
	public String createFileUrl(ContentId cId, String filePath) {
		try {
			return urlBuilder.createFileUrl(cId, filePath, request);
		} catch (CMException e) {
			LOGGER.log(Level.WARNING, "While creating file URL for " + cId.getContentIdString()
					+ " and path " + filePath + ": " + e.getMessage(), e);

			return "";
		}
	}

	@Override
	public String createUrl(ContentId[] path, Map<String, String> parametersMap) {
		try {
			return urlBuilder.createUrl(path, parametersMap, request);
		} catch (CMException e) {
			LOGGER.log(
					Level.WARNING,
					"While creating URL for " + toString(path)
							+ (parametersMap.isEmpty() ? "" : " and parameters " + parametersMap) + ": "
							+ e.getMessage(), e);

			return "";
		}
	}

	private String toString(ContentId[] path) {
		StringBuffer result = new StringBuffer();

		for (ContentId id : path) {
			if (result.length() > 0) {
				result.append("/");
			}

			result.append(id.getContentIdString());
		}

		return result.toString();
	}

	@Override
	public String getStaticAsset(String assetUriRelativeToRoot) {
		return request.getContextPath() + "/" + assetUriRelativeToRoot;
	}

}