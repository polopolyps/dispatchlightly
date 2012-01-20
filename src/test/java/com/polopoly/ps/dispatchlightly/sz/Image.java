package com.polopoly.ps.dispatchlightly.sz;

import com.polopoly.ps.dispatchlightly.annotation.ModelClass;

@ModelClass(ImageModel.class)
public interface Image {
	ImageDerivative getDerivative(ImageFormat format);
}
