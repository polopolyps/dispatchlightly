package com.polopoly.ps.dispatchlightly.polopoly;

import static com.polopoly.util.policy.Util.util;
import static java.util.logging.Level.WARNING;

import java.io.File;
import java.util.logging.Logger;

import com.polopoly.cm.client.OutputTemplate;
import com.polopoly.ps.dispatchlightly.Model;
import com.polopoly.ps.dispatchlightly.ModelContext;
import com.polopoly.ps.dispatchlightly.ModelContextRetriever;
import com.polopoly.ps.dispatchlightly.ModelFactory;
import com.polopoly.ps.dispatchlightly.exception.FatalDispatchingException;
import com.polopoly.ps.dispatchlightly.exception.ModelFactoryException;
import com.polopoly.ps.dispatchlightly.render.Renderer;
import com.polopoly.ps.layout.element.util.ControllerUtil;
import com.polopoly.ps.pcmd.util.CamelCase;
import com.polopoly.render.CacheInfo;
import com.polopoly.render.RenderRequest;
import com.polopoly.siteengine.dispatcher.ControllerContext;
import com.polopoly.siteengine.model.TopModel;
import com.polopoly.siteengine.mvc.RenderControllerBase;
import com.polopoly.util.client.PolopolyContext;
import com.polopoly.util.content.ContentUtil;
import com.polopoly.util.policy.Util;

public class AdaptingController extends RenderControllerBase {
	private static final Logger LOGGER = Logger
			.getLogger(AdaptingController.class.getName());

	private static final String MODEL_CLASS_COMPONENT_GROUP = "dispatchlightly/modelClass";
	private static final String MODEL_CLASS_COMPONENT = "value";

	@Override
	public void populateModelBeforeCacheKey(RenderRequest request, TopModel m,
			ControllerContext context) {
		super.populateModelBeforeCacheKey(request, m, context);

		setLightModel(request, m, context);
	}

	@Override
	public void populateModelAfterCacheKey(RenderRequest request, TopModel m,
			CacheInfo cacheInfo, ControllerContext context) {
		super.populateModelAfterCacheKey(request, m, cacheInfo, context);

		setLightModel(request, m, context);
	}

	private void setLightModel(RenderRequest request, TopModel m,
			ControllerContext context) {
		ControllerUtil util = new ControllerUtil(request, m, context);

		ModelContext modelContext = getModelContext(util);

		util.setLocal(Renderer.MODEL_KEY_IN_CONTEXT,
				createModel(util, modelContext));
		util.setLocal(Renderer.CONTEXT_KEY_IN_CONTEXT, modelContext);
	}

	protected Model createModel(ControllerUtil util, ModelContext context) {
		try {
			Class<Model> modelClass = getModelClass(getOutputTemplate(util),
					util.getPolopolyContext());

			return new ModelFactory(modelClass, context).create();
		} catch (ModelFactoryException e) {
			throw new FatalDispatchingException("While creating the model for "
					+ util + ": " + e.getMessage(), e);
		}
	}

	protected ModelContext getModelContext(ControllerUtil util) {
		ModelContext result = new ModelContextRetriever().getModelContext(util);

		populateContext(result, util);

		return result;
	}

	/**
	 * Intended for overriding.
	 */
	protected void populateContext(ModelContext context, ControllerUtil util) {
	}

	protected OutputTemplate getOutputTemplate(ControllerUtil util) {
		OutputTemplate result = util.getOutputTemplate();

		verifyNamingConvention(result, util);

		return result;
	}

	protected void verifyNamingConvention(OutputTemplate result,
			ControllerUtil controllerUtil) {
		String outputTemplateName = util(result,
				controllerUtil.getPolopolyContext()).getContentIdString();
		String mode = controllerUtil.getControllerContext().getMode();
		String inputTemplateName = util(
				util(controllerUtil.getPolicy()).getInputTemplate(),
				controllerUtil.getPolopolyContext()).getContentIdString();

		String modeSuffix;

		if (new RenderMode(mode).equals(RenderMode.DEFAULT)) {
			modeSuffix = "";
		} else {
			modeSuffix = "." + mode;
		}

		String expectedName = inputTemplateName + modeSuffix + ".ot";

		if (!outputTemplateName.equals(expectedName)) {
			LOGGER.log(WARNING, "Expected the output template "
					+ outputTemplateName + " to be called " + expectedName
					+ ".");
		}

		ContentUtil ot = util(controllerUtil.getOutputTemplate(),
				controllerUtil.getPolopolyContext());

		String velocityFile = ot
				.getComponent(
						"output/renderer/selected/defaultVelocityViewFileName",
						"value");

		if (velocityFile == null) {
			return;
		}

		velocityFile = new File(velocityFile).getName();

		String expectedVelocityFileName = new CamelCase()
				.fromCamelCase(getModelClass(
						controllerUtil.getOutputTemplate(),
						controllerUtil.getPolopolyContext()).getSimpleName())
				+ modeSuffix + ".vm";

		if (!velocityFile.equals(expectedVelocityFileName)) {
			LOGGER.log(
					WARNING,
					"Expected the velocity file "
							+ velocityFile
							+ " used in output template "
							+ outputTemplateName
							+ " to be called "
							+ expectedVelocityFileName
							+ ". If this naming convention is not followed, lightweight dispatching cannot be used.");
		}
	}

	@SuppressWarnings("unchecked")
	public static Class<Model> getModelClass(OutputTemplate outputTemplate,
			PolopolyContext context) {
		ContentUtil otUtil = Util.util(outputTemplate, context);

		String className = otUtil.getComponent(MODEL_CLASS_COMPONENT_GROUP,
				MODEL_CLASS_COMPONENT);

		if (className == null) {
			className = otUtil.getInputTemplate().getPolicyClassName();
		}

		try {
			Class<?> result = Class.forName(className);

			if (!Model.class.isAssignableFrom(result)) {
				throw new FatalDispatchingException("The model class "
						+ className + " specified in the output template "
						+ otUtil + " should implement the interface "
						+ Model.class.getName() + ".");
			}

			return (Class<Model>) result;
		} catch (ClassNotFoundException e) {
			throw new FatalDispatchingException("The model class " + className
					+ " specified in the output template " + otUtil
					+ " could not be loaded: " + e.getMessage(), e);
		}
	}

}
