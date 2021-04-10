package com.serhatmerak.wordlist.huds;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.serhatmerak.wordlist.helpers.AppInfo;
import com.serhatmerak.wordlist.helpers.Assets;
import com.serhatmerak.wordlist.helpers.ButtonHoverAnimation;
import com.serhatmerak.wordlist.helpers.ColorPalette;
import com.serhatmerak.wordlist.helpers.DataHolder;
import com.serhatmerak.wordlist.helpers.Fonts;
import com.serhatmerak.wordlist.kumar.SlowScrollPane;
import com.serhatmerak.wordlist.word.Word;
import com.serhatmerak.wordlist.word.WordList;

import javax.xml.crypto.Data;

public class WordListsMainActor extends Group {

    public float width = 1500;
    public float height = 1060;
    private float topListsHeight = 60;
    private float topListsWidth = width - AppInfo.PADDING * 2;
    private float botListHeight = height - 3 * AppInfo.PADDING - topListsHeight;
    private TextField topCreateListTextField;

    private Array<Label> topListsArray;
    private SlowScrollPane wordCardsScrollPane;
    private ScrollPane.ScrollPaneStyle scrollPaneStyle;

    private BorderImage backgroundImage,topListImage,botListImage;
    private Group topListsGroup,topCreateListGroup;

    private Table wordsTable;
    private Label noListWarningLabel;
    private Group noWordsInListGroup;
    private Group wordListOptions;

    private boolean showAll = false,showExample = false,showTurkish = false;
    public boolean showKnown = true;
    private boolean showStars;


    public WordListsMainActor(){
        createAllActors();
        checkListAndFillTable();
        addActors();
    }

    public WordListsMainActor(float width,float height){
        this.width = width;
        this.height = height;

        createAllActors();
        checkListAndFillTable();
        addActors();
    }


    private void createAllActors() {
        setSize(width , height);
        float mainBorder = 4;
        backgroundImage = new BorderImage(new Vector3(width,height, mainBorder));

        float insideBorder = 2;
        topListImage = new BorderImage(new Vector3(topListsWidth,topListsHeight, insideBorder));
        topListImage.setPosition(AppInfo.PADDING,height - AppInfo.PADDING - topListsHeight);

        botListImage = new BorderImage(new Vector3(topListsWidth,botListHeight, insideBorder));
        botListImage.setPosition(AppInfo.PADDING,AppInfo.PADDING);

        createTopLists();
        createTopCreateListGroup();
        createBottomEmptyActors();
        createWordListTable();
        createWordListOptions();
    }

