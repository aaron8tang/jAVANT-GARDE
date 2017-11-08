package com.steveflames.javalab.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.steveflames.javalab.MyGdxGame;
import com.steveflames.javalab.sprites.InteractiveTileObject;
import com.steveflames.javalab.tools.global.Loader;

/**
 * Created by Flames on 19/10/2017.
 */

public class Item extends InteractiveTileObject {

    private int dir = -1;
    private Texture texture;
    private static int nOfClasses = 0;

    public Item(String name, World world, TiledMap map, Rectangle bounds) {
        super(name, world, map, bounds, true);
        if(name.contains("health"))
            texture = Loader.heartT;
        else if(name.contains("class")) {
            texture = Loader.classT;
            nOfClasses++;
        }
    }

    public void update(float dt) {
        b2body.setTransform(b2body.getPosition().x, b2body.getPosition().y+0.15f*dt*dir, 0);

        if(b2body.getPosition().y*MyGdxGame.PPM > bounds.y + 5)
            dir = -1;
        else if (b2body.getPosition().y*MyGdxGame.PPM < bounds.y - 5)
            dir = 1;
    }

    public void draw(SpriteBatch sb) {
        sb.draw(texture, b2body.getPosition().x - 30/MyGdxGame.PPM, b2body.getPosition().y - 30/MyGdxGame.PPM, 60/MyGdxGame.PPM, 60/MyGdxGame.PPM);
    }

    public static void reset() {
        nOfClasses = 0;
    }

    public static int getnOfClasses() {
        return nOfClasses;
    }
}
