package com.steveflames.javantgarde.hud.order_pc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.steveflames.javantgarde.MyGdxGame;
import com.steveflames.javantgarde.screens.PlayScreen;
import com.steveflames.javantgarde.sprites.Pc;
import com.steveflames.javantgarde.sprites.Player;
import com.steveflames.javantgarde.tools.global.Cameras;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Flames on 10/12/2017.
 */

public class EditorOrderWindow extends Window {

    private Pc currentPc;
    private PlayScreen playScreen;
    private Table codeTable;
    private Table preCodeTable;
    private Table afterCodeTable;
    private TextButton compileBtn;
    private Table botBarTable;

    private ArrayList<String> linesOfCodeTexts = new ArrayList<String>();

    private float timer=0;
    private int answered = 0;

    private float tempCamX = - 1;
    private boolean showEditor = true;
    private boolean doOnce = true;

    public EditorOrderWindow(String title, Skin skin, final PlayScreen playScreen) {
        super(title, skin);
        this.playScreen = playScreen;

        //bot bar
        botBarTable = new Table(playScreen.getAssets().getNeonSkin());
        compileBtn = new TextButton(" compile & run ", playScreen.getAssets().getNeonSkin());
        TextButton exitBtn = new TextButton("x", playScreen.getAssets().getNeonSkin());
        Label infoLabel = new Label("[CYAN]Put the code in the correct order![]", playScreen.getAssets().getTerraSkin());
        botBarTable.add(exitBtn).left();
        botBarTable.add(infoLabel).expandX().center();
        botBarTable.add(compileBtn).right();

        //code table
        codeTable = new Table(playScreen.getAssets().getNeonSkin());
        preCodeTable = new Table(playScreen.getAssets().getNeonSkin());
        afterCodeTable = new Table(playScreen.getAssets().getNeonSkin());

        //add components to window
        this.setSize(800, MyGdxGame.HEIGHT-120);
        this.setX(MyGdxGame.WIDTH/2 - this.getWidth()/2 + 150);
        this.setY(50);
        this.row();
        this.add(preCodeTable).expand().fillX().left();
        this.row();
        this.add(codeTable).expand().fillX().top();
        this.row();
        this.add(afterCodeTable).expand().fillX().left();
        this.row();
        this.add(botBarTable).expand().fillX().bottom();


        //ADD LISTENERS
        compileBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                StringBuilder temp = new StringBuilder();
                for(int i=0; i<linesOfCodeTexts.size(); i++) {
                    temp.append(linesOfCodeTexts.get(i));
                    if(i != linesOfCodeTexts.size()-1)
                        temp.append("\n");
                }
                if(temp.toString().equals(currentPc.getQuest().getCurrentQuestStepText())
                        || (temp.toString()+"\n").equals(currentPc.getQuest().getCurrentQuestStepText())) { //CORRECT ORDER
                    answered =1;
                    playScreen.getPlayer().showPlayerMsg("correct!");
                    for(int i=0; i<linesOfCodeTexts.size(); i++)
                        linesOfCodeTexts.set(i, "[GREEN]"+linesOfCodeTexts.get(i)+"[]");
                    updateUIcode();
                }
                else { //WRONG ORDER
                    closeCurrentEditor();
                    playScreen.getPlayer().b2body.applyLinearImpulse(-14, 6, 0, 0, true);
                }
            }
        });
        exitBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                closeCurrentEditor();
            }
        });
    }

    public void update(float dt) {
        if(currentPc != null) {
            timer += dt * answered;
            if(timer > 1.5 && doOnce) { //1.5 after the answer
                doOnce = false;
                tempHideEditor(-1); //hide the editor temporarily to show the result in the map (e.g. open door)
                completed();
            }
            else if (timer > 2.8f) { //now show the editor again
                answered = 0;
                timer = 0;
                doOnce = true;

                if (showEditor) {
                    linesOfCodeTexts.clear();
                    tempShowEditor();
                }
                else {//dont show editor again
                    playScreen.getPlayer().setCurrentState(Player.State.STANDING);
                    playScreen.getHud().showAndroidInputTable();
                }

                if (!currentPc.getQuest().nextQuestStep()) { //whole quest completed
                    closeCurrentEditor();
                }

                initUI();
            }
        }
    }

    private void completed() {
        if(playScreen.getCurrentLevelID().equals("3_1")) {
            switch (currentPc.getQuest().getProgress()) {
                case 0:
                    playScreen.getObjectManager().getDoors().get(0).open();
                    break;
                case 1:
                    playScreen.getPlayer().b2body.applyLinearImpulse(16,6,0,0,true);
                    timer = 2.9f;
                    break;
            }
        }
        else if(playScreen.getCurrentLevelID().equals("4_1")) {
            if(currentPc.getName().equals("orderpc-4_1-0")) {
                playScreen.getObjectManager().getFloatingPlatforms().get(0).b2body.setLinearVelocity(0, 2);
                playScreen.getObjectManager().getPcs().get(0).b2body.setLinearVelocity(0, 2);
            }
            else {
                playScreen.getObjectManager().getFloatingPlatforms().get(1).b2body.setLinearVelocity(0,-2);
                playScreen.getObjectManager().getPcs().get(1).b2body.setLinearVelocity(0,-2);
            }
        }
        else if(playScreen.getCurrentLevelID().equals("5_2")) {
            Cameras.setCameraTo(playScreen.getObjectManager().getSensorRobots().get(0).b2body.getPosition().x);
            playScreen.getObjectManager().getSensorRobots().get(0).setRunRight(true);
        }
    }

    public void show(Pc pc) {
        currentPc = pc;

        //add to stage
        playScreen.getHud().stage.addActor(this);
        Gdx.input.setInputProcessor(playScreen.getHud().stage);

        initUI();
        playScreen.getHud().hideAndroidInputTable();
    }

    private void initUI() {
        String questStepText = currentPc.getQuest().getCurrentQuestStepText();
        if (!questStepText.contains("Quest completed!")) { //next quest step
            if (linesOfCodeTexts.size() == 0) {
                if(!botBarTable.getChildren().contains(compileBtn, false))
                    botBarTable.add(compileBtn).right();
                codeTable.clear();
                linesOfCodeTexts.clear();
                preCodeTable.clear();
                afterCodeTable.clear();

                //init immutable code before orderCode
                for(int i=0; i<currentPc.getQuest().getCurrentQuestStep().getHints().size(); i++) {
                    if(currentPc.getQuest().getCurrentQuestStep().getHints().get(i).startsWith("[")) {
                        preCodeTable.add(new Label(currentPc.getQuest().getCurrentQuestStep().getHints().get(i).substring(1), playScreen.getAssets().getTerraSkin())).expandX().left();
                        if(i != currentPc.getQuest().getCurrentQuestStep().getHints().size()-1)
                            preCodeTable.row();
                    }
                }
                //init orderCode
                String[] s = questStepText.split("\n");
                for (int i = 0; i < s.length; i++) {
                    linesOfCodeTexts.add(s[i]);
                    if (i > 0)
                        codeTable.row();

                    Table lineOfCode = new Table(playScreen.getAssets().getNeonSkin());
                    Label codeLabel = new Label(s[i], playScreen.getAssets().getTerraSkin());
                    lineOfCode.add(codeLabel).expandX().left();
                    codeTable.add(lineOfCode).expand().fill();

                    if (i > 0) {
                        final TextButton upBtn = new TextButton("^", playScreen.getAssets().getNeonSkin());
                        upBtn.setName(i + "");
                        upBtn.addListener(new ClickListener() {
                            @Override
                            public void clicked(InputEvent event, float x, float y) {
                                Collections.swap(linesOfCodeTexts, Integer.parseInt(upBtn.getName()), Integer.parseInt(upBtn.getName()) - 1);
                                updateUIcode();
                            }
                        });
                        lineOfCode.add(upBtn).width(70);
                        if(i==s.length-1)
                            lineOfCode.add(new Label("", playScreen.getAssets().getTerraSkin())).width(70);
                    }
                    if (i != s.length - 1) {
                        final TextButton downBtn = new TextButton("v", playScreen.getAssets().getNeonSkin());
                        downBtn.setName(i + "");
                        downBtn.addListener(new ClickListener() {
                            @Override
                            public void clicked(InputEvent event, float x, float y) {
                                Collections.swap(linesOfCodeTexts, Integer.parseInt(downBtn.getName()), Integer.parseInt(downBtn.getName()) + 1);
                                updateUIcode();
                            }
                        });
                        lineOfCode.add(downBtn).width(70);
                    }
                }
                Collections.shuffle(linesOfCodeTexts);
                updateUIcode();
                //init immutable code after orderCode
                for(int i=0; i<currentPc.getQuest().getCurrentQuestStep().getHints().size(); i++) {
                    if(currentPc.getQuest().getCurrentQuestStep().getHints().get(i).startsWith("]")) {
                        afterCodeTable.add(new Label(currentPc.getQuest().getCurrentQuestStep().getHints().get(i).substring(1), playScreen.getAssets().getTerraSkin())).expandX().left();
                        if(i != currentPc.getQuest().getCurrentQuestStep().getHints().size()-1)
                            afterCodeTable.row();
                    }
                }
            }
        }
        else { //quest completed
            preCodeTable.remove();
            afterCodeTable.remove();
            linesOfCodeTexts.clear();
            codeTable.clear();
            codeTable.add(new Label("[GREEN]Quest completed![]", playScreen.getAssets().getTerraSkin()));
            compileBtn.remove();
        }
    }

    private void updateUIcode() {
        int i=0;
        for(Actor a : codeTable.getChildren()) {
            ((Label)(((Table)a).getChildren().get(0))).setText(linesOfCodeTexts.get(i));
            i++;
        }
    }

    private void tempHideEditor(float camX) {
        if(tempCamX == -1)
            tempCamX = Cameras.playScreenCam.position.x;
        if(camX != -1)
            Cameras.setCameraTo(camX);
        this.remove();
    }

    private void tempShowEditor() {
        Cameras.playScreenCam.position.x = tempCamX;
        tempCamX = -1;
        playScreen.getHud().stage.addActor(this);
        Gdx.input.setInputProcessor(playScreen.getHud().stage);
    }

    public void closeCurrentEditor() {
        if(this.getStage()!=null && answered==0) {
            playScreen.getPlayer().setCurrentState(Player.State.STANDING);
            this.remove();
            playScreen.getHud().showAndroidInputTable();

            if(currentPc.getQuest().isCompleted()) { //if text completion has a toast, show it
                if(currentPc.getQuest().getCompletedText()!= null)
                    playScreen.getHud().newToast(currentPc.getQuest().getCompletedText());
            }
        }
    }
}