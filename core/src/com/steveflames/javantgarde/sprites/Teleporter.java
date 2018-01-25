package com.steveflames.javantgarde.sprites;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.steveflames.javantgarde.MyGdxGame;
import com.steveflames.javantgarde.tools.Assets;

/**
 * Implements the finishing gate on every level of the game.
 * When the player interacts with a finishing gate, the level is completed.
 */

public class Teleporter extends GameObject {

    public enum State { HIDDEN, IDLE, DISAPPEARING }
    private State currentState;
    private State previousState;
    private TextureRegion currentTR;
    private float stateTimer = 0f;

    private Assets assets;

    public Teleporter(String name, World world, TiledMap map, Rectangle bounds, Assets assets) {
        super(name, world, map, bounds, true);
        this.assets = assets;
        currentState = State.IDLE;
        previousState = State.IDLE;

        currentTR = assets.teleporterIdleAnimation.getKeyFrame(0);

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
                region = assets.teleporterDisappearingAnimation.getKeyFrame(stateTimer, false);
                break;
            default:
                region = assets.teleporterIdleAnimation.getKeyFrame(stateTimer, true);
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
        assets.playSound(assets.teleportSound);
        stateTimer = 0;
        currentState = State.DISAPPEARING;
    }
}
