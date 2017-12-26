package com.steveflames.javantgarde.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.steveflames.javantgarde.MyGdxGame;
import com.steveflames.javantgarde.tools.Assets;
import com.steveflames.javantgarde.tools.global.Fonts;

/**
 * Created by Flames on 25/12/2017.
 */

public class MainMenuScreen implements Screen {

    private MyGdxGame game;
    private Viewport viewport;
    private Stage stage;
    private Sound clickSound;
    private GlyphLayout glyphLayout;

    private Label musicLabel;
    private Label sfxLabel;

    public MainMenuScreen(final MyGdxGame game) {
        this.game = game;
        clickSound = game.assets.get(Assets.clickSOUND, Sound.class);
        viewport = new StretchViewport(MyGdxGame.WIDTH, MyGdxGame.HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, game.sb);
        Skin neonSkin = game.assets.getNeonSkin();
        Skin terraSkin = game.assets.getTerraSkin();
        Skin lmlSkin = game.assets.getLmlSkin();
        glyphLayout = new GlyphLayout(Fonts.big, MyGdxGame.TITLE);

        if(!game.mainMenuMusic.isPlaying())
            game.mainMenuMusic.play();

        //window
        Window window = new Window("", terraSkin);
        window.setFillParent(true);
        window.top();

        Table midTable = new Table(neonSkin);

        //audioTable
        Image audioImg = new Image(game.assets.get(Assets.audioT, Texture.class));
        Table audioTable = new Table(neonSkin);
        Table table = new Table(neonSkin);
        Button musicBtn = new Button(lmlSkin);
        if(MyGdxGame.musicOn)
            musicLabel = new Label("[GREEN]Music: ON[]   ", terraSkin);
        else
            musicLabel = new Label("[RED]Music: OFF[]   ", terraSkin);
        musicBtn.add(musicLabel).expand().height(70).right();
        Button sfxBtn = new Button(lmlSkin);
        if(MyGdxGame.sfxOn)
            sfxLabel = new Label("[GREEN]Sound Effects: ON[]   ", terraSkin);
        else
            sfxLabel = new Label("[RED]Sound Effects: OFF[]   ", terraSkin);
        sfxBtn.add(sfxLabel).expand().height(70).right();
        table.add(musicBtn).expand().fill();
        table.row();
        table.add(sfxBtn).expand().fill();
        audioTable.add(audioImg).padLeft(40);
        audioTable.add(table).expand().fill();

        //playBtn
        Button playBtn = new Button(neonSkin);
        Image playImg = new Image(game.assets.get(Assets.playT, Texture.class));
        playBtn.add(playImg).expand().left().padLeft(30);
        Label playLabel = new Label("Play      ", neonSkin);
        playBtn.add(playLabel).expand().center();

        //aboutBtn
        Button aboutBtn = new Button(neonSkin);
        Image aboutImg = new Image(game.assets.get(Assets.aboutUpT, Texture.class));
        aboutBtn.add(aboutImg).expand().left().padLeft(30);
        //ImageButton aboutBtn = new ImageButton(new TextureRegionDrawable(new TextureRegion(game.assets.get(Assets.aboutUpT, Texture.class))), new TextureRegionDrawable(new TextureRegion(game.assets.get(Assets.aboutDownT, Texture.class))));
        //aboutTable.add(aboutBtn).expand().top();
        Label aboutLabel = new Label("About     ", neonSkin);
        aboutBtn.add(aboutLabel).expand().center();


        midTable.add(playBtn).width(500).height(200);
        midTable.row();
        midTable.add(aboutBtn).width(500).height(200);
        midTable.row();
        midTable.add(audioTable).width(500).height(160);



        window.add(midTable).expand().bottom().padBottom(30);

        //add window to stage
        stage.addActor(window);
        Gdx.input.setInputProcessor(stage);


        //ADD LISTENERS
        playBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play();
                game.setScreen(new ChooseLevelScreen(game));
                dispose();
            }
        });
        aboutBtn.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play();
            }
        });
        musicBtn.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play();
                if(MyGdxGame.musicOn) {
                    musicLabel.setText(musicLabel.getText().replace("GREEN", "RED"));
                    musicLabel.setText(musicLabel.getText().replace("ON", "OFF"));
                    MyGdxGame.musicOn=false;
                    game.assets.unloadMainMenuMusic();
                }
                else {
                    musicLabel.setText(musicLabel.getText().replace("RED", "GREEN"));
                    musicLabel.setText(musicLabel.getText().replace("OFF", "ON"));
                    MyGdxGame.musicOn=true;
                    game.assets.loadMainMenuMusic();
                    game.assets.finishLoading();
                }
                musicLabel.layout();
            }
        });
        sfxBtn.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play();
                if(MyGdxGame.sfxOn) {
                    sfxLabel.setText(sfxLabel.getText().replace("GREEN", "RED"));
                    sfxLabel.setText(sfxLabel.getText().replace("ON", "OFF"));
                    MyGdxGame.sfxOn = false;
                    game.assets.unloadMainMenuSounds();
                }
                else {
                    sfxLabel.setText(sfxLabel.getText().replace("RED", "GREEN"));
                    sfxLabel.setText(sfxLabel.getText().replace("OFF", "ON"));
                    MyGdxGame.sfxOn = true;
                    game.assets.loadMainMenuSounds();
                    game.assets.finishLoading();
                }
                sfxLabel.layout();
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
        handleInput();

        game.sb.begin();
        Fonts.big.setColor(Color.RED);
        Fonts.big.draw(game.sb, MyGdxGame.TITLE, MyGdxGame.WIDTH/2 - glyphLayout.width/2, MyGdxGame.HEIGHT-60);
        game.sb.end();
    }

    private void handleInput() {
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            Gdx.app.exit();
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {
        game.mainMenuMusic.pause();
    }

    @Override
    public void resume() {
        game.mainMenuMusic.play();
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
