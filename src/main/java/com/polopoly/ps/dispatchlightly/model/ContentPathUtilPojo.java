package com.polopoly.ps.dispatchlightly.model;

import java.util.Arrays;

import com.polopoly.cm.client.CMRuntimeException;
import com.polopoly.ps.layout.element.util.NoCurrentArticleException;
import com.polopoly.ps.layout.element.util.NoCurrentPageException;
import com.polopoly.util.CheckedCast;
import com.polopoly.util.CheckedClassCastException;
import com.polopoly.util.Require;

public class ContentPathUtilPojo implements ContentPathUtil {
	private Object[] contentPath;

	public ContentPathUtilPojo(Object... contentPath) {
		this.contentPath = Require.require(contentPath);
	}

	@Override
	public <S> S getSite(Class<S> siteClass) {
		for (Object o : contentPath) {
			try {
				return CheckedCast.cast(o, siteClass);
			} catch (CheckedClassCastException e) {
				continue;
			}
		}

		throw new CMRuntimeException("No site available.");
	}

	@Override
	public <A> A getFirstArticle(Class<A> articleClass)
			throws NoCurrentArticleException {
		for (Object o : contentPath) {
			try {
				return CheckedCast.cast(o, articleClass);
			} catch (CheckedClassCastException e) {
				continue;
			}
		}

		throw new NoCurrentArticleException("No article in content path "
				+ Arrays.toString(contentPath) + ".");
	}

	@Override
	public <P> P getFirstPage(Class<P> pageClass) throws NoCurrentPageException {
		for (Object o : contentPath) {
			try {
				return CheckedCast.cast(o, pageClass);
			} catch (CheckedClassCastException e) {
				continue;
			}
		}

		throw new NoCurrentPageException("No article in content path "
				+ Arrays.toString(contentPath) + ".");
	}

}
