package com.polopoly.ps.dispatchlightly.render;

import com.polopoly.ps.dispatchlightly.ChainedModelContext;
import com.polopoly.ps.dispatchlightly.Model;
import com.polopoly.ps.dispatchlightly.ModelContext;
import com.polopoly.ps.dispatchlightly.polopoly.RenderMode;
import com.polopoly.util.Require;

public class RenderLightlyDirectiveTestModel implements Model {
	private ModelContext context;
	private String string;

	public RenderLightlyDirectiveTestModel(ModelContext context, String string) {
		this.string = Require.require(string);
		this.context = Require.require(context);
	}

	public String getValue() {
		return string;
	}

	public RenderRequest getCompleteRequest() {
		ChainedModelContext childContext = new ChainedModelContext(context);

		childContext.put("complete");

		return new DefaultRenderRequest(RenderLightlyDirectiveTestModel.class, new RenderMode("nodispatch"),
				childContext);
	}

	public RenderRequest getIncompleteRequest() {
		return new DefaultRenderRequest(RenderLightlyDirectiveTestModel.class, new RenderMode("nodispatch"));
	}
}
