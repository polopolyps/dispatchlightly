package com.polopoly.ps.dispatchlightly.sz;

import com.polopoly.ps.dispatchlightly.model.LightURLBuilder;

public interface ImageDerivative {
	String getUrl(LightURLBuilder urlBuilder);

	int getWidth();

	int getHeight();
}
