package com.mygdx.game;

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
import com.mygdx.game.systems.SysManager;
import com.mygdx.game.templates.TestUnit;

import java.util.Iterator;

public class GameScreen implements Screen {
	final Drop game;

	Texture dropImage;
	Texture bucketImage;
	Sound dropSound;
	Music rainMusic;
	OrthographicCamera camera;
	Rectangle bucket;
	Array<Rectangle> raindrops;
	long lastDropTime;
	int dropsGathered;
	
	SysManager sysManager = new SysManager();
	Vector2 p = new Vector2(200,200);
	Vector2 d = new Vector2(800,800);

	public GameScreen(final Drop game) {
		TestUnit.createUnit(sysManager, p, d, 0); //Needs all the lists of components from ListSystem. Needs location/rally and which player from creator.
		this.game = game;
		// load the images for the droplet and the bucket, 64x64 pixels each
		dropImage = new Texture(Gdx.files.internal("unit.png"));
		bucketImage = new Texture(Gdx.files.internal("structure.png"));

		// load the drop sound effect and the rain background "music"
		dropSound = Gdx.audio.newSound(Gdx.files.internal("culture.mp3"));
		rainMusic = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
		rainMusic.setVolume((float) 0.2);
		rainMusic.setLooping(true);

		// create the camera and the SpriteBatch
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1920, 1080); //camera.setToOrtho(false, 640, 360);

		// create a Rectangle to logically represent the bucket
		bucket = new Rectangle();
		bucket.width = 100;
		bucket.height = 100;
		bucket.x = (camera.viewportWidth / 2) - (bucket.width / 2); // center the bucket horizontally
		bucket.y = 20; // bottom left corner of the bucket is 20 pixels above


		// create the raindrops array and spawn the first raindrop
		raindrops = new Array<Rectangle>();
		spawnRaindrop();

	}

	private void spawnRaindrop() {
		Rectangle raindrop = new Rectangle();
		raindrop.x = MathUtils.random(0, 1600 - 100);
		raindrop.y = 700;
		raindrop.width = 100;
		raindrop.height = 100;
		raindrops.add(raindrop);
		lastDropTime = TimeUtils.nanoTime();
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
		game.font.draw(game.batch, "Drops Collected: " + dropsGathered, 20, 880);
		game.batch.draw(bucketImage, bucket.x, bucket.y, bucket.width, bucket.height);
		for (Rectangle raindrop : raindrops) {
			game.batch.draw(dropImage, raindrop.x, raindrop.y);
		}
		game.batch.end();

		// process user input
		if (Gdx.input.isTouched()) {
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			bucket.x = touchPos.x - bucket.width / 2;
		}
		if (Gdx.input.isKeyPressed(Keys.LEFT))
			bucket.x -= 400 * Gdx.graphics.getDeltaTime();
		if (Gdx.input.isKeyPressed(Keys.RIGHT))
			bucket.x += 400 * Gdx.graphics.getDeltaTime();
		
		if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
			game.pause();
			game.setScreen(new MainMenuScreen(game));
		}
			

		if (bucket.x < 0)
			bucket.x = 0;
		if (bucket.x > 1600 - bucket.width)
			bucket.x = 1600 - bucket.width;
		
		if (Gdx.input.isKeyPressed(Keys.A) && camera.position.x - (camera.viewportWidth / 2) > 0)
			camera.translate(-400 * Gdx.graphics.getDeltaTime(), 0);
		if (Gdx.input.isKeyPressed(Keys.D) && camera.position.x + (camera.viewportWidth / 2) < 1600)
			camera.translate(400 * Gdx.graphics.getDeltaTime(), 0);
		if (Gdx.input.isKeyPressed(Keys.W) && camera.position.y + (camera.viewportHeight / 2) < 900)
			camera.translate(0, 400 * Gdx.graphics.getDeltaTime());
		if (Gdx.input.isKeyPressed(Keys.S) && camera.position.y - (camera.viewportHeight / 2) > 0)
			camera.translate(0, -400 * Gdx.graphics.getDeltaTime());

		// check if we need to create a new raindrop
		if (TimeUtils.nanoTime() - lastDropTime > 1000000000)
			spawnRaindrop();

		// move the raindrops, remove any that are beneath the bottom edge of
		// the screen or that hit the bucket. In the later case we increase the
		// value our drops counter and add a sound effect.
		Iterator<Rectangle> iter = raindrops.iterator();
		while (iter.hasNext()) {
			Rectangle raindrop = iter.next();
			raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
			if (raindrop.y + 64 < 0)
				iter.remove();
			if (raindrop.overlaps(bucket)) {
				dropsGathered++;
				dropSound.play((float) 0.1);
				iter.remove();
			}
		}
	sysManager.moveSystem.updatePosition(delta);
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
		dropImage.dispose();
		bucketImage.dispose();
		dropSound.dispose();
		rainMusic.dispose();
	}

}