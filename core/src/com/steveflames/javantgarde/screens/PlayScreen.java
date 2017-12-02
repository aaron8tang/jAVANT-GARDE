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

import java.util.ArrayList;

/**
 * Created by Flames on 23/9/2017.
 */

public class PlayScreen implements Screen{

    private com.steveflames.javantgarde.MyGdxGame game;
    private static float WIDTH;
    private static float HEIGHT;
    private static final int GRAVITY = -20;
    private static Viewport gamePort;
    public static OrthographicCamera cam;
    public static com.steveflames.javantgarde.tools.LevelListItem currentLevel;

    //fixed timestep
    private double accumulator = 0;
    private final float delta = 0.0133f;	// logic updates approx. @ 75 hz //todo 0.0133f   todo maybe 45f or 300f (recommended)
    private final static int logic_FPSupdateIntervall = 1;
    private long logic_lastRender;
    private long logic_now;
    private int logic_frameCount = 0;
    private int logic_lastFPS = 0;

    private com.steveflames.javantgarde.scenes.Hud hud;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    //Box2d variables
    private World world;
    private Box2DDebugRenderer b2dr;
    private FPSLogger fpsLogger;

    private com.steveflames.javantgarde.GameObjectManager objectManager = new com.steveflames.javantgarde.GameObjectManager();
    //world bodies
    private ArrayList<com.steveflames.javantgarde.sprites.GameObject> objectsToRemove = new ArrayList<com.steveflames.javantgarde.sprites.GameObject>();
    private com.steveflames.javantgarde.sprites.Player player;
    private ArrayList<com.steveflames.javantgarde.sprites.Pc> pcs = new ArrayList<com.steveflames.javantgarde.sprites.Pc>();
    private ArrayList<com.steveflames.javantgarde.sprites.InfoSign> infoSigns = new ArrayList<com.steveflames.javantgarde.sprites.InfoSign>();
    private ArrayList<com.steveflames.javantgarde.sprites.Door> doors = new ArrayList<com.steveflames.javantgarde.sprites.Door>();
    private ArrayList<com.steveflames.javantgarde.sprites.ropes.Rope> ropes = new ArrayList<com.steveflames.javantgarde.sprites.ropes.Rope>();
    private ArrayList<com.steveflames.javantgarde.sprites.Item> items = new ArrayList<com.steveflames.javantgarde.sprites.Item>();
    private ArrayList<com.steveflames.javantgarde.sprites.Checkpoint> checkpoints = new ArrayList<com.steveflames.javantgarde.sprites.Checkpoint>();
    private ArrayList<com.steveflames.javantgarde.sprites.FloatingPlatform> floatingPlatforms = new ArrayList<com.steveflames.javantgarde.sprites.FloatingPlatform>();
    private ArrayList<com.steveflames.javantgarde.sprites.Lever> levers = new ArrayList<com.steveflames.javantgarde.sprites.Lever>();
    private ArrayList<Rectangle> markers = new ArrayList<Rectangle>();
    private ArrayList<com.steveflames.javantgarde.quests.Quiz> quizes = new ArrayList<com.steveflames.javantgarde.quests.Quiz>();
    private com.steveflames.javantgarde.sprites.Teleporter teleporter;

    //input
    private com.steveflames.javantgarde.tools.InputHandler inputHandler;
    private boolean enterKeyHandled = false;

    //onScreenMsg
    private long onScreenMsgMillis = 0; //is used to print the level name when the level starts
    private float onScreenMsgAlpha = 1;
    private GlyphLayout onScreenMsgGlyphLayout1 = new GlyphLayout();
    private GlyphLayout onScreenMsgGlyphLayout2 = new GlyphLayout();


