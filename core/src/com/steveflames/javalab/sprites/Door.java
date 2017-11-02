package com.steveflames.javalab.sprites;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.steveflames.javalab.MyGdxGame;
import com.steveflames.javalab.screens.Window;
import com.steveflames.javalab.tools.global.Loader;

/**
 * Created by Flames on 15/10/2017.
 */

public class Door extends InteractiveTileObject {

    private boolean opening = false;

    public Door(String name, World world, TiledMap map, Rectangle bounds) {
        super(name, world, map, bounds, false);
    }


    public void update(float dt) {
        if(opening) {
            if(bounds.height>0) {
                bounds.y += 400*dt;
                bounds.height -= 400*dt;
            }
        }
    }

    public void drawFilled(ShapeRenderer sr) {
        sr.rect(bounds.x + Window.getHudCameraOffsetX(), bounds.y, bounds.width, bounds.height);
    }

    public void drawLine(ShapeRenderer sr) {

        sr.setColor(Color.BLACK);
        sr.rect(bounds.x + Window.getHudCameraOffsetX(), bounds.y, bounds.width, bounds.height);
    }

    public void open() {
        opening = true;
        b2body.getFixtureList().get(0).setSensor(true);
    }

}
