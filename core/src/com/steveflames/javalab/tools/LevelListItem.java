package com.steveflames.javalab.tools;


/**
 * Created by Flames on 12/10/2017.
 */

public class LevelListItem {

    private String id;
    private String name;
    private String categoryName;


    public LevelListItem(String categoryName, String id, String name) {
        this.categoryName = categoryName;
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategoryName() {
        return categoryName;
    }
}
