package com.mweis.game.entity.agents;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.steer.behaviors.Pursue;
import com.badlogic.gdx.ai.steer.behaviors.Seek;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mweis.game.box2d.Box2dBodyFactory;
import com.mweis.game.entity.components.SteeringComponent;
import com.mweis.game.entity.fsm.PlayerState;
import com.mweis.game.util.Mappers;
import com.mweis.game.util.Messages;

/*
 * Where player is a single waypoint entity (todo? might be good for anims)
 */
public class PlayerAgent implements Agent {
	public float justABit = 0.001f; // how close to pos you must be to stop moving
	public StateMachine<PlayerAgent, PlayerState> fsm;
	public SteeringComponent steer, target, move_to;
	
//	public float attackRangeSquared = 1.0f;
//	public float attackTime = 1.0f;
//	public float timer = 0.0f;
	
	private Pursue<Vector2> pursue;
	private Seek<Vector2> move;
	
	public PlayerAgent(Entity entity, World world) {
		this.fsm = new DefaultStateMachine<PlayerAgent, PlayerState>(this);
		this.steer = Mappers.steeringMapper.get(entity);
		move_to = new SteeringComponent(Box2dBodyFactory.createWaypoint(new Vector2(0, 0), world), false, 1.0f);
		move = new Seek<Vector2>(this.steer, this.move_to)
				.setEnabled(true);
		target = null;
		MessageManager.getInstance().addListener(this, Messages.PLAYER_ATTACK_X);
		MessageManager.getInstance().addListener(this, Messages.PLAYER_MOVE_TO_X);
		
		// always do states last
		fsm.setGlobalState(PlayerState.GLOBAL);
		fsm.changeState(PlayerState.IDLE);
	}
	
	@Override
	public void update() {
		fsm.update();
	}
	
	public void setPursueTarget(SteeringComponent target) {
		this.target = target;
		if (pursue != null) {
			pursue.setTarget(target);
		} else {
			this.target = target;
			this.pursue = new Pursue<Vector2>(this.steer, target)
			.setEnabled(true)
			.setMaxPredictionTime(2);
		}
	}
	
	public Pursue<Vector2> getPursue() {
		return pursue;
	}
	
	public Seek<Vector2> getMove() {
		return move;
	}
	
	public void setMoveWaypoint(Vector2 position) {
		move_to.getBody().setTransform(position, 0.0f);
	}
	
	@Override
	public boolean handleMessage(Telegram msg) {
		return fsm.handleMessage(msg);
	}
}
