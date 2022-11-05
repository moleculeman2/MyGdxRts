package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.Graph;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.components.BoundingBox;
import com.mygdx.game.components.Position;
import com.mygdx.game.systems.SysManager;
import com.mygdx.game.templates.TestUnit;

import static java.lang.Math.abs;
import static java.lang.Math.min;

public class TestScreen implements Screen {
	final ScreenManager game;
	Texture unitImg;
	Sound dropSound;
	Music rainMusic;
	OrthographicCamera camera;
	OrthographicCamera hudCamera;
	Array<Rectangle> raindrops;
	Lwjgl3ApplicationConfiguration config;
	MyInputProcessor inputProcessor;
	Vector3 clickedPos = new Vector3(0,0,0);
	Vector3 currentPos = new Vector3(0,0,0);
	boolean clicked;
	boolean shiftMod;
	Rectangle selectBox = new Rectangle();
	Array<Position> hoveredList = new Array<Position>(false, 100);
	Array<Position> selectedList = new Array<Position>(false, 100);

	SysManager sysManager;
	Vector2 p = new Vector2(200,200);
	Vector2 d = new Vector2(800,800);
	Vector2 center;
	Vector2 center2;

	public TestScreen(final ScreenManager game, Lwjgl3ApplicationConfiguration config) {
		this.game = game;
		this.config = config;
		inputProcessor = new MyInputProcessor();
		Gdx.input.setInputProcessor(inputProcessor);
		//config.resizable = false;
		//Gdx.graphics.setUndecorated(true);
		sysManager  = new SysManager();
		unitImg = new Texture(Gdx.files.internal("unit.png"));

		// load the drop sound effect and the rain background "music"
		dropSound = Gdx.audio.newSound(Gdx.files.internal("culture.mp3"));
		rainMusic = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
		rainMusic.setVolume((float) 0.2);
		rainMusic.setLooping(true);

		// create the camera and the SpriteBatch
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1920, 1080); //camera.setToOrtho(false, 640, 360);
		hudCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		hudCamera.position.set(hudCamera.viewportWidth / 2.0f, hudCamera.viewportHeight / 2.0f, 1.0f);

