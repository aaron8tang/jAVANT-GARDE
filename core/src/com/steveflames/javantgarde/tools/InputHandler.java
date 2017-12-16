package com.steveflames.javantgarde.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.steveflames.javantgarde.MyGdxGame;
import com.steveflames.javantgarde.hud.Hud;
import com.steveflames.javantgarde.sprites.Player;
import com.steveflames.javantgarde.sprites.Quiz;
import com.steveflames.javantgarde.screens.PlayScreen;
import com.steveflames.javantgarde.sprites.FloatingPlatform;
import com.steveflames.javantgarde.sprites.InfoSign;
import com.steveflames.javantgarde.sprites.Pc;
import com.steveflames.javantgarde.tools.global.Cameras;

/**
 * Created by Flames on 29/10/2017.
 */

public class InputHandler {

    private PlayScreen playScreen;

    public InputHandler(PlayScreen playScreen) {
        this.playScreen = playScreen;
    }

    public void handleInput() {
        if (!playScreen.getHud().getCurrentToast().isShowing() || playScreen.getHud().getCurrentToast() == null) {
            if (playScreen.getPlayer().canMove) {
                //move player on key press
                if (Gdx.input.isKeyJustPressed(Input.Keys.UP) || playScreen.getHud().isJumpBtnPressed()) {
                    playScreen.getHud().setJumpBtnPressed();
                    playScreen.getPlayer().jump();
                }
                else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || playScreen.getHud().isRightBtnPressed()) {
                    playScreen.getPlayer().setRunRight(true);
                }
                else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || playScreen.getHud().isLeftBtnPressed()) {
                    playScreen.getPlayer().setRunRight(false);
                    playScreen.getPlayer().setRunLeft(true);
                }
                else {
                    playScreen.getPlayer().setRunRight(false);
                    playScreen.getPlayer().setRunLeft(false);
                }
                if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                    Hud.newToast("Use the ARROWS to move around and jump\n" +
                            "ENTER to use item\n" +
                            "ESCAPE to exit");
                }

                //use item
                if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || playScreen.getHud().isUseBtnPressed()) {
                    playScreen.getHud().setUseBtnPressed();
                    if (Player.colliding) {
                        if (!playScreen.isEnterKeyHandled()) {
                            for (Pc pc : playScreen.getObjectManager().getPcs()) {
                                if (pc.isUsable()) {
                                    playScreen.getPlayer().setFacingDirection(1);
                                    playScreen.getPlayer().b2body.setLinearVelocity(0, 0);
                                    playScreen.getPlayer().b2body.setTransform(pc.position.x - pc.getBounds().width/2/MyGdxGame.PPM + 0.1f, pc.position.y - (pc.getBounds().height/2 + playScreen.getPlayer().b2body.getPosition().y) / MyGdxGame.PPM + 0.3f, 0);
                                    playScreen.getPlayer().setCurrentState(Player.State.CODING);
                                    switch (pc.getPcType()) {
                                        case 0: //normal pc
                                            Cameras.setCameraTo(pc.getBounds().x / MyGdxGame.PPM + 1.5f);
                                            playScreen.getHud().showEditorWindow(pc);
                                            if (playScreen.getPlayer().b2body.getPosition().y > Cameras.playScreenCam.position.y)
                                                playScreen.getHud().getEditorWindow().getQuestWindow().setPosition(0, 45);
                                            break;
                                        case 1://quiz pc
                                            Cameras.setCameraTo(pc.getBounds().x / MyGdxGame.PPM + 2.5f);
                                            playScreen.getHud().showEditorQuizWindow(pc);
                                            break;
                                        case 2://order pc
                                            Cameras.setCameraTo(pc.getBounds().x / MyGdxGame.PPM + 2.5f);
                                            playScreen.getHud().showEditorOrderWindow(pc);
                                            break;
                                    }
                                }
                            }
                            for (InfoSign infoSign : playScreen.getObjectManager().getInfoSigns()) {
                                if (infoSign.isUsable()) {
                                    playScreen.getPlayer().b2body.setLinearVelocity(0, 0);
                                    playScreen.getPlayer().b2body.setTransform(infoSign.getBounds().x / MyGdxGame.PPM + 0.17f, (infoSign.getBounds().y + playScreen.getPlayer().b2body.getPosition().y) / MyGdxGame.PPM + 0.3f, 0);
                                    playScreen.getPlayer().setCurrentState(Player.State.READING);
                                    playScreen.getHud().showInfoWindow(infoSign, infoSign.getText());
                                }
                            }
                            for (Quiz quiz : playScreen.getObjectManager().getQuizes()) {
                                for (FloatingPlatform floatingPlatform : quiz.getFloatingPlatforms()) {
                                    if (floatingPlatform.getLever().isUsable()) {
                                        playScreen.getPlayer().b2body.setLinearVelocity(0, 0);
                                        playScreen.getPlayer().b2body.setTransform(floatingPlatform.getLever().getB2body().getPosition().x - floatingPlatform.getLever().getBounds().width / 2 / MyGdxGame.PPM + 0.239f,
                                                floatingPlatform.getLever().getB2body().getPosition().y + 0.08f, 0);
                                        quiz.pull(floatingPlatform);
                                        if (floatingPlatform.isCorrect())
                                            playScreen.getPlayer().showPlayerMsg("correct!");
                                    }
                                }
                            }
                        }
                    }
                }
            }
            else {
                playScreen.getPlayer().setRunRight(false);
                playScreen.getPlayer().setRunLeft(false);
            }
        }
        else {
            playScreen.getPlayer().setRunRight(false);
            playScreen.getPlayer().setRunLeft(false);
            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || Gdx.input.justTouched())
                playScreen.getHud().getCurrentToast().handleNextPressed();
        }

        playScreen.setEnterKeyHandled(false);
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            if (playScreen.getPlayer().getCurrentState() == Player.State.CODING) {
                playScreen.getHud().getEditorWindow().closeCurrentEditor();
                playScreen.getHud().getEditorQuizWindow().closeCurrentEditor();
                playScreen.getHud().getEditorOrderWindow().closeCurrentEditor();
                playScreen.getPlayer().setCurrentState(Player.State.STANDING);
            }
            else if (playScreen.getPlayer().getCurrentState() == Player.State.READING)
                playScreen.getHud().closeCurrentInfo();
            else if (playScreen.getHud().isPauseWindowShowing())
                playScreen.getHud().getPauseWindow().remove();
            else
                playScreen.getHud().showPauseWindow();
        }
    }
}
