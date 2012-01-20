package com.polopoly.ps.dispatchlightly.model;

import com.polopoly.ps.layout.element.util.NoCurrentArticleException;
import com.polopoly.ps.layout.element.util.NoCurrentPageException;

public interface ContentPathUtil {
	<S> S getSite(Class<S> siteClass);

	<A> A getFirstArticle(Class<A> articleClass)
			throws NoCurrentArticleException;

	<P> P getFirstPage(Class<P> pageClass) throws NoCurrentPageException;
}
