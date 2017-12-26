package com.steveflames.javantgarde.tools;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ObjectMap;
import com.steveflames.javantgarde.MyGdxGame;
import com.steveflames.javantgarde.screens.MainMenuScreen;
import com.steveflames.javantgarde.tools.global.Fonts;

/**
 * Created by Flames on 6/12/2017.
 */

public class Assets {

    private AssetManager manager = new AssetManager();
    //GRAPHICS - TEXTURE REGIONS
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
    public static final String aboutUpT = "images/helpUp.png";
    public static final String aboutDownT = "images/helpDown.png";
    public static final String audioT = "images/audio.png";
    public static final String playT = "images/play.png";

    //AUDIO
    public static final String mainMenuMUSIC = "audio/music/jewelbeat - electro dance.wav";
    public static final String playScreenMUSIC = "audio/music/jewelbeat - stepping stones.wav";
    public static final String jumpSOUND = "audio/sounds/jump.wav";
    public static final String correctSOUND = "audio/sounds/correct.wav";
    public static final String levelCompletedSOUND = "audio/sounds/levelCompleted.wav";
    public static final String wrongAnswerSOUND = "audio/sounds/wrong.wav";
    public static final String errorSOUND = "audio/sounds/error.wav";
    public static final String loseHealthSOUND = "audio/sounds/loseHealth.wav";
    public static final String deadSOUND = "audio/sounds/dead.wav";
    public static final String clickSOUND = "audio/sounds/click.wav";
    public static final String bumpSOUND = "audio/sounds/bump.wav";
    public static final String robotTalkingSOUND = "audio/sounds/robotTalk.wav";
    public static final String useItemSOUND = "audio/sounds/useItem.wav";
    public static final String leverSOUND = "audio/sounds/lever.wav";
    public static final String doorSOUND = "audio/sounds/door.wav";
    public static final String teleportSOUND = "audio/sounds/teleport.wav";
    public static final String getItemSOUND = "audio/sounds/coin.wav";
    public static final String frogSOUND = "audio/sounds/frog.wav";
    public static final String materializeSOUND = "audio/sounds/materialize.wav";
    public static final String riseSOUND = "audio/sounds/rise.wav";
    public static final String musicAnnouncerSOUND = "audio/sounds/musicAnnouncer.wav";


    //SKINS
    public static final String neonSKIN = "skins/neon/skin/neon-ui.json";
    public static final String terraSKIN = "skins/terra-mother/skin/terra-mother-ui.json";
    public static final String lmlSKIN = "skins/lml/skin/skin.json";


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

    public void loadAllMainMenuAssets() {
        manager.load(playT, Texture.class);
        manager.load(aboutUpT, Texture.class);
        manager.load(aboutDownT, Texture.class);
        manager.load(audioT, Texture.class);
        if(MyGdxGame.musicOn)
            loadMainMenuMusic();
        if(MyGdxGame.sfxOn)
            loadMainMenuSounds();
        loadSkins();
    }

    public void unloadAllMainMenuAssets() {
        manager.unload(playT);
        manager.unload(aboutUpT);
        manager.unload(aboutDownT);
        manager.unload(audioT);
        if(MyGdxGame.musicOn)
            manager.unload(mainMenuMUSIC);
        if(MyGdxGame.sfxOn)
            manager.unload(clickSOUND);
        //todo unload skins?
    }

    public void loadMainMenuMusic() {
        manager.load(mainMenuMUSIC, Music.class);
    }

    public void loadMainMenuSounds() {
        manager.load(clickSOUND, Sound.class);
    }

    public void unloadMainMenuMusic() {
        manager.unload(mainMenuMUSIC);
    }

    public void unloadMainMenuSounds() {
        manager.unload(clickSOUND);
    }

    public void loadAllPlayScreenAssets() {
        manager.load("images/textures.pack", TextureAtlas.class);
        manager.load("images/frog/frog.pack", TextureAtlas.class);
        if(MyGdxGame.musicOn)
            manager.load(playScreenMUSIC, Music.class);
        if(MyGdxGame.sfxOn) {
            manager.load(jumpSOUND, Sound.class);
            manager.load(deadSOUND, Sound.class);
            manager.load(getItemSOUND, Sound.class);
            manager.load(bumpSOUND, Sound.class);
            manager.load(frogSOUND, Sound.class);
            manager.load(doorSOUND, Music.class);
            manager.load(materializeSOUND, Sound.class);
            manager.load(robotTalkingSOUND, Sound.class);
            manager.load(errorSOUND, Sound.class);
            manager.load(correctSOUND, Sound.class);
            manager.load(leverSOUND, Sound.class);
            manager.load(wrongAnswerSOUND, Sound.class);
            manager.load(loseHealthSOUND, Sound.class);
            manager.load(teleportSOUND, Sound.class);
            manager.load(useItemSOUND, Sound.class);
            manager.load(levelCompletedSOUND, Sound.class);
            manager.load(riseSOUND, Sound.class);
            manager.load(musicAnnouncerSOUND, Sound.class);
        }
    }

    public void unloadAllPlayScreenAssets() {
        manager.unload("images/textures.pack");
        manager.unload("images/frog/frog.pack");
        if(manager.isLoaded(bumpSOUND)) {
            manager.unload(jumpSOUND);
            manager.unload(deadSOUND);
            manager.unload(getItemSOUND);
            manager.unload(bumpSOUND);
            manager.unload(frogSOUND);
            manager.unload(doorSOUND);
            manager.unload(materializeSOUND);
            manager.unload(robotTalkingSOUND);
            manager.unload(errorSOUND);
            manager.unload(correctSOUND);
            manager.unload(leverSOUND);
            manager.unload(wrongAnswerSOUND);
            manager.unload(loseHealthSOUND);
            manager.unload(teleportSOUND);
            manager.unload(useItemSOUND);
            manager.unload(levelCompletedSOUND);
            manager.unload(riseSOUND);
            manager.unload(musicAnnouncerSOUND);
        }
    }

    private void loadSkins() {
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

    public TextureAtlas getTextureAtlas() {
        return manager.get("images/textures.pack");
    }
    public TextureAtlas getFrogAtlas() {
        return manager.get("images/frog/frog.pack");
    }
    public Skin getNeonSkin() {
        return manager.get(neonSKIN, Skin.class);
    }
    public Skin getTerraSkin() {
        return manager.get(terraSKIN, Skin.class);
    }
    public Skin getLmlSkin() {
        return manager.get(lmlSKIN, Skin.class);
    }

}
