package com.mweis.game.entity.fsm;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.steer.behaviors.PrioritySteering;
import com.badlogic.gdx.ai.utils.Ray;
import com.badlogic.gdx.math.Vector2;
import com.mweis.game.entity.agents.ZombieAgent;
import com.mweis.game.util.Constants;

public enum ZombieState implements State<ZombieAgent> {
	PURSUE() {
		
		@Override
		public void enter(ZombieAgent agent) {
			agent.steer.setSteeringBehavior(agent.prioritySteeringSB);
		}
		
		@Override
		public void update(ZombieAgent agent) {
			if (agent.target != null) {
				if (agent.steer.getPosition().dst2(agent.target.getPosition()) <= agent.attackRangeSquared) {
					agent.fsm.changeState(ZombieState.ATTACK);
					return; // must return after state change
				}
			}
//			System.out.println(((PrioritySteering)agent.steer.getSteeringBehavior()).getSelectedBehaviorIndex());
			for (Ray ray : agent.psrc.getRays()) {
				Constants.DrawDebugLine((Vector2)ray.start, (Vector2)ray.end);
			}
//			agent.steer.update(GdxAI.getTimepiece().getDeltaTime());
		}

		@Override
		public void exit(ZombieAgent agent) {
			agent.steer.getBody().setLinearVelocity(0, 0);
			agent.steer.getBody().setAngularVelocity(0.0f);
			agent.steer.getBody().setAwake(false);
			agent.steer.setSteeringBehavior(null);
		}
		
		@Override
		public boolean onMessage(ZombieAgent agent, Telegram telegram) {
			// handle new target spotted ?
			return false;
		}
	},
	
	WANDER() {
		
		@Override
		public void enter(ZombieAgent agent) {
			agent.steer.setSteeringBehavior(agent.getWander());
		}
		
		@Override
		public void update(ZombieAgent agent) {
//			agent.steer.update(GdxAI.getTimepiece().getDeltaTime());
		}

		@Override
		public void exit(ZombieAgent agent) {
			agent.steer.getBody().setLinearVelocity(0, 0);
			agent.steer.getBody().setAngularVelocity(0.0f);
//			agent.steer.getBody().setAwake(false);
			agent.steer.setSteeringBehavior(null);
		}
		
		@Override
		public boolean onMessage(ZombieAgent agent, Telegram telegram) {
			// handle target spotted
			return false;
		}
	},
	
	ATTACK() {
		@Override
		public void enter(ZombieAgent agent) {
			agent.steer.getBody().setLinearVelocity(0, 0);
			agent.steer.getBody().setAngularVelocity(0.0f);
//			agent.steer.getBody().setAwake(false);
//			agent.steer.setSteeringBehavior(null);
			agent.timer = agent.attackTime;
		}
		
		@Override
		public void update(ZombieAgent agent) {
			if (agent.timer <= 0) {
				agent.fsm.changeState(ZombieState.PURSUE);
				return;
			} else {
				agent.timer -= GdxAI.getTimepiece().getDeltaTime();
			}			
		}
		
		@Override
		public void exit(ZombieAgent agent) {
			agent.timer = 0;
		}
		
		@Override
		public boolean onMessage(ZombieAgent agent, Telegram telegram) {
			// handle interrupts
			return false;
		}
	},
	
	GLOBAL() {

		@Override
		public void enter(ZombieAgent entity) {
			// must be called manually
		}

		@Override
		public void update(ZombieAgent entity) {
			// automatically called before each update
		}

		@Override
		public void exit(ZombieAgent entity) {
			// must be called manually
		}

		@Override
		public boolean onMessage(ZombieAgent entity, Telegram telegram) {
			return false;
		}
		
	};
}
