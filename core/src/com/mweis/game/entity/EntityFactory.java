package com.mweis.game.entity;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mweis.game.box2d.Box2dBodyFactory;
import com.mweis.game.entity.agents.ZombieAgent;
import com.mweis.game.entity.components.AgentComponent;
import com.mweis.game.entity.components.AnimationComponent;
import com.mweis.game.entity.components.DynamicBodyComponent;
import com.mweis.game.entity.components.InterpolationComponent;
import com.mweis.game.entity.components.SteeringComponent;
import com.mweis.game.gfx.AnimationMap;
import com.mweis.game.gfx.ResourceManager;

public class EntityFactory {
	
	private static AnimationComponent createZombieAnimComponent(float scale) {
		Texture walkSheet = ResourceManager.getTexture("paladin_run");
		final int FRAME_COLS = 8;
		final int FRAME_ROWS = 16;
		
		TextureRegion[][] tmp = TextureRegion.split(walkSheet, walkSheet.getWidth() / FRAME_COLS, walkSheet.getHeight() / FRAME_ROWS);
		
		float ratioY = scale / (walkSheet.getHeight() / FRAME_ROWS);
		float ratioX = scale / (walkSheet.getWidth() / FRAME_COLS);
		float frameTime = 0.15f;
		
		AnimationMap map = new AnimationMap() {
			@Override
			public Animation<TextureRegion> run_1_16() {
				TextureRegion[] frames = {tmp[0][0], tmp[0][1], tmp[0][2], tmp[0][3],
										tmp[0][4], tmp[0][5], tmp[0][6], tmp[0][7]};
				return new Animation<TextureRegion>(frameTime, frames);
			}

			@Override
			public Animation<TextureRegion> run_2_16() {
				TextureRegion[] frames = {tmp[1][0], tmp[1][1], tmp[1][2], tmp[1][3],
						tmp[1][4], tmp[1][5], tmp[1][6], tmp[1][7]};
				return new Animation<TextureRegion>(frameTime, frames);
			}

			@Override
			public Animation<TextureRegion> run_3_16() {
				TextureRegion[] frames = {tmp[2][0], tmp[2][1], tmp[2][2], tmp[2][3],
						tmp[2][4], tmp[2][5], tmp[2][6], tmp[2][7]};
				return new Animation<TextureRegion>(frameTime, frames);
			}

			@Override
			public Animation<TextureRegion> run_4_16() {
				TextureRegion[] frames = {tmp[3][0], tmp[3][1], tmp[3][2], tmp[3][3],
						tmp[3][4], tmp[3][5], tmp[3][6], tmp[3][7]};
				return new Animation<TextureRegion>(frameTime, frames);
			}

			@Override
			public Animation<TextureRegion> run_5_16() {
				TextureRegion[] frames = {tmp[4][0], tmp[4][1], tmp[4][2], tmp[4][3],
						tmp[4][4], tmp[4][5], tmp[4][6], tmp[4][7]};
				return new Animation<TextureRegion>(frameTime, frames);
			}

			@Override
			public Animation<TextureRegion> run_6_16() {
				TextureRegion[] frames = {tmp[5][0], tmp[5][1], tmp[5][2], tmp[5][3],
						tmp[5][4], tmp[5][5], tmp[5][6], tmp[5][7]};
				return new Animation<TextureRegion>(frameTime, frames);
			}

			@Override
			public Animation<TextureRegion> run_7_16() {
				TextureRegion[] frames = {tmp[6][0], tmp[6][1], tmp[6][2], tmp[6][3],
						tmp[6][4], tmp[6][5], tmp[6][6], tmp[6][7]};
				return new Animation<TextureRegion>(frameTime, frames);
			}

			@Override
			public Animation<TextureRegion> run_8_16() {
				TextureRegion[] frames = {tmp[7][0], tmp[7][1], tmp[7][2], tmp[7][3],
						tmp[7][4], tmp[7][5], tmp[7][6], tmp[7][7]};
				return new Animation<TextureRegion>(frameTime, frames);
			}

			@Override
			public Animation<TextureRegion> run_9_16() {
				TextureRegion[] frames = {tmp[8][0], tmp[8][1], tmp[8][2], tmp[8][3],
						tmp[8][4], tmp[8][5], tmp[8][6], tmp[8][7]};
				return new Animation<TextureRegion>(frameTime, frames);
			}

			@Override
			public Animation<TextureRegion> run_10_16() {
				TextureRegion[] frames = {tmp[9][0], tmp[9][1], tmp[9][2], tmp[9][3],
						tmp[9][4], tmp[9][5], tmp[9][6], tmp[9][7]};
				return new Animation<TextureRegion>(frameTime, frames);
			}

			@Override
			public Animation<TextureRegion> run_11_16() {
				TextureRegion[] frames = {tmp[10][0], tmp[10][1], tmp[10][2], tmp[10][3],
						tmp[10][4], tmp[10][5], tmp[10][6], tmp[10][7]};
				return new Animation<TextureRegion>(frameTime, frames);
			}

			@Override
			public Animation<TextureRegion> run_12_16() {
				TextureRegion[] frames = {tmp[11][0], tmp[11][1], tmp[11][2], tmp[11][3],
						tmp[11][4], tmp[11][5], tmp[11][6], tmp[11][7]};
				return new Animation<TextureRegion>(frameTime, frames);
			}

			@Override
			public Animation<TextureRegion> run_13_16() {
				TextureRegion[] frames = {tmp[12][0], tmp[12][1], tmp[12][2], tmp[12][3],
						tmp[12][4], tmp[12][5], tmp[12][6], tmp[12][7]};
				return new Animation<TextureRegion>(frameTime, frames);
			}

			@Override
			public Animation<TextureRegion> run_14_16() {
				TextureRegion[] frames = {tmp[13][0], tmp[13][1], tmp[13][2], tmp[13][3],
						tmp[13][4], tmp[13][5], tmp[13][6], tmp[13][7]};
				return new Animation<TextureRegion>(frameTime, frames);
			}

			@Override
			public Animation<TextureRegion> run_15_16() {
				TextureRegion[] frames = {tmp[14][0], tmp[14][1], tmp[14][2], tmp[14][3],
						tmp[14][4], tmp[14][5], tmp[14][6], tmp[14][7]};
				return new Animation<TextureRegion>(frameTime, frames);
			}

			@Override
			public Animation<TextureRegion> run_16_16() {
				TextureRegion[] frames = {tmp[15][0], tmp[15][1], tmp[15][2], tmp[15][3],
						tmp[15][4], tmp[15][5], tmp[15][6], tmp[15][7]};
				return new Animation<TextureRegion>(frameTime, frames);
			}

			@Override
			public Animation<TextureRegion> _default() {
				TextureRegion[] frames = {tmp[0][0]};
				return new Animation<TextureRegion>(frameTime, frames);
			}
		};
		
		float anim_ox = ratioX * (walkSheet.getHeight() / FRAME_ROWS) / 2;
		float anim_oy = ratioY * (walkSheet.getWidth() / FRAME_COLS) / 2;
		
		return new AnimationComponent(map, new Vector2(anim_ox, anim_oy), scale, scale);
	}
	
