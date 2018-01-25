package com.steveflames.javantgarde.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

/**
 * The starting point of the game when run on the Web.
 * After the PlatformDepended class is initialized, the flow
 * of the program moves to the core (MyGdxGame class).
 */

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(1200, 768); //predefined sizes for web
        }

        @Override
        public ApplicationListener createApplicationListener () {
                return new com.steveflames.javantgarde.MyGdxGame(new PlatformDepended());
        }
}