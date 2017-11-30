package com.steveflames.javalab;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.steveflames.javalab.sprites.GameObject;

import java.util.ArrayList;

/**
 * Created by Flames on 29/11/2017.
 */

public class GameObjectManager {

    private ArrayList<GameObject> dynamicGameObjectList;
    private GameObject dynamicGameObject;


    public GameObjectManager() {
        dynamicGameObjectList = new ArrayList<GameObject>();
    }

    public void copyCurrentPosition() {
        for (int i = 0; i < dynamicGameObjectList.size(); i++) {
            dynamicGameObject = dynamicGameObjectList.get(i);
            if (dynamicGameObject.b2body != null) {
                if (dynamicGameObject.b2body.getType() == BodyDef.BodyType.DynamicBody || dynamicGameObject.b2body.getType() == BodyDef.BodyType.KinematicBody && dynamicGameObject.b2body.isActive()) {
                    dynamicGameObject.position_previous.x = dynamicGameObject.b2body.getPosition().x;
                    dynamicGameObject.position_previous.y = dynamicGameObject.b2body.getPosition().y;
                    dynamicGameObject.angle_previous = dynamicGameObject.b2body.getAngle();
                }
            }
        }
    }

    public void interpolateCurrentPosition(float alpha) {
        for (int i = 0; i < dynamicGameObjectList.size(); i++) {
            dynamicGameObject = dynamicGameObjectList.get(i);
            if (dynamicGameObject.b2body != null) {
                if (dynamicGameObject.b2body.getType() == BodyDef.BodyType.DynamicBody || dynamicGameObject.b2body.getType() == BodyDef.BodyType.KinematicBody && dynamicGameObject.b2body.isActive()) {
                    //---- interpolate: currentState*alpha + previousState * ( 1.0 - alpha ); ------------------
                    dynamicGameObject.position.x = dynamicGameObject.b2body.getPosition().x * alpha + dynamicGameObject.position_previous.x * (1.0f - alpha);
                    dynamicGameObject.position.y = dynamicGameObject.b2body.getPosition().y * alpha + dynamicGameObject.position_previous.y * (1.0f - alpha);
                    dynamicGameObject.angle = dynamicGameObject.b2body.getAngle() * alpha + dynamicGameObject.angle_previous * (1.0f - alpha);
                }
            }
        }
    }

    public ArrayList<GameObject> getDynamicGameObjectList() {
        return dynamicGameObjectList;
    }

    public void addDynamicGameObject(GameObject dynamicGameObject) {
        dynamicGameObjectList.add(dynamicGameObject);
    }

    public void removeDynamicGameObject(GameObject dynamicGameObject) {
        dynamicGameObjectList.remove(dynamicGameObject);
    }

    public void clearList() {
        dynamicGameObjectList.clear();
    }
}
