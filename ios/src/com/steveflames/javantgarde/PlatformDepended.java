package com.steveflames.javantgarde;

/**
 * Created by Flames on 11/11/2017.
 */

public class PlatformDepended implements iPlatformDepended {
    @Override
    public String getNextPrompt() {
        return "Press ENTER";
    }

    @Override
    public String getLevel1Tip() {
        return "Hello and welcome to jAVANT-GARDE tutorials!\n" +
                "Here you will get the chance to learn the basics of programming in Java from scratch - let's begin!\n" +
                "Use the ONSCREEN BUTTONS to move, jump and use items\n" +
                "BACK to exit";
    }

    @Override
    public boolean deviceHasKeyboard() {
        return true;
    }
}
