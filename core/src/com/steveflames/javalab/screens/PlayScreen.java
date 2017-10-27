package com.steveflames.javalab.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
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
import com.steveflames.javalab.MyGdxGame;
import com.steveflames.javalab.buttons.LevelListItem;
import com.steveflames.javalab.sprites.Checkpoint;
import com.steveflames.javalab.sprites.Door;
import com.steveflames.javalab.sprites.Health;
import com.steveflames.javalab.sprites.InfoSign;
import com.steveflames.javalab.sprites.Teleporter;
import com.steveflames.javalab.sprites.ropes.Rope;
import com.steveflames.javalab.tools.B2WorldCreator;
import com.steveflames.javalab.scenes.Toast;
import com.steveflames.javalab.tools.global.Fonts;
import com.steveflames.javalab.tools.global.Loader;
import com.steveflames.javalab.tools.B2WorldContactListener;
import com.steveflames.javalab.scenes.Hud;
import com.steveflames.javalab.sprites.Pc;
import com.steveflames.javalab.sprites.Player;

import java.util.ArrayList;

/**
 * Created by Flames on 23/9/2017.
 */

public class PlayScreen extends Window {

    private static final int GRAVITY = -20;
    public static LevelListItem currentLevel;

    private Hud hud;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    //Box2d variables
    private World world;
    private Box2DDebugRenderer b2dr;

    //world bodies
    private Player player;
    private ArrayList<Pc> pcs = new ArrayList<Pc>();
    private ArrayList<InfoSign> infoSigns = new ArrayList<InfoSign>();
    private ArrayList<Door> doors = new ArrayList<Door>();
    public static ArrayList<Rope> ropes = new ArrayList<Rope>(); //TODO static :/
    private ArrayList<Health> healths = new ArrayList<Health>();
    private ArrayList<Checkpoint> checkpoints = new ArrayList<Checkpoint>();
    private ArrayList<Body> bodiesToRemove = new ArrayList<Body>();
    private Teleporter teleporter;

    //input
    private boolean touchDown = false;
    private boolean touchDownDoOnce = true;
    private Vector2 touchDownVector = new Vector2();
    private boolean enterKeyHandled = false;

    //onScreenMsg
    private long onScreenMsgMillis = 0; //is used to print the level name when the level starts
    private float onScreenMsgAlpha = 1;
    private GlyphLayout onScreenMsgGlyphLayout = new GlyphLayout();


