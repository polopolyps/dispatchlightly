package com.polopoly.ps.dispatchlightly.sz;

import java.util.ArrayList;
import java.util.List;

public class ArticlePlacementPojo implements ArticlePlacement {
	private Article article;
	private List<Button> buttons = new ArrayList<Button>();

	public ArticlePlacementPojo(Article article) {
		this.article = article;
	}

	public Article getArticle() {
		return article;
	}

	public void addButton(Button button) {
		buttons.add(button);
	}

	public List<Button> getButtons() {
		return buttons;
	}
}
