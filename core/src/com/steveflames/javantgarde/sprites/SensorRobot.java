package com.steveflames.javantgarde.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.steveflames.javantgarde.MyGdxGame;
import com.steveflames.javantgarde.tools.global.Loader;

/**
 * Created by Flames on 14/12/2017.
 */

public class SensorRobot extends GameObject {

    public enum State { IDLE, STARTMOVE, ENDMOVE}
    private State currentState;
    private State previousState;
    private TextureRegion currentTR;
    private Animation<TextureRegion> idleAnim;
    private Animation<TextureRegion> startAnim;
    private Animation<TextureRegion> endAnim;
    private float stateTimer = 0f;


    public SensorRobot(String name, World world, TiledMap map, Rectangle bounds) {
        super(name, world, map, bounds, false);

        currentState = State.IDLE;
        previousState = State.IDLE;

        idleAnim = new Animation<TextureRegion>(0.06f ,Loader.loadAnim(Loader.frogAtlas.findRegion("frog_talk"), 16,4,2));
        startAnim = new Animation<TextureRegion>(0.06f ,Loader.loadAnim(Loader.frogAtlas.findRegion("frog_moveStart"), 5,1,0));
        endAnim = new Animation<TextureRegion>(0.06f ,Loader.loadAnim(Loader.frogAtlas.findRegion("frog_moveEnd"), 5,1,0));
    }

    @Override
    public void update(float dt) {
        currentTR = getFrame(dt);
        previousState = currentState;
    }

    private TextureRegion getFrame(float dt) {
        TextureRegion region;
        switch (currentState) {
            case STARTMOVE:
                region = startAnim.getKeyFrame(stateTimer, true);
                break;
            case ENDMOVE:
                region = endAnim.getKeyFrame(stateTimer, true);
                break;
            default:
                region = idleAnim.getKeyFrame(stateTimer, true);
                break;
        }

        /*if(facingDirection ==1 && !region.isFlipX())
            region.flip(true, false);
        else if(facingDirection ==-1 && region.isFlipX())
            region.flip(true, false);*/
        if(!region.isFlipY())
            region.flip(false, true);

        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;
        return region;
    }

    @Override
    public void drawFilled(ShapeRenderer sr) {

    }

    @Override
    public void drawLine(ShapeRenderer sr) {

    }

    @Override
    public void drawFont(SpriteBatch sb) {

    }

    @Override
    public void drawFontScaled(SpriteBatch sb) {
        sb.draw(currentTR, position.x - bounds.width/2/ MyGdxGame.PPM, position.y - bounds.height/2/MyGdxGame.PPM, bounds.width/MyGdxGame.PPM, bounds.height/MyGdxGame.PPM);
    }
}
