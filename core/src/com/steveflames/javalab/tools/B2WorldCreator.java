package com.steveflames.javalab.tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.steveflames.javalab.MyGdxGame;
import com.steveflames.javalab.quests.Quiz;
import com.steveflames.javalab.screens.PlayScreen;
import com.steveflames.javalab.sprites.Checkpoint;
import com.steveflames.javalab.sprites.Door;
import com.steveflames.javalab.sprites.FloatingPlatform;
import com.steveflames.javalab.sprites.Item;
import com.steveflames.javalab.sprites.InfoSign;
import com.steveflames.javalab.sprites.Lever;
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
            playScreen.getObjectManager().addDynamicGameObject(playScreen.getPcs().get(playScreen.getPcs().size()-1));
        }

        //initialize info
        for(MapObject object: playScreen.getMap().getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            playScreen.getInfoSigns().add(new InfoSign(object.getName(), playScreen.getWorld(), playScreen.getMap(), rect, 1));
            playScreen.getObjectManager().addDynamicGameObject(playScreen.getInfoSigns().get(playScreen.getInfoSigns().size()-1));
        }

        //initialize doors
        for(MapObject object: playScreen.getMap().getLayers().get(6).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            playScreen.getDoors().add(new Door(object.getName(), playScreen.getWorld(), playScreen.getMap(), rect));
            playScreen.getObjectManager().addDynamicGameObject(playScreen.getDoors().get(playScreen.getDoors().size()-1));
        }

        //initialize checkpoints
        for(MapObject object: playScreen.getMap().getLayers().get(7).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            playScreen.getCheckpoints().add(new Checkpoint(object.getName(), playScreen.getWorld(), playScreen.getMap(), rect));
            playScreen.getObjectManager().addDynamicGameObject(playScreen.getCheckpoints().get(playScreen.getCheckpoints().size()-1));
        }

        //initialize teleporter
        for(MapObject object: playScreen.getMap().getLayers().get(8).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            playScreen.setTeleporter(new Teleporter(object.getName(), playScreen.getWorld(), playScreen.getMap(), rect));
            playScreen.getObjectManager().addDynamicGameObject(playScreen.getTeleporter());
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
                        playScreen.getObjectManager().addDynamicGameObject(playScreen.getItems().get(playScreen.getItems().size()-1));
                    }
                    else if(object.getName().contains("class")) {
                        Item item = new Item(object.getName(), playScreen.getWorld(), playScreen.getMap(), rect);
                        item.setUsable(true);
                        playScreen.getItems().add(item);
                        playScreen.getObjectManager().addDynamicGameObject(playScreen.getItems().get(playScreen.getItems().size()-1));
                    }
                    else if(object.getName().contains("marker")) {
                        playScreen.getMarkers().add(rect);
                    }
                    else if(object.getName().contains("quiz")) {
                        playScreen.setQuiz(new Quiz(object.getName(), playScreen.getWorld(), playScreen.getMap(), rect, playScreen.getCurrentLevel().getId(), playScreen.getDoors()));
                        playScreen.getObjectManager().addDynamicGameObject(playScreen.getQuiz());
                    }
                    else if(object.getName().contains("floatingPlatform")) {
                        if(object.getName().contains("-")) { //quiz floatingPlatform
                            String[] splitter = object.getName().split("-");
                            playScreen.getQuiz().addFloatingPlatform(new FloatingPlatform(object.getName(), playScreen.getWorld(), playScreen.getMap(), rect,
                                    new Lever("lever_" + splitter[1], playScreen.getWorld(), playScreen.getMap(), new Rectangle(rect.x+ rect.width/2-45,
                                            rect.y + rect.height, 90,90), 1, true)));
                            playScreen.getObjectManager().addDynamicGameObject(playScreen.getQuiz().getFloatingPlatforms().get(playScreen.getQuiz().getFloatingPlatforms().size()-1));
                        }
                        else {//not a quiz floatingPlatform
                            playScreen.getFloatingPlatforms().add(new FloatingPlatform(object.getName(), playScreen.getWorld(), playScreen.getMap(), rect, null));
                            playScreen.getObjectManager().addDynamicGameObject(playScreen.getFloatingPlatforms().get(playScreen.getFloatingPlatforms().size()-1));
                        }
                    }
                }
            }

            if(playScreen.getMap().getLayers().getCount() > 10) {
                //initialize ropes
                for (MapObject object : playScreen.getMap().getLayers().get(10).getObjects().getByType(RectangleMapObject.class)) {
                    Rectangle rect = ((RectangleMapObject) object).getRectangle();
                    playScreen.getRopes().add(new Rope(object.getName(), playScreen.getWorld(), playScreen.getMap(), rect, playScreen.getObjectManager()));
                    playScreen.getObjectManager().addDynamicGameObject(playScreen.getRopes().get(playScreen.getRopes().size()-1));
                }
            }
        }

    }
}
