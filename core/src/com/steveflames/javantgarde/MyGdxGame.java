package com.steveflames.javantgarde;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.steveflames.javantgarde.screens.ChooseLevelScreen;
import com.steveflames.javantgarde.screens.MainMenuScreen;
import com.steveflames.javantgarde.tools.Assets;
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
 * unload skins?
 * COMPILER: if the user types { or } on a println there's a problem
 * COMPILER: add compiler errors: (method doesn't exist, var not declared)
 * EDITOR: ston editor otan afhnw 1h grammh keno k kanw click varaei error index out of bounds -1
 * add greek
 * sto loading screen na deixnei se poia pista eimai
 * SOUNDS TODO
 * na prepei na termatiseis mia pista g na ksekleidwseis thn epomenh. html ola ksekleidwmena
 *
 *
 * ---------------LEVELS TO ADD---------------
 * na tonisw oti kathe entolh grafetai se mia kainouria grammh
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

	public static final int WIDTH = 1280; //1185, 854, 1920, 1280
	public static final int HEIGHT = 768; //600, 480,  1080, 768
	public static final float PPM = 200; //pixels per meter. has to do with b2body scaling
	public static final String TITLE = "jAVANT-GARDE";
	public static boolean musicOn = true;
	public static boolean sfxOn = true;

	public SpriteBatch sb; //used to render textures
	public ShapeRenderer sr; //used to render shapes

	public static iPlatformDepended platformDepended;
	public Assets assets;
	public Music mainMenuMusic;

	public MyGdxGame(iPlatformDepended platformDepended) {
		MyGdxGame.platformDepended = platformDepended;
		assets  = new Assets();
	}

	@Override
	public void create () {
		Fonts.load();
		//Skins.load();
		sb = new SpriteBatch();
		sr = new ShapeRenderer();
		assets.loadAllMainMenuAssets();
		assets.finishLoading();
		mainMenuMusic = assets.get(Assets.mainMenuMUSIC, Music.class);
		mainMenuMusic.setLooping(true);
		mainMenuMusic.play();
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
		Fonts.dispose();
		//Loader.dispose();
	}
}
