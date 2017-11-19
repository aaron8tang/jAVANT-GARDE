package com.steveflames.javalab.tools.compiler;

import java.util.ArrayList;

/**
 * Created by Flames on 6/11/2017.
 */

public class MyMethod {

    private String modifier;
    private String returnType;
    private String name;
    private ArrayList<MyVariable> arguments;
    private String code;

    private ArrayList<MyVariable> localVariables = new ArrayList<MyVariable>();

    public MyMethod(String modifier, String returnType, String name, ArrayList<MyVariable> arguments, String code) {
        this.modifier = modifier;
        this.returnType = returnType;
        this.name = name;
        this.arguments = arguments;
        this.code = code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }
}
