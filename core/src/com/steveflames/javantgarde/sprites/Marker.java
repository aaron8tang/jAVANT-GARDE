package com.steveflames.javantgarde.sprites;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.steveflames.javantgarde.tools.global.Cameras;
import com.steveflames.javantgarde.tools.global.Fonts;

/**
 * Created by Flames on 12/12/2017.
 */

public class Marker extends GameObject {

    private String text = "";

    public Marker(String name, World world, TiledMap map, Rectangle bounds) {
        super(name, world, map, bounds, true);
        b2body.setGravityScale(0);
        b2body.setType(BodyDef.BodyType.DynamicBody);
        if(name.contains("_"))
            this.name = name.split("_")[1];
    }

    @Override
    public void update(float dt) {}
    @Override
    public void drawFilled(ShapeRenderer sr) {}
    @Override
    public void drawLine(ShapeRenderer sr) {}
    @Override
    public void drawFontScaled(SpriteBatch sb) {}
    @Override
    public void drawFont(SpriteBatch sb) {}

    public void drawFontInBackground(SpriteBatch sb){
        if(name.equals("console")) {
            Fonts.xsmall.setColor(Color.WHITE);
            Fonts.xsmall.draw(sb, text, bounds.x + 15 + Cameras.getHudCameraOffsetX(), bounds.y + bounds.height - 15);
        }
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
