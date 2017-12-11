package com.steveflames.javantgarde.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(480, 320);
                //return new GwtApplicationConfiguration(MyGdxGame.WIDTH, MyGdxGame.HEIGHT);
        }

        @Override
        public ApplicationListener createApplicationListener () {
                return new com.steveflames.javantgarde.MyGdxGame(new PlatformDepended());
        }
}