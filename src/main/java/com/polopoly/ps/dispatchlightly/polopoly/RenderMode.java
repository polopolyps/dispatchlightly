package com.polopoly.ps.dispatchlightly.polopoly;

import com.polopoly.util.Require;

public class RenderMode {
	public static final RenderMode DEFAULT = new RenderMode("www");

	private String mode;

	public RenderMode(String mode) {
		this.mode = Require.require(mode);
	}

	public String getMode() {
		return mode;
	}

	public String toString() {
		return mode;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof RenderMode && ((RenderMode) obj).getMode().equals(mode);
	}

	@Override
	public int hashCode() {
		return mode.hashCode();
	}

	public String getModeString() {
		return mode;
	}

}
