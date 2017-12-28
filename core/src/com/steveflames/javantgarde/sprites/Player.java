package com.steveflames.javantgarde.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.TimeUtils;
import com.steveflames.javantgarde.MyGdxGame;
import com.steveflames.javantgarde.sprites.ropes.Rope;
import com.steveflames.javantgarde.tools.Assets;
import com.steveflames.javantgarde.tools.global.Cameras;
import com.steveflames.javantgarde.tools.global.Fonts;
import com.steveflames.javantgarde.tools.global.MyFileReader;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by Flames on 23/9/2017.
 */

public class Player extends GameObject {

    private static final float PLAYERSPEED = 10f; //multiplied by dt
    private static final float MAXSPEED = 2.4f;
    private static final float JUMPSPEED = 8.3f;

    public enum State { FALLING, JUMPING, STANDING, RUNNING, DEAD, CODING, READING, DISAPPEARING, DISAPPEARED }
    private State currentState;
    private State previousState;
    private boolean outOfBounds = false;
    public boolean canMove = true;
    private int facingDirection = 1;
    private boolean runLeft = false;
    private boolean runRight = false;
    private int health = 5;
    public static final float radius = 24/MyGdxGame.PPM;
    private int currentCheckpointIndex = 0;
    private TextureRegion currentTR;
    private LinkedHashMap<String, String> classes = new LinkedHashMap<String, String>();
    private ArrayList<Checkpoint> checkpoints;

    //player msg variables (e.g. "-1 health")
    private String playerMsg = null;
    private long playerMsgMillis = TimeUtils.millis();
    private float playerMsgAlpha = 1f;
    private GlyphLayout playerMsgGlyph = new GlyphLayout();
    private Vector2 playerMsgVector = new Vector2();
    private float red;
    private float green;
    private float blue;
    private float alpha = 1f;

    public static boolean colliding = false;

    private float stateTimer = 0f;
    private float rotation =0;

    private Assets assets;

    public Player(World world, TiledMap map, ArrayList<Checkpoint> checkpoints, Assets assets) {
        super("player", world, map, new Rectangle(), false);
        this.assets = assets;
        currentTR = assets.botMoveTR;
        this.checkpoints = checkpoints;
        setInitialPosition();

        currentState = State.STANDING;
        previousState = State.STANDING;
    }

    private void setInitialPosition() {
        setBounds(b2body.getPosition().x - bounds.width/MyGdxGame.PPM/2, b2body.getPosition().y - bounds.height/MyGdxGame.PPM/2, bounds.width/MyGdxGame.PPM, bounds.height/MyGdxGame.PPM);
        b2body.setTransform(checkpoints.get(currentCheckpointIndex).getBounds().x / MyGdxGame.PPM,
                (checkpoints.get(currentCheckpointIndex).getBounds().y + checkpoints.get(currentCheckpointIndex).getBounds().height / 2) / MyGdxGame.PPM, 0);
        position = new Vector2(b2body.getPosition().x, b2body.getPosition().y);
        position_previous = new Vector2(b2body.getPosition().x, b2body.getPosition().y);
    }

    public void drawFilled(ShapeRenderer sr) {}
    public void drawLine(ShapeRenderer sr) {}
    public void drawFont(SpriteBatch sb) {}

    public void drawFontScaled(SpriteBatch sb) {
        sb.setColor(1,1,1,alpha);
        sb.draw(currentTR, position.x - bounds.width/2/MyGdxGame.PPM, position.y - 17/MyGdxGame.PPM, bounds.width/MyGdxGame.PPM, bounds.height/MyGdxGame.PPM);
        sb.draw(assets.botWheelTR, position.x - 49/2/MyGdxGame.PPM , position.y - 0.32f
                , 49/MyGdxGame.PPM/2,  49/MyGdxGame.PPM/2
                , 49/MyGdxGame.PPM, 49/MyGdxGame.PPM
                , 0.935f, 0.935f
                , rotation);
    }

    public void drawPlayerMsg(SpriteBatch sb) {
        if(playerMsg != null) {
            Fonts.small.setColor(red,green,blue,playerMsgAlpha);
            Fonts.small.draw(sb, playerMsg, playerMsgVector.x*MyGdxGame.PPM  + Cameras.getHudCameraOffsetX()
                    - playerMsgGlyph.width/2, playerMsgVector.y*MyGdxGame.PPM + 30);
        }
    }

