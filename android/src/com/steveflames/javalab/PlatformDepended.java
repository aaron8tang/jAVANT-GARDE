package com.steveflames.javalab;

/**
 * Created by Flames on 14/10/2017.
 */

public class PlatformDepended implements iPlatformDepended {
    @Override
    public String getNextPrompt() {
        return "TAP";
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
        return false;
    }
}
