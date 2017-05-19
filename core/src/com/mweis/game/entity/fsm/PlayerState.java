package com.mweis.game.entity.fsm;

import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.math.Vector2;
import com.mweis.game.entity.agents.PlayerAgent;
import com.mweis.game.util.Messages;

public enum PlayerState implements State<PlayerAgent> {
	
	IDLE() { // stand idle
		@Override
		public void enter(PlayerAgent agent) {
			agent.steer.getBody().setLinearVelocity(0, 0);
			agent.steer.getBody().setAngularVelocity(0.0f);
			agent.steer.getBody().setAwake(false);
			agent.steer.setSteeringBehavior(null);
		}

		@Override
		public void update(PlayerAgent agent) {
			
		}

		@Override
		public void exit(PlayerAgent agent) {
			
		}

		@Override
		public boolean onMessage(PlayerAgent agent, Telegram telegram) {
			return false;
		}
	},
	
	MOVE() { // move to a waypoint
		@Override
		public void enter(PlayerAgent agent) {
			agent.steer.setSteeringBehavior(agent.getMove());
		}

		@Override
		public void update(PlayerAgent agent) {
			if (agent.move_to != null) {
				if (agent.steer.getPosition().dst2(agent.move_to.getPosition()) <= agent.justABit) {
					agent.fsm.changeState(PlayerState.IDLE);
					return; // must return after state change
				}
			}
		}

		@Override
		public void exit(PlayerAgent agent) {
			agent.steer.getBody().setLinearVelocity(0, 0);
			agent.steer.getBody().setAngularVelocity(0.0f);
			agent.steer.getBody().setAwake(false);
			agent.steer.setSteeringBehavior(null);
		}

		@Override
		public boolean onMessage(PlayerAgent agent, Telegram telegram) {
			return false;
		}
		
	},
	
	PURSUE() { // pursue an enemy

		@Override
		public void enter(PlayerAgent agent) {
			agent.steer.setSteeringBehavior(agent.getPursue());
		}

		@Override
		public void update(PlayerAgent agent) {
			
		}

		@Override
		public void exit(PlayerAgent agent) {
			agent.steer.getBody().setLinearVelocity(0, 0);
			agent.steer.getBody().setAngularVelocity(0.0f);
			agent.steer.getBody().setAwake(false);
			agent.steer.setSteeringBehavior(null);
		}

		@Override
		public boolean onMessage(PlayerAgent agent, Telegram telegram) {
			return false;
		}
	},
	
	GLOBAL() {

		@Override
		public void enter(PlayerAgent agent) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void update(PlayerAgent agent) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void exit(PlayerAgent agent) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean onMessage(PlayerAgent agent, Telegram telegram) {
			if (telegram.message == Messages.PLAYER_MOVE_TO_X) {
				Vector2 position = (Vector2) telegram.extraInfo;
				agent.setMoveWaypoint(position);
				if (agent.fsm.getCurrentState() != PlayerState.MOVE) {
					agent.fsm.changeState(MOVE);
				}
				return true;
			}
			return false;
		}
	};
}
