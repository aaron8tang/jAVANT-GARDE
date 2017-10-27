package com.steveflames.javalab.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.steveflames.javalab.MyGdxGame;
import com.steveflames.javalab.tools.global.Loader;

/**
 * Created by Flames on 23/10/2017.
 */

public class Teleporter extends InteractiveTileObject {

    public enum State { HIDDEN, APPEARING, IDLE, DISAPPEARING }
    private State currentState;
    public State previousState;
    private Animation<TextureRegion> appearingAnim;
    private Animation<TextureRegion> idleAnim;
    private Animation<TextureRegion> disappearingAnim;
    private float stateTimer = 0f;

    public Teleporter(String name, World world, TiledMap map, Rectangle bounds) {
        super(name, world, map, bounds, true);
        setRegion(Loader.teleporterAtlas.findRegion("teleporter_appear"));
        currentState = State.IDLE;
        previousState = State.IDLE;

        appearingAnim = new Animation<TextureRegion>(0.12f, Loader.loadAnim(Loader.teleporterAtlas.findRegion("teleporter_appear"), 4, 4, 3));
        idleAnim = new Animation<TextureRegion>(0.08f, Loader.loadAnim(Loader.teleporterAtlas.findRegion("teleporter_idle"), 4, 4, 1));
        //idleAnim = new Animation<TextureRegion>(0.08f, Loader.loadAnim(Loader.botAtlas.findRegion("bot_talk"), 12, 6, 3));
        disappearingAnim = new Animation<TextureRegion>(0.12f, Loader.loadAnim(Loader.teleporterAtlas.findRegion("teleporter_disappear"), 4, 4, 1));

        setBounds(b2body.getPosition().x - Loader.teleporterAtlas.findRegion("teleporter_appear").getRegionWidth()/4/ MyGdxGame.PPM/2, b2body.getPosition().y - 170/MyGdxGame.PPM/2,
                Loader.teleporterAtlas.findRegion("teleporter_appear").getRegionWidth()/4/MyGdxGame.PPM, Loader.teleporterAtlas.findRegion("teleporter_appear").getRegionHeight()/4/MyGdxGame.PPM);
    }

    public void update(float dt) {
        setRegion(getFrame(dt));
        previousState = currentState;
    }

    private TextureRegion getFrame(float dt) {
        TextureRegion region;
        switch (currentState) {
            case APPEARING:
                region = appearingAnim.getKeyFrame(stateTimer, false);
                break;
            case DISAPPEARING:
                region = disappearingAnim.getKeyFrame(stateTimer, false);
                break;
            default:
                region = idleAnim.getKeyFrame(stateTimer, true);
                break;
        }

        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;
        return region;
    }

    public void disappear() {
        currentState = State.DISAPPEARING;
    }

}
