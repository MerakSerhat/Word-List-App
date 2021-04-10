package com.serhatmerak.wordlist.huds.wordtesthuds;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Array;
import com.serhatmerak.wordlist.helpers.AppInfo;
import com.serhatmerak.wordlist.helpers.Assets;
import com.serhatmerak.wordlist.helpers.Fonts;
import com.serhatmerak.wordlist.huds.MainWordCard;
import com.serhatmerak.wordlist.huds.RoundedRectangle;
import com.serhatmerak.wordlist.kumar.SlowScrollPane;
import com.serhatmerak.wordlist.screens.WordTestScreen;
import com.serhatmerak.wordlist.word.Word;

import java.awt.Font;
import java.util.Arrays;

public class ResultWordTest {

    private WordTestScreen wordTestScreen;
    private Array<Word> words;
    private  Array<Integer> rightAnswer;
    private int[] answers;

    private int right = 0;
    private int wrong = 0;
    private int empty = 0;

    public Group resultWordGroup;
    private MainWordCard mainWordCard;
    private ScrollPane.ScrollPaneStyle scrollPaneStyle;
    private SlowScrollPane wordCardScrollPane;


    public ResultWordTest(WordTestScreen wordTestScreen, Array<Word> words,
                          Array<Integer> rightAnswers,int[] answers){
        this.wordTestScreen = wordTestScreen;
        this.words = words;
        this.rightAnswer = rightAnswers;
        this.answers = answers;

        for (int i = 0; i < rightAnswers.size;i++) {
            if (answers[i] == rightAnswers.get(i))
                right++;
            else if (answers[i] == -1)
                empty++;
            else wrong++;
        }

        createActors();
    }

    private void createActors() {
        resultWordGroup = new Group();
        resultWordGroup.setSize(AppInfo.WIDTH,AppInfo.HEIGHT - 80);

        createWordCardGroup();
        createResultGroup();
        createWordsGroup();

    }

    private void createWordCardGroup() {
        Sprite scroll = new Sprite(Assets.assets.pix);
        Sprite knob = new Sprite(Assets.assets.pix);

        scroll.setSize(20, 100);
        scroll.setColor(Color.LIGHT_GRAY);
        knob.setSize(20, 20);
        knob.setColor(Color.DARK_GRAY);
        scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        scrollPaneStyle.vScrollKnob = new SpriteDrawable(knob);

        wordCardScrollPane = new SlowScrollPane(null, scrollPaneStyle);
        wordCardScrollPane.setScrollingDisabled(true, false);
        wordCardScrollPane.setVariableSizeKnobs(true);
        wordCardScrollPane.setOverscroll(false, true);
        wordCardScrollPane.setPosition(0, 0);
        resultWordGroup.addActor(wordCardScrollPane);

    }

