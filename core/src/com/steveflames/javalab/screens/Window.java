package com.steveflames.javalab.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.steveflames.javalab.MyGdxGame;

/**
 * This class is extended from every window in this project.
 * Represents a window including coordinates, camera, labels and more.
 */
public abstract class Window implements Screen, InputProcessor {

    protected float WIDTH;
    protected float HEIGHT;
    protected Rectangle clickCoords;
    protected Vector3 clickVector;

    protected static OrthographicCamera cam; //the static (shared) camera
    protected static Viewport gamePort; //the static (shared) viewport

    protected MyGdxGame game;

    protected Window(MyGdxGame game) {
        this.game = game;
        Gdx.input.setCatchBackKey(true);
        clickCoords = new Rectangle();
        clickVector = new Vector3();

        Gdx.input.setInputProcessor(this);
    }

    /**
     * Updates the window's parameters. Each window has an update method.
     * @param dt -> delta time
     */
    public abstract void update(float dt);

    /**
     * Dispose the unused variables.
     */
    public abstract void dispose();

    public static float getHudCameraOffsetX() {
        return -cam.position.x * MyGdxGame.PPM
                + cam.viewportWidth / 2 * MyGdxGame.PPM;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    public void pause() {}

    public void resume() {}

    public MyGdxGame getGame() {
        return game;
    }
}
