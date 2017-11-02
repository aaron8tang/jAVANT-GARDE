package com.steveflames.javalab.sprites;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.steveflames.javalab.MyGdxGame;
import com.steveflames.javalab.screens.Window;
import com.steveflames.javalab.tools.global.Fonts;

/**
 * Created by Flames on 30/10/2017.
 */

public class FloatingPlatform extends InteractiveTileObject {

    private String name;
    private GlyphLayout glyphLayout = new GlyphLayout();

    public FloatingPlatform(String name, World world, TiledMap map, Rectangle bounds) {
        super(name, world, map, bounds, false);
        String[] splitter = name.split("_");
        this.name = splitter[1];
        glyphLayout.setText(Fonts.small, this.name);
    }

    public void drawFilled(ShapeRenderer sr) {
        sr.rect(bounds.x + Window.getHudCameraOffsetX(), bounds.y, bounds.width, bounds.height);
    }

    public void drawFont(SpriteBatch sb) {
        Fonts.small.draw(sb, name, bounds.x + bounds.width/2 - glyphLayout.width/2 + Window.getHudCameraOffsetX(), bounds.y + bounds.height/2 + glyphLayout.height/2);
    }
}
