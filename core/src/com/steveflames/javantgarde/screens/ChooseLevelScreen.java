package com.steveflames.javantgarde.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.steveflames.javantgarde.MyGdxGame;
import com.steveflames.javantgarde.tools.Assets;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by Flames on 25/9/2017.
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
        loadCategories();
        viewport = new StretchViewport(MyGdxGame.WIDTH, MyGdxGame.HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, game.sb);
        game.assets.loadAllMainMenuAssets();
        game.assets.finishLoading();
        game.assets.refreshMainMenuAssets();
        assets.playMusic(assets.mainMenuMusic);

        Window window = new Window("", assets.terraSkin);
        window.setFillParent(true);
        window.top();

        Table topTable = new Table(assets.neonSkin);
        TextButton backBtn = new TextButton("< BACK  ", assets.neonSkin);
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

        Table mainTable = new Table(assets.terraSkin);
        ScrollPane scroll = new ScrollPane(mainTable, assets.neonSkin);
        window.add(scroll).expandX().fillX().left();

        ArrayList<Table> categoryTables = new ArrayList<Table>();
        for(final String category : categories.keySet()) {
            //new categoryTable
            categoryTables.add(new Table(assets.terraSkin));
            categoryTables.get(categoryTables.size()-1).add(new Label(category, assets.neonSkin)).expandX().fillX().left().top();
            categoryTables.get(categoryTables.size()-1).row();

            ArrayList<Table> levelTables = new ArrayList<Table>();
            Table levelsTable = new Table(assets.terraSkin);
            for(final LevelListItem level : categories.get(category)) {
                //create new level table
                levelTables.add(new Table(assets.terraSkin).padRight(20));
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
                if(!MyGdxGame.platformDepended.isHTML()) {
                    int categoryN = Integer.parseInt(level.getId().split("_")[0]);
                    int levelN = Integer.parseInt(level.getId().split("_")[1]);
                    int savedCategoryN = Integer.parseInt(game.preferences.getLevelProgress().split("_")[0]);
                    int savedLevelN = Integer.parseInt(game.preferences.getLevelProgress().split("_")[1]);
                    if (categoryN > savedCategoryN || (categoryN == savedCategoryN && levelN > savedLevelN)) {
                        levelBtn.setDisabled(true);
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
        handleInput();
        stage.act(delta);
        stage.draw();

        /*game.sb.begin();
        for(int i=0; i<lockedLevelBtnsVectors.size(); i++) {
            game.sb.setColor(Color.WHITE);
            game.sb.draw(assets.lockT, lockedLevelBtnsVectors.get(i).x + 50, lockedLevelBtnsVectors.get(i).y);
        }
        game.sb.end();*/
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {
        assets.pauseMusic(assets.mainMenuMusic);
        game.assets.unloadAllMainMenuAssets();
    }

    @Override
    public void resume() {
        game.assets.loadAllMainMenuAssets();
        game.assets.finishLoading();
        game.assets.refreshMainMenuAssets();
        assets.playMusic(assets.mainMenuMusic);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    private void loadCategories() {
        categories.put("INTRO", new ArrayList<LevelListItem>()); //1
        categories.get("INTRO").add(new LevelListItem("INTRO", "1_1", "Hello World!"));
        categories.get("INTRO").add(new LevelListItem("INTRO", "1_2", "comments"));

        categories.put("VARIABLES", new ArrayList<LevelListItem>()); //2
        categories.get("VARIABLES").add(new LevelListItem("VARIABLES", "2_1", "naming\n&\ntypes"));
        categories.get("VARIABLES").add(new LevelListItem("VARIABLES", "2_2", "initialization\n&\noperations"));

        categories.put("METHODS", new ArrayList<LevelListItem>()); //3
        categories.get("METHODS").add(new LevelListItem("METHODS", "3_1", "calling a method"));
        categories.get("METHODS").add(new LevelListItem("METHODS", "3_2", "return types"));

        categories.put("LOOPS", new ArrayList<LevelListItem>()); //4
        categories.get("LOOPS").add(new LevelListItem("LOOPS", "4_1", "while\n&\ndo while"));
        categories.get("LOOPS").add(new LevelListItem("LOOPS", "4_2", "for"));

        categories.put("CONDITIONALS", new ArrayList<LevelListItem>()); //5
        categories.get("CONDITIONALS").add(new LevelListItem("CONDITIONALS", "5_1", "if\n&\nlogical operators"));
        categories.get("CONDITIONALS").add(new LevelListItem("CONDITIONALS", "5_2", "nested if\n&\nif else"));
        //categories.get("CONDITIONALS").add(new LevelListItem("CONDITIONALS", "4_3", "switch"));

        categories.put("ARRAYS", new ArrayList<LevelListItem>()); //6
        categories.get("ARRAYS").add(new LevelListItem("ARRAYS", "6_1", "initialization"));
        categories.get("ARRAYS").add(new LevelListItem("ARRAYS", "6_2", "enhanced for"));
        //categories.get("ARRAYS").add(new LevelListItem("ARRAYS", "6_3", "multidimensional"));

        categories.put("CLASSES", new ArrayList<LevelListItem>()); //7
        categories.get("CLASSES").add(new LevelListItem("CLASSES", "7_1", "intro\n&\ncreating objects"));
        categories.get("CLASSES").add(new LevelListItem("CLASSES", "7_2", "access modifiers"));

        categories.put("EXTRA", new ArrayList<LevelListItem>()); //8
        //categories.get("EXTRA").add(new LevelListItem("EXTRA", "8_1", "user input"));
        categories.get("EXTRA").add(new LevelListItem("EXTRA", "8_2", "ALL"));
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
