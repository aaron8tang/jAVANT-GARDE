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
    private Skin neonSkin;

    public AndroidExtraKeyboardWindow(String title, Skin neonSkin, Skin terraSkin, final EditorWindow editorWindow) {
        super(title, terraSkin);
        this.neonSkin = neonSkin;
        this.setSize(700,85);
        this.setX(MyGdxGame.WIDTH - this.getWidth());
        this.setY(MyGdxGame.HEIGHT - this.getHeight() - 10);

        Table table = new Table(neonSkin);
        TextButton tabBtn = new TextButton("TAB", neonSkin);
        tabBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                editorWindow.editorKeyTyped('\t');
            }
        });
        TextButton leftBracketBtn = new TextButton("{", neonSkin);
        leftBracketBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                editorWindow.virtualTypeKey('{');
            }
        });
        TextButton rightBracketBtn = new TextButton("}", neonSkin);
        rightBracketBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                editorWindow.virtualTypeKey('}');
                editorWindow.editorKeyTyped('}');
            }
        });
        TextButton leftParBtn = new TextButton("(", neonSkin);
        leftParBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                editorWindow.virtualTypeKey('(');
                editorWindow.editorKeyTyped('(');
            }
        });
        TextButton rightParBtn = new TextButton(")", neonSkin);
        rightParBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                editorWindow.virtualTypeKey(')');
                editorWindow.editorKeyTyped(')');
            }
        });
        TextButton semicolonBtn = new TextButton(";", neonSkin);
        semicolonBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                editorWindow.virtualTypeKey(';');
            }
        });
        TextButton singleQuoteBtn = new TextButton("'", neonSkin);
        singleQuoteBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                editorWindow.virtualTypeKey('\'');
                editorWindow.editorKeyTyped('\'');
            }
        });
        TextButton quoteBtn = new TextButton("\"", neonSkin);
        quoteBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                editorWindow.virtualTypeKey('"');
                editorWindow.editorKeyTyped('"');
            }
        });
        TextButton equalsBtn = new TextButton("=", neonSkin);
        equalsBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                editorWindow.virtualTypeKey('=');
            }
        });

        keyBoardBtn = new ImageButton(new TextureRegionDrawable(editorWindow.getHud().playScreen.getAssets().getTextureAtlas().findRegion(Assets.keyboardUpREGION)), new TextureRegionDrawable(editorWindow.getHud().playScreen.getAssets().getTextureAtlas().findRegion(Assets.keyboardDownREGION)), new TextureRegionDrawable(editorWindow.getHud().playScreen.getAssets().getTextureAtlas().findRegion(Assets.keyboardDownREGION)));
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
        ScrollPane scrollPane = new ScrollPane(table, neonSkin);

        this.add(scrollPane).expand().fill();
        this.add(keyBoardBtn).right().width(90).height(70);
    }

    public AndroidExtraKeyboardWindow(String title, Skin neonSkin, Skin terraSkin, final EditorQuizWindow editorQuizWindow) {
        super(title, terraSkin);
        this.neonSkin = neonSkin;
        this.editorQuizWindow = editorQuizWindow;
        this.setSize(10,85);
        this.setX(editorQuizWindow.getX() - this.getWidth());
        this.setY(editorQuizWindow.getY() - this.getHeight() - 5);

        table = new Table(neonSkin);
        this.add(table).expand().fill();
    }

    public void addButton(final String text) {
        TextButton btn = new TextButton(text.substring(1), neonSkin);
        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
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
