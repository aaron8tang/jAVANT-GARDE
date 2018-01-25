package com.steveflames.javantgarde.tools;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.steveflames.javantgarde.sprites.Checkpoint;
import com.steveflames.javantgarde.sprites.Door;
import com.steveflames.javantgarde.sprites.FloatingPlatform;
import com.steveflames.javantgarde.sprites.GameObject;
import com.steveflames.javantgarde.sprites.InfoSign;
import com.steveflames.javantgarde.sprites.Item;
import com.steveflames.javantgarde.sprites.Lever;
import com.steveflames.javantgarde.sprites.Marker;
import com.steveflames.javantgarde.sprites.Pc;
import com.steveflames.javantgarde.sprites.Player;
import com.steveflames.javantgarde.sprites.Quiz;
import com.steveflames.javantgarde.sprites.SensorRobot;
import com.steveflames.javantgarde.sprites.Teleporter;
import com.steveflames.javantgarde.sprites.ropes.Rope;
import com.steveflames.javantgarde.tools.global.Cameras;

import java.util.ArrayList;

/**
 * This class contains all the dynamic objects of the game
 * as well as appropriate methods to handle them.
 * Also, the interpolation method is implemented here.
 */

public class GameObjectManager {

    private ArrayList<GameObject> gameObjects;
    private ArrayList<GameObject> objectsToRemove = new ArrayList<GameObject>();
    //Box2D world bodies
    private Player player;
    private ArrayList<Pc> pcs = new ArrayList<Pc>();
    private ArrayList<InfoSign> infoSigns = new ArrayList<InfoSign>();
    private ArrayList<Door> doors = new ArrayList<Door>();
    private ArrayList<Rope> ropes = new ArrayList<Rope>();
    private ArrayList<Item> items = new ArrayList<Item>();
    private ArrayList<Checkpoint> checkpoints = new ArrayList<Checkpoint>();
    private ArrayList<FloatingPlatform> floatingPlatforms = new ArrayList<FloatingPlatform>();
    private ArrayList<Lever> levers = new ArrayList<Lever>();
    private ArrayList<Marker> markers = new ArrayList<Marker>();
    private ArrayList<Quiz> quizes = new ArrayList<Quiz>();
    private Teleporter teleporter;
    private ArrayList<SensorRobot> sensorRobots = new ArrayList<SensorRobot>();


    public GameObjectManager() {
        gameObjects = new ArrayList<GameObject>();
    }

    public void copyCurrentPosition() { //part of interpolation
        for (int i = 0; i < gameObjects.size(); i++) {
            if (gameObjects.get(i).b2body != null) {
                if ((gameObjects.get(i).b2body.getType() == BodyDef.BodyType.DynamicBody || gameObjects.get(i).b2body.getType() == BodyDef.BodyType.KinematicBody) && gameObjects.get(i).b2body.isActive()) {
                    gameObjects.get(i).position_previous.x = gameObjects.get(i).b2body.getPosition().x;
                    gameObjects.get(i).position_previous.y = gameObjects.get(i).b2body.getPosition().y;
                    gameObjects.get(i).angle_previous = gameObjects.get(i).b2body.getAngle();
                }
            }
        }
    }

    public void interpolateCurrentPosition(float alpha) { //the actual interpolation
        for (int i = 0; i < gameObjects.size(); i++) {
            if (gameObjects.get(i).b2body != null) {
                if ((gameObjects.get(i).b2body.getType() == BodyDef.BodyType.DynamicBody || gameObjects.get(i).b2body.getType() == BodyDef.BodyType.KinematicBody) && gameObjects.get(i).b2body.isActive()) {
                    //---- interpolate: currentState*alpha + previousState * ( 1.0 - alpha ); ------------------
                    gameObjects.get(i).position.x = gameObjects.get(i).b2body.getPosition().x * alpha + gameObjects.get(i).position_previous.x * (1.0f - alpha);
                    gameObjects.get(i).position.y = gameObjects.get(i).b2body.getPosition().y * alpha + gameObjects.get(i).position_previous.y * (1.0f - alpha);
                    gameObjects.get(i).angle = gameObjects.get(i).b2body.getAngle() * alpha + gameObjects.get(i).angle_previous * (1.0f - alpha);
                }
            }
        }
    }

    public void drawFontScaled(SpriteBatch sb) {
        for(int i=0; i<gameObjects.size(); i++)
            if (Cameras.inLineOfSight(gameObjects.get(i)))
                gameObjects.get(i).drawFontScaled(sb);
    }

    public void drawFilled(ShapeRenderer sr) {
        for(int i=0; i<gameObjects.size(); i++)
            if(Cameras.inLineOfSight(gameObjects.get(i)))
                gameObjects.get(i).drawFilled(sr);
    }

    public void drawLine(ShapeRenderer sr) {
        for(int i=0; i<gameObjects.size(); i++)
            if(Cameras.inLineOfSight(gameObjects.get(i)))
                gameObjects.get(i).drawLine(sr);
    }

    public void drawFont(SpriteBatch sb) {
        for(int i=0; i<gameObjects.size(); i++)
            if(Cameras.inLineOfSight(gameObjects.get(i)))
                gameObjects.get(i).drawFont(sb);
    }

    public void addGameObject(GameObject gameObject) {
        gameObjects.add(gameObject);
    }

    public void addPlayer(Player player) {
        gameObjects.add(player);
        this.player = player;
    }

    public void addGameObjectBeforePlayer(GameObject gameObject) { //layout
        gameObjects.add(gameObjects.size()-2, gameObject);
    }

    public void destroyUnusedBodies() {
		if(objectsToRemove.size()>0) {
			for(int i = 0; i< objectsToRemove.size(); i++) {
				gameObjects.remove(objectsToRemove.get(i));
				//playScreen.getWorld().destroyBody(objectsToRemove.get(i).b2body); //todo DESTROY BODY BUG
			}
			objectsToRemove.clear();
		}
    }

    public void clearGameObjects() {
        gameObjects.clear();
    }


    public ArrayList<GameObject> getGameObjects() {
        return gameObjects;
    }

    public Player getPlayer() {
        return player;
    }

    public ArrayList<Pc> getPcs() {
        return pcs;
    }

    public ArrayList<InfoSign> getInfoSigns() {
        return infoSigns;
    }

    public ArrayList<Door> getDoors() {
        return doors;
    }

    public ArrayList<Rope> getRopes() {
        return ropes;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public ArrayList<Checkpoint> getCheckpoints() {
        return checkpoints;
    }

    public ArrayList<FloatingPlatform> getFloatingPlatforms() {
        return floatingPlatforms;
    }

    public ArrayList<Lever> getLevers() {
        return levers;
    }

    public ArrayList<Marker> getMarkers() {
        return markers;
    }

    public ArrayList<Quiz> getQuizes() {
        return quizes;
    }

    public Teleporter getTeleporter() {
        return teleporter;
    }

    public void setTeleporter(Teleporter teleporter) {
        this.teleporter = teleporter;
    }

    public ArrayList<GameObject> getObjectsToRemove() {
        return objectsToRemove;
    }

    public ArrayList<SensorRobot> getSensorRobots() {
        return sensorRobots;
    }
}
