package com.steveflames.javantgarde.hud.code_pc.compiler;

/**
 * Created by Flames on 5/11/2017.
 */

public class MyVariable {

    private String type;
    private boolean array;
    private String name;
    private String value;

    public MyVariable(String type, String name, String value) {
        this.type = type;
        this.name = name;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isArray() {
        return array;
    }

    public void setArray(boolean array) {
        this.array = array;
    }
}
