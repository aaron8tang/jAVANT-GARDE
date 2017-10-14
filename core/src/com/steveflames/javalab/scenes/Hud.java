package com.steveflames.javalab.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
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
import com.steveflames.javalab.sprites.InfoSign;
import com.steveflames.javalab.tools.Fonts;
import com.steveflames.javalab.screens.PlayScreen;
import com.steveflames.javalab.sprites.Pc;
import com.steveflames.javalab.sprites.Player;
import com.steveflames.javalab.tools.MyFileReader;
import com.steveflames.javalab.tools.Skins;

import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.Arrays;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;


/**
 * Created by Flames on 9/4/16.
 */
public class Hud implements Disposable {

    //hud components
    public Stage stage;
    private Window editorWindow;
    private Window consoleWindow;
    private Window questWindow;
    private Table gameOverWindow;
    private ProgressBar progressBar;
    private TextArea codeTextArea;
    private TextArea consoleTextArea;
    private ScrollPane consoleScroll;

    public static Viewport viewport;

    private PlayScreen playScreen;
    private Quest quest = new Quest();

    private Toast currentToast = null;


    public Hud(PlayScreen playScreen, SpriteBatch sb) {
        this.playScreen = playScreen;
        viewport = new StretchViewport(MyGdxGame.WIDTH, MyGdxGame.HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);
    }

    public void update(float dt) {
        if(currentToast != null) {
            currentToast.update(dt);
            if(currentToast.getCurrentState() == Toast.State.LEFT)
                currentToast = null;
        }
    }

    public void render(SpriteBatch sb, ShapeRenderer sr) {
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        sr.begin(ShapeRenderer.ShapeType.Filled);
        if(playScreen.getPlayer().getHealth() > 0) {
            for(int i = 0; i< playScreen.getPlayer().getHealth(); i++) {
                sr.setColor(Color.BLACK);
                sr.rect(20 +(60*i), stage.getCamera().viewportHeight - 20 - 29, 54, 29);
                sr.setColor(Color.RED);
                sr.rect(22 +(60*i), stage.getCamera().viewportHeight - 20 - 27, 50, 25);
            }
        }
        sr.end();

        if(currentToast != null)
            currentToast.render(sb, sr);

        for(Pc pc: playScreen.getPcs()) { //draw the 'use PC' prompt
            if(pc.isUsable()) {
                if(playScreen.getPlayer().currentState != Player.State.CODING) {
                    sb.setProjectionMatrix(sr.getProjectionMatrix());
                    sb.begin();
                    Fonts.medium.setColor(Color.RED);
                    Fonts.medium.draw(sb, "!", pc.getBounds().x + pc.getBounds().width / 2 - 10 + com.steveflames.javalab.Window.getHudCameraOffsetX(), pc.getBounds().y + pc.getBounds().height + 50);
                    sb.end();
                    break;
                }
            }
        }

        //TODO valto mesa sto infosign class (k to pc apo panw)
        for(InfoSign infoSign : playScreen.getInfoSigns()) {
            if(infoSign.isUsable()) {
                if(playScreen.getPlayer().currentState != Player.State.IDLE) {
                    sb.setProjectionMatrix(sr.getProjectionMatrix());
                    sb.begin();
                    Fonts.medium.setColor(Color.RED);
                    Fonts.medium.draw(sb, "!", infoSign.getBounds().x + infoSign.getBounds().width / 2 - 5 + com.steveflames.javalab.Window.getHudCameraOffsetX() , infoSign.getBounds().y + infoSign.getBounds().height + 90);
                    sb.end();
                    break;
                }
            }
        }

    }

    public void newToast(String text) {
        currentToast = new Toast(text, stage.getCamera());
    }

