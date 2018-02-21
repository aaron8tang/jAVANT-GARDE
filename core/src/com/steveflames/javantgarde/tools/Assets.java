package com.steveflames.javantgarde.tools;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.ObjectMap;
import com.steveflames.javantgarde.MyGdxGame;
import com.steveflames.javantgarde.tools.global.Fonts;

/**
 * This class is responsible for the managing of the assets.
 * The tool AssetManager that LibGDX provides is utilized.
 */

public class Assets {

    private AssetManager manager = new AssetManager();
    public static final float SFXVOLUME = 0.3f;
    private static final float MUSICVOLUME = 0.2f;

    //-------------------------------------GRAPHICS-------------------------------------
    //TextureAtlas PATHS
    private static final String texturesATLAS = "images/textures.pack";
    private static final String frogATLAS = "images/frog/frog.pack";
    private static final String mainMenuTexturesATLAS = "images/mainMenuTextures.pack";
    //TextureAtlas regions
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
    private static final String earthREGION = "earth";
    private static final String optionsREGION = "options";
    private static final String lockREGION = "images/lock.png";

    //Animation - TextureRegion - Texture objects
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
    public TextureRegion playTR;
    public TextureRegion aboutUpTR;
    public TextureRegion audioTR;
    public TextureRegion optionsTR;
    public TextureRegion earthTR;
    public Texture lockT;


    //-------------------------------------AUDIO-------------------------------------
    //Music and Sound PATHS
    private static final String mainMenuMUSIC = "audio/music/jewelbeat_electro_dance.ogg";
    private static final String playScreenMUSIC = "audio/music/jewelbeat_stepping_stones.ogg";
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
    private static final String questSOUND = "audio/sounds/quest.ogg";

    //Music and Sound objects
    public Music mainMenuMusic;
    private Music playScreenMusic;
    private Sound playScreenMusicSound; //Music on pc makes the game stutter, so im using a looping Sound for pc instead of Music
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
    public Sound questSound;


    //-------------------------------------SKINS-------------------------------------
    //PATHS
    private static final String neonSKIN = "skins/neon/skin/neon-ui.json";
    //Skin objects
    public Skin neonSkin;

