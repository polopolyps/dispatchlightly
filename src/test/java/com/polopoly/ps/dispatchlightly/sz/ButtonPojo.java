package com.polopoly.ps.dispatchlightly.sz;

import com.polopoly.ps.dispatchlightly.url.LightURLBuilder;
import com.polopoly.util.Require;

public class ButtonPojo implements Button {
	private ButtonType type;
	private String url;

	public ButtonPojo(ButtonType type, String url) {
		super();
		this.type = type;
		this.url = url;
	}

	public ButtonType getType() {
		return type;
	}

	public void setType(ButtonType type) {
		this.type = Require.require(type);
	}

	public String getUrl(LightURLBuilder urlBuilder) {
		return url;
	}

	public void setUrl(String url) {
		this.url = Require.require(url);
	}

}
