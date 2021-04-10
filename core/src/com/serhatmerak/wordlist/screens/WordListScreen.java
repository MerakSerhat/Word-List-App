package com.serhatmerak.wordlist.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
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
import com.serhatmerak.wordlist.huds.addwordhuds.AddWordWindow;
import com.serhatmerak.wordlist.huds.Alert;
import com.serhatmerak.wordlist.huds.BorderImage;
import com.serhatmerak.wordlist.huds.ColorButton;
import com.serhatmerak.wordlist.huds.QuestionPanel;
import com.serhatmerak.wordlist.huds.ShowWindow;
import com.serhatmerak.wordlist.huds.WordCardsOption;
import com.serhatmerak.wordlist.huds.WordListTable;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class WordListScreen extends CustomScreen {

    private OrthographicCamera camera;
    private Stage stage;
    private AppMain appMain;
    private SpriteBatch batch;

    Color bg = Color.valueOf("f5f6fa");
//    Color bg = ColorPalette.Cream;

    private Group leftListGroup;
    private Table leftListTable;
    private ColorButton signedButton;

    private Group topListGroup;
    private Table topListTable;
    private Table topChangingTable;

    private Group wordListOptions;
    private boolean showKnown = true,showExample,showAll,showTurkish,showStars;

    private WordListTable wordListsMainActor;
    private Image darkBg;
    private ColorButton btnStudyCards;
    private ColorButton btnWordTest;
    private BorderImage leftListBg;
    private ShowWindow window;

    public WordListScreen(AppMain appMain) {
        this.appMain = appMain;
        batch = appMain.batch;

        camera = new OrthographicCamera(AppInfo.WIDTH, AppInfo.HEIGHT);
        camera.setToOrtho(false, AppInfo.WIDTH, AppInfo.HEIGHT);
        Viewport viewport = new StretchViewport(AppInfo.WIDTH, AppInfo.HEIGHT, camera);
        stage = new Stage(viewport, batch);

        Gdx.input.setInputProcessor(stage);

        createActors();

        stage.addListener(new InputListener(){
            @Override
            public boolean keyDown(InputEvent event, int keycode) {

                if (keycode == Input.Keys.PLUS){
                    DataHolder.dataholder.bigWordCards = true;
                    wordListsMainActor.checkListAndFillTable();
                }else if (keycode == Input.Keys.MINUS){
                    DataHolder.dataholder.bigWordCards = false;
                    wordListsMainActor.checkListAndFillTable();
                }

                return super.keyDown(event, keycode);
            }
        });
    }

    private void createActors() {
        createTopList();

        wordListsMainActor = new WordListTable(AppInfo.WIDTH,
                AppInfo.HEIGHT - topListTable.getHeight());
        wordListsMainActor.setPosition(0,0);
        stage.addActor(wordListsMainActor);

        stage.addActor(darkBg);
        createLeftList();
        createTopButtons();
    }

    private void createTopButtons() {
        Sprite btnBgNorm = new Sprite(Assets.assets.pix);
        btnBgNorm.setSize(200,50);
        btnBgNorm.setColor(Color.valueOf("77dddd"));

        Sprite btnBgHover = new Sprite(Assets.assets.pix);
        btnBgHover.setSize(200,50);
        btnBgHover.setColor(Color.valueOf("3ccfcf"));

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = Fonts.bl_bold_26;
        textButtonStyle.up = new SpriteDrawable(btnBgNorm);
        textButtonStyle.over = new SpriteDrawable(btnBgHover);
        textButtonStyle.fontColor = Color.valueOf("4257b2");

        TextButton btnAddWord = new TextButton("Add Word", textButtonStyle);
        btnAddWord.setSize(200,50);
        btnAddWord.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                AddWordWindow addWordWindow = new AddWordWindow(DataHolder.dataholder.wordLists.get(DataHolder.dataholder.SELECTED_LIST_INDEX),
                        wordListsMainActor);
                window = new ShowWindow(addWordWindow);

                window.img.setSize(window.img.getWidth(),stage.getViewport().getWorldHeight());
                window.group.setPosition(AppInfo.WIDTH / 2f - window.group.getWidth() / 2,
                        stage.getViewport().getWorldHeight() / 2f - window.group.getHeight() / 2);

                stage.addActor(window);
            }
        });

        createWordListOptions();

        /*
        Add To Table
         */

        topChangingTable.add(btnAddWord).padLeft(50);
        topChangingTable.add(wordListOptions).width(wordListOptions.getWidth()).
                height(wordListOptions.getHeight()).padLeft(200);
    }

    private void createWordListOptions() {
        TextButton.TextButtonStyle textButtonStyle2 = new TextButton.TextButtonStyle();
        textButtonStyle2.font = Fonts.bl_bold_45;
        textButtonStyle2.fontColor = Color.WHITE;
        textButtonStyle2.downFontColor = Color.DARK_GRAY;
        textButtonStyle2.overFontColor = Color.LIGHT_GRAY;

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = Fonts.bl_bold_32;


        final Label btnShowExamples = new Label("Examples",labelStyle);
        final Label btnShowTurkish = new Label("Turkish Def.",labelStyle);
        final Label btnShowAll = new Label("Expand",labelStyle);

        btnShowExamples.setScale(1.2f);
        btnShowAll.setScale(1.2f);
        btnShowTurkish.setScale(1.2f);

        final Image exampleBg = new Image(Assets.assets.pix);
        exampleBg.setSize(btnShowExamples.getWidth() + 30,btnShowExamples.getHeight() + 4);
        exampleBg.setColor( Color.valueOf("4257b2"));

        final Image turkishBg = new Image(Assets.assets.pix);
        turkishBg.setSize(btnShowTurkish.getWidth() + 30,btnShowTurkish.getHeight() + 4);
        turkishBg.setColor( Color.valueOf("4257b2"));

        final Image allBg = new Image(Assets.assets.pix);
        allBg.setSize(btnShowAll.getWidth() + 30,btnShowAll.getHeight() + 4);
        allBg.setColor( Color.valueOf("4257b2"));



        final Image btnStar = new Image(Assets.assets.star);

        final Image btnKnown = new Image(Assets.assets.tick);




        btnShowAll.setOrigin(Align.center);
        btnShowExamples.setOrigin(Align.center);
        btnShowTurkish.setOrigin(Align.center);
        btnStar.setOrigin(Align.center);
        btnKnown.setOrigin(Align.center);

        btnShowAll.setPosition(0,0);
        allBg.setPosition(btnShowAll.getX() - 15,btnShowAll.getY() - 2);

        btnShowTurkish.setPosition(btnShowAll.getWidth() +
                3 * AppInfo.PADDING,0);
        turkishBg.setPosition(btnShowTurkish.getX() - 15,btnShowTurkish.getY() - 2);

        btnShowExamples.setPosition(btnShowTurkish.getX()
                + btnShowTurkish.getWidth() + 3 * AppInfo.PADDING,0);
        exampleBg.setPosition(btnShowExamples.getX() - 15,btnShowExamples.getY() - 2);

        btnStar.setPosition(btnShowExamples.getX() + btnShowExamples.getWidth()
                + 3 * AppInfo.PADDING,btnShowAll.getHeight() / 2 - btnStar.getHeight() / 2);

        btnKnown.setPosition(btnStar.getX() + btnStar.getWidth()
                + 3 * AppInfo.PADDING,btnShowAll.getHeight() / 2 - btnKnown.getHeight() / 2);

        btnKnown.setColor(ColorPalette.text1);


        wordListOptions = new Group();
        wordListOptions.addActor(allBg);
        wordListOptions.addActor(exampleBg);
        wordListOptions.addActor(turkishBg);
        wordListOptions.addActor(btnShowAll);
        wordListOptions.addActor(btnShowTurkish);
        wordListOptions.addActor(btnShowExamples);
        wordListOptions.addActor(btnStar);
        wordListOptions.addActor(btnKnown);
        wordListOptions.setSize(btnKnown.getX() + btnKnown.getWidth(),
                Math.max(btnKnown.getHeight(),btnShowAll.getHeight()));


        btnShowExamples.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                showExample = !showExample;
                if (showExample) {
                    exampleBg.setColor(Color.valueOf("ffcd1f"));
                    btnShowExamples.setColor(Color.BLACK);
                }else {
                    exampleBg.setColor(Color.valueOf("4257b2"));
                    btnShowExamples.setColor(Color.WHITE);

                }
                //TODO: card change
                wordListsMainActor.changeCardType(showExample,
                        showTurkish,showAll);
            }
        });

        btnShowAll.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                showAll = !showAll;
                if (showAll) {
                    allBg.setColor(Color.valueOf("ffcd1f"));
                    btnShowAll.setColor(Color.BLACK);
                }else {
                    allBg.setColor(Color.valueOf("4257b2"));
                    btnShowAll.setColor(Color.WHITE);
                }

                //TODO: card change
                wordListsMainActor.changeCardType(showExample,
                        showTurkish,showAll);            }
        });

        btnShowTurkish.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                showTurkish = !showTurkish;
                if (showTurkish) {
                    turkishBg.setColor(Color.valueOf("ffcd1f"));
                    btnShowTurkish.setColor(Color.BLACK);
                }else {
                    turkishBg.setColor(Color.valueOf("4257b2"));
                    btnShowTurkish.setColor(Color.WHITE);

                }

                //TODO: card change
                wordListsMainActor.changeCardType(showExample,
                        showTurkish,showAll);            }
        });

        btnStar.addListener(new ButtonHoverAnimation(btnStar){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                showStars = !showStars;
                if (showStars)
                    btnStar.setColor(Color.GOLD);
                else
                    btnStar.setColor(Color.WHITE);

                //TODO: card change
                wordListsMainActor.showStars = showStars;
                wordListsMainActor.checkListAndFillTable();
            }
        });

        btnKnown.addListener(new ButtonHoverAnimation(btnKnown){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                showKnown = !showKnown;
                if (showKnown)
                    btnKnown.setColor(ColorPalette.text1);
                else
                    btnKnown.setColor(Color.WHITE);

                //TODO: card change
               wordListsMainActor.showKnown = showKnown;
               wordListsMainActor.checkListAndFillTable();
            }
        });

    }


    private void createLeftList() {
        leftListBg = new BorderImage(new Vector3(
                300, AppInfo.HEIGHT - topListTable.getHeight(), 0
        ), Color.WHITE, Color.WHITE);

        leftListBg.setPosition(0,0);
//        stage.addActor(leftListBg);


        leftListTable = new Table();
        leftListTable.setPosition(leftListBg.getX(), leftListBg.getY());
        leftListTable.setSize(leftListBg.getWidth(), leftListBg.getHeight());
        leftListTable.top();
//        stage.addActor(leftListTable);

        leftListGroup = new Group();
        leftListGroup.setSize(leftListBg.getWidth(),leftListBg.getHeight());
        leftListGroup.setPosition(-leftListBg.getWidth(),0);
        leftListGroup.addActor(leftListBg);
        leftListGroup.addActor(leftListTable);
        stage.addActor(leftListGroup);

////        leftListTable.debug();
//
//
        /*
         *Actors
         */

        Group titleGroup = new Group();

        Image titleBg = new Image(Assets.assets.pix);
        titleBg.setSize(leftListBg.getWidth(),60);
        titleBg.setColor(Color.valueOf("ffcd1f"));

        Label.LabelStyle titleStyle = new Label.LabelStyle();
        titleStyle.font = Fonts.bl_bold_32;

        Label lblTitle = new Label(DataHolder.dataholder.getSelectedList().listName,titleStyle);
        lblTitle.setColor(Color.BLACK);
        lblTitle.setPosition(titleBg.getWidth() / 2 - lblTitle.getWidth() / 2,
                titleBg.getHeight() / 2 - lblTitle.getHeight() / 2);

        titleGroup.setSize(titleBg.getWidth(),titleBg.getHeight());
        titleGroup.addActor(titleBg);
        titleGroup.addActor(lblTitle);


        Image line0 = new Image(Assets.assets.pix);
        line0.setSize(leftListBg.getWidth(),200);
        line0.setColor(Color.valueOf("ffcd1f"));

        final ColorButton btnWordList = new ColorButton("Word List");
        btnWordList.setSize(leftListBg.getWidth(),60);
        btnWordList.select();
        signedButton = btnWordList;

        Image line1 = new Image(Assets.assets.pix);
        line1.setSize(leftListBg.getWidth(),200);
        line1.setColor(Color.valueOf("edeff4"));

        btnStudyCards = new ColorButton("Study Cards");
        btnStudyCards.setSize(leftListBg.getWidth(),60);

        btnWordTest = new ColorButton("Word Test");
        btnWordTest.setSize(leftListBg.getWidth(),60);

        Image line2 = new Image(Assets.assets.pix);
        line2.setSize(leftListBg.getWidth(),200);
        line2.setColor(Color.valueOf("edeff4"));

        final ColorButton btnExport = new ColorButton("Export List");
        btnExport.setSize(leftListBg.getWidth(),60);

        final ColorButton btnRemove = new ColorButton("Remove List");
        btnRemove.setSize(leftListBg.getWidth(),60);

        Image line3 = new Image(Assets.assets.pix);
        line3.setSize(leftListBg.getWidth(),200);
        line3.setColor(Color.valueOf("edeff4"));

        final ColorButton btnBack = new ColorButton("Back");
        btnBack.setSize(leftListBg.getWidth(),60);

        /*
         *Filling table
         */
        leftListTable.add(titleGroup).padTop(30).row();
        leftListTable.add(line0).width(leftListBg.getWidth()).height(5).padTop(30).row();
        leftListTable.add(btnWordList).padTop(30).row();
        leftListTable.add(line1).width(leftListBg.getWidth()).height(5).padTop(30).row();
        leftListTable.add(btnStudyCards).padTop(30).row();
        leftListTable.add(btnWordTest).row();
        leftListTable.add(line2).width(leftListBg.getWidth()).height(5).padTop(30).row();
        leftListTable.add(btnExport).padTop(30).row();
        leftListTable.add(btnRemove).row();
        leftListTable.add(line3).width(leftListBg.getWidth()).height(5).padTop(30).row();
        leftListTable.add(btnBack).padBottom(30).bottom().expandY().row();

//        /*
//         *Listeners
//         */
//

        btnExport.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                Alert.show(stage,
                        "Select a file to export file");

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        JFileChooser chooser = new JFileChooser();
                        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                        chooser.setDragEnabled(true);
                        JFrame f = new JFrame();
                        f.setVisible(true);
                        f.toFront();
                        f.setVisible(false);
                        int res = chooser.showSaveDialog(f);
                        f.dispose();
                        if (res == JFileChooser.APPROVE_OPTION) {
                            FileHandle file = Gdx.files.absolute(chooser.getSelectedFile().getAbsolutePath() + "/" +
                                    DataHolder.dataholder.getSelectedList().listName + ".wordlist");
                            file.writeString(DataHolder.dataholder.getSelectedListTextForExporting(),false);
                        }
                    }
                }).start();

            }
        });

        btnRemove.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                QuestionPanel.QuestionPanelStyle questionPanelStyle = new QuestionPanel.QuestionPanelStyle(){
                    @Override
                    public void yesClicked() {
                        DataHolder.dataholder.removeListFile(DataHolder.dataholder.SELECTED_LIST_INDEX);

                        DataHolder.dataholder.SELECTED_LIST_INDEX = 0;
                        appMain.setScreen(new FirstScreen(appMain));
                    }
                };
                questionPanelStyle.title = "Want to remove the list?";
                questionPanelStyle.bigText = DataHolder.dataholder.getSelectedList().listName;
                questionPanelStyle.normalText = "Do you want to remove this list?\nYou can't bring it back!";;

                QuestionPanel questionPanel = new QuestionPanel(questionPanelStyle);
                stage.addActor(questionPanel);


            }
        });


        btnStudyCards.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

