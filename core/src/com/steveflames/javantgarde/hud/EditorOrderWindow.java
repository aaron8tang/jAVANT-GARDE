package com.steveflames.javantgarde.hud;

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
import com.steveflames.javantgarde.tools.global.Skins;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Flames on 10/12/2017.
 */

public class EditorOrderWindow extends Window {

    private Pc currentPc;
    private PlayScreen playScreen;
    private Table codeTable;
    private TextButton compileBtn;

    private ArrayList<String> linesOfCodeTexts = new ArrayList<String>();

    private float timer=0;
    private int answered = 0;

    private float tempCamX = - 1;
    private boolean showEditor = true;

    EditorOrderWindow(String title, Skin skin, final PlayScreen playScreen) {
        super(title, skin);
        this.playScreen = playScreen;

        //bot bar
        Table botBarTable = new Table(Skins.neonSkin);
        compileBtn = new TextButton(" compile & run ", Skins.neonSkin);
        TextButton exitBtn = new TextButton("x", Skins.neonSkin);
        Label infoLabel = new Label("[CYAN]Put the code in the correct order![]", Skins.skin);
        botBarTable.add(exitBtn).left();
        botBarTable.add(infoLabel).expandX().center();
        botBarTable.add(compileBtn).right();

        //code table
        codeTable = new Table(Skins.neonSkin);

        //add components to window
        this.setSize(800, MyGdxGame.HEIGHT-170);
        this.setX(MyGdxGame.WIDTH/2 - this.getWidth()/2 + 150);
        this.setY(100);
        this.row();
        this.add(codeTable).expand().fillX().top();
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
                    playScreen.getPlayer().b2body.applyLinearImpulse(-14, 4, 0, 0, true);
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
            if(timer > 1.5) { //1.5 after the answer
                    tempHideEditor(-1); //hide the editor temporarily to show the result in the map (e.g. open door)
                if(playScreen.getCurrentLevel().getId().equals("3_1")) {
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
            }
            if (timer > 2.8f) { //now show the editor again
                answered = 0;
                timer = 0;

                if (showEditor)
                    tempShowEditor();
                else  //dont show editor again
                    Hud.playScreen.getPlayer().setCurrentState(Player.State.STANDING);

                if (currentPc.getQuest().nextQuestStep()) { //quest step completed
                    linesOfCodeTexts.clear();
                    initUI();
                }
                else {//whole quest completed
                    closeCurrentEditor();
                }


            }
        }
    }

    void show(Pc pc) {
        currentPc = pc;

        //add to stage
        playScreen.getHud().stage.addActor(this);
        Gdx.input.setInputProcessor(playScreen.getHud().stage);

        initUI();
    }

    private void initUI() {
        String questStepText = currentPc.getQuest().getCurrentQuestStepText();
        if (!questStepText.equals("Quest completed!")) { //next quest step
            if (linesOfCodeTexts.size() == 0) {
                codeTable.clear();
                linesOfCodeTexts.clear();

                String[] s = questStepText.split("\n");
                for (int i = 0; i < s.length; i++) {
                    linesOfCodeTexts.add(s[i]);
                    if (i > 0)
                        codeTable.row();

                    Table lineOfCode = new Table(Skins.neonSkin);
                    Label codeLabel = new Label(s[i], Skins.skin);
                    lineOfCode.add(codeLabel).expandX().left();
                    codeTable.add(lineOfCode).expand().fill();

                    if (i > 0) {
                        final TextButton upBtn = new TextButton("^", Skins.neonSkin);
                        upBtn.setName(i + "");
                        upBtn.addListener(new ClickListener() {
                            @Override
                            public void clicked(InputEvent event, float x, float y) {
                                Collections.swap(linesOfCodeTexts, Integer.parseInt(upBtn.getName()), Integer.parseInt(upBtn.getName()) - 1);
                                updateUIcode();
                            }
                        });
                        lineOfCode.add(upBtn).width(70);
                    }
                    if (i != s.length - 1) {
                        final TextButton downBtn = new TextButton("v", Skins.neonSkin);
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
            }
        }
        else { //quest completed
            codeTable.clear();
            codeTable.add(new Label("[GREEN]Quest completed![]", Skins.skin));
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
            Cameras.playScreenCam.position.x = camX;
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
            Hud.playScreen.getPlayer().setCurrentState(Player.State.STANDING);
            this.remove();
            Hud.showAndroidInputTable();

            if(currentPc.getQuest().isCompleted()) { //if text completion has a toast, show it
                if(currentPc.getQuest().getCompletedText()!= null)
                    Hud.newToast(currentPc.getQuest().getCompletedText());
            }
        }
    }
}
