package com.steveflames.javalab.scenes;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.steveflames.javalab.MyGdxGame;
import com.steveflames.javalab.quests.Quest;
import com.steveflames.javalab.tools.global.Skins;

/**
 * Created by Flames on 10/11/2017.
 */

class QuestWindow extends Window {

    private Quest currentQuest;
    private ProgressBar progressBar;
    private Label questTextArea;
    private TextButton helpBtn;
    private ScrollPane questScroll;

    QuestWindow(String title, Skin skin, final Stage stage) {
        super(title, skin);

        //quest text area
        Table table = new Table(Skins.lmlSkin);
        questTextArea = new Label("", Skins.skin);
        questTextArea.setWrap(true);
        table.add(questTextArea).left().top().expand().fillX().padLeft(5);
        questScroll = new ScrollPane(table, Skins.neonSkin);
        if(MyGdxGame.platformDepended.deviceHasKeyboard())
            questScroll.setFlickScroll(false);

        //bottom bar
        Table bottomBarTable = new Table(Skins.lmlSkin);
        progressBar = new ProgressBar(0, 3, 1, false, Skins.neonSkin);
        helpBtn = new TextButton("help", Skins.neonSkin);
        helpBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Hud.playScreen.getPlayer().reduceHealth(1);
                Hud.playScreen.getPlayer().setPlayerMsgAlpha(1);
                questTextArea.setText(questTextArea.getText() + "\n[CYAN]HELP:[]\n");
                String text = currentQuest.getNextHint();
                if(text.contains("\r")) {
                    text = text.replace("\r", "");
                    text += "\n";
                }
                currentQuest.getCurrentQuestStep().setText(questTextArea.getText() + text);
                questTextArea.setText(questTextArea.getText() + text);
                if(currentQuest.getCurrentQuestStep().getHintPtr() >= currentQuest.getCurrentQuestStep().getHints().size()-1)
                    helpBtn.setVisible(false);
                questScroll.layout();
                questScroll.setScrollPercentY(100);
            }
        });
        bottomBarTable.add(progressBar).expandX().fillX().left().padLeft(10);
        bottomBarTable.add(helpBtn).right();

        //add components to window
        this.setSize(580, 340);
        this.setX(0);
        this.setY(MyGdxGame.HEIGHT-408);
        if(!MyGdxGame.platformDepended.deviceHasKeyboard()) {
            this.setHeight(320);
            this.setY(MyGdxGame.HEIGHT-421);
        }
        this.add(questScroll).expand().fill().top().left().padTop(10);
        this.row();
        this.add(bottomBarTable).expandX().fillX();
        this.addListener(new ClickListener() {
            public void enter (InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                stage.setScrollFocus(questTextArea);
            }
        });
    }

    void incrementQuestStep(Quest quest) {
        //add progress
        quest.setProgress(quest.getProgress()+1);
        progressBar.setValue(quest.getProgress());

        questScroll.scrollTo(0, Hud.viewport.getCamera().position.y + Hud.viewport.getCamera().viewportHeight, 0, 0);
        if(quest.getProgress() < quest.getSize()) {
            questTextArea.setText(quest.getCurrentQuestStep().getText());
            helpBtn.setVisible(false);
            if(quest.getCurrentQuestStep().getHints().size()>0)
                helpBtn.setVisible(true);
        }
        else { //quest completed
            questTextArea.setText("[GREEN]Quest completed![]");
            helpBtn.setVisible(false);

            quest.completed(Hud.playScreen);
        }

    }

    void show(Stage stage, Quest quest) {
        currentQuest = quest;
        questTextArea.setText(quest.getCurrentQuestStepText());
        progressBar.setRange(0, quest.getSize());
        progressBar.setValue(quest.getProgress());
        if(!quest.isCompleted()) {
            if (quest.getCurrentQuestStep().getHints().size() == 0)
                helpBtn.setVisible(false);
        }
        else
            helpBtn.setVisible(false);
        stage.addActor(this);
    }

}
