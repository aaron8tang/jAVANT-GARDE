package com.steveflames.javalab.quests;

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
                    questSteps.add(new QuestStep(temp2[0].substring(2, temp2[0].length()-1)));
                    if(temp2.length > 1) {
                        for (int j=1; j<temp2.length; j++) {
                            questSteps.get(questSteps.size()-1).addHint(temp2[j].substring(2, temp2[j].length()-1));
                        }
                    }
                    else {
                        System.out.println("ERROR: no hints in file\n" + quest);
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
