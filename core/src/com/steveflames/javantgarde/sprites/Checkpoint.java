package com.steveflames.javantgarde.sprites;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Flames on 11/10/2017.
 */

public class Checkpoint extends GameObject {

    private boolean visited = false;
    private String text;

    public Checkpoint(String name, World world, TiledMap map, Rectangle bounds) {
        super(name, world, map, bounds, true);
        if (com.steveflames.javantgarde.tools.global.MyFileReader.exists("txt/checkpoints/" + name + ".txt"))
            text = com.steveflames.javantgarde.tools.global.MyFileReader.readFile("txt/checkpoints/" + name + ".txt");
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

    public void update(float dt) {}
    public void drawFilled(ShapeRenderer sr) {}
    public void drawLine(ShapeRenderer sr) {}
    public void drawFont(SpriteBatch sb) {}
    public void drawFontScaled(SpriteBatch sb) {}
}
