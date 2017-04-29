package com.mweis.game.view;


import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.msg.MessageDispatcher;
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
import com.mweis.game.gfx.RenderingManager;
import com.mweis.game.util.Constants;
import com.mweis.game.util.Mappers;
import com.mweis.game.util.Messages;
import com.mweis.game.world.WorldFactory;

import box2dLight.PointLight;
import box2dLight.RayHandler;

public class GameScreen implements Screen {
	
	private Engine engine;
//	private Entity target, chaser;
	
	private OrthographicCamera cam;
	private SpriteBatch batch;
	    
    private World world;
    private Box2DDebugRenderer debugRenderer;
//	private RayHandler rayHandler;
    
    private boolean isCameraLocked;
	
    /*
     * TODO
     * Need to abstract Agents more ?
     * Need to make SteeringComponent only hold a SteeringClass
     */
    
    
	@Override
	public void show() {
		Gdx.app.log("GameScreen", "GameScreen is now being shown");
		
		float w = 720.0f/1.0f; //  /8 = 90
		float h = 480.0f/1.0f; //  /8 = 60
		cam = new OrthographicCamera(w, h);
		batch = new SpriteBatch();
        world = WorldFactory.generateWorld(true);
//        rayHandler = new RayHandler(world);
        debugRenderer = new Box2DDebugRenderer();
        
        engine = new Engine();
		engine.addSystem(new AgentSystem());
		
		// make 2 entities, one of which chases the other
		boolean independentFacing = false;
		Entity target = EntityFactory.spawnZombie(5, 0, world, engine, false, 0.60f, independentFacing);
		Entity chaser = EntityFactory.spawnZombie(0, 5, world, engine, false, 0.60f, independentFacing);
		Entity player = EntityFactory.spawnPlayer(0, 0, world, engine, 1.0f);
		
		AgentComponent<ZombieAgent> zombie1 = Mappers.agentMapper.get(target);
		AgentComponent<ZombieAgent> zombie2 = Mappers.agentMapper.get(chaser);
		
		zombie1.agent.setWander();
		zombie1.agent.setPursue(Mappers.steeringMapper.get(player));
		zombie2.agent.setWander();
		zombie2.agent.setPursue(Mappers.steeringMapper.get(player));
		
		zombie1.agent.fsm.changeState(ZombieState.PURSUE);
		zombie2.agent.fsm.changeState(ZombieState.PURSUE);

	
		// setup input (this class is the listener)
		setupInput();
		spawnTestLights();
		initMessaging();
	}
	
	private void initMessaging() {
		
	}
	
	private void spawnTestLights() {
		final int RAYS_NUM = 5000;
		final float lightDistance = 40.0f;
//		new PointLight(rayHandler, RAYS_NUM, new Color(0.75f,0.75f,0.75f,1), lightDistance, 5.0f, 0.0f);
//		new PointLight(rayHandler, RAYS_NUM, new Color(0.75f,0.25f,0.75f,1), lightDistance, 30.0f, 45.0f);
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
    	MessageManager.getInstance().update();
    	world.step(GdxAI.getTimepiece().getDeltaTime(), 8, 3);
    	Matrix4 drawMat = new Matrix4(cam.combined);
		drawMat.scl(Constants.PPM);
//		rayHandler.update();
    	engine.update(GdxAI.getTimepiece().getDeltaTime()); // what is best order of world -> engine?
	}
	
	private void draw() {
		Matrix4 drawMat = new Matrix4(cam.combined);
		drawMat.scl(Constants.PPM);
		
//		RenderingManager.render(cam.combined, batch, engine);
//		RenderingManager.render(drawMat, batch, engine);
//		rayHandler.setCombinedMatrix(drawMat);
//		rayHandler.render();
		debugRenderer.render(world, drawMat);//drawMat);
//		debugRenderer.render(world, cam.combined);
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
//		rayHandler.dispose();
	}
	
	private void handleInput() {
		final float sensitivity = 1.0f * cam.zoom;
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
//        	cam.position.set(Vector3.Zero);
        } else if (!isCameraLocked) {
			final float camEdge = 0.07f; // 7% of the screen can be used for cam movement
	        final float xCameraBound = Gdx.graphics.getWidth() * camEdge;
	        final float yCameraBound = Gdx.graphics.getHeight() * camEdge;
	        final float camSpeed = 3.0f * cam.zoom;
	        
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
        
	 if (Gdx.input.isButtonPressed(Buttons.RIGHT)) {
     	vec.x = Gdx.input.getX();
     	vec.y = Gdx.input.getY();
     	Vector3 mouse3 = cam.unproject(vec);
     	Vector2 worldCoords = new Vector2(mouse3.x, mouse3.y);
     	worldCoords.scl(Constants.MPP);
     	
     	MessageManager.getInstance().dispatchMessage(Messages.PLAYER_MOVE_TO_X, worldCoords);
     }
	}
	
	Vector3 vec = new Vector3();
	private void setupInput() {
		
		Gdx.input.setInputProcessor(new InputProcessor() {
            @Override
            public boolean touchDown(int x, int y, int pointer, int button) {
//                if (button == Input.Buttons.RIGHT) {
//                	vec.x = x;
//                	vec.y = y;
//                	Vector3 mouse3 = cam.unproject(vec);
//                	Vector2 worldCoords = new Vector2(mouse3.x, mouse3.y);
//                	worldCoords.scl(Constants.MPP);
//                	
//                	MessageManager.getInstance().dispatchMessage(Messages.PLAYER_MOVE_TO_X, worldCoords);
//                	
//                	return true;
//                }
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