    public PlayScreen(MyGdxGame game, LevelListItem level) {
        super(game);
        currentLevel = level;
        onScreenMsgGlyphLayout.setText(Fonts.big, currentLevel.getName());
        cam = new OrthographicCamera();
        cam.setToOrtho(false, MyGdxGame.WIDTH / MyGdxGame.PPM, MyGdxGame.HEIGHT / MyGdxGame.PPM);
        gamePort = new StretchViewport(MyGdxGame.WIDTH / MyGdxGame.PPM, MyGdxGame.HEIGHT / MyGdxGame.PPM); //TODO: h mhpws fit

        TmxMapLoader mapLoader = new TmxMapLoader();
        map = mapLoader.load("tiled/level-"+level.getId()+".tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / MyGdxGame.PPM);

        setMapProperties(map);

        world = new World(new Vector2(0, GRAVITY), true);
        b2dr = new Box2DDebugRenderer();

        new B2WorldCreator(this);

        player = new Player(world, checkpoints);
        hud = new Hud(this, game.sb);

        world.setContactListener(new B2WorldContactListener(this));
        onScreenMsgMillis = TimeUtils.millis();

        //hud.newEditorWindow("1_1-0");
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

    @Override
    public void show() {

    }

    private void handleInput(float dt) {
        if(hud.getCurrentToast() == null) {
            if(Gdx.input.getInputProcessor()==this && player.currentState != Player.State.DEAD) {
                //move player on key pressed
                if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
                    player.jump();
                }
                else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= Player.PLAYERSPEED*10) {
                    player.runRight();
                }
                else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -Player.PLAYERSPEED*10) {
                    player.runLeft();
                }

                //use item
                if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                    if(Player.colliding) {
                        if (!enterKeyHandled) {
                            for (Pc pc : pcs) {
                                if (pc.isUsable()) {
                                    player.b2body.setLinearVelocity(0, 0);
                                    player.b2body.setTransform(pc.getBounds().x / MyGdxGame.PPM + 0.1f, (pc.getBounds().y + player.b2body.getPosition().y) / MyGdxGame.PPM + 0.3f, 0);
                                    player.currentState = Player.State.CODING;
                                    cam.position.x = pc.getBounds().x / MyGdxGame.PPM + 1.5f;
                                    hud.newEditorWindow(pc.getName().substring(3));
                                }
                            }
                            for (InfoSign infoSign : infoSigns) {
                                if (infoSign.isUsable()) {
                                    player.b2body.setLinearVelocity(0, 0);
                                    player.b2body.setTransform(infoSign.getBounds().x / MyGdxGame.PPM + 0.17f, (infoSign.getBounds().y + player.b2body.getPosition().y) / MyGdxGame.PPM + 0.3f, 0);
                                    player.currentState = Player.State.READING;
                                    hud.newInfoWindow(infoSign.getName());
                                }
                            }
                        }
                    }
                }
                //keep updating touch down coords while screen is touched
                if(Gdx.input.isTouched())
                    touchDown(Gdx.input.getX(), Gdx.input.getY(), 0, 0);

                //move player on tap
                if(touchDown) {
                    if (clickCoords.x > player.b2body.getPosition().x) {
                        if (player.b2body.getLinearVelocity().x <= Player.PLAYERSPEED*10) //2
                            player.runRight();
                    } else if (clickCoords.x < player.b2body.getPosition().x) {
                        if (player.b2body.getLinearVelocity().x >= -Player.PLAYERSPEED*10)
                            player.runLeft();
                    }
                }

                if(player.currentState != Player.State.CODING)
                    updateCameraPosition();
            }
            else if(player.currentState == Player.State.DEAD)
                hud.newGameOverWindow();
        }
        else if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            if(hud.getCurrentToast().getCurrentState() == Toast.State.READY)
                hud.getCurrentToast().setCurrentState(Toast.State.NEXT);
            else if (hud.getCurrentToast().getCurrentState() == Toast.State.WRITING)
                hud.getCurrentToast().setCurrentState(Toast.State.SKIP);
        }
        enterKeyHandled = false;
    }

    @Override
    public void update(float dt) {
        handleInput(dt);
        world.step(1/60f, 6, 2);

        cam.update();
        renderer.setView(cam);

        player.update(dt);
        hud.update(dt);
        teleporter.update(dt);
        for(int i=0; i<doors.size(); i++)
            doors.get(i).update(dt);
        for(int i=0; i<ropes.size(); i++) {
            ropes.get(i).update(dt);
        }
        for(int i=0; i<healths.size(); i++)
            healths.get(i).update(dt);

        if(player.currentState == Player.State.DISAPPEARED) {
            hud.newLevelCompletedWindow();
        }
    }

    @Override
    public void render(float dt) {
        update(dt);

        //render our game map
        renderer.render();

        //render our Box2DDebugLines
        //b2dr.render(world, cam.combined);

        //set projection matrix according to world scaling. (1280x768 / 200)
        game.sb.setProjectionMatrix(cam.combined);
        game.sr.setProjectionMatrix(cam.combined);
        game.sb.setColor(Color.WHITE);
        game.sb.begin();
        for(int i=0; i<healths.size(); i++)
            healths.get(i).draw(game.sb);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        player.draw(game.sb);
        Gdx.gl.glDisable(GL20.GL_BLEND);

        if(inLineOfSight(teleporter.getBounds()))
            teleporter.draw(game.sb);
        game.sb.end();


        //change projection matrix. (unscaled 1280x768)
        game.sb.setProjectionMatrix(hud.stage.getCamera().combined);
        game.sr.setProjectionMatrix(hud.stage.getCamera().combined);
        player.drawPlayerMsg(game.sb);
        for(int i=0; i<ropes.size(); i++) {
            if(inLineOfSight(ropes.get(i).getBounds()))
                ropes.get(i).draw(game.sb, game.sr);
        }
        if(Gdx.input.getInputProcessor()==this) { //if player is coding or reading sign, dont draw the use prompt
            for(int i=0; i<infoSigns.size(); i++)
                infoSigns.get(i).drawUsePrompt(game.sb);
            for(int i=0; i<pcs.size(); i++)
                pcs.get(i).drawUsePrompt(game.sb);
        }
        for(int i=0; i<doors.size(); i++) {
            if(inLineOfSight(doors.get(i).getBounds()))
                doors.get(i).draw(game.sr);
        }
        hud.render(game.sb, game.sr);
        drawOnScreenMsg();

        destroyUnusedBodies();
        //set screen and dispose should be called in the end of render method
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            if(player.currentState == Player.State.CODING) {
                hud.closeCurrentEditor();
            }
            /*else if(hud.getInfoDialog().) {
                //hud.closeCurrentInfo();
            }*/
            else {
                game.setScreen(new ChooseLevelScreen(game));
                dispose();
            }
        }
    }

    private void updateCameraPosition() {
        if(!player.isOutOfBounds()) {
            if (player.b2body.getPosition().x >= cam.viewportWidth / 2 && player.b2body.getPosition().x <= WIDTH - cam.viewportWidth / 2)
                cam.position.x = player.b2body.getPosition().x;
            else if (player.b2body.getPosition().x < cam.viewportWidth / 2)
                cam.position.x = cam.viewportWidth / 2;
            else
                cam.position.x = WIDTH - cam.viewportWidth / 2;
            //if(player.b2body.getPosition().y >= HEIGHT - HEIGHT/4) //TODO y camera
            //   cam.position.y = player.b2body.getPosition().y;
        }
    }

    private boolean inLineOfSight(Rectangle bounds) { //todo checkare k y otan kaneis k alla lvls
        return Math.abs(cam.position.x - bounds.x / MyGdxGame.PPM) < cam.viewportWidth;
    }

    private void drawOnScreenMsg() {
        if(onScreenMsgAlpha>0) {
            if (TimeUtils.timeSinceMillis(onScreenMsgMillis) > 2000)
                onScreenMsgAlpha -= 1f*Gdx.graphics.getDeltaTime();

            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            Fonts.big.setColor(new Color(1, 0, 0, onScreenMsgAlpha));

            game.sb.begin();
            Fonts.big.draw(game.sb, currentLevel.getName(), cam.viewportWidth/2 * MyGdxGame.PPM - onScreenMsgGlyphLayout.width / 2, cam.viewportHeight/2 * MyGdxGame.PPM + 50);
            game.sb.end();

            Gdx.gl.glDisable(GL20.GL_BLEND);
        }
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
        hud.getViewport().update(width, height);
    }

    @Override
    public void hide() {

    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        clickVector.set(screenX, screenY, 0);
        clickVector = cam.unproject(clickVector);
        if(touchDownDoOnce) {
            touchDownDoOnce = false;
            touchDownVector.set(clickVector.x, clickVector.y);
        }
        clickCoords.set(clickVector.x, clickVector.y, 1, 1);

        if(hud.getCurrentToast() == null)
            touchDown = true;

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        clickVector.set(screenX, screenY, 0);
        clickVector = cam.unproject(clickVector);
        clickCoords.set(clickVector.x * MyGdxGame.PPM, clickVector.y * MyGdxGame.PPM, 1, 1);
        touchDownDoOnce = true;

        if(hud.getCurrentToast() == null) {
            touchDown = false;

            if (touchDownVector.y < clickVector.y - 0.4f)
                player.jump();

            if (Player.colliding) {
                for (Pc pc : pcs) {
                    if (pc.isUsable()) {
                        if (clickCoords.overlaps(pc.getBounds())) {
                            player.b2body.setLinearVelocity(0, 0);
                            player.b2body.setTransform(pc.getBounds().x / MyGdxGame.PPM + 0.1f,  (pc.getBounds().y + player.b2body.getPosition().y)/ MyGdxGame.PPM + 0.3f, 0);
                            player.currentState = Player.State.CODING;
                            cam.position.x = pc.getBounds().x / MyGdxGame.PPM + 1.5f;
                            hud.newEditorWindow(pc.getName().substring(3));
                        }
                        break;
                    }
                }
                for (InfoSign infoSign : infoSigns) {
                    if (infoSign.isUsable()) {
                        if (clickCoords.overlaps(infoSign.getBounds())) {
                            player.b2body.setLinearVelocity(0, 0);
                            player.b2body.setTransform(infoSign.getBounds().x / MyGdxGame.PPM + 0.17f,  (infoSign.getBounds().y + player.b2body.getPosition().y)/ MyGdxGame.PPM + 0.3f, 0);
                            player.currentState = Player.State.READING;
                            hud.newInfoWindow(infoSign.getName());
                        }
                        break;
                    }
                }
            }
        }
        else {
            if(hud.getCurrentToast().getCurrentState() == Toast.State.READY)
                hud.getCurrentToast().setCurrentState(Toast.State.NEXT);
            else if (hud.getCurrentToast().getCurrentState() == Toast.State.WRITING)
                hud.getCurrentToast().setCurrentState(Toast.State.SKIP);
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        clickVector.set(screenX, screenY, 0);
        clickVector = cam.unproject(clickVector);
        clickCoords.set(clickVector.x, clickVector.y, 1, 1);
        return false;
    }

    private void destroyUnusedBodies() {
        for(int i=0; i<bodiesToRemove.size(); i++) {
            world.destroyBody(bodiesToRemove.get(i));

            //if(bodiesToRemove.get(i).getUserData() instanceof Health)
            //    healths.remove(bodiesToRemove.get(i).getUserData());
        }
        bodiesToRemove.clear();
    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
        ropes.clear();
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

    public boolean isEnterKeyHandled() {
        return enterKeyHandled;
    }

    public void setEnterKeyHandled(boolean enterKeyHandled) {
        this.enterKeyHandled = enterKeyHandled;
    }

    public ArrayList<Health> getHealths() {
        return healths;
    }

    public ArrayList<Body> getBodiesToRemove() {
        return bodiesToRemove;
    }

    public Hud getHud() {
        return hud;
    }

    public Teleporter getTeleporter() {
        return teleporter;
    }

    public void setTeleporter(Teleporter teleporter) {
        this.teleporter = teleporter;
    }
}
