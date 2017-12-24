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
 * Created by Flames on 24/9/2017.
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
                shape.setAsBox(0.005f, 0.15f, new Vector2(0.19f, 0.02f), 0);
                fdef.shape = shape;
                fixture = b2body.createFixture(fdef);
                fixture.setUserData("cyberfrogRightSensor");
                fixture.setSensor(true);

                shape.setAsBox(0.005f, 0.15f, new Vector2(-0.19f, 0.02f), 0);
                fdef.shape = shape;
                fixture = b2body.createFixture(fdef);
                fixture.setUserData("cyberfrogLeftSensor");
                fixture.setSensor(true);

                shape.setAsBox(0.17f, 0.005f, new Vector2(0, 0.2f), 0);
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
            polyShape.setAsBox(0.1f,0.21f, new Vector2(0, 0.13f), 0);
            fdef.shape = polyShape;
            b2body.createFixture(fdef).setUserData(this);

            fdef = new FixtureDef();
            polyShape = new PolygonShape();
            polyShape.setAsBox(0.07f,0.01f, new Vector2(0, -0.3f), 0);
            fdef.shape = polyShape;
            Fixture fixture = b2body.createFixture(fdef);
            fixture.setUserData("playerDownSensor"); //bot_lower_sensor
            fixture.setSensor(true);

            //bot wheel
            fdef = new FixtureDef();
            CircleShape circleShape = new CircleShape();
            circleShape.setPosition(new Vector2(0,-0.19f));
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

    public abstract void update(float dt);
    public abstract void drawFilled(ShapeRenderer sr);
    public abstract void drawLine(ShapeRenderer sr);
    public abstract void drawFont(SpriteBatch sb);
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
