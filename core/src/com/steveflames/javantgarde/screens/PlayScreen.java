package com.steveflames.javantgarde.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.steveflames.javantgarde.MyGdxGame;
import com.steveflames.javantgarde.hud.Hud;
import com.steveflames.javantgarde.sprites.*;
import com.steveflames.javantgarde.sprites.ropes.Rope;
import com.steveflames.javantgarde.tools.B2WorldContactListener;
import com.steveflames.javantgarde.tools.B2WorldCreator;
import com.steveflames.javantgarde.tools.GameObjectManager;
import com.steveflames.javantgarde.tools.InputHandler;
import com.steveflames.javantgarde.tools.LevelListItem;
import com.steveflames.javantgarde.tools.global.Cameras;
import com.steveflames.javantgarde.tools.global.Fonts;

import java.util.ArrayList;

/**
 * Created by Flames on 23/9/2017.
 */

public class PlayScreen implements Screen{

    private MyGdxGame game;
    private static float WIDTH;
    private static float HEIGHT;
    private static final int GRAVITY = -20;
    public static LevelListItem currentLevel;

    //fixed timestep
    private double accumulator = 0;
    private final float delta = 0.0133f;	// logic updates approx. @ 75 hz //todo 0.0133f   todo maybe 45f or 300f (recommended)
    private final static int logic_FPSupdateIntervall = 1;
    private long logic_lastRender;
    private long logic_now;
    private int logic_frameCount = 0;
    private int logic_lastFPS = 0;

    private Hud hud;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    //Box2d variables
    private World world;
    private Box2DDebugRenderer b2dr;
    private FPSLogger fpsLogger;

    private GameObjectManager objectManager = new GameObjectManager();
    //world bodies
    private ArrayList<GameObject> objectsToRemove = new ArrayList<GameObject>();
    private Player player;
    private ArrayList<Pc> pcs = new ArrayList<Pc>();
    private ArrayList<InfoSign> infoSigns = new ArrayList<InfoSign>();
    private ArrayList<Door> doors = new ArrayList<Door>();
    private ArrayList<Rope> ropes = new ArrayList<Rope>();
    private ArrayList<Item> items = new ArrayList<Item>();
    private ArrayList<Checkpoint> checkpoints = new ArrayList<Checkpoint>();
    private ArrayList<FloatingPlatform> floatingPlatforms = new ArrayList<FloatingPlatform>();
    private ArrayList<Lever> levers = new ArrayList<Lever>();
    private ArrayList<Rectangle> markers = new ArrayList<Rectangle>();
    private ArrayList<Quiz> quizes = new ArrayList<Quiz>();
    private Teleporter teleporter;

    //input
    private InputHandler inputHandler;
    private boolean enterKeyHandled = false;

    //onScreenMsg
    private long onScreenMsgMillis = 0; //is used to print the level name when the level starts
    private float onScreenMsgAlpha = 1;
    private GlyphLayout onScreenMsgGlyphLayout1 = new GlyphLayout();
    private GlyphLayout onScreenMsgGlyphLayout2 = new GlyphLayout();


