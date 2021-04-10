package com.serhatmerak.wordlist.huds.firstmenutables;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
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
import com.serhatmerak.wordlist.helpers.ButtonHoverAnimation;
import com.serhatmerak.wordlist.helpers.DataHolder;
import com.serhatmerak.wordlist.helpers.Fonts;
import com.serhatmerak.wordlist.huds.CustomTextField;
import com.serhatmerak.wordlist.screens.WordListScreen;
import com.serhatmerak.wordlist.word.WordList;

public class ListsMenuTable {
    public Group listsGroup;
    public TextButton btnCreateList;
    public CustomTextField newListTextField;

    private AppMain appMain;
    private Stage stage;
    private Table mainTable;

    private Table yourListsTable;


    public ListsMenuTable(AppMain appMain, Stage stage,Table mainTable){
        this.appMain = appMain;
        this.stage = stage;
        this.mainTable = mainTable;
        createListsGroup();
        createTopChangingTable();
    }

    private void createTopChangingTable() {

    }

    private void createListsGroup() {
        listsGroup = new Group();
        listsGroup.setSize(mainTable.getWidth(),mainTable.getHeight());

        Table table = new Table();
        table.setSize(listsGroup.getWidth(),listsGroup.getHeight());
        table.top().left();

        listsGroup.addActor(table);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = Fonts.bl_bold_32;

        Label.LabelStyle smallLabelStyle = new Label.LabelStyle();
        smallLabelStyle.font =Fonts.bl_bold_20;


        Label yourLists = new Label("Your Lists",labelStyle);
        yourLists.setColor(Color.BLACK);
        Label customLists = new Label("Custom Lists",labelStyle);
        customLists.setColor(Color.BLACK);

        yourListsTable = new Table();
        yourListsTable.setSize(table.getWidth() - 120, 500);
        yourListsTable.top().left();
        yourListsTable.defaults().pad(AppInfo.PADDING);

        float w = (yourListsTable.getWidth() - 4 * AppInfo.PADDING) / 3;
        float h = (yourListsTable.getHeight() - 4 * AppInfo.PADDING) / 3;

        final Array<ListNameCard> listNameCards = new Array<>();

        int listCount = Math.min(9,DataHolder.dataholder.wordLists.size);
        for (int i = 0; i < listCount; i++) {

            final WordList wordList = DataHolder.dataholder.wordLists.get(i);

             if (i != 0 && i % 3 == 0)
                 yourListsTable.row();

             ListNameCard group = new ListNameCard(new Vector2(w,h));
             group.setWordList(wordList, i,true);

             yourListsTable.add(group).width(w).height(h);

             listNameCards.add(group);
        }

        // order lists
        Array<WordList> orderedListsAsStars = new Array<>();
        for (WordList wordList:DataHolder.dataholder.wordLists) {
            if (wordList.isStarry)
                orderedListsAsStars.add(wordList);
        }
        for (WordList wordList:DataHolder.dataholder.wordLists) {
            if (!wordList.isStarry)
                orderedListsAsStars.add(wordList);
        }

        //first order
        for (int i = 0; i < listCount; i++) {
            listNameCards.get(i).setWordList(orderedListsAsStars.get(i),
                    DataHolder.dataholder.wordLists.indexOf(orderedListsAsStars.get(i),true),true);
        }

        //change page codes;
        Group changePageGroup = getChangePageGroup(listNameCards,orderedListsAsStars);

        //custom list

        Table customListsTable = new Table();
        customListsTable.setSize(table.getWidth() - 120, 500);
        customListsTable.top().left();
        customListsTable.defaults().pad(AppInfo.PADDING);

        for (int i = 0; i < 1; i++) {

            ListNameCard group = new ListNameCard(new Vector2(w,h));
            group.setWordList(DataHolder.dataholder.favList,-1,false);


            customListsTable.add(group).width(w).height(h);

        }



        //Adding to table

//        table.debug();

        table.add(yourLists).padLeft(30 + AppInfo.PADDING).padTop(60).width(300).left();
        table.add(changePageGroup).padTop(60).padRight(AppInfo.PADDING).right().row();
        table.add(yourListsTable).padLeft(30).width(yourListsTable.getWidth()).height(yourListsTable.getHeight()).colspan(2).row();
        table.add(customLists).padLeft(30+ AppInfo.PADDING).padTop(60).width(300).left().colspan(2).row();
        table.add(customListsTable).padLeft(30).width(yourListsTable.getWidth()).height(yourListsTable.getHeight()).colspan(2).row();

        /*
         * Top changing Table için
         */

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

        btnCreateList = new TextButton("Create List",textButtonStyle);
        btnCreateList.setSize(200,50);
        btnCreateList.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                btnCreateListClicked();
            }
        });


        newListTextField = new CustomTextField(400,50);
        stage.getRoot().addCaptureListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                if (!(event.getTarget() instanceof TextField))
                    stage.setKeyboardFocus(null);
                return false;
            }
        });

        ((TextField) newListTextField.getChild(1)).setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char key) {
                if ((key == '\r' || key == '\n')){
                    btnCreateListClicked();
                }
            }
        });

    }

    private Group getChangePageGroup(final Array<ListNameCard> listNameCards,
                                    final Array<WordList> orderedListsAsStars) {
        final int[] pageIndex = {0};

        final int pageCount = (DataHolder.dataholder.wordLists.size +
                (9 - DataHolder.dataholder.wordLists.size%9)) / 9;



        //change page codes;
        Group changePageGroup = new Group();
        {
            Label.LabelStyle labelStyle = new Label.LabelStyle();
            labelStyle.font = Fonts.bl_bold_32;

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

//                    int remainedCardSize = DataHolder.dataholder.wordLists.size - 9 * pageIndex[0];
//                    int remainedCardSize = DataHolder.dataholder.wordLists.size - 9 * pageCount;
                    int remainedCardSize = (pageIndex[0] == pageCount - 1)?DataHolder.dataholder.wordLists.size % 9:9;
                    for (int i = 0; i < remainedCardSize; i++) {
                        listNameCards.get(i).setWordList(orderedListsAsStars.get(pageIndex[0] * 9 + i),
                                DataHolder.dataholder.wordLists.indexOf(orderedListsAsStars.get(pageIndex[0] * 9 + i),true),true);
                    }
                    for (int i = 0; i < 9 - remainedCardSize; i++) {
                        listNameCards.get(remainedCardSize + i).setVisible(false);
                    }
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

                    for (int i = 0; i < 9; i++) {
                        listNameCards.get(i).setWordList(orderedListsAsStars.get(pageIndex[0] * 9 + i),
                                DataHolder.dataholder.wordLists.indexOf(
                                        orderedListsAsStars.get(pageIndex[0] * 9 + i),true),true);
                        if (!listNameCards.get(i).isVisible()) listNameCards.get(i).setVisible(true);
                    }
                }
            });

            if (pageCount == 1) changePageGroup.setVisible(false);
        }

        return changePageGroup;
    }

    private void btnCreateListClicked() {
        String name = ((TextField) newListTextField.getChild(1)).getText();
        if (name.equals("")) {
            newListTextField.getChild(3).addAction(Actions.sequence
                    (Actions.color(Color.RED,0.3f), Actions.color(Color.WHITE,0.3f)));
            return;
        }
        for (WordList wordList:DataHolder.dataholder.wordLists) {
            if (wordList.listName.equals(name)){
                newListTextField.getChild(3).addAction(Actions.sequence
                        (Actions.color(Color.RED,0.3f), Actions.color(Color.WHITE,0.3f)));
                return;
            }
        }

        WordList wordList = new WordList();
        wordList.listName = name;
        DataHolder.dataholder.wordLists.add(wordList);
        DataHolder.dataholder.SELECTED_LIST_INDEX = DataHolder.dataholder.wordLists.indexOf(wordList,true);
        DataHolder.dataholder.saveList(wordList);
        appMain.setScreen(new WordListScreen(appMain));
    }


    class ListNameCard extends Group{



       public ListNameCard(Vector2 size){
           setSize(size.x,size.y);


       }

       public void setWordList(final WordList wordList,
                           int wordIndex,boolean hasStar){

           clear();
           Label.LabelStyle labelStyle = new Label.LabelStyle();
           labelStyle.font = Fonts.bl_bold_32;

           Label.LabelStyle smallLabelStyle = new Label.LabelStyle();
           smallLabelStyle.font =Fonts.bl_bold_20;

           Image bg = new Image(Assets.assets.pix);
           bg.setSize(getWidth(),getHeight());

           final Image bottomLine = new Image(Assets.assets.pix);
           bottomLine.setSize(getWidth(), 10);
           bottomLine.setColor(Color.valueOf("ffcd1f"));
           bottomLine.setVisible(false);



           Label listName = new Label(wordList.listName,labelStyle);
           listName.setColor(Color.BLACK);
           listName.setPosition(AppInfo.PADDING,getHeight() - AppInfo.PADDING - listName.getPrefHeight());

           Label wordCount = new Label(
                   wordList.words.size + " word",smallLabelStyle);
           wordCount.setPosition(listName.getX(),listName.getY() - wordCount.getPrefHeight());
           wordCount.setColor(Color.GRAY);

           listName.setTouchable(Touchable.disabled);
           wordCount.setTouchable(Touchable.disabled);
           bottomLine.setTouchable(Touchable.disabled);





           addActor(bg);
           addActor(listName);
           addActor(wordCount);
           addActor(bottomLine);
           setName("" + wordIndex);

           bg.addListener(new ClickListener(){
               @Override
               public void clicked(InputEvent event, float x, float y) {
                   super.clicked(event, x, y);
                   //TODO:
                   DataHolder.dataholder.SELECTED_LIST_INDEX = Integer.parseInt(getName());
                   appMain.setScreen(new WordListScreen(appMain));
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
           if (hasStar) {
               final Image star = new Image(Assets.assets.star);
               star.setColor(Color.LIGHT_GRAY);
               star.setPosition(getWidth() - star.getWidth() - 2 * AppInfo.PADDING,
                       2 * AppInfo.PADDING);
               star.setOrigin(Align.center);
               if (!wordList.isStarry)
                   star.setColor(Color.valueOf("f5f6fa"));
               else
                   star.setColor(Color.valueOf("ffcd1f"));

               addActor(star);

               star.addListener(new ButtonHoverAnimation(star) {
                   @Override
                   public void clicked(InputEvent event, float x, float y) {
                       super.clicked(event, x, y);
                       wordList.isStarry = !wordList.isStarry;

                       if (!wordList.isStarry)
                           star.setColor(Color.valueOf("f5f6fa"));
                       else
                           star.setColor(Color.valueOf("ffcd1f"));

                       DataHolder.dataholder.saveList(wordList);

                   }
               });
           }


       }




    }


}
