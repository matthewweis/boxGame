package com.mweis.game.view;


import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.steer.behaviors.Pursue;
import com.badlogic.gdx.ai.steer.behaviors.Wander;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.mweis.game.entity.EntityFactory;
import com.mweis.game.entity.agents.ZombieAgent;
import com.mweis.game.entity.components.AgentComponent;
import com.mweis.game.entity.fsm.ZombieState;
import com.mweis.game.entity.systems.AgentSystem;
import com.mweis.game.util.Constants;
import com.mweis.game.util.Mappers;
import com.mweis.game.world.WorldFactory;

public class GameScreen implements Screen {
	
	private Engine engine;
	private Entity target, chaser;
	
	private OrthographicCamera cam;
	private SpriteBatch batch;
    
    private World world;
    private Box2DDebugRenderer debugRenderer;
	
    private boolean isCameraLocked;
	
	@Override
	public void show() {
		Gdx.app.log("GameScreen", "GameScreen is now being shown");
		
		float w = 720.0f/8.0f;
		float h = 480.0f/8.0f;
		cam = new OrthographicCamera(w, h);
		
		batch = new SpriteBatch();
        world = WorldFactory.generateWorld(true);
        debugRenderer = new Box2DDebugRenderer();
        
        engine = new Engine();
		engine.addSystem(new AgentSystem());
		
		// make 2 entities, one of which chases the other
		target = EntityFactory.spawnZombie(20, 20, world, engine, true, 1.0f);
		chaser = EntityFactory.spawnZombie(0, 5, world, engine, false, 1.25f);
		
		AgentComponent<ZombieAgent> target_c = Mappers.agentMapper.get(target);
		AgentComponent<ZombieAgent> chaser_c = Mappers.agentMapper.get(chaser);
		chaser_c.agent.target = target_c.agent.steer;
		/*
		 * Max. Linear Arr = 100.0
		 * Max. Linear Speed = 5.0
		 * Deceleration Radius = 3.0
		 * Arrival tolerance = 0.001
		 * time to target = 0.1 (sec)
		 * 
		 * ==
		 * 
		 * bounding radius = 0.32
		 */
		Arrive<Vector2> arriveSB = new Arrive<Vector2>(chaser_c.agent.steer, target_c.agent.steer)
				.setTimeToTarget(0.1f) //
				.setArrivalTolerance(0.001f) //
				.setDecelerationRadius(3.0f);
		
		Wander<Vector2> wanderSB = new Wander<Vector2>(target_c.agent.steer) //
				.setFaceEnabled(true) // We want to use Face internally (independent facing is on)
				.setAlignTolerance(0.001f) // Used by Face
				.setDecelerationRadius(1) // Used by Face
				.setTimeToTarget(0.1f) // Used by Face
				.setWanderOffset(3) //
				.setWanderOrientation(3) //
				.setWanderRadius(1) //
				.setWanderRate(MathUtils.PI2 * 4);
		
		Pursue<Vector2> pursueSB = new Pursue<Vector2>(chaser_c.agent.steer, target_c.agent.steer)
				.setEnabled(true)
				.setMaxPredictionTime(1.0f);
		
		target_c.agent.steer.setSteeringBehavior(wanderSB);
		target_c.agent.fsm.changeState(ZombieState.STEER);
		
		chaser_c.agent.steer.setSteeringBehavior(pursueSB);
		chaser_c.agent.fsm.changeState(ZombieState.STEER);
		
	
		// setup input (this class is the listener)
		setupInput();
		
		initMessaging();
	}
	
	private void initMessaging() {
		MessageManager.getInstance().dispatchMessage(0, engine);
	}
	
	private float accumulator = 0.0f;
	@Override
	public void render(float deltaTime) {
		if (deltaTime > 0.25f) { deltaTime = 0.25f; }
		accumulator += deltaTime;
		while (accumulator >= Constants.DELTA_TIME) {
			update();
			accumulator -= Constants.DELTA_TIME;
		}
		draw();
	}
	
