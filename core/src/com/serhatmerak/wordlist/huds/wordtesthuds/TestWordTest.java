package com.serhatmerak.wordlist.huds.wordtesthuds;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.serhatmerak.wordlist.helpers.AppInfo;
import com.serhatmerak.wordlist.helpers.Assets;
import com.serhatmerak.wordlist.helpers.ButtonHoverAnimation;
import com.serhatmerak.wordlist.helpers.ColorPalette;
import com.serhatmerak.wordlist.helpers.DataHolder;
import com.serhatmerak.wordlist.helpers.Fonts;
import com.serhatmerak.wordlist.huds.Alert;
import com.serhatmerak.wordlist.huds.MainWordCard;
import com.serhatmerak.wordlist.huds.RoundedRectangle;
import com.serhatmerak.wordlist.huds.ShowWindow;
import com.serhatmerak.wordlist.kumar.SlowScrollPane;
import com.serhatmerak.wordlist.word.Word;

import java.util.Arrays;
import java.util.function.Consumer;


public class TestWordTest {

    private boolean EVERY_WORD = true;
    private boolean DEF_WORD = true;
    private int NUMBER_OF_QUESTION = 20;
    private boolean USE_TURKISH = false;
    private boolean SHOW_ANSWERS = false;



    private int QuestionIndex = 0;
    public Array<Word> answerWords;
    private Array<Array<Word>> optionWords;
    private Array<Group> changeQuestionButtons;
    public Array<Integer> rightAnswers;
    public int[] answers;
    /*
    A = 0
    B = 1
    C = 2
    D = 3
    E = 4
     */
    
    public Group testPageGroup;
    private Group questionGroup;
    private Image btnUp,btnDown;
    private Image btnExpand;
    private SlowScrollPane buttonsScrollPane;
    private Group changingQuestionGroup;
    private TextButton btnNextQuestion;

    public TestWordTest(){
        answerWords = new Array<>();
        optionWords = new Array<>();
        changeQuestionButtons = new Array<>();
        rightAnswers = new Array<>();
        createTestPageGroup();
        createSomeCertainActors();
    }

