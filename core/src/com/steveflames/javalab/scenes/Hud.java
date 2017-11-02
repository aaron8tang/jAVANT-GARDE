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
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.steveflames.javalab.MyGdxGame;
import com.steveflames.javalab.quests.Quest;
import com.steveflames.javalab.screens.ChooseLevelScreen;
import com.steveflames.javalab.screens.PlayScreen;
import com.steveflames.javalab.sprites.InfoSign;
import com.steveflames.javalab.sprites.Pc;
import com.steveflames.javalab.sprites.Player;
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
    private static Table androidInputTable;
    private TextButton useBtn;

    private Dialog infoDialog;
    private String currentInfoSignName;

    private boolean leftBtnPressed = false;
    private boolean rightBtnPressed = false;
    private boolean useBtnPressed = false;
    private boolean jumpBtnPressed = false;

    private LinkedHashMap<String, String> program = new LinkedHashMap<String, String>();

    public static Viewport viewport;

    private static PlayScreen playScreen;
    private Quest quest = new Quest();

    private static Toast toast;


    public Hud(final PlayScreen playScreen, SpriteBatch sb) {
        Hud.playScreen = playScreen;
        viewport = new StretchViewport(MyGdxGame.WIDTH, MyGdxGame.HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);
        toast = new Toast();
    }

    public void update(float dt) {
        if(toast.isShowing()) {
            toast.update(dt);
        }
    }

    public void drawStage(SpriteBatch sb) {
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
        sb.setColor(1,1,1,1);
    }

    public void drawFilled(ShapeRenderer sr) {
        if(toast.isShowing()) {
            toast.drawFilled(sr);
        }
    }

    public void drawFont(SpriteBatch sb) {
        if(playScreen.getPlayer().getHealth() > 0) {
            for(int i = 0; i< playScreen.getPlayer().getHealth(); i++)
                sb.draw(Loader.heartT, 20 +(60*i), MyGdxGame.HEIGHT - 60, 50, 50);
        }
        if(toast.isShowing())
            toast.drawFont(sb);
    }

    public static void newToast(String text) {
        toast.newToast(text);
        hideAndroidInputTable();
    }

    public void newAndroidInputTable() {
        androidInputTable = new Table(Skins.neonSkin);
        androidInputTable.setSize(MyGdxGame.WIDTH, 120);
        final TextButton leftBtn = new TextButton("<", Skins.neonSkin);
        leftBtn.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                leftBtnPressed = true;
                for(int i=0; i<leftBtn.getListeners().size; i++) {
                    if(leftBtn.getListeners().get(i) instanceof ClickListener){
                        ((ClickListener)leftBtn.getListeners().get(i)).touchDown(event,x,y,pointer,0);
                    }
                }
            }
            public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                leftBtnPressed = false;
                for(int i=0; i<leftBtn.getListeners().size; i++) {
                    if(leftBtn.getListeners().get(i) instanceof ClickListener){
                        ((ClickListener)leftBtn.getListeners().get(i)).touchUp(event,x,y,pointer,0);
                    }
                }
            }
        });
        final TextButton rightBtn = new TextButton(">", Skins.neonSkin);
        rightBtn.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                rightBtnPressed = true;
                for(int i=0; i<rightBtn.getListeners().size; i++) {
                    if(rightBtn.getListeners().get(i) instanceof ClickListener){
                        ((ClickListener)rightBtn.getListeners().get(i)).touchDown(event,x,y,pointer,0);
                    }
                }
            }
            public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                rightBtnPressed = false;
                for(int i=0; i<rightBtn.getListeners().size; i++) {
                    if(rightBtn.getListeners().get(i) instanceof ClickListener){
                        ((ClickListener)rightBtn.getListeners().get(i)).touchUp(event,x,y,pointer,0);
                    }
                }
            }
        });
        useBtn = new TextButton("USE", Skins.neonSkin);
        useBtn.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                useBtnPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                useBtnPressed = false;
            }
        });
        TextButton jumpBtn = new TextButton("JUMP", Skins.neonSkin);
        jumpBtn.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                jumpBtnPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                jumpBtnPressed = false;
            }
        });


        Table leftTable = new Table(Skins.neonSkin);
        leftTable.add(leftBtn).left().width(150).height(120).expandX();
        leftTable.add(rightBtn).left().width(150).height(120).expandX();
        Table rightTable = new Table(Skins.neonSkin);
        rightTable.add(useBtn).right().width(200).height(120).expandX();
        rightTable.add(jumpBtn).right().width(200).height(120).expandX();


        androidInputTable.add(leftTable).expand().left();
        androidInputTable.add(rightTable).expand().right();
        stage.addActor(androidInputTable);
        Gdx.input.setInputProcessor(stage);
        useBtn.setVisible(false);
    }

    public static void showAndroidInputTable() {
        if(!MyGdxGame.platformDepended.deviceHasKeyboard())
            androidInputTable.setVisible(true);
    }

    public static void hideAndroidInputTable() {
        if(!MyGdxGame.platformDepended.deviceHasKeyboard())
            androidInputTable.setVisible(false);
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
            scroll.setFadeScrollBars(false);

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
                    //compile and run todo
                    //if(compiler.compile(program) || (playScreen.getCurrentLevel().getId().equals("1_1") && quest.getProgress()==0)) {
                        if (quest.handleQuestResult(playScreen)) {
                            if (!nextQuest()) {
                                quest.completed(playScreen);
                            }
                        }
                    //}
                }
            });

            Table bottomBarTable = new Table(Skins.neonSkin);
            bottomBarTable.add(resetBtn).left().expandX();
            bottomBarTable.add(compileAndRunBtn).right().expandX();

            //add components to window
            editorWindow.setSize(700, MyGdxGame.HEIGHT-213);
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
        hideAndroidInputTable();
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
            questWindow.setY(MyGdxGame.HEIGHT-408);
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
                closeCurrentInfo();
            }
        };
        currentInfoSignName = name;
        TextButton dummy = new TextButton("", Skins.neonSkin);
        infoDialog.button("     OK     ", true, dummy.getStyle()); //sends "true" as the result
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
        hideAndroidInputTable();
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
        hideAndroidInputTable();
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

    private void showEditorInfo() {
        newToast("dis iz editor...");
    }

    public void closeCurrentEditor() {
        if(editorWindow!=null && editorWindow.getStage()!=null) {
            playScreen.getPlayer().currentState = Player.State.STANDING;
            editorWindow.remove();
            consoleWindow.remove();
            questWindow.remove();
            showAndroidInputTable();
        }
    }

    public void closeCurrentInfo() {
        if(infoDialog.getStage()!=null)
            infoDialog.remove();
        playScreen.getPlayer().currentState = Player.State.STANDING;

        playScreen.setEnterKeyHandled(true);
        if(currentInfoSignName.equals("info-1_1-0")) {
            playScreen.getDoors().get(0).open();
        }
        else if(currentInfoSignName.equals("info-1_1-1")) {
            playScreen.getDoors().get(1).open();
        }
        else if(currentInfoSignName.equals("info-1_1-2")) {
            playScreen.getDoors().get(2).open();
        }
    }

    public void showUseBtn(String text) {
        if(!MyGdxGame.platformDepended.deviceHasKeyboard()) {
            useBtn.setVisible(true);
            useBtn.setText(text);
        }
    }

    public void hideUseBtn() {
        if(!MyGdxGame.platformDepended.deviceHasKeyboard())
            useBtn.setVisible(false);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public Toast getCurrentToast() {
        return toast;
    }

    public Viewport getViewport() {
        return viewport;
    }

    public boolean isLeftBtnPressed() {
        return leftBtnPressed;
    }

    public boolean isRightBtnPressed() {
        return rightBtnPressed;
    }

    public boolean isUseBtnPressed() {
        return useBtnPressed;
    }

    public boolean isJumpBtnPressed() {
        return jumpBtnPressed;
    }

    public void setUseBtnPressed(boolean useBtnPressed) {
        if(!MyGdxGame.platformDepended.deviceHasKeyboard())
            this.useBtnPressed = useBtnPressed;
    }

    public void setJumpBtnPressed(boolean jumpBtnPressed) {
        if(!MyGdxGame.platformDepended.deviceHasKeyboard())
            this.jumpBtnPressed = jumpBtnPressed;
    }
}
