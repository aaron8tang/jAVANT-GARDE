package com.steveflames.javalab.sprites;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.steveflames.javalab.quests.Quest;
import com.steveflames.javalab.screens.PlayScreen;
import com.steveflames.javalab.tools.MyFileReader;
import com.steveflames.javalab.tools.global.Fonts;
import com.steveflames.javalab.tools.global.Loader;

/**
 * Created by Flames on 24/9/2017.
 */

public class Pc extends InteractiveTileObject {

    private String editorText;
    private Quest quest;

    public Pc(String name, World world, TiledMap map, Rectangle bounds) {
        super(name, world, map, bounds, true);
        quest = new Quest(name.substring(3));

        if(MyFileReader.exists("txt/pcs/" + name + ".txt"))
            editorText = MyFileReader.readFile("txt/pcs/" + name + ".txt").replaceAll("\r", "");
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
            sb.draw(Loader.fixT, bounds.x + bounds.width / 2 - 30 + PlayScreen.getHudCameraOffsetX(), bounds.y + bounds.height + 20, 60, 60);
            //Fonts.medium.draw(sb, "!", bounds.x + bounds.width / 2 - 10 + Window.getHudCameraOffsetX(), bounds.y + bounds.height + 50);
        }
    }

    public String getEditorText() {
        return editorText;
    }

    public void setEditorText(String text) {
        editorText = text;
    }

    public Quest getQuest() {
        return quest;
    }
}
