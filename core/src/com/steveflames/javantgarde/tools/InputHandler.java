package com.steveflames.javantgarde.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.steveflames.javantgarde.MyGdxGame;
import com.steveflames.javantgarde.sprites.Player;
import com.steveflames.javantgarde.sprites.Quiz;
import com.steveflames.javantgarde.screens.PlayScreen;
import com.steveflames.javantgarde.sprites.FloatingPlatform;
import com.steveflames.javantgarde.sprites.InfoSign;
import com.steveflames.javantgarde.sprites.Pc;
import com.steveflames.javantgarde.tools.global.Cameras;

/**
 * This class is responsible for receiving the inputs that
 * the player gives (e.g. keyboard button press, screen touch etc.)
 */

public class InputHandler {

    private PlayScreen playScreen;

    public InputHandler(PlayScreen playScreen) {
        this.playScreen = playScreen;
    }

    public void handleInput() {
        if (!playScreen.getHud().isToastShowing() || playScreen.getHud().getCurrentToast() == null) {
            if (playScreen.getPlayer().canMove) {
                //move player on key press
                if (Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || playScreen.getHud().isJumpBtnPressed()) {
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
                //if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                //    playScreen.getHud().newToast(playScreen.getAssets().playscreenBundle.get("controls"));
                //}

                //use item
                if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || playScreen.getHud().isUseBtnPressed()) {
                    playScreen.getHud().setUseBtnPressed();
                    if (Player.colliding) {
                        if (!playScreen.isEnterKeyHandled()) {
                            for (Pc pc : playScreen.getObjectManager().getPcs()) {
                                if (pc.isUsable()) {
                                    playScreen.getPlayer().setFacingDirection(1);
                                    playScreen.getPlayer().b2body.setLinearVelocity(0, 0);
                                    playScreen.getPlayer().b2body.setTransform(pc.position.x - pc.getBounds().width/2/MyGdxGame.PPM + 20/MyGdxGame.PPM, pc.position.y - (pc.getBounds().height/2 + playScreen.getPlayer().b2body.getPosition().y) / MyGdxGame.PPM + 60/MyGdxGame.PPM, 0);
                                    playScreen.getPlayer().setCurrentState(Player.State.CODING);
                                    switch (pc.getPcType()) {
                                        case 0: //normal pc
                                            Cameras.setCameraTo(pc.getBounds().x / MyGdxGame.PPM + 300/MyGdxGame.PPM);
                                            playScreen.getHud().showEditorWindow(pc);
                                            if (playScreen.getPlayer().b2body.getPosition().y > Cameras.playScreenCam.position.y)
                                                playScreen.getHud().setEditorWindowBot();
                                            break;
                                        case 1://quiz pc
                                            Cameras.setCameraTo(pc.getBounds().x / MyGdxGame.PPM + 500/MyGdxGame.PPM);
                                            playScreen.getHud().showEditorQuizWindow(pc);
                                            break;
                                        case 2://order pc
                                            Cameras.setCameraTo(pc.getBounds().x / MyGdxGame.PPM + 500/MyGdxGame.PPM);
                                            playScreen.getHud().showEditorOrderWindow(pc);
                                            break;
                                    }
                                }
                            }
                            for (InfoSign infoSign : playScreen.getObjectManager().getInfoSigns()) {
                                if (infoSign.isUsable()) {
                                    playScreen.getPlayer().b2body.setLinearVelocity(0, 0);
                                    playScreen.getPlayer().b2body.setTransform(infoSign.getBounds().x / MyGdxGame.PPM + 34/MyGdxGame.PPM, (infoSign.getBounds().y + playScreen.getPlayer().b2body.getPosition().y) / MyGdxGame.PPM + 60/MyGdxGame.PPM, 0);
                                    playScreen.getPlayer().setCurrentState(Player.State.READING);
                                    playScreen.getHud().showInfoWindow(infoSign, infoSign.getText());
                                }
                            }
                            for (Quiz quiz : playScreen.getObjectManager().getQuizes()) {
                                for (FloatingPlatform floatingPlatform : quiz.getFloatingPlatforms()) {
                                    if (floatingPlatform.getLever().isUsable()) {
                                        playScreen.getPlayer().b2body.setLinearVelocity(0, 0);
                                        playScreen.getPlayer().b2body.setTransform(floatingPlatform.getLever().getB2body().getPosition().x - floatingPlatform.getLever().getBounds().width / 2 / MyGdxGame.PPM + 47.8f/MyGdxGame.PPM,
                                                floatingPlatform.getLever().getB2body().getPosition().y + 16/MyGdxGame.PPM, 0);
                                        quiz.pull(floatingPlatform);
                                        if (floatingPlatform.isCorrect()) {
                                            playScreen.getPlayer().showPlayerMsg(playScreen.getAssets().playscreenBundle.get("correct"));
                                            playScreen.getHud().hideUseBtn();
                                        }
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
        else { //toast next
            playScreen.getPlayer().setRunRight(false);
            playScreen.getPlayer().setRunLeft(false);
            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || Gdx.input.justTouched())
                playScreen.getHud().handleToastNextPressed();
        }

        playScreen.setEnterKeyHandled(false);
        //back button
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            if (playScreen.getPlayer().getCurrentState() == Player.State.CODING)
                playScreen.getHud().closeEditors();
            else if (playScreen.getPlayer().getCurrentState() == Player.State.READING)
                playScreen.getHud().closeCurrentInfo();
            else {
                if(playScreen.getPlayer().getCurrentState() != Player.State.DISAPPEARED && playScreen.getPlayer().getCurrentState() != Player.State.DEAD)
                    playScreen.getHud().showPauseWindow();
            }
        }
    }
}
