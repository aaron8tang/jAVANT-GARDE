package com.steveflames.javalab.scenes;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;
import com.steveflames.javalab.MyGdxGame;
import com.steveflames.javalab.buttons.Button;
import com.steveflames.javalab.buttons.LineOfCode;
import com.steveflames.javalab.tools.Fonts;

import java.util.ArrayList;

/**
 * Created by Flames on 25/9/2017.
 */

public class Editor extends Button implements InputProcessor{
    private static final int SPEED = 800;

    public enum State { INCOMING, READY, WRITING, LEAVING, LEFT};
    private State currentState = State.INCOMING;

    private Rectangle editRect;
    private Rectangle tabsRect;

    private ArrayList<LineOfCode> lines = new ArrayList<LineOfCode>();
    private ArrayList<Button> tabs = new ArrayList<Button>();

    private Camera cam;

    private int currentTabPtr = 0;
    private int indentCounter = 0;

    private Rectangle cursor = new Rectangle(0,-100,2,20);
    private long cursorTimer;
    private int currentLinePtr = -1;
    private GlyphLayout inputCharGlyphLayout = new GlyphLayout();
    private Vector3 touchCoords = new Vector3();
    private Rectangle touchRect = new Rectangle();


    public Editor(Camera cam) { //na pairnei to level, to pc, to progress
        super("", new Rectangle(cam.position.x+ cam.viewportWidth/2, 190, 700, 555), Fonts.small);
        this.cam = cam;

        editRect = new Rectangle();
        tabsRect = new Rectangle();
        Gdx.input.setInputProcessor(this);
    }

    private void updateRects() {
        editRect.set(rect.x + 3, rect.y + 3, rect.width - 6, rect.height - 40);
        tabsRect.set(rect.x + 3, rect.y + 5 + rect.height - 40, rect.width - 6, 32);
        addTab("Main.java");
        addTab("Player.java");

        addLine("package yoyoyo;", false);
        addLine("", false);
        addLine("import yooo;", false);
        addLine("", false);
        addLine("public class Main {", false);
        addLine("", false);
        addLine("public static void main (String[] arg) {", false);
        addLine("", true);
        addLine("", true);
        addLine("System.out.println(\"Hello World!\");", false);
        addLine("}", false);
        addLine("}", false);

        //
        changeCurrentTab(0);
    }