    //-------------------------------------BUNDLES-------------------------------------
    //PATHS
    private static final String mainMenuStrings_en_BUNDLE = "strings/mainMenuStrings";
    private static final String mainMenuStrings_gr_BUNDLE = "strings/mainMenuStrings_gr";
    private static final String playscreenStrings_en_BUNDLE = "strings/playscreenStrings";
    private static final String playscreenStrings_gr_BUNDLE = "strings/playscreenStrings_gr";
    private static final String[] mainMenuBUNDLES =
                    {mainMenuStrings_en_BUNDLE,
                    mainMenuStrings_gr_BUNDLE};
    private static final String[] playscreenBUNDLES =
                    {playscreenStrings_en_BUNDLE,
                    playscreenStrings_gr_BUNDLE};
    //Bundle objects
    public I18NBundle mainMenuBundle;
    public I18NBundle playscreenBundle;


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
        if(!manager.isLoaded(texturesATLAS)) {
            manager.load(frogATLAS, TextureAtlas.class);
            manager.load(texturesATLAS, TextureAtlas.class);
        }
        if(MyGdxGame.musicOn) {
            if(!manager.isLoaded(playScreenMUSIC)) {
                if (Gdx.app.getType()== Application.ApplicationType.Desktop) //platform specific
                    manager.load(playScreenMUSIC, Sound.class);
                else
                    manager.load(playScreenMUSIC, Music.class);
            }
        }
        if(MyGdxGame.sfxOn) {
            if(!manager.isLoaded(jumpSOUND)) {
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
                manager.load(questSOUND, Sound.class);
                manager.load(clickSOUND, Sound.class);
            }
        }
        //loadSkins();
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
            manager.unload(questSOUND);
            if(manager.isLoaded(clickSOUND))
                manager.unload(clickSOUND);
        }
    }

    public void refreshPlayScreenAssets() {
        if(MyGdxGame.musicOn) {
            //playScreenMusic = manager.get(playScreenMUSIC, Music.class);
            if(Gdx.app.getType()== Application.ApplicationType.Desktop) { //platform specific
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
            questSound = manager.get(questSOUND, Sound.class);
            clickSound = manager.get(clickSOUND, Sound.class);
        }
        if(manager.isLoaded(neonSKIN)) {
            neonSkin = manager.get(neonSKIN, Skin.class);
        }
        TextureAtlas textureAtlas = manager.get("images/textures.pack");
        TextureAtlas frogAtlas = manager.get("images/frog/frog.pack");
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

    public void loadPlayScreenBundles(String lang) {
        for(String bundle: playscreenBUNDLES) { //unload all bundles
            if(manager.isLoaded(bundle))
                manager.unload(bundle);
        }

        if(lang.equals("English")) {
            manager.load(playscreenStrings_en_BUNDLE, I18NBundle.class);
        }
        else if(lang.equals("Ελληνικά")){
            manager.load(playscreenStrings_gr_BUNDLE, I18NBundle.class);
        }

        manager.finishLoading();
        for(String bundle : playscreenBUNDLES) {
            if(manager.isLoaded(bundle))
                playscreenBundle = manager.get(bundle, I18NBundle.class);
        }
    }

    public void unloadPlayScreenBundles() {
        for(String bundle: playscreenBUNDLES) {
            if (manager.isLoaded(bundle)) {
                manager.unload(bundle);
                playscreenBundle = null;
            }
        }
    }

    public void loadAllMainMenuAssets() {
        if(!manager.isLoaded(mainMenuTexturesATLAS)) {
            manager.load(mainMenuTexturesATLAS, TextureAtlas.class);
        }
        if(!manager.isLoaded(lockREGION))
            manager.load(lockREGION, Texture.class);
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
        for(String bundle: mainMenuBUNDLES) {
            if (manager.isLoaded(bundle)) {
                manager.unload(bundle);
                mainMenuBundle = null;
            }
        }
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
        }
        if(manager.isLoaded(mainMenuTexturesATLAS)) {
            TextureAtlas mainMenuTexturesAtlas = manager.get(mainMenuTexturesATLAS);
            playTR = mainMenuTexturesAtlas.findRegion(playREGION);
            earthTR = mainMenuTexturesAtlas.findRegion(earthREGION);
            aboutUpTR = mainMenuTexturesAtlas.findRegion(aboutUpREGION);
            audioTR = mainMenuTexturesAtlas.findRegion(audioREGION);
            optionsTR = mainMenuTexturesAtlas.findRegion(optionsREGION);
            lockT = manager.get(lockREGION, Texture.class);
        }
    }

    public void loadMainMenuBundles(String lang) {
        for(String bundle: mainMenuBUNDLES) { //unload all bundles
            if(manager.isLoaded(bundle))
                manager.unload(bundle);
        }

        if(lang.equals("English")) {
            manager.load(mainMenuStrings_en_BUNDLE, I18NBundle.class);
        }
        else if(lang.equals("Ελληνικά")){
            manager.load(mainMenuStrings_gr_BUNDLE, I18NBundle.class);
        }

        manager.finishLoading();
        for(String bundle : mainMenuBUNDLES) {
            if(manager.isLoaded(bundle))
                mainMenuBundle = manager.get(bundle, I18NBundle.class);
        }
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

    public void loadSkins() {
        if(!manager.isLoaded(neonSKIN)) {
            ObjectMap<String, Object> resources = new ObjectMap<String, Object>();
            resources.put("LiberationMono", Fonts.xsmallMono);
            resources.put("LiberationMonoBIG", Fonts.big);
            resources.put("LiberationMonoMARKUP", Fonts.xsmallMonoMarkup);
            resources.put("mvboli", Fonts.xsmall);
            manager.load("skins/neon/skin/neon-ui.json", Skin.class, new SkinLoader.SkinParameter("skins/neon/skin/neon-ui.atlas", resources));
        }
    }

    public void unloadSkins() {
        if(manager.isLoaded(neonSKIN)) {
            manager.unload(neonSKIN);
        }
    }

    /**
     * Utilized to load a TextureRegion[] that is used to create an Animation.
     * @param region The region of the animation.
     * @param columns The number of columns to split in the region.
     * @param rows The number of rows to split in the region.
     * @param lastNoImageN The number of missing textures in the last line.
     * @return The TextureRegion[] that is used to create an Animation.
     */
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
            if(Gdx.app.getType()== Application.ApplicationType.Desktop) // platform specific
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

    public void pauseMusic(Music music) {
        if(MyGdxGame.musicOn)
            music.pause();
    }

    //fixes html sound delay bug
    public void playAllPlayScreenSoundsMuted() {
        jumpSound.play(0);
        correctSound.play(0);
        levelCompletedSound.play(0);
        wrongSound.play(0);
        errorSound.play(0);
        loseHealthSound.play(0);
        deadSound.play(0);
        clickSound.play(0);
        bumpSound.play(0);
        robotTalkingSound.play(0);
        useItemSound.play(0);
        doorSound.play(0);
        teleportSound.play(0);
        getItemSound.play(0);
        frogSound.play(0);
        riseSound.play(0);
        questSound.play(0);
    }

    public void stopAllPlayScreenAudio() {
        if(MyGdxGame.sfxOn) {
            jumpSound.stop();
            correctSound.stop();
            levelCompletedSound.stop();
            wrongSound.stop();
            errorSound.stop();
            loseHealthSound.stop();
            deadSound.stop();
            //clickSound.stop();
            bumpSound.stop();
            robotTalkingSound.stop();
            useItemSound.stop();
            doorSound.stop();
            teleportSound.stop();
            getItemSound.stop();
            frogSound.stop();
            riseSound.stop();
            questSound.stop();
        }
        if(MyGdxGame.musicOn) {
            if(Gdx.app.getType()== Application.ApplicationType.Desktop) //platform specific
                playScreenMusicSound.stop();
            else
                playScreenMusic.stop();
        }
    }

    //fixes html sound delay bug
    public void playAllMenuSoundsMuted() {
        clickSound.play(0);
    }
}
