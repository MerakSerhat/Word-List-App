package com.serhatmerak.wordlist.huds.wordtesthuds;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Array;
import com.serhatmerak.wordlist.helpers.AppInfo;
import com.serhatmerak.wordlist.helpers.Assets;
import com.serhatmerak.wordlist.helpers.DataHolder;
import com.serhatmerak.wordlist.helpers.Fonts;
import com.serhatmerak.wordlist.huds.Alert;
import com.serhatmerak.wordlist.huds.RoundedRectangle;
import com.serhatmerak.wordlist.screens.WordTestScreen;
import com.serhatmerak.wordlist.word.Word;

public class SettingsWordTest {
    public Group settingsGroup;
    WordTestScreen wordTestScreen;

    private Stage stage;

    public boolean EVERY_WORD = true;
    public boolean DEF_WORD = true;
    public int NUMBER_OF_QUESTION = 20;
    public boolean USE_TURKISH = false;
    public boolean SHOW_ANSWERS = false;

    private Label maxQuestionLabel;
    private TextField textField;
    private int maxQuestionNumber;
    private Group turkishDefSelectionBox;
    private Group showRightSelectionBox;
    private Array<Group> questionSelections;
    private Group definitionWordSelection;
    private Array<Group> wordSelections;
    private Group everyWordsSelection;


    public SettingsWordTest(Stage stage, WordTestScreen wordTestScreen){
        this.stage = stage;
        this.wordTestScreen = wordTestScreen;

        createSettingsGroup();
        setMaxQuestionNumber();
        if (DataHolder.dataholder.getSelectedList().words.size >= 20)
            textField.setText(20+"");
        else
            textField.setText(DataHolder.dataholder.getSelectedList().words.size + "");
    }

    private void createSettingsGroup() {
        Image bg = new Image(Assets.assets.pix);
        bg.setSize(850 , AppInfo.HEIGHT - 300);

        Image titleBg = new Image(Assets.assets.pix);
        titleBg.setSize(bg.getWidth(),100);
        titleBg.setColor(Color.valueOf("4257b2"));

        settingsGroup = new Group();
        settingsGroup.setSize(bg.getWidth(),bg.getHeight());
        settingsGroup.setPosition(AppInfo.WIDTH / 2f - settingsGroup.getWidth() / 2,110);

        Label.LabelStyle titleStyle = new Label.LabelStyle();
        titleStyle.font = Fonts.bl_bold_36;

        Label titleLabel = new Label("Test Settings",titleStyle);

        titleBg.setPosition(0,bg.getHeight() - titleBg.getHeight());
        titleLabel.setPosition(AppInfo.PADDING,
                bg.getHeight() - titleBg.getHeight() + (titleBg.getHeight() - titleLabel.getHeight()) / 2);


        Sprite btnBgNorm = new Sprite(Assets.assets.pix);
        btnBgNorm.setSize(bg.getWidth() - 100,80);
        btnBgNorm.setColor(Color.valueOf("3CCECE"));

        Sprite btnBgHover = new Sprite(Assets.assets.pix);
        btnBgHover.setSize(btnBgNorm.getWidth(),btnBgNorm.getHeight());
        btnBgHover.setColor(Color.valueOf("28A6A6"));

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = Fonts.bl_bold_36;
        textButtonStyle.up = new SpriteDrawable(btnBgNorm);
        textButtonStyle.over = new SpriteDrawable(btnBgHover);
        textButtonStyle.fontColor = Color.WHITE;

        TextButton btnCreateTest = new TextButton("Create new test",textButtonStyle);
        btnCreateTest.setSize(btnBgHover.getWidth(),btnBgHover.getHeight());
        btnCreateTest.setPosition(50,
                titleBg.getY() - 3 *  AppInfo.PADDING - btnCreateTest.getHeight());


        /*

        ***************************************************
            Settings
        ***************************************************

          ---------------------------------------------
                           Create Test
          ---------------------------------------------

        Words                         Amount of question
        o Every Words                 20 / 150
        o Starry Words                --

        Question - Answer Type         *Please enter a valid
        o Definition - Word             number*
        o Word - Definition
                                       ----------------------
        + Use turkish definition       |      Word          |
                                       |  a          c      |
        + Show right answer after      |  b          d      |
          each question                |                    |
                                       ----------------------
         */

        settingsGroup.addActor(bg);
        settingsGroup.addActor(titleBg);
        settingsGroup.addActor(titleLabel);
        settingsGroup.addActor(btnCreateTest);


        createSelectionGroups(settingsGroup,btnCreateTest);

//        stage.addActor(settingsGroup);

    }

