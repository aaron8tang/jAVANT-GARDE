package com.steveflames.javantgarde.hud.navigation_windows;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.steveflames.javantgarde.screens.ChooseLevelScreen;
import com.steveflames.javantgarde.screens.LoadingScreen;
import com.steveflames.javantgarde.screens.PlayScreen;
import com.steveflames.javantgarde.tools.global.Cameras;

/**
 * This class implements the screen that pops up
 * when the player completes a level.
 */

public class LevelCompletedWindow extends Table {


    public LevelCompletedWindow(final PlayScreen playScreen) {
        super(playScreen.getAssets().neonSkin);

        recreateUI(playScreen);
    }

    public void recreateUI(final PlayScreen playScreen) {
        this.setSize(400,260);
        this.setPosition(Cameras.hudPort.getCamera().viewportWidth/2 - 200, Cameras.hudPort.getCamera().viewportHeight/2 - 110);

        Label levelCompletedLabel = new Label(playScreen.getAssets().playscreenBundle.get("level_completed"), playScreen.getAssets().neonSkin, "big");
        levelCompletedLabel.scaleBy(1.2f, 1.2f);
        this.add(levelCompletedLabel).top().expandX().padTop(5);
        this.row();

        Table optionsTable = new Table(playScreen.getAssets().neonSkin);
        TextButton restartBtn = new TextButton(" "+playScreen.getAssets().playscreenBundle.get("restart")+" ", playScreen.getAssets().neonSkin);
        restartBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playScreen.getAssets().playSound(playScreen.getAssets().clickSound);
                playScreen.setRestartLevel();
                playScreen.dispose();
                playScreen.getGame().setScreen(new PlayScreen(playScreen.getGame(), playScreen.getCurrentLevel(), playScreen.getMap(), playScreen.getRenderer()));
            }
        });
        TextButton exitBtn = new TextButton(" "+playScreen.getAssets().playscreenBundle.get("exit")+" ", playScreen.getAssets().neonSkin);
        exitBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playScreen.getAssets().playSound(playScreen.getAssets().clickSound);
                playScreen.dispose();
                playScreen.getGame().setScreen(new ChooseLevelScreen(playScreen.getGame()));

            }
        });
        TextButton nextLvlBtn = new TextButton(" "+playScreen.getAssets().playscreenBundle.get("next")+" ", playScreen.getAssets().neonSkin);
        nextLvlBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playScreen.getAssets().playSound(playScreen.getAssets().clickSound);
                playScreen.setRestartLevel();
                playScreen.dispose();
                playScreen.getGame().setScreen(new LoadingScreen(playScreen.getGame(), ChooseLevelScreen.getNextLevel(playScreen.getCurrentLevel())));
            }
        });
        optionsTable.padTop(20);
        optionsTable.add(restartBtn).left().expand().width(200).height(100);
        optionsTable.add(exitBtn).left().expand().padLeft(50).width(200).height(100);
        optionsTable.add(nextLvlBtn).left().expand().padLeft(50).width(200).height(100);
        this.add(optionsTable).expand().fill();
    }
}
