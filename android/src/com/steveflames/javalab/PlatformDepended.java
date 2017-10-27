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
        return "TAP to move around and slide up to jump\n" +
                "TAP on a usable item to use\n" +
                "BACK to exit";
    }
}
