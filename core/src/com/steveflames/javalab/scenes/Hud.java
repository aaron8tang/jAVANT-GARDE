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
import com.steveflames.javalab.sprites.Player;
import com.steveflames.javalab.tools.compiler.MyClass;
import com.steveflames.javalab.tools.global.Loader;
import com.steveflames.javalab.tools.compiler.MyCompiler;
import com.steveflames.javalab.tools.MyFileReader;
import com.steveflames.javalab.tools.global.Skins;

import java.util.ArrayList;
import java.util.Map;


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
    private Label questTextArea;
    private Label consoleTextArea;
    private ScrollPane consoleScroll;
    private TextButton hintBtn;
    private ScrollPane questScroll;
    private Table classTable;
    private static Table androidInputTable;
    private TextButton useBtn;
    private Window pauseWindow;

    private Dialog infoDialog;
    private String currentInfoSignName;

    private boolean leftBtnPressed = false;
    private boolean rightBtnPressed = false;
    private boolean useBtnPressed = false;
    private boolean jumpBtnPressed = false;

    private String myClassCode;
    private int myClassCursorPosition =-1;

    //private LinkedHashMap<String, String> program = new LinkedHashMap<String, String>();
    private ArrayList<MyClass> myClasses = new ArrayList<MyClass>();

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

    public void newPauseWindow() {
        if(pauseWindow == null) {
            pauseWindow = new Window("GAME PAUSED", Skins.skin);
            pauseWindow.setSize(800,500);
            pauseWindow.setPosition(MyGdxGame.WIDTH/2 - pauseWindow.getWidth()/2, MyGdxGame.HEIGHT/2 - pauseWindow.getHeight()/2);

            TextButton resumeBtn = new TextButton("RESUME", Skins.neonSkin);
            resumeBtn.addListener(new ClickListener() {
                  @Override
                  public void clicked(InputEvent event, float x, float y) {
                      pauseWindow.remove();
                  }
            });
            TextButton restartBtn = new TextButton("RESTART LEVEL", Skins.neonSkin);
            restartBtn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    playScreen.dispose();
                    playScreen.getGame().setScreen(new PlayScreen(playScreen.getGame(), playScreen.getCurrentLevel()));
                }
            });
            TextButton exitBtn = new TextButton("EXIT", Skins.neonSkin);
            exitBtn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    playScreen.dispose();
                    playScreen.getGame().setScreen(new ChooseLevelScreen(playScreen.getGame()));
                }
            });

            pauseWindow.add(resumeBtn).width(240).height(100);
            pauseWindow.row();
            pauseWindow.add(restartBtn).width(240).height(100);
            pauseWindow.row().padTop(70);
            pauseWindow.add(exitBtn).width(240).height(100);
            //TODO DOUBLE ESCAPE
        }
        //add to stage
        stage.addActor(pauseWindow);
        Gdx.input.setInputProcessor(stage);
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
            Table dummyTable = new Table(Skins.skin);
            classTable = new Table(Skins.skin);
            TextButton classBtn = new TextButton("MyClass.java", Skins.neonSkin);
            classBtn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if(!codeTextArea.isDisabled())
                        myClassCode = codeTextArea.getText();
                    if(myClassCode != null && !myClassCode.isEmpty())
                        codeTextArea.setText(myClassCode);
                    if(myClassCursorPosition>0)
                        codeTextArea.setCursorPosition(myClassCursorPosition);
                    codeTextArea.setDisabled(false);
                }
            });
            classTable.add(classBtn).left().height(50).padLeft(0);
            dummyTable.add(classTable).left().expandX();
            ScrollPane scroll = new ScrollPane(dummyTable, Skins.neonSkin);
            if(MyGdxGame.platformDepended.deviceHasKeyboard())
                scroll.setFlickScroll(false);
            scroll.addListener(new ClickListener() {
                public void enter (InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    super.enter(event, x, y, pointer, fromActor);
                    stage.setScrollFocus(classTable);
                }
            });

            topBarTable.add(scroll).left().height(50).expand().fill().padLeft(60);
            topBarTable.add(infoBtn).right().fill();
            topBarTable.add(exitBtn).right().fill();


            //textArea
            codeTextArea = new TextArea(MyFileReader.readFile("txt/pc-" + name + ".txt").replaceAll("\r",""), Skins.neonSkin);
            codeTextArea.setFocusTraversal(false);
            codeTextArea.getOnscreenKeyboard().show(true);
            codeTextArea.getStyle().fontColor = Color.WHITE;
            myClassCode = codeTextArea.getText();
            codeTextArea.addListener(new InputListener(){
                 @Override
                 public boolean keyDown(InputEvent event, int keycode) {
                     editorKeyDown();
                     return false;
                 }

                @Override
                public boolean keyTyped(InputEvent event, char character) {
                    editorKeyTyped();
                    return false;
                }
            });
            //lineNumTable
            Table lineNumTable = new Table(Skins.neonSkin);
            lineNumTable.add(new Label(1 + "", Skins.neonSkin)).width(60).height(codeTextArea.getStyle().font.getLineHeight());
            for (int i = 1; i < 150; i++) {
                lineNumTable.row();
                lineNumTable.add(new Label(i + 1 + "", Skins.neonSkin)).width(60).height(codeTextArea.getStyle().font.getLineHeight());
            }

            //codeTable
            Table codeTable = new Table(Skins.lmlSkin);
            codeTable.add(lineNumTable).top().left();
            codeTable.add(codeTextArea).expand().fill().width(1000).padTop(5);
            scroll = new ScrollPane(codeTable, Skins.neonSkin);
            //scroll.setFadeScrollBars(false);
            if(MyGdxGame.platformDepended.deviceHasKeyboard())
                scroll.setFlickScroll(false);
            scroll.addListener(new ClickListener() {
                public void enter (InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    super.enter(event, x, y, pointer, fromActor);
                    stage.setScrollFocus(codeTextArea);
                }
            });

            //bottom bar
            /*TextButton resetBtn = new TextButton(" reset ", Skins.neonSkin);
            resetBtn.addListener(new ClickListener() {
                 @Override
                 public void clicked(InputEvent event, float x, float y) {
                    codeTextArea.setText(MyFileReader.readFile("txt/pc-" + name + ".txt").replaceAll("\r",""));
                 }
            });*/
            TextButton compileAndRunBtn = new TextButton(" compile & run ", Skins.neonSkin);
            compileAndRunBtn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if(!codeTextArea.isDisabled())
                        myClassCode = codeTextArea.getText();
                    consoleTextArea.setText("");
                    consoleScroll.scrollTo(0, viewport.getCamera().position.y + viewport.getCamera().viewportHeight, 0, 0);

                    //store the class names and code
                    myClasses.clear();
                    myClasses.add(new MyClass("MyClass", myClassCode));
                    for(Map.Entry<String, String> entry: playScreen.getPlayer().getClasses().entrySet())
                        myClasses.add(new MyClass(entry.getKey(), entry.getValue()));

                    //compile and run todo
                    if(compiler.compile(myClasses) || (playScreen.getCurrentLevel().getId().equals("1_1") && quest.getProgress()==0)) {
                        if (quest.validateCodeForQuest(playScreen, myClasses)) {
                            if (!nextQuest()) {
                                quest.completed(playScreen);
                            }
                        }
                    }
                }
            });

            Table bottomBarTable = new Table(Skins.neonSkin);
            //bottomBarTable.add(resetBtn).left().expandX();
            bottomBarTable.add(compileAndRunBtn).right().expandX().pad(0).height(50);

            //add components to window
            editorWindow.setSize(700, MyGdxGame.HEIGHT-213);
            editorWindow.setX(MyGdxGame.WIDTH - editorWindow.getWidth());
            editorWindow.setY(195);
            editorWindow.add(topBarTable).expandX().fillX().top();
            editorWindow.row().padTop(5);
            editorWindow.add(scroll).expandX().fillX();
            editorWindow.row();
            editorWindow.add(bottomBarTable).expandX().fillX();
        }
        //add to stage
        stage.addActor(editorWindow);
        stage.setKeyboardFocus(codeTextArea);
        Gdx.input.setInputProcessor(stage);

        //add the myClasses that the player found in the editor
        boolean flag = true;
        for(final Map.Entry<String, String> s : playScreen.getPlayer().getClasses().entrySet()) {
            for(Actor textBtn: classTable.getChildren()) {
                if(((TextButton)textBtn).getText().toString().equals(s.getKey()+".java")) {
                    flag = false;
                    break;
                }
            }
            if(flag) {
                final TextButton btn = new TextButton(s.getKey() + ".java", Skins.neonSkin);
                btn.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        if(!codeTextArea.isDisabled()) {
                            myClassCode = codeTextArea.getText();
                            myClassCursorPosition = codeTextArea.getCursorPosition();
                        }
                        codeTextArea.setText(s.getValue().replaceAll("\r",""));
                        codeTextArea.setDisabled(true);
                        if(quest.getProgress()==0 && btn.getText().toString().equals("InfoSign.java"))
                            nextQuest();
                    }
                });
                classTable.add(btn).left().height(50).padLeft(0);
            }
        }

        newConsoleWindow();
        newQuestWindow(name);
        hideAndroidInputTable();
        compiler = new MyCompiler(consoleTextArea);
    }

    private void newConsoleWindow() {
        if(consoleWindow == null) {
            consoleWindow = new Window("CONSOLE", Skins.skin);

            //textArea of console window
            Table table = new Table(Skins.neonSkin);
            consoleTextArea = new Label("", Skins.skin);
            consoleTextArea.setWrap(true);
            //consoleTextArea.setDisabled(true);
            table.add(consoleTextArea).expand().fillX().left().top().padLeft(5);

            consoleScroll = new ScrollPane(table, Skins.neonSkin);
            //scroll.setFadeScrollBars(false);
            if(MyGdxGame.platformDepended.deviceHasKeyboard())
                consoleScroll.setFlickScroll(false);

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
            questTextArea = new Label(quest.getQuestSteps().get(quest.getProgress()).getText(), Skins.skin);
            questTextArea.setWrap(true);
            table.add(questTextArea).left().top().expand().fillX().padLeft(5);
            questScroll = new ScrollPane(table, Skins.neonSkin);
            if(MyGdxGame.platformDepended.deviceHasKeyboard())
                questScroll.setFlickScroll(false);

            //bottom bar
            Table bottomBarTable = new Table(Skins.lmlSkin);
            progressBar = new ProgressBar(0, quest.getQuestSteps().size(), 1, false, Skins.neonSkin);
            hintBtn = new TextButton("hint", Skins.neonSkin);
            hintBtn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    playScreen.getPlayer().reduceHealth(1);
                    playScreen.getPlayer().setPlayerMsgAlpha(1);
                    questTextArea.setText(questTextArea.getText() + "\n\nHINT:\n");
                    String text = quest.getNextHint(quest);
                    if(text.contains("\r")) {
                        text = text.replace("\r", "");
                        text += "\n";
                    }
                    questTextArea.setText(questTextArea.getText() + text);
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

    private void editorKeyDown() {
        if(Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE)) {
            int pos = codeTextArea.getCursorPosition();
            int charsToDelete = 0;
            StringBuilder stringBuilder = new StringBuilder();

            int i = pos-1;
            while(i>=0 && codeTextArea.getText().charAt(i) != '\n') {
                if(codeTextArea.getText().charAt(i) == ' ')
                    charsToDelete++;
                else
                    return;
                i--;
            }

            for(int j=0; j<pos-charsToDelete; j++)
                stringBuilder.append(codeTextArea.getText().charAt(j));
            for(int j=pos; j < codeTextArea.getText().length(); j++)
                stringBuilder.append(codeTextArea.getText().charAt(j));

            codeTextArea.setText(stringBuilder.toString());
            codeTextArea.setCursorPosition(pos - charsToDelete);
        }

    }

    private void editorKeyTyped() {
        //int x = codeTextArea.getCursorPosition(); //todo multicolor... markup
        //codeTextArea.texsetText(codeTextArea.getText().replaceAll("public", "[ORANGE]public[]"));
        //codeTextArea.moveCursorLine(1);
        if(Gdx.input.isKeyJustPressed(Input.Keys.TAB)) {
            int pos = codeTextArea.getCursorPosition();
            StringBuilder stringBuilder = new StringBuilder();

            int i=0;
            while(i<codeTextArea.getCursorPosition()) {
                stringBuilder.append(codeTextArea.getText().charAt(i));
                i++;
            }
            stringBuilder.append("    ");
            for(int j=i; j < codeTextArea.getText().length(); j++) {
                stringBuilder.append(codeTextArea.getText().charAt(j));
            }
            codeTextArea.setText(stringBuilder.toString());
            codeTextArea.setCursorPosition(pos + 4);
        }
        else if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            int bracketCounter = 0;
            int pos = codeTextArea.getCursorPosition();
            StringBuilder stringBuilder = new StringBuilder();

            for(int i=0; i<pos; i++) {
                if(codeTextArea.getText().charAt(i) == '{') //todo if the user types { or } as a String there's a problem
                    bracketCounter++;
                else if(codeTextArea.getText().charAt(i) == '}')
                    bracketCounter--;
                stringBuilder.append(codeTextArea.getText().charAt(i));
            }
            if(bracketCounter<=0)
                return;
            for(int i=0; i<bracketCounter; i++)
                stringBuilder.append("    ");

            //check if previous character is {, to automatically make new line and close bracket }
            int j=pos-1;
            boolean flag = false;
            while(j>=0) {
                if(codeTextArea.getText().charAt(j) == '{')
                    flag = true;
                else if(codeTextArea.getText().charAt(j) != ' ' && codeTextArea.getText().charAt(j) != '\n')
                    break;
                j--;
            }
            if(flag) {
                stringBuilder.append("\n");
                for(int i=0; i<bracketCounter-1; i++)
                    stringBuilder.append("    ");
                stringBuilder.append('}');
            }

            //append the rest code
            for(int i=pos; i < codeTextArea.getText().length(); i++) {
                stringBuilder.append(codeTextArea.getText().charAt(i));
            }
            codeTextArea.setText(stringBuilder.toString());
            codeTextArea.setCursorPosition(pos + 4*bracketCounter);

        }
        else if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT_BRACKET)) {
            if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT)) {
                int pos = codeTextArea.getCursorPosition();
                int charsToDelete = 0;
                int bracketCounter = 0;
                StringBuilder stringBuilder = new StringBuilder();

                int i = pos-2;
                while(i>=0 && codeTextArea.getText().charAt(i) != '\n') {
                    if(codeTextArea.getText().charAt(i) == ' ') //if the whole line is spaces
                        charsToDelete++;
                    else
                        return;
                    i--;
                }

                for(int j=0; j<pos - charsToDelete; j++) { //count brackets
                    if(codeTextArea.getText().charAt(j) == '{') //todo if the user types { or } as a String there's a problem
                        bracketCounter++;
                    else if(codeTextArea.getText().charAt(j) == '}')
                        bracketCounter--;

                    stringBuilder.append(codeTextArea.getText().charAt(j));
                }

                if(bracketCounter<0)
                    return;

                for(int j=0; j<bracketCounter-1; j++)
                    stringBuilder.append("    ");
                stringBuilder.setCharAt(stringBuilder.length()-1, '}');
                bracketCounter = stringBuilder.length();

                for(int j=pos; j < codeTextArea.getText().length(); j++)
                    stringBuilder.append(codeTextArea.getText().charAt(j));

                codeTextArea.setText(stringBuilder.toString());
                codeTextArea.setCursorPosition(bracketCounter);
            }
        }
        //questTextArea.setText(codeTextArea.getText()); //todo for the color markup. you will be writing your code in another textarea
                                                         //todo and what u see will be a multicolor label that gets copied with each KEY DOWN
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
        if(currentInfoSignName.equals("info-1_1-0") || currentInfoSignName.equals("info-7_1-0")) {
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

    public void handleExitFromPauseMenuInput() {
        if(MyGdxGame.platformDepended.deviceHasKeyboard()) {
            if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
                pauseWindow.remove();
        }
        else {
            if(Gdx.input.isKeyJustPressed(Input.Keys.BACK))
                pauseWindow.remove();
        }
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public Toast getCurrentToast() {
        return toast;
    }

    public boolean isPauseWindowShowing() {
        return pauseWindow!=null && pauseWindow.getStage() != null;
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

    public Window getPauseWindow() {
        return pauseWindow;
    }

    public Window getQuestWindow() {
        return questWindow;
    }

    public Quest getQuest() {
        return quest;
    }

    public Label getConsoleTextArea() {
        return consoleTextArea;
    }
}
