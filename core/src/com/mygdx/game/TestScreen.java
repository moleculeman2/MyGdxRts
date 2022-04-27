package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.components.Position;
import com.mygdx.game.systems.SysManager;
import com.mygdx.game.templates.TestUnit;

public class TestScreen implements Screen {
	final ScreenManager game;
	Texture unitImg;
	Sound dropSound;
	Music rainMusic;
	OrthographicCamera camera;
	OrthographicCamera hudCamera;
	Rectangle bucket;
	Array<Rectangle> raindrops;
	Lwjgl3ApplicationConfiguration config;

	
	SysManager sysManager;
	Vector2 p = new Vector2(200,200);
	Vector2 d = new Vector2(800,800);

	public TestScreen(final ScreenManager game, Lwjgl3ApplicationConfiguration config) {
		this.game = game;
		this.config = config;
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

	}

	@Override
	public void render(float delta) {
		ScreenUtils.clear(0, 0, 0.2f, 1);
		
		

		// tell the camera to update its matrices.
		camera.update();
		hudCamera.update();

		// tell the SpriteBatch to render in the
		// coordinate system specified by the camera.
		game.batch.setProjectionMatrix(camera.combined);
		game.batch.setProjectionMatrix(hudCamera.combined);
		// begin a new batch and draw the bucket and all drops

		
		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			game.pause();
			game.setScreen(new MainMenuScreen(game, config));
		}



		sysManager.moveSystem.updatePosition(delta);
		game.batch.begin();
		game.font.draw(game.batch, "Upper left, FPS=" + Gdx.graphics.getFramesPerSecond(), 0, hudCamera.viewportHeight);
		game.font.draw(game.batch, "Upper left,  real FPS=" + Math.round(1/Gdx.graphics.getDeltaTime()), 500, hudCamera.viewportHeight);
		game.font.draw(game.batch, "Lower left", 0, game.font.getLineHeight());
		//for my game, I would loop through all rectangle components and get its ID
		//Then, lookup that ID in the "sprites" component list, and draw it at rectangle
		for (Position p : sysManager.moveSystem.getPositionList()){
			game.batch.draw(p.getSprite(), p.getPosition().x, p.getPosition().y);
			game.font.draw(game.batch, "Pos", p.getPosition().x, p.getPosition().y);
			if (p.getDestination() != null){
				game.font.draw(game.batch, "Des", p.getDestination().x, p.getDestination().y);
			}
		}

		//for (Rectangle raindrop : raindrops) {
		//game.batch.draw(dropImage, raindrop.x, raindrop.y); }
		game.batch.end();
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