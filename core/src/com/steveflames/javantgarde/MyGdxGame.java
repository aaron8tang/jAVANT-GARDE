package com.steveflames.javantgarde;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.steveflames.javantgarde.screens.MainMenuScreen;
import com.steveflames.javantgarde.tools.Assets;
import com.steveflames.javantgarde.tools.MyPreferences;
import com.steveflames.javantgarde.tools.global.Fonts;

/**
 * This class initializes the basic parameters of the games
 * and sets the first screen of the game (MainMenuScreen).
 *
 *
 * TODO NOTES
 * COMPILER: if the user types { or } on a println, uneven brackets error is triggered
 * COMPILER: add compiler errors: (method doesn't exist, var not declared)
 * EDITOR: remake codeArea
 * add greek
 * MAKE TILESETS POWER OF 2 RESOLUTION (1024x1024) and rmk maps
 * skins enable MIPMAP
 * load/unload skins resume/pause. constructors call refresh method. resume calls refresh method aswell
 * start displaying the PlayScreen after world initialization (idea: put b2world creation in loading screen and pass as parameter)
 * html/web version no android keyboard..
 * TODO minor
 * POOL objects
 * add loading screen random programming tip
 * hide/show game, draw black rect on whole screen
 *
 * ---------------LEVELS TO ADD---------------
 * INTRO STAGE: point out that each command is written on a new line (line of code)
 * INTRO STAGE: method BODY, class BODY
 * INTRO STAGE: indentation (spacing)
 * package? import?
 * LOCAL VARIABLES (SCOPE)
 * getters setters
 * inheritance
 * interfaces
 * useful java classes
 * files
 * exceptions
 * -------------------------------------------
 *
 */
public class MyGdxGame extends Game {

	public static final int WIDTH = 1280; //800, 1920, 1280, 32
	public static final int HEIGHT = 768; //480,  1080, 768, 16
	public static final float PPM = 100; //pixels per meter. has to do with b2body and world scaling
	public static final String TITLE = "jAVANT-GARDE";
	public static boolean musicOn;
	public static boolean sfxOn;

	public SpriteBatch sb; //utilized to render textures
	public ShapeRenderer sr; //utilized to render shapes

	public static iPlatformDepended platformDepended;
	public Assets assets;
	public MyPreferences preferences;

	public MyGdxGame(iPlatformDepended platformDepended) {
		MyGdxGame.platformDepended = platformDepended;
		assets  = new Assets();
		preferences = new MyPreferences();
	}

	@Override
	public void create () {
		Fonts.load();
		sb = new SpriteBatch();
		sr = new ShapeRenderer();
		musicOn = preferences.isMusicEnabled();
		sfxOn = preferences.isSoundEffectsEnabled();
		setScreen(new MainMenuScreen(this)); //sets the first screen of the game, MainMenuScreen
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		super.render();
	}
	
	@Override
	public void dispose () {
		sb.dispose();
		sr.dispose();
		assets.unloadAllMainMenuAssets();
		assets.unloadAllPlayScreenAssets();
		Fonts.dispose();
	}
}
