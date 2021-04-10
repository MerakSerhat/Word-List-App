package com.serhatmerak.wordlist.huds;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.serhatmerak.wordlist.helpers.AppInfo;
import com.serhatmerak.wordlist.helpers.Assets;
import com.serhatmerak.wordlist.helpers.ButtonHoverAnimation;
import com.serhatmerak.wordlist.helpers.ColorPalette;
import com.serhatmerak.wordlist.helpers.DataHolder;
import com.serhatmerak.wordlist.helpers.Fonts;
import com.serhatmerak.wordlist.huds.wordcardhuds.WordDataHud;
import com.serhatmerak.wordlist.word.MeaningDiv;
import com.serhatmerak.wordlist.word.Word;

public class MainWordCard extends Group {

    private Table table;
    public Word word;
    private Group settingsGroup;
    private Image goldImage;
    Actor pad;

    private boolean TypeWord = true;
    BorderImage borderImage;

    boolean examplesOn;
    boolean turkishOn;
    boolean otherDivsOn;

    private Image wordDataHighlighter;
    private Image settingsHighlighter;


    public MainWordCard(final Word word,boolean examplesOn,boolean turkishOn,boolean otherDivsOn, float width){
        this.word = word;
        this.examplesOn = examplesOn;
        this.turkishOn = turkishOn;
        this.otherDivsOn = otherDivsOn;

        init(width);
    }

    public MainWordCard createButtonHighlighter() {
        wordDataHighlighter = new Image(Assets.assets.pix);
        settingsHighlighter = new Image(Assets.assets.pix);

        wordDataHighlighter.setSize(getWidth(),table.getChildren().first().getHeight());
        settingsHighlighter.setSize(getWidth(),getHeight() - wordDataHighlighter.getHeight());

        settingsHighlighter.setColor(ColorPalette.LightTopBlue.r,
                ColorPalette.LightTopBlue.g,ColorPalette.LightTopBlue.b,0f);
        wordDataHighlighter.setColor(ColorPalette.ButtonBlue.r,ColorPalette.ButtonBlue.g
                ,ColorPalette.ButtonBlue.b,0f);

        wordDataHighlighter.setPosition(0,settingsHighlighter.getHeight());

        wordDataHighlighter.setTouchable(Touchable.enabled);
        settingsHighlighter.setTouchable(Touchable.enabled);

        addActor(wordDataHighlighter);
        addActor(settingsHighlighter);


//        wordDataHighlighter.addAction(Actions.sequence(
//                Actions.delay(0.8f),
//                Actions.alpha(0.4f,0.8f, Interpolation.fastSlow),
//                Actions.alpha(0.2f,0.8f,Interpolation.slowFast),
//                Actions.alpha(0.4f,0.8f,Interpolation.slowFast),
//                Actions.alpha(0f,1.6f,Interpolation.slowFast)
//        ));
//        settingsHighlighter.addAction(Actions.sequence(
//                Actions.delay(0.8f),
//                Actions.alpha(0.4f,0.8f, Interpolation.fastSlow),
//                Actions.alpha(0.2f,0.8f,Interpolation.slowFast),
//                Actions.alpha(0.4f,0.8f,Interpolation.slowFast),
//                Actions.alpha(0f,1.6f,Interpolation.slowFast)
//        ));

        settingsHighlighter.addListener(new ClickListener(){
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                settingsHighlighter.addAction(Actions.alpha(0.15f,0.2f,Interpolation.fastSlow));
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                settingsHighlighter.addAction(Actions.alpha(0f,0.2f,Interpolation.slowFast));

            }
        });

