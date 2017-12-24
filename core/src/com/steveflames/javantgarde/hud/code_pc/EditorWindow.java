package com.steveflames.javantgarde.hud.code_pc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.steveflames.javantgarde.MyGdxGame;
import com.steveflames.javantgarde.hud.AndroidExtraKeyboardWindow;
import com.steveflames.javantgarde.hud.Hud;
import com.steveflames.javantgarde.screens.PlayScreen;
import com.steveflames.javantgarde.sprites.FloatingPlatform;
import com.steveflames.javantgarde.sprites.InfoSign;
import com.steveflames.javantgarde.sprites.Lever;
import com.steveflames.javantgarde.sprites.Pc;
import com.steveflames.javantgarde.sprites.Player;
import com.steveflames.javantgarde.hud.code_pc.compiler.MyClass;
import com.steveflames.javantgarde.hud.code_pc.compiler.MyCompiler;
import com.steveflames.javantgarde.hud.code_pc.compiler.MyVariable;
import com.steveflames.javantgarde.tools.global.Cameras;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Flames on 10/11/2017.
 */

public class EditorWindow extends Window {

    private Pc currentPc;
    private MyCompiler compiler;
    private Hud hud;
    private Table classesTable;
    private TextArea codeTextArea;

    private ArrayList<MyClass> myClasses = new ArrayList<MyClass>();
    private int myClassCursorPosition =-1;

    private boolean tempHide = false;
    private float tempHideTimer = 0;
    private boolean showEditorAgain = true;

    private ConsoleWindow consoleWindow;
    private QuestWindow questWindow;
    private AndroidExtraKeyboardWindow androidExtraKeyboardWindow;

    private float tempCamX = - 1;


