package com.polopoly.ps.dispatchlightly.url;

import java.util.Collections;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.polopoly.cm.ContentId;
import com.polopoly.ps.test.AbstractIntegrationTest;
import com.polopoly.util.contentid.ContentIdUtil;

public class DefaultLightUrlBuilderIntegrationTest extends AbstractIntegrationTest {

	private ContentIdUtil site;
	private ContentIdUtil department;
	private ContentIdUtil article;

	@Before
	public void setUp() throws Exception {
		site = testContent(".site").getContentId().unversioned();
		department = testContent(".department").getContentId().unversioned();
		article = testContent(".article").getContentId().unversioned();
	}

	@Test
	public void testDifferentHostArticle() throws Exception {
		ContentId[] idPath = new ContentId[] { site, department, article };

		String url = getUrl(idPath, "http://www.a.com/");

		Assert.assertEquals("http://site:8080/department/article-" + article, url);

		url = getUrl(idPath, "http://site/");

		Assert.assertEquals("http://site:8080/department/article-" + article, url);
	}

	@Test
	public void testSameHostArticle() throws Exception {
		ContentId[] idPath = new ContentId[] { site, department, article };

		String url = getUrl(idPath, "http://site:8080/");

		Assert.assertEquals("/department/article-" + article, url);
	}

	private String getUrl(ContentId[] idPath, String requestHost) {
		DefaultLightUrlBuilder urlBuilder = new DefaultLightUrlBuilder(getContext(), requestHost);

		String url = urlBuilder.createUrl(idPath, Collections.<String, String> emptyMap());

		return url;
	}
}
