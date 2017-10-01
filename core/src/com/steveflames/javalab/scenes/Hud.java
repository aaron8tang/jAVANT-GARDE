package com.steveflames.javalab.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.steveflames.javalab.MyGdxGame;
import com.steveflames.javalab.tools.Fonts;
import com.steveflames.javalab.screens.PlayScreen;
import com.steveflames.javalab.sprites.Pc;
import com.steveflames.javalab.sprites.Player;


/**
 * Created by Flames on 9/4/16.
 */
public class Hud implements Disposable {
    public Stage stage;
    private Viewport viewport;

    private PlayScreen level;

    private Toast currentToast = null;
    private Editor currentEditor = null;

    public Hud(PlayScreen level, SpriteBatch sb) {
        this.level = level;

        viewport = new StretchViewport(MyGdxGame.WIDTH, MyGdxGame.HEIGHT, new OrthographicCamera());


        //Skin skin = new Skin(Gdx.files.internal("skins/lml/skin/skin.json"));
        //skin.getFont("font-label").setFixedWidthGlyphs("qwertyuiop[]asdfghjkl;'zxcvbnm,./`1234567890-=\\QWERTYUIOP{}ASDFGHJKL:\"ZXCVBNM<>?~!@#$%^&*()_+");

        stage = new Stage(viewport, sb);
        /*Window container = new Window("PC", skin);
        container.setSize(710,555);

        TextArea textArea = new TextArea(
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.\n"
                        + "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.\n"
                        + "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.\n"
                        + "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.\n"
                        + "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.\n"
                        + "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.\n"
                        + "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.\n"
                        + "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.\n"
                        + "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.\n"
                        + "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.\n"
                        + "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.\n"
                        + "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.\n"
                        + "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.\n"
                        + "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.\n"
                        + "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.\n"
                        + "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.\n"
                        + "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.\n"
                        + "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.\n"
                        + "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.\n"
                        + "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.\n"
                        + "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.\n"
                        + "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.\n"
                        + "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.\n"
                        + "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.\n",
                skin);

        Table codeTable = new Table(skin);
        codeTable.add(textArea).width(600).height(2000);

        ScrollPane scroll = new ScrollPane(codeTable, skin);


        Table lineNumTable = new Table(skin);
        for(int i=0; i<textArea.getLines(); i++) {
            lineNumTable.add(new TextButton(i+"", skin)).height(18f);
            lineNumTable.row();
        }


        container.add(lineNumTable).left().top().expandX();
        container.add(scroll).expandX();
        stage.addActor(container);
        Gdx.input.setInputProcessor(stage);
        */
    }

    public void update(float dt) {
        if(currentToast != null) {
            currentToast.update(dt);
            if(currentToast.getCurrentState() == Toast.State.LEFT)
                currentToast = null;
        }
        if(currentEditor != null) {
            currentEditor.update(dt);
            if(currentEditor.getCurrentState() == Editor.State.LEFT)
                currentEditor = null;
        }
    }

    public void render(SpriteBatch sb, ShapeRenderer sr) {
        sb.setProjectionMatrix(stage.getCamera().combined);
        sr.setProjectionMatrix(stage.getCamera().combined);

        sr.begin(ShapeRenderer.ShapeType.Filled);
        if(level.getPlayer().getHealth() > 0) {
            for(int i=0; i<level.getPlayer().getHealth(); i++) {
                sr.setColor(Color.BLACK);
                sr.rect(20 +(60*i), stage.getCamera().viewportHeight - 20 - 29, 54, 29);
                sr.setColor(Color.RED);
                sr.rect(22 +(60*i), stage.getCamera().viewportHeight - 20 - 27, 50, 25);
            }
        }
        sr.end();

        if(currentToast != null)
            currentToast.render(sb, sr);
        if(currentEditor != null)
            currentEditor.render(sb, sr);

        for(Pc pc: level.getPcs()) { //draw the 'use PC' prompt
            if(pc.isUsable()) {
                if(level.getPlayer().currentState != Player.State.CODING) {
                    sb.setProjectionMatrix(sr.getProjectionMatrix());
                    sb.begin();
                    Fonts.medium.setColor(Color.RED);
                    Fonts.medium.draw(sb, "!", pc.getBounds().x + pc.getBounds().width / 2 - 10 - com.steveflames.javalab.Window.getCam().position.x * MyGdxGame.PPM + com.steveflames.javalab.Window.getCam().viewportWidth / 2 * MyGdxGame.PPM, pc.getBounds().y + pc.getBounds().height + 50);
                    sb.end();
                    break;
                }
            }
        }

    }

    public void newToast(String text) {
        currentToast = new Toast(text, stage.getCamera());
    }

    public void newEditor() {
        currentEditor = new Editor(stage.getCamera());
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public Toast getCurrentToast() {
        return currentToast;
    }

    public Editor getCurrentEditor() {
        return currentEditor;
    }

    public void closeCurrentEditor() {
        currentEditor = null;
        Gdx.input.setInputProcessor(level);
    }
}
