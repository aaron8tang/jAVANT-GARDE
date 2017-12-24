package com.steveflames.javantgarde.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.steveflames.javantgarde.MyGdxGame;
import com.steveflames.javantgarde.tools.Assets;

/**
 * Created by Flames on 19/10/2017.
 */

public class Item extends GameObject {

    private int dir = 1;
    private TextureRegion textureRegion;
    private static int nOfClasses = 0;

    public Item(String name, World world, TiledMap map, Rectangle bounds, TextureAtlas textureAtlas) {
        super(name, world, map, bounds, true);
        if(name.contains("health"))
            textureRegion = textureAtlas.findRegion(Assets.heartREGION);
        else if(name.contains("class")) {
            textureRegion = textureAtlas.findRegion(Assets.classREGION);
            nOfClasses++;
        }
    }

    public void update(float dt) {
        b2body.setTransform(b2body.getPosition().x, b2body.getPosition().y+0.15f*dt*dir, 0);

        if(b2body.getPosition().y +0.15f*dt > bounds.y/MyGdxGame.PPM + 0.15)
            dir = -1;
        else if (b2body.getPosition().y +0.15f*dt < bounds.y/MyGdxGame.PPM + 0.05)
            dir = 1;
    }

    public void drawFontScaled(SpriteBatch sb) {
        sb.draw(textureRegion, b2body.getPosition().x - 30/MyGdxGame.PPM, b2body.getPosition().y - 30/MyGdxGame.PPM, 60/MyGdxGame.PPM, 60/MyGdxGame.PPM);
    }

    public void drawFont(SpriteBatch sb) {}
    public void drawFilled(ShapeRenderer sr) {}
    public void drawLine(ShapeRenderer sr) {}

    public static void reset() {
        nOfClasses = 0;
    }

    public static int getnOfClasses() {
        return nOfClasses;
    }
}
