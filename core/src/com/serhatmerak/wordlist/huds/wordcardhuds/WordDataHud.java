package com.serhatmerak.wordlist.huds.wordcardhuds;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Array;
import com.serhatmerak.wordlist.helpers.AppInfo;
import com.serhatmerak.wordlist.helpers.Assets;
import com.serhatmerak.wordlist.helpers.ColorPalette;
import com.serhatmerak.wordlist.helpers.DataHolder;
import com.serhatmerak.wordlist.helpers.Fonts;
import com.serhatmerak.wordlist.huds.MainWordCard;
import com.serhatmerak.wordlist.huds.RoundedRectangle;
import com.serhatmerak.wordlist.kumar.CollectDataFromNet;
import com.serhatmerak.wordlist.kumar.SlowScrollPane;
import com.serhatmerak.wordlist.word.MeaningDiv;
import com.serhatmerak.wordlist.word.Word;

public class WordDataHud extends Group {

    private Word newWord;
    private CollectDataFromNet.WordData wordData;
    private boolean missFrame = false;

    private Label waitingLabel;
    private SlowScrollPane wordCardsScrollPane;
    private Group synAntGroup;
    private Group examplesGroup;
    private ScrollPane.ScrollPaneStyle scrollPaneStyle;

    public WordDataHud(Word word){
        createNewWord(word);


        setSize(AppInfo.WIDTH * 0.9f,AppInfo.HEIGHT * 0.9f);
        createActors();
        if (DataHolder.dataholder.loadedWordData.get(word.name) != null) {
            wordData = DataHolder.dataholder.loadedWordData.get(word.name);
            waitingLabel.remove();
            createSynAntGroup();
            createExamplesGroup();
            fillExamplesTable();

            addActor(wordCardsScrollPane);
            addActor(synAntGroup);
            addActor(examplesGroup);
        }

    }

    private void createNewWord(Word word) {
        newWord = new Word(word.name);
        newWord.meaningDivs = new Array<>();
        for (MeaningDiv meaningDiv :word.meaningDivs) {
            MeaningDiv newDiv = new MeaningDiv();
            newDiv.title = meaningDiv.title;
            newDiv.engDef = meaningDiv.engDef;
            newDiv.trDef = meaningDiv.trDef;
            newDiv.examples = meaningDiv.examples;
            newDiv.difficulty = meaningDiv.difficulty;
            newDiv.isCollacation = meaningDiv.isCollacation;
            newWord.meaningDivs.add(newDiv);
        }
        newWord.star = word.star;

    }

    private void createActors() {
        Image bg = new Image(Assets.assets.pix);
        bg.setSize(getWidth(),getHeight());
        bg.setColor(ColorPalette.LightTopBlue);
        addActor(bg);
        createWaitingGroup();
        createWordCardGroup();
    }

    private void createExamplesGroup() {
        examplesGroup = new Group();
        examplesGroup.setSize(synAntGroup.getWidth(),getHeight() - 2 * AppInfo.PADDING);
        examplesGroup.setPosition(AppInfo.PADDING,AppInfo.PADDING);

        Image bg = new Image(Assets.assets.pix);
        bg.setSize(examplesGroup.getWidth(),examplesGroup.getHeight());
        bg.setColor(Color.valueOf("c3cad5"));

        Image topBg = new Image(Assets.assets.pix);
        topBg.setSize(examplesGroup.getWidth(),60);
        topBg.setColor(ColorPalette.TopBlue);
        topBg.setPosition(0,bg.getHeight() - topBg.getHeight());

        Label.LabelStyle titleStyle = new Label.LabelStyle();
        titleStyle.font = Fonts.bl_bold_36;
        Label title = new Label("Example Sentences",titleStyle);
        title.setPosition(2* AppInfo.PADDING,topBg.getHeight() / 2 - title.getHeight() / 2 + topBg.getY());

        examplesGroup.addActor(bg);
        examplesGroup.addActor(topBg);
        examplesGroup.addActor(title);


    }

