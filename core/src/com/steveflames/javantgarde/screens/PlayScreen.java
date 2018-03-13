package com.steveflames.javantgarde.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.TimeUtils;
import com.steveflames.javantgarde.MyGdxGame;
import com.steveflames.javantgarde.hud.Hud;
import com.steveflames.javantgarde.sprites.Door;
import com.steveflames.javantgarde.sprites.Item;
import com.steveflames.javantgarde.sprites.Player;
import com.steveflames.javantgarde.tools.Assets;
import com.steveflames.javantgarde.tools.B2WorldContactListener;
import com.steveflames.javantgarde.tools.B2WorldCreator;
import com.steveflames.javantgarde.tools.GameObjectManager;
import com.steveflames.javantgarde.tools.InputHandler;
import com.steveflames.javantgarde.tools.global.Cameras;
import com.steveflames.javantgarde.tools.global.Fonts;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

/**
 * This class holds the main loop and processing of the game.
 * One of the most critical parts of the game.
 */
public class PlayScreen implements Screen{

    private static final int GRAVITY = -Math.round(4000/MyGdxGame.PPM);
    private MyGdxGame game;
    private LevelListItem currentLevel;
    private Hud hud; //head-up display
    private World world; //box2D variable
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    //fixed timestep with interpolation variables
    private double accumulator = 0;
    private final float step = 0.0133f; //logic updates 1/75f
    //public static final long RENDERER_SLEEP_MS = 0; // 34 -> limits to 30 fps, 30 -> 34 fps, 22 gives ~46 FPS, 20 = 100, 10 = 50
    //private long now2, diff, start; //variables to utilize fps limit

    //**************DEBUG VARIABLES**************
    /*private final static int logic_FPSupdateIntervall = 1;
    private long logic_lastRender;
    private long logic_now;
    private int logic_frameCount = 0;
    private int logic_lastFPS = 0; //logic frames per second
    private Box2DDebugRenderer b2dr; //draws the outline on every object for debugging
    private FPSLogger fpsLogger; //display game loop real frames per second
    private GLProfiler profiler;
    */
    //*******************************************

    //world bodies
    private GameObjectManager objectManager;

    //input
    private InputHandler inputHandler;
    private boolean enterKeyHandled = false;

    //onScreenMsg - print the level name when the level starts
    private long onScreenMsgMillis = TimeUtils.millis();
    private float onScreenMsgAlpha = 1;
    private GlyphLayout onScreenMsgGlyphLayout1 = new GlyphLayout();
    private GlyphLayout onScreenMsgGlyphLayout2 = new GlyphLayout();

    private boolean restartLevel = false; //prevents unloading assets if user chooses 'restart' or 'next' level
    private boolean gameOver = false;


    public PlayScreen(MyGdxGame game, LevelListItem level, TiledMap map, OrthogonalTiledMapRenderer renderer) {
        this.game = game;
        this.map = map;
        this.renderer = renderer;
        Gdx.input.setCatchBackKey(true);
        currentLevel = level;
        currentLevel.setName(currentLevel.getName().replaceAll("\n", " "));
        onScreenMsgGlyphLayout1.setText(Fonts.medium, currentLevel.getName());
        onScreenMsgGlyphLayout2.setText(Fonts.big, currentLevel.getCategoryName());

        //***********************DEBUG VARIABLES***********************
        //fpsLogger = new FPSLogger();
        //b2dr = new Box2DDebugRenderer();
        //profiler = new GLProfiler(Gdx.graphics);
        //*************************************************************

        game.assets.refreshPlayScreenAssets();
        hud = new Hud(this, game.sb); //initialize hud

        //create world
        world = new World(new Vector2(0, GRAVITY), true);
        objectManager = new GameObjectManager();
        world.setContactListener(new B2WorldContactListener(hud, objectManager, getAssets()));
        new B2WorldCreator(this); //initialize world
        getObjectManager().addPlayer(new Player(world, map, getObjectManager().getCheckpoints(), getAssets()));; //initialize player

        //initialize inputHandler
        inputHandler = new InputHandler(this);

        hud.initAfterWorldCreation(); //hud initialization after world creation

        //play music
        game.assets.playPlayScreenMusic();

        if(!MyGdxGame.platformDepended.deviceHasKeyboard())
            hud.newAndroidInputTable();

        //fixes html sound delay bug
        if(Gdx.app.getType()== Application.ApplicationType.WebGL) { //web specific
            for(int i=0; i<6; i++)
                game.assets.playAllPlayScreenSoundsMuted();
        }
    }

