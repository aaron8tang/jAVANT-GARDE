package com.steveflames.javalab.sprites.ropes;

import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.steveflames.javalab.MyGdxGame;
import com.steveflames.javalab.screens.PlayScreen;
import com.steveflames.javalab.sprites.GameObject;
import com.steveflames.javalab.tools.global.Fonts;

/**
 * Created by Flames on 10/10/2017.
 */

public class Platform extends GameObject {

    private static final float SPEED = 0.8f;
    private String text;
    private GlyphLayout glyphLayout = new GlyphLayout();
    private boolean flag;

    private boolean active = true;
    private float alpha = 1f;


    Platform(String text, World world, TiledMap map, Rectangle bounds) {
        super(text, world, map, bounds, false);
        this.text = text.substring(1);
        this.flag = text.charAt(0)=='!';

        glyphLayout.setText(Fonts.small, this.text);
        b2body.setLinearVelocity(0, -SPEED);
    }

    public void update(float dt) {
        if(b2body.getPosition().y <= -bounds.getHeight()/2/MyGdxGame.PPM)
            b2body.setTransform(b2body.getPosition().x, b2body.getPosition().y + 7.5f, 0);
        if(alpha < 1 && alpha >= 0)
            alpha -= 2 * dt;
    }

    public void drawFilled(ShapeRenderer sr) {
        sr.setColor(0.21f, 0.18f, 0.17f, alpha);
        sr.rect(position.x*MyGdxGame.PPM + PlayScreen.getHudCameraOffsetX() - bounds.width/2 , position.y*MyGdxGame.PPM - bounds.height/2, bounds.width, bounds.height);
    }

    public void drawLine(ShapeRenderer sr) {
        sr.setColor(0, 0, 0, alpha);
        sr.rect(position.x*MyGdxGame.PPM + PlayScreen.getHudCameraOffsetX() - bounds.width/2 , position.y*MyGdxGame.PPM - bounds.height/2, bounds.width, bounds.height);
    }

    public void drawFont(SpriteBatch sb) {
        Fonts.small.setColor(1, 1, 1, alpha);
        Fonts.small.draw(sb, text, position.x * MyGdxGame.PPM + PlayScreen.getHudCameraOffsetX() - glyphLayout.width/2, position.y * MyGdxGame.PPM + glyphLayout.height/2);
    }

    void fade() {
        fixture.setSensor(true);
        alpha=0.99f;
    }

    void reset() {
        alpha = 1;
        fixture.setSensor(false);
        b2body.setLinearVelocity(0, -SPEED);
        active = true;
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
}
