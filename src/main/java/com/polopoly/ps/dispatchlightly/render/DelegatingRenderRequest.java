package com.polopoly.ps.dispatchlightly.render;

import com.polopoly.ps.dispatchlightly.Model;
import com.polopoly.ps.dispatchlightly.ModelContext;
import com.polopoly.ps.dispatchlightly.exception.NoContextAvailableException;
import com.polopoly.ps.dispatchlightly.exception.NoModelClassAvailableException;
import com.polopoly.ps.dispatchlightly.polopoly.RenderMode;
import com.polopoly.util.Require;

public class DelegatingRenderRequest implements RenderRequest {
	private RenderRequest delegate;

	public DelegatingRenderRequest(RenderRequest delegate) {
		super();
		this.delegate = Require.require(delegate);
	}

	public ModelContext getParentContext() throws NoContextAvailableException {
		return delegate.getParentContext();
	}

	public Class<? extends Model> getModelClass()
			throws NoModelClassAvailableException {
		return delegate.getModelClass();
	}

	public RenderMode getMode() {
		return delegate.getMode();
	}

	public void populateChildContext(ModelContext context) {
		delegate.populateChildContext(context);
	}

	public String toString() {
		return delegate.toString();
	}
}
