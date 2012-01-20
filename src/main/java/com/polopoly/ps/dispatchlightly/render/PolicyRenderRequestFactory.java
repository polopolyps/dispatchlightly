package com.polopoly.ps.dispatchlightly.render;

import static com.polopoly.util.Require.require;

import com.polopoly.cm.policy.Policy;
import com.polopoly.ps.dispatchlightly.ModelContext;
import com.polopoly.ps.dispatchlightly.exception.RenderException;
import com.polopoly.ps.dispatchlightly.polopoly.RenderMode;
import com.polopoly.util.CheckedCast;
import com.polopoly.util.CheckedClassCastException;

public class PolicyRenderRequestFactory implements RenderRequestFactory {
	private ModelContext context;
	private RenderMode mode;

	public PolicyRenderRequestFactory() {
		this(null, RenderMode.DEFAULT);
	}

	public PolicyRenderRequestFactory(RenderMode mode) {
		this(null, mode);
	}

	public PolicyRenderRequestFactory(ModelContext context) {
		this.context = context;
		this.mode = RenderMode.DEFAULT;
	}

	public PolicyRenderRequestFactory(ModelContext context, RenderMode mode) {
		this.context = context;
		this.mode = require(mode);
	}

	@Override
	public RenderRequest createRenderRequest(Object object) throws RenderException {
		try {
			return new PolicyRenderRequest(CheckedCast.cast(object, Policy.class), mode, context);
		} catch (CheckedClassCastException e) {
			throw new RenderException(e);
		}
	}

}
