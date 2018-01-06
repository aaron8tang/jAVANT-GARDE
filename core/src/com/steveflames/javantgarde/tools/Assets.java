package com.steveflames.javantgarde.tools;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ObjectMap;
import com.steveflames.javantgarde.MyGdxGame;
import com.steveflames.javantgarde.tools.global.Fonts;

/**
 * Created by Flames on 6/12/2017.
 */

public class Assets {

    private AssetManager manager = new AssetManager();
    public static final float SFXVOLUME = 0.3f;
    private static final float MUSICVOLUME = 0.2f;

    //GRAPHICS - TEXTURE REGIONS
    private static final String texturesATLAS = "images/textures.pack";
    private static final String frogATLAS = "images/frog/frog.pack";
    private static final String mainMenuTexturesATLAS = "images/mainMenuTextures.pack";
    private static final String teleporterDisappearREGION = "teleporter_disappear";
    private static final String botTypingREGION = "bot_typing";
    private static final String pcREGION = "pc";
    private static final String leverClosedREGION = "lever_closed";
    private static final String leverOpenREGION = "lever_open";
    private static final String botMoveREGION = "bot_move";
    private static final String infoSignREGION = "infoSign";
    //private static final String diplomaREGION = "diploma";
    //private static final String scrollREGION = "scroll";
    private static final String botWheelREGION = "bot_wheel";
    private static final String keyboardDownREGION = "keyboardDown";
    private static final String keyboardUpREGION = "keyboardUp";
    private static final String robotAntennasREGION = "robot-antennas";
    private static final String classREGION = "class";
    private static final String eyeREGION = "eye";
    private static final String fixREGION = "fix";
    private static final String handREGION = "hand";
    private static final String heartREGION = "heart";
    private static final String teleporterIdleREGION = "teleporter_idle";
    private static final String botTalkREGION = "bot_talk";
    private static final String frogTalkREGION = "frog_talk";
    private static final String aboutUpREGION = "helpUp";
    private static final String audioREGION = "audio";
    private static final String playREGION = "play";
    private static final String lockTEXTURE = "images/lock.png";

    private TextureAtlas textureAtlas;
    private TextureAtlas frogAtlas;
    private TextureAtlas mainMenuTexturesAtlas;
    public Animation<TextureRegion> botIdleAnimation;
    public Animation<TextureRegion> botTypingAnimation;
    public Animation<TextureRegion> robotTalkinAnimation;
    public Animation<TextureRegion> frogIdleAnimation;
    public Animation<TextureRegion> teleporterIdleAnimation;
    public Animation<TextureRegion> teleporterDisappearingAnimation;
    public TextureRegion pcTR;
    public TextureRegion leverClosedTR;
    public TextureRegion leverOpenTR;
    public TextureRegion botMoveTR;
    public TextureRegion infoSignTR;
    //public TextureRegion diplomaTR;
    //public TextureRegion scrollTR;
    public TextureRegion botWheelTR;
    public TextureRegion keyboardDownTR;
    public TextureRegion keyboardUpTR;
    public TextureRegion classTR;
    public TextureRegion eyeTR;
    public TextureRegion fixTR;
    public TextureRegion handTR;
    public TextureRegion heartTR;
    public TextureRegion playT;
    public TextureRegion aboutUpT;
    public TextureRegion audioT;
    public Texture lockT;


