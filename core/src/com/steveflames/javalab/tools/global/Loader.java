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
    public static TextureAtlas botAtlas;
    public static TextureRegion botWheelTR;
    public static TextureRegion botMoveTR;

    //door
    public static TextureAtlas doorAtlas;

    //hud
    public static Texture heartT;
    public static Texture fixT;
    public static Texture eyeT;

    //teleporter
    public static TextureAtlas teleporterAtlas;


    public static void load() {
        //player
        botAtlas = new TextureAtlas(Gdx.files.internal("images/bot/bot.pack"));
        botWheelTR = new TextureRegion(new Texture(Gdx.files.internal("images/bot/bot_wheel.png")), 0, 0, 45, 45);
        botMoveTR = Loader.botAtlas.findRegion("bot_move");

        //door
        doorAtlas = new TextureAtlas(Gdx.files.internal("images/door/door.pack"));

        //hud
        heartT = new Texture(Gdx.files.internal("images/heart.png"));
        fixT = new Texture(Gdx.files.internal("images/fix.png"));
        eyeT = new Texture(Gdx.files.internal("images/eye.png"));

        //teleporter
        teleporterAtlas = new TextureAtlas(Gdx.files.internal("images/teleporter/teleporter.pack"));
    }

    public static void dispose() {
        //player
        botAtlas.dispose();
        botWheelTR.getTexture().dispose();
        botMoveTR.getTexture().dispose();

        //door
        doorAtlas.dispose();

        //hud
        heartT.dispose();
        fixT.dispose();
        eyeT.dispose();

        //teleporter
        teleporterAtlas.dispose();
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