    private void createSynAntGroup() {
        synAntGroup = new Group();
        synAntGroup.setSize(wordCardsScrollPane.getWidth(),getHeight() - 3 * AppInfo.PADDING -
                Math.min(wordCardsScrollPane.getHeight(),wordCardsScrollPane.getActor().getHeight()));
        synAntGroup.setPosition(wordCardsScrollPane.getX(),getHeight() - synAntGroup.getHeight() - AppInfo.PADDING);

        Image topBgLeft = new Image(Assets.assets.pix);
        Image topBgRight = new Image(Assets.assets.pix);

        topBgLeft.setSize((wordCardsScrollPane.getWidth() - AppInfo.PADDING) / 2f,60);
        topBgRight.setSize(topBgLeft.getWidth(),topBgLeft.getHeight());

        topBgLeft.setColor(ColorPalette.TopBlue);
        topBgRight.setColor(ColorPalette.TopBlue);

        topBgLeft.setPosition(0,synAntGroup.getHeight() - topBgLeft.getHeight());
        topBgRight.setPosition(topBgLeft.getWidth() + AppInfo.PADDING,topBgLeft.getY());

        Image bgLeft = new Image(Assets.assets.pix);
        Image bgRight = new Image(Assets.assets.pix);

        bgLeft.setSize(topBgLeft.getWidth(),synAntGroup.getHeight());
        bgRight.setSize(bgLeft.getWidth(),bgLeft.getHeight());

        bgRight.setPosition(topBgRight.getX(),0);

        bgLeft.setColor(Color.valueOf("c3cad5"));
        bgRight.setColor(Color.valueOf("c3cad5"));

        Label.LabelStyle titleStyle = new Label.LabelStyle();
        titleStyle.font = Fonts.bl_bold_36;

        Label synonymTitle = new Label("Synonyms",titleStyle);
        synonymTitle.setPosition(2* AppInfo.PADDING,topBgLeft.getHeight() / 2 - synonymTitle.getHeight() / 2 + topBgLeft.getY());

        Label antonymTitle = new Label("Antonyms",titleStyle);
        antonymTitle.setPosition(topBgRight.getX() + 2* AppInfo.PADDING,topBgLeft.getHeight() / 2 - synonymTitle.getHeight() / 2 + topBgLeft.getY());

        synAntGroup.addActor(bgLeft);
        synAntGroup.addActor(bgRight);
        synAntGroup.addActor(topBgLeft);
        synAntGroup.addActor(topBgRight);
        synAntGroup.addActor(synonymTitle);
        synAntGroup.addActor(antonymTitle);

        {

            Table synonymTable = new Table();
            synonymTable.setSize(bgLeft.getWidth(), bgLeft.getHeight() - topBgLeft.getHeight());
            synonymTable.left().top();

            SlowScrollPane synonymScrollPane = new SlowScrollPane(synonymTable, scrollPaneStyle);
            synonymScrollPane.setScrollingDisabled(true, false);
            synonymScrollPane.setVariableSizeKnobs(true);
            synonymScrollPane.setOverscroll(false, true);
            synonymScrollPane.setSize(synonymTable.getWidth(),synonymTable.getHeight());


            Label.LabelStyle labelStyle = new Label.LabelStyle();
            labelStyle.font = Fonts.bl_bold_26;
            for (int i = 0; i < wordData.synonyms.size; i++) {
                if (wordData.synonyms.get(i)!= null) {
                    Label sense = new Label("[BLACK]" + (i + 1) + ": In the sense of [RED]" +
                            wordData.definitions.get(i) + "[]", labelStyle);
                    sense.setWidth(synonymTable.getWidth() - 12);
                    sense.setWrap(true);
                    synonymTable.add(sense).width(sense.getWidth()).pad(6).row();

                    for (String synonymWord : wordData.synonyms.get(i)) {
                        Label synonymLabel = new Label(synonymWord, labelStyle);
                        synonymLabel.setWidth(synonymTable.getWidth() - 12);
                        synonymLabel.setWrap(true);
                        synonymLabel.setColor(Color.BLACK);
                        synonymTable.add(synonymLabel).width(synonymLabel.getWidth()).pad(6).row();
                    }
                    synonymTable.padTop(6).row();
                }
            }

            synAntGroup.addActor(synonymScrollPane);

        }

        {

            Table antonymTable = new Table();
            antonymTable.setSize(bgLeft.getWidth(), bgLeft.getHeight() - topBgLeft.getHeight());
            antonymTable.left().top();

            SlowScrollPane antonymScrollPane = new SlowScrollPane(antonymTable, scrollPaneStyle);
            antonymScrollPane.setScrollingDisabled(true, false);
            antonymScrollPane.setVariableSizeKnobs(true);
            antonymScrollPane.setOverscroll(false, true);
            antonymScrollPane.setSize(antonymTable.getWidth(),antonymTable.getHeight());
            antonymScrollPane.setPosition(bgRight.getX(),0);


            Label.LabelStyle labelStyle = new Label.LabelStyle();
            labelStyle.font = Fonts.bl_bold_26;
            int index = 1;
            for (int i = 0; i < wordData.synonyms.size; i++) {
                if (wordData.antonyms.get(i)!= null) {
                    Label sense = new Label("[BLACK]" + index + ": In the sense of [RED]" +
                            wordData.definitions.get(i) + "[]", labelStyle);
                    sense.setWidth(antonymTable.getWidth() - 12);
                    sense.setWrap(true);
                    antonymTable.add(sense).width(sense.getWidth()).pad(6).row();

                    for (String antonymWord : wordData.antonyms.get(i)) {
                        Label antonymLabel = new Label(antonymWord, labelStyle);
                        antonymLabel.setWidth(antonymTable.getWidth() - 12);
                        antonymLabel.setWrap(true);
                        antonymLabel.setColor(Color.BLACK);
                        antonymTable.add(antonymLabel).width(antonymLabel.getWidth()).pad(6).row();
                    }
                    antonymTable.padTop(6).row();
                    index ++;
                }
            }

            synAntGroup.addActor(antonymScrollPane);

        }

    }

