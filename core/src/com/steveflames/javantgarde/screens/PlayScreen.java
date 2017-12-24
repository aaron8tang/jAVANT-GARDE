package com.steveflames.javantgarde.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.TimeUtils;
import com.steveflames.javantgarde.MyGdxGame;
import com.steveflames.javantgarde.hud.Hud;
import com.steveflames.javantgarde.sprites.Item;
import com.steveflames.javantgarde.sprites.Player;
import com.steveflames.javantgarde.tools.Assets;
import com.steveflames.javantgarde.tools.B2WorldContactListener;
import com.steveflames.javantgarde.tools.B2WorldCreator;
import com.steveflames.javantgarde.tools.GameObjectManager;
import com.steveflames.javantgarde.tools.InputHandler;
import com.steveflames.javantgarde.tools.global.Cameras;
import com.steveflames.javantgarde.tools.global.Fonts;

/**
 * This class holds the main loop and processing of the game.
 */
public class PlayScreen implements Screen{

    private MyGdxGame game;
    private static float WIDTH; //width of the map
    private static float HEIGHT; //height of the map
    private static final int GRAVITY = -20;
    private LevelListItem currentLevel;
    private Hud hud;
    private World world; //box2D variable
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    //fixed timestep with interpolation variables
    private double accumulator = 0;
    private final float delta = 0.0133f; //logic updates 1/75f

    //**************DEBUG**************
    private final static int logic_FPSupdateIntervall = 1;
    private long logic_lastRender;
    private long logic_now;
    private int logic_frameCount = 0;
    private int logic_lastFPS = 0; //logic frames per second
    private Box2DDebugRenderer b2dr; //draws the outline on every object for debugging
    private FPSLogger fpsLogger; //display game loop real frames per second

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

    private boolean restartLevel = false;


    public PlayScreen(MyGdxGame game, LevelListItem level) {
        this.game = game;
        Gdx.input.setCatchBackKey(true);
        currentLevel = level;
        currentLevel.setName(currentLevel.getName().replaceAll("\n", " "));
        onScreenMsgGlyphLayout1.setText(Fonts.medium, currentLevel.getName());
        onScreenMsgGlyphLayout2.setText(Fonts.big, currentLevel.getCategoryName());

        //**************DEBUG**************
        fpsLogger = new FPSLogger();
        b2dr = new Box2DDebugRenderer();
        //GLProfiler.enable();

        //initialize map
        TmxMapLoader mapLoader = new TmxMapLoader();
        map = mapLoader.load("tiled/level-"+level.getId()+".tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / MyGdxGame.PPM);
        setMapProperties(map);

        //initialize camera and viewport
        Cameras.load(WIDTH, HEIGHT);

        //create world
        world = new World(new Vector2(0, GRAVITY), true);
        world.setContactListener(new B2WorldContactListener(this));
        objectManager = new GameObjectManager(this);
        new B2WorldCreator(this); //initialize world
        objectManager.initializePlayer(world, map); //initialize player
        hud = new Hud(this, game.sb); //initialize hud

        //initialize inputHandler
        inputHandler = new InputHandler(this);

        if(!MyGdxGame.platformDepended.deviceHasKeyboard())
            hud.newAndroidInputTable();

    }

    private void setMapProperties(TiledMap map) {
        MapProperties prop = map.getProperties();

        int mapWidth = prop.get("width", Integer.class);
        int mapHeight = prop.get("height", Integer.class);
        int tilePixelWidth = prop.get("tilewidth", Integer.class);
        int tilePixelHeight = prop.get("tileheight", Integer.class);

        WIDTH = mapWidth * tilePixelWidth / MyGdxGame.PPM;
        HEIGHT = mapHeight * tilePixelHeight / MyGdxGame.PPM;
    }

    public void render(float dt) {
        if (dt > 0.25f) dt = 0.25f; //max frame time to avoid spiral of death

        //System.out.println("DT: " +dt); //DEBUG
        //logic_frameCount = 0;

        //*************************FIXED TIMESTEP METHOD*************************
        accumulator += dt;
        while (accumulator >= delta) {
            objectManager.copyCurrentPosition(); //INTERPOLATION
            updatePhysics(delta);
            accumulator -= delta;
            objectManager.interpolateCurrentPosition((float)(accumulator / delta)); //INTERPOLATION

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
        if(!hud.isPauseWindowShowing()) { //game not paused
            inputHandler.handleInput();

            if (getPlayer().getCurrentState() == Player.State.DISAPPEARED)
                hud.showLevelCompletedWindow();
            else if (getPlayer().getCurrentState() == Player.State.DEAD)
                hud.showGameOverWindow();
            else if (getPlayer().getCurrentState() != Player.State.CODING)
                Cameras.updateCameraPosition(getPlayer());

            if (getPlayer().isOutOfBounds() && getPlayer().getPlayerMsgAlpha() == 1)
                getPlayer().respawnAtCheckpoint(objectManager.getRopes());
        }
        else //game paused
            hud.getPauseWindow().handleExitFromPauseMenuInput();

        Cameras.playScreenCam.update();
        renderer.setView(Cameras.playScreenCam);
    }

    private void rendering() {
        //render the game map
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

        //draw filled over line (unscaled)
        game.sr.begin(ShapeRenderer.ShapeType.Filled);
        hud.drawFilled(game.sr);
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
        game.sb.end();

        //disable alpha
        Gdx.gl.glDisable(GL20.GL_BLEND);

        //draw HUD stage
        hud.drawStage(game.sb);


        //*************************DEBUG*************************
        //b2dr.render(world, Cameras.playScreenCam.combined);
        //fpsLogger.log();
        /*System.out.println("GL calls: " + GLProfiler.calls);
        System.out.println("GL drawCalls: " + GLProfiler.drawCalls);
        System.out.println("GL shaderSwitches: " + GLProfiler.shaderSwitches);
        System.out.println("GL textureBindings: " + GLProfiler.textureBindings);
        System.out.println("GL vertexCount: " + GLProfiler.vertexCount);
        GLProfiler.reset();*/
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

    public void pause() {
        //getAssets().unloadAllPlayScreen();
        if(getPlayer().canMove)
            hud.showPauseWindow();
    }

    public void resume() {
        //getAssets().loadAllPlayScreen();
        //getAssets().finishLoading(); //todo not working prepei na ksanaperasw kathe texture ena ena
    }

    /**
     * Dispose the unused assets.
     */
    @Override
    public void dispose() {
        if(!restartLevel)
            game.assets.unloadAllPlayScreen();
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
        Item.reset();

        for(int i=0; i<objectManager.getGameObjects().size(); i++)
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
}
