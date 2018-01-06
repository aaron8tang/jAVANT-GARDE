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
 * THOUGHTS
 * ARRAYS! parse array k printf k prepei na pas sto swsto. an lathos tote -1 health
 * IF + VARIABLES! na pas px na vlepeis ton kwdika ths portas k na exei if(x > 100) open k na prepei na pas na to kaneis
 * VARIABLES! na paizeis me tis suntetagmenes tou kleidiou gia na anoikseis thn porta
 * na vriskei polles klaseis k na prepei na tis diavasei k na ktlvei poia einai h swsth gia na xrhsimopoihsei
 * na perpatan bugs sthn othoni pou se sprwxnoun
 *
 *
 * TODO
 * unload skins? if i unload and reload i must refresh all stage actors
 * COMPILER: if the user types { or } on a println, uneven brackets error is triggered
 * COMPILER: add compiler errors: (method doesn't exist, var not declared)
 * EDITOR: ston editor otan afhnw 1h grammh keno k kanw click varaei error index out of bounds -1
 * add greek
 * javalanche rename?
 * fix resolution (use FitViewport? instead of StretchViewport)
 * POOL objects
 *
 *
 * ---------------LEVELS TO ADD---------------
 * point out that each command is written on a new line (line of code)
 * method BODY, class BODY
 * indentation (spacing)
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

	public SpriteBatch sb; //used to render textures
	public ShapeRenderer sr; //used to render shapes

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
		/*WIDTH = (Gdx.graphics.getWidth()/Gdx.graphics.getHeight())*HEIGHT*2;
		if(WIDTH > Gdx.graphics.getWidth())
			WIDTH = Gdx.graphics.getWidth();*/
		Fonts.load();
		sb = new SpriteBatch();
		sr = new ShapeRenderer();
		musicOn = preferences.isMusicEnabled();
		sfxOn = preferences.isSoundEffectsEnabled();
		setScreen(new MainMenuScreen(this));
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
