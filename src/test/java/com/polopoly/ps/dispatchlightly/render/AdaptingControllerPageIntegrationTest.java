package com.polopoly.ps.dispatchlightly.render;

import org.junit.Test;

import com.polopoly.ps.test.AbstractMockedPageIntegrationTest;
import com.polopoly.ps.test.MockedPageIntegrationTestCaseAdapter;

public class AdaptingControllerPageIntegrationTest extends AbstractMockedPageIntegrationTest {

	@Override
	protected String getRequestURI() throws Exception {
		return uri(testContent(".renderedObject"));
	}

	protected MockedPageIntegrationTestCaseAdapter createTestCaseAdapter() throws Exception {
		return new MockedPageIntegrationTestCaseAdapter(getRequestURI(), context) {

			@Override
			protected String getDefaultWebappProjectDir() {
				return ".";
			}

		};
	}

	@Test
	public void testSimpleRendering() throws Exception {
		assertHTMLContains(testContent(".renderedObject").getContent().getContentIdString());
	}

	@Test
	public void testLightDispatching() throws Exception {
		assertHTMLContains("dispatched");
	}

	@Test
	public void testHeavyDispatching() throws Exception {
		assertHTMLContains("conventional");
	}
}
