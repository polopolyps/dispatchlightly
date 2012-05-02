package com.polopoly.ps.dispatchlightly.render;

import com.polopoly.cm.policy.Policy;
import com.polopoly.ps.dispatchlightly.Model;
import com.polopoly.ps.dispatchlightly.ModelContext;
import com.polopoly.ps.dispatchlightly.exception.NoContextAvailableException;
import com.polopoly.ps.dispatchlightly.exception.NoModelClassAvailableException;
import com.polopoly.ps.dispatchlightly.polopoly.RenderMode;
import com.polopoly.util.Require;
import com.polopoly.util.policy.Util;

public class DefaultRenderRequest implements RenderRequest {
	private ModelContext context;
	private Class<? extends Model> modelClass;
	private RenderMode mode;
	private Object[] addToChildModel;

	public DefaultRenderRequest(Class<? extends Model> modelClass, RenderMode mode, ModelContext context,
			Object... addToChildModel) {
		// optional. will be derived from annotation if not present
		this.modelClass = modelClass;
		this.mode = Require.require(mode);

		// optional
		this.context = context;
		this.addToChildModel = addToChildModel;
	}

	public DefaultRenderRequest(Class<? extends Model> modelClass, ModelContext context, Object... addToChildModel) {
		this(modelClass, RenderMode.DEFAULT, context, addToChildModel);
	}

	public DefaultRenderRequest(Class<? extends Model> modelClass, RenderMode mode) {
		this(modelClass, mode, null);
	}

	public DefaultRenderRequest(Class<? extends Model> modelClass) {
		this(modelClass, RenderMode.DEFAULT, null);
	}

	public DefaultRenderRequest(RenderMode mode, ModelContext context, Object primaryObject, Object... addToChildModel) {
		this(null, RenderMode.DEFAULT, context, append(primaryObject, addToChildModel));
	}

	public DefaultRenderRequest(ModelContext context, Object primaryObject, Object... addToChildModel) {
		this(null, RenderMode.DEFAULT, context, append(primaryObject, addToChildModel));
	}

	public DefaultRenderRequest(RenderMode mode, Object primaryObject, Object... addToChildModel) {
		this(null, mode, null, append(primaryObject, addToChildModel));
	}

	public DefaultRenderRequest(Object primaryObject, Object... addToChildModel) {
		this(null, RenderMode.DEFAULT, null, append(primaryObject, addToChildModel));
	}

	public Class<? extends Model> getModelClass() throws NoModelClassAvailableException {
		if (modelClass == null) {
			if (addToChildModel.length == 0) {
				throw new NoModelClassAvailableException("Neither model class nor object to render "
						+ "was specified for the render request.");
			}

			return new ModelClassCalculator().deriveModelFromAnnotation(addToChildModel[0]);
		}
		return modelClass;
	}

	public RenderMode getMode() {
		return mode;
	}

	public void populateChildContext(ModelContext context) {
		for (Object o : addToChildModel) {
			context.put(o);
		}

		// intended for overwriting
	}

	@Override
	public ModelContext getParentContext() throws NoContextAvailableException {
		if (context == null) {
			throw new NoContextAvailableException();
		}

		return context;
	}

	private static Object[] append(Object primaryObject, Object... addToChildModel) {
		Object[] result = new Object[addToChildModel.length + 1];
		result[0] = primaryObject;

		System.arraycopy(addToChildModel, 0, result, 1, addToChildModel.length);

		return result;
	}

	public String toString() {
		return toString(addToChildModel) + (modelClass != null ? " using " + modelClass.getSimpleName() : "")
				+ " in mode " + mode + (context != null ? " using context " + context : "");
	}

	private String toString(Object[] objects) {
		StringBuffer result = new StringBuffer();

		for (Object object : objects) {
			if (result.length() > 0) {
				result.append(", ");
			}

			if (object instanceof Policy) {
				result.append(Util.util((Policy) object));
			} else {
				result.append(object.toString());
			}
		}

		return result.toString();
	}
}
