package com.steveflames.javantgarde.hud;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.steveflames.javantgarde.MyGdxGame;
import com.steveflames.javantgarde.tools.global.Skins;

/**
 * Created by Flames on 10/11/2017.
 */

public class ConsoleWindow extends Window {

    private Label consoleTextArea;
    private ScrollPane consoleScroll;

    ConsoleWindow(String title, Skin skin, final Stage stage) {
        super(title, skin);

        //textArea of console window
        Table table = new Table(Skins.neonSkin);
        consoleTextArea = new Label("", Skins.skin);
        consoleTextArea.setWrap(true);
        //consoleTextArea.setDisabled(true);
        table.add(consoleTextArea).expand().fillX().left().top().padLeft(5);

        consoleScroll = new ScrollPane(table, Skins.neonSkin);
        //scroll.setFadeScrollBars(false);
        if(MyGdxGame.platformDepended.deviceHasKeyboard())
            consoleScroll.setFlickScroll(false);

        //add components to window
        this.setSize(700, 190);
        this.setX(MyGdxGame.WIDTH - this.getWidth());
        this.setY(0);
        this.add(consoleScroll).expand().fill().top().left().padTop(5);
        this.addListener(new ClickListener() {
            public void enter (InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                stage.setScrollFocus(consoleTextArea);
            }
        });
    }

    public Label getConsoleTextArea() {
        return consoleTextArea;
    }

    ScrollPane getConsoleScroll() {
        return consoleScroll;
    }


    void show(Stage stage) {
        stage.addActor(this);
    }
}
