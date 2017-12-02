package com.steveflames.javantgarde.quests;

import com.badlogic.gdx.math.Rectangle;
import com.steveflames.javantgarde.screens.PlayScreen;
import com.steveflames.javantgarde.sprites.FloatingPlatform;
import com.steveflames.javantgarde.sprites.InfoSign;
import com.steveflames.javantgarde.tools.compiler.MyVariable;

import java.util.ArrayList;

/**
 * Created by Flames on 6/10/2017.
 */

public class Quest {

    private int questN; //which quest of this level. 1st? 2nd?
    private ArrayList<QuestStep> questSteps = new ArrayList<QuestStep>();
    private int progress;
    private String completedText;

    public Quest(String id) {
        questN = Integer.parseInt(id.substring(id.length()-1));
        parseQuestString(com.steveflames.javantgarde.tools.global.MyFileReader.readFile("txt/quests/quest-" + id + ".txt"));
        progress = 0;
        if(com.steveflames.javantgarde.tools.global.MyFileReader.exists("txt/quests/quest-"+id+"-completed.txt"))
            completedText = com.steveflames.javantgarde.tools.global.MyFileReader.readFile("txt/quests/quest-"+id+"-completed.txt");
    }

    private void parseQuestString(String quest) {
        quest = quest.replaceAll("\r", "");
        questSteps.clear();
        String[] temp = quest.split("~QUEST");
        String[] temp2;

        for (int i = 1; i < temp.length; i++) {
            temp2 = temp[i].split("#HELP");
            questSteps.add(new QuestStep(temp2[0].substring(1, temp2[0].length())));
            if (temp2.length > 1) {
                for (int j = 1; j < temp2.length; j++) {
                    questSteps.get(questSteps.size() - 1).addHint(temp2[j].substring(1, temp2[j].length()));
                }
            }
        }
    }