    private void createSelectionGroups(Group mainGroup,
                                       TextButton createTest) {

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = Fonts.bl_bold_26;
        labelStyle.fontColor = Color.BLACK;

        Table wordSelectionTable = new Table();
        wordSelectionTable.left().top();
        Label wordSelectionLabel = new Label("Words",labelStyle);
        wordSelections = new Array<>();
        Group starryWordsSelection = createSelection("Starry Words",wordSelections);
        everyWordsSelection = createSelection("Unknown Words",wordSelections);

        wordSelectionTable.add(wordSelectionLabel).left().row();
        wordSelectionTable.add(everyWordsSelection).left().padTop(2 * AppInfo.PADDING).row();
        wordSelectionTable.add(starryWordsSelection).left().padTop(AppInfo.PADDING).row();

        wordSelectionTable.pack();

        ClickListener setMaxQuestionListener = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                EVERY_WORD = ((Label)wordSelections.get(1).getChild(2)).getText().toString().equals("Unknown Words");
                setMaxQuestionNumber();
            }
        };

        everyWordsSelection.addListener(setMaxQuestionListener);
        starryWordsSelection.addListener(setMaxQuestionListener);
/////////////////////////////////////////////////////////////

        Table questionSelectionTable = new Table();
        questionSelectionTable.left().top();
        Label questionSelectionLabel = new Label("Question Type",labelStyle);
        questionSelections = new Array<>();
        Group wordDefinitionSelection = createSelection("Word - Definition",questionSelections);
        definitionWordSelection = createSelection("Definition - Word",questionSelections);


        questionSelectionTable.add(questionSelectionLabel).left().row();
        questionSelectionTable.add(definitionWordSelection).left().padTop(2 * AppInfo.PADDING).row();
        questionSelectionTable.add(wordDefinitionSelection).left().padTop(AppInfo.PADDING).row();

        questionSelectionTable.pack();


        turkishDefSelectionBox = createSelectBox("Use Turkish definitions.");

        showRightSelectionBox = createSelectBox("Show right answer after each question.");


        mainGroup.addActor(wordSelectionTable);
        mainGroup.addActor(questionSelectionTable);
        mainGroup.addActor(turkishDefSelectionBox);
        mainGroup.addActor(showRightSelectionBox);

        showRightSelectionBox.setPosition(450,100);
        turkishDefSelectionBox.setPosition(450,
                showRightSelectionBox.getY() + showRightSelectionBox.getHeight() + 75);
        questionSelectionTable.setPosition(30,
                100);
        wordSelectionTable.setPosition(30,
                questionSelectionTable.getY() + questionSelectionTable.getHeight() + 100);



        Group numberOfQuestionGroup = createNumberOfQuestionGroup();


        numberOfQuestionGroup.setPosition(450,wordSelectionTable.getY());
        mainGroup.addActor(numberOfQuestionGroup);

        if (DataHolder.dataholder.getStarryWordCount() < 5){
            RoundedRectangle image = new RoundedRectangle((int)starryWordsSelection.getWidth() + 12,
                    (int)starryWordsSelection.getHeight() + 12,15);
            image.setColor(Color.GRAY.r,Color.GRAY.g,Color.GRAY.b,0.7f);
            image.setPosition(wordSelectionTable.getX() - 6,
                    wordSelectionTable.getY() - 6);
            image.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    Alert.show(stage,"There must be at least 5 starry words in this list!");
                }
            });
            mainGroup.addActor(image);
        }

        createTest.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                createClicked();

