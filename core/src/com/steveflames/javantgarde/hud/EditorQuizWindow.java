package com.steveflames.javantgarde.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
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

/**
 * Created by Flames on 3/12/2017.
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

    EditorQuizWindow(String title, Skin skin, final PlayScreen playScreen) {
        super(title, skin);
        this.playScreen = playScreen;

        //top bar
        Table topBarTable = new Table(Skins.neonSkin);
        TextButton exitBtn = new TextButton("x", Skins.neonSkin);
        exitBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                closeCurrentEditor();
            }
        });
        topBarTable.add(exitBtn).expandX().top().right();

        //codeLabel
        codeLabel = new Label("", Skins.skin);
        codeLabel.getStyle().fontColor = Color.WHITE;

        Table codeTable = new Table(Skins.neonSkin);
        codeTable.add(codeLabel).expand().fillX().top();

        //add components to window
        this.setSize(700, MyGdxGame.HEIGHT-240);
        this.setX(MyGdxGame.WIDTH/2 - this.getWidth()/2);
        this.setY(170);
        this.add(topBarTable).expandX().fillX().top();
        this.row();
        this.add(codeTable).expand().fillX().top();
        extraKeyboardWindow = new AndroidExtraKeyboardWindow("", Skins.skin, this);
    }

    public void update(float dt) {
        if(currentPc != null) {
            timer += dt * answered;
            if(timer > 1.5) { //1.5 after the answer
                if(correct) { //if it is correct
                    tempHideEditor(-1); //hide the editor temporarily to show the result in the map (e.g. open door)
                    if(playScreen.getCurrentLevel().getId().equals("4_1")) {
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
                }
            }
            if (timer > 2.8) { //now show the editor again
                answered = 0;
                timer = 0;
                if(correct) {
                    if (currentPc.getQuest().nextQuestStep()) //quest step completed
                        updateUI();
                    else //whole quest completed
                        closeCurrentEditor();
                }
                else
                    playScreen.getObjectManager().getFloatingPlatforms().get(0).resetB2Body();

                if(correct) {
                    correct = false;
                    if (showEditor)
                        tempShowEditor();
                    else  //dont show editor again
                        Hud.playScreen.getPlayer().setCurrentState(Player.State.STANDING);
                }
            }
        }
    }

    void btnPressed(String text) {
        if(answered == 0) {
            codeLabel.setText(codeLabel.getText().toString().replaceAll("\\[RED].*\\[]", "[RED]" + text.substring(1) + "[]"));
            answered = 1;

            if (text.charAt(0) == '!') { //correct answer
                playScreen.getPlayer().showPlayerMsg("correct!");
                codeLabel.getText().replace("RED", "GREEN");
                correct = true;
            }
            else { //wrong answer
                closeCurrentEditor();
                playScreen.getPlayer().setCurrentState(Player.State.CODING); //so that the player can't run
                playScreen.getObjectManager().getFloatingPlatforms().get(0).b2body.setLinearVelocity(0, -5);
            }
        }
    }

    void show(Pc pc) {
        currentPc = pc;

        //add to stage
        playScreen.getHud().stage.addActor(this);
        playScreen.getHud().stage.addActor(extraKeyboardWindow);
        Gdx.input.setInputProcessor(playScreen.getHud().stage);

        updateUI();
    }

    private void updateUI() {
        String questStepText = currentPc.getQuest().getCurrentQuestStepText();
        if(!questStepText.equals("Quest completed!")) { //next quest step
            codeLabel.setText(questStepText);
            extraKeyboardWindow.clearButtons();
            for (String s : currentPc.getQuest().getCurrentQuestStep().getHints()) //add buttons
                extraKeyboardWindow.addButton(s.substring(0, s.length()).replace("\n", ""));
        }
        else { //quest completed
            codeLabel.setText("[GREEN]Quest completed![]");
            extraKeyboardWindow.clearButtons();
            extraKeyboardWindow.remove();
        }
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
        Cameras.playScreenCam.position.x = tempCamX;
        tempCamX = -1;
        playScreen.getHud().stage.addActor(this);
        playScreen.getHud().stage.addActor(extraKeyboardWindow);
        Gdx.input.setInputProcessor(playScreen.getHud().stage);
    }

    public void closeCurrentEditor() {
        if(this.getStage()!=null) {
            Hud.playScreen.getPlayer().setCurrentState(Player.State.STANDING);
            this.remove();
            extraKeyboardWindow.clearButtons();
            extraKeyboardWindow.remove();
            Hud.showAndroidInputTable();

            if(currentPc.getQuest().isCompleted()) { //if text completion has a toast, show it
                if(currentPc.getQuest().getCompletedText()!= null)
                    Hud.newToast(currentPc.getQuest().getCompletedText());
            }
        }
    }
}
