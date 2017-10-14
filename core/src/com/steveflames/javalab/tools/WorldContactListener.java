package com.steveflames.javalab.tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.steveflames.javalab.sprites.Checkpoint;
import com.steveflames.javalab.sprites.InteractiveTileObject;
import com.steveflames.javalab.sprites.Pc;
import com.steveflames.javalab.sprites.Player;
import com.steveflames.javalab.sprites.ropes.Platform;

/**
 * Created by Flames on 19/4/16.
 */
public class WorldContactListener implements ContactListener {

    private String[] splitter;

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if(fixA.getUserData() instanceof Player || fixB.getUserData() instanceof Player) {
            Fixture player = fixA.getUserData() instanceof Player ? fixA : fixB;
            Fixture object = player == fixA ? fixB : fixA;

            if(object.getUserData() instanceof InteractiveTileObject) {
                //System.out.println("COLLISION START "+object.getBody().getPosition().x + " " + object.getBody().getPosition().y);
                ((InteractiveTileObject) object.getUserData()).setUsable(true);
                Player.colliding = true;
                if(object.getUserData() instanceof Checkpoint) {
                    splitter = ((Checkpoint) object.getUserData()).getName().split("_");
                    ((Player)player.getUserData()).setCurrentCheckpointIndex(Integer.parseInt(splitter[1]));
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
                ((InteractiveTileObject) object.getUserData()).setUsable(false);
                Player.colliding = false;
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
