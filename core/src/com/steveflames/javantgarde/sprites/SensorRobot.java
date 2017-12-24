package com.steveflames.javantgarde.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.steveflames.javantgarde.MyGdxGame;
import com.steveflames.javantgarde.screens.PlayScreen;
import com.steveflames.javantgarde.tools.Assets;

/**
 * Created by Flames on 14/12/2017.
 */

public class SensorRobot extends GameObject {

    private static final float JUMPSPEED = 5.5f;

    public enum State { IDLE, MOVING, JUMPING, COMPLETEDTASK}
    private State currentState;
    private State previousState;
    private TextureRegion currentTR;
    private Animation<TextureRegion> idleAnim;
    private Animation<TextureRegion> startAnim;
    private Animation<TextureRegion> endAnim;
    private float stateTimer = 0f;

    private boolean upperSensorEnabled = false;
    private int upperSensorDetectsObject = 0;
    private boolean runRight = false;
    private boolean runLeft = false;


    public SensorRobot(String name, World world, TiledMap map, Rectangle bounds, TextureAtlas textureAtlas) {
        super(name, world, map, bounds, false);
        b2body.setType(BodyDef.BodyType.DynamicBody);
        b2body.setGravityScale(0.8f);

        currentState = State.IDLE;
        previousState = State.IDLE;
        idleAnim = new Animation<TextureRegion>(0.06f , Assets.loadAnim(textureAtlas.findRegion(Assets.frogTalkREGION), 16,4,2));
    }

    @Override
    public void update(float dt) {
        currentTR = getFrame(dt);
        setCurrentState(getState());
        previousState = currentState;

        if(runRight && b2body.getLinearVelocity().x < 0.5)
            b2body.applyLinearImpulse(0.1f, 0,0,0,true);
        else if(runLeft && b2body.getLinearVelocity().x > -0.5)
            b2body.applyLinearImpulse(-0.1f, 0,0,0,true);

        if(upperSensorEnabled) {
            if(upperSensorDetectsObject == 2){ //jump
                upperSensorDetectsObject = 0;
                if(currentState != State.JUMPING) {
                    jump();
                    setRunLeft(false);
                    setRunRight(true);
                }
            }
        }
    }

    private State getState(){
            //if player is going positive in Y-Axis he is jumping
        if(b2body.getLinearVelocity().y > 0 && currentState == State.JUMPING)
            return State.JUMPING;
            //if player is positive or negative in the X axis he is running
        else if(b2body.getLinearVelocity().x != 0)
            return State.MOVING;
            //if none of these return then he must be standing
        else
            return State.IDLE;
    }

    private TextureRegion getFrame(float dt) {
        TextureRegion region;
        switch (currentState) {
            /*case STARTMOVE:
                region = startAnim.getKeyFrame(stateTimer, false);
                break;
            case ENDMOVE:
                region = endAnim.getKeyFrame(stateTimer, false);
                break;*/
            default:
                region = idleAnim.getKeyFrame(stateTimer, true);
                break;
        }

        /*if(facingDirection ==1 && !region.isFlipX())
            region.flip(true, false);
        else if(facingDirection ==-1 && region.isFlipX())
            region.flip(true, false);*/

        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;
        return region;
    }

    public void setUpperSensor(boolean upperSensor) {
        this.upperSensorEnabled = upperSensor;
    }

    public void setRunLeft(boolean runLeft) {
        this.runLeft = runLeft;
    }

    public void setRunRight(boolean runRight) {
        this.runRight = runRight;
    }

    public void jump() {
        if(upperSensorDetectsObject == 0 && currentState != State.JUMPING) {
            b2body.applyLinearImpulse(0, JUMPSPEED, b2body.getWorldCenter().x, b2body.getWorldCenter().y, true);
            currentState = State.JUMPING;
        }
        else if (upperSensorDetectsObject == 1) {
            setRunRight(false);
            setRunLeft(true);
        }
    }

    @Override
    public void drawFilled(ShapeRenderer sr) {}
    @Override
    public void drawLine(ShapeRenderer sr) {}
    @Override
    public void drawFont(SpriteBatch sb) {}

    @Override
    public void drawFontScaled(SpriteBatch sb) {
        sb.draw(currentTR, position.x - bounds.width/2/ MyGdxGame.PPM, position.y - bounds.height/2/MyGdxGame.PPM, bounds.width/MyGdxGame.PPM, bounds.height/MyGdxGame.PPM);
    }

    public void completed(PlayScreen playScreen) {
        setCurrentState(State.COMPLETEDTASK);
        if(playScreen.getCurrentLevelID().equals("5_2")) {
            if(name.equals("0")) {
                playScreen.getObjectManager().getDoors().get(0).open();
            }
            else if(name.equals("1")) {
                playScreen.getObjectManager().getDoors().get(1).open();
            }
        }
    }

    public void setCurrentState(State currentState) {
        if(currentState == State.COMPLETEDTASK) {
            setRunRight(false);
            b2body.setLinearVelocity(0, 0);
        }
        this.currentState = currentState;
    }

    public void setUpperSensorDetectsObject(int upperSensorDetectsObject) {
        this.upperSensorDetectsObject = upperSensorDetectsObject;
    }

    public boolean isUpperSensorEnabled() {
        return upperSensorEnabled;
    }
}