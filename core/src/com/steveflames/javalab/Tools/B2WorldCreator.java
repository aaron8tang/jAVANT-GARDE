package com.steveflames.javalab.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.steveflames.javalab.MyGdxGame;
import com.steveflames.javalab.levels.PlayScreen;
import com.steveflames.javalab.sprites.Pc;

/**
 * Created by Flames on 23/9/2017.
 */

public class B2WorldCreator {

    public B2WorldCreator(PlayScreen playScreen) {
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        //initialize ground
        for(MapObject object: playScreen.getMap().getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth()/2) / MyGdxGame.PPM, (rect.getY() + rect.getHeight()/2) / MyGdxGame.PPM);
            body = playScreen.getWorld().createBody(bdef);
            shape.setAsBox(rect.getWidth()/2 / MyGdxGame.PPM, rect.getHeight()/2 / MyGdxGame.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
        }

        for(MapObject object: playScreen.getMap().getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            playScreen.getPcs().add(new Pc(playScreen.getWorld(), playScreen.getMap(), rect));
        }
    }
}
