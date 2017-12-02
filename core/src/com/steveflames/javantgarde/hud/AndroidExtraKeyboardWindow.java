package com.steveflames.javantgarde.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.steveflames.javantgarde.MyGdxGame;
import com.steveflames.javantgarde.tools.global.Loader;
import com.steveflames.javantgarde.tools.global.Skins;

/**
 * Created by Flames on 14/11/2017.
 */

class AndroidExtraKeyboardWindow extends Window {
    private boolean keyboardShown = false;
    private ImageButton keyBoardBtn;

    AndroidExtraKeyboardWindow(String title, Skin skin, final EditorWindow editorWindow) {
        super(title, skin);
        this.setSize(700,85);
        this.setX(MyGdxGame.WIDTH - this.getWidth());
        this.setY(MyGdxGame.HEIGHT - this.getHeight() - 10);

        Table table = new Table(Skins.neonSkin);
        TextButton tabBtn = new TextButton("TAB", Skins.neonSkin);
        tabBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                editorWindow.editorKeyTyped('\t');
            }
        });
        TextButton leftBracketBtn = new TextButton("{", Skins.neonSkin);
        leftBracketBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                editorWindow.virtualTypeKey('{');
            }
        });
        TextButton rightBracketBtn = new TextButton("}", Skins.neonSkin);
        rightBracketBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                editorWindow.virtualTypeKey('}');
                editorWindow.editorKeyTyped('}');
            }
        });
        TextButton leftParBtn = new TextButton("(", Skins.neonSkin);
        leftParBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                editorWindow.virtualTypeKey('(');
                editorWindow.editorKeyTyped('(');
            }
        });
        TextButton rightParBtn = new TextButton(")", Skins.neonSkin);
        rightParBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                editorWindow.virtualTypeKey(')');
            }
        });
        TextButton semicolonBtn = new TextButton(";", Skins.neonSkin);
        semicolonBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                editorWindow.virtualTypeKey(';');
            }
        });
        TextButton singleQuoteBtn = new TextButton("'", Skins.neonSkin);
        singleQuoteBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                editorWindow.virtualTypeKey('\'');
                editorWindow.editorKeyTyped('\'');
            }
        });
        TextButton quoteBtn = new TextButton("\"", Skins.neonSkin);
        quoteBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                editorWindow.virtualTypeKey('"');
                editorWindow.editorKeyTyped('"');
            }
        });
        TextButton equalsBtn = new TextButton("=", Skins.neonSkin);
        equalsBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                editorWindow.virtualTypeKey('=');
            }
        });

        keyBoardBtn = new ImageButton(new TextureRegionDrawable(Loader.keyboardUpTR), new TextureRegionDrawable(Loader.keyboardDownTR), new TextureRegionDrawable(Loader.keyboardDownTR));
        keyBoardBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                keyboardShown = !keyboardShown;
                setKeyboardShown(keyboardShown);
            }
        });
        table.add(leftBracketBtn).height(69).width(100).left();
        table.add(rightBracketBtn).height(69).width(100).left();
        table.add(leftParBtn).height(69).width(100).left();
        table.add(rightParBtn).height(69).width(100).left();
        table.add(equalsBtn).height(69).width(100).left();
        table.add(semicolonBtn).height(69).width(100).left();
        table.add(singleQuoteBtn).height(69).width(100).left();
        table.add(quoteBtn).height(69).width(100).left();
        table.add(tabBtn).height(69).width(100).left();
        ScrollPane scrollPane = new ScrollPane(table, Skins.neonSkin);


        this.add(scrollPane).expand().fill();
        this.add(keyBoardBtn).right().width(90).height(70);
    }

    void show(Stage stage) {
        stage.addActor(this);
    }

    public boolean isKeyboardShown() {
        return keyboardShown;
    }

    public void setKeyboardShown(boolean keyboardShown) {
        Gdx.input.setOnscreenKeyboardVisible(keyboardShown);
        keyBoardBtn.setChecked(keyboardShown);
        this.keyboardShown = keyboardShown;
    }
}
