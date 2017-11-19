package com.steveflames.javalab.quests;

import java.util.ArrayList;

/**
 * Created by Flames on 6/10/2017.
 */

public class QuestStep {

    private String text;
    private ArrayList<String> hints = new ArrayList<String>();
    private int hintPtr = -1;

    QuestStep(String text) {
        this.text = text;
    }

    void addHint(String hint) {
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

    void incrementHintPtr() {
        hintPtr++;
    }

    public void setText(String text) {
        this.text = text;
    }
}
