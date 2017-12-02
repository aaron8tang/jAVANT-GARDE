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
    //protected TiledMapTile tile;
    protected Rectangle bounds;
    public Body b2body;
    protected Fixture fixture;
    protected float alpha = 1;

    protected boolean usable = false;

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

        bdef.type = BodyDef.BodyType.KinematicBody;
        bdef.position.set((bounds.getX() + bounds.getWidth()/2)/ MyGdxGame.PPM, (bounds.getY() + bounds.getHeight()/2)/ MyGdxGame.PPM);
        b2body = world.createBody(bdef);

        shape.setAsBox(bounds.getWidth()/ MyGdxGame.PPM/2, bounds.getHeight()/ MyGdxGame.PPM/2);
        fdef.shape = shape;
        fixture = b2body.createFixture(fdef);
        fixture.setUserData(this);
        fixture.setSensor(sensor);

        position = new Vector2(b2body.getPosition().x, b2body.getPosition().y);
        position_previous = new Vector2(b2body.getPosition().x, b2body.getPosition().y);
    }

    public GameObject() {

    }

    //define player
    public void definePlayer(World world, float radius) {
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        //bot upper body
        FixtureDef fdef = new FixtureDef();
        PolygonShape polyShape = new PolygonShape();
        polyShape.setAsBox(0.1f,0.21f, new Vector2(0, 0.13f), 0);
        fdef.shape = polyShape;
        b2body.createFixture(fdef).setUserData(this);

        fdef = new FixtureDef();
        polyShape = new PolygonShape();
        polyShape.setAsBox(0.07f,0.01f, new Vector2(0, -0.3f), 0);
        fdef.shape = polyShape;
        Fixture fixture = b2body.createFixture(fdef);
        fixture.setUserData("bot_lower_sensor"); //bot_lower_sensor
        fixture.setSensor(true);

        //bot wheel
        fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setPosition(new Vector2(0,-0.19f));
        shape.setRadius(radius);
        fdef.shape = shape;
        fixture = b2body.createFixture(fdef);
        fixture.setUserData(this);

        this.bounds = new Rectangle(b2body.getPosition().x, b2body.getPosition().y, 81, 88);
    }

    public abstract void update(float dt);
    public abstract void drawFilled(ShapeRenderer sr);
    public abstract void drawLine(ShapeRenderer sr);
    public abstract void drawFont(SpriteBatch sb);
    public abstract void drawFontScaled(SpriteBatch sb);

    void updateAlpha(float dt) {
        if(alpha + dt*0.65f < 1)
            alpha += dt*0.65f;
        else
            alpha = 1;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public boolean isUsable() {
        return usable;
    }

    public void setUsable(boolean usable) {
        this.usable = usable;
    }

    public String getName() {
        return name;
    }

    public Body getB2body() {
        return b2body;
    }
}
