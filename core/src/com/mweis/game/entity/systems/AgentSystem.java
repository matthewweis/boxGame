package com.mweis.game.entity.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.mweis.game.entity.components.AgentComponent;
import com.mweis.game.util.Mappers;

public class AgentSystem extends IteratingSystem {
	
	public AgentSystem() {
		super(Family.all(AgentComponent.class).get());
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		Mappers.agentMapper.get(entity).agent.update();
	}

}
