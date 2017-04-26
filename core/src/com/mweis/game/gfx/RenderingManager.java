package com.mweis.game.gfx;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.mweis.game.entity.components.AnimationComponent;
import com.mweis.game.entity.components.DynamicBodyComponent;
import com.mweis.game.util.Mappers;

public class RenderingManager {
	
	private static final Family family = Family.all(AnimationComponent.class, DynamicBodyComponent.class).get();
	
	public static void render(Matrix4 projection, SpriteBatch batch, Engine engine) {
		batch.begin();
		batch.setProjectionMatrix(projection);
		for (Entity entity : engine.getEntitiesFor(family)) {
			AnimationComponent anim = Mappers.animationMapper.get(entity);
//			InterpolationComponent ic = Mappers.interpolationMapper.get(entity);
			DynamicBodyComponent body = Mappers.dynamicBodyMapper.get(entity); // TEMP
			
//		    ac.sprite.setPosition(Box2dConversionUtils.metersToPixels(ic.position.x),
//		    		Box2dConversionUtils.metersToPixels(ic.position.y));
////		    ac.sprite.setCenter(ic.position.x, ic.position.y);
//			ac.sprite.draw(batch);
			
//			ac.sprite.setPosition(Box2dConversionUtils.metersToPixels(dc.body.getPosition().x),
//		    		Box2dConversionUtils.metersToPixels(dc.body.getPosition().y));
////		    ac.sprite.setCenter(ic.position.x, ic.position.y);
//			ac.sprite.draw(batch);
			
			
			
			TextureRegion currFrame = anim.map._default().getKeyFrame(anim.stateTime, true);
			
			float dir = MathUtils.radiansToDegrees * body.body.getAngle() - 180.0f;
			dir = -dir;
			if (dir < 22.5) {
				currFrame = anim.map.run_1_16().getKeyFrame(anim.stateTime, true);
			} else if(dir < 2*22.5) {
				currFrame = anim.map.run_2_16().getKeyFrame(anim.stateTime, true);
			} else if(dir < 3*22.5) {
				currFrame = anim.map.run_3_16().getKeyFrame(anim.stateTime, true);
			} else if(dir < 4*22.5) {
				currFrame = anim.map.run_4_16().getKeyFrame(anim.stateTime, true);
			} else if(dir < 5*22.5) {
				currFrame = anim.map.run_5_16().getKeyFrame(anim.stateTime, true);
			} else if(dir < 6*22.5) {
				currFrame = anim.map.run_6_16().getKeyFrame(anim.stateTime, true);
			} else if(dir < 7*22.5) {
				currFrame = anim.map.run_7_16().getKeyFrame(anim.stateTime, true);
			} else if(dir < 8*22.5) {
				currFrame = anim.map.run_8_16().getKeyFrame(anim.stateTime, true);
			} else if(dir < 9*22.5) {
				currFrame = anim.map.run_9_16().getKeyFrame(anim.stateTime, true);
			} else if(dir < 10*22.5) {
				currFrame = anim.map.run_10_16().getKeyFrame(anim.stateTime, true);
			} else if(dir < 11*22.5) {
				currFrame = anim.map.run_11_16().getKeyFrame(anim.stateTime, true);
			} else if(dir < 12*22.5) {
				currFrame = anim.map.run_12_16().getKeyFrame(anim.stateTime, true);
			} else if(dir < 13*22.5) {
				currFrame = anim.map.run_13_16().getKeyFrame(anim.stateTime, true);
			} else if(dir < 14*22.5) {
				currFrame = anim.map.run_14_16().getKeyFrame(anim.stateTime, true);
			} else if(dir < 15*22.5) {
				currFrame = anim.map.run_15_16().getKeyFrame(anim.stateTime, true);
			} else if(dir < 16*22.5) {
				currFrame = anim.map.run_16_16().getKeyFrame(anim.stateTime, true);
			}
			
			anim.stateTime += GdxAI.getTimepiece().getDeltaTime();
			
			Vector2 position = body.body.getPosition();
			// draw sprite s.t. position is its midpoint
			batch.draw(currFrame, position.x - anim.offset.x, position.y - anim.offset.y, anim.width, anim.height);
			
		}
		batch.end();
	}
	
	private RenderingManager() { };
}
