package com.steveflames.javantgarde.sprites;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.steveflames.javantgarde.MyGdxGame;
import com.steveflames.javantgarde.screens.PlayScreen;
import com.steveflames.javantgarde.tools.global.Cameras;
import com.steveflames.javantgarde.tools.global.Fonts;
import com.steveflames.javantgarde.tools.global.Loader;

/**
 * Created by Flames on 19/11/2017.
 */

public class Lever extends GameObject {

    private boolean open = false;
    private Texture currentT = Loader.leverClosedT;
    private boolean colliding = false;

    private boolean manualPull;

    public Lever(String name, World world, TiledMap map, Rectangle bounds, float alpha, boolean manualPull) {
        super(name, world, map, bounds, true);
        this.alpha = alpha;
        this.manualPull = manualPull;
    }

    public void drawUsePrompt(SpriteBatch sb) {
        if(usable) {
            Fonts.medium.setColor(Color.RED);
            sb.draw(Loader.handT, b2body.getPosition().x*MyGdxGame.PPM - 25 + Cameras.getHudCameraOffsetX(), b2body.getPosition().y*MyGdxGame.PPM + bounds.height/2 + 40, 50, 50);
        }
    }

    public void update(float dt) {
        updateAlpha(dt);
    }

    public void drawFontScaled(SpriteBatch sb) {
        sb.setColor(1,1,1, alpha);
        sb.draw(currentT, b2body.getPosition().x - bounds.width/2/MyGdxGame.PPM, b2body.getPosition().y - bounds.height/2/MyGdxGame.PPM, bounds.width/MyGdxGame.PPM, bounds.height/MyGdxGame.PPM);
    }

    public void drawFont(SpriteBatch sb) {}
    public void drawLine(ShapeRenderer sr) {}
    public void drawFilled(ShapeRenderer sr) {}

    public void pull() {
        open = !open;
        currentT = open ? Loader.leverOpenT : Loader.leverClosedT;
    }

    void quizReset() {
        open = false;
        manualPull = true;
        currentT = Loader.leverClosedT;
        b2body.setLinearVelocity(0,0);
        b2body.setTransform((bounds.getX() + bounds.getWidth()/2)/ MyGdxGame.PPM, (bounds.getY() + bounds.getHeight()/2)/ MyGdxGame.PPM, 0);
    }

    public void setUsable(boolean usable) {
        if(manualPull)
            this.usable = usable;
    }

    public void setManualPull(boolean manualPull) {
        this.manualPull = manualPull;
    }

    public boolean isManualPull() {
        return manualPull;
    }

    public boolean isColliding() {
        return colliding;
    }

    public void setColliding(boolean colliding) {
        this.colliding = colliding;
    }
}
