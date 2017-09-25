package com.steveflames.javalab.scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.steveflames.javalab.MyGdxGame;
import com.steveflames.javalab.Tools.Fonts;
import com.steveflames.javalab.Tools.Toast;
import com.steveflames.javalab.Window;
import com.steveflames.javalab.levels.PlayScreen;
import com.steveflames.javalab.sprites.Pc;
import com.steveflames.javalab.sprites.Player;


/**
 * Created by Flames on 9/4/16.
 */
public class Hud implements Disposable {
    public Stage stage;
    private Viewport viewport;

    private PlayScreen level;
    private Label healthLabel;

    private Toast currentToast = null;

    public Hud(PlayScreen level, SpriteBatch sb) {
        this.level = level;

        viewport = new StretchViewport(MyGdxGame.WIDTH, MyGdxGame.HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        Table table = new Table();
        table.top();
        table.left();
        table.setFillParent(true);

        healthLabel = new Label("HEALTH" , new Label.LabelStyle(Fonts.small, Color.WHITE));

        table.add(healthLabel).padLeft(15).padTop(5);

        stage.addActor(table);

    }

    public void update(float dt) {
        if(currentToast != null) {
            currentToast.update(dt);
            if(currentToast.getCurrentState() == Toast.State.LEFT)
                currentToast = null;
        }
    }

    public void render(SpriteBatch sb, ShapeRenderer sr) {
        sb.setProjectionMatrix(stage.getCamera().combined);
        sr.setProjectionMatrix(stage.getCamera().combined);

        sr.begin(ShapeRenderer.ShapeType.Filled);
        if(level.getPlayer().getHealth() > 0) {
            for(int i=0; i<level.getPlayer().getHealth(); i++) {
                sr.setColor(Color.BLACK);
                sr.rect(180 +(70*i), stage.getCamera().viewportHeight - 22 - 32, 64, 34);
                sr.setColor(Color.RED);
                sr.rect(182 +(70*i), stage.getCamera().viewportHeight - 22 - 30, 60, 30);
            }
        }
        sr.end();

        if(currentToast != null)
            currentToast.render(sb, sr);

        for(Pc pc: level.getPcs()) { //draw the 'use PC' prompt
            if(pc.isUsable()) {
                if(level.getPlayer().currentState != Player.State.CODING) {
                    sb.setProjectionMatrix(sr.getProjectionMatrix());
                    sb.begin();
                    Fonts.medium.setColor(Color.RED);
                    Fonts.medium.draw(sb, "!", pc.getBounds().x + pc.getBounds().width / 2 - 10 - Window.getCam().position.x * MyGdxGame.PPM + Window.getCam().viewportWidth / 2 * MyGdxGame.PPM, pc.getBounds().y + pc.getBounds().height + 50);
                    sb.end();
                    break;
                }
            }
        }
    }

    public void newToast(String text) {
        currentToast = new Toast(text, stage.getCamera());
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public Toast getCurrentToast() {
        return currentToast;
    }
}
