package com.mweis.game.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.mweis.game.box2d.Box2dConversionUtils;
import com.mweis.game.box2d.Box2dLocation;
import com.mweis.game.box2d.Box2dSteeringUtils;
import com.mweis.game.util.Constants;

public class SteeringComponent implements Component, Steerable<Vector2> {

	private Body body;
 
	private float boundingRadius;
	private boolean tagged;

	private float maxLinearSpeed;
	private float maxLinearAcceleration;
	private float maxAngularSpeed;
	private float maxAngularAcceleration;
	private boolean independentFacing;
	
	private SteeringBehavior<Vector2> steeringBehavior;

	private static final SteeringAcceleration<Vector2> steeringOutput = new SteeringAcceleration<Vector2>(new Vector2());
	
	public SteeringComponent(Body body, boolean independentFacing, float boundingRadius) {
		this.body = body;
		this.independentFacing = independentFacing;
		this.boundingRadius = boundingRadius;
		this.tagged = false;
		
//		position = new Vector2(body.getPosition());
//	    linearVelocity = new Vector2(body.getLinearVelocity());
//	    orientation = this.getOrientation();
//	    angularVelocity = this.getAngularVelocity();
		
//		body.setUserData(this);
	}
	
	
	public Body getBody () {
		return body;
	}

	public void setBody (Body body) {
		this.body = body;
	}

	public boolean isIndependentFacing () {
		return independentFacing;
	}

	public void setIndependentFacing (boolean independentFacing) {
		this.independentFacing = independentFacing;
	}

	@Override
	public Vector2 getPosition () {
		return body.getPosition();
	}

	@Override
	public float getOrientation () {
		return body.getAngle();
	}

	@Override
	public void setOrientation (float orientation) {
		body.setTransform(getPosition(), orientation);
	}

	@Override
	public Vector2 getLinearVelocity () {
		return body.getLinearVelocity();
	}

	@Override
	public float getAngularVelocity () {
		return body.getAngularVelocity();
	}

	@Override
	public float getBoundingRadius () {
		return boundingRadius;
	}

	@Override
	public boolean isTagged () {
		return tagged;
	}

	@Override
	public void setTagged (boolean tagged) {
		this.tagged = tagged;
	}

	@Override
	public Location<Vector2> newLocation () {
		return new Box2dLocation();
	}

	@Override
	public float vectorToAngle (Vector2 vector) {
		return Box2dSteeringUtils.vectorToAngle(vector);
	}

	@Override
	public Vector2 angleToVector (Vector2 outVector, float angle) {
		return Box2dSteeringUtils.angleToVector(outVector, angle);
	}

	public SteeringBehavior<Vector2> getSteeringBehavior () {
		return steeringBehavior;
	}

	public void setSteeringBehavior (SteeringBehavior<Vector2> steeringBehavior) {
		this.steeringBehavior = steeringBehavior;
	}

	public void update (float deltaTime) {
		if (steeringBehavior != null) {
			// Calculate steering acceleration
			steeringBehavior.calculateSteering(steeringOutput);
			/*
			 * Here you might want to add a motor control layer filtering steering accelerations.
			 * 
			 * For instance, a car in a driving game has physical constraints on its movement: it cannot turn while stationary; the
			 * faster it moves, the slower it can turn (without going into a skid); it can brake much more quickly than it can
			 * accelerate; and it only moves in the direction it is facing (ignoring power slides).
			 */

			// Apply steering acceleration
			applySteering(steeringOutput, deltaTime);
		}
	}
	
//	 	protected Vector2 position;
//	    protected Vector2 linearVelocity;
//	    protected float orientation;
//	    protected float angularVelocity;

