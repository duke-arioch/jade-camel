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

public class CyclicEventBroadcastBehaviourImpl extends CyclicBehaviour {

	static final long serialVersionUID = 1L;

	public CyclicEventBroadcastBehaviourImpl(Agent a) {
		super(a);
	}

	@Override
    public void action() {
        final ACLMessage msg = myAgent.receive();
        if ((msg != null)) {
            ((AgentMessageSource) myAgent).getListeners().stream().forEach((listener) -> {
                listener.handle(msg);
            });
        } else {
            block();
        }
    }
}
