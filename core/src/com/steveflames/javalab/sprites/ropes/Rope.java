package com.steveflames.javalab.sprites.ropes;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.steveflames.javalab.sprites.InteractiveTileObject;
import com.steveflames.javalab.tools.MyFileReader;

import java.util.ArrayList;

/**
 * Created by Flames on 10/10/2017.
 */

public class Rope extends InteractiveTileObject {

    private ArrayList<Platform> platforms = new ArrayList<Platform>();
    private boolean active = true;

    public Rope(String name, World world, TiledMap map, Rectangle bounds) {
        super(name, world, map, bounds, true);
        String text = MyFileReader.readFile("txt/"+name+".txt");
        String[] temp = text.split("\r\n");
        String[] temp2;

        for (String s : temp) {
            temp2 = s.split("~");
            platforms.add(new Platform(temp2[0], temp2[1], world, bounds));
        }
    }

    public void update(float dt) {
        for(Platform platform : platforms)
            platform.update(dt);

        if(active) { //if rope is currently active
            for (Platform platform : platforms) {
                if(!platform.isActive()) { //if player touched this platform
                    active = false; //deactivate the rope
                    for(Platform p : platforms) { //fade all platforms
                        p.b2body.setLinearVelocity(0, 0);
                        if(!platform.isFlag() || platform != p) //if player touched right platform, dont fade it
                            p.fade();
                    }
                }
            }
        }
    }

    public void draw(SpriteBatch sb, ShapeRenderer sr) {
        for(Platform platform: platforms) {
            platform.draw(sb, sr);
        }
    }

    public void reset() {
        for(Platform platform: platforms) {
            platform.setAlpha(1);
            platform.getFixture().setSensor(false);
            platform.b2body.setLinearVelocity(0, -1f);
            platform.setActive(true);
        }
        active = true;
    }
}
