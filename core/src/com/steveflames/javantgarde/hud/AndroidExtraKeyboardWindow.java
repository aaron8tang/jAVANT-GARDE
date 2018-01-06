package com.steveflames.javantgarde.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.steveflames.javantgarde.hud.code_pc.EditorWindow;
import com.steveflames.javantgarde.hud.quiz_pc.EditorQuizWindow;
import com.steveflames.javantgarde.tools.Assets;
import com.steveflames.javantgarde.tools.global.Cameras;

/**
 * Created by Flames on 14/11/2017.
 */

public class AndroidExtraKeyboardWindow extends Window {
    private boolean keyboardShown = false;
    private ImageButton keyBoardBtn;
    private Table table;
    private EditorQuizWindow editorQuizWindow;

    private Assets assets;

    public AndroidExtraKeyboardWindow(String title, final Assets assets, final EditorWindow editorWindow) {
        super(title, assets.terraSkin);
        this.assets = assets;
        this.setSize(700,85);
        this.setX(Cameras.hudPort.getCamera().viewportWidth - this.getWidth());
        this.setY(Cameras.hudPort.getCamera().viewportHeight - this.getHeight() - 10);

        Table table = new Table(assets.neonSkin);
        TextButton tabBtn = new TextButton("TAB", assets.neonSkin);
        tabBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                assets.playSound(assets.clickSound);
                editorWindow.editorKeyTyped('\t');
            }
        });
        TextButton leftBracketBtn = new TextButton("{", assets.neonSkin);
        leftBracketBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                assets.playSound(assets.clickSound);
                editorWindow.virtualTypeKey('{');
            }
        });
        TextButton rightBracketBtn = new TextButton("}", assets.neonSkin);
        rightBracketBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                assets.playSound(assets.clickSound);
                editorWindow.virtualTypeKey('}');
                editorWindow.editorKeyTyped('}');
            }
        });
        TextButton leftParBtn = new TextButton("(", assets.neonSkin);
        leftParBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                assets.playSound(assets.clickSound);
                editorWindow.virtualTypeKey('(');
                editorWindow.editorKeyTyped('(');
            }
        });
        TextButton rightParBtn = new TextButton(")", assets.neonSkin);
        rightParBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                assets.playSound(assets.clickSound);
                editorWindow.virtualTypeKey(')');
                editorWindow.editorKeyTyped(')');
            }
        });
        TextButton semicolonBtn = new TextButton(";", assets.neonSkin);
        semicolonBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                assets.playSound(assets.clickSound);
                editorWindow.virtualTypeKey(';');
            }
        });
        TextButton singleQuoteBtn = new TextButton("'", assets.neonSkin);
        singleQuoteBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                assets.playSound(assets.clickSound);
                editorWindow.virtualTypeKey('\'');
                editorWindow.editorKeyTyped('\'');
            }
        });
        TextButton quoteBtn = new TextButton("\"", assets.neonSkin);
        quoteBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                assets.playSound(assets.clickSound);
                editorWindow.virtualTypeKey('"');
                editorWindow.editorKeyTyped('"');
            }
        });
        TextButton equalsBtn = new TextButton("=", assets.neonSkin);
        equalsBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                assets.playSound(assets.clickSound);
                editorWindow.virtualTypeKey('=');
            }
        });

        keyBoardBtn = new ImageButton(new TextureRegionDrawable(editorWindow.getHud().playScreen.getAssets().keyboardUpTR),
                new TextureRegionDrawable(editorWindow.getHud().playScreen.getAssets().keyboardDownTR),
                new TextureRegionDrawable(editorWindow.getHud().playScreen.getAssets().keyboardDownTR));
        keyBoardBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                assets.playSound(assets.clickSound);
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
        ScrollPane scrollPane = new ScrollPane(table, assets.neonSkin);

        this.add(scrollPane).expand().fill();
        this.add(keyBoardBtn).right().width(90).height(70);
    }

    public AndroidExtraKeyboardWindow(String title, Assets assets, final EditorQuizWindow editorQuizWindow) {
        super(title, assets.terraSkin);
        this.assets = assets;
        this.editorQuizWindow = editorQuizWindow;
        this.setSize(10,85);
        this.setX(editorQuizWindow.getX() - this.getWidth());
        this.setY(editorQuizWindow.getY() - this.getHeight() - 5);

        table = new Table(assets.neonSkin);
        this.add(table).expand().fill();
    }

    public void addButton(final String text) {
        TextButton btn = new TextButton(text.substring(1), assets.neonSkin);
        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                assets.playSound(assets.clickSound);
                editorQuizWindow.btnPressed(text);
            }
        });
        if(text.length()>10) {
            table.add(btn).height(80).width(360);
            this.setWidth(10 + table.getChildren().size*360);
        }
        else {
            table.add(btn).height(80).width(140);
            this.setWidth(10 + table.getChildren().size*140);
        }

        this.setX(Cameras.hudPort.getCamera().viewportWidth/2 - this.getWidth()/2);
    }

    public void clearButtons() {
        this.setWidth(10);
        this.setX(editorQuizWindow.getX() - this.getWidth());
        table.clear();
    }

    public void show(Stage stage) {
        stage.addActor(this);
    }

    public void setKeyboardShown(boolean keyboardShown) {
        Gdx.input.setOnscreenKeyboardVisible(keyboardShown);
        keyBoardBtn.setChecked(keyboardShown);
        this.keyboardShown = keyboardShown;
    }
}
