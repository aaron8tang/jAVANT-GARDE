package com.steveflames.javalab.sprites;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.steveflames.javalab.MyGdxGame;
import com.steveflames.javalab.screens.PlayScreen;
import com.steveflames.javalab.tools.global.MyFileReader;
import com.steveflames.javalab.tools.global.Fonts;
import com.steveflames.javalab.tools.global.Loader;

/**
 * Created by Flames on 4/10/2017.
 */

public class InfoSign extends GameObject {

    private String text = "";

    public InfoSign(String name, World world, TiledMap map, Rectangle bounds, float alpha) {
        super(name, world, map, bounds, true);
        this.alpha = alpha;

        if (MyFileReader.exists("txt/info/" + name + ".txt"))
            text = MyFileReader.readFile("txt/info/" + name + ".txt");
    }

    public void drawUsePrompt(SpriteBatch sb) {
        if(usable) {
            Fonts.medium.setColor(Color.RED);
            sb.draw(Loader.eyeT, bounds.x + bounds.width / 2 - 25 + PlayScreen.getHudCameraOffsetX(), bounds.y + bounds.height + 40, 50, 50);
        }
    }

    public void draw(SpriteBatch sb, float dt) {
        updateAlpha(dt);
        sb.setColor(1,1,1,alpha);
        sb.draw(Loader.infoSignT, (bounds.x + 32)/MyGdxGame.PPM, bounds.y/MyGdxGame.PPM, 64/ MyGdxGame.PPM, 64/MyGdxGame.PPM);
    }

    public String getText() {
        return text;
    }
}
