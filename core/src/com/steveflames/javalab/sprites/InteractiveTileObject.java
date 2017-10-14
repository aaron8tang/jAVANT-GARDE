package com.steveflames.javalab.sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.steveflames.javalab.MyGdxGame;

/**
 * Created by Flames on 24/9/2017.
 */

public abstract class InteractiveTileObject {
    protected String name;
    protected World world;
    protected TiledMap map;
    protected TiledMapTile tile;
    protected Rectangle bounds;
    protected Body body;
    protected Fixture fixture;

    protected boolean usable = false;

    public InteractiveTileObject(String name, World world, TiledMap map, Rectangle bounds, boolean sensor) {
        this.name = name;
        this.world = world;
        this.map = map;
        this.bounds = bounds;

        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();

        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((bounds.getX() + bounds.getWidth()/2)/ MyGdxGame.PPM, (bounds.getY() + bounds.getHeight()/2)/ MyGdxGame.PPM);

        body = world.createBody(bdef);

        shape.setAsBox(bounds.getWidth()/2/MyGdxGame.PPM, bounds.getHeight()/2/MyGdxGame.PPM);
        fdef.shape = shape;
        fixture = body.createFixture(fdef);
        fixture.setUserData(this);
        fixture.setSensor(sensor);
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
}
