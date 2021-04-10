package com.serhatmerak.wordlist.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.serhatmerak.wordlist.AppMain;
import com.serhatmerak.wordlist.helpers.AppInfo;
import com.serhatmerak.wordlist.helpers.Assets;
import com.serhatmerak.wordlist.helpers.CustomScreen;
import com.serhatmerak.wordlist.helpers.Fonts;
import com.serhatmerak.wordlist.kumar.SlowScrollPane;

public class ReadBookScreen extends CustomScreen {

    private OrthographicCamera camera;
    private AppMain appMain;
    private Viewport viewport;
    private Stage stage;
    private SpriteBatch batch;

    Color bg = Color.valueOf("f5f6fa");
    private SlowScrollPane slowScrollPane;

    public ReadBookScreen(AppMain appMain){
        this.appMain = appMain;
        batch = appMain.batch;

        camera = new OrthographicCamera(AppInfo.WIDTH,AppInfo.HEIGHT);
        camera.setToOrtho(false, AppInfo.WIDTH,AppInfo.HEIGHT);
        viewport = new StretchViewport(AppInfo.WIDTH,AppInfo.HEIGHT,camera);
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
        scrollPaneStyle.background = new SpriteDrawable(new Sprite(Assets.assets.pix));

        slowScrollPane = new SlowScrollPane(null,scrollPaneStyle);
        slowScrollPane.setScrollingDisabled(true,false);
        slowScrollPane.setVariableSizeKnobs(true);
        slowScrollPane.setOverscroll(false, true);
        slowScrollPane.setSize((AppInfo.WIDTH) / 2f ,AppInfo.HEIGHT - 30);
        slowScrollPane.setPosition(15,15);

        stage.addActor(slowScrollPane);

        createLabels();
//        FileHandle listFile = Gdx.files.internal("belge.txt");
//        String s = listFile.readString();
//
//        Label.LabelStyle labelStyle = new Label.LabelStyle();
//        labelStyle.font = Fonts.bl_bold_36;
//
//        Label label = new Label(s,labelStyle);
//        label.setColor(Color.BLACK);
//        slowScrollPane.setActor(label);

    }

    private void createLabels() {
        FileHandle listFile = Gdx.files.internal("Zengin Baba Yoksul Baba.txt");
        String text = listFile.readString();
        text = text.replaceAll("E-Text Conversion by Nalanda Digital Library","").
                replaceAll("Brave New World By Aldous Huxley","");


        Group biggestGroup = new Group();
        biggestGroup.setWidth(AppInfo.WIDTH / 2f);


        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = Fonts.bl_bold_32;

        Label allText = new Label(text,labelStyle);
        allText.setColor(Color.BLACK);

        String[] firstSplitedArray = text.split("\\r?\\n");
        Array<Label> lineLabels = new Array<>();

        GlyphLayout layout = new GlyphLayout();
        layout.setText(labelStyle.font,"Brave");

        for (int i = 0;i < firstSplitedArray.length;i++) {
            final String[] splitedFromLineWords = firstSplitedArray[i].split(" ");
            final Array<Array<Float>> wordBounds = new Array<>();

            for (String word:splitedFromLineWords) {



                String previousString = firstSplitedArray[i].substring(
                       0, firstSplitedArray[i].indexOf(word)
                );



                layout.setText(labelStyle.font,previousString);
                float minX = layout.width;
                layout.setText(labelStyle.font,word);
                float maxX = minX + layout.width;



                Array<Float> wordBound = new Array<>();
                wordBound.add(minX,maxX);
                wordBounds.add(wordBound);
            }

            final Label label = new Label(firstSplitedArray[i],labelStyle);
            label.setColor(Color.BLACK);
            label.setHeight(48);

            if(label.getWidth() > slowScrollPane.getWidth() - 2 * AppInfo.PADDING)
                slowScrollPane.setWidth(label.getWidth() + 2 * AppInfo.PADDING);

            label.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);

                    int i = 0;
                    for (Array<Float> wordBound:wordBounds) {
                        if (x >= wordBound.first() && x <= wordBound.get(1)){
                            System.out.println(splitedFromLineWords[i]);
                        }
                        i++;
                    }
                }
            });

            lineLabels.add(label);
        }

        for (int i = 0; i < lineLabels.size; i++) {
            int index = lineLabels.size - 1 - i;
            lineLabels.get(index).setY( i * lineLabels.get(index).getHeight());
            biggestGroup.addActor(lineLabels.get(index));
        }
        biggestGroup.setHeight(lineLabels.size * lineLabels.first().getHeight());
        slowScrollPane.setActor(biggestGroup);

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
}
