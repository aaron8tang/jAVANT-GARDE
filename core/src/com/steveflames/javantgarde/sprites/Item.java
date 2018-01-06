package com.steveflames.javantgarde.sprites;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.steveflames.javantgarde.MyGdxGame;
import com.steveflames.javantgarde.tools.Assets;
import com.steveflames.javantgarde.tools.global.MyFileReader;

/**
 * Created by Flames on 19/10/2017.
 */

public class Item extends GameObject {

    private int dir = 1;
    private int itemID;
    private static int nOfClasses = 0;
    private Assets assets;
    private String text = "";

    public Item(String name, World world, TiledMap map, Rectangle bounds, Assets assets) {
        super(name, world, map, bounds, true);
        this.assets = assets;
        if(name.contains("health"))
            itemID = 1;
        else if(name.contains("class")) {
            itemID = 2;
            nOfClasses++;
            text = MyFileReader.readFile("txt/classes/"+name+".txt");
        }
    }

    public void update(float dt) {
        b2body.setTransform(b2body.getPosition().x, b2body.getPosition().y+30/MyGdxGame.PPM*dt*dir, 0);

        if(b2body.getPosition().y +30/MyGdxGame.PPM*dt > bounds.y/MyGdxGame.PPM + 30/MyGdxGame.PPM)
            dir = -1;
        else if (b2body.getPosition().y +30/MyGdxGame.PPM*dt < bounds.y/MyGdxGame.PPM + 10/MyGdxGame.PPM)
            dir = 1;
    }

    public void drawFontScaled(SpriteBatch sb) {
        if(itemID == 1)
            sb.draw(assets.heartTR, b2body.getPosition().x - 30/MyGdxGame.PPM, b2body.getPosition().y - 30/MyGdxGame.PPM, 60/MyGdxGame.PPM, 60/MyGdxGame.PPM);
        else
            sb.draw(assets.classTR, b2body.getPosition().x - 30/MyGdxGame.PPM, b2body.getPosition().y - 30/MyGdxGame.PPM, 60/MyGdxGame.PPM, 60/MyGdxGame.PPM);
    }

    public void drawFont(SpriteBatch sb) {}
    public void drawFilled(ShapeRenderer sr) {}
    public void drawLine(ShapeRenderer sr) {}

    public static void reset() {
        nOfClasses = 0;
    }

    public static int getnOfClasses() {
        return nOfClasses;
    }

    public String getText() {
        return text;
    }
}