    private void createSomeCertainActors() {
        questionGroup = new Group();
        questionGroup.setSize(1450,1000);


        final RoundedRectangle wholeBg = new RoundedRectangle((int) (AppInfo.WIDTH - 15 * AppInfo.PADDING - questionGroup.getWidth()),
                800 + 2 * AppInfo.PADDING,15);
        wholeBg.setColor(ColorPalette.TopBlue);
        wholeBg.setPosition(2 * AppInfo.PADDING,
                testPageGroup.getHeight() / 2 - wholeBg.getHeight() / 2);
        wholeBg.setZIndex(0);

        wholeBg.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if (changeQuestionButtons.first().getParent() instanceof Table){
                    for (Group g:changeQuestionButtons) {
                        g.addAction(Actions.fadeOut(0.2f));
                    }
                    buttonsScrollPane.addAction(Actions.sequence(
                            Actions.delay(0.2f),
                            Actions.run(new Runnable() {
                                @Override
                                public void run() {
                                    buttonsScrollPane.setVisible(false);
                                    for (Group g:changeQuestionButtons) {
                                        g.setVisible(false);
                                        changingQuestionGroup.addActor(g);
                                        g.addAction(Actions.fadeIn(0.2f));
                                    }
                                    changingQuestionGroup.addAction(Actions.fadeIn(0.2f));
                                    questionChanged();
                                }
                            })
                    ));
                }
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                if (changeQuestionButtons.first().getParent() instanceof Table) {
                    wholeBg.setColor(wholeBg.getColor().r,
                            wholeBg.getColor().g,wholeBg.getColor().b,1);
                }
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                if (changeQuestionButtons.first().getParent() instanceof Table) {
                    wholeBg.setColor(wholeBg.getColor().r,
                            wholeBg.getColor().g,wholeBg.getColor().b,0.6f);                }
            }
        });



        testPageGroup.addActor(wholeBg);



        RoundedRectangle roundedRectangle = new RoundedRectangle((int)wholeBg.getWidth() - 60,80,15);

        Sprite hoverSprite = new Sprite(roundedRectangle.lastTexture);
        hoverSprite.setColor(ColorPalette.ButtonOverBlue);

        Sprite normSprite = new Sprite(roundedRectangle.lastTexture);
        normSprite.setColor(ColorPalette.ButtonBlue);


        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = Fonts.bl_bold_36;
        textButtonStyle.up = new SpriteDrawable(normSprite);
        textButtonStyle.over = new SpriteDrawable(hoverSprite);
        textButtonStyle.fontColor = ColorPalette.TopBlue;

        btnNextQuestion = new TextButton("Next", textButtonStyle);
        btnNextQuestion.setSize(roundedRectangle.getWidth(),roundedRectangle.getHeight());

        btnNextQuestion.setPosition(wholeBg.getX() + 30,wholeBg.getY() + 2 * AppInfo.PADDING);
        testPageGroup.addActor(btnNextQuestion);
        btnNextQuestion.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
               nextQuestionClicked(true);
            }
        });

        TextButton btnShowCard = new TextButton("Show Card", textButtonStyle);
        btnShowCard.setSize(roundedRectangle.getWidth(),roundedRectangle.getHeight());

        btnShowCard.setPosition(wholeBg.getX() + 30,
                wholeBg.getY() + wholeBg.getHeight() - btnShowCard.getHeight() - 2 * AppInfo.PADDING);
        testPageGroup.addActor(btnShowCard);

        Sprite scroll = new Sprite(Assets.assets.pix);
        Sprite knob = new Sprite(Assets.assets.pix);

        scroll.setSize(20, 100);
        scroll.setColor(Color.LIGHT_GRAY);
        knob.setSize(20, 20);
        knob.setColor(Color.DARK_GRAY);
        ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        scrollPaneStyle.vScrollKnob = new SpriteDrawable(knob);

        final SlowScrollPane wordCardScrollPane = new SlowScrollPane(null, scrollPaneStyle);
        wordCardScrollPane.setScrollingDisabled(true, false);
        wordCardScrollPane.setVariableSizeKnobs(true);
        wordCardScrollPane.setOverscroll(false, true);
        wordCardScrollPane.setSize(AppInfo.WIDTH / 1.4f,900);
        wordCardScrollPane.setPosition(AppInfo.WIDTH / 2f - wordCardScrollPane.getWidth() / 2,
                (AppInfo.HEIGHT)/2f - wordCardScrollPane.getHeight() / 2 );


        btnShowCard.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                MainWordCard mainWordCard = new MainWordCard(answerWords.get(QuestionIndex),
                        true,true,true,AppInfo.WIDTH / 1.4f) ;
                if (mainWordCard.getHeight() > 900){
                    wordCardScrollPane.setActor(mainWordCard);
                    final ShowWindow showWindow = new ShowWindow(wordCardScrollPane);
                    testPageGroup.getStage().addActor(showWindow);
                    showWindow.img.addListener(new ClickListener(){
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            super.clicked(event, x, y);
                            showWindow.remove();
                        }
                    });
                }else {
                    final ShowWindow showWindow = new ShowWindow(mainWordCard);
                    testPageGroup.getStage().addActor(showWindow);
                    showWindow.img.addListener(new ClickListener(){
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            super.clicked(event, x, y);
                            showWindow.remove();
                        }
                    });
                }

            }
        });




    }

    public void nextQuestionClicked(boolean next) {
        if (next) {
            if (QuestionIndex < NUMBER_OF_QUESTION - 1) {
                QuestionIndex++;
                questionGroup.remove();
                createQuestion();
                questionChanged();
            }
        }else {
            if (QuestionIndex !=0) {
                QuestionIndex--;
                questionGroup.remove();
                createQuestion();
                questionChanged();
            }
        }
    }

    private void createTestPageGroup() {
        testPageGroup = new Group();
        testPageGroup.setSize(AppInfo.WIDTH,AppInfo.HEIGHT - 80);

        testPageGroup.addListener(new ClickListener(){
//            @Override
////            public void clicked(InputEvent event, float x, float y) {
////                super.clicked(event, x, y);
////                QuestionIndex++;
////                questionGroup.remove();
////                createQuestion();
////                questionChanged();
////            }


            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                super.touchDragged(event, x, y, pointer);
//                for (int i = 0; i < NUMBER_OF_QUESTION; i++) {
//                    //TODO: şuraya şöyle bi red green yap sonra ççevirirsin
//                    if (answers[i] != -1) {
//                        if (answers[i] == rightAnswers.get(i)) {
//                            changeQuestionButtons.get(i).getChild(0).setColor(Color.GREEN);
//                        } else
//                            changeQuestionButtons.get(i).getChild(0).setColor(Color.RED);
//                    }
//                }
            }
        });
    }

    public void createTest(){
        answerWords.clear();
        optionWords.clear();
        rightAnswers.clear();
        answers = new int[NUMBER_OF_QUESTION];
        changeQuestionButtons.clear();

        for (int i = 0; i < NUMBER_OF_QUESTION; i++) {
            answers[i] = -1;
        }

        QuestionIndex = 0;
        
        for (Word w : DataHolder.dataholder.getSelectedList().words) {
            if (EVERY_WORD || w.star) {
//                if (!DataHolder.dataholder.knownWords.contains(w.name,false))
                    answerWords.add(w);
            }
        }
        answerWords.shuffle();

        for (int i = 0;i < NUMBER_OF_QUESTION;i++){
            Array<Word> optionsForOneWord = new Array<>();
            while (optionsForOneWord.size < 4){
                Word word = answerWords.random();
                if (!word.name.equals(answerWords.get(i).name))
                    optionsForOneWord.add(word);
            }
            //right answer
            optionsForOneWord.add(answerWords.get(i));
            optionsForOneWord.shuffle();

            rightAnswers.add(optionsForOneWord.indexOf(answerWords.get(i),true));

            optionWords.add(optionsForOneWord);
        }

        createQuestion();
        createChangingQuestionButtons();
        /*
        //Writing
        for (int i = 0; i < NUMBER_OF_QUESTION; i++) {
            System.out.print((i+1) + ") ");
            if (DEF_WORD){
                if (USE_TURKISH)
                    System.out.println(answerWords.get(i).meaningDivs.first().trDef);
                else
                    System.out.println(answerWords.get(i).meaningDivs.first().engDef);

                System.out.println();
                String option = "A";
                for (Word w:optionWords.get(i)) {
                    System.out.println(option + ") " + w.name);
                    switch (option) {
                        case "A":
                            option = "B";
                            break;
                        case "B":
                            option = "C";
                            break;
                        case "C":
                            option = "D";
                            break;
                        case "D":
                            option = "E";
                            break;
                    }
                }
            }
            else {

                System.out.println(answerWords.get(i).name);

                System.out.println();
                String option = "A";
                for (Word w:optionWords.get(i)) {
                    if (USE_TURKISH)
                        System.out.println(option + ") " + w.meaningDivs.first().trDef);
                    else
                        System.out.println(option + ") " + w.meaningDivs.first().engDef);
                    switch (option) {
                        case "A":
                            option = "B";
                            break;
                        case "B":
                            option = "C";
                            break;
                        case "C":
                            option = "D";
                            break;
                        case "D":
                            option = "E";
                            break;
                    }
                }
            }

            if (SHOW_ANSWERS){
                String chars = "ABCDE";
                System.out.println("\n (" + chars.substring(answers.get(i),answers.get(i) + 1) + ")");
            }

            System.out.println("\n**************************\n");
        }

        if (!SHOW_ANSWERS){
            for (int i = 0; i < NUMBER_OF_QUESTION; i++) {
                String chars = "ABCDE";
                System.out.println((i+1) + ") " + chars.substring(answers.get(i),answers.get(i) + 1));
            }
        }

         */

    }

    private void createChangingQuestionButtons() {
        changingQuestionGroup = new Group();
        changingQuestionGroup.setSize(0,610);

        final int border = 5;
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = Fonts.bl_bold_36;
        labelStyle.fontColor = Color.BLACK;
        for (int i = 0; i < NUMBER_OF_QUESTION; i++) {
            Group changeButton = new Group();
            RoundedRectangle borderRec = new RoundedRectangle(70,70,15);
            RoundedRectangle innerRec = new RoundedRectangle((int)borderRec.getWidth() - 2 *border,
                    (int)borderRec.getHeight() - 2 * border,15);

//            borderRec.setColor(Color.valueOf("ffcd1f"));
            borderRec.setColor(Color.valueOf("ffeeb3"));
            innerRec.setColor(Color.WHITE);

            Label number = new Label((i + 1 )+ "",labelStyle);
            changeButton.setSize(borderRec.getWidth(),borderRec.getHeight());

            innerRec.setPosition(border,border);
            number.setPosition(borderRec.getWidth() / 2 - number.getPrefWidth() / 2,
                    borderRec.getHeight() / 2 - number.getHeight() / 2);


            changeButton.addActor(borderRec);
//            changeButton.addActor(innerRec);
            changeButton.addActor(number);
            changeButton.setPosition(15,15);
            changeButton.setSize(borderRec.getWidth(),borderRec.getHeight());
            changeButton.setOrigin(Align.center);

            final int indexOfButton = i;
            changeButton.addListener(new ButtonHoverAnimation(changeButton,true){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    QuestionIndex = indexOfButton;
                    questionGroup.remove();
                    createQuestion();
                    questionChanged();
                }
            });

            changeButton.setVisible(false);

//            testPageGroup.addActor(changeButton);
            changingQuestionGroup.addActor(changeButton);
            changeQuestionButtons.add(changeButton);
        }

        changingQuestionGroup.setPosition(3 * AppInfo.PADDING,
                testPageGroup.getHeight() / 2 - changingQuestionGroup.getHeight() / 2);

        testPageGroup.addActor(changingQuestionGroup);

        //first entry
        changeQuestionButtons.first().setVisible(true);
        changeQuestionButtons.get(1).setVisible(true);
        changeQuestionButtons.get(2).setVisible(true);

        changeQuestionButtons.first().setPosition(60,270);
        changeQuestionButtons.get(1).setPosition(30,180);
        changeQuestionButtons.get(2).setPosition(0,90);

        changeQuestionButtons.get(1).setScale(0.9f);
        changeQuestionButtons.get(2).setScale(0.8f);

        //Create Expand Buttons
        Sprite sptBtnUp = new Sprite(Assets.assets.expand);
        Sprite sptBtnExpand = new Sprite(Assets.assets.expand);

        sptBtnUp.flip(false,true);
        sptBtnExpand.setOrigin(sptBtnExpand.getWidth() / 2,
                sptBtnExpand.getHeight() / 2);


        btnUp = new Image(sptBtnUp);
        btnDown = new Image(Assets.assets.expand);
        btnExpand = new Image(Assets.assets.expand);
        btnExpand.setOrigin(Align.center);
        btnExpand.rotateBy(90);

        btnUp.setSize(50,(50 / btnUp.getWidth()) * btnUp.getHeight());
        btnDown.setSize(50,(50 / btnDown.getWidth()) * btnDown.getHeight());
        btnExpand.setSize(40,(40 / btnDown.getWidth()) * btnDown.getHeight());



        btnDown.setPosition(10,55 - btnDown.getHeight() / 2);
        btnUp.setPosition(10,520 + 35 - btnUp.getHeight() / 2);
        btnExpand.setPosition(-10,270 + 35 - btnExpand.getHeight() / 2);

        btnDown.setOrigin(Align.center);
        btnUp.setOrigin(Align.center);

