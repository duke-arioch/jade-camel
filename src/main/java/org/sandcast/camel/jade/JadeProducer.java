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
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentController;
import java.util.Map;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
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
        JadeConfiguration config = endpoint.getConfig();
        Message inMessage = exchange.getIn();
        Map<String, Object> headers = inMessage.getHeaders();

        final int performative;
        if (headers.containsKey(JadeConfiguration.JADE_PERFORMATIVE)) {
            performative = ACLMessage.getInteger(inMessage.getHeader(JadeConfiguration.JADE_PERFORMATIVE, String.class));
        } else {
            performative = config.getPerformative() == null ? ACLMessage.REQUEST : ACLMessage.getInteger(config.getPerformative());
        }
        ACLMessage aclMessage = new ACLMessage(performative == ACLMessage.UNKNOWN ? ACLMessage.REQUEST : performative);
        aclMessage.setContent(inMessage.getBody(String.class));

        final String agentName;
        if (headers.containsKey(JadeConfiguration.JADE_AGENT_NAME)) {
            agentName = inMessage.getHeader(JadeConfiguration.JADE_AGENT_NAME, String.class);
        } else {
            agentName = config.getAgentName();
        }

        final String defaultForward;
        if (headers.containsKey(JadeConfiguration.JADE_DEFAULT_FORWARD)) {
            defaultForward = inMessage.getHeader(JadeConfiguration.JADE_DEFAULT_FORWARD, String.class);
        } else {
            defaultForward = config.getDefaultForward() == null ? agentName : config.getDefaultForward();
        }
        aclMessage.addReceiver(new AID(defaultForward, AID.ISLOCALNAME));

        final String replyTo;
        if (headers.containsKey(JadeConfiguration.JADE_REPLY_TO)) {
            replyTo = inMessage.getHeader(JadeConfiguration.JADE_REPLY_TO, String.class);
        } else {
            replyTo = config.getReplyTo() == null ? agentName : config.getReplyTo();
        }
        aclMessage.addReplyTo(new AID(replyTo, AID.ISLOCALNAME));

        inMessage.getHeaders().entrySet().stream().forEach((entry) -> {
            aclMessage.addUserDefinedParameter(entry.getKey(), (String) entry.getValue());
        });

        AgentController agent = config.getContainer().getAgent(agentName, AID.ISLOCALNAME);
        if (agent != null) {
            agent.putO2AObject(aclMessage, false);
        } else {
            throw new NullPointerException("Target agent cannot be null");
        }
    }
}
