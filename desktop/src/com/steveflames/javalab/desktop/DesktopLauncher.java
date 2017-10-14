package com.steveflames.javalab.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.steveflames.javalab.MyGdxGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = MyGdxGame.WIDTH; //1220
		config.height = MyGdxGame.HEIGHT; //630
		config.title = MyGdxGame.TITLE;
		//config.resizable = false;
		//config.addIcon("images/logo.png", Files.FileType.Internal);
		new LwjglApplication(new MyGdxGame(), config);
	}
}
