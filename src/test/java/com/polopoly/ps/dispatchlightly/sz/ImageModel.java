package com.polopoly.ps.dispatchlightly.sz;

import com.polopoly.ps.dispatchlightly.Model;
import com.polopoly.ps.dispatchlightly.url.LightURLBuilder;

public class ImageModel implements Model {
	private ImageDerivative derivative;
	private LightURLBuilder urlBuilder;

	public ImageModel(Image image, ImageFormat imageFormat, LightURLBuilder urlBuilder) {
		this.urlBuilder = urlBuilder;

		derivative = image.getDerivative(imageFormat);
	}

	public int getWidth() {
		return derivative.getWidth();
	}

	public int getHeight() {
		return derivative.getHeight();
	}

	public String getUrl() {
		return derivative.getUrl(urlBuilder);
	}
}
