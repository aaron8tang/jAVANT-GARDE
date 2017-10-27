package com.steveflames.javalab.quests;

import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.steveflames.javalab.ingame_classes.iUserCode;
import com.steveflames.javalab.screens.PlayScreen;

import java.util.ArrayList;

/**
 * Created by Flames on 6/10/2017.
 */

public class Quest {

    private ArrayList<QuestStep> questSteps = new ArrayList<QuestStep>();
    private int progress = 0;

    public void parseQuestString(String quest) {
        if(quest.contains("~QUEST")) {
            String[] temp = quest.split("~QUEST");
            String[] temp2;

            if (temp.length > 1) {
                for (int i = 1; i < temp.length; i++) {
                    temp2 = temp[i].split("#HINT");
                    questSteps.add(new QuestStep(temp2[0].substring(1, temp2[0].length()-1)));
                    if(temp2.length > 1) {
                        for (int j=1; j<temp2.length; j++) {
                            questSteps.get(questSteps.size()-1).addHint(temp2[j].substring(1, temp2[j].length()-1));
                        }
                    }
                    else {
                        System.out.println("WARNING: no hints");
                    }
                }
            }
            else {
                System.out.println("ERROR: file length < 2\n" + quest);
            }
        }
        else {
            System.out.println("ERROR: wrong text format\n" + quest);
        }
    }

