package com.steveflames.javalab.sprites;

import com.badlogic.gdx.graphics.Color;
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
import com.steveflames.javalab.MyGdxGame;

/**
 * Created by Flames on 23/9/2017.
 */

public class Player extends Sprite {


    public enum State { FALLING, JUMPING, STANDING, RUNNING, DEAD, CODING };
    public State currentState;
    public State previousState;

    public World world;
    public Body b2body;
    private int health = 5;
    private final float radius = 50/MyGdxGame.PPM;

    private Fixture fixture;


    public Player(World world) {
        this.world = world;
        definePlayer();

        currentState = State.STANDING;
        previousState = State.STANDING;
    }

    private void definePlayer() {
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

    public void draw(ShapeRenderer sr) {
        sr.setColor(Color.BLACK);
        sr.circle(b2body.getPosition().x, b2body.getPosition().y, radius, 100);
    }

    public void update(float dt) {
        currentState = getState();
        previousState = currentState;
    }

    public int getHealth() {
        return health;
    }

    public void jump() {
        if ( currentState != State.JUMPING ) {
            b2body.applyLinearImpulse(new Vector2(0, 8f), b2body.getWorldCenter(), true);
            currentState = State.JUMPING;
        }
    }

    public State getState(){
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
            return State.STANDING;
        }
    }

    public float getRadius() {
        return radius;
    }
}
