package com.steveflames.javantgarde.hud.navigation_windows;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.steveflames.javantgarde.MyGdxGame;
import com.steveflames.javantgarde.screens.ChooseLevelScreen;
import com.steveflames.javantgarde.screens.LoadingScreen;
import com.steveflames.javantgarde.screens.PlayScreen;

/**
 * Created by Flames on 11/11/2017.
 */

public class LevelCompletedWindow extends Table {

    public LevelCompletedWindow(Skin neonSkin, Skin lmlSkin,  final PlayScreen playScreen) {
        super(neonSkin);
        this.setSize(400,260);
        this.setPosition(MyGdxGame.WIDTH/2 - 200, MyGdxGame.HEIGHT/2 - 110);

        Label levelCompletedLabel = new Label("LEVEL COMPLETED", lmlSkin);
        levelCompletedLabel.scaleBy(1.2f, 1.2f);
        this.add(levelCompletedLabel).top().expandX().padTop(5);
        this.row();

        Table optionsTable = new Table(neonSkin);
        TextButton restartBtn = new TextButton(" RESTART ", neonSkin);
        restartBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playScreen.setRestartLevel();
                playScreen.dispose();
                playScreen.getGame().setScreen(new PlayScreen(playScreen.getGame(), playScreen.getCurrentLevel()));
            }
        });
        TextButton exitBtn = new TextButton(" EXIT ", neonSkin);
        exitBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playScreen.getGame().setScreen(new ChooseLevelScreen(playScreen.getGame()));
                playScreen.dispose();
            }
        });
        TextButton nextLvlBtn = new TextButton(" NEXT ", neonSkin);
        nextLvlBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
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
