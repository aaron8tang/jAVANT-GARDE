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
        float queuedAssets = game.assets.getQueuedAssets();
        glyphLayout = new GlyphLayout(Fonts.medium, loadingString);

        //loading table
        TextButton exitBtn = new TextButton("< BACK", game.assets.neonSkin);
        progressBar = new ProgressBar(0, queuedAssets, 1/queuedAssets, false, game.assets.neonSkin);
        Table table = new Table(game.assets.neonSkin);


        table.add(exitBtn).expandX().right().width(200).height(100);
        table.row().space(180);
        table.add(progressBar).expand().fillX().padLeft(400).padRight(100).height(200).top();


        //add components to window
        this.setSize(MyGdxGame.WIDTH, MyGdxGame.HEIGHT);
        this.setX(0);
        this.setY(0);
        this.add(table).expand().fill();

        viewport = new StretchViewport(MyGdxGame.WIDTH, MyGdxGame.HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, game.sb);
        Gdx.input.setInputProcessor(stage);
        stage.addActor(this);

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
            Fonts.xsmall.draw(game.sb, category, 30, MyGdxGame.HEIGHT - i*30);
            for(final LevelListItem level : ChooseLevelScreen.categories.get(category)) {
                i++;
                levelName = level.getName().replaceAll("\n", " ");
                if(this.level.getName().equals(level.getName()))
                    levelName = "[GREEN]"+levelName+"[]";
                Fonts.xsmall.draw(game.sb, levelName, 80, MyGdxGame.HEIGHT - i*30);
            }
        }
        Fonts.medium.draw(game.sb, loadingString, 780 - glyphLayout.width/2, 450);
        game.sb.end();


        stage.act(delta);
        stage.draw();
        game.sb.setColor(1,1,1,1);

        if(game.assets.update()) {
            if(MyGdxGame.platformDepended.deviceHasKeyboard()) {
                loadingString = "[GREEN]Press ENTER to begin![]";
                glyphLayout = new GlyphLayout(Fonts.medium, loadingString);
                if(Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
                    assets.playSound(assets.clickSound);
                    game.setScreen(new PlayScreen(game, level));
                    dispose();
                    game.assets.refreshPlayScreenAssets();
                }
            }
            else {
                loadingString = "[GREEN]Tap to begin![]";
                glyphLayout = new GlyphLayout(Fonts.medium, loadingString);
                if(!xPressed && Gdx.input.isTouched()) {
                    assets.playSound(assets.clickSound);
                    game.setScreen(new PlayScreen(game, level));
                }
            }
        }
        else {
            if(game.assets.getLoadedAssets() > game.assets.getQueuedAssets()/2)
                loadingString = "[YELLOW]LOADING[]";
            progressBar.setValue(game.assets.getLoadedAssets());
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
