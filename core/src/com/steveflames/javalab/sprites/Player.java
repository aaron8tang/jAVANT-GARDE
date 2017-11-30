package com.steveflames.javalab.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.TimeUtils;
import com.steveflames.javalab.MyGdxGame;
import com.steveflames.javalab.screens.PlayScreen;
import com.steveflames.javalab.sprites.ropes.Rope;
import com.steveflames.javalab.tools.global.MyFileReader;
import com.steveflames.javalab.tools.global.Fonts;
import com.steveflames.javalab.tools.global.Loader;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by Flames on 23/9/2017.
 */

public class Player extends GameObject {

    public static final float PLAYERSPEED = 0.24f; //0.24f
    private static final float JUMPSPEED = 8.3f;

    public enum State { FALLING, JUMPING, STANDING, RUNNING, DEAD, CODING, READING, DISAPPEARING, DISAPPEARED }
    private State currentState;
    private State previousState;
    private boolean outOfBounds = false;
    private ArrayList<Checkpoint> checkpoints;
    private int currentCheckpointIndex = 0;
    private int health = 5;
    private LinkedHashMap<String, String> classes = new LinkedHashMap<String, String>();

    private TextureRegion currentTR;
    private final float radius = 22/MyGdxGame.PPM;
    public boolean canMove = true;

    private String hitMsg = null;
    private long playerMsgMillis = TimeUtils.millis();
    private float playerMsgAlpha = 1f;
    private GlyphLayout playerMsgGlyph = new GlyphLayout();
    private Vector2 playerMsgVector = new Vector2();
    private int facingDirection = 1;
    private float red;
    private float green;
    private float blue;
    private float alpha = 1f;

    public static boolean colliding = false;

    private Animation<TextureRegion> idleAnim;
    private Animation<TextureRegion> typingAnim;
    private float stateTimer = 0f;
    private float rotation =0;


    public Player(World world, ArrayList<Checkpoint> checkpoints) {
        definePlayer(world, radius);
        currentTR = Loader.botAtlas.findRegion("bot_talk");
        this.checkpoints = checkpoints;
        setInitialPosition();

        currentState = State.STANDING;
        previousState = State.STANDING;

        idleAnim = new Animation<TextureRegion>(0.08f, Loader.loadAnim(Loader.botAtlas.findRegion("bot_talk"), 12, 6, 3));
        typingAnim = new Animation<TextureRegion>(0.015f, Loader.loadAnim(Loader.botAtlas.findRegion("bot_typing"), 12, 2, 0));
    }

    private void setInitialPosition() {
        setBounds(b2body.getPosition().x - 81/MyGdxGame.PPM/2, b2body.getPosition().y - 88/MyGdxGame.PPM/2, 81/MyGdxGame.PPM, 88/MyGdxGame.PPM);
        b2body.setTransform(checkpoints.get(currentCheckpointIndex).getBounds().x / MyGdxGame.PPM,
                (checkpoints.get(currentCheckpointIndex).getBounds().y + checkpoints.get(currentCheckpointIndex).getBounds().height / 2) / MyGdxGame.PPM, 0);
        position = new Vector2(b2body.getPosition().x, b2body.getPosition().y);
        position_previous = new Vector2(b2body.getPosition().x, b2body.getPosition().y);
    }

    @Override
    public void draw(Batch sb) {
        sb.setColor(1,1,1,alpha);
        sb.draw(currentTR, position.x - 81/2/MyGdxGame.PPM, position.y - 17/MyGdxGame.PPM, 81/MyGdxGame.PPM, 88/MyGdxGame.PPM);
        sb.draw(Loader.botWheelTR, position.x - 45/2/MyGdxGame.PPM , position.y - 0.31f
                , 45/MyGdxGame.PPM/2,  45/MyGdxGame.PPM/2
                , 45/MyGdxGame.PPM, 45/MyGdxGame.PPM
                , 1, 1
                , rotation);
    }