//        changingQuestionGroup.addActor(btnDown);
//        changingQuestionGroup.addActor(btnUp);
        changingQuestionGroup.addActor(btnExpand);


        btnUp.addListener(new ButtonHoverAnimation(btnUp,true){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                QuestionIndex = 0;
                questionGroup.remove();
                createQuestion();
                questionChanged();
            }
        });
        btnDown.addListener(new ButtonHoverAnimation(btnDown,true){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                QuestionIndex = NUMBER_OF_QUESTION - 1;
                questionGroup.remove();
                createQuestion();
                questionChanged();
            }
        });

        ///////Long List
        ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();

        Sprite scroll = new Sprite(Assets.assets.pix);
        Sprite knob = new Sprite(Assets.assets.pix);

        scroll.setSize(5,100);
        scroll.setColor(Color.LIGHT_GRAY);
        knob.setSize(5,20);
        knob.setColor(Color.DARK_GRAY);
        scrollPaneStyle.vScroll = new SpriteDrawable(scroll);
        scrollPaneStyle.vScrollKnob = new SpriteDrawable(knob);

        buttonsScrollPane = new SlowScrollPane(null, scrollPaneStyle);
        buttonsScrollPane.setScrollingDisabled(true,false);
        buttonsScrollPane.setVariableSizeKnobs(true);
        buttonsScrollPane.setOverscroll(false, true);
        buttonsScrollPane.setSize(210 + 4 * AppInfo.PADDING,
                400 + 6 * AppInfo.PADDING);
        buttonsScrollPane.setPosition(2 * AppInfo.PADDING,
                changingQuestionGroup.getY() + changingQuestionGroup.getHeight() / 2 -
                buttonsScrollPane.getHeight() / 2);
        testPageGroup.addActor(buttonsScrollPane);
