package com.steveflames.javalab.sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Flames on 4/10/2017.
 */

public class InfoSign extends InteractiveTileObject {

    public InfoSign(String name, World world, TiledMap map, Rectangle bounds) {
        super(name, world, map, bounds, true);
    }

}
