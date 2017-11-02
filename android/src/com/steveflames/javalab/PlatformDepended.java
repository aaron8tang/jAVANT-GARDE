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
        return "Use the ONSCREEN BUTTONS to move, jump and use items\n" +
                "BACK to exit";
    }

    @Override
    public boolean deviceHasKeyboard() {
        return false;
    }
}
