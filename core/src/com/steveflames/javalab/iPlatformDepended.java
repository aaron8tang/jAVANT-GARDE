package com.steveflames.javalab;

/**
 * Created by Flames on 14/10/2017.
 */

public interface iPlatformDepended {
    String getNextPrompt();
    String getLevel1Tip();
    boolean deviceHasKeyboard();
}