    //AUDIO
    private static final String mainMenuMUSIC = "audio/music/jewelbeat_electro_dance.ogg";
    private static final String playScreenMUSIC = "audio/music/jewelbeat_stepping_stones.wav";
    private static final String jumpSOUND = "audio/sounds/jump.ogg";
    private static final String correctSOUND = "audio/sounds/correct.ogg";
    private static final String levelCompletedSOUND = "audio/sounds/levelCompleted.ogg";
    private static final String wrongAnswerSOUND = "audio/sounds/wrong.ogg";
    private static final String errorSOUND = "audio/sounds/error.ogg";
    private static final String loseHealthSOUND = "audio/sounds/loseHealth.ogg";
    private static final String deadSOUND = "audio/sounds/dead.ogg";
    private static final String clickSOUND = "audio/sounds/click.ogg";
    private static final String bumpSOUND = "audio/sounds/bump.ogg";
    private static final String robotTalkingSOUND = "audio/sounds/robotTalk.ogg";
    private static final String useItemSOUND = "audio/sounds/useItem.ogg";
    private static final String doorSOUND = "audio/sounds/door.ogg";
    private static final String teleportSOUND = "audio/sounds/teleport.ogg";
    private static final String getItemSOUND = "audio/sounds/coin.ogg";
    private static final String frogSOUND = "audio/sounds/frog.ogg";
    private static final String riseSOUND = "audio/sounds/rise.ogg";

    public Music mainMenuMusic;
    private Music playScreenMusic;
    private Sound playScreenMusicSound; //Music on pc makes the game lag, so im using a looping Sound for pc
    public Sound clickSound;
    public Sound jumpSound;
    public Sound correctSound;
    public Sound levelCompletedSound;
    public Sound wrongSound;
    public Sound errorSound;
    public Sound loseHealthSound;
    public Sound deadSound;
    public Sound bumpSound;
    public Sound robotTalkingSound;
    public Sound useItemSound;
    public Sound doorSound;
    public Sound teleportSound;
    public Sound getItemSound;
    public Sound frogSound;
    public Sound riseSound;


    //SKINS
    private static final String neonSKIN = "skins/neon/skin/neon-ui.json";
    private static final String terraSKIN = "skins/terra-mother/skin/terra-mother-ui.json";
    private static final String lmlSKIN = "skins/lml/skin/skin.json";

    public Skin neonSkin;
    public Skin terraSkin;
    public Skin lmlSkin;


    public <T> T get(String path, Class<T> tClass) {
        return manager.get(path, tClass);
    }
    public boolean update() {
        return manager.update();
    }
    public void finishLoading() {
        manager.finishLoading();
    }
    public float getQueuedAssets() {
        return manager.getQueuedAssets();
    }
    public float getProgress() {
        return manager.getProgress();
    }

    public void loadAllPlayScreenAssets() {
        manager.load(frogATLAS, TextureAtlas.class);
        manager.load(texturesATLAS, TextureAtlas.class);
        if(MyGdxGame.musicOn) {
            if(MyGdxGame.platformDepended.isPC())
                manager.load(playScreenMUSIC, Sound.class);
            else
                manager.load(playScreenMUSIC, Music.class);
        }
        if(MyGdxGame.sfxOn) {
            manager.load(jumpSOUND, Sound.class);
            manager.load(deadSOUND, Sound.class);
            manager.load(getItemSOUND, Sound.class);
            manager.load(bumpSOUND, Sound.class);
            manager.load(frogSOUND, Sound.class);
            manager.load(doorSOUND, Sound.class);
            manager.load(robotTalkingSOUND, Sound.class);
            manager.load(errorSOUND, Sound.class);
            manager.load(correctSOUND, Sound.class);
            manager.load(wrongAnswerSOUND, Sound.class);
            manager.load(loseHealthSOUND, Sound.class);
            manager.load(teleportSOUND, Sound.class);
            manager.load(useItemSOUND, Sound.class);
            manager.load(levelCompletedSOUND, Sound.class);
            manager.load(riseSOUND, Sound.class);
        }
    }

    public void unloadAllPlayScreenAssets() {
        if(manager.isLoaded(texturesATLAS)) {
            manager.unload(texturesATLAS);
            manager.unload(frogATLAS);
        }
        if(manager.isLoaded(bumpSOUND)) {
            manager.unload(jumpSOUND);
            manager.unload(deadSOUND);
            manager.unload(getItemSOUND);
            manager.unload(bumpSOUND);
            manager.unload(frogSOUND);
            manager.unload(doorSOUND);
            manager.unload(robotTalkingSOUND);
            manager.unload(errorSOUND);
            manager.unload(correctSOUND);
            manager.unload(wrongAnswerSOUND);
            manager.unload(loseHealthSOUND);
            manager.unload(teleportSOUND);
            manager.unload(useItemSOUND);
            manager.unload(levelCompletedSOUND);
            manager.unload(riseSOUND);
        }
    }

