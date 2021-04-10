package com.serhatmerak.wordlist.huds;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.serhatmerak.wordlist.AppMain;
import com.serhatmerak.wordlist.helpers.AppInfo;
import com.serhatmerak.wordlist.helpers.ColorPalette;
import com.serhatmerak.wordlist.helpers.DataHolder;
import com.serhatmerak.wordlist.helpers.Fonts;
import com.serhatmerak.wordlist.huds.wordcardhuds.StudyWordCard;
import com.serhatmerak.wordlist.screens.StudyCardScreen;

public class WordCardsOption extends Group {

    private AppMain appMain;

    private final float width = 900;
    private final float height = 600;

    private Label lblListName,lblCardType;

    private int SELECTED_TYPE = 0;
    /*
    * 0 = Name - Definition
    * 1 = Definition - Name
    * 2 = Custom Word Cards
     */

    private int FOR_WORDS = 0;
    /*
     * 0 = UnknownWords
     * 1 = All Words
     * 2 = Starry Words
     */

    StudyWordCard frontCard;

    private boolean showTurkish,showExamples,showAllDefinitions;

    public Label lblStarryWords;
    public Label lblAllWords;
    public Label lblUnknownWords;


    public WordCardsOption(AppMain appMain){
        this.appMain = appMain;

        createActors();
        createSmallCard();
        createLastButton();
    }

