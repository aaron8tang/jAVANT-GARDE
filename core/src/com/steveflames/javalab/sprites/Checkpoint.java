package com.steveflames.javalab.sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Flames on 11/10/2017.
 */

public class Checkpoint extends InteractiveTileObject {

    private boolean visited = false;

    public Checkpoint(String name, World world, TiledMap map, Rectangle bounds) {
        super(name, world, map, bounds, true);
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }
}
