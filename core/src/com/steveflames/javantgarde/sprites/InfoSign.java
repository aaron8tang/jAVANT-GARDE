package com.steveflames.javantgarde.sprites;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.steveflames.javantgarde.screens.PlayScreen;

/**
 * Created by Flames on 4/10/2017.
 */

public class InfoSign extends GameObject {

    private String text = "";

    public InfoSign(String name, World world, TiledMap map, Rectangle bounds, float alpha) {
        super(name, world, map, bounds, true);
        this.alpha = alpha;

        if (com.steveflames.javantgarde.tools.global.MyFileReader.exists("txt/info/" + name + ".txt"))
            text = com.steveflames.javantgarde.tools.global.MyFileReader.readFile("txt/info/" + name + ".txt");
    }

    public void drawUsePrompt(SpriteBatch sb) {
        if(usable) {
            com.steveflames.javantgarde.tools.global.Fonts.medium.setColor(Color.RED);
            sb.draw(com.steveflames.javantgarde.tools.global.Loader.eyeT, bounds.x + bounds.width / 2 - 25 + PlayScreen.getHudCameraOffsetX(), bounds.y + bounds.height + 40, 50, 50);
        }
    }

    public void update(float dt) {
        updateAlpha(dt);
    }

    public void drawFontScaled(SpriteBatch sb) {
        sb.setColor(1,1,1,alpha);
        sb.draw(com.steveflames.javantgarde.tools.global.Loader.infoSignT, (bounds.x + 32)/ com.steveflames.javantgarde.MyGdxGame.PPM, bounds.y/ com.steveflames.javantgarde.MyGdxGame.PPM, 64/ com.steveflames.javantgarde.MyGdxGame.PPM, 64/ com.steveflames.javantgarde.MyGdxGame.PPM);
    }

    public void drawFont(SpriteBatch sb) {}
    public void drawFilled(ShapeRenderer sr) {}
    public void drawLine(ShapeRenderer sr) {}

    public String getText() {
        return text;
    }
}
