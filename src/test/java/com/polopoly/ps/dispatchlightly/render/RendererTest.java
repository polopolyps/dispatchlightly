package com.polopoly.ps.dispatchlightly.render;

import java.io.CharArrayWriter;

import org.junit.Assert;
import org.junit.Test;

import com.polopoly.ps.dispatchlightly.DefaultModelContext;
import com.polopoly.ps.dispatchlightly.Model;
import com.polopoly.ps.dispatchlightly.VelocityInitializer;
import com.polopoly.ps.dispatchlightly.polopoly.RenderMode;

public class RendererTest {

	@Test
	public void testRender() throws Exception {
		new VelocityInitializer().initialize();

		CharArrayWriter writer = new CharArrayWriter(1000);

		DefaultModelContext context = new DefaultModelContext();

		context.put(new Integer(1));

		RenderRequest request = new DefaultRenderRequest((Class<? extends Model>) RendererTestModel.class,
				RenderMode.DEFAULT, context);

		new Renderer(request).render(writer);

		writer.flush();

		Assert.assertEquals("object: 1", writer.toString());
	}
}