    public void update(float dt) {
        //set current region of the bot's animation
        currentTR = getFrame(dt);
        setCurrentState(getState());
        previousState = currentState;
        setPosition(b2body.getPosition().x - bounds.width/MyGdxGame.PPM/2, b2body.getPosition().y - bounds.height/MyGdxGame.PPM/2 + 0.12f);

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

        //player run left or right
        if(runRight && b2body.getLinearVelocity().x <= Player.MAXSPEED)
            b2body.applyLinearImpulse(PLAYERSPEED*dt, 0, b2body.getWorldCenter().x, b2body.getWorldCenter().y, true);
        else if(runLeft && b2body.getLinearVelocity().x >= -Player.MAXSPEED)
            b2body.applyLinearImpulse(-PLAYERSPEED*dt, 0, b2body.getWorldCenter().x, b2body.getWorldCenter().y, true);


        //update DISAPPEARING state of player
        if(currentState == State.DISAPPEARING) {
            if(alpha - 0.9f*dt > 0) {
                alpha -= 0.9f*dt;
            }
            else { //todo kamia fora de bainei
                setCurrentState(State.DISAPPEARED);
                alpha = 0;
                b2body.setLinearVelocity(0,0);
                b2body.setTransform(b2body.getPosition().x + 0.2f, b2body.getPosition().y, 0);
            }
            setAlpha(alpha);
        }

        //update playerMsg (e.g. '-1 health')
        if(playerMsg!= null) {
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
                region = assets.botTypingAnimation.getKeyFrame(stateTimer, true);
                break;
            case JUMPING:
                region = assets.botMoveTR;
                break;
            default:
                region = assets.botIdleAnimation.getKeyFrame(stateTimer, true);
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
        else if(currentState == State.DISAPPEARED)
            return State.DISAPPEARED;
            //if player is going positive in Y-Axis he is jumping
        else if(b2body.getLinearVelocity().y > 0 && currentState == State.JUMPING)
            return State.JUMPING;
            //if negative in Y-Axis player is falling
        else if(b2body.getLinearVelocity().y < 0)
            return State.FALLING;
            //if player is positive or negative in the X axis he is running
        else if (b2body.getLinearVelocity().x < -0.8 || b2body.getLinearVelocity().x > 0.8)
            return State.RUNNING;
            //if none of these return then he must be standing
        else
            return State.STANDING;
    }

    public void jump() {
        if (currentState != State.JUMPING  && currentState != State.FALLING) {
            b2body.applyLinearImpulse(0, JUMPSPEED, b2body.getWorldCenter().x, b2body.getWorldCenter().y, true);
            setCurrentState(State.JUMPING);
            assets.playSound(assets.jumpSound);
        }
    }

    public void setRunLeft(boolean runLeft) {
        if(runLeft)
            facingDirection = -1;
        this.runLeft = runLeft;
    }

    public void setRunRight(boolean runRight) {
        if(runRight)
            facingDirection = 1;
        this.runRight = runRight;
    }

    public void respawnAtCheckpoint(ArrayList<Rope> ropes) {
        if(health>0) {
            currentState = State.STANDING;
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
        playerMsg = "+1 health";
        playerMsgGlyph.setText(Fonts.small, playerMsg);
        playerMsgVector.x = b2body.getPosition().x;
        playerMsgVector.y = b2body.getPosition().y + 0.25f;
        red = 0;
        green = 1;
        blue = 0;
        playerMsgAlpha = 1;
    }

    public void reduceHealth(int k) {
        assets.playSound(assets.loseHealthSound);
        health -= k;
        playerMsg = "-"+k+" health";
        playerMsgGlyph.setText(Fonts.small, playerMsg);
        playerMsgVector.x = b2body.getPosition().x;
        playerMsgVector.y = b2body.getPosition().y + 0.25f;
        if(health <= 0) {
            setCurrentState(State.DEAD);
            assets.playSound(assets.deadSound);
        }
        green = 0;
        red = 1;
        blue = 0;
        playerMsgAlpha = 1;
    }

    public void addClass(String text) {
        String[] temp = text.split("-");
        classes.put(temp[2], MyFileReader.readFile("txt/classes/"+text+".txt")); //todo
        playerMsg = "+"+temp[2]+" class";
        playerMsgGlyph.setText(Fonts.small, playerMsg);
        playerMsgVector.x = b2body.getPosition().x;
        playerMsgVector.y = b2body.getPosition().y + 0.25f;
        red = 1;
        green = 0;
        blue = 0;
        playerMsgAlpha = 1;
    }

    public void showPlayerMsg(String msg) {
        playerMsg = msg;
        playerMsgGlyph.setText(Fonts.small, playerMsg);
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
            playerMsg = null;
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

    public void setFacingDirection(int facingDirection) {
        this.facingDirection = facingDirection;
    }

    public float getPlayerMsgAlpha() {
        return playerMsgAlpha;
    }
}
