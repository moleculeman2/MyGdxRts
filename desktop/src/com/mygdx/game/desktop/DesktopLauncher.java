package com.mygdx.game.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3WindowAdapter;
import com.mygdx.game.ScreenManager;

import java.awt.*;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

		/**
		config.title = "RTS prototype";
		config.width = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getWidth();
		config.height = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getHeight();
		config.undecorated = true;
		config.foregroundFPS = 0;
		config.backgroundFPS = 0;
		config.fullscreen = false;
		config.forceExit = true;
		config.vSyncEnabled = false;
		//new LwjglApplication(new Drop(), config);
		 **/
		config.setTitle("RTS prototype");

		config.setDecorated(false);
		config.setForegroundFPS(0);
		config.useVsync(false);
		config.setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode());
		config.setAutoIconify(true);
		config.setWindowedMode(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getWidth(),
				GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getHeight());
		config.setWindowListener(new Lwjgl3WindowAdapter() {
			@Override
			public void maximized(boolean isMaximized) {
				if(isMaximized)
					Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
				else
					Gdx.graphics.setWindowedMode(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getWidth(),
							GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getHeight());
			}

			@Override
			public void focusLost() {
				Gdx.app.postRunnable(new Runnable() {
					@Override
					public void run() {
						maximized(false);
					}
				});
			}
			@Override
			public void focusGained() {
				Gdx.app.postRunnable(new Runnable() {
					@Override
					public void run() {
						maximized(false);
					}
				});
			}


		});
		Lwjgl3Application x = new Lwjgl3Application(new ScreenManager(config), config);

	}
}
