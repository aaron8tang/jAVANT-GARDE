package com.steveflames.javantgarde;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.steveflames.javantgarde.screens.ChooseLevelScreen;
import com.steveflames.javantgarde.screens.PlayScreen;
import com.steveflames.javantgarde.tools.LevelListItem;
import com.steveflames.javantgarde.tools.global.Cameras;
import com.steveflames.javantgarde.tools.global.Fonts;
import com.steveflames.javantgarde.tools.global.Loader;
import com.steveflames.javantgarde.tools.global.Skins;

/**
 * TODO:
 *
 * MISC
 * java cofe
 * granazia
 * matrix lines
 * find free emojis gia decor
 * verbose/laconic
 * na grafei kapou tis suntetagmenes tou paixth
 * analoga me to life s sto telos pairneis toso exp. me exp ksekleidwneis ta next lvls
 *
 *
 * THOUGHTS
 * ARRAYS! parse array k printf k prepei na pas sto swsto. an lathos tote -1 health
 * IF + VARIABLES! na pas px na vlepeis ton kwdika ths portas k na exei if(x > 100) open k na prepei na pas na to kaneis
 * VARIABLES! na paizeis me tis suntetagmenes tou kleidiou gia na anoikseis thn porta
 * otan kaneis click sto console na kanei copy to clipboard
 * tha erxontai kata panw m opws super mario san tis xelwnes me swsta k lathos k prepei na phdhksw
 * meta apo kathe kleisth porta na zwgrafizw ena mavro rect g na mh vlepei o paikths parakatw
 * na vriskei polles klaseis k na prepei na tis diavasei k na ktlvei poia einai h swsth gia na xrhsimopoihsei
 *
 *
 * COMPILER
 * to className einai ontws MyClass?? check k ERROR
 * variable already declared in scope ERROR
 * method doesn't exist ERROR
 * package? import? prin to class declaration
 *
 * TODO
 * na dw ta TODO
 * ston editor otan afhnw 1h grammh keno k kanw click varaei error index out of bounds -1
 * kapoios kwdikas sta hints einai polu megalos k paei apo katw gt to label kanei warp
 * ftiakse ton loader me assetmanager gia na borw na valw k progress bar sthn arxh p fortwnei
 * na tonisw oti kathe entolh grafetai se mia kainouria grammh
 * StartMenuScreen
 *
 * fields.. local variables.. modifiers
 * CLASSES
 * METHODS
 * IF
 * LOOP
 * ARRAY
 *
 *
 * PARSE
 * parse ton kwdika
 * METHODS
 * FIELDS
 *
 */
public class MyGdxGame extends Game {

	public static final int WIDTH = 1280; //1185, 854, 1920, 1280
	public static final int HEIGHT = 768; //600, 480,  1080, 768
	public static final float PPM = 200; //pixels per meter. has to do with b2body scaling
	public static final String TITLE = "jAVANT-GARDE";

	public SpriteBatch sb; //used to render textures
	public ShapeRenderer sr; //used to render shapes

	public static iPlatformDepended platformDepended;
	//public AssetManager assetManager = new AssetManager(); //TODO

	public MyGdxGame(iPlatformDepended platformDepended) {
		MyGdxGame.platformDepended = platformDepended;
	}

	@Override
	public void create () {
		Fonts.load();
		Skins.load();
		sb = new SpriteBatch();
		sr = new ShapeRenderer();
		//setScreen(new ChooseLevelScreen(this));
		setScreen(new PlayScreen(this, new LevelListItem("COMPILER", "4_1", "test")));
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
		Loader.dispose();
	}
}