//        buttonsScrollPane.setColor(1,1,1,0);
        buttonsScrollPane.setVisible(false);

        final Table table = new Table();
        table.setWidth(buttonsScrollPane.getWidth());
        table.top().left();

        buttonsScrollPane.setActor(table);


        btnExpand.addListener(new ButtonHoverAnimation(btnExpand,true){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                changingQuestionGroup.addAction(Actions.sequence(
                        Actions.fadeOut(0.2f, Interpolation.fastSlow),
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                table.clear();
                                buttonsScrollPane.setVisible(true);
                                buttonsScrollPane.setColor(1,1,1,0);
                                buttonsScrollPane.addAction(Actions.fadeIn(0.2f, Interpolation.slowFast));

                                for (int i = 0; i < changeQuestionButtons.size; i++) {
                                    changeQuestionButtons.get(i).setVisible(true);
                                    changeQuestionButtons.get(i).setScale(1,1);
                                    changeQuestionButtons.get(i).setColor(1,1,1,0);
                                    table.add(changeQuestionButtons.get(i)).padLeft(AppInfo.PADDING);
                                    if (i%3 == 2)
                                        table.row().padTop(AppInfo.PADDING);
                                    changeQuestionButtons.get(i).addAction(Actions.fadeIn(0.2f, Interpolation.slowFast));
                                }
                                table.pack();
                                changeQuestionButtons.get(QuestionIndex).addAction(Actions.sequence(
                                        Actions.delay(0.2f),Actions.alpha(0.7f)
                                ));
                            }
                        })

                ));

            }
        });

    }

    private void questionChanged() {

        if (changeQuestionButtons.first().getParent() instanceof Table) {
            for (Group g : changeQuestionButtons) {
                g.setColor(1,1,1,1);
            }

            changeQuestionButtons.get(QuestionIndex).setColor(1,1,1,0.7f);
        }else {
            for (Group g : changeQuestionButtons) {
                g.setVisible(false);
            }

            changeQuestionButtons.get(QuestionIndex).addAction(Actions.sequence(
                    Actions.visible(true),
                    Actions.parallel(
                            Actions.scaleTo(1, 1, 0.2f),
                            Actions.moveTo(60, 270, 0.2f)
                    )
            ));

            if (QuestionIndex + 1 < NUMBER_OF_QUESTION)
                changeQuestionButtons.get(QuestionIndex + 1).addAction(Actions.sequence(
                        Actions.visible(true),
                        Actions.parallel(
                                Actions.scaleTo(0.9f, 0.9f, 0.2f),
                                Actions.moveTo(30, 180, 0.2f)
                        )
                ));

            if (QuestionIndex + 2 < NUMBER_OF_QUESTION)
                changeQuestionButtons.get(QuestionIndex + 2).addAction(Actions.sequence(
                        Actions.visible(true),
                        Actions.parallel(
                                Actions.scaleTo(0.8f, 0.8f, 0.2f),
                                Actions.moveTo(0, 90, 0.2f)
                        )
                ));


            if (QuestionIndex - 1 >= 0)
                changeQuestionButtons.get(QuestionIndex - 1).addAction(Actions.sequence(
                        Actions.visible(true),
                        Actions.parallel(
                                Actions.scaleTo(0.9f, 0.9f, 0.2f),
                                Actions.moveTo(30, 360, 0.2f)
                        )
                ));

            if (QuestionIndex - 2 >= 0)
                changeQuestionButtons.get(QuestionIndex - 2).addAction(Actions.sequence(
                        Actions.visible(true),
                        Actions.parallel(
                                Actions.scaleTo(0.8f, 0.8f, 0.2f),
                                Actions.moveTo(0, 450, 0.2f)
                        )
                ));

        }


//        if (QuestionIndex == answers.length - 1 && btnNextQuestion.getText().equals("Next")){
//            btnNextQuestion.setText("Finish");
//        }else if (QuestionIndex != answers.length - 1 && btnNextQuestion.getText().equals("Finish")){
//            btnNextQuestion.setText("Next");
//        }
    }

    private void createQuestion() {
        questionGroup.clear();

        String questionString = "";
        String[] optionStrings = new String[5];

        //Finding Strings
        if (DEF_WORD){
            if (USE_TURKISH){
                questionString = answerWords.get(QuestionIndex).meaningDivs.first().trDef;
            }else {
                questionString = answerWords.get(QuestionIndex).meaningDivs.first().engDef;
            }
            for (int i = 0; i < 5; i++) {
                optionStrings[i] = optionWords.get(QuestionIndex).get(i).meaningDivs.first().title;
            }
        }else {
            questionString = answerWords.get(QuestionIndex).meaningDivs.first().title;
            if (USE_TURKISH){
                for (int i = 0; i < 5; i++) {
                    optionStrings[i] = optionWords.get(QuestionIndex).get(i).meaningDivs.first().trDef;
                }
            }else {
                for (int i = 0; i < 5; i++) {
                    optionStrings[i] = optionWords.get(QuestionIndex).get(i).meaningDivs.first().engDef;
                }
            }
        }

        Label.LabelStyle bigLabelStyle = new Label.LabelStyle();
        bigLabelStyle.font = Fonts.bl_bold_36;
        bigLabelStyle.fontColor = Color.BLACK;

        Label.LabelStyle smallLabelStyle = new Label.LabelStyle();
        smallLabelStyle.font = Fonts.bl_bold_32;
        smallLabelStyle.fontColor = Color.BLACK;

        Label questionLabel = new Label(questionString,bigLabelStyle);
        questionLabel.setWidth(questionGroup.getWidth() - 100);
        questionLabel.setWrap(true);

        //if it is one line
        if (questionLabel.getPrefHeight() < 50)
            questionLabel.setAlignment(Align.center);

        RoundedRectangle questionBg = new RoundedRectangle((int)questionLabel.getWidth() + 4 * AppInfo.PADDING,
                (int)questionLabel.getPrefHeight() + 4 * AppInfo.PADDING,30);

        float height = questionBg.getHeight();


        final Array<Group> optionsFromAtoE = new Array<>();
        String letters = "ABCDE";
        for (int i = 0; i < 5; i++) {
            Group option = new Group();
            option.setWidth(questionBg.getWidth());

            Label optionLabel = new Label(optionStrings[i],bigLabelStyle);
            optionLabel.setWidth(questionLabel.getWidth() - 80);
            optionLabel.setWrap(true);
            if (optionLabel.getPrefHeight()>100)
                optionLabel.setStyle(smallLabelStyle);
            optionLabel.setHeight(optionLabel.getPrefHeight());

            final RoundedRectangle optionBg = new RoundedRectangle((int)optionLabel.getWidth() + 4 * AppInfo.PADDING,
                    (int)optionLabel.getPrefHeight() + 2 * AppInfo.PADDING,10);
            height += optionBg.getHeight();

            Label letterLabel = new Label(letters.substring(i,i+1),bigLabelStyle);
            final RoundedRectangle letterBg = new RoundedRectangle(60,(int)optionBg.getHeight(),10);

            option.setHeight(letterBg.getHeight());

            option.addActor(letterBg);
            option.addActor(optionBg);
            option.addActor(optionLabel);
            option.addActor(letterLabel);

//            letterBg.setColor(Color.valueOf("fff9e3"));
//            optionBg.setColor(Color.valueOf("fff9e3"));


            letterBg.setPosition(0,0);
            optionBg.setPosition(80,0);
            optionLabel.setPosition(optionBg.getX() + 2 * AppInfo.PADDING,
                    optionBg.getHeight() / 2 - optionLabel.getPrefHeight() / 2);
            letterLabel.setPosition(letterBg.getWidth() / 2 - letterLabel.getPrefWidth() / 2,
                    letterBg.getHeight() - letterLabel.getPrefHeight() - AppInfo.PADDING);
            optionsFromAtoE.add(option);

            final int optionIndex = i;
            option.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    if (!letterBg.getColor().equals(Color.WHITE)) {
                        //Learning new thing
                        for (Group g:optionsFromAtoE) {
                            g.getChild(0).setColor(Color.WHITE);
                            g.getChild(1).setColor(Color.WHITE);
                        }

                        changeQuestionButtons.get(QuestionIndex).getChild(0).setColor(Color.valueOf("ffeeb3"));
                        answers[QuestionIndex] = -1;

                    }else {
                        for (Group g : optionsFromAtoE) {
                            g.getChild(0).setColor(Color.WHITE);
                            g.getChild(1).setColor(Color.WHITE);
                        }

                        changeQuestionButtons.get(QuestionIndex).getChild(0).setColor(ColorPalette.Orange);
                        answers[QuestionIndex] = optionIndex;
                        if (SHOW_ANSWERS){
                            letterBg.setColor(Color.RED);
                            optionBg.setColor(Color.RED);
                            optionsFromAtoE.get(rightAnswers.get(QuestionIndex)).getChild(0).setColor(Color.GREEN);
                            optionsFromAtoE.get(rightAnswers.get(QuestionIndex)).getChild(1).setColor(Color.GREEN);
                        }else {
                            letterBg.setColor(ColorPalette.Orange);
                            optionBg.setColor(ColorPalette.Orange);

                        }
                    }
                }
            });
        }

        height += 4 * 25 + 75;



        questionBg.setPosition(questionGroup.getWidth() / 2 - questionBg.getWidth() / 2,
                (questionGroup.getHeight() - height) / 2 + height - questionBg.getHeight());
        questionLabel.setPosition(questionBg.getX() + questionBg.getWidth() / 2 - questionLabel.getWidth() / 2,
                questionBg.getY() + questionBg.getHeight() / 2 - questionLabel.getHeight() / 2);

        questionGroup.addActor(questionBg);
        questionGroup.addActor(questionLabel);

        for (int i = 0; i < 5; i++) {
            if (i == 0) {
                optionsFromAtoE.first().setPosition(questionBg.getX(),
                        questionBg.getY() - 75 - optionsFromAtoE.first().getHeight());
            }else {
                optionsFromAtoE.get(i).setPosition(questionBg.getX(),
                        optionsFromAtoE.get(i - 1).getY() - 25 - optionsFromAtoE.get(i).getHeight());
            }

            questionGroup.addActor(optionsFromAtoE.get(i));

        }

        testPageGroup.addActor(questionGroup);
        questionGroup.setPosition(AppInfo.WIDTH - questionBg.getWidth() - AppInfo.PADDING * 7,0);

        if (answers[QuestionIndex] != -1){
            if (SHOW_ANSWERS){
                optionsFromAtoE.get(answers[QuestionIndex]).getChild(0).setColor(Color.RED);
                optionsFromAtoE.get(answers[QuestionIndex]).getChild(1).setColor(Color.RED);

                optionsFromAtoE.get(rightAnswers.get(QuestionIndex)).getChild(0).setColor(Color.GREEN);
                optionsFromAtoE.get(rightAnswers.get(QuestionIndex)).getChild(1).setColor(Color.GREEN);
            }else {
                optionsFromAtoE.get(answers[QuestionIndex]).getChild(0).setColor(ColorPalette.Orange);
                optionsFromAtoE.get(answers[QuestionIndex]).getChild(1).setColor(ColorPalette.Orange);
            }
        }
    }

    public void setTestType(boolean EVERY_WORD,boolean DEF_WORD,int NUMBER_OF_QUESTION
    ,boolean USE_TURKISH,boolean SHOW_ANSWERS){
        this.EVERY_WORD = EVERY_WORD;
        this.DEF_WORD = DEF_WORD;
        this.NUMBER_OF_QUESTION = NUMBER_OF_QUESTION;
        this.USE_TURKISH = USE_TURKISH;
        this.SHOW_ANSWERS = SHOW_ANSWERS;
    }



}
