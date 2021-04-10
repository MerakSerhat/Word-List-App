package com.serhatmerak.wordlist.huds.wordcardhuds;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.serhatmerak.wordlist.helpers.AppInfo;
import com.serhatmerak.wordlist.helpers.Assets;
import com.serhatmerak.wordlist.helpers.DataHolder;
import com.serhatmerak.wordlist.helpers.Fonts;
import com.serhatmerak.wordlist.huds.Alert;
import com.serhatmerak.wordlist.huds.RoundedRectangle;
import com.serhatmerak.wordlist.screens.StudyCardScreen;

public class SettingsWordCards {
    public Group settingsGroup;
    StudyCardScreen studyCardScreen;

    private Stage stage;

    public boolean EVERY_WORD = true;
    public boolean SHOW_TURKISH = false;
    public boolean SHOW_EXAMPLES = false;

    private StudyWordCard frontCard;
    private TextButton btnCreateTest;
    private Array<Group> wordSelections;
    private Group everyWordsSelection;
    private Group turkishDefSelectionBox;
    private Group showExamplesBox;

    public SettingsWordCards(Stage stage, StudyCardScreen studyCardScreen){
        this.stage = stage;
        this.studyCardScreen = studyCardScreen;

        createSettingsGroup();
        createSmallCard();
    }

    private void createSettingsGroup() {
        Image bg = new Image(Assets.assets.pix);
        bg.setSize(1200 , AppInfo.HEIGHT - 300);

        Image titleBg = new Image(Assets.assets.pix);
        titleBg.setSize(bg.getWidth(),100);
        titleBg.setColor(Color.valueOf("4257b2"));

        settingsGroup = new Group();
        settingsGroup.setSize(bg.getWidth(),bg.getHeight());
        settingsGroup.setPosition(AppInfo.WIDTH / 2f - settingsGroup.getWidth() / 2,140);

        Label.LabelStyle titleStyle = new Label.LabelStyle();
        titleStyle.font = Fonts.bl_bold_36;

        Label titleLabel = new Label("Study Cards Settings",titleStyle);

        titleBg.setPosition(0,bg.getHeight() - titleBg.getHeight());
        titleLabel.setPosition(AppInfo.PADDING,
                bg.getHeight() - titleBg.getHeight() + (titleBg.getHeight() - titleLabel.getHeight()) / 2);


        Sprite btnBgNorm = new Sprite(Assets.assets.pix);
        btnBgNorm.setSize(bg.getWidth() - 200,80);
        btnBgNorm.setColor(Color.valueOf("3CCECE"));

        Sprite btnBgHover = new Sprite(Assets.assets.pix);
        btnBgHover.setSize(btnBgNorm.getWidth(),btnBgNorm.getHeight());
        btnBgHover.setColor(Color.valueOf("28A6A6"));

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = Fonts.bl_bold_36;
        textButtonStyle.up = new SpriteDrawable(btnBgNorm);
        textButtonStyle.over = new SpriteDrawable(btnBgHover);
        textButtonStyle.fontColor = Color.WHITE;

        btnCreateTest = new TextButton("Create study cards",textButtonStyle);
        btnCreateTest.setSize(btnBgHover.getWidth(),btnBgHover.getHeight());
        btnCreateTest.setPosition(100,
                titleBg.getY() - 3 *  AppInfo.PADDING - btnCreateTest.getHeight());


        /*

        ***************************************************
            Settings
        ***************************************************

          ---------------------------------------------
                           Create Test
          ---------------------------------------------

        Words                         Amount of question
        o Every Words                 20 / 150
        o Starry Words                --

        Question - Answer Type         *Please enter a valid
        o Definition - Word             number*
        o Word - Definition
                                       ----------------------
        + Use turkish definition       |      Word          |
                                       |  a          c      |
        + Show right answer after      |  b          d      |
          each question                |                    |
                                       ----------------------
         */

        settingsGroup.addActor(bg);
        settingsGroup.addActor(titleBg);
        settingsGroup.addActor(titleLabel);
        settingsGroup.addActor(btnCreateTest);


        createSelectionGroups(settingsGroup,btnCreateTest);


    }

    private void createSelectionGroups(Group mainGroup,
                                       TextButton createTest) {

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = Fonts.bl_bold_26;
        labelStyle.fontColor = Color.BLACK;

        Table wordSelectionTable = new Table();
        wordSelectionTable.left().top();
        Label wordSelectionLabel = new Label("Words",labelStyle);
        wordSelections = new Array<>();
        Group starryWordsSelection = createSelection("Starry Words",wordSelections);
        everyWordsSelection = createSelection("Unknown Words",wordSelections);

        wordSelectionTable.add(wordSelectionLabel).left().row();
        wordSelectionTable.add(everyWordsSelection).left().padTop(2 * AppInfo.PADDING).row();
        wordSelectionTable.add(starryWordsSelection).left().padTop(AppInfo.PADDING).row();

        wordSelectionTable.pack();

/////////////////////////////////////////////////////////////

        turkishDefSelectionBox = createSelectBox("Show Turkish definitions.");

        showExamplesBox = createSelectBox("Show examples.");


        mainGroup.addActor(wordSelectionTable);
        mainGroup.addActor(turkishDefSelectionBox);
        mainGroup.addActor(showExamplesBox);


        wordSelectionTable.setPosition(30,
                createTest.getY()  - wordSelectionTable.getHeight() -  100);
        turkishDefSelectionBox.setPosition(30,wordSelectionTable.getY() - 100 - turkishDefSelectionBox.getHeight());
        showExamplesBox.setPosition(30,
                turkishDefSelectionBox.getY() - showExamplesBox.getHeight() - 2 * AppInfo.PADDING);




        if (DataHolder.dataholder.getStarryWordCount() < 5){
            RoundedRectangle image = new RoundedRectangle((int)starryWordsSelection.getWidth() + 12,
                    (int)starryWordsSelection.getHeight() + 12,15);
            image.setColor(Color.GRAY.r,Color.GRAY.g,Color.GRAY.b,0.7f);
            image.setPosition(wordSelectionTable.getX() - 6,
                    wordSelectionTable.getY() - 6);
            image.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    Alert.show(stage,"There must be at least 5 starry words in this list!");
                }
            });
            mainGroup.addActor(image);
        }

        createTest.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                EVERY_WORD = wordSelections.get(1) == everyWordsSelection;
                SHOW_TURKISH = turkishDefSelectionBox.getChild(1).isVisible();
                SHOW_EXAMPLES = showExamplesBox.getChild(1).isVisible();


                studyCardScreen.createCardsClicked();
