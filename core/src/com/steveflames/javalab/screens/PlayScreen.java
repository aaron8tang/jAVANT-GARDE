package com.steveflames.javalab.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.steveflames.javalab.MyGdxGame;
import com.steveflames.javalab.sprites.Checkpoint;
import com.steveflames.javalab.sprites.InfoSign;
import com.steveflames.javalab.sprites.ropes.Rope;
import com.steveflames.javalab.tools.B2WorldCreator;
import com.steveflames.javalab.scenes.Toast;
import com.steveflames.javalab.tools.WorldContactListener;
import com.steveflames.javalab.Window;
import com.steveflames.javalab.scenes.Hud;
import com.steveflames.javalab.sprites.Pc;
import com.steveflames.javalab.sprites.Player;

import java.util.ArrayList;

/**
 * Created by Flames on 23/9/2017.
 */

public class PlayScreen extends Window {

    private Hud hud;

    private static final int GRAVITY = -20 ;
    private static final float PLAYERSPEED = 0.3f;
    private int currentLevel;

    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    //Box2d variables
    private World world;
    private Box2DDebugRenderer b2dr;

    private Player player;
    private ArrayList<Pc> pcs = new ArrayList<Pc>();
    private ArrayList<InfoSign> infoSigns = new ArrayList<InfoSign>();
    public static ArrayList<Rope> ropes = new ArrayList<Rope>();
    private ArrayList<Checkpoint> checkpoints = new ArrayList<Checkpoint>();
    private boolean touchDown = false;
    private Vector2 touchDownVector = new Vector2();

