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

import jade.wrapper.AgentContainer;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import org.apache.camel.util.URISupport;

public class JadeConfiguration {

	public static final String DEFAULT_FORWARD = "defaultForward";
	public static final String REPLY_TO = "replyTo";
	public static final String PERFORMATIVE = "performative";
	public static final String JADE_PROTOCOL = "jade";
	private AgentContainer container;
	private String agentName;
	private String defaultForward;
	private String replyTo;
	private String performative;

	public JadeConfiguration(AgentContainer container, URI uri)
			throws IllegalArgumentException, URISyntaxException {
		parseURI(uri);
		this.container = container;
	}

	public final void parseURI(URI uri) throws IllegalArgumentException,
			URISyntaxException {
		String protocol = uri.getScheme();
		if (!protocol.equalsIgnoreCase(JADE_PROTOCOL)) {
			throw new IllegalArgumentException("Unrecognized Jade protocol: "
					+ protocol + " for uri: " + uri);
		}
		setAgentName(uri.getHost());
		Map<String, Object> cacheSettings = URISupport.parseParameters(uri);
		if (cacheSettings.containsKey(DEFAULT_FORWARD)) {
			setDefaultForward((String) cacheSettings.get(DEFAULT_FORWARD));
		}
		if (cacheSettings.containsKey(PERFORMATIVE)) {
			setDefaultForward((String) cacheSettings.get(PERFORMATIVE));
		}
		if (cacheSettings.containsKey(REPLY_TO)) {
			setReplyTo((String) cacheSettings.get(REPLY_TO));
		}
	}

	public String getReplyTo() {
		return replyTo;
	}

	public void setReplyTo(String replyTo) {
		this.replyTo = replyTo;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public String getDefaultForward() {
		return defaultForward;
	}

	public void setDefaultForward(String defaultForward) {
		this.defaultForward = defaultForward;
	}

	public String getPerformative() {
		return performative;
	}

	public void setPerformative(String performative) {
		this.performative = performative;
	}

	public AgentContainer getContainer() {
		return container;
	}

	public void setContainer(AgentContainer container) {
		this.container = container;
	}

}
