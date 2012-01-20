package com.polopoly.ps.dispatchlightly.render;

import com.polopoly.ps.dispatchlightly.Model;
import com.polopoly.ps.dispatchlightly.ModelContext;
import com.polopoly.ps.dispatchlightly.exception.NoContextAvailableException;
import com.polopoly.ps.dispatchlightly.exception.NoModelClassAvailableException;
import com.polopoly.ps.dispatchlightly.polopoly.RenderMode;

public interface RenderRequest {
	ModelContext getParentContext() throws NoContextAvailableException;

	Class<? extends Model> getModelClass() throws NoModelClassAvailableException;

	RenderMode getMode();

	void populateChildContext(ModelContext context);

}
