package com.serhatmerak.wordlist.huds.wordcardhuds;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
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
import com.serhatmerak.wordlist.huds.BorderImage;
import com.serhatmerak.wordlist.word.MeaningDiv;
import com.serhatmerak.wordlist.word.Word;

public class StudyWordCard extends Group {

    private StudyWordCardStyle studyWordCardStyle;
    public Word word;

    private BorderImage bgImage;
    private final float width = 1170;
    private final float height = 780;

    private Table frontTable,backTable;
    private Actor listenerActor;
    private Group settingsGroup;

    private Group wordNameGroup;
    private Label wordDefLabel,turkishDefLabel;
    private Array<Label> examples;

    private Image btnDontShowAgain,btnStar,btnSave,btnShowCard,btnShowWordData;

    public boolean isAnswer = false;
    private StudyWordCardListener studyWordCardListener;

    private Image goldImg;


    public StudyWordCard(Word word,StudyWordCardStyle studyWordCardStyle) {
        this.word = word;
        this.studyWordCardStyle = studyWordCardStyle;
        createActors();

    }

    private void createActors() {
        bgImage = new BorderImage(new Vector3(width,height,10), Color.valueOf("c3cad5"),
                Color.valueOf("0a0c0f"));
        bgImage.addActor(bgImage.rightBorder);
        bgImage.rightBorder.setColor(Color.valueOf("0a0c0f"));
        setSize(width,height);

        addActor(bgImage);

        createTopButtons();
        createTableActors();
        createFrontTable();
        createBackTable();
        listenerActor = new Actor();
        listenerActor.setSize(frontTable.getWidth(),frontTable.getHeight());
        listenerActor.setPosition(frontTable.getX(),frontTable.getY());

        ClickListener changeListener = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                changeCard();
            }
        };

        listenerActor.addListener(changeListener);
        addActor(listenerActor);

        Sprite sprite = new Sprite(Assets.assets.gold);
        goldImg = new Image(sprite);
        goldImg.setWidth(50);
        goldImg.setOrigin(Align.center);
        goldImg.setSize(goldImg.getWidth(),bgImage.getWidth() - 20);
        goldImg.setOrigin(Align.bottomRight);
        goldImg.setRotation(-90);
        goldImg.setPosition(-40,10);
        addActor(goldImg);
        goldImg.setVisible(false);

    }

    private void createTableActors() {
        {
            Label.LabelStyle labelStyle = new Label.LabelStyle();
            labelStyle.font = Fonts.bl_bold_96;
            wordNameGroup = new Group();
            Label wordLabel = new Label(word.meaningDivs.first().title, labelStyle);
            wordLabel.setHeight(height - 4 * AppInfo.PADDING - settingsGroup.getHeight() - 100);
            wordLabel.setWidth(width - 4 * AppInfo.PADDING);
            wordLabel.setWrap(true);
            wordLabel.setAlignment(Align.center);
            wordLabel.setColor(Color.BLACK);
            wordNameGroup.addActor(wordLabel);
            wordNameGroup.setOrigin(Align.center);
            wordNameGroup.setSize(wordLabel.getWidth(), wordLabel.getHeight());

        }
        {
            Label.LabelStyle labelStyle = new Label.LabelStyle();
            labelStyle.font = Fonts.bl_bold_45;

            wordDefLabel = new Label(word.meaningDivs.first().engDef, labelStyle);
            wordDefLabel.setColor(Color.BLACK);
            wordDefLabel.setWidth(width - 5 * AppInfo.PADDING);
            wordDefLabel.setAlignment(Align.center);
            wordDefLabel.setWrap(true);

        }
        {
            if (studyWordCardStyle.SHOW_TURKISH){
                Label.LabelStyle labelStyle = new Label.LabelStyle();
                labelStyle.font = Fonts.bl_bold_32;

                turkishDefLabel = new Label(word.meaningDivs.first().trDef, labelStyle);
                turkishDefLabel.setColor(Color.valueOf("3c6ffa"));
                turkishDefLabel.setWidth(width - 4 * AppInfo.PADDING);
                turkishDefLabel.setWrap(true);
                turkishDefLabel.setHeight(turkishDefLabel.getPrefHeight());
            }
        }
        {
            examples = new Array<>();

            if (studyWordCardStyle.SHOW_EXAMPLES){
                for (int i = 0; i < word.meaningDivs.first().examples.length; i++) {
                    Label.LabelStyle labelStyle = new Label.LabelStyle();
                    labelStyle.font = Fonts.bl_bold_26;

                    Label label = new Label(word.meaningDivs.first().examples[i], labelStyle);
                    label.setColor(Color.BLACK);
                    label.setWidth(width - 4 * AppInfo.PADDING);
                    label.setWrap(true);
                    examples.add(label);
                }

            }
        }
    }

    private void createTopButtons() {
        btnDontShowAgain = new Image(Assets.assets.tick);
        btnStar = new Image(Assets.assets.star);
        btnSave = new Image(Assets.assets.save);

        btnShowWordData = new Image(Assets.assets.showWordData);
        btnShowCard = new Image(Assets.assets.showCard);


        btnSave.setSize((36 / btnSave.getHeight()) * btnSave.getWidth(),36);
        btnShowWordData.setSize((36 / btnShowWordData.getHeight()) * btnShowWordData.getWidth(),36);
        btnShowCard.setSize((36 / btnShowCard.getHeight()) * btnShowCard.getWidth(),36);

        if (!word.star)
            btnStar.setColor(Color.BLACK);
        else
            btnStar.setColor(Color.GOLD);

        if (!DataHolder.dataholder.isThisWordFav(word))
            btnSave.setColor(Color.BLACK);
        else
            btnSave.setColor(ColorPalette.topMenuBg);


        if (!DataHolder.dataholder.knownWords.contains(word.name,false))
            btnDontShowAgain.setColor(Color.BLACK);
        else
            btnDontShowAgain.setColor(Color.BLUE);


        btnSave.setPosition(0,0);
        btnDontShowAgain.setPosition(btnSave.getWidth() + AppInfo.PADDING * 3,0);
        btnStar.setPosition(btnDontShowAgain.getX() + btnDontShowAgain.getWidth()
                + AppInfo.PADDING * 3,0);


        btnStar.setOrigin(Align.center);
        btnDontShowAgain.setOrigin(Align.center);
        btnSave.setOrigin(Align.center);
        btnShowCard.setOrigin(Align.center);
        btnShowWordData.setOrigin(Align.center);



        settingsGroup =  new Group();
        settingsGroup.setSize(width - 4 * AppInfo.PADDING,btnDontShowAgain.getPrefHeight());
        settingsGroup.setPosition(AppInfo.PADDING * 2,height - 2 * AppInfo.PADDING - settingsGroup.getHeight());
        settingsGroup.addActor(btnDontShowAgain);
        settingsGroup.addActor(btnStar);
        settingsGroup.addActor(btnSave);
        settingsGroup.addActor(btnShowCard);
        settingsGroup.addActor(btnShowWordData);
        addActor(settingsGroup);

        btnShowWordData.setPosition(settingsGroup.getWidth() - btnShowWordData.getWidth(),0);
        btnShowCard.setPosition(btnShowWordData.getX() - 3 * AppInfo.PADDING
                - btnShowCard.getWidth(),0);


        btnDontShowAgain.addListener(new ButtonHoverAnimation(btnDontShowAgain) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                dontShowClicked();
            }
        });

        btnStar.addListener(new ButtonHoverAnimation(btnStar){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                starClicked();
            }
        });

        btnSave.addListener(new ButtonHoverAnimation(btnSave,true){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                saveClicked();
            }
        });

        btnShowWordData.addListener(new ButtonHoverAnimation(btnShowWordData,true){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if (studyWordCardListener != null)
                    studyWordCardListener.showWordDataClicked();
            }
        });

        btnShowCard.addListener(new ButtonHoverAnimation(btnShowCard,true){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if (studyWordCardListener != null)
                    studyWordCardListener.showCardClicked();
            }
        });


    }

    public void saveClicked(){
        if (DataHolder.dataholder.isThisWordFav(word)) {
            DataHolder.dataholder.removeWordFromFavorites(word);
            btnSave.setColor(Color.BLACK);
        }else {
            btnSave.setColor(ColorPalette.topMenuBg);
            DataHolder.dataholder.favList.words.add(word);
            DataHolder.dataholder.saveList(DataHolder.dataholder.favList);

        }
    }

    public void dontShowClicked() {
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
    }

    public void starClicked(){
        word.star = !word.star;
        if (!word.star)
            btnStar.setColor(Color.BLACK);
        else
            btnStar.setColor(Color.GOLD);
        DataHolder.dataholder.saveList(DataHolder.dataholder.getSelectedList());
    }

    private void createFrontTable() {
        frontTable = new Table();
        frontTable.setSize(width - 4 * AppInfo.PADDING,
                height - AppInfo.PADDING * 3 - settingsGroup.getHeight());
        frontTable.setPosition(AppInfo.PADDING,AppInfo.PADDING);
        addActor(frontTable);

        frontTable.add(wordNameGroup).expandX().padBottom(100);


    }

    private void createBackTable() {

        backTable = new Table();
        backTable.setSize(width - 2 * AppInfo.PADDING,
                height - AppInfo.PADDING * 3 - settingsGroup.getHeight());
        backTable.setPosition(AppInfo.PADDING,AppInfo.PADDING);
        backTable.center();

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = Fonts.bl_bold_32;
//

        if (!studyWordCardStyle.SHOW_EXAMPLES && !studyWordCardStyle.SHOW_TURKISH)
            backTable.add(wordDefLabel).width(wordDefLabel.getWidth()).height(wordDefLabel.getPrefHeight()).
                    padLeft(AppInfo.PADDING * 1.5f).center().padBottom(100);
        else
            backTable.add(wordDefLabel).width(wordDefLabel.getWidth()).height(wordDefLabel.getPrefHeight()).
                    padLeft(AppInfo.PADDING * 1.5f).center().padBottom(2 * AppInfo.PADDING).row();


        if (studyWordCardStyle.SHOW_TURKISH){
            backTable.add(turkishDefLabel).width(turkishDefLabel.getWidth()).height(turkishDefLabel.getHeight()).
                    padLeft(AppInfo.PADDING).row();
        }

        if (studyWordCardStyle.SHOW_EXAMPLES){
            for (Label label:examples){
                backTable.add(label).width(label.getWidth()).height(label.getHeight()).
                        padLeft(AppInfo.PADDING).padTop(AppInfo.PADDING).row();

            }
        }

//        addActor(backTable);



    }

    public void changeCard(){
        if (frontTable.getParent() == null){
            backTable.remove();
            addActor(frontTable);
        }else {
            frontTable.remove();
            addActor(backTable);
        }
        isAnswer = !isAnswer;
        listenerActor.toFront();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (word.star) {
//            if (!bgImage.rightBorder.getColor().equals(Color.GOLD))
//                bgImage.rightBorder.setColor(Color.GOLD);
            if (!goldImg.isVisible())
                goldImg.setVisible(true);
        } else if (DataHolder.dataholder.knownWords.contains(word.name, false)) {
            if (!bgImage.rightBorder.getColor().equals(Color.BLUE))
                bgImage.rightBorder.setColor(Color.BLUE);
        } else {
            if (!bgImage.rightBorder.getColor().equals(Color.valueOf("0a0c0f"))) {
                bgImage.rightBorder.setColor(Color.valueOf("0a0c0f"));
            }
        }
    }

    public void disableButtons() {
        btnDontShowAgain.setTouchable(Touchable.disabled);
        btnStar.setTouchable(Touchable.disabled);
        btnSave.setTouchable(Touchable.disabled);
        btnShowWordData.setTouchable(Touchable.disabled);
        btnShowCard.setTouchable(Touchable.disabled);
    }

    public void setListener(StudyWordCardListener studyWordCardListener){
        this.studyWordCardListener = studyWordCardListener;
    }




    public static class StudyWordCardStyle{
//        public int SELECTED_TYPE = 0;
//        /*
//         * 0 = Name - Definition
//         * 1 = Definition - Name
//         * 2 = Custom Word Cards
//         */
//
//        public int FOR_WORDS = 0;
//
//        public static final int UNKNOWN_WORDS = 0;
//        public static final int ALL_WORDS = 1;
//        public static final int STARRY_WORDS = 2;
//        /*
//         * 0 = UnknownWords
//         * 1 = All Words
//         * 2 = Starry Words
//         */
//
//        public boolean showTurkish = true;
//        public boolean showExamples = true;
////        public boolean showAllDefinitions;

        /////////// NEW
        public boolean EVERY_WORD = true;
        public boolean SHOW_TURKISH = false;
        public boolean SHOW_EXAMPLES = false;    }

    public interface StudyWordCardListener{
        void showCardClicked();
        void showWordDataClicked();
    }
}
