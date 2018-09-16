package com.steveflames.javantgarde.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextTooltip;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.steveflames.javantgarde.MyGdxGame;
import com.steveflames.javantgarde.tools.Assets;
import com.steveflames.javantgarde.tools.global.Fonts;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * In this Screen the learning categories and their levels
 * are presented. The player chooses which level he wants
 * to play from the list.
 */

public class ChooseLevelScreen implements Screen{

    private MyGdxGame game;
    private Stage stage;
    private Viewport viewport;
    static LinkedHashMap<String, ArrayList<LevelListItem>> categories = new LinkedHashMap<String, ArrayList<LevelListItem>>();
    private Assets assets;

    /**
     * The components Hierarchy:
     * Stage
     *   Window
     *     TopTable
     *       BackBtn
     *     ScrollPane
     *       MainTable
     *         CategoryTables
     *           CategoryLabel
     *           LevelsTable
     *             LevelTables
     */
    public ChooseLevelScreen(final MyGdxGame game) {
        this.game = game;
        this.assets = game.assets;
        viewport = new FitViewport(MyGdxGame.WIDTH, MyGdxGame.HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, game.sb);

        game.assets.loadAllMainMenuAssets();
        game.assets.finishLoading();
        game.assets.refreshMainMenuAssets();
        game.assets.playMusic(game.assets.mainMenuMusic);
        game.assets.loadMainMenuBundles(game.preferences.getLanguage());
        loadCategories();
        recreateUI();
    }

