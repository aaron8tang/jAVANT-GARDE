package com.steveflames.javantgarde.tools.global;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.steveflames.javantgarde.MyGdxGame;
import com.steveflames.javantgarde.sprites.Player;

/**
 * Created by Flames on 2/12/2017.
 */

public class Cameras {

    public static OrthographicCamera playScreenCam;
    public static Viewport playScreenPort;
    private static float mapWidth;
    private static float mapHeight;

    public static Viewport hudPort;


    public static void load(float mapWidth, float mapHeight) {
        Cameras.mapWidth = mapWidth;
        Cameras.mapHeight = mapHeight;
        playScreenCam = new OrthographicCamera();
        Cameras.playScreenCam.setToOrtho(false, MyGdxGame.WIDTH / MyGdxGame.PPM, MyGdxGame.HEIGHT / MyGdxGame.PPM);
        playScreenPort = new FitViewport(MyGdxGame.WIDTH / MyGdxGame.PPM, MyGdxGame.HEIGHT / MyGdxGame.PPM); //TODO: h mhpws fit
        hudPort = new FitViewport(MyGdxGame.WIDTH, MyGdxGame.HEIGHT, new OrthographicCamera());
    }

    public static void updateCameraPosition(Player player) {
        if(!player.isOutOfBounds()) {
            if (player.position.x + player.b2body.getLinearVelocity().x/ com.steveflames.javantgarde.MyGdxGame.PPM < playScreenCam.viewportWidth / 2)
                playScreenCam.position.x = playScreenCam.viewportWidth / 2;
            else if(player.position.x + player.b2body.getLinearVelocity().x/ com.steveflames.javantgarde.MyGdxGame.PPM > mapWidth - playScreenCam.viewportWidth / 2)
                playScreenCam.position.x = mapWidth - playScreenCam.viewportWidth / 2;
            else
                playScreenCam.position.x = player.position.x;
        }
    }

    public static boolean inLineOfSight(com.steveflames.javantgarde.sprites.GameObject gameObject) {
        return (inLineOfSightX(gameObject) && inLineOfSightY(gameObject));
    }

    private static boolean inLineOfSightX(com.steveflames.javantgarde.sprites.GameObject gameObject) {
        float extra = 0;
        if(gameObject instanceof com.steveflames.javantgarde.sprites.ropes.Rope)
            extra = 1;
        return Math.abs(playScreenCam.position.x - gameObject.b2body.getPosition().x) < (playScreenCam.viewportWidth/2 + gameObject.getBounds().getWidth()/2/ com.steveflames.javantgarde.MyGdxGame.PPM + extra);
    }

    private static boolean inLineOfSightY(com.steveflames.javantgarde.sprites.GameObject gameObject) {
        return Math.abs(playScreenCam.position.y - gameObject.b2body.getPosition().y) < (playScreenCam.viewportHeight/2 + gameObject.getBounds().getHeight()/2/ com.steveflames.javantgarde.MyGdxGame.PPM);
    }

    public static float getHudCameraOffsetX() {
        return -playScreenCam.position.x * com.steveflames.javantgarde.MyGdxGame.PPM + playScreenCam.viewportWidth / 2 * com.steveflames.javantgarde.MyGdxGame.PPM;
    }

    public static void setCameraTo(float x) {
        if(x < playScreenCam.viewportWidth/2)
            x = playScreenCam.viewportWidth/2;
        else if(x > mapWidth - playScreenCam.viewportWidth/2)
            x = mapWidth - playScreenCam.viewportWidth/2;
        playScreenCam.position.x = x;
    }
}
