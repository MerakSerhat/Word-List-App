package com.serhatmerak.wordlist.huds.addwordhuds;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.serhatmerak.wordlist.helpers.AppInfo;
import com.serhatmerak.wordlist.helpers.Assets;
import com.serhatmerak.wordlist.helpers.ColorPalette;
import com.serhatmerak.wordlist.helpers.DataHolder;
import com.serhatmerak.wordlist.helpers.Fonts;
import com.serhatmerak.wordlist.huds.BorderImage;
import com.serhatmerak.wordlist.huds.RoundedRectangle;
import com.serhatmerak.wordlist.huds.ShowWindow;
import com.serhatmerak.wordlist.huds.WordListTable;
import com.serhatmerak.wordlist.word.GeneratorThread;
import com.serhatmerak.wordlist.word.MeaningDiv;
import com.serhatmerak.wordlist.word.Word;
import com.serhatmerak.wordlist.word.WordList;

public class AddWordWindow extends Group {

    private final WordListTable wordListTable;
    private final WordList list;

        private final float width = 900;
        private final float height = 600;

        private int selectedButtonIndex = 0;
        private Group wordGroup;
        private AddWordsFromFileHud fileGroup;
        private AddWordWithTextHud textGroup;
        private TextField.TextFieldStyle textFieldStyle;
        private Array<Group> wordFieldArray;

    private GeneratorThread generatorThread;

    private ObjectMap<TextField,Word> wordMap;

    private boolean addWordWithNameClicked = false;
    public Label errorLbl;
    private Array<Image> selectTypeBgs;


    public AddWordWindow(WordList list, WordListTable wordListTable){
        this.list = list;
        this.wordListTable = wordListTable;

        generatorThread = new GeneratorThread();
        generatorThread.initalize(this);

        wordMap = new ObjectMap<>();

       createActors();
    }

