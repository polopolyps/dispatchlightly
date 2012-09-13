package com.polopoly.ps.dispatchlightly.url;

import com.polopoly.cm.ContentId;
import com.polopoly.cm.alias.AliasRegistry;
import com.polopoly.cm.alias.AliasRegistryExternalId;
import com.polopoly.cm.alias.UrlPathSegmentFromPolicy;
import com.polopoly.cm.client.CMException;
import com.polopoly.cm.path.ExtendedTocBasedPathTranslator;
import com.polopoly.cm.path.impl.PathSegmentTranslator;
import com.polopoly.cm.path.impl.PathSegmentTranslatorChain;
import com.polopoly.cm.path.impl.PathSegmentTranslatorFactory;
import com.polopoly.cm.path.impl.PathSegmentTranslatorFriendlyUrl;
import com.polopoly.cm.path.impl.PathSegmentTranslatorFromPathSegmentProviderAndId;
import com.polopoly.cm.path.impl.PathSegmentTranslatorUrlPathSegment;
import com.polopoly.cm.policy.ContentIdFetcherSecurityParent;
import com.polopoly.cm.policy.PolicyCMServer;
import com.polopoly.pear.config.ConfigRead;

/**
 * This is essentially
 * com.polopoly.cm.path.impl.PathSegmentTranslatorFriendlyUrlFactory, only
 * modified to look for parents using security parent, not insert parent. It's
 * unclear why this would be required.
 */
public class EvenFriendlierPathSegmentTranslatorFactory implements PathSegmentTranslatorFactory {
	@Override
	public PathSegmentTranslator create(ConfigRead config, PolicyCMServer cmServer) {
		// changed: use the security parent rather than insert parent.
		ContentIdFetcherSecurityParent parentIdFetcher = new ContentIdFetcherSecurityParent();

		ExtendedTocBasedPathTranslator tocBasedPathTranslator = new ExtendedTocBasedPathTranslator();

		tocBasedPathTranslator.setPolicyCMServer(cmServer);
		tocBasedPathTranslator.init(config);

		AliasRegistry aliasRegistry = new AliasRegistryExternalId.Factory().create(cmServer);

		PathSegmentTranslatorFriendlyUrl pathSegmentTranslatorFriendlyUrl = new PathSegmentTranslatorFriendlyUrl(
				new PathSegmentTranslatorFromPathSegmentProviderAndId(cmServer), tocBasedPathTranslator) {

			@Override
			public String translate(ContentId contentId, ContentId parentId) throws CMException {
				return super.translate(contentId.getContentId(), parentId);
			}

		};

		PathSegmentTranslatorChain translatorChain = PathSegmentTranslatorChain.chain(new PathSegmentTranslator[] {
				new PathSegmentTranslatorUrlPathSegment(aliasRegistry, new UrlPathSegmentFromPolicy(parentIdFetcher),
						cmServer), pathSegmentTranslatorFriendlyUrl });

		return translatorChain;
	}
}
