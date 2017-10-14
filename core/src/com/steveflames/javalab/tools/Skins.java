package com.steveflames.javalab.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Created by Flames on 12/10/2017.
 */

public class Skins {

    public static Skin neonSkin;
    public static Skin lmlSkin;
    public static Skin skin;

    public static void load() {
        neonSkin = new Skin();
        neonSkin.addRegions(new TextureAtlas(Gdx.files.internal("skins/neon/skin/neon-ui.atlas")));
        neonSkin.add("LiberationMono", Fonts.xsmallMono);
        neonSkin.load(Gdx.files.internal("skins/neon/skin/neon-ui.json"));

        lmlSkin = new Skin();
        lmlSkin.addRegions(new TextureAtlas(Gdx.files.internal("skins/lml/skin/skin.atlas")));
        lmlSkin.add("LiberationMono", Fonts.big);
        lmlSkin.load(Gdx.files.internal("skins/lml/skin/skin.json"));

        skin = new Skin();
        //skin.getFont("font-label").setFixedWidthGlyphs("qwertyuiop[]asdfghjkl;'zxcvbnm,./`1234567890-=\\QWERTYUIOP{}ASDFGHJKL:\"ZXCVBNM<>?~!@#$%^&*()_+");
        skin.addRegions(new TextureAtlas(Gdx.files.internal("skins/terra-mother/skin/terra-mother-ui.atlas")));
        skin.add("LiberationMono", Fonts.xsmallMono);
        skin.load(Gdx.files.internal("skins/terra-mother/skin/terra-mother-ui.json"));
    }
}
