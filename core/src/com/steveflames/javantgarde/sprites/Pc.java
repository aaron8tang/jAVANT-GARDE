package com.steveflames.javantgarde.sprites;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.steveflames.javantgarde.screens.PlayScreen;
import com.steveflames.javantgarde.tools.global.Fonts;

/**
 * Created by Flames on 24/9/2017.
 */

public class Pc extends GameObject {

    private String editorText;
    private com.steveflames.javantgarde.quests.Quest quest;

    public Pc(String name, World world, TiledMap map, Rectangle bounds) {
        super(name, world, map, bounds, true);
        quest = new com.steveflames.javantgarde.quests.Quest(name.substring(3));

        if(com.steveflames.javantgarde.tools.global.MyFileReader.exists("txt/pcs/" + name + ".txt"))
            editorText = com.steveflames.javantgarde.tools.global.MyFileReader.readFile("txt/pcs/" + name + ".txt").replaceAll("\r", "");
        else
            editorText = "class MyClass {\n" +
                    "\n" +
                    "    public static void main(String[] args) {\n" +
                    "        \n" +
                    "    }\n" +
                    "\n" +
                    "}";
    }

    public void drawUsePrompt(SpriteBatch sb) {
        if(usable) {
            Fonts.medium.setColor(Color.RED);
            sb.draw(com.steveflames.javantgarde.tools.global.Loader.fixT, bounds.x + bounds.width / 2 - 30 + PlayScreen.getHudCameraOffsetX(), bounds.y + bounds.height + 20, 60, 60);
        }
    }

    public void update(float dt) {}
    public void drawFilled(ShapeRenderer sr) {}
    public void drawLine(ShapeRenderer sr) {}
    public void drawFont(SpriteBatch sb) {}
    public void drawFontScaled(SpriteBatch sb) {}

    public String getEditorText() {
        return editorText;
    }

    public void setEditorText(String text) {
        editorText = text;
    }

    public com.steveflames.javantgarde.quests.Quest getQuest() {
        return quest;
    }
}
