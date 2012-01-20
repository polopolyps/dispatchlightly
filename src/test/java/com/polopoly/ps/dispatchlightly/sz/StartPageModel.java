package com.polopoly.ps.dispatchlightly.sz;

import com.polopoly.ps.dispatchlightly.Model;
import com.polopoly.ps.dispatchlightly.ModelContext;
import com.polopoly.ps.dispatchlightly.model.LightURLBuilder;
import com.polopoly.ps.dispatchlightly.render.DefaultListRenderRequest;
import com.polopoly.ps.dispatchlightly.render.ListRenderRequest;

public class StartPageModel implements Model {

	private StartPage page;
	private ModelContext context;
	private LightURLBuilder urlBuilder;

	public StartPageModel(StartPage page, ModelContext context,
			LightURLBuilder urlBuilder) {
		this.page = page;
		this.context = context;
		this.urlBuilder = urlBuilder;
	}

	public ArticleModel getMainArticleModel() throws NoMainArticleException {
		return new ArticleModel(page.getMainArticle(), context, urlBuilder);
	}

	public StartPage getPage() {
		return page;
	}

	public ListRenderRequest getMainSlotArticlePlacementRequests() {
		return new DefaultListRenderRequest(context, page.getMainSlot());
	}
}
