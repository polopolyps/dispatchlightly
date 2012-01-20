package com.polopoly.ps.dispatchlightly;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.polopoly.ps.dispatchlightly.exception.NoSuchModelContextObjectException;

public class DefaultModelContextTest {
	private ModelContext modelContext;

	private class Parent {
	}

	private class Child {
	}

	@Before
	public void setUp() {
		modelContext = new DefaultModelContext();
	}

	@Test(expected = NoSuchModelContextObjectException.class)
	public void testGetNonExisting() throws Exception {
		modelContext.get(Parent.class);
	}

	@Test
	public void testConflictResolution() throws Exception {
		modelContext.put(new Parent());

		modelContext.put(new Child());

		Assert.assertEquals(Parent.class, modelContext.get(Parent.class).getClass());
	}

}
