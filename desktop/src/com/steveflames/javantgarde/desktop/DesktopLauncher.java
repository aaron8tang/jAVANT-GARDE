package com.steveflames.javantgarde.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.steveflames.javantgarde.MyGdxGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1280;
		config.height = 768;
		config.title = MyGdxGame.TITLE;
		config.y = 0;
		//config.resizable = false;
		config.addIcon("images/logo16.png", Files.FileType.Internal);
		config.addIcon("images/logo32.png", Files.FileType.Internal);
		config.addIcon("images/logo128.png", Files.FileType.Internal);
		new LwjglApplication(new MyGdxGame(new PlatformDepended()), config);
	}
}
