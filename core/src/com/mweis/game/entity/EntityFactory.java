package com.mweis.game.entity;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.steer.behaviors.Wander;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mweis.game.box2d.Box2dBodyFactory;
import com.mweis.game.box2d.Box2dConversionUtils;
import com.mweis.game.box2d.Box2dSteeringUtils;
import com.mweis.game.entity.agents.ZombieAgent;
import com.mweis.game.entity.components.AgentComponent;
import com.mweis.game.entity.components.AnimationComponent;
import com.mweis.game.entity.components.DynamicBodyComponent;
import com.mweis.game.entity.components.InterpolationComponent;
import com.mweis.game.entity.components.SteeringComponent;
import com.mweis.game.entity.fsm.ZombieState;
import com.mweis.game.util.Mappers;

public class EntityFactory {
	
	public static Entity spawnZombie(float x, float y, World world, Engine engine, boolean isSensor, float speedFactor) {
		float radius = 0.32f;
		Body body = Box2dBodyFactory.createDynamicCircle(new Vector2(x, y), radius, world, isSensor);
		
		Sprite sprite = new Sprite(new Texture(Gdx.files.internal("badlogic.jpg")));
		
		Entity entity = new Entity();
		
		entity.add(new AnimationComponent(new Texture(Gdx.files.internal("badlogic.jpg")), sprite));
		
		entity.add(new DynamicBodyComponent(body));
		
		InterpolationComponent ic = new InterpolationComponent();
		entity.add(ic);
		
		SteeringComponent sc = new SteeringComponent(body, true, radius);
		
		// FOR ARRIVE
//		sc.setMaxLinearSpeed(5);
//		sc.setMaxLinearAcceleration(100);
		
		// FOR WANDER
		sc.setMaxLinearAcceleration(10*speedFactor);
		sc.setMaxLinearSpeed(3*speedFactor);
		sc.setMaxAngularAcceleration(.5f*speedFactor); // greater than 0 because independent facing is enabled
		sc.setMaxAngularSpeed(5*speedFactor);
		
		entity.add(sc);
		AgentComponent<ZombieAgent> ac = new AgentComponent<ZombieAgent>(new ZombieAgent(entity));
		
		entity.add(ac);
		
//		ic.synchronize(body);
		
		engine.addEntity(entity);
		
		return entity;
	}
	
	private EntityFactory() { };
}
