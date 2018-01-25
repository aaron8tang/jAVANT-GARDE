package com.steveflames.javantgarde;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

/**
 * The starting point (main) of the game when run on Android.
 * The app's configurations are set in the manifest.
 * After the PlatformDepended class is initialized, the flow
 * of the program moves to the core (MyGdxGame class).
 */

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new MyGdxGame(new PlatformDepended()), config);
	}
}
