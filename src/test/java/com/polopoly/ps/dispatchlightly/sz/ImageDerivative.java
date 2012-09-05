package com.polopoly.ps.dispatchlightly.sz;

import com.polopoly.ps.dispatchlightly.url.LightURLBuilder;

public interface ImageDerivative {
	String getUrl(LightURLBuilder urlBuilder);

	int getWidth();

	int getHeight();
}
