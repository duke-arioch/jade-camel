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
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class CyclicO2ABehaviourImpl extends CyclicBehaviour {

	public static final Logger LOGGER = LoggerFactory
			.getLogger(CyclicO2ABehaviourImpl.class);

	public CyclicO2ABehaviourImpl(Agent a) {
		super(a);
	}

	@Override
	public void action() {
		Object inval = myAgent.getO2AObject();
		if (inval != null) {
			if (inval instanceof ACLMessage) {
				myAgent.send((ACLMessage) inval);
			} else if (inval instanceof ListenerRequest
					&& myAgent instanceof AgentMessageSource) {
				ListenerRequest request = (ListenerRequest) inval;
				if (request.shouldRegister()) {
					((AgentMessageSource) myAgent).addListener(request
							.getListener());
				} else {
					((AgentMessageSource) myAgent).removeListener(request
							.getListener());
				}
			}
		} else {
			block();
		}
	}
}
