package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
public class ScreenManager extends Game {

	public SpriteBatch batch;
	public BitmapFont font;
	Screen gameScreen;
	Screen menuScreen;
	Lwjgl3ApplicationConfiguration config;

	public ScreenManager(Lwjgl3ApplicationConfiguration config) {
		this.config = config;
	}


	public void create() {
		batch = new SpriteBatch();
		font = new BitmapFont(); // use libGDX's default Arial font
		gameScreen = new TestScreen(this, config);
		menuScreen = new MainMenuScreen(this, config);
		this.setScreen(menuScreen);
	}

	public void render() {
		super.render(); // important!
	}

	public void dispose() {
		batch.dispose();
		font.dispose();
	}

}
