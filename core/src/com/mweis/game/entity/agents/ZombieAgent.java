package com.mweis.game.entity.agents;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.mweis.game.entity.components.SteeringComponent;
import com.mweis.game.entity.fsm.ZombieState;
import com.mweis.game.util.Mappers;
import com.mweis.game.util.Messages;

public class ZombieAgent implements Agent {
	public StateMachine<ZombieAgent, ZombieState> fsm;
	public SteeringComponent steer, target; // handled by FSM
	public float attackRangeSquared = 1.0f;
	public float attackTime = 1.0f;
	public float timer = 0.0f;
	
	public ZombieAgent(Entity entity) {
		this.fsm = new DefaultStateMachine<ZombieAgent, ZombieState>(this);
		this.steer = Mappers.steeringMapper.get(entity);
		target = null;
		MessageManager.getInstance().addListener(this, Messages.ACTION_COMPLETED);
	}

	public void update() {
		fsm.update();
	}

	@Override
	public boolean handleMessage(Telegram msg) {
		return fsm.handleMessage(msg);
	}
}
