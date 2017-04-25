package com.mweis.game.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class AnimationComponent implements Component {
	public Sprite sprite;
	public Texture texture;
	
	public AnimationComponent(Texture texture, Sprite sprite) {
		this.sprite = sprite;
		this.texture = texture;
	}
}
