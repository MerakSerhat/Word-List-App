package com.serhatmerak.wordlist.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.serhatmerak.wordlist.AppMain;
import com.serhatmerak.wordlist.MainScreen;
import com.serhatmerak.wordlist.helpers.AppInfo;
import com.serhatmerak.wordlist.helpers.Assets;
import com.serhatmerak.wordlist.helpers.ButtonHoverAnimation;
import com.serhatmerak.wordlist.helpers.ColorPalette;
import com.serhatmerak.wordlist.helpers.CustomScreen;
import com.serhatmerak.wordlist.helpers.DataHolder;
import com.serhatmerak.wordlist.helpers.Fonts;
import com.serhatmerak.wordlist.huds.BorderImage;

public class KnownWordsScreen extends CustomScreen {
    private Viewport viewport;
    private Stage stage;
    private OrthographicCamera camera;
    private AppMain appMain;
    private SpriteBatch batch;

    private Image btnBack;
    private Group btnYourWords,btnWords1,btnWords2,btnWords3;
    private Group selectedGroup;
    private Image stick;
    private Table wordsTable;
    private int tableState = -1;



    public KnownWordsScreen(AppMain appMain){
        this.appMain = appMain;
        batch = appMain.batch;

        camera = new OrthographicCamera(AppInfo.WIDTH,AppInfo.HEIGHT);
        camera.setToOrtho(false, AppInfo.WIDTH,AppInfo.HEIGHT);
        viewport = new FitViewport(AppInfo.WIDTH,AppInfo.HEIGHT,camera);
        stage = new Stage(viewport, batch);

        Image bg = new Image(new Texture("pix.png"));
        bg.setSize(AppInfo.WIDTH,AppInfo.HEIGHT);
        bg.setColor(ColorPalette.bg);
        stage.addActor(bg);

        createActors();
        createListButtons();

        Gdx.input.setInputProcessor(stage);
    }

