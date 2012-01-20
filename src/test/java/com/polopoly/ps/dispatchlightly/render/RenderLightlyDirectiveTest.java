package com.polopoly.ps.dispatchlightly.render;

import java.io.CharArrayWriter;

import org.junit.Assert;
import org.junit.Test;

import com.polopoly.ps.dispatchlightly.DefaultModelContext;
import com.polopoly.ps.dispatchlightly.Model;
import com.polopoly.ps.dispatchlightly.VelocityInitializer;
import com.polopoly.ps.dispatchlightly.polopoly.RenderMode;

public class RenderLightlyDirectiveTest {

	@Test
	public void testRender() throws Exception {
		new VelocityInitializer().initialize();

		CharArrayWriter writer = new CharArrayWriter(1000);

		DefaultModelContext context = new DefaultModelContext();

		context.put(context);
		context.put("parentValue");

		RenderRequest request = new DefaultRenderRequest(
				(Class<? extends Model>) RenderLightlyDirectiveTestModel.class, new RenderMode("dispatch"),
				context);

		new Renderer(request).render(writer);

		writer.flush();

		Assert.assertEquals("complete parentValue", writer.toString());
	}
}
