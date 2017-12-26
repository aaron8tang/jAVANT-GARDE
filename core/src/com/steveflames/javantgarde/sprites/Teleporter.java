package com.steveflames.javantgarde.sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.steveflames.javantgarde.MyGdxGame;
import com.steveflames.javantgarde.tools.Assets;

/**
 * Created by Flames on 23/10/2017.
 */

public class Teleporter extends GameObject {

    public enum State { HIDDEN, IDLE, DISAPPEARING }
    private State currentState;
    public State previousState;
    private TextureRegion currentTR;
    private Animation<TextureRegion> idleAnim;
    private Animation<TextureRegion> disappearingAnim;
    private float stateTimer = 0f;

    private Sound teleporterSound;

    public Teleporter(String name, World world, TiledMap map, Rectangle bounds, Assets assets) {
        super(name, world, map, bounds, true);
        currentState = State.IDLE;
        previousState = State.IDLE;
        teleporterSound = assets.get(Assets.teleportSOUND, Sound.class);

        idleAnim = new Animation<TextureRegion>(0.08f, Assets.loadAnim(assets.getTextureAtlas().findRegion(Assets.teleporterIdleREGION), 4, 4, 3));
        disappearingAnim = new Animation<TextureRegion>(0.12f, Assets.loadAnim(assets.getTextureAtlas().findRegion(Assets.teleporterDisappearREGION), 4, 4, 1 ));

        currentTR = idleAnim.getKeyFrame(0);

        setBounds(b2body.getPosition().x - currentTR.getRegionWidth()/4/ MyGdxGame.PPM/2,
                b2body.getPosition().y - 170/ MyGdxGame.PPM/2,
                currentTR.getRegionWidth()/4/ MyGdxGame.PPM,
                currentTR.getRegionHeight()/4/ MyGdxGame.PPM);
    }

    public void update(float dt) {
        currentTR = getFrame(dt);
        previousState = currentState;
    }

    private TextureRegion getFrame(float dt) {
        TextureRegion region;
        switch (currentState) {
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

    public void drawFont(SpriteBatch sb) {}
    public void drawFilled(ShapeRenderer sr) {}
    public void drawLine(ShapeRenderer sr) {}

    public void drawFontScaled(SpriteBatch sb) {
        sb.draw(currentTR, position.x - 225/2/ MyGdxGame.PPM, position.y - 180/2/ MyGdxGame.PPM, 225/ MyGdxGame.PPM, 211/ MyGdxGame.PPM);
    }

    public void disappear() {
        teleporterSound.play();
        stateTimer = 0;
        currentState = State.DISAPPEARING;
    }
}
