package com.steveflames.javalab.scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;
import com.steveflames.javalab.MyGdxGame;
import com.steveflames.javalab.tools.global.Fonts;

import java.util.ArrayList;

/**
 * Created by Flames on 24/9/2017.
 */

public class Toast {
    private static final int SPEED = 700;
    public enum State { INCOMING, READY, WRITING, NEXT, SKIP, LEAVING, LEFT}
    private State currentState = State.LEFT;

    private Rectangle rect;
    private GlyphLayout glyphLayout;
    private String text;

    private int currentPage;
    private int numOfPages;

    private ArrayList<ArrayList<String>> linesOfText;
    private ArrayList<ArrayList<String>> finalLinesOfText; //to draw the toast letter by letter
    private int k=0;

    private long timerMillis;
    private int letterPtr = 0;
    private int linePtr = 0;

    private float nextPromptOffset = 0;

    Toast() {
        rect = new Rectangle(10, -150, MyGdxGame.WIDTH-20, 150);
        glyphLayout = new GlyphLayout();
        linesOfText = new ArrayList<ArrayList<String>>();
        finalLinesOfText = new ArrayList<ArrayList<String>>();
        if(!MyGdxGame.platformDepended.deviceHasKeyboard())
            nextPromptOffset = 90;
    }

    void newToast(String text) {
        this.text = text;
        currentState = State.INCOMING;
        glyphLayout.setText(Fonts.small, text);
        currentPage = 0;
        numOfPages = 1;
        linesOfText.clear();
        finalLinesOfText.clear();
        linesOfText.add(new ArrayList<String>());

        breakTextIntoLines();
        timerMillis = TimeUtils.millis();

        //initialize finalLinesOfText
        for(ArrayList<String> sList: linesOfText) {
            finalLinesOfText.add(new ArrayList<String>());
            for(String s: sList) {
                finalLinesOfText.get(finalLinesOfText.size()-1).add("");
            }
        }
    }

    private void breakTextIntoLines() {
        if(glyphLayout.width > rect.width - 170 || text.contains("\n")) { //more than one line
            GlyphLayout tempGlyphLayout = new GlyphLayout();
            String tempText = "";
            for(int i=0; i<text.length(); i++) {
                if(text.charAt(i) != '\n') {
                    if(!tempText.equals("") || text.charAt(i)!=' ') //if new line and next character is space, ignore it
                        tempText += text.charAt(i);
                    tempGlyphLayout.setText(Fonts.small, tempText);
                    //System.out.println("TEMP: " + tempText);
                    if (tempGlyphLayout.width >= rect.width - 170 && !tempText.equals("")) { //check characters sum width
                        //System.out.println("NEWLINE");
                        addNewLine(tempText);
                        tempText = "";
                    }
                }
                else {
                    addNewLine(tempText);
                    tempText = "";
                }
            }
            int spaceIndex=0; //remove last lines spaces before the first word
            for(int i=0; i<tempText.length(); i++) {
                if(tempText.charAt(i) == ' ')
                    spaceIndex++;
                else
                    break;
            }
            tempText = tempText.substring(spaceIndex);
            addNewLine(tempText);
        }
        else { //only 1 line
            linesOfText.get(0).add(text);
        }
    }

    private void addNewLine(String tempText) {
        if(linesOfText.get(k).size()==3) {
            k++;
            linesOfText.add(new ArrayList<String>());
            numOfPages++;
        }
        linesOfText.get(k).add(tempText);
    }

    public void update(float dt) {
        if (currentState == State.INCOMING) {
            if (rect.y + rect.height <= 160) {
                rect.y += SPEED * dt;
            } else {
                rect.y = 160 - rect.height;
                currentState = State.WRITING;
                timerMillis = TimeUtils.millis();
            }
        } else if (currentState == State.WRITING) {
            if (TimeUtils.timeSinceMillis(timerMillis) > 30) {
                timerMillis = TimeUtils.millis();

                if (linesOfText.get(currentPage).get(linePtr).length() >= 1)
                    finalLinesOfText.get(currentPage).set(linePtr, linesOfText.get(currentPage).get(linePtr).substring(0, letterPtr));
                if (letterPtr >= linesOfText.get(currentPage).get(linePtr).length()) {
                    letterPtr = 0;
                    linePtr++;
                    if (linePtr >= linesOfText.get(currentPage).size()) {
                        currentState = State.READY;
                        linePtr = 0;
                    }
                }
                letterPtr++;
            }
        } else if (currentState == State.NEXT) {
            currentState = State.WRITING;
            currentPage++;
            if (currentPage > numOfPages - 1) {
                currentPage--;
                currentState = State.LEAVING;
            }
        } else if (currentState == State.SKIP) {
            finalLinesOfText.set(currentPage, linesOfText.get(currentPage));
            letterPtr = 0;
            linePtr = 0;
            currentState = State.READY;
        } else if (currentState == State.LEAVING) {
            if (currentState != State.LEFT) {
                rect.y -= SPEED * dt;
                if (rect.y + rect.height < 0) {
                    currentState = State.LEFT;
                    Hud.showAndroidInputTable();
                }
            }
        }
    }

    void drawFilled(ShapeRenderer sr) {
        sr.setColor(Color.BLACK);
        sr.rect(rect.x, rect.y, rect.width, rect.height);
        sr.setColor(Color.WHITE);
        sr.rect(rect.x + 2, rect.y + 2, rect.width -4, rect.height-4);
    }

    void drawFont(SpriteBatch sb) {
        if(currentState == State.READY || currentState == State.WRITING || currentState == State.LEAVING) {
            Fonts.small.setColor(Color.BLACK);
            Fonts.xsmall.setColor(Color.BLACK);
            for(int i=0; i<finalLinesOfText.get(currentPage).size(); i++) {
                Fonts.small.draw(sb, finalLinesOfText.get(currentPage).get(i), rect.x + 120, rect.y + rect.height - 20 - (33) * i);
            }
            Fonts.xsmall.draw(sb, MyGdxGame.platformDepended.getNextPrompt(), rect.x + rect.width - 180 + nextPromptOffset, rect.y + 25);
        }
    }


    public State getCurrentState() {
        return currentState;
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }

    public boolean isShowing() {
        return currentState!=State.LEFT;
    }
}
