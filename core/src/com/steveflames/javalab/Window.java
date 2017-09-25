package com.steveflames.javalab;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.steveflames.javalab.Tools.Fonts;

import java.util.ArrayList;

/**
 * This class is extended from every window in this project.
 * Represents a window including coordinates, camera, labels and more.
 */
public abstract class Window implements Screen, InputProcessor {

    protected float WIDTH;
    protected float HEIGHT;
    protected static final int BTNWIDTH = 300;
    protected static final int BTNHEIGHT = 120;
    protected Rectangle clickCoords;
    protected Vector3 clickVector;

    protected ArrayList<String> strings;    //the on-screen strings
    protected ArrayList<GlyphLayout> glyphLayouts; //the string's glyph layouts
    protected static OrthographicCamera cam; //the static (shared) camera
    protected static Viewport gamePort; //the static (shared) viewport

    protected MyGdxGame game;

    protected Window(MyGdxGame game) {
        this.game = game;
        Gdx.input.setCatchBackKey(true);
        clickCoords = new Rectangle();
        clickVector = new Vector3();
        glyphLayouts = new ArrayList<GlyphLayout>();
        strings = new ArrayList<String>();

        Fonts.medium.setColor(Color.WHITE);
        Fonts.small.setColor(Color.WHITE);
        Fonts.big.setColor(Color.WHITE);

        Gdx.input.setInputProcessor(this);
    }

    public static OrthographicCamera getCam() {
        return cam;
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

    /**
     * Add a string to the window. For the string to be shown, it has
     * to be rendered.
     * @param s -> the string
     * @param size -> the size of the string (1, 2 or 3)
     */
    public void addString(String s, int size) {
        strings.add(s);
        glyphLayouts.add(new GlyphLayout());
        if(size==1)
            glyphLayouts.get(glyphLayouts.size()-1).setText(Fonts.small, s);
        if(size==2)
            glyphLayouts.get(glyphLayouts.size()-1).setText(Fonts.medium, s);
        if(size==3)
            glyphLayouts.get(glyphLayouts.size()-1).setText(Fonts.big, s);
    }

    /**
     * Changes a string of the window.
     * @param index -> the index of the string
     * @param s -> the new string
     * @param size -> the size of the string (1, 2 or 3)
     */
    public void changeString(int index, String s, int size) {
        strings.set(index, s);
        if(size==1)
            glyphLayouts.get(index).setText(Fonts.small, strings.get(index));
        else if(size==2)
            glyphLayouts.get(index).setText(Fonts.medium, strings.get(index));
        else if(size==3)
            glyphLayouts.get(index).setText(Fonts.big, strings.get(index));
    }

    public void pause()
    {

    }

    public void resume()
    {

    }
}
