package com.steveflames.javalab.quests;

import com.steveflames.javalab.scenes.Hud;
import com.steveflames.javalab.screens.PlayScreen;
import com.steveflames.javalab.tools.compiler.MyClass;
import com.steveflames.javalab.tools.compiler.MyVariable;

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

    public boolean validateCodeForQuest(PlayScreen playScreen, ArrayList<MyClass> myClasses) {
        MyClass myClass = myClasses.get(0);

        if(playScreen.getCurrentLevel().getId().equals("1_1")) {
            switch (playScreen.getHud().getQuest().getProgress()) {
                case 0:
                    if (playScreen.getHud().getConsoleTextArea().getText().toString().equals("[RED]Error: main method not found[]\n"))
                        return true;
                    break;
                case 1:
                        return true;
                case 2:
                    if(myClasses.get(0).getCode().contains("System.out.println(\"Hello World!\");"))
                        return true;
                    break;
            }
        }
        else if(playScreen.getCurrentLevel().getId().equals("2_1")) {
            switch (playScreen.getHud().getQuest().getProgress()) {
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

            switch (playScreen.getHud().getQuest().getProgress()) {
                case 0:

                    break;
                case 1:

                    break;
                case 2:

                    break;
                case 3:

                    break;
                case 4:

                    break;
                case 5:

                    break;
                case 6:

                    break;
            }
        }
        return false;
    }

    public void completed(PlayScreen playScreen) {
        if(playScreen.getCurrentLevel().getId().equals("1_1")) {
            playScreen.getHud().closeCurrentEditor();
            Hud.newToast("Congratulations! You completed your first quest!\n" +
                    "You managed to print to the user of your program the phrase 'Hello World!'\n" +
                    "Go through the portal to continue your journey and learn more about the world of Java!");
            playScreen.getDoors().get(3).open();
        }
        else if(playScreen.getCurrentLevel().getId().equals("1_2")) {

        }
        else if(playScreen.getCurrentLevel().getId().equals("2_1")) {
            playScreen.getHud().closeCurrentEditor();
            /*Hud.newToast("Congratulations! You completed your first quest!\n" +
                    "You managed to print to the user of your program the phrase 'Hello World!'\n" +
                    "Go through the portal to continue your journey and learn more about the world of Java!");*/
            playScreen.getDoors().get(0).open();
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
