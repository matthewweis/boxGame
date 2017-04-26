package com.mweis.game.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class DynamicBodyComponent implements Component {
	public Body body;
	
	public DynamicBodyComponent(Body body) {
		this.body = body;
//		if (body.getType() != BodyType.DynamicBody) {
//			Gdx.app.error("DynamicBodyComponent", "A non-dynamic body was added to DynamicBodyComponent",
//					new IllegalArgumentException());
//		}
	}
}
