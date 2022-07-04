package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainMenuScreen implements Screen {

	final ScreenManager game;
	boolean running;
	OrthographicCamera camera;

	public MainMenuScreen(final ScreenManager game, Lwjgl3ApplicationConfiguration config) {
		this.game = game;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1920, 1080);

	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
		ScreenUtils.clear(0, 0, 0.2f, 1);

		camera.update();
		game.batch.setProjectionMatrix(camera.combined);

		game.batch.begin();
		game.font.draw(game.batch, "Welcome to HELL!!! ", 100, 150);
		game.font.draw(game.batch, "Tap anywhere to begin!", 100, 100);
		game.batch.end();

		if (Gdx.input.isTouched()) {
			game.setScreen(game.gameScreen);
			//game.setScreen(new GameScreen(game));
		}

		Gdx.input.setInputProcessor(new MyInputProcessor() {
			@Override public boolean keyDown (int keycode) {
				switch (keycode) {
					case Input.Keys.LEFT: int x = 0; break;
				}
				return false;
			}
			@Override public boolean keyUp (int keycode) {
				switch (keycode) {
					case Keys.ESCAPE: {
						Gdx.app.exit();
						break;
					}
				}
				return false;
			}
			@Override
			public boolean touchDown (int x, int y, int pointer, int button) {
				return false;
			}
			@Override
			public boolean touchUp (int x, int y, int pointer, int button) {
				return false;
			}
		});
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
