package com.steveflames.javantgarde.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.steveflames.javantgarde.MyGdxGame;
import com.steveflames.javantgarde.tools.Assets;
import com.steveflames.javantgarde.tools.global.Fonts;

/**
 * Created by Flames on 19/12/2017.
 */

public class LoadingScreen extends Window implements Screen {

    private MyGdxGame game;
    private LevelListItem level;

    private ProgressBar progressBar;
    private String loadingString = "[RED]LOADING[]";

    private Stage stage;
    private Viewport viewport;

    private Assets assets;
    private boolean xPressed = false;
    private String levelName = "";
    private GlyphLayout glyphLayout;


    public LoadingScreen(final MyGdxGame game, LevelListItem levelListItem) {
        super("", game.assets.neonSkin);
        this.assets = game.assets;
        this.game = game;
        this.level = levelListItem;
        game.assets.loadAllPlayScreenAssets();
        glyphLayout = new GlyphLayout(Fonts.medium, loadingString);

        //loading table
        TextButton exitBtn = new TextButton("< BACK", game.assets.neonSkin);
        progressBar = new ProgressBar(0, 1, 1/game.assets.getQueuedAssets(), false, game.assets.neonSkin);
        Table table = new Table(game.assets.neonSkin);

        table.add(exitBtn).expandX().right().width(200).height(100);
        table.row().space(180);
        table.add(progressBar).expand().fillX().padLeft(400).padRight(100).height(200).top();

        viewport = new FitViewport(MyGdxGame.WIDTH, MyGdxGame.HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, game.sb);
        Gdx.input.setInputProcessor(stage);
        stage.addActor(this);

        //add components to window
        this.setSize(viewport.getCamera().viewportWidth, viewport.getCamera().viewportHeight);
        this.setX(0);
        this.setY(0);
        this.add(table).expand().fill();


        exitBtn.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                xPressed = true;
            }
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                xPressed = false;
            }
            @Override
            public void clicked(InputEvent event, float x, float y) {
                xPressed = true;
                assets.playSound(assets.clickSound);
                game.assets.unloadAllPlayScreenAssets();
                game.setScreen(new ChooseLevelScreen(game));
                dispose();
            }
        });
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        int i=0;
        game.sb.begin();
        for(String category : ChooseLevelScreen.categories.keySet()) {
            i++;
            Fonts.xsmall.setColor(Color.WHITE);
            Fonts.xsmall.draw(game.sb, category, 30, viewport.getCamera().viewportHeight - i*30);
            for(final LevelListItem level : ChooseLevelScreen.categories.get(category)) {
                i++;
                levelName = level.getName().replaceAll("\n", " ");
                if(this.level.getName().equals(level.getName()))
                    levelName = "[GREEN]"+levelName+"[]";
                Fonts.xsmall.draw(game.sb, levelName, 80, viewport.getCamera().viewportHeight - i*30);
            }
        }
        Fonts.medium.draw(game.sb, loadingString, 780 - glyphLayout.width/2, 450);
        game.sb.end();


        stage.act(delta);
        stage.draw();
        game.sb.setColor(1,1,1,1);

        if(game.assets.update()) {
            progressBar.setValue(game.assets.getProgress());
            if(MyGdxGame.platformDepended.deviceHasKeyboard()) {
                loadingString = "[GREEN]Press ENTER to begin![]";
                glyphLayout = new GlyphLayout(Fonts.medium, loadingString);
                if(Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
                    assets.playSound(assets.clickSound);
                    dispose();
                    game.setScreen(new PlayScreen(game, level));
                }
            }
            else {
                loadingString = "[GREEN]Tap to begin![]";
                glyphLayout = new GlyphLayout(Fonts.medium, loadingString);
                if(!xPressed && Gdx.input.isTouched()) {
                    assets.playSound(assets.clickSound);
                    dispose();
                    game.setScreen(new PlayScreen(game, level));
                }
            }
        }
        else {
            if(game.assets.getProgress() > 0.5f)
                loadingString = "[YELLOW]LOADING[]";
            progressBar.setValue(game.assets.getProgress());
        }

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {
        game.assets.unloadAllMainMenuAssets();
    }

    @Override
    public void resume() {
        game.assets.loadAllMainMenuAssets();
        game.assets.finishLoading();
        game.assets.refreshMainMenuAssets();
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
