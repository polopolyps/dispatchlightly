package com.polopoly.ps.dispatchlightly.sz;

import java.util.List;

import com.polopoly.ps.dispatchlightly.annotation.ModelClass;

@ModelClass(ArticlePlacementModel.class)
public interface ArticlePlacement {
	Article getArticle();

	List<Button> getButtons();
}
