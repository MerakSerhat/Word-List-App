package com.serhatmerak.wordlist.huds.firstmenutables;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.serhatmerak.wordlist.AppMain;
import com.serhatmerak.wordlist.helpers.AppInfo;
import com.serhatmerak.wordlist.helpers.Assets;
import com.serhatmerak.wordlist.helpers.DataHolder;
import com.serhatmerak.wordlist.helpers.Fonts;
import com.serhatmerak.wordlist.huds.CustomTextField;
import com.serhatmerak.wordlist.screens.WordListScreen;
import com.serhatmerak.wordlist.word.Word;
import com.serhatmerak.wordlist.word.WordList;

public class CombineListsMenu {
    public Group combineListsGroup;

    private AppMain appMain;
    private Table mainTable;

    public CombineListsMenu(AppMain appMain,Table mainTable){
        this.appMain = appMain;
        this.mainTable = mainTable;

        createCombineListsGroup();
    }

    private void createCombineListsGroup() {
        combineListsGroup = new Group();
        combineListsGroup.setSize(mainTable.getWidth(),mainTable.getHeight());

        Table table = new Table();
        table.setSize(combineListsGroup.getWidth(),
                combineListsGroup.getHeight());
        table.top().left();

        combineListsGroup.addActor(table);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = Fonts.bl_bold_32;

        Label.LabelStyle smallLabelStyle = new Label.LabelStyle();
        smallLabelStyle.font =Fonts.bl_bold_20;


        Label selectListsToCombine = new Label("Select Lists To Combine",labelStyle);
        selectListsToCombine.setColor(Color.BLACK);

        final Label selectedListsWillBeCombined = new Label("You must select minimum two list.",labelStyle);
        selectedListsWillBeCombined.setColor(Color.BLACK);

        // Combining Part

        final Array<Integer> selectedListsIndexes = new Array<>();


        final CustomTextField customTextField = new CustomTextField(400,50);

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

        final TextButton btnCombine = new TextButton("Combine Lists",textButtonStyle);
        btnCombine.setSize(200,50);
        btnCombine.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                combineListsClicked(selectedListsIndexes,customTextField);
            }
        });

        ((TextField) customTextField.getChild(1)).setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char key) {
                if ((key == '\r' || key == '\n')){
                    if (btnCombine.isTouchable())
                        combineListsClicked(selectedListsIndexes,customTextField);
                }
            }
        });

        final Group textFieldButtonGruop = new Group();
        textFieldButtonGruop.setWidth(customTextField.getWidth() + 120 + btnCombine.getWidth());
        textFieldButtonGruop.setHeight(Math.max(customTextField.getHeight(),btnCombine.getHeight()));

        customTextField.setPosition(0,btnCombine.getHeight() / 2 - textFieldButtonGruop.getHeight() / 2);
        btnCombine.setPosition(customTextField.getWidth() + 120,customTextField.getHeight() / 2 - btnCombine.getHeight() / 2);

        textFieldButtonGruop.addActor(customTextField);
        textFieldButtonGruop.addActor(btnCombine);

        btnCombine.setTouchable(Touchable.disabled);
        btnCombine.setColor(1,1,1,0.5f);


        //Table part

        final int pageCount = (DataHolder.dataholder.wordLists.size +
                (9 - DataHolder.dataholder.wordLists.size%9)) / 9;


        final Table yourListsToSelectTable = new Table();
        yourListsToSelectTable.setSize(table.getWidth() - 120, 500);
        yourListsToSelectTable.top().left();

        final Array<Table> listTablesArray = new Array<>();

        for (int j = 0; j < pageCount; j++ ) {

            Table lists = new Table();
            lists.setSize(table.getWidth() - 120, 500);
            lists.top().left();
            lists.defaults().pad(AppInfo.PADDING);


            int listCount = Math.min(9,DataHolder.dataholder.wordLists.size - (j * 9));
            for (int i = 0; i < listCount; i++) {
                float w = (lists.getWidth() - 4 * AppInfo.PADDING) / 3;
                float h = (lists.getHeight() - 4 * AppInfo.PADDING) / 3;

                if (i != 0 && i % 3 == 0)
                    lists.row();

                final Group group = new Group();
                group.setSize(w,h);

                Image bg = new Image(Assets.assets.pix);
                bg.setSize(w, h);

                final Image bottomLine = new Image(Assets.assets.pix);
                bottomLine.setSize(w, 10);
                bottomLine.setColor(Color.valueOf("ffcd1f"));
                bottomLine.setVisible(false);

                final Image selectedBackground = new Image(Assets.assets.pix);
                selectedBackground.setSize(w, h);
                selectedBackground.setColor(Color.valueOf("ffde66"));
                selectedBackground.setVisible(false);

                Label listName = new Label(DataHolder.dataholder.wordLists.get(j * 9 + i).listName,labelStyle);
                listName.setColor(Color.BLACK);
                listName.setPosition(AppInfo.PADDING,h - AppInfo.PADDING - listName.getPrefHeight());

                Label wordCount = new Label(
                        DataHolder.dataholder.wordLists.get(j * 9 + i).words.size + " word",smallLabelStyle);
                wordCount.setPosition(listName.getX(),listName.getY() - wordCount.getPrefHeight());
                wordCount.setColor(Color.GRAY);

                group.addActor(bg);
                group.addActor(selectedBackground);
                group.addActor(listName);
                group.addActor(wordCount);
                group.addActor(bottomLine);
                group.setName("" + (j * 9 + i));

                group.addListener(new ClickListener(){
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        super.clicked(event, x, y);
                        //TODO:
                        selectedBackground.setVisible(!selectedBackground.isVisible());
                        if (selectedListsIndexes.indexOf(Integer.parseInt(group.getName()),false) >= 0){
                            selectedListsIndexes.removeValue(Integer.parseInt(group.getName()),false);
                        }else {
                            selectedListsIndexes.add(Integer.parseInt(group.getName()));
                        }

                        if (selectedListsIndexes.size >= 2){
                            selectedListsWillBeCombined.setText(selectedListsIndexes.size + " lists will be combined.");
                            btnCombine.setTouchable(Touchable.enabled);
                            btnCombine.setColor(1,1,1,1);
                        }else {
                            selectedListsWillBeCombined.setText("You must select minimum two list.");
                            btnCombine.setTouchable(Touchable.disabled);
                            btnCombine.setColor(1,1,1,0.5f);
                        }
                    }

                    @Override
                    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                        super.enter(event, x, y, pointer, fromActor);
                        if (!bottomLine.isVisible())
                            bottomLine.setVisible(true);
                    }

                    @Override
                    public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                        super.exit(event, x, y, pointer, toActor);
                        if (bottomLine.isVisible() && pointer == -1)
                            bottomLine.setVisible(false);
                    }
                });

                lists.add(group).width(w).height(h);

            }

            listTablesArray.add(lists);

        }

        final int[] pageIndex = {0};

        //change page codes;
        Group changePageGroup = new Group();
        {
            TextButton.TextButtonStyle changePageStyle = new TextButton.TextButtonStyle();
            changePageStyle.font = Fonts.bl_bold_32;
            changePageStyle.fontColor = Color.valueOf("303545");
            changePageStyle.disabledFontColor = Color.LIGHT_GRAY;
            changePageStyle.overFontColor = Color.valueOf("ffcd1f");

            final TextButton nextPage = new TextButton(">", changePageStyle);
            nextPage.setOrigin(Align.center);
            nextPage.setScale(2.5f);

            final TextButton previousPage = new TextButton("<", changePageStyle);
            previousPage.setOrigin(Align.center);
            previousPage.setScale(2.5f);

            final Label changePageLabel = new Label("1/" + pageCount, labelStyle);
            changePageLabel.setColor(Color.valueOf("303545"));

            previousPage.setPosition(changePageLabel.getWidth() + AppInfo.PADDING * 2, 0);
            nextPage.setPosition(changePageLabel.getWidth() +
                    AppInfo.PADDING * 5 + previousPage.getPrefWidth(), 0);

            changePageGroup.addActor(changePageLabel);
            changePageGroup.addActor(nextPage);
            changePageGroup.addActor(previousPage);

            changePageGroup.setSize(nextPage.getX() + nextPage.getWidth(),
                    Math.max(nextPage.getHeight(), changePageLabel.getHeight()));

            previousPage.setTouchable(Touchable.disabled);
            previousPage.setDisabled(true);

            nextPage.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    pageIndex[0]++;
                    changePageLabel.setText( (pageIndex[0] + 1) + "/" + pageCount);

                    if (previousPage.isDisabled()) {
                        previousPage.setTouchable(Touchable.enabled);
                        previousPage.setDisabled(false);
                    }

                    if (pageIndex[0] + 1 == pageCount) {
                        nextPage.setTouchable(Touchable.disabled);
                        nextPage.setDisabled(true);
                    }

                    yourListsToSelectTable.clear();
                    yourListsToSelectTable.add(listTablesArray.get(pageIndex[0])).top().left()
                            .width(yourListsToSelectTable.getWidth()).height(yourListsToSelectTable.getHeight());
                }
            });
            previousPage.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    pageIndex[0]--;
                    changePageLabel.setText( (pageIndex[0] + 1) + "/" + pageCount);

                    if (nextPage.isDisabled()) {
                        nextPage.setTouchable(Touchable.enabled);
                        nextPage.setDisabled(false);
                    }

                    if (pageIndex[0] == 0) {
                        previousPage.setTouchable(Touchable.disabled);
                        previousPage.setDisabled(true);
                    }

                    yourListsToSelectTable.clear();
                    yourListsToSelectTable.add(listTablesArray.get(pageIndex[0])).top().left()
                            .width(yourListsToSelectTable.getWidth()).height(yourListsToSelectTable.getHeight());
                }
            });

            if (pageCount == 1) changePageGroup.setVisible(false);
        }

        if (listTablesArray.size > 0) {
            yourListsToSelectTable.clear();
            yourListsToSelectTable.add(listTablesArray.first()).
                    width(yourListsToSelectTable.getWidth()).height(yourListsToSelectTable.getHeight());
        }

        //Adding to table

        table.add(selectListsToCombine).padLeft(30 + AppInfo.PADDING).padTop(60).width(300).left();
        table.add(changePageGroup).padTop(60).padRight(AppInfo.PADDING).right().row();
        table.add(yourListsToSelectTable).padLeft(30).width(yourListsToSelectTable.getWidth()).height(yourListsToSelectTable.getHeight()).colspan(2).row();
        table.add(selectedListsWillBeCombined).padTop(120).padLeft(30 + AppInfo.PADDING).left().row();
        table.add(textFieldButtonGruop).padTop(60).padLeft(30 + AppInfo.PADDING).left().row();


    }

    private void combineListsClicked(Array<Integer> selectedListsIndexes,CustomTextField customTextField) {
        String name = ((TextField) customTextField.getChild(1)).getText();
        if (name.equals("")) {
            customTextField.getChild(3).addAction(Actions.sequence
                    (Actions.color(Color.RED,0.3f), Actions.color(Color.WHITE,0.3f)));
            return;
        }

        for (WordList wordList:DataHolder.dataholder.wordLists) {
            if (wordList.listName.equals(name)){
                customTextField.getChild(3).addAction(Actions.sequence
                        (Actions.color(Color.RED,0.3f), Actions.color(Color.WHITE,0.3f)));
                return;
            }
        }

        WordList wordList = new WordList();
        wordList.listName = name;
        //Filling the list
        for (int listIndex:selectedListsIndexes) {
            for (Word word : DataHolder.dataholder.wordLists.get(listIndex).words) {
                if (!wordList.words.contains(word,false)){
                    wordList.words.add(word);
                }
            }
        }

        DataHolder.dataholder.wordLists.add(wordList);
        DataHolder.dataholder.SELECTED_LIST_INDEX = DataHolder.dataholder.wordLists.indexOf(wordList,true);
        DataHolder.dataholder.saveList(wordList);
        appMain.setScreen(new WordListScreen(appMain));
    }


}
