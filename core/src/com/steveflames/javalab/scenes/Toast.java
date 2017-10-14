package com.steveflames.javalab.scenes;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;
import com.steveflames.javalab.buttons.Button;
import com.steveflames.javalab.tools.Fonts;

import java.util.ArrayList;

/**
 * Created by Flames on 24/9/2017.
 */

public class Toast extends Button{
    private static final int SPEED = 700;
    public enum State { INCOMING, READY, WRITING, NEXT, SKIP, LEAVING, LEFT};
    private State currentState = State.INCOMING;

    private int currentPage;
    private int numOfPages;

    private ArrayList<ArrayList<String>> linesOfText;
    private ArrayList<ArrayList<String>> finalLinesOfText; //to draw the toast letter by letter
    private int k=0;

    private long timerMillis;
    private int letterPtr = 0;
    private int linePtr = 0;

    public Toast(String text, Camera cam) {
        super(text, new Rectangle(10, -150, cam.viewportWidth-20, 150), Fonts.small);
        linesOfText = new ArrayList<ArrayList<String>>();
        linesOfText.add(new ArrayList<String>());
        currentPage = 0;
        numOfPages = 1;

        breakTextIntoLines();
        timerMillis = TimeUtils.millis();

        //initialize finalLinesOfText
        finalLinesOfText = new ArrayList<ArrayList<String>>();
        for(ArrayList<String> sList: linesOfText) {
            finalLinesOfText.add(new ArrayList<String>());
            for(String s: sList) {
                finalLinesOfText.get(finalLinesOfText.size()-1).add("");
            }
        }
    }

    private void breakTextIntoLines() {
        if(glyphLayout.width > getRect().width - 170 || text.contains("\n")) { //more than one line
            GlyphLayout tempGlyphLayout = new GlyphLayout();
            String tempText = "";
            for(int i=0; i<text.length(); i++) {
                if(text.charAt(i) != '\n') {
                    if(!tempText.equals("") || text.charAt(i)!=' ') //if new line and next character is space, ignore it
                        tempText += text.charAt(i);
                    tempGlyphLayout.setText(Fonts.small, tempText);
                    //System.out.println("TEMP: " + tempText);
                    if (tempGlyphLayout.width >= getRect().width - 170 && !tempText.equals("")) { //check characters sum width
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
        if(currentState == State.INCOMING) {
            if (rect.y + rect.height <= 160) {
                rect.y += SPEED * dt;
            } else {
                currentState = State.WRITING;
                timerMillis = TimeUtils.millis();
            }
        }
        else if(currentState == State.WRITING) {
            if(TimeUtils.timeSinceMillis(timerMillis) > 30) {
                timerMillis = TimeUtils.millis();

                if(linesOfText.get(currentPage).get(linePtr).length()>=1)
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
        }
        else if(currentState == State.NEXT) {
            currentState = State.WRITING;
            currentPage++;
            if(currentPage > numOfPages-1) {
                currentPage--;
                currentState = State.LEAVING;
            }
        }
        else if(currentState == State.SKIP) {
            finalLinesOfText.set(currentPage, linesOfText.get(currentPage));
            letterPtr = 0;
            linePtr = 0;
            currentState = State.READY;
        }
        else if(currentState == State.LEAVING) {
            if(currentState != State.LEFT) {
                getRect().y -= SPEED*dt;
                if(getRect().y + getRect().height < 0)
                    currentState = State.LEFT;
            }
        }
    }

    public void render(SpriteBatch sb, ShapeRenderer sr) {
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(Color.BLACK);
        drawShape(sr);
        sr.setColor(Color.WHITE);
        sr.rect(getRect().x + 2, getRect().y + 2, getRect().width -4, getRect().height-4);
        sr.end();

        if(currentState == State.READY || currentState == State.WRITING || currentState == State.LEAVING) {
            sb.begin();
            Fonts.small.setColor(Color.BLACK);
            Fonts.xsmall.setColor(Color.BLACK);
            for(int i=0; i<finalLinesOfText.get(currentPage).size(); i++) {
                Fonts.small.draw(sb, finalLinesOfText.get(currentPage).get(i), rect.x + 120, rect.y + rect.height - 20 - (33) * i);
            }
            Fonts.xsmall.draw(sb, "Press ENTER", getRect().x + getRect().width - 160, getRect().y + 25);
            sb.end();
        }
    }


    public State getCurrentState() {
        return currentState;
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }
}
