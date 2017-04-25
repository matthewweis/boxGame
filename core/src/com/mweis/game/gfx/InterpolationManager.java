package com.mweis.game.gfx;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.mweis.game.entity.components.DynamicBodyComponent;
import com.mweis.game.entity.components.InterpolationComponent;
import com.mweis.game.util.Mappers;

public class InterpolationManager {
	// TODO Make this family also interpolate Kinematic Objects
	private static final Family family = Family.all(DynamicBodyComponent.class, InterpolationComponent.class).get();
	
	public static void copyCurrentPosition(Engine engine) {
		for (Entity entity : engine.getEntitiesFor(family)) {
			DynamicBodyComponent dc = Mappers.dynamicBodyMapper.get(entity);
			if (dc.body.isActive()) {
				InterpolationComponent ic = Mappers.interpolationMapper.get(entity);
				
				ic.oldPosition.set(dc.body.getPosition());
				ic.oldAngle = dc.body.getAngle();
			}
		}
	}
	
	public static void interpolateCurrentPosition(float alpha, Engine engine) {
		for (Entity entity : engine.getEntitiesFor(family)) {
			DynamicBodyComponent dc = Mappers.dynamicBodyMapper.get(entity);
			if (dc.body.isActive()) {
				InterpolationComponent ic = Mappers.interpolationMapper.get(entity);
				
				ic.position.x = dc.body.getPosition().x * alpha + ic.oldPosition.x * (1.0f - alpha);
				ic.position.y = dc.body.getPosition().y * alpha + ic.oldPosition.y * (1.0f - alpha);
				ic.angle = dc.body.getAngle() * alpha + ic.oldAngle * (1.0f - alpha);
				
//		        Gdx.app.log("Interpolate New", dc.body.getPosition().toString()  );
//		        Gdx.app.log("Interpolate Old", ic.oldPosition.toString());
//		        Gdx.app.log("Interpolate Midpoint", ic.position.toString());
				
			}
		}
	}
}
