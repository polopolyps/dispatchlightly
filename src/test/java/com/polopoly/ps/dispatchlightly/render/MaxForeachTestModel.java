package com.polopoly.ps.dispatchlightly.render;

import java.util.ArrayList;

import com.polopoly.ps.dispatchlightly.Model;

public class MaxForeachTestModel implements Model {
	public Iterable<Integer> getIterable() {
		ArrayList<Integer> result = new ArrayList<Integer>();

		for (int i = 0; i < 100; i++) {
			result.add(i);
		}

		return result;
	}
}