        wordDataHighlighter.addListener(new ClickListener(){
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                wordDataHighlighter.addAction(Actions.alpha(0.15f,0.2f,Interpolation.fastSlow));
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                wordDataHighlighter.addAction(Actions.alpha(0f,0.2f,Interpolation.slowFast));

            }

            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                WordDataHud wordDataHud = new WordDataHud(word);
                final ShowWindow showWindow = new ShowWindow(wordDataHud);
                getStage().addActor(showWindow);
                showWindow.img.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        super.clicked(event, x, y);
                        showWindow.remove();
                    }
                });
            }
        });

        return this;
    }

    public void init(float width){
        table = new Table();
        table.setWidth(width);
//        table.setTouchable(Touchable.disabled);
        table.left().pad(10).top();
        pad = new Actor();
        pad.setSize(15,15);

        createLabels();
        createTable(examplesOn,turkishOn,otherDivsOn);

        setSize(table.getWidth(),table.getHeight());

        borderImage = new BorderImage(new Vector3(table.getWidth(),table.getHeight(),0),Color.valueOf("c3cad5"),
                Color.valueOf("0a0c0f"));

        borderImage.addActor(borderImage.rightBorder);
        borderImage.rightBorder.setColor(Color.valueOf("0a0c0f"));
        addActor(borderImage);
        addActor(table);

        addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {

                super.clicked(event, x, y);
                if (TypeWord) {
                    table.clear();
                    //Todo Settings Table
                    createSettingsTable();
                    TypeWord = false;
                    if (settingsHighlighter.getStage() != null){
                        settingsHighlighter.setTouchable(Touchable.disabled);
                    }
                }
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);

                if (toActor == settingsGroup ||
                        toActor == (Image)((BorderImage)getChild(0)).getChild(0) ||
                        toActor == (Image)((BorderImage)getChild(0)).getChild(1) ||
                        toActor == (Table)getChild(1) ||
                        toActor == ((Image)settingsGroup.getChild(0)) ||
                        toActor == ((Image)settingsGroup.getChild(1)) ||
                        toActor == ((Image)settingsGroup.getChild(2)) ||
                        toActor == ((Image)settingsGroup.getChild(3)))
                    return;

                if (pointer == -1 && !TypeWord) {
                    table.clear();
                    createTable(MainWordCard.this.examplesOn,
                            MainWordCard.this.turkishOn,
                            MainWordCard.this.otherDivsOn);
                    if (settingsHighlighter.getStage() != null){
                        settingsHighlighter.setTouchable(Touchable.enabled);
                    }
                    TypeWord = true;
                }
            }
        });


        goldImage = new Image(Assets.assets.gold);
        goldImage.setSize(60,getHeight());
        goldImage.setPosition(getWidth() - goldImage.getWidth(),0);
        addActor(goldImage);

        if(!word.star){
            goldImage.setVisible(false);
        }


    };

    private void createSettingsTable() {
        table.add(word.meaningDivs.first().titleLabel).expandX().left().row();
        table.add(settingsGroup).left().padTop(AppInfo.PADDING).row();
    }

    private void createLabels() {
        Label.LabelStyle titleLabelStyle = new Label.LabelStyle();


        Label.LabelStyle engDefLabelStyle = new Label.LabelStyle();


        Label.LabelStyle trDefAndExampleLabelStyle = new Label.LabelStyle();


        if (DataHolder.dataholder.bigWordCards){
            titleLabelStyle.font = Fonts.bl_bold_52;
            engDefLabelStyle.font = Fonts.bl_bold_45;
            trDefAndExampleLabelStyle.font = Fonts.bl_bold_36;
        }else {
            titleLabelStyle.font = Fonts.bl_bold_36;
            engDefLabelStyle.font = Fonts.bl_bold_32;
            trDefAndExampleLabelStyle.font = Fonts.bl_bold_26;
        }

        word.meaningDivs.get(0).titleLabel = new Label("[BLACK]" + word.name + "[]",titleLabelStyle);

        for (MeaningDiv div:word.meaningDivs) {
            div.exampleLabels = new Array<>();
            Label engDefLabel = new Label("[BLACK]" + "+" + div.engDef + "[]",engDefLabelStyle);

            Label trDefLabel = new Label("[#3c6ffa]" + div.trDef + "[]",trDefAndExampleLabelStyle);

            div.engDefLabel = engDefLabel;
            div.trDefLabel = trDefLabel;

            engDefLabel.setWidth(table.getWidth() - 2 * AppInfo.PADDING);
            trDefLabel.setWidth(table.getWidth() - 2 * AppInfo.PADDING);
            engDefLabel.setWrap(true);
            trDefLabel.setWrap(true);



            Label collLabel = new Label("[BLACK]" + div.title + "[]", titleLabelStyle);
            div.titleLabel = collLabel;
            collLabel.setWidth(table.getWidth() - 2 * AppInfo.PADDING);
            collLabel.setWrap(true);

            for (String ex:div.examples) {
                Label exampleLabel = new Label("[BLACK] - " + ex + "[]",trDefAndExampleLabelStyle);
                exampleLabel.setWidth(table.getWidth() - 2 * AppInfo.PADDING);
                exampleLabel.setWrap(true);

                div.exampleLabels.add(exampleLabel);
                exampleLabel.setWrap(true);
            }

        }

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = Fonts.bl_bold_32;
        textButtonStyle.fontColor = Color.BLACK;
        textButtonStyle.downFontColor = Color.DARK_GRAY;
        textButtonStyle.disabledFontColor = Color.LIGHT_GRAY;

        Image btnRemove = new Image(Assets.assets.remove);
        final Image btnDontShowAgain = new Image(Assets.assets.tick);
        final Image btnStar = new Image(Assets.assets.star);
        final Image btnSave = new Image(Assets.assets.save);

        btnSave.setSize((36 / btnSave.getHeight()) * btnSave.getWidth(),36);

        btnRemove.setColor(Color.BLACK);
        btnDontShowAgain.setColor(Color.BLACK);
        btnSave.setColor(Color.BLACK);


        if (!word.star)
            btnStar.setColor(Color.BLACK);
        else
            btnStar.setColor(Color.GOLD);

        if (DataHolder.dataholder.isThisWordFav(word))
            btnSave.setColor(ColorPalette.topMenuBg);

        if (!DataHolder.dataholder.knownWords.contains(word.name,false))
            btnDontShowAgain.setColor(Color.BLACK);
        else
            btnDontShowAgain.setColor(Color.BLUE);

        btnRemove.setPosition(0,0);
        btnDontShowAgain.setPosition(btnRemove.getPrefWidth() + AppInfo.PADDING * 5,0);
        btnStar.setPosition(btnDontShowAgain.getPrefWidth() + AppInfo.PADDING * 5 + btnDontShowAgain.getX(),0);
        btnSave.setPosition(table.getWidth() - 5 * AppInfo.PADDING - btnSave.getPrefWidth(),0);

        btnRemove.setOrigin(Align.center);
        btnDontShowAgain.setOrigin(Align.center);
        btnStar.setOrigin(Align.center);
        btnSave.setOrigin(Align.center);

        settingsGroup =  new Group();
        settingsGroup.setSize(table.getWidth(),btnDontShowAgain.getPrefHeight());
        settingsGroup.addActor(btnRemove);
        settingsGroup.addActor(btnDontShowAgain);
        settingsGroup.addActor(btnStar);
        settingsGroup.addActor(btnSave);

        btnRemove.addListener(new ButtonHoverAnimation(btnRemove) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                WordListTable wordListTable = (WordListTable) getParent().getParent().getParent();
                wordListTable.removeWord(MainWordCard.this);

            }
        });

        btnDontShowAgain.addListener(new ButtonHoverAnimation(btnDontShowAgain) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
//                wordListMainActor.removeWord(word,MainWordCard.this);

                if (!DataHolder.dataholder.knownWords.contains(word.name,false)){

                    if (DataHolder.dataholder.firstKnownWords.contains(word.name,false) ||
                            DataHolder.dataholder.secondKnownWords.contains(word.name,false) ||
                            DataHolder.dataholder.thirdKnownWords.contains(word.name,false)){
                        DataHolder.dataholder.knownWords.add(word.name);
                        DataHolder.dataholder.saveKnownCustomWords();
                    }else {

                        DataHolder.dataholder.yourKnownWords.add(word.name);
                        DataHolder.dataholder.knownWords.add(word.name);
                        DataHolder.dataholder.saveYourKnownWords();
                    }
                }else {
                    if (DataHolder.dataholder.yourKnownWords.contains(word.name,false)){
                        DataHolder.dataholder.yourKnownWords.removeValue(word.name,false);
                        DataHolder.dataholder.knownWords.removeValue(word.name,false);
                        DataHolder.dataholder.saveYourKnownWords();
                    }else {

                        DataHolder.dataholder.knownWords.removeValue(word.name,false);
                        DataHolder.dataholder.saveKnownCustomWords();

                    }
                }

                if (!DataHolder.dataholder.knownWords.contains(word.name,false))
                    btnDontShowAgain.setColor(Color.BLACK);
                else
                    btnDontShowAgain.setColor(Color.BLUE);

//                if (!wordListMainActor.showKnown){
//                    wordListMainActor.changeWordCards();
//                }



            }
        });

        btnStar.addListener(new ButtonHoverAnimation(btnStar){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                word.star = !word.star;
                if (!word.star) {
                    btnStar.setColor(Color.BLACK);
                    goldImage.setVisible(false);
                }else {
                    btnStar.setColor(Color.GOLD);
                    goldImage.setVisible(true);
                }
                DataHolder.dataholder.saveList(DataHolder.dataholder.getSelectedList());
//                wordListMainActor.changeWordCards();
            }
        });

        btnSave.addListener(new ButtonHoverAnimation(btnSave,true){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if (DataHolder.dataholder.isThisWordFav(word)) {
                    DataHolder.dataholder.removeWordFromFavorites(word);
                    btnSave.setColor(Color.BLACK);
                }else {
                    btnSave.setColor(ColorPalette.topMenuBg);
                    DataHolder.dataholder.favList.words.add(word);
                    DataHolder.dataholder.saveList(DataHolder.dataholder.favList);

                }
            }
        });

