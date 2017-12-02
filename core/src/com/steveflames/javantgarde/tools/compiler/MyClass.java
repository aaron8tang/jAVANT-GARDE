package com.steveflames.javantgarde.tools.compiler;

import java.util.ArrayList;

/**
 * Created by Flames on 5/11/2017.
 */

public class MyClass {

    private String name;
    private String code;
    private ArrayList<MyVariable> fields = new ArrayList<MyVariable>();
    private ArrayList<com.steveflames.javantgarde.tools.compiler.MyMethod> methods = new ArrayList<com.steveflames.javantgarde.tools.compiler.MyMethod>();
    private ArrayList<String> errors = new ArrayList<String>();
    private ArrayList<String> methodsCalled = new ArrayList<String>();

    private boolean classDeclared = false;
    private static boolean mainDeclared = false;
    private boolean endOfClass = false;

    public MyClass(String name, String code) {
        this.name = name;
        this.code = code;

        if(name.equals("Lever")) {
            methods.add(new com.steveflames.javantgarde.tools.compiler.MyMethod("package", "void", "pull", new ArrayList<MyVariable>(), ""));
        }
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public ArrayList<MyVariable> getFields() {
        return fields;
    }

    void addErrorInLine(String error, int lineN) {
        if(error==null) {
            if (!errors.contains("[RED]Error: in line " + lineN + " in file " + name + ".java[]"))
                errors.add("[RED]Error: in line " + lineN + " in file " + name + ".java[]");
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

    public void setClassDeclared(boolean classDeclared) {
        this.classDeclared = classDeclared;
    }

    public boolean isClassDeclared() {
        return classDeclared;
    }

    public ArrayList<com.steveflames.javantgarde.tools.compiler.MyMethod> getMethods() {
        return methods;
    }

    public void addMethod(com.steveflames.javantgarde.tools.compiler.MyMethod method) {
        methods.add(method);
    }

    public static boolean isMainDeclared() {
        return mainDeclared;
    }

    public static void setMainDeclared(boolean mainDeclared) {
        MyClass.mainDeclared = mainDeclared;
    }

    public boolean isEndOfClass() {
        return endOfClass;
    }

    public void setEndOfClass(boolean endOfClass) {
        this.endOfClass = endOfClass;
    }

    public ArrayList<String> getMethodsCalled() {
        return methodsCalled;
    }
}
