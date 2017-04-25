package com.mweis.game.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class InterpolationComponent implements Component {
	public Vector2 oldPosition, position; // currPos is used for rendering position
	public float oldAngle, angle;
	
	public InterpolationComponent() {
		oldPosition = Vector2.Zero;
		position = Vector2.Zero;
		oldAngle = 0.0f;
		angle = 0.0f;
	}
	
	public void synchronize(Body body) {
		this.oldPosition.set(body.getPosition());
		this.position.set(this.oldPosition);
		this.oldAngle = body.getAngle();
		this.angle = this.oldAngle;
	}
}
