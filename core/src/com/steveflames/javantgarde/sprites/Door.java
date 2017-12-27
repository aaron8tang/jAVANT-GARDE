package com.steveflames.javantgarde.sprites;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.steveflames.javantgarde.MyGdxGame;
import com.steveflames.javantgarde.tools.Assets;
import com.steveflames.javantgarde.tools.global.Cameras;

/**
 * Created by Flames on 15/10/2017.
 */

public class Door extends GameObject {

    private boolean opening = false;
    private Assets assets;

    public Door(String name, World world, TiledMap map, Rectangle bounds, Assets assets) {
        super(name, world, map, bounds, false);
        this.assets = assets;
    }


    public void update(float dt) {
        if(opening) {
            if(bounds.height - 400*dt >0) {
                bounds.y += 400*dt;
                bounds.height -= 400*dt;
            }
            else {
                if(MyGdxGame.sfxOn)
                    assets.doorSound.stop();
                opening = false;
                bounds.height = 0;
            }
        }
    }

    public void drawFont(SpriteBatch sb) {}
    public void drawFontScaled(SpriteBatch sb) {}

    public void drawFilled(ShapeRenderer sr) {
        sr.setColor(0, 0, 0, 0.7f);
        sr.rect(bounds.x + Cameras.getHudCameraOffsetX(), bounds.y+1, bounds.width, bounds.height-1);
    }

    public void drawLine(ShapeRenderer sr) {
        sr.setColor(0.14f, 0.87f, 0.88f, 1);
        sr.rect(bounds.x + Cameras.getHudCameraOffsetX(), bounds.y+1, bounds.width, bounds.height-1);
    }

    public void open() {
        if(MyGdxGame.sfxOn)
            assets.doorSound.loop();
        opening = true;
        b2body.getFixtureList().get(0).setSensor(true);
    }

}
