package com.steveflames.javantgarde.hud.navigation_windows;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.steveflames.javantgarde.MyGdxGame;
import com.steveflames.javantgarde.screens.ChooseLevelScreen;
import com.steveflames.javantgarde.screens.PlayScreen;
import com.steveflames.javantgarde.tools.Assets;

/**
 * Created by Flames on 10/11/2017.
 */

public class PauseWindow extends Window {

    private Sound clickSound;

    public PauseWindow(String title, final PlayScreen playScreen) {
        super(title, playScreen.getAssets().getTerraSkin());
        clickSound = playScreen.getAssets().get(Assets.clickSOUND, Sound.class);
        Skin neonSkin = playScreen.getAssets().getNeonSkin();

        this.setSize(800,500);
        this.setPosition(MyGdxGame.WIDTH/2 - this.getWidth()/2, MyGdxGame.HEIGHT/2 - this.getHeight()/2);

        TextButton resumeBtn = new TextButton("RESUME", neonSkin);
        resumeBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play();
                remove();
            }
        });
        TextButton restartBtn = new TextButton("RESTART LEVEL", neonSkin);
        restartBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play();
                playScreen.setRestartLevel();
                playScreen.dispose();
                playScreen.getGame().setScreen(new PlayScreen(playScreen.getGame(), playScreen.getCurrentLevel()));
            }
        });
        TextButton exitBtn = new TextButton("EXIT", neonSkin);
        exitBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play();
                playScreen.dispose();
                playScreen.getGame().setScreen(new ChooseLevelScreen(playScreen.getGame()));
            }
        });

        this.add(resumeBtn).width(240).height(100);
        this.row();
        this.add(restartBtn).width(240).height(100);
        this.row().padTop(70);
        this.add(exitBtn).width(240).height(100);
    }

    public void handleExitFromPauseMenuInput() {
        if(MyGdxGame.platformDepended.deviceHasKeyboard()) {
            if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                clickSound.play();
                this.remove();
            }
        }
        else {
            if(Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
                clickSound.play();
                this.remove();
            }
        }
    }
}
