package com.mweis.game.world;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

public class WorldFactory {
	
	/*
	 * Boxes are [x][y][width][height]
	 */
	public static World generateWorld(boolean isEmpty) {
		World world = new World(Vector2.Zero, true);
//		World world = new World(new Vector2(0.0f, -9.8f), true);
		if (isEmpty) {
			return world;
		}
		
		BodyDef def = new BodyDef();
		def.type = BodyType.StaticBody;
		
		float[][] boxes = { {0.0f, 0.0f, 3.0f, 3.0f}, {5.0f, 5.0f, 4.0f, 2.0f}};
		for (float[] box : boxes) {
			def.position.set(box[0], box[1]);
			Body body = world.createBody(def);
			
			PolygonShape shape = new PolygonShape();
			
			shape.setAsBox(box[2]/2.0f, box[3]/2.0f);
			
			body.createFixture(shape, 0.0f);
			
			shape.dispose();
//			EdgeShape edge = new EdgeShape();
//			
//			edge.set(0, 0, room.getWidth(), 0);
//			body.createFixture(edge, 0.0f);
//			edge.set(0, 0, 0, room.getHeight());
//			body.createFixture(edge, 0.0f);
//			edge.set(room.getWidth(), room.getHeight(), room.getWidth(), 0);
//			body.createFixture(edge, 0.0f);
//			edge.set(room.getWidth(), room.getHeight(), 0, room.getHeight());
//			body.createFixture(edge, 0.0f);
//			
//			edge.dispose();
		}
		
		return world;
	}
	
	
	private WorldFactory() { };
}
