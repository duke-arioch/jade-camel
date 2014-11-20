package org.sandcast.camel.jade;

/*
 * #%L
 * Jade Camel Component
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2014 Matthew Sandoz
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
import jade.wrapper.ControllerException;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JadeEndpoint extends DefaultEndpoint {

	private JadeConfiguration config;
	public static final Logger LOGGER = LoggerFactory
			.getLogger(JadeEndpoint.class);

	public JadeEndpoint(String uri, JadeComponent component)
			throws ControllerException, URISyntaxException {
		super(uri, component);
		config = new JadeConfiguration(component.getContainer(),
				URI.create(uri));
	}

	@Override
	public Producer createProducer() throws Exception {
		return new JadeProducer(this);
	}

	@Override
	public Consumer createConsumer(Processor processor) throws Exception {
		return new JadeConsumer(this, processor);
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public JadeConfiguration getConfig() {
		return config;
	}

	public void setConfig(JadeConfiguration config) {
		this.config = config;
	}

}