    public void refreshPlayScreenAssets() {
        if(MyGdxGame.musicOn) {
            //playScreenMusic = manager.get(playScreenMUSIC, Music.class);
            if(MyGdxGame.platformDepended.isPC()) {
                playScreenMusicSound = manager.get(playScreenMUSIC);
            }
            else {
                playScreenMusic = manager.get(playScreenMUSIC);
                playScreenMusic.setVolume(MUSICVOLUME);
                playScreenMusic.setLooping(true);
            }
        }
        if(MyGdxGame.sfxOn) {
            jumpSound = manager.get(jumpSOUND, Sound.class);
            correctSound = manager.get(correctSOUND, Sound.class);
            levelCompletedSound = manager.get(levelCompletedSOUND, Sound.class);
            wrongSound = manager.get(wrongAnswerSOUND, Sound.class);
            errorSound = manager.get(errorSOUND, Sound.class);
            loseHealthSound = manager.get(loseHealthSOUND, Sound.class);
            deadSound = manager.get(deadSOUND, Sound.class);
            bumpSound = manager.get(bumpSOUND, Sound.class);
            robotTalkingSound = manager.get(robotTalkingSOUND, Sound.class);
            useItemSound = manager.get(useItemSOUND, Sound.class);
            doorSound = manager.get(doorSOUND, Sound.class);
            teleportSound = manager.get(teleportSOUND, Sound.class);
            getItemSound = manager.get(getItemSOUND, Sound.class);
            frogSound = manager.get(frogSOUND, Sound.class);
            riseSound = manager.get(riseSOUND, Sound.class);
        }
        if(manager.isLoaded(neonSKIN)) {
            neonSkin = manager.get(neonSKIN, Skin.class);
            terraSkin = manager.get(terraSKIN, Skin.class);
            lmlSkin = manager.get(lmlSKIN, Skin.class);
        }
        textureAtlas = manager.get("images/textures.pack");
        frogAtlas = manager.get("images/frog/frog.pack");
        pcTR = textureAtlas.findRegion(pcREGION);
        leverClosedTR = textureAtlas.findRegion(leverClosedREGION);
        leverOpenTR = textureAtlas.findRegion(leverOpenREGION);
        botMoveTR = textureAtlas.findRegion(botMoveREGION);
        infoSignTR = textureAtlas.findRegion(infoSignREGION);
        //diplomaTR = textureAtlas.findRegion(diplomaREGION);
        //scrollTR = textureAtlas.findRegion(scrollREGION);
        botWheelTR = textureAtlas.findRegion(botWheelREGION);
        keyboardDownTR = textureAtlas.findRegion(keyboardDownREGION);
        keyboardUpTR = textureAtlas.findRegion(keyboardUpREGION);
        classTR = textureAtlas.findRegion(classREGION);
        eyeTR = textureAtlas.findRegion(eyeREGION);
        fixTR = textureAtlas.findRegion(fixREGION);
        handTR = textureAtlas.findRegion(handREGION);
        heartTR = textureAtlas.findRegion(heartREGION);
        botIdleAnimation = new Animation<TextureRegion>(0.08f, loadAnim(textureAtlas.findRegion(botTalkREGION), 12, 6, 3 ));
        botTypingAnimation = new Animation<TextureRegion>(0.015f, loadAnim(textureAtlas.findRegion(botTypingREGION), 12, 2, 0 ));
        robotTalkinAnimation = new Animation<TextureRegion>(0.16f, loadAnim(textureAtlas.findRegion(Assets.robotAntennasREGION),4, 1, 0));
        frogIdleAnimation = new Animation<TextureRegion>(0.06f , loadAnim(frogAtlas.findRegion(Assets.frogTalkREGION), 16,4,2));
        teleporterIdleAnimation = new Animation<TextureRegion>(0.08f, loadAnim(textureAtlas.findRegion(Assets.teleporterIdleREGION), 4, 4, 3));
        teleporterDisappearingAnimation = new Animation<TextureRegion>(0.12f, loadAnim(textureAtlas.findRegion(Assets.teleporterDisappearREGION), 4, 4, 1 ));
    }