    public void render(float dt) {
        if (dt > 0.25f) dt = 0.25f; //max frame time to avoid spiral of death

        //logic_frameCount = 0; //DEBUG

        //*******************FIXED TIMESTEP WITH INTERPOLATION METHOD*******************
        accumulator += dt;
        while (accumulator >= step) {
            objectManager.copyCurrentPosition(); //INTERPOLATION
            updatePhysics(step);
            accumulator -= step;
            objectManager.interpolateCurrentPosition((float)(accumulator / step)); //INTERPOLATION

            // FPS check
            /*logic_frameCount ++;
            logic_now = System.nanoTime();
            if ((logic_now - logic_lastRender) >= logic_FPSupdateIntervall * 1000000000)  {
                logic_lastFPS = logic_frameCount / logic_FPSupdateIntervall;
                logic_frameCount = 0;
                logic_lastRender = System.nanoTime();
            }*/
        }
        //System.out.println("LAST FPS: " + logic_lastFPS); //DEBUG
        //System.out.println("FRAME COUNT: " + logic_frameCount); //DEBUG
        update();
        rendering();
        enterKeyHandled = false;
        objectManager.destroyUnusedBodies();
    }

    /**
     * Updates the game's physics.
     * @param dt -> delta time
     */
    private void updatePhysics(float dt) {
        if(!hud.isPauseWindowShowing()) {
            //update the onScreenMsg
            if (onScreenMsgAlpha > 0) {
                if (TimeUtils.timeSinceMillis(onScreenMsgMillis) > 2000)
                    onScreenMsgAlpha -= 1f * dt;
            }

            //update HUD
            hud.update(dt);

            //update all game objects
            for(int i=0; i < objectManager.getGameObjects().size(); i++)
                objectManager.getGameObjects().get(i).update(dt);

            //TIMESTEP
            world.step(dt, 6, 2);
        }
    }

    private void update() {
        if(!gameOver) {
            if (!hud.isPauseWindowShowing()) { //game not paused
                inputHandler.handleInput();

                if (getPlayer().getCurrentState() == Player.State.DISAPPEARED) {
                    //getPlayer().setCurrentState(Player.State.READING);
                    gameOver = true;
                    hud.showLevelCompletedWindow();
                    getAssets().playSound(getAssets().levelCompletedSound);
                } else if (getPlayer().getCurrentState() == Player.State.DEAD) {
                    //getPlayer().setCurrentState(Player.State.READING);
                    gameOver = true;
                    hud.showGameOverWindow();
                    getAssets().playSound(getAssets().deadSound);
                } else if (getPlayer().getCurrentState() != Player.State.CODING)
                    Cameras.updateCameraPosition(getPlayer());

                if (getPlayer().isOutOfBounds()  ) {
                    getPlayer().setCurrentState(Player.State.STANDING);
                    if (getPlayer().getPlayerMsgAlpha() == 1)
                        getPlayer().respawnAtCheckpoint(objectManager.getRopes());
                }
            } else //game paused
                hud.getPauseWindow().handleExitFromPauseMenuInput();
        }

        Cameras.playScreenCam.update();
    }

