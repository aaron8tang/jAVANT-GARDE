package com.steveflames.javantgarde.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
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
import com.steveflames.javantgarde.MyGdxGame;
import com.steveflames.javantgarde.hud.code_pc.EditorWindow;
import com.steveflames.javantgarde.hud.navigation_windows.GameOverWindow;
import com.steveflames.javantgarde.hud.navigation_windows.LevelCompletedWindow;
import com.steveflames.javantgarde.hud.navigation_windows.PauseWindow;
import com.steveflames.javantgarde.hud.order_pc.EditorOrderWindow;
import com.steveflames.javantgarde.hud.quiz_pc.EditorQuizWindow;
import com.steveflames.javantgarde.screens.PlayScreen;
import com.steveflames.javantgarde.sprites.InfoSign;
import com.steveflames.javantgarde.sprites.Item;
import com.steveflames.javantgarde.sprites.Pc;
import com.steveflames.javantgarde.sprites.Player;
import com.steveflames.javantgarde.tools.Assets;
import com.steveflames.javantgarde.tools.global.Cameras;
import com.steveflames.javantgarde.tools.global.Fonts;


/**
 * Created by Flames on 9/4/16.
 */
public class Hud implements Disposable {

    public PlayScreen playScreen;
    private Toast toast;

    //hud components
    public Stage stage;
    private EditorWindow editorWindow;
    private EditorQuizWindow editorQuizWindow;
    private EditorOrderWindow editorOrderWindow;
    private PauseWindow pauseWindow;
    private Table gameOverWindow;
    private Table levelCompletedWindow;
    private Dialog infoDialog;

    private InfoSign currentInfoSign;

    //android input table
    private Table androidInputTable;
    private TextButton useBtn;
    private boolean leftBtnPressed = false;
    private boolean rightBtnPressed = false;
    private boolean useBtnPressed = false;
    private boolean jumpBtnPressed = false;

    private Assets assets;

    public Hud(final PlayScreen playScreen, SpriteBatch sb) {
        this.playScreen = playScreen;
        this.assets = playScreen.getAssets();
        stage = new Stage(Cameras.hudPort, sb);
        toast = new Toast((playScreen.getAssets()));
        editorWindow = new EditorWindow("EDITOR", playScreen.getAssets(), this);
        editorQuizWindow = new EditorQuizWindow("EDITOR", playScreen);
        editorOrderWindow = new EditorOrderWindow("EDITOR", playScreen);
        pauseWindow = new PauseWindow("GAME PAUSED", playScreen);
        gameOverWindow = new GameOverWindow(playScreen);
        levelCompletedWindow = new LevelCompletedWindow(playScreen);
        Gdx.input.setInputProcessor(stage);
    }

    public void update(float dt) {
        if(toast.isShowing())
            toast.update(dt, this);
        editorWindow.update(dt);
        editorQuizWindow.update(dt);
        editorOrderWindow.update(dt);
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
            if(Item.getnOfClasses() > 0) {
                Fonts.small.setColor(Color.WHITE);
                Fonts.small.draw(sb, "Classes found: " + playScreen.getPlayer().getClasses().size() +"/" + Item.getnOfClasses(), 15, Cameras.hudPort.getCamera().viewportHeight - 67);
            }
            for(int i = 0; i< playScreen.getPlayer().getHealth(); i++)
                sb.draw(assets.heartTR, 20 +(60*i), Cameras.hudPort.getCamera().viewportHeight - 60, 50, 50);
        }