    public boolean validateCodeForQuest(PlayScreen playScreen, com.steveflames.javantgarde.tools.compiler.MyClass myClass, int questN) {

        if(playScreen.getCurrentLevel().getId().equals("1_1")) {
            switch (progress) {
                case 0:
                    if (playScreen.getHud().getEditorWindow().getConsoleWindow().getConsoleTextArea().getText().toString().equals("[RED]Error: main method not found[]\n"))
                        return true;
                    break;
                case 1:
                        return true;
                case 2:
                    if(myClass.getCode().contains("System.out.println(\"Hello World!\");"))
                        return true;
                    break;
            }
        }
        else if(playScreen.getCurrentLevel().getId().equals("2_1")) {
            switch (progress) {
                case 0:
                    for(MyVariable variable : myClass.getFields()) {
                        if(variable.getType().equals("int"))
                            return true;
                    }
                    break;

                case 1:
                    for(MyVariable variable : myClass.getFields()) {
                        if(variable.getType().equals("double"))
                            return true;
                    }
                    break;
                case 2:
                    for(MyVariable variable : myClass.getFields()) {
                        if(variable.getType().equals("boolean"))
                            return true;
                    }
                    break;
                case 3:
                    for(MyVariable variable : myClass.getFields()) {
                        if(variable.getType().equals("char"))
                            return true;
                    }
                    break;
                case 4:
                    for(MyVariable variable : myClass.getFields()) {
                        if(variable.getType().equals("String"))
                            return true;
                    }
                    break;
            }
        }
        else if(playScreen.getCurrentLevel().getId().equals("2_2")) {

            for(MyVariable myVariable: myClass.getFields()) {
                if(myVariable.getType().equals("int")) {
                    for (FloatingPlatform floatingPlatform : playScreen.getFloatingPlatforms()) {
                        if (floatingPlatform.getName().equals(myVariable.getName())) {
                            if(!myVariable.getValue().equals("null")) {
                                floatingPlatform.setTransform(playScreen.getMarkers().get(0).x / com.steveflames.javantgarde.MyGdxGame.PPM + playScreen.getMarkers().get(0).width / 2 / com.steveflames.javantgarde.MyGdxGame.PPM,
                                        playScreen.getMarkers().get(0).y / com.steveflames.javantgarde.MyGdxGame.PPM - floatingPlatform.getBounds().height / 2 / com.steveflames.javantgarde.MyGdxGame.PPM + playScreen.getMarkers().get(0).height / 2 / com.steveflames.javantgarde.MyGdxGame.PPM
                                                + 0.64f * Integer.parseInt(myVariable.getValue()), 0);
                                break;
                            }
                        }
                    }
                }
                else if(myVariable.getType().equals("double")) {
                    for(FloatingPlatform floatingPlatform: playScreen.getFloatingPlatforms()) {
                        if(floatingPlatform.getName().equals(myVariable.getName())) {
                            if(!myVariable.getValue().equals("null")) {
                                floatingPlatform.setTransform(playScreen.getMarkers().get(1).x / com.steveflames.javantgarde.MyGdxGame.PPM + playScreen.getMarkers().get(1).width / 2 / com.steveflames.javantgarde.MyGdxGame.PPM,
                                        playScreen.getMarkers().get(1).y / com.steveflames.javantgarde.MyGdxGame.PPM - floatingPlatform.getBounds().height / 2 / com.steveflames.javantgarde.MyGdxGame.PPM + playScreen.getMarkers().get(1).height / 2 / com.steveflames.javantgarde.MyGdxGame.PPM
                                                + 0.64f * (float) Double.parseDouble(myVariable.getValue()), 0);
                                break;
                            }
                        }
                    }
                }
                else if(myVariable.getType().equals("boolean")) {
                    for(FloatingPlatform floatingPlatform: playScreen.getFloatingPlatforms()) {
                        if(floatingPlatform.getName().equals(myVariable.getName())) {
                            if(myVariable.getValue().equals("true")) {
                                floatingPlatform.setTransform(playScreen.getMarkers().get(2).x / com.steveflames.javantgarde.MyGdxGame.PPM + playScreen.getMarkers().get(2).width / 2 / com.steveflames.javantgarde.MyGdxGame.PPM,
                                        playScreen.getMarkers().get(2).y / com.steveflames.javantgarde.MyGdxGame.PPM - floatingPlatform.getBounds().height / 2 / com.steveflames.javantgarde.MyGdxGame.PPM + playScreen.getMarkers().get(2).height / 2 / com.steveflames.javantgarde.MyGdxGame.PPM
                                                + 0.64f*1.5f, 0);
                            }
                            else {
                                floatingPlatform.setTransform(playScreen.getMarkers().get(2).x / com.steveflames.javantgarde.MyGdxGame.PPM + playScreen.getMarkers().get(2).width / 2 / com.steveflames.javantgarde.MyGdxGame.PPM,
                                        playScreen.getMarkers().get(2).y / com.steveflames.javantgarde.MyGdxGame.PPM - floatingPlatform.getBounds().height / 2 / com.steveflames.javantgarde.MyGdxGame.PPM + playScreen.getMarkers().get(2).height / 2 / com.steveflames.javantgarde.MyGdxGame.PPM
                                                - 0.64f*1.5f, 0);
                            }
                            break;
                        }
                    }
                }
                else if(myVariable.getType().equals("char")) {
                    for(FloatingPlatform floatingPlatform: playScreen.getFloatingPlatforms()) {
                        if(floatingPlatform.getName().equals(myVariable.getName())) {
                            if(myVariable.getValue().equals("a")) {
                                floatingPlatform.setTransform(playScreen.getMarkers().get(3).x / com.steveflames.javantgarde.MyGdxGame.PPM + playScreen.getMarkers().get(3).width / 2 / com.steveflames.javantgarde.MyGdxGame.PPM,
                                        playScreen.getMarkers().get(3).y / com.steveflames.javantgarde.MyGdxGame.PPM - floatingPlatform.getBounds().height / 2 / com.steveflames.javantgarde.MyGdxGame.PPM + playScreen.getMarkers().get(3).height / 2 / com.steveflames.javantgarde.MyGdxGame.PPM
                                                + 0.64f*1.5f, 0);
                                break;
                            }
                            else if(myVariable.getValue().equals("b")) {
                                floatingPlatform.setTransform(playScreen.getMarkers().get(3).x / com.steveflames.javantgarde.MyGdxGame.PPM + playScreen.getMarkers().get(3).width / 2 / com.steveflames.javantgarde.MyGdxGame.PPM,
                                        playScreen.getMarkers().get(3).y / com.steveflames.javantgarde.MyGdxGame.PPM - floatingPlatform.getBounds().height / 2 / com.steveflames.javantgarde.MyGdxGame.PPM + playScreen.getMarkers().get(3).height / 2 / com.steveflames.javantgarde.MyGdxGame.PPM, 0);
                                break;
                            }
                            else if(myVariable.getValue().equals("c")) {
                                floatingPlatform.setTransform(playScreen.getMarkers().get(3).x / com.steveflames.javantgarde.MyGdxGame.PPM + playScreen.getMarkers().get(3).width / 2 / com.steveflames.javantgarde.MyGdxGame.PPM,
                                        playScreen.getMarkers().get(3).y / com.steveflames.javantgarde.MyGdxGame.PPM - floatingPlatform.getBounds().height / 2 / com.steveflames.javantgarde.MyGdxGame.PPM + playScreen.getMarkers().get(3).height / 2 / com.steveflames.javantgarde.MyGdxGame.PPM
                                                - 0.64f*1.5f, 0);
                                break;
                            }
                            else {
                                floatingPlatform.setTransform(-5, -5, 0);
                                break;
                            }
                        }
                    }
                }
                else if(myVariable.getType().equals("String")) {
                    for(FloatingPlatform floatingPlatform: playScreen.getFloatingPlatforms()) {
                        if(floatingPlatform.getName().equals(myVariable.getName())) {
                            if(myVariable.getValue().equals("hello")) {
                                floatingPlatform.setTransform(playScreen.getMarkers().get(4).x / com.steveflames.javantgarde.MyGdxGame.PPM + playScreen.getMarkers().get(4).width / 2 / com.steveflames.javantgarde.MyGdxGame.PPM,
                                        playScreen.getMarkers().get(4).y / com.steveflames.javantgarde.MyGdxGame.PPM - floatingPlatform.getBounds().height / 2 / com.steveflames.javantgarde.MyGdxGame.PPM + playScreen.getMarkers().get(4).height / 2 / com.steveflames.javantgarde.MyGdxGame.PPM, 0);
                                break;
                            }
                            else {
                                floatingPlatform.setTransform(-5, -5, 0);
                                break;
                            }
                        }
                    }
                }
            }

            switch (progress) {
                case 0:
                    for(MyVariable myVariable: myClass.getFields()) {
                        if(myVariable.getName().equals("a")) {
                            if (myVariable.getType().equals("int")) {
                                if(myVariable.getValue().equals("2")) {
                                    playScreen.getHud().getEditorWindow().tempHideEditor(playScreen.getMarkers().get(0).x/ com.steveflames.javantgarde.MyGdxGame.PPM , true);
                                    return true;
                                }
                            }
                        }
                    }
                    break;
                case 1:
                    for(MyVariable myVariable: myClass.getFields()) {
                        if(myVariable.getName().equals("a")) {
                            if (myVariable.getType().equals("int")) {
                                if(!myVariable.getValue().equals("null")) {
                                    playScreen.getHud().getEditorWindow().tempHideEditor(playScreen.getMarkers().get(0).x/ com.steveflames.javantgarde.MyGdxGame.PPM, true);
                                    return true;
                                }
                            }
                        }
                    }
                    break;
                case 2:
                    for(MyVariable myVariable: myClass.getFields()) {
                        if (myVariable.getType().equals("double")) {
                            if(myVariable.getValue().contains(".")) {
                                playScreen.getHud().getEditorWindow().tempHideEditor(playScreen.getMarkers().get(1).x/ com.steveflames.javantgarde.MyGdxGame.PPM, true);
                                return true;
                            }
                        }
                    }
                    break;
                case 3:
                    for(MyVariable myVariable: myClass.getFields()) {
                        if (myVariable.getType().equals("boolean")) {
                            if(!myVariable.getValue().equals("null")) {
                                playScreen.getHud().getEditorWindow().tempHideEditor(playScreen.getMarkers().get(2).x/ com.steveflames.javantgarde.MyGdxGame.PPM, true);
                                return true;
                            }
                        }
                    }
                    break;
                case 4:
                    for(MyVariable myVariable: myClass.getFields()) {
                        if (myVariable.getType().equals("char")) {
                            if(!myVariable.getValue().equals("null")) {
                                playScreen.getHud().getEditorWindow().tempHideEditor(playScreen.getMarkers().get(3).x/ com.steveflames.javantgarde.MyGdxGame.PPM, true);
                                return true;
                            }
                        }
                    }
                    break;
                case 5:
                    for(MyVariable myVariable: myClass.getFields()) {
                        if (myVariable.getType().equals("String")) {
                            if(myVariable.getValue().equals("hello")) {
                                playScreen.getHud().getEditorWindow().tempHideEditor(playScreen.getMarkers().get(4).x/ com.steveflames.javantgarde.MyGdxGame.PPM, true);
                                return true;
                            }
                        }
                    }
                    break;
            }
        }
        else if(playScreen.getCurrentLevel().getId().equals("7_1")) {
            switch (questN) {
                case 0: //1st quest
                    if (progress == 1) {
                        for (MyVariable myVariable : myClass.getFields()) {
                            if (myVariable.getType().equals("InfoSign")) {
                                playScreen.getInfoSigns().add(new InfoSign("info-7_1-3", playScreen.getWorld(), playScreen.getMap(), playScreen.getMarkers().get(0), 0));
                                playScreen.getObjectManager().addGameObjectBeforePlayer(playScreen.getInfoSigns().get(playScreen.getInfoSigns().size()-1));
                                return true;
                            }
                        }
                    }
                    break;
                case 1: //2nd quest
                    if (progress == 1) {
                        for (MyVariable myVariable : myClass.getFields()) {
                            if (myVariable.getType().equals("Lever") && !myVariable.getName().equals("null")) {
                                playScreen.getHud().getEditorWindow().tempHideEditor(-1, true);
                                playScreen.getLevers().add(new com.steveflames.javantgarde.sprites.Lever("lever-7_1-0", playScreen.getWorld(), playScreen.getMap(), new Rectangle(playScreen.getHud().getEditorWindow().getCurrentPc().getBounds().x + 200,
                                        playScreen.getHud().getEditorWindow().getCurrentPc().getBounds().y, 90, 90), 0, false));
                                playScreen.getObjectManager().addGameObjectBeforePlayer(playScreen.getLevers().get(playScreen.getLevers().size()-1));
                                return true;
                            }
                        }
                    }
                    else if (progress==2) {
                        if(myClass.getMethodsCalled().contains("Lever.pull"))
                            return true;
                    }
                    break;
            }
        }
        return false;
    }