    private void createListButtons() {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = Fonts.bl_bold_45;

        btnYourWords = new Group();
        btnWords1 = new Group();
        btnWords2 = new Group();
        btnWords3 = new Group();

        btnYourWords.addActor(new Label("Your Words",labelStyle));
        btnWords1.addActor(new Label("1. Words",labelStyle));
        btnWords2.addActor(new Label("2. Words",labelStyle));
        btnWords3.addActor(new Label("3. Words",labelStyle));

        btnWords1.setHeight(((Label)btnWords1.getChild(0)).getPrefHeight());
        btnWords2.setHeight(((Label)btnWords1.getChild(0)).getPrefHeight());
        btnWords3.setHeight(((Label)btnWords1.getChild(0)).getPrefHeight());
        btnYourWords.setHeight(((Label)btnWords1.getChild(0)).getPrefHeight());




        float w =  (AppInfo.WIDTH - btnBack.getWidth() - btnBack.getX() - 4 * AppInfo.PADDING) / 4;

        btnWords1.setWidth(w);
        btnWords2.setWidth(w);
        btnWords3.setWidth(w);
        btnYourWords.setWidth(w);

        (btnWords1.getChild(0)).setWidth(w);
        (btnWords2.getChild(0)).setWidth(w);
        (btnWords3.getChild(0)).setWidth(w);
        (btnYourWords.getChild(0)).setWidth(w);

        ((Label)btnWords1.getChild(0)).setAlignment(Align.center);
        ((Label)btnWords2.getChild(0)).setAlignment(Align.center);
        ((Label)btnWords3.getChild(0)).setAlignment(Align.center);
        ((Label)btnYourWords.getChild(0)).setAlignment(Align.center);

        btnYourWords.setPosition(btnBack.getX() + btnBack.getWidth() +  AppInfo.PADDING * 2,btnBack.getY());
        btnWords1.setPosition(btnYourWords.getX() + w,btnYourWords.getY() );
        btnWords2.setPosition(btnWords1.getX() + w,btnYourWords.getY() );
        btnWords3.setPosition(btnWords2.getX() + w,btnYourWords.getY() );

        btnYourWords.setColor(ColorPalette.text1);

        btnWords1.setOrigin(Align.center);
        btnWords2.setOrigin(Align.center);
        btnWords3.setOrigin(Align.center);
        btnYourWords.setOrigin(Align.center);

        selectedGroup = btnYourWords;
        btnYourWords.getChild(0).setColor(ColorPalette.text1);

        stick = new Image(Assets.assets.pix);
        stick.setSize(w - 4,2);
        stick.setColor(ColorPalette.box);


        btnYourWords.addListener(new ButtonHoverAnimation(btnYourWords,true){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                selectedGroup.getChild(0).setColor(Color.WHITE);
                selectedGroup = btnYourWords;
                selectedGroup.getChild(0).setColor(ColorPalette.text1);
                stick.setPosition(btnYourWords.getX() + 2,stick.getY());
                fillTable();
            }
        });
        btnWords1.addListener(new ButtonHoverAnimation(btnWords1,true){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                selectedGroup.getChild(0).setColor(Color.WHITE);
                selectedGroup = btnWords1;
                selectedGroup.getChild(0).setColor(ColorPalette.text1);
                stick.setPosition(btnWords1.getX() + 2,stick.getY());
                fillTable();
            }
        });
        btnWords2.addListener(new ButtonHoverAnimation(btnWords2,true){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                selectedGroup.getChild(0).setColor(Color.WHITE);
                selectedGroup = btnWords2;
                selectedGroup.getChild(0).setColor(ColorPalette.text1);
                stick.setPosition(btnWords2.getX() + 2,stick.getY());
                fillTable();
            }
        });
        btnWords3.addListener(new ButtonHoverAnimation(btnWords3,true){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                selectedGroup.getChild(0).setColor(Color.WHITE);
                selectedGroup = btnWords3;
                selectedGroup.getChild(0).setColor(ColorPalette.text1);
                stick.setPosition(btnWords3.getX() + 2,stick.getY());
                fillTable();

            }
        });

        BorderImage tableBg = new BorderImage(new Vector3(w * 4,AppInfo.HEIGHT - 5 * AppInfo.PADDING - selectedGroup.getHeight(),2));
        tableBg.setPosition(btnYourWords.getX(),AppInfo.PADDING * 2);
        stage.addActor(tableBg);

        for (int i = 0; i < 4; i++) {
            BorderImage buttonBg = new BorderImage(new Vector3(w,btnYourWords.getHeight() + AppInfo.PADDING,2));
            buttonBg.setPosition(btnYourWords.getX() + i* w,tableBg.getY() + tableBg.getHeight() - 2);
            stage.addActor(buttonBg);
        }
        stick.setPosition(btnYourWords.getX() + 2,tableBg.getY() + tableBg.getHeight() - 2);
        stage.addActor(stick);

        stage.addActor(btnYourWords);
        stage.addActor(btnWords1);
        stage.addActor(btnWords2);
        stage.addActor(btnWords3);

        Sprite scroll = new Sprite(Assets.assets.pix);
        Sprite knob = new Sprite(Assets.assets.pix);

        ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        scrollPaneStyle.vScroll = new SpriteDrawable(scroll);
        scrollPaneStyle.vScrollKnob = new SpriteDrawable(knob);
        ScrollPane topLabelListScrollPane = new ScrollPane(null, scrollPaneStyle);
        topLabelListScrollPane.setVariableSizeKnobs(true);
        topLabelListScrollPane.setOverscroll(false, true);
        topLabelListScrollPane.setSize(tableBg.getWidth(),tableBg.getHeight());

        wordsTable = new Table();
        wordsTable.setWidth(tableBg.getWidth());
