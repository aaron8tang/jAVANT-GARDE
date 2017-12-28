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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextTooltip;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.steveflames.javantgarde.MyGdxGame;
import com.steveflames.javantgarde.tools.Assets;
import com.steveflames.javantgarde.tools.global.Fonts;
import com.steveflames.javantgarde.tools.global.MyFileReader;

/**
 * Created by Flames on 25/12/2017.
 */

public class MainMenuScreen implements Screen {

    private MyGdxGame game;
    private Viewport viewport;
    private Stage stage;
    private Stage dialogStage;
    private GlyphLayout glyphLayout;
    private String aboutString;

    private Label musicLabel;
    private Label sfxLabel;

    private Table audioTable;
    private Button playBtn;
    private Button aboutBtn;

    private boolean backBtnPressed = false;


    public MainMenuScreen(final MyGdxGame game) {
        this.game = game;
        viewport = new FitViewport(MyGdxGame.WIDTH, MyGdxGame.HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, game.sb);
        dialogStage = new Stage(viewport, game.sb);
        glyphLayout = new GlyphLayout(Fonts.big, MyGdxGame.TITLE);
        aboutString = MyFileReader.readFile("txt/about.txt");
        Gdx.input.setCatchBackKey(true);
        game.assets.loadAllMainMenuAssets();
        game.assets.finishLoading();
        game.assets.refreshMainMenuAssets();
        game.assets.playMusic(game.assets.mainMenuMusic);

        //window
        Window window = new Window("", game.assets.terraSkin);
        window.setFillParent(true);
        window.top();

        Table midTable = new Table(game.assets.neonSkin);

        //audioTable
        audioTable = new Table(game.assets.neonSkin);
        Table table = new Table(game.assets.neonSkin);
        Button musicBtn = new Button(game.assets.lmlSkin);
        if(game.preferences.isMusicEnabled())
            musicLabel = new Label("[GREEN]Music: ON[]   ", game.assets.terraSkin);
        else
            musicLabel = new Label("[RED]Music: OFF[]   ", game.assets.terraSkin);
        musicBtn.add(musicLabel).expand().height(70).right();
        Button sfxBtn = new Button(game.assets.lmlSkin);
        if(game.preferences.isSoundEffectsEnabled())
            sfxLabel = new Label("[GREEN]Sound Effects: ON[]   ", game.assets.terraSkin);
        else
            sfxLabel = new Label("[RED]Sound Effects: OFF[]   ", game.assets.terraSkin);
        sfxBtn.add(sfxLabel).expand().height(70).right();
        table.add(musicBtn).expand().fill();
        table.row();
        table.add(sfxBtn).expand().fill();
        audioTable.add(table).expand().fill();

        //playBtn
        playBtn = new Button(game.assets.neonSkin);
        Label playLabel = new Label("Play", game.assets.neonSkin);
        playBtn.add(playLabel).expand().center();

        //aboutBtn
        aboutBtn = new Button(game.assets.neonSkin);
        //ImageButton aboutBtn = new ImageButton(new TextureRegionDrawable(new TextureRegion(game.assets.get(Assets.aboutUpT, Texture.class))), new TextureRegionDrawable(new TextureRegion(game.assets.get(Assets.aboutDownT, Texture.class))));
        //aboutTable.add(aboutBtn).expand().top();
        Label aboutLabel = new Label("About", game.assets.neonSkin);
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
                game.assets.playSound(game.assets.clickSound);
                game.setScreen(new ChooseLevelScreen(game));
                dispose();
            }
        });
        aboutBtn.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.assets.playSound(game.assets.clickSound);
                Gdx.input.setInputProcessor(dialogStage);
                Dialog infoDialog = new Dialog("ABOUT", game.assets.terraSkin, "dialog") {
                    public void result(Object obj) {
                        game.assets.playSound(game.assets.clickSound);
                        this.remove();
                        Gdx.input.setInputProcessor(stage);
                        backBtnPressed = true;
                    }
                };
                TextButton dummy = new TextButton("", game.assets.neonSkin);
                infoDialog.button("     BACK     ", true, dummy.getStyle()).setHeight(100); //sends "true" as the result
                infoDialog.key(Input.Keys.ENTER, true); //sends "true" when the ENTER key is pressed
                infoDialog.key(Input.Keys.BACK, true);
                infoDialog.key(Input.Keys.ESCAPE, true);
                infoDialog.text(aboutString);
                infoDialog.show(dialogStage);
            }
        });
        musicBtn.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.assets.playSound(game.assets.clickSound);
                if(MyGdxGame.musicOn) {
                    musicLabel.setText(musicLabel.getText().replace("GREEN", "RED"));
                    musicLabel.setText(musicLabel.getText().replace("ON", "OFF"));
                    game.preferences.setMusicEnabled(false);
                    game.assets.pauseMusic(game.assets.mainMenuMusic);
                    MyGdxGame.musicOn=false;
                    game.assets.unloadMainMenuMusic();
                }
                else {
                    musicLabel.setText(musicLabel.getText().replace("RED", "GREEN"));
                    musicLabel.setText(musicLabel.getText().replace("OFF", "ON"));
                    game.preferences.setMusicEnabled(true);
                    MyGdxGame.musicOn=true;
                    game.assets.loadMainMenuMusic();
                    game.assets.finishLoading();
                    game.assets.refreshMainMenuAssets();
                    game.assets.playMusic(game.assets.mainMenuMusic);
                }
                musicLabel.layout();
            }
        });
        sfxBtn.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.assets.playSound(game.assets.clickSound);
                if(MyGdxGame.sfxOn) {
                    sfxLabel.setText(sfxLabel.getText().replace("GREEN", "RED"));
                    sfxLabel.setText(sfxLabel.getText().replace("ON", "OFF"));
                    game.preferences.setSoundEffectsEnabled(false);
                    MyGdxGame.sfxOn = false;
                    game.assets.unloadMainMenuSounds();
                }
                else {
                    sfxLabel.setText(sfxLabel.getText().replace("RED", "GREEN"));
                    sfxLabel.setText(sfxLabel.getText().replace("OFF", "ON"));
                    game.preferences.setSoundEffectsEnabled(true);
                    MyGdxGame.sfxOn = true;
                    game.assets.loadMainMenuSounds();
                    game.assets.finishLoading();
                    game.assets.refreshMainMenuAssets();
                    game.assets.playSound(game.assets.clickSound);
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
        stage.act(delta);
        stage.draw();
        handleInput();

        game.sb.begin();
        game.sb.draw(game.assets.audioT, audioTable.localToStageCoordinates(new Vector2(0,0)).x + 30, audioTable.localToStageCoordinates(new Vector2(0,0)).y+ 20);
        game.sb.draw(game.assets.playT, playBtn.localToStageCoordinates(new Vector2(0,0)).x + 30, playBtn.localToStageCoordinates(new Vector2(0,0)).y+ 35);
        game.sb.draw(game.assets.aboutUpT, aboutBtn.localToStageCoordinates(new Vector2(0,0)).x + 30, aboutBtn.localToStageCoordinates(new Vector2(0,0)).y+ 35);
        Fonts.big.setColor(Color.RED);
        Fonts.big.draw(game.sb, MyGdxGame.TITLE, MyGdxGame.WIDTH/2 - glyphLayout.width/2, MyGdxGame.HEIGHT-60);
        game.sb.end();

        dialogStage.act(delta);
        dialogStage.draw();
        backBtnPressed = false;
    }

    private void handleInput() {
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            if(!backBtnPressed)
                Gdx.app.exit();
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {
        game.assets.pauseMusic(game.assets.mainMenuMusic);
        game.assets.unloadAllMainMenuAssets();
    }

    @Override
    public void resume() {
        game.assets.loadAllMainMenuAssets();
        game.assets.finishLoading();
        game.assets.refreshMainMenuAssets();
        game.assets.playMusic(game.assets.mainMenuMusic);
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        dialogStage.dispose();
    }
}
