package com.serhatmerak.wordlist.huds;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
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
import com.serhatmerak.wordlist.helpers.Fonts;
import com.serhatmerak.wordlist.helpers.ListHolder;

import javax.xml.soap.Text;

public class cakmaWordListMain extends Group {

    private float width = 1200;
    private float height = 1060;
    private float mainBorder = 4,insideBorder = 2;
    private float listsHeight = 60, listsWidth = width - AppInfo.PADDING * 2;
    private float wordsHeight = height - 3 * AppInfo.PADDING - listsHeight;

    private BorderImage backgroundImage,listImage,wordsImage;

    Array<Label> listsList;
    private int SELECTED_LIST_INDEX = 0;
    ScrollPane listScrollPane,wordCardsScrollPane;

    private Table wordsTable;
    ;
    private Group addFileGroup,writeWordOrLinkGroup;
    Label emptyListDefLabel;
    private Actor empty100Pad;

    public Group wordOrLinkGroup;
    public TextField wordOrLinkField;
    public TextButton btnAddWordWithName;


    public cakmaWordListMain(){
        createActors();
        createListScrollPane();
        createListsList();
        createCardsListScrollPane();
        createEmptyListActors();
        createWordTable();

        ifCardsExist();


    }

    private void createEmptyListActors() {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = Fonts.bl_bold_32;

        Label addFileLabel = new Label("Choose File",labelStyle);
        addFileLabel.setWidth(600);
        addFileLabel.setWrap(true);

        Label writeWordOrLinkLabel = new Label("Write a word or copy a cambridge dictionary link",labelStyle);
        writeWordOrLinkLabel.setWidth(600);
        writeWordOrLinkLabel.setWrap(true);

        emptyListDefLabel = new Label("List is empty\nSelect your text file which is filled with your words \n" +
                "(If you want to add a phrasal word,use copying link)",labelStyle);
        emptyListDefLabel.setAlignment(Align.center);
        emptyListDefLabel.setWrap(true);

        Label iconLabel = new Label(":",labelStyle);
        iconLabel.setPosition(600,0);

        Label iconLabel2 = new Label(":",labelStyle);
        iconLabel2.setPosition(600,0);

        writeWordOrLinkGroup = new Group();
        writeWordOrLinkGroup.addActor(writeWordOrLinkLabel);
        writeWordOrLinkGroup.addActor(iconLabel2);

        addFileGroup = new Group();
        addFileGroup.addActor(addFileLabel);
        addFileGroup.addActor(iconLabel);

        empty100Pad = new Actor();
        empty100Pad.setSize(100,100);

        Texture pix = new Texture("pix.png");

        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = Fonts.bl_bold_26;
        Sprite bgSprite = new Sprite(pix);
        bgSprite.setColor(Color.LIGHT_GRAY);
        textFieldStyle.background = new SpriteDrawable(bgSprite);
        textFieldStyle.fontColor = Color.BLACK;


        Sprite cursorSprite = new Sprite(pix);
        cursorSprite.setSize(2,100);
        cursorSprite.setColor(Color.BLACK);
        textFieldStyle.cursor = new SpriteDrawable(cursorSprite);
        Sprite selectionSprite = new Sprite(new Texture("pix.png"));
        selectionSprite.setColor(Color.valueOf("007f7f"));
        textFieldStyle.selection = new SpriteDrawable(selectionSprite);

        wordOrLinkField = new TextField("",textFieldStyle);
        wordOrLinkField.setSize(450,50);
        wordOrLinkField.setAlignment(Align.center);



        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = Fonts.bl_bold_26;
        textButtonStyle.fontColor = Color.WHITE;
        textButtonStyle.overFontColor = Color.LIGHT_GRAY;
        textButtonStyle.downFontColor = Color.DARK_GRAY;

        btnAddWordWithName = new TextButton("Add word",textButtonStyle);
        btnAddWordWithName.setSize(btnAddWordWithName.getPrefWidth(),btnAddWordWithName.getPrefHeight());
        btnAddWordWithName.setPosition((450 - btnAddWordWithName.getWidth()) / 2,0);

        wordOrLinkField.setPosition(0,btnAddWordWithName.getPrefHeight());

        wordOrLinkGroup = new Group();
        wordOrLinkGroup.addActor(wordOrLinkField);
        wordOrLinkGroup.addActor(btnAddWordWithName);
        wordOrLinkGroup.setSize(450,50 + btnAddWordWithName.getPrefHeight());

        btnAddWordWithName.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

            }
        });

    }

    private void ifCardsExist() {
        // kart var yoksa başka ekran
        if(false){
            wordsTable.clear();
            for (MainWordCard card: ListHolder.cards) {
                wordsTable.add(card).pad(3).row();
            }
        }else {
            wordsTable.clear();
//            wordsTable.add(empty100Pad).left().row();
//            wordsTable.add(emptyListDefLabel).width(1200).colspan(2).row();
//            wordsTable.add(empty100Pad).expandX().fillX().row();
//            wordsTable.add(empty100Pad).expandX().fillX().row();
//            wordsTable.add(addFileGroup).expandX().fillX().row();
//            wordsTable.add(empty100Pad).colspan(2).row();
//            wordsTable.add(empty100Pad).colspan(2).row();
//            wordsTable.add(writeWordOrLinkGroup).width(600);
//            wordsTable.add(wordOrLinkField).row();
            wordsTable.add(empty100Pad).expandX().colspan(2).row();
            wordsTable.add(emptyListDefLabel).colspan(2).expandX().row();
            wordsTable.add(empty100Pad).expandX().colspan(2).row();
            wordsTable.add(empty100Pad).expandX().colspan(2).row();
            wordsTable.add(writeWordOrLinkGroup).width(600).left().padLeft(30);
            wordsTable.add(wordOrLinkGroup).right().padBottom(20).padRight(15);



        }
    }

    private void createCardsListScrollPane() {
        ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        Texture pix = new Texture("pix.png");

        Sprite scroll = new Sprite(pix);
        Sprite knob = new Sprite(pix);
        scrollPaneStyle.vScroll = new SpriteDrawable(scroll);
        scrollPaneStyle.vScrollKnob = new SpriteDrawable(knob);

        wordCardsScrollPane = new ScrollPane(null, scrollPaneStyle);
        wordCardsScrollPane.setVariableSizeKnobs(true);
        wordCardsScrollPane.setOverscroll(false, false);
        wordCardsScrollPane.setSize(listsWidth,height - listsHeight - 5 * AppInfo.PADDING);
        wordCardsScrollPane.setPosition(AppInfo.PADDING * 2,AppInfo.PADDING * 2);

        addActor(wordCardsScrollPane);

    }

    private void createWordTable() {
        wordsTable = new Table();
//        wordsTable.defaults().pad(5);
//        wordsTable.setSize(listsWidth,height - listsHeight - 5 * AppInfo.PADDING);
        wordsTable.setWidth(listsWidth);
        wordsTable.top().left();
        wordsTable.setPosition(AppInfo.PADDING * 2,AppInfo.PADDING * 2);
        wordCardsScrollPane.setActor(wordsTable);
    }

    private void createListScrollPane() {
        ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        Texture pix = new Texture("pix.png");

        Sprite scroll = new Sprite(pix);
        Sprite knob = new Sprite(pix);
        scrollPaneStyle.vScroll = new SpriteDrawable(scroll);
        scrollPaneStyle.vScrollKnob = new SpriteDrawable(knob);
        listScrollPane = new ScrollPane(null, scrollPaneStyle);
        listScrollPane.setVariableSizeKnobs(true);
        listScrollPane.setOverscroll(false, false);
        listScrollPane.setSize(listsWidth - 75,listsHeight);
        listScrollPane.setPosition(listImage.getX() + 15,listImage.getY());
        addActor(listScrollPane);
    }

    private void createListsList() {
        listsList = new Array<>();
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = Fonts.bl_bold_26;
        float totalX = 0;

        for (int i = 0; i < 15; i++) {
            totalX += 30;
            final Label label = new Label("Liste Cat " + (i+1),labelStyle);
            label.setPosition(totalX ,7);
            listsList.add(label);
            totalX += label.getWidth();
            if(i ==0)
                label.setColor(Color.GOLD);

            label.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    SELECTED_LIST_INDEX = listsList.indexOf(label,true);
                    for (Label label:listsList) {
                        label.setColor(Color.WHITE);
                    }
                    listsList.get(SELECTED_LIST_INDEX).setColor(Color.GOLD);
                }
            });
        }

        Group groupOfLabelList = new Group();
        groupOfLabelList.setSize(totalX,listsHeight);
        for (Label label : listsList) {
            groupOfLabelList.addActor(label);
        }
        listScrollPane.setActor(groupOfLabelList);
    }

    private void createActors() {
        setSize(width , height);
        backgroundImage = new BorderImage(new Vector3(width,height,mainBorder));

        listImage = new BorderImage(new Vector3(listsWidth,listsHeight,insideBorder));
        listImage.setPosition(AppInfo.PADDING,height - AppInfo.PADDING - listsHeight);

        wordsImage = new BorderImage(new Vector3(listsWidth,wordsHeight,insideBorder));
        wordsImage.setPosition(AppInfo.PADDING,AppInfo.PADDING);

        addActor(backgroundImage);
        addActor(listImage);
        addActor(wordsImage);
    }
}