    private void createWordListOptions() {
        TextButton.TextButtonStyle textButtonStyle2 = new TextButton.TextButtonStyle();
        textButtonStyle2.font = Fonts.bl_bold_45;
        textButtonStyle2.fontColor = Color.WHITE;
        textButtonStyle2.downFontColor = Color.DARK_GRAY;
        textButtonStyle2.overFontColor = Color.LIGHT_GRAY;

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = Fonts.bl_bold_32;


        final Label btnShowExamples = new Label("Ex",labelStyle);
        final Label btnShowTurkish = new Label("Tr",labelStyle);
        final Label btnShowAll = new Label("All",labelStyle);
        final Image btnStar = new Image(Assets.assets.star);
        final Image btnKnown = new Image(Assets.assets.tick);

        btnShowAll.setOrigin(Align.center);
        btnShowExamples.setOrigin(Align.center);
        btnShowTurkish.setOrigin(Align.center);
        btnStar.setOrigin(Align.center);
        btnKnown.setOrigin(Align.center);

        TextButton btnRemoveList = new TextButton("-",textButtonStyle2);
        TextButton btnAddWord = new TextButton("+",textButtonStyle2);

        btnRemoveList.setPosition(13,0);
        btnAddWord.setPosition(8,btnRemoveList.getPrefHeight() + AppInfo.PADDING);
        btnRemoveList.setScale(1.2f,2);

        btnShowAll.setPosition(0,wordCardsScrollPane.getY() + wordCardsScrollPane.getHeight() - btnShowAll.getPrefHeight() - 138);
        btnShowTurkish.setPosition(0,btnShowAll.getPrefHeight() + btnShowAll.getY() + AppInfo.PADDING);
        btnShowExamples.setPosition(0,btnShowTurkish.getPrefHeight() + btnShowTurkish.getY() + AppInfo.PADDING);
        btnStar.setPosition(0,btnShowAll.getY() - btnStar.getPrefHeight() - 2 * AppInfo.PADDING);
        btnKnown.setPosition(0,btnStar.getY() - btnKnown.getPrefHeight() - 2 * AppInfo.PADDING);

        btnKnown.setColor(Color.BLUE);

        wordListOptions = new Group();
        wordListOptions.addActor(btnRemoveList);
        wordListOptions.addActor(btnAddWord);
        wordListOptions.addActor(btnShowAll);
        wordListOptions.addActor(btnShowTurkish);
        wordListOptions.addActor(btnShowExamples);
        wordListOptions.addActor(btnStar);
        wordListOptions.addActor(btnKnown);
        wordListOptions.setPosition(wordsTable.getWidth() + wordsTable.getX() + AppInfo.PADDING,wordCardsScrollPane.getY());

        btnAddWord.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                addWordToListClicked();
            }
        });

        btnRemoveList.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
               removeListClicked();
            }
        });

        btnShowExamples.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                showExample = !showExample;
                if (showExample)
                    btnShowExamples.setColor(ColorPalette.text1);
                else
                    btnShowExamples.setColor(Color.WHITE);
                //TODO: card change
                changeWordCards();
            }
        });

        btnShowAll.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                showAll = !showAll;
                if (showAll)
                    btnShowAll.setColor(ColorPalette.text1);
                else
                    btnShowAll.setColor(Color.WHITE);

                //TODO: card change
                changeWordCards();
            }
        });

        btnShowTurkish.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                showTurkish = !showTurkish;
                if (showTurkish)
                    btnShowTurkish.setColor(ColorPalette.text1);
                else
                    btnShowTurkish.setColor(Color.WHITE);

                //TODO: card change
                changeWordCards();
            }
        });

        btnStar.addListener(new ButtonHoverAnimation(btnStar){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                showStars = !showStars;
                if (showStars)
                    btnStar.setColor(Color.GOLD);
                else
                    btnStar.setColor(Color.WHITE);

                //TODO: card change
                changeWordCards();
            }
        });

        btnKnown.addListener(new ButtonHoverAnimation(btnKnown){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                showKnown = !showKnown;
                if (showKnown)
                    btnKnown.setColor(Color.BLUE);
                else
                    btnKnown.setColor(Color.WHITE);

                //TODO: card change
                changeWordCards();
            }
        });

    }

    public void changeWordCards(){
//        wordsTable.clear();
//        for (Word word:DataHolder.dataholder.wordLists.get(DataHolder.dataholder.SELECTED_LIST_INDEX).words) {
//            if (showStars) {
//                if (word.star) {
//                    wordsTable.add(new MainWordCard(word,
//                            showExample, showTurkish, showAll, this)).pad(3).row();
//                }
//            }else {
//                if (!(!showKnown && DataHolder.dataholder.knownWords.contains(word.name,false))) {
//                    wordsTable.add(new MainWordCard(word,
//                            showExample, showTurkish, showAll, this)).pad(3).row();
//                }
//            }
//        }
//        wordListOptions.setVisible(true);
    }

    public void addWordToListClicked(){
//        AddWordWindow addWordWindow = new AddWordWindow(DataHolder.dataholder.wordLists.get(DataHolder.dataholder.SELECTED_LIST_INDEX),
//                WordListsMainActor.this);
//        ShowWindow window = new ShowWindow(addWordWindow);
//        getStage().addActor(window);
    }

    public void removeListClicked(){
        DataHolder.dataholder.removeListFile(DataHolder.dataholder.SELECTED_LIST_INDEX);
        topListsArray.removeIndex(DataHolder.dataholder.SELECTED_LIST_INDEX);


        fillTopListLabelsTable();

        DataHolder.dataholder.SELECTED_LIST_INDEX = 0;
        selectedIndexChanged();
    }

    private void createBottomEmptyActors() {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = Fonts.bl_bold_45;
        noListWarningLabel = new Label("You don't have any word list.",labelStyle);

        noWordsInListGroup = new Group();
        noWordsInListGroup.setWidth(topListsWidth);
        Label label = new Label("List is empty",labelStyle);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = Fonts.bl_bold_45;
        textButtonStyle.fontColor = Color.WHITE;
        textButtonStyle.downFontColor = Color.DARK_GRAY;
            textButtonStyle.overFontColor = Color.LIGHT_GRAY;

        TextButton btnAddWord = new TextButton("Add Word",textButtonStyle);
        TextButton btnRemoveList = new TextButton("Remove List",textButtonStyle);

        btnRemoveList.setPosition(noWordsInListGroup.getWidth() / 2 - btnRemoveList.getPrefWidth() / 2,0);
        btnAddWord.setPosition(noWordsInListGroup.getWidth() / 2 - btnAddWord.getPrefWidth() / 2,
                btnRemoveList.getHeight() + 100);
        label.setPosition(noWordsInListGroup.getWidth() / 2 - label.getPrefWidth() / 2,
                btnAddWord.getY() + btnAddWord.getPrefHeight() + 300);
        noWordsInListGroup.setHeight(label.getPrefHeight() + label.getY());

        noWordsInListGroup.addActor(label);
        noWordsInListGroup.addActor(btnAddWord);
        noWordsInListGroup.addActor(btnRemoveList);

        btnRemoveList.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                removeListClicked();
            }
        });

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
        wordsTable.setPosition(AppInfo.PADDING * 2,AppInfo.PADDING * 2);

        wordCardsScrollPane = new SlowScrollPane(null, scrollPaneStyle);
        wordCardsScrollPane.setScrollingDisabled(true,false);
        wordCardsScrollPane.setVariableSizeKnobs(true);
        wordCardsScrollPane.setOverscroll(false, true);
        wordCardsScrollPane.setSize(width - 90,  height - topListsHeight - 5 * AppInfo.PADDING);
        wordCardsScrollPane.setPosition(AppInfo.PADDING * 2,AppInfo.PADDING * 2);
        wordCardsScrollPane.setVelocityY(wordCardsScrollPane.getVelocityY() / 100);
        wordCardsScrollPane.setActor(wordsTable);
    }

    private void createTopCreateListGroup() {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = Fonts.bl_bold_26;

        Label label1 = new Label("Title : ",labelStyle);
        label1.setSize(label1.getPrefWidth(),label1.getPrefHeight());

        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        Sprite cursorSprite = new Sprite(Assets.assets.pix);
        cursorSprite.setSize(2,100);
        cursorSprite.setColor(Color.BLACK);
        textFieldStyle.cursor = new SpriteDrawable(cursorSprite);
        textFieldStyle.font = Fonts.bl_bold_26;
        Sprite selectionSprite = new Sprite(Assets.assets.pix);
        selectionSprite.setColor(Color.valueOf("007f7f"));
        textFieldStyle.selection = new SpriteDrawable(selectionSprite);
        Sprite bgSprite = new Sprite(Assets.assets.pix);
        bgSprite.setColor(Color.LIGHT_GRAY);
        textFieldStyle.background = new SpriteDrawable(bgSprite);
        textFieldStyle.fontColor = Color.BLACK;

        topCreateListTextField = new TextField("", textFieldStyle);
        topCreateListTextField.setAlignment(Align.center);
        topCreateListTextField.setWidth(400);

        topCreateListTextField.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char key) {
                if ((key == '\r' || key == '\n')){
                    btnCreateListClicked();
                }
            }
        });

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = Fonts.bl_bold_32;
        textButtonStyle.fontColor = Color.WHITE;
        textButtonStyle.downFontColor = Color.DARK_GRAY;
        textButtonStyle.overFontColor = Color.LIGHT_GRAY;

        TextButton btnCreateList = new TextButton("Create", textButtonStyle);
        TextButton btnTurnBack = new TextButton("Back",textButtonStyle);

        label1.setPosition(45,0);
        topCreateListTextField.setPosition(label1.getX() + label1.getWidth() + 60,0);
        btnCreateList.setPosition(topCreateListTextField.getX() + topCreateListTextField.getWidth() + 100,-5);
        btnTurnBack.setPosition(topListsWidth - btnTurnBack.getPrefWidth() - 40,-5);

        topCreateListGroup = new Group();
        topCreateListGroup.addActor(label1);
        topCreateListGroup.addActor(topCreateListTextField);
        topCreateListGroup.addActor(btnCreateList);
        topCreateListGroup.addActor(btnTurnBack);

        topCreateListGroup.setPosition(topListsGroup.getX(),topListsGroup.getY() + 15);
        topCreateListGroup.setVisible(false);

        btnTurnBack.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                topCreateListGroup.setVisible(false);
                topListsGroup.setVisible(true);
                topCreateListTextField.setText("");
                getStage().setKeyboardFocus(null);
            }
        });

        btnCreateList.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                btnCreateListClicked();
            }
        });

    }

    private void btnCreateListClicked() {
        if (topCreateListTextField.getText().equals(""))
            return;

        createNewList(topCreateListTextField.getText());

        topCreateListGroup.setVisible(false);
        topListsGroup.setVisible(true);
        topCreateListTextField.setText("");
        topCreateListTextField.setFocusTraversal(false);
        getStage().setKeyboardFocus(null);
        selectedIndexChanged();
    }
    private void createNewList(String name) {
        WordList wordList = new WordList();
        wordList.listName = name;

        DataHolder.dataholder.wordLists.add(wordList);


        //CreateLabel
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = Fonts.bl_bold_32;
        final Label label = new Label(wordList.listName,labelStyle);
        //Add it to array and group
        topListsArray.add(label);
        fillTopListLabelsTable();


        {
            DataHolder.dataholder.SELECTED_LIST_INDEX = topListsArray.indexOf(label,true);
            for (Label otherLabels:topListsArray) {
                otherLabels.setColor(Color.WHITE);
            }
            topListsArray.get(DataHolder.dataholder.SELECTED_LIST_INDEX).setColor(ColorPalette.text1);
        }

        label.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                DataHolder.dataholder.SELECTED_LIST_INDEX = topListsArray.indexOf(label,true);
                for (Label label:topListsArray) {
                    label.setColor(Color.WHITE);
                }
                topListsArray.get(DataHolder.dataholder.SELECTED_LIST_INDEX).setColor(ColorPalette.text1);
                selectedIndexChanged();
            }
        });

    }

    private void fillTopListLabelsTable() {
        //Find group
        ScrollPane scrollPane = (ScrollPane) topListsGroup.getChild(0);
//        Group labelGroup = (Group) scrollPane.getActor();
        Table table = (Table) scrollPane.getActor();

        table.clear();
        for (Label label : topListsArray) {
            table.add(label).width(label.getPrefWidth()).padLeft(10).padRight(20);
        }
    }

    public void checkListAndFillTable() {
        if(topListsArray.size == 0){
            wordsTable.clear();
            wordsTable.add(noListWarningLabel).expandX().expandY();
            wordListOptions.setVisible(false);
        }else if (DataHolder.dataholder.wordLists.get(DataHolder.dataholder.SELECTED_LIST_INDEX).words == null ||
                DataHolder.dataholder.wordLists.get(DataHolder.dataholder.SELECTED_LIST_INDEX).words.size == 0){
            wordsTable.clear();
            wordsTable.add(noWordsInListGroup).expandY().expandX();
            wordListOptions.setVisible(false);
        }else {
            wordsTable.clear();
//            for (Word word:DataHolder.dataholder.wordLists.get(DataHolder.dataholder.SELECTED_LIST_INDEX).words) {
//                wordsTable.add(new MainWordCard(word,
//                        showExample,showTurkish,showAll,this)).pad(3).row();
//            }
            if (showStars){
                showStars = false;
                wordListOptions.getChild(5).setColor(Color.WHITE);
            }
            wordListOptions.setVisible(true);
        }
    }
    private void createTopLists() {

        //Top Lists Scroll Pane
        scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        Texture pix = new Texture("pix.png");

        Sprite scroll = new Sprite(pix);
        Sprite knob = new Sprite(pix);

        scroll.setSize(5,100);
        knob.setSize(20,20);
        knob.setColor(Color.RED);
        scrollPaneStyle.vScroll = new SpriteDrawable(scroll);
        scrollPaneStyle.vScrollKnob = new SpriteDrawable(knob);
        ScrollPane topLabelListScrollPane = new ScrollPane(null, scrollPaneStyle);
        topLabelListScrollPane.setVariableSizeKnobs(true);
        topLabelListScrollPane.setOverscroll(false, false);
        topLabelListScrollPane.setSize(topListsWidth - 120,topListsHeight);
        topLabelListScrollPane.setPosition(0,0);

        topListsArray = new Array<>();
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = Fonts.bl_bold_32;


        for (int i = 0; i < DataHolder.dataholder.wordLists.size; i++) {
            final Label label = new Label(DataHolder.dataholder.wordLists.get(i).listName,labelStyle);
            topListsArray.add(label);
            if(i ==DataHolder.dataholder.SELECTED_LIST_INDEX)
                label.setColor(ColorPalette.text1);

            label.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    DataHolder.dataholder.SELECTED_LIST_INDEX = topListsArray.indexOf(label,true);
                    for (Label label:topListsArray) {
                        label.setColor(Color.WHITE);
                    }
                    topListsArray.get(DataHolder.dataholder.SELECTED_LIST_INDEX).setColor(ColorPalette.text1);
                    selectedIndexChanged();
                }
            });
        }

        Table table = new Table();
        table.left();
        table.setHeight(topLabelListScrollPane.getHeight());
        for (Label label : topListsArray) {
            table.add(label).width(label.getPrefWidth()).padLeft(10).padRight(20);
        }
        topLabelListScrollPane.setActor(table);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = Fonts.bl_bold_72;
        textButtonStyle.fontColor = Color.WHITE;
        textButtonStyle.downFontColor = Color.DARK_GRAY;
        textButtonStyle.overFontColor = Color.LIGHT_GRAY;

        TextButton btnCreateNewList = new TextButton("+",textButtonStyle);
        btnCreateNewList.setPosition(topLabelListScrollPane.getX() + topLabelListScrollPane.getWidth() + 50,
                topLabelListScrollPane.getY() - 10);

        topListsGroup = new Group();
        topListsGroup.addActor(topLabelListScrollPane);
        topListsGroup.addActor(btnCreateNewList);
        topListsGroup.setPosition(topListImage.getX() + 15,topListImage.getY());

        btnCreateNewList.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                topListsGroup.setVisible(false);
                topCreateListGroup.setVisible(true);
                getStage().setKeyboardFocus(topCreateListTextField);
            }
        });
    }
    private void selectedIndexChanged() {

        if(topListsArray.size != 0) {
            for (Label otherLabels : topListsArray) {
                otherLabels.setColor(Color.WHITE);
            }
            topListsArray.get(DataHolder.dataholder.SELECTED_LIST_INDEX).setColor(ColorPalette.text1);
        }
        checkListAndFillTable();
    }
    private void addActors() {
        addActor(backgroundImage);
        addActor(topListImage);
        addActor(botListImage);
        addActor(topListsGroup);
        addActor(topCreateListGroup);
        addActor(wordCardsScrollPane);
        addActor(wordListOptions);
    }

    public void removeWord(Word word,MainWordCard card){
        DataHolder.dataholder.wordLists.get(DataHolder.dataholder.SELECTED_LIST_INDEX).words.removeValue(word,true);
        DataHolder.dataholder.saveList(DataHolder.dataholder.wordLists.get(DataHolder.dataholder.SELECTED_LIST_INDEX));
        wordsTable.removeActor(card);
        Array<Actor> actors = wordsTable.getChildren();
        wordsTable.clear();
        for (Actor a:actors) {
            wordsTable.add(a).pad(3).row();
        }

        if (wordsTable.getChildren().size == 0)
            checkListAndFillTable();

    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }
}
