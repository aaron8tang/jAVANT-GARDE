package com.steveflames.javantgarde.hud.quiz_pc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.steveflames.javantgarde.MyGdxGame;
import com.steveflames.javantgarde.hud.AndroidExtraKeyboardWindow;
import com.steveflames.javantgarde.screens.PlayScreen;
import com.steveflames.javantgarde.sprites.Pc;
import com.steveflames.javantgarde.sprites.Player;
import com.steveflames.javantgarde.tools.Assets;
import com.steveflames.javantgarde.tools.global.Cameras;

import java.util.Random;

/**
 * This class implements the 'fill in the blank' pc.
 */

public class EditorQuizWindow extends Window {

    private Pc currentPc;
    private PlayScreen playScreen;
    private Label codeLabel;
    private AndroidExtraKeyboardWindow extraKeyboardWindow;

    private float timer=0;
    private int answered = 0;
    private boolean correct = false;

    private float tempCamX = - 1;
    private boolean showEditor = true;
    private boolean doOnce = true;
    private boolean[] booleanArray;

    private Assets assets;

    public EditorQuizWindow(final PlayScreen playScreen) {
        super(playScreen.getAssets().playscreenBundle.get("editor"), playScreen.getAssets().neonSkin, "window2");
        this.playScreen = playScreen;
        this.assets = playScreen.getAssets();

        //top bar
        Table topBarTable = new Table(playScreen.getAssets().neonSkin);
        TextButton exitBtn = new TextButton("x", playScreen.getAssets().neonSkin);
        exitBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                closeCurrentEditor();
            }
        });
        Label infoLabel = new Label("[CYAN]"+assets.playscreenBundle.get("fill_prompt")+"[]", playScreen.getAssets().neonSkin);
        topBarTable.add(infoLabel).expandX().center();
        topBarTable.add(exitBtn).top().right();

        //codeLabel
        codeLabel = new Label("", playScreen.getAssets().neonSkin);
        codeLabel.getStyle().fontColor = Color.WHITE;

        Table codeTable = new Table(playScreen.getAssets().neonSkin);
        codeTable.add(codeLabel).expand().fillX().top();

        //add components to window
        this.setSize(740, Cameras.hudPort.getCamera().viewportHeight-240);
        this.setX(Cameras.hudPort.getCamera().viewportWidth/2 - this.getWidth()/2);
        this.setY(170);
        this.add(topBarTable).expandX().fillX().top();
        this.row();
        this.add(codeTable).expand().fillX().top();
        extraKeyboardWindow = new AndroidExtraKeyboardWindow("", playScreen.getAssets(), this);

        //level specific
        if(playScreen.getCurrentLevelID().equals("6_2")) {
            playScreen.getObjectManager().getMarkers().get(0).setText(assets.playscreenBundle.get("console")+":");

            booleanArray = new boolean[12];
            for(int i=0; i<booleanArray.length; i++)
                booleanArray[i] = false;
            Random rand = new Random();
            int k = rand.nextInt(12);
            booleanArray[k] = true;

            playScreen.getObjectManager().getQuizes().get(0).getFloatingPlatforms().get(k).setAnswerText("!"+playScreen.getObjectManager().getQuizes().get(0).getFloatingPlatforms().get(k).getName());
        }
    }

    public void update(float dt) {
        if(currentPc != null) {
            timer += dt * answered;
            if(timer > 1.5 && doOnce) { //1.5 after the answer
                doOnce = false;
                if(correct) { //if it is correct
                    if(playScreen.getCurrentLevelID().equals("6_2") || playScreen.getCurrentLevelID().equals("5_2")) //on level 6_2 no need to hide editor
                        timer = 2.5f;
                    else
                        tempHideEditor(-1); //hide the editor temporarily to show the result in the map (e.g. open door)
                    if(playScreen.getCurrentLevelID().equals("3_2")) {
                        switch (currentPc.getQuest().getProgress()) {
                            case 0:
                                playScreen.getObjectManager().getFloatingPlatforms().get(1).drop(1600/MyGdxGame.PPM);
                                break;
                            case 1:
                                playScreen.getObjectManager().getFloatingPlatforms().get(2).drop(1600/MyGdxGame.PPM);
                                break;
                            case 2:
                                showEditor = false;
                                playScreen.getObjectManager().getFloatingPlatforms().get(3).drop(1600/MyGdxGame.PPM);
                                break;
                        }
                    }

                    else if(playScreen.getCurrentLevelID().equals("4_2")) {
                        switch (currentPc.getQuest().getProgress()) {
                            case 0:
                                Cameras.setCameraTo(playScreen.getObjectManager().getDoors().get(0).b2body.getPosition().x);
                                playScreen.getObjectManager().getDoors().get(0).open();
                                break;
                            case 1:
                                Cameras.setCameraTo(playScreen.getObjectManager().getDoors().get(1).b2body.getPosition().x);
                                playScreen.getObjectManager().getDoors().get(1).open();
                                break;
                            case 2:
                                Cameras.setCameraTo(playScreen.getObjectManager().getDoors().get(2).b2body.getPosition().x);
                                playScreen.getObjectManager().getDoors().get(2).open();
                                break;
                            case 3:
                                Cameras.setCameraTo(playScreen.getObjectManager().getDoors().get(3).b2body.getPosition().x);
                                playScreen.getObjectManager().getDoors().get(3).open();
                                break;
                            case 4:
                                Cameras.setCameraTo(playScreen.getObjectManager().getDoors().get(4).b2body.getPosition().x);
                                playScreen.getObjectManager().getDoors().get(4).open();
                                break;
                            case 5:
                                Cameras.setCameraTo(playScreen.getObjectManager().getDoors().get(5).b2body.getPosition().x);
                                showEditor = false;
                                playScreen.getObjectManager().getDoors().get(5).open();
                                break;
                        }
                    }
                    else if(playScreen.getCurrentLevelID().equals("5_1")) {
                        switch (currentPc.getQuest().getProgress()) {
                            case 0:
                                playScreen.getObjectManager().getDoors().get(1).open();
                                break;
                            case 1:
                                playScreen.getObjectManager().getDoors().get(2).open();
                                break;
                            case 2:
                                playScreen.getObjectManager().getDoors().get(3).open();
                                break;
                            case 3:
                                playScreen.getObjectManager().getDoors().get(4).open();
                                break;
                            case 4:
                                showEditor = false;
                                playScreen.getObjectManager().getDoors().get(5).open();
                                break;
                        }
                    }
                    else if(playScreen.getCurrentLevelID().equals("5_2")) {
                        if(currentPc.getQuest().getProgress() == 3) {
                            timer = 3;
                            playScreen.getObjectManager().getSensorRobots().get(1).setUpperSensor(true);
                            playScreen.getObjectManager().getSensorRobots().get(1).setRunRight(true);
                        }
                    }
                    else if(playScreen.getCurrentLevelID().equals("6_1")) {
                        switch (currentPc.getQuest().getProgress()) {
                            case 0:
                                Cameras.setCameraTo(playScreen.getObjectManager().getFloatingPlatforms().get(1).b2body.getPosition().x);
                                playScreen.getObjectManager().getFloatingPlatforms().get(1).drop(-2000/MyGdxGame.PPM);
                                break;
                            case 1:
                                Cameras.setCameraTo(playScreen.getObjectManager().getFloatingPlatforms().get(2).b2body.getPosition().x);
                                playScreen.getObjectManager().getFloatingPlatforms().get(2).drop(-2400/MyGdxGame.PPM);
                                break;
                            case 2:
                                Cameras.setCameraTo(playScreen.getObjectManager().getFloatingPlatforms().get(3).b2body.getPosition().x);
                                playScreen.getObjectManager().getFloatingPlatforms().get(3).drop(-2800/MyGdxGame.PPM);
                                break;
                            case 3:
                                Cameras.setCameraTo(playScreen.getObjectManager().getFloatingPlatforms().get(4).b2body.getPosition().x);
                                playScreen.getObjectManager().getFloatingPlatforms().get(4).drop(-2000/MyGdxGame.PPM);
                                break;
                            case 4:
                                Cameras.setCameraTo(playScreen.getObjectManager().getFloatingPlatforms().get(5).b2body.getPosition().x);
                                showEditor = false;
                                playScreen.getObjectManager().getFloatingPlatforms().get(5).drop(-2200/MyGdxGame.PPM);
                                break;
                        }
                    }
                    else if(playScreen.getCurrentLevelID().equals("6_2")) {
                        if(currentPc.getQuest().getProgress() == 3) {
                            timer = 3;
                            for(boolean b : booleanArray) {
                                playScreen.getObjectManager().getMarkers().get(0).setText(playScreen.getObjectManager().getMarkers().get(0).getText()+ "\n" + b);
                            }
                        }
                    }
                }
            }
            else if (timer > 2.8) { //now show the editor again
                answered = 0;
                timer = 0;
                doOnce = true;
                if(correct) {
                    if (currentPc.getQuest().nextQuestStep()) //quest step completed
                        updateUI();
                    else {//whole quest completed
                        completed();
                        closeCurrentEditor();
                    }
                }
                else {
                    if(!playScreen.getCurrentLevelID().equals("6_2") && !playScreen.getCurrentLevelID().equals("5_2"))
                        playScreen.getObjectManager().getFloatingPlatforms().get(0).resetB2Body();
                }

                if(correct) {
                    correct = false;
                    if (showEditor)
                        tempShowEditor();
                    else { //dont show editor again
                        playScreen.getPlayer().setCurrentState(Player.State.STANDING);
                        playScreen.getHud().showAndroidInputTable();
                    }
                }
            }
        }
        for(int i=0; i<playScreen.getObjectManager().getFloatingPlatforms().size(); i++) {
            if(playScreen.getObjectManager().getFloatingPlatforms().get(i).b2body.getLinearVelocity().y==0)
                playScreen.getObjectManager().getFloatingPlatforms().get(i).b2body.setType(BodyDef.BodyType.KinematicBody);
        }
    }

    public void btnPressed(String text) {
        if(answered == 0) {
            text = text.replaceAll("\\[]","[[]");
            codeLabel.setText(codeLabel.getText().toString().replaceAll("\\[RED]_*\\[]", "[RED]" + text.substring(1) + "[]"));

            if (text.charAt(0) == '!') { //correct answer
                assets.playSound(assets.correctSound);
                playScreen.getPlayer().showPlayerMsg(assets.playscreenBundle.get("correct"));
                codeLabel.getText().replace("RED", "GREEN");
                correct = true;
            }
            else { //wrong answer
                assets.playSound(assets.wrongSound);
                closeCurrentEditor();
                playScreen.getPlayer().setCurrentState(Player.State.CODING); //so that the player can't run
                if(!playScreen.getCurrentLevelID().equals("6_2") && !playScreen.getCurrentLevelID().equals("5_2"))
                    playScreen.getObjectManager().getFloatingPlatforms().get(0).drop(1200/MyGdxGame.PPM);
                else {
                    playScreen.getPlayer().setCurrentState(Player.State.STANDING);
                    playScreen.getPlayer().b2body.applyLinearImpulse(-4000/MyGdxGame.PPM, 1200/MyGdxGame.PPM, 0, 0, true);
                }
            }
            answered = 1;
        }
    }

    public void show(Pc pc) {
        currentPc = pc;

        //add to stage
        playScreen.getHud().stage.addActor(this);
        playScreen.getHud().stage.addActor(extraKeyboardWindow);
        Gdx.input.setInputProcessor(playScreen.getHud().stage);

        updateUI();
        playScreen.getHud().hideAndroidInputTable();
    }

    private void updateUI() {
        String questStepText = currentPc.getQuest().getCurrentQuestStepText(assets.playscreenBundle.get("quest_completed"));
        if(!questStepText.contains("Quest completed!")) { //next quest step
            codeLabel.setText(questStepText);
            extraKeyboardWindow.clearButtons();
            for (String s : currentPc.getQuest().getCurrentQuestStep().getHints()) //add buttons
                extraKeyboardWindow.addButton(s.substring(0, s.length()).replace("\n", ""));
        }
    }

    private void completed() {
        assets.playSound(assets.questSound);
        codeLabel.setText("[GREEN]Quest completed![]");
        extraKeyboardWindow.clearButtons();
        extraKeyboardWindow.remove();
    }

    private void tempHideEditor(float camX) {
        if(tempCamX == -1)
            tempCamX = Cameras.playScreenCam.position.x;
        if(camX != -1)
            Cameras.playScreenCam.position.x = camX;
        this.remove();
        extraKeyboardWindow.remove();
    }

    private void tempShowEditor() {
        if(!playScreen.getCurrentLevelID().equals("6_2") && !playScreen.getCurrentLevelID().equals("5_2")) {
            Cameras.playScreenCam.position.x = tempCamX;
            tempCamX = -1;
            playScreen.getHud().stage.addActor(this);
            playScreen.getHud().stage.addActor(extraKeyboardWindow);
            Gdx.input.setInputProcessor(playScreen.getHud().stage);
        }
    }

    public void closeCurrentEditor() {
        if(this.getStage()!=null && answered == 0) {
            assets.playSound(assets.clickSound);
            playScreen.getPlayer().setCurrentState(Player.State.STANDING);
            this.remove();
            extraKeyboardWindow.clearButtons();
            extraKeyboardWindow.remove();
            playScreen.getHud().showAndroidInputTable();

            if(currentPc.getQuest().isCompleted()) { //if text completion has a toast, show it
                if(currentPc.getQuest().getCompletedText()!= null)
                    playScreen.getHud().newToast(currentPc.getQuest().getCompletedText());
            }
        }
    }
}
