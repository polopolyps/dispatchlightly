package com.polopoly.ps.dispatchlightly.sz;

import com.polopoly.ps.dispatchlightly.Model;
import com.polopoly.ps.dispatchlightly.ModelContext;
import com.polopoly.ps.dispatchlightly.model.LightURLBuilder;
import com.polopoly.ps.dispatchlightly.render.DefaultListRenderRequest;
import com.polopoly.ps.dispatchlightly.render.ListRenderRequest;

public class ArticlePlacementModel implements Model {
	private ModelContext context;
	private ArticlePlacement articlePlacement;
	private LightURLBuilder urlBuilder;

	public ArticlePlacementModel(ModelContext context, ArticlePlacement articlePlacement,
			LightURLBuilder urlBuilder) {
		this.context = context;
		this.articlePlacement = articlePlacement;
		this.urlBuilder = urlBuilder;
	}

	public String getPCssClass() {
		StringBuffer result = new StringBuffer();

		result.append("entry-summary");

		if (articlePlacement.getArticle().isOpinion()) {
			result.append(" hasIcon");
		}

		return result.toString();
	}

	public String getCssClass() {
		StringBuffer result = new StringBuffer();

		result.append("hentry");

		try {
			articlePlacement.getArticle().getImage();
		} catch (NoImageException e) {
			result.append(" noimage");
		}

		if (articlePlacement.getArticle().isOpinion()) {
			result.append(" Meinung");
		}

		return result.toString();
	}

	public ArticleModel getArticleModel() {
		return new ArticleModel(articlePlacement.getArticle(), context, urlBuilder);
	}

	public ArticlePlacement getArticlePlacement() {
		return articlePlacement;
	}

	public ListRenderRequest getButtonRequests() {
		return new DefaultListRenderRequest(context, articlePlacement.getButtons());
	}

	public ImageDerivative getImage() throws NoImageException {
		return articlePlacement.getArticle().getImage().getDerivative(ImageFormat.TOP_TEASER);
	}

	public ImageModel getImageModel() throws NoImageException {
		return new ImageModel(articlePlacement.getArticle().getImage(), ImageFormat.TOP_TEASER, urlBuilder);
	}
}
