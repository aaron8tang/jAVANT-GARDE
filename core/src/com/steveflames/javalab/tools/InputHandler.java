package com.steveflames.javalab.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.steveflames.javalab.MyGdxGame;
import com.steveflames.javalab.scenes.Toast;
import com.steveflames.javalab.screens.PlayScreen;
import com.steveflames.javalab.sprites.FloatingPlatform;
import com.steveflames.javalab.sprites.InfoSign;
import com.steveflames.javalab.sprites.Lever;
import com.steveflames.javalab.sprites.Pc;
import com.steveflames.javalab.sprites.Player;

/**
 * Created by Flames on 29/10/2017.
 */

public class InputHandler {

    private PlayScreen playScreen;

    public InputHandler(PlayScreen playScreen) {
        this.playScreen = playScreen;
    }

    public void handlePlayscreenInput(float dt) {
        if(!playScreen.getHud().getCurrentToast().isShowing() || playScreen.getHud().getCurrentToast() == null) {
            if(playScreen.getPlayer().canMove) {
                //move player on key press
                if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
                    playScreen.getPlayer().jump();
                }
                else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && playScreen.getPlayer().b2body.getLinearVelocity().x <= Player.PLAYERSPEED*10) {
                    playScreen.getPlayer().runRight(dt);
                }
                else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && playScreen.getPlayer().b2body.getLinearVelocity().x >= -Player.PLAYERSPEED*10) {
                    playScreen.getPlayer().runLeft(dt);
                }