    public PlayScreen(MyGdxGame game, int level) {
        super(game);
        currentLevel = level;
        cam = new OrthographicCamera();
        cam.setToOrtho(false, MyGdxGame.WIDTH / MyGdxGame.PPM, MyGdxGame.HEIGHT / MyGdxGame.PPM);
        gamePort = new StretchViewport(MyGdxGame.WIDTH / MyGdxGame.PPM, MyGdxGame.HEIGHT / MyGdxGame.PPM); //TODO: h mhpws fit
        hud = new Hud(this, game.sb);

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("tiled/level"+level+".tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / MyGdxGame.PPM); //TODO mhpws svhsimo to 2o

        setMapProperties(map);

        world = new World(new Vector2(0, GRAVITY), true);
        b2dr = new Box2DDebugRenderer();

        new B2WorldCreator(this);

        player = new Player(world, checkpoints);

        world.setContactListener(new WorldContactListener());

        if(level==2) {
            hud.newToast("In programming, variables are used to store information.\nIt could be a number, " +
                    "a sequence of characters, etc.\n\n" +
                    "To declare a variable you need to choose a descriptive name \nfor it.\n" +
                    "There are certain rules to follow when naming a variable.\n" +
                    "Go on and jump on the correct platforms to move on!");
        }
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
            if(player.currentState != Player.State.CODING && player.currentState != Player.State.IDLE && player.currentState != Player.State.DEAD) {
                if (Gdx.input.isKeyJustPressed(Input.Keys.W))
                    player.jump();
                else if (Gdx.input.isKeyPressed(Input.Keys.D) && player.b2body.getLinearVelocity().x <= PLAYERSPEED*10) {
                    player.b2body.applyLinearImpulse(new Vector2(PLAYERSPEED, 0), player.b2body.getWorldCenter(), true); //0.2f
                }
                else if (Gdx.input.isKeyPressed(Input.Keys.A) && player.b2body.getLinearVelocity().x >= -PLAYERSPEED*10) {
                    player.b2body.applyLinearImpulse(new Vector2(-PLAYERSPEED, 0), player.b2body.getWorldCenter(), true);
                }
                else if (Gdx.input.isKeyJustPressed(Input.Keys.T))
                    hud.newToast("Sample textSample is\n just a iug come on now momo\n yoyo lorem ipsum merde kap\n lolololloa weaweawe\n xaxxaxaxaxaxaxa kikikikikiririririirirkikiki xoxoxo xax\naxaxaxa adwodka opwdk oawk dpoakwdpo akwpod kak aop aaaaaaaakakakakakakka");
                if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                    for (Pc pc : pcs) {
                        if (pc.isUsable()) {
                            player.b2body.setLinearVelocity(0, 0);
                            player.b2body.setTransform(pc.getBounds().x / MyGdxGame.PPM + 0.1f, pc.getBounds().y / MyGdxGame.PPM + player.getRadius(), 0);
                            player.currentState = Player.State.CODING;
                            cam.position.x = pc.getBounds().x / MyGdxGame.PPM + 1.5f;
                            hud.newEditorWindow(pc.getName().substring(3));
                        }
                    }
                    for(InfoSign infoSign : infoSigns) {
                        if(infoSign.isUsable()) {
                            player.b2body.setLinearVelocity(0, 0);
                            player.b2body.setTransform(infoSign.getBounds().x / MyGdxGame.PPM + 0.17f, infoSign.getBounds().y / MyGdxGame.PPM + player.getRadius(), 0);
                            player.currentState = Player.State.IDLE;
                            hud.newInfoWindow(infoSign.getName());
                        }
                    }
                }
            }
        }
        else if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            if(hud.getCurrentToast().getCurrentState() == Toast.State.READY)
                hud.getCurrentToast().setCurrentState(Toast.State.NEXT);
            else if (hud.getCurrentToast().getCurrentState() == Toast.State.WRITING)
                hud.getCurrentToast().setCurrentState(Toast.State.SKIP);
        }
    }


    @Override
    public void update(float dt) {
        handleInput(dt);
        world.step(1/60f, 6, 2);

        if(player.currentState != Player.State.CODING) {
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

            if(touchDown) {
                if (clickCoords.x > player.b2body.getPosition().x) {
                    if (player.b2body.getLinearVelocity().x <= PLAYERSPEED*10) //2
                        player.b2body.applyLinearImpulse(new Vector2(PLAYERSPEED, 0), player.b2body.getWorldCenter(), true); //0.2f
                } else if (clickCoords.x < player.b2body.getPosition().x) {
                    if (player.b2body.getLinearVelocity().x >= -PLAYERSPEED*10)
                        player.b2body.applyLinearImpulse(new Vector2(-PLAYERSPEED, 0), player.b2body.getWorldCenter(), true);
                }
            }

            if(player.currentState == Player.State.DEAD) {
                hud.newGameOverWindow();
            }
        }

        cam.update();
        renderer.setView(cam);

        player.update(dt);
        hud.update(dt);

        for(Rope rope: ropes)
            rope.update(dt);
    }

    @Override
    public void render(float dt) {
        update(dt);

        //render our game map
        renderer.render();

        //render our Box2DDebugLines
        //b2dr.render(world, cam.combined);

        game.sb.setProjectionMatrix(cam.combined);
        game.sr.setProjectionMatrix(cam.combined);
        for(Rope rope: ropes)
            rope.draw(game.sb, game.sr);
        player.draw(game.sb, game.sr);


        game.sb.setProjectionMatrix(hud.stage.getCamera().combined);
        game.sr.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.render(game.sb, game.sr);
        player.drawPlayerMsg(game.sb);


        //set screen and dispose should be called in the end of render method
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            if(player.currentState == Player.State.CODING) {
                hud.closeCurrentEditor();
            }
            else {
                game.setScreen(new ChooseLevelScreen(game));
                dispose();
            }
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
        touchDownVector.set(clickVector.x, clickVector.y);
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


        if(hud.getCurrentToast() == null) {
            touchDown = false;

            if (touchDownVector.y < clickVector.y - 0.6f)
                player.jump();

            if (Player.colliding) {
                for (Pc pc : pcs) {
                    if (pc.isUsable()) {
                        if (clickCoords.overlaps(pc.getBounds())) {
                            player.b2body.setLinearVelocity(0, 0);
                            player.b2body.setTransform(pc.getBounds().x / MyGdxGame.PPM + 0.1f, pc.getBounds().y / MyGdxGame.PPM + player.getRadius(), 0);
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
                            player.b2body.setTransform(infoSign.getBounds().x / MyGdxGame.PPM + 0.17f, infoSign.getBounds().y / MyGdxGame.PPM + player.getRadius(), 0);
                            player.currentState = Player.State.IDLE;
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

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
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

    public int getCurrentLevel() {
        return currentLevel;
    }
}
