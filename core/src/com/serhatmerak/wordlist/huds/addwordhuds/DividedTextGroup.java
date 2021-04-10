package com.serhatmerak.wordlist.huds.addwordhuds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.serhatmerak.wordlist.helpers.AppInfo;
import com.serhatmerak.wordlist.helpers.Assets;
import com.serhatmerak.wordlist.helpers.ButtonHoverAnimation;
import com.serhatmerak.wordlist.helpers.ColorPalette;
import com.serhatmerak.wordlist.helpers.DataHolder;
import com.serhatmerak.wordlist.helpers.Fonts;
import com.serhatmerak.wordlist.huds.BorderImage;
import com.serhatmerak.wordlist.huds.RoundedRectangle;
import com.serhatmerak.wordlist.huds.addwordhuds.AddWordWindow;
import com.serhatmerak.wordlist.kumar.SlowScrollPane;
import com.serhatmerak.wordlist.word.GeneratorThread;
import com.serhatmerak.wordlist.word.MeaningDiv;
import com.serhatmerak.wordlist.word.Word;
import com.serhatmerak.wordlist.word.WordList;

import java.awt.Font;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class DividedTextGroup extends Group {

    private AddWordWindow addWordWindow;

    private Group loadFileAndWordsGroup;
    private Table loadFileAndWordsTable;

    public Label errorLbl;


    private ScrollPane wordsScrollPane;
    private Table wordTable;
    private Group addWordsFromListGroup;
    private Label lblWordCount,lblInvalidCount,lblValidCount,lblKnownCount;
    private int doneCount,validCount,invalidCount,knownCount;
    private TextButton addWords;
    private TextButton finish;

    private GeneratorThread generatorThread;
    private Array<Group> wordsGroup;


    public ObjectMap<Label,Word> wordMap;

    public DividedTextGroup(String text,Vector2 size, AddWordWindow addWordWindow){

        wordsGroup = new Array<>();
        this.addWordWindow = addWordWindow;

        setSize(size.x,size.y);

        createActors();
        createWordGenerators();
        setListeners();


        addActor(loadFileAndWordsGroup);
        readFile(text);
    }

    private void createWordGenerators() {
        generatorThread = new GeneratorThread();
        generatorThread.initalize();
        wordMap = new ObjectMap<>();
    }

    private void setListeners() {

        addWords.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                addWords.setDisabled(true);
                addWords.setColor(1,1,1,0.5f);
                //startToAdd = true;
                Array<Label> labels = new Array<>();
                for (Group g:wordsGroup) {
                    Label label;
                    if (g.getChild(1) instanceof Label)
                        label = (Label) g.getChild(1);
                    else
                        label = (Label) ((SlowScrollPane) g.getChild(1)).getActor();


                    labels.add(label);
                }
                generatorThread.addWordWithFile(labels,DividedTextGroup.this);
            }
        });

        finish.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                Array<String> nameCheck = new Array<>();
                Array<Word> wordArray = new Array<>();

                for (Word w:wordMap.values().toArray()) {
                    if (w!= null && w.meaningDivs.size > 0) {
                        if (!nameCheck.contains(w.name,false)) {
                            wordArray.add(w);
                            nameCheck.add(w.name);
                        }
                    }
                }

                addWordWindow.finishLoadingFromFile(wordArray);
            }
        });
    }

    private void readFile(String text) {

            String[] wordsArray = getWordListFromText(text);

            Label.LabelStyle labelStyle = new Label.LabelStyle();
            labelStyle.font = Fonts.bl_bold_26;
            labelStyle.fontColor = Color.BLACK;


            if (wordsArray.length > 0) {
                loadFileAndWordsTable.clear();
                Array<String> checkWords = new Array<>();
                for (String word : wordsArray) {
                    if (!word.equals("") && !checkWords.contains(word,false)) {
                        if (!DataHolder.dataholder.knownWords.contains(word, false)) {
                            checkWords.add(word);

                            Label label = new Label(word, labelStyle);
                            label.setAlignment(Align.center);

                            Image bg = new Image(Assets.assets.pix);
                            bg.setSize(wordTable.getWidth() - 10,label.getPrefHeight() + 10);
                            bg.setColor(ColorPalette.Cream);

                            label.setPosition(bg.getWidth() / 2 - label.getWidth() / 2,5);

                            final Group group = new Group();
                            group.setSize(bg.getWidth(),bg.getHeight());

                            Image box = new Image(Assets.assets.pix);
                            box.setSize(10,group.getHeight());
                            box.setColor(ColorPalette.Orange);
                            box.setPosition(group.getWidth() - box.getWidth(),0);

                            ///Close Button
                            Group btnRemove;
                            {
                                btnRemove = new Group();
                                Label.LabelStyle xStyle = new Label.LabelStyle();
                                xStyle.font = Fonts.bl_bold_45;
                                Label xLabel = new Label("×",xStyle);
                                xLabel.setColor(Color.BLACK);
                                btnRemove.addActor(xLabel);
                                btnRemove.setSize(xLabel.getWidth(),xLabel.getPrefHeight());
                                btnRemove.setPosition(5,-8);
                                btnRemove.setOrigin(Align.center);

//                                btnRemove = new Group();
//                                btnRemove.setSize(20,20);
//
//                                Image circle = new Image(Assets.assets.cancelCircle);
//                                circle.setSize(btnRemove.getWidth(),btnRemove.getHeight());
//                                circle.setColor(ColorPalette.LightTopBlue);
//
//                                Image x = new Image(Assets.assets.cancelX);
//                                x.setSize(btnRemove.getWidth(),btnRemove.getHeight());
//                                x.setColor(ColorPalette.TopBlue);
//
//                                btnRemove.addActor(circle);
//                                btnRemove.addActor(x);
//                                btnRemove.setPosition(5,group.getHeight() / 2 - btnRemove.getHeight() / 2);
                            }
                            ///

                            group.addActor(bg);
                            if (label.getPrefWidth() <= group.getWidth() - 90){
                                group.addActor(label);
                            }else {
                                Sprite scroll = new Sprite(Assets.assets.pix);
                                Sprite knob = new Sprite(Assets.assets.pix);

                                scroll.setSize(100, 20);
                                scroll.setColor(Color.LIGHT_GRAY);
                                knob.setSize(20, 20);
                                knob.setColor(Color.DARK_GRAY);
                                ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
                                scrollPaneStyle.vScroll = new SpriteDrawable(scroll);
                                scrollPaneStyle.vScrollKnob = new SpriteDrawable(knob);

                                SlowScrollPane wordCardsScrollPane = new SlowScrollPane(null, scrollPaneStyle);
                                wordCardsScrollPane.setScrollingDisabled(false, true);
                                wordCardsScrollPane.setVariableSizeKnobs(true);
                                wordCardsScrollPane.setOverscroll(false, true);
                                wordCardsScrollPane.setSize(group.getWidth() - 90,group.getHeight());
                                wordCardsScrollPane.setPosition(10 + group.getWidth() / 2 - wordCardsScrollPane.getWidth() / 2,0);
                                wordCardsScrollPane.setActor(label);
                                group.addActor(wordCardsScrollPane);
                            }
                            group.addActor(box);
                            group.addActor(btnRemove);

                            wordsGroup.add(group);
                            wordTable.add(group).padTop(5).row();
                            btnRemove.addListener(new ButtonHoverAnimation(btnRemove,true){
                                @Override
                                public void clicked(InputEvent event, float x, float y) {
                                    super.clicked(event, x, y);
                                    wordsGroup.removeValue(group, true);
                                    wordTable.clear();
                                    for (Group g : wordsGroup) {
                                        wordTable.add(g).padTop(5).row();
                                    }
                                }
                            });
                        }else {
                            knownCount++;
                        }
                    }
                }
                loadFileAndWordsTable.clear();
                loadFileAndWordsTable.add(wordsScrollPane).width(wordsScrollPane.getWidth());
                loadFileAndWordsTable.add(addWordsFromListGroup).width(addWordsFromListGroup.getWidth());


                lblWordCount.setText(wordTable.getChildren().size + " / " + 0);
                lblValidCount.setText(0);
                lblInvalidCount.setText(0);
                lblKnownCount.setText(knownCount);
            }

    }

    private String[] getWordListFromText(String text) {
        text = text.replaceAll("[^a-zA-Z]", " ");
        Array<String> words = new Array<>();
        String[] firstSplitedArray = text.split("\\r?\\n? ");

        for (String firstSplitedWord:firstSplitedArray) {
            if (!firstSplitedWord.equals("")){
                String lineRemovedWord = firstSplitedWord.replaceAll("\\r?\\n?","@").replaceAll("@@"," ");
                String[] secondSplitedArray = lineRemovedWord.split(" ");
                for (String secondSplitedWord:secondSplitedArray) {
                    String lastWord = secondSplitedWord.replaceAll(
                            "[^a-zA-Z]", "").toLowerCase().replaceAll("ı","i");
                    if (!lastWord.equals(""))
                        words.add(lastWord);
                }
            }
        }

        String[] returninArray = new String[words.size];
        int i= 0;
        for (String s:words) {
            returninArray[i] = s;
            i++;
        }
        return returninArray;
    }


    private void createActors() {
//        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
//        textButtonStyle.font = Fonts.bl_bold_32;
//        textButtonStyle.fontColor = Color.WHITE;
//        textButtonStyle.downFontColor = Color.DARK_GRAY;
//        textButtonStyle.overFontColor = Color.LIGHT_GRAY;
//        textButtonStyle.disabledFontColor = Color.DARK_GRAY;

        Sprite btnBgNorm = new Sprite(Assets.assets.pix);
        btnBgNorm.setSize(400,70);
        btnBgNorm.setColor(ColorPalette.ButtonBlue);

        Sprite btnBgHover = new Sprite(Assets.assets.pix);
        btnBgHover.setSize(400,70);
        btnBgHover.setColor(ColorPalette.ButtonOverBlue);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = Fonts.bl_bold_32;
        textButtonStyle.up = new SpriteDrawable(btnBgHover);
        textButtonStyle.over = new SpriteDrawable(btnBgNorm);
        textButtonStyle.fontColor = Color.WHITE;

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = Fonts.bl_bold_32;


        loadFileAndWordsGroup = new Group();
        loadFileAndWordsGroup.setSize(getWidth(),getHeight());



        loadFileAndWordsTable = new Table();
        loadFileAndWordsTable.setSize(loadFileAndWordsGroup.getWidth() - 2 * AppInfo.PADDING,
                loadFileAndWordsGroup.getHeight() - 2 * AppInfo.PADDING);
        loadFileAndWordsTable.setPosition(AppInfo.PADDING,AppInfo.PADDING);
        loadFileAndWordsTable.defaults().pad(AppInfo.PADDING);
        loadFileAndWordsTable.left().top();

        loadFileAndWordsGroup.addActor(loadFileAndWordsTable);


        ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        Texture pix = new Texture("pix.png");
        Sprite scroll = new Sprite(pix);
        Sprite knob = new Sprite(pix);
        scrollPaneStyle.vScroll = new SpriteDrawable(scroll);
        scrollPaneStyle.vScrollKnob = new SpriteDrawable(knob);

        wordsScrollPane = new ScrollPane(null, scrollPaneStyle);
        wordsScrollPane.setVariableSizeKnobs(true);
        wordsScrollPane.setOverscroll(false, false);
        wordsScrollPane.setSize(loadFileAndWordsTable.getWidth() / 2 - 2 * AppInfo.PADDING,
                loadFileAndWordsTable.getHeight() - 2 * AppInfo.PADDING);

        wordTable = new Table();
        wordTable.setWidth(wordsScrollPane.getWidth());
        wordTable.left().top();

        wordsScrollPane.setActor(wordTable);

        addWordsFromListGroup = new Group();
        addWordsFromListGroup.setSize(wordsScrollPane.getWidth(),wordsScrollPane.getHeight());

        RoundedRectangle rightListBg = new RoundedRectangle(addWordsFromListGroup.getWidth(),addWordsFromListGroup.getHeight(),20
        );
        rightListBg.setColor(Color.LIGHT_GRAY);


        Label.LabelStyle countsStyle = new Label.LabelStyle();
        countsStyle.font = Fonts.bl_bold_32;

        lblWordCount = new Label("0",countsStyle);
        lblValidCount = new Label("0",countsStyle);
        lblInvalidCount = new Label("0",countsStyle);
        lblKnownCount = new Label("0",countsStyle);

        Label wordCountName = new Label("[BLACK]Word Count[]",countsStyle);
        Label validCountName = new Label("[BLACK]Valid[]",countsStyle);
        Label invalidCountName = new Label("[BLACK]Invalid[]",countsStyle);
        Label knownCountName = new Label("[BLACK]Known[]",countsStyle);

        lblWordCount.setColor(Color.BLACK);
        lblValidCount.setColor(Color.valueOf("00dd00"));
        lblInvalidCount.setColor(Color.valueOf("#DD0000"));
        lblKnownCount.setColor(Color.valueOf("#0000DD"));

        addWords = new TextButton("Add Words",textButtonStyle);
        addWords.setSize(350,70);
        addWords.setPosition(addWordsFromListGroup.getWidth() / 2 - addWords.getWidth() / 2,AppInfo.PADDING);

        wordCountName.setPosition(AppInfo.PADDING * 2,
                addWordsFromListGroup.getHeight() - 2 * lblWordCount.getPrefHeight() - AppInfo.PADDING);
        validCountName.setPosition(wordCountName.getX(),
                wordCountName.getY() - 5 - lblValidCount.getPrefHeight());
        invalidCountName.setPosition(wordCountName.getX(),
                validCountName.getY() - 5 - lblInvalidCount.getPrefHeight());
        knownCountName.setPosition(wordCountName.getX(),
                invalidCountName.getY() - 5 - lblInvalidCount.getPrefHeight());

        lblWordCount.setPosition(wordCountName.getX() + wordCountName.getWidth() + 50,wordCountName.getY());
        lblValidCount.setPosition(lblWordCount.getX(),validCountName.getY());
        lblInvalidCount.setPosition(lblWordCount.getX(),invalidCountName.getY());
        lblKnownCount.setPosition(lblWordCount.getX(),knownCountName.getY());


        Label.LabelStyle errorStyle = new Label.LabelStyle();
        errorStyle.font = Fonts.bl_bold_26;


        errorLbl = new Label("[RED]!Connection Problem[]",errorStyle);
        errorLbl.setPosition(addWords.getX() + addWords.getWidth() / 2 - errorLbl.getPrefWidth() / 2,addWords.getY() - 5 - errorLbl.getPrefHeight());
        errorLbl.setVisible(false);

        addWordsFromListGroup.addActor(errorLbl);
        addWordsFromListGroup.addActor(rightListBg);
        addWordsFromListGroup.addActor(addWords);
        addWordsFromListGroup.addActor(invalidCountName);
        addWordsFromListGroup.addActor(knownCountName);
        addWordsFromListGroup.addActor(validCountName);
        addWordsFromListGroup.addActor(wordCountName);
        for (int i = 0; i < 4; i++) {
            Label label = new Label("[BLACK]:[]",countsStyle);
            label.setPosition(wordCountName.getX() + wordCountName.getWidth() + 20,
                    knownCountName.getY() + i * (knownCountName.getPrefHeight() + 5));
            addWordsFromListGroup.addActor(label);
        }
        addWordsFromListGroup.addActor(lblInvalidCount);
        addWordsFromListGroup.addActor(lblValidCount);
        addWordsFromListGroup.addActor(lblWordCount);
        addWordsFromListGroup.addActor(lblKnownCount);


        finish = new TextButton("Finish",textButtonStyle);
        finish.setSize(350,70);
        finish.setPosition(addWordsFromListGroup.getWidth() / 2 - finish.getWidth() / 2,AppInfo.PADDING);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (doneCount == wordTable.getChildren().size && finish.getStage() == null){
                    addWordsFromListGroup.removeActor(addWords);
                    addWordsFromListGroup.addActor(finish);
        }
    }

    public void getWordFromThreadWithFile(Label label, int valid){
        label.setName("done");
        Group group;
        if (label.getParent() instanceof SlowScrollPane)
            group = label.getParent().getParent();
        else group = label.getParent();

        Image box = (Image) group.getChild(2);
        if (valid == 0) {
            box.setColor(Color.RED);
            invalidCount++;
            lblInvalidCount.setText(invalidCount);
        }else if (valid == 2){
            knownCount++;
            lblKnownCount.setText(knownCount);
            box.setColor(lblKnownCount.getColor());
        } else {
            box.setColor(Color.GREEN);
            validCount++;
            lblValidCount.setText(validCount);
        }
        doneCount++;
        lblWordCount.setText(wordTable.getChildren().size + " / " + doneCount);
    }

}