    public boolean handleQuestResult(PlayScreen playScreen) {
        if(playScreen.getCurrentLevel().getId().equals("1_1")) {
            switch (playScreen.getHud().getQuest().getProgress()) {
                case 0:
                    if (playScreen.getHud().getConsoleTextArea().getText().equals("Error: main method not found\n"))
                        return true;
                    break;
                case 1:
                        return true;
                case 2:
                    //if (playScreen.getHud().getConsoleTextArea().getText().startsWith("Hello World!"))
                    if(playScreen.getHud().getCodeTextArea().getText().contains("System.out.println(\"Hello World!\");"))
                        return true;
                    break;
            }
        }
        else if(playScreen.getCurrentLevel().getId().equals("1_2")) {
            String[] splitter;
            if (playScreen.getHud().getCodeTextArea().getText().contains("\n")) {
                splitter = playScreen.getHud().getCodeTextArea().getText().split("\n");
                switch (playScreen.getHud().getQuest().getProgress()) {
                    case 0:
                        for (String lineOfCode : splitter) {
                            if (lineOfCode.trim().startsWith("int"))
                                return true;
                        }
                        break;

                    case 1:
                        for (String lineOfCode : splitter) {
                            if (lineOfCode.trim().startsWith("double"))
                                return true;
                        }
                        break;
                    case 2:
                        for (String lineOfCode : splitter) {
                            if (lineOfCode.trim().startsWith("boolean"))
                                return true;
                        }
                        break;
                    case 3:
                        for (String lineOfCode : splitter) {
                            if (lineOfCode.trim().startsWith("char"))
                                return true;
                        }
                        break;
                    case 4:
                        for (String lineOfCode : splitter) {
                            if (lineOfCode.trim().startsWith("String"))
                                return true;
                        }
                        break;
                }
            }
        }
        else if(playScreen.getCurrentLevel().getId().equals("1_3")) {
            boolean flag = false;
            String varName = "~";
            String[] splitter2;
            switch (playScreen.getHud().getQuest().getProgress()) {
                case 0:
                    if (playScreen.getHud().getCodeTextArea().getText().contains("\n")) {
                        String[] splitter = playScreen.getHud().getCodeTextArea().getText().split("\n");
                        for (String lineOfCode : splitter) {
                            if (lineOfCode.trim().startsWith("int") && lineOfCode.replaceAll("\\s+", "").contains("=10;"))
                                return true;
                        }
                    }
                    break;
                case 1:
                    if (playScreen.getHud().getCodeTextArea().getText().contains("\n")) {
                        String[] splitter = playScreen.getHud().getCodeTextArea().getText().split("\n");
                        for (String lineOfCode : splitter) {
                            if (lineOfCode.trim().startsWith("int")) {
                                flag = true;
                                splitter2 = lineOfCode.trim().replaceAll("\\s+", " ").split(" ");
                                varName = splitter2[1].replaceAll(";", "");
                                continue;
                            }
                            if (flag && lineOfCode.replaceAll("\\s+", "").contains(varName + "="))
                                return true;
                        }
                    }
                    break;
                case 2:
                    if (playScreen.getHud().getCodeTextArea().getText().contains("\n")) {
                        String[] splitter = playScreen.getHud().getCodeTextArea().getText().split("\n");
                        for (String lineOfCode : splitter) {
                            if (lineOfCode.trim().startsWith("double")) {
                                flag = true;
                                splitter2 = lineOfCode.trim().replaceAll("\\s+", " ").split(" ");
                                varName = splitter2[1].replaceAll(";", "");
                                if (lineOfCode.contains("=") && lineOfCode.contains("."))
                                    return true;
                                continue;
                            }
                            if (flag && lineOfCode.replaceAll("\\s+", "").contains(varName + "=") && lineOfCode.contains("."))
                                return true;
                        }
                    }
                    break;
                case 3:
                    if (playScreen.getHud().getCodeTextArea().getText().contains("\n")) {
                        String[] splitter = playScreen.getHud().getCodeTextArea().getText().split("\n");
                        for (String lineOfCode : splitter) {
                            if (lineOfCode.trim().startsWith("boolean")) {
                                flag = true;
                                splitter2 = lineOfCode.trim().replaceAll("\\s+", " ").split(" ");
                                varName = splitter2[1].replaceAll(";", "");
                                if (lineOfCode.contains("=") && (lineOfCode.contains("true") || lineOfCode.contains("false")))
                                    return true;
                                continue;
                            }
                            if (flag && lineOfCode.replaceAll("\\s+", "").contains(varName + "=") && (lineOfCode.contains("true") || lineOfCode.contains("false")))
                                return true;
                        }
                    }
                    break;
                case 4:
                    if (playScreen.getHud().getCodeTextArea().getText().contains("\n")) {
                        String[] splitter = playScreen.getHud().getCodeTextArea().getText().split("\n");
                        for (String lineOfCode : splitter) {
                            if (lineOfCode.trim().startsWith("char")) {
                                flag = true;
                                splitter2 = lineOfCode.trim().replaceAll("\\s+", " ").split(" ");
                                varName = splitter2[1].replaceAll(";", "");
                                if (lineOfCode.contains("=") && lineOfCode.contains("'"))
                                    return true;
                                continue;
                            }
                            if (flag && lineOfCode.replaceAll("\\s+", "").contains(varName + "=") && lineOfCode.contains("'"))
                                return true;
                        }
                    }
                    break;
                case 5:
                    if (playScreen.getHud().getCodeTextArea().getText().contains("\n")) {
                        String[] splitter = playScreen.getHud().getCodeTextArea().getText().split("\n");
                        for (String lineOfCode : splitter) {
                            if (lineOfCode.trim().startsWith("String")) {
                                flag = true;
                                splitter2 = lineOfCode.trim().replaceAll("\\s+", " ").split(" ");
                                varName = splitter2[1].replaceAll(";", "");
                                if (lineOfCode.contains("=") && lineOfCode.contains("\""))
                                    return true;
                                continue;
                            }
                            if (flag && lineOfCode.replaceAll("\\s+", "").contains(varName + "=") && lineOfCode.contains("\""))
                                return true;
                        }
                    }
                    break;
                case 6:
                    if (playScreen.getHud().getCodeTextArea().getText().contains("\n")) {
                        String[] splitter = playScreen.getHud().getCodeTextArea().getText().split("\n");
                        for (String lineOfCode : splitter) {
                            if (lineOfCode.trim().contains("String")) {
                                flag = true;
                                splitter2 = lineOfCode.trim().replaceAll("\\s+", " ").split(" ");
                                varName = splitter2[1].replaceAll(";", "");
                                if (lineOfCode.contains("=") && lineOfCode.contains("\""))
                                    return true;
                                continue;
                            }
                            if (flag && lineOfCode.replaceAll("\\s+", "").contains(varName + "=") && lineOfCode.contains("\""))
                                return true;
                        }
                    }
                    break;
            }
        }
        return false;
    }

    public void completed(PlayScreen playScreen) {
        if(playScreen.getCurrentLevel().getId().equals("1_1")) {
            playScreen.getHud().newToast("Congratulations! You completed your first quest!\n" +
                    "You managed to print to the user of your program the phrase 'Hello World!'\n" +
                    "Go through the portal to continue your journey and learn more about the world of Java!");
            playScreen.getDoors().get(3).open();
        }
        else if(playScreen.getCurrentLevel().getId().equals("1_2")) {

        }
        else if(playScreen.getCurrentLevel().getId().equals("1_3")) {

        }
    }


    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public ArrayList<QuestStep> getQuestSteps() {
        return questSteps;
    }

    public String getNextHint(Quest quest) {
        quest.getQuestSteps().get(quest.getProgress()).incrementHintPtr();
        return quest.getQuestSteps().get(quest.getProgress()).getHints().get(quest.getQuestSteps().get(quest.getProgress()).getHintPtr());
    }
}
