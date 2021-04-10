package com.serhatmerak.wordlist.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.serhatmerak.wordlist.AppMain;
import com.serhatmerak.wordlist.helpers.AppInfo;
import com.serhatmerak.wordlist.helpers.Assets;
import com.serhatmerak.wordlist.helpers.ButtonHoverAnimation;
import com.serhatmerak.wordlist.helpers.ColorPalette;
import com.serhatmerak.wordlist.helpers.CustomScreen;
import com.serhatmerak.wordlist.helpers.DataHolder;
import com.serhatmerak.wordlist.helpers.Fonts;
import com.serhatmerak.wordlist.huds.Alert;
import com.serhatmerak.wordlist.huds.BorderImage;
import com.serhatmerak.wordlist.huds.RoundedRectangle;
import com.serhatmerak.wordlist.huds.wordcardhuds.SettingsWordCards;
import com.serhatmerak.wordlist.huds.wordtesthuds.ResultWordTest;
import com.serhatmerak.wordlist.huds.wordtesthuds.SettingsWordTest;
import com.serhatmerak.wordlist.huds.wordtesthuds.TestWordTest;

import java.util.Arrays;


public class WordTestScreen extends CustomScreen {

    private OrthographicCamera camera;
    private Stage stage;
    private AppMain appMain;
    private SpriteBatch batch;

    private SettingsWordTest settingsWordTest;
    private ResultWordTest resultWordTest;

    Color bg = ColorPalette.LightTopBlue;

    private boolean EVERY_WORD = true;
    private boolean DEF_WORD = true;
    private int NUMBER_OF_QUESTION = 20;
    private boolean USE_TURKISH = false;
    private boolean SHOW_ANSWERS = false;

    private enum ScreenType  {
            SETTINGS,EXAM,RESULT
    }

    private ScreenType screenType = ScreenType.SETTINGS;
    private TestWordTest testWordTest;

    private TextButton btnFinishQuiz;


    public WordTestScreen(AppMain appMain) {
        this.appMain = appMain;
        batch = appMain.batch;

        camera = new OrthographicCamera(AppInfo.WIDTH, AppInfo.HEIGHT);
        camera.setToOrtho(false, AppInfo.WIDTH, AppInfo.HEIGHT);
        Viewport viewport = new StretchViewport(AppInfo.WIDTH, AppInfo.HEIGHT, camera);
        stage = new Stage(viewport, batch);

        Gdx.input.setInputProcessor(stage);

        createActors();

        shortcuts();

    }

    private void shortcuts() {
            stage.addListener(new ClickListener(){
                @Override
                public boolean keyDown(InputEvent event, int keycode) {
                    if (settingsWordTest.settingsGroup.getStage() != null) {
                        if (keycode == Input.Keys.ENTER) {
                            settingsWordTest.createClicked();
                        }
                    }

                    else if (testWordTest.testPageGroup.getStage() != null) {
                        if (keycode == Input.Keys.RIGHT) {
                            testWordTest.nextQuestionClicked(true);
                        }else if (keycode == Input.Keys.LEFT){
                            testWordTest.nextQuestionClicked(false);
                        }
                    }
                    return super.keyDown(event, keycode);

                }
            });

    }

    private void createActors() {
        createTopList();
        settingsWordTest = new SettingsWordTest(stage,this);
        stage.addActor(settingsWordTest.settingsGroup);
//        SettingsWordCards settingsWordCards = new SettingsWordCards(stage,null);
//        stage.addActor(settingsWordCards.settingsGroup);
    }


    private void createTopList() {
        BorderImage topListBg = new BorderImage(new Vector3(
                AppInfo.WIDTH,80,0
        ), ColorPalette.TopBlue,Color.WHITE);


        Table topListTable = new Table();
        topListTable.setSize(topListBg.getWidth(),topListBg.getHeight());
        topListTable.left();


        Group topListGroup = new Group();
        topListGroup.setSize(topListBg.getWidth(),topListBg.getHeight());
        topListGroup.setPosition(0,AppInfo.HEIGHT - topListBg.getHeight());
        topListGroup.addActor(topListBg);
        topListGroup.addActor(topListTable);

        /*
         *Actors
         */

        Label.LabelStyle titleStyle = new Label.LabelStyle();
        titleStyle.font = Fonts.bl_bold_45;

        Label title = new Label("Word App",titleStyle);

        title.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                appMain.setScreen(new WordListScreen(appMain));
            }
        });

        stage.addActor(topListGroup);
        /*
         *Filling table
         */



        Table topChangingTable = new Table();
        topChangingTable.setSize(1500, topListTable.getHeight());
        topChangingTable.center();
        Image back = new Image(Assets.assets.back);
        back.setSize(40,40);
        back.addListener(new ButtonHoverAnimation(back,true){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                appMain.setScreen(new WordListScreen(appMain));
            }
        });
        back.setOrigin(Align.center);

        topListTable.add(back).width(back.getWidth()).height(back.getHeight()).pad(AppInfo.PADDING);


