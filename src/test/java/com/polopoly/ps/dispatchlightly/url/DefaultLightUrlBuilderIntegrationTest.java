package com.polopoly.ps.dispatchlightly.url;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import com.polopoly.cm.ContentId;
import com.polopoly.cm.policy.Policy;
import com.polopoly.ps.test.AbstractIntegrationTest;

public class DefaultLightUrlBuilderIntegrationTest extends AbstractIntegrationTest {
	private Policy site;
	private Policy department;
	private Policy article;

	@Before
	public void setUp() throws Exception {
		site = testContent(".site");
		department = testContent(".department");
		article = testContent(".article");
	}

	@Test
	public void testArticle() {
		DefaultLightUrlBuilder urlBuilder = new DefaultLightUrlBuilder(getContext(), "http://www.a.com/");

		String url = urlBuilder.createUrl(
				new ContentId[] { site.getContentId(), department.getContentId(), article.getContentId() },
				Collections.<String, String> emptyMap());

		System.out.println(url);
	}
}
