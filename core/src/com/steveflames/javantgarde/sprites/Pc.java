package com.steveflames.javantgarde.sprites;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.steveflames.javantgarde.MyGdxGame;
import com.steveflames.javantgarde.quests.Quest;
import com.steveflames.javantgarde.tools.Assets;
import com.steveflames.javantgarde.tools.global.Cameras;
import com.steveflames.javantgarde.tools.global.MyFileReader;

/**
 * Implements the pc sprites of the levels.
 * A pc can be a code-pc, a quiz-pc or an order-pc.
 */

public class Pc extends GameObject {

    private String editorText;
    private Quest quest;
    private int pcType; //0=code, 1=quiz, 2=order

    private Assets assets;

    public Pc(String name, World world, TiledMap map, Rectangle bounds, Assets assets) {
        super(name, world, map, bounds, true);
        this.assets = assets;

        if(name.startsWith("quiz")) { //quiz pc
            pcType = 1;
            name = name.substring(4);
            quest = new Quest(name.substring(3));
            editorText = quest.getCurrentQuestStepText();
        }
        else if(name.startsWith("order")) { //order pc
            pcType = 2;
            name = name.substring(5);
            quest = new Quest(name.substring(3));
            editorText = quest.getCurrentQuestStepText();
        }
        else { //code pc
            pcType = 0;
            quest = new Quest(name.substring(3));

            if(MyFileReader.exists("txt/pcs/" + name + ".txt"))
                if(!MyGdxGame.platformDepended.isHTML())
                    editorText = MyFileReader.readFile("txt/pcs/" + name + ".txt").replaceAll("\r", "");
                else
                    editorText = "";
            else
                editorText = "class MyClass {\n" +
                        "\n" +
                        "    public static void main(String[] args) {\n" +
                        "        \n" +
                        "    }\n" +
                        "\n" +
                        "}";
        }

    }

    public void drawUsePrompt(SpriteBatch sb) {
        if(usable) {
            sb.draw(assets.fixTR, position.x*MyGdxGame.PPM - 30 + Cameras.getHudCameraOffsetX(), position.y*MyGdxGame.PPM + 80 + 20, 60, 60);
        }
    }

    public void update(float dt) {}
    public void drawFilled(ShapeRenderer sr) {}
    public void drawLine(ShapeRenderer sr) {}
    public void drawFont(SpriteBatch sb) {}

    public void drawFontScaled(SpriteBatch sb) {
        sb.setColor(1,1,1,alpha);
        sb.draw(assets.pcTR, position.x - bounds.width/2/MyGdxGame.PPM, position.y - bounds.height/2/MyGdxGame.PPM, bounds.width/ MyGdxGame.PPM, bounds.height/ MyGdxGame.PPM);
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

    public int getPcType() {
        return pcType;
    }
}