    private void createActors() {
        Image wholeBg = new Image(Assets.assets.pix);
        wholeBg.setSize(width,height);
        addActor(wholeBg);
        setSize(wholeBg.getWidth(),wholeBg.getHeight());

        Image topBg = new Image(Assets.assets.pix);
        topBg.setSize(width,80);
        topBg.setPosition(0,height - topBg.getHeight());
        topBg.setColor(ColorPalette.TopBlue);
        addActor(topBg);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = Fonts.bl_bold_36;
        Label listName = new Label(list.listName,labelStyle);
        listName.setPosition(width / 2 - listName.getPrefWidth() / 2,
                topBg.getY() + topBg.getHeight() / 2 - listName.getHeight() / 2);
        addActor(listName);

        TextButton.TextButtonStyle style1 = new TextButton.TextButtonStyle();
        style1.font = Fonts.bl_bold_32;
        style1.fontColor = Color.BLACK;

        final TextButton btnWithWord = new TextButton("Word", style1);
        final TextButton btnWithText = new TextButton("Text",new TextButton.TextButtonStyle(style1));
        final TextButton btnWithFile = new TextButton("File",new TextButton.TextButtonStyle(style1));

        btnWithFile.setTouchable(Touchable.disabled);
        btnWithWord.setTouchable(Touchable.disabled);
        btnWithText.setTouchable(Touchable.disabled);

        Image wordBg = new Image(Assets.assets.pix);
        wordBg.setSize(getWidth() / 3,btnWithWord.getHeight() + 2 * AppInfo.PADDING);
        Image textBg = new Image(Assets.assets.pix);
        textBg.setSize(getWidth() / 3,btnWithWord.getHeight() + 2 * AppInfo.PADDING);
        Image fileBg = new Image(Assets.assets.pix);
        fileBg.setSize(getWidth() / 3,btnWithFile.getHeight() + 2 * AppInfo.PADDING);

        wordBg.setColor(ColorPalette.Orange);
        textBg.setColor(ColorPalette.LightOrange);
        fileBg.setColor(ColorPalette.LightOrange);

        wordBg.setPosition(0,topBg.getY() - AppInfo.PADDING - btnWithFile.getPrefHeight());
        textBg.setPosition(getWidth() / 3,wordBg.getY());
        fileBg.setPosition((getWidth() / 3 )* 2,wordBg.getY());

        btnWithWord.setPosition(wordBg.getWidth() / 2 - btnWithWord.getWidth() / 2,
                wordBg.getY() + AppInfo.PADDING);
        btnWithText.setPosition(textBg.getX() + textBg.getWidth() / 2 - btnWithText.getWidth() / 2,
                btnWithWord.getY());
        btnWithFile.setPosition(fileBg.getX() + fileBg.getWidth() / 2 - btnWithFile.getWidth() / 2,
                btnWithWord.getY());


        addActor(fileBg);
        addActor(wordBg);
        addActor(textBg);
        addActor(btnWithFile);
        addActor(btnWithText);
        addActor(btnWithWord);



        final Array<TextButton> selectTypeButtons = new Array<>();
        selectTypeButtons.add(btnWithWord,btnWithFile,btnWithText);

        selectTypeBgs = new Array<>();
        selectTypeBgs.add(wordBg,fileBg,textBg);


        wordBg.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                int id = selectTypeButtons.indexOf(btnWithWord,true);
                if (id != selectedButtonIndex){
                    selectedButtonIndex = id;
                    changeSelectedButton();
                }

                wordGroup.setVisible(true);
                fileGroup.setVisible(false);
                textGroup.setVisible(false);

            }
        });

        fileBg.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                int id = selectTypeButtons.indexOf( btnWithFile,true);
                if (id != selectedButtonIndex){
                    selectedButtonIndex = id;
                    changeSelectedButton();
                }

                wordGroup.setVisible(false);
                textGroup.setVisible(false);
                fileGroup.setVisible(true);
            }
        });

        textBg.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                int id = selectTypeButtons.indexOf( btnWithText,true);
                if (id != selectedButtonIndex){
                    selectedButtonIndex = id;
                    changeSelectedButton();
                }

                wordGroup.setVisible(false);
                fileGroup.setVisible(false);
                textGroup.setVisible(true);
            }
        });

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = Fonts.bl_bold_72;
        textButtonStyle.fontColor = Color.WHITE;
        textButtonStyle.downFontColor = Color.DARK_GRAY;
        textButtonStyle.overFontColor = Color.LIGHT_GRAY;

        TextButton btnCloseWindow = new TextButton("x", textButtonStyle);
        btnCloseWindow.setPosition(width - AppInfo.PADDING - btnCloseWindow.getPrefWidth(),
                height + AppInfo.PADDING - btnCloseWindow.getPrefHeight());

        btnCloseWindow.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                AddWordWindow.this.getParent().remove();
            }
        });

        addActor(btnCloseWindow);



        changeSelectedButton();
        createWordGroup();
        createFileGroup();
        createTextGroup();


    }

    private void createWordGroup() {
        wordGroup = new Group();
        BorderImage borderImage = new BorderImage(new Vector3(width - AppInfo.PADDING * 2,
                selectTypeBgs.first().getY() - AppInfo.PADDING * 2,2),Color.WHITE,Color.WHITE);
        wordGroup.addActor(borderImage);
        wordGroup.setSize(borderImage.getWidth(),borderImage.getHeight());
        wordGroup.setPosition(AppInfo.PADDING,AppInfo.PADDING);
        addActor(wordGroup);

        Texture pix = new Texture("pix.png");
        textFieldStyle = new TextField.TextFieldStyle();
        Sprite cursorSprite = new Sprite(pix);
        cursorSprite.setSize(2,100);
        cursorSprite.setColor(Color.BLACK);
        textFieldStyle.cursor = new SpriteDrawable(cursorSprite);
        textFieldStyle.font = Fonts.bl_bold_26;
        Sprite selectionSprite = new Sprite(pix);
        selectionSprite.setColor(Color.valueOf("007f7f"));
        textFieldStyle.selection = new SpriteDrawable(selectionSprite);
        Sprite bgSprite = new Sprite(Assets.assets.pix);
        bgSprite.setColor(ColorPalette.Cream);
        textFieldStyle.background = new SpriteDrawable(bgSprite);
        textFieldStyle.fontColor = Color.BLACK;


        ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();

        wordFieldArray = new Array<>();

        Sprite scroll = new Sprite(pix);
        Sprite knob = new Sprite(pix);
        scrollPaneStyle.vScroll = new SpriteDrawable(scroll);
        scrollPaneStyle.vScrollKnob = new SpriteDrawable(knob);
        ScrollPane scrollPane = new ScrollPane(null, scrollPaneStyle);
        scrollPane.setVariableSizeKnobs(true);
        scrollPane.setOverscroll(false, false);
        scrollPane.setSize(550,wordGroup.getHeight() - 2*AppInfo.PADDING);
        scrollPane.setPosition(0,AppInfo.PADDING);
//
        wordGroup.addActor(scrollPane);
//        Group group = new Group();
//
        Table table = new Table();
        table.setWidth(scrollPane.getWidth());
        table.left().top();
        table.defaults().pad(5).padLeft(0);
        scrollPane.setActor(table);
        addTextFieldToTable(table);

        RoundedRectangle roundedRectangle = new RoundedRectangle(300,100,15);

        Sprite hoverSprite = new Sprite(roundedRectangle.lastTexture);
        hoverSprite.setColor(ColorPalette.ButtonOverBlue);

        Sprite normSprite = new Sprite(roundedRectangle.lastTexture);
        normSprite.setColor(ColorPalette.ButtonBlue);


        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = Fonts.bl_bold_36;
        textButtonStyle.up = new SpriteDrawable(normSprite);
        textButtonStyle.over = new SpriteDrawable(hoverSprite);
        textButtonStyle.fontColor = ColorPalette.TopBlue;


        TextButton btnAdd = new TextButton("Add Words",textButtonStyle);
        btnAdd.setPosition(getWidth() / 2 + getWidth() / 4 - btnAdd.getWidth() / 2  ,
                wordGroup.getHeight() / 2 - btnAdd.getPrefHeight() / 2);
        wordGroup.addActor(btnAdd);

        btnAdd.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                for (Group g:wordFieldArray) {
                    TextField textField = (TextField) ((Group) g.getChild(1)).getChild(1);
                    if (!textField.getName().equals("sent") && !textField.getName().equals("done")){
                        generatorThread.addWordWithNameOrLink(textField);
                        textField.setName("sent");
                    }

                }
                addWordWithNameClicked = true;
                ShowWindow showWindow = new ShowWindow(new BorderImage(new Vector3(0,0,0)));
                showWindow.img.setHeight(getStage().getViewport().getWorldHeight());
                showWindow.setPosition(-getX(), -getY());
                addActor(showWindow);

            }
        });

        Label.LabelStyle errorStyle = new Label.LabelStyle();
        errorStyle.font = Fonts.bl_bold_26;


        errorLbl = new Label("[RED]!Connection Problem[]",errorStyle);
        errorLbl.setPosition(btnAdd.getX() + btnAdd.getWidth() / 2 - errorLbl.getPrefWidth() / 2,btnAdd.getY() - 200);
        errorLbl.setVisible(false);

        wordGroup.addActor(errorLbl);

    }
