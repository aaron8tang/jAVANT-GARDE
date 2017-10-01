package com.steveflames.javalab.buttons;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.steveflames.javalab.tools.Fonts;

/**
 * Created by Flames on 25/9/2017.
 */

public class LineOfCode extends Button {

    private boolean editable;
    private int lineNum;
    public static int linesTotal = 0;
    public static final int LEFTPAD = 60;

    public LineOfCode(String text, Rectangle rect, boolean editable) {
        super(text,rect, Fonts.xsmallMono);
        this.editable = editable;
        lineNum = linesTotal;
        LineOfCode.linesTotal++;
    }

    @Override
    public void drawFont(SpriteBatch sb) {
        font.draw(sb, text, rect.x + LEFTPAD, rect.y + rect.height/2 + glyphLayout.height/2);
        font.draw(sb, lineNum+"", rect.x + 10, rect.y + rect.height/2 + 6);
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }
}
