package com.steveflames.javantgarde.quests;

import com.steveflames.javantgarde.tools.global.MyFileReader;

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
        parseQuestString(MyFileReader.readFile("txt/quests/quest-" + id + ".txt"));
        progress = 0;
        if(MyFileReader.exists("txt/quests/quest-"+id+"-completed.txt"))
            completedText = MyFileReader.readFile("txt/quests/quest-"+id+"-completed.txt");
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

    public boolean nextQuestStep() {
        progress++;
        return progress < getSize();
    }

    public boolean isCompleted() {
        return progress > questSteps.size()-1;
    }


    public int getProgress() {
        return progress;
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
            return "[GREEN]Quest completed![]";
    }

    public String getCompletedText() {
        return completedText;
    }

    public int getQuestN() {
        return questN;
    }
}
