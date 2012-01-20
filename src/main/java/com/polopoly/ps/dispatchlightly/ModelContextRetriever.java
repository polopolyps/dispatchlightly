package com.polopoly.ps.dispatchlightly;

import com.polopoly.ps.layout.element.util.ControllerUtil;
import com.polopoly.ps.layout.element.util.ModelGetException;
import com.polopoly.util.Require;

public class ModelContextRetriever {
	private static final String LIGHT_MODEL_CONTEXT_VARIABLE = "lightModelContext";
	private ModelContextFactory factory;

	public ModelContextRetriever() {
		factory = new ModelContextFactory();
	}

	ModelContextRetriever(ModelContextFactory factory) {
		this.factory = Require.require(factory);
	}

	public ModelContext getModelContext(ControllerUtil util) {
		try {
			return util.getLocal(LIGHT_MODEL_CONTEXT_VARIABLE, ModelContext.class);
		} catch (ModelGetException e) {
			ModelContext result;

			try {
				ModelContext parent = util.getStack(LIGHT_MODEL_CONTEXT_VARIABLE, ModelContext.class);

				result = new ChainedModelContext(parent);

				setChildContextSpecificFields(util, result);
			} catch (ModelGetException e2) {
				result = createModelContext(util);
			}

			util.setLocal(LIGHT_MODEL_CONTEXT_VARIABLE, result);

			return result;
		}
	}

	protected void setChildContextSpecificFields(ControllerUtil util, ModelContext context) {
		context.put(util.getPolicy());
		context.put(context);
		context.put(util);
	}

	protected ModelContext createModelContext(ControllerUtil util) {
		return factory.create(util);
	}

	public void setModelContext(ControllerUtil util, ChainedModelContext context) {
		util.setStack(LIGHT_MODEL_CONTEXT_VARIABLE, context);
	}

}
