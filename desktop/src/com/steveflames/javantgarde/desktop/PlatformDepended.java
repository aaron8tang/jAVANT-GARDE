package com.steveflames.javantgarde.desktop;

/**
 * Created by Flames on 14/10/2017.
 */

public class PlatformDepended implements com.steveflames.javantgarde.iPlatformDepended {
    @Override
    public String getNextPrompt() {
        return "Press ENTER";
    }

    @Override
    public String getLevel1Tip() {
        return "Hello and welcome to jAVANT-GARDE tutorials!\n" +
                "Here you will get the chance to learn the basics of programming in Java from scratch - let's begin!\n" +
                "Use the ARROWS to move around and jump\n" +
                "ENTER to use item\n" +
                "ESCAPE to exit";
    }

    @Override
    public boolean deviceHasKeyboard() {
        return true;
    }

    @Override
    public boolean isHTML() {
        return false;
    }
}