//
            }
        });


    }


    public void createSmallCard(){
        if (frontCard != null){
            frontCard.remove();
        }

        EVERY_WORD = wordSelections.get(1) == everyWordsSelection;
        SHOW_TURKISH = turkishDefSelectionBox.getChild(1).isVisible();
        SHOW_EXAMPLES = showExamplesBox.getChild(1).isVisible();

        StudyWordCard.StudyWordCardStyle studyWordCardStyle = new StudyWordCard.StudyWordCardStyle();
//        studyWordCardStyle.showExamples = SHOW_EXAMPLES;
//        studyWordCardStyle.showTurkish = SHOW_TURKISH;
//        studyWordCardStyle.SELECTED_TYPE = 0;
//        studyWordCardStyle.FOR_WORDS = (EVERY_WORD?0:1);

        frontCard = new StudyWordCard(DataHolder.dataholder.getSelectedList().words.first(),studyWordCardStyle);


        frontCard.setOrigin(Align.center);
        frontCard.setScale(0.55f);
        frontCard.disableButtons();

//        frontCard.addListener(new ClickListener(){
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                super.clicked(event, x, y);
//                frontCard.changeCard();
//            }
//        });

        frontCard.setPosition(260,
                btnCreateTest.getY() / 2 - frontCard.getHeight() / 2);

        settingsGroup.addActor(frontCard);

    }

    private Group createSelection(String text,final Array<Group> selections){
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = Fonts.bl_bold_26;

        Label label = new Label(text,labelStyle);

        final Image circle = new Image(Assets.assets.circle);
        circle.setSize(32,32);
        final Image tick = new Image(Assets.assets.tick);
        tick.setSize(16,16);

        tick.setVisible(false);

        label.setColor(Color.BLACK);
        circle.setColor(Color.GRAY);
        tick.setColor(Color.GOLD);

        final Group group = new Group();
        group.setHeight(Math.max(circle.getHeight(),label.getHeight()));

        circle.setPosition(0,label.getHeight() / 2 - circle.getHeight() / 2);
        tick.setPosition(circle.getWidth() / 2 - tick.getWidth() / 2,
                circle.getHeight() / 2 - tick.getHeight() / 2 + circle.getY());
        label.setPosition(circle.getWidth() + 2 * AppInfo.PADDING,
                circle.getHeight() / 2 - label.getHeight() / 2);

        group.addActor(circle);
        group.addActor(tick);
        group.addActor(label);

        group.setWidth(label.getWidth() + label.getX());

        group.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                for (Group selection:selections) {
                    selection.getChild(0).setColor(Color.GRAY);
                    selection.getChild(1).setVisible(false);
                }

                if (!tick.isVisible()){
                    tick.setVisible(true);
                    circle.setColor(Color.GOLD);
                }else {
                    tick.setVisible(false);
                    circle.setColor(Color.GRAY);
                }

                selections.removeValue(group,true);
                selections.add(group);

                createSmallCard();
            }
        });

        selections.add(group);

        if (selections.size == 2){
            tick.setVisible(true);
            circle.setColor(Color.GOLD);
        }
        return group;
    }
    private Group createSelectBox(String text){
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = Fonts.bl_bold_26;

        Label label = new Label(text,labelStyle);

        final Image box = new Image(Assets.assets.box);
        box.setSize(32,32);
        final Image tick = new Image(Assets.assets.tick);
        tick.setSize(16,16);

        tick.setVisible(false);

        label.setColor(Color.BLACK);
        box.setColor(Color.GRAY);
        tick.setColor(Color.GOLD);

        Group group = new Group();
        group.setHeight(Math.max(box.getHeight(),label.getHeight()));

        box.setPosition(0,label.getHeight() / 2 - box.getHeight() / 2);
        tick.setPosition(box.getWidth() / 2 - tick.getWidth() / 2,
                box.getHeight() / 2 - tick.getHeight() / 2 + box.getY());
        label.setPosition(box.getWidth() + 2 * AppInfo.PADDING,
                box.getHeight() / 2 - label.getHeight() / 2);


        group.addActor(box);
        group.addActor(tick);
        group.addActor(label);

        group.setWidth(label.getWidth() + label.getX());

        group.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);


                if (!tick.isVisible()){
                    tick.setVisible(true);
                    box.setColor(Color.GOLD);
                }else {
                    tick.setVisible(false);
                    box.setColor(Color.GRAY);
                }
                createSmallCard();

            }
        });

        return group;
    }

}
