package com.polopoly.ps.dispatchlightly.sz;

import com.polopoly.ps.dispatchlightly.annotation.ModelClass;

@ModelClass(StartPageModel.class)
public interface StartPage {

	Article getMainArticle() throws NoMainArticleException;

	Iterable<ArticlePlacement> getMainSlot();

}
