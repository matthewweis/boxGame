package com.mweis.game.entity.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.mweis.game.entity.components.SteeringComponent;
import com.mweis.game.util.Mappers;

public class SteeringSystem extends IteratingSystem {

	public SteeringSystem() {
		super(Family.all(SteeringComponent.class).get());
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		Mappers.steeringMapper.get(entity).update(deltaTime);
	}
}