		TestUnit.createUnit(sysManager, p, d, 0); //Needs all the lists of components from ListSystem. Needs location/rally and which player from creator.
		TestUnit.createUnit(sysManager, new Vector2(200,200), new Vector2(100, 1000), 0);
	}

	@Override
	public void render(float delta) {

		ScreenUtils.clear(0, 0, 0.2f, 1);
		inputProcessorInit();

		// tell the camera to update its matrices.
		camera.update();
		hudCamera.update();

		// tell the SpriteBatch to render in the
		// coordinate system specified by the camera.
		game.batch.setProjectionMatrix(camera.combined);
		game.batch.setProjectionMatrix(hudCamera.combined);

		sysManager.moveSystem.updatePosition(delta);
		game.batch.begin();
		game.font.draw(game.batch, "Upper left, FPS=" + Gdx.graphics.getFramesPerSecond(), 0, hudCamera.viewportHeight);
		game.font.draw(game.batch, "Upper left,  real FPS=" + Math.round(1/Gdx.graphics.getDeltaTime()), 500, hudCamera.viewportHeight);
		game.font.draw(game.batch, "Lower left", 0, game.font.getLineHeight());
		//for my game, I would loop through all rectangle components and get its ID
		//Then, lookup that ID in the "sprites" component list, and draw it at rectangle

		//If new units hovered and released on, clears the old selected units
		if (hoveredList.notEmpty() && !clicked){
			selectedList.clear();
		}
		for (Position p : sysManager.moveSystem.getPositionList()){
			if (p.getDestination() == null && p.getMoveQueue().notEmpty()){
				p.setDestination(p.getMoveQueue().removeFirst());
			}
			if (!(selectBox.overlaps(p.getBox().getBoundingBox()))){ //remove units that passed outside select box
				hoveredList.removeValue(p, true);
			}
			if (clicked && p.getPlayer() == 0 && selectBox.overlaps(p.getBox().getBoundingBox())){ //highlight player units in selectbox
				if (!(hoveredList.contains(p, true))){hoveredList.add(p);}
				game.batch.setColor(0.6F, 0.9F,0.6F, 1);
				game.batch.draw(p.getSprite(), p.getBox().getBoundingBox().getX(), p.getBox().getBoundingBox().getY());
			}
			else if (!clicked && hoveredList.contains(p, true)){ //if click release, remove add to selected and remove from hover
				selectedList.add(p);
				hoveredList.removeValue(p, true);
			}
			else if(selectedList.contains(p, true)){ // if selected, draw as green
				game.batch.setColor(0, 1,0, 1);
				game.batch.draw(p.getSprite(), p.getBox().getBoundingBox().getX(), p.getBox().getBoundingBox().getY());
			}
			else { //render everything else normally
				game.batch.setColor(1, 1,1,1);
				game.batch.draw(p.getSprite(), p.getBox().getBoundingBox().getX(), p.getBox().getBoundingBox().getY());
			}



			game.font.draw(game.batch, "Pos", p.getPosition().x, p.getPosition().y);
			if (p.getDestination() != null){
				game.font.draw(game.batch, "Des", p.getDestination().x, p.getDestination().y);
			}
		}
		game.batch.end();

		if (clicked){
			camera.unproject(currentPos.set(Gdx.input.getX(),Gdx.input.getY(),0));
			DebugUtils.DrawDebugLine(clickedPos.x, clickedPos.y, clickedPos.x, currentPos.y, 3 , Color.GREEN, camera.combined);
			DebugUtils.DrawDebugLine(clickedPos.x, clickedPos.y, currentPos.x, clickedPos.y, 3 , Color.GREEN, camera.combined);
			DebugUtils.DrawDebugLine(currentPos.x, currentPos.y, clickedPos.x, currentPos.y, 3 , Color.GREEN, camera.combined);
			DebugUtils.DrawDebugLine(currentPos.x, currentPos.y, currentPos.x, clickedPos.y, 3 , Color.GREEN, camera.combined);
		}

		if (!clicked) { selectBox.set(0,0,0,0);}
	}

	private void inputProcessorInit() {
		if (clicked) { selectBox.set(min(clickedPos.x, currentPos.x), min(clickedPos.y, currentPos.y),
				abs(clickedPos.x - currentPos.x), abs(clickedPos.y - currentPos.y));}
		Gdx.input.setInputProcessor(new MyInputProcessor() {
			@Override public boolean keyDown (int keycode) {
				switch (keycode) {
					case Keys.SHIFT_LEFT: {
						shiftMod = true;
					}
				}
				return false;
			}
			@Override public boolean keyUp (int keycode) {
				switch (keycode) {
					case Keys.SHIFT_LEFT: {
						shiftMod = false;
						break;
					}
					case Keys.ESCAPE: {
						game.pause();
						game.setScreen(new MainMenuScreen(game, config));
						break;
					}

				}
				return false;
			}
			@Override
			public boolean touchDown (int x, int y, int pointer, int button) {
				switch (button) {
					case Input.Buttons.LEFT:{
						clicked = true;
						camera.unproject(clickedPos.set(Gdx.input.getX(),Gdx.input.getY(),0));
						camera.unproject(currentPos.set(Gdx.input.getX(),Gdx.input.getY(),0));
						selectBox.set(min(clickedPos.x, currentPos.x), min(clickedPos.y, currentPos.y),
								abs(clickedPos.x - currentPos.x), abs(clickedPos.y - currentPos.y));
						break;
					}
					case Input.Buttons.RIGHT:{
						camera.unproject(clickedPos.set(Gdx.input.getX(),Gdx.input.getY(),0));
						for (Position p : selectedList){
							if (shiftMod){
								p.getMoveQueue().addLast(new Vector2(clickedPos.x,clickedPos.y));
							}
							else{
								p.getMoveQueue().clear();
								p.setDestination(new Vector2(clickedPos.x,clickedPos.y));
							}

							//
						}
						break;
					}
				}
				return false;
			}
			@Override
			public boolean touchUp (int x, int y, int pointer, int button) {
				switch (button) {
					case Input.Buttons.LEFT:{
						clicked = false;
						break;
					}
				}
				return false;
			}
		});
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		// start the playback of the background music
		// when the screen is shown
		rainMusic.play();
	}

	@Override
	public void hide() {
		rainMusic.pause();
	}

	@Override
	public void pause() {
		rainMusic.pause();
	}

	@Override
	public void resume() {
		rainMusic.play();
	}

	@Override
	public void dispose() {
		unitImg.dispose();
		dropSound.dispose();
		rainMusic.dispose();
	}

}