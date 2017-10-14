package com.steveflames.javalab.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.TimeUtils;
import com.steveflames.javalab.MyGdxGame;
import com.steveflames.javalab.Window;
import com.steveflames.javalab.scenes.Hud;
import com.steveflames.javalab.screens.PlayScreen;
import com.steveflames.javalab.sprites.ropes.Rope;
import com.steveflames.javalab.tools.Fonts;

import java.util.ArrayList;

/**
 * Created by Flames on 23/9/2017.
 */

public class Player extends Sprite {

    public boolean isOutOfBounds() {
        return outOfBounds;
    }

    public enum State { FALLING, JUMPING, STANDING, RUNNING, DEAD, CODING, IDLE }
    public State currentState;
    public State previousState;
    private boolean outOfBounds = false;

    public Body b2body;
    private final float radius = 50/MyGdxGame.PPM;

    private int health = 5;
    private String hitMsg = null;
    private long playerMsgMillis = TimeUtils.millis();
    private float playerMsgAlpha = 1f;
    private Vector2 playerMsgVector = new Vector2();

    private Fixture fixture;
    public static boolean colliding = false;

    private ArrayList<Checkpoint> checkpoints;
    private int currentCheckpointIndex = 0;


    public Player(World world, ArrayList<Checkpoint> checkpoints) {
        this.checkpoints = checkpoints;
        definePlayer(world);

        currentState = State.STANDING;
        previousState = State.STANDING;
    }

    private void definePlayer(World world) {
        BodyDef bdef = new BodyDef();
        bdef.position.set(100 / MyGdxGame.PPM, 300 / MyGdxGame.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(radius);


        fdef.shape = shape;
        fixture = b2body.createFixture(fdef);
        fixture.setUserData(this);
    }

    public void draw(SpriteBatch sb, ShapeRenderer sr) {
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(Color.BLACK);
        sr.circle(b2body.getPosition().x, b2body.getPosition().y, radius, 100);
        sr.end();
    }

    public void drawPlayerMsg(SpriteBatch sb) {
        if(hitMsg != null) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

            sb.setProjectionMatrix(Hud.viewport.getCamera().combined);
            sb.begin();
            Fonts.small.setColor(1,0,0,playerMsgAlpha);
            Fonts.small.draw(sb, hitMsg, playerMsgVector.x*MyGdxGame.PPM  + Window.getHudCameraOffsetX()
                    - 80, playerMsgVector.y*MyGdxGame.PPM + 20); //TODO cam k gia to y otan valw new lvls
            sb.end();

            if(TimeUtils.timeSinceMillis(playerMsgMillis) > 10) {
                playerMsgMillis = TimeUtils.millis();
                playerMsgTick(Gdx.graphics.getDeltaTime());
                if(playerMsgAlpha==1 && outOfBounds) { //respawn
                    respawnAtCheckpoint();
                }
            }
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }
    }

    public void update(float dt) {
        currentState = getState();
        previousState = currentState;

        if(b2body.getPosition().y + radius <= 0 && !outOfBounds) {
            outOfBounds = true;
            b2body.setLinearVelocity(0,0);
            reduceHealth(1);
        }
    }

    private void respawnAtCheckpoint() {
        if(health>0) {
            b2body.setTransform(checkpoints.get(currentCheckpointIndex).getBounds().x / MyGdxGame.PPM,
                    (checkpoints.get(currentCheckpointIndex).getBounds().y + checkpoints.get(currentCheckpointIndex).getBounds().height / 2) / MyGdxGame.PPM, 0);
            outOfBounds = false;
            for (Rope rope : PlayScreen.ropes)
                rope.reset();
        }
    }

    public int getHealth() {
        return health;
    }

    public void jump() {
        if ( currentState != State.JUMPING  && currentState != State.FALLING) {
            b2body.applyLinearImpulse(new Vector2(0, 8f), b2body.getWorldCenter(), true);
            currentState = State.JUMPING;
        }
    }

    private State getState(){
        //Test to Box2D for velocity on the X and Y-Axis
        //if player is going positive in Y-Axis he is jumping... or if he just jumped and is falling remain in jump state
        if(health<=0)
            return State.DEAD;
        else if((b2body.getLinearVelocity().y > 0 && currentState == State.JUMPING) || (b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING))
            return State.JUMPING;
            //if negative in Y-Axis player is falling
        else if(b2body.getLinearVelocity().y < 0)
            return State.FALLING;
            //if player is positive or negative in the X axis he is running
        else if(b2body.getLinearVelocity().x != 0)
            return State.RUNNING;
            //if none of these return then he must be standing
        else {
            if(currentState == State.CODING)
                return State.CODING;
            else if(currentState == State.IDLE)
                return State.IDLE;
            return State.STANDING;
        }
    }

    public void reduceHealth(int k) {
        health -= k;
        hitMsg = "-"+k+" health";
        playerMsgVector.x = b2body.getPosition().x;
        playerMsgVector.y = b2body.getPosition().y + 0.25f;
        if(health <= 0) {
            currentState = State.DEAD;
        }
    }

    private void playerMsgTick(float dt) {
        playerMsgVector.y += 0.8f * dt;
        playerMsgAlpha -= 0.4f * dt;
        if(playerMsgAlpha<=0) { //when playerMsgAlpha reaches 0 stop rendering the msg
            hitMsg = null;
            playerMsgAlpha = 1f;
        }
    }

    public float getRadius() {
        return radius;
    }

    public void setPlayerMsgAlpha(float f) {
        playerMsgAlpha = f;
    }

    public void setCurrentCheckpointIndex(int currentCheckpointIndex) {
        this.currentCheckpointIndex = currentCheckpointIndex;
    }

}
