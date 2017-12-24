package com.steveflames.javantgarde.tools;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ObjectMap;
import com.steveflames.javantgarde.tools.global.Fonts;

/**
 * Created by Flames on 6/12/2017.
 */

public class Assets {

    private AssetManager manager = new AssetManager();
    public static final String teleporterDisappearREGION = "teleporter_disappear";
    public static final String botTypingREGION = "bot_typing";
    public static final String pcREGION = "pc";
    public static final String leverClosedREGION = "lever_closed";
    public static final String leverOpenREGION = "lever_open";
    public static final String botMoveREGION = "bot_move";
    public static final String infoSignREGION = "infoSign";
    public static final String diplomaREGION = "diploma";
    public static final String scrollREGION = "scroll";
    public static final String botWheelREGION = "bot_wheel";
    public static final String keyboardDownREGION = "keyboardDown";
    public static final String keyboardUpREGION = "keyboardUp";
    public static final String robotAntennasREGION = "robot-antennas";
    public static final String classREGION = "class";
    public static final String eyeREGION = "eye";
    public static final String fixREGION = "fix";
    public static final String handREGION = "hand";
    public static final String heartREGION = "heart";
    public static final String teleporterIdleREGION = "teleporter_idle";
    public static final String botTalkREGION = "bot_talk";
    public static final String frogTalkREGION = "frog_talk";

    public static final String mainMenuMUSIC = "/audio/sounds/JewelBeat - Electro Dance.wav";


    public <T> T get(String path, Class<T> tClass) {
        return manager.get(path, tClass);
    }

    public boolean update() {
        return manager.update();
    }

    public void finishLoading() {
        manager.finishLoading();
    }

    public float getLoadedAssets() {
        return manager.getLoadedAssets();
    }

    public int getQueuedAssets() {
        return manager.getQueuedAssets();
    }


    public void loadAllPlayScreen() {
        manager.load("images/textures.pack", TextureAtlas.class);
        manager.load("images/frog/frog.pack", TextureAtlas.class);
        loadSounds();
    }

    public void unloadAllPlayScreen() {
        manager.unload("images/textures.pack");
        unloadSounds();
    }

    public void loadSkins() {
        ObjectMap<String, Object> resources = new ObjectMap<String, Object>();
        resources.put("LiberationMono", Fonts.xsmallMono);

        manager.load("skins/neon/skin/neon-ui.atlas", TextureAtlas.class);
        manager.load("skins/neon/skin/neon-ui.json", Skin.class, new SkinLoader.SkinParameter("skins/neon/skin/neon-ui.atlas", resources));

        resources = new ObjectMap<String, Object>();
        resources.put("LiberationMono", Fonts.big);
        manager.load("skins/lml/skin/skin.atlas", TextureAtlas.class);
        manager.load("skins/lml/skin/skin.json", Skin.class, new SkinLoader.SkinParameter("skins/lml/skin/skin.atlas", resources));

        resources = new ObjectMap<String, Object>();
        resources.put("LiberationMono", Fonts.xsmallMonoMarkup);
        resources.put("mvboli", Fonts.xsmall);
        manager.load("skins/terra-mother/skin/terra-mother-ui.atlas", TextureAtlas.class);
        manager.load("skins/terra-mother/skin/terra-mother-ui.json", Skin.class, new SkinLoader.SkinParameter("skins/terra-mother/skin/terra-mother-ui.atlas", resources));
    }

    public Skin getNeonSkin() {
        return manager.get("skins/neon/skin/neon-ui.json");
    }

    public Skin getTerraSkin() {
        return manager.get("skins/terra-mother/skin/terra-mother-ui.json");
    }

    public Skin getLmlSkin() {
        return manager.get("skins/lml/skin/skin.json");
    }

    public TextureAtlas getTextureAtlas() {
        return manager.get("images/textures.pack", TextureAtlas.class);
    }

    public TextureAtlas getFrogAtlas() {
        return manager.get("images/frog/frog.pack");
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

    private void loadSounds() {
        manager.load(mainMenuMUSIC, Sound.class);
    }

    private void unloadSounds() {
        manager.unload(mainMenuMUSIC);
    }
}
