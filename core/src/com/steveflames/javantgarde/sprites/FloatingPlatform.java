package com.steveflames.javantgarde.sprites;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.steveflames.javantgarde.MyGdxGame;
import com.steveflames.javantgarde.hud.Hud;
import com.steveflames.javantgarde.tools.Assets;
import com.steveflames.javantgarde.tools.global.Cameras;
import com.steveflames.javantgarde.tools.global.Fonts;


/**
 * Implements any dynamic floating platform of the game.
 * (e.g. quiz floating platforms with levers,
 *  floating platforms on quiz-pc,
 *  elevators..)
 */

public class FloatingPlatform extends GameObject {

    private String name ="";
    private GlyphLayout glyphLayout = new GlyphLayout();
    private Lever lever;
    private boolean correct = false;

    private Assets assets;

    public FloatingPlatform(String name, World world, TiledMap map, Rectangle bounds, Lever lever, Assets assets) {
        super(name, world, map, bounds, false);
        this.name = name;
        glyphLayout.setText(Fonts.small, this.name);
        this.lever = lever;
        this.assets = assets;
    }

    public void update(float dt) {}

    public void drawFilled(ShapeRenderer sr) {
        sr.setColor(Color.BLACK);
        sr.rect(b2body.getPosition().x* MyGdxGame.PPM + Cameras.getHudCameraOffsetX() - bounds.width/2, b2body.getPosition().y* MyGdxGame.PPM - bounds.height/2, bounds.width, bounds.height);
    }

    public void drawLine(ShapeRenderer sr) {
        sr.setColor(0.14f, 0.87f, 0.88f, 1);
        sr.rect(b2body.getPosition().x* MyGdxGame.PPM + Cameras.getHudCameraOffsetX() - bounds.width/2, b2body.getPosition().y* MyGdxGame.PPM - bounds.height/2, bounds.width, bounds.height);
    }

    public void drawFont(SpriteBatch sb) {
        Fonts.small.setColor(Color.WHITE);
        Fonts.small.draw(sb, name, b2body.getPosition().x* MyGdxGame.PPM - glyphLayout.width/2 + Cameras.getHudCameraOffsetX(), b2body.getPosition().y* MyGdxGame.PPM + glyphLayout.height/2);
        if(lever != null)
            lever.drawUsePrompt(sb);
    }

    public void drawFontScaled(SpriteBatch sb) {
        if(lever!=null)
            lever.drawFontScaled(sb);
    }

    /**
     * Reset the state of the FloatingPlatform and its lever.
     * @param name The text of the FloatingPlatform (quiz choice).
     * @param isCompleted Whether the whole quiz is completed or not.
     */
    void quizReset(String name, Hud hud, boolean isCompleted) {
        correct = false;
        resetB2Body();
        if(!name.equals("$")) { //not empty answer
            lever.quizReset();
            if (lever.isColliding() && !isCompleted) {
                lever.setUsable(true);
                hud.showUseBtn("PULL");
            }

            setAnswerText(name);
            if (name.equals(" ")) {
                lever.setUsable(false);
                lever.setManualPull(false);
            }
        }
        else { //deactivate this floatingPlatform's lever and name
            setAnswerText(" ");
            lever.b2body.setTransform(-400/MyGdxGame.PPM,-400/MyGdxGame.PPM, 0);
        }
    }

    /**
     * Reset the Box2D body to its initial state.
     */
    public void resetB2Body() {
        b2body.setType(BodyDef.BodyType.KinematicBody);
        b2body.setLinearVelocity(0,0);
        b2body.setTransform((bounds.getX() + bounds.getWidth() / 2) / MyGdxGame.PPM, (bounds.getY() + bounds.getHeight() / 2) / MyGdxGame.PPM, 0);
    }

    /**
     * Pull the lever of the current FloatingPlatform (part of the quiz).
     */
    void quizPull() {
        lever.pull();
        if(correct) {
            assets.playSound(assets.correctSound);
        }
        else {
            assets.playSound(assets.wrongSound);
            b2body.setLinearVelocity(0, -1200/MyGdxGame.PPM);
            lever.b2body.setLinearVelocity(0, -1200/MyGdxGame.PPM);
        }
    }

    /**
     * Applies velocity to the FloatingPlatform.
     * @param speed The velocity to apply to the FloatingPlatform.
     */
    public void drop(float speed) {
        assets.playSound(assets.riseSound);
        b2body.setType(BodyDef.BodyType.DynamicBody);
        b2body.setLinearVelocity(0, -speed);
    }

    public void setAnswerText(String name) {
        if(name.charAt(0) == '!')
            correct = true;
        this.name = name.substring(1);
        glyphLayout.setText(Fonts.small, this.name);
    }

    public void setTransform(float x, float y, float angle) {
        b2body.setTransform(x, y, angle);
        bounds.setX(x* MyGdxGame.PPM);
        bounds.setY(y* MyGdxGame.PPM);
    }

    @Override
    public String getName() {
        return name;
    }

    public Lever getLever() {
        return lever;
    }

    public boolean isCorrect() {
        return correct;
    }
}
