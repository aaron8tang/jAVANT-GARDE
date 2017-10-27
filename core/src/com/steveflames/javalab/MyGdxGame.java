package com.steveflames.javalab;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.steveflames.javalab.buttons.LevelListItem;
import com.steveflames.javalab.screens.ChooseLevelScreen;
import com.steveflames.javalab.screens.PlayScreen;
import com.steveflames.javalab.tools.global.Fonts;
import com.steveflames.javalab.tools.global.Loader;
import com.steveflames.javalab.tools.global.Skins;

/**
 * TODO:
 *
 * MISC
 * bugs sto background gia decor
 * java cofe
 * hello world
 * granazia
 * matrix lines
 * find free emojis gia decor
 * 0/2 classes found
 * verbose/laconic
 * na grafei kapou tis suntetagmenes tou paixth
 * analoga me to life s sto telos pairneis toso exp. me exp ksekleidwneis ta next lvls
 * tha erxontai kata panw m opws super mario san tis xelwnes me swsta k lathos k prepei na phdhksw
 *
 *
 * THOUGHTS
 * levers quiz gia na energopoihseis to termatiko or sth
 * o kakos na kanei read input sou se fash quiz
 * ARRAYS! parse array k printf k prepei na pas sto swsto. an lathos tote -1 health
 * VARIABLES! na pas px na vlepeis ton kwdika ths portas k na exei if(x > 10) open k na prepei na pas na to kaneis
 * VARIABLES! ellhnika stis troxalies
 * otan kaneis click sto console na kanei copy to clipboard
 *
 *
 * STARTMENUSCREEN
 * play
 * tutorial
 * options
 * exit
 *
 *
 * EDITOR
 * compile
 * tab (pathmeno space gia android?)
 * indents
 * enter na xwnei indents
 * LOIPON. DIKO M ERROR OTI DEN EXEI MAIN METHOD AN DN EXEI
 *
 *
 *
 * TODO!!!!
 * kalutero rotation
 * otan xtupaw elevator na sunexizoun na kouniounte alliws kanw respawn k ksekinaei apo kei p htan
 * bigger FONTS, bigger info sign font, bigger pc CODE HERE font (test sto xperia L)
 * allo handleInput k messages g move gia android k allo gia pc. interface? opws bt
 */
public class MyGdxGame extends Game {

	public static final int WIDTH = 1280; //1185, 854, 1920, 1280
	public static final int HEIGHT = 768; //600, 480,  1080, 768
	public static final float PPM = 200; //pixels per meter. has to do with b2body scaling
	public static final String TITLE = "Java Lab";

	public SpriteBatch sb; //used to render textures
	public ShapeRenderer sr; //used to render shapes

	public static iPlatformDepended platformDepended;
	public AssetManager assetManager = new AssetManager(); //TODO

	public MyGdxGame(iPlatformDepended platformDepended) {
		MyGdxGame.platformDepended = platformDepended;
	}

	@Override
	public void create () {
		Fonts.load();
		Skins.load();
		Loader.load();
		sb = new SpriteBatch();
		sr = new ShapeRenderer();
		setScreen(new ChooseLevelScreen(this));
		//setScreen(new PlayScreen(this, new LevelListItem("1_1", "Hello World!")));
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
		Fonts.dispose();
		Loader.dispose();
	}
}
