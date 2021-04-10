package com.serhatmerak.wordlist.kumar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
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
import com.serhatmerak.wordlist.huds.BorderImage;
import com.serhatmerak.wordlist.huds.MainWordCard;
import com.serhatmerak.wordlist.huds.ShowWindow;
import com.serhatmerak.wordlist.screens.FirstScreen;
import com.serhatmerak.wordlist.word.Word;
import com.serhatmerak.wordlist.word.WordGenerator;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.FlavorEvent;
import java.awt.datatransfer.FlavorListener;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import javax.swing.SwingUtilities;

public class ReaderScreen extends CustomScreen {


    private final Viewport viewport;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private AppMain appMain;
    private Stage stage;

    private float WIDTH = 500;


    //TODO: remove
    Color bg = Color.valueOf("f5f6fa");
    MainWordCard mainWordCard;
    private Table topListTable;
    private SlowScrollPane wordCardsScrollPane;
    private Word word;
    private boolean isCopiedFromApplication;
    private boolean changed;

    private boolean listSelected = false;
    private Group btnAddToList;


    public ReaderScreen(AppMain appMain){
        this.appMain = appMain;
        batch = appMain.batch;

        camera = new OrthographicCamera(AppInfo.WIDTH ,AppInfo.HEIGHT);
        camera.setToOrtho(false, AppInfo.WIDTH,AppInfo.HEIGHT);
        viewport = new StretchViewport(AppInfo.WIDTH, AppInfo.HEIGHT, camera);
        stage = new Stage(viewport, batch);

        createTopList();
        createActors();

        Gdx.input.setInputProcessor(stage);

        checkWhenClipboardChanged();
    }

