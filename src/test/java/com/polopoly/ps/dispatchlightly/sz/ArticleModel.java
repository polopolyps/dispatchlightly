package com.polopoly.ps.dispatchlightly.sz;

import com.polopoly.ps.dispatchlightly.Model;
import com.polopoly.ps.dispatchlightly.ModelContext;
import com.polopoly.ps.dispatchlightly.polopoly.RenderMode;
import com.polopoly.ps.dispatchlightly.render.DefaultListRenderRequest;
import com.polopoly.ps.dispatchlightly.render.DefaultRenderRequest;
import com.polopoly.ps.dispatchlightly.render.ListRenderRequest;
import com.polopoly.ps.dispatchlightly.render.RenderRequest;
import com.polopoly.ps.dispatchlightly.url.LightURLBuilder;

public class ArticleModel implements Model {
	private Article article;
	private ModelContext context;
	private LightURLBuilder urlBuilder;

	public ArticleModel(Article article, ModelContext context, LightURLBuilder urlBuilder) {
		this.article = article;
		this.context = context;
		this.urlBuilder = urlBuilder;
	}

	public Article getArticle() {
		return article;
	}

	public String getCssClass() {
		if (article.isOpinion()) {
			return "Meinung";
		} else {
			return "plaintext";
		}
	}

	public RenderRequest getPanoramaImageRequest() throws NoImageException {
		Image image = article.getImage();

		return new DefaultRenderRequest(context, image, ImageFormat.PANORAMA);
	}

	public ListRenderRequest getOnelinerRequests() {
		return new DefaultListRenderRequest(context, getArticle().getOneliners(), new RenderMode("oneliner"));
	}

	public String getUrl() {
		return article.getUrl(urlBuilder);
	}
}