	private void update() {
    	GdxAI.getTimepiece().update(Constants.DELTA_TIME);
    	handleInput();
    	cam.update();
    	batch.setProjectionMatrix(cam.combined);
    	world.step(Constants.DELTA_TIME, 6, 2); // 10, 8?
    	engine.update(Constants.DELTA_TIME); // what is best order of world -> engine?
	}
	
	private void draw() {
		debugRenderer.render(world, cam.combined);
	}

	@Override
	public void resize(int width, int height) {
		Gdx.app.log("GameScreen", "GameScreen was resized");
	}

	@Override
	public void pause() {
		Gdx.app.log("GameScreen", "GameScreen was paused");
	}

	@Override
	public void resume() {
		Gdx.app.log("GameScreen", "GameScreen was resumed");
	}

	@Override
	public void hide() {
		Gdx.app.log("GameScreen", "GameScreen was hidden");
	}

	@Override
	public void dispose() {
		Gdx.app.log("GameScreen", "GameScreen was disposed");
		batch.dispose();
	}
	
	private void handleInput() {
		final float sensitivity = 1.0f;
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            cam.translate(-sensitivity, 0, 0);
            //If the LEFT Key is pressed, translate the camera -3 units in the X-Axis
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            cam.translate(sensitivity, 0, 0);
            //If the RIGHT Key is pressed, translate the camera 3 units in the X-Axis
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            cam.translate(0, -sensitivity, 0);
            //If the DOWN Key is pressed, translate the camera -3 units in the Y-Axis
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            cam.translate(0, sensitivity, 0);
            //If the UP Key is pressed, translate the camera 3 units in the Y-Axis
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            cam.rotate(-0.2f, 0, 0, 1);
            //If the W Key is pressed, rotate the camera by -rotationSpeed around the Z-Axis
        }
        if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            cam.rotate(0.2f, 0, 0, 1);
            //If the E Key is pressed, rotate the camera by rotationSpeed around the Z-Axis
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
        	if (Gdx.graphics.isFullscreen()) {
        		Gdx.graphics.setWindowedMode(1280, 720);
        	} else {
        		Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        	}
        }
        
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
        	isCameraLocked = !isCameraLocked;
        } 
        if (isCameraLocked) {
        	cam.position.set(Vector3.Zero);
		} else {
			final float camEdge = 0.07f; // 7% of the screen can be used for cam movement
	        final float xCameraBound = Gdx.graphics.getWidth() * camEdge;
	        final float yCameraBound = Gdx.graphics.getHeight() * camEdge;
	        final float camSpeed = 1.0f;
	        
	        if (Gdx.input.getX() < xCameraBound) {
	        	cam.translate(-camSpeed, 0, 0);
	        } else if (Gdx.input.getX() > Gdx.graphics.getWidth() - xCameraBound) {
	        	cam.translate(camSpeed, 0, 0);
	        }
	        if (Gdx.input.getY() < yCameraBound) {
	        	cam.translate(0, camSpeed, 0);
	        } else if (Gdx.input.getY() > Gdx.graphics.getHeight() - yCameraBound) {
	        	cam.translate(0, -camSpeed, 0);
	        }
		}
	}
	
	private void setupInput() {
		
		Gdx.input.setInputProcessor(new InputProcessor() {
            @Override
            public boolean touchDown(int x, int y, int pointer, int button) {
                return false;
            }

			@Override
			public boolean keyDown(int keycode) {
				return false;
			}

			@Override
			public boolean keyUp(int keycode) {
				return false;
			}

			@Override
			public boolean keyTyped(char character) {
				return false;
			}

			@Override
			public boolean touchUp(int screenX, int screenY, int pointer, int button) {
				return false;
			}

			@Override
			public boolean touchDragged(int screenX, int screenY, int pointer) {
				return false;
			}

			@Override
			public boolean mouseMoved(int screenX, int screenY) {
				return false;
			}

			@Override
			public boolean scrolled(int amount) {
				if (cam.zoom < 0.1f) {
					cam.zoom += amount * 0.005f;
				} else {
					cam.zoom += amount * cam.zoom * 0.3;
				}
				return true;
			}
        });
		
    }
	//===== reduce FPS =======================	
