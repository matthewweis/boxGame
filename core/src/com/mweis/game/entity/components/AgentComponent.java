package com.mweis.game.entity.components;

import com.badlogic.ashley.core.Component;
import com.mweis.game.entity.agents.Agent;

public class AgentComponent<A extends Agent> implements Component {
	public A agent;
	
	public AgentComponent(A agent) {
		this.agent = agent;
	}
}
