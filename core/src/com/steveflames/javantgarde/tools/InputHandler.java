package com.steveflames.javantgarde.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.steveflames.javantgarde.MyGdxGame;
import com.steveflames.javantgarde.quests.Quiz;
import com.steveflames.javantgarde.screens.PlayScreen;
import com.steveflames.javantgarde.sprites.FloatingPlatform;
import com.steveflames.javantgarde.sprites.InfoSign;
import com.steveflames.javantgarde.sprites.Pc;

/**
 * Created by Flames on 29/10/2017.
 */

public class InputHandler {

    private PlayScreen playScreen;

    public InputHandler(PlayScreen playScreen) {
        this.playScreen = playScreen;
    }

    public void handleInput() {
        if (MyGdxGame.platformDepended.deviceHasKeyboard()) {
            if (!playScreen.getHud().getCurrentToast().isShowing() || playScreen.getHud().getCurrentToast() == null) {
                if (playScreen.getPlayer().canMove) {
                    //move player on key press
                    if (Gdx.input.isKeyJustPressed(Input.Keys.UP))
                        playScreen.getPlayer().jump();
                    else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && playScreen.getPlayer().b2body.getLinearVelocity().x <= com.steveflames.javantgarde.sprites.Player.PLAYERSPEED * 10)
                        playScreen.getPlayer().runRight();
                    else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && playScreen.getPlayer().b2body.getLinearVelocity().x >= -com.steveflames.javantgarde.sprites.Player.PLAYERSPEED * 10)
                        playScreen.getPlayer().runLeft();

                    //use item
                    if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                        if (com.steveflames.javantgarde.sprites.Player.colliding) {
                            if (!playScreen.isEnterKeyHandled()) {
                                for (Pc pc : playScreen.getPcs()) {
                                    if (pc.isUsable()) {
                                        playScreen.getPlayer().setCoding(pc.getBounds());
                                        PlayScreen.setCameraTo(pc.getBounds().x / MyGdxGame.PPM + 1.5f);
                                        playScreen.getHud().showEditorWindow(pc);
                                        if (playScreen.getPlayer().b2body.getPosition().y > PlayScreen.cam.position.y)
                                            playScreen.getHud().getEditorWindow().getQuestWindow().setPosition(0, 45);
                                    }
                                }
                                for (InfoSign infoSign : playScreen.getInfoSigns()) {
                                    if (infoSign.isUsable()) {
                                        playScreen.getPlayer().b2body.setLinearVelocity(0, 0);
                                        playScreen.getPlayer().b2body.setTransform(infoSign.getBounds().x / MyGdxGame.PPM + 0.17f, (infoSign.getBounds().y + playScreen.getPlayer().b2body.getPosition().y) / MyGdxGame.PPM + 0.3f, 0);
                                        playScreen.getPlayer().setCurrentState(com.steveflames.javantgarde.sprites.Player.State.READING);
                                        playScreen.getHud().showInfoWindow(infoSign.getName(), infoSign.getText());
                                    }
                                }
                                for (Quiz quiz : playScreen.getQuizes()) {
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
            }
            else if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER))
                playScreen.getHud().getCurrentToast().handleNextPressed();
        }
        else {
            if(!playScreen.getHud().getCurrentToast().isShowing() || playScreen.getHud().getCurrentToast() == null) {
                if (playScreen.getPlayer().canMove) {
                    if (playScreen.getHud().isRightBtnPressed()) {
                        if (playScreen.getPlayer().b2body.getLinearVelocity().x <= com.steveflames.javantgarde.sprites.Player.PLAYERSPEED * 10)
                            playScreen.getPlayer().runRight();
                    }
                    else if (playScreen.getHud().isLeftBtnPressed()) {
                        if (playScreen.getPlayer().b2body.getLinearVelocity().x >= -com.steveflames.javantgarde.sprites.Player.PLAYERSPEED * 10)
                            playScreen.getPlayer().runLeft();
                    }

                    if (playScreen.getHud().isJumpBtnPressed()) {
                        playScreen.getHud().setJumpBtnPressed();
                        playScreen.getPlayer().jump();
                    }
                    else if (playScreen.getHud().isUseBtnPressed()) {
                        playScreen.getHud().setUseBtnPressed();
                        if (com.steveflames.javantgarde.sprites.Player.colliding) {
                            if (!playScreen.isEnterKeyHandled()) {
                                for (Pc pc : playScreen.getPcs()) {
                                    if (pc.isUsable()) {
                                        playScreen.getPlayer().setCoding(pc.getBounds());
                                        playScreen.getHud().showEditorWindow(pc);
                                        PlayScreen.setCameraTo(pc.getBounds().x / MyGdxGame.PPM + 1.5f);
                                        if(playScreen.getPlayer().b2body.getPosition().y > PlayScreen.cam.position.y)
                                            playScreen.getHud().getEditorWindow().getQuestWindow().setPosition(0, 45);
                                    }
                                }
                                for (InfoSign infoSign : playScreen.getInfoSigns()) {
                                    if (infoSign.isUsable()) {
                                        playScreen.getPlayer().b2body.setLinearVelocity(0, 0);
                                        playScreen.getPlayer().b2body.setTransform(infoSign.getBounds().x / MyGdxGame.PPM + 0.17f, (infoSign.getBounds().y + playScreen.getPlayer().b2body.getPosition().y) / MyGdxGame.PPM + 0.3f, 0);
                                        playScreen.getPlayer().setCurrentState(com.steveflames.javantgarde.sprites.Player.State.READING);
                                        playScreen.getHud().showInfoWindow(infoSign.getName(), infoSign.getText());
                                    }
                                }
                                for (Quiz quiz : playScreen.getQuizes()) {
                                    for (FloatingPlatform floatingPlatform : quiz.getFloatingPlatforms()) {
                                        if (floatingPlatform.getLever().isUsable()) {
                                            playScreen.getPlayer().b2body.setLinearVelocity(0, 0);
                                            playScreen.getPlayer().b2body.setTransform(floatingPlatform.getLever().getB2body().getPosition().x - floatingPlatform.getLever().getBounds().width/2/MyGdxGame.PPM + 0.239f,
                                                    floatingPlatform.getLever().getB2body().getPosition().y + 0.08f, 0);
                                            quiz.pull(floatingPlatform);
                                            if(floatingPlatform.isCorrect())
                                                playScreen.getPlayer().showPlayerMsg("correct!");
                                            playScreen.getHud().hideUseBtn();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            else if(Gdx.input.justTouched())
                    playScreen.getHud().getCurrentToast().handleNextPressed();
        }

        //COMMON
        playScreen.setEnterKeyHandled(false);

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            if (playScreen.getPlayer().getCurrentState() == com.steveflames.javantgarde.sprites.Player.State.CODING)
                playScreen.getHud().getEditorWindow().closeCurrentEditor();
            else if (playScreen.getPlayer().getCurrentState() == com.steveflames.javantgarde.sprites.Player.State.READING)
                playScreen.getHud().closeCurrentInfo();
            else if (playScreen.getHud().isPauseWindowShowing())
                playScreen.getHud().getPauseWindow().remove();
            else
                playScreen.getHud().showPauseWindow();
        }
    }
}
