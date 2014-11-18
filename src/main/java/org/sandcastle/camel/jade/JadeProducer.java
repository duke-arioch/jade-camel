package org.sandcastle.camel.jade;

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
import jade.wrapper.AgentController;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JadeProducer extends DefaultProducer {

	private final JadeEndpoint endpoint;
	public static final Logger LOGGER = LoggerFactory
			.getLogger(JadeConsumer.class);

	public JadeProducer(JadeEndpoint endpoint) {
		super(endpoint);
		this.endpoint = endpoint;
	}

	@Override
	public void process(Exchange exchange) throws Exception {
		AgentController agent = endpoint.getAgent();
		if (agent != null) {
			agent.putO2AObject(exchange, false);
		} else {
			LOGGER.error("FFFFFDSSSSSSSSSSSSSSS");
			throw new NullPointerException("Target agent cannot be null");
		}
	}
}
