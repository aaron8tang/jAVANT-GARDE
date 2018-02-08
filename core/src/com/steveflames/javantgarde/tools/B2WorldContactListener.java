package com.steveflames.javantgarde.tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.TimeUtils;
import com.steveflames.javantgarde.MyGdxGame;
import com.steveflames.javantgarde.hud.Hud;
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
 * This class is responsible for all the collisions
 * that happen in-game (e.g. player-ground collision,
 * player-infoSign collision).
 */
public class B2WorldContactListener implements ContactListener {

    private String[] splitter;
    private Hud hud;
    private GameObjectManager objectManager;
    private Assets assets;
    private long bumpSoundMillis = TimeUtils.millis();

    public B2WorldContactListener(Hud hud, GameObjectManager objectManager, Assets assets) {
        this.hud = hud;
        this.objectManager = objectManager;
        this.assets = assets;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        //one of the colliding fixtures is Player
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
                                if(MyGdxGame.platformDepended.deviceHasKeyboard())
                                    hud.newToast(assets.playscreenBundle.get("1_1_pc"));
                                else
                                    hud.newToast(assets.playscreenBundle.get("1_1_mob"));
                            }
                        } else {
                            if(((Checkpoint) object.getUserData()).getText() != null)
                                hud.newToast(((Checkpoint) object.getUserData()).getText());
                        }
                        ((Checkpoint) object.getUserData()).setVisited(true);
                    }
                }
                else if(object.getUserData() instanceof Item) {
                    objectManager.getObjectsToRemove().add((Item)object.getUserData());
                    objectManager.getItems().remove(object.getUserData());
                    if(((Item) object.getUserData()).isUsable()) {
                        assets.playSound(assets.getItemSound);
                        if (((Item) object.getUserData()).getName().equals("health"))
                            ((Player) player.getUserData()).addHealth();
                        else if (((Item) object.getUserData()).getName().contains("class")) {
                            ((Player) player.getUserData()).addClass(((Item) object.getUserData()).getName(), ((Item) object.getUserData()).getText());
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
                    hud.showUseBtn(assets.playscreenBundle.get("read"));
                }
                else if(object.getUserData() instanceof Pc) {
                    ((GameObject) object.getUserData()).setUsable(true);
                    Player.colliding = true;
                    hud.showUseBtn(assets.playscreenBundle.get("code"));
                }
                else if(object.getUserData() instanceof Lever) {
                    ((Lever) object.getUserData()).setUsable(true);
                    Player.colliding = true;
                    ((Lever) object.getUserData()).setColliding(true);
                    if(((Lever) object.getUserData()).isManualPull()) {
                        if(((Player)player.getUserData()).position.y > 0)
                            hud.showUseBtn(assets.playscreenBundle.get("pull"));
                    }
                }
                else if(object.getUserData() instanceof FloatingPlatform) {
                    if(((FloatingPlatform) object.getUserData()).getB2body().getLinearVelocity().y ==0) { //for quiz wrong answer rebump
                        if (TimeUtils.timeSinceMillis(bumpSoundMillis) > 100) {
                            bumpSoundMillis = TimeUtils.millis();
                            assets.playSound(assets.bumpSound);
                        }
                    }
                }

            }
            else if(object.getUserData().equals("cyberfrogLeftSensor")) {
                assets.playSound(assets.frogSound);
                ((Player)player.getUserData()).b2body.applyLinearImpulse(-1000/MyGdxGame.PPM,0,0,0,true);
            }
            else if(object.getUserData().equals("cyberfrogRightSensor")) {
                assets.playSound(assets.frogSound);
                ((Player)player.getUserData()).b2body.applyLinearImpulse(1000/MyGdxGame.PPM,0,0,0,true);
            }
            else if(object.getUserData().equals("cyberfrogUpperSensor")) {
                assets.playSound(assets.frogSound);
                ((Player)player.getUserData()).setCurrentState(Player.State.JUMPING);
                ((Player)player.getUserData()).b2body.applyLinearImpulse(0,4000/MyGdxGame.PPM,0,0,true);
            }
        }
        //one of the colliding fixtures is Player's down sensor
        else if(fixA.getUserData().equals("playerDownSensor") || fixB.getUserData().equals("playerDownSensor")) {
            Fixture player = fixA.getUserData().equals("playerDownSensor") ? fixA : fixB;
            Fixture object = player == fixA ? fixB : fixA;

            if(object.getUserData() instanceof Platform) {
                ((Platform) object.getUserData()).setActive(false);
            }
        }
        //one of the colliding fixtures is cyber-frog's right sensor
        else if(fixA.getUserData().equals("cyberfrogRightSensor") || fixB.getUserData().equals("cyberfrogRightSensor")) {
            Fixture cyberfrogSensor = fixA.getUserData().equals("cyberfrogRightSensor") ? fixA : fixB;
            Fixture object = cyberfrogSensor == fixA ? fixB : fixA;

            if(object.getUserData().equals("ground")) {
                for(SensorRobot cyberfrog: objectManager.getSensorRobots()) {
                    if (cyberfrog.b2body.getFixtureList().contains(cyberfrogSensor, true)) {
                        cyberfrog.jump();
                        break;
                    }
                }
            }
            else if(object.getUserData() instanceof Marker) {
                if(((Marker) object.getUserData()).getName().contains("destination")) {
                    for(SensorRobot cyberfrog: objectManager.getSensorRobots()) {
                        if (cyberfrog.b2body.getFixtureList().contains(cyberfrogSensor, true)) {
                            assets.playSound(assets.frogSound);
                            cyberfrog.completed(objectManager);
                            break;
                        }
                    }
                }
            }
        }
        //one of the colliding fixtures is cyber-frog's upper sensor
        else if(fixA.getUserData().equals("cyberfrogUpperSensor") || fixB.getUserData().equals("cyberfrogUpperSensor")) {
            Fixture cyberfrogSensor = fixA.getUserData().equals("cyberfrogUpperSensor") ? fixA : fixB;
            //Fixture object = cyberfrogSensor == fixA ? fixB : fixA;

            for(SensorRobot cyberfrog: objectManager.getSensorRobots()) {
                if (cyberfrog.b2body.getFixtureList().contains(cyberfrogSensor, true)) {
                    if(cyberfrog.isUpperSensorEnabled())
                        cyberfrog.setUpperSensorDetectsObject(1);
                    break;
                }
            }
        }
        //one of the colliding fixtures is a floating platform
        else if(fixA.getUserData() instanceof FloatingPlatform || fixB.getUserData() instanceof FloatingPlatform) {
            Fixture floatingPlatform = fixA.getUserData() instanceof FloatingPlatform ? fixA : fixB;
            Fixture object = floatingPlatform == fixA ? fixB : fixA;

            if(object.getUserData() instanceof Marker) {
                ((FloatingPlatform)floatingPlatform.getUserData()).b2body.setGravityScale(0);
                ((FloatingPlatform)floatingPlatform.getUserData()).b2body.setLinearVelocity(0,0);
                if(objectManager.getPcs().size()>=2) {
                    objectManager.getPcs().get(0).b2body.setLinearVelocity(0,0);
                    objectManager.getPcs().get(1).b2body.setLinearVelocity(0,0);
                }
            }
        }
        //one of the colliding fixtures is the ground.
        if(fixA.getUserData().equals("ground") || fixB.getUserData().equals("ground")) {
            if(TimeUtils.timeSinceMillis(bumpSoundMillis) > 100) {
                bumpSoundMillis = TimeUtils.millis();
                assets.playSound(assets.bumpSound);
            }
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        //one of the colliding fixtures is the Player.
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
                    hud.hideUseBtn();
                }
                else if (object.getUserData() instanceof Lever) {
                    ((GameObject) object.getUserData()).setUsable(false);
                    ((Lever) object.getUserData()).setColliding(false);
                    Player.colliding = false;
                    hud.hideUseBtn();
                }
            }
        }
        //one of the colliding fixtures is cyber-frog's upper sensor.
        else if(fixA.getUserData().equals("cyberfrogUpperSensor") || fixB.getUserData().equals("cyberfrogUpperSensor")) {
            Fixture cyberfrogSensor = fixA.getUserData().equals("cyberfrogUpperSensor") ? fixA : fixB;
            Fixture object = cyberfrogSensor == fixA ? fixB : fixA;

            if(object.getUserData().equals("ground")) {
                for (SensorRobot cyberfrog : objectManager.getSensorRobots()) {
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
