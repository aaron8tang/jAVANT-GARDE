package com.steveflames.javantgarde.sprites;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.steveflames.javantgarde.MyGdxGame;
import com.steveflames.javantgarde.tools.Assets;
import com.steveflames.javantgarde.tools.global.Cameras;
import com.steveflames.javantgarde.tools.global.Fonts;
import com.steveflames.javantgarde.tools.global.MyFileReader;

import java.util.ArrayList;

/**
 * Created by Flames on 4/10/2017.
 */

public class InfoSign extends GameObject {

    private String text = "";
    private TextureRegion infoSignTR;
    private TextureRegion infoSignUseTR;

    public InfoSign(String name, World world, TiledMap map, Rectangle bounds, float alpha, TextureAtlas textureAtlas) {
        super(name, world, map, bounds, true);
        this.alpha = alpha;
        this.infoSignTR = textureAtlas.findRegion(Assets.infoSignREGION);
        this.infoSignUseTR = textureAtlas.findRegion(Assets.eyeREGION);

        if (MyFileReader.exists("txt/info/" + name + ".txt"))
            text = MyFileReader.readFile("txt/info/" + name + ".txt");
    }

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
            Fonts.medium.setColor(Color.RED);
            sb.draw(infoSignUseTR, bounds.x + bounds.width / 2 - 25 + Cameras.getHudCameraOffsetX(), bounds.y + bounds.height + 40, 50, 50);
        }
    }

    public void update(float dt) {
        updateAlpha(dt);
    }

    public void drawFontScaled(SpriteBatch sb) {
        sb.setColor(1,1,1,alpha);
        sb.draw(infoSignTR, position.x - 32/MyGdxGame.PPM, position.y - bounds.height/2/MyGdxGame.PPM, 64/ MyGdxGame.PPM, 64/ MyGdxGame.PPM);
    }

    public void drawFont(SpriteBatch sb) {}
    public void drawFilled(ShapeRenderer sr) {}
    public void drawLine(ShapeRenderer sr) {}

    public String getText() {
        return text;
    }
}
