package com.steveflames.javalab.tools.global;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Flames on 16/10/2017.
 */

public class Loader {

    //player
    public static final TextureAtlas botAtlas = new TextureAtlas(Gdx.files.internal("images/bot/bot.pack"));
    public static final TextureRegion botWheelTR = new TextureRegion(new Texture(Gdx.files.internal("images/bot/bot_wheel.png")), 0, 0, 45, 45);
    public static final TextureRegion botMoveTR = Loader.botAtlas.findRegion("bot_move");

    //world
    public static final TextureAtlas teleporterAtlas = new TextureAtlas(Gdx.files.internal("images/teleporter/teleporter.pack"));
    public static final Texture infoSignT = new Texture(Gdx.files.internal("images/infoSign.png"));
    public static final Texture leverOpenT = new Texture(Gdx.files.internal("images/lever_open.png"));
    public static final Texture leverClosedT = new Texture(Gdx.files.internal("images/lever_closed.png"));

    //hud
    public static final Texture heartT = new Texture(Gdx.files.internal("images/heart.png"));
    public static final Texture classT = new Texture(Gdx.files.internal("images/class.png"));
    public static final Texture fixT = new Texture(Gdx.files.internal("images/fix.png"));
    public static final Texture eyeT = new Texture(Gdx.files.internal("images/eye.png"));


    //editor
    public static final TextureRegion keyboardUpTR = new TextureRegion(new Texture(Gdx.files.internal("images/keyboardUp.png")));
    public static final TextureRegion keyboardDownTR = new TextureRegion(new Texture(Gdx.files.internal("images/keyboardDown.png")));


    public static void dispose() {
        //player
        botAtlas.dispose();
        botWheelTR.getTexture().dispose();
        botMoveTR.getTexture().dispose();

        //world
        teleporterAtlas.dispose();
        infoSignT.dispose();
        leverOpenT.dispose();
        leverClosedT.dispose();

        //hud
        heartT.dispose();
        classT.dispose();
        fixT.dispose();
        eyeT.dispose();

        //editor
        keyboardUpTR.getTexture().dispose();
        keyboardDownTR.getTexture().dispose();
    }

    public static TextureRegion[] loadAnim(TextureAtlas.AtlasRegion region, int columns, int rows, int lastNoImageN) {
        TextureRegion[] temp = new TextureRegion[columns * rows - lastNoImageN];

        TextureRegion[][] tmp = region.split(region.getRegionWidth() / columns,
                region.getRegionHeight() / rows);

        int index = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if(i == rows-1) {
                    if(j > columns - 1 - lastNoImageN)
                        break;
                }
                temp[index++] = tmp[i][j];
            }
        }

        return temp;
    }
}