//        word.meaningDivs.first().titleLabel.addListener(new ClickListener(){
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                super.clicked(event, x, y);
//                WordDataHud wordDataHud = new WordDataHud(word);
//                final ShowWindow showWindow = new ShowWindow(wordDataHud);
//                getStage().addActor(showWindow);
//                showWindow.img.addListener(new ClickListener() {
//                    @Override
//                    public void clicked(InputEvent event, float x, float y) {
//                        super.clicked(event, x, y);
//                        showWindow.remove();
//                    }
//                });
//            }
//        });
    }

    public void createTable(boolean examplesOn,boolean turkishOn,boolean otherDivsOn) {
        table.clear();

        this.examplesOn = examplesOn;
        this.turkishOn = turkishOn;
        this.otherDivsOn = otherDivsOn;

        int i=0;
        for (MeaningDiv meaningDiv : word.meaningDivs) {
            if(!otherDivsOn && i != 0) break;

            if (i== 0 || meaningDiv.isCollacation)
                table.add(meaningDiv.titleLabel).width(table.getWidth() - 2 * AppInfo.PADDING).left().row();

            table.add(meaningDiv.engDefLabel).width(table.getWidth() - 2 * AppInfo.PADDING).
                    height(meaningDiv.engDefLabel.getPrefHeight()).left().row();
            if (turkishOn)
                table.add(meaningDiv.trDefLabel).width(table.getWidth() - 2 * AppInfo.PADDING)
                        .height(meaningDiv.trDefLabel.getPrefHeight()).left().row();
            if (examplesOn)
                for (Label label:meaningDiv.exampleLabels) {
                    table.add(label).width(table.getWidth() - 2 * AppInfo.PADDING).width(table.getWidth() - 2 * AppInfo.PADDING)
                            .height(label.getPrefHeight()).left().row();
                }

            i++;
            table.add(pad).row();
        }

        table.setSize(table.getWidth(),table.getPrefHeight());
        if (borderImage != null) {
            borderImage.setHeight(table.getHeight());
        }
        setHeight(table.getHeight());

        if (goldImage != null)
            goldImage.setHeight(getHeight());

        if (settingsHighlighter != null) {
            settingsHighlighter.setHeight(getHeight() - wordDataHighlighter.getHeight());
            wordDataHighlighter.setPosition(0, settingsHighlighter.getHeight());
        }

    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (word.star){
            if (!borderImage.rightBorder.getColor().equals(Color.GOLD))
                borderImage.rightBorder.setColor(Color.GOLD);
        }else if (DataHolder.dataholder.knownWords.contains(word.name,false)){
            if (!borderImage.rightBorder.getColor().equals(Color.BLUE))
                borderImage.rightBorder.setColor(Color.BLUE);
        } else {
            if (!borderImage.rightBorder.getColor().equals(Color.valueOf("0a0c0f"))) {
                borderImage.rightBorder.setColor(Color.valueOf("0a0c0f"));
            }
        }
    }
}