    public void completed(PlayScreen playScreen) {
        if(playScreen.getCurrentLevel().getId().equals("1_1")) {
            playScreen.getHud().getEditorWindow().tempHideEditor(-1, false);
            playScreen.getDoors().get(3).open();
        }
        //else if(playScreen.getCurrentLevel().getId().equals("1_2")) {

        //}
        else if(playScreen.getCurrentLevel().getId().equals("2_1")) {
            playScreen.getHud().getEditorWindow().closeCurrentEditor();
            playScreen.getDoors().get(0).open();
        }

        else if(playScreen.getCurrentLevel().getId().equals("7_1")) {
            playScreen.getHud().getEditorWindow().tempHideEditor(-1, true);
            if(questN!=0) {
                playScreen.getLevers().get(0).pull();
                playScreen.getDoors().get(4).open();
            }
        }
    }

    public boolean isCompleted() {
        return progress > questSteps.size()-1;
    }


    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getSize() {
        return questSteps.size();
    }

    public QuestStep getCurrentQuestStep() {
        return questSteps.get(progress);
    }

    public String getNextHint() {
        getCurrentQuestStep().incrementHintPtr();
        return getCurrentQuestStep().getHints().get(getCurrentQuestStep().getHintPtr());
    }

    public String getCurrentQuestStepText() {
        if(progress < questSteps.size())
            return questSteps.get(progress).getText();
        else
            return "Quest completed!";
    }

    public String getCompletedText() {
        return completedText;
    }

    public int getQuestN() {
        return questN;
    }
}