//
    private void addTextFieldToTable(final Table table){

        final Group group = new Group();

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = Fonts.bl_bold_45;
        labelStyle.fontColor = Color.BLACK;

        Label label = new Label(">",labelStyle);

        final TextField textField = new TextField("",textFieldStyle);
        textField.setWidth(300);
        textField.setAlignment(Align.center);
        textField.setName("");

        Image bottomLane = new Image(Assets.assets.pix);
        bottomLane.setSize(textField.getWidth(),3);
        bottomLane.setColor(ColorPalette.Orange);



        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = Fonts.bl_bold_72;
        textButtonStyle.fontColor = Color.BLACK;
        textButtonStyle.downFontColor = Color.GRAY;
        textButtonStyle.overFontColor = Color.DARK_GRAY;

        TextButton removeWordBtn = new TextButton("-",textButtonStyle);


        Group innerGroup = new Group();
        innerGroup.setHeight(textField.getPrefHeight());

        label.setPosition(0,innerGroup.getHeight() / 2 - label.getPrefHeight() / 2);
        textField.setPosition(label.getPrefWidth() + AppInfo.PADDING,
                innerGroup.getHeight() / 2 - textField.getPrefHeight() / 2);
        bottomLane.setPosition(textField.getX(),-5);
        removeWordBtn.setPosition(textField.getX() + textField.getWidth() + 25,
                innerGroup.getHeight() / 2 - removeWordBtn.getHeight() / 2 + 5);

        innerGroup.setWidth(removeWordBtn.getX() + removeWordBtn.getWidth());

        group.setSize(getWidth() / 2 - 2 * AppInfo.PADDING,innerGroup.getHeight() + 20);


        innerGroup.addActor(label);
        innerGroup.addActor(textField);
        innerGroup.addActor(bottomLane);
        innerGroup.addActor(removeWordBtn);


        innerGroup.setPosition(group.getWidth() / 2 - innerGroup.getWidth() / 2, 10);




        Image bg = new Image(Assets.assets.pix);
        bg.setSize(group.getWidth(),group.getHeight());
        bg.setColor(ColorPalette.Cream);

        group.addActor(bg);
        group.addActor(innerGroup);



        textField.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(final TextField textField, char key) {
                if ((key == '\r' || key == '\n') && !textField.getText().equals("")){
                    addTextFieldToTable(table);
                    getStage().setKeyboardFocus(((Group)((Group)table.getChildren().get(
                            table.getChildren().size - 1)).getChild(1)).getChild(1));
                    generatorThread.addWordWithNameOrLink(textField);
                    textField.setName("sent");
                }
            }
        });

        removeWordBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                table.removeActor(group);
                wordFieldArray.removeValue(group,true);
                wordMap.remove((TextField) ((Group) group.getChild(1)).getChild(1));

                if (wordFieldArray.size == 0)
                    addTextFieldToTable(table);


                    table.clear();
                for (Group f:wordFieldArray) {
//                    table.add(f).width(400).height(f.getHeight()).row();
                    table.add(f).center().row();

                }

            }
        });

        wordFieldArray.add(group);