	protected void applySteering (SteeringAcceleration<Vector2> steering, float deltaTime) {
		boolean anyAccelerations = false;

		// Update position and linear velocity.
		if (!steeringOutput.linear.isZero()) {
			// this method internally scales the force by deltaTime
			body.applyForceToCenter(steeringOutput.linear, true);
			anyAccelerations = true;
		}

		// Update orientation and angular velocity
		if (isIndependentFacing()) {
			if (steeringOutput.angular != 0) {
				// this method internally scales the torque by deltaTime
				body.applyTorque(steeringOutput.angular, true);
				anyAccelerations = true;
			}
		} else {
			// If we haven't got any velocity, then we can do nothing.
			Vector2 linVel = getLinearVelocity();
			if (!linVel.isZero(getZeroLinearSpeedThreshold())) {
				float newOrientation = vectorToAngle(linVel);
				body.setAngularVelocity((newOrientation - getAngularVelocity()) * deltaTime); // this is superfluous if independentFacing is always true
				body.setTransform(body.getPosition(), newOrientation);
			}
		}

		if (anyAccelerations) {
			//body.setActive(true);

			// TODO:
			// Looks like truncating speeds here after applying forces doesn't work as expected.
			// We should likely cap speeds form inside an InternalTickCallback, see
			// http://www.bulletphysics.org/mediawiki-1.5.8/index.php/Simulation_Tick_Callbacks

			// Cap the linear speed
			Vector2 velocity = body.getLinearVelocity();
			float currentSpeedSquare = velocity.len2();
			float maxLinearSpeed = getMaxLinearSpeed();
			if (currentSpeedSquare > maxLinearSpeed * maxLinearSpeed) {
				body.setLinearVelocity(velocity.scl(maxLinearSpeed / (float)Math.sqrt(currentSpeedSquare)));
			}
			

			// Cap the angular speed
			float maxAngVelocity = getMaxAngularSpeed();
			if (body.getAngularVelocity() > maxAngVelocity) {
				body.setAngularVelocity(maxAngVelocity);
			}
		}
//		this.linearVelocity.mulAdd(steering.linear, deltaTime).limit(this.getMaxLinearSpeed());
//
////        Body body = 
//        linearVelocity = body.getLinearVelocity();
//        float   velocityX = linearVelocity.x;
//
//        float desiredVelocity = Math.max(velocityX - maxLinearSpeed,
//                Math.min(Math.signum(steering.linear.x) * maxLinearAcceleration, velocityX + maxLinearSpeed));
//
//        desiredVelocity = Math.min(desiredVelocity, maxLinearSpeed);
//
//        float velocityChange = desiredVelocity - velocityX;
//        float impulse = body.getMass() * velocityChange;
//        body.applyLinearImpulse(impulse, 0, body.getWorldCenter().x, body.getWorldCenter().y, true);
//
//        position.x = body.getPosition().x * Constants.METERS_TO_PIXELS;
//        position.y = body.getPosition().y * Constants.METERS_TO_PIXELS;
//
//        // Update orientation and angular velocity
//        if (independentFacing) {
//            this.orientation += angularVelocity * deltaTime;
//            this.angularVelocity += steering.angular * deltaTime;
//        } else {
//            // For non-independent facing we have to align orientation to linear velocity
//            float newOrientation = calculateOrientationFromLinearVelocity(this);
//            if (newOrientation != this.orientation) {
//                this.angularVelocity = (newOrientation - this.orientation) * deltaTime;
//                this.orientation = newOrientation;
//            }
//        }
	}
	
//    public static float calculateOrientationFromLinearVelocity (Steerable<Vector2> character) {
//        // If we haven't got any velocity, then we can do nothing.
//        if (character.getLinearVelocity().isZero(MathUtils.FLOAT_ROUNDING_ERROR))
//            return character.getOrientation();
//
//        return character.vectorToAngle(character.getLinearVelocity());
//    }

	//
	// Limiter implementation
	//

	@Override
	public float getMaxLinearSpeed () {
		return maxLinearSpeed;
	}

	@Override
	public void setMaxLinearSpeed (float maxLinearSpeed) {
		this.maxLinearSpeed = maxLinearSpeed;
	}

	@Override
	public float getMaxLinearAcceleration () {
		return maxLinearAcceleration;
	}

	@Override
	public void setMaxLinearAcceleration (float maxLinearAcceleration) {
		this.maxLinearAcceleration = maxLinearAcceleration;
	}

	@Override
	public float getMaxAngularSpeed () {
		return maxAngularSpeed;
	}

	@Override
	public void setMaxAngularSpeed (float maxAngularSpeed) {
		this.maxAngularSpeed = maxAngularSpeed;
	}

	@Override
	public float getMaxAngularAcceleration () {
		return maxAngularAcceleration;
	}

	@Override
	public void setMaxAngularAcceleration (float maxAngularAcceleration) {
		this.maxAngularAcceleration = maxAngularAcceleration;
	}

	@Override
	public float getZeroLinearSpeedThreshold () {
		return 0.001f;
	}

	@Override
	public void setZeroLinearSpeedThreshold (float value) {
		throw new UnsupportedOperationException();
	}
}
