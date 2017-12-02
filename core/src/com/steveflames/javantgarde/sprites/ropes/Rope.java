package com.steveflames.javantgarde.sprites.ropes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.steveflames.javantgarde.screens.PlayScreen;
import com.steveflames.javantgarde.tools.global.Fonts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Created by Flames on 10/10/2017.
 */

public class Rope extends com.steveflames.javantgarde.sprites.GameObject {

    private ArrayList<Platform> platforms = new ArrayList<Platform>();
    private boolean active = true;
    private int id;

    private String prompt = "";
    private GlyphLayout glyphLayout = new GlyphLayout();

    public Rope(String name, World world, TiledMap map, Rectangle bounds, com.steveflames.javantgarde.GameObjectManager objectManager) {
        super(name, world, map, bounds, true);
        String text = com.steveflames.javantgarde.tools.global.MyFileReader.readFile("txt/ropes/"+name+".txt");
        String[] lineSplitter = text.split("\r\n");
        String[] attributeSplitter;
        Random random = new Random();
        float randExtraY = random.nextFloat()*3;
        ArrayList<String> platformTexts = new ArrayList<String>();

        //get the rope group from the name
        attributeSplitter = name.split("-");
        id = Character.getNumericValue(attributeSplitter[2].charAt(0));

        Collections.addAll(platformTexts, lineSplitter);
        Collections.shuffle(platformTexts);

        int i=0;
        for (String s : platformTexts) {
            if(s.charAt(0) == '~') {
                prompt = s.substring(1);
                continue;
            }
            else
                platforms.add(new Platform(s, world, map, new Rectangle(bounds.x + bounds.width/2 - 100,
                        bounds.y + bounds.height + (randExtraY + 2.5f*i)* com.steveflames.javantgarde.MyGdxGame.PPM, 200, 50)));
            i++;
        }
        glyphLayout.setText(Fonts.small, prompt);

        for(Platform platform: platforms)
            objectManager.addGameObject(platform);
    }

    public void update(float dt) {
        if(active) { //if rope is currently active
            for(int i=0; i<platforms.size(); i++) {
                if(!platforms.get(i).isActive()) { //if player touched this platform
                    active = false; //deactivate the rope
                    for(int j=0; j<platforms.size(); j++) { //fade all platforms
                        platforms.get(j).getB2body().setLinearVelocity(0, 0);
                        if(!platforms.get(i).isFlag() || platforms.get(i) != platforms.get(j)) //if player touched right platform, dont fade it
                            platforms.get(j).fade();
                    }
                }
            }
        }
    }

    public void drawFilled(ShapeRenderer sr) {}
    public void drawLine(ShapeRenderer sr) {}
    public void drawFont(SpriteBatch sb) {}
    public void drawFontScaled(SpriteBatch sb) {}

    public void drawFontInBackground(SpriteBatch sb) {
        Fonts.small.setColor(Color.ORANGE);
        Fonts.small.draw(sb, prompt, bounds.x - 95 + PlayScreen.getHudCameraOffsetX() - glyphLayout.width/2, (bounds.y + bounds.height) - glyphLayout.height/2 - 5);
    }

    public void reset() {
        for(Platform platform: platforms)
            platform.reset();
        active = true;
    }

    public int getId() {
        return id;
    }
}
