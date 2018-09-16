package com.steveflames.javantgarde.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.steveflames.javantgarde.MyGdxGame;
import com.steveflames.javantgarde.tools.global.Fonts;
import com.steveflames.javantgarde.tools.global.MyFileReader;

/**
 * The Screen of the Main Menu. This is the first screen
 * that the player sees when he runs the game.
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
    private Button optionsBtn;
    private Button aboutBtn;
    private SelectBox<String> languageSb;

    private boolean backBtnPressed = false;
    private int aboutPressedCounter = 0; //trick to unlock all levels


    public MainMenuScreen(final MyGdxGame game) {
        this.game = game;
        viewport = new FitViewport(MyGdxGame.WIDTH, MyGdxGame.HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, game.sb);
        dialogStage = new Stage(viewport, game.sb);
        glyphLayout = new GlyphLayout();
        Fonts.changeFont(game.preferences.getLanguage());
        Gdx.input.setCatchBackKey(true);

        game.assets.loadAllMainMenuAssets();
        game.assets.finishLoading();
        game.assets.refreshMainMenuAssets();
        game.gameMinimized = false;
        game.assets.playMusic(game.assets.mainMenuMusic);
        recreateUI();


        //fixes html sound delay bug
        if(Gdx.app.getType()== Application.ApplicationType.WebGL) { //web specific
            for(int i=0; i<6; i++)
                game.assets.playAllMenuSoundsMuted();
        }
    }

    private void recreateUI() {
        game.assets.loadMainMenuBundles(game.preferences.getLanguage());
        glyphLayout.setText(Fonts.big, MyGdxGame.TITLE);
        aboutString = MyFileReader.readFile("txt/"+Fonts.languageShort+"/about.txt");

        //window
        Window window = new Window("", game.assets.neonSkin, "window2");
        window.setFillParent(true);
        window.top().left();

        Table midTable = new Table(game.assets.neonSkin);

        //audioTable
        audioTable = new Table(game.assets.neonSkin);
        Table table = new Table(game.assets.neonSkin);
        Button musicBtn = new Button(game.assets.neonSkin, "button2");
        if(game.preferences.isMusicEnabled())
            musicLabel = new Label("[GREEN]"+game.assets.mainMenuBundle.get("music_on")+"[]   ", game.assets.neonSkin);
        else
            musicLabel = new Label("[RED]"+game.assets.mainMenuBundle.get("music_off")+"[]   ", game.assets.neonSkin);
        musicBtn.add(musicLabel).expand().height(70).right();
        Button sfxBtn = new Button(game.assets.neonSkin, "button2");
        if(game.preferences.isSoundEffectsEnabled())
            sfxLabel = new Label("[GREEN]"+game.assets.mainMenuBundle.get("sound_effects_on")+"[]   ", game.assets.neonSkin);
        else
            sfxLabel = new Label("[RED]"+game.assets.mainMenuBundle.get("sound_effects_off")+"[]   ", game.assets.neonSkin);
        sfxBtn.add(sfxLabel).expand().height(70).right();
        table.add(musicBtn).expand().fill();
        table.row();
        table.add(sfxBtn).expand().fill();
        audioTable.add(table).expand().fill();

        //playBtn
        playBtn = new Button(game.assets.neonSkin);
        Label playLabel = new Label(game.assets.mainMenuBundle.get("play"), game.assets.neonSkin);
        playBtn.add(playLabel).expand().center();
/*

        //optionsBtn
        optionsBtn = new Button(game.assets.neonSkin);
        Label optionsLabel = new Label("Options", game.assets.neonSkin);
        optionsBtn.add(optionsLabel).expand().center();
*/

        //aboutBtn
        aboutBtn = new Button(game.assets.neonSkin);
        Label aboutLabel = new Label(game.assets.mainMenuBundle.get("about"), game.assets.neonSkin);
        aboutBtn.add(aboutLabel).expand().center();

        //language selectBox
        Table topTable = new Table(game.assets.neonSkin);
        languageSb = new SelectBox<String>(game.assets.neonSkin);
        languageSb.setItems("English", "Ελληνικά");
        languageSb.setSelected(game.preferences.getLanguage());
        topTable.add(languageSb).expandX().left().top().width(200).height(70);

        //add to midTable
        midTable.add(playBtn).width(500).height(190);
        midTable.row();
        //midTable.add(optionsBtn).width(500).height(190);
        //midTable.row();
        midTable.add(aboutBtn).width(500).height(190);
        midTable.row();
        midTable.add(audioTable).width(500).height(160);

        //virtual keyboardBtn for html version
        final CheckBox keyboardChkBox;
        /*if(Gdx.app.getType()== Application.ApplicationType.WebGL) { //web specific
            keyboardChkBox = new CheckBox(game.assets.mainMenuBundle.get("virtual_keyboard"), game.assets.neonSkin);
            if(!MyGdxGame.platformDepended.deviceHasKeyboard())
                keyboardChkBox.setChecked(true);
            midTable.row();
            midTable.add(keyboardChkBox).padTop(10);
            keyboardChkBox.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    MyGdxGame.platformDepended.setDeviceHasKeyboard(!keyboardChkBox.isChecked());
                }
            });
        }
        else*/ if(Gdx.app.getType()== Application.ApplicationType.Desktop) { //desktop specific
                keyboardChkBox = new CheckBox(game.assets.mainMenuBundle.get("fullscreen"), game.assets.neonSkin);
            midTable.row();
            midTable.add(keyboardChkBox).padTop(10);
            if(Gdx.graphics.isFullscreen())
                keyboardChkBox.setChecked(true);
            keyboardChkBox.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if(keyboardChkBox.isChecked())
                        Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
                    else
                        Gdx.graphics.setWindowedMode(MyGdxGame.WIDTH, MyGdxGame.HEIGHT);
                }
            });
        }

        window.add(topTable).expandX().top().left().padLeft(64).padTop(5);
        window.row();
        window.add(midTable).expand().bottom().padBottom(30);

        //add window to stage
        stage.addActor(window);
        Gdx.input.setInputProcessor(stage);


        //ADD LISTENERS
        languageSb.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(!languageSb.getSelected().equals(game.preferences.getLanguage())) {
                    game.preferences.setLanguage(languageSb.getSelected());
                    for(Actor a: stage.getActors())
                        a.remove();
                    Fonts.changeFont(game.preferences.getLanguage());
                    game.assets.unloadSkins();
                    game.assets.loadSkins();
                    game.assets.finishLoading();
                    game.assets.refreshMainMenuAssets();
                    recreateUI();
                }
            }
        });
        playBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.assets.playSound(game.assets.clickSound);
                game.setScreen(new ChooseLevelScreen(game));
                dispose();
            }
        });
        /*optionsBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.assets.playSound(game.assets.clickSound);
                game.setScreen(new ChooseLevelScreen(game));
                dispose();
            }
        });*/
        aboutBtn.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.assets.playSound(game.assets.clickSound);
                Gdx.input.setInputProcessor(dialogStage);
                Dialog infoDialog = new Dialog(game.assets.mainMenuBundle.get("about"), game.assets.neonSkin, "dialog") {
                    public void result(Object obj) {
                        game.assets.playSound(game.assets.clickSound);
                        this.remove();
                        Gdx.input.setInputProcessor(stage);
                        backBtnPressed = true;
                    }
                };
                TextButton dummy = new TextButton("", game.assets.neonSkin);
                infoDialog.button("     "+game.assets.mainMenuBundle.get("back")+"     ", true, dummy.getStyle()).setHeight(100); //sends "true" as the result
                infoDialog.key(Input.Keys.ENTER, true); //sends "true" when the ENTER key is pressed
                infoDialog.key(Input.Keys.BACK, true);
                infoDialog.key(Input.Keys.ESCAPE, true);
                infoDialog.text(aboutString);
                infoDialog.show(dialogStage);
                aboutPressedCounter++; //unlock all levels
                if(aboutPressedCounter == 5) {
                    game.preferences.setLevelProgress("9_1");
                    game.assets.playSound(game.assets.clickSound);
                    infoDialog.remove();
                    Gdx.input.setInputProcessor(stage);
                    backBtnPressed = true;
                }
            }
        });
        musicBtn.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.assets.playSound(game.assets.clickSound);
                if(MyGdxGame.musicOn) {
                    musicLabel.setText("[RED]"+game.assets.mainMenuBundle.get("music_off")+"[]   ");
                    game.preferences.setMusicEnabled(false);
                    game.assets.pauseMusic(game.assets.mainMenuMusic);
                    MyGdxGame.musicOn=false;
                    game.assets.unloadMainMenuMusic();
                }
                else {
                    musicLabel.setText("[GREEN]"+game.assets.mainMenuBundle.get("music_on")+"[]   ");
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
                    sfxLabel.setText("[RED]"+game.assets.mainMenuBundle.get("sound_effects_off")+"[]   ");
                    game.preferences.setSoundEffectsEnabled(false);
                    MyGdxGame.sfxOn = false;
                    game.assets.unloadMainMenuSounds();
                }
                else {
                    sfxLabel.setText("[GREEN]"+game.assets.mainMenuBundle.get("sound_effects_on")+"[]   ");
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
        if(game.gameMinimized)
            game.drawMinimized();
        else {
            stage.act(delta);
            stage.draw();
            handleInput();

            game.sb.setColor(Color.WHITE);
            game.sb.begin();
            game.sb.draw(game.assets.earthTR, languageSb.localToStageCoordinates(new Vector2(0, 0)).x - 60, languageSb.localToStageCoordinates(new Vector2(0, 0)).y + 4, 64, 64);
            game.sb.draw(game.assets.audioTR, audioTable.localToStageCoordinates(new Vector2(0, 0)).x + 30, audioTable.localToStageCoordinates(new Vector2(0, 0)).y + 20);
            game.sb.draw(game.assets.playTR, playBtn.localToStageCoordinates(new Vector2(0, 0)).x + 30, playBtn.localToStageCoordinates(new Vector2(0, 0)).y + 35);
//            game.sb.draw(game.assets.optionsT, optionsBtn.localToStageCoordinates(new Vector2(0, 0)).x + 30, optionsBtn.localToStageCoordinates(new Vector2(0, 0)).y + 35);
            game.sb.draw(game.assets.aboutUpTR, aboutBtn.localToStageCoordinates(new Vector2(0, 0)).x + 30, aboutBtn.localToStageCoordinates(new Vector2(0, 0)).y + 35);
            Fonts.big.setColor(Color.RED);
            Fonts.big.draw(game.sb, MyGdxGame.TITLE, viewport.getCamera().viewportWidth / 2 - glyphLayout.width / 2, viewport.getCamera().viewportHeight - 60);
            game.sb.end();

            dialogStage.act(delta);
            dialogStage.draw();
            backBtnPressed = false;
        }
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
        game.gameMinimized = true;
        game.assets.pauseMusic(game.assets.mainMenuMusic);
        game.assets.unloadAllMainMenuAssets(true);
        //game.assets.unloadSkins();
        //for(Actor actor: dialogStage.getActors())
        //    actor.remove();
    }

    @Override
    public void resume() {
        game.assets.loadAllMainMenuAssets();
        game.assets.finishLoading();
        game.assets.refreshMainMenuAssets();
        game.assets.loadMainMenuBundles(game.preferences.getLanguage());
        game.gameMinimized = false;
        game.assets.playMusic(game.assets.mainMenuMusic);
        //recreateUI();
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