    public PlayScreen(MyGdxGame game, LevelListItem level) {
        this.game = game;
        Gdx.input.setCatchBackKey(true);
        currentLevel = level;
        currentLevel.setName(currentLevel.getName().replaceAll("\n", " "));
        onScreenMsgGlyphLayout1.setText(Fonts.medium, currentLevel.getName());
        onScreenMsgGlyphLayout2.setText(Fonts.big, currentLevel.getCategoryName());
        ropes = new ArrayList<Rope>();

        inputHandler = new InputHandler(this);
        fpsLogger = new FPSLogger();
        //GLProfiler.enable();

        //initialize map
        TmxMapLoader mapLoader = new TmxMapLoader();
        map = mapLoader.load("tiled/level-"+level.getId()+".tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / MyGdxGame.PPM);
        setMapProperties(map);

        //initialize camera and viewport
        Cameras.load(WIDTH, HEIGHT);

        world = new World(new Vector2(0, GRAVITY), true);
        b2dr = new Box2DDebugRenderer();

        new B2WorldCreator(this); //create world
        player = new Player(world, checkpoints);
        objectManager.addGameObject(player);
        hud = new Hud(this, game.sb);

        world.setContactListener(new B2WorldContactListener(this));
        onScreenMsgMillis = TimeUtils.millis();

        if(!MyGdxGame.platformDepended.deviceHasKeyboard())
            hud.newAndroidInputTable();
        Quiz.setHud(hud);

        //hud.showEditorWindow("1_1-0");
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

        //System.out.println("DT: " +dt); //debug
        //FIXED TIMESTEP METHOD
        accumulator += dt;
        while (accumulator >= delta) {
            objectManager.copyCurrentPosition(); //INTERPOLATION
            updatePhysics(delta);
            accumulator -= delta;
            objectManager.interpolateCurrentPosition((float)(accumulator / delta)); //INTERPOLATION

            // FPS check
            logic_frameCount ++;
            logic_now = System.nanoTime();
            if ((logic_now - logic_lastRender) >= logic_FPSupdateIntervall * 1000000000)  {
                logic_lastFPS = logic_frameCount / logic_FPSupdateIntervall;
                logic_frameCount = 0;
                logic_lastRender = System.nanoTime();
            }
        }
        //System.out.println("LOGIC: " + logic_lastFPS); //debug
        update();
        rendering();
        enterKeyHandled = false;
        destroyUnusedBodies();
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

            if (player.getCurrentState() == Player.State.DISAPPEARED)
                hud.showLevelCompletedWindow();
            else if (player.getCurrentState() == Player.State.DEAD)
                hud.showGameOverWindow();
            else if (player.getCurrentState() != Player.State.CODING)
                Cameras.updateCameraPosition(player);

            if (player.isOutOfBounds() && player.getPlayerMsgAlpha() == 1)
                player.respawnAtCheckpoint(ropes);
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
        for (int i = 0; i < ropes.size(); i++)
            if (Cameras.inLineOfSight(ropes.get(i)))
                ropes.get(i).drawFontInBackground(game.sb);
        for (int i = 0; i < quizes.size(); i++)
            if (Cameras.inLineOfSight(quizes.get(i)))
                quizes.get(i).drawFontInBackground(game.sb);
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
        for(int i=0; i<objectManager.getGameObjects().size(); i++)
            if (Cameras.inLineOfSight(objectManager.getGameObjects().get(i)))
                objectManager.getGameObjects().get(i).drawFontScaled(game.sb);
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
        for(int i=0; i<objectManager.getGameObjects().size(); i++)
            if(Cameras.inLineOfSight(objectManager.getGameObjects().get(i)))
                objectManager.getGameObjects().get(i).drawFilled(game.sr);
        game.sr.end();

        //draw line shapes (unscaled)
        game.sr.begin(ShapeRenderer.ShapeType.Line);

        for(int i=0; i<objectManager.getGameObjects().size(); i++)
            if(Cameras.inLineOfSight(objectManager.getGameObjects().get(i)))
                objectManager.getGameObjects().get(i).drawLine(game.sr);
        game.sr.end();

        //draw filled over line (unscaled)
        game.sr.begin(ShapeRenderer.ShapeType.Filled);
        hud.drawFilled(game.sr);
        game.sr.end();

        //draw fonts and textures (unscaled)
        game.sb.begin();
        for(int i=0; i<objectManager.getGameObjects().size(); i++)
            if(Cameras.inLineOfSight(objectManager.getGameObjects().get(i)))
                objectManager.getGameObjects().get(i).drawFont(game.sb);
        //draw the use item prompts
        if (player.getCurrentState() != Player.State.READING && player.getCurrentState() != Player.State.CODING) { //if player is coding or reading sign, dont draw the use prompt
            for (int i = 0; i < infoSigns.size(); i++)
                infoSigns.get(i).drawUsePrompt(game.sb);
            for (int i = 0; i < pcs.size(); i++)
                pcs.get(i).drawUsePrompt(game.sb);
            for (int i = 0; i < levers.size(); i++)
                levers.get(i).drawUsePrompt(game.sb);
        }
        hud.drawFont(game.sb);
        drawOnScreenMsg();
        player.drawPlayerMsg(game.sb);
        game.sb.end();

        //disable alpha
        Gdx.gl.glDisable(GL20.GL_BLEND);

        //draw HUD stage
        hud.drawStage(game.sb);


        //*************************DEBUG*************************
        //b2dr.render(world, cam.combined);
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

    private void destroyUnusedBodies() {
        for(int i = 0; i< objectsToRemove.size(); i++) {
            objectManager.removeGameObject(objectsToRemove.get(i));
            world.destroyBody(objectsToRemove.get(i).b2body);
        }
        objectsToRemove.clear();
    }

    public void pause() {
        hud.showPauseWindow();
    }

    public void resume() {}

    /**
     * Dispose the unused variables.
     */
    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
        ropes.clear();
        Item.reset();

        for(int i=0; i<objectManager.getGameObjects().size(); i++)
            objectManager.clearGameObjects();
    }

    public Player getPlayer() {
        return player;
    }

    public TiledMap getMap() {
        return map;
    }

    public World getWorld() {
        return world;
    }

    public ArrayList<Pc> getPcs() {
        return pcs;
    }

    public ArrayList<InfoSign> getInfoSigns() {
        return infoSigns;
    }

    public ArrayList<Rope> getRopes() {
        return ropes;
    }

    public ArrayList<Checkpoint> getCheckpoints() {
        return checkpoints;
    }

    public LevelListItem getCurrentLevel() {
        return currentLevel;
    }

    public ArrayList<Door> getDoors() {
        return doors;
    }

    public void setEnterKeyHandled(boolean enterKeyHandled) {
        this.enterKeyHandled = enterKeyHandled;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public ArrayList<GameObject> getObjectsToRemove() {
        return objectsToRemove;
    }

    public Hud getHud() {
        return hud;
    }

    public void setTeleporter(Teleporter teleporter) {
        this.teleporter = teleporter;
    }

    public boolean isEnterKeyHandled() {
        return enterKeyHandled;
    }

    public ArrayList<FloatingPlatform> getFloatingPlatforms() {
        return floatingPlatforms;
    }

    public ArrayList<Rectangle> getMarkers() {
        return markers;
    }

    public MyGdxGame getGame() {
        return game;
    }

    public ArrayList<Lever> getLevers() {
        return levers;
    }

    public ArrayList<Quiz> getQuizes() {
        return quizes;
    }

    public GameObjectManager getObjectManager() {
        return objectManager;
    }

    public Teleporter getTeleporter() {
        return teleporter;
    }
}
