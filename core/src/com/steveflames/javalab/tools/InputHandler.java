package com.steveflames.javalab.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.steveflames.javalab.MyGdxGame;
import com.steveflames.javalab.scenes.Toast;
import com.steveflames.javalab.screens.PlayScreen;
import com.steveflames.javalab.screens.Window;
import com.steveflames.javalab.sprites.InfoSign;
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

    public void handlePlayscreenInput() {
        if(!playScreen.getHud().getCurrentToast().isShowing() || playScreen.getHud().getCurrentToast() == null) {
            if(playScreen.getPlayer().currentState != Player.State.CODING && playScreen.getPlayer().currentState != Player.State.READING && playScreen.getPlayer().currentState != Player.State.DEAD) {
                //move player on key pressed
                if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
                    playScreen.getPlayer().jump();
                }
                else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && playScreen.getPlayer().b2body.getLinearVelocity().x <= Player.PLAYERSPEED*10) {
                    playScreen.getPlayer().runRight();
                }
                else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && playScreen.getPlayer().b2body.getLinearVelocity().x >= -Player.PLAYERSPEED*10) {
                    playScreen.getPlayer().runLeft();
                }

                //use item
                if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                    if(Player.colliding) {
                        System.out.println(playScreen.isEnterKeyHandled());
                        if (!playScreen.isEnterKeyHandled()) {
                            for (Pc pc : playScreen.getPcs()) {
                                if (pc.isUsable()) {
                                    playScreen.getPlayer().b2body.setLinearVelocity(0, 0);
                                    playScreen.getPlayer().b2body.setTransform(pc.getBounds().x / MyGdxGame.PPM + 0.1f, (pc.getBounds().y + playScreen.getPlayer().b2body.getPosition().y) / MyGdxGame.PPM + 0.3f, 0);
                                    playScreen.getPlayer().currentState = Player.State.CODING;
                                    Window.getCam().position.x = pc.getBounds().x / MyGdxGame.PPM + 1.5f;
                                    playScreen.getHud().newEditorWindow(pc.getName().substring(3));
                                }
                            }
                            for (InfoSign infoSign : playScreen.getInfoSigns()) {
                                if (infoSign.isUsable()) {
                                    playScreen.getPlayer().b2body.setLinearVelocity(0, 0);
                                    playScreen.getPlayer().b2body.setTransform(infoSign.getBounds().x / MyGdxGame.PPM + 0.17f, (infoSign.getBounds().y + playScreen.getPlayer().b2body.getPosition().y) / MyGdxGame.PPM + 0.3f, 0);
                                    playScreen.getPlayer().currentState = Player.State.READING;
                                    playScreen.getHud().newInfoWindow(infoSign.getName());
                                }
                            }
                        }
                    }
                }

                if(playScreen.getPlayer().currentState != Player.State.CODING)
                    playScreen.updateCameraPosition();
            }
            else if(playScreen.getPlayer().currentState == Player.State.DEAD)
                playScreen.getHud().newGameOverWindow();
        }
        else if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            if(playScreen.getHud().getCurrentToast().getCurrentState() == Toast.State.READY)
                playScreen.getHud().getCurrentToast().setCurrentState(Toast.State.NEXT);
            else if (playScreen.getHud().getCurrentToast().getCurrentState() == Toast.State.WRITING)
                playScreen.getHud().getCurrentToast().setCurrentState(Toast.State.SKIP);
        }
        playScreen.setEnterKeyHandled(false);
    }

    public void handlePlayscreenAndroidInput() {
        if(!playScreen.getHud().getCurrentToast().isShowing() || playScreen.getHud().getCurrentToast() == null) {
            if (playScreen.getPlayer().currentState != Player.State.CODING) {
                if (playScreen.getHud().isRightBtnPressed()) {
                    if (playScreen.getPlayer().b2body.getLinearVelocity().x <= Player.PLAYERSPEED * 10)
                        playScreen.getPlayer().runRight();
                }
                else if (playScreen.getHud().isLeftBtnPressed()) {
                    if (playScreen.getPlayer().b2body.getLinearVelocity().x >= -Player.PLAYERSPEED * 10)
                        playScreen.getPlayer().runLeft();
                }

                if (playScreen.getHud().isJumpBtnPressed()) {
                    playScreen.getHud().setJumpBtnPressed(false);
                    playScreen.getPlayer().jump();
                }
                else if (playScreen.getHud().isUseBtnPressed()) {
                    playScreen.getHud().setUseBtnPressed(false);
                    if (Player.colliding) {
                        if (!playScreen.isEnterKeyHandled()) {
                            for (Pc pc : playScreen.getPcs()) {
                                if (pc.isUsable()) {
                                    playScreen.getPlayer().b2body.setLinearVelocity(0, 0);
                                    playScreen.getPlayer().b2body.setTransform(pc.getBounds().x / MyGdxGame.PPM + 0.1f, (pc.getBounds().y + playScreen.getPlayer().b2body.getPosition().y) / MyGdxGame.PPM + 0.3f, 0);
                                    playScreen.getPlayer().currentState = Player.State.CODING;
                                    Window.getCam().position.x = pc.getBounds().x / MyGdxGame.PPM + 1.5f;
                                    playScreen.getHud().newEditorWindow(pc.getName().substring(3));
                                }
                            }
                            for (InfoSign infoSign : playScreen.getInfoSigns()) {
                                if (infoSign.isUsable()) {
                                    playScreen.getPlayer().b2body.setLinearVelocity(0, 0);
                                    playScreen.getPlayer().b2body.setTransform(infoSign.getBounds().x / MyGdxGame.PPM + 0.17f, (infoSign.getBounds().y + playScreen.getPlayer().b2body.getPosition().y) / MyGdxGame.PPM + 0.3f, 0);
                                    playScreen.getPlayer().currentState = Player.State.READING;
                                    playScreen.getHud().newInfoWindow(infoSign.getName());
                                }
                            }
                        }
                    }
                }

                playScreen.updateCameraPosition();
            }
            if (playScreen.getPlayer().currentState == Player.State.DEAD)
                playScreen.getHud().newGameOverWindow();
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
    }
}
