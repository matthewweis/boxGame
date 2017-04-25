package com.mweis.game.util;

import com.badlogic.ashley.core.ComponentMapper;
import com.mweis.game.entity.components.AgentComponent;
import com.mweis.game.entity.components.AnimationComponent;
import com.mweis.game.entity.components.CollisionComponent;
import com.mweis.game.entity.components.DynamicBodyComponent;
import com.mweis.game.entity.components.InterpolationComponent;
import com.mweis.game.entity.components.SteeringComponent;

public final class Mappers {
	public static final ComponentMapper<AgentComponent> agentMapper = ComponentMapper.getFor(AgentComponent.class);
	public static final ComponentMapper<AnimationComponent> animationMapper = ComponentMapper.getFor(AnimationComponent.class);
	public static final ComponentMapper<CollisionComponent> collisionMapper = ComponentMapper.getFor(CollisionComponent.class);
	public static final ComponentMapper<DynamicBodyComponent> dynamicBodyMapper = ComponentMapper.getFor(DynamicBodyComponent.class);
	public static final ComponentMapper<InterpolationComponent> interpolationMapper = ComponentMapper.getFor(InterpolationComponent.class);
	public static final ComponentMapper<SteeringComponent> steeringMapper = ComponentMapper.getFor(SteeringComponent.class);
	
	private Mappers() { };
}
