package com.serhatmerak.wordlist.huds.addwordhuds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.serhatmerak.wordlist.helpers.AppInfo;
import com.serhatmerak.wordlist.helpers.Assets;
import com.serhatmerak.wordlist.helpers.ColorPalette;
import com.serhatmerak.wordlist.helpers.DataHolder;
import com.serhatmerak.wordlist.helpers.Fonts;
import com.serhatmerak.wordlist.huds.Alert;
import com.serhatmerak.wordlist.huds.BorderImage;
import com.serhatmerak.wordlist.huds.RoundedRectangle;
import com.serhatmerak.wordlist.huds.addwordhuds.AddWordWindow;
import com.serhatmerak.wordlist.kumar.SlowScrollPane;
import com.serhatmerak.wordlist.word.GeneratorThread;
import com.serhatmerak.wordlist.word.MeaningDiv;
import com.serhatmerak.wordlist.word.Word;
import com.serhatmerak.wordlist.word.WordList;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class AddWordsFromFileHud extends Group {

    private WordList wordList;
    private AddWordWindow addWordWindow;

    private TextButton selectFileButton;
    private Label showFileNameLabel;
    private String SelectedFilePath = "";

    private TextButton btnLoadFile;
    private Label labelLoading;

    private GeneratorThread generatorThread;


    public ObjectMap<Label,Word> wordMap;
    private boolean showAlert;

    private String fileText;
    private Label fileTextLabel;

    public AddWordsFromFileHud(Vector2 size, WordList wordList, AddWordWindow addWordWindow){

        this.wordList = wordList;
        this.addWordWindow = addWordWindow;

        setSize(size.x,size.y);
        createActors();
        createWordGenerators();
        setListeners();


        addActor(selectFileButton);
    }

    private void createWordGenerators() {
        generatorThread = new GeneratorThread();
        generatorThread.initalize(this);
        wordMap = new ObjectMap<>();
    }

    private void setListeners() {
        selectFileButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        JFileChooser chooser = new JFileChooser();
                        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                        chooser.setDragEnabled(true);
                        JFrame f = new JFrame();
                        f.setVisible(true);
                        f.toFront();
                        f.setVisible(false);
                        int res = chooser.showSaveDialog(f);
                        f.dispose();
                        if (res == JFileChooser.APPROVE_OPTION) {
                                SelectedFilePath = chooser.getSelectedFile().getAbsolutePath();
                                showFileNameLabel.setText(chooser.getSelectedFile().getName());
                                showFileNameLabel.setColor(Color.BLACK);

                            if (!SelectedFilePath.equals("")) {
                                FileHandle handle = Gdx.files.absolute(SelectedFilePath);
                                fileText = handle.readString().replaceAll("[,.?]"," ");
                                fileTextLabel.setText(fileText);
                            }

                            if(!chooser.getSelectedFile().getName().endsWith(".txt") &&
                                    !chooser.getSelectedFile().getName().endsWith(".srt") ){
                                showAlert = true;
                            }
                        }
                    }
                }).start();
            }
        });

        btnLoadFile.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                addActor(labelLoading);
                selectFileButton.setDisabled(true);
                readFile();
            }
        });

    }

    private void readFile() {
        if (!SelectedFilePath.equals("")) {
            clear();
            DividedTextGroup dividedTextGroup = new DividedTextGroup(fileText,new Vector2(getWidth(),getHeight()),addWordWindow);
            addActor(dividedTextGroup);

        }
    }



    private void createActors() {
        RoundedRectangle buttonTexture = new RoundedRectangle(300,40,30);

        Sprite oilOver = new Sprite(buttonTexture.lastTexture);
        oilOver.setColor(ColorPalette.OilBlueHover);

        Sprite oilUp = new Sprite(buttonTexture.lastTexture);
        oilUp.setColor(ColorPalette.OilBlue);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = Fonts.bl_bold_26;
        textButtonStyle.up = new SpriteDrawable(oilUp);
        textButtonStyle.over = new SpriteDrawable(oilOver);

        selectFileButton = new TextButton("Select File",textButtonStyle);
        selectFileButton.setPosition(AppInfo.PADDING,getHeight() - AppInfo.PADDING - selectFileButton.getPrefHeight());

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = Fonts.bl_bold_32;
        createFileNameLabel();

        Sprite btnBgNorm = new Sprite(Assets.assets.pix);
        btnBgNorm.setSize(300,40);
        btnBgNorm.setColor(ColorPalette.ButtonOverBlue);

        Sprite btnBgHover = new Sprite(Assets.assets.pix);
        btnBgHover.setSize(300,40);
        btnBgHover.setColor(ColorPalette.ButtonBlue);

        TextButton.TextButtonStyle bluStyle = new TextButton.TextButtonStyle();
        bluStyle.font = Fonts.bl_bold_26;
        bluStyle.up = new SpriteDrawable(btnBgHover);
        bluStyle.over = new SpriteDrawable(btnBgNorm);
        bluStyle.fontColor = ColorPalette.TopBlue;

        btnLoadFile = new TextButton("Load File",bluStyle);
        btnLoadFile.setPosition(getWidth() - AppInfo.PADDING - btnLoadFile.getWidth(),
                AppInfo.PADDING);
        addActor(btnLoadFile);

        btnLoadFile.setTouchable(Touchable.disabled);
        btnLoadFile.setColor(1,1,1,0.7f);

        RoundedRectangle roundedRectangle = new RoundedRectangle(getWidth() - 2 * AppInfo.PADDING,
                getHeight() - 4 * AppInfo.PADDING - 2 * btnLoadFile.getHeight(),20);
        roundedRectangle.setPosition(AppInfo.PADDING,
                btnLoadFile.getY() + btnLoadFile.getHeight() + AppInfo.PADDING);
        roundedRectangle.setColor(Color.LIGHT_GRAY);

        Sprite scroll = new Sprite(Assets.assets.pix);
        Sprite knob = new Sprite(Assets.assets.pix);

        scroll.setSize(20, 100);
        scroll.setColor(Color.LIGHT_GRAY);
        knob.setSize(20, 20);
        knob.setColor(Color.DARK_GRAY);
        ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        scrollPaneStyle.vScroll = new SpriteDrawable(scroll);
        scrollPaneStyle.vScrollKnob = new SpriteDrawable(knob);

        SlowScrollPane slowScrollPane = new SlowScrollPane(null, scrollPaneStyle);
        slowScrollPane.setScrollingDisabled(true, false);
        slowScrollPane.setVariableSizeKnobs(true);
        slowScrollPane.setOverscroll(false, true);
        slowScrollPane.setSize(roundedRectangle.getWidth() - 2 * AppInfo.PADDING,
                roundedRectangle.getHeight() - 2 * AppInfo.PADDING);
        slowScrollPane.setPosition(roundedRectangle.getX() + AppInfo.PADDING,
                roundedRectangle.getY() + AppInfo.PADDING);

        ///////

        Label.LabelStyle textStyle = new Label.LabelStyle();
        textStyle.font = Fonts.bl_bold_26;


        fileTextLabel = new Label("",textStyle);
        fileTextLabel.setColor(Color.BLACK);
        fileTextLabel.setWidth(slowScrollPane.getWidth());
        fileTextLabel.setWrap(true);
        slowScrollPane.setActor(fileTextLabel);

        addActor(roundedRectangle);
        addActor(slowScrollPane);

        labelLoading = new Label("Loading ...",labelStyle);
        labelLoading.setAlignment(Align.center);

    }

    private void createFileNameLabel() {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = Fonts.bl_bold_32;

        showFileNameLabel = new Label("< select a txt file",labelStyle);
        showFileNameLabel.setWidth(getWidth() - 3 * AppInfo.PADDING - selectFileButton.getPrefWidth());
        showFileNameLabel.setAlignment(Align.center);
        showFileNameLabel.setPosition(selectFileButton.getX() + selectFileButton.getPrefWidth() + AppInfo.PADDING,
                selectFileButton.getY());

        Image bg = new Image(Assets.assets.pix);
        bg.setSize(getWidth() - 5 * AppInfo.PADDING - selectFileButton.getWidth(),
                showFileNameLabel.getPrefHeight() + 10);
        bg.setPosition(getWidth() - 2 * AppInfo.PADDING - bg.getWidth(),
                selectFileButton.getY() + selectFileButton.getHeight() / 2 - bg.getHeight() / 2);

        addActor(bg);

        Sprite scroll = new Sprite(Assets.assets.pix);
        Sprite knob = new Sprite(Assets.assets.pix);

        scroll.setSize(20, 100);
        scroll.setColor(Color.LIGHT_GRAY);
        knob.setSize(20, 20);
        knob.setColor(Color.DARK_GRAY);
        ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        scrollPaneStyle.vScroll = new SpriteDrawable(scroll);
        scrollPaneStyle.vScrollKnob = new SpriteDrawable(knob);

        SlowScrollPane slowScrollPane = new SlowScrollPane(null, scrollPaneStyle);
        slowScrollPane.setScrollingDisabled(false, true);
        slowScrollPane.setVariableSizeKnobs(true);
        slowScrollPane.setOverscroll(true, false);
        slowScrollPane.setSize(bg.getWidth(),bg.getHeight());
        slowScrollPane.setPosition(bg.getX(),bg.getY());
        addActor(slowScrollPane);
        slowScrollPane.setActor(showFileNameLabel);


    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (!SelectedFilePath.equals("") && !btnLoadFile.isTouchable()){
            btnLoadFile.setTouchable(Touchable.enabled);
            btnLoadFile.setColor(1,1,1,1);
        }

        if (showAlert){
            Alert.show(getStage(),
                    "Some files are protected to read!\n(.doc , .pdf , etc.)\nYou can copy all the text in these files and than add words with 'Text' menu. ",8);

            showAlert = false;
        }
    }
}
