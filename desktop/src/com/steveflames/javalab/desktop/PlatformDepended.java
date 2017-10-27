package com.steveflames.javalab.desktop;

import com.steveflames.javalab.iPlatformDepended;

/**
 * Created by Flames on 14/10/2017.
 */

public class PlatformDepended implements iPlatformDepended {
    @Override
    public String getNextPrompt() {
        return "Press ENTER";
    }

    @Override
    public String getLevel1Tip() {
        return "Use the arrows to move around\n" +
                "ENTER to use item\n" +
                "ESCAPE to exit";
    }
}