    private void recreateUI() {
        Window window = new Window("", assets.neonSkin, "window2");
        window.setFillParent(true);
        window.top();

        Table topTable = new Table(assets.neonSkin);
        TextButton backBtn = new TextButton("< "+game.assets.mainMenuBundle.get("back")+"  ", assets.neonSkin);
        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                assets.playSound(assets.clickSound);
                game.setScreen(new MainMenuScreen(game));
                dispose();
            }
        });
        topTable.add(backBtn).expandX().width(200).left();
        topTable.top();
        window.add(topTable).expandX().left();
        window.row();

        final Table mainTable = new Table(assets.neonSkin);
        ScrollPane scroll = new ScrollPane(mainTable, assets.neonSkin);
        scroll.setFadeScrollBars(false);
        window.add(scroll).expandX().fillX().left();

        ArrayList<Table> categoryTables = new ArrayList<Table>();
        for(final String category : categories.keySet()) {
            //new categoryTable
            categoryTables.add(new Table(assets.neonSkin));
            categoryTables.get(categoryTables.size()-1).add(new Label(category, assets.neonSkin)).expandX().fillX().left().top();
            categoryTables.get(categoryTables.size()-1).row();

            ArrayList<Table> levelTables = new ArrayList<Table>();
            Table levelsTable = new Table(assets.neonSkin);
            TextTooltip textTooltip = new TextTooltip(" " +game.assets.mainMenuBundle.get("unlock"), assets.neonSkin);
            textTooltip.setInstant(true);
            for(final LevelListItem level : categories.get(category)) {
                //create new level table
                levelTables.add(new Table(assets.neonSkin).padRight(20));
                final TextButton levelBtn = new TextButton(level.getName(), assets.neonSkin);
                levelBtn.addListener(new ChangeListener() {
                    @Override
                    public void changed (ChangeListener.ChangeEvent event, Actor actor) {
                        assets.playSound(assets.clickSound);
                        assets.stopMusic(assets.mainMenuMusic);
                        dispose();
                        game.setScreen(new LoadingScreen(game, level));
                    }
                });
                levelTables.get(levelTables.size()-1).add(levelBtn).expand().fill().left();

                //lock levels that the player hasn't reached
                if(Gdx.app.getType()!= Application.ApplicationType.WebGL) {
                    int categoryN = Integer.parseInt(level.getId().split("_")[0]);
                    int levelN = Integer.parseInt(level.getId().split("_")[1]);
                    int savedCategoryN = Integer.parseInt(game.preferences.getLevelProgress().split("_")[0]);
                    int savedLevelN = Integer.parseInt(game.preferences.getLevelProgress().split("_")[1]);
                    if (categoryN > savedCategoryN || (categoryN == savedCategoryN && levelN > savedLevelN)) {
                        levelBtn.setDisabled(true);
                        levelBtn.addListener(textTooltip);
                        levelBtn.row();
                        levelBtn.add(new Image(assets.lockT)).right().width(64).height(64);
                    }
                }

                //add new levelTable to the levelsTable
                levelsTable.add(levelTables.get(levelTables.size()-1)).minWidth(300).height(200).left().top().padRight(10);
            }
            //add levelsTable to category
            categoryTables.get(categoryTables.size()-1).add(levelsTable).expandX().left().top();

            mainTable.add(categoryTables.get(categoryTables.size()-1)).expandX().fillX().height(250).padLeft(20).top();
            mainTable.row().padTop(20);
        }

        stage.addActor(window);
        stage.setScrollFocus(scroll);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    private void handleInput() {
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            assets.playSound(assets.clickSound);
            dispose();
            game.setScreen(new MainMenuScreen(game));
        }
    }

    @Override
    public void render(float delta) {
        if(game.gameMinimized)
            game.drawMinimized();
        else {
            handleInput();
            stage.act(delta);
            stage.draw();
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {
        game.gameMinimized = true;
        assets.pauseMusic(assets.mainMenuMusic);
        game.assets.unloadAllMainMenuAssets(true);
        //game.assets.unloadSkins();
    }

    @Override
    public void resume() {
        game.assets.loadAllMainMenuAssets();
        game.assets.finishLoading();
        game.assets.refreshMainMenuAssets();
        game.assets.loadMainMenuBundles(game.preferences.getLanguage());
        game.gameMinimized = false;
        assets.playMusic(assets.mainMenuMusic);
        recreateUI();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    private void loadCategories() {
        categories.clear();
        categories.put(game.assets.mainMenuBundle.get("intro"), new ArrayList<LevelListItem>()); //1
        categories.get(game.assets.mainMenuBundle.get("intro")).add(new LevelListItem(game.assets.mainMenuBundle.get("intro"), "1_1", game.assets.mainMenuBundle.get("1_1")));
        categories.get(game.assets.mainMenuBundle.get("intro")).add(new LevelListItem(game.assets.mainMenuBundle.get("intro"), "1_2", game.assets.mainMenuBundle.get("1_2")));
        categories.get(game.assets.mainMenuBundle.get("intro")).add(new LevelListItem(game.assets.mainMenuBundle.get("intro"), "1_3", game.assets.mainMenuBundle.get("1_3")));

        categories.put(game.assets.mainMenuBundle.get("variables"), new ArrayList<LevelListItem>()); //2
        categories.get(game.assets.mainMenuBundle.get("variables")).add(new LevelListItem(game.assets.mainMenuBundle.get("variables"), "2_1", game.assets.mainMenuBundle.get("2_1")));
        categories.get(game.assets.mainMenuBundle.get("variables")).add(new LevelListItem(game.assets.mainMenuBundle.get("variables"), "2_2", game.assets.mainMenuBundle.get("2_2")));

        categories.put(game.assets.mainMenuBundle.get("methods"), new ArrayList<LevelListItem>()); //3
        categories.get(game.assets.mainMenuBundle.get("methods")).add(new LevelListItem(game.assets.mainMenuBundle.get("methods"), "3_1", game.assets.mainMenuBundle.get("3_1")));
        categories.get(game.assets.mainMenuBundle.get("methods")).add(new LevelListItem(game.assets.mainMenuBundle.get("methods"), "3_2", game.assets.mainMenuBundle.get("3_2")));

        categories.put(game.assets.mainMenuBundle.get("loops"), new ArrayList<LevelListItem>()); //4
        categories.get(game.assets.mainMenuBundle.get("loops")).add(new LevelListItem(game.assets.mainMenuBundle.get("loops"), "4_1", game.assets.mainMenuBundle.get("4_1")));
        categories.get(game.assets.mainMenuBundle.get("loops")).add(new LevelListItem(game.assets.mainMenuBundle.get("loops"), "4_2", game.assets.mainMenuBundle.get("4_2")));

        categories.put(game.assets.mainMenuBundle.get("conditionals"), new ArrayList<LevelListItem>()); //5
        categories.get(game.assets.mainMenuBundle.get("conditionals")).add(new LevelListItem(game.assets.mainMenuBundle.get("conditionals"), "5_1", game.assets.mainMenuBundle.get("5_1")));
        categories.get(game.assets.mainMenuBundle.get("conditionals")).add(new LevelListItem(game.assets.mainMenuBundle.get("conditionals"), "5_2", game.assets.mainMenuBundle.get("5_2")));
        //categories.get("CONDITIONALS").add(new LevelListItem("CONDITIONALS", "4_3", "switch"));

        categories.put(game.assets.mainMenuBundle.get("arrays"), new ArrayList<LevelListItem>()); //6
        categories.get(game.assets.mainMenuBundle.get("arrays")).add(new LevelListItem(game.assets.mainMenuBundle.get("arrays"), "6_1", game.assets.mainMenuBundle.get("6_1")));
        categories.get(game.assets.mainMenuBundle.get("arrays")).add(new LevelListItem(game.assets.mainMenuBundle.get("arrays"), "6_2", game.assets.mainMenuBundle.get("6_2")));
        //categories.get("ARRAYS").add(new LevelListItem("ARRAYS", "6_3", "multidimensional"));

        categories.put(game.assets.mainMenuBundle.get("classes"), new ArrayList<LevelListItem>()); //7
        categories.get(game.assets.mainMenuBundle.get("classes")).add(new LevelListItem(game.assets.mainMenuBundle.get("classes"), "7_1", game.assets.mainMenuBundle.get("7_1")));
        categories.get(game.assets.mainMenuBundle.get("classes")).add(new LevelListItem(game.assets.mainMenuBundle.get("classes"), "7_2", game.assets.mainMenuBundle.get("7_2")));

        categories.put(game.assets.mainMenuBundle.get("extra"), new ArrayList<LevelListItem>()); //8
        //categories.get("EXTRA").add(new LevelListItem("EXTRA", "8_1", "user input"));
        categories.get(game.assets.mainMenuBundle.get("extra")).add(new LevelListItem(game.assets.mainMenuBundle.get("extra"), "8_2", game.assets.mainMenuBundle.get("8_1")));
    }

    public static LevelListItem getNextLevel(LevelListItem currentLevel) {
        boolean flag = false;
        for(ArrayList<LevelListItem> value : categories.values()) {
            if(flag) {
                return value.get(0);
            }
            else{
                for (int i = 0; i < value.size(); i++) {
                    if (value.get(i).getId().equals(currentLevel.getId())) {
                        if (value.size() > (i + 1))
                            return value.get(i + 1);
                        else {
                            flag = true;
                            break;
                        }
                    }
                }
            }
        }
        return null;
    }

    public static String getNextLevelId(LevelListItem currentLevel) {
        return getNextLevel(currentLevel).getId();
    }
}

class LevelListItem {

    private String id;
    private String name;
    private String categoryName;

    LevelListItem(String categoryName, String id, String name) {
        this.categoryName = categoryName;
        this.id = id;
        this.name = name;
    }

    String getCategoryName() {
        return categoryName;
    }
    public String getId() {
        return id;
    }
    String getName() {
        return name;
    }
    void setName(String name) {
        this.name = name;
    }
}
