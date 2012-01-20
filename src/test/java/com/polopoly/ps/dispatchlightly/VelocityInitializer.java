package com.polopoly.ps.dispatchlightly;

import java.util.Properties;

import org.apache.velocity.app.Velocity;

import com.polopoly.ps.dispatchlightly.render.MaxForeachDirective;
import com.polopoly.ps.dispatchlightly.render.RenderLightlyDirective;
import com.polopoly.ps.dispatchlightly.render.RenderListDirective;
import com.polopoly.ps.dispatchlightly.render.TentativeDirective;

public class VelocityInitializer {

	public void initialize() throws Exception {
		Properties velocityProperties = new Properties();

		velocityProperties.put("resource.loader", "classpath");
		velocityProperties
				.put("classpath.resource.loader.class",
						"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		velocityProperties.put("runtime.log.invalid.references", "true");
		velocityProperties.put("userdirective",
				RenderLightlyDirective.class.getName() + ","
						+ TentativeDirective.class.getName() + ","
						+ RenderListDirective.class.getName() + ","
						+ MaxForeachDirective.class.getName());

		Velocity.init(velocityProperties);
	}

}
