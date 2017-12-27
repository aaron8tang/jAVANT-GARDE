package com.steveflames.javantgarde.hud.code_pc;

import com.badlogic.gdx.audio.Sound;
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
import com.steveflames.javantgarde.MyGdxGame;
import com.steveflames.javantgarde.hud.Hud;
import com.steveflames.javantgarde.quests.Quest;
import com.steveflames.javantgarde.tools.Assets;
import com.steveflames.javantgarde.tools.global.Cameras;

/**
 * Created by Flames on 10/11/2017.
 */

class QuestWindow extends Window {

    private Quest currentQuest;
    private ProgressBar progressBar;
    private Label questTextArea;
    private TextButton helpBtn;
    private ScrollPane questScroll;

    QuestWindow(String title, final Assets assets, final Hud hud) {
        super(title, assets.terraSkin);

        //quest text area
        Table table = new Table(assets.lmlSkin);
        questTextArea = new Label("", assets.terraSkin);
        questTextArea.setWrap(true);
        table.add(questTextArea).left().top().expand().fillX().padLeft(5);
        questScroll = new ScrollPane(table, assets.neonSkin);
        if(MyGdxGame.platformDepended.deviceHasKeyboard())
            questScroll.setFlickScroll(false);

        //bottom bar
        Table bottomBarTable = new Table(assets.lmlSkin);
        progressBar = new ProgressBar(0, 3, 1, false, assets.neonSkin);
        helpBtn = new TextButton("help", assets.neonSkin);
        helpBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                assets.playSound(assets.clickSound);
                hud.playScreen.getPlayer().reduceHealth(1);
                hud.playScreen.getPlayer().setPlayerMsgAlpha(1);
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
        bottomBarTable.add(helpBtn).right().width(180);

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
                hud.stage.setScrollFocus(questTextArea);
            }
        });
    }

    void incrementQuestStep(Quest quest, EditorWindow editorWindow) {
        questScroll.scrollTo(0, Cameras.hudPort.getCamera().position.y + Cameras.hudPort.getCamera().viewportHeight, 0, 0);
        if(quest.nextQuestStep()) {
            helpBtn.setVisible(false);
            if(quest.getCurrentQuestStep().getHints().size()>0)
                helpBtn.setVisible(true);
        }
        else { //quest completed
            helpBtn.setVisible(false);
            editorWindow.completed();
        }

        questTextArea.setText(quest.getCurrentQuestStepText());
        progressBar.setValue(quest.getProgress());
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
