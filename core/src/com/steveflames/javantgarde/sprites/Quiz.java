package com.steveflames.javantgarde.sprites;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.steveflames.javantgarde.hud.Hud;
import com.steveflames.javantgarde.screens.PlayScreen;
import com.steveflames.javantgarde.tools.global.Cameras;
import com.steveflames.javantgarde.tools.global.Fonts;
import com.steveflames.javantgarde.tools.global.MyFileReader;

import java.util.ArrayList;

/**
 * Created by Flames on 21/11/2017.
 */

public class Quiz extends GameObject {

    private ArrayList<String> questions = new ArrayList<String>();
    private ArrayList<String[]> answers = new ArrayList<String[]>();
    private ArrayList<FloatingPlatform> floatingPlatforms = new ArrayList<FloatingPlatform>();

    private int currentQuestion = 0;
    private int pulled = 0;
    private float timer=0;
    private String id;
    private ArrayList<Door> doors;
    private static Hud hud;

    public Quiz(String name, World world, TiledMap map, Rectangle bounds, String id, ArrayList<Door> doors) {
        super(name, world, map, bounds, true);
        this.id = id;
        this.doors = doors;
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
                if(currentQuestion + 1 <questions.size())
                    floatingPlatforms.get(i).quizReset(answers.get(currentQuestion)[i], hud);
                else { //quiz completed
                    floatingPlatforms.get(i).quizReset(" ", hud);
                    if(id.equals("2_2") || id.equals("1_2")) {
                        doors.get(0).open();
                    }
                }
            }
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

    public void drawFontScaled(SpriteBatch sb) { //draws the quizes levers
        for(int i=0; i<floatingPlatforms.size(); i++)
            floatingPlatforms.get(i).drawLever(sb);
    }

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

    public ArrayList<FloatingPlatform> getFloatingPlatforms() {
        return floatingPlatforms;
    }

    public void addFloatingPlatform(FloatingPlatform floatingPlatform) {
        floatingPlatforms.add(floatingPlatform);
        floatingPlatform.setName(answers.get(0)[floatingPlatforms.size()-1]);
    }

    public static void setHud(Hud hud) {
        Quiz.hud = hud;
    }
}
