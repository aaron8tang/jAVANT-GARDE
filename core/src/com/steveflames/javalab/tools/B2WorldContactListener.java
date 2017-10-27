package com.steveflames.javalab.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.steveflames.javalab.MyGdxGame;
import com.steveflames.javalab.scenes.Hud;
import com.steveflames.javalab.screens.PlayScreen;
import com.steveflames.javalab.sprites.Checkpoint;
import com.steveflames.javalab.sprites.Health;
import com.steveflames.javalab.sprites.InteractiveTileObject;
import com.steveflames.javalab.sprites.Player;
import com.steveflames.javalab.sprites.Teleporter;
import com.steveflames.javalab.sprites.ropes.Platform;

/**
 * Created by Flames on 19/4/16.
 */
public class B2WorldContactListener implements ContactListener {

    private String[] splitter;
    private PlayScreen playScreen;

    public B2WorldContactListener(PlayScreen playScreen) {
        this.playScreen = playScreen;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if(fixA.getUserData() instanceof Player || fixB.getUserData() instanceof Player) {
            Fixture player = fixA.getUserData() instanceof Player ? fixA : fixB;
            Fixture object = player == fixA ? fixB : fixA;

            if(object.getUserData() instanceof InteractiveTileObject) {

                if(object.getUserData() instanceof Checkpoint) {
                    splitter = ((Checkpoint) object.getUserData()).getName().split("-");
                    ((Player)player.getUserData()).setCurrentCheckpointIndex(Integer.parseInt(splitter[2]));
                    if(PlayScreen.currentLevel.getId().equals("1_1")) {
                        if(!((Checkpoint) object.getUserData()).isVisited()) {
                            ((Checkpoint) object.getUserData()).setVisited(true);
                            Hud.newToast(MyGdxGame.platformDepended.getLevel1Tip());
                        }
                    }
                    else {
                        //((Player)player.getUserData()).b2body.setTransform(((Player)player.getUserData()).b2body.getPosition().x, ((Player)player.getUserData()).b2body.getPosition().y, 0);
                        if(MyFileReader.exists("txt/" + ((Checkpoint) object.getUserData()).getName() + ".txt")) {
                            if(!((Checkpoint) object.getUserData()).isVisited()) {
                                ((Checkpoint) object.getUserData()).setVisited(true);
                                Hud.newToast(MyFileReader.readFile("txt/" + ((Checkpoint) object.getUserData()).getName() + ".txt"));
                            }
                        }
                    }
                }
                else if(object.getUserData() instanceof Health) {
                    playScreen.getBodiesToRemove().add(object.getBody());
                    playScreen.getHealths().remove(object.getUserData());
                    ((Player)player.getUserData()).addHealth();
                }
                else if(object.getUserData() instanceof Teleporter) {
                    ((InteractiveTileObject) object.getUserData()).setUsable(true);
                    Player.colliding = true;
                    if(((Teleporter) object.getUserData()).getName().equals("teleporter_end")) {
                        Gdx.input.setInputProcessor(playScreen.getHud().stage);
                        ((Player)player.getUserData()).fadeOut();
                        ((Teleporter) object.getUserData()).disappear();
                        //((Player)player.getUserData()).b2body.setTransform(((Teleporter)object.getUserData()).getB2body().getPosition().x, ((Teleporter)object.getUserData()).getB2body().getPosition().y, 0);
                    }
                }
                else {
                    //System.out.println("COLLISION START "+object.getBody().getPosition().x + " " + object.getBody().getPosition().y);
                    ((InteractiveTileObject) object.getUserData()).setUsable(true);
                    Player.colliding = true;
                }
            }
            else if(object.getUserData() instanceof Platform) {
                ((Platform) object.getUserData()).setActive(false);
            }
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if(fixA.getUserData() instanceof Player || fixB.getUserData() instanceof Player) {
            Fixture player = fixA.getUserData() instanceof Player ? fixA : fixB;
            Fixture object = player == fixA ? fixB : fixA;

            if(object.getUserData() instanceof InteractiveTileObject) {

                //System.out.println("COLLISION END "+object.getBody().getPosition().x + " " + object.getBody().getPosition().y);
                if(!(object.getUserData() instanceof Checkpoint)) {
                    ((InteractiveTileObject) object.getUserData()).setUsable(false);
                    Player.colliding = false;
                }
            }
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
