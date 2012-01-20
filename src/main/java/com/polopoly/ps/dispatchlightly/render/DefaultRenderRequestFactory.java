package com.polopoly.ps.dispatchlightly.render;

import static com.polopoly.util.Require.require;

import com.polopoly.ps.dispatchlightly.Model;
import com.polopoly.ps.dispatchlightly.ModelContext;
import com.polopoly.ps.dispatchlightly.exception.RenderException;
import com.polopoly.ps.dispatchlightly.polopoly.RenderMode;

public class DefaultRenderRequestFactory implements RenderRequestFactory {
	private ModelContext context;
	private RenderMode mode;
	private Class<? extends Model> modelClass;

	public DefaultRenderRequestFactory(Class<? extends Model> modelClass) {
		this(modelClass, null, RenderMode.DEFAULT);
	}

	public DefaultRenderRequestFactory(Class<? extends Model> modelClass, RenderMode mode) {
		this(modelClass, null, mode);
	}

	public DefaultRenderRequestFactory(Class<? extends Model> modelClass, ModelContext context) {
		this(modelClass, context, RenderMode.DEFAULT);
	}

	public DefaultRenderRequestFactory(Class<? extends Model> modelClass, ModelContext context,
			RenderMode mode) {
		this.modelClass = require(modelClass);
		this.context = context;
		this.mode = require(mode);
	}

	@Override
	public RenderRequest createRenderRequest(final Object object) throws RenderException {
		return new DefaultRenderRequest(modelClass, mode, context, object);
	}
}