    private void createLastButton() {
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = Fonts.bl_bold_45;
        textButtonStyle.fontColor = Color.WHITE;
        textButtonStyle.downFontColor = Color.DARK_GRAY;
        textButtonStyle.overFontColor = Color.LIGHT_GRAY;

        TextButton btnCreateCards = new TextButton("Create Cards", textButtonStyle);
        btnCreateCards.setPosition(frontCard.getX() + (frontCard.getWidth() * frontCard.getScaleX()) / 2 - btnCreateCards.getPrefWidth() / 2,
                (frontCard.getY() - lblStarryWords.getY()) / 2 + lblStarryWords.getY() - btnCreateCards.getPrefHeight() / 2);
        addActor(btnCreateCards);

        btnCreateCards.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //TODO: Create Screen
                StudyWordCard.StudyWordCardStyle studyWordCardStyle = new StudyWordCard.StudyWordCardStyle();
//                studyWordCardStyle.showExamples = showExamples;
//                studyWordCardStyle.showTurkish = showTurkish;
//                studyWordCardStyle.SELECTED_TYPE = SELECTED_TYPE;
//                studyWordCardStyle.FOR_WORDS = FOR_WORDS;

                appMain.setScreen(new StudyCardScreen(appMain));
//                appMain.setScreen(new StudyCardScreen(appMain,studyWordCardStyle));

            }
        });

    }

    public void createSmallCard(){
        if (frontCard != null){
            frontCard.remove();
        }

        StudyWordCard.StudyWordCardStyle studyWordCardStyle = new StudyWordCard.StudyWordCardStyle();
//        studyWordCardStyle.showExamples = showExamples;
//        studyWordCardStyle.showTurkish = showTurkish;
//        studyWordCardStyle.SELECTED_TYPE = SELECTED_TYPE;
//        studyWordCardStyle.FOR_WORDS = FOR_WORDS;

        frontCard = new StudyWordCard(DataHolder.dataholder.getSelectedList().words.first(),studyWordCardStyle);


        frontCard.setScale(0.4f);
        frontCard.disableButtons();

//        frontCard.addListener(new ClickListener(){
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                super.clicked(event, x, y);
//                frontCard.changeCard();
//            }
//        });

        frontCard.setPosition(500,lblCardType.getY() +
                lblCardType.getPrefHeight() - frontCard.getHeight() * frontCard.getScaleX());

        addActor(frontCard);

    }

    private void createActors() {
        BorderImage borderImage = new BorderImage(new Vector3(width,height,4));
        addActor(borderImage);
        setSize(borderImage.getWidth(),borderImage.getHeight());

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = Fonts.bl_bold_72;
        textButtonStyle.fontColor = Color.WHITE;
        textButtonStyle.downFontColor = Color.DARK_GRAY;
        textButtonStyle.overFontColor = Color.LIGHT_GRAY;

        TextButton btnCloseWindow = new TextButton("x", textButtonStyle);
        btnCloseWindow.setPosition(width - AppInfo.PADDING - btnCloseWindow.getPrefWidth(),
                height + AppInfo.PADDING - btnCloseWindow.getPrefHeight());

        btnCloseWindow.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                WordCardsOption.this.getParent().remove();
            }
        });

        addActor(btnCloseWindow);

        Label.LabelStyle listNameStyle = new Label.LabelStyle();
        listNameStyle.font = Fonts.bl_bold_32;

        lblListName = new Label(DataHolder.dataholder.getSelectedList().listName,listNameStyle);
        lblListName.setPosition(width / 2 - lblListName.getPrefWidth() / 2,
                height - AppInfo.PADDING - lblListName.getPrefHeight());


        BorderImage bg2 = new BorderImage(new Vector3(width - 2 * AppInfo.PADDING,height - 3 * AppInfo.PADDING - lblListName.getPrefHeight(),4));
        bg2.setPosition(AppInfo.PADDING,AppInfo.PADDING);
        addActor(bg2);

        Label.LabelStyle otherLabelStyle = new Label.LabelStyle();
        otherLabelStyle.font = Fonts.bl_bold_26;

        lblCardType = new Label("Card Type: ",otherLabelStyle);
        lblCardType.setPosition(2 * AppInfo.PADDING, lblListName.getY() - 3 * AppInfo.PADDING - lblCardType.getPrefHeight());

        final Label lblNameDef = new Label("Name - Definition",otherLabelStyle);
        final Label lblDefName = new Label("Definition - Name",otherLabelStyle);

        lblNameDef.setColor(Color.GOLD);

        lblNameDef.setPosition(lblCardType.getX() + lblCardType.getPrefWidth() + 3 * AppInfo.PADDING,lblCardType.getY());
        lblDefName.setPosition(lblNameDef.getX(),lblNameDef.getY() - AppInfo.PADDING - lblDefName.getPrefHeight());

        lblNameDef.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                SELECTED_TYPE = 0;
                lblDefName.setColor(Color.WHITE);
                lblNameDef.setColor(Color.GOLD);
                createSmallCard();
            }
        });

        lblDefName.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                SELECTED_TYPE = 1;
                lblDefName.setColor(Color.GOLD);
                lblNameDef.setColor(Color.WHITE);
                createSmallCard();
            }
        });

        Label inAnswers = new Label("In Answers:",otherLabelStyle);
        inAnswers.setPosition(lblCardType.getX(),lblDefName.getY() - AppInfo.PADDING * 4 - inAnswers.getPrefHeight());

        final Label lblShowTurkish = new Label("Turkish Definition",otherLabelStyle);
        final Label lblShowExamples = new Label("Examples",otherLabelStyle);
        final Label lblShowAllDefinitions = new Label("All Definitions",otherLabelStyle);

        lblShowTurkish.setPosition(inAnswers.getX() + inAnswers.getPrefWidth() + 3*AppInfo.PADDING,inAnswers.getY());
        lblShowExamples.setPosition(lblShowTurkish.getX(),lblShowTurkish.getY() - AppInfo.PADDING - lblShowExamples.getPrefHeight());
        lblShowAllDefinitions.setPosition(lblShowTurkish.getX(),lblShowExamples.getY() - AppInfo.PADDING - lblShowAllDefinitions.getPrefHeight());

        lblShowAllDefinitions.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                showAllDefinitions = !showAllDefinitions;
                if (showAllDefinitions)
                    lblShowAllDefinitions.setColor(ColorPalette.text1);
                else
                    lblShowAllDefinitions.setColor(Color.WHITE);

                createSmallCard();
            }
        });
        lblShowExamples.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                showExamples = !showExamples;
                if (showExamples)
                    lblShowExamples.setColor(ColorPalette.text1);
                else
                    lblShowExamples.setColor(Color.WHITE);

                createSmallCard();
            }
        });
        lblShowTurkish.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                showTurkish = !showTurkish;
                if (showTurkish)
                    lblShowTurkish.setColor(ColorPalette.text1);
                else
                    lblShowTurkish.setColor(Color.WHITE);

                createSmallCard();
            }
        });

        Label whichWords = new Label("For Words:",otherLabelStyle);
        whichWords.setPosition(lblCardType.getX(),lblShowAllDefinitions.getY() - AppInfo.PADDING * 4 - whichWords.getPrefHeight());

        lblUnknownWords = new Label("Unknown Words",otherLabelStyle);
        lblAllWords = new Label("All Words",otherLabelStyle);
        lblStarryWords = new Label("Starry Words",otherLabelStyle);

        lblUnknownWords.setPosition(whichWords.getX() + whichWords.getPrefWidth() + 3*AppInfo.PADDING,whichWords.getY());
        lblAllWords.setPosition(lblUnknownWords.getX(),lblUnknownWords.getY() - AppInfo.PADDING - lblAllWords.getPrefHeight());
        lblStarryWords.setPosition(lblUnknownWords.getX(),lblAllWords.getY() - AppInfo.PADDING - lblStarryWords.getPrefHeight());

        lblUnknownWords.setColor(Color.GOLD);

        lblUnknownWords.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                FOR_WORDS = 0;

                lblStarryWords.setColor((lblStarryWords.getTouchable() == Touchable.disabled)?Color.DARK_GRAY:Color.WHITE);
                lblAllWords.setColor(Color.WHITE);
                lblUnknownWords.setColor(Color.GOLD);

                createSmallCard();
            }
        });
        lblAllWords.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                FOR_WORDS = 1;

                lblStarryWords.setColor((lblStarryWords.getTouchable() == Touchable.disabled)?Color.DARK_GRAY:Color.WHITE);
                lblAllWords.setColor(Color.GOLD);
                lblUnknownWords.setColor((lblUnknownWords.getTouchable() == Touchable.disabled)?Color.DARK_GRAY:Color.WHITE);

                createSmallCard();
            }
        });
        lblStarryWords.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                FOR_WORDS = 2;

                lblStarryWords.setColor(Color.GOLD);
                lblAllWords.setColor(Color.WHITE);
                lblUnknownWords.setColor((lblUnknownWords.getTouchable() == Touchable.disabled)?Color.DARK_GRAY:Color.WHITE);

                createSmallCard();
            }
        });

        float scale = 1.3f;

        lblNameDef.setOrigin(Align.center);
        lblDefName.setOrigin(Align.center);

        lblShowAllDefinitions.setOrigin(Align.center);
        lblShowExamples.setOrigin(Align.center);
        lblShowTurkish.setOrigin(Align.center);

        lblStarryWords.setOrigin(Align.center);
        lblAllWords.setOrigin(Align.center);
        lblUnknownWords.setOrigin(Align.center);

        lblNameDef.setScale(scale);
        lblDefName.setScale(scale);

        lblShowAllDefinitions.setScale(scale);
        lblShowExamples.setScale(scale);
        lblShowTurkish.setScale(scale);

        lblStarryWords.setScale(scale);
        lblAllWords.setScale(scale);
        lblUnknownWords.setScale(scale);

        addActor(lblStarryWords);
        addActor(lblAllWords);
        addActor(lblUnknownWords);
        addActor(whichWords);
//        addActor(lblShowAllDefinitions);
        addActor(lblShowExamples);
        addActor(lblShowTurkish);
        addActor(lblListName);
        addActor(lblCardType);
        addActor(lblDefName);
        addActor(lblNameDef);
        addActor(inAnswers);

    }

    public void selectAllWordsOption(){
        FOR_WORDS = 1;

        lblStarryWords.setColor((lblStarryWords.getTouchable() == Touchable.disabled)?Color.DARK_GRAY:Color.WHITE);
        lblAllWords.setColor(Color.GOLD);
        lblUnknownWords.setColor((lblUnknownWords.getTouchable() == Touchable.disabled)?Color.DARK_GRAY:Color.WHITE);

        createSmallCard();
    }
}
