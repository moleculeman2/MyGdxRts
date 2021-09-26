package com.mygdx.game;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.systems.HpSystem;
import com.mygdx.game.systems.SysManager;
import com.mygdx.game.templates.Kharvaach;
import com.mygdx.game.templates.TestUnit;

public class TestScreen implements Screen {
	final Drop game;
	Texture unitImg;
	Sound dropSound;
	Music rainMusic;
	OrthographicCamera camera;
	Rectangle bucket;
	Array<Rectangle> raindrops;

	
	SysManager sysManager = new SysManager();
	Vector2 p = new Vector2(200,200);
	Vector2 d = new Vector2(800,800);

	public TestScreen(final Drop game) {
		this.game = game;
		unitImg = new Texture(Gdx.files.internal("unit.png"));

		// load the drop sound effect and the rain background "music"
		dropSound = Gdx.audio.newSound(Gdx.files.internal("culture.mp3"));
		rainMusic = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
		rainMusic.setVolume((float) 0.2);
		rainMusic.setLooping(true);

		// create the camera and the SpriteBatch
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1920, 1080); //camera.setToOrtho(false, 640, 360);
		
		TestUnit.createUnit(sysManager, p, d, 0); //Needs all the lists of components from ListSystem. Needs location/rally and which player from creator.

	}

	@Override
	public void render(float delta) {
		ScreenUtils.clear(0, 0, 0.2f, 1);
		
		

		// tell the camera to update its matrices.
		camera.update();

		// tell the SpriteBatch to render in the
		// coordinate system specified by the camera.
		game.batch.setProjectionMatrix(camera.combined);

		// begin a new batch and draw the bucket and
		// all drops
		game.batch.begin();
		//for my game, I would loop through all rectangle components and get its ID
		//Then, lookup that ID in the "sprites" component list, and draw it at rectangle 
		
		
		//for (Rectangle raindrop : raindrops) {
			//game.batch.draw(dropImage, raindrop.x, raindrop.y); }
		game.batch.end();
		
		if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
			game.pause();
			game.setScreen(new MainMenuScreen(game));
		}

		sysManager.moveSystem.updatePosition();
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
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		unitImg.dispose();
		dropSound.dispose();
		rainMusic.dispose();
	}

}