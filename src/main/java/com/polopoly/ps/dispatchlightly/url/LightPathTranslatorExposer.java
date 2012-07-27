package com.polopoly.ps.dispatchlightly.url;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import com.polopoly.cm.ContentId;
import com.polopoly.cm.client.CMException;
import com.polopoly.cm.path.ExtendedTocBasedPathTranslator;
import com.polopoly.cm.path.impl.RecursivePathTranslator.ContentPathTranslatorExposer;
import com.polopoly.util.client.PolopolyContext;

class LightPathTranslatorExposer implements ContentPathTranslatorExposer {
	private final ExtendedTocBasedPathTranslator pathSegmentTranslator;
	private PolopolyContext context;

	LightPathTranslatorExposer(ExtendedTocBasedPathTranslator pathSegmentTranslator, PolopolyContext context) {
		this.pathSegmentTranslator = pathSegmentTranslator;
		this.context = context;
	}

	@Override
	public ContentId validateContentId(ContentId contentId) throws CMException {
		if (contentId.isSymbolicId()) {
			return context.getPolicyCMServer().translateSymbolicContentId(contentId);
		}

		return contentId.getContentId();
	}

	@Override
	public String encode(String string) throws CMException {
		try {
			return URLEncoder.encode(string, "UTF-8");
		} catch (UnsupportedEncodingException uee) {
			return string;
		}
	}

	public String decode(String string) throws CMException {
		try {
			return URLDecoder.decode(string, "UTF-8");
		} catch (UnsupportedEncodingException uee) {
			return string;
		}
	}

	@Override
	public String translate(ContentId child, ContentId parent) throws CMException {
		return pathSegmentTranslator.translate(child, parent);
	}

	@Override
	public ContentId translate(String pathSegment, ContentId parent) throws CMException {
		return pathSegmentTranslator.translate(pathSegment, parent);
	}
}