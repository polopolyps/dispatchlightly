package com.polopoly.ps.dispatchlightly;

import com.polopoly.ps.layout.element.util.ControllerUtil;

public class ModelContextFactory {

	public ModelContext create(ControllerUtil util) {
		ModelContext result = new DefaultModelContext();

		for (ModelContextInitializer initializer : new ModelContextInitializerRetriever().getInitializers()) {
			initializer.initialize(result, util);
		}

		return result;
	}

}
