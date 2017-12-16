package com.steveflames.javantgarde.sprites;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Flames on 12/12/2017.
 */

public class Marker extends GameObject {

    public Marker(String name, World world, TiledMap map, Rectangle bounds) {
        super(name, world, map, bounds, true);
        b2body.setGravityScale(0);
        b2body.setType(BodyDef.BodyType.DynamicBody);
    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void drawFilled(ShapeRenderer sr) {

    }

    @Override
    public void drawLine(ShapeRenderer sr) {

    }

    @Override
    public void drawFont(SpriteBatch sb) {

    }

    @Override
    public void drawFontScaled(SpriteBatch sb) {

    }
}
