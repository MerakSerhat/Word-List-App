package com.serhatmerak.wordlist.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.serhatmerak.wordlist.AppMain;
import com.serhatmerak.wordlist.helpers.AppInfo;
import com.serhatmerak.wordlist.helpers.ColorPalette;
import com.serhatmerak.wordlist.helpers.CustomScreen;
import com.serhatmerak.wordlist.helpers.Fonts;
import com.serhatmerak.wordlist.huds.Alert;
import com.serhatmerak.wordlist.kumar.SlowScrollPane;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class MetinScreen extends CustomScreen {

    private Viewport viewport;
    private Stage stage;
    private OrthographicCamera camera;
    private AppMain appMain;
    private SpriteBatch batch;

    private Label wholeLabel;

    public MetinScreen(AppMain appMain){
        this.appMain = appMain;
        batch = appMain.batch;

        camera = new OrthographicCamera(AppInfo.WIDTH,AppInfo.HEIGHT);
        camera.setToOrtho(false, AppInfo.WIDTH,AppInfo.HEIGHT);
        viewport = new FitViewport(AppInfo.WIDTH,AppInfo.HEIGHT,camera);
        stage = new Stage(viewport, batch);


        Gdx.input.setInputProcessor(stage);

        createActors();
    }

    private void createActors() {
        ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        Texture pix = new Texture("pix.png");

        Sprite scroll = new Sprite(pix);
        Sprite knob = new Sprite(pix);

        scroll.setSize(20,100);
        scroll.setColor(Color.LIGHT_GRAY);
        knob.setSize(20,20);
        knob.setColor(Color.DARK_GRAY);
        scrollPaneStyle.vScroll = new SpriteDrawable(scroll);
        scrollPaneStyle.vScrollKnob = new SpriteDrawable(knob);

        SlowScrollPane wordCardsScrollPane = new SlowScrollPane(null, scrollPaneStyle);
        wordCardsScrollPane.setScrollingDisabled(true,false);
        wordCardsScrollPane.setVariableSizeKnobs(true);
        wordCardsScrollPane.setOverscroll(false, true);
        wordCardsScrollPane.setSize(1000,1000);
        wordCardsScrollPane.setPosition(AppInfo.PADDING,40);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = Fonts.bl_bold_32;
        labelStyle.fontColor = Color.BLACK;

        wholeLabel = new Label("",labelStyle);
        wholeLabel.setWidth(1000);
        wholeLabel.setWrap(true);
        wordCardsScrollPane.setActor(wholeLabel);

        Label lblPaste = new Label("PASTE",labelStyle);
        lblPaste.setPosition(1300,700);

        Label lblRemoveOthers = new Label("REMOVE\nOTHERS",labelStyle);
        lblRemoveOthers.setPosition(1300,600);

        Label lblSplit = new Label("Split",labelStyle);
        lblSplit.setPosition(1300,500);


        stage.addActor(wordCardsScrollPane);
        stage.addActor(lblPaste);
        stage.addActor(lblSplit);
        stage.addActor(lblRemoveOthers);

        lblPaste.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                final Transferable contents = clipboard.getContents(null);
                boolean hasTransferableText = (contents != null) && contents.isDataFlavorSupported(DataFlavor.stringFlavor);
                String copiedText = "";
                if (hasTransferableText) {
                    try {
                        copiedText = (String) contents.getTransferData(DataFlavor.stringFlavor);

                    } catch (UnsupportedFlavorException | IOException ignored) { }
                    wholeLabel.setText(copiedText);
                    System.out.println(copiedText);


                }else {
                    Alert.show(wholeLabel.getStage(),"Copied data is not valid!");
                }
            }
        });

        lblSplit.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
            }
        });

        lblRemoveOthers.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                wholeLabel.setText(wholeLabel.getText().toString().replaceAll("[^a-zA-Z]", " "));

            }
        });
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
