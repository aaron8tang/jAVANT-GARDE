package com.steveflames.javalab.sprites.ropes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.steveflames.javalab.MyGdxGame;
import com.steveflames.javalab.screens.Window;
import com.steveflames.javalab.sprites.InteractiveTileObject;
import com.steveflames.javalab.tools.global.Fonts;
import com.steveflames.javalab.tools.MyFileReader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Created by Flames on 10/10/2017.
 */

public class Rope extends InteractiveTileObject {

    private ArrayList<Platform> platforms = new ArrayList<Platform>();
    private boolean active = true;
    private int id;

    private String prompt = "";
    private GlyphLayout glyphLayout = new GlyphLayout();

    public Rope(String name, World world, TiledMap map, Rectangle bounds) {
        super(name, world, map, bounds, true);
        String text = MyFileReader.readFile("txt/"+name+".txt");
        String[] lineSplitter = text.split("\r\n");
        String[] attributeSplitter;
        int platformCounter = -1;
        Random random = new Random();
        float randExtraY = random.nextFloat()*3;
        ArrayList<String> platformTexts = new ArrayList<String>();

        //get the rope group from the name
        attributeSplitter = name.split("-");
        id = Character.getNumericValue(attributeSplitter[2].charAt(0));

        Collections.addAll(platformTexts, lineSplitter);
        Collections.shuffle(platformTexts);

        for (String s : platformTexts) {
            if(s.contains("~")) {
                attributeSplitter = s.split("~");
                platformCounter++;
                platforms.add(new Platform(attributeSplitter[0], attributeSplitter[1], platformCounter, randExtraY, world, bounds));
            }
            else
                prompt = s;
        }
        glyphLayout.setText(Fonts.small, prompt);
    }

    public void update(float dt) {
        for(int i=0; i<platforms.size(); i++)
            platforms.get(i).update(dt);

        if(active) { //if rope is currently active
            for(int i=0; i<platforms.size(); i++) {
                if(!platforms.get(i).isActive()) { //if player touched this platform
                    active = false; //deactivate the rope
                    for(int j=0; j<platforms.size(); j++) { //fade all platforms
                        platforms.get(j).b2body.setLinearVelocity(0, 0);
                        if(!platforms.get(i).isFlag() || platforms.get(i) != platforms.get(j)) //if player touched right platform, dont fade it
                            platforms.get(j).fade();
                    }
                }
            }
        }
    }

    public void drawFilled(ShapeRenderer sr) {
        //draw platforms
        for(int i=0; i<platforms.size(); i++)
            platforms.get(i).drawFilled(sr);
    }

    public void drawLine(ShapeRenderer sr) {
        for(int i=0; i<platforms.size(); i++)
            platforms.get(i).drawLine(sr);
    }

    public void drawFont(SpriteBatch sb) {
        //draw prompt font
        Fonts.small.setColor(Color.ORANGE);
        Fonts.small.draw(sb, prompt, bounds.x - 95 + Window.getHudCameraOffsetX() - glyphLayout.width/2, (bounds.y + bounds.height) - glyphLayout.height/2 - 5);
        //draw platform fonts
        Fonts.small.setColor(Color.WHITE);
        for(int i=0; i<platforms.size(); i++)
            platforms.get(i).drawFont(sb);
    }

    public void drawPromptTile(ShapeRenderer sr) {
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(new Color(0.21f, 0.18f, 0.17f, 1));
        sr.rect((bounds.x + bounds.width/2)/MyGdxGame.PPM - 0.6f, (bounds.y + bounds.height)/MyGdxGame.PPM - 0.32f, 1.2f, 0.32f);
        sr.end();

        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(Color.BLACK);
        sr.rect((bounds.x + bounds.width/2)/MyGdxGame.PPM - 0.6f, (bounds.y + bounds.height)/MyGdxGame.PPM - 0.32f, 1.2f, 0.32f);
        sr.end();
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

    public int getId() {
        return id;
    }
}
