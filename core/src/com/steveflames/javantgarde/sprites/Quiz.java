package com.steveflames.javantgarde.sprites;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.steveflames.javantgarde.hud.Hud;
import com.steveflames.javantgarde.tools.Assets;
import com.steveflames.javantgarde.tools.GameObjectManager;
import com.steveflames.javantgarde.tools.global.Cameras;
import com.steveflames.javantgarde.tools.global.Fonts;
import com.steveflames.javantgarde.tools.global.MyFileReader;

import java.util.ArrayList;

/**
 * Implements the quizzes of the game.
 * Includes three floating platforms with one lever on top
 * of each platform.
 */

public class Quiz extends GameObject {

    private ArrayList<String> questions = new ArrayList<String>();
    private ArrayList<String[]> answers = new ArrayList<String[]>();
    private ArrayList<FloatingPlatform> floatingPlatforms = new ArrayList<FloatingPlatform>();

    private int currentQuestion = 0;
    private int pulled = 0;
    private float timer=0;
    private String id;

    private Assets assets;
    private GameObjectManager objectManager;
    private Hud hud;


    public Quiz(String name, World world, TiledMap map, Rectangle bounds, Assets assets, GameObjectManager objectManager, Hud hud) {
        super(name, world, map, bounds, true);
        this.assets = assets;
        this.objectManager = objectManager;
        this.hud = hud;
        this.id = name.split("-")[1];
        parseQuizString(MyFileReader.readFile("txt/quizes/"+name+".txt"));
    }

    private void parseQuizString(String quiz) {
        quiz = quiz.replaceAll("\r", "");
        String[] temp = quiz.split("~QUIZ");
        String[] temp2;
        String[] temp3;

        for (int i = 1; i < temp.length; i++) {
            temp2 = temp[i].split("#ANSWERS");
            questions.add(temp2[0].substring(1)); //skip first \n
            temp2[1] = temp2[1].substring(1); //skip first \n
            temp3 = temp2[1].split("\n");
            answers.add(temp3);
        }
        if(id.equals("1_2"))
            questions.add("[GREEN]Quiz completed![]\n\n\n\n[ORANGE]* In this game, comments are not supported in the\n  code that YOU write.\n\n  You won't be asked to write any complex code![]");
        else
            questions.add("[GREEN]Quiz completed![]");
    }

    public void update(float dt) {
        timer += dt * pulled;
        if(timer > 1.7) {
            pulled = 0;
            timer = 0;
            incrementCurrentQuestion();
            for(int i=0; i<floatingPlatforms.size(); i++){
                if(!isQuizCompleted())
                    floatingPlatforms.get(i).quizReset(answers.get(currentQuestion)[i], hud, false);
                else { //QUIZ COMPLETED
                    floatingPlatforms.get(i).quizReset(" ", hud, true);
                }
            }
            if(isQuizCompleted()) {
                completed();
            }
        }
    }

    private void completed() {
        assets.playSound(assets.questSound);
        if (id.equals("2_2") || id.equals("1_2") || id.equals("6_2"))
            objectManager.getDoors().get(0).open();
        else if (id.equals("4_1"))
            objectManager.getDoors().get(0).open();
        else if (id.equals("5_1"))
            objectManager.getDoors().get(6).open();
        else if (id.equals("7_2")) {
            objectManager.getTeleporter().b2body.setTransform(objectManager.getInfoSigns().get(0).position.x,
                    objectManager.getInfoSigns().get(0).position.y, 0);
            objectManager.getInfoSigns().get(0).b2body.setTransform(0, -5, 0);
        }
    }

    public void drawLine(ShapeRenderer sr) {
        for(int i=0; i<floatingPlatforms.size(); i++)
        floatingPlatforms.get(i).drawLine(sr);
        sr.setColor(Color.CYAN);
        for(int i=0; i<questions.size()-1; i++) {
            sr.rect(bounds.x + Cameras.getHudCameraOffsetX() + 10 + i*(bounds.width/(questions.size()-1)), bounds.y + bounds.height + 5, bounds.width/(questions.size()-1) - 20, 25);
        }
    }

    public void drawFilled(ShapeRenderer sr) {
        for(int i=0; i<floatingPlatforms.size(); i++)
            floatingPlatforms.get(i).drawFilled(sr);
        sr.setColor(Color.GREEN);
        for(int i=0; i<questions.size()-1; i++) {
            if(i<currentQuestion)
                sr.rect(bounds.x + Cameras.getHudCameraOffsetX() + 10 + i*(bounds.width/(questions.size()-1)), bounds.y +bounds.height + 5, bounds.width/(questions.size()-1) - 20, 25);
        }
    }

    public void drawFontInBackground(SpriteBatch sb) {
        Fonts.xsmallMonoMarkup.setColor(Color.WHITE);
        Fonts.xsmallMonoMarkup.draw(sb, questions.get(currentQuestion), bounds.x + Cameras.getHudCameraOffsetX()+20, bounds.y +bounds.height - 20);
    }

    public void drawFontScaled(SpriteBatch sb) {}

    public void drawFont(SpriteBatch sb) {
        for(int i=0; i<floatingPlatforms.size(); i++) {
            floatingPlatforms.get(i).drawFont(sb);
        }
    }

    public void pull(FloatingPlatform floatingPlatform) {
        if(pulled!=1) {
            floatingPlatform.getLever().setUsable(false);
            floatingPlatform.quizPull();
            if (floatingPlatform.isCorrect()) {
                pulled = 1;
                for (FloatingPlatform fp : floatingPlatforms) {
                    fp.getLever().setUsable(false);
                    fp.getLever().setManualPull(false);
                }
            }
        }
    }

    private void incrementCurrentQuestion() {
        if(currentQuestion + 1 < questions.size())
            currentQuestion++;
    }

    private boolean isQuizCompleted() {
        return currentQuestion + 1 >= questions.size();
    }

    public ArrayList<FloatingPlatform> getFloatingPlatforms() {
        return floatingPlatforms;
    }

    public void addFloatingPlatform(FloatingPlatform floatingPlatform) {
        floatingPlatforms.add(floatingPlatform);
        if(answers.size()>0)
            floatingPlatform.setAnswerText(answers.get(0)[floatingPlatforms.size()-1]);
    }

}
