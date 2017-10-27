package com.steveflames.javalab.sprites;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.steveflames.javalab.screens.Window;
import com.steveflames.javalab.tools.global.Fonts;

/**
 * Created by Flames on 4/10/2017.
 */

public class InfoSign extends InteractiveTileObject {

    public InfoSign(String name, World world, TiledMap map, Rectangle bounds) {
        super(name, world, map, bounds, true);
    }

    public void drawUsePrompt(SpriteBatch sb) {
        if(usable) {
            sb.begin();
            Fonts.medium.setColor(Color.RED);
            Fonts.medium.draw(sb, "!", bounds.x + bounds.width / 2 - 5 + Window.getHudCameraOffsetX() , bounds.y + bounds.height + 90);
            sb.end();
        }
    }

}
