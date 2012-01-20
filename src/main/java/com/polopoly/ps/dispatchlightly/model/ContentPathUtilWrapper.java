package com.polopoly.ps.dispatchlightly.model;

import com.polopoly.ps.layout.element.util.ControllerUtil;
import com.polopoly.ps.layout.element.util.NoCurrentArticleException;
import com.polopoly.ps.layout.element.util.NoCurrentPageException;
import com.polopoly.util.Require;

public class ContentPathUtilWrapper implements ContentPathUtil {

	private ControllerUtil controllerUtil;

	public ContentPathUtilWrapper(ControllerUtil controllerUtil) {
		this.controllerUtil = Require.require(controllerUtil);
	}

	@Override
	public <S> S getSite(Class<S> siteClass) {
		return controllerUtil.getSite(siteClass);
	}

	@Override
	public <A> A getFirstArticle(Class<A> articleClass)
			throws NoCurrentArticleException {
		return controllerUtil.getArticle(articleClass);
	}

	@Override
	public <P> P getFirstPage(Class<P> pageClass) throws NoCurrentPageException {
		return controllerUtil.getPage(pageClass);
	}

}
