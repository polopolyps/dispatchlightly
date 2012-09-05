package com.polopoly.ps.dispatchlightly.sz;

import com.polopoly.ps.dispatchlightly.url.LightURLBuilder;

public class ImagePojo implements Image {
	private int originalWidth;
	private int originalHeight;
	private String url;

	ImagePojo(int originalWidth, int originalHeight, String url) {
		this.originalWidth = originalWidth;
		this.originalHeight = originalHeight;
		this.url = url;
	}

	@Override
	public ImageDerivative getDerivative(ImageFormat format) {
		return new ImageDerivative() {

			@Override
			public int getWidth() {
				return originalWidth;
			}

			@Override
			public String getUrl(LightURLBuilder urlBuilder) {
				return url;
			}

			@Override
			public int getHeight() {
				return originalHeight;
			}
		};
	}
}
