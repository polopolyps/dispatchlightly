package com.polopoly.ps.dispatchlightly.sz;

import com.polopoly.ps.dispatchlightly.annotation.ModelClass;
import com.polopoly.ps.dispatchlightly.url.LightURLBuilder;

@ModelClass(ArticleModel.class)
public interface Article {
	Image getImage() throws NoImageException;

	String getTitle();

	String getIntro();

	String getAuthor();

	String getOverline();

	Iterable<Article> getOneliners();

	String getUrl(LightURLBuilder urlBuilder);

	boolean isOpinion();
}
