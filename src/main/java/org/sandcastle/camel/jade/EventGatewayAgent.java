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
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventGatewayAgent extends Agent implements AgentMessageSource {

    private final transient Set<AgentMessageListener> listeners = new HashSet<>();
    public static final transient Logger LOGGER = LoggerFactory.getLogger(EventGatewayAgent.class);
	static final long serialVersionUID = 1L;

    public EventGatewayAgent() {
        super();
        setEnabledO2ACommunication(true, 0);
    }

    @Override
    public void setup() {
        super.setup();
        CyclicO2ABehaviourImpl behaviour = new CyclicO2ABehaviourImpl(this);
        addBehaviour(new CyclicEventBroadcastBehaviourImpl(this));
        addBehaviour(behaviour);
        setO2AManager(behaviour);
    }

    @Override
    public void addListener(AgentMessageListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(AgentMessageListener listener) {
        listeners.remove(listener);
    }

    @Override
    public Set<AgentMessageListener> getListeners() {
        return listeners;
    }
}
