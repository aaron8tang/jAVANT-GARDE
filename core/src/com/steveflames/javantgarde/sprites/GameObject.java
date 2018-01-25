package com.steveflames.javantgarde.sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.steveflames.javantgarde.MyGdxGame;

/**
 * Contains the basic characteristics of a Box2D body.
 * Every dynamic object in this game, extends this class.
 */

public abstract class GameObject extends Sprite{
    protected String name;
    protected World world;
    protected TiledMap map;
    protected Rectangle bounds;
    public Body b2body;
    protected Fixture fixture;

    float alpha = 1;
    boolean usable = false;

    public Vector2 position;
    public Vector2 position_previous;
    public float angle;
    public float angle_previous;

    public GameObject(String name, World world, TiledMap map, Rectangle bounds, boolean sensor) {
        this.name = name;
        this.world = world;
        this.map = map;
        this.bounds = bounds;

        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();

        if(!(this instanceof Player)) {
            bdef.type = BodyDef.BodyType.KinematicBody;
            bdef.position.set((bounds.getX() + bounds.getWidth() / 2) / MyGdxGame.PPM, (bounds.getY() + bounds.getHeight() / 2) / MyGdxGame.PPM);
            b2body = world.createBody(bdef);

            shape.setAsBox(bounds.getWidth() / 2 / MyGdxGame.PPM, bounds.getHeight() / 2 / MyGdxGame.PPM);
            fdef.shape = shape;
            fixture = b2body.createFixture(fdef);
            fixture.setUserData(this);
            fixture.setSensor(sensor);

            position = new Vector2(b2body.getPosition().x, b2body.getPosition().y);
            position_previous = new Vector2(b2body.getPosition().x, b2body.getPosition().y);

            //add object specific body parts
            if(this instanceof SensorRobot) { //cyberfrog
                shape.setAsBox(1/MyGdxGame.PPM, 30/MyGdxGame.PPM, new Vector2(38/MyGdxGame.PPM, 4/MyGdxGame.PPM), 0);
                fdef.shape = shape;
                fixture = b2body.createFixture(fdef);
                fixture.setUserData("cyberfrogRightSensor");
                fixture.setSensor(true);

                shape.setAsBox(1/MyGdxGame.PPM, 30/MyGdxGame.PPM, new Vector2(-38/MyGdxGame.PPM, 4/MyGdxGame.PPM), 0);
                fdef.shape = shape;
                fixture = b2body.createFixture(fdef);
                fixture.setUserData("cyberfrogLeftSensor");
                fixture.setSensor(true);

                shape.setAsBox(34/MyGdxGame.PPM, 1/MyGdxGame.PPM, new Vector2(0, 40/MyGdxGame.PPM), 0);
                fdef.shape = shape;
                fixture = b2body.createFixture(fdef);
                fixture.setUserData("cyberfrogUpperSensor");
                fixture.setSensor(true);
            }
        }
        else { //different body structure for Player sprite
            bdef = new BodyDef();
            bdef.type = BodyDef.BodyType.DynamicBody;
            b2body = world.createBody(bdef);

            //bot upper body
            fdef = new FixtureDef();
            PolygonShape polyShape = new PolygonShape();
            polyShape.setAsBox(20/MyGdxGame.PPM,42/MyGdxGame.PPM, new Vector2(0, 26/MyGdxGame.PPM), 0);
            fdef.shape = polyShape;
            b2body.createFixture(fdef).setUserData(this);

            fdef = new FixtureDef();
            polyShape = new PolygonShape();
            polyShape.setAsBox(14/MyGdxGame.PPM,2/MyGdxGame.PPM, new Vector2(0, -60/MyGdxGame.PPM), 0);
            fdef.shape = polyShape;
            Fixture fixture = b2body.createFixture(fdef);
            fixture.setUserData("playerDownSensor"); //bot_lower_sensor
            fixture.setSensor(true);

            //bot wheel
            fdef = new FixtureDef();
            CircleShape circleShape = new CircleShape();
            circleShape.setPosition(new Vector2(0,-38/MyGdxGame.PPM));
            circleShape.setRadius(Player.radius);
            fdef.shape = circleShape;
            fixture = b2body.createFixture(fdef);
            fixture.setUserData(this);

            this.bounds = new Rectangle(b2body.getPosition().x, b2body.getPosition().y, 81, 88);
        }
    }

    void updateAlpha(float dt) {
        if(alpha + dt*0.65f < 1)
            alpha += dt*0.65f;
        else
            alpha = 1;
    }

    /**
     * Update the physics of the current object in the game loop.
     * @param dt The delta time between two consecutive loops.
     */
    public abstract void update(float dt);

    /**
     * Draw the color-filled shapes of the object.
     * @param sr The ShapeRenderer provided by the main loop.
     */
    public abstract void drawFilled(ShapeRenderer sr);

    /**
     * Draw the colored line shapes of the object.
     * @param sr The ShapeRenderer provided by the main loop.
     */
    public abstract void drawLine(ShapeRenderer sr);

    /**
     * Draw the fonts and the textures of the object. (camera unscaled)
     * @param sb The SpriteBatch provided by the main loop.
     */
    public abstract void drawFont(SpriteBatch sb);

    /**
     * Draw the fonts and textures of the object. (camera scaled)
     * @param sb The SpriteBatch provided by the main loop.
     */
    public abstract void drawFontScaled(SpriteBatch sb);

    public boolean isUsable() {
        return usable;
    }

    public void setUsable(boolean usable) {
        this.usable = usable;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public String getName() {
        return name;
    }

    public Body getB2body() {
        return b2body;
    }
}
