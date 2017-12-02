package com.steveflames.javantgarde.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = com.steveflames.javantgarde.MyGdxGame.WIDTH; //1220
		config.height = com.steveflames.javantgarde.MyGdxGame.HEIGHT; //630
		config.title = com.steveflames.javantgarde.MyGdxGame.TITLE;
		config.y = 0;
		//config.resizable = false;
		config.addIcon("images/logo16.png", Files.FileType.Internal);
		config.addIcon("images/logo32.png", Files.FileType.Internal);
		config.addIcon("images/logo128.png", Files.FileType.Internal);
		new LwjglApplication(new com.steveflames.javantgarde.MyGdxGame(new PlatformDepended()), config);
	}
}
