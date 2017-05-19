package com.mweis.game.entity.agents;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.steer.behaviors.PrioritySteering;
import com.badlogic.gdx.ai.steer.behaviors.Pursue;
import com.badlogic.gdx.ai.steer.behaviors.RaycastObstacleAvoidance;
import com.badlogic.gdx.ai.steer.behaviors.Wander;
import com.badlogic.gdx.ai.steer.utils.rays.ParallelSideRayConfiguration;
import com.badlogic.gdx.ai.steer.utils.rays.SingleRayConfiguration;
import com.badlogic.gdx.ai.utils.RaycastCollisionDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mweis.game.box2d.Box2dRaycastCollisionDetector;
import com.mweis.game.entity.components.SteeringComponent;
import com.mweis.game.entity.fsm.ZombieState;
import com.mweis.game.util.Constants;
import com.mweis.game.util.Mappers;

public class ZombieAgent implements Agent {
	public StateMachine<ZombieAgent, ZombieState> fsm;
	public SteeringComponent steer, target; // handled by FSM
	public float attackRangeSquared = 0.60f;
	public float attackTime = 1.0f;
	public float timer = 0.0f;
	
	private Pursue<Vector2> pursue;
	private Wander<Vector2> wander;
	public RaycastObstacleAvoidance<Vector2> raycast;
	public PrioritySteering<Vector2> prioritySteeringSB;
	public SingleRayConfiguration<Vector2> src;
	public ParallelSideRayConfiguration<Vector2> psrc;
	
	public ZombieAgent(Entity entity, World world, Entity target) {
		this.fsm = new DefaultStateMachine<ZombieAgent, ZombieState>(this);
		this.steer = Mappers.steeringMapper.get(entity);
//		MessageManager.getInstance().addListener(this, Messages.SOME_MESSAGE_HERE);
		setWander();
		setPursue(Mappers.steeringMapper.get(target));
		RaycastCollisionDetector<Vector2> rcd = new Box2dRaycastCollisionDetector(world);
		src = new SingleRayConfiguration<Vector2>(this.steer, Constants.MPP*100);
		psrc = new ParallelSideRayConfiguration<Vector2>(this.steer, Constants.MPP*100,
				this.steer.getBoundingRadius());
		raycast = new RaycastObstacleAvoidance<Vector2>(this.steer, psrc, rcd);
		prioritySteeringSB = new PrioritySteering<Vector2>(this.steer, this.steer.getZeroLinearSpeedThreshold())
				.add(raycast).add(pursue);
		
		fsm.setGlobalState(ZombieState.GLOBAL); // always do states last (because init will be called)
	}
	
	
	public Pursue<Vector2> getPursue() {
		return pursue;
	}
	
	public void setPursue(SteeringComponent target) {
		if (pursue != null) {
			this.target = target;
			pursue.setTarget(target);
		} else {
			setPursue(target, 0);
		}
	}
	
	public void setPursue(SteeringComponent target, float maxPredictionTime) {
		this.target = target;
		if (pursue != null) {
			pursue.setTarget(target);
			pursue.setMaxPredictionTime(maxPredictionTime);
		} else {
			this.target = target;
			this.pursue = new Pursue<Vector2>(this.steer, target)
			.setEnabled(true)
			.setMaxPredictionTime(maxPredictionTime);
		}
	}

	public Wander<Vector2> getWander() {
		return wander;
	}
	
	public void setWander() {
		setWander(3, 3, 1, MathUtils.PI2 * 4);
	}
	
	public void setWander(float offset, float orientation, float radius, float rate) {
		wander = new Wander<Vector2>(this.steer) //
				
		.setFaceEnabled(false) // We want to use Face internally (independent facing is on)
		.setAlignTolerance(0.001f) // Used by Face
		.setDecelerationRadius(1) // Used by Face
		.setTimeToTarget(0.1f) // Used by Face
		
		.setWanderOffset(offset) //
		.setWanderOrientation(orientation) //
		.setWanderRadius(radius) //
		.setWanderRate(rate);
	}

	public void update() {
		fsm.update();
	}

	@Override
	public boolean handleMessage(Telegram msg) {
		return fsm.handleMessage(msg);
	}
}