    private void checkWhenClipboardChanged() {

        final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

        Toolkit.getDefaultToolkit().getSystemClipboard().addFlavorListener(new FlavorListener() {
            @Override
            public void flavorsChanged(final FlavorEvent e) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (isCopiedFromApplication){
                            isCopiedFromApplication = false;
                        }else {
                            final Transferable contents = clipboard.getContents(null);
                            boolean hasTransferableText = (contents != null) && contents.isDataFlavorSupported(DataFlavor.stringFlavor);
                            String copiedText = "";
                            if (hasTransferableText) {
                                try {
                                    copiedText = (String) contents.getTransferData(DataFlavor.stringFlavor);

                                } catch (UnsupportedFlavorException | IOException ignored) { }
                            }
                            String clearedText = copiedText.replaceAll("[^a-zA-Z]", "");

                            if (word == null || !word.name.equals(clearedText)){
                                WordGenerator wordGenerator = new WordGenerator();
                                word = wordGenerator.GenerateWord(clearedText);
                            }

                            StringSelection selection = new StringSelection(copiedText);
                            clipboard.setContents(selection, selection);

                            isCopiedFromApplication = true;
                        }
                    }
                }).start();
            }
        });

    }

    private void createTopList() {
        BorderImage borderImage = new BorderImage(new Vector3(
                WIDTH,80,0
        ), Color.valueOf("4257b2"),Color.WHITE);

        borderImage.setPosition(0,AppInfo.HEIGHT - borderImage.getHeight());
        stage.addActor(borderImage);

        topListTable = new Table();
        topListTable.setPosition(borderImage.getX(),borderImage.getY());
        topListTable.setSize(borderImage.getWidth(),borderImage.getHeight());
        topListTable.left();
        stage.addActor(topListTable);

        /*
         *Actors
         */

        Label.LabelStyle titleStyle = new Label.LabelStyle();
        titleStyle.font = Fonts.bl_bold_45;

        Label title = new Label("Word App",titleStyle);

        Sprite btnBgNorm = new Sprite(Assets.assets.pix);
        btnBgNorm.setSize(60,50);
        btnBgNorm.setColor(Color.valueOf("77dddd"));

        Sprite btnBgHover = new Sprite(Assets.assets.pix);
        btnBgHover.setSize(60,50);
        btnBgHover.setColor(Color.valueOf("3ccfcf"));

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = Fonts.bl_bold_26;
        textButtonStyle.up = new SpriteDrawable(btnBgNorm);
        textButtonStyle.over = new SpriteDrawable(btnBgHover);
        textButtonStyle.fontColor = Color.valueOf("4257b2");

        TextButton btnFit = new TextButton("Fit",textButtonStyle);
        btnFit.setSize(60,50);
        btnFit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                camera.setToOrtho(false,WIDTH,AppInfo.HEIGHT);
                camera.update();
                viewport.setWorldSize((int) WIDTH,AppInfo.HEIGHT);

                Gdx.graphics.setWindowedMode(400, AppInfo.HEIGHT);
            }
        });

        title.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                appMain.setScreen(new FirstScreen(appMain));
            }
        });



        /*
         *Filling table
         */

        topListTable.add(title).width(300 - AppInfo.PADDING).padLeft(AppInfo.PADDING);
        topListTable.add(btnFit).padLeft(25);





    }


    private void createActors() {
        final Image bgMiddle = new Image(Assets.assets.pix);
        bgMiddle.setSize(WIDTH,800);
        bgMiddle.setColor(Color.valueOf("7d8ccf"));
        bgMiddle.setPosition(0,AppInfo.HEIGHT - topListTable.getHeight() - bgMiddle.getHeight());
        stage.addActor(bgMiddle);

        Image bgBot = new Image(Assets.assets.pix);
        bgBot.setSize(WIDTH,AppInfo.HEIGHT - topListTable.getHeight() - bgMiddle.getHeight());
        bgBot.setColor(Color.valueOf("8f9cd6"));
        bgBot.setPosition(0,0);
        stage.addActor(bgBot);

        final ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        //Scroll Pane
        {
            Texture pix = new Texture("pix.png");
            Sprite scroll = new Sprite(pix);
            Sprite knob = new Sprite(pix);

            scroll.setSize(20, 100);
            scroll.setColor(Color.LIGHT_GRAY);
            knob.setSize(20, 20);
            knob.setColor(Color.DARK_GRAY);
            scrollPaneStyle.vScroll = new SpriteDrawable(scroll);
            scrollPaneStyle.vScrollKnob = new SpriteDrawable(knob);

            wordCardsScrollPane = new SlowScrollPane(null, scrollPaneStyle);
            wordCardsScrollPane.setScrollingDisabled(true, false);
            wordCardsScrollPane.setVariableSizeKnobs(true);
            wordCardsScrollPane.setOverscroll(false, true);
            wordCardsScrollPane.setSize(WIDTH, bgMiddle.getHeight());
            wordCardsScrollPane.setPosition(0, bgBot.getHeight());
            wordCardsScrollPane.setVelocityY(wordCardsScrollPane.getVelocityY() / 100);
            stage.addActor(wordCardsScrollPane);
        }

        Sprite btnBgNorm = new Sprite(Assets.assets.pix);
        btnBgNorm.setSize(200,50);
        btnBgNorm.setColor(Color.valueOf("4257b2"));

        Sprite btnBgHover = new Sprite(Assets.assets.pix);
        btnBgHover.setSize(200,50);
        btnBgHover.setColor(Color.valueOf("576bc1"));

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = Fonts.bl_bold_26;
        textButtonStyle.up = new SpriteDrawable(btnBgNorm);
        textButtonStyle.over = new SpriteDrawable(btnBgHover);
        textButtonStyle.fontColor = Color.WHITE;

        TextButton btnSelectList = new TextButton("Select List",textButtonStyle);
        btnSelectList.setPosition(AppInfo.PADDING,bgBot.getHeight() - AppInfo.PADDING - 50);
        btnSelectList.setWidth(200);
        stage.addActor(btnSelectList);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = Fonts.bl_bold_26;
        labelStyle.fontColor = Color.BLACK;

        final Label listNameLabel = new Label("",labelStyle);
        listNameLabel.setWidth(WIDTH - 2 * AppInfo.PADDING - btnSelectList.getWidth() - btnSelectList.getX());
        listNameLabel.setPosition(btnSelectList.getX() + btnSelectList.getWidth() + AppInfo.PADDING,
                btnSelectList.getY() +btnSelectList.getHeight() / 2 - listNameLabel.getHeight() / 2);
        listNameLabel.setAlignment(Align.center);


        final Image listNameLabelBg =new Image(Assets.assets.pix);
        listNameLabelBg.setSize(listNameLabel.getWidth(),btnSelectList.getHeight());
        listNameLabelBg.setPosition(listNameLabel.getX(),btnSelectList.getY());
        listNameLabelBg.setColor(Color.valueOf("ffcd1f"));
        listNameLabelBg.setVisible(false);

        stage.addActor(listNameLabelBg);
        stage.addActor(listNameLabel);

        btnSelectList.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                final Group group = new Group();
                Image darkBg = new Image(Assets.assets.pix);
                darkBg.setSize(WIDTH,AppInfo.HEIGHT);
                darkBg.setColor(0.2f,0.2f,0.2f,0.7f);
                group.addActor(darkBg);

                BorderImage borderImage = new BorderImage(
                        new Vector3(450,600,5)
                );
                borderImage.setPosition((WIDTH - borderImage.getWidth()) / 2,
                        (AppInfo.HEIGHT - borderImage.getHeight()) / 2);

                group.addActor(borderImage);
                group.setSize(darkBg.getWidth(),darkBg.getHeight());

                if (DataHolder.dataholder.wordLists.size == 0){
                    Label.LabelStyle labelStyle = new Label.LabelStyle();
                    labelStyle.font = Fonts.bl_bold_32;

                    Label label = new Label("You don't have any list. Create a list from previous menu",labelStyle);
                    label.setWidth(borderImage.getWidth() - 2 * AppInfo.PADDING);
                    label.setWrap(true);
                    label.setAlignment(Align.center);

                    label.setPosition(WIDTH / 2 - label.getWidth() / 2,darkBg.getHeight() / 2 - label.getHeight() / 2);
                    group.addActor(label);
                }else {
                    SlowScrollPane scrollPane = new SlowScrollPane(null, scrollPaneStyle);
                    scrollPane.setScrollingDisabled(true, false);
                    scrollPane.setVariableSizeKnobs(true);
                    scrollPane.setOverscroll(false, true);
                    scrollPane.setSize(borderImage.getWidth() - 2 * AppInfo.PADDING, borderImage.getHeight());
                    scrollPane.setPosition(WIDTH / 2 - scrollPane.getWidth() / 2 ,
                            darkBg.getHeight() / 2 - scrollPane.getHeight() / 2);
                    group.addActor(scrollPane);

                    Table table = new Table();
                    table.setWidth(scrollPane.getWidth());
                    table.center().top();
                    scrollPane.setActor(table);
                    table.defaults().padTop(AppInfo.PADDING);

                    for (int i = 0; i < DataHolder.dataholder.wordLists.size; i++) {
                        Group labelGroup = new Group();
                        labelGroup.setSize(table.getWidth() - 2 * AppInfo.PADDING,40);

                        Image labelBg = new Image(Assets.assets.pix);
                        labelBg.setSize(labelGroup.getWidth(),labelGroup.getHeight());
                        labelBg.setColor(ColorPalette.darkBg);

                        labelGroup.addActor(labelBg);

                        Label.LabelStyle labelStyle = new Label.LabelStyle();
                        labelStyle.font = Fonts.bl_bold_32;
                        Label label = new Label(DataHolder.dataholder.wordLists.get(i).listName,labelStyle);
                        label.setWidth(labelGroup.getWidth());
                        label.setAlignment(Align.center);

                        labelGroup.addActor(label);
                        final  int j = i;
                        labelGroup.addListener(new ClickListener(){
                            @Override
                            public void clicked(InputEvent event, float x, float y) {
                                super.clicked(event, x, y);
                                DataHolder.dataholder.SELECTED_LIST_INDEX = j;
                                listSelected = true;
                                listNameLabel.setText(DataHolder.dataholder.getSelectedList().listName);
                                listNameLabelBg.setVisible(true);
                                group.remove();
                            }
                        });

                        table.add(labelGroup).row();

                    }
                }

                stage.addActor(group);

                darkBg.addListener(new ClickListener(){
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        super.clicked(event, x, y);
                        group.remove();
                    }
                });
            }
        });

        float saveButtonHeight = (bgBot.getHeight() - 4 * AppInfo.PADDING - listNameLabelBg.getHeight()) / 2;

        btnAddToList = new Group();
        btnAddToList.setSize(bgBot.getWidth() - 2 * AppInfo.PADDING,saveButtonHeight);
        btnAddToList.setPosition(AppInfo.PADDING, (2 * saveButtonHeight + AppInfo.PADDING) / 2 - btnAddToList.getHeight() / 2);

        Label.LabelStyle btnLabelStyle = new Label.LabelStyle();
        btnLabelStyle.fontColor = Color.WHITE;
        btnLabelStyle.font = Fonts.bl_bold_45;
        btnAddToList.setOrigin(Align.center);

        final Label lblAddToList = new Label("Add To List",btnLabelStyle);
        lblAddToList.setSize(btnAddToList.getWidth(),btnAddToList.getHeight());
        lblAddToList.setAlignment(Align.center);

        btnAddToList.addActor(lblAddToList);

        stage.addActor(btnAddToList);

        btnAddToList.addListener(new ButtonHoverAnimation(btnAddToList,true){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if (word != null && word.meaningDivs.size > 0) {
                    for (int i = 0; i < DataHolder.dataholder.getSelectedList().words.size; i++) {
                        if (DataHolder.dataholder.getSelectedList().words.get(i).name.equals(word.name)){
                            lblAddToList.addAction(Actions.sequence(
                                    Actions.color(Color.RED,0.3f, Interpolation.fastSlow),
                                    Actions.color(Color.WHITE,0.3f, Interpolation.slowFast)
                            ));
                            return;
                        }
                    }
                    DataHolder.dataholder.getSelectedList().words.add(word);
                    DataHolder.dataholder.saveList(DataHolder.dataholder.getSelectedList());
                    lblAddToList.addAction(Actions.sequence(
                            Actions.color(ColorPalette.text1,0.3f, Interpolation.fastSlow),
                            Actions.color(Color.WHITE,0.3f, Interpolation.slowFast)
                    ));
                }
            }
        });


    }


    public void render(float delta) {
        Gdx.gl.glClearColor(bg.r,bg.g,bg.b,bg.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.end();

        if (word != null && word.meaningDivs.size != 0 && (mainWordCard == null ||
                !mainWordCard.word.name.equals(word.name))){
            mainWordCard = new MainWordCard(word,true,true,true,WIDTH);
            wordCardsScrollPane.setActor(mainWordCard);
        }else if (word != null && word.meaningDivs.size == 0){
            //get from translate
        }

        if (!listSelected){
            if (btnAddToList.isVisible())
                btnAddToList.setVisible(false);
        }else {
            if (!btnAddToList.isVisible())
                btnAddToList.setVisible(true);
        }

        stage.act();
        stage.draw();


    }


    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }
}