    private void createWordCardGroup() {
        Texture pix = new Texture("pix.png");
        Sprite scroll = new Sprite(pix);
        Sprite knob = new Sprite(pix);

        scroll.setSize(20, 100);
        scroll.setColor(Color.LIGHT_GRAY);
        knob.setSize(5, 20);
        knob.setColor(Color.DARK_GRAY);
        scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        scrollPaneStyle.vScroll = new SpriteDrawable(scroll);
        scrollPaneStyle.vScrollKnob = new SpriteDrawable(knob);

        wordCardsScrollPane = new SlowScrollPane(null, scrollPaneStyle);
        wordCardsScrollPane.setScrollingDisabled(true, false);
        wordCardsScrollPane.setVariableSizeKnobs(true);
        wordCardsScrollPane.setOverscroll(false, true);
        wordCardsScrollPane.setSize(getWidth() / 2 - 2 * AppInfo.PADDING,
                (getHeight() - 3 * AppInfo.PADDING) / 2.5f);
        wordCardsScrollPane.setPosition(getWidth() - AppInfo.PADDING - wordCardsScrollPane.getWidth()
                , AppInfo.PADDING);

        MainWordCard wordCard = new MainWordCard(newWord,true,true,
                true,getWidth() / 2 - 3 * AppInfo.PADDING);
        //TODO: if >
        wordCard.setPosition(getWidth() - wordCard.getWidth() - 2 * AppInfo.PADDING,
                getHeight() / 2 - wordCard.getHeight() / 2);

        wordCardsScrollPane.setActor(wordCard);
    }

    private void createWaitingGroup() {

        Label.LabelStyle bigStyle = new Label.LabelStyle();
        bigStyle.font = Fonts.bl_bold_45;

        waitingLabel = new Label("Loading ...",bigStyle);

        waitingLabel.setPosition(getWidth() / 2 - waitingLabel.getWidth() / 2,
                getHeight() / 2 - waitingLabel.getHeight() / 2);

        addActor(waitingLabel);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (!missFrame)
            missFrame = true;
        else if (wordData == null){
            CollectDataFromNet collectDataFromNet = new CollectDataFromNet();
            wordData =
                            collectDataFromNet.getData(newWord);

            waitingLabel.remove();
            createSynAntGroup();
            createExamplesGroup();
            fillExamplesTable();

            addActor(wordCardsScrollPane);
            addActor(synAntGroup);
            addActor(examplesGroup);

            DataHolder.dataholder.saveWordData(wordData);

        }
    }

    private void fillExamplesTable() {
        SlowScrollPane sentencesScrollPane = new SlowScrollPane(null, scrollPaneStyle);
        sentencesScrollPane.setScrollingDisabled(true, false);
        sentencesScrollPane.setVariableSizeKnobs(true);
        sentencesScrollPane.setOverscroll(false, true);
        sentencesScrollPane.setSize(examplesGroup.getWidth(),examplesGroup.getHeight() - 60);
        examplesGroup.addActor(sentencesScrollPane);

        Table sentencesTable = new Table();
        sentencesTable.top().left();
        sentencesTable.setHeight(examplesGroup.getWidth());
        sentencesScrollPane.setActor(sentencesTable);


        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = Fonts.bl_bold_26;
        for (String sentence:wordData.sentences) {
            Group g = new Group();
            g.setWidth(examplesGroup.getWidth() - 2 * AppInfo.PADDING);

            Label sentenceLabel = new Label(sentence,labelStyle);
            sentenceLabel.setWidth(g.getWidth() - 2 * AppInfo.PADDING);
            sentenceLabel.setWrap(true);
            sentenceLabel.setColor(Color.BLACK);
            sentenceLabel.setHeight(sentenceLabel.getPrefHeight());
            sentenceLabel.setPosition(AppInfo.PADDING,5);

            RoundedRectangle gBg = new RoundedRectangle(g.getWidth(),sentenceLabel.getPrefHeight() + 10,15);
            gBg.setColor(Color.valueOf("#d2d7e0"));

            g.addActor(gBg);
            g.addActor(sentenceLabel);

            g.setHeight(gBg.getHeight());

            sentencesTable.add(g).padLeft(AppInfo.PADDING).padBottom(AppInfo.PADDING / 2f).padTop(AppInfo.PADDING / 2f).row();
        }
    }
}
