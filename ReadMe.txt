Camel Component Project
====================
This Project creates an integration mechanism for using Jade Agent Framework with Camel integration patterns.

Included is a JadeComponent, consumer and producer along with a simplistic unit test. ASYNC only is supported. For sync, one could also hook up the Jade Gateway.

Usage pattern:

1. Set up your container and add whatever agents you want
2. Inject the container into the JadeComponent
3. Use your route as you would normally. Set a header on your exchange indicating the agent name you want to redirect to after the gateway. Default behaviour is to add a reply to to send back to the agent and then back out to camel.

Example:

from("jade:agent1").to("amqp:fromJade_queue");
from("amqp:toJade_queue").to("jade:agent2");

Jade-Camel uses O2A to send exchanges to Jade. 

Your feedback is appreciated!

