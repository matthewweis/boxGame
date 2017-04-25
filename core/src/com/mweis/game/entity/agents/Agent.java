package com.mweis.game.entity.agents;

import com.badlogic.gdx.ai.msg.Telegraph;

/*
 * Agents are created for each unique entity that has behavior driven from a state.
 */
public interface Agent extends Telegraph {
	public abstract void update();
}
