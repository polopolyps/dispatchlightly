package com.polopoly.ps.dispatchlightly.render;

import com.polopoly.ps.dispatchlightly.Model;
import com.polopoly.util.Require;

public class RendererTestModel implements Model {
	private Integer integer;

	public RendererTestModel(Integer integer) {
		this.integer = Require.require(integer);
	}

	public int getValue() {
		return integer;
	}
}