    private void addTab(String text) {
        tabs.add(new Button(text, new Rectangle(0, -200, 20, 20), Fonts.xsmallMono));
        if(tabs.size()>1) {
            tabs.get(tabs.size() - 1).getRect().x = tabs.get(tabs.size() - 2).getRect().x + tabs.get(tabs.size() - 2).getRect().width + 1;
        }
        else {
            tabs.get(tabs.size() - 1).getRect().x = tabsRect.x;
        }
        tabs.get(tabs.size()-1).getRect().y = tabsRect.y;
        tabs.get(tabs.size()-1).getRect().width = tabs.get(tabs.size()-1).getGlyphLayout().width + 26;
        tabs.get(tabs.size()-1).getRect().height = tabs.get(tabs.size()-1).getGlyphLayout().height + 10;
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            touchCoords.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            cam.unproject(touchCoords);
            touchRect.set(touchCoords.x, touchCoords.y, 2,2);

            for(int i=0; i<lines.size(); i++) {
                if(touchRect.overlaps(lines.get(i).getRect())) {
                    if (lines.get(i).isEditable()) {
                        cursor.setX(lines.get(i).getRect().x + LineOfCode.LEFTPAD + lines.get(i).getGlyphLayout().width);
                        cursor.setY(lines.get(i).getRect().y + 2);
                        currentLinePtr = i;
                    }
                }
            }
        }

    }

    private void addLine(String text, boolean editable) {
        if(text.contains("}"))
            indentCounter--;
        for(int i=0; i<indentCounter; i++)
            text = "    " + text;
        if(text.contains("{"))
            indentCounter++;


        lines.add(new LineOfCode(text, new Rectangle(0, -200, 20, 20), editable));
        if(lines.size()>1) {
            lines.get(lines.size() - 1).getRect().y = lines.get(lines.size() - 2).getRect().y - lines.get(lines.size() - 2).getRect().height - 1;
        }
        else {
            lines.get(lines.size()-1).getRect().y = editRect.y + editRect.height - lines.get(lines.size()-1).getGlyphLayout().height - 12;
        }
        lines.get(lines.size() - 1).getRect().x = editRect.x;
        lines.get(lines.size()-1).getRect().width = editRect.width;
        lines.get(lines.size()-1).getRect().height = lines.get(lines.size()-1).getGlyphLayout().height + 12;

        //set the cursor to the first editable line
        if(editable && currentLinePtr<0) {
            currentLinePtr = lines.size() - 1;
            System.out.println(currentLinePtr);
            cursor.setX(lines.get(currentLinePtr).getRect().x + LineOfCode.LEFTPAD + lines.get(currentLinePtr).getGlyphLayout().width);
            cursor.setY(lines.get(currentLinePtr).getRect().y + 2);
        }
    }

    private void changeCurrentTab(int tabPtr) {
        currentTabPtr = tabPtr;
        tabs.get(currentTabPtr).getRect().height = tabsRect.height;
    }

    public void update(float dt) {
        if(currentState == State.INCOMING) {
            if (rect.x + rect.width > cam.position.x+ cam.viewportWidth/2) {
                rect.x -= SPEED * dt;
            } else {
                rect.x = cam.position.x+ cam.viewportWidth/2 - rect.width;
                updateRects();
                currentState = State.READY;
            }
        }
        else {
            handleInput();
        }
    }

    public void render(SpriteBatch sb, ShapeRenderer sr) {
        Fonts.xsmallMono.setColor(Color.BLACK);

        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(Color.BLACK);
        sr.rect(rect.x, rect.y, rect.width, rect.height);
        sr.end();

        if(currentState == State.INCOMING) { //draw PC splash screen
            sb.begin();
            Fonts.big.setColor(Color.WHITE);
            Fonts.big.draw(sb, "CODE", rect.x + rect.width/2 - 60, rect.y + rect.height/2 +20);
            sb.end();
        }
        else { //draw the editor
            sr.begin(ShapeRenderer.ShapeType.Filled);
            sr.setColor(Color.WHITE);
            sr.rect(editRect.x, editRect.y, editRect.width, editRect.height);
            sr.setColor(Color.GRAY);
            sr.rect(tabsRect.x, tabsRect.y, tabsRect.width, tabsRect.height);
            for(int i = 0; i< tabs.size(); i++) {
                if(i!=currentTabPtr)
                    sr.setColor(Color.LIGHT_GRAY);
                else
                    sr.setColor(Color.WHITE);
                tabs.get(i).drawShape(sr);
            }
            for(LineOfCode line: lines) {
                if(line.isEditable()) {
                    sr.setColor(Color.YELLOW);
                    line.drawShape(sr);
                }
            }
            sr.end();

            sr.begin(ShapeRenderer.ShapeType.Line);
            sr.setColor(Color.BLACK);
            for(Button btn: tabs)
                btn.drawShape(sr);
            sr.end();

            sb.begin();
            for(Button btn: tabs)
                btn.drawFont(sb);
            for(int i=0; i<lines.size(); i++) {
                lines.get(i).drawFont(sb);
            }
            sb.end();

            sr.begin(ShapeRenderer.ShapeType.Filled);
            sr.setColor(Color.BLACK);
            if (TimeUtils.timeSinceMillis(cursorTimer) > 400) {
                if (TimeUtils.timeSinceMillis(cursorTimer) > 800)
                    cursorTimer = TimeUtils.millis();
                sr.rect(cursor.x, cursor.y, cursor.width, cursor.height);
            }
            sr.end();
        }

    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.BACKSPACE) {
            if (lines.get(currentLinePtr).getText().length() > 0) {
                inputCharGlyphLayout.setText(Fonts.xsmallMono, String.valueOf(lines.get(currentLinePtr).getText().charAt(lines.get(currentLinePtr).getText().length() - 1)));
                lines.get(currentLinePtr).getGlyphLayout().setText(Fonts.xsmallMono, String.valueOf(lines.get(currentLinePtr).getText().charAt(lines.get(currentLinePtr).getText().length() - 1)));
                lines.get(currentLinePtr).setText(lines.get(currentLinePtr).getText().substring(0, lines.get(currentLinePtr).getText().length() - 1));
                cursor.x -= inputCharGlyphLayout.width;
            }
        }
        else if (keycode == Input.Keys.TAB) {
            inputCharGlyphLayout.setText(Fonts.xsmallMono, "    ");
            if (cursor.x + inputCharGlyphLayout.width < lines.get(currentLinePtr).getRect().x + lines.get(currentLinePtr).getRect().width) {
                lines.get(currentLinePtr).setText(lines.get(currentLinePtr).getText() + "    ");
                cursor.x += inputCharGlyphLayout.width;
            }

        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        if(currentLinePtr > 0) {
            if ((character + "").matches("[a-zA-z0-9`~!@#$%^&*()-=_+,<.>/?;:'\"{\\}| ]")) {
                inputCharGlyphLayout.setText(Fonts.xsmallMono, String.valueOf(character));
                if (cursor.x + inputCharGlyphLayout.width < lines.get(currentLinePtr).getRect().x + lines.get(currentLinePtr).getRect().width) {
                    lines.get(currentLinePtr).getGlyphLayout().setText(Fonts.xsmallMono, lines.get(currentLinePtr).getText() + String.valueOf(character));
                    lines.get(currentLinePtr).setText(lines.get(currentLinePtr).getText() + character);
                    cursor.x += inputCharGlyphLayout.width;
                }
            }
        }
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    public State getCurrentState() {
        return currentState;
    }
}
