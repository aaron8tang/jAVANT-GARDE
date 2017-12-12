package com.steveflames.javantgarde.hud.navigation;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.steveflames.javantgarde.MyGdxGame;
import com.steveflames.javantgarde.screens.ChooseLevelScreen;
import com.steveflames.javantgarde.screens.PlayScreen;
import com.steveflames.javantgarde.tools.global.Skins;

/**
 * Created by Flames on 10/11/2017.
 */

public class GameOverWindow extends Table {

    public GameOverWindow(Skin skin, final PlayScreen playScreen) {
        super(skin);
        this.setSize(400,260);
        this.setPosition(MyGdxGame.WIDTH/2 - 200, MyGdxGame.HEIGHT/2 - 130);

        Label gameOverLabel = new Label("GAME OVER", Skins.lmlSkin);
        gameOverLabel.scaleBy(1.4f, 1.4f);
        this.add(gameOverLabel).top().expandX().padTop(5);
        this.row();

        Table optionsTable = new Table(Skins.neonSkin);
        TextButton tryAgainBtn = new TextButton(" RESTART ", Skins.neonSkin);
        tryAgainBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playScreen.dispose();
                playScreen.getGame().setScreen(new PlayScreen(playScreen.getGame(), playScreen.getCurrentLevel()));
            }
        });
        TextButton exitBtn = new TextButton(" EXIT ", Skins.neonSkin);
        exitBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
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
