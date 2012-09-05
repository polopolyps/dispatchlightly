package com.polopoly.ps.dispatchlightly.url;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.polopoly.cm.ContentId;
import com.polopoly.cm.client.CMException;
import com.polopoly.cm.path.impl.RecursivePathTranslator;
import com.polopoly.cm.path.impl.SiteEngineContentPathTranslator;
import com.polopoly.cm.policy.Policy;
import com.polopoly.pear.config.impl.ConfigReadMap;
import com.polopoly.siteengine.structure.Alias;
import com.polopoly.siteengine.structure.Site;
import com.polopoly.util.client.PolopolyContext;
import com.polopoly.util.exception.PolicyGetException;
import com.polopoly.util.policy.Util;

public class DefaultLightUrlBuilder implements LightURLBuilder {
	private static final Logger LOGGER = Logger.getLogger(DefaultLightUrlBuilder.class.getName());

	public class CannotCreateUrlException extends Exception {
		public CannotCreateUrlException(String message) {
			super(message);
		}
	}

	private PolopolyContext context;
	private LightURLBuilder fallback = new SimpleLightURLBuilder("/cmlink");

	private SiteEngineContentPathTranslator pathSegmentTranslator;

	private RecursivePathTranslator pathTranslator;

	private boolean forceAbsoluteUrls = false;
	private String requestHost;

	/**
	 * @param requestHost
	 *            In the same format as the domain aliases, i.e. starting with
	 *            "http://" and possibly including port.
	 */
	public DefaultLightUrlBuilder(PolopolyContext context, String requestHost) {
		this.context = context;

		if (!requestHost.startsWith("http")) {
			requestHost = "http://" + requestHost;
		}

		if (requestHost.endsWith("/")) {
			requestHost = requestHost.substring(0, requestHost.length() - 1);
		}

		this.requestHost = requestHost;

		pathSegmentTranslator = createPathSegmentTranslator();

		pathTranslator = createPathTranslator(pathSegmentTranslator);
	}

	@Override
	public String createFileUrl(ContentId cId, String filePath) {
		return (forceAbsoluteUrls ? requestHost : "") + "/polopoly_fs/" + cId.getContentIdString() + "!" + filePath;
	}

	@Override
	public String createUrl(ContentId[] idPath, Map<String, String> parametersMap) {
		try {
			Policy[] path = new Policy[idPath.length];

			int i = 0;

			int siteIndex = -1;

			for (ContentId id : idPath) {
				Policy policy = context.getPolicy(id);

				if (policy instanceof Site) {
					if (siteIndex != -1) {
						throw new CannotCreateUrlException("Found several sites in path: " + policy + " and "
								+ path[siteIndex] + ".");
					}

					siteIndex = i;
				}

				path[i] = policy;

				i++;
			}

			if (siteIndex < 0) {
				return fallback.createUrl(idPath, parametersMap);
			}

			if (siteIndex > 0) {
				throw new CannotCreateUrlException("The site was not the first object in the path.");
			}

			Site site = (Site) path[siteIndex];

			String host = "";

			Alias mainAlias;

			try {
				mainAlias = site.getMainAlias();
			} catch (CMException e) {
				LOGGER.log(Level.WARNING,
						"Getting main alias from " + Util.util((Policy) site) + ": " + e.getMessage(), e);
				mainAlias = null;
			}

			if (mainAlias != null) {
				host = mainAlias.getDomain();

				if (!forceAbsoluteUrls && addPort(host).equals(addPort(requestHost))) {
					host = "";
				}

				if (host.endsWith("/")) {
					host = host.substring(0, host.length() - 1);
				}

				return host + pathTranslator.createPath(idPath, 1, new EverythingUnsupportedHttpServletRequest());
			} else {
				return createFallbackPath(idPath);
			}
		} catch (PolicyGetException e) {
			LOGGER.log(Level.WARNING, "Creating URL for " + toString(idPath) + ": " + e.getMessage(), e);
			return fallback.createUrl(idPath, parametersMap);
		} catch (CMException e) {
			LOGGER.log(Level.WARNING, "Creating URL for " + toString(idPath) + ": " + e.getMessage(), e);
			return fallback.createUrl(idPath, parametersMap);
		} catch (CannotCreateUrlException e) {
			LOGGER.log(Level.WARNING, "Creating URL for " + toString(idPath) + ": " + e.getMessage(), e);

			return createFallbackPath(idPath);
		}
	}

	private String addPort(String host) {
		if (host.contains(":")) {
			return host;
		} else {
			return host + ":80";
		}
	}

	private String toString(ContentId[] idPath) {
		StringBuffer result = new StringBuffer();

		for (ContentId id : idPath) {
			if (result.length() > 0) {
				result.append("/");
			}

			result.append(id.getContentIdString());
		}

		return result.toString();
	}

	private String createFallbackPath(ContentId[] idPath) {
		try {
			return "/cmlink" + pathTranslator.createPath(idPath, 0, new EverythingUnsupportedHttpServletRequest());
		} catch (CMException e) {
			LOGGER.log(Level.WARNING, "Creating URL for " + toString(idPath) + ": " + e.getMessage(), e);

			return fallback.createUrl(idPath, Collections.<String, String> emptyMap());
		}
	}

	private RecursivePathTranslator createPathTranslator(final SiteEngineContentPathTranslator pathSegmentTranslator) {
		return new RecursivePathTranslator(context.getPolicyCMServer(), new LightPathTranslatorExposer(
				pathSegmentTranslator, context));
	}

	private SiteEngineContentPathTranslator createPathSegmentTranslator() {
		HashMap<String, String> configMap = new HashMap<String, String>();

		ConfigReadMap config = new ConfigReadMap(configMap);

		configMap.put("rootContentId", "p.siteengine.Sites.d");
		configMap.put("contentListNames", "polopoly.Department,default,pages,articles,feeds,landingPages");
		configMap.put("contentNameFallback", "false");
		configMap.put("ignoreCaseInPathSegment", "true");
		configMap.put("pathSegmentTranslatorFactory",
				com.polopoly.ps.dispatchlightly.url.EvenFriendlierPathSegmentTranslatorFactory.class.getName());
		pathSegmentTranslator = new SiteEngineContentPathTranslator();

		pathSegmentTranslator.setPolicyCMServer(context.getPolicyCMServer());
		pathSegmentTranslator.init(config);

		return pathSegmentTranslator;
	}

	@Override
	public String getStaticAsset(String assetUriRelativeToRoot) {
		if (assetUriRelativeToRoot.startsWith("/")) {
			assetUriRelativeToRoot = assetUriRelativeToRoot.substring(1);
		}

		return (forceAbsoluteUrls ? requestHost : "") + assetUriRelativeToRoot;
	}

	public boolean isForceAbsoluteUrls() {
		return forceAbsoluteUrls;
	}

	public void setForceAbsoluteUrls(boolean forceAbsoluteUrls) {
		this.forceAbsoluteUrls = forceAbsoluteUrls;
	}

}
