package com.steveflames.javalab.buttons;

/**
 * Created by Flames on 12/10/2017.
 */

public class LevelListItem {

    private String id;
    private String name;


    public LevelListItem(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
