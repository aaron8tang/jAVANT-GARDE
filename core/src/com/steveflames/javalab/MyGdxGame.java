package com.steveflames.javalab;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.steveflames.javalab.tools.Fonts;
import com.steveflames.javalab.screens.PlayScreen;

/**
 * TODO:
 * allo handleInput gia android k allo gia pc. interface? opws bt
 */
public class MyGdxGame extends Game {

	public static final int WIDTH = 1280; //1185, 854, 1920, 1280
	public static final int HEIGHT = 768; //600, 480,  1080, 768
	public static final float PPM = 200; //pixels per meter. has to do with b2body scaling
	public static final String TITLE = "Java Lab";


	public SpriteBatch sb;
	public ShapeRenderer sr; //used to render shapes
	
	@Override
	public void create () {
		new Fonts();
		sb = new SpriteBatch();
		sr = new ShapeRenderer();
		setScreen(new PlayScreen(this));
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
	}
}