    public EditorWindow(String title, Skin neonSkin, Skin lmlSkin, Skin terraSkin, final Hud hud) {
        super(title, terraSkin);
        this.hud = hud;

        //top bar
        Table topBarTable = new Table(neonSkin);
        TextButton exitBtn = new TextButton("x", neonSkin);
        Table dummyTable = new Table(terraSkin);
        classesTable = new Table(terraSkin);
        TextButton classBtn = new TextButton("MyClass.java", neonSkin);
        classesTable.add(classBtn).left().height(50).padLeft(0);
        dummyTable.add(classesTable).left().expandX();
        ScrollPane classesScroll = new ScrollPane(dummyTable, neonSkin);
        if(MyGdxGame.platformDepended.deviceHasKeyboard())
            classesScroll.setFlickScroll(false);

        topBarTable.add(classesScroll).left().height(60).expand().fill().padLeft(55);

        topBarTable.add(exitBtn).right().fill();

        //textArea
        codeTextArea = new TextArea("", neonSkin);
        codeTextArea.setFocusTraversal(false);
        codeTextArea.getStyle().fontColor = Color.WHITE;
        codeTextArea.getStyle().disabledFontColor = Color.LIGHT_GRAY;

        //lineNumTable
        Table lineNumTable = new Table(neonSkin);
        Label label = new Label(1 + "", neonSkin);
        label.setColor(Color.CYAN);
        lineNumTable.add(label).width(60).height(codeTextArea.getStyle().font.getLineHeight());
        for (int i = 1; i < 150; i++) {
            lineNumTable.row();
            label = new Label(i + 1 + "", neonSkin);
            label.setColor(Color.CYAN);
            lineNumTable.add(label).width(60).height(codeTextArea.getStyle().font.getLineHeight());
        }

        //codeTable
        Table codeTable = new Table(lmlSkin);
        codeTable.add(lineNumTable).top().left();
        codeTable.add(codeTextArea).top().expand().fill().width(1000).padTop(5); //todo otan pataw 1h grammh enter k meta click indexoutOfBouds
        ScrollPane codeScroll = new ScrollPane(codeTable, neonSkin);       //todo kalutero textArea.. na valw height k scroll mono sto textArea
        if(MyGdxGame.platformDepended.deviceHasKeyboard())
            codeScroll.setFlickScroll(false);

        //bottom bar
        TextButton compileAndRunBtn = new TextButton(" compile & run ", neonSkin);
        Table bottomBarTable = new Table(neonSkin);
        bottomBarTable.add(compileAndRunBtn).right().expandX().pad(0).height(60);

        //add components to window
        if(MyGdxGame.platformDepended.deviceHasKeyboard())
            this.setSize(700, MyGdxGame.HEIGHT-213);
        else
            this.setSize(700, MyGdxGame.HEIGHT-295);
        this.setX(MyGdxGame.WIDTH - this.getWidth());
        this.setY(195);
        this.add(topBarTable).expandX().fillX().top();
        this.row().padTop(5);
        this.add(codeScroll).expandX().fillX();
        this.row();
        this.add(bottomBarTable).expandX().fillX();

        consoleWindow = new ConsoleWindow("CONSOLE", neonSkin, terraSkin, hud.stage);
        questWindow = new QuestWindow("QUEST", neonSkin, lmlSkin, terraSkin, hud);
        if(!MyGdxGame.platformDepended.deviceHasKeyboard())
            androidExtraKeyboardWindow = new AndroidExtraKeyboardWindow("+KEYBOARD", neonSkin, terraSkin, this);
        compiler = new MyCompiler(consoleWindow.getConsoleTextArea());

        //ADD LISTENERS
        exitBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                closeCurrentEditor();
                if(!MyGdxGame.platformDepended.deviceHasKeyboard())
                    androidExtraKeyboardWindow.setKeyboardShown(false);
            }
        });
        classBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(!codeTextArea.isDisabled())
                    currentPc.setEditorText(codeTextArea.getText());
                if(!currentPc.getEditorText().isEmpty())
                    codeTextArea.setText(currentPc.getEditorText());
                if(myClassCursorPosition>0)
                    codeTextArea.setCursorPosition(myClassCursorPosition);
                codeTextArea.setDisabled(false);
            }
        });
        classesScroll.addListener(new ClickListener() {
            public void enter (InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                hud.stage.setScrollFocus(classesTable);
            }
        });
        codeTextArea.addListener(new InputListener(){
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                editorKeyDown(keycode);
                return false;
            }
            @Override
            public boolean keyTyped(InputEvent event, char character) {
                editorKeyTyped(character);
                return false;
            }
        });
        if(!MyGdxGame.platformDepended.deviceHasKeyboard()) {
            codeTextArea.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    androidExtraKeyboardWindow.setKeyboardShown(true);
                }
            });
        }
        codeScroll.addListener(new ClickListener() {
            public void enter (InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                hud.stage.setScrollFocus(codeTextArea);
            }
        });
        compileAndRunBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(!codeTextArea.isDisabled())
                    currentPc.setEditorText(codeTextArea.getText());
                consoleWindow.getConsoleTextArea().setText("");
                consoleWindow.getConsoleScroll().scrollTo(0, Cameras.hudPort.getCamera().position.y + Cameras.hudPort.getCamera().viewportHeight, 0, 0);

                //store the class names and code
                myClasses.clear();
                myClasses.add(new MyClass("MyClass", currentPc.getEditorText()));
                for(Map.Entry<String, String> entry: hud.playScreen.getPlayer().getClasses().entrySet())
                    myClasses.add(new MyClass(entry.getKey(), entry.getValue()));

                //compile and run
                if(compiler.compile(myClasses) || (hud.playScreen.getCurrentLevelID().equals("1_1") && currentPc.getQuest().getProgress()==0)) {
                    if (validateCodeForQuest(hud.playScreen, myClasses.get(0), currentPc.getQuest().getQuestN())) {
                        questWindow.incrementQuestStep(currentPc.getQuest(), EditorWindow.this);
                    }
                }
            }
        });
    }

    public void update(float dt) {
        if(tempHide) {
            tempHideTimer+=dt;
            if(tempHideTimer > 2.5) {
                tempHide = false;
                tempHideTimer = 0;
                if(showEditorAgain)
                    tempShowEditor();
                else { //dont show editor again
                    showEditorAgain = true;
                    consoleWindow.remove();
                    hud.playScreen.getPlayer().setCurrentState(Player.State.STANDING);
                    hud.showAndroidInputTable();
                    if(currentPc.getQuest().isCompleted()) { //if text completion has a toast, show it
                        if(currentPc.getQuest().getCompletedText()!= null)
                            hud.newToast(currentPc.getQuest().getCompletedText());
                    }
                }
            }
        }
    }

    private void editorKeyDown(int keycode) {
        if(keycode== Input.Keys.BACKSPACE) {
            if(codeTextArea.getSelection().length()==0) {
                int pos = codeTextArea.getCursorPosition();
                int charsToDelete = -1;
                int deleteNextChar = 0;
                StringBuilder stringBuilder = new StringBuilder();

                int i = pos - 1;
                if(i>0) { //if spaces, delete more than just one (max 4 = 1 tab)
                    if (codeTextArea.getText().charAt(i) != ' ') { //if character to delete is not space
                        charsToDelete = 0;
                        if(codeTextArea.getText().length()>i+1) {
                            if (codeTextArea.getText().charAt(i) == '(' && codeTextArea.getText().charAt(i+1) == ')'
                                    || codeTextArea.getText().charAt(i) == '[' && codeTextArea.getText().charAt(i+1) == ']'
                                    || codeTextArea.getText().charAt(i) == '"' && codeTextArea.getText().charAt(i+1) == '"'
                                    || codeTextArea.getText().charAt(i) == '\'' && codeTextArea.getText().charAt(i+1) == '\'')
                                deleteNextChar = 1;
                        }
                    }
                    else {
                        while (i >= 0 && codeTextArea.getText().charAt(i) != '\n') { //delete up to 4 spaces (tab)
                            if (codeTextArea.getText().charAt(i) == ' ')
                                charsToDelete++;
                            else
                                break;
                            if (charsToDelete == 3)
                                break;
                            i--;
                        }
                    }

                    for (int j = 0; j < pos - charsToDelete; j++)
                        stringBuilder.append(codeTextArea.getText().charAt(j));
                    for (int j = pos + deleteNextChar; j < codeTextArea.getText().length(); j++)
                        stringBuilder.append(codeTextArea.getText().charAt(j));

                    codeTextArea.setText(stringBuilder.toString());
                    codeTextArea.setCursorPosition(pos - charsToDelete);
                }
            }
        }
    }

    public void editorKeyTyped(char character) {
        if(character == '\t') {
            int pos = codeTextArea.getCursorPosition();
            StringBuilder stringBuilder = new StringBuilder();

            int i=0;
            while(i<codeTextArea.getCursorPosition()) {
                stringBuilder.append(codeTextArea.getText().charAt(i));
                i++;
            }

            //append the right amount of spaces
            int currentCharPtr = i-1;
            while (currentCharPtr>=0 && codeTextArea.getText().charAt(currentCharPtr) != '\n') {
                currentCharPtr--;
            }
            currentCharPtr++;
            for(int j=0; j<(4-(i-currentCharPtr)%4); j++)
                stringBuilder.append(" ");

            //append the rest of the text
            for(int j=i; j < codeTextArea.getText().length(); j++)
                stringBuilder.append(codeTextArea.getText().charAt(j));
            codeTextArea.setText(stringBuilder.toString());
            codeTextArea.setCursorPosition(pos + (4-(i-currentCharPtr)%4));
        }
        else if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            int bracketCounter = 0;
            int pos = codeTextArea.getCursorPosition();
            StringBuilder stringBuilder = new StringBuilder();

            for(int i=0; i<pos; i++) {
                if(codeTextArea.getText().charAt(i) == '{') //todo if the user types { or } as a String there's a problem
                    bracketCounter++;
                else if(codeTextArea.getText().charAt(i) == '}')
                    bracketCounter--;
                stringBuilder.append(codeTextArea.getText().charAt(i));
            }
            if(bracketCounter<=0)
                return;
            for(int i=0; i<bracketCounter; i++)
                stringBuilder.append("    ");

            int bracketCounter2 = bracketCounter;
            for(int i=pos; i<codeTextArea.getText().length(); i++) {
                if (codeTextArea.getText().charAt(i) == '{') //todo if the user types { or } as a String there's a problem
                    bracketCounter2++;
                else if (codeTextArea.getText().charAt(i) == '}')
                    bracketCounter2--;
            }
            if(bracketCounter2>0) {
                //check if previous character is {, to automatically make new line and close bracket }
                int j = pos - 1;
                boolean flag = false;
                while (j >= 0) {
                    if (codeTextArea.getText().charAt(j) == '{')
                        flag = true;
                    else if (codeTextArea.getText().charAt(j) != ' ' && codeTextArea.getText().charAt(j) != '\n')
                        break;
                    j--;
                }
                if (flag) {
                    stringBuilder.append("\n");
                    for (int i = 0; i < bracketCounter - 1; i++)
                        stringBuilder.append("    ");
                    stringBuilder.append('}');
                }
            }

            //append the rest code
            for(int i=pos; i < codeTextArea.getText().length(); i++)
                stringBuilder.append(codeTextArea.getText().charAt(i));

            codeTextArea.setText(stringBuilder.toString());
            codeTextArea.setCursorPosition(pos + 4*bracketCounter);
            codeTextArea.setPrefRows(codeTextArea.getText().split("\n").length);
            codeTextArea.layout();

        }
        else if(character == '}') {
            int pos = codeTextArea.getCursorPosition();
            int charsToDelete = 0;
            int bracketCounter = 0;
            StringBuilder stringBuilder = new StringBuilder();

            int i = pos-2;
            while(i>=0 && codeTextArea.getText().charAt(i) != '\n') {
                if(codeTextArea.getText().charAt(i) == ' ') //if the whole line is spaces
                    charsToDelete++;
                else
                    return;
                i--;
            }

            for(int j=0; j<pos - charsToDelete; j++) { //count brackets
                if(codeTextArea.getText().charAt(j) == '{') //todo if the user types { or } in a String there's a problem
                    bracketCounter++;
                else if(codeTextArea.getText().charAt(j) == '}')
                    bracketCounter--;

                stringBuilder.append(codeTextArea.getText().charAt(j));
            }

            if(bracketCounter<0)
                return;

            for(int j=0; j<bracketCounter-1; j++)
                stringBuilder.append("    ");
            stringBuilder.setCharAt(stringBuilder.length()-1, '}');
            bracketCounter = stringBuilder.length();

            for(int j=pos; j < codeTextArea.getText().length(); j++)
                stringBuilder.append(codeTextArea.getText().charAt(j));

            codeTextArea.setText(stringBuilder.toString());
            codeTextArea.setCursorPosition(bracketCounter);
        }
        else if(character == '"' || character == '\'') {
            String temp = codeTextArea.getText();
            int x = codeTextArea.getCursorPosition();

            if(x<temp.length()) {
                if(temp.charAt(x) == character) {
                    temp = temp.substring(0, x-1)+ temp.substring(x, temp.length());
                    codeTextArea.setText(temp);
                    codeTextArea.setCursorPosition(x);
                    return;
                }
            }

            temp = temp.substring(0, x) + character + temp.substring(x, temp.length());
            codeTextArea.setText(temp);
            codeTextArea.setCursorPosition(x);
        }
        else if(character == '(' || character == '[') {
            String temp = codeTextArea.getText();
            int x = codeTextArea.getCursorPosition();

            if(character == '(')
                temp = temp.substring(0, x) + ')' + temp.substring(x, temp.length());
            else
                temp = temp.substring(0, x) + ']' + temp.substring(x, temp.length());
            codeTextArea.setText(temp);
            codeTextArea.setCursorPosition(x);
        }
        else if(character == ')' || character == ']') {
            String temp = codeTextArea.getText();
            int x = codeTextArea.getCursorPosition();

            if(x<temp.length()) {
                if(temp.charAt(x) == character) {
                    temp = temp.substring(0, x-1)+ temp.substring(x, temp.length());
                    codeTextArea.setText(temp);
                    codeTextArea.setCursorPosition(x);
                }
            }
        }
    }

    private void tempHideEditor(float camX, boolean hideConsole) {
        tempCamX = Cameras.playScreenCam.position.x;
        if(camX != -1)
            Cameras.playScreenCam.position.x = camX;
        this.remove();
        questWindow.remove();
        if(hideConsole)
            consoleWindow.remove();
        if(!MyGdxGame.platformDepended.deviceHasKeyboard())
            androidExtraKeyboardWindow.remove();
        tempHide = true;
    }

    private void tempShowEditor() {
        Cameras.playScreenCam.position.x = tempCamX;
        tempCamX = -1;
        hud.stage.addActor(this);
        hud.stage.addActor(questWindow);
        if(consoleWindow.getStage()==null)
            hud.stage.addActor(consoleWindow);
        hud.stage.setKeyboardFocus(codeTextArea);
        if(!MyGdxGame.platformDepended.deviceHasKeyboard())
            androidExtraKeyboardWindow.show(hud.stage);
        Gdx.input.setInputProcessor(hud.stage);
    }

    public void closeCurrentEditor() {
        if(this.getStage()!=null) {
            hud.playScreen.getPlayer().setCurrentState(Player.State.STANDING);
            this.remove();
            consoleWindow.remove();
            questWindow.remove();
            if(!MyGdxGame.platformDepended.deviceHasKeyboard())
                androidExtraKeyboardWindow.remove();
            hud.showAndroidInputTable();

            //save text to pc
            if(!codeTextArea.isDisabled())
                currentPc.setEditorText(codeTextArea.getText());
        }
    }

    public void show(Pc pc) {
        currentPc = pc;
        codeTextArea.setText(pc.getEditorText());
        codeTextArea.setDisabled(false);

        //add to stage
        hud.stage.addActor(this);
        hud.stage.setKeyboardFocus(codeTextArea);
        Gdx.input.setInputProcessor(hud.stage);

        //add the myClasses that the player found in the editor
        boolean flag;
        for(final Map.Entry<String, String> s : hud.playScreen.getPlayer().getClasses().entrySet()) {
            flag = true;
            for(Actor textBtn: classesTable.getChildren()) {
                if(((TextButton)textBtn).getText().toString().equals(s.getKey()+".java")) {
                    flag = false;
                    break;
                }
            }
            if(flag) {
                final TextButton btn = new TextButton(s.getKey() + ".java", hud.playScreen.getAssets().getNeonSkin());
                btn.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        if(!codeTextArea.isDisabled()) {
                            currentPc.setEditorText(codeTextArea.getText());
                            myClassCursorPosition = codeTextArea.getCursorPosition();
                        }
                        codeTextArea.setText(s.getValue().replaceAll("\r",""));
                        codeTextArea.setDisabled(true);
                        if(currentPc.getQuest().getQuestN()==0) {
                            if (currentPc.getQuest().getProgress() == 0 && btn.getText().toString().equals("InfoSign.java"))
                                questWindow.incrementQuestStep(currentPc.getQuest(), EditorWindow.this);
                        }
                        else {
                            if (currentPc.getQuest().getProgress() == 0 && btn.getText().toString().equals("Lever.java"))
                                questWindow.incrementQuestStep(currentPc.getQuest(), EditorWindow.this);
                        }
                    }
                });
                classesTable.add(btn).left().height(50).padLeft(0);
            }
        }
        if(codeTextArea.getText().length()>0)
            codeTextArea.setCursorPosition(70);

        consoleWindow.show(hud.stage);
        questWindow.show(hud.stage, currentPc.getQuest());
        if(!MyGdxGame.platformDepended.deviceHasKeyboard())
            androidExtraKeyboardWindow.show(hud.stage);
        hud.hideAndroidInputTable();
    }

    public void virtualTypeKey(char character) {
        String temp = codeTextArea.getText();
        int pos = codeTextArea.getCursorPosition();
        temp = temp.substring(0, pos) + character + temp.substring(pos, temp.length());
        codeTextArea.setText(temp);
        codeTextArea.setCursorPosition(pos+1);
    }


    private boolean validateCodeForQuest(PlayScreen playScreen, MyClass myClass, int questN) {
        if(playScreen.getCurrentLevelID().equals("1_1")) {
            switch (currentPc.getQuest().getProgress()) {
                case 0:
                    if (consoleWindow.getConsoleTextArea().getText().toString().equals("[RED]Error: main method not found[]\n"))
                        return true;
                    break;
                case 1:
                    return true;
                case 2:
                    if(myClass.getCode().contains("System.out.println(\"Hello World!\");")) {
                        showEditorAgain = false;
                        return true;
                    }
                    break;
            }
        }
        else if(playScreen.getCurrentLevelID().equals("2_1")) {
            switch (currentPc.getQuest().getProgress()) {
                case 0:
                    for(MyVariable variable : myClass.getVariables()) {
                        if(variable.getType().equals("int"))
                            return true;
                    }
                    break;

                case 1:
                    for(MyVariable variable : myClass.getVariables()) {
                        if(variable.getType().equals("double"))
                            return true;
                    }
                    break;
                case 2:
                    for(MyVariable variable : myClass.getVariables()) {
                        if(variable.getType().equals("boolean"))
                            return true;
                    }
                    break;
                case 3:
                    for(MyVariable variable : myClass.getVariables()) {
                        if(variable.getType().equals("char"))
                            return true;
                    }
                    break;
                case 4:
                    for(MyVariable variable : myClass.getVariables()) {
                        if(variable.getType().equals("String")) {
                            return true;
                        }
                    }
                    break;
            }
        }
        else if(playScreen.getCurrentLevelID().equals("2_2")) {

            for(MyVariable myVariable: myClass.getVariables()) {
                if(myVariable.getType().equals("int")) {
                    for (FloatingPlatform floatingPlatform : playScreen.getObjectManager().getFloatingPlatforms()) {
                        if (floatingPlatform.getName().equals(myVariable.getName())) {
                            if(!myVariable.getValue().equals("null")) {
                                floatingPlatform.setTransform(playScreen.getObjectManager().getMarkers().get(0).getBounds().x / MyGdxGame.PPM + playScreen.getObjectManager().getMarkers().get(0).getBounds().width / 2 / MyGdxGame.PPM,
                                        playScreen.getObjectManager().getMarkers().get(0).getBounds().y / MyGdxGame.PPM - floatingPlatform.getBounds().height / 2 / MyGdxGame.PPM + playScreen.getObjectManager().getMarkers().get(0).getBounds().height / 2 / MyGdxGame.PPM
                                                + 0.64f * Integer.parseInt(myVariable.getValue()), 0);
                                break;
                            }
                        }
                    }
                }
                else if(myVariable.getType().equals("double")) {
                    for(FloatingPlatform floatingPlatform: playScreen.getObjectManager().getFloatingPlatforms()) {
                        if(floatingPlatform.getName().equals(myVariable.getName())) {
                            if(!myVariable.getValue().equals("null")) {
                                floatingPlatform.setTransform(playScreen.getObjectManager().getMarkers().get(1).getBounds().x / MyGdxGame.PPM + playScreen.getObjectManager().getMarkers().get(1).getBounds().width / 2 / MyGdxGame.PPM,
                                        playScreen.getObjectManager().getMarkers().get(1).getBounds().y / MyGdxGame.PPM - floatingPlatform.getBounds().height / 2 / MyGdxGame.PPM + playScreen.getObjectManager().getMarkers().get(1).getBounds().height / 2 / MyGdxGame.PPM
                                                + 0.64f * (float) Double.parseDouble(myVariable.getValue()), 0);
                                break;
                            }
                        }
                    }
                }
                else if(myVariable.getType().equals("boolean")) {
                    for(FloatingPlatform floatingPlatform: playScreen.getObjectManager().getFloatingPlatforms()) {
                        if(floatingPlatform.getName().equals(myVariable.getName())) {
                            if(myVariable.getValue().equals("true")) {
                                floatingPlatform.setTransform(playScreen.getObjectManager().getMarkers().get(2).getBounds().x / MyGdxGame.PPM + playScreen.getObjectManager().getMarkers().get(2).getBounds().width / 2 / MyGdxGame.PPM,
                                        playScreen.getObjectManager().getMarkers().get(2).getBounds().y / MyGdxGame.PPM - floatingPlatform.getBounds().height / 2 / MyGdxGame.PPM + playScreen.getObjectManager().getMarkers().get(2).getBounds().height / 2 / MyGdxGame.PPM
                                                + 0.64f*1.5f, 0);
                            }
                            else {
                                floatingPlatform.setTransform(playScreen.getObjectManager().getMarkers().get(2).getBounds().x / MyGdxGame.PPM + playScreen.getObjectManager().getMarkers().get(2).getBounds().width / 2 / MyGdxGame.PPM,
                                        playScreen.getObjectManager().getMarkers().get(2).getBounds().y / MyGdxGame.PPM - floatingPlatform.getBounds().height / 2 / MyGdxGame.PPM + playScreen.getObjectManager().getMarkers().get(2).getBounds().height / 2 / MyGdxGame.PPM
                                                - 0.64f*1.5f, 0);
                            }
                            break;
                        }
                    }
                }
                else if(myVariable.getType().equals("char")) {
                    for(FloatingPlatform floatingPlatform: playScreen.getObjectManager().getFloatingPlatforms()) {
                        if(floatingPlatform.getName().equals(myVariable.getName())) {
                            if(myVariable.getValue().equals("a")) {
                                floatingPlatform.setTransform(playScreen.getObjectManager().getMarkers().get(3).getBounds().x / MyGdxGame.PPM + playScreen.getObjectManager().getMarkers().get(3).getBounds().width / 2 / MyGdxGame.PPM,
                                        playScreen.getObjectManager().getMarkers().get(3).getBounds().y / MyGdxGame.PPM - floatingPlatform.getBounds().height / 2 / MyGdxGame.PPM + playScreen.getObjectManager().getMarkers().get(3).getBounds().height / 2 / MyGdxGame.PPM
                                                + 0.64f*1.5f, 0);
                                break;
                            }
                            else if(myVariable.getValue().equals("b")) {
                                floatingPlatform.setTransform(playScreen.getObjectManager().getMarkers().get(3).getBounds().x / MyGdxGame.PPM + playScreen.getObjectManager().getMarkers().get(3).getBounds().width / 2 / MyGdxGame.PPM,
                                        playScreen.getObjectManager().getMarkers().get(3).getBounds().y / MyGdxGame.PPM - floatingPlatform.getBounds().height / 2 / MyGdxGame.PPM + playScreen.getObjectManager().getMarkers().get(3).getBounds().height / 2 / MyGdxGame.PPM, 0);
                                break;
                            }
                            else if(myVariable.getValue().equals("c")) {
                                floatingPlatform.setTransform(playScreen.getObjectManager().getMarkers().get(3).getBounds().x / MyGdxGame.PPM + playScreen.getObjectManager().getMarkers().get(3).getBounds().width / 2 / MyGdxGame.PPM,
                                        playScreen.getObjectManager().getMarkers().get(3).getBounds().y / MyGdxGame.PPM - floatingPlatform.getBounds().height / 2 / MyGdxGame.PPM + playScreen.getObjectManager().getMarkers().get(3).getBounds().height / 2 / MyGdxGame.PPM
                                                - 0.64f*1.5f, 0);
                                break;
                            }
                            else {
                                floatingPlatform.setTransform(-5, -5, 0);
                                break;
                            }
                        }
                    }
                }
                else if(myVariable.getType().equals("String")) {
                    for(FloatingPlatform floatingPlatform: playScreen.getObjectManager().getFloatingPlatforms()) {
                        if(floatingPlatform.getName().equals(myVariable.getName())) {
                            if(myVariable.getValue().equals("hello")) {
                                floatingPlatform.setTransform(playScreen.getObjectManager().getMarkers().get(4).getBounds().x / MyGdxGame.PPM + playScreen.getObjectManager().getMarkers().get(4).getBounds().width / 2 / MyGdxGame.PPM,
                                        playScreen.getObjectManager().getMarkers().get(4).getBounds().y / MyGdxGame.PPM - floatingPlatform.getBounds().height / 2 / MyGdxGame.PPM + playScreen.getObjectManager().getMarkers().get(4).getBounds().height / 2 / MyGdxGame.PPM, 0);
                                break;
                            }
                            else {
                                floatingPlatform.setTransform(-5, -5, 0);
                                break;
                            }
                        }
                    }
                }
            }

            switch (currentPc.getQuest().getProgress()) {
                case 0:
                    for(MyVariable myVariable: myClass.getVariables()) {
                        if(myVariable.getName().equals("a")) {
                            if (myVariable.getType().equals("int")) {
                                if(myVariable.getValue().equals("2")) {
                                    tempHideEditor(playScreen.getObjectManager().getMarkers().get(0).getBounds().x/ MyGdxGame.PPM , true);
                                    return true;
                                }
                            }
                        }
                    }
                    break;
                case 1:
                    for(MyVariable myVariable: myClass.getVariables()) {
                        if(myVariable.getName().equals("a")) {
                            if (myVariable.getType().equals("int")) {
                                if(!myVariable.getValue().equals("null")) {
                                    tempHideEditor(playScreen.getObjectManager().getMarkers().get(0).getBounds().x/ MyGdxGame.PPM, true);
                                    return true;
                                }
                            }
                        }
                    }
                    break;
                case 2:
                    for(MyVariable myVariable: myClass.getVariables()) {
                        if (myVariable.getType().equals("double")) {
                            if(myVariable.getValue().contains(".")) {
                                tempHideEditor(playScreen.getObjectManager().getMarkers().get(1).getBounds().x/ MyGdxGame.PPM, true);
                                return true;
                            }
                        }
                    }
                    break;
                case 3:
                    for(MyVariable myVariable: myClass.getVariables()) {
                        if (myVariable.getType().equals("boolean")) {
                            if(!myVariable.getValue().equals("null")) {
                                tempHideEditor(playScreen.getObjectManager().getMarkers().get(2).getBounds().x/ MyGdxGame.PPM, true);
                                return true;
                            }
                        }
                    }
                    break;
                case 4:
                    for(MyVariable myVariable: myClass.getVariables()) {
                        if (myVariable.getType().equals("char")) {
                            if(!myVariable.getValue().equals("null")) {
                                tempHideEditor(playScreen.getObjectManager().getMarkers().get(3).getBounds().x/ MyGdxGame.PPM, true);
                                return true;
                            }
                        }
                    }
                    break;
                case 5:
                    for(MyVariable myVariable: myClass.getVariables()) {
                        if (myVariable.getType().equals("String")) {
                            if(myVariable.getValue().equals("hello")) {
                                showEditorAgain = false;
                                tempHideEditor(playScreen.getObjectManager().getMarkers().get(4).getBounds().x/ MyGdxGame.PPM, true);
                                return true;
                            }
                        }
                    }
                    break;
            }
        }
        else if(playScreen.getCurrentLevelID().equals("4_1")) {
            switch (currentPc.getQuest().getProgress()) {
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
            }
        }
        else if(playScreen.getCurrentLevelID().equals("7_1")) {
            switch (questN) {
                case 0: //1st quest
                    if (currentPc.getQuest().getProgress() == 1) {
                        for (MyVariable myVariable : myClass.getVariables()) {
                            if (myVariable.getType().equals("InfoSign")) {
                                playScreen.getObjectManager().getInfoSigns().add(new InfoSign("info-7_1-3", playScreen.getWorld(), playScreen.getMap(), playScreen.getObjectManager().getMarkers().get(0).getBounds(), 0, playScreen.getAssets().getTextureAtlas()));
                                playScreen.getObjectManager().addGameObjectBeforePlayer(playScreen.getObjectManager().getInfoSigns().get(playScreen.getObjectManager().getInfoSigns().size()-1));
                                showEditorAgain = false;
                                return true;
                            }
                        }
                    }
                    break;
                case 1: //2nd quest
                    if (currentPc.getQuest().getProgress() == 1) {
                        for (MyVariable myVariable : myClass.getVariables()) {
                            if (myVariable.getType().equals("Lever") && !myVariable.getName().equals("null")) {
                                tempHideEditor(-1, true);
                                playScreen.getObjectManager().getLevers().add(new Lever("lever-7_1-0", playScreen.getWorld(), playScreen.getMap(), new Rectangle(currentPc.getBounds().x + 200,
                                        currentPc.getBounds().y, 90, 90), 0, false, playScreen.getAssets().getTextureAtlas()));
                                playScreen.getObjectManager().addGameObjectBeforePlayer(playScreen.getObjectManager().getLevers().get(playScreen.getObjectManager().getLevers().size()-1));
                                return true;
                            }
                        }
                    }
                    else if (currentPc.getQuest().getProgress()==2) {
                        if(myClass.getMethodsCalled().contains("Lever.pull"))
                            return true;
                    }
                    break;
            }
        }
        return false;
    }

    public void completed() {
        if(hud.playScreen.getCurrentLevelID().equals("1_1")) {
            tempHideEditor(-1, false);
            hud.playScreen.getObjectManager().getDoors().get(3).open();
        }
        else if(hud.playScreen.getCurrentLevelID().equals("2_1")) {
            closeCurrentEditor();
            hud.playScreen.getObjectManager().getDoors().get(0).open();
        }

        else if(hud.playScreen.getCurrentLevelID().equals("7_1")) {
            tempHideEditor(-1, true);
            if(currentPc.getQuest().getQuestN()!=0) {
                hud.playScreen.getObjectManager().getLevers().get(0).pull();
                hud.playScreen.getObjectManager().getDoors().get(4).open();
                showEditorAgain = false;
            }
        }
    }

    public Window getQuestWindow() {
        return questWindow;
    }

    public Hud getHud() {
        return hud;
    }
}
