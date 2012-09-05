package com.polopoly.ps.dispatchlightly.sz;

import com.polopoly.ps.dispatchlightly.annotation.ModelClass;
import com.polopoly.ps.dispatchlightly.url.LightURLBuilder;

@ModelClass(ButtonModel.class)
public interface Button {
	ButtonType getType();

	String getUrl(LightURLBuilder urlBuilder);
}
