package com.steveflames.javantgarde.tools.global;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Global class that loads the fonts and provides access to them.
 * TODO: make non-global, embed into the assets
 * TODO: use only one big font and scale accordingly
 */

public class Fonts {
    public static BitmapFont xsmallMono;
    public static BitmapFont xsmallMonoMarkup;
    public static BitmapFont xsmall;
    public static BitmapFont small;
    public static BitmapFont medium;
    public static BitmapFont big;

    public static void load() {
        Texture texture = new Texture(Gdx.files.internal("fonts/LiberationMono23.png"), true); // true enables mipmaps
        texture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.Linear);
        TextureRegion textureRegion = new TextureRegion(texture);
        xsmallMono = new BitmapFont(Gdx.files.internal("fonts/LiberationMono23.fnt"), textureRegion);
        xsmallMonoMarkup = new BitmapFont(Gdx.files.internal("fonts/LiberationMono23.fnt"), textureRegion);

        texture = new Texture(Gdx.files.internal("fonts/mvboli24.png"), true);
        texture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.Linear);
        textureRegion = new TextureRegion(texture);
        xsmall = new BitmapFont(Gdx.files.internal("fonts/mvboli24.fnt"), textureRegion);

        texture = new Texture(Gdx.files.internal("fonts/mvboli36.png"), true);
        texture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.Linear);
        textureRegion = new TextureRegion(texture);
        small = new BitmapFont(Gdx.files.internal("fonts/mvboli36.fnt"), textureRegion);

        texture = new Texture(Gdx.files.internal("fonts/mvboli50.png"), true);
        texture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.Linear);
        textureRegion = new TextureRegion(texture);
        medium = new BitmapFont(Gdx.files.internal("fonts/mvboli50.fnt"), textureRegion);
        big = new BitmapFont(Gdx.files.internal("fonts/mvboli50.fnt"), textureRegion);
        big.getData().setScale(2f);

        //enable markup
        xsmall.getData().markupEnabled = true;
        xsmallMonoMarkup.getData().markupEnabled = true;
        medium.getData().markupEnabled = true;
        big.getData().markupEnabled = true;

        Fonts.xsmallMono.setColor(Color.BLACK);
        Fonts.xsmallMonoMarkup.setColor(Color.BLACK);
        Fonts.xsmall.setColor(Color.BLACK);
        Fonts.medium.setColor(Color.WHITE);
        Fonts.small.setColor(Color.WHITE);
        Fonts.big.setColor(Color.WHITE);
    }

    public static void dispose() {
        xsmallMono.dispose();
        xsmall.dispose();
        medium.dispose();
        small.dispose();
        big.dispose();
    }

}