//                if (DataHolder.dataholder.getSelectedList().words.size == 0){
//                    Alert.show(stage,
//                            "There must be at least a word to study word cards!");
//                }else {
//
//                    WordCardsOption wordCardsOption = new WordCardsOption(appMain);
//                /*
//                set disabilities
//                 */
//                    if (DataHolder.dataholder.getStarryWordCount() == 0) {
//                        wordCardsOption.lblStarryWords.setTouchable(Touchable.disabled);
//                        wordCardsOption.lblStarryWords.setColor(Color.DARK_GRAY);
//                    }
//
//                    if (DataHolder.dataholder.getUnknownWordCount() == 0) {
//                        wordCardsOption.selectAllWordsOption();
//                        wordCardsOption.lblUnknownWords.setColor(Color.DARK_GRAY);
//                        wordCardsOption.lblUnknownWords.setTouchable(Touchable.disabled);
//                    }
//
//
//                    final ShowWindow showWindow = new ShowWindow(wordCardsOption);
//
//                    stage.addActor(showWindow);
//                }

                appMain.setScreen(new StudyCardScreen(appMain));


            }
        });

        btnWordTest.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
//                Alert.show(stage,
//                        "We are working hardly to create this part.");

                if (DataHolder.dataholder.getSelectedList().words.size < 5){
                    Alert.show(stage,
                            "There must be at least 5 words in this list to create a test!",3);
                }else {
                    appMain.setScreen(new WordTestScreen(appMain));
                }

            }
        });

        btnBack.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if (signedButton != btnBack){

//                    mainTable.clear();

                    appMain.setScreen(new FirstScreen(appMain));

                    btnBack.select();
                    signedButton.unselect();
                    signedButton = btnBack;
                }

            }
        });


    }
    private void createTopList() {
        BorderImage topListBg = new BorderImage(new Vector3(
                AppInfo.WIDTH,80,0
        ), Color.valueOf("4257b2"),Color.WHITE);

//        borderImage.setPosition(0,AppInfo.HEIGHT - borderImage.getHeight());
//        stage.addActor(borderImage);

        topListTable = new Table();
//        topListTable.setPosition(borderImage.getX(),borderImage.getY());
        topListTable.setSize(topListBg.getWidth(),topListBg.getHeight());
        topListTable.left();
//        stage.addActor(topListTable);



        topListGroup = new Group();
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

        topChangingTable = new Table();
        topChangingTable.setSize(1500,topListTable.getHeight());
        topChangingTable.center();

        final Image menu = new Image(Assets.assets.menu);

        Group menuGroup = new Group();
        menuGroup.setSize(menu.getWidth() + 40,menu.getHeight() + 30);
        menu.setPosition(20,15);

        menuGroup.addActor(menu);

        menuGroup.addListener(new ClickListener(){
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                menu.setColor(Color.LIGHT_GRAY);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                menu.setColor(Color.WHITE);
            }
        });

        Image line = new Image(Assets.assets.pix);
        line.setSize(3,60);


        /*

         */
        darkBg = new Image(Assets.assets.pix);
        darkBg.setSize(AppInfo.WIDTH,AppInfo.HEIGHT - topListGroup.getHeight());
        darkBg.setColor(0,0,0,0);
        darkBg.setTouchable(Touchable.disabled);

        ClickListener leftTableOpeningListener = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if (leftListGroup.getActions().size != 0) return;
                if (leftListGroup.getX() != 0) {
                    leftListGroup.addAction(Actions.moveTo(0, 0, 0.3f, Interpolation.fastSlow));

                    //dark bg action
                    darkBg.addAction(Actions.sequence(
                            Actions.alpha(0.7f,0.3f,Interpolation.fastSlow),
                            Actions.run(new Runnable() {
                                @Override
                                public void run() {
                                    darkBg.setTouchable(Touchable.enabled);
                                }
                            })
                    ));


                }else {
                    leftListGroup.addAction(Actions.moveTo(-leftListGroup.getWidth(), 0, 0.3f,
                            Interpolation.slowFast));

                    darkBg.addAction(Actions.sequence(
                            Actions.color(new Color(0,0,0,0),0.3f,Interpolation.slowFast),
                            Actions.run(new Runnable() {
                                @Override
                                public void run() {
                                    darkBg.setTouchable(Touchable.disabled);
                                }
                            })
                    ));

                }
            }
        };

        darkBg.addListener(leftTableOpeningListener);


        stage.addActor(topListGroup);
        /*
         *Filling table
         */
        topListTable.add(menuGroup).padLeft(2 * AppInfo.PADDING);
        topListTable.add(line).width(2).height(60).padLeft(2 * AppInfo.PADDING);
        topListTable.add(title).width(300 - AppInfo.PADDING).padLeft(2 * AppInfo.PADDING);
        topListTable.add(topChangingTable);


        /*
        * Listener
         */

        menuGroup.addListener(leftTableOpeningListener);


        topListBg.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if (topListGroup.getY() == stage.getViewport().getWorldHeight() - AppInfo.PADDING){
                    topListGroup.addAction(Actions.moveTo(0,stage.getViewport().getWorldHeight()
                                    - topListGroup.getHeight(),
                            0.3f, Interpolation.fastSlow));
                    wordListsMainActor.setHeight(stage.getViewport().getWorldHeight()
                            - topListGroup.getHeight());
                }else {
                    topListGroup.addAction(Actions.moveTo(0,stage.getViewport().getWorldHeight()
                                    - AppInfo.PADDING,
                            0.3f, Interpolation.fastSlow));
                    wordListsMainActor.setHeight(stage.getViewport().getWorldHeight()
                            - AppInfo.PADDING);
                }
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

