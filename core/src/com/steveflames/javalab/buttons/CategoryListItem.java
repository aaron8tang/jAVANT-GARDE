package com.steveflames.javalab.buttons;

import java.util.ArrayList;

/**
 * Created by Flames on 12/10/2017.
 */

public class CategoryListItem {

    private String name;
    private ArrayList<LevelListItem> levels;

    public CategoryListItem(String name) {
        this.name = name;
        loadLevels();
    }

    private void loadLevels() {
        levels.add(new LevelListItem(1, "Hello World!"));
        levels.add(new LevelListItem(2, "Variables"));
    }
}
