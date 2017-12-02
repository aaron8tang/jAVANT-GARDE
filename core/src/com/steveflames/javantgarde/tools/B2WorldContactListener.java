package com.steveflames.javantgarde.tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.steveflames.javantgarde.scenes.Hud;
import com.steveflames.javantgarde.screens.PlayScreen;
import com.steveflames.javantgarde.sprites.Checkpoint;
import com.steveflames.javantgarde.sprites.InfoSign;
import com.steveflames.javantgarde.sprites.Pc;
import com.steveflames.javantgarde.sprites.Teleporter;

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

        if(fixA.getUserData() instanceof com.steveflames.javantgarde.sprites.Player || fixB.getUserData() instanceof com.steveflames.javantgarde.sprites.Player) {
            Fixture player = fixA.getUserData() instanceof com.steveflames.javantgarde.sprites.Player ? fixA : fixB;
            Fixture object = player == fixA ? fixB : fixA;

            if(object.getUserData() instanceof com.steveflames.javantgarde.sprites.GameObject) {

                if(object.getUserData() instanceof Checkpoint) {
                    if(!((Checkpoint) object.getUserData()).isVisited()) {
                        splitter = ((Checkpoint) object.getUserData()).getName().split("-");
                        ((com.steveflames.javantgarde.sprites.Player)player.getUserData()).setCurrentCheckpointIndex(Integer.parseInt(splitter[2]));

                        if (PlayScreen.currentLevel.getId().equals("1_1")) {
                            if (!((Checkpoint) object.getUserData()).isVisited()) {
                                Hud.newToast(com.steveflames.javantgarde.MyGdxGame.platformDepended.getLevel1Tip());
                            }
                        } else {
                            if(((Checkpoint) object.getUserData()).getText() != null)
                                Hud.newToast(((Checkpoint) object.getUserData()).getText());
                        }
                        ((Checkpoint) object.getUserData()).setVisited(true);
                    }
                }
                else if(object.getUserData() instanceof com.steveflames.javantgarde.sprites.Item) {
                    playScreen.getObjectsToRemove().add((com.steveflames.javantgarde.sprites.Item)object.getUserData());
                    playScreen.getItems().remove(object.getUserData());
                    if(((com.steveflames.javantgarde.sprites.Item) object.getUserData()).isUsable()) {
                        if (((com.steveflames.javantgarde.sprites.Item) object.getUserData()).getName().equals("health"))
                            ((com.steveflames.javantgarde.sprites.Player) player.getUserData()).addHealth();
                        else if (((com.steveflames.javantgarde.sprites.Item) object.getUserData()).getName().contains("class")) {
                            ((com.steveflames.javantgarde.sprites.Player) player.getUserData()).addClass(((com.steveflames.javantgarde.sprites.Item) object.getUserData()).getName());
                        }
                    }
                    ((com.steveflames.javantgarde.sprites.Item) object.getUserData()).setUsable(false);
                }
                else if(object.getUserData() instanceof Teleporter) {
                    ((com.steveflames.javantgarde.sprites.GameObject) object.getUserData()).setUsable(true);
                    if(((Teleporter) object.getUserData()).getName().equals("teleporter_end")) {
                        ((com.steveflames.javantgarde.sprites.Player)player.getUserData()).fadeOut();
                        ((Teleporter) object.getUserData()).disappear();
                    }
                }
                else if(object.getUserData() instanceof InfoSign ) {
                    ((com.steveflames.javantgarde.sprites.GameObject) object.getUserData()).setUsable(true);
                    com.steveflames.javantgarde.sprites.Player.colliding = true;
                    playScreen.getHud().showUseBtn("READ");
                }
                else if(object.getUserData() instanceof Pc) {
                    ((com.steveflames.javantgarde.sprites.GameObject) object.getUserData()).setUsable(true);
                    com.steveflames.javantgarde.sprites.Player.colliding = true;
                    playScreen.getHud().showUseBtn("CODE");
                }
                else if(object.getUserData() instanceof com.steveflames.javantgarde.sprites.Lever) {
                    ((com.steveflames.javantgarde.sprites.Lever) object.getUserData()).setUsable(true);
                    com.steveflames.javantgarde.sprites.Player.colliding = true;
                    ((com.steveflames.javantgarde.sprites.Lever) object.getUserData()).setColliding(true);
                    if(((com.steveflames.javantgarde.sprites.Lever) object.getUserData()).isManualPull())
                        playScreen.getHud().showUseBtn("PULL");
                }
                /*else if(object.getUserData() instanceof FloatingPlatform) {
                    ((GameObject) object.getUserData()).setUsable(true);
                    ((GameObject) object.getUserData()).getB2body().setLinearVelocity(((FloatingPlatform) object.getUserData()).getFacing()*0.7f, -((Player)player.getUserData()).b2body.getLinearVelocity().y);
                    ((Player)player.getUserData()).b2body.setLinearVelocity(0,0);

                }*/
            }
        }
        else if(fixA.getUserData().equals("bot_lower_sensor") || fixB.getUserData().equals("bot_lower_sensor")) {
            Fixture player = fixA.getUserData().equals("bot_lower_sensor") ? fixA : fixB;
            Fixture object = player == fixA ? fixB : fixA;

            if(object.getUserData() instanceof com.steveflames.javantgarde.sprites.ropes.Platform) {
                ((com.steveflames.javantgarde.sprites.ropes.Platform) object.getUserData()).setActive(false);
            }
        }
        /*else if(fixA.getUserData() instanceof FloatingPlatform || fixB.getUserData() instanceof FloatingPlatform) {
            Fixture floatingPlatform = fixA.getUserData() instanceof FloatingPlatform ? fixA : fixB;
            //Fixture object = floatingPlatform == fixA ? fixB : fixA;

            ((FloatingPlatform)floatingPlatform.getUserData()).setFacing(-((FloatingPlatform)floatingPlatform.getUserData()).getFacing());
            ((GameObject) floatingPlatform.getUserData()).getB2body().setLinearVelocity(((FloatingPlatform) floatingPlatform.getUserData()).getFacing()*0.7f, 0);
        }*/
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if(fixA.getUserData() instanceof com.steveflames.javantgarde.sprites.Player || fixB.getUserData() instanceof com.steveflames.javantgarde.sprites.Player) {
            Fixture player = fixA.getUserData() instanceof com.steveflames.javantgarde.sprites.Player ? fixA : fixB;
            Fixture object = player == fixA ? fixB : fixA;

            if(object.getUserData() instanceof com.steveflames.javantgarde.sprites.GameObject) {

                if(!(object.getUserData() instanceof Checkpoint)) {
                    ((com.steveflames.javantgarde.sprites.GameObject) object.getUserData()).setUsable(false);
                }
                if(object.getUserData() instanceof InfoSign || object.getUserData() instanceof  Pc) {
                    ((com.steveflames.javantgarde.sprites.GameObject) object.getUserData()).setUsable(false);
                    com.steveflames.javantgarde.sprites.Player.colliding = false;
                    playScreen.getHud().hideUseBtn();
                }
                else if (object.getUserData() instanceof com.steveflames.javantgarde.sprites.Lever) {
                    ((com.steveflames.javantgarde.sprites.GameObject) object.getUserData()).setUsable(false);
                    ((com.steveflames.javantgarde.sprites.Lever) object.getUserData()).setColliding(false);
                    com.steveflames.javantgarde.sprites.Player.colliding = false;
                    playScreen.getHud().hideUseBtn();
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
