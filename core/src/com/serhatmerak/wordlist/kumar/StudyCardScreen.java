package com.serhatmerak.wordlist.kumar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.serhatmerak.wordlist.AppMain;
import com.serhatmerak.wordlist.helpers.AppInfo;
import com.serhatmerak.wordlist.helpers.Assets;
import com.serhatmerak.wordlist.helpers.ButtonHoverAnimation;
import com.serhatmerak.wordlist.helpers.ColorPalette;
import com.serhatmerak.wordlist.helpers.CustomScreen;
import com.serhatmerak.wordlist.helpers.DataHolder;
import com.serhatmerak.wordlist.helpers.Fonts;
import com.serhatmerak.wordlist.huds.BorderImage;
import com.serhatmerak.wordlist.huds.ShowWindow;
import com.serhatmerak.wordlist.huds.wordcardhuds.StudyWordCard;
import com.serhatmerak.wordlist.screens.WordListScreen;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class StudyCardScreen extends CustomScreen {

    private Viewport viewport;
    private Stage stage;
    private OrthographicCamera camera;
    private AppMain appMain;
    private SpriteBatch batch;

    private StudyWordCard.StudyWordCardStyle studyWordCardStyle;

    private StudyWordCard customCard;
    private Image btnBack,btnNextCard,btnPreviousCard;
    private Label lblShowCardIndex;

    private Array<StudyWordCard> wordCards;
    private int cardIndex;
    private Group cardListGroup;

    private ObjectMap<StudyWordCard,Group> cardGroupMap;
    private SlowScrollPane scrollPane;
    private Table table;

    private boolean mix = false;
    private Group stopGroup,playGroup,mixGroup;
    private Image showSec;


    private boolean auto = false;
    private float deltaAuto;

    private int answerTime = 3,questionTime = 3;

    private boolean pause = false;
    private boolean finishedWords = false;
    private Group finishedGroup;
    private Group showShortcutGroup;


    public StudyCardScreen(AppMain appMain,StudyWordCard.StudyWordCardStyle studyWordCardStyle){
        this.appMain = appMain;
        this.studyWordCardStyle = studyWordCardStyle;
        batch = appMain.batch;

        camera = new OrthographicCamera(AppInfo.WIDTH,AppInfo.HEIGHT);
        camera.setToOrtho(false, AppInfo.WIDTH,AppInfo.HEIGHT);
        viewport = new FitViewport(AppInfo.WIDTH,AppInfo.HEIGHT,camera);
        stage = new Stage(viewport, batch);

        Gdx.input.setInputProcessor(stage);

        customCard = new StudyWordCard(DataHolder.dataholder.getSelectedList() .words.first(),studyWordCardStyle);
        customCard.setOrigin(Align.center);
        customCard.setScale(1.25f);
        customCard.setPosition(AppInfo.WIDTH / 2f - customCard.getWidth() / 2 - 100,
                AppInfo.HEIGHT / 2f - customCard.getHeight() / 2 + 90);


        stage.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
//                if (auto && deltaAuto != 0){
//                    auto = false;
//                    stopGroup.remove();
//                    stage.addActor(playGroup);
//                }
            }
        });

        createActors();
        createCards();
        createLeftButtons();
        changeCard();
        createFinishedTable();
        shortcuts();
    }

    private void createFinishedTable() {

//        if (finishedGroup != null)
//            finishedGroup.remove();
//
//        finishedGroup = new Group();
//
//        BorderImage borderImage = new BorderImage(new Vector3(customCard.getWidth(),
//                customCard.getHeight(),3));
//
//        Label.LabelStyle bigLabelStyle = new Label.LabelStyle();
//        bigLabelStyle.font = Fonts.bl_bold_72;
//
//        Label.LabelStyle smallLabelStyle = new Label.LabelStyle();
//        smallLabelStyle.font = Fonts.bl_bold_32;
//
//        Label lblGoodJob = new Label("Good Job!",bigLabelStyle);
//
//        Label smallLabel;
//
//
//        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
//        textButtonStyle.font = Fonts.bl_bold_45;
//        textButtonStyle.disabledFontColor = Color.GRAY;
//
//        TextButton btnAgain = new TextButton("Study Again",textButtonStyle);
//        TextButton btnStudyOthers1 = new TextButton("",textButtonStyle);
//        TextButton btnStudyOthers2= new TextButton("",textButtonStyle);
//
//        Group groupAgain = new Group();
//        Group groupOther1 = new Group();
//        Group groupOther2 = new Group();
//
//        if (studyWordCardStyle.FOR_WORDS == StudyWordCard.StudyWordCardStyle.STARRY_WORDS){
//            smallLabel = new Label("You have studied " + wordCards.size + " starry words.",smallLabelStyle);
//            if (DataHolder.dataholder.getStarryWordCount() == 0){
//                groupAgain.setTouchable(Touchable.disabled);
//                btnAgain.setDisabled(true);
//            }
//            btnStudyOthers1.setText("Study Unknown Words");
//            if (DataHolder.dataholder.getUnknownWordCount() == 0){
//                btnStudyOthers1.setTouchable(Touchable.disabled);
//                btnStudyOthers1.setDisabled(true);
//            }
//            btnStudyOthers2.setText("Study All Words");
//        }else if (studyWordCardStyle.FOR_WORDS == StudyWordCard.StudyWordCardStyle.ALL_WORDS){
//            smallLabel = new Label("You have studied all " + wordCards.size + " words.",smallLabelStyle);
//            btnStudyOthers1.setText("Study Starry Words");
//            if (DataHolder.dataholder.getStarryWordCount() == 0){
//                btnStudyOthers1.setTouchable(Touchable.disabled);
//                btnStudyOthers1.setDisabled(true);
//            }
//
//            btnStudyOthers2.setText("Study Unknown Words");
//            if (DataHolder.dataholder.getUnknownWordCount() == 0){
//                btnStudyOthers2.setTouchable(Touchable.disabled);
//                btnStudyOthers2.setDisabled(true);
//            }
//        }else {
//            smallLabel = new Label("You have studied " + wordCards.size + " unknown words.",smallLabelStyle);
//            if (DataHolder.dataholder.getUnknownWordCount() == 0){
//                groupAgain.setTouchable(Touchable.disabled);
//                btnAgain.setDisabled(true);
//            }
//
//            btnStudyOthers1.setText("Study Starry Words");
//            if (DataHolder.dataholder.getStarryWordCount() == 0){
//                btnStudyOthers1.setTouchable(Touchable.disabled);
//                btnStudyOthers1.setDisabled(true);
//            }
//
//
//            btnStudyOthers2.setText("Study All Words");
//        }
//
//
//
//        Group buttonGroup = new Group();
//        buttonGroup.setWidth(borderImage.getWidth());
//
//        groupAgain.setSize(btnAgain.getWidth(),btnAgain.getHeight());
//        groupOther1.setSize(btnStudyOthers1.getWidth(),btnStudyOthers1.getHeight());
//        groupOther2.setSize(btnStudyOthers2.getWidth(),btnStudyOthers2.getHeight());
//
//
//        groupOther2.setPosition(buttonGroup.getWidth() / 2 - btnStudyOthers2.getWidth() / 2,0);
//        groupOther1.setPosition(buttonGroup.getWidth() / 2 - btnStudyOthers1.getWidth() / 2,
//                btnAgain.getPrefHeight() + AppInfo.PADDING);
//        groupAgain.setPosition(buttonGroup.getWidth() / 2 - btnAgain.getWidth() / 2,
//                groupOther1.getY() + btnStudyOthers1.getHeight() + AppInfo.PADDING);
//
//        buttonGroup.setHeight(groupAgain.getY() + btnAgain.getHeight());
//
//
//        groupAgain.addActor(btnAgain);
//        groupOther1.addActor(btnStudyOthers1);
//        groupOther2.addActor(btnStudyOthers2);
//
//
//        buttonGroup.addActor(groupAgain);
//        buttonGroup.addActor(groupOther1);
//        buttonGroup.addActor(groupOther2);
//
//        buttonGroup.setPosition(0, 2 * AppInfo.PADDING);
//
//
//
//
//        Table table = new Table();
//        table.setSize(borderImage.getWidth(),borderImage.getHeight());
//        table.top();
//        table.defaults().pad(AppInfo.PADDING);
//
//        table.add(lblGoodJob).expandX().row();
//        table.add(smallLabel).expandX().center().row();
//
//
//
//        finishedGroup.setSize(borderImage.getWidth(),borderImage.getHeight());
//        finishedGroup.setPosition(customCard.getX(),customCard.getY());
//        finishedGroup.setOrigin(Align.center);
//        finishedGroup.setScale(customCard.getScaleX());
//        finishedGroup.addActor(borderImage);
//        finishedGroup.addActor(table);
//        finishedGroup.addActor(buttonGroup);
//
//
//
//        groupAgain.setOrigin(Align.center);
//        groupOther1.setOrigin(Align.center);
//        groupOther2.setOrigin(Align.center);
//
//        groupAgain.addListener(new ButtonHoverAnimation(groupAgain,true){
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                finishedWords = false;
//                finishedGroup.setVisible(false);
//                customCard.setVisible(true);
//                createCards();
//                changeCard();
//            }
//        });
//        groupOther1.addListener(new ButtonHoverAnimation(groupOther1,true){
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                if (studyWordCardStyle.FOR_WORDS == StudyWordCard.StudyWordCardStyle.STARRY_WORDS){
//                    studyWordCardStyle.FOR_WORDS = StudyWordCard.StudyWordCardStyle.UNKNOWN_WORDS;
//                }else if (studyWordCardStyle.FOR_WORDS == StudyWordCard.StudyWordCardStyle.ALL_WORDS){
//                    studyWordCardStyle.FOR_WORDS = StudyWordCard.StudyWordCardStyle.STARRY_WORDS;
//                }else {
//                    studyWordCardStyle.FOR_WORDS = StudyWordCard.StudyWordCardStyle.STARRY_WORDS;
//                }
//                finishedWords = false;
//                finishedGroup.setVisible(false);
//                customCard.setVisible(true);
//                createCards();
//                changeCard();
//            }
//        });
//        groupOther2.addListener(new ButtonHoverAnimation(groupOther2,true){
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                if (studyWordCardStyle.FOR_WORDS == StudyWordCard.StudyWordCardStyle.STARRY_WORDS){
//                    studyWordCardStyle.FOR_WORDS = StudyWordCard.StudyWordCardStyle.ALL_WORDS;
//                    finishedWords = false;
//                    finishedGroup.setVisible(false);
//                    customCard.setVisible(true);
//
//                    createCards();
//                    changeCard();
//                }else if (studyWordCardStyle.FOR_WORDS == StudyWordCard.StudyWordCardStyle.ALL_WORDS){
//                    studyWordCardStyle.FOR_WORDS = StudyWordCard.StudyWordCardStyle.UNKNOWN_WORDS;
//                    finishedWords = false;
//                    finishedGroup.setVisible(false);
//                    customCard.setVisible(true);
//                    createCards();
//                    changeCard();
//                }else {
//                    studyWordCardStyle.FOR_WORDS = StudyWordCard.StudyWordCardStyle.ALL_WORDS;
//                    finishedWords = false;
//                    finishedGroup.setVisible(false);
//                    customCard.setVisible(true);
//                    createCards();
//                    changeCard();
//                }
//            }
//        });
//
///*,
//if (studyWordCardStyle.FOR_WORDS == StudyWordCard.StudyWordCardStyle.STARRY_WORDS){
//            smallLabel = new Label("You have studied " + wordCards.size + " starry words.",smallLabelStyle);
//            btnStudyOthers1.setText("Study Unknown Words");
//            btnStudyOthers2.setText("Study All Words");
//        }else if (studyWordCardStyle.FOR_WORDS == StudyWordCard.StudyWordCardStyle.ALL_WORDS){
//            smallLabel = new Label("You have studied all " + wordCards.size + " words.",smallLabelStyle);
//            btnStudyOthers1.setText("Study Starry Words");
//            btnStudyOthers2.setText("Study Unknown Words");
//        }else {
//            smallLabel = new Label("You have studied " + wordCards.size + " unknown words.",smallLabelStyle);
//            btnStudyOthers1.setText("Study Starry Words");
//            btnStudyOthers2.setText("Study All Words");
//        }
// */
//        stage.addActor(finishedGroup);
//        finishedGroup.setVisible(false);
//

    }

    private void shortcuts() {
        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.SPACE || keycode == Input.Keys.DOWN){
                    customCard.changeCard();
                }else if (keycode == Input.Keys.RIGHT){
                    nextClicked();
                }else if (keycode == Input.Keys.LEFT){
                    previousClicked();
                }else if (keycode == Input.Keys.P){
                    pause = !pause;
                }else if (keycode == Input.Keys.S){
                    if (!finishedWords){
                        customCard.starClicked();
                    }
                }else if (keycode == Input.Keys.K){
                    if (!finishedWords){
                        customCard.dontShowClicked();
                    }
                }else if (keycode == Input.Keys.M){
                    mixClicked();
                }else if (keycode == Input.Keys.U){
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    //odd: the Object param of getContents is not currently used
                    Transferable contents = clipboard.getContents(null);
                    boolean hasTransferableText = (contents != null) && contents.isDataFlavorSupported(DataFlavor.stringFlavor);
                    if (hasTransferableText) {
                        try {
                            String result = (String) contents.getTransferData(DataFlavor.stringFlavor);
                            System.out.println(result);
                        } catch (UnsupportedFlavorException ex) {
                            //highly unlikely since we are using a standard DataFlavor
                            System.out.println(ex);
                            ex.printStackTrace();
                        } catch (IOException ex) {
                            System.out.println(ex);
                            ex.printStackTrace();
                        }
                    }
                }else if (keycode == Input.Keys.F){
                    if (DataHolder.dataholder.isThisWordFav(wordCards.get(cardIndex).word)) {
                        DataHolder.dataholder.favList.words.add(wordCards.get(cardIndex).word);
                        DataHolder.dataholder.saveList(DataHolder.dataholder.favList);
                    }
                }else if (keycode == Input.Keys.ENTER){
                    if (!finishedWords){
                        if (auto){
                            stopClicked();
                        }else
                            playClicked();
                    }
                }
                return super.keyDown(event, keycode);
            }
        });
    }

    private void createLeftButtons() {

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = Fonts.bl_bold_45;

        playGroup = new Group();
        Group playGroupForPlayButton = new Group();
        Label playLabel = new Label("Play", labelStyle);
        Image playImage = new Image(Assets.assets.play);

        playImage.setPosition(0, 0);
        playLabel.setPosition(playImage.getWidth() + 3 * AppInfo.PADDING,
                playImage.getHeight() / 2 - playLabel.getHeight() / 2);

        playGroup.setSize(playLabel.getWidth() + playLabel.getX(),
                Math.max(playImage.getHeight(), playLabel.getPrefHeight()));
        playGroupForPlayButton.setSize(playGroup.getWidth(),playGroup.getHeight());


        playGroup.setPosition((customCard.getX() - ((customCard.getScaleX() - 1) * customCard.getWidth())
                / 2 - AppInfo.PADDING) / 2 + AppInfo.PADDING - playGroup.getWidth() / 2, 500);

        playGroupForPlayButton.setOrigin(Align.center);

        playGroupForPlayButton.addActor(playImage);
        playGroupForPlayButton.addActor(playLabel);

        Image increase1 = new Image(Assets.assets.increase);
        increase1.setSize(40,40);

        Sprite decSprite = new Sprite(Assets.assets.increase);
        decSprite.flip(false,true);
        Image decrease1 = new Image(decSprite);
        decrease1.setSize(increase1.getWidth(),increase1.getHeight());





        float w = increase1.getWidth() * 2;
        decrease1.setPosition(-5 + playGroup.getWidth() / 2 - w / 2, - AppInfo.PADDING * 2 - decrease1.getHeight() * 2);
        increase1.setPosition(decrease1.getX(), decrease1.getY() + decrease1.getHeight());


        decrease1.setOrigin(Align.center);
        increase1.setOrigin(Align.center);


        Image numBg1 = new Image(Assets.assets.pix);
        Image numBg2 = new Image(Assets.assets.pix);

        numBg1.setSize(50,decrease1.getHeight() * 2);
        numBg2.setSize(numBg1.getWidth(),numBg1.getHeight());

        numBg1.setColor(ColorPalette.darkBg);
        numBg2.setColor(ColorPalette.darkBg);


        numBg1.setPosition(- 10,decrease1.getY());
        numBg2.setPosition(10 + playGroupForPlayButton.getWidth() - numBg1.getWidth(),decrease1.getY());

        ////////////////////////
        Image increase2 = new Image(Assets.assets.increase);
        Image decrease2 = new Image(decSprite);

        decrease2.setSize(increase1.getWidth(),increase1.getHeight());
        increase2.setSize(increase1.getWidth(),increase1.getHeight());

        decrease2.setPosition(5 + playGroup.getWidth() / 2, decrease1.getY());
        increase2.setPosition(decrease2.getX(), decrease2.getY() + decrease2.getHeight());

        decrease2.setOrigin(Align.center);
        increase2.setOrigin(Align.center);

        Label.LabelStyle numLabStyle = new Label.LabelStyle();
        numLabStyle.font = Fonts.bl_bold_26;

        final Label numLab1 = new Label("3\nsec",numLabStyle);
        final Label numLab2 = new Label("3\nsec",numLabStyle);

        numLab1.setSize(numBg1.getWidth(),numBg1.getHeight());
        numLab2.setSize(numBg1.getWidth(),numBg1.getHeight());

        numLab1.setAlignment(Align.center);
        numLab2.setAlignment(Align.center);

        numLab1.setPosition(numBg1.getX(),numBg1.getY());
        numLab2.setPosition(numBg2.getX(),numBg2.getY());



        decrease1.addListener(new ButtonHoverAnimation(decrease1,true){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if (questionTime != 1){
                    questionTime--;
                    numLab1.setText(questionTime +"\nsec");
                }
            }
        });
        increase1.addListener(new ButtonHoverAnimation(increase1,true){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if (questionTime != 10){
                    questionTime++;
                    numLab1.setText(questionTime +"\nsec");
                }
            }
        });
        decrease2.addListener(new ButtonHoverAnimation(decrease2,true){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if (answerTime != 1){
                    answerTime--;
                    numLab2.setText(answerTime +"\nsec");
                }
            }
        });
        increase2.addListener(new ButtonHoverAnimation(increase2,true){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if (answerTime != 10){
                    answerTime++;
                    numLab2.setText(answerTime +"\nsec");
                }
            }
        });




        playGroup.addActor(numBg1);
        playGroup.addActor(numBg2);
        playGroup.addActor(numLab1);
        playGroup.addActor(numLab2);

        playGroup.addActor(playGroupForPlayButton);
        playGroup.addActor(decrease1);
        playGroup.addActor(decrease2);
        playGroup.addActor(increase1);
        playGroup.addActor(increase2);






        stage.addActor(playGroup);

        stopGroup = new Group();
        Label stopLabel = new Label("Pause", labelStyle);
        Image stopImage = new Image(Assets.assets.pause);
        stopImage.setSize(stopImage.getWidth() * 0.8f,stopImage.getHeight() * 0.8f);

        stopImage.setPosition(0, 0);
        stopLabel.setPosition(stopImage.getWidth() + 2 * AppInfo.PADDING,
                stopImage.getHeight() / 2 - stopLabel.getHeight() / 2);

        stopGroup.setSize(stopLabel.getWidth() + stopLabel.getX(),
                Math.max(stopImage.getHeight(), stopLabel.getPrefHeight()));

        stopGroup.setPosition((customCard.getX() - ((customCard.getScaleX() - 1) * customCard.getWidth())
                / 2 - AppInfo.PADDING) / 2 + AppInfo.PADDING - stopGroup.getWidth() / 2,
                playGroup.getY() + playGroup.getHeight() / 2 - stopGroup.getHeight() / 2);

        stopGroup.setOrigin(Align.center);

        stopGroup.addActor(stopImage);
        stopGroup.addActor(stopLabel);

        for (Actor a :stopGroup.getChildren()) {
            a.setColor(ColorPalette.text1);
        }

        Image showSecBg = new Image(Assets.assets.pix);
        showSecBg.setSize(stopGroup.getWidth(),40);
        showSecBg.setColor( ColorPalette.darkBg);
        showSecBg.setPosition(0,-AppInfo.PADDING - stopGroup.getHeight());

        showSec = new Image(Assets.assets.pix);
        showSec.setSize(0,40);
        showSec.setColor(ColorPalette.text1);
        showSec.setPosition(showSecBg.getX(),showSecBg.getY());

        stopGroup.addActor(showSecBg);
        stopGroup.addActor(showSec);



        mixGroup = new Group();
        Label mixLabel = new Label("Mix",labelStyle);
        Image mixImage = new Image(Assets.assets.mix);

        mixImage.setPosition(0,0);
        mixLabel.setPosition(mixImage.getWidth() + 2 * AppInfo.PADDING,
                mixImage.getHeight() / 2 - mixLabel.getHeight() / 2);

        mixGroup.setSize(mixLabel.getWidth() + mixLabel.getX(),
                Math.max(mixImage.getHeight(),mixLabel.getPrefHeight()));

        mixGroup.setPosition(playGroup.getX(),250);

        mixGroup.setOrigin(Align.center);
        mixGroup.addListener(new ButtonHoverAnimation(mixGroup,true){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                mixClicked();
            }
        });

        mixGroup.addActor(mixImage);
        mixGroup.addActor(mixLabel);

        stage.addActor(mixGroup);

        btnBack.setPosition(mixGroup.getX(),AppInfo.HEIGHT - 2 * AppInfo.PADDING - btnBack.getHeight() - 30);





        stopGroup.addListener(new ButtonHoverAnimation(stopGroup, true){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
//                if (auto){
//                    auto = false;
//                    stopGroup.remove();
//                    stage.addActor(playGroup);
//                }
                stopClicked();
            }
        });

        playGroupForPlayButton.addListener(new ButtonHoverAnimation(playGroupForPlayButton, true){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                playClicked();
//                if (!auto && !finishedWords){
//                    pause = false;
//                    auto = true;
//                    playGroup.remove();
//                    stage.addActor(stopGroup);
//                    deltaAuto = 0;
//                }
            }
        });

        Group keyboardGroup = new Group();

        Image imgKeyboard = new Image(Assets.assets.keyboard);
        imgKeyboard.setSize(50,(50/imgKeyboard.getWidth() )* imgKeyboard.getHeight());
        Label lblKeyboard = new Label("Shortcuts",labelStyle);

        imgKeyboard.setPosition(lblKeyboard.getWidth() / 2 - imgKeyboard.getWidth() / 2,0);

        lblKeyboard.setPosition(0,
                - imgKeyboard.getHeight());

        keyboardGroup.setSize(lblKeyboard.getWidth(),
                lblKeyboard.getHeight() + imgKeyboard.getHeight());

        keyboardGroup.setPosition(playGroup.getX() + playGroup.getWidth() / 2 - keyboardGroup.getWidth() / 2,
                680);

        keyboardGroup.setOrigin(Align.center);

        keyboardGroup.addActor(lblKeyboard);
        keyboardGroup.addActor(imgKeyboard);
        stage.addActor(keyboardGroup);

        keyboardGroup.addListener(new ButtonHoverAnimation(keyboardGroup,true){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                final ShowWindow showWindow = new ShowWindow(showShortcutGroup);
                stage.addActor(showWindow);
                showWindow.addListener(new ClickListener(){
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        super.clicked(event, x, y);
                        showWindow.remove();
                    }
                });
            }
        });

        createShortcutsTable();
    }

    private void createShortcutsTable() {
        showShortcutGroup = new Group();
        BorderImage borderImage = new BorderImage(new Vector3(1200,800,4));
        Table table = new Table();
        table.setSize(borderImage.getWidth(),borderImage.getHeight());
        table.center().left();
        table.defaults().pad(AppInfo.PADDING);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = Fonts.bl_bold_32;

        table.add(new Label("Right Arrow",labelStyle)).width(300).padLeft(30).padTop(30);
        table.add(new Label(":",labelStyle)).width(30);
        table.add(new Label("Next Card",labelStyle)).width(950).row();
        table.add(new Label("Left Arrow",labelStyle)).width(300).padLeft(30);
        table.add(new Label(":",labelStyle)).width(30);
        table.add(new Label("Previous Card",labelStyle)).width(950).row();
        table.add(new Label("Down Arrow",labelStyle)).width(300).padLeft(30);
        table.add(new Label(":",labelStyle)).width(30);
        table.add(new Label("Turn the other side",labelStyle)).width(950).row();
        table.add(new Label("Space",labelStyle)).width(300).padLeft(30);
        table.add(new Label(":",labelStyle)).width(30);
        table.add(new Label("Turn the other side",labelStyle)).width(950).row();
        table.add(new Label("Enter",labelStyle)).width(300).padLeft(30);
        table.add(new Label(":",labelStyle)).width(30);
        table.add(new Label("Open/Close auto study",labelStyle)).width(950).row();
        table.add(new Label("K",labelStyle)).width(300).padLeft(30);
        table.add(new Label(":",labelStyle)).width(30);
        table.add(new Label("Add/Remove it to/from Known Words",labelStyle)).width(950).row();
        table.add(new Label("S",labelStyle)).width(300).padLeft(30);
        table.add(new Label(":",labelStyle)).width(30);
        table.add(new Label("Add/Remove it to/from Starry Words",labelStyle)).width(950).row();
        table.add(new Label("P",labelStyle)).width(300).padLeft(30);
        table.add(new Label(":",labelStyle)).width(30);
        table.add(new Label("Pause the auto study",labelStyle)).width(950).row();

        showShortcutGroup.setSize(borderImage.getWidth(),borderImage.getHeight());

        showShortcutGroup.addActor(borderImage);
        showShortcutGroup.addActor(table);
    }

    private void stopClicked(){
        if (auto){
            auto = false;
            stopGroup.remove();
            stage.addActor(playGroup);
        }
    }

    private void playClicked(){
        if (!auto && !finishedWords){
            pause = false;
            auto = true;
            playGroup.remove();
            stage.addActor(stopGroup);
            deltaAuto = 0;
        }
    }

    private void mixClicked() {
        mix = !mix;
        if (mix){
            for (Actor a :mixGroup.getChildren()) {
                a.setColor(ColorPalette.text1);
            }
        } else {
            for (Actor a :mixGroup.getChildren()) {
                a.setColor(Color.WHITE);
            }
        }
    }

    private void createCards() {
        if (wordCards == null)
            wordCards = new Array<>();
        else
            wordCards.clear();

//        for (int i =0;i < DataHolder.dataholder.getSelectedList().words.size;i++){
//            Word word = DataHolder.dataholder.getSelectedList().words.get(i);
//            if (!((studyWordCardStyle.FOR_WORDS == 0 && DataHolder.dataholder.knownWords.contains(word.name,false))
//                ||
//            (studyWordCardStyle.FOR_WORDS == 2 && !word.star))) {
//
//                final StudyWordCard studyWordCard = new StudyWordCard(word, studyWordCardStyle);
//                studyWordCard.setPosition(customCard.getX(), customCard.getY());
//                studyWordCard.setOrigin(Align.center);
//                studyWordCard.setScale(customCard.getScaleX());
//                wordCards.add(studyWordCard);
//            }
//        }
//        cardIndex = 0;
//        createCardListTable();
//        //TODO:
////        createFinishedTable();

    }

    private void changeCard() {
        if (!finishedWords) {
            if (customCard.getStage() != null) {
                customCard.remove();
                if ( cardGroupMap.get(customCard) != null) {
                    Label label = (Label) cardGroupMap.get(customCard).getChild(1);
                    label.setColor(Color.WHITE);
                }
            }

            StudyWordCard studyWordCard = wordCards.get(cardIndex);

            if (mix)
                studyWordCard = wordCards.random();

            stage.addActor(studyWordCard);
            customCard = studyWordCard;
            if (studyWordCard.isAnswer)
                studyWordCard.changeCard();

            lblShowCardIndex.setText((cardIndex + 1) + " / " + wordCards.size);

            Label label = (Label) cardGroupMap.get(studyWordCard).getChild(1);
            label.setColor(ColorPalette.text1);

            float index = table.getChildren().indexOf(cardGroupMap.get(studyWordCard), true);
            float size = table.getChildren().size;

            deltaAuto = 0;
            scrollPane.setScrollY(Math.max(0, (scrollPane.getMaxY() / (size - 15))) * (index - 4));

        }
    }

    private void createActors() {
        BorderImage borderImage = new BorderImage(new Vector3(AppInfo.WIDTH - 2 * AppInfo.PADDING,
                AppInfo.HEIGHT - 2 * AppInfo.PADDING , 4));
        borderImage.setPosition(AppInfo.PADDING,AppInfo.PADDING);

        btnBack = new Image(Assets.assets.back);
        btnBack.setOrigin(Align.center);
        btnBack.setSize(64,64);
        btnBack.addListener(new ButtonHoverAnimation(btnBack){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                appMain.setScreen(new WordListScreen(appMain));
            }
        });

        stage.addActor(borderImage);
        stage.addActor(btnBack);

        Sprite backSprite = new Sprite(Assets.assets.arrow);
        backSprite.flip(true,false);

        btnNextCard = new Image(Assets.assets.arrow);
        btnPreviousCard = new Image(new SpriteDrawable(backSprite));

        btnNextCard.setHeight(btnNextCard.getHeight());
        btnPreviousCard.setHeight(btnNextCard.getHeight());

        btnPreviousCard.setPosition(customCard.getX() - 45,(customCard.getY() - AppInfo.PADDING) / 2 - btnNextCard.getHeight() / 2 - 20);
        btnNextCard.setPosition(customCard.getX() + 45 +customCard.getWidth() - btnNextCard.getWidth(),btnPreviousCard.getY());



        btnNextCard.setOrigin(Align.center);
        btnPreviousCard.setOrigin(Align.center);

        btnNextCard.addListener(new ButtonHoverAnimation(btnNextCard,true){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                nextClicked();
            }
        });
        btnPreviousCard.addListener(new ButtonHoverAnimation(btnPreviousCard,true){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                previousClicked();
            }
        });

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = Fonts.bl_bold_45;

        lblShowCardIndex = new Label("A",labelStyle);
        lblShowCardIndex.setWidth(btnNextCard.getX() - btnPreviousCard.getX() - btnPreviousCard.getWidth());
        lblShowCardIndex.setPosition(btnPreviousCard.getWidth() + btnPreviousCard.getX(),
                btnPreviousCard.getY() + btnPreviousCard.getHeight() / 2 - lblShowCardIndex.getPrefHeight() / 2);
        lblShowCardIndex.setAlignment(Align.center);

        stage.addActor(btnNextCard);
        stage.addActor(btnPreviousCard);
        stage.addActor(lblShowCardIndex);

    }

    private void nextClicked() {
        cardIndex++;
        if (cardIndex == wordCards.size) {
            cardIndex--;
            finishedWords = true;
        }else
            changeCard();
    }

    private void previousClicked() {
        cardIndex --;
        if (cardIndex == -1)
            cardIndex = 0;
        else
            changeCard();
    }

    private void createCardListTable() {

        if (cardListGroup != null){
            cardListGroup.remove();
        }
        cardListGroup = new Group();
        cardListGroup.setSize(AppInfo.WIDTH - 6 * AppInfo.PADDING - customCard.getX() -  customCard.getWidth() - (customCard.getWidth() * (customCard.getScaleX() - 1)) / 2,
                AppInfo.HEIGHT - btnPreviousCard.getY() - (AppInfo.HEIGHT - customCard.getY() - customCard.getHeight() - (customCard.getHeight() * (customCard.getScaleY() - 1)) / 2));
        cardListGroup.setPosition(AppInfo.WIDTH - 2 * AppInfo.PADDING - cardListGroup.getWidth(),
                btnPreviousCard.getY());

        BorderImage bg = new BorderImage(new Vector3(cardListGroup.getWidth(), cardListGroup.getHeight(),2));
        cardListGroup.addActor(bg);

        ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();


        Sprite scroll = new Sprite(Assets.assets.pix);
        Sprite knob = new Sprite(Assets.assets.pix);
        scrollPaneStyle.vScroll = new SpriteDrawable(scroll);
        scrollPaneStyle.vScrollKnob = new SpriteDrawable(knob);
        scrollPane = new SlowScrollPane(null, scrollPaneStyle);
        scrollPane.setVariableSizeKnobs(true);
        scrollPane.setOverscroll(false, false);
        scrollPane.setSize(bg.getWidth() - 6,bg.getHeight() - 6);
        scrollPane.setPosition(3,3);
        cardListGroup.addActor(scrollPane);

        table = new Table();
        table.top().center();
        table.setWidth(bg.getWidth());
        scrollPane.setActor(table);

        if (cardGroupMap == null)
            cardGroupMap = new ObjectMap<>();
        else
            cardGroupMap.clear();

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = Fonts.bl_bold_32;

        for (final StudyWordCard s :wordCards) {
            Label label = new Label(s.word.name,labelStyle);
            label.setPosition(10,5);

            BorderImage borderImage = new BorderImage(new Vector3(bg.getWidth() - 20,label.getPrefHeight() + 10,0), ColorPalette.darkBg,Color.WHITE);
            Group group = new Group();
            group.setSize(borderImage.getWidth(),borderImage.getHeight());
            group.addActor(borderImage);
            group.addActor(label);
            table.add(group).pad(5).row();

            group.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    //TODO: eğer ranmdomsa
                    if (mix) {
                        mix = false;
                        for (Actor a : mixGroup.getChildren()) {
                            a.setColor(Color.WHITE);
                        }
                    }

                    cardIndex = wordCards.indexOf(s,true);
                    changeCard();
                }
            });

            cardGroupMap.put(s,group);
        }

        stage.addActor(cardListGroup);

    }

    public void render(float delta) {
        Gdx.gl.glClearColor(ColorPalette.bg.r,ColorPalette.bg.g,ColorPalette.bg.b,ColorPalette.bg.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.end();

        if (auto){
            if (!pause)
                deltaAuto += delta;

            if (deltaAuto< questionTime){
                showSec.setWidth(stopGroup.getWidth() * (deltaAuto / questionTime));
                if (customCard.isAnswer)
                    deltaAuto = questionTime;
            }else if (deltaAuto >= questionTime && deltaAuto < (questionTime + answerTime)){
                if (!customCard.isAnswer && deltaAuto - questionTime < delta) {
                    customCard.changeCard();
                }
                showSec.setWidth(stopGroup.getWidth() -
                        stopGroup.getWidth() * ((deltaAuto-questionTime) / answerTime));

            }else if (deltaAuto > questionTime + answerTime){
                cardIndex++;
                showSec.setWidth(0);
                if (cardIndex == wordCards.size) {
                    cardIndex--;
                    auto = false;
                    finishedWords = true;
                    stopGroup.remove();
                    stage.addActor(playGroup);
                }else {
                    changeCard();
                    deltaAuto = 0;
                }
            }
        }


        ///check finished words

        if(finishedWords){
            if (customCard.isVisible()){
                createFinishedTable();
                customCard.setVisible(false);
                finishedGroup.setVisible(true);

            }
        }

        stage.act();
        stage.draw();
    }


    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);

    }
}
