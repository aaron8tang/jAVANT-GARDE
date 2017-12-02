package com.steveflames.javantgarde.tools.global;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Created by Flames on 12/10/2017.
 */

public class Skins {

    public static final Skin neonSkin = new Skin();
    public static final Skin lmlSkin = new Skin();
    public static final Skin skin = new Skin();

    public static void load() {
        neonSkin.addRegions(new TextureAtlas(Gdx.files.internal("skins/neon/skin/neon-ui.atlas")));
        neonSkin.add("LiberationMono", Fonts.xsmallMono);
        neonSkin.load(Gdx.files.internal("skins/neon/skin/neon-ui.json"));

        lmlSkin.addRegions(new TextureAtlas(Gdx.files.internal("skins/lml/skin/skin.atlas")));
        lmlSkin.add("LiberationMono", Fonts.big);
        lmlSkin.load(Gdx.files.internal("skins/lml/skin/skin.json"));

        //skin.getFont("font-label").setFixedWidthGlyphs("qwertyuiop[]asdfghjkl;'zxcvbnm,./`1234567890-=\\QWERTYUIOP{}ASDFGHJKL:\"ZXCVBNM<>?~!@#$%^&*()_+");
        skin.addRegions(new TextureAtlas(Gdx.files.internal("skins/terra-mother/skin/terra-mother-ui.atlas")));
        skin.add("LiberationMono", Fonts.xsmallMonoMarkup);
        skin.add("mvboli", Fonts.xsmall);
        skin.load(Gdx.files.internal("skins/terra-mother/skin/terra-mother-ui.json"));
    }

}
