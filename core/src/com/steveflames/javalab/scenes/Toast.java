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
    private int numOfPages =0;

    private ArrayList<StringBuilder> linesOfText;
    private int currentLine = -1;

    private long timerMillis;
    private ArrayList<StringBuilder> drawStrings = new ArrayList<StringBuilder>();
    private int letterPtr = 0;
    private int linePtr = 0;

    private float nextPromptOffset = 0;

    Toast() {
        rect = new Rectangle(10, MyGdxGame.HEIGHT + 260, MyGdxGame.WIDTH-20, 160);
        glyphLayout = new GlyphLayout();
        linesOfText = new ArrayList<StringBuilder>();
        if(!MyGdxGame.platformDepended.deviceHasKeyboard())
            nextPromptOffset = 90;
        drawStrings.add(new StringBuilder());
        drawStrings.add(new StringBuilder());
        drawStrings.add(new StringBuilder());
    }

    void newToast(String text) {
        this.text = text;
        currentState = State.INCOMING;
        glyphLayout.setText(Fonts.small, text);
        currentPage = 0;
        numOfPages = 0;
        currentLine = -1;
        linesOfText.clear();

        breakTextIntoLines();
        timerMillis = TimeUtils.millis();

        //for(StringBuilder s : linesOfText)
          //  System.out.println("EDW: " + s);
    }

    private void breakTextIntoLines() {
        if(glyphLayout.width > rect.width - 170 || text.contains("\n")) { //more than one line
            GlyphLayout tempGlyphLayout = new GlyphLayout();
            StringBuilder currentChar = new StringBuilder();
            for(int i=0; i<text.length(); i++) { //parse each character of the text
                if(text.charAt(i) != '\n') {
                    if(currentChar.length()!=0 || text.charAt(i)!=' ') //if new line and next character is space, ignore it
                        currentChar.append(text.charAt(i));

                    tempGlyphLayout.setText(Fonts.small, currentChar);
                    if (tempGlyphLayout.width >= rect.width - 170 && currentChar.length()!=0) { //check characters sum width
                        addNewLine(currentChar.toString());
                        currentChar.setLength(0);
                    }
                }
                else {
                    addNewLine(currentChar.toString());
                    currentChar.setLength(0);
                }
            }

            int spaceIndex=0; //remove last line's spaces before the first word
            for(int i=0; i<currentChar.length(); i++) {
                if(currentChar.charAt(i) == ' ')
                    spaceIndex++;
                else
                    break;
            }
            addNewLine(currentChar.substring(spaceIndex)); //add final line
        }
        else { //only 1 line
            linesOfText.add(new StringBuilder(text));
        }
    }

    private void addNewLine(String currentChar) {
        currentLine++;
        if(currentLine==3) {
            numOfPages++;
            currentLine = 0;
        }
        if(linesOfText.size() <= numOfPages)
            linesOfText.add(new StringBuilder());
        linesOfText.get(numOfPages).append(currentChar);
        if(currentLine!=2)
            linesOfText.get(numOfPages).append("\n");
    }

    public void update(float dt) {
        if (currentState == State.INCOMING) {
            if (rect.y - SPEED * dt > MyGdxGame.HEIGHT - rect.height - 65) {
                rect.y -= SPEED * dt;
            } else {
                rect.y = MyGdxGame.HEIGHT - rect.height - 65;
                currentState = State.WRITING;
                timerMillis = TimeUtils.millis();
            }
        }
        else if (currentState == State.WRITING) {
            if (TimeUtils.timeSinceMillis(timerMillis) > 30) {
                timerMillis = TimeUtils.millis();

                if(linesOfText.get(currentPage).toString().charAt(letterPtr) == '\n')
                    linePtr++;
                else
                    drawStrings.get(linePtr).append(linesOfText.get(currentPage).toString().charAt(letterPtr));
                //System.out.printf("|"+ linesOfText.get(currentPage).toString().charAt(letterPtr) +"|  ");

                letterPtr++;
                if(letterPtr >= linesOfText.get(currentPage).length()) {
                    letterPtr = 0;
                    linePtr = 0;
                    currentState = State.READY;
                }
            }
        }
        else if (currentState == State.NEXT) {
            drawStrings.get(0).setLength(0);
            drawStrings.get(1).setLength(0);
            drawStrings.get(2).setLength(0);
            currentState = State.WRITING;
            currentPage++;
            if (currentPage > numOfPages) {
                currentPage--;
                currentState = State.LEAVING;
            }
        }
        else if (currentState == State.SKIP) {
            drawStrings.get(0).setLength(0); //reset drawStrings
            drawStrings.get(1).setLength(0);
            drawStrings.get(2).setLength(0);
            linePtr = 0;
            for(int i=0; i<linesOfText.get(currentPage).length(); i++) { //append all chars to corresponding drawStrings
                if (linesOfText.get(currentPage).charAt(i) == '\n')
                    linePtr++;
                else
                    drawStrings.get(linePtr).append(linesOfText.get(currentPage).charAt(i));
            }
            letterPtr = 0;
            linePtr = 0;
            currentState = State.READY;
        }
        else if (currentState == State.LEAVING) {
            if (currentState != State.LEFT) {
                rect.y += SPEED * dt;
                if (rect.y > MyGdxGame.HEIGHT) {
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

            for(int i=0; i<drawStrings.size(); i++)
                Fonts.small.draw(sb, drawStrings.get(i), rect.x + 120, rect.y + rect.height - 20 - i*40);
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
