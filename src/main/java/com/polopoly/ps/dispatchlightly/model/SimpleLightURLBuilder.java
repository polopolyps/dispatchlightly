package com.polopoly.ps.dispatchlightly.model;

import static java.net.URLEncoder.encode;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.polopoly.cm.ContentId;

/**
 * A simple URL builder that builds plausible URLs. Only intended for testing
 * purposes.
 */
public class SimpleLightURLBuilder implements LightURLBuilder {
	private static final Logger LOGGER = Logger
			.getLogger(SimpleLightURLBuilder.class.getName());

	@Override
	public String createUrl(ContentId[] path, Map<String, String> parametersMap) {
		StringBuffer result = new StringBuffer(100);
		String stringResult = appendPathAndParameter(path, parametersMap,
				result);
		return stringResult;
	}

	private String appendPathAndParameter(ContentId[] path,
			Map<String, String> parametersMap, StringBuffer result) {
		for (ContentId contentId : path) {
			result.append("/");
			result.append(contentId.getContentId().getContentIdString());
		}
		result.append(buildParamString(parametersMap));
		return result.toString();
	}

	@Override
	public String createFileUrl(ContentId cId, String filePath) {
		return "/polopoly_fs/" + cId.getContentIdString() + "!" + filePath;
	}

	private String buildParamString(Map<String, String> parameters) {
		StringBuilder paramBuffer = new StringBuilder();
		Iterator<Map.Entry<String, String>> paramIter = parameters.entrySet()
				.iterator();
		if (paramIter.hasNext()) {
			Map.Entry<String, String> param = paramIter.next();
			paramBuffer.append('?');
			appendNameValue(paramBuffer, param);
		}
		while (paramIter.hasNext()) {
			Map.Entry<String, String> param = paramIter.next();
			paramBuffer.append('&');
			appendNameValue(paramBuffer, param);
		}
		return paramBuffer.toString();
	}

	private void appendNameValue(StringBuilder paramBuffer,
			Map.Entry<String, String> param) {
		try {
			paramBuffer.append(encode(param.getKey(), "UTF-8"));
			if (!param.getValue().isEmpty()) {
				paramBuffer.append('=');
				paramBuffer.append(encode(param.getValue(), "UTF-8"));
			}
		} catch (UnsupportedEncodingException e) {
			LOGGER.log(Level.WARNING, e.getMessage(), e);
			paramBuffer.append(param.getKey());

			if (!param.getValue().isEmpty()) {
				paramBuffer.append('=');
				paramBuffer.append(param.getValue());
			}
		}
	}

	@Override
	public String getStaticAsset(String assetUriRelativeToRoot) {
		return "../web/src/main/webapp/" + assetUriRelativeToRoot;
	}

}