//        wordsTable.defaults().pad(AppInfo.PADDING);

        topLabelListScrollPane.setPosition(tableBg.getX(),tableBg.getY());
        topLabelListScrollPane.setActor(wordsTable);

        fillTable();
        stage.addActor(topLabelListScrollPane);
    }

    private void fillTable(){
        if (selectedGroup == btnYourWords){
            if (tableState!= 0){
                wordsTable.clear();

                Label.LabelStyle labelStyle = new Label.LabelStyle();
                labelStyle.font = Fonts.bl_bold_32;
                int i = 0;

                for (final String word:DataHolder.dataholder.yourKnownWords) {
                    final Label label  = new Label(word,labelStyle);
                    label.setAlignment(Align.center);

                    label.addListener(new ClickListener(){
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            super.clicked(event, x, y);
                            if (DataHolder.dataholder.knownWords.contains(word,false)){
                                label.setColor(Color.RED);
                                DataHolder.dataholder.knownWords.removeValue(word,false);
                                DataHolder.dataholder.yourKnownWords.removeValue(word,false);
                            }else {
                                label.setColor(ColorPalette.text1);
                                DataHolder.dataholder.knownWords.add(word);
                                DataHolder.dataholder.yourKnownWords.add(word);
                            }

                            //TODO: save
                            DataHolder.dataholder.saveYourKnownWords();
                        }
                    });

                    wordsTable.add(label).width(wordsTable.getWidth() / 5f);
                    i++;
                    if (i%5 == 0) wordsTable.row();
                }

                tableState =0;
            }
        }else if (selectedGroup == btnWords1){
            if (tableState!= 1){
                wordsTable.clear();


                Label.LabelStyle labelStyle = new Label.LabelStyle();
                labelStyle.font = Fonts.bl_bold_32;
                int i = 0;

                for (final String word:DataHolder.dataholder.firstKnownWords) {
                    final Label label  = new Label(word,labelStyle);
                    label.setAlignment(Align.center);

                    if (DataHolder.dataholder.knownWords.contains(word,false))
                        label.setColor(Color.WHITE);
                    else
                        label.setColor(Color.RED);

                    label.addListener(new ClickListener(){
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            super.clicked(event, x, y);
                            if (DataHolder.dataholder.knownWords.contains(word,false)){
                                label.setColor(Color.RED);
                                DataHolder.dataholder.knownWords.removeValue(word,false);
                            }else {
                                label.setColor(Color.WHITE);
                                DataHolder.dataholder.knownWords.add(word);
                            }

                            //TODO: save
                            DataHolder.dataholder.saveKnownCustomWords();
                        }
                    });

                    wordsTable.add(label).width(wordsTable.getWidth() / 6f);
                    i++;
                    if (i%6 == 0) wordsTable.row();
                }

                tableState =1;
            }
        }else if (selectedGroup == btnWords2){
            if (tableState!= 2){
                wordsTable.clear();

                Label.LabelStyle labelStyle = new Label.LabelStyle();
                labelStyle.font = Fonts.bl_bold_32;
                int i = 0;

                for (final String word:DataHolder.dataholder.secondKnownWords) {
                    final Label label  = new Label(word,labelStyle);
                    label.setAlignment(Align.center);

                    if (DataHolder.dataholder.knownWords.contains(word,false))
                        label.setColor(Color.WHITE);
                    else
                        label.setColor(Color.RED);


                    label.addListener(new ClickListener(){
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            super.clicked(event, x, y);
                            if (DataHolder.dataholder.knownWords.contains(word,false)){
                                label.setColor(Color.RED);
                                DataHolder.dataholder.knownWords.removeValue(word,false);
                            }else {
                                label.setColor(Color.WHITE);
                                DataHolder.dataholder.knownWords.add(word);
                            }

                            //TODO: save
                            DataHolder.dataholder.saveKnownCustomWords();
                        }
                    });

                    wordsTable.add(label).width(wordsTable.getWidth() / 6f);
                    i++;
                    if (i%6 == 0) wordsTable.row();
                }

                tableState =2;
            }
        }else if (selectedGroup == btnWords3){
            if (tableState!= 3){
                wordsTable.clear();

                Label.LabelStyle labelStyle = new Label.LabelStyle();
                labelStyle.font = Fonts.bl_bold_32;
                int i = 0;
                for (final String word:DataHolder.dataholder.thirdKnownWords) {
                    final Label label  = new Label(word,labelStyle);
                    label.setAlignment(Align.center);

                    if (DataHolder.dataholder.knownWords.contains(word,false))
                        label.setColor(Color.WHITE);
                    else
                        label.setColor(Color.RED);

                    label.addListener(new ClickListener(){
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            super.clicked(event, x, y);
                            if (DataHolder.dataholder.knownWords.contains(word,false)){
                                label.setColor(Color.RED);
                                DataHolder.dataholder.knownWords.removeValue(word,false);
                            }else {
                                label.setColor(Color.WHITE);
                                DataHolder.dataholder.knownWords.add(word);
                            }

                            //TODO: save
                            DataHolder.dataholder.saveKnownCustomWords();
                        }
                    });

                    wordsTable.add(label).width(wordsTable.getWidth() / 6f);
                    i++;
                    if (i%6 == 0) wordsTable.row();
                }

                tableState =3;
            }
        }
    }

    private void createActors() {
        BorderImage bg = new BorderImage(new Vector3(AppInfo.WIDTH - 2 * AppInfo.PADDING,
                AppInfo.HEIGHT - 2 * AppInfo.PADDING,4));
        bg.setPosition(AppInfo.PADDING,AppInfo.PADDING);

        btnBack = new Image(Assets.assets.back);
        btnBack.setOrigin(Align.center);
        btnBack.setSize(48,48);
        btnBack.setPosition(AppInfo.PADDING * 2 + 12,AppInfo.HEIGHT - 2 * AppInfo.PADDING - btnBack.getHeight() - 16);
        btnBack.addListener(new ButtonHoverAnimation(btnBack){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                appMain.setScreen(new MainScreen(appMain));
            }
        });

        stage.addActor(bg);
        stage.addActor(btnBack);

    }

    public void render(float delta) {
        Gdx.gl.glClearColor(ColorPalette.bg.r,ColorPalette.bg.g,ColorPalette.bg.b,ColorPalette.bg.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.end();

        stage.act();
        stage.draw();
    }


    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);

    }
}
