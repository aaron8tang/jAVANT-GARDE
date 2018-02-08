package com.steveflames.javantgarde.hud.navigation_windows;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.steveflames.javantgarde.MyGdxGame;
import com.steveflames.javantgarde.screens.ChooseLevelScreen;
import com.steveflames.javantgarde.screens.PlayScreen;
import com.steveflames.javantgarde.tools.Assets;
import com.steveflames.javantgarde.tools.global.Cameras;

/**
 * This class implements the in-game Pause screen.
 */

public class PauseWindow extends Window {

    private PlayScreen playScreen;
    private Assets assets;

    public PauseWindow(final PlayScreen playScreen) {
        super(playScreen.getAssets().playscreenBundle.get("paused"), playScreen.getAssets().neonSkin, "window2");
        this.assets = playScreen.getAssets();
        this.playScreen = playScreen;

        recreateUI();
    }

    public void recreateUI() {
        this.setSize(800,500);
        this.setPosition(Cameras.hudPort.getCamera().viewportWidth/2 - this.getWidth()/2, Cameras.hudPort.getCamera().viewportHeight/2 - this.getHeight()/2);

        TextButton resumeBtn = new TextButton(assets.playscreenBundle.get("resume"), playScreen.getAssets().neonSkin);
        resumeBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playScreen.getAssets().playSound(playScreen.getAssets().clickSound);
                remove();
            }
        });
        TextButton restartBtn = new TextButton(assets.playscreenBundle.get("restart_level"), playScreen.getAssets().neonSkin);
        restartBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playScreen.getAssets().playSound(playScreen.getAssets().clickSound);
                playScreen.setRestartLevel();
                playScreen.dispose();
                playScreen.getGame().setScreen(new PlayScreen(playScreen.getGame(), playScreen.getCurrentLevel(), playScreen.getMap(), playScreen.getRenderer()));
            }
        });
        TextButton exitBtn = new TextButton(assets.playscreenBundle.get("exit"), playScreen.getAssets().neonSkin);
        exitBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playScreen.getAssets().playSound(playScreen.getAssets().clickSound);
                playScreen.dispose();
                playScreen.getGame().setScreen(new ChooseLevelScreen(playScreen.getGame()));
            }
        });

        this.add(resumeBtn).width(300).height(100);
        this.row();
        this.add(restartBtn).width(300).height(100);
        this.row().padTop(70);
        this.add(exitBtn).width(300).height(100);
    }

    public void handleExitFromPauseMenuInput() {
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            assets.playSound(assets.clickSound);
            this.remove();
            playScreen.getHud().showAndroidInputTable();
        }
    }
}
