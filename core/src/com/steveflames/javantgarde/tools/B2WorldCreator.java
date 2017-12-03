package com.steveflames.javantgarde.tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.steveflames.javantgarde.MyGdxGame;
import com.steveflames.javantgarde.sprites.Item;
import com.steveflames.javantgarde.sprites.Quiz;
import com.steveflames.javantgarde.screens.PlayScreen;
import com.steveflames.javantgarde.sprites.Checkpoint;
import com.steveflames.javantgarde.sprites.Door;
import com.steveflames.javantgarde.sprites.FloatingPlatform;
import com.steveflames.javantgarde.sprites.InfoSign;
import com.steveflames.javantgarde.sprites.Lever;
import com.steveflames.javantgarde.sprites.Pc;
import com.steveflames.javantgarde.sprites.Teleporter;
import com.steveflames.javantgarde.sprites.ropes.Rope;

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
            playScreen.getObjectManager().getPcs().add(new Pc(object.getName(), playScreen.getWorld(), playScreen.getMap(), rect));
            playScreen.getObjectManager().addGameObject(playScreen.getObjectManager().getPcs().get(playScreen.getObjectManager().getPcs().size()-1));
        }

        //initialize info
        for(MapObject object: playScreen.getMap().getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            playScreen.getObjectManager().getInfoSigns().add(new InfoSign(object.getName(), playScreen.getWorld(), playScreen.getMap(), rect, 1));
            playScreen.getObjectManager().addGameObject(playScreen.getObjectManager().getInfoSigns().get(playScreen.getObjectManager().getInfoSigns().size()-1));
        }

        //initialize doors
        for(MapObject object: playScreen.getMap().getLayers().get(6).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            playScreen.getObjectManager().getDoors().add(new Door(object.getName(), playScreen.getWorld(), playScreen.getMap(), rect));
            playScreen.getObjectManager().addGameObject(playScreen.getObjectManager().getDoors().get(playScreen.getObjectManager().getDoors().size()-1));
        }

        //initialize checkpoints
        for(MapObject object: playScreen.getMap().getLayers().get(7).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            playScreen.getObjectManager().getCheckpoints().add(new Checkpoint(object.getName(), playScreen.getWorld(), playScreen.getMap(), rect));
            playScreen.getObjectManager().addGameObject(playScreen.getObjectManager().getCheckpoints().get(playScreen.getObjectManager().getCheckpoints().size()-1));
        }

        //initialize teleporter
        for(MapObject object: playScreen.getMap().getLayers().get(8).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            playScreen.getObjectManager().setTeleporter(new Teleporter(object.getName(), playScreen.getWorld(), playScreen.getMap(), rect));
            playScreen.getObjectManager().addGameObject(playScreen.getObjectManager().getTeleporter());
        }

        if(playScreen.getMap().getLayers().getCount() > 9) {
            //initialize items
            for (MapObject object : playScreen.getMap().getLayers().get(9).getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                if(object.getName() != null) {
                    if (object.getName().equals("health")) {
                        Item item = new Item(object.getName(), playScreen.getWorld(), playScreen.getMap(), rect);
                        item.setUsable(true);
                        playScreen.getObjectManager().getItems().add(item);
                        playScreen.getObjectManager().addGameObject(playScreen.getObjectManager().getItems().get(playScreen.getObjectManager().getItems().size()-1));
                    }
                    else if(object.getName().contains("class")) {
                        Item item = new Item(object.getName(), playScreen.getWorld(), playScreen.getMap(), rect);
                        item.setUsable(true);
                        playScreen.getObjectManager().getItems().add(item);
                        playScreen.getObjectManager().addGameObject(playScreen.getObjectManager().getItems().get(playScreen.getObjectManager().getItems().size()-1));
                    }
                    else if(object.getName().contains("marker")) {
                        playScreen.getObjectManager().getMarkers().add(rect);
                    }
                    else if(object.getName().contains("quiz")) {
                        playScreen.getObjectManager().getQuizes().add(new Quiz(object.getName(), playScreen.getWorld(), playScreen.getMap(), rect, playScreen.getCurrentLevel().getId(), playScreen.getObjectManager().getDoors()));
                        playScreen.getObjectManager().addGameObject(playScreen.getObjectManager().getQuizes().get(playScreen.getObjectManager().getQuizes().size()-1));
                    }
                    else if(object.getName().contains("floatingPlatform")) {
                        if(object.getName().contains("-")) { //quiz floatingPlatform
                            String[] splitter = object.getName().split("-");
                            playScreen.getObjectManager().getQuizes().get(0).addFloatingPlatform(new FloatingPlatform(object.getName(), playScreen.getWorld(), playScreen.getMap(), rect,
                                    new Lever("lever_" + splitter[1], playScreen.getWorld(), playScreen.getMap(), new Rectangle(rect.x+ rect.width/2-45,
                                            rect.y + rect.height, 90,90), 1, true)));
                            playScreen.getObjectManager().addGameObject(playScreen.getObjectManager().getQuizes().get(0).getFloatingPlatforms().get(playScreen.getObjectManager().getQuizes().get(0).getFloatingPlatforms().size()-1));// todo OXI get(0) alla auto p einai ontws.. prepei ta floatingPlatforms na len poianou quiz einai
                        }
                        else {//not a quiz floatingPlatform
                            playScreen.getObjectManager().getFloatingPlatforms().add(new FloatingPlatform(object.getName(), playScreen.getWorld(), playScreen.getMap(), rect, null));
                            playScreen.getObjectManager().addGameObject(playScreen.getObjectManager().getFloatingPlatforms().get(playScreen.getObjectManager().getFloatingPlatforms().size()-1));
                        }
                    }
                }
            }

            if(playScreen.getMap().getLayers().getCount() > 10) {
                //initialize ropes
                for (MapObject object : playScreen.getMap().getLayers().get(10).getObjects().getByType(RectangleMapObject.class)) {
                    Rectangle rect = ((RectangleMapObject) object).getRectangle();
                    playScreen.getObjectManager().getRopes().add(new Rope(object.getName(), playScreen.getWorld(), playScreen.getMap(), rect, playScreen.getObjectManager()));
                    playScreen.getObjectManager().addGameObject(playScreen.getObjectManager().getRopes().get(playScreen.getObjectManager().getRopes().size()-1));
                }
            }
        }

    }
}
