package com.steveflames.javalab.sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.steveflames.javalab.tools.global.MyFileReader;

/**
 * Created by Flames on 11/10/2017.
 */

public class Checkpoint extends GameObject {

    private boolean visited = false;
    private String text;

    public Checkpoint(String name, World world, TiledMap map, Rectangle bounds) {
        super(name, world, map, bounds, true);
        if (MyFileReader.exists("txt/checkpoints/" + name + ".txt"))
            text = MyFileReader.readFile("txt/checkpoints/" + name + ".txt");
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public String getText() {
        return text;
    }
}
