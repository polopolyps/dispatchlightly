package com.polopoly.ps.dispatchlightly.model;

/**
 * Will be put into the model scope for controllers to be able to determine
 * whether they are in preview or not.
 */
public enum PreviewMode {
	PREVIEW(true), NON_PREVIEW(true), INTERACTIVE_PREVIEW(true);

	private boolean isPreview;

	private PreviewMode(boolean isPreview) {
		this.isPreview = isPreview;
	}

	public boolean isPreview() {
		return isPreview;
	}
}
