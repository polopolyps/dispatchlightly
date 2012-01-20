package com.polopoly.ps.dispatchlightly.render;

import com.polopoly.ps.dispatchlightly.exception.RenderException;

public interface RenderRequestFactory {
	RenderRequest createRenderRequest(Object object) throws RenderException;
}
