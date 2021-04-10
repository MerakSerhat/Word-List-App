package com.serhatmerak.wordlist.huds.wordcardhuds;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
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
import com.serhatmerak.wordlist.huds.BorderImage;
import com.serhatmerak.wordlist.huds.MainWordCard;
import com.serhatmerak.wordlist.huds.ShowWindow;
import com.serhatmerak.wordlist.kumar.SlowScrollPane;
import com.serhatmerak.wordlist.screens.StudyCardScreen;
import com.serhatmerak.wordlist.word.Word;
import com.serhatmerak.wordlist.word.WordList;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class WordCardsHud {
    private StudyCardScreen studyCardScreen;

    public Group wordCardGroup;
    public Group topMenuGroup;

    private StudyWordCard.StudyWordCardStyle studyWordCardStyle;
    
    
    
    private int questionTime = 3;
    private int answerTime = 3;
    private boolean auto;
    private boolean pause;
    private float deltaAuto;
    private Group stopGroup;
    private Group playGroup;
    private boolean finishedWords;
    private Image showSec;
    private Image showSecBg;
    private Group showShortcutGroup;
    private boolean mix;
    /////////////////
    private Array<StudyWordCard> wordCards;
    private int cardIndex;
    private Image btnNextCard;
    private Image btnPreviousCard;
    public Label lblShowCardIndex;
    private SlowScrollPane wordCardScrollPane;

    private boolean isShortcutsOn = false;
    private Group wordTableButton;
    private Group keyboardGroup;
    private WordListOfWordScreen wordListOfWordScreen;


    public WordCardsHud(StudyWordCard.StudyWordCardStyle studyWordCardStyle, StudyCardScreen studyCardScreen){
        this.studyWordCardStyle = studyWordCardStyle;
        this.studyCardScreen = studyCardScreen;
        createTopMenuGroup();
        createCards();
        createActors();
        changeCard();
    }

    private void shortcuts() {
        wordCardGroup.getStage().addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.SPACE || keycode == Input.Keys.DOWN){
                    wordCards.get(cardIndex).changeCard();
                }else if (keycode == Input.Keys.RIGHT){
                    nextClicked();
                }else if (keycode == Input.Keys.LEFT){
                    previousClicked();
                }else if (keycode == Input.Keys.P){
                    pause = !pause;
                }else if (keycode == Input.Keys.S){
                    if (!finishedWords){
                        wordCards.get(cardIndex).starClicked();
                    }
                }else if (keycode == Input.Keys.K){
                    if (!finishedWords){
                        wordCards.get(cardIndex).dontShowClicked();
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
                }else if (keycode == Input.Keys.C){
                    if (!(topMenuGroup.getStage().getActors().get(topMenuGroup.getStage().getActors().size - 1) instanceof ShowWindow))
                        showCardClicked();
                    else {
                        topMenuGroup.getStage().getActors().get(topMenuGroup.getStage().getActors().size - 1).remove();
                        pause = false;
                    }

                }else if (keycode == Input.Keys.TAB){

                    showWordDataClicked();

                }else if (keycode == Input.Keys.W){

                    if (!(topMenuGroup.getStage().getActors().get(
                            topMenuGroup.getStage().getActors().size - 1) instanceof ShowWindow)) {
                        ShowWindow showWindow = new ShowWindow(wordListOfWordScreen);
                        topMenuGroup.getStage().addActor(showWindow);
                    }

                }else if (keycode == Input.Keys.ESCAPE){
                    if (wordCardGroup.getStage().getActors().get(wordCardGroup.getStage().getActors().size - 1) instanceof ShowWindow){
                        wordCardGroup.getStage().getActors().get(wordCardGroup.getStage().getActors().size - 1).remove();
                    }
                }
                return super.keyDown(event, keycode);
            }
        });


        Sprite scroll = new Sprite(Assets.assets.pix);
        Sprite knob = new Sprite(Assets.assets.pix);

        scroll.setSize(20, 100);
        scroll.setColor(Color.LIGHT_GRAY);
        knob.setSize(20, 20);
        knob.setColor(Color.DARK_GRAY);
        ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        scrollPaneStyle.vScrollKnob = new SpriteDrawable(knob);

        wordCardScrollPane = new SlowScrollPane(null, scrollPaneStyle);
        wordCardScrollPane.setScrollingDisabled(true, false);
        wordCardScrollPane.setVariableSizeKnobs(true);
        wordCardScrollPane.setOverscroll(false, true);
        wordCardScrollPane.setSize(AppInfo.WIDTH / 1.4f,900);
        wordCardScrollPane.setPosition(AppInfo.WIDTH / 2f - wordCardScrollPane.getWidth() / 2,
                (AppInfo.HEIGHT)/2f - wordCardScrollPane.getHeight() / 2 );
    }

    private void showWordDataClicked() {
        if (!(wordCardGroup.getStage().getActors().get(
                wordCardGroup.getStage().getActors().size - 1) instanceof ShowWindow)) {
            WordDataHud wordDataHud = new WordDataHud(wordCards.get(cardIndex).word);
            final ShowWindow showWindow = new ShowWindow(wordDataHud);
            wordCardGroup.getStage().addActor(showWindow);
            pause = true;
            showWindow.img.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    showWindow.remove();
                    pause = false;
                }
            });


        }
    }

    private void showCardClicked() {
        MainWordCard mainWordCard = new MainWordCard(wordCards.get(cardIndex).word,
                true,true,true,AppInfo.WIDTH / 1.4f) ;
        pause = true;
        if (mainWordCard.getHeight() > 900){
            wordCardScrollPane.setActor(mainWordCard);
            final ShowWindow showWindow = new ShowWindow(wordCardScrollPane);
            wordCardGroup.getStage().addActor(showWindow);
            showWindow.img.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    pause = false;
                    showWindow.remove();
                }
            });
        }else {
            final ShowWindow showWindow = new ShowWindow(mainWordCard);
            wordCardGroup.getStage().addActor(showWindow);
            showWindow.img.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    pause = false;
                    showWindow.remove();
                }
            });
        }

    }

    private void createActors() {
        wordCardGroup = new Group();
        wordCardGroup.setSize(AppInfo.WIDTH,AppInfo.HEIGHT -80);

        Sprite backSprite = new Sprite(Assets.assets.arrow);
        backSprite.flip(true,false);

        btnNextCard = new Image(Assets.assets.arrow);
        btnPreviousCard = new Image(new SpriteDrawable(backSprite));

//        btnNextCard.setSize(btnNextCard.getWidth() * 1.5f,btnNextCard.getHeight() / 1.7f);
//        btnPreviousCard.setSize(btnPreviousCard.getWidth() * 1.5f,btnNextCard.getHeight());

        btnPreviousCard.setPosition(2 * AppInfo.PADDING,
                (AppInfo.HEIGHT - 80) / 2f - btnPreviousCard.getHeight() / 2);
        btnNextCard.setPosition(
                AppInfo.WIDTH - 2 * AppInfo.PADDING - btnNextCard.getWidth(),btnPreviousCard.getY());



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
        lblShowCardIndex.setWidth(150);
        lblShowCardIndex.setPosition(AppInfo.WIDTH - AppInfo.PADDING - 200,
                1040 - lblShowCardIndex.getHeight() / 2);
        lblShowCardIndex.setAlignment(Align.right);

        wordCardGroup.addActor(btnNextCard);
        wordCardGroup.addActor(btnPreviousCard);
//        wordCardGroup.addActor(lblShowCardIndex);

    }


    private void createCards() {
        if (wordCards == null)
            wordCards = new Array<>();
        else
            wordCards.clear();

        WordList sortedList = DataHolder.dataholder.sortAZlist(DataHolder.dataholder.getSelectedList());


        for (int i = 0; i < sortedList.words.size; i++){
            Word word = sortedList.words.get(i);
            if (!(!studyWordCardStyle.EVERY_WORD && !word.star)) {

                final StudyWordCard studyWordCard = new StudyWordCard(word, studyWordCardStyle);
                studyWordCard.setPosition(AppInfo.WIDTH / 2f - studyWordCard.getWidth() / 2,
                        (AppInfo.HEIGHT - 80) / 2f - studyWordCard.getHeight() / 2);
                studyWordCard.setOrigin(Align.center);
                studyWordCard.setScale(1.25f);
                wordCards.add(studyWordCard);

                StudyWordCard.StudyWordCardListener studyWordCardListener = new StudyWordCard.StudyWordCardListener() {
                    @Override
                    public void showCardClicked() {
                        WordCardsHud.this.showCardClicked();
                    }

                    @Override
                    public void showWordDataClicked() {
                        WordCardsHud.this.showWordDataClicked();
                    }
                };
                studyWordCard.setListener(studyWordCardListener);
            }
        }
        cardIndex = 0;
        //TODO:
//        createFinishedTable();

    }



    private void nextClicked() {

        if (wordCards.get(cardIndex).getStage() != null) {
            wordCards.get(cardIndex).remove();
        }

        cardIndex++;
        if (cardIndex == wordCards.size) {
            cardIndex = 0;
        }

        changeCard();
    }

    private void previousClicked() {
        cardIndex --;
        if (cardIndex == -1)
            cardIndex = wordCards.size - 1;

        changeCard();
    }

    private void changeCard() {
        if (!finishedWords) {

            StudyWordCard studyWordCard = wordCards.get(cardIndex);

            if (mix) {
                studyWordCard = wordCards.random();
                cardIndex = wordCards.indexOf(studyWordCard,true);
            }

            wordCardGroup.addActor(studyWordCard);
            if (studyWordCard.isAnswer)
                studyWordCard.changeCard();

            lblShowCardIndex.setText((cardIndex + 1) + " / " + wordCards.size);

//            Label label = (Label) cardGroupMap.get(studyWordCard).getChild(1);
//            label.setColor(ColorPalette.text1);
//
//            float index = table.getChildren().indexOf(cardGroupMap.get(studyWordCard), true);
//            float size = table.getChildren().size;

            deltaAuto = 0;
//            scrollPane.setScrollY(Math.max(0, (scrollPane.getMaxY() / (size - 15))) * (index - 4));

        }
    }

    private void createTopMenuGroup() {
        topMenuGroup = new Group();
        topMenuGroup.setSize(1380,80);

        final MenuIconicButton mixButton = new MenuIconicButton(Assets.assets.mix,"Mix words (M)",
                new Vector2(4,4));

        final MenuIconicButton shortcutsButton = new MenuIconicButton(Assets.assets.keyboard,"Show shortcuts");
        createShortcutsTable();

        final MenuIconicButton restartButton = new MenuIconicButton(Assets.assets.restart,"New cards");

        final MenuIconicButton wordTableButton = new MenuIconicButton(Assets.assets.w_letter,"Word list (W)",new Vector2(5,5),35);

        createTimeButtons();
        final Actor emptyActor = new Actor();
        emptyActor.setWidth(stopGroup.getWidth());
        stopGroup.setWidth(0);
        playGroup.setWidth(0);
        stopGroup.setVisible(false);

        final Table table = new Table();
        table.center();
        table.defaults().padLeft(100);
        table.setSize(topMenuGroup.getWidth(),topMenuGroup.getHeight());

        table.add(mixButton);
        table.add(wordTableButton);
        table.add(restartButton);
        table.add(shortcutsButton);
        table.add(stopGroup);
        table.add(playGroup).padLeft(0);
        table.add(emptyActor).padLeft(0);
        table.padRight(100);

        topMenuGroup.addActor(table);

        mixButton.addListener(new ButtonHoverAnimation(mixButton,true){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                mixClicked();
            }
        });

        shortcutsButton.addListener(new ButtonHoverAnimation(shortcutsButton,true){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                final ShowWindow showWindow = new ShowWindow(showShortcutGroup);
                topMenuGroup.getStage().addActor(showWindow);
                showWindow.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        super.clicked(event, x, y);
                        showWindow.remove();
                    }
                });
            }
        });

        restartButton.addListener(new ButtonHoverAnimation(restartButton,true){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                studyCardScreen.restartClicked();

            }
        });

        wordListOfWordScreen = new WordListOfWordScreen(this);
        wordTableButton.addListener(new ButtonHoverAnimation(wordTableButton,true){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                ShowWindow showWindow = new ShowWindow(wordListOfWordScreen);
                topMenuGroup.getStage().addActor(showWindow);
            }
        });

        final Image line = new Image(Assets.assets.pix);
        line.setColor(ColorPalette.Orange);
        line.setSize(table.getWidth(),8);
        line.setPosition(table.getX(),-4);

        line.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if (line.getColor().equals(ColorPalette.Orange)){
                    table.addAction(Actions.moveBy(0,80,0.4f));
                    line.setColor(ColorPalette.Orange.r,ColorPalette.Orange.g,ColorPalette.Orange.b,
                            0.6f);
                }else {
                    table.addAction(Actions.moveBy(0,-80,0.4f));
                    line.setColor(ColorPalette.Orange);
                }
            }
        });
        topMenuGroup.addActor(line);

    }

    private void createWordTableButton() {

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = Fonts.bl_bold_32;

    }

    private void mixClicked() {
        mix = !mix;
        if (mix){
            ((Table)topMenuGroup.getChildren().first()).getChild(0).setColor(ColorPalette.text1);
        } else {
            ((Table)topMenuGroup.getChildren().first()).getChild(0).setColor(Color.WHITE);
        }
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
        table.add(new Label("C",labelStyle)).width(300).padLeft(30);
        table.add(new Label(":",labelStyle)).width(30);
        table.add(new Label("Show/Hide word table",labelStyle)).width(950).row();
        table.add(new Label("W",labelStyle)).width(300).padLeft(30);
        table.add(new Label(":",labelStyle)).width(30);
        table.add(new Label("Show word list table",labelStyle)).width(950).row();

        showShortcutGroup.setSize(borderImage.getWidth(),borderImage.getHeight());

        showShortcutGroup.addActor(borderImage);
        showShortcutGroup.addActor(table);
    }


    private void createTimeButtons() {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = Fonts.bl_bold_32;

        playGroup = new Group();
        playGroup.setHeight(80);

        MenuIconicButton playImage = new MenuIconicButton(Assets.assets.play,"Auto play",
                new Vector2(10,0) , 32);
        playImage.setPosition(0,playGroup.getHeight() / 2
                - playImage.getHeight() / 2);
        playImage.setOrigin(Align.center);




        Image increase1 = new Image(Assets.assets.increase);
        increase1.setSize(30,30);

        Sprite decSprite = new Sprite(Assets.assets.increase);
        decSprite.flip(false,true);
        Image decrease1 = new Image(decSprite);
        decrease1.setSize(increase1.getWidth(),increase1.getHeight());

        decrease1.setPosition(playImage.getWidth() + 90,playGroup.getHeight() / 2 -  decrease1.getHeight());
        increase1.setPosition(decrease1.getX(), decrease1.getY() + decrease1.getHeight());


        decrease1.setOrigin(Align.center);
        increase1.setOrigin(Align.center);


        Image numBg1 = new Image(Assets.assets.pix);
        Image numBg2 = new Image(Assets.assets.pix);

        numBg1.setSize(45,decrease1.getHeight() * 2 + 4);
        numBg2.setSize(numBg1.getWidth(),numBg1.getHeight());

        numBg1.setColor(Color.valueOf("374895"));
        numBg2.setColor(Color.valueOf("374895"));


        numBg1.setPosition(increase1.getX() - 5 - numBg1.getWidth(),decrease1.getY() - 2);


        ////////////////////////
        Image increase2 = new Image(Assets.assets.increase);
        Image decrease2 = new Image(decSprite);

        decrease2.setSize(increase1.getWidth(),increase1.getHeight());
        increase2.setSize(increase1.getWidth(),increase1.getHeight());

        decrease2.setPosition(increase1.getWidth() + increase1.getX() + 5, decrease1.getY());
        increase2.setPosition(decrease2.getX(), decrease2.getY() + decrease2.getHeight());

        decrease2.setOrigin(Align.center);
        increase2.setOrigin(Align.center);

        numBg2.setPosition(increase2.getX() + increase2.getWidth() + 5,numBg1.getY());
//        numBg2.setPosition(numBg1.getX() + numBg1.getWidth(),decrease1.getY());

        Label.LabelStyle numLabStyle = new Label.LabelStyle();
        numLabStyle.font = Fonts.bl_bold_26;

        final Label numLab1 = new Label("3\nsc",numLabStyle);
        final Label numLab2 = new Label("3\nsc",numLabStyle);

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
                    numLab1.setText(questionTime +"\nsc");
                }
            }
        });
        increase1.addListener(new ButtonHoverAnimation(increase1,true){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if (questionTime != 10){
                    questionTime++;
                    numLab1.setText(questionTime +"\nsc");
                }
            }
        });
        decrease2.addListener(new ButtonHoverAnimation(decrease2,true){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if (answerTime != 1){
                    answerTime--;
                    numLab2.setText(answerTime +"\nsc");
                }
            }
        });
        increase2.addListener(new ButtonHoverAnimation(increase2,true){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if (answerTime != 10){
                    answerTime++;
                    numLab2.setText(answerTime +"\nsc");
                }
            }
        });




        playGroup.addActor(numBg1);
        playGroup.addActor(numBg2);
        playGroup.addActor(numLab1);
        playGroup.addActor(numLab2);

        playGroup.addActor(playImage);
        playGroup.addActor(decrease1);
        playGroup.addActor(decrease2);
        playGroup.addActor(increase1);
        playGroup.addActor(increase2);







        stopGroup = new Group();
        MenuIconicButton stopImage = new MenuIconicButton(Assets.assets.pause,
                "Stop auto play",new Vector2(10,0),35);
        stopGroup.setHeight(80);
        stopImage.setPosition(0, stopGroup.getHeight() / 2 - stopImage.getHeight() / 2);

        stopGroup.setOrigin(Align.center);

        stopGroup.addActor(stopImage);

        for (Actor a :stopGroup.getChildren()) {
            a.setColor(ColorPalette.text1);
        }

        showSecBg = new Image(Assets.assets.pix);
        showSecBg.setSize(200,40);
        showSecBg.setColor( Color.valueOf("374895"));
        showSecBg.setPosition(stopImage.getX() + stopImage.getWidth() + 25,
                stopImage.getY() + stopImage.getHeight() / 2 - showSecBg.getHeight() / 2);

        showSec = new Image(Assets.assets.pix);
        showSec.setSize(0,40);
        showSec.setColor(ColorPalette.text1);
        showSec.setPosition(showSecBg.getX(),showSecBg.getY());

        stopGroup.addActor(showSecBg);
        stopGroup.addActor(showSec);

        stopImage.addListener(new ButtonHoverAnimation(stopImage, true){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                stopClicked();
            }
        });

        playImage.addListener(new ButtonHoverAnimation(playImage, true){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                playClicked();
            }
        });

        stopGroup.setWidth(showSec.getX() + showSecBg.getWidth());

    }

    private void stopClicked(){
        if (auto){
            auto = false;
            stopGroup.setVisible(false);
            playGroup.setVisible(true);
        }
    }

    private void playClicked(){
        if (!auto && !finishedWords){
            pause = false;
            auto = true;
            stopGroup.setVisible(true);
            playGroup.setVisible(false);
            deltaAuto = 0;
        }
    }

    public void act(float delta){

        if (auto){
            if (!pause)
                deltaAuto += delta;

            if (deltaAuto< questionTime){
                showSec.setWidth(showSecBg.getWidth() * (deltaAuto / questionTime));
                System.out.println(showSec.getWidth());
                if ( wordCards.get(cardIndex).isAnswer)
                    deltaAuto = questionTime;
            }else if (deltaAuto >= questionTime && deltaAuto < (questionTime + answerTime)){
                if (!wordCards.get(cardIndex).isAnswer && deltaAuto - questionTime < delta) {
                    wordCards.get(cardIndex).changeCard();
                }
                showSec.setWidth(showSecBg.getWidth() -
                        showSecBg.getWidth() * ((deltaAuto-questionTime) / answerTime));

            }else if (deltaAuto > questionTime + answerTime){

                showSec.setWidth(0);
                if (cardIndex == wordCards.size - 1) {
                    auto = false;
                    finishedWords = true;
                    stopGroup.setVisible(false);
                    playGroup.setVisible(true);

                }else {
                    wordCards.get(cardIndex).remove();
                    cardIndex++;
                    changeCard();
                    deltaAuto = 0;
                }
            }
        }

        if (!isShortcutsOn && wordCardGroup.getStage()!= null){
            shortcuts();
            isShortcutsOn = true;
        }

    }

    public void changeCardWithIndex(int index){
        cardIndex = index;
        changeCard();
    }
}
