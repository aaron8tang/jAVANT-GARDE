package com.steveflames.javantgarde.hud.navigation_windows;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.steveflames.javantgarde.screens.ChooseLevelScreen;
import com.steveflames.javantgarde.screens.PlayScreen;
import com.steveflames.javantgarde.tools.global.Cameras;

/**
 * This class implements the screen that pops up when
 * the player is out of lives.
 */

public class GameOverWindow extends Table {

    public GameOverWindow(final PlayScreen playScreen) {
        recreateUI(playScreen);
    }

    public void recreateUI(final PlayScreen playScreen) {
        this.setSize(400,260);
        this.setPosition(Cameras.hudPort.getCamera().viewportWidth/2 - 200, Cameras.hudPort.getCamera().viewportHeight/2 - 130);

        Label gameOverLabel = new Label(playScreen.getAssets().playscreenBundle.get("game_over"), playScreen.getAssets().neonSkin, "big");
        gameOverLabel.scaleBy(1.4f, 1.4f);
        this.add(gameOverLabel).top().expandX().padTop(5);
        this.row();

        Table optionsTable = new Table(playScreen.getAssets().neonSkin);
        TextButton tryAgainBtn = new TextButton(" "+playScreen.getAssets().playscreenBundle.get("restart")+" ", playScreen.getAssets().neonSkin);
        tryAgainBtn.addListener(new ClickListener() {
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
                playScreen.getGame().setScreen(new ChooseLevelScreen(playScreen.getGame()));
                playScreen.dispose();
            }
        });
        optionsTable.padTop(20);
        optionsTable.add(tryAgainBtn).left().expand().padRight(20).width(200).height(100);
        optionsTable.add(exitBtn).right().expand().padLeft(20).width(200).height(100);
        this.add(optionsTable).expand().fill();
    }

}
