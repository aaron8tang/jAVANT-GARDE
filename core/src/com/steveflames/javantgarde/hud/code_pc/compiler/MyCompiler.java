package com.steveflames.javantgarde.hud.code_pc.compiler;

import com.badlogic.gdx.scenes.scene2d.ui.Label;

import java.util.ArrayList;

/**
 * This class implements the custom compiler of the code-pc.
 * It is quite restricted and needs review.
 */

public class MyCompiler {

    private Label consoleTextArea;
    private ArrayList<MyClass> classes;
    private MyClass compilationClass;

    private String[] lineSplitter;
    private String[] wordSplitter;
    private int currentWordPtr = 0;
    private int lineN = 0;


    public MyCompiler(Label consoleTextArea) {
        this.consoleTextArea = consoleTextArea;
    }


    /* todo
     * lexical analysis
     * syntax analysis
     * semantic analysis
     *
     * LEXICAL
     * cut into words
     *
     * SYNTAX
     * the order of the words that the lexical analysis gave
     *
     * SEMANTIC ERRORS
     * Type mismatch.
     * Undeclared variable.
     * Reserved identifier misuse.
     * Multiple declaration of variable in a scope.
     * Accessing an out of scope variable.
     * Actual and formal parameter mismatch.
     */
    public boolean compile(ArrayList<MyClass> myClasses) {
        classes = myClasses;
        consoleTextArea.setText("");
        int bracketsCounter = 0;

        //parse MyClass and its code
        compilationClass = classes.get(0);
        compilationClass.addError("Error: class not defined in file " + compilationClass.getName() + ".java");
        compilationClass.addError("Error: main method not found");
        //boolean inFieldDeclaration = false;


        lineSplitter = compilationClass.getCode().split("\n"); //get the lines of code

        for(int i=0; i<lineSplitter.length; i++) {
            //prepare lineOfCode
            lineSplitter[i] = prepareLineOfCode(lineSplitter[i]);
        }

        lineN = 0;
        for(String lineOfCode: lineSplitter) { //parse the lines of code
            lineN++;

            //split line and get its words
            if(!lineOfCode.isEmpty()) {
                splitLineToWords(lineOfCode);

                currentWordPtr = 0;
                String nextWord;
                //parse each word of the line
                if (wordSplitter.length > 0) {
                    if (!compilationClass.isClassDeclared()) { //class not yet declared
                        if(wordSplitter[0].equals("class") || wordSplitter[0].equals("public") && isNextWordStrictlyEqualTo("class")) {
                            if ((nextWord = getNextWordInLine()) != null && validVarName(nextWord)) { //2nd word
                                if (!Character.isUpperCase(nextWord.charAt(0))) { //1st letter not capital
                                    compilationClass.addError("[YELLOW]Warning: class names should start with a capital letter.[]");
                                }

                                else {
                                    if(!nextWord.equals("MyClass")) { //wrong class name
                                        compilationClass.addError("Error: class " + nextWord + " should be defined in its own file.");
                                        compilationClass.addError("[YELLOW]Warning: the class name should be same as the file name. (MyClass)[]");
                                    }
                                    else {
                                        if (isNextWordStrictlyEqualTo("{")) {
                                            if (getNextWordInLine() == null) { //CORRECT class declaration
                                                compilationClass.removeError("Error: class not defined in file " + compilationClass.getName() + ".java");
                                                compilationClass.setClassDeclared(true);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        else
                            compilationClass.addErrorInLine(null,lineN);
                    }
                    else { //class is declared previously
                        if (!isMethodDeclaration(wordSplitter[0])
                                && !isSystemOutPrintln()
                                && !isVariableDeclaration()
                                && !(wordSplitter[0].equals("}") && wordSplitter.length == 1)
                                && !isVariableAssignment()
                                && !assignClassValue(wordSplitter[0], "null")
                                && !isMethodCall()
                        ) {
                            compilationClass.addErrorInLine(null, lineN);
                        }
                    }
                }
            }

            //check if '{' are even with '}'
            bracketsCounter += lineOfCode.length() - lineOfCode.replace("{", "").length();
            bracketsCounter -= lineOfCode.length() - lineOfCode.replace("}", "").length();
        }

        if(bracketsCounter != 0)
            compilationClass.addError("Error: uneven brackets in file " + compilationClass.getName() +".java\n[YELLOW]Remember to close any open curly brackets '{' with '}'[]");


        //append errors in console window
        boolean flag = true;
        for(MyClass myClass: myClasses) {
            for(String error: myClass.getErrors()) {
                consoleTextArea.setText(consoleTextArea.getText() + error+"\n");
                flag = false;
            }
        }
        if(flag)
            consoleTextArea.setText(consoleTextArea.getText() + "\n[GREEN](Compilation finished with no errors)[]");
        return flag;
    }

    private void splitLineToWords(String lineOfCode) {
        if (lineOfCode.contains(" ")) {
            wordSplitter = lineOfCode.split(" ");
        }
        else { //if only 1 word, assign it to wordSplitter
            wordSplitter = new String[1];
            wordSplitter[0] = lineOfCode;
        }
    }

    private String prepareLineOfCode(String lineOfCode) {
        //add a space before and after every symbol
        lineOfCode = lineOfCode.replaceAll("\\{", " \\{ ");
        lineOfCode = lineOfCode.replaceAll("\\}", " \\} ");
        lineOfCode = lineOfCode.replaceAll("\\(", " \\( ");
        lineOfCode = lineOfCode.replaceAll("\\)", " \\) ");
        lineOfCode = lineOfCode.replaceAll("\\{", " \\{ ");
        lineOfCode = lineOfCode.replaceAll("\\[", " \\[ ");
        lineOfCode = lineOfCode.replaceAll("=", " = ");
        lineOfCode = lineOfCode.replaceAll(";", " ; ");
        lineOfCode = lineOfCode.replaceAll("<", " < ");
        lineOfCode = lineOfCode.replaceAll(">", " > ");
        lineOfCode = lineOfCode.replaceAll("\\^", " \\^ ");
        lineOfCode = lineOfCode.replaceAll("\\|", " \\| ");
        lineOfCode = lineOfCode.replaceAll("&", " & ");
        lineOfCode = lineOfCode.replaceAll("%", " % ");
        lineOfCode = lineOfCode.replaceAll("\\*", " \\* ");
        lineOfCode = lineOfCode.replaceAll("\\+", " \\+ ");
        lineOfCode = lineOfCode.replaceAll("-", " - ");
        lineOfCode = lineOfCode.replaceAll("\"", " \\\" ");
        lineOfCode = lineOfCode.replaceAll("'", " ' ");
        lineOfCode = lineOfCode.replaceAll("!", " ! ");
        lineOfCode = lineOfCode.replaceAll(",", " , ");

        //trim and replace all multiple spaces to only one space
        return lineOfCode.trim().replaceAll("\\s+", " ");
    }

    private boolean isMethodCall() {
        String[] splitter;
        if(wordSplitter[0].contains(".")) //wordsplitter[0] -> object.method
            splitter = wordSplitter[0].split("\\."); //splitter[0] -> object name, splitter[1] -> method name
        else
            return false;
        if(splitter.length<2)
            return false;

        for(MyVariable myVariable: compilationClass.getVariables()) {
            if (splitter[0].equals(myVariable.getName())) {
                for(MyClass myClass: classes) {
                    if(myVariable.getType().equals(myClass.getName())) {
                        for (MyMethod myMethod : myClass.getMethods()) {
                            if (myMethod.getName().equals(splitter[1])) { //todo arguments
                                if(isNextWordStrictlyEqualTo("(")) {
                                    if(isNextWordStrictlyEqualTo(")")) {
                                        if(isNextWordStrictlyEqualTo(";")) {
                                            if(getNextWordInLine()==null) {
                                                compilationClass.getMethodsCalled().add("Lever.pull");
                                                return true;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return  false;
    }

    private boolean isVariableAssignment() {
        for(MyVariable myVariable: compilationClass.getVariables()) {
            if(myVariable.getName().equals(wordSplitter[0])) {
                if (isNextWordStrictlyEqualTo("=")) {
                    if (myVariable.getType().equals("int"))
                        return assignIntValue(myVariable.getName());
                    else if (myVariable.getType().equals("double"))
                        return assignDoubleValue(myVariable.getName());
                    else if (myVariable.getType().equals("boolean"))
                        return assignBooleanValue(myVariable.getName());
                    else if (myVariable.getType().equals("char"))
                        return assignCharValue(myVariable.getName());
                    else if (myVariable.getType().equals("String"))
                        return assignStringValue(myVariable.getName());
                }
                break;
            }
        }
        return false;
    }

    private boolean isSystemOutPrintln() {
        currentWordPtr=0;
        String nextWord;
        StringBuilder stringToPrint = new StringBuilder();

        if(wordSplitter[0].equals("System.out.println")) { //1st word
            if (isNextWordStrictlyEqualTo("(")) {
                if (isNextWordEqualTo("\"")) {
                    currentWordPtr++;
                    while ((nextWord = getNextWordInLine()) != null && !nextWord.equals("\"")) {
                        stringToPrint.append(nextWord); //todo more than 1 space
                        stringToPrint.append(" ");
                    }
                    if (nextWord == null || !nextWord.equals("\"")) {
                        compilationClass.addErrorInLine(null, lineN);
                        stringToPrint.setLength(0);
                    }

                }
                else {
                    boolean variableExists = false;
                    for(MyVariable var : compilationClass.getVariables()) {
                        if(isNextWordEqualTo(var.getName())) {
                            currentWordPtr++;
                            stringToPrint.append(var.getValue());
                            variableExists = true;
                            break;
                        }
                    }
                    if((nextWord = getNextWordInLine())!=null) {
                        if (!variableExists)
                            compilationClass.addErrorInLine("variable [WHITE]" + nextWord + "[] not declared", lineN);
                    }
                }
                if(stringToPrint.length()>0) {
                    if ((nextWord=getNextWordInLine()) != null && nextWord.equals(")")) {
                        if (isNextWordStrictlyEqualTo(";")) {
                            if(getNextWordInLine()==null) {
                                if(compilationClass.getErrors().size()==0)
                                    consoleTextArea.setText(consoleTextArea.getText() + stringToPrint.toString() + "\n");
                            }
                            else
                                compilationClass.addErrorInLine(null, lineN);
                        }
                        else
                            compilationClass.addErrorInLine(null, lineN);
                    }
                    else
                        compilationClass.addErrorInLine(null, lineN);
                    /*else if(isNextWordEqualTo("+")) { //todo + operator
                        currentWordPtr++;
                        while((nextWord = getNextWordInLine()) != null && !nextWord.equals(")")) {

                        }
                    }*/
                }
            }
            return true;
        }
        return false;
    }

    private boolean isMethodDeclaration(String firstWord) { //todo 'or field'
        String returnType = null;
        currentWordPtr=0;
        String modifier = "";
        if (firstWord.equals("public")) { //1st word
            modifier = firstWord;
            if (isNextWordEqualTo("static")) { //2nd word
                currentWordPtr++;
                if (isNextWordStrictlyEqualTo("void")) { //3rd word
                    if (isNextWordStrictlyEqualTo("main")) { //4th word
                        if (isNextWordStrictlyEqualTo("(")) { //5th word
                            if (isNextWordStrictlyEqualTo("String")) { //6th word
                                if (isNextWordStrictlyEqualTo("[")) { //7th word
                                    if (isNextWordStrictlyEqualTo("]")) { //8th word
                                        if (isNextWordStrictlyEqualTo("args")) { //9th word
                                            if (isNextWordStrictlyEqualTo(")")) { //10th word
                                                if (isNextWordStrictlyEqualTo("{")) { //11th word
                                                    if(getNextWordInLine()==null) {
                                                        compilationClass.addMethod(new MyMethod("public", "void", "main", new ArrayList<MyVariable>(), ""));
                                                        compilationClass.removeError("Error: main method not found");
                                                        return true;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        else if(firstWord.equals("private")) {
            modifier = firstWord;
        }
        else if(validVarReturnType(firstWord)) {
            modifier = "package";
            returnType = firstWord;
        }

        if(!modifier.equals("")) {
            if (returnType != null || ((returnType = getNextWordInLine()) != null && validVarReturnType(returnType))) {
                String methodName;
                if ((methodName = getNextWordInLine()) != null && validVarName(methodName)) {
                    if (isNextWordEqualTo("(")) {
                        currentWordPtr++;
                        ArrayList<MyVariable> arguments = new ArrayList<MyVariable>();
                        String varType;
                        String varName;
                        int commaCounter = 0;
                        do {
                            if(commaCounter>0)
                                currentWordPtr++;
                            if ((varType = getNextWordInLine()) != null && validVarType(varType)) {
                                if ((varName = getNextWordInLine()) != null && validVarName(varName)) {
                                    arguments.add(new MyVariable(varType, varName, "null"));
                                    commaCounter++;
                                }
                                else
                                    return false;
                            }
                            else {
                                if(commaCounter>0)
                                    return false;
                                break;
                            }
                        } while (isNextWordEqualTo(","));

                        if (varType != null && varType.equals(")") || isNextWordStrictlyEqualTo(")")) {
                            if (isNextWordStrictlyEqualTo("{")) {
                                if(getNextWordInLine()==null) {
                                    //System.out.println("KOBLE METHOD: " + modifier + " " + returnType + " " + methodName + " (" + arguments + ") {");
                                    compilationClass.addMethod(new MyMethod(modifier, returnType, methodName, arguments, ""));

                                    int bracketCounter = 1;
                                    String nextWord;
                                    StringBuilder code = new StringBuilder();
                                    int tempLine = lineN;
                                    for (int i = lineN; i < lineSplitter.length; i++) {
                                        lineN++;
                                        currentWordPtr = -1;
                                        if (!lineSplitter[i].isEmpty()) {
                                            splitLineToWords(lineSplitter[i]);
                                            while (bracketCounter != 0 && !isNextWordEqualTo("}")) {

                                                if ((nextWord = getNextWordInLine()) == null)
                                                    break;
                                                if (nextWord.equals("}"))
                                                    bracketCounter--;
                                                else if (nextWord.equals("{"))
                                                    bracketCounter++;
                                                else {
                                                    code.append(nextWord);
                                                    code.append(" ");
                                                }
                                            }
                                            code.append("\n");
                                        }
                                    }
                                    compilationClass.getMethods().get(compilationClass.getMethods().size() - 1).setCode(code.toString());
                                    lineN = tempLine;

                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }

        return false;
    }

    private boolean isVariableDeclaration() {
        currentWordPtr=0;
        String varName;
        boolean flag = false;

        if(validVarType(wordSplitter[0])) { //1st word VARIABLE DECLARATION
            if ((varName = getNextWordInLine()) != null && validVarName(varName)) {
                if(!checkIfVarAlreadyExists(varName)) {
                    if (isNextWordEqualTo(";")) {
                        currentWordPtr++;
                        if (getNextWordInLine() == null) {
                            compilationClass.getVariables().add(new MyVariable(wordSplitter[0], varName, "null"));
                            if (wordSplitter[0].equals("int") || wordSplitter[0].equals("double"))
                                compilationClass.getVariables().get(compilationClass.getVariables().size() - 1).setValue("0");
                            else if (wordSplitter[0].equals("boolean"))
                                compilationClass.getVariables().get(compilationClass.getVariables().size() - 1).setValue("true");
                            flag = true;
                        }
                    } else if (isNextWordStrictlyEqualTo("=")) { // ASSIGN VALUE
                        compilationClass.getVariables().add(new MyVariable(wordSplitter[0], varName, "null"));
                        if ((wordSplitter[0].equals("int"))) { //int
                            return assignIntValue(varName);
                        } else if ((wordSplitter[0].equals("double"))) { //double
                            return assignDoubleValue(varName);
                        } else if ((wordSplitter[0].equals("boolean"))) { //boolean
                            return assignBooleanValue(varName);
                        } else if ((wordSplitter[0].equals("char"))) { //char
                            return assignCharValue(varName);
                        } else if ((wordSplitter[0].equals("String"))) { //String
                            return assignStringValue(varName);
                        } else {
                            for (MyClass myClass : classes) {
                                if (wordSplitter[0].equals(myClass.getName())) {
                                    return assignClassValue(getNextWordInLine(), varName);
                                }
                            }
                        }
                    }
                }
            }
        }
        return flag;
    }

    private boolean checkIfVarAlreadyExists(String varName) {
        for(MyVariable var : compilationClass.getVariables()) { //check if variable already exists
            if(var.getName().equals(varName)) {
                compilationClass.addErrorInLine("variable [WHITE]"+varName+"[] already exists", lineN);
                return true;
            }
        }
        return  false;
    }

    private boolean assignIntValue(String varName) {
        String temp;
        if ((temp = getNextWordInLine()) != null) {
            if (temp.equals("-")) {
                String temp2;
                if ((temp2 = getNextWordInLine()) != null) {
                    temp += temp2;
                }
            }
            if (isNextWordStrictlyEqualTo(";")) {
                if(getNextWordInLine()==null) {
                    if (temp.matches("^-?\\d+$")) { //is integer
                        for (MyVariable myVariable : compilationClass.getVariables()) {
                            if (myVariable.getName().equals(varName))
                                myVariable.setValue(temp);
                        }
                        //System.out.println("int: " + temp);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean assignDoubleValue(String varName) {
        String temp;
        if ((temp = getNextWordInLine()) != null) {
            if (temp.equals("-")) {
                String temp2;
                if ((temp2 = getNextWordInLine()) != null) {
                    temp += temp2;
                }
            }
            if(temp.charAt(temp.length()-1) == 'f' || temp.charAt(temp.length()-1) == 'd')
                temp = temp.substring(0, temp.length() - 1);
            if (isNextWordStrictlyEqualTo(";")) {
                if(getNextWordInLine()==null) {
                    if (temp.matches("^-?\\d+(.\\d+)?$")) {
                        for (MyVariable myVariable : compilationClass.getVariables()) {
                            if (myVariable.getName().equals(varName))
                                myVariable.setValue(temp);
                        }
                        //System.out.println("double " + temp);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean assignBooleanValue(String varName) {
        String temp;
        if ((temp = getNextWordInLine()) != null && !temp.equals(";")) {
            if (temp.equals("true") || temp.equals("false")) {
                if (isNextWordStrictlyEqualTo(";")) {
                    if(getNextWordInLine()==null) {
                        for (MyVariable myVariable : compilationClass.getVariables()) {
                            if (myVariable.getName().equals(varName))
                                myVariable.setValue(temp);
                        }
                        //System.out.println("boolean: " + temp);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean assignCharValue(String varName) {
        String temp;
        if (isNextWordEqualTo("'")) {
            currentWordPtr++;
            if ((temp = getNextWordInLine()) != null) {
                if (temp.length() == 1) {
                    if (isNextWordStrictlyEqualTo("'")) {
                        if (isNextWordStrictlyEqualTo(";")) {
                            if(getNextWordInLine()==null) {
                                for (MyVariable myVariable : compilationClass.getVariables()) {
                                    if (myVariable.getName().equals(varName))
                                        myVariable.setValue(temp);
                                }
                                //System.out.println("char: " + temp);
                                return true;
                            }
                        }
                    }
                } else
                    compilationClass.addErrorInLine(null, lineN);
            }
        }
        return false;
    }

    private boolean assignStringValue(String varName) {
        String temp;
        StringBuilder value = new StringBuilder();
        String finalValue;
        if (isNextWordEqualTo("\"")) {
            currentWordPtr++;
            while ((temp = getNextWordInLine()) != null && !temp.equals(";") && !temp.equals("\"")) {
                value.append(temp);
                value.append(" "); //todo more than 1 space problem
            }
            finalValue = value.toString();
            if(value.length()>0)
                finalValue = finalValue.substring(0, value.length() - 1);

            if (temp != null && temp.equals("\"")) {
                if (isNextWordStrictlyEqualTo(";")) {
                    if(getNextWordInLine()==null) {
                        for (MyVariable myVariable : compilationClass.getVariables()) {
                            if (myVariable.getName().equals(varName))
                                myVariable.setValue(finalValue);
                        }
                        //System.out.println("String: " + finalValue);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean assignClassValue(String firstWord, String varName) {
        if(firstWord!=null && firstWord.equals("new")) {
            String nextWord;
            if((nextWord = getNextWordInLine()) != null) {
                for (MyClass myClass : classes) {
                    if (nextWord.equals(myClass.getName())) {
                        if(isNextWordStrictlyEqualTo("(")) { //todo constructor arguments
                            if(isNextWordStrictlyEqualTo(")")) {
                                if(isNextWordStrictlyEqualTo(";")) {
                                    if(getNextWordInLine()==null) {
                                        //System.out.println("added class type var");
                                        compilationClass.getVariables().add(new MyVariable(nextWord, varName, ""));
                                        return true;
                                    }
                                }
                            }
                        }
                        break;
                    }
                }
            }
        }
        return false;
    }

    private String getNextWordInLine() {
        currentWordPtr++;
        if(wordSplitter.length > currentWordPtr)
            return wordSplitter[currentWordPtr];
        else {
            //currentClass.addErrorInLine(lineN);
            return null;
        }
    }

    private boolean isNextWordEqualTo(String s) {
        if(wordSplitter.length > currentWordPtr + 1) {
            if (wordSplitter[currentWordPtr+1].equals(s))
                return true;
        }
        return false;
    }

    private boolean isNextWordStrictlyEqualTo(String s) {
        currentWordPtr++;
        if(wordSplitter.length > currentWordPtr) {
            if (wordSplitter[currentWordPtr].equals(s))
                return true;
        }
        compilationClass.addErrorInLine(null, lineN);
        return false;
    }

    private boolean validVarName(String name) {
        if(name.matches("^[a-zA-Z_][a-zA-Z_0-9]*$"))
            return true;
        else {
            compilationClass.addErrorInLine(null, lineN);
            return false;
        }
    }

    private boolean validVarType(String varType) {
        for(MyClass myClass: classes) {
            if(myClass.getName().equals(varType))
                return true;
        }
        return  (varType.equals("int")
                //|| varType.equals("float")
                || varType.equals("double")
                || varType.equals("boolean")
                || varType.equals("char")
                || varType.equals("String"));
    }

    private boolean validVarReturnType(String varType) {
        return (validVarType(varType) || varType.equals("void"));
    }

}
