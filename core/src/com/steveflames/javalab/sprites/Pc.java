package com.steveflames.javalab.sprites;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.steveflames.javalab.screens.Window;
import com.steveflames.javalab.tools.global.Fonts;
import com.steveflames.javalab.tools.global.Loader;

/**
 * Created by Flames on 24/9/2017.
 */

public class Pc extends InteractiveTileObject {

    public Pc(String name, World world, TiledMap map, Rectangle bounds) {
        super(name, world, map, bounds, true);
    }

    public void drawUsePrompt(SpriteBatch sb) {
        if(usable) {
            Fonts.medium.setColor(Color.RED);
            sb.draw(Loader.fixT, bounds.x + bounds.width / 2 - 30 + Window.getHudCameraOffsetX(), bounds.y + bounds.height + 20, 60, 60);
            //Fonts.medium.draw(sb, "!", bounds.x + bounds.width / 2 - 10 + Window.getHudCameraOffsetX(), bounds.y + bounds.height + 50);
        }
    }

}
