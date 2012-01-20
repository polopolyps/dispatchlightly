package com.polopoly.ps.dispatchlightly.render;

import static com.polopoly.util.Require.require;
import static com.polopoly.util.policy.Util.util;

import com.polopoly.cm.client.CMException;
import com.polopoly.cm.client.OutputTemplate;
import com.polopoly.cm.policy.Policy;
import com.polopoly.ps.dispatchlightly.Model;
import com.polopoly.ps.dispatchlightly.ModelContext;
import com.polopoly.ps.dispatchlightly.exception.RenderException;
import com.polopoly.ps.dispatchlightly.polopoly.AdaptingController;
import com.polopoly.ps.dispatchlightly.polopoly.RenderMode;
import com.polopoly.util.Require;
import com.polopoly.util.policy.Util;

public class PolicyRenderRequest extends DefaultRenderRequest {

	private Policy policy;

	public PolicyRenderRequest(Policy policy, RenderMode mode,
			ModelContext context) throws RenderException {
		super(getModelClass(policy, mode), mode, context);

		this.policy = Require.require(policy);
	}

	public PolicyRenderRequest(Policy policy, ModelContext context)
			throws RenderException {
		super(getModelClass(policy, RenderMode.DEFAULT), RenderMode.DEFAULT,
				context);

		this.policy = Require.require(policy);
	}

	public PolicyRenderRequest(Policy policy, RenderMode mode)
			throws RenderException {
		super(getModelClass(policy, mode), mode);

		this.policy = Require.require(policy);
	}

	public PolicyRenderRequest(Policy policy) throws RenderException {
		super(getModelClass(policy, RenderMode.DEFAULT), RenderMode.DEFAULT);

		this.policy = policy;
	}

	private static Class<? extends Model> getModelClass(Policy policy,
			RenderMode mode) throws RenderException {
		OutputTemplate outputTemplate;

		try {
			outputTemplate = require(policy).getOutputTemplate(
					mode.getModeString());
		} catch (CMException e) {
			throw new RenderException(
					"Could not retrieve output template for mode " + mode
							+ " from " + util(policy) + ": " + e.getMessage(),
					e);
		}

		if (outputTemplate == null) {
			throw new RenderException(
					"Could not retrieve output template for mode " + mode
							+ " from " + util(policy) + ".");
		}

		return AdaptingController.getModelClass(outputTemplate, util(policy)
				.getContext());
	}

	@Override
	public final void populateChildContext(ModelContext context) {
		super.populateChildContext(context);

		context.put(policy);

		populatePolicyChildContext(context);
	}

	public void populatePolicyChildContext(ModelContext context) {
		// intended for overriding
	}

	public String toString() {
		return Util.util(policy) + " " + super.toString();
	}
}
