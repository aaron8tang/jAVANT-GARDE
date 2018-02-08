package com.steveflames.javantgarde.desktop;

/**
 * Implements the iPlatformDepended interface and provides
 * information to the core about the device that is currently
 * running the game.
 */

public class PlatformDepended implements com.steveflames.javantgarde.iPlatformDepended {

    private boolean keyboard = true;

    @Override
    public boolean deviceHasKeyboard() {
        return keyboard;
    }

    @Override
    public void setDeviceHasKeyboard(boolean bool) {
        keyboard = bool;
    }
}
