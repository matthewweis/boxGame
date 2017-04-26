package com.mweis.game.entity.fsm;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.mweis.game.entity.agents.ZombieAgent;

public enum ZombieState implements State<ZombieAgent> {
	STEER() {
		
		@Override
		public void enter(ZombieAgent agent) {
			agent.steer.getBody().setActive(true);
		}
		
		@Override
		public void update(ZombieAgent agent) {
			if (agent.target != null) {
				if (agent.steer.getPosition().dst2(agent.target.getPosition()) <= agent.attackRangeSquared) {
//					MessageManager.getInstance().dispatchMessage(Messages.IN_ATTACK_RANGE);
					agent.fsm.changeState(ZombieState.ATTACK);
				}
			}
			agent.steer.update(GdxAI.getTimepiece().getDeltaTime());
		}

		@Override
		public boolean onMessage(ZombieAgent agent, Telegram telegram) {
			return false;
		}
	},
	
	ATTACK() {
		@Override
		public void enter(ZombieAgent agent) {
			Gdx.app.log(this.toString(), "Enter " + agent.fsm.getCurrentState().toString());
			agent.steer.getBody().setLinearVelocity(0, 0);
//			agent.steer.getBody().setAngularVelocity(0.0f);
			agent.steer.getBody().setActive(false);
			agent.timer = agent.attackTime;
		}
		
		@Override
		public void update(ZombieAgent agent) {
			if (agent.timer <= 0) {
				agent.fsm.changeState(ZombieState.STEER);
			} else {
				agent.timer -= GdxAI.getTimepiece().getDeltaTime();
			}			
		}

		@Override
		public boolean onMessage(ZombieAgent agent, Telegram telegram) {
			return false;
		}
	},
	
	GLOBAL_STATE() {

		@Override
		public void update(ZombieAgent agent) {
			 // eventually look for targets here
		}

		@Override
		public boolean onMessage(ZombieAgent agent, Telegram telegram) {
			return false;
		}
	};
	
	@Override
	public void enter(ZombieAgent agent) {
		Gdx.app.log(this.toString(), "Enter " + agent.fsm.getCurrentState().toString());
	}
	
	@Override
	public void exit(ZombieAgent agent) {
		Gdx.app.log(this.toString(), "Exit " + agent.fsm.getCurrentState().toString());
	}
}