    public void drawPlayerMsg(SpriteBatch sb) {
        if(hitMsg != null) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

            Fonts.small.setColor(red,green,blue,playerMsgAlpha);
            Fonts.small.draw(sb, hitMsg, playerMsgVector.x*MyGdxGame.PPM  + PlayScreen.getHudCameraOffsetX()
                    - playerMsgGlyph.width/2, playerMsgVector.y*MyGdxGame.PPM + 20); //TODO cam k gia to y otan valw new lvls

            Gdx.gl.glDisable(GL20.GL_BLEND);
        }
    }

    public void update(float dt) {
        //set current region of the bot's animation
        currentTR = getFrame(dt);
        setCurrentState(getState());
        previousState = currentState;
        setPosition(b2body.getPosition().x - 81/MyGdxGame.PPM/2, b2body.getPosition().y - 88/MyGdxGame.PPM/2 + 0.12f);

        //update the rotation of the bot's wheel
        rotation += -dt*(b2body.getLinearVelocity().x*340 +b2body.getLinearVelocity().y*400);
        if(rotation >=360 || rotation <=-360)
            rotation = 0;

        //player out of bounds. reduce health and respawn
        if(b2body.getPosition().y + radius <= 0 && !outOfBounds) {
            outOfBounds = true;
            b2body.setLinearVelocity(0,0);
            reduceHealth(1);
        }

        //update DISAPPEARING state of player
        if(currentState == State.DISAPPEARING) {
            if(alpha - 0.9f*dt > 0) {
                alpha -= 0.9f*dt;
            }
            else {
                setCurrentState(State.DISAPPEARED);
                alpha = 0;
                b2body.setLinearVelocity(0,0);
                b2body.setTransform(b2body.getPosition().x + 0.2f, b2body.getPosition().y, 0);
            }
            setAlpha(alpha);
        }

        //update playerMsg (e.g. '-1 health')
        if(hitMsg!= null) {
            if(TimeUtils.timeSinceMillis(playerMsgMillis) > 10) {
                playerMsgMillis = TimeUtils.millis();
                playerMsgTick(Gdx.graphics.getDeltaTime());
            }
        }
    }

    private TextureRegion getFrame(float dt) {
        TextureRegion region;
        switch (currentState) {
            case RUNNING:
            case FALLING:
            case CODING:
                region = typingAnim.getKeyFrame(stateTimer, true);
                break;
            case JUMPING:
                region = Loader.botMoveTR;
                break;
            default:
                region = idleAnim.getKeyFrame(stateTimer, true);
                break;
        }

        if(facingDirection ==1 && !region.isFlipX())
            region.flip(true, false);
        else if(facingDirection ==-1 && region.isFlipX())
            region.flip(true, false);

        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;
        return region;
    }

    private State getState(){
        if(health<=0)
            return State.DEAD;
        if(currentState == State.CODING)
            return State.CODING;
        else if(currentState == State.READING)
            return State.READING;
        else if(currentState == State.DISAPPEARING)
            return State.DISAPPEARING;
            //if player is going positive in Y-Axis he is jumping
        else if(b2body.getLinearVelocity().y > 0 && currentState == State.JUMPING)
            return State.JUMPING;
            //if negative in Y-Axis player is falling
        else if(b2body.getLinearVelocity().y < 0)
            return State.FALLING;
            //if player is positive or negative in the X axis he is running
        else if(b2body.getLinearVelocity().x != 0) //todo otan strivei k ginei 0 to vel allazei stigmiaia h eikona
            return State.RUNNING;
            //if none of these return then he must be standing
        else
            return State.STANDING;
    }

    public void setCoding(Rectangle pcBounds) {
        facingDirection = 1;
        b2body.setLinearVelocity(0, 0);
        b2body.setTransform(pcBounds.x / MyGdxGame.PPM + 0.1f, (pcBounds.y + b2body.getPosition().y) / MyGdxGame.PPM + 0.3f, 0);
        setCurrentState(Player.State.CODING);
    }


    public void jump() {
        if ( currentState != State.JUMPING  && currentState != State.FALLING) {
            b2body.applyLinearImpulse(0, JUMPSPEED, b2body.getWorldCenter().x, b2body.getWorldCenter().y, true);
            setCurrentState(State.JUMPING);
        }
    }

    public void runRight(float dt) {
        b2body.applyLinearImpulse(PLAYERSPEED, 0, b2body.getWorldCenter().x,  b2body.getWorldCenter().y, true); //0.2f
        facingDirection = 1;
    }

    public void runLeft(float dt) {
        b2body.applyLinearImpulse(-PLAYERSPEED, 0, b2body.getWorldCenter().x, b2body.getWorldCenter().y, true);
        facingDirection = -1;
    }

    public void respawnAtCheckpoint(ArrayList<Rope> ropes) {
        if(health>0) {
            b2body.setTransform(checkpoints.get(currentCheckpointIndex).getBounds().x / MyGdxGame.PPM,
                    (checkpoints.get(currentCheckpointIndex).getBounds().y + checkpoints.get(currentCheckpointIndex).getBounds().height / 2) / MyGdxGame.PPM, 0);
            outOfBounds = false;
            for (Rope rope : ropes) {
                if(rope.getId() >= currentCheckpointIndex)
                    rope.reset();
            }
        }
    }

    public void addHealth() {
        health++;
        hitMsg = "+1 health";
        playerMsgGlyph.setText(Fonts.small, hitMsg);
        playerMsgVector.x = b2body.getPosition().x;
        playerMsgVector.y = b2body.getPosition().y + 0.25f;
        red = 0;
        green = 1;
        blue = 0;
        playerMsgAlpha = 1;
    }

    public void reduceHealth(int k) {
        health -= k;
        hitMsg = "-"+k+" health";
        playerMsgGlyph.setText(Fonts.small, hitMsg);
        playerMsgVector.x = b2body.getPosition().x;
        playerMsgVector.y = b2body.getPosition().y + 0.25f;
        if(health <= 0)
            setCurrentState(State.DEAD);
        green = 0;
        red = 1;
        blue = 0;
        playerMsgAlpha = 1;
    }

    public void addClass(String text) {
        String[] temp = text.split("-");
        classes.put(temp[2], MyFileReader.readFile("txt/classes/"+text+".txt"));
        hitMsg = "+"+temp[2]+" class";
        playerMsgGlyph.setText(Fonts.small, hitMsg);
        playerMsgVector.x = b2body.getPosition().x;
        playerMsgVector.y = b2body.getPosition().y + 0.25f;
        red = 1;
        green = 0;
        blue = 0;
        playerMsgAlpha = 1;
    }

    public void showPlayerMsg(String msg) {
        hitMsg = msg;
        playerMsgGlyph.setText(Fonts.small, hitMsg);
        playerMsgVector.x = b2body.getPosition().x;
        playerMsgVector.y = b2body.getPosition().y + 0.25f;
        red = 0;
        green = 1;
        blue = 0;
        playerMsgAlpha = 1;
    }

    public void fadeOut() {
        float sign = Math.signum(b2body.getLinearVelocity().x);
        b2body.setLinearVelocity(0,0);
        b2body.applyLinearImpulse(1.88f*sign, 0, 0, 0, false);
        setCurrentState(State.DISAPPEARING);
    }

    private void playerMsgTick(float dt) {
        if(playerMsgVector.y + 0.8f*dt < MyGdxGame.HEIGHT/MyGdxGame.PPM - 0.15) {
            if(currentState!=State.CODING)
                playerMsgVector.y += 0.8f * dt;
        }
        else
            playerMsgVector.y = MyGdxGame.HEIGHT/MyGdxGame.PPM - 0.14f + 0.8f*dt;
        playerMsgAlpha -= 0.4f * dt;
        if(playerMsgAlpha<=0) { //when playerMsgAlpha reaches 0 stop rendering the msg
            hitMsg = null;
            playerMsgAlpha = 1f;
        }
    }

    public void setPlayerMsgAlpha(float f) {
        playerMsgAlpha = f;
    }

    public void setCurrentCheckpointIndex(int currentCheckpointIndex) {
        this.currentCheckpointIndex = currentCheckpointIndex;
    }

    public boolean isOutOfBounds() {
        return outOfBounds;
    }

    public int getHealth() {
        return health;
    }

    public LinkedHashMap<String, String> getClasses() {
        return classes;
    }

    public State getCurrentState() {
        return currentState;
    }

    public void setCurrentState(State currentState) {
        canMove = !(currentState == State.CODING || currentState == State.READING || currentState == State.DISAPPEARING
                || currentState == State.DISAPPEARED || currentState == State.DEAD);
        this.currentState = currentState;
    }

    public float getPlayerMsgAlpha() {
        return playerMsgAlpha;
    }
}
