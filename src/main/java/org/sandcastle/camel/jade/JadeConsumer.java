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
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JadeConsumer extends DefaultConsumer
		implements
			AgentMessageListener {

	private final JadeEndpoint endpoint;
	public static final Logger LOGGER = LoggerFactory
			.getLogger(JadeConsumer.class);

	public JadeConsumer(JadeEndpoint endpoint, Processor processor) {
		super(endpoint, processor);
		this.endpoint = endpoint;
	}

	@Override
    public void handle(ACLMessage message) {
        final Exchange exchange = endpoint.createExchange();
        Message inMessage = exchange.getIn();
        Map<String, Object> headers = new HashMap<>();
        message.getAllUserDefinedParameters().entrySet().stream().forEach((entry) -> {
            headers.put(entry.getKey().toString(), entry.getValue());
        });
        inMessage.setHeaders(headers);
        inMessage.setBody(message.getContent());
        try {
            getProcessor().process(exchange);
        } catch (Exception ex) {
            exchange.setException(ex);
        } finally {
            if (null != exchange.getException()) {
                getExceptionHandler().handleException("Error processing exchange", exchange, exchange.getException());
            }
        }
    }
	@Override
	public void start() throws Exception {
		super.start();
		try {
			AgentController agent = endpoint.getAgent();
			agent.putO2AObject(new ListenerRequest(true, this), false);
		} catch (StaleProxyException ex) {
			getExceptionHandler().handleException(ex);
		}
	}

	@Override
	public void shutdown() throws Exception {
		super.start();
		try {
			AgentController agent = endpoint.getAgent();
			agent.putO2AObject(new ListenerRequest(false, this), false);
		} catch (StaleProxyException ex) {
			getExceptionHandler().handleException(ex);
		}
		super.shutdown();
	}

}
