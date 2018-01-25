package com.steveflames.javantgarde;

/**
 * The iPlatformDepended interface is implemented from every
 * Launcher and provides information to the core about the device
 * that is currently running the game.
 */

public interface iPlatformDepended {

    boolean deviceHasKeyboard();

    void setDeviceHasKeyboard(boolean bool);

    boolean isHTML();

    boolean isPC();
}