//	private final static int FPSupdateIntervall = 1;  //--- display FPS alle x sekunden
//	private long lastRender;
//	private long now;
//	public int frameCount = 0;
//	public int lastFPS = 0;
//	
//	private final static int logic_FPSupdateIntervall = 1;  //--- display FPS alle x sekunden
//	private long logic_lastRender;
//	private long logic_now;
//	public int logic_frameCount = 0;
//	public int logic_lastFPS = 0;
//	
//	final float dt = Constants.DELTA_TIME; // logic updates approx. @ 75 hz. This if for update, render will use delta.
//	float accumulator;
//	/*
//	 * Called 60 times per second as per LibGdx specification.
//	 */
//	@Override
//	public void render(float delta) {
//		
////		frameCount++;
//		now = System.nanoTime();
//		
////		if ((now - lastRender) >= FPSupdateIntervall * 1000000000)  {
////			lastFPS = frameCount / FPSupdateIntervall;
////			frameCount = 0;
////			lastRender = System.nanoTime();
////		}    	
//
//		if (delta > 0.25f) {
//			delta = 0.25f;	  // max frame time to avoid spiral of death
//		}
//        
//        accumulator += delta;
//        while (accumulator >= dt) {
//        	
//        	InterpolationManager.copyCurrentPosition(engine);
//        	GdxAI.getTimepiece().update(dt);
//        	handleInput();
//        	cam.update();
//        	batch.setProjectionMatrix(cam.combined);
//        	world.step(dt, 6, 2); // 10, 8?
//        	engine.update(dt); // what is best order of world -> engine?
//        	
//        	accumulator -= dt;
//        	
//        	//---------- FPS check -----------------------------
////        	logic_frameCount++;
////        	logic_now = System.nanoTime();	// zeit loggen
////    		
////    		if ((logic_now - logic_lastRender) >= logic_FPSupdateIntervall * 1000000000)  {
////
////    			logic_lastFPS = logic_frameCount / logic_FPSupdateIntervall;		
////    			logic_frameCount = 0;
////    			logic_lastRender = System.nanoTime();
////    		}
//    		//--------------------------------------------------------------
//        }
//        InterpolationManager.interpolateCurrentPosition(accumulator / dt, engine);
//        debugRenderer.render(world, cam.combined);
////        DrawDebugLine(new Vector2(0, 0), new Vector2(5, 0), debugRenderer.AABB_COLOR, cam.combined);
////        DrawDebugLine(new Vector2(0, 2), new Vector2(5, 2), debugRenderer.JOINT_COLOR, cam.combined);
////        DrawDebugLine(new Vector2(0, 4), new Vector2(5, 4), debugRenderer.SHAPE_AWAKE, cam.combined);
////        DrawDebugLine(new Vector2(0, 6), new Vector2(5, 6), debugRenderer.SHAPE_KINEMATIC, cam.combined);
////        DrawDebugLine(new Vector2(0, 8), new Vector2(5, 8), debugRenderer.SHAPE_NOT_ACTIVE, cam.combined);
////        DrawDebugLine(new Vector2(0, 10), new Vector2(5, 10), debugRenderer.SHAPE_NOT_AWAKE, cam.combined);
////        DrawDebugLine(new Vector2(0, 12), new Vector2(5, 12), debugRenderer.SHAPE_STATIC, cam.combined);
////        DrawDebugLine(new Vector2(0, 14), new Vector2(5, 14), debugRenderer.VELOCITY_COLOR, cam.combined);
//		RenderingManager.render(batch, engine); // no rendering yet
//		
////		DynamicBodyComponent dc = Mappers.dynamicBodyMapper.get(chaser);
////		InterpolationComponent ic = Mappers.interpolationMapper.get(chaser);
////		System.out.println("");
////		Gdx.app.log("Interpolate New", dc.body.getPosition().toString()  );
////        Gdx.app.log("Interpolate Old", ic.oldPosition.toString());
////        Gdx.app.log("Interpolate Midpoint", ic.position.toString());
//	}
}
