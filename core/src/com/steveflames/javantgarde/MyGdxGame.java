package com.steveflames.javantgarde;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
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
 * COMPILER: use java code formatter library to format code to Google Java Format
 * EDITOR: remake codeArea
 *
 * create CUSTOM ONSCREEN KEYBOARD (html/web version no android keyboard)
 *
 * TODO minor
 * POOL objects
 * add loading screen random programming tip
 * new custom infosign window with scroll (no dialog)
 * make scifi tilesets power of 2 (1024x1024) and rmk maps
 * #load/unload SKINS resume/pause. constructors call refresh method. resume calls refresh method aswell
 * #lockpad textureregion dispose, recreate but on swap windows (click) resume is not called (no recreation)
 *
 *
 * ---------------LEVELS TO ADD---------------
 * INTRO STAGE: indentation (spacing)
 * CMD COMPILE
 * package? import?
 * LOCAL VARIABLES (SCOPE)
 * getters setters
 * inheritance
 * interfaces
 * useful java classes
 * files
 * exceptions
 * -------------------------------------------
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
	public boolean gameMinimized = false;

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
		assets.unloadAllMainMenuAssets(true);
		assets.unloadAllPlayScreenAssets();
		Fonts.dispose();
	}

	public void drawMinimized() {
		sr.setColor(Color.BLACK);
		sr.begin(ShapeRenderer.ShapeType.Filled);
		sr.rect(0, 0, MyGdxGame.WIDTH, MyGdxGame.HEIGHT);
		sr.end();
		Fonts.big.setColor(Color.RED);
		Fonts.medium.setColor(Color.WHITE);
		sb.begin();
		Fonts.big.draw(sb, "jAVANT-GARDE", MyGdxGame.WIDTH/2-390, MyGdxGame.HEIGHT/2 + 100);
		if(Fonts.languageShort.equals("en"))
			Fonts.medium.draw(sb, "PAUSED", MyGdxGame.WIDTH / 2 - 120, MyGdxGame.HEIGHT / 2 - 10);
		else if(Fonts.languageShort.equals("gr"))
			Fonts.medium.draw(sb, "ΠΑΥΣΗ", MyGdxGame.WIDTH / 2 - 120, MyGdxGame.HEIGHT / 2 - 10);
		sb.end();
	}
}