                //use item
                if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                    if(Player.colliding) {
                        if (!playScreen.isEnterKeyHandled()) {
                            for (Pc pc : playScreen.getPcs()) {
                                if (pc.isUsable()) {
                                    playScreen.getPlayer().setCoding(pc.getBounds());
                                    PlayScreen.setCameraTo(pc.getBounds().x / MyGdxGame.PPM + 1.5f, -1);
                                    playScreen.getHud().showEditorWindow(pc);
                                    if(playScreen.getPlayer().b2body.getPosition().y > PlayScreen.cam.position.y)
                                        playScreen.getHud().getEditorWindow().getQuestWindow().setPosition(0, 45);
                                }
                            }
                            for (InfoSign infoSign : playScreen.getInfoSigns()) {
                                if (infoSign.isUsable()) {
                                    playScreen.getPlayer().b2body.setLinearVelocity(0, 0);
                                    playScreen.getPlayer().b2body.setTransform(infoSign.getBounds().x / MyGdxGame.PPM + 0.17f, (infoSign.getBounds().y + playScreen.getPlayer().b2body.getPosition().y) / MyGdxGame.PPM + 0.3f, 0);
                                    playScreen.getPlayer().setCurrentState(Player.State.READING);
                                    playScreen.getHud().showInfoWindow(infoSign.getName(), infoSign.getText());
                                }
                            }
                            if(playScreen.getQuiz()!=null) {
                                for (FloatingPlatform floatingPlatform : playScreen.getQuiz().getFloatingPlatforms()) {
                                    if (floatingPlatform.getLever().isUsable()) {
                                        playScreen.getPlayer().b2body.setLinearVelocity(0, 0);
                                        playScreen.getPlayer().b2body.setTransform(floatingPlatform.getLever().getB2body().getPosition().x - floatingPlatform.getLever().getBounds().width/2/MyGdxGame.PPM + 0.239f,
                                                floatingPlatform.getLever().getB2body().getPosition().y + 0.08f, 0);
                                        playScreen.getQuiz().pull(floatingPlatform);
                                        if(floatingPlatform.isCorrect())
                                            playScreen.getPlayer().showPlayerMsg("correct!");
                                    }
                                }
                            }
                        }
                    }
                }

                if(playScreen.getPlayer().getCurrentState() != Player.State.CODING)
                    playScreen.updateCameraPosition();
            }
            else if(playScreen.getPlayer().getCurrentState() == Player.State.DEAD)
                playScreen.getHud().showGameOverWindow();
        }
        else if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            if(playScreen.getHud().getCurrentToast().getCurrentState() == Toast.State.READY)
                playScreen.getHud().getCurrentToast().setCurrentState(Toast.State.NEXT);
            else if (playScreen.getHud().getCurrentToast().getCurrentState() == Toast.State.WRITING)
                playScreen.getHud().getCurrentToast().setCurrentState(Toast.State.SKIP);
        }
        playScreen.setEnterKeyHandled(false);

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            if(playScreen.getPlayer().getCurrentState() == Player.State.CODING)
                playScreen.getHud().getEditorWindow().closeCurrentEditor();
            else if(playScreen.getPlayer().getCurrentState() == Player.State.READING)
                playScreen.getHud().closeCurrentInfo();
            else if(playScreen.getHud().isPauseWindowShowing())
                playScreen.getHud().getPauseWindow().remove();
            else
                playScreen.getHud().showPauseWindow();
        }
    }

    public void handlePlayscreenAndroidInput() {
        if(!playScreen.getHud().getCurrentToast().isShowing() || playScreen.getHud().getCurrentToast() == null) {
            if (playScreen.getPlayer().canMove) {
                if (playScreen.getHud().isRightBtnPressed()) {
                    if (playScreen.getPlayer().b2body.getLinearVelocity().x <= Player.PLAYERSPEED * 10)
                        playScreen.getPlayer().runRight(0.1f);
                }
                else if (playScreen.getHud().isLeftBtnPressed()) {
                    if (playScreen.getPlayer().b2body.getLinearVelocity().x >= -Player.PLAYERSPEED * 10)
                        playScreen.getPlayer().runLeft(0.1f);
                }

                if (playScreen.getHud().isJumpBtnPressed()) {
                    playScreen.getHud().setJumpBtnPressed();
                    playScreen.getPlayer().jump();
                }
                else if (playScreen.getHud().isUseBtnPressed()) {
                    playScreen.getHud().setUseBtnPressed();
                    if (Player.colliding) {
                        if (!playScreen.isEnterKeyHandled()) {
                            for (Pc pc : playScreen.getPcs()) {
                                if (pc.isUsable()) {
                                    playScreen.getPlayer().setCoding(pc.getBounds());
                                    playScreen.getHud().showEditorWindow(pc);
                                    PlayScreen.setCameraTo(pc.getBounds().x / MyGdxGame.PPM + 1.5f, -1);
                                    if(playScreen.getPlayer().b2body.getPosition().y > PlayScreen.cam.position.y)
                                        playScreen.getHud().getEditorWindow().getQuestWindow().setPosition(0, 45);
                                }
                            }
                            for (InfoSign infoSign : playScreen.getInfoSigns()) {
                                if (infoSign.isUsable()) {
                                    playScreen.getPlayer().b2body.setLinearVelocity(0, 0);
                                    playScreen.getPlayer().b2body.setTransform(infoSign.getBounds().x / MyGdxGame.PPM + 0.17f, (infoSign.getBounds().y + playScreen.getPlayer().b2body.getPosition().y) / MyGdxGame.PPM + 0.3f, 0);
                                    playScreen.getPlayer().setCurrentState(Player.State.READING);
                                    playScreen.getHud().showInfoWindow(infoSign.getName(), infoSign.getText());
                                }
                            }
                            if(playScreen.getQuiz()!=null) {
                                for (FloatingPlatform floatingPlatform : playScreen.getQuiz().getFloatingPlatforms()) {
                                    if (floatingPlatform.getLever().isUsable()) {
                                        playScreen.getPlayer().b2body.setLinearVelocity(0, 0);
                                        playScreen.getPlayer().b2body.setTransform(floatingPlatform.getLever().getB2body().getPosition().x - floatingPlatform.getLever().getBounds().width/2/MyGdxGame.PPM + 0.239f,
                                                floatingPlatform.getLever().getB2body().getPosition().y + 0.08f, 0);
                                        playScreen.getQuiz().pull(floatingPlatform);
                                        if(floatingPlatform.isCorrect())
                                            playScreen.getPlayer().showPlayerMsg("correct!");
                                        playScreen.getHud().hideUseBtn();
                                    }
                                }
                            }
                        }
                    }
                }

                if(playScreen.getPlayer().getCurrentState() != Player.State.CODING)
                    playScreen.updateCameraPosition();
            }
            if (playScreen.getPlayer().getCurrentState() == Player.State.DEAD)
                playScreen.getHud().showGameOverWindow();
        }
        else {
            if(Gdx.input.justTouched()) {
                if(playScreen.getHud().getCurrentToast().getCurrentState() == Toast.State.READY)
                    playScreen.getHud().getCurrentToast().setCurrentState(Toast.State.NEXT);
                else if (playScreen.getHud().getCurrentToast().getCurrentState() == Toast.State.WRITING)
                    playScreen.getHud().getCurrentToast().setCurrentState(Toast.State.SKIP);
            }
        }
        playScreen.setEnterKeyHandled(false);

        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            if(playScreen.getPlayer().getCurrentState() == Player.State.CODING)
                playScreen.getHud().getEditorWindow().closeCurrentEditor();
            else if(playScreen.getPlayer().getCurrentState() == Player.State.READING)
                playScreen.getHud().closeCurrentInfo();
            else if(playScreen.getHud().isPauseWindowShowing())
                playScreen.getHud().getPauseWindow().remove();
            else
                playScreen.getHud().showPauseWindow();
        }
    }
}
