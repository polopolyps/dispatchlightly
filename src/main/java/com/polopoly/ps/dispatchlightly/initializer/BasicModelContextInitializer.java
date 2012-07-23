package com.polopoly.ps.dispatchlightly.initializer;

import static com.polopoly.cm.servlet.RequestPreparator.getURLBuilder;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import com.polopoly.cm.client.CMRuntimeException;
import com.polopoly.cm.policy.PolicyCMServer;
import com.polopoly.cm.servlet.URLBuilder;
import com.polopoly.ps.dispatchlightly.ModelContext;
import com.polopoly.ps.dispatchlightly.ModelContextInitializer;
import com.polopoly.ps.dispatchlightly.exception.NoSuchModelContextObjectException;
import com.polopoly.ps.dispatchlightly.model.ContentPathUtilWrapper;
import com.polopoly.ps.dispatchlightly.model.LightURLBuilder;
import com.polopoly.ps.dispatchlightly.model.LightURLBuilderWrapper;
import com.polopoly.ps.dispatchlightly.model.PreviewMode;
import com.polopoly.ps.dispatchlightly.polopoly.RenderMode;
import com.polopoly.ps.layout.element.util.ControllerUtil;
import com.polopoly.ps.layout.element.util.NoCurrentPageException;
import com.polopoly.ps.layout.element.util.NoPageScopeAvailableException;
import com.polopoly.siteengine.model.context.PageScope;
import com.polopoly.siteengine.model.request.ContentPath;
import com.polopoly.siteengine.model.request.PreviewScope;
import com.polopoly.siteengine.resource.Resources;
import com.polopoly.siteengine.structure.Page;
import com.polopoly.siteengine.structure.Site;
import com.polopoly.util.client.PolopolyContext;

/**
 * TODO Break up into a class per variable.
 */
public class BasicModelContextInitializer implements ModelContextInitializer {

	@Override
	public void initialize(ModelContext context, ControllerUtil util) {
		putUnlessExists(context, context, ModelContext.class);
		context.put(new ContentPathUtilWrapper(util));
		putUnlessExists(context, util.getPolopolyContext(), PolopolyContext.class);
		putUnlessExists(context, util.getPolicyCMServer(), PolicyCMServer.class);
		context.put(new RenderMode(util.getControllerContext().getMode()));
		context.put(util.getPolicy());
		context.put(util.getModel().getRequest());

		try {
			putUnlessExists(context, util.getPage(Page.class), Page.class);
		} catch (NoCurrentPageException e1) {
			// fine.
		}

		try {
			putUnlessExists(context, util.getSite(Site.class), Site.class);
		} catch (CMRuntimeException e1) {
			// fine.
		}

		putUnlessExists(context, util.getRequest(), HttpServletRequest.class);
		context.put(util);

		PageScope page = util.getModel().getContext().getPage();

		if (page != null) {
			context.put(page);
		}

		try {
			putUnlessExists(context, util.getContentPath(), ContentPath.class);
		} catch (NoPageScopeAvailableException e) {
			// ignore.
		}

		context.put(calculatePreviewModel(util.getModel().getRequest().getPreview()));

		try {
			Resources resources = util.getSite(Site.class).getResources();
			context.put(resources);
			putUnlessExists(context, resources.getLocale(), Locale.class);
		} catch (CMRuntimeException e) {
			// no site. ignore.
		}

		URLBuilder urlBuilder = getURLBuilder(util.getRequest());

		if (urlBuilder != null) {
			putUnlessExists(context, new LightURLBuilderWrapper(urlBuilder, util.getRequest()),
					LightURLBuilder.class);
		}
	}

	private <T> void putUnlessExists(ModelContext context, T object, Class<? extends T> klass) {
		try {
			context.get(klass);
		} catch (NoSuchModelContextObjectException e) {
			context.put(object);
		}

	}

	private PreviewMode calculatePreviewModel(PreviewScope preview) {
		if (preview == null) {
			return PreviewMode.NON_PREVIEW;
		} else if (preview.isInInteractivePreviewMode()) {
			return PreviewMode.INTERACTIVE_PREVIEW;
		} else if (preview.isInPreviewMode()) {
			return PreviewMode.PREVIEW;
		} else {
			return PreviewMode.NON_PREVIEW;
		}
	}
}
