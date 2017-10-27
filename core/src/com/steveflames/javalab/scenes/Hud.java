package com.steveflames.javalab.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.steveflames.javalab.MyGdxGame;
import com.steveflames.javalab.ingame_classes.iUserCode;
import com.steveflames.javalab.quests.Quest;
import com.steveflames.javalab.screens.ChooseLevelScreen;
import com.steveflames.javalab.screens.PlayScreen;
import com.steveflames.javalab.sprites.Player;
import com.steveflames.javalab.tools.global.Fonts;
import com.steveflames.javalab.tools.global.Loader;
import com.steveflames.javalab.tools.MyCompiler;
import com.steveflames.javalab.tools.MyFileReader;
import com.steveflames.javalab.tools.global.Skins;

import java.util.LinkedHashMap;


/**
 * Created by Flames on 9/4/16.
 */
public class Hud implements Disposable {

    private MyCompiler compiler;

    //hud components
    public Stage stage;
    private Window editorWindow;
    private Window consoleWindow;
    private Window questWindow;
    private Table gameOverWindow;
    private ProgressBar progressBar;
    private TextArea codeTextArea;
    private TextArea questTextArea;
    private TextArea consoleTextArea;
    private ScrollPane consoleScroll;
    private TextButton hintBtn;
    private ScrollPane questScroll;
    private Table classTable;

    private Dialog infoDialog;

    private LinkedHashMap<String, String> program = new LinkedHashMap<String, String>();

    public static Viewport viewport;

    private static PlayScreen playScreen;
    private Quest quest = new Quest();

    private static Toast currentToast = null;


    public Hud(final PlayScreen playScreen, SpriteBatch sb) {
        this.playScreen = playScreen;
        viewport = new StretchViewport(MyGdxGame.WIDTH, MyGdxGame.HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);
    }

    public void update(float dt) {
        if(currentToast != null) {
            currentToast.update(dt);
            if(currentToast.getCurrentState() == Toast.State.LEFT) {
                currentToast = null;
                if(editorWindow != null && editorWindow.isVisible())
                    Gdx.input.setInputProcessor(stage);
                else
                    Gdx.input.setInputProcessor(playScreen);
            }
        }
    }

    public void render(SpriteBatch sb, ShapeRenderer sr) {
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
        sb.setColor(1,1,1,1);

        sb.begin();
        if(playScreen.getPlayer().getHealth() > 0) {
            for(int i = 0; i< playScreen.getPlayer().getHealth(); i++)
                sb.draw(Loader.heartT, 20 +(60*i), MyGdxGame.HEIGHT - 60, 50, 50);
        }
        sb.end();

        if(currentToast != null)
            currentToast.render(sb, sr);
    }

    public static void newToast(String text) {
        currentToast = new Toast(text);
        Gdx.input.setInputProcessor(playScreen);
    }

