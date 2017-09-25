package com.steveflames.javalab.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.steveflames.javalab.MyGdxGame;
import com.steveflames.javalab.scenes.Editor;
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

    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    //Box2d variables
    private World world;
    private Box2DDebugRenderer b2dr;

    private Player player;
    private ArrayList<Pc> pcs = new ArrayList<Pc>();

    public PlayScreen(MyGdxGame game) {
        super(game);
        cam = new OrthographicCamera();
        cam.setToOrtho(false, MyGdxGame.WIDTH / MyGdxGame.PPM, MyGdxGame.HEIGHT / MyGdxGame.PPM);
        gamePort = new StretchViewport(MyGdxGame.WIDTH / MyGdxGame.PPM, MyGdxGame.HEIGHT / MyGdxGame.PPM); //TODO: h mhpws fit
        hud = new Hud(this, game.sb);

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("tiled/level1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / MyGdxGame.PPM);

        setMapProperties(map);

        world = new World(new Vector2(0, GRAVITY), true);
        b2dr = new Box2DDebugRenderer();

        new B2WorldCreator(this);

        player = new Player(world);

        world.setContactListener(new WorldContactListener());

        //hud.newToast("Hello my friend, welcome to Java Lab!");
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
            if(player.currentState != Player.State.CODING) {
                if (Gdx.input.isKeyJustPressed(Input.Keys.W))
                    player.jump();
                else if (Gdx.input.isKeyPressed(Input.Keys.D) && player.b2body.getLinearVelocity().x <= 5) //2
                    player.b2body.applyLinearImpulse(new Vector2(0.5f, 0), player.b2body.getWorldCenter(), true); //0.2f
                else if (Gdx.input.isKeyPressed(Input.Keys.A) && player.b2body.getLinearVelocity().x >= -5)
                    player.b2body.applyLinearImpulse(new Vector2(-0.5f, 0), player.b2body.getWorldCenter(), true);
                else if (Gdx.input.isKeyJustPressed(Input.Keys.T))
                    hud.newToast("Sample textSample is\n just a iug come on now momo\n yoyo lorem ipsum merde kap\n lolololloa weaweawe\n xaxxaxaxaxaxaxa kikikikikiririririirirkikiki xoxoxo xax\naxaxaxa adwodka opwdk oawk dpoakwdpo akwpod kak aop aaaaaaaakakakakakakka");
                else if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                    for (Pc pc : pcs) {
                        if (pc.isUsable()) {
                            player.b2body.setLinearVelocity(0,0);
                            player.b2body.setTransform(pc.getBounds().x/MyGdxGame.PPM + 0.1f, pc.getBounds().y/MyGdxGame.PPM + player.getRadius(), 0);
                            player.currentState = Player.State.CODING;
                            cam.position.x = pc.getBounds().x / MyGdxGame.PPM + 1.5f;
                            hud.newEditor();
                        }
                    }
                }
            }
            else {
                if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                    player.currentState = Player.State.STANDING;
                    hud.closeCurrentEditor();
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
            if (player.b2body.getPosition().x >= cam.viewportWidth / 2 && player.b2body.getPosition().x <= WIDTH - cam.viewportWidth / 2)
                cam.position.x = player.b2body.getPosition().x;
            //if(player.b2body.getPosition().y >= HEIGHT - HEIGHT/4)
            //   cam.position.y = player.b2body.getPosition().y;
        }

        cam.update();
        renderer.setView(cam);

        player.update(dt);
        hud.update(dt);
    }

    @Override
    public void render(float dt) {
        game.sb.setProjectionMatrix(cam.combined);
        game.sr.setProjectionMatrix(cam.combined);
        update(dt);

        renderer.render(); //render map

        //b2dr.render(world, cam.combined);

        game.sr.begin(ShapeRenderer.ShapeType.Filled);
        player.draw(game.sr);
        game.sr.end();

        hud.stage.draw();
        hud.render(game.sb, game.sr); //must be last in render because it messes with projection matrix
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    @Override
    public void hide() {

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
}
