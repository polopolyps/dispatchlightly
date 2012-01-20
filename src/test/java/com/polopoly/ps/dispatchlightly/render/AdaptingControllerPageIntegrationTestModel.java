package com.polopoly.ps.dispatchlightly.render;

import static com.polopoly.util.Require.require;
import static com.polopoly.util.policy.Util.util;

import com.polopoly.cm.policy.Policy;
import com.polopoly.ps.dispatchlightly.Model;
import com.polopoly.ps.dispatchlightly.ModelContext;
import com.polopoly.ps.dispatchlightly.polopoly.RenderMode;
import com.polopoly.util.exception.PolicyGetException;

public class AdaptingControllerPageIntegrationTestModel implements Model {

	private Policy policy;

	public AdaptingControllerPageIntegrationTestModel(Policy policy) {
		this.policy = require(policy);
	}

	public Policy getConventionalPolicy() throws PolicyGetException {
		return util(policy).getContent().getSecurityParentId().asPolicy();
	}

	public String getContentIdString() {
		return util(policy).getContent().getContentIdString();
	}

	public RenderRequest getRenderRequest() {
		return new DefaultRenderRequest(RenderLightlyDirectiveTestModel.class, new RenderMode("nodispatch")) {

			@Override
			public void populateChildContext(ModelContext context) {
				context.put("dispatched");
			}

		};
	}
}
