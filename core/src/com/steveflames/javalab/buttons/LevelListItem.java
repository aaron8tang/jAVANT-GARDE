package com.steveflames.javalab.buttons;

/**
 * Created by Flames on 12/10/2017.
 */

public class LevelListItem {

    private int id;
    private String name;


    public LevelListItem(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
