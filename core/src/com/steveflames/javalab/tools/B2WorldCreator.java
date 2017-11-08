package com.steveflames.javalab.tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.steveflames.javalab.MyGdxGame;
import com.steveflames.javalab.screens.PlayScreen;
import com.steveflames.javalab.sprites.Checkpoint;
import com.steveflames.javalab.sprites.Door;
import com.steveflames.javalab.sprites.FloatingPlatform;
import com.steveflames.javalab.sprites.Item;
import com.steveflames.javalab.sprites.InfoSign;
import com.steveflames.javalab.sprites.Pc;
import com.steveflames.javalab.sprites.Teleporter;
import com.steveflames.javalab.sprites.ropes.Rope;

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
        for(MapObject object: playScreen.getMap().getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth()/2) / MyGdxGame.PPM, (rect.getY() + rect.getHeight()/2) / MyGdxGame.PPM);
            body = playScreen.getWorld().createBody(bdef);
            shape.setAsBox(rect.getWidth()/2 / MyGdxGame.PPM, rect.getHeight()/2 / MyGdxGame.PPM);
            fdef.shape = shape;
            body.createFixture(fdef).setUserData("ground");
        }

        //initialize pcs
        for(MapObject object: playScreen.getMap().getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            playScreen.getPcs().add(new Pc(object.getName(), playScreen.getWorld(), playScreen.getMap(), rect));
        }

        //initialize helps
        for(MapObject object: playScreen.getMap().getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            playScreen.getInfoSigns().add(new InfoSign(object.getName(), playScreen.getWorld(), playScreen.getMap(), rect));
        }

        //initialize doors
        for(MapObject object: playScreen.getMap().getLayers().get(6).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            playScreen.getDoors().add(new Door(object.getName(), playScreen.getWorld(), playScreen.getMap(), rect));
        }

        //initialize checkpoints
        for(MapObject object: playScreen.getMap().getLayers().get(7).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            playScreen.getCheckpoints().add(new Checkpoint(object.getName(), playScreen.getWorld(), playScreen.getMap(), rect));
        }

        //initialize teleporter
        for(MapObject object: playScreen.getMap().getLayers().get(8).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            playScreen.setTeleporter(new Teleporter(object.getName(), playScreen.getWorld(), playScreen.getMap(), rect));
        }

        if(playScreen.getMap().getLayers().getCount() > 9) {
            //initialize items
            for (MapObject object : playScreen.getMap().getLayers().get(9).getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                if(object.getName() != null) {
                    if (object.getName().equals("health")) {
                        Item item = new Item(object.getName(), playScreen.getWorld(), playScreen.getMap(), rect);
                        item.setUsable(true);
                        playScreen.getItems().add(item);
                    }
                    else if(object.getName().contains("class")) {
                        Item item = new Item(object.getName(), playScreen.getWorld(), playScreen.getMap(), rect);
                        item.setUsable(true);
                        playScreen.getItems().add(item);
                    }
                    else if(object.getName().contains("floatingPlatform")) {
                        playScreen.getFloatingPlatforms().add(new FloatingPlatform(object.getName(), playScreen.getWorld(), playScreen.getMap(), rect));
                    }
                }
            }

            if(playScreen.getMap().getLayers().getCount() > 10) {
                //initialize ropes
                for (MapObject object : playScreen.getMap().getLayers().get(10).getObjects().getByType(RectangleMapObject.class)) {
                    Rectangle rect = ((RectangleMapObject) object).getRectangle();
                    playScreen.getRopes().add(new Rope(object.getName(), playScreen.getWorld(), playScreen.getMap(), rect));
                }
            }
        }

    }
}
