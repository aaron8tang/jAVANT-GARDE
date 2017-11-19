package com.steveflames.javalab.sprites;

import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.steveflames.javalab.MyGdxGame;
import com.steveflames.javalab.screens.PlayScreen;
import com.steveflames.javalab.tools.global.Fonts;

/**
 * Created by Flames on 30/10/2017.
 */

public class FloatingPlatform extends InteractiveTileObject {

    private String name;
    private GlyphLayout glyphLayout = new GlyphLayout();
    private boolean movingPlatformX = false;
    private boolean colliding = false;
    private int facing = 1;

    public FloatingPlatform(String name, World world, TiledMap map, Rectangle bounds) {
        super(name, world, map, bounds, false);
        if(name.contains("_")) {
            String[] splitter = name.split("_");
            this.name = splitter[1];
        }
        else {
            this.name = "";
            movingPlatformX = true;
            b2body.setLinearVelocity(0.7f, 0);
        }
        glyphLayout.setText(Fonts.small, this.name);
        //b2body.setGravityScale(0);
        //b2body.setType(BodyDef.BodyType.StaticBody);
    }

    public void update(float dt) {

    }

    public void drawRect(ShapeRenderer sr) {
        sr.rect(b2body.getPosition().x*MyGdxGame.PPM + PlayScreen.getHudCameraOffsetX() - bounds.width/2, b2body.getPosition().y*MyGdxGame.PPM - bounds.height/2, bounds.width, bounds.height);
    }

    public void drawFont(SpriteBatch sb) {
        Fonts.small.draw(sb, name, b2body.getPosition().x*MyGdxGame.PPM - glyphLayout.width/2 + PlayScreen.getHudCameraOffsetX(), b2body.getPosition().y*MyGdxGame.PPM + glyphLayout.height/2);
    }

    public int getFacing() {
        return facing;
    }

    public void setFacing(int facing) {
        this.facing = facing;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setTransform(float x, float y, float angle) {
        b2body.setTransform(x, y, angle);
        bounds.setX(x*MyGdxGame.PPM);
        bounds.setY(y*MyGdxGame.PPM);
    }
}
