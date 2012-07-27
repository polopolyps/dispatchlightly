package com.polopoly.ps.dispatchlightly.sz;

import java.util.ArrayList;
import java.util.List;

import com.polopoly.ps.dispatchlightly.url.LightURLBuilder;
import com.polopoly.util.Require;

public class ArticlePojo implements Article {
	String title = "";
	String intro = "";
	String author = "";
	String authorPrefix = "Von";

	String overline = "";
	boolean opinion = false;
	String url;
	List<Article> oneliners = new ArrayList<Article>();
	Image image;

	public ArticlePojo(String title, String overline, String url) {
		super();
		this.title = title;
		this.overline = overline;
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = Require.require(title);
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = Require.require(intro);
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = Require.require(author);
	}

	public String getOverline() {
		return overline;
	}

	public void setOverline(String overline) {
		this.overline = Require.require(overline);
	}

	public String getUrl(LightURLBuilder urlBuilder) {
		return url;
	}

	public void setUrl(String url) {
		this.url = Require.require(url);
	}

	public void addOneliner(Article article) {
		oneliners.add(article);
	}

	public Iterable<Article> getOneliners() {
		return oneliners;
	}

	public boolean isOpinion() {
		return opinion;
	}

	public void setOpinion(boolean opinion) {
		this.opinion = Require.require(opinion);
	}

	public Image getImage() throws NoImageException {
		if (image == null) {
			throw new NoImageException();
		}

		return image;
	}

	public void setImage(Image image) {
		this.image = Require.require(image);
	}

	public String getAuthorPrefix() {
		return authorPrefix;
	}

	public void setAuthorPrefix(String authorPrefix) {
		this.authorPrefix = authorPrefix;
	}
}
