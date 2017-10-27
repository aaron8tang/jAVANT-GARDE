package com.steveflames.javalab.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.StringBuilder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

/**
 * Created by Flames on 18/10/2017.
 */

public class MyCompiler {

    private TextArea codeTextArea;
    public static TextArea consoleTextArea;
    private Table classTable;

    private FileHandle classFile;


    public MyCompiler(TextArea codeTextArea, TextArea consoleTextArea, Table classTable) {
        this.codeTextArea = codeTextArea;
        MyCompiler.consoleTextArea = consoleTextArea;
        this.classTable = classTable;
    }


    /**
     * lexical analysis
     * syntax analysis
     * semantic analysis
     *
     * SEMANTIC ERRORS
     * Type mismatch
     * Undeclared variable
     * Reserved identifier misuse.
     * Multiple declaration of variable in a scope.
     * Accessing an out of scope variable.
     * Actual and formal parameter mismatch.
     */
    public boolean compile(LinkedHashMap<String, String> program) {

        HashMap<String, Integer> errors = new HashMap<String, Integer>();
        errors.put("Error: class not defined", 1);
        errors.put("Error: main method not found", 1);
        String[] lineSplitter;
        String[] wordSplitter;
        String className;
        String classNameWithExtension;
        String code;
        boolean flag = true;
        boolean classDeclaration = false;
        boolean mainDeclaration = false;
        consoleTextArea.setText("");
        int bracketsCounter = 0;

        for(Map.Entry<String, String> entry: program.entrySet()) { //parse classes and their codes
            classNameWithExtension = entry.getKey();
            code = entry.getValue();
            className = classNameWithExtension.substring(0, classNameWithExtension.length() - 5); //get the name of the class
            lineSplitter = code.split("\n"); //get the lines of code

            for(String lineOfCode: lineSplitter) { //parse the lines of code
                lineOfCode = lineOfCode.replaceAll("\\{", " \\{ ");
                lineOfCode = lineOfCode.replaceAll("\\}", " \\} ");
                lineOfCode = lineOfCode.replaceAll("\\(", " \\( ");
                lineOfCode = lineOfCode.replaceAll("\\)", " \\) ");
                lineOfCode = lineOfCode.replaceAll("\\{", " \\{ ");
                lineOfCode = lineOfCode.replaceAll("\\[", " \\[ ");
                lineOfCode = lineOfCode.replaceAll("\\]", " \\] ");

                lineOfCode = lineOfCode.trim().replaceAll("\\s+", " ");
                wordSplitter = lineOfCode.split(" ");

                /*
                for(String word : wordSplitter) { //parse each word of the line

                }
                */

                if(lineOfCode.equals("class "+className+" {")
                 || lineOfCode.equals("public class "+className+" {")) { //check if class definition is written
                    errors.remove("Error: class not defined");
                }
                else if(lineOfCode.equals("public static void main ( String [ ] args ) {"))
                    errors.remove("Error: main method not found");
                bracketsCounter += lineOfCode.length() - lineOfCode.replace("{", "").length();
                bracketsCounter -= lineOfCode.length() - lineOfCode.replace("}", "").length();
            }


            for(Map.Entry<String, Integer> errorEntry: errors.entrySet()) {
                if(errorEntry.getValue()==1) {
                    consoleTextArea.appendText(errorEntry.getKey());
                    if(!errorEntry.getKey().startsWith("Error: main method"))
                        consoleTextArea.appendText(" in file "+ classNameWithExtension +"\n");
                    else
                        consoleTextArea.appendText("\n");
                    flag = false;
                }
            }
            if(bracketsCounter != 0)
                consoleTextArea.appendText("Error: uneven brackets in file " + classNameWithExtension+"\nRemember to close any open curly brackets '{' with '}'");

        }

        if(flag)
            consoleTextArea.appendText("\n\n(Compilation finished with no errors)");
        return flag;
    }

    public boolean varType(String lineOfCode) {
        return lineOfCode.trim().equals("int")
                || lineOfCode.trim().equals("float")
                || lineOfCode.trim().equals("double")
                || lineOfCode.trim().equals("boolean")
                || lineOfCode.trim().equals("char")
                || lineOfCode.trim().equals("String");
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
                                    "import com.steveflames.javalab.tools.MyCompiler;\n" +
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
