package com.steveflames.javalab.tools.compiler;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;

import java.util.ArrayList;

/**
 * Created by Flames on 18/10/2017.
 */

public class MyCompiler {

    public static Label consoleTextArea;
    //private HashMap<String, Integer> errors = new HashMap<String, Integer>();
    private ArrayList<MyClass> classes;
    private MyClass currentClass;

    private String[] wordSplitter;
    private int currentWordPtr = 0;
    private int lineN = 0;

    private FileHandle classFile;


    public MyCompiler(Label consoleTextArea) {
        MyCompiler.consoleTextArea = consoleTextArea;
    }


    /**
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
     * Type mismatch
     * Undeclared variable
     * Reserved identifier misuse.
     * Multiple declaration of variable in a scope.
     * Accessing an out of scope variable.
     * Actual and formal parameter mismatch.
     */
    public boolean compile(ArrayList<MyClass> myClasses) {
        classes = myClasses;
        //errors.clear();
        //errors.put("Error: class not defined", 1);
        //errors.put("Error: main method not found", 1);
        String[] lineSplitter;
        consoleTextArea.setText("");
        int bracketsCounter = 0;

        for(MyClass myClass: classes) { //parse classes and their codes
            currentClass = myClass;
            currentClass.addError("Error: class not defined in file " + currentClass.getName() + ".java");
            currentClass.addError("Error: main method not found");
            //boolean inFieldDeclaration = false;


            //parseClass(); //todo


            lineSplitter = currentClass.getCode().split("\n"); //get the lines of code

            lineN = 0;
            for(String lineOfCode: lineSplitter) { //parse the lines of code
                lineN++;

                //prepare lineOfCode
                lineOfCode = prepareLineOfCode(lineOfCode);

                //split line and get its words
                if(!lineOfCode.isEmpty()) {
                    if (lineOfCode.contains(" ")) {
                        wordSplitter = lineOfCode.split(" ");
                    }
                    else { //if only 1 word, assign it to wordSplitter
                        wordSplitter = new String[1];
                        wordSplitter[0] = lineOfCode;
                    }

                    currentWordPtr = 0;
                    String nextWord;
                    //parse each word of the line
                    if (wordSplitter.length > 0) {
                        if (!currentClass.isClassDeclared()) { //class not yet declared
                            if(wordSplitter[0].equals("class") || wordSplitter[0].equals("public") && isNextWordStrictlyEqualTo("class")) {
                                if ((nextWord = getNextWordInLine()) != null && validVarName(nextWord)) { //2nd word
                                    if (isNextWordStrictlyEqualTo("{")) {
                                        currentClass.removeError("Error: class not defined in file " + currentClass.getName() + ".java");
                                        currentClass.setClassDeclared(true);
                                    }
                                }
                            }
                            else
                                currentClass.addErrorInLine(lineN);
                        }
                        else { //class is declared previously
                            if (isMethodDeclaration(wordSplitter[0])) { //check if main method declaration

                            }

                            else if (isSystemOutPrintln()) {
                            }

                            else if (variableDeclaration()) {
                                //DO NOTHING
                            }

                            else if (wordSplitter[0].equals("}") && wordSplitter.length == 1) { //1st word: }
                                //DO NOTHING
                            }

                            else {
                                currentClass.addErrorInLine(lineN);
                            }
                        }
                    }
                }


                //check if '{' are even with '}'
                bracketsCounter += lineOfCode.length() - lineOfCode.replace("{", "").length();
                bracketsCounter -= lineOfCode.length() - lineOfCode.replace("}", "").length();
            }

            if(bracketsCounter != 0)
                currentClass.addError("Error: uneven brackets in file " + currentClass.getName() +".java\n[YELLOW]Remember to close any open curly brackets '{' with '}'[]");
        }
        //append errors in console window
        boolean flag = true;
        for(MyClass myClass: myClasses) {
            for(String error: myClass.getErrors()) {
                consoleTextArea.setText(consoleTextArea.getText() + error+"\n");
                flag = false;
            }
        }
        if(flag)
            consoleTextArea.setText(consoleTextArea.getText() + "\n\n[GREEN](Compilation finished with no errors)[]");
        return flag;
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
                    if (nextWord != null && nextWord.equals("\"")) {
                        if (isNextWordStrictlyEqualTo(")")) {
                            if (isNextWordStrictlyEqualTo(";")) {
                                consoleTextArea.setText(consoleTextArea.getText() + stringToPrint.toString());
                            }
                        }
                    } else
                        currentClass.addErrorInLine(lineN);
                }
            }
            return true;
        }
        return false;
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
                                                    currentClass.addMethod(new MyMethod("public", "void", "main", new ArrayList<MyVariable>(), ""));
                                                    currentClass.removeError("Error: main method not found");
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
                                System.out.println("KOBLE METHOD: " + modifier + " " + returnType + " " + methodName + " (" + arguments + ") {");
                                currentClass.addMethod(new MyMethod(modifier, returnType, methodName, arguments, ""));
                                return true;
                            }
                        }
                    }
                    else {
                        return false;
                    }
                }
            }
        }

        return false;
    }

    private boolean variableDeclaration() {
        currentWordPtr=0;
        String varName;
        boolean flag = false;

        if(validVarType(wordSplitter[0])) { //1st word VARIABLE DECLARATION
            if ((varName = getNextWordInLine()) != null && validVarName(varName)) {
                if (isNextWordEqualTo(";")) {
                    currentClass.getFields().add(new MyVariable(wordSplitter[0], varName, "null"));
                    flag = true;
                }
                else if (isNextWordStrictlyEqualTo("=")) { // ASSIGN VALUE
                    String temp;

                    if ((wordSplitter[0].equals("int"))) { //int
                        if ((temp = getNextWordInLine()) != null) {
                            if(temp.equals("-")) {
                                String temp2;
                                if((temp2 = getNextWordInLine()) != null) {
                                    temp += temp2;
                                }
                            }
                            if (isNextWordStrictlyEqualTo(";")) {
                                System.out.println(temp);
                                if(temp.matches("^-?\\d+$")) {
                                    currentClass.getFields().add(new MyVariable("int", varName, temp));
                                    System.out.println("int: " + temp);
                                    return true;
                                }
                            }
                        }
                    } else if ((wordSplitter[0].equals("double"))) { //double
                        if ((temp = getNextWordInLine()) != null) {
                            if(temp.equals("-")) {
                                String temp2;
                                if((temp2 = getNextWordInLine()) != null) {
                                    temp += temp2;
                                }
                            }
                            if (isNextWordStrictlyEqualTo(";")) {
                                System.out.println(temp);
                                if(temp.matches("^-?\\d+(.\\d+)?$")) { //todo f h d sto telos
                                    currentClass.getFields().add(new MyVariable("double", varName, temp));
                                    System.out.println("double " + temp);
                                    return true;
                                }
                            }
                        }

                    } else if ((wordSplitter[0].equals("boolean"))) { //boolean
                        if ((temp = getNextWordInLine()) != null && !temp.equals(";")) {
                            if (temp.equals("true") || temp.equals("false")) {
                                if(isNextWordStrictlyEqualTo(";")) {
                                    currentClass.getFields().add(new MyVariable("boolean", varName, temp));
                                    System.out.println("boolean: " + temp);
                                    return true;
                                }
                            }
                        }
                    } else if ((wordSplitter[0].equals("char"))) { //char
                        if (isNextWordEqualTo("'")) {
                            currentWordPtr++;
                            if ((temp = getNextWordInLine()) != null) {
                                if (temp.length() == 1) {
                                    if (isNextWordStrictlyEqualTo("'")) {
                                        if (isNextWordStrictlyEqualTo(";")) {
                                            currentClass.getFields().add(new MyVariable("char", varName, temp));
                                            System.out.println("char: " + temp);
                                            return true;
                                        }
                                    }
                                } else
                                    currentClass.addErrorInLine(lineN);
                            }
                        }
                    } else if ((wordSplitter[0].equals("String"))) { //String
                        StringBuilder value = new StringBuilder();
                        if (isNextWordEqualTo("\"")) {
                            currentWordPtr++;
                            while ((temp = getNextWordInLine()) != null && !temp.equals(";") && !temp.equals("\"")) {
                                value.append(temp);
                                value.append(" "); //todo more than 1 space rip
                            }
                            if (temp != null && temp.equals("\"")) {
                                if (isNextWordStrictlyEqualTo(";")) {
                                    currentClass.getFields().add(new MyVariable("String", varName, value.toString()));
                                    System.out.println("String: " + value);
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return flag;
    }

    private String getNextWordInLine() {
        currentWordPtr++;
        if(wordSplitter.length > currentWordPtr)
            return wordSplitter[currentWordPtr];
        else {
            currentClass.addErrorInLine(lineN);
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
        currentClass.addErrorInLine(lineN);
        return false;
    }

    private boolean validVarName(String name) {
        if(name.matches("^[a-zA-Z_][a-zA-Z_0-9]*$"))
            return true;
        else {
            currentClass.addErrorInLine(lineN);
            return false;
        }
    }

    private boolean validVarType(String varType) {
        return varType != null && (varType.equals("int")
                //|| varType.equals("float")
                || varType.equals("double")
                || varType.equals("boolean")
                || varType.equals("char")
                || varType.equals("String"));
    }

    private boolean validVarReturnType(String varType) {
        return (validVarType(varType) || varType.equals("void"));
    }

    private boolean containsAtLeastOneLetter(String text) {
        for(int i=0; i<text.length(); i++) {
            if(Character.isLetter(text.charAt(i))){
                return true;
            }
        }
        return false;
    }

    /*
    *//**
     * Compile and run the user's class name and the main method.
     * If correct, then write a new class file implementing the iUserCode interface. but with the code
     * that the user wrote, so that the code can be called from within this program.
     * @param className
     * @param code
     * @return
     *//*
    public boolean firstCompile(String className, String code) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();

        JavaFileObject file = new JavaSourceFromString(className, code);

        Iterable<? extends JavaFileObject> compilationUnits = Arrays.asList(file);
        JavaCompiler.CompilationTask task = compiler.getTask(null, null, diagnostics, null, null, compilationUnits);


        if (task.call()) {
            // Create a new custom class loader, pointing to the directory that contains the compiled
            // classes, this should point to the top of the package structure!
            URLClassLoader classLoader = null;
            try {
                classLoader = new URLClassLoader(new URL[]{new File("").toURI().toURL()});
                // Load the class from the classloader by name....
                Class<?> loadedClass = classLoader.loadClass(className);
                // Create a new instance...
                loadedClass.newInstance();
                consoleTextArea.setText("");


                if(containsAtLeastOneLetter(code)) {
                    if (codeTextArea.getText().replaceAll("\\s+", "").contains("publicstaticvoidmain(String[]args){")) { //todo edw
                        String[] splitter = codeTextArea.getText().split("\\{");

                        if (splitter.length >= 2) {
                            splitter = splitter[2].split("}");
                            String userCodeInMain = "";
                            for (int i = 0; i < splitter.length - 1; i++) { //ignore the last 2 curly brackets of the user's code
                                userCodeInMain = splitter[i].trim();
                                //System.out.println("i"+ i+": "+ splitter[i]);
                            }

                            ArrayList<String> userCodeInMainWithSystemOut = new ArrayList<String>();
                            splitter = userCodeInMain.split(";"); //get the lines of code
                            String[] printlnSplitter; //is used to get the text between the '(' and ')' of the println("");
                            String finalPrintlnString = ""; //the final text
                            for (String lineOfCode : splitter) {
                                userCodeInMainWithSystemOut.add(lineOfCode+";");
                                if (lineOfCode.contains("System.out.println")) {
                                    printlnSplitter = lineOfCode.split("\\("); //get the text after '(' in println
                                    printlnSplitter = printlnSplitter[1].split("\\)"); //split the text at ')'

                                    for (int j = 0; j < printlnSplitter.length; j++) { //get the text and ignore the last parenthesis of the println("")
                                        finalPrintlnString += printlnSplitter[j].trim();
                                        if (j < printlnSplitter.length - 1)
                                            finalPrintlnString += ")";
                                    }

                                    userCodeInMainWithSystemOut.add("\nMyCompiler.consoleTextArea.appendText(" + finalPrintlnString + "+\"\");\n");
                                }
                            }

                            String finalcode = "package code;\n" +
                                    "import com.steveflames.javalab.ingame_classes.iUserCode;\n" +
                                    "import com.steveflames.javalab.tools.compiler.MyCompiler;\n" +
                                    "\n" +
                                    "public class UserCode implements iUserCode{\n" +
                                    "    public void doStuff() {\n";
                            for (String s : userCodeInMainWithSystemOut)
                                finalcode += s;
                            finalcode +=
                                    "    }\n" +
                                            "}";
                            //System.out.println("\n"+finalcode);

                            classFile = Gdx.files.local("code/UserCode.java");
                            classFile.writeString(finalcode, false);
                            return true;
                        } else {
                            consoleTextArea.setText("Error: ");
                            return false;
                        }
                    } else {
                        consoleTextArea.setText("Error: main method not found");
                        return false;
                    }
                }
                else {
                    consoleTextArea.setText("Error: "+className+" is empty");
                    return false;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
                consoleTextArea.setText(e.toString());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                if(e.toString().contains("with modifiers \"\""))
                    consoleTextArea.setText("Cannot access a member of class " + className +" with modifiers \"\"");
                else
                    consoleTextArea.setText(e.toString());
            } catch (InstantiationException e) {
                e.printStackTrace();
                consoleTextArea.setText(e.toString());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                consoleTextArea.setText(e.toString());
            }
        }
        else {
            consoleTextArea.setText("");
            for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
                System.out.format("Error on line %d in %s%n", diagnostic.getLineNumber(), diagnostic.getSource().toUri());
                consoleTextArea.appendText("Error on line " + diagnostic.getLineNumber() + " in: " + (diagnostic.getSource().toUri()+"").substring(10)+"\n");
            }
        }
        return false;
    }


    public Object compile(String className, String code) {
        if(firstCompile("MyClass", code)) {
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
            StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);

            Iterable<? extends JavaFileObject> compilationUnit = fileManager.getJavaFileObjectsFromFiles(Arrays.asList(classFile.file()));
            JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, null, null, compilationUnit);

            return createObject(className, task, diagnostics);
        }
        else
            return null;
    }

    public Object createObject(String className, JavaCompiler.CompilationTask task, DiagnosticCollector<JavaFileObject> diagnostics) {
        if (task.call()) {
            // Create a new custom class loader, pointing to the directory that contains the compiled
            // classes, this should point to the top of the package structure!
            URLClassLoader classLoader = null;
            try {
                classLoader = new URLClassLoader(new URL[]{new File("./").toURI().toURL()});
                // Load the class from the classloader by name....
                Class<?> loadedClass = classLoader.loadClass("code."+className);
                // Create a new instance...
                Object obj = loadedClass.newInstance();

                return obj;

            } catch (MalformedURLException e) {
                e.printStackTrace();
                consoleTextArea.setText(e.toString());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                if(e.toString().contains("with modifiers \"\""))
                    consoleTextArea.setText("Cannot access a member of class " + className +" with modifiers \"\"");
                else
                    consoleTextArea.setText(e.toString());
            } catch (InstantiationException e) {
                e.printStackTrace();
                consoleTextArea.setText(e.toString());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                consoleTextArea.setText(e.toString());
            }
        }
        else {
            consoleTextArea.setText("");
            for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
                System.out.format("Error on line %d in %s%n", diagnostic.getLineNumber(), diagnostic.getSource().toUri());
                consoleTextArea.appendText("Error on line " + diagnostic.getLineNumber() + " in: " + (diagnostic.getSource().toUri()+"").substring(10)+"\n");
            }
        }
        return null;
    }

    *//**
     * A file object used to represent source coming from a string.
     *//*
    public class JavaSourceFromString extends SimpleJavaFileObject {
        *//**
         * The source code of this "file".
         *//*
        final String code;

        *//**
         * Constructs a new JavaSourceFromString.
         * @param name the name of the compilation unit represented by this file object
         * @param code the source code for the compilation unit represented by this file object
         *//*
        JavaSourceFromString(String name, String code) {
            super(URI.create("string:///" + name.replace('.','/') + Kind.SOURCE.extension), Kind.SOURCE);
            this.code = code;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return code;
        }
    }
    */
}
