package com.steveflames.javantgarde.sprites.ropes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.steveflames.javantgarde.MyGdxGame;
import com.steveflames.javantgarde.sprites.GameObject;
import com.steveflames.javantgarde.tools.Assets;
import com.steveflames.javantgarde.tools.GameObjectManager;
import com.steveflames.javantgarde.tools.global.Cameras;
import com.steveflames.javantgarde.tools.global.Fonts;
import com.steveflames.javantgarde.tools.global.MyFileReader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Implements the Rope sprites
 * (jump on the correct platform elevators e.g. level 2_1)
 */

public class Rope extends GameObject {

    private ArrayList<Platform> platforms = new ArrayList<Platform>();
    private boolean active = true;
    private int id;

    private String prompt = "";
    private GlyphLayout glyphLayout = new GlyphLayout();

    private Assets assets;

    public Rope(String name, World world, TiledMap map, Rectangle bounds, GameObjectManager objectManager, Assets assets) {
        super(name, world, map, bounds, true);
        String text = MyFileReader.readFile("txt/"+Fonts.languageShort+"/ropes/"+name+".txt");
        String[] lineSplitter = text.split("\r\n");
        String[] attributeSplitter;
        Random random = new Random();
        float randExtraY = random.nextFloat()*3;
        ArrayList<String> platformTexts = new ArrayList<String>();
        this.assets = assets;

        //get the rope group from the name
        attributeSplitter = name.split("-");
        id = Integer.parseInt(String.valueOf(attributeSplitter[2].charAt(0)));

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
                        bounds.y + bounds.height + (randExtraY + 500/MyGdxGame.PPM*i)* MyGdxGame.PPM, 200, 50)));
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
                    if(platforms.get(i).isFlag())
                        assets.playSound(assets.correctSound);
                    else
                        assets.playSound(assets.wrongSound);
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
        Fonts.small.draw(sb, prompt, bounds.x - 95 + Cameras.getHudCameraOffsetX() - glyphLayout.width/2, (bounds.y + bounds.height) - glyphLayout.height/2 - 5);
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
