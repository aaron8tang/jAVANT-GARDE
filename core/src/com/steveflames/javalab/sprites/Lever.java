package com.steveflames.javalab.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.steveflames.javalab.MyGdxGame;
import com.steveflames.javalab.tools.global.Loader;

/**
 * Created by Flames on 19/11/2017.
 */

public class Lever extends InteractiveTileObject {

    private boolean open = false;
    private Texture currentTexture = Loader.leverClosedT;

    public Lever(String name, World world, TiledMap map, Rectangle bounds, float alpha) {
        super(name, world, map, bounds, true);
        this.alpha = alpha;
    }

    public void draw(SpriteBatch sb, float dt) {
        updateAlpha(dt);
        sb.setColor(1,1,1,alpha);
        sb.draw(currentTexture, bounds.x/ MyGdxGame.PPM, bounds.y/MyGdxGame.PPM, bounds.width/MyGdxGame.PPM, bounds.height/MyGdxGame.PPM);
    }

    public void pull() {
        open = !open;
        currentTexture = open ? Loader.leverOpenT : Loader.leverClosedT;
    }

}