    public void loadAllMainMenuAssets() {
        if(!manager.isLoaded(mainMenuTexturesATLAS)) {
            manager.load(mainMenuTexturesATLAS, TextureAtlas.class);
            manager.load(lockTEXTURE, Texture.class);
        }
        if(MyGdxGame.musicOn)
            loadMainMenuMusic();
        if(MyGdxGame.sfxOn)
            loadMainMenuSounds();
        loadSkins();
    }

    public void unloadAllMainMenuAssets() {
        if(manager.isLoaded(mainMenuTexturesATLAS))
            manager.unload(mainMenuTexturesATLAS);
        if(manager.isLoaded(mainMenuMUSIC))
            manager.unload(mainMenuMUSIC);
        if(manager.isLoaded(clickSOUND))
            manager.unload(clickSOUND);
        /*if(manager.isLoaded(neonSKIN)) {
            manager.unload(neonSKIN);
            manager.unload(terraSKIN);
            manager.unload(lmlSKIN);
        }*/
    }

    public void refreshMainMenuAssets() {
        if(MyGdxGame.musicOn) {
            mainMenuMusic = manager.get(mainMenuMUSIC, Music.class);
            mainMenuMusic.setVolume(MUSICVOLUME);
            mainMenuMusic.setLooping(true);
        }
        if(MyGdxGame.sfxOn) {
            clickSound = manager.get(clickSOUND, Sound.class);
        }
        if(manager.isLoaded(neonSKIN)) {
            neonSkin = manager.get(neonSKIN, Skin.class);
            terraSkin = manager.get(terraSKIN, Skin.class);
            lmlSkin = manager.get(lmlSKIN, Skin.class);
        }
        mainMenuTexturesAtlas = manager.get(mainMenuTexturesATLAS);
        playT = mainMenuTexturesAtlas.findRegion(playREGION);
        aboutUpT = mainMenuTexturesAtlas.findRegion(aboutUpREGION);
        audioT = mainMenuTexturesAtlas.findRegion(audioREGION);
        lockT = manager.get(lockTEXTURE, Texture.class);
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

    private void loadSkins() {
        if(!manager.isLoaded(neonSKIN)) {
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
    }

    private TextureRegion[] loadAnim(TextureAtlas.AtlasRegion region, int columns, int rows, int lastNoImageN) {
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

    public void playSound(Sound sound) {
        if(MyGdxGame.sfxOn)
            sound.play(SFXVOLUME);
    }

    public void playMusic(Music music) {
        if(MyGdxGame.musicOn) {
            if(!music.isPlaying())
                music.play();
        }
    }

    public void playPlayScreenMusic() {
        if(MyGdxGame.musicOn) {
            if(MyGdxGame.platformDepended.isPC())
                playScreenMusicSound.loop(MUSICVOLUME);
            else
                playScreenMusic.play();
        }
    }

    public void stopSound(Sound sound) {
        if(MyGdxGame.sfxOn)
            sound.stop();
    }

    public void stopMusic(Music music) {
        if(MyGdxGame.musicOn)
            music.stop();
    }

    public void stopPlayScreenMusic() {
        if(MyGdxGame.musicOn) {
            if(MyGdxGame.platformDepended.isPC())
                playScreenMusicSound.stop();
            else
                playScreenMusic.stop();
        }
    }

    public void pauseMusic(Music music) {
        if(MyGdxGame.musicOn)
            music.pause();
    }

}