//        table.add(group).width(group.getWidth()).height(group.getHeight()).row();
        table.add(group).center().row();
        table.pack();
        ((ScrollPane)table.getParent()).scrollTo(0,0,0,0);
    }



    private void createFileGroup() {
        fileGroup = new AddWordsFromFileHud(new Vector2(width ,
                selectTypeBgs.first().getY()),list,this);
        fileGroup.setPosition(0,0);
        addActor(fileGroup);
        fileGroup.setVisible(false);

    }

    private void createTextGroup() {
        textGroup = new AddWordWithTextHud(getWidth(),
                selectTypeBgs.first().getY(),this);
        addActor(textGroup);
        textGroup.setVisible(false);

    }

    private void changeSelectedButton() {
        for (Image img:selectTypeBgs){
            img.setColor(ColorPalette.LightOrange);
        }
        selectTypeBgs.get(selectedButtonIndex).setColor(ColorPalette.Orange);
    }

    public void getWordFromThreadWithName(TextField textField,Word word){
        textField.setName("done");
        if (word == null)
            textField.getParent().getChild(2).setColor(Color.RED);
        else {
            textField.getParent().getChild(2).setColor(Color.GREEN);
            wordMap.put(textField,createNewWord(word));

        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if(addWordWithNameClicked){
            for (Group g:wordFieldArray) {
                TextField textField = (TextField) ((Group) g.getChild(1)).getChild(1);
                if (!textField.getName().equals("done") && !textField.getText().equals(""))
                    return;

            }
            for (Word w:wordMap.values().toArray()) {
                if (w != null) {
                    list.words.add(w);
                    wordListTable.addWord(w);
                }
            }
            getParent().remove();
            DataHolder.dataholder.saveList(list);
        }
    }

    public void finishLoadingFromFile(Array<Word> wordArray){
        Array<String> wordNames = new Array<>();
        for (Word word:DataHolder.dataholder.getSelectedList().words){
            wordNames.add(word.name);
        }
        for (Word w:wordArray) {
            if (w != null && !wordNames.contains(w.name,false)) {
                list.words.add(w);
                wordListTable.addWord(w);
            }
        }

        getParent().remove();
        DataHolder.dataholder.saveList(list);
    }

    private Word createNewWord(Word word){
        Word word2 = new Word(word.name);
        word2.meaningDivs = new Array<>();
        for (MeaningDiv div:word.meaningDivs) {
            MeaningDiv meaningDiv = new MeaningDiv();
            meaningDiv.trDef = div.trDef;
            meaningDiv.engDef = div.engDef;
            meaningDiv.title = div.title;
            meaningDiv.isCollacation = div.isCollacation;
            meaningDiv.difficulty = div.difficulty;
            meaningDiv.examples = new String[div.examples.length];
            int i = 0;
            for (String s:div.examples) {
                meaningDiv.examples[i] = s;
                i++;
            }
            word2.meaningDivs.add(meaningDiv);
        }
        return word2;
    }

}
