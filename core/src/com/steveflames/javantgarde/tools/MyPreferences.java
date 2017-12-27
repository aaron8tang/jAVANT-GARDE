package com.steveflames.javantgarde.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * Created by Flames on 27/12/2017.
 */

public class MyPreferences {

    private Preferences preferences;
    private static final String PREFS_NAME = "prefFile";
    private static final String PREF_MUSIC_ENABLED = "musicenabled";
    private static final String PREF_SOUND_ENABLED = "soundenabled";
    private static final String LEVEL_PROGRESS = "levelprogress";

    private Preferences getPrefs() {
        if(preferences==null){
            preferences = Gdx.app.getPreferences(PREFS_NAME);
        }
        return preferences;
    }

    public boolean isSoundEffectsEnabled() {
        return getPrefs().getBoolean(PREF_SOUND_ENABLED, true);
    }

    public void setSoundEffectsEnabled(boolean soundEffectsEnabled) {
        getPrefs().putBoolean(PREF_SOUND_ENABLED, soundEffectsEnabled);
        getPrefs().flush();
    }

    public boolean isMusicEnabled() {
        return getPrefs().getBoolean(PREF_MUSIC_ENABLED, true);
    }

    public void setMusicEnabled(boolean musicEnabled) {
        getPrefs().putBoolean(PREF_MUSIC_ENABLED, musicEnabled);
        getPrefs().flush();
    }

    public String getLevelProgress() {
        return getPrefs().getString(LEVEL_PROGRESS, "1_1");
    }

    public void setLevelProgress(String id) {
        getPrefs().putString(LEVEL_PROGRESS, id);
        getPrefs().flush();
    }
}