	public static Entity spawnZombie(float x, float y, World world, Engine engine, boolean isSensor, float speedFactor, boolean independentFacing) {
		float radius = 0.32f;
		Body body = Box2dBodyFactory.createDynamicCircle(new Vector2(x, y), radius, world, isSensor);
		
		Sprite sprite = new Sprite(new Texture(Gdx.files.internal("badlogic.jpg")));
		
		Entity entity = new Entity();
		
		entity.add(createZombieAnimComponent(2.0f));
		
		entity.add(new DynamicBodyComponent(body));
		
		InterpolationComponent ic = new InterpolationComponent();
		entity.add(ic);
		
		SteeringComponent sc = new SteeringComponent(body, independentFacing, radius);
		
		// FOR ARRIVE
//		sc.setMaxLinearSpeed(5);
//		sc.setMaxLinearAcceleration(100);
		
		// FOR WANDER
		sc.setMaxLinearAcceleration(10*speedFactor);
		sc.setMaxLinearSpeed(3*speedFactor);
		
//		if (independentFacing) { // can be set regardless
			sc.setMaxAngularAcceleration(.5f*speedFactor); // greater than 0 because independent facing is enabled
			sc.setMaxAngularSpeed(5*speedFactor);
//		}
		
		entity.add(sc);
		AgentComponent<ZombieAgent> ac = new AgentComponent<ZombieAgent>(new ZombieAgent(entity));
		
		entity.add(ac);
		
//		ic.synchronize(body);
		
		engine.addEntity(entity);
		
		return entity;
	}
	
	private EntityFactory() { };
}
