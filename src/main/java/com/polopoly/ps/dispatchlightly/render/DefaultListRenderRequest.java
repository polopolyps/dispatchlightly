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

	public DefaultListRenderRequest(ModelContext context, Iterable<?> objectsToRender, RenderMode mode) {
		this(context, objectsToRender, null, mode);
	}

	public DefaultListRenderRequest(ModelContext context, Iterable<?> objectsToRender) {
		this(context, objectsToRender, null, RenderMode.DEFAULT);
	}

	public DefaultListRenderRequest(ModelContext context, Iterable<?> objectsToRender,
			Class<? extends Model> modelClass) {
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

	@Override
	public Iterator<RenderRequest> iterator() {
		return new ListPositionTransformingIterator<Object, RenderRequest>(objectsToRender.iterator()) {
			@Override
			protected RenderRequest transform(Object next, ListPosition position) {
				return new DefaultRenderRequest(modelClass, mode, context, next, position);
			}
		};
	}

}
