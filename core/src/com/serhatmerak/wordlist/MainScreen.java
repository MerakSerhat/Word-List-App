package com.serhatmerak.wordlist;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.serhatmerak.wordlist.helpers.AppInfo;
import com.serhatmerak.wordlist.helpers.ColorPalette;
import com.serhatmerak.wordlist.helpers.CustomScreen;
import com.serhatmerak.wordlist.helpers.DataHolder;
import com.serhatmerak.wordlist.helpers.Fonts;
import com.serhatmerak.wordlist.huds.BorderImage;
import com.serhatmerak.wordlist.huds.ShowWindow;
import com.serhatmerak.wordlist.huds.WordCardsOption;
import com.serhatmerak.wordlist.huds.WordListsMainActor;
import com.serhatmerak.wordlist.screens.KnownWordsScreen;
import com.serhatmerak.wordlist.word.Word;
import com.serhatmerak.wordlist.word.WordGenerator;


import java.io.IOException;
import java.io.Reader;
import java.io.Writer;


public class MainScreen extends CustomScreen {

    private AppMain app;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Viewport viewport;

    private Stage stage;
    private Label lblStudy;

    private TextButton btnKnownWords,btnWordCards;
    WordListsMainActor wordListsMainActor;


    public MainScreen(AppMain app){
        this.app = app;
        batch = app.batch;
        camera = new OrthographicCamera(AppInfo.WIDTH,AppInfo.HEIGHT);
        camera.setToOrtho(false, AppInfo.WIDTH,AppInfo.HEIGHT);
        viewport = new FitViewport(AppInfo.WIDTH,AppInfo.HEIGHT,camera);
        stage = new Stage(viewport, batch);

        Image bg = new Image(new Texture("pix.png"));
        bg.setSize(AppInfo.WIDTH,AppInfo.HEIGHT);
        bg.setColor(ColorPalette.bg);
        stage.addActor(bg);


        wordListsMainActor = new WordListsMainActor();
        wordListsMainActor.setPosition(AppInfo.PADDING,AppInfo.PADDING);




        stage.addActor(wordListsMainActor);
        Gdx.input.setInputProcessor(stage);

        createActors();
    }

    private void remove4() {
        FileHandle yourHandle = Gdx.files.internal("knownWords/customKnownWords.xml");

        XmlReader xmlReader2 = new XmlReader();
        Reader reader2 = yourHandle.reader("ISO-8859-9");
        XmlReader.Element root2 = xmlReader2.parse(reader2);

        for (XmlReader.Element wordElement : root2.getChildByName("third").getChildrenByName("word")) {
            String name = wordElement.getChildByName("title").getText();
            WordGenerator wordGenerator = new WordGenerator();
            Word word = wordGenerator.GenerateWord(name);
            if (!word.name.equals(name)){
                System.out.println(name + " ->>>> " + word.name);
            }
        }

    }

    private void remove3() throws IOException {

        FileHandle file = Gdx.files.local("knownWords/customKnownWords.xml");
        Writer writer = file.writer(false);
        XmlWriter xmlWriter = new XmlWriter(writer);
//        for (Word word : list.words) {
//            xmlWriter.element("word");
//            xmlWriter.element("name", word.name);
//            xmlWriter.element("star",word.star);
//            for (MeaningDiv d : word.meaningDivs) {
//                xmlWriter.element("div");
//                xmlWriter.element("title", d.title);
//                xmlWriter.element("eng-def", d.engDef);
//                xmlWriter.element("tr-def", d.trDef);
//                xmlWriter.element("examples");
//                for (String ex : d.examples) {
//                    xmlWriter.element("ex", ex);
//                }
//                xmlWriter.pop().pop();
//            }
//            xmlWriter.pop();
//        }
        FileHandle dirHandle = Gdx.files.local("has");

        xmlWriter.element("main");
        xmlWriter.element("first");
        for (final FileHandle listFile:dirHandle.list()) {
            int count = Integer.parseInt(listFile.readString());
            if (count >= 300){
                xmlWriter.element("word");
                xmlWriter.element("title",listFile.name());
                xmlWriter.element("known",true);
                xmlWriter.pop();
            }

        }
        xmlWriter.pop();
        xmlWriter.element("second");
        for (final FileHandle listFile:dirHandle.list()) {
            int count = Integer.parseInt(listFile.readString());
            if (count >100 && 300 > count){
                xmlWriter.element("word");
                xmlWriter.element("title",listFile.name());
                xmlWriter.element("known",true);
                xmlWriter.pop();
            }

        }
        xmlWriter.pop();
        xmlWriter.element("third");
        for (final FileHandle listFile:dirHandle.list()) {
            int count = Integer.parseInt(listFile.readString());
            if (count <= 100){
                xmlWriter.element("word");
                xmlWriter.element("title",listFile.name());
                xmlWriter.element("known",true);
                xmlWriter.pop();
            }

        }
        xmlWriter.pop();
        xmlWriter.pop();
        xmlWriter.close();

    }