//        topListTable.add(title).width(300 - AppInfo.PADDING).padLeft(AppInfo.PADDING);
        topListTable.add(topChangingTable).padLeft(100);





        Label.LabelStyle listNameStyle = new Label.LabelStyle();
        listNameStyle.font = Fonts.bl_bold_32;



        Label listName = new Label(DataHolder.dataholder.getSelectedList().listName,listNameStyle);
        topChangingTable.add(listName);

        RoundedRectangle roundedRectangle = new RoundedRectangle(
                250,40,15);

        Sprite hoverSprite = new Sprite(roundedRectangle.lastTexture);
        hoverSprite.setColor(ColorPalette.ButtonOverBlue);

        Sprite normSprite = new Sprite(roundedRectangle.lastTexture);
        normSprite.setColor(ColorPalette.ButtonBlue);


        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = Fonts.bl_bold_36;
        textButtonStyle.up = new SpriteDrawable(normSprite);
        textButtonStyle.over = new SpriteDrawable(hoverSprite);
        textButtonStyle.fontColor = ColorPalette.TopBlue;

        btnFinishQuiz = new TextButton("Finish Quiz",textButtonStyle);
        btnFinishQuiz.setSize(roundedRectangle.getWidth(),roundedRectangle.getHeight());
        btnFinishQuiz.setVisible(false);

        topChangingTable.add(btnFinishQuiz).padLeft(1000 - listName.getWidth());
        btnFinishQuiz.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                testWordTest.testPageGroup.remove();
                screenType =  ScreenType.RESULT;
                resultWordTest = new ResultWordTest(WordTestScreen.this,
                        testWordTest.answerWords,testWordTest.rightAnswers,testWordTest.answers);
                stage.addActor(resultWordTest.resultWordGroup);
                btnFinishQuiz.setVisible(false);
            }
        });
    }


    public void render(float delta) {
        Gdx.gl.glClearColor(bg.r,bg.g,bg.b,bg.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.end();



        stage.act();
        stage.draw();

    }


    @Override
    public void resize(int width, int height) {

        if(height > width) {
            if (stage.getViewport() instanceof StretchViewport){
                Viewport fit = new FitViewport(AppInfo.WIDTH,AppInfo.HEIGHT,camera);
                stage.setViewport(fit);
            }
        }else {
            if (stage.getViewport() instanceof FitViewport){
                Viewport fit = new StretchViewport(AppInfo.WIDTH,AppInfo.HEIGHT,camera);
                stage.setViewport(fit);
            }
        }

        stage.getViewport().update(width, height, true);

    }

    public void quitClicked() {
        appMain.setScreen(new WordListScreen(appMain));
    }

    public void newQuizClicked() {
        resultWordTest.resultWordGroup.remove();
        settingsWordTest = new SettingsWordTest(stage,this);
        stage.addActor(settingsWordTest.settingsGroup);
    }

    public void createTestClicked(){
        EVERY_WORD = settingsWordTest.EVERY_WORD;
        DEF_WORD = settingsWordTest.DEF_WORD;
        NUMBER_OF_QUESTION = settingsWordTest.NUMBER_OF_QUESTION;
        USE_TURKISH = settingsWordTest.USE_TURKISH;
        SHOW_ANSWERS = settingsWordTest.SHOW_ANSWERS;

        testWordTest = new TestWordTest();
        testWordTest.setTestType(EVERY_WORD,DEF_WORD,NUMBER_OF_QUESTION,USE_TURKISH,SHOW_ANSWERS);

        System.out.println("EVERY Word : " + EVERY_WORD);
        System.out.println("DEF_WORD : " + DEF_WORD);
        System.out.println("USE_TURKISH : " + USE_TURKISH);
        System.out.println("SHOW_ANSWERS : " + SHOW_ANSWERS);
        System.out.println("NUMBER_OF_QUESTION : " + NUMBER_OF_QUESTION);
        System.out.println("\n\n\n");

        testWordTest.createTest();
        settingsWordTest.settingsGroup.remove();
        stage.addActor(testWordTest.testPageGroup);

        screenType = ScreenType.EXAM;
        btnFinishQuiz.setVisible(true);

    }
}