    private void rendering() {
        if(game.gameMinimized)
            game.drawMinimized();
        else {
            //render the game map
            renderer.setView(Cameras.playScreenCam);
            renderer.render();


            //************set projection matrix UNSCALED (1280x768)************
            game.sb.setProjectionMatrix(hud.stage.getCamera().combined);
            game.sr.setProjectionMatrix(hud.stage.getCamera().combined);

            //draw textures and fonts in background (some fonts must be behind the player (different layout)) (unscaled)
            game.sb.begin();
            for (int i = 0; i < objectManager.getRopes().size(); i++)
                if (Cameras.inLineOfSight(objectManager.getRopes().get(i)))
                    objectManager.getRopes().get(i).drawFontInBackground(game.sb);
            for (int i = 0; i < objectManager.getQuizes().size(); i++)
                if (Cameras.inLineOfSight(objectManager.getQuizes().get(i)))
                    objectManager.getQuizes().get(i).drawFontInBackground(game.sb);
            for (int i = 0; i < objectManager.getMarkers().size(); i++)
                if (Cameras.inLineOfSight(objectManager.getMarkers().get(i)))
                    objectManager.getMarkers().get(i).drawFontInBackground(game.sb);
            game.sb.end();

            //disable alpha
            Gdx.gl.glDisable(GL20.GL_BLEND);


            //***********set projection matrix according to world SCALE (1280x768 / 200)***********
            game.sb.setProjectionMatrix(Cameras.playScreenCam.combined);
            game.sr.setProjectionMatrix(Cameras.playScreenCam.combined);
            //enable alpha
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

            //draw textures and fonts (scaled)
            game.sb.setColor(Color.WHITE);
            game.sb.begin();
            objectManager.drawFontScaled(game.sb);
            game.sb.end();

            //disable alpha
            Gdx.gl.glDisable(GL20.GL_BLEND);


            //***********change projection matrix UNSCALED (1280x768)************
            game.sb.setProjectionMatrix(hud.stage.getCamera().combined);
            game.sr.setProjectionMatrix(hud.stage.getCamera().combined);
            //enable alpha
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

            //draw filled shapes (unscaled)
            game.sr.begin(ShapeRenderer.ShapeType.Filled);
            objectManager.drawFilled(game.sr);
            game.sr.end();

            //draw line shapes (unscaled)
            game.sr.begin(ShapeRenderer.ShapeType.Line);
            objectManager.drawLine(game.sr);
            game.sr.end();

            //draw fonts and textures (unscaled)
            game.sb.begin();
            objectManager.drawFont(game.sb);
            //draw the use item prompts
            if (getPlayer().getCurrentState() != Player.State.READING && getPlayer().getCurrentState() != Player.State.CODING) { //if player is coding or reading sign, dont draw the use prompt
                for (int i = 0; i < objectManager.getInfoSigns().size(); i++)
                    objectManager.getInfoSigns().get(i).drawUsePrompt(game.sb);
                for (int i = 0; i < objectManager.getPcs().size(); i++)
                    objectManager.getPcs().get(i).drawUsePrompt(game.sb);
                for (int i = 0; i < objectManager.getLevers().size(); i++)
                    objectManager.getLevers().get(i).drawUsePrompt(game.sb);
            }
            hud.drawFont(game.sb);
            drawOnScreenMsg();
            getPlayer().drawPlayerMsg(game.sb);
            Fonts.medium.setColor(Color.RED);
            game.sb.end();

            //draw filled over line (unscaled)
            game.sr.begin(ShapeRenderer.ShapeType.Filled);
            hud.drawFilled(game.sr);
            game.sr.end();

            //draw toast font
            game.sb.begin();
            hud.drawToastFont(game.sb);
            game.sb.end();

            //disable alpha
            Gdx.gl.glDisable(GL20.GL_BLEND);

            //draw HUD stage
            hud.drawStage(game.sb);


            //*************************DEBUG*************************
            //b2dr.render(world, Cameras.playScreenCam.combined);
            //fpsLogger.log();
            /*
            System.out.println("GL calls: " + profiler.getCalls());
            System.out.println("GL drawCalls: " + profiler.getDrawCalls());
            System.out.println("GL shaderSwitches: " + profiler.getShaderSwitches());
            System.out.println("GL textureBindings: " + profiler.getTextureBindings());
            System.out.println("GL vertexCount: " + profiler.getVertexCount());
            profiler.reset();
            */

            //***********************LIMIT FPS***********************
            /*if (RENDERER_SLEEP_MS > 0) {
                now2 = System.currentTimeMillis();
                diff = now2 - start;

                if (diff < RENDERER_SLEEP_MS) {
                    try {
                        Thread.sleep(RENDERER_SLEEP_MS - diff);
                    } catch (InterruptedException e) {
                    }
                }
                start = System.currentTimeMillis();
            }*/
        }
    }

