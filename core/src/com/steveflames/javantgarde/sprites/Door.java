package com.steveflames.javantgarde.sprites;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.steveflames.javantgarde.screens.PlayScreen;
import com.steveflames.javantgarde.tools.global.Cameras;

/**
 * Created by Flames on 15/10/2017.
 */

public class Door extends GameObject {

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
            else
                opening = false;
        }
    }

    public void drawFont(SpriteBatch sb) {}
    public void drawFontScaled(SpriteBatch sb) {}

    public void drawFilled(ShapeRenderer sr) {
        sr.setColor(0.21f, 0.18f, 0.17f, 1);
        sr.rect(bounds.x + Cameras.getHudCameraOffsetX(), bounds.y, bounds.width, bounds.height);
    }

    public void drawLine(ShapeRenderer sr) {
        sr.setColor(Color.BLACK);
        sr.rect(bounds.x + Cameras.getHudCameraOffsetX(), bounds.y, bounds.width, bounds.height);
    }

    public void open() {
        opening = true;
        b2body.getFixtureList().get(0).setSensor(true);
    }

}
