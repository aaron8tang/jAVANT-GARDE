package com.steveflames.javalab.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.steveflames.javalab.MyGdxGame;
import com.steveflames.javalab.screens.PlayScreen;
import com.steveflames.javalab.sprites.Pc;
import com.steveflames.javalab.sprites.Player;
import com.steveflames.javalab.tools.global.Loader;
import com.steveflames.javalab.tools.global.Skins;


/**
 * Created by Flames on 9/4/16.
 */
public class Hud implements Disposable {

    static PlayScreen playScreen;
    private static Toast toast;
    static Viewport viewport;

    //hud components
    public Stage stage;
    private EditorWindow editorWindow;
    private PauseWindow pauseWindow;
    private Table gameOverWindow;
    private Table levelCompletedWindow;
    private Dialog infoDialog;

    private String currentInfoSignName;

    //android input table
    private static Table androidInputTable;
    private TextButton useBtn;
    private boolean leftBtnPressed = false;
    private boolean rightBtnPressed = false;
    private boolean useBtnPressed = false;
    private boolean jumpBtnPressed = false;


    public Hud(final PlayScreen playScreen, SpriteBatch sb) {
        Hud.playScreen = playScreen;
        viewport = new StretchViewport(MyGdxGame.WIDTH, MyGdxGame.HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);
        toast = new Toast();
        editorWindow = new EditorWindow("EDITOR", Skins.skin, this);
        pauseWindow = new PauseWindow("GAME PAUSED", Skins.skin, playScreen);
        gameOverWindow = new GameOverWindow(Skins.neonSkin, playScreen);
        levelCompletedWindow = new LevelCompletedWindow(Skins.neonSkin, playScreen);
        Gdx.input.setInputProcessor(stage);
    }

    public void update(float dt) {
        if(toast.isShowing())
            toast.update(dt);
        editorWindow.update(dt);
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

    static void showAndroidInputTable() {
        if(!MyGdxGame.platformDepended.deviceHasKeyboard())
            androidInputTable.setVisible(true);
    }

    static void hideAndroidInputTable() {
        if(!MyGdxGame.platformDepended.deviceHasKeyboard())
            androidInputTable.setVisible(false);
    }

    public void showPauseWindow() {
        stage.addActor(pauseWindow);
    }

    public void showEditorWindow(Pc pc) {
        editorWindow.show(pc);
    }

    public void showInfoWindow(String name, String text) {
        infoDialog = new Dialog("", Skins.skin, "dialog") {
            public void result(Object obj) {
                closeCurrentInfo();
            }
        };
        currentInfoSignName = name;
        TextButton dummy = new TextButton("", Skins.neonSkin);
        infoDialog.button("     OK     ", true, dummy.getStyle()).setHeight(100); //sends "true" as the result
        infoDialog.key(Input.Keys.ENTER, true); //sends "true" when the ENTER key is pressed
        infoDialog.text(text);
        infoDialog.show(stage);
        //close infoDialog if user clicks outside of it
        infoDialog.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(x<0 || y<0 || x>infoDialog.getWidth() || y>infoDialog.getHeight())
                    closeCurrentInfo();
            }
        });
    }

    public void showGameOverWindow() {
        stage.addActor(gameOverWindow);
        editorWindow.closeCurrentEditor();
        hideAndroidInputTable();
    }

    public void showLevelCompletedWindow() {
        stage.addActor(levelCompletedWindow);
        hideAndroidInputTable();
    }

    public void closeCurrentInfo() {
        if(infoDialog.getStage()!=null)
            infoDialog.remove();
        playScreen.getPlayer().setCurrentState(Player.State.STANDING);

        playScreen.setEnterKeyHandled(true);
        if(currentInfoSignName.equals("info-1_1-0") || currentInfoSignName.equals("info-7_1-0"))
            playScreen.getDoors().get(0).open();
        else if(currentInfoSignName.equals("info-1_1-1") || currentInfoSignName.equals("info-7_1-1"))
            playScreen.getDoors().get(1).open();
        else if(currentInfoSignName.equals("info-1_1-2") || currentInfoSignName.equals("info-7_1-2"))
            playScreen.getDoors().get(2).open();
        else if(currentInfoSignName.equals("info-7_1-3"))
            playScreen.getDoors().get(3).open();
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

    public boolean isPauseWindowShowing() {
        return pauseWindow!=null && pauseWindow.getStage() != null;
    }

    public Viewport getViewport() {
        return viewport;
    }

    public PauseWindow getPauseWindow() {
        return pauseWindow;
    }

    public EditorWindow getEditorWindow() {
        return editorWindow;
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

    public void setUseBtnPressed() {
        if(!MyGdxGame.platformDepended.deviceHasKeyboard())
            this.useBtnPressed = false;
    }

    public void setJumpBtnPressed() {
        if(!MyGdxGame.platformDepended.deviceHasKeyboard())
            this.jumpBtnPressed = false;
    }
}
