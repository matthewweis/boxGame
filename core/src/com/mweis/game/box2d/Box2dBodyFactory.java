package com.mweis.game.box2d;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Box2dBodyFactory {
	
	public static Body createDynamicCircle(Vector2 position, float radius, World world, boolean isSensor, float density) {
		// First we create a body definition
				BodyDef bodyDef = new BodyDef();
				// We set our body to dynamic, for something like ground which doesn't move we would set it to StaticBody
				bodyDef.type = BodyType.DynamicBody;
				// Set our body's starting position in the world
				bodyDef.position.set(position);

				// Create our body in the world using our body definition
				Body body = world.createBody(bodyDef);

				// Create a circle shape and set its radius to 6
				CircleShape circle = new CircleShape();
				circle.setRadius(radius);

				// Create a fixture definition to apply our shape to
				FixtureDef fixtureDef = new FixtureDef();
				fixtureDef.shape = circle;
				fixtureDef.density = density;
//				fixtureDef.friction = 1.0f;
//				fixtureDef.restitution = 0.6f; // Make it bounce a little bit
//				fixtureDef.filter.groupIndex = 1;
				fixtureDef.isSensor = isSensor;
				
				// Create our fixture and attach it to the body
				Fixture fixture = body.createFixture(fixtureDef);

				// Remember to dispose of any shapes after you're done with them!
				// BodyDef and FixtureDef don't need disposing, but shapes do.
				circle.dispose();
				
				return body;
	}
	
	/*
	 * Inactive sensor circle object
	 */
	public static Body createWaypoint(Vector2 position, World world) {
		// First we create a body definition
				BodyDef bodyDef = new BodyDef();
				// We set our body to dynamic, for something like ground which doesn't move we would set it to StaticBody
				bodyDef.type = BodyType.StaticBody;
				// Set our body's starting position in the world
				bodyDef.position.set(position);

				// Create our body in the world using our body definition
				Body body = world.createBody(bodyDef);

				// Create a circle shape and set its radius to 6
				CircleShape circle = new CircleShape();
				circle.setRadius(0.1f);

				// Create a fixture definition to apply our shape to
				FixtureDef fixtureDef = new FixtureDef();
				fixtureDef.shape = circle;
//				fixtureDef.density = 1.0f; 
//				fixtureDef.friction = 1.0f;
//				fixtureDef.restitution = 0.6f; // Make it bounce a little bit
//				fixtureDef.filter.groupIndex = 1;
				fixtureDef.isSensor = true;
				
				// Create our fixture and attach it to the body
				Fixture fixture = body.createFixture(fixtureDef);

				// Remember to dispose of any shapes after you're done with them!
				// BodyDef and FixtureDef don't need disposing, but shapes do.
				circle.dispose();
				body.setActive(false);
				
				return body;
	}
	
	private Box2dBodyFactory() { };
}
