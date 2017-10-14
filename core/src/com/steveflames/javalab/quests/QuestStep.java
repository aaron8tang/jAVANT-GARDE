package com.steveflames.javalab.quests;

import java.util.ArrayList;

/**
 * Created by Flames on 6/10/2017.
 */

public class QuestStep {

    private String text;
    private ArrayList<String> hints = new ArrayList<String>();
    private int hintPtr = -1;

    public QuestStep(String text) {
        this.text = text;
    }

    public void addHint(String hint) {
        hints.add(hint);
    }

    public String getText() {
        return text;
    }

    public ArrayList<String> getHints() {
        return hints;
    }

    public int getHintPtr() {
        return hintPtr;
    }

    public void setHintPtr(int hintPtr) {
        this.hintPtr = hintPtr;
    }

    public void incrementHintPtr() {
        hintPtr++;
    }
}