    public PlayScreen(com.steveflames.javantgarde.MyGdxGame game, com.steveflames.javantgarde.tools.LevelListItem level) {
        this.game = game;
        Gdx.input.setCatchBackKey(true);
        currentLevel = level;
        currentLevel.setName(currentLevel.getName().replaceAll("\n", " "));
        onScreenMsgGlyphLayout1.setText(com.steveflames.javantgarde.tools.global.Fonts.medium, currentLevel.getName());
        onScreenMsgGlyphLayout2.setText(com.steveflames.javantgarde.tools.global.Fonts.big, currentLevel.getCategoryName());
        ropes = new ArrayList<com.steveflames.javantgarde.sprites.ropes.Rope>();

        inputHandler = new com.steveflames.javantgarde.tools.InputHandler(this);
        fpsLogger = new FPSLogger();
        //GLProfiler.enable();

        //initialize camera and viewport
        cam = new OrthographicCamera();
        cam.setToOrtho(false, com.steveflames.javantgarde.MyGdxGame.WIDTH / com.steveflames.javantgarde.MyGdxGame.PPM, com.steveflames.javantgarde.MyGdxGame.HEIGHT / com.steveflames.javantgarde.MyGdxGame.PPM);
        gamePort = new StretchViewport(com.steveflames.javantgarde.MyGdxGame.WIDTH / com.steveflames.javantgarde.MyGdxGame.PPM, com.steveflames.javantgarde.MyGdxGame.HEIGHT / com.steveflames.javantgarde.MyGdxGame.PPM); //TODO: h mhpws fit

        //initialize map
        TmxMapLoader mapLoader = new TmxMapLoader();
        map = mapLoader.load("tiled/level-"+level.getId()+".tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / com.steveflames.javantgarde.MyGdxGame.PPM);
        setMapProperties(map);

        world = new World(new Vector2(0, GRAVITY), true);
        b2dr = new Box2DDebugRenderer();

        new com.steveflames.javantgarde.tools.B2WorldCreator(this); //create world
        player = new com.steveflames.javantgarde.sprites.Player(world, checkpoints);
        objectManager.addGameObject(player);
        hud = new com.steveflames.javantgarde.scenes.Hud(this, game.sb);

        world.setContactListener(new com.steveflames.javantgarde.tools.B2WorldContactListener(this));
        onScreenMsgMillis = TimeUtils.millis();

        if(!com.steveflames.javantgarde.MyGdxGame.platformDepended.deviceHasKeyboard())
            hud.newAndroidInputTable();
        com.steveflames.javantgarde.quests.Quiz.setHud(hud);

        //hud.showEditorWindow("1_1-0");
    }

    private void setMapProperties(TiledMap map) {
        MapProperties prop = map.getProperties();

        int mapWidth = prop.get("width", Integer.class);
        int mapHeight = prop.get("height", Integer.class);
        int tilePixelWidth = prop.get("tilewidth", Integer.class);
        int tilePixelHeight = prop.get("tileheight", Integer.class);

        WIDTH = mapWidth * tilePixelWidth / com.steveflames.javantgarde.MyGdxGame.PPM;
        HEIGHT = mapHeight * tilePixelHeight / com.steveflames.javantgarde.MyGdxGame.PPM;
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

            if (player.getCurrentState() == com.steveflames.javantgarde.sprites.Player.State.DISAPPEARED)
                hud.showLevelCompletedWindow();
            else if (player.getCurrentState() == com.steveflames.javantgarde.sprites.Player.State.DEAD)
                hud.showGameOverWindow();
            else if (player.getCurrentState() != com.steveflames.javantgarde.sprites.Player.State.CODING)
                updateCameraPosition();

            if (player.isOutOfBounds() && player.getPlayerMsgAlpha() == 1)
                player.respawnAtCheckpoint(ropes);
        }
        else //game paused
            hud.getPauseWindow().handleExitFromPauseMenuInput();

        cam.update();
        renderer.setView(cam);
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
            if (inLineOfSightX(ropes.get(i)))
                ropes.get(i).drawFontInBackground(game.sb);
        for (int i = 0; i < quizes.size(); i++)
            if (inLineOfSightX(quizes.get(i)))
                quizes.get(i).drawFontInBackground(game.sb);
        game.sb.end();

        //disable alpha
        Gdx.gl.glDisable(GL20.GL_BLEND);


        //***********set projection matrix according to world SCALE (1280x768 / 200)***********
        game.sb.setProjectionMatrix(cam.combined);
        game.sr.setProjectionMatrix(cam.combined);
        //enable alpha
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        //draw textures and fonts (scaled)
        game.sb.setColor(Color.WHITE);
        game.sb.begin();
        for(int i=0; i<objectManager.getGameObjects().size(); i++)
            if (inLineOfSight(objectManager.getGameObjects().get(i)))
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
            if(inLineOfSight(objectManager.getGameObjects().get(i)))
                objectManager.getGameObjects().get(i).drawFilled(game.sr);
        game.sr.end();

        //draw line shapes (unscaled)
        game.sr.begin(ShapeRenderer.ShapeType.Line);

        for(int i=0; i<objectManager.getGameObjects().size(); i++)
            if(inLineOfSight(objectManager.getGameObjects().get(i)))
                objectManager.getGameObjects().get(i).drawLine(game.sr);
        game.sr.end();

        //draw filled over line (unscaled)
        game.sr.begin(ShapeRenderer.ShapeType.Filled);
        hud.drawFilled(game.sr);
        game.sr.end();

        //draw fonts and textures (unscaled)
        game.sb.begin();
        for(int i=0; i<objectManager.getGameObjects().size(); i++)
            if(inLineOfSight(objectManager.getGameObjects().get(i)))
                objectManager.getGameObjects().get(i).drawFont(game.sb);
        //draw the use item prompts
        if (player.getCurrentState() != com.steveflames.javantgarde.sprites.Player.State.READING && player.getCurrentState() != com.steveflames.javantgarde.sprites.Player.State.CODING) { //if player is coding or reading sign, dont draw the use prompt
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

    public void updateCameraPosition() {
        if(!player.isOutOfBounds()) {
            if (player.position.x + player.b2body.getLinearVelocity().x/ com.steveflames.javantgarde.MyGdxGame.PPM < cam.viewportWidth / 2)
                cam.position.x = cam.viewportWidth / 2;
            else if(player.position.x + player.b2body.getLinearVelocity().x/ com.steveflames.javantgarde.MyGdxGame.PPM > WIDTH - cam.viewportWidth / 2)
                cam.position.x = WIDTH - cam.viewportWidth / 2;
            else
                cam.position.x = player.position.x;
        }
    }

    private boolean inLineOfSight(com.steveflames.javantgarde.sprites.GameObject gameObject) {
        return (inLineOfSightX(gameObject) && inLineOfSightY(gameObject));
    }

    private boolean inLineOfSightX(com.steveflames.javantgarde.sprites.GameObject gameObject) {
        float extra = 0;
        if(gameObject instanceof com.steveflames.javantgarde.sprites.ropes.Rope)
            extra = 1;
        return Math.abs(cam.position.x - gameObject.b2body.getPosition().x) < (cam.viewportWidth/2 + gameObject.getBounds().getWidth()/2/ com.steveflames.javantgarde.MyGdxGame.PPM + extra);
    }

    private boolean inLineOfSightY(com.steveflames.javantgarde.sprites.GameObject gameObject) {
        return Math.abs(cam.position.y - gameObject.b2body.getPosition().y) < (cam.viewportHeight/2 + gameObject.getBounds().getHeight()/2/ com.steveflames.javantgarde.MyGdxGame.PPM);
    }

    public static float getHudCameraOffsetX() {
        return -cam.position.x * com.steveflames.javantgarde.MyGdxGame.PPM + cam.viewportWidth / 2 * com.steveflames.javantgarde.MyGdxGame.PPM;
    }

    public static void setCameraTo(float x) {
        if(x < 0)
            x = 0;
        else if(x > WIDTH - cam.viewportWidth/2)
            x = WIDTH - cam.viewportWidth/2;
        cam.position.x = x;
    }

    private void drawOnScreenMsg() {
        if(onScreenMsgAlpha>0) {
            com.steveflames.javantgarde.tools.global.Fonts.big.setColor(1, 0, 0, onScreenMsgAlpha);
            com.steveflames.javantgarde.tools.global.Fonts.medium.setColor(1, 0, 0, onScreenMsgAlpha);
            com.steveflames.javantgarde.tools.global.Fonts.big.draw(game.sb, currentLevel.getCategoryName(), cam.viewportWidth/2 * com.steveflames.javantgarde.MyGdxGame.PPM - onScreenMsgGlyphLayout2.width / 2, cam.viewportHeight/2 * com.steveflames.javantgarde.MyGdxGame.PPM + 110);
            com.steveflames.javantgarde.tools.global.Fonts.medium.draw(game.sb, currentLevel.getName(), cam.viewportWidth/2 * com.steveflames.javantgarde.MyGdxGame.PPM - onScreenMsgGlyphLayout1.width / 2, cam.viewportHeight/2 * com.steveflames.javantgarde.MyGdxGame.PPM + 15);
        }
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
        hud.getViewport().update(width, height);
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
        com.steveflames.javantgarde.sprites.Item.reset();

        for(int i=0; i<objectManager.getGameObjects().size(); i++)
            objectManager.clearGameObjects();
    }

    public com.steveflames.javantgarde.sprites.Player getPlayer() {
        return player;
    }

    public TiledMap getMap() {
        return map;
    }

    public World getWorld() {
        return world;
    }

    public ArrayList<com.steveflames.javantgarde.sprites.Pc> getPcs() {
        return pcs;
    }

    public ArrayList<com.steveflames.javantgarde.sprites.InfoSign> getInfoSigns() {
        return infoSigns;
    }

    public ArrayList<com.steveflames.javantgarde.sprites.ropes.Rope> getRopes() {
        return ropes;
    }

    public ArrayList<com.steveflames.javantgarde.sprites.Checkpoint> getCheckpoints() {
        return checkpoints;
    }

    public com.steveflames.javantgarde.tools.LevelListItem getCurrentLevel() {
        return currentLevel;
    }

    public ArrayList<com.steveflames.javantgarde.sprites.Door> getDoors() {
        return doors;
    }

    public void setEnterKeyHandled(boolean enterKeyHandled) {
        this.enterKeyHandled = enterKeyHandled;
    }

    public ArrayList<com.steveflames.javantgarde.sprites.Item> getItems() {
        return items;
    }

    public ArrayList<com.steveflames.javantgarde.sprites.GameObject> getObjectsToRemove() {
        return objectsToRemove;
    }

    public com.steveflames.javantgarde.scenes.Hud getHud() {
        return hud;
    }

    public void setTeleporter(com.steveflames.javantgarde.sprites.Teleporter teleporter) {
        this.teleporter = teleporter;
    }

    public boolean isEnterKeyHandled() {
        return enterKeyHandled;
    }

    public ArrayList<com.steveflames.javantgarde.sprites.FloatingPlatform> getFloatingPlatforms() {
        return floatingPlatforms;
    }

    public ArrayList<Rectangle> getMarkers() {
        return markers;
    }

    public com.steveflames.javantgarde.MyGdxGame getGame() {
        return game;
    }

    public ArrayList<com.steveflames.javantgarde.sprites.Lever> getLevers() {
        return levers;
    }

    public ArrayList<com.steveflames.javantgarde.quests.Quiz> getQuizes() {
        return quizes;
    }

    public com.steveflames.javantgarde.GameObjectManager getObjectManager() {
        return objectManager;
    }

    public com.steveflames.javantgarde.sprites.Teleporter getTeleporter() {
        return teleporter;
    }
}