    public void newEditorWindow(final String name) {
        if(editorWindow==null) {
            editorWindow = new Window("EDITOR", Skins.skin);

            //top bar
            Table topBarTable = new Table(Skins.neonSkin);
            TextButton exitBtn = new TextButton("x", Skins.neonSkin);
            exitBtn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    closeCurrentEditor();
                }
            });
            Table classTable = new Table(Skins.skin);
            TextButton classBtn = new TextButton("Main.java", Skins.skin);
            classTable.add(classBtn).left().expandX();
            ScrollPane scroll = new ScrollPane(classTable, Skins.neonSkin);

            topBarTable.add(scroll).left().expandX().fillX().padLeft(90);
            topBarTable.add(exitBtn).right();


            //textArea
            codeTextArea = new TextArea(MyFileReader.readFile("txt/pc_" + name + ".txt"), Skins.neonSkin);
            codeTextArea.setFocusTraversal(false);
            codeTextArea.getOnscreenKeyboard().show(true);
            //lineNumTable
            Table lineNumTable = new Table(Skins.neonSkin);
            for (int i = 0; i < 100; i++) {
                lineNumTable.add(new Label(i + 1 + "", Skins.neonSkin)).width(60).height(23);
                lineNumTable.row();
            }
            //codeTable
            Table codeTable = new Table(Skins.neonSkin);
            codeTable.add(lineNumTable).top().left();
            codeTable.add(codeTextArea).height(2300).fillX().expandX().padLeft(20f).padTop(5);
            scroll = new ScrollPane(codeTable, Skins.neonSkin);

            //bottom bar
            TextButton resetBtn = new TextButton(" reset ", Skins.neonSkin);
            resetBtn.addListener(new ClickListener() {
                 @Override
                 public void clicked(InputEvent event, float x, float y) {
                    codeTextArea.setText(MyFileReader.readFile("txt/pc_" + name + ".txt"));
                 }
            });
            TextButton compileAndRunBtn = new TextButton(" compile & run ", Skins.neonSkin);
            compileAndRunBtn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    consoleTextArea.setText("");
                    consoleScroll.scrollTo(0, viewport.getCamera().position.y + viewport.getCamera().viewportHeight, 0, 0);
                    compile();
                }
            });

            Table bottomBarTable = new Table(Skins.neonSkin);
            bottomBarTable.add(resetBtn).left().expandX();
            bottomBarTable.add(compileAndRunBtn).right().expandX();

            //add to editorWindow
            editorWindow.add(topBarTable).expandX().fillX();
            editorWindow.row();
            editorWindow.setSize(710, 555);
            editorWindow.setX(MyGdxGame.WIDTH - 710);
            editorWindow.setY(195);
            editorWindow.add(scroll).expandX().fillX().top().left();
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

        newConsoleWindow();
        newQuestWindow(name);
    }

    private void compile() {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();

        JavaFileObject file = new JavaSourceFromString("MyClass", codeTextArea.getText());

        Iterable<? extends JavaFileObject> compilationUnits = Arrays.asList(file);
        JavaCompiler.CompilationTask task = compiler.getTask(null, null, diagnostics, null, null, compilationUnits);

        boolean success = task.call();
        for (Diagnostic diagnostic : diagnostics.getDiagnostics()) {
            System.out.println("------------------------");
            System.out.println(diagnostic.getCode());
            System.out.println(diagnostic.getKind());
            System.out.println(diagnostic.getPosition());
            System.out.println(diagnostic.getStartPosition());
            System.out.println(diagnostic.getEndPosition());
            System.out.println(diagnostic.getSource());
            System.out.println(diagnostic.getMessage(null));
            System.out.println("------------------------");
        }

        System.out.println("Success: " + success);
        //TODO COMPILE

        if (success) {
            try {
                Class.forName("MyClass").getDeclaredMethod("main", new Class[] { String[].class })
                        .invoke(null, new Object[] { null });
            } catch (ClassNotFoundException e) {
                System.err.println("Class not found: " + e);
                consoleTextArea.appendText("Class not found: " + e);
            } catch (NoSuchMethodException e) {
                System.err.println("No such method: " + e);
                consoleTextArea.appendText("No such method: " + e);
            } catch (IllegalAccessException e) {
                System.err.println("Illegal access: " + e);
                consoleTextArea.appendText("Illegal access: " + e);
            } catch (InvocationTargetException e) {
                System.err.println("Invocation target: " + e);
                consoleTextArea.appendText("Invocation target: " + e);
            }
        }
    }

    private void newConsoleWindow() {
        if(consoleWindow == null) {
            consoleWindow = new Window("CONSOLE", Skins.skin);
            Table table = new Table(Skins.neonSkin);
            consoleTextArea = new TextArea("", Skins.neonSkin);
            consoleTextArea.setDisabled(true);
            table.add(consoleTextArea).height(1000).left().expand().fillX().padTop(5);

            consoleScroll = new ScrollPane(table, Skins.neonSkin);
            //scroll.setFadeScrollBars(false);

            consoleWindow.setSize(710, 190);
            consoleWindow.setX(MyGdxGame.WIDTH - 710);
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
            quest.parseQuestString(MyFileReader.readFile("txt/quest_" + name + ".txt"));

            Table table = new Table(Skins.lmlSkin);
            final TextArea textArea = new TextArea(quest.getQuestSteps().get(quest.getProgress()).getText(), Skins.skin);
            textArea.setDisabled(true);
            table.add(textArea).height(1000).left().expand().fillX().padTop(5);

            final ScrollPane scroll = new ScrollPane(table, Skins.neonSkin);

            Table bottomBarTable = new Table(Skins.lmlSkin);
            progressBar = new ProgressBar(0, quest.getQuestSteps().size(), 1, false, Skins.neonSkin);
            final TextButton hintBtn = new TextButton("hint", Skins.neonSkin);
            hintBtn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    playScreen.getPlayer().reduceHealth(1);
                    playScreen.getPlayer().setPlayerMsgAlpha(1);
                    textArea.appendText("\n#HINT:\n");
                    String text = quest.getNextHint(quest);
                    if(text.contains("\r")) {
                        text = text.replace('\r', ' ');
                        text += "\n";
                    }
                    textArea.appendText(text);
                    if(quest.getQuestSteps().get(quest.getProgress()).getHintPtr() >= quest.getQuestSteps().get(quest.getProgress()).getHints().size()-1)
                        hintBtn.setVisible(false);
                    //TODO scroll h apla emfanhse to scroll
                }
            });
            bottomBarTable.add(progressBar).expandX().left().padLeft(10);
            bottomBarTable.add(hintBtn).expandX().right();

            questWindow.setSize(500, 300);
            questWindow.setX(15);
            questWindow.setY(390);
            questWindow.add(scroll).expand().fill().top().left().padTop(10);
            questWindow.row();
            questWindow.add(bottomBarTable).expandX().fillX();
            questWindow.addListener(new ClickListener() {
                public void enter (InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    super.enter(event, x, y, pointer, fromActor);
                    stage.setScrollFocus(textArea);
                }
            });
        }
        stage.addActor(questWindow);
    }

    private void addProgress() {
        progressBar.setValue(progressBar.getValue()+1);
    }

    public void newInfoWindow(String name) {
        Dialog dialog = new Dialog("", Skins.skin, "dialog") {
            public void result(Object obj) {
                playScreen.getPlayer().currentState = Player.State.STANDING;
                Gdx.input.setInputProcessor(playScreen);
            }
        };
        dialog.text(MyFileReader.readFile("txt/"+name+".txt"));
        dialog.button(" OK ", true); //sends "true" as the result
        dialog.key(Input.Keys.ENTER, true); //sends "true" when the ENTER key is pressed
        dialog.key(Input.Keys.SPACE, true);
        dialog.show(stage);

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
            TextButton tryAgainBtn = new TextButton(" TRY AGAIN ", Skins.neonSkin);
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
        Gdx.input.setInputProcessor(stage);

    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public Toast getCurrentToast() {
        return currentToast;
    }

    public void closeCurrentEditor() {
        if(playScreen.getPlayer().currentState == Player.State.CODING) {
            playScreen.getPlayer().currentState = Player.State.STANDING;
            editorWindow.remove();
            consoleWindow.remove();
            questWindow.remove();
            Gdx.input.setInputProcessor(playScreen);
        }
    }

    public Viewport getViewport() {
        return viewport;
    }


    /**
     * A file object used to represent source coming from a string.
     */
    public class JavaSourceFromString extends SimpleJavaFileObject {
        /**
         * The source code of this "file".
         */
        final String code;

        /**
         * Constructs a new JavaSourceFromString.
         * @param name the name of the compilation unit represented by this file object
         * @param code the source code for the compilation unit represented by this file object
         */
        JavaSourceFromString(String name, String code) {
            super(URI.create("string:///" + name.replace('.','/') + Kind.SOURCE.extension), Kind.SOURCE);
            this.code = code;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return code;
        }
    }
}