//
            }
        });


    }

    public void createClicked(){
        EVERY_WORD = wordSelections.get(1) == everyWordsSelection;
        DEF_WORD = questionSelections.get(1) == definitionWordSelection;
        USE_TURKISH = turkishDefSelectionBox.getChild(1).isVisible();
        SHOW_ANSWERS = showRightSelectionBox.getChild(1).isVisible();
        NUMBER_OF_QUESTION = Integer.parseInt(textField.getText());

        wordTestScreen.createTestClicked();
    }

    private void setMaxQuestionNumber(){
        if (EVERY_WORD){
            int i = 0;
            for (Word word:DataHolder.dataholder.getSelectedList().words) {
                if (!DataHolder.dataholder.knownWords.contains(word.name,false))
                    i++;
            }
            maxQuestionNumber = i;
            System.out.println(i);
        }else {
            maxQuestionNumber = DataHolder.dataholder.getStarryWordCount();
        }

        maxQuestionLabel.setText(" / " + maxQuestionNumber);

        String text = textField.getText();
        if (text.equals(""))
            return;

        int num = Integer.parseInt(text);

        if (num > maxQuestionNumber)
            textField.setText(maxQuestionNumber +"");
    }

    private Group createNumberOfQuestionGroup() {

        Group numberOfQuestionGroup = new Group();

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.fontColor = Color.BLACK;
        labelStyle.font = Fonts.bl_bold_26;

        Label numberOfQuestionLabel = new Label("Number of questions",labelStyle);
        maxQuestionLabel = new Label(" / " +
                DataHolder.dataholder.getSelectedList().words.size,labelStyle);
        numberOfQuestionLabel.setColor(Color.BLACK);
        maxQuestionLabel.setColor(Color.DARK_GRAY);


        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        Sprite cursorSprite = new Sprite(Assets.assets.pix);
        cursorSprite.setSize(2,30);
        cursorSprite.setColor(Color.DARK_GRAY);
        textFieldStyle.cursor = new SpriteDrawable(cursorSprite);

        textFieldStyle.font = Fonts.bl_bold_26;

        Sprite selectionSprite = new Sprite(Assets.assets.pix);
        selectionSprite.setColor(Color.valueOf("007f7f"));
        textFieldStyle.selection = new SpriteDrawable(selectionSprite);

        textFieldStyle.fontColor = Color.DARK_GRAY;

        textField = new TextField("",textFieldStyle);
        textField.setSize(50,numberOfQuestionLabel.getHeight());
//
        numberOfQuestionLabel.setPosition(0,maxQuestionLabel.getHeight() + AppInfo.PADDING);
        maxQuestionLabel.setPosition(textField.getWidth(),0);
        textField.setPosition(0,0);
        textField.setMaxLength(3);

        TextField.TextFieldFilter textFieldFilter = new TextField.TextFieldFilter() {
            @Override
            public boolean acceptChar(TextField textField, char c) {
                for (char a : new char[]{'1','2','3','4','5','6','7','8','9','0'})
                    if (a == c) return true;
                return false;
            }
        };

        textField.setTextFieldFilter(textFieldFilter);

        final Image bottomLine = new Image(Assets.assets.pix);
        bottomLine.setSize(textField.getWidth() - 10,4);
        bottomLine.setColor(Color.valueOf("ffcd1f"));
        bottomLine.setPosition(0,-10);

        numberOfQuestionGroup.addActor(maxQuestionLabel);
        numberOfQuestionGroup.addActor(numberOfQuestionLabel);
        numberOfQuestionGroup.addActor(textField);
        numberOfQuestionGroup.addActor(bottomLine);

        numberOfQuestionGroup.setSize(maxQuestionLabel.getWidth() + maxQuestionLabel.getX()
                ,numberOfQuestionGroup.getY() + numberOfQuestionGroup.getHeight());

        stage.getRoot().addCaptureListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                if (!(event.getTarget() instanceof TextField))
                    stage.setKeyboardFocus(null);

                String text = textField.getText();
                int num = Integer.parseInt(text);

                if (text.equals("") || num < 5){
                    textField.setText(5+"");
                }

                return false;
            }
        });

        final GlyphLayout layout = new GlyphLayout();
        layout.setText(labelStyle.font,"Brave");

        textField.addListener(new InputListener(){

            @Override
            public boolean keyTyped(InputEvent event, char character) {


                String text = textField.getText();
                if (text.equals("")) {
                    bottomLine.setWidth(20);
                    return super.keyTyped(event, character);

                }

                int num = Integer.parseInt(text);

                if (num > maxQuestionNumber)
                    textField.setText(maxQuestionNumber +"");

                layout.setText(Fonts.bl_bold_26,textField.getText());
                bottomLine.setWidth(Math.max(20,layout.width));
                return super.keyTyped(event, character);
            }
        });

        return numberOfQuestionGroup;
    }



    private Group createSelection(String text,final Array<Group> selections){
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = Fonts.bl_bold_26;

        Label label = new Label(text,labelStyle);

        final Image circle = new Image(Assets.assets.circle);
        circle.setSize(32,32);
        final Image tick = new Image(Assets.assets.tick);
        tick.setSize(16,16);

        tick.setVisible(false);

        label.setColor(Color.BLACK);
        circle.setColor(Color.GRAY);
        tick.setColor(Color.GOLD);

        final Group group = new Group();
        group.setHeight(Math.max(circle.getHeight(),label.getHeight()));

        circle.setPosition(0,label.getHeight() / 2 - circle.getHeight() / 2);
        tick.setPosition(circle.getWidth() / 2 - tick.getWidth() / 2,
                circle.getHeight() / 2 - tick.getHeight() / 2 + circle.getY());
        label.setPosition(circle.getWidth() + 2 * AppInfo.PADDING,
                circle.getHeight() / 2 - label.getHeight() / 2);

        group.addActor(circle);
        group.addActor(tick);
        group.addActor(label);

        group.setWidth(label.getWidth() + label.getX());

        group.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                for (Group selection:selections) {
                    selection.getChild(0).setColor(Color.GRAY);
                    selection.getChild(1).setVisible(false);
                }

                if (!tick.isVisible()){
                    tick.setVisible(true);
                    circle.setColor(Color.GOLD);
                }else {
                    tick.setVisible(false);
                    circle.setColor(Color.GRAY);
                }

                selections.removeValue(group,true);
                selections.add(group);
            }
        });

        selections.add(group);

        if (selections.size == 2){
            tick.setVisible(true);
            circle.setColor(Color.GOLD);
        }
        return group;
    }
    private Group createSelectBox(String text){
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = Fonts.bl_bold_26;

        Label label = new Label(text,labelStyle);

        final Image box = new Image(Assets.assets.box);
        box.setSize(32,32);
        final Image tick = new Image(Assets.assets.tick);
        tick.setSize(16,16);

        tick.setVisible(false);

        label.setColor(Color.BLACK);
        box.setColor(Color.GRAY);
        tick.setColor(Color.GOLD);

        Group group = new Group();
        group.setHeight(Math.max(box.getHeight(),label.getHeight()));

        box.setPosition(0,label.getHeight() / 2 - box.getHeight() / 2);
        tick.setPosition(box.getWidth() / 2 - tick.getWidth() / 2,
                box.getHeight() / 2 - tick.getHeight() / 2 + box.getY());
        label.setPosition(box.getWidth() + 2 * AppInfo.PADDING,
                box.getHeight() / 2 - label.getHeight() / 2);

        label.setWidth(300);
        label.setWrap(true);

        group.addActor(box);
        group.addActor(tick);
        group.addActor(label);

        group.setWidth(label.getWidth() + label.getX());

        group.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);


                if (!tick.isVisible()){
                    tick.setVisible(true);
                    box.setColor(Color.GOLD);
                }else {
                    tick.setVisible(false);
                    box.setColor(Color.GRAY);
                }

            }
        });

        return group;
    }

}