    private void drawOnScreenMsg() {
        if(onScreenMsgAlpha>0) {
            Fonts.big.setColor(1, 0, 0, onScreenMsgAlpha);
            Fonts.medium.setColor(1, 0, 0, onScreenMsgAlpha);
            Fonts.big.draw(game.sb, currentLevel.getCategoryName(), Cameras.playScreenCam.viewportWidth/2 * MyGdxGame.PPM - onScreenMsgGlyphLayout2.width / 2, Cameras.playScreenCam.viewportHeight/2 * MyGdxGame.PPM + 110);
            Fonts.medium.draw(game.sb, currentLevel.getName(), Cameras.playScreenCam.viewportWidth/2 * MyGdxGame.PPM - onScreenMsgGlyphLayout1.width / 2, Cameras.playScreenCam.viewportHeight/2 * MyGdxGame.PPM + 15);
        }
    }

    @Override
    public void resize(int width, int height) {
        Cameras.playScreenPort.update(width, height);
        Cameras.hudPort.update(width, height);
    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {
        game.gameMinimized = true;
        if(getPlayer().canMove)
            hud.showPauseWindow();
        getAssets().unloadAllPlayScreenAssets();
        //getAssets().unloadSkins();
        getAssets().stopAllPlayScreenAudio();
        getAssets().unloadPlayScreenBundles();
    }

    @Override
    public void resume() {
        getAssets().playPlayScreenMusic();
        getAssets().loadAllPlayScreenAssets();
        getAssets().finishLoading();
        game.assets.refreshPlayScreenAssets();
        game.gameMinimized = false;

        //update once to refresh each currentTR
        float dt = Gdx.graphics.getDeltaTime();
        getPlayer().update(dt);
        objectManager.getTeleporter().update(dt);
        hud.update(dt);
        for(int i=0; i<objectManager.getSensorRobots().size(); i++)
            objectManager.getSensorRobots().get(i).update(dt);
        game.assets.loadPlayScreenBundles(game.preferences.getLanguage());
        //hud.recreateUI();
    }

    @Override
    public void dispose() { //dispose unused assets
        game.assets.stopAllPlayScreenAudio();
        if(!restartLevel) {
            game.assets.unloadAllPlayScreenAssets();
            map.dispose();
            renderer.dispose();
            game.assets.unloadPlayScreenBundles();
        }
        world.dispose();
        hud.dispose();
        Item.reset();
        //b2dr.dispose(); //DEBUG

        for(Door door: objectManager.getDoors())
            door.reset();
        objectManager.clearGameObjects();
    }

    public TiledMap getMap() {
        return map;
    }

    public World getWorld() {
        return world;
    }

    public LevelListItem getCurrentLevel() {
        return currentLevel;
    }

    public Hud getHud() {
        return hud;
    }

    public GameObjectManager getObjectManager() {
        return objectManager;
    }

    public Player getPlayer() {
        return objectManager.getPlayer();
    }

    public String getCurrentLevelID() {
        return currentLevel.getId();
    }

    public MyGdxGame getGame() {
        return game;
    }

    public void setEnterKeyHandled(boolean enterKeyHandled) {
        this.enterKeyHandled = enterKeyHandled;
    }

    public boolean isEnterKeyHandled() {
        return enterKeyHandled;
    }

    public Assets getAssets() {
        return game.assets;
    }

    public void setRestartLevel() {
        this.restartLevel = true;
    }

    public OrthogonalTiledMapRenderer getRenderer() {
        return renderer;
    }
}
