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
import jade.core.ProfileImpl;
import jade.wrapper.AgentContainer;
import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

public class JadeComponentTest extends CamelTestSupport {

	@Produce(uri = "direct:x")
	protected ProducerTemplate producerTemplate;

	@Test
	public void testJade() throws Exception {
		MockEndpoint mock = getMockEndpoint("mock:result");
		mock.expectedMinimumMessageCount(1);
		producerTemplate.sendBodyAndHeader("the body",
				JadeConfiguration.JADE_REPLY_TO, "foo");
		assertMockEndpointsSatisfied();
		Exchange toCheck = mock.getExchanges().get(0);
		assertThat(toCheck.getIn().getBody(String.class),
				CoreMatchers.equalTo("the body"));
	}

	@Override
	protected RouteBuilder createRouteBuilder() throws Exception {
		RouteBuilder retval;
		retval = new RouteBuilder() {
			@Override
			public void configure() {
				try {
					from("jade:foo").to("mock:result");
					from("direct:x").to("jade:foo");
				} catch (Exception ex) {
					throw new RuntimeException(ex);
				}
			}
		};
		return retval;
	}

	@Override
	public int getShutdownTimeout() {
		return 2;
	}

	@Override
	public JndiRegistry createRegistry() throws Exception {
		JndiRegistry registry = super.createRegistry();
		AgentContainer container = jade.core.Runtime.instance()
				.createMainContainer(new ProfileImpl());
		EventGatewayAgent fooAgent = new EventGatewayAgent();
		container.start();
		container.acceptNewAgent("foo", fooAgent).start();
		JadeComponent jadeComponent = new JadeComponent();
		jadeComponent.setContainer(container);
		registry.bind("jade", jadeComponent);
		return registry;
	}

}
