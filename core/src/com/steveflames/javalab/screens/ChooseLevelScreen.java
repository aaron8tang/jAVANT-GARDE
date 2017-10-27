package com.steveflames.javalab.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.steveflames.javalab.MyGdxGame;
import com.steveflames.javalab.buttons.LevelListItem;
import com.steveflames.javalab.tools.global.Fonts;
import com.steveflames.javalab.tools.global.Skins;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Flames on 25/9/2017.
 */

public class ChooseLevelScreen implements Screen{

    private Stage stage;
    private Viewport viewport;
    public static LinkedHashMap<String, ArrayList<LevelListItem>> categories = new LinkedHashMap<String, ArrayList<LevelListItem>>();

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
        loadCategories();
        viewport = new StretchViewport(MyGdxGame.WIDTH, MyGdxGame.HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, game.sb);

        Window window = new Window("", Skins.skin);
        window.setFillParent(true);
        window.top();

        Table topTable = new Table(Skins.neonSkin);
        TextButton backBtn = new TextButton("< BACK  ", Skins.neonSkin);
        topTable.add(backBtn).expandX().left();
        topTable.top();
        window.add(topTable).expandX().left();
        window.row();

        Table mainTable = new Table(Skins.skin);
        ScrollPane scroll = new ScrollPane(mainTable, Skins.neonSkin);
        window.add(scroll).expandX().fillX().left();

        ArrayList<Table> categoryTables = new ArrayList<Table>();
        for(String category : categories.keySet()) {
            //new categoryTable
            categoryTables.add(new Table(Skins.skin));
            categoryTables.get(categoryTables.size()-1).add(new Label(category, Skins.neonSkin)).expandX().fillX().left().top();
            categoryTables.get(categoryTables.size()-1).row();

            ArrayList<Table> levelTables = new ArrayList<Table>();
            Table levelsTable = new Table(Skins.skin);
            for(final LevelListItem level : categories.get(category)) {
                //create new level table
                levelTables.add(new Table(Skins.skin).padRight(20));
                //levelTables.get(levelTables.size()-1).add(new Label("Level " + level.getId(), Skins.neonSkin)).expandX().left();
                //levelTables.get(levelTables.size()-1).row();
                final TextButton levelBtn = new TextButton(level.getName(), Skins.neonSkin);
                levelBtn.addListener(new ClickListener() {
                     @Override
                     public void clicked(InputEvent event, float x, float y) {
                         game.setScreen(new PlayScreen(game, level));
                         dispose();
                     }
                });
                levelTables.get(levelTables.size()-1).add(levelBtn).expand().fill().left();

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

    private void handleInput() {
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            Gdx.app.exit();
        }
    }

    @Override
    public void render(float delta) {
        handleInput();
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
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
        stage.dispose();
    }

    private void loadCategories() {
        categories.put("INTRO", new ArrayList<LevelListItem>());
        categories.get("INTRO").add(new LevelListItem("1_1", "Hello World!"));
        categories.get("INTRO").add(new LevelListItem("1_2", "variables"));
        categories.get("INTRO").add(new LevelListItem("1_3", "operations"));
        categories.get("INTRO").add(new LevelListItem("1_4", "user input"));

        categories.put("CONDITIONALS", new ArrayList<LevelListItem>());
        categories.get("CONDITIONALS").add(new LevelListItem("2_1", " if - nested if - if else "));
        categories.get("CONDITIONALS").add(new LevelListItem("2_2", "switch"));

        categories.put("LOOPS", new ArrayList<LevelListItem>());
        categories.get("LOOPS").add(new LevelListItem("3_1", " while - do while "));
        categories.get("LOOPS").add(new LevelListItem("3_2", "for"));

        categories.put("ARRAYS", new ArrayList<LevelListItem>());
        categories.get("ARRAYS").add(new LevelListItem("4_1", "arrays"));
        categories.get("ARRAYS").add(new LevelListItem("4_2", "enhanced for"));
        categories.get("ARRAYS").add(new LevelListItem("4_3", "multidimensional"));

        categories.put("METHODS", new ArrayList<LevelListItem>());
        categories.get("METHODS").add(new LevelListItem("5_1", "call method"));
        categories.get("METHODS").add(new LevelListItem("5_2", "return types"));
        categories.get("METHODS").add(new LevelListItem("5_3", "access modifiers"));

        categories.put("CLASSES", new ArrayList<LevelListItem>());
        categories.get("CLASSES").add(new LevelListItem("6_1", "intro"));
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
}
