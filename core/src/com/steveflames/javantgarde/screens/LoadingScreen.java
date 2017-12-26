package com.steveflames.javantgarde.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.steveflames.javantgarde.MyGdxGame;
import com.steveflames.javantgarde.tools.Assets;

/**
 * Created by Flames on 19/12/2017.
 */

public class LoadingScreen extends Window implements Screen {

    private MyGdxGame game;
    private LevelListItem level;

    private ProgressBar progressBar;
    private Label loadingLabel;

    private Stage stage;
    private Viewport viewport;

    private Sound clickSound;


    public LoadingScreen(final MyGdxGame game, LevelListItem levelListItem) {
        super("", game.assets.getNeonSkin());
        clickSound = game.assets.get(Assets.clickSOUND, Sound.class);
        this.game = game;
        this.level = levelListItem;
        game.assets.loadAllPlayScreenAssets();
        float queuedAssets = game.assets.getQueuedAssets();

        //loading table
        TextButton exitBtn = new TextButton("x", game.assets.getNeonSkin());
        loadingLabel = new Label("LOADING", game.assets.getLmlSkin());
        progressBar = new ProgressBar(0, queuedAssets, 1/queuedAssets, false, game.assets.getNeonSkin());
        Table table = new Table(game.assets.getNeonSkin());


        table.add(exitBtn).expandX().right();
        table.row();
        table.add(loadingLabel).expand().bottom();
        table.row();
        table.add(progressBar).expand().fillX().padLeft(200).padRight(200).height(200).top();


        //add components to window
        this.setSize(MyGdxGame.WIDTH, MyGdxGame.HEIGHT);
        this.setX(0);
        this.setY(0);
        this.row();
        this.add(table).expand().fill();

        viewport = new StretchViewport(MyGdxGame.WIDTH, MyGdxGame.HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, game.sb);
        Gdx.input.setInputProcessor(stage);
        stage.addActor(this);

        exitBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play();
                game.assets.unloadAllPlayScreenAssets();
                game.setScreen(new ChooseLevelScreen(game));
            }
        });
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
        game.sb.setColor(1,1,1,1);


        if(game.assets.update()) {
            if(MyGdxGame.platformDepended.deviceHasKeyboard()) {
                loadingLabel.setText("Press ENTER to begin!");
                if(Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
                    clickSound.play();
                    game.setScreen(new PlayScreen(game, level));
                }
            }
            else {
                loadingLabel.setText("Tap to begin!");
                if(Gdx.input.isTouched()) {
                    clickSound.play();
                    game.setScreen(new PlayScreen(game, level));
                }
            }
        }
        else {
            progressBar.setValue(game.assets.getLoadedAssets());
        }

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
