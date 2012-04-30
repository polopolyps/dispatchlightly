package com.polopoly.ps.dispatchlightly.render;

import java.util.Iterator;

import com.polopoly.ps.dispatchlightly.Model;
import com.polopoly.ps.dispatchlightly.ModelContext;
import com.polopoly.ps.dispatchlightly.model.ListPosition;
import com.polopoly.ps.dispatchlightly.polopoly.RenderMode;
import com.polopoly.util.Require;

public class DefaultListRenderRequest implements ListRenderRequest, Iterable<RenderRequest> {

	private Iterable<Object> objectsToRender;
	private Class<? extends Model> modelClass;
	private RenderMode mode;
	private ModelContext context;
	private Object[] addToChildModel = new Object[1];

	public DefaultListRenderRequest(ModelContext context, Iterable<?> objectsToRender, RenderMode mode) {
		this(context, objectsToRender, null, mode);
	}

	public DefaultListRenderRequest(ModelContext context, Iterable<?> objectsToRender) {
		this(context, objectsToRender, null, RenderMode.DEFAULT);
	}

	public DefaultListRenderRequest(ModelContext context, Iterable<?> objectsToRender, Class<? extends Model> modelClass) {
		this(context, objectsToRender, modelClass, RenderMode.DEFAULT);
	}

	@SuppressWarnings("unchecked")
	public DefaultListRenderRequest(ModelContext context, Iterable<?> objectsToRender,
			Class<? extends Model> modelClass, RenderMode mode) {
		this.context = Require.require(context);
		this.objectsToRender = (Iterable<Object>) Require.require(objectsToRender);
		// optional.
		this.modelClass = modelClass;
		this.mode = Require.require(mode);
	}

	public void setAddToChildModel(Object[] addToChildModel) {
		this.addToChildModel = addToChildModel;
	}

	@Override
	public Iterator<RenderRequest> iterator() {
		return new ListPositionTransformingIterator<Object, RenderRequest>(objectsToRender.iterator()) {
			@Override
			protected RenderRequest transform(Object next, ListPosition position) {
				return createRenderRequest(next, position);
			}

		};
	}

	protected RenderRequest createRenderRequest(Object next, ListPosition position) {
		Object[] add = new Object[addToChildModel.length + 2];
		add[0] = next;
		add[1] = position;
		System.arraycopy(addToChildModel, 0, add, 2, addToChildModel.length);

		return new DefaultRenderRequest(modelClass, mode, context, add);
	}
}