    public void newEditorWindow(final String name) {
        if(editorWindow==null) {
            editorWindow = new Window("EDITOR", Skins.skin);

            //top bar
            Table topBarTable = new Table(Skins.neonSkin);
            TextButton infoBtn = new TextButton("?", Skins.neonSkin);
            infoBtn.pad(2);
            infoBtn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    showEditorInfo();
                }
            });
            TextButton exitBtn = new TextButton("x", Skins.neonSkin);
            exitBtn.pad(2);
            exitBtn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    closeCurrentEditor();
                    Gdx.input.setOnscreenKeyboardVisible(false);
                }
            });
            classTable = new Table(Skins.skin);
            TextButton classBtn = new TextButton("MyClass.java", Skins.neonSkin);
            classTable.add(classBtn).left().expandX();
            ScrollPane scroll = new ScrollPane(classTable, Skins.neonSkin);

            topBarTable.add(scroll).left().expandX().fillX().padLeft(75);
            topBarTable.add(infoBtn).right().fill();
            topBarTable.add(exitBtn).right().fill();


            //textArea
            codeTextArea = new TextArea(MyFileReader.readFile("txt/pc-" + name + ".txt").replaceAll("\r",""), Skins.neonSkin);
            codeTextArea.setFocusTraversal(false);
            codeTextArea.getOnscreenKeyboard().show(true);
            /*codeTextArea.addListener(new InputListener(){ //todo multicolor... markup
                @Override
                public boolean keyTyped(InputEvent event, char character) {
                    //int x = codeTextArea.getCursorPosition();
                    //codeTextArea.texsetText(codeTextArea.getText().replaceAll("public", "[ORANGE]public[]"));
                    //codeTextArea.moveCursorLine(1);
                    return false;
                }

            });*/
            //lineNumTable
            Table lineNumTable = new Table(Skins.neonSkin);
            lineNumTable.add(new Label(1 + "", Skins.neonSkin)).width(60).height(codeTextArea.getStyle().font.getLineHeight());
            for (int i = 1; i < 100; i++) {
                lineNumTable.row();
                lineNumTable.add(new Label(i + 1 + "", Skins.neonSkin)).width(60).height(codeTextArea.getStyle().font.getLineHeight());
            }

            //codeTable
            Table codeTable = new Table(Skins.lmlSkin);
            codeTable.add(lineNumTable).top().left();
            codeTable.add(codeTextArea).expand().fill().width(1000).padTop(5);
            scroll = new ScrollPane(codeTable, Skins.neonSkin);

            //bottom bar
            TextButton resetBtn = new TextButton(" reset ", Skins.neonSkin);
            resetBtn.addListener(new ClickListener() {
                 @Override
                 public void clicked(InputEvent event, float x, float y) {
                    codeTextArea.setText(MyFileReader.readFile("txt/pc-" + name + ".txt").replaceAll("\r",""));
                 }
            });
            TextButton compileAndRunBtn = new TextButton(" compile & run ", Skins.neonSkin);
            compileAndRunBtn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    consoleTextArea.setText("");
                    consoleScroll.scrollTo(0, viewport.getCamera().position.y + viewport.getCamera().viewportHeight, 0, 0);

                    program.clear();
                    program.put("MyClass.java", codeTextArea.getText());
                    //compile and run
                    if(compiler.compile(program) || (playScreen.getCurrentLevel().getId().equals("1_1") && quest.getProgress()==0)) {
                        if (quest.handleQuestResult(playScreen)) {
                            if (!nextQuest()) {
                                quest.completed(playScreen);
                            }
                        }
                    }
                }
            });

            Table bottomBarTable = new Table(Skins.neonSkin);
            bottomBarTable.add(resetBtn).left().expandX();
            bottomBarTable.add(compileAndRunBtn).right().expandX();

            //add components to window
            editorWindow.setSize(700, 555);
            editorWindow.setX(MyGdxGame.WIDTH - editorWindow.getWidth());
            editorWindow.setY(195);
            editorWindow.add(topBarTable).expandX().fillX().top();
            editorWindow.row().padTop(5);
            editorWindow.add(scroll).expandX().fillX();
            editorWindow.row().padTop(10);
            editorWindow.add(bottomBarTable).expandX().fillX();
            editorWindow.addListener(new ClickListener() {
                public void enter (InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    super.enter(event, x, y, pointer, fromActor);
                    stage.setScrollFocus(codeTextArea);
                }
            });
        }
        //add to stage
        stage.addActor(editorWindow);
        Gdx.input.setInputProcessor(stage);
        stage.setKeyboardFocus(codeTextArea);
        //editorWindow.debugAll();

        newConsoleWindow();
        newQuestWindow(name);
        compiler = new MyCompiler(codeTextArea, consoleTextArea, classTable);
    }

    private void newConsoleWindow() {
        if(consoleWindow == null) {
            consoleWindow = new Window("CONSOLE", Skins.skin);

            //textArea of console window
            Table table = new Table(Skins.neonSkin);
            consoleTextArea = new TextArea("", Skins.neonSkin);
            consoleTextArea.setDisabled(true);
            table.add(consoleTextArea).height(1000).expand().fillX().left().padTop(5);

            consoleScroll = new ScrollPane(table, Skins.neonSkin);
            //scroll.setFadeScrollBars(false);

            //add components to window
            consoleWindow.setSize(editorWindow.getWidth(), 190);
            consoleWindow.setX(MyGdxGame.WIDTH - consoleWindow.getWidth());
            consoleWindow.setY(0);
            consoleWindow.add(consoleScroll).expand().fill().top().left().padTop(5);
            consoleWindow.addListener(new ClickListener() {
                public void enter (InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    super.enter(event, x, y, pointer, fromActor);
                    stage.setScrollFocus(consoleTextArea);
                }
            });
        }
        stage.addActor(consoleWindow);
    }

    private void newQuestWindow(String name) {
        if(questWindow == null) {
            questWindow = new Window("QUEST", Skins.skin);
            quest.parseQuestString(MyFileReader.readFile("txt/quest-" + name + ".txt").replaceAll("\r",""));

            //quest text area
            Table table = new Table(Skins.lmlSkin);
            questTextArea = new TextArea(quest.getQuestSteps().get(quest.getProgress()).getText(), Skins.neonSkin);
            questTextArea.getStyle().fontColor = Color.WHITE;
            questTextArea.setDisabled(true);
            table.add(questTextArea).height(1000).left().expand().fillX().padTop(5);
            questScroll = new ScrollPane(table, Skins.neonSkin);

            //bottom bar
            Table bottomBarTable = new Table(Skins.lmlSkin);
            progressBar = new ProgressBar(0, quest.getQuestSteps().size(), 1, false, Skins.neonSkin);
            hintBtn = new TextButton("hint", Skins.neonSkin);
            hintBtn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    playScreen.getPlayer().reduceHealth(1);
                    playScreen.getPlayer().setPlayerMsgAlpha(1);
                    questTextArea.appendText("\n\nHINT:\n");
                    String text = quest.getNextHint(quest);
                    if(text.contains("\r")) {
                        text = text.replace("\r", "");
                        text += "\n";
                    }
                    questTextArea.appendText(text);
                    if(quest.getQuestSteps().get(quest.getProgress()).getHintPtr() >= quest.getQuestSteps().get(quest.getProgress()).getHints().size()-1)
                        hintBtn.setVisible(false);
                    //TODO scroll h apla emfanhse to scroll
                }
            });
            if(quest.getQuestSteps().get(quest.getProgress()).getHints().size()==0)
                hintBtn.setVisible(false);
            bottomBarTable.add(progressBar).expandX().fillX().left().padLeft(10);
            bottomBarTable.add(hintBtn).right();

            //add components to window
            questWindow.setSize(580, 340);
            questWindow.setX(0);
            questWindow.setY(360);
            questWindow.add(questScroll).expand().fill().top().left().padTop(10);
            questWindow.row();
            questWindow.add(bottomBarTable).expandX().fillX();
            questWindow.addListener(new ClickListener() {
                public void enter (InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    super.enter(event, x, y, pointer, fromActor);
                    stage.setScrollFocus(questTextArea);
                }
            });
        }
        stage.addActor(questWindow);
    }

    public void newInfoWindow(final String name) {
        infoDialog = new Dialog("", Skins.skin, "dialog") {
            public void result(Object obj) {
                playScreen.getPlayer().currentState = Player.State.STANDING;
                Gdx.input.setInputProcessor(playScreen);
                playScreen.setEnterKeyHandled(true);
                if(name.equals("info-1_1-0")) {
                    playScreen.getDoors().get(0).open();
                }
                else if(name.equals("info-1_1-1")) {
                    playScreen.getDoors().get(1).open();
                }
                else if(name.equals("info-1_1-2")) {
                    playScreen.getDoors().get(2).open();
                }
            }
        };
        TextButton dummy = new TextButton("", Skins.neonSkin);
        infoDialog.button("    OK    ", true, dummy.getStyle()); //sends "true" as the result
        infoDialog.key(Input.Keys.ENTER, true); //sends "true" when the ENTER key is pressed
        infoDialog.text(MyFileReader.readFile("txt/"+name+".txt"));
        infoDialog.show(stage);
        Gdx.input.setInputProcessor(stage);
    }

    public void newGameOverWindow() {
        if(gameOverWindow == null) {
            gameOverWindow = new Table(Skins.neonSkin);
            gameOverWindow.setSize(400,260);
            gameOverWindow.setPosition(MyGdxGame.WIDTH/2 - 200, MyGdxGame.HEIGHT/2 - 130);

            Label gameOverLabel = new Label("GAME OVER", Skins.lmlSkin);
            gameOverLabel.scaleBy(1.4f, 1.4f);
            gameOverWindow.add(gameOverLabel).top().expandX().padTop(5);
            gameOverWindow.row();

            Table optionsTable = new Table(Skins.neonSkin);
            TextButton tryAgainBtn = new TextButton(" RESTART ", Skins.neonSkin);
            tryAgainBtn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    playScreen.dispose();
                    playScreen.getGame().setScreen(new PlayScreen(playScreen.getGame(), playScreen.getCurrentLevel()));
                }
            });
            TextButton exitBtn = new TextButton(" EXIT ", Skins.neonSkin);
            exitBtn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    playScreen.getGame().setScreen(new ChooseLevelScreen(playScreen.getGame()));
                    playScreen.dispose();
                }
            });
            optionsTable.padTop(20);
            optionsTable.add(tryAgainBtn).left().expand().padRight(20).width(200).height(100);
            optionsTable.add(exitBtn).right().expand().padLeft(20).width(200).height(100);
            gameOverWindow.add(optionsTable).expand().fill();
        }
        stage.addActor(gameOverWindow);
        closeCurrentEditor();
        Gdx.input.setInputProcessor(stage);
    }

    public void newLevelCompletedWindow() {

        Table levelCompletedWindow = new Table(Skins.neonSkin);
        levelCompletedWindow.setSize(400,260);
        levelCompletedWindow.setPosition(MyGdxGame.WIDTH/2 - 200, MyGdxGame.HEIGHT/2 - 130);

        Label levelCompletedLabel = new Label("LEVEL COMPLETED", Skins.lmlSkin);
        levelCompletedLabel.scaleBy(1.2f, 1.2f);
        levelCompletedWindow.add(levelCompletedLabel).top().expandX().padTop(5);
        levelCompletedWindow.row();

        Table optionsTable = new Table(Skins.neonSkin);
        TextButton restartBtn = new TextButton(" RESTART ", Skins.neonSkin);
        restartBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playScreen.dispose();
                playScreen.getGame().setScreen(new PlayScreen(playScreen.getGame(), playScreen.getCurrentLevel()));
            }
        });
        TextButton exitBtn = new TextButton(" EXIT ", Skins.neonSkin);
        exitBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playScreen.getGame().setScreen(new ChooseLevelScreen(playScreen.getGame()));
                playScreen.dispose();
            }
        });
        TextButton nextLvlBtn = new TextButton(" NEXT ", Skins.neonSkin);
        nextLvlBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playScreen.dispose();
                playScreen.getGame().setScreen(new PlayScreen(playScreen.getGame(), ChooseLevelScreen.getNextLevel(playScreen.getCurrentLevel())));

            }
        });
        optionsTable.padTop(20);
        optionsTable.add(restartBtn).left().expand().width(200).height(100);
        optionsTable.add(exitBtn).left().expand().padLeft(50).width(200).height(100);
        optionsTable.add(nextLvlBtn).left().expand().padLeft(50).width(200).height(100);
        levelCompletedWindow.add(optionsTable).expand().fill();

        stage.addActor(levelCompletedWindow);
        Gdx.input.setInputProcessor(stage);
    }

    private boolean nextQuest() {
        //add progress
        quest.setProgress(quest.getProgress()+1);
        progressBar.setValue(quest.getProgress());

        questScroll.scrollTo(0, viewport.getCamera().position.y + viewport.getCamera().viewportHeight, 0, 0);
        if(quest.getProgress() < quest.getQuestSteps().size()) {
            questTextArea.setText(quest.getQuestSteps().get(quest.getProgress()).getText());
            hintBtn.setVisible(false);
            if(quest.getQuestSteps().get(quest.getProgress()).getHints().size()>0)
                hintBtn.setVisible(true);
            return true;
        }
        else {
            questTextArea.setText("Quest completed!");
            hintBtn.setVisible(false);
            return false;
        }

    }

    public void showEditorInfo() {
        newToast("dis iz editor...");
    }

    public void closeCurrentEditor() {
        if(editorWindow!=null && editorWindow.isVisible()) {
            playScreen.getPlayer().currentState = Player.State.STANDING;
            editorWindow.remove();
            consoleWindow.remove();
            questWindow.remove();
            Gdx.input.setInputProcessor(playScreen);
        }
    }

    public void closeCurrentInfo() {
        infoDialog.remove();
        playScreen.getPlayer().currentState = Player.State.STANDING;
        Gdx.input.setInputProcessor(playScreen);
    }


    @Override
    public void dispose() {
        stage.dispose();
    }

    public Toast getCurrentToast() {
        return currentToast;
    }

    public Viewport getViewport() {
        return viewport;
    }

    public Dialog getInfoDialog() {
        return infoDialog;
    }


    public TextArea getConsoleTextArea() {
        return consoleTextArea;
    }

    public Quest getQuest() {
        return quest;
    }

    public TextArea getCodeTextArea() {
        return codeTextArea;
    }
}
