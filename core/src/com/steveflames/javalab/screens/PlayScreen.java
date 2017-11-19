package com.steveflames.javalab.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
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
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.steveflames.javalab.MyGdxGame;
import com.steveflames.javalab.quests.Quest;
import com.steveflames.javalab.sprites.FloatingPlatform;
import com.steveflames.javalab.sprites.Lever;
import com.steveflames.javalab.tools.LevelListItem;
import com.steveflames.javalab.sprites.Checkpoint;
import com.steveflames.javalab.sprites.Door;
import com.steveflames.javalab.sprites.Item;
import com.steveflames.javalab.sprites.InfoSign;
import com.steveflames.javalab.sprites.Teleporter;
import com.steveflames.javalab.sprites.ropes.Rope;
import com.steveflames.javalab.tools.B2WorldCreator;
import com.steveflames.javalab.tools.InputHandler;
import com.steveflames.javalab.tools.global.Fonts;
import com.steveflames.javalab.tools.B2WorldContactListener;
import com.steveflames.javalab.scenes.Hud;
import com.steveflames.javalab.sprites.Pc;
import com.steveflames.javalab.sprites.Player;

import java.util.ArrayList;

/**
 * Created by Flames on 23/9/2017.
 */

public class PlayScreen implements Screen{

    private MyGdxGame game;
    private static float WIDTH;
    private static float HEIGHT;
    private static final int GRAVITY = -20;
    private static Viewport gamePort;
    public static OrthographicCamera cam;
    public static LevelListItem currentLevel;

    private Hud hud;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    //Box2d variables
    private World world;
    private Box2DDebugRenderer b2dr;
    //private FPSLogger fpsLogger;

    //world bodies
    private Player player;
    private ArrayList<Pc> pcs = new ArrayList<Pc>();
    private ArrayList<InfoSign> infoSigns = new ArrayList<InfoSign>();
    private ArrayList<Door> doors = new ArrayList<Door>();
    public static ArrayList<Rope> ropes; //TODO static :/
    private ArrayList<Item> items = new ArrayList<Item>();
    private ArrayList<Checkpoint> checkpoints = new ArrayList<Checkpoint>();
    private ArrayList<Body> bodiesToRemove = new ArrayList<Body>();
    private ArrayList<FloatingPlatform> floatingPlatforms = new ArrayList<FloatingPlatform>();
    private ArrayList<Lever> levers = new ArrayList<Lever>();
    private ArrayList<Rectangle> markers = new ArrayList<Rectangle>();
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
        //fpsLogger = new FPSLogger();
        //GLProfiler.enable();

        //initialize camera and viewport
        cam = new OrthographicCamera();
        cam.setToOrtho(false, MyGdxGame.WIDTH / MyGdxGame.PPM, MyGdxGame.HEIGHT / MyGdxGame.PPM);
        gamePort = new StretchViewport(MyGdxGame.WIDTH / MyGdxGame.PPM, MyGdxGame.HEIGHT / MyGdxGame.PPM); //TODO: h mhpws fit

