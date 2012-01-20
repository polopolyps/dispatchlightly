package com.polopoly.ps.dispatchlightly.render;

import java.io.CharArrayWriter;

import org.junit.Assert;
import org.junit.Test;

import com.polopoly.ps.dispatchlightly.DefaultModelContext;
import com.polopoly.ps.dispatchlightly.Model;
import com.polopoly.ps.dispatchlightly.VelocityInitializer;
import com.polopoly.ps.dispatchlightly.polopoly.RenderMode;

public class MaxForeachTest {

	@Test
	public void testRender() throws Exception {
		new VelocityInitializer().initialize();

		CharArrayWriter writer = new CharArrayWriter(1000);

		DefaultModelContext context = new DefaultModelContext();

		context.put(context);

		RenderRequest request = new DefaultRenderRequest(
				(Class<? extends Model>) MaxForeachTestModel.class,
				new RenderMode("www"), context);

		new Renderer(request).render(writer);

		writer.flush();

		System.out.println(writer.toString());

		Assert.assertTrue(writer.toString().indexOf("1") > 0);
		Assert.assertTrue(writer.toString().indexOf("99") < 0);
	}
}
