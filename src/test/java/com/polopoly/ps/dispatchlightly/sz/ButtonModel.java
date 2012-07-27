package com.polopoly.ps.dispatchlightly.sz;

import com.polopoly.ps.dispatchlightly.Model;
import com.polopoly.ps.dispatchlightly.model.ListPosition;
import com.polopoly.ps.dispatchlightly.url.LightURLBuilder;

public class ButtonModel implements Model {
	private Button button;
	private ListPosition listPosition;
	private LightURLBuilder urlBuilder;

	public ButtonModel(Button button, ListPosition listPosition, LightURLBuilder urlBuilder) {
		this.button = button;
		this.listPosition = listPosition;
		this.urlBuilder = urlBuilder;
	}

	public Button getButton() {
		return button;
	}

	public String getCssClass() {
		StringBuffer result = new StringBuffer(20);

		if (button.getType() == ButtonType.IMAGE) {
			result.append("ref-image");
		} else if (button.getType() == ButtonType.VIDEO) {
			result.append("ref-video");
		}

		if (listPosition == ListPosition.LAST) {
			result.append(" last");
		}

		return result.toString();
	}

	public String getLinkLabel() {
		if (button.getType() == ButtonType.IMAGE) {
			return "Bilder";
		} else if (button.getType() == ButtonType.VIDEO) {
			return "Video";
		} else {
			return "n/a";
		}
	}

	public String getUrl() {
		return button.getUrl(urlBuilder);
	}
}
