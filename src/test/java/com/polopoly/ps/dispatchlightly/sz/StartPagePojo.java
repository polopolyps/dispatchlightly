package com.polopoly.ps.dispatchlightly.sz;

import java.util.ArrayList;
import java.util.List;

import com.polopoly.util.Require;

public class StartPagePojo implements StartPage {
	private List<ArticlePlacement> mainSlot = new ArrayList<ArticlePlacement>();

	private Article mainArticle;

	public void setMainArticle(Article mainArticle) {
		this.mainArticle = Require.require(mainArticle);
	}

	@Override
	public Article getMainArticle() throws NoMainArticleException {
		if (mainArticle == null) {
			throw new NoMainArticleException();
		}

		return mainArticle;
	}

	public void addInMainSlot(ArticlePlacement placement) {
		mainSlot.add(placement);
	}

	@Override
	public Iterable<ArticlePlacement> getMainSlot() {
		return mainSlot;
	}

}