//        checkThings();
    }

    @Override
    public void resize(int width, int height) {



        if(height > width) {
            stage.getViewport().setWorldSize(AppInfo.WIDTH,
                    ((float)height / (float)width) * AppInfo.WIDTH);
        }else {
            stage.getViewport().setWorldSize(AppInfo.WIDTH, AppInfo.HEIGHT);
        }



        topListGroup.setPosition(0,stage.getViewport().getWorldHeight() -
                topListGroup.getHeight());
        wordListsMainActor.setHeight(stage.getViewport().getWorldHeight() - topListGroup.getHeight());

        leftListBg.setHeight(stage.getViewport().getWorldHeight());
        leftListGroup.setHeight(stage.getViewport().getWorldHeight());
        leftListTable.setY(leftListBg.getHeight() - leftListTable.getHeight());

        darkBg.setSize(darkBg.getWidth(),stage.getViewport().getWorldHeight());
        if (window != null && window.getStage() != null){
            window.img.setSize(window.img.getWidth(),stage.getViewport().getWorldHeight());
            window.group.setPosition(AppInfo.WIDTH / 2f - window.group.getWidth() / 2,
                    stage.getViewport().getWorldHeight() / 2f - window.group.getHeight() / 2);
        }
        darkBg.setSize(darkBg.getWidth(),stage.getViewport().getWorldHeight());

        stage.getViewport().update(width, height, true);

    }
}
