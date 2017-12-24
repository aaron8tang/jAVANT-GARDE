package com.steveflames.javantgarde.hud.code_pc.compiler;

import java.util.ArrayList;

/**
 * Created by Flames on 5/11/2017.
 */

public class MyClass {

    private String name;
    private String code;
    private ArrayList<MyVariable> variables = new ArrayList<MyVariable>();
    private ArrayList<MyMethod> methods = new ArrayList<MyMethod>();
    private ArrayList<String> errors = new ArrayList<String>();
    private ArrayList<String> methodsCalled = new ArrayList<String>();

    private boolean classDeclared = false;

    public MyClass(String name, String code) {
        this.name = name;
        this.code = code;

        if(name.equals("Lever")) {
            methods.add(new MyMethod("package", "void", "pull", new ArrayList<MyVariable>(), ""));
        }
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public ArrayList<MyVariable> getVariables() {
        return variables;
    }

    void addErrorInLine(String error, int lineN) {
        if(error==null) {
            if(classDeclared) {
                if (!errors.contains("[RED]Error: in line " + lineN + " in file " + name + ".java[]"))
                    errors.add("[RED]Error: in line " + lineN + " in file " + name + ".java[]");
            }
        }
        else {
            if (!errors.contains("[RED]Error: "+error+" (line " + lineN + " in file " + name + ".java)[]"))
                errors.add("[RED]Error: "+error+" (line " + lineN + " in file " + name + ".java)[]");
        }
    }

    void addError(String error) {
        if(!errors.contains("[RED]"+error+"[]"))
            errors.add("[RED]"+error+"[]");
    }

    void removeError(String error) {
        errors.remove("[RED]"+error+"[]");
    }

    ArrayList<String> getErrors() {
        return errors;
    }

    void setClassDeclared(boolean classDeclared) {
        this.classDeclared = classDeclared;
    }

    boolean isClassDeclared() {
        return classDeclared;
    }

    public ArrayList<MyMethod> getMethods() {
        return methods;
    }

    void addMethod(MyMethod method) {
        methods.add(method);
    }

    public ArrayList<String> getMethodsCalled() {
        return methodsCalled;
    }
}
