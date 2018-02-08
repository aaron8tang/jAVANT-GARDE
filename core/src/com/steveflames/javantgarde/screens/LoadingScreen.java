package com.steveflames.javantgarde.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.steveflames.javantgarde.MyGdxGame;
import com.steveflames.javantgarde.hud.Hud;
import com.steveflames.javantgarde.tools.Assets;
import com.steveflames.javantgarde.tools.B2WorldContactListener;
import com.steveflames.javantgarde.tools.B2WorldCreator;
import com.steveflames.javantgarde.tools.GameObjectManager;
import com.steveflames.javantgarde.tools.InputHandler;
import com.steveflames.javantgarde.tools.global.Cameras;
import com.steveflames.javantgarde.tools.global.Fonts;

/**
 * This is the Loading Screen on which the assets
 * of the level that the player chose are loaded.
 */

public class LoadingScreen extends Window implements Screen {

    private MyGdxGame game;
    private LevelListItem level;
    private static float MAPWIDTH; //width of the map
    private static float MAPHEIGHT; //height of the map

    private ProgressBar progressBar;
    private String loadingString;

    private Stage stage;
    private Viewport viewport;

    private Assets assets;
    private boolean xPressed = false;
    private String levelName = "";
    private GlyphLayout glyphLayout;

    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;


    public LoadingScreen(final MyGdxGame game, LevelListItem levelListItem) {
        super("", game.assets.neonSkin);
        this.assets = game.assets;
        this.game = game;
        this.level = levelListItem;

        assets.loadPlayScreenBundles(game.preferences.getLanguage());
        recreateUI();

        game.assets.loadAllPlayScreenAssets();
        game.assets.loadPlayScreenBundles(game.preferences.getLanguage());
        loadingString = "[RED]"+game.assets.playscreenBundle.get("loading")+"[]";
        glyphLayout = new GlyphLayout(Fonts.medium, loadingString);

        //initialize map
        TmxMapLoader mapLoader = new TmxMapLoader();
        map = mapLoader.load("tiled/level-"+level.getId()+".tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / MyGdxGame.PPM);
        setMapProperties(map);

        //initialize camera and viewport
        Cameras.load(MAPWIDTH, MAPHEIGHT);
    }

    private void recreateUI() {
        //loading table
        TextButton exitBtn = new TextButton("< "+game.assets.playscreenBundle.get("back"), game.assets.neonSkin);
        if(game.assets.getQueuedAssets()==0)
            progressBar = new ProgressBar(0, 1, 1, false, game.assets.neonSkin, "big");
        else
            progressBar = new ProgressBar(0, 1, 1/game.assets.getQueuedAssets(), false, game.assets.neonSkin, "big");

        Table table = new Table(game.assets.neonSkin);

        table.add(exitBtn).expandX().right().width(200).height(100);
        table.row().space(240);
        table.add(progressBar).expand().fillX().padLeft(400).padRight(100).height(100).top();

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
                assets.unloadPlayScreenBundles();
                game.assets.unloadAllPlayScreenAssets();
                game.setScreen(new ChooseLevelScreen(game));
                dispose();
            }
        });
    }

    private void setMapProperties(TiledMap map) {
        MapProperties prop = map.getProperties();

        int mapWidth = prop.get("width", Integer.class);
        int mapHeight = prop.get("height", Integer.class);
        int tilePixelWidth = prop.get("tilewidth", Integer.class);
        int tilePixelHeight = prop.get("tileheight", Integer.class);

        MAPWIDTH = mapWidth * tilePixelWidth / MyGdxGame.PPM;
        MAPHEIGHT = mapHeight * tilePixelHeight / MyGdxGame.PPM;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if(game.gameMinimized)
            game.drawMinimized();
        else {
            int i = 0;
            //draw all the categories and levels, and highlight the current one being loaded
            game.sb.begin();
            for (String category : ChooseLevelScreen.categories.keySet()) {
                i++;
                Fonts.xsmall.setColor(Color.WHITE);
                Fonts.xsmall.draw(game.sb, category, 30, viewport.getCamera().viewportHeight - i * 30);
                for (final LevelListItem level : ChooseLevelScreen.categories.get(category)) {
                    i++;
                    levelName = level.getName().replaceAll("\n", " ");
                    if (this.level.getName().equals(level.getName()))
                        levelName = "[GREEN]" + levelName + "[]";
                    Fonts.xsmall.draw(game.sb, levelName, 80, viewport.getCamera().viewportHeight - i * 30);
                }
            }
            Fonts.medium.draw(game.sb, loadingString, 780 - glyphLayout.width / 2, 450);
            game.sb.end();


            stage.act(delta);
            stage.draw();
            game.sb.setColor(1, 1, 1, 1);

            //loading bar
            if (game.assets.update()) {
                progressBar.setValue(1f);
                if (MyGdxGame.platformDepended.deviceHasKeyboard()) {
                    loadingString = "[GREEN]"+game.assets.playscreenBundle.get("ready_enter")+"[]";
                    glyphLayout.setText(Fonts.medium, loadingString);
                    if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
                        assets.playSound(assets.clickSound);
                        dispose();
                        game.assets.unloadAllMainMenuAssets();
                        game.setScreen(new PlayScreen(game, level, map, renderer));
                    }
                } else {
                    loadingString = "[GREEN]"+game.assets.playscreenBundle.get("ready_tap")+"[]";
                    glyphLayout.setText(Fonts.medium, loadingString);
                    if (!xPressed && Gdx.input.isTouched()) {
                        assets.playSound(assets.clickSound);
                        dispose();
                        game.setScreen(new PlayScreen(game, level, map, renderer));
                    }
                }
            } else {
                if (game.assets.getProgress() > 0.5f)
                    loadingString = "[YELLOW]"+game.assets.playscreenBundle.get("loading")+"[]";
                progressBar.setValue(game.assets.getProgress());
            }
        }

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {
        game.gameMinimized = true;
        game.assets.unloadAllMainMenuAssets();
        //game.assets.unloadSkins();
    }

    @Override
    public void resume() {
        game.assets.loadAllMainMenuAssets();
        game.assets.finishLoading();
        game.assets.refreshMainMenuAssets();
        game.assets.loadMainMenuBundles(game.preferences.getLanguage());
        game.gameMinimized = false;
        //recreateUI();
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