    private void createWordsGroup() {
        final Group wordsGroup = new Group();
        wordsGroup.setSize(400,840);
        resultWordGroup.addActor(wordsGroup);
        wordsGroup.setPosition(2 * AppInfo.PADDING,80);


        RoundedRectangle bg = new RoundedRectangle((int)wordsGroup.getWidth(),
                (int)wordsGroup.getHeight(),15);
        wordsGroup.addActor(bg);

        RoundedRectangle titleBg = new RoundedRectangle((int)wordsGroup.getWidth(),
                80,15,true);
        titleBg.setColor(Color.valueOf("4257b2"));
        titleBg.setPosition(0,bg.getHeight() - titleBg.getHeight());
        wordsGroup.addActor(titleBg);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = Fonts.bl_bold_45;

        Label title= new Label("Words",labelStyle);
        title.setPosition(wordsGroup.getWidth() / 2 - title.getWidth() / 2,
                bg.getHeight() - titleBg.getHeight() + titleBg.getHeight() / 2 - title.getHeight() / 2);
        wordsGroup.addActor(title);

        SlowScrollPane scrollPane = new SlowScrollPane(null, scrollPaneStyle);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setVariableSizeKnobs(true);
        scrollPane.setOverscroll(false, true);
        scrollPane.setSize(titleBg.getWidth(), wordsGroup.getHeight() - titleBg.getHeight());
        scrollPane.setPosition(0, 0);

        wordsGroup.addActor(scrollPane);

        Table table = new Table();
        table.setSize(scrollPane.getWidth(),scrollPane.getHeight());
        table.left().top();
        table.defaults().pad(AppInfo.PADDING);

        scrollPane.setActor(table);

        Label.LabelStyle wordLabelStyle = new Label.LabelStyle();
        wordLabelStyle.font = Fonts.bl_bold_36;
        wordLabelStyle.fontColor = Color.BLACK;


        wordCardScrollPane.setSize(AppInfo.WIDTH - 2 * wordsGroup.getWidth() - 10 * AppInfo.PADDING,
                wordsGroup.getHeight());
        wordCardScrollPane.setPosition(AppInfo.WIDTH / 2f - wordCardScrollPane.getWidth() / 2,
                wordsGroup.getY());
        for (int i = 0; i < rightAnswer.size; i++) {
            Group group = new Group();
            Image image = new Image(Assets.assets.pix);
            image.setSize(titleBg.getWidth() - 2 * AppInfo.PADDING,60);

            if (answers[i] == -1)
                image.setColor(Color.valueOf("f5f6fa"));
            else if (answers[i] == rightAnswer.get(i))
                image.setColor(0.3f,1f,0.3f,0.7f);
            else
                image.setColor(1f,0.3f,0.3f,0.7f);

            group.setSize(image.getWidth(),image.getHeight());
            group.addActor(image);

            Label label = new Label(words.get(i).meaningDivs.first().title,wordLabelStyle);
            label.setPosition(group.getWidth() / 2 - label.getWidth() / 2 ,
                    group.getHeight() / 2 - label.getHeight() / 2);
            group.addActor(label);
            table.add(group).row();

            final int j = i;
            group.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    if (mainWordCard != null)
                        mainWordCard.remove();

                    mainWordCard = new MainWordCard(words.get(j),true,true,true,
                           AppInfo.WIDTH - 2 * wordsGroup.getWidth() - 10 * AppInfo.PADDING );
                    if (mainWordCard.getHeight() >= wordsGroup.getHeight())
                        wordCardScrollPane.setActor(mainWordCard);
                    else {
                        mainWordCard.setPosition(AppInfo.WIDTH / 2f - mainWordCard.getWidth() / 2,
                                AppInfo.HEIGHT / 2f - mainWordCard.getHeight() / 2);
                        resultWordGroup.addActor(mainWordCard);
                    }
                }
            });

        }

    }

    private void createResultGroup() {
        Group resultGroup = new Group();
        resultGroup.setSize(400,840);
        resultGroup.setPosition(AppInfo.WIDTH - 2 * AppInfo.PADDING - resultGroup.getWidth(),80);
        resultWordGroup.addActor(resultGroup);
        // 1 - 3 - 3 - 1

        RoundedRectangle bg = new RoundedRectangle((int)resultGroup.getWidth(),
                (int)resultGroup.getHeight(),30);
        resultGroup.addActor(bg);

        RoundedRectangle titleBg = new RoundedRectangle((int)resultGroup.getWidth(),
                80,15,true);
        titleBg.setColor(Color.valueOf("4257b2"));
        titleBg.setPosition(0,bg.getHeight() - titleBg.getHeight());
        resultGroup.addActor(titleBg);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = Fonts.bl_bold_45;

        Label title= new Label("Result",labelStyle);
        title.setPosition(resultGroup.getWidth() / 2 - title.getWidth() / 2,
                bg.getHeight() - titleBg.getHeight() + titleBg.getHeight() / 2 - title.getHeight() / 2);
        resultGroup.addActor(title);

        Image rightBg = getLabelBg(titleBg.getWidth() - 4 * AppInfo.PADDING);
        Image wrongBg = getLabelBg(titleBg.getWidth() - 4 * AppInfo.PADDING);
        Image emptyBg = getLabelBg(titleBg.getWidth() - 4 * AppInfo.PADDING);

        rightBg.setPosition(titleBg.getWidth() / 2 - rightBg.getWidth() / 2 ,
                titleBg.getY() - rightBg.getHeight() - 5 * AppInfo.PADDING);
        wrongBg.setPosition(titleBg.getWidth() / 2 - rightBg.getWidth() / 2 ,
                rightBg.getY() - rightBg.getHeight() - 2 * AppInfo.PADDING);
        emptyBg.setPosition(titleBg.getWidth() / 2 - rightBg.getWidth() / 2 ,
                wrongBg.getY() - rightBg.getHeight() - 2 * AppInfo.PADDING);

        resultGroup.addActor(rightBg);
        resultGroup.addActor(wrongBg);
//        resultGroup.addActor(emptyBg);

        Label.LabelStyle resultStyle = new Label.LabelStyle();
        resultStyle.font = Fonts.bl_bold_36;

        Label titRight = new Label("[GREEN]Right[]",resultStyle);
        Label titWrong = new Label("[RED]WRONG[]",resultStyle);
        Label titEmpty = new Label("EMPTY",resultStyle);

        Label numRight = new Label("[GREEN]" + right +"[]",resultStyle);
        Label numWrong = new Label("[RED]" + wrong + "[]",resultStyle);
        Label numEmpty = new Label("ZZ",resultStyle);

        titEmpty.setColor(Color.NAVY);
        numEmpty.setColor(Color.NAVY);

        titRight.setPosition(rightBg.getX() + rightBg.getWidth() - titRight.getWidth() - AppInfo.PADDING,rightBg.getY() + rightBg.getHeight() / 2 - titRight.getHeight() / 2);
        titWrong.setPosition(wrongBg.getX() + rightBg.getWidth() - titWrong.getWidth() - AppInfo.PADDING,wrongBg.getY() + wrongBg.getHeight() / 2 - titRight.getHeight() / 2);
        titEmpty.setPosition(emptyBg.getX() + rightBg.getWidth() - titEmpty.getWidth() - AppInfo.PADDING,emptyBg.getY() + emptyBg.getHeight() / 2 - titRight.getHeight() / 2);

        numRight.setPosition(rightBg.getX() + AppInfo.PADDING,rightBg.getY() + rightBg.getHeight() / 2 - titRight.getHeight() / 2);
        numWrong.setPosition(wrongBg.getX() + AppInfo.PADDING,wrongBg.getY() + wrongBg.getHeight() / 2 - titRight.getHeight() / 2);
        numEmpty.setPosition(emptyBg.getX() + AppInfo.PADDING,emptyBg.getY() + emptyBg.getHeight() / 2 - titRight.getHeight() / 2);

        resultGroup.addActor(titRight);
        resultGroup.addActor(titWrong);
//        resultGroup.addActor(titEmpty);
        resultGroup.addActor(numRight);
        resultGroup.addActor(numWrong);
//        resultGroup.addActor(numEmpty);

        Label.LabelStyle scoreStyle = new Label.LabelStyle();
        scoreStyle.font = Fonts.bl_bold_72;
        scoreStyle.fontColor = Color.BLACK;

        Group scoreGroup = new Group();
        Label score = new Label(right + "/" + rightAnswer.size,scoreStyle);
        scoreGroup.addActor(score);
        scoreGroup.setSize(score.getWidth(),score.getHeight());
        scoreGroup.setOrigin(scoreGroup.getWidth() / 2,scoreGroup.getHeight() / 2);
        scoreGroup.setScale(1.4f);

        scoreGroup.setPosition(titleBg.getWidth() / 2 - score.getWidth() / 2,
                emptyBg.getY() - 2 * AppInfo.PADDING - score.getHeight());
        resultGroup.addActor(scoreGroup);



        RoundedRectangle roundedRectangle = new RoundedRectangle(
                (int)titleBg.getWidth() - 2 * AppInfo.PADDING,120,15);
        Sprite hoverSprite = new Sprite(roundedRectangle.lastTexture);
        hoverSprite.setColor(Color.valueOf("3ccfcf"));
        Sprite normSprite = new Sprite(roundedRectangle.lastTexture);
        normSprite.setColor(Color.valueOf("77dddd"));
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = Fonts.bl_bold_36;
        textButtonStyle.up = new SpriteDrawable(normSprite);
        textButtonStyle.over = new SpriteDrawable(hoverSprite);
        textButtonStyle.fontColor = Color.valueOf("4257b2");

        TextButton btnNewQuiz = new TextButton("New Quiz",textButtonStyle);
        TextButton btnQuit = new TextButton("Quit",textButtonStyle);

        btnQuit.setPosition(AppInfo.PADDING,2 * AppInfo.PADDING);
        btnNewQuiz.setPosition(AppInfo.PADDING,btnQuit.getY() + btnQuit.getHeight() + AppInfo.PADDING);

        resultGroup.addActor(btnNewQuiz);
        resultGroup.addActor(btnQuit);

        btnNewQuiz.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                wordTestScreen.newQuizClicked();
            }
        });

        btnQuit.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                wordTestScreen.quitClicked();
            }
        });


    }

    private Image getLabelBg(float with){
        Image rightBg = new Image(Assets.assets.pix);
        rightBg.setSize(with,50);
        rightBg.setColor(Color.valueOf("f5f6fa"));
        return rightBg;
    }
}
