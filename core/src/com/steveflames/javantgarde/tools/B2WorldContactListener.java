package com.steveflames.javantgarde.tools;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.steveflames.javantgarde.MyGdxGame;
import com.steveflames.javantgarde.hud.Hud;
import com.steveflames.javantgarde.screens.PlayScreen;
import com.steveflames.javantgarde.sprites.Checkpoint;
import com.steveflames.javantgarde.sprites.FloatingPlatform;
import com.steveflames.javantgarde.sprites.GameObject;
import com.steveflames.javantgarde.sprites.InfoSign;
import com.steveflames.javantgarde.sprites.Item;
import com.steveflames.javantgarde.sprites.Lever;
import com.steveflames.javantgarde.sprites.Marker;
import com.steveflames.javantgarde.sprites.Pc;
import com.steveflames.javantgarde.sprites.Player;
import com.steveflames.javantgarde.sprites.SensorRobot;
import com.steveflames.javantgarde.sprites.Teleporter;
import com.steveflames.javantgarde.sprites.ropes.Platform;

/**
 * Created by Flames on 19/4/16.
 */
public class B2WorldContactListener implements ContactListener {

    private String[] splitter;
    private PlayScreen playScreen;
    private Assets assets;

    public B2WorldContactListener(PlayScreen playScreen) {
        this.playScreen = playScreen;
        this.assets = playScreen.getAssets();
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if(fixA.getUserData() instanceof Player || fixB.getUserData() instanceof Player) {
            Fixture player = fixA.getUserData() instanceof Player ? fixA : fixB;
            Fixture object = player == fixA ? fixB : fixA;

            if(object.getUserData() instanceof GameObject) {
                if(object.getUserData() instanceof Checkpoint) {
                    if(!((Checkpoint) object.getUserData()).isVisited()) {
                        splitter = ((Checkpoint) object.getUserData()).getName().split("-");
                        ((Player)player.getUserData()).setCurrentCheckpointIndex(Integer.parseInt(splitter[2]));

                        if (((Checkpoint) object.getUserData()).getName().equals("checkpoint-1_1-0")) {
                            if (!((Checkpoint) object.getUserData()).isVisited()) {
                                playScreen.getHud().newToast(MyGdxGame.platformDepended.getLevel1Tip());
                            }
                        } else {
                            if(((Checkpoint) object.getUserData()).getText() != null)
                                playScreen.getHud().newToast(((Checkpoint) object.getUserData()).getText());
                        }
                        ((Checkpoint) object.getUserData()).setVisited(true);
                    }
                }
                else if(object.getUserData() instanceof Item) {
                    playScreen.getObjectManager().getObjectsToRemove().add((Item)object.getUserData());
                    playScreen.getObjectManager().getItems().remove(object.getUserData());
                    if(((Item) object.getUserData()).isUsable()) {
                        assets.playSound(assets.getItemSound);
                        if (((Item) object.getUserData()).getName().equals("health"))
                            ((Player) player.getUserData()).addHealth();
                        else if (((Item) object.getUserData()).getName().contains("class")) {
                            ((Player) player.getUserData()).addClass(((Item) object.getUserData()).getName());
                        }
                    }
                    ((Item) object.getUserData()).setUsable(false);
                }
                else if(object.getUserData() instanceof Teleporter) {
                    ((GameObject) object.getUserData()).setUsable(true);
                    if(((Teleporter) object.getUserData()).getName().equals("teleporter_end")) {
                        ((Player)player.getUserData()).fadeOut();
                        ((Teleporter) object.getUserData()).disappear();
                    }
                }
                else if(object.getUserData() instanceof InfoSign ) {
                    ((GameObject) object.getUserData()).setUsable(true);
                    Player.colliding = true;
                    playScreen.getHud().showUseBtn("READ");
                }
                else if(object.getUserData() instanceof Pc) {
                    ((GameObject) object.getUserData()).setUsable(true);
                    Player.colliding = true;
                    playScreen.getHud().showUseBtn("CODE");
                }
                else if(object.getUserData() instanceof Lever) {
                    ((Lever) object.getUserData()).setUsable(true);
                    Player.colliding = true;
                    ((Lever) object.getUserData()).setColliding(true);
                    if(((Lever) object.getUserData()).isManualPull()) {
                        if(((Player)player.getUserData()).position.y > 0)
                            playScreen.getHud().showUseBtn("PULL");
                    }
                }

            }
            else if(object.getUserData().equals("cyberfrogLeftSensor")) {
                assets.playSound(assets.frogSound);
                ((Player)player.getUserData()).b2body.applyLinearImpulse(-5,0,0,0,true);
            }
            else if(object.getUserData().equals("cyberfrogRightSensor")) {
                assets.playSound(assets.frogSound);
                ((Player)player.getUserData()).b2body.applyLinearImpulse(5,0,0,0,true);
            }
            else if(object.getUserData().equals("cyberfrogUpperSensor")) {
                assets.playSound(assets.frogSound);
                ((Player)player.getUserData()).setCurrentState(Player.State.JUMPING);
                ((Player)player.getUserData()).b2body.applyLinearImpulse(0,20,0,0,true);
            }
        }
        else if(fixA.getUserData().equals("playerDownSensor") || fixB.getUserData().equals("playerDownSensor")) {
            Fixture player = fixA.getUserData().equals("playerDownSensor") ? fixA : fixB;
            Fixture object = player == fixA ? fixB : fixA;

            if(object.getUserData() instanceof Platform) {
                ((Platform) object.getUserData()).setActive(false);
            }
        }
        else if(fixA.getUserData().equals("cyberfrogRightSensor") || fixB.getUserData().equals("cyberfrogRightSensor")) {
            Fixture cyberfrogSensor = fixA.getUserData().equals("cyberfrogRightSensor") ? fixA : fixB;
            Fixture object = cyberfrogSensor == fixA ? fixB : fixA;

            if(object.getUserData().equals("ground")) {
                for(SensorRobot cyberfrog: playScreen.getObjectManager().getSensorRobots()) {
                    if (cyberfrog.b2body.getFixtureList().contains(cyberfrogSensor, true)) {
                        cyberfrog.jump();
                        break;
                    }
                }
            }
            else if(object.getUserData() instanceof Marker) {
                if(((Marker) object.getUserData()).getName().contains("destination")) {
                    for(SensorRobot cyberfrog: playScreen.getObjectManager().getSensorRobots()) {
                        if (cyberfrog.b2body.getFixtureList().contains(cyberfrogSensor, true)) {
                            assets.playSound(assets.frogSound);
                            cyberfrog.completed(playScreen);
                            break;
                        }
                    }
                }
            }
        }
        else if(fixA.getUserData().equals("cyberfrogUpperSensor") || fixB.getUserData().equals("cyberfrogUpperSensor")) {
            Fixture cyberfrogSensor = fixA.getUserData().equals("cyberfrogUpperSensor") ? fixA : fixB;
            //Fixture object = cyberfrogSensor == fixA ? fixB : fixA;

            for(SensorRobot cyberfrog: playScreen.getObjectManager().getSensorRobots()) {
                if (cyberfrog.b2body.getFixtureList().contains(cyberfrogSensor, true)) {
                    if(cyberfrog.isUpperSensorEnabled())
                        cyberfrog.setUpperSensorDetectsObject(1);
                    break;
                }
            }
        }
        else if(fixA.getUserData() instanceof FloatingPlatform || fixB.getUserData() instanceof FloatingPlatform) {
            Fixture floatingPlatform = fixA.getUserData() instanceof FloatingPlatform ? fixA : fixB;
            Fixture object = floatingPlatform == fixA ? fixB : fixA;

            if(object.getUserData() instanceof Marker) {
                ((FloatingPlatform)floatingPlatform.getUserData()).b2body.setGravityScale(0);
                ((FloatingPlatform)floatingPlatform.getUserData()).b2body.setLinearVelocity(0,0);
                if(playScreen.getObjectManager().getPcs().size()>=2) {
                    playScreen.getObjectManager().getPcs().get(0).b2body.setLinearVelocity(0,0);
                    playScreen.getObjectManager().getPcs().get(1).b2body.setLinearVelocity(0,0);
                }
            }
        }
        if(fixA.getUserData().equals("ground") || fixB.getUserData().equals("ground")) {
            assets.playSound(assets.bumpSound);
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if(fixA.getUserData() instanceof Player || fixB.getUserData() instanceof Player) {
            Fixture player = fixA.getUserData() instanceof Player ? fixA : fixB;
            Fixture object = player == fixA ? fixB : fixA;

            if(object.getUserData() instanceof GameObject) {

                if(!(object.getUserData() instanceof Checkpoint)) {
                    ((GameObject) object.getUserData()).setUsable(false);
                }
                if(object.getUserData() instanceof InfoSign || object.getUserData() instanceof  Pc) {
                    ((GameObject) object.getUserData()).setUsable(false);
                    Player.colliding = false;
                    playScreen.getHud().hideUseBtn();
                }
                else if (object.getUserData() instanceof Lever) {
                    ((GameObject) object.getUserData()).setUsable(false);
                    ((Lever) object.getUserData()).setColliding(false);
                    Player.colliding = false;
                    playScreen.getHud().hideUseBtn();
                }
            }
        }
        else if(fixA.getUserData().equals("cyberfrogUpperSensor") || fixB.getUserData().equals("cyberfrogUpperSensor")) {
            Fixture cyberfrogSensor = fixA.getUserData().equals("cyberfrogUpperSensor") ? fixA : fixB;
            Fixture object = cyberfrogSensor == fixA ? fixB : fixA;

            if(object.getUserData().equals("ground")) {
                for (SensorRobot cyberfrog : playScreen.getObjectManager().getSensorRobots()) {
                    if (cyberfrog.b2body.getFixtureList().contains(cyberfrogSensor, true)) {
                        if(cyberfrog.isUpperSensorEnabled())
                            cyberfrog.setUpperSensorDetectsObject(2);
                        break;
                    }
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
