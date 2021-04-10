
package com.serhatmerak.wordlist.screens;

        import com.badlogic.gdx.Gdx;
        import com.badlogic.gdx.graphics.Color;
        import com.badlogic.gdx.graphics.GL20;
        import com.badlogic.gdx.graphics.OrthographicCamera;
        import com.badlogic.gdx.graphics.g2d.SpriteBatch;
        import com.badlogic.gdx.math.Vector3;
        import com.badlogic.gdx.scenes.scene2d.Group;
        import com.badlogic.gdx.scenes.scene2d.InputEvent;
        import com.badlogic.gdx.scenes.scene2d.Stage;
        import com.badlogic.gdx.scenes.scene2d.ui.Image;
        import com.badlogic.gdx.scenes.scene2d.ui.Table;
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
        import com.serhatmerak.wordlist.huds.BorderImage;
        import com.serhatmerak.wordlist.huds.wordcardhuds.StudyWordCard;
        import com.serhatmerak.wordlist.huds.wordcardhuds.SettingsWordCards;
        import com.serhatmerak.wordlist.huds.wordcardhuds.WordCardsHud;

public class StudyCardScreen extends CustomScreen {

    private OrthographicCamera camera;
    private Stage stage;
    private AppMain appMain;
    private SpriteBatch batch;


    Color bg = ColorPalette.LightTopBlue;

    public boolean EVERY_WORD = true;
    public boolean SHOW_TURKISH = false;
    public boolean SHOW_EXAMPLES = false;

    private SettingsWordCards settingsWordCards;
    private Table topChangingTable;
    private WordCardsHud wordCardsHud;
    private Image back;


    public StudyCardScreen(AppMain appMain) {
        this.appMain = appMain;
        batch = appMain.batch;

        camera = new OrthographicCamera(AppInfo.WIDTH, AppInfo.HEIGHT);
        camera.setToOrtho(false, AppInfo.WIDTH, AppInfo.HEIGHT);
        Viewport viewport = new StretchViewport(AppInfo.WIDTH, AppInfo.HEIGHT, camera);
        stage = new Stage(viewport, batch);

        Gdx.input.setInputProcessor(stage);

        createActors();

    }

    private void createActors() {
        createTopList();
//        settingsWordTest = new SettingsWordTest(stage,this);
//        stage.addActor(settingsWordTest.settingsGroup);
        settingsWordCards = new SettingsWordCards(stage,this);
        stage.addActor(settingsWordCards.settingsGroup);
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

        stage.addActor(topListGroup);
        /*
         *Filling table
         */



        topChangingTable = new Table();
        topChangingTable.setSize(1500, topListTable.getHeight());
        topChangingTable.center();

        topListTable.add(topChangingTable);

        back = new Image(Assets.assets.back);
        back.setSize(40,40);
        back.addListener(new ButtonHoverAnimation(back,true){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                appMain.setScreen(new WordListScreen(appMain));
            }
        });
        back.setOrigin(Align.center);

        topChangingTable.add(back).width(back.getWidth()).height(back.getHeight()).pad(AppInfo.PADDING);

    }


    public void render(float delta) {
        Gdx.gl.glClearColor(bg.r,bg.g,bg.b,bg.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.end();

        if (wordCardsHud != null)
            wordCardsHud.act(delta);

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


    public void createCardsClicked(){

        EVERY_WORD = settingsWordCards.EVERY_WORD;
        SHOW_TURKISH = settingsWordCards.SHOW_TURKISH;
        SHOW_EXAMPLES = settingsWordCards.SHOW_EXAMPLES;

//        testWordTest = new TestWordTest();
//        testWordTest.setTestType(EVERY_WORD,DEF_WORD,NUMBER_OF_QUESTION,USE_TURKISH,SHOW_ANSWERS);

//        testWordTest.createTest();
        settingsWordCards.settingsGroup.remove();
        StudyWordCard.StudyWordCardStyle studyWordCardStyle = new StudyWordCard.StudyWordCardStyle();
        studyWordCardStyle.EVERY_WORD = EVERY_WORD;
        studyWordCardStyle.SHOW_TURKISH = SHOW_TURKISH;
        studyWordCardStyle.SHOW_EXAMPLES = SHOW_EXAMPLES;
        wordCardsHud = new WordCardsHud(studyWordCardStyle,this);
        stage.addActor(wordCardsHud.wordCardGroup);
        topChangingTable.add(wordCardsHud.topMenuGroup).padLeft(200);

        topChangingTable.getParent().getParent().toFront();

        stage.addActor(wordCardsHud.lblShowCardIndex);
        wordCardsHud.lblShowCardIndex.toFront();

//        stage.addActor(testWordTest.testPageGroup);

//        btnFinishQuiz.setVisible(true);

    }

    public void restartClicked() {
        topChangingTable.clear();
        topChangingTable.add(back).width(back.getWidth()).height(back.getHeight()).pad(AppInfo.PADDING);
        wordCardsHud.wordCardGroup.remove();
        wordCardsHud.lblShowCardIndex.remove();

        settingsWordCards = new SettingsWordCards(stage,this);
        stage.addActor(settingsWordCards.settingsGroup);
    }
}


