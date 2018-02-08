package com.steveflames.javantgarde.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.steveflames.javantgarde.MyGdxGame;

/**
 * The starting point (main) of the game when run on Desktop.
 * Window and icon configurations are set and
 * after the PlatformDepended class is initialized, the flow
 * of the program moves to the core (MyGdxGame class).
 */

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1280; //window width
		config.height = 768; //window height
		config.title = MyGdxGame.TITLE; //window title
		config.y = 0; //window y coordinate
		//config.fullscreen = true;
		//config.resizable = false;

		//desktop logo icons
		config.addIcon("images/logo128.png", Files.FileType.Internal);
		config.addIcon("images/logo32.png", Files.FileType.Internal);
		config.addIcon("images/logo16.png", Files.FileType.Internal);

		new LwjglApplication(new MyGdxGame(new PlatformDepended()), config);
	}
}
