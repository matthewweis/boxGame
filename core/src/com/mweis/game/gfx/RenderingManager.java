package com.mweis.game.gfx;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.mweis.game.box2d.Box2dConversionUtils;
import com.mweis.game.entity.components.AnimationComponent;
import com.mweis.game.entity.components.DynamicBodyComponent;
import com.mweis.game.entity.components.InterpolationComponent;
import com.mweis.game.util.Mappers;

public class RenderingManager {
	
	private static final Family family = Family.all(AnimationComponent.class, InterpolationComponent.class).get();
	
	public static void render(SpriteBatch batch, Engine engine) {
		batch.begin();
		for (Entity entity : engine.getEntitiesFor(family)) {
			AnimationComponent ac = Mappers.animationMapper.get(entity);
			InterpolationComponent ic = Mappers.interpolationMapper.get(entity);
			DynamicBodyComponent dc = Mappers.dynamicBodyMapper.get(entity); // TEMP
			
		    ac.sprite.setPosition(Box2dConversionUtils.metersToPixels(ic.position.x),
		    		Box2dConversionUtils.metersToPixels(ic.position.y));
//		    ac.sprite.setCenter(ic.position.x, ic.position.y);
			ac.sprite.draw(batch);
			
			ac.sprite.setPosition(Box2dConversionUtils.metersToPixels(dc.body.getPosition().x),
		    		Box2dConversionUtils.metersToPixels(dc.body.getPosition().y));
//		    ac.sprite.setCenter(ic.position.x, ic.position.y);
			ac.sprite.draw(batch);
			
		}
		batch.end();
	}
	
	private RenderingManager() { };
}
