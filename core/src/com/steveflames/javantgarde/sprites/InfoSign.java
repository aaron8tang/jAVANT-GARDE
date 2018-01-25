package com.steveflames.javantgarde.sprites;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.steveflames.javantgarde.MyGdxGame;
import com.steveflames.javantgarde.tools.Assets;
import com.steveflames.javantgarde.tools.global.Cameras;
import com.steveflames.javantgarde.tools.global.MyFileReader;

import java.util.ArrayList;

/**
 * Implements the Info Signs that contain the theory.
 * The player reads these signs and learns about programming.
 */

public class InfoSign extends GameObject {

    private String text = "";
    private Assets assets;

    public InfoSign(String name, World world, TiledMap map, Rectangle bounds, float alpha, Assets assets) {
        super(name, world, map, bounds, true);
        this.assets = assets;
        this.alpha = alpha;

        if (MyFileReader.exists("txt/info/" + name + ".txt"))
            text = MyFileReader.readFile("txt/info/" + name + ".txt");
    }

    /**
     * Called when the player has read an Info Sign.
     * @param doors The Door objects of the game.
     */
    public void setRead(ArrayList<Door> doors) {
        if(name.equals("info-1_1-0") || name.equals("info-7_1-0") || name.equals("info-5_1-0") || name.equals("info-6_1-1"))
            doors.get(0).open();
        else if(name.equals("info-1_1-1") || name.equals("info-7_1-1"))
            doors.get(1).open();
        else if(name.equals("info-1_1-2") || name.equals("info-7_1-2"))
            doors.get(2).open();
        else if(name.equals("info-7_1-3"))
            doors.get(3).open();
    }

    public void drawUsePrompt(SpriteBatch sb) {
        if(usable) {
            sb.draw(assets.eyeTR, bounds.x + bounds.width / 2 - 25 + Cameras.getHudCameraOffsetX(), bounds.y + bounds.height + 40, 50, 50);
        }
    }

    public void update(float dt) {
        updateAlpha(dt);
    }

    public void drawFontScaled(SpriteBatch sb) {
        sb.setColor(1,1,1,alpha);
        sb.draw(assets.infoSignTR, position.x - 32/MyGdxGame.PPM, position.y - bounds.height/2/MyGdxGame.PPM, 64/ MyGdxGame.PPM, 64/ MyGdxGame.PPM);
    }

    public void drawFont(SpriteBatch sb) {}
    public void drawFilled(ShapeRenderer sr) {}
    public void drawLine(ShapeRenderer sr) {}

    public String getText() {
        return text;
    }
}