        //initialize map
        TmxMapLoader mapLoader = new TmxMapLoader();
        map = mapLoader.load("tiled/level-"+level.getId()+".tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / MyGdxGame.PPM);
        setMapProperties(map);

        world = new World(new Vector2(0, GRAVITY), true);
        b2dr = new Box2DDebugRenderer();

        new B2WorldCreator(this); //create world

        player = new Player(world, checkpoints);
        hud = new Hud(this, game.sb);

        world.setContactListener(new B2WorldContactListener(this));
        onScreenMsgMillis = TimeUtils.millis();

        if(!MyGdxGame.platformDepended.deviceHasKeyboard())
            hud.newAndroidInputTable();

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

    /**
     * Updates the window's parameters.
     * @param dt -> delta time
     */
    public void update(float dt) {
        if(MyGdxGame.platformDepended.deviceHasKeyboard())
            inputHandler.handlePlayscreenInput();
        else
            inputHandler.handlePlayscreenAndroidInput();

        world.step(1/60f, 6, 2);

        cam.update();
        renderer.setView(cam);

        if(onScreenMsgAlpha>0) {
            if (TimeUtils.timeSinceMillis(onScreenMsgMillis) > 2000)
                onScreenMsgAlpha -= 1f * Gdx.graphics.getDeltaTime();
        }

        player.update(dt);
        hud.update(dt);
        if(inLineOfSight(teleporter.getBounds()))
            teleporter.update(dt);
        for(int i=0; i<doors.size(); i++)
            doors.get(i).update(dt);
        for(int i=0; i<ropes.size(); i++)
            ropes.get(i).update(dt);
        for(int i = 0; i< items.size(); i++)
            items.get(i).update(dt);

        if(player.getCurrentState() == Player.State.DISAPPEARED)
            hud.showLevelCompletedWindow();
    }

    public void render(float dt) {
        if(!hud.isPauseWindowShowing())
            update(dt);
        else
            hud.getPauseWindow().handleExitFromPauseMenuInput();

        //render our game map
        renderer.render();

        //set projection matrix according to world scaling. (1280x768 / 200)
        game.sb.setProjectionMatrix(cam.combined);
        game.sr.setProjectionMatrix(cam.combined);

        //draw textures scaled to world
        game.sb.setColor(Color.WHITE);
        game.sb.begin();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        for (int i = 0; i < items.size(); i++)
            items.get(i).draw(game.sb);
        for (int i=0; i<infoSigns.size(); i++)
            infoSigns.get(i).draw(game.sb, dt);
        for (int i=0; i<levers.size(); i++)
            levers.get(i).draw(game.sb, dt);
        player.draw(game.sb);
        Gdx.gl.glDisable(GL20.GL_BLEND);

        if (inLineOfSight(teleporter.getBounds()))
            teleporter.draw(game.sb);
        game.sb.end();


        //change projection matrix. (unscaled 1280x768)
        game.sb.setProjectionMatrix(hud.stage.getCamera().combined);
        game.sr.setProjectionMatrix(hud.stage.getCamera().combined);

        //enable alpha
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        //draw filled shapes
        game.sr.begin(ShapeRenderer.ShapeType.Filled);
        for (int i = 0; i < ropes.size(); i++) {
            if (inLineOfSight(ropes.get(i).getBounds()))
                ropes.get(i).drawFilled(game.sr);
        }
        game.sr.setColor(0.21f, 0.18f, 0.17f, 1);
        for (int i = 0; i < doors.size(); i++) {
            if (inLineOfSight(doors.get(i).getBounds()))
                doors.get(i).drawFilled(game.sr);
        }
        for (int i = 0; i < floatingPlatforms.size(); i++) {
            if (inLineOfSight(floatingPlatforms.get(i).getBounds()))
                floatingPlatforms.get(i).drawRect(game.sr);
        }
        game.sr.end();

        //draw line shapes
        game.sr.begin(ShapeRenderer.ShapeType.Line);
        for (int i = 0; i < ropes.size(); i++) {
            if (inLineOfSight(ropes.get(i).getBounds()))
                ropes.get(i).drawLine(game.sr);
        }
        game.sr.setColor(Color.BLACK);
        for (int i = 0; i < doors.size(); i++) {
            if (inLineOfSight(doors.get(i).getBounds()))
                doors.get(i).drawLine(game.sr);
        }
        for (int i = 0; i < floatingPlatforms.size(); i++) {
            if (inLineOfSight(floatingPlatforms.get(i).getBounds()))
                floatingPlatforms.get(i).drawRect(game.sr);
        }
        game.sr.end();

        //draw filled over line
        game.sr.begin(ShapeRenderer.ShapeType.Filled);
        hud.drawFilled(game.sr);
        game.sr.end();

        //draw fonts and textures
        game.sb.begin();
        player.drawPlayerMsg(game.sb);
        Fonts.small.setColor(Color.WHITE);
        for (int i = 0; i < ropes.size(); i++) {
            if (inLineOfSight(ropes.get(i).getBounds()))
                ropes.get(i).drawFont(game.sb);
        }
        for (int i = 0; i < floatingPlatforms.size(); i++) {
            if (inLineOfSight(floatingPlatforms.get(i).getBounds()))
                floatingPlatforms.get(i).drawFont(game.sb);
        }
        if (player.getCurrentState() != Player.State.READING && player.getCurrentState() != Player.State.CODING) { //if player is coding or reading sign, dont draw the use prompt
            for (int i = 0; i < infoSigns.size(); i++)
                infoSigns.get(i).drawUsePrompt(game.sb);
            for (int i = 0; i < pcs.size(); i++)
                pcs.get(i).drawUsePrompt(game.sb);
        }
        hud.drawFont(game.sb);
        drawOnScreenMsg();

        if(Item.getnOfClasses() > 0) {
            Fonts.small.setColor(Color.WHITE);
            Fonts.small.draw(game.sb, "Classes found: " + player.getClasses().size() +"/" + Item.getnOfClasses(), 15, MyGdxGame.HEIGHT - 67);
        }

        game.sb.end();

        //disable alpha
        Gdx.gl.glDisable(GL20.GL_BLEND);

        hud.drawStage(game.sb);


        //DEBUG
        //b2dr.render(world, cam.combined);
        //fpsLogger.log();
        /*System.out.println("GL calls: " + GLProfiler.calls);
        System.out.println("GL drawCalls: " + GLProfiler.drawCalls);
        System.out.println("GL shaderSwitches: " + GLProfiler.shaderSwitches);
        System.out.println("GL textureBindings: " + GLProfiler.textureBindings);
        System.out.println("GL vertexCount: " + GLProfiler.vertexCount);
        GLProfiler.reset();*/


        enterKeyHandled = false;
        destroyUnusedBodies();
    }

    public void updateCameraPosition() {
        if(!player.isOutOfBounds()) {
            if (player.b2body.getPosition().x + player.b2body.getLinearVelocity().x/MyGdxGame.PPM < cam.viewportWidth / 2)
                cam.position.x = cam.viewportWidth / 2;
            else if(player.b2body.getPosition().x + player.b2body.getLinearVelocity().x/MyGdxGame.PPM > WIDTH - cam.viewportWidth / 2)
                cam.position.x = WIDTH - cam.viewportWidth / 2;
            else
                cam.position.x = player.b2body.getPosition().x + player.b2body.getLinearVelocity().x/MyGdxGame.PPM;
            //if(player.b2body.getPosition().y >= HEIGHT - HEIGHT/4) //TODO y camera
            //   cam.position.y = player.b2body.getPosition().y;
        }
    }

    private boolean inLineOfSight(Rectangle bounds) { //todo checkare k y otan kaneis k alla lvls
        return Math.abs(cam.position.x - bounds.x / MyGdxGame.PPM) < cam.viewportWidth;
    }

    private void drawOnScreenMsg() {
        if(onScreenMsgAlpha>0) {
            Fonts.big.setColor(new Color(1, 0, 0, onScreenMsgAlpha));
            Fonts.medium.setColor(new Color(1, 0, 0, onScreenMsgAlpha));
            Fonts.big.draw(game.sb, currentLevel.getCategoryName(), cam.viewportWidth/2 * MyGdxGame.PPM - onScreenMsgGlyphLayout2.width / 2, cam.viewportHeight/2 * MyGdxGame.PPM + 110);
            Fonts.medium.draw(game.sb, currentLevel.getName(), cam.viewportWidth/2 * MyGdxGame.PPM - onScreenMsgGlyphLayout1.width / 2, cam.viewportHeight/2 * MyGdxGame.PPM + 15);
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
        for(int i=0; i<bodiesToRemove.size(); i++) {
            world.destroyBody(bodiesToRemove.get(i));
        }
        bodiesToRemove.clear();
    }

    public static float getHudCameraOffsetX() {
        return -cam.position.x * MyGdxGame.PPM
                + cam.viewportWidth / 2 * MyGdxGame.PPM;
    }

    public void pause() {}

    public void resume() {}

    public static void setCameraTo(float x, float y) {
        if(x < 0)
            x = 0;
        else if(x > WIDTH - cam.viewportWidth/2)
            x = WIDTH - cam.viewportWidth/2;
        cam.position.x = x;
        //cam.position.y = y;
    }

    /**
     * Dispose the unused variables.
     */
    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        world.dispose();
        //b2dr.dispose();
        hud.dispose();
        ropes.clear();
        Item.reset();

        pcs = null;
        infoSigns = null;
        doors = null;
        ropes = null;
        items = null;
        checkpoints = null;
        bodiesToRemove = null;
        floatingPlatforms = null;
        markers = null;
        teleporter = null;
        //todo dispose static variables
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

    public ArrayList<Body> getBodiesToRemove() {
        return bodiesToRemove;
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
}