    private void remove2() {
        FileHandle dirHandle = Gdx.files.absolute("C:/Users/merak/Desktop/WordListApp/core/assets/has");
        FileHandle newFile = Gdx.files.absolute("C:/Users/merak/Desktop/WordListApp/core/assets/sa.txt");

        String sa = "I ";
        for (final FileHandle listFile:dirHandle.list()) {
            sa += listFile.name() + " ";
        }
        newFile.writeString(sa,false);
    }

    private void createActors() {

        BorderImage borderImage = new BorderImage(new Vector3(
                AppInfo.WIDTH - 3 * AppInfo.PADDING - wordListsMainActor.getWidth(),
                wordListsMainActor.getHeight(),
                4
        ));
        borderImage.setPosition(AppInfo.WIDTH - AppInfo.PADDING - borderImage.getWidth(),
                AppInfo.PADDING);


        Table table = new Table();
        table.setSize(borderImage.getWidth(),borderImage.getHeight());
        table.setPosition(AppInfo.WIDTH - AppInfo.PADDING - borderImage.getWidth(),
                AppInfo.PADDING);
        table.top().padTop(50);
//        table.debug();


        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.fontColor = Color.WHITE;
        textButtonStyle.overFontColor = Color.LIGHT_GRAY;
        textButtonStyle.downFontColor = Color.DARK_GRAY;
        textButtonStyle.disabledFontColor = Color.DARK_GRAY;
        textButtonStyle.font = Fonts.bl_bold_45;

        btnWordCards = new TextButton("Word Cards",textButtonStyle);
        btnKnownWords = new TextButton("Known Words",textButtonStyle);


        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = Fonts.bl_bold_32;

        lblStudy = new Label("Study List: ",labelStyle);
        lblStudy.setColor(Color.LIGHT_GRAY);


        table.add(lblStudy).expandX().left().padLeft(30).row();
        table.add(btnWordCards).center().padBottom(150).row();
        table.add(btnKnownWords).center().row();

        btnKnownWords.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                app.setScreen(new KnownWordsScreen(app));
            }
        });

        btnWordCards.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                final ShowWindow showWindow = new ShowWindow(new WordCardsOption(app));
//                final ShowWindow showWindow = new ShowWindow(new StudyWordCard(DataHolder.dataholder.wordLists.first().words.first(),new StudyWordCard.StudyWordCardStyle()));
                stage.addActor(showWindow);
            }
        });

        stage.addActor(borderImage);
        stage.addActor(table);

    }

    private void remove() {
        ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        Texture pix = new Texture("pix.png");

        Sprite scroll = new Sprite(pix);
        Sprite knob = new Sprite(pix);
        scrollPaneStyle.vScroll = new SpriteDrawable(scroll);
        scrollPaneStyle.vScrollKnob = new SpriteDrawable(knob);
        ScrollPane topLabelListScrollPane = new ScrollPane(null, scrollPaneStyle);
        topLabelListScrollPane.setVariableSizeKnobs(true);
        topLabelListScrollPane.setOverscroll(false, false);
        topLabelListScrollPane.setSize(300,1000);
        topLabelListScrollPane.setPosition(1600,40);

        Table table = new Table();
        table.setWidth(1600);
        topLabelListScrollPane.setActor(table);

        FileHandle dirHandle = Gdx.files.absolute("C:/Users/merak/Desktop/WordListApp/core/assets/has");

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = Fonts.bl_bold_26;
        for (final FileHandle listFile:dirHandle.list()) {
            int count = Integer.parseInt(listFile.readString());
            if (count >800 && count < 1000){
                Label label = new Label(listFile.name(),labelStyle);
                label.addListener(new ClickListener(){
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        super.clicked(event, x, y);
                        listFile.delete();
                    }
                });
                table.add(label).row();
            }
        }

        stage.addActor(topLabelListScrollPane);
    }


    public void render(float delta) {
        Gdx.gl.glClearColor(ColorPalette.bg.r,ColorPalette.bg.g,ColorPalette.bg.b,ColorPalette.bg.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.end();

        checkLblStudy();

        stage.act();
        stage.draw();
    }

    private void checkLblStudy() {
        if (DataHolder.dataholder.wordLists.size == 0){
            if (!btnWordCards.isDisabled()){
                btnWordCards.setDisabled(true);
                lblStudy.setText("");
            }
        }else {
            if (btnWordCards.isDisabled())
                btnWordCards.setDisabled(false);

            if (!lblStudy.getText().toString().equals("Study List: " + DataHolder.dataholder.wordLists.
                    get(DataHolder.dataholder.SELECTED_LIST_INDEX).listName)){
                lblStudy.setText("Study List: " + DataHolder.dataholder.wordLists.get(DataHolder.dataholder.SELECTED_LIST_INDEX).listName);
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);

    }

}
