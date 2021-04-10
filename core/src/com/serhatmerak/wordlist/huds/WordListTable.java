package com.serhatmerak.wordlist.huds;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.serhatmerak.wordlist.helpers.AppInfo;
import com.serhatmerak.wordlist.helpers.ColorPalette;
import com.serhatmerak.wordlist.helpers.DataHolder;
import com.serhatmerak.wordlist.helpers.Fonts;
import com.serhatmerak.wordlist.huds.addwordhuds.AddWordWindow;
import com.serhatmerak.wordlist.kumar.SlowScrollPane;
import com.serhatmerak.wordlist.word.Word;
import com.serhatmerak.wordlist.word.WordList;

public class WordListTable extends Group {

    public float width;
    public float height;

    private SlowScrollPane wordCardsScrollPane;

    private BorderImage backgroundImage;

    private Table wordsTable;
    private Group noWordsInListGroup;

    public boolean showStars,showKnown = true;

    boolean examplesOn, turkishOn, otherDivsOn;

    boolean emptyList = false;
    private boolean first = true;


    public WordListTable(float width,float height){
        this.width = width;
        this.height = height;

        createAllActors();
        checkListAndFillTable();
        addActors();

        addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                getStage().setKeyboardFocus(WordListTable.this);
            }
        });
    }


    private void createAllActors() {
        setSize(width , height);
        float mainBorder = 4;
        backgroundImage = new BorderImage(new Vector3(width,height,0),
                ColorPalette.LightTopBlue,Color.WHITE);

        createBottomEmptyActors();
        createWordListTable();
    }

    public void changeCardType(boolean examplesOn,boolean turkishOn,boolean otherDivsOn){
        this.examplesOn = examplesOn;
        this.turkishOn = turkishOn;
        this.otherDivsOn = otherDivsOn;

        for (Actor cardActor:wordsTable.getChildren()) {
            MainWordCard card = (MainWordCard) cardActor;
            card.createTable(examplesOn,turkishOn,otherDivsOn);
        }
        Actor[] actors = wordsTable.getChildren().toArray();
        wordsTable.clear();
        for (Actor cardActor:actors) {
            wordsTable.add(cardActor).height(cardActor.getHeight()).pad(3).row();
        }
    }

    //TODO:
    public void addWordToListClicked(){
        AddWordWindow addWordWindow = new AddWordWindow(DataHolder.dataholder.getSelectedList(),
                this);
        ShowWindow window = new ShowWindow(addWordWindow);
        window.img.setHeight(getStage().getViewport().getWorldHeight());
        window.group.setY(getStage().getViewport().getWorldHeight() / 2 - addWordWindow.getHeight() / 2);
        getStage().addActor(window);
    }

    private void createBottomEmptyActors() {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = Fonts.bl_bold_45;

        noWordsInListGroup = new Group();
        noWordsInListGroup.setWidth(width - 2 * AppInfo.PADDING);
        Label label = new Label("List is empty",labelStyle);
        label.setColor(Color.BLACK);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = Fonts.bl_bold_45;
        textButtonStyle.fontColor = Color.BLACK;
        textButtonStyle.downFontColor = Color.LIGHT_GRAY;
        textButtonStyle.overFontColor = Color.DARK_GRAY;

        TextButton btnAddWord = new TextButton("Add Word",textButtonStyle);

        btnAddWord.setPosition(noWordsInListGroup.getWidth() / 2 - btnAddWord.getPrefWidth() / 2,
                300);
        label.setPosition(noWordsInListGroup.getWidth() / 2 - label.getPrefWidth() / 2,
                btnAddWord.getY() + btnAddWord.getPrefHeight() + 300);
        noWordsInListGroup.setHeight(label.getPrefHeight() + label.getY());

        noWordsInListGroup.addActor(label);
        noWordsInListGroup.addActor(btnAddWord);


        btnAddWord.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                addWordToListClicked();
            }
        });


    }

    private void createWordListTable() {
        wordsTable = new Table();
        wordsTable.setWidth(width - 100);
        wordsTable.top().left();
        wordsTable.setPosition(AppInfo.PADDING,AppInfo.PADDING);

        ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        Texture pix = new Texture("pix.png");

        Sprite scroll = new Sprite(pix);
        Sprite knob = new Sprite(pix);

        scroll.setSize(20,100);
        scroll.setColor(Color.LIGHT_GRAY);
        knob.setSize(20,20);
        knob.setColor(Color.DARK_GRAY);
        scrollPaneStyle.vScroll = new SpriteDrawable(scroll);
        scrollPaneStyle.vScrollKnob = new SpriteDrawable(knob);

        wordCardsScrollPane = new SlowScrollPane(null, scrollPaneStyle);
        wordCardsScrollPane.setScrollingDisabled(true,false);
        wordCardsScrollPane.setVariableSizeKnobs(true);
        wordCardsScrollPane.setOverscroll(false, true);
        wordCardsScrollPane.setSize(width - AppInfo.PADDING - 7,  height);
        wordCardsScrollPane.setPosition(AppInfo.PADDING,0);
        wordCardsScrollPane.setVelocityY(wordCardsScrollPane.getVelocityY() / 100);
        wordCardsScrollPane.setActor(wordsTable);
    }


    public void removeWord(MainWordCard mainWordCard) {
        Array<MainWordCard> mainWordCards = new Array<>();
        for (Actor actor:wordsTable.getChildren()) {
            MainWordCard card = (MainWordCard) actor;
            mainWordCards.add(card);
        }

        mainWordCards.removeValue(mainWordCard,true);

        wordsTable.clear();

        DataHolder.dataholder.getSelectedList().words.removeValue(mainWordCard.word,true);
        DataHolder.dataholder.saveList(DataHolder.dataholder.getSelectedList());

        if (mainWordCards.size == 0)
            checkListAndFillTable();
        else {

            for (MainWordCard card:mainWordCards) {
                wordsTable.add(card).pad(3).row();
            }
        }
    }

    public void checkListAndFillTable() {
        WordList wordList = DataHolder.dataholder.getSelectedList();
        if (wordList.words == null || wordList.words.size == 0){
            wordsTable.clear();
            wordsTable.add(noWordsInListGroup).expandY().expandX();
            emptyList = true;
        }else {
            emptyList = false;
            wordsTable.clear();


            WordList sortedList = DataHolder.dataholder.sortAZlist(wordList);

            for (int i=0;i < sortedList.words.size;i ++) {
                Word word = sortedList.words.get(i);
                if (!(showStars && !word.star) && !(!showKnown &&
                        DataHolder.dataholder.knownWords.contains(word.name,false))) {
                    wordsTable.add(new MainWordCard(word,examplesOn,turkishOn,otherDivsOn,
                            wordCardsScrollPane.getWidth() - 25).createButtonHighlighter()).pad(5).row();
                }
            }
        }
    }

    public void addWord(Word w) {
        if (!emptyList) {
            MainWordCard wordCard = new MainWordCard(w, examplesOn, turkishOn,
                    otherDivsOn, wordCardsScrollPane.getWidth() - 25);
            wordCard.createButtonHighlighter();
            wordsTable.add(wordCard).pad(5).row();
        }else {
            checkListAndFillTable();
        }

    }

    private void addActors() {
        addActor(backgroundImage);
        addActor(wordCardsScrollPane);
    }


    @Override
    public void act(float delta) {
        super.act(delta);

        if (first){
            getStage().addListener(new InputListener(){
                @Override
                public boolean keyTyped(InputEvent event, char character) {

                    if (DataHolder.dataholder.getSelectedList().words.size == 0)
                        return super.keyTyped(event, character);
                    if (showStars && DataHolder.dataholder.getStarryWordCount() == 0)
                        return super.keyTyped(event, character);

                    String typedLetter = String.valueOf(character).toLowerCase().replace("ı","i");

                    String letters = "abcdefghijklmnoprstuvwxyz";

                    if (letters.indexOf(typedLetter) > 0) {
                        ObjectMap<String, Float> letterHeights = new ObjectMap<>();

                        //set letter heights
                        {
                            String previousLetter = "";
                            float previousHeight = 0;
                            for (Actor actor : wordsTable.getChildren()) {
                                MainWordCard card = (MainWordCard) actor;
                                String firstLetter = card.word.name.toLowerCase().replace("ı", "i").substring(0, 1);

                                if (firstLetter.equals(previousLetter)) {
                                    previousHeight += card.getHeight() + 10;
                                } else {
                                    if (!previousLetter.equals("")) {
                                        letterHeights.put(previousLetter, previousHeight);
                                    }

                                    previousLetter = firstLetter;
                                    previousHeight = card.getHeight();
                                }
                            }

                            if (!previousLetter.equals("")) {
                                letterHeights.put(previousLetter, previousHeight);
                            }
                        }

                        Array<String> alphabet = new Array<>();

                        for (int i = 0; i < letters.length(); i++) {
                            alphabet.add(letters.substring(i, i + 1));
                        }

                        float heightToLetter = 0;
                        boolean found = false;
                        for (String let : alphabet) {
                            if (!found) {
                                if (typedLetter.equals(let)) {
                                    found = true;
                                } else {
                                    heightToLetter += letterHeights.get(let, 0f);
                                }
                            }
                        }

//                    wordCardsScrollPane.setScrollY(
//                            Math.max(0, ((wordCardsScrollPane.getMaxY() / (wordsTable.getHeight() - 118)) * heightToLetter)));
                        wordCardsScrollPane.setScrollY(heightToLetter);
                    }

                    return super.keyTyped(event, character);
                }
            });
            first = false;
        }

//        System.out.println(wordCardsScrollPane.getScrollY());
    }



    @Override
    public void setHeight(float height) {
        this.height = height;
        /*
        topListGroup.addAction(Actions.moveTo(0,AppInfo.HEIGHT - AppInfo.PADDING,
                            0.3f, Interpolation.fastSlow));
         */
        backgroundImage.addAction(Actions.sizeTo(backgroundImage.getWidth(),height,0.3f, Interpolation.fastSlow));
        wordCardsScrollPane.addAction(Actions.sizeTo(backgroundImage.getWidth(),height,0.3f, Interpolation.fastSlow));
    }

}