        if(toast.isShowing())
            toast.drawFont(sb);
    }

    public void newToast(String text) {
        toast.newToast(text);
        hideAndroidInputTable();
    }

    public void newAndroidInputTable() {
        androidInputTable = new Table(playScreen.getAssets().neonSkin);
        androidInputTable.setSize(Cameras.hudPort.getCamera().viewportWidth, 120);
        final TextButton leftBtn = new TextButton("<", playScreen.getAssets().neonSkin);
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
        final TextButton rightBtn = new TextButton(">", playScreen.getAssets().neonSkin);
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
        useBtn = new TextButton("USE", playScreen.getAssets().neonSkin);
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
        TextButton jumpBtn = new TextButton("JUMP", playScreen.getAssets().neonSkin);
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


        Table leftTable = new Table(playScreen.getAssets().neonSkin);
        leftTable.add(leftBtn).left().width(150).height(120).expandX();
        leftTable.add(rightBtn).left().width(150).height(120).expandX();
        Table rightTable = new Table(playScreen.getAssets().neonSkin);
        rightTable.add(useBtn).right().width(200).height(120).expandX();
        rightTable.add(jumpBtn).right().width(200).height(120).expandX();


        androidInputTable.add(leftTable).expand().left();
        androidInputTable.add(rightTable).expand().right();
        stage.addActor(androidInputTable);
        Gdx.input.setInputProcessor(stage);
        useBtn.setVisible(false);
    }

    public void showAndroidInputTable() {
        if(!MyGdxGame.platformDepended.deviceHasKeyboard())
            androidInputTable.setVisible(true);
    }

    public void hideAndroidInputTable() {
        if(!MyGdxGame.platformDepended.deviceHasKeyboard())
            androidInputTable.setVisible(false);
    }

    public void showPauseWindow() {
        stage.addActor(pauseWindow);
    }

    public void showEditorWindow(Pc pc) {
        assets.playSound(assets.useItemSound);
        editorWindow.show(pc);
    }

    public void showEditorQuizWindow(Pc pc) {
        assets.playSound(assets.useItemSound);
        editorQuizWindow.show(pc);
    }

    public void showEditorOrderWindow(Pc pc) {
        assets.playSound(assets.useItemSound);
        editorOrderWindow.show(pc);
    }

    public void showInfoWindow(InfoSign infoSign, String text) {
        assets.playSound(assets.useItemSound);
        hideAndroidInputTable();
        infoDialog = new Dialog("", playScreen.getAssets().terraSkin, "dialog") {
            public void result(Object obj) {
                closeCurrentInfo();
                showAndroidInputTable();
            }
        };
        currentInfoSign = infoSign;
        TextButton dummy = new TextButton("", playScreen.getAssets().neonSkin);
        infoDialog.button("     OK     ", true, dummy.getStyle()).setHeight(100); //sends "true" as the result
        infoDialog.key(Input.Keys.ENTER, true); //sends "true" when the ENTER key is pressed
        infoDialog.text(text);
        infoDialog.show(stage);
        //close infoDialog if user clicks outside of it
        infoDialog.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(x<0 || y<0 || x>infoDialog.getWidth() || y>infoDialog.getHeight()) {
                    closeCurrentInfo();
                    showAndroidInputTable();
                }
            }
        });
    }

    public void showGameOverWindow() {
        assets.stopPlayScreenMusic();
        stage.addActor(gameOverWindow);
        editorWindow.closeCurrentEditor();
        hideAndroidInputTable();
    }

    public void showLevelCompletedWindow() {
        assets.stopPlayScreenMusic();
        assets.playSound(assets.levelCompletedSound);
        stage.addActor(levelCompletedWindow);
        hideAndroidInputTable();
    }

    public void closeCurrentInfo() {
        assets.playSound(assets.clickSound);
        if(infoDialog.getStage()!=null)
            infoDialog.remove();
        playScreen.getPlayer().setCurrentState(Player.State.STANDING);

        playScreen.setEnterKeyHandled(true);
        currentInfoSign.setRead(playScreen.getObjectManager().getDoors());
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

    public void handleToastNextPressed() {
        toast.handleNextPressed();
    }

    public boolean isToastShowing() {
        return toast.isShowing();
    }

    public EditorQuizWindow getEditorQuizWindow() {
        return editorQuizWindow;
    }

    public EditorOrderWindow getEditorOrderWindow() {
        return editorOrderWindow;
    }
}
