package com.steveflames.javalab.sprites.ropes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.steveflames.javalab.MyGdxGame;
import com.steveflames.javalab.Window;
import com.steveflames.javalab.scenes.Hud;
import com.steveflames.javalab.tools.Fonts;

/**
 * Created by Flames on 10/10/2017.
 */

public class Platform extends Sprite {

    private static final float WIDTH = 1f;
    private static final float HEIGHT = 0.25f;
    Body b2body;
    private Fixture fixture;

    private String text;
    private GlyphLayout glyphLayout = new GlyphLayout();
    private boolean flag;

    private boolean active = true;
    private float alpha = 1f;

    private static int platformCounter = -1;


    Platform(String text, String flag, World world, Rectangle ropeBounds) {
        this.text = text;
        this.flag = !flag.equals("0");
        platformCounter++;
        float startY = ropeBounds.getY() + 2.5f*platformCounter;

        glyphLayout.setText(Fonts.small, text);
        definePlatform(world, ropeBounds, startY);
    }

    private void definePlatform(World world, Rectangle ropeBounds, float startY) {
        BodyDef bdef = new BodyDef();
        bdef.position.set((ropeBounds.getX() + ropeBounds.width/2)/ MyGdxGame.PPM, startY);
        bdef.type = BodyDef.BodyType.KinematicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(WIDTH/2, HEIGHT/2);


        fdef.shape = shape;
        fixture = b2body.createFixture(fdef);
        fixture.setUserData(this);
        b2body.setLinearVelocity(0, -1f);
    }

    void update(float dt) {
        if(b2body.getPosition().y <= -2.5f) {
            b2body.setTransform(b2body.getPosition().x, b2body.getPosition().y + 7.5f, 0);
        }
        if(alpha < 1 && alpha >= 0) {
            alpha -= 2 * dt;
        }
    }

    void draw(SpriteBatch sb, ShapeRenderer sr) {
        sb.setProjectionMatrix(Hud.viewport.getCamera().combined);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(new Color(0.21f, 0.18f, 0.17f, alpha));
        sr.rect(b2body.getPosition().x - WIDTH/2, b2body.getPosition().y - HEIGHT/2, WIDTH, HEIGHT);
        sr.end();

        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(new Color(0, 0, 0, alpha));
        sr.rect(b2body.getPosition().x - WIDTH/2, b2body.getPosition().y - HEIGHT/2, WIDTH, HEIGHT);
        sr.end();

        sb.begin();
        Fonts.small.setColor(1, 1, 1, alpha);
        Fonts.small.draw(sb, text, b2body.getPosition().x * MyGdxGame.PPM + Window.getHudCameraOffsetX() - glyphLayout.width/2, b2body.getPosition().y * MyGdxGame.PPM + glyphLayout.height/2);
        sb.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);
    }


    void fade() {
        fixture.setSensor(true);
        alpha=0.99f;
    }

    boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getText() {
        return text;
    }

    boolean isFlag() {
        return flag;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    Fixture getFixture() {
        return fixture;
    }
}
