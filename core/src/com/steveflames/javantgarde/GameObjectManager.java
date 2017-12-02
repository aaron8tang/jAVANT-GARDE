package com.steveflames.javantgarde;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.steveflames.javantgarde.sprites.GameObject;

import java.util.ArrayList;

/**
 * Created by Flames on 29/11/2017.
 */

public class GameObjectManager {

    private ArrayList<GameObject> gameObjects;

    public GameObjectManager() {
        gameObjects = new ArrayList<GameObject>();
    }

    public void copyCurrentPosition() {
        for (int i = 0; i < gameObjects.size(); i++) {
            if (gameObjects.get(i).b2body != null) {
                if (gameObjects.get(i).b2body.getType() == BodyDef.BodyType.DynamicBody || gameObjects.get(i).b2body.getType() == BodyDef.BodyType.KinematicBody && gameObjects.get(i).b2body.isActive()) {
                    gameObjects.get(i).position_previous.x = gameObjects.get(i).b2body.getPosition().x;
                    gameObjects.get(i).position_previous.y = gameObjects.get(i).b2body.getPosition().y;
                    gameObjects.get(i).angle_previous = gameObjects.get(i).b2body.getAngle();
                }
            }
        }
    }

    public void interpolateCurrentPosition(float alpha) {
        for (int i = 0; i < gameObjects.size(); i++) {
            if (gameObjects.get(i).b2body != null) {
                if (gameObjects.get(i).b2body.getType() == BodyDef.BodyType.DynamicBody || gameObjects.get(i).b2body.getType() == BodyDef.BodyType.KinematicBody && gameObjects.get(i).b2body.isActive()) {
                    //---- interpolate: currentState*alpha + previousState * ( 1.0 - alpha ); ------------------
                    gameObjects.get(i).position.x = gameObjects.get(i).b2body.getPosition().x * alpha + gameObjects.get(i).position_previous.x * (1.0f - alpha);
                    gameObjects.get(i).position.y = gameObjects.get(i).b2body.getPosition().y * alpha + gameObjects.get(i).position_previous.y * (1.0f - alpha);
                    gameObjects.get(i).angle = gameObjects.get(i).b2body.getAngle() * alpha + gameObjects.get(i).angle_previous * (1.0f - alpha);
                }
            }
        }
    }

    public void addGameObject(GameObject gameObject) {
        gameObjects.add(gameObject);
    }

    public void addGameObjectBeforePlayer(GameObject gameObject) { //layout
        gameObjects.add(gameObjects.size()-2, gameObject);
    }

    public ArrayList<GameObject> getGameObjects() {
        return gameObjects;
    }

    public void removeGameObject(GameObject gameObject) {
        gameObjects.remove(gameObject);
    }

    public void clearGameObjects() {
        gameObjects.clear();
    }
}
