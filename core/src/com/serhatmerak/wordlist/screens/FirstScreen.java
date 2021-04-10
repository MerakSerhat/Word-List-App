package com.serhatmerak.wordlist.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.serhatmerak.wordlist.AppMain;
import com.serhatmerak.wordlist.helpers.AppInfo;
import com.serhatmerak.wordlist.helpers.Assets;
import com.serhatmerak.wordlist.helpers.ColorPalette;
import com.serhatmerak.wordlist.helpers.CustomScreen;
import com.serhatmerak.wordlist.helpers.DataHolder;
import com.serhatmerak.wordlist.helpers.Fonts;
import com.serhatmerak.wordlist.huds.Alert;
import com.serhatmerak.wordlist.huds.BorderImage;
import com.serhatmerak.wordlist.huds.ColorButton;
import com.serhatmerak.wordlist.huds.firstmenutables.CombineListsMenu;
import com.serhatmerak.wordlist.huds.firstmenutables.KnownWordsTable;
import com.serhatmerak.wordlist.huds.firstmenutables.ListsMenuTable;
import com.serhatmerak.wordlist.kumar.ReaderScreen;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class FirstScreen extends CustomScreen {


    private SpriteBatch batch;
    private AppMain appMain;
    private OrthographicCamera camera;
    private Stage stage;

    private Table leftListTable,topListTable,mainTable;
    private Table topChangingTable;

    //List Group
    private Group listsGroup;
    private TextButton btnCreateList;
    private Group newListTextField;

    //Known Words Group
    private Group knownWordsGroup;
    private Group knownWordsTopGroup;

    //Combine Lists Group
    private Group combineListsGroup;

    //
    private ColorButton signedButton;


    private boolean changeScreenToWordlist;

    public FirstScreen(AppMain appMain){
        this.appMain = appMain;
        batch = appMain.batch;

        camera = new OrthographicCamera(AppInfo.WIDTH,AppInfo.HEIGHT);
        camera.setToOrtho(false, AppInfo.WIDTH,AppInfo.HEIGHT);
        Viewport viewport = new StretchViewport(AppInfo.WIDTH, AppInfo.HEIGHT, camera);
        stage = new Stage(viewport, batch);

        Gdx.input.setInputProcessor(stage);
        createActors();
        firstEntry();

    }

    private void firstEntry() {

        topChangingTable.add(newListTextField).width(newListTextField.getWidth()).padLeft(50);
        topChangingTable.add(btnCreateList).padLeft(50);
        mainTable.add(listsGroup);

    }

    private void createActors() {
        createTopList();
        createLeftList();
        createMainTable();
        createMenuTables();
    }

    private void createMenuTables() {
        ListsMenuTable listsMenuTable = new ListsMenuTable(appMain,stage,mainTable);
        listsGroup = listsMenuTable.listsGroup;
        btnCreateList = listsMenuTable.btnCreateList;
        newListTextField = listsMenuTable.newListTextField;

        CombineListsMenu combineListsMenu = new CombineListsMenu(appMain,mainTable);
        combineListsGroup = combineListsMenu.combineListsGroup;

        KnownWordsTable knownWordsTable = new KnownWordsTable(mainTable);
        knownWordsGroup = knownWordsTable.knownWordsGroup;
        knownWordsTopGroup = knownWordsTable.knownWordsTopGroup;
    }

    private void createMainTable() {
        mainTable = new Table();
        mainTable.setSize(AppInfo.WIDTH - leftListTable.getWidth(),
                AppInfo.HEIGHT - topListTable.getHeight());
        mainTable.setPosition(leftListTable.getWidth(),0);
        stage.addActor(mainTable);
    }

    private void createLeftList() {
        BorderImage borderImage = new BorderImage(new Vector3(
                300,AppInfo.HEIGHT - topListTable.getHeight(),0
        ), Color.WHITE,Color.WHITE);

        borderImage.setPosition(0,0);
        stage.addActor(borderImage);

        leftListTable = new Table();
        leftListTable.setPosition(borderImage.getX(),borderImage.getY());
        leftListTable.setSize(borderImage.getWidth(),borderImage.getHeight());
        leftListTable.top();
        stage.addActor(leftListTable);

//        leftListTable.debug();


        /*
        *Actors
         */

        final ColorButton btnLists = new ColorButton("Word Lists");
        btnLists.setSize(borderImage.getWidth(),60);

        btnLists.select();
        signedButton = btnLists;

        final ColorButton btnKnown = new ColorButton("Known Words");
        btnKnown.setSize(borderImage.getWidth(),60);

        final ColorButton btnReader = new ColorButton("Reader Mood");
        btnReader.setSize(borderImage.getWidth(),60);

        Image line1 = new Image(Assets.assets.pix);
        line1.setSize(borderImage.getWidth(),200);
        line1.setColor(Color.valueOf("edeff4"));


        final ColorButton btnHelp = new ColorButton("Help");
        btnHelp.setSize(borderImage.getWidth(),60);

        final ColorButton btnWriteUs = new ColorButton("Write Us");
        btnWriteUs.setSize(borderImage.getWidth(),60);

        Image line2 = new Image(Assets.assets.pix);
        line2.setSize(borderImage.getWidth(),200);
        line2.setColor(Color.valueOf("edeff4"));

        final ColorButton btnExport = new ColorButton("Export Lists");
        btnExport.setSize(borderImage.getWidth(),60);

        final ColorButton btnImport = new ColorButton("Import List");
        btnImport.setSize(borderImage.getWidth(),60);

        final ColorButton btnCombine = new ColorButton("Combine Lists");
        btnCombine.setSize(borderImage.getWidth(),60);

        Image line3 = new Image(Assets.assets.pix);
        line3.setSize(borderImage.getWidth(),200);
        line3.setColor(Color.valueOf("edeff4"));

        final ColorButton btnAboutUs = new ColorButton("About Us");
        btnAboutUs.setSize(borderImage.getWidth(),60);

        /*
         *Filling table
         */
        leftListTable.add(btnLists).padTop(30).row();
        leftListTable.add(btnKnown).row();
        leftListTable.add(btnReader).row();
        leftListTable.add(line1).width(borderImage.getWidth()).height(5).padTop(30).row();
        leftListTable.add(btnExport).padTop(30).row();
        leftListTable.add(btnImport).row();
        leftListTable.add(btnCombine).row();
        leftListTable.add(line2).width(borderImage.getWidth()).height(5).padTop(30).row();
        leftListTable.add(btnHelp).padTop(30).row();
        leftListTable.add(btnWriteUs).row();
        leftListTable.add(line3).width(borderImage.getWidth()).height(5).padTop(30).row();
        leftListTable.add(btnAboutUs).padTop(30).row();

        /*
         *Listeners
         */

        btnLists.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if (signedButton != btnLists){
                    topChangingTable.clear();
                    topChangingTable.add(newListTextField).width(newListTextField.getWidth()).padLeft(50);
                    topChangingTable.add(btnCreateList).padLeft(50);

                    mainTable.clear();
                    mainTable.add(listsGroup);

                    btnLists.select();
                    signedButton.unselect();
                    signedButton = btnLists;
                }
            }
        });

        btnKnown.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if (signedButton != btnKnown){
                    topChangingTable.clear();
                    topChangingTable.add(knownWordsTopGroup).width(knownWordsTopGroup.getWidth()).padLeft(250);


                    mainTable.clear();
                    mainTable.add(knownWordsGroup);

                    btnKnown.select();
                    signedButton.unselect();
                    signedButton = btnKnown;
                }

            }
        });

        btnReader.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                appMain.setScreen(new ReaderScreen(appMain));

            }
        });

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

                            for (int i = 0; i < DataHolder.dataholder.wordLists.size; i++) {

                                FileHandle file = Gdx.files.absolute(chooser.getSelectedFile().getAbsolutePath() + "/" +
                                        DataHolder.dataholder.wordLists.get(i).listName + ".wordlist");
                                file.writeString(DataHolder.dataholder.fileTextsForExporting.get(i), false);

                            }
                            //for fav
                            {

                                FileHandle file = Gdx.files.absolute(chooser.getSelectedFile().getAbsolutePath() + "/" +
                                        DataHolder.dataholder.favList.listName + ".wordlist");
                                file.writeString(DataHolder.dataholder.favListTextForExporting, false);

                            }
                        }

                    }
                }).start();

            }
        });

        btnImport.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                Alert.show(stage,
                        "Select a .wordlist file to import");

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
                            if(chooser.getSelectedFile().getName().endsWith(".wordlist")){
                                String path = chooser.getSelectedFile().getAbsolutePath();
                                FileHandle fileHandle = Gdx.files.absolute(path);
                                String text = fileHandle.readString();
                                FileHandle newFile = Gdx.files.local("lists/" + fileHandle.nameWithoutExtension() +".xml");
                                newFile.writeString(text,false);
                                DataHolder.dataholder.loadExportedList(newFile.name());
                                changeScreenToWordlist = true;
                            }else {
                                //TODO:
                            }
                        }
                    }
                }).start();


            }
        });

        btnCombine.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                if (signedButton != btnCombine){
                    topChangingTable.clear();
//                    topChangingTable.add(knownWordsTopGroup).width(knownWordsTopGroup.getWidth()).padLeft(250);


                    mainTable.clear();
                    mainTable.add(combineListsGroup);

                    btnCombine.select();
                    signedButton.unselect();
                    signedButton = btnCombine;
                }


            }
        });
        btnHelp.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if (signedButton != btnHelp){
                    topChangingTable.clear();

                    mainTable.clear();

                    btnHelp.select();
                    signedButton.unselect();
                    signedButton = btnHelp;
                }
            }
        });

        btnWriteUs.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if (signedButton != btnWriteUs){
                    topChangingTable.clear();

                    mainTable.clear();

                    btnWriteUs.select();
                    signedButton.unselect();
                    signedButton = btnWriteUs;
                }

            }
        });

        btnAboutUs.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if (signedButton != btnAboutUs){
                    topChangingTable.clear();

                    mainTable.clear();

                    btnAboutUs.select();
                    signedButton.unselect();
                    signedButton = btnAboutUs;
                }

            }
        });


    }

    private void createTopList() {
        BorderImage borderImage = new BorderImage(new Vector3(
                AppInfo.WIDTH,80,0
        ), ColorPalette.TopBlue,Color.WHITE);

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

        topChangingTable = new Table();
        topChangingTable.setSize(1000,80);
        topChangingTable.center();

        Label.LabelStyle titleStyle = new Label.LabelStyle();
        titleStyle.font = Fonts.bl_bold_45;

        Label title = new Label("Word App",titleStyle);

        /*
        *Filling table
         */

        topListTable.add(title).width(300 - AppInfo.PADDING).padLeft(AppInfo.PADDING);
        topListTable.add(topChangingTable);




    }


    public void render(float delta) {
        Gdx.gl.glClearColor(ColorPalette.Cream.r,
                ColorPalette.Cream.g,
                ColorPalette.Cream.b
                ,ColorPalette.Cream.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.end();

        if (changeScreenToWordlist)
            appMain.setScreen(new WordListScreen(appMain));

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
        //TODO:
//        appMain.setScreen(new StudyCardScreen(appMain));
    }
}
