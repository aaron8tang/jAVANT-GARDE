package com.steveflames.javalab.sprites;

import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.steveflames.javalab.MyGdxGame;
import com.steveflames.javalab.scenes.Hud;
import com.steveflames.javalab.screens.PlayScreen;
import com.steveflames.javalab.tools.global.Fonts;


/**
 * Created by Flames on 30/10/2017.
 */

public class FloatingPlatform extends GameObject {

    private String name ="";
    private GlyphLayout glyphLayout = new GlyphLayout();
    private Lever lever;
    private boolean correct = false;


    public FloatingPlatform(String name, World world, TiledMap map, Rectangle bounds, Lever lever) {
        super(name, world, map, bounds, false);
        if(name.contains("_")) {
            String[] splitter = name.split("_");
            this.name = splitter[1];
        }
        glyphLayout.setText(Fonts.small, this.name);
        this.lever = lever;
    }

    public void drawRect(ShapeRenderer sr) {
        sr.rect(b2body.getPosition().x*MyGdxGame.PPM + PlayScreen.getHudCameraOffsetX() - bounds.width/2, b2body.getPosition().y*MyGdxGame.PPM - bounds.height/2, bounds.width, bounds.height);
    }

    public void drawFont(SpriteBatch sb) {
        Fonts.small.draw(sb, name, b2body.getPosition().x*MyGdxGame.PPM - glyphLayout.width/2 + PlayScreen.getHudCameraOffsetX(), b2body.getPosition().y*MyGdxGame.PPM + glyphLayout.height/2);
        if(lever != null)
            lever.drawUsePrompt(sb);
    }

    public void drawLever(SpriteBatch sb, float dt) {
        lever.draw(sb, dt);
    }

    public void quizReset(String name, Hud hud) {
        b2body.setLinearVelocity(0,0);
        correct = false;
        lever.quizReset();
        if(lever.isColliding()) {
            lever.setUsable(true);
            hud.showUseBtn("PULL");
        }
        b2body.setTransform((bounds.getX() + bounds.getWidth()/2)/ MyGdxGame.PPM, (bounds.getY() + bounds.getHeight()/2)/ MyGdxGame.PPM, 0);
        setName(name);
        if(name.equals(" ")) {
            lever.setUsable(false);
            lever.setManualPull(false);
        }
    }

    public void quizPull() {
        lever.pull();
        if(!correct) {
            b2body.setLinearVelocity(0, -3);
            lever.b2body.setLinearVelocity(0, -3);
        }
    }

    public void setName(String name) {
        if(name.charAt(0) == '!')
            correct = true;
        this.name = name.substring(1);
        glyphLayout.setText(Fonts.small, this.name);
    }

    public void setTransform(float x, float y, float angle) {
        b2body.setTransform(x, y, angle);
        bounds.setX(x*MyGdxGame.PPM);
        bounds.setY(y*MyGdxGame.PPM);
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
