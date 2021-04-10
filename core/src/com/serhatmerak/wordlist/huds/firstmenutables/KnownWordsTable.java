package com.serhatmerak.wordlist.huds.firstmenutables;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.serhatmerak.wordlist.helpers.AppInfo;
import com.serhatmerak.wordlist.helpers.Assets;
import com.serhatmerak.wordlist.helpers.DataHolder;
import com.serhatmerak.wordlist.helpers.Fonts;
import com.serhatmerak.wordlist.kumar.SlowScrollPane;

public class KnownWordsTable {

    public Group knownWordsGroup;
    public Group knownWordsTopGroup;

    private Table mainTable;

    public KnownWordsTable(Table mainTable){
        this.mainTable = mainTable;
        createKnownGroup();
    }

    private void createKnownGroup() {

        /*
         * MIIIIDDDLLLEE
         */

        ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = Fonts.bl_bold_32;

        knownWordsGroup = new Group();
        knownWordsGroup.setSize(mainTable.getWidth(),mainTable.getHeight());

        final Table yourWordsTable = new Table();
        yourWordsTable.setSize(knownWordsGroup.getWidth(),knownWordsGroup.getHeight());
        yourWordsTable.top().left();

        final Table easyWordsTable = new Table();
        easyWordsTable.setSize(knownWordsGroup.getWidth(),knownWordsGroup.getHeight());
        easyWordsTable.top().left();

        final Table easiestWordsTable = new Table();
        easiestWordsTable.setSize(knownWordsGroup.getWidth(),knownWordsGroup.getHeight());
        easiestWordsTable.top().left();


        Label yourLbl = new Label("Words You Know",labelStyle);
        yourLbl.setColor(Color.BLACK);

        Label easyLbl = new Label("Easy Words",labelStyle);
        easyLbl.setColor(Color.BLACK);

        Label easiestWords = new Label("Easiest Words",labelStyle);
        easiestWords.setColor(Color.BLACK);



        SlowScrollPane yourWordsScrollPane = getScrollPane(0);
        SlowScrollPane easyWordsScrollPane = getScrollPane(1);
        SlowScrollPane easiestWordsScrollPane = getScrollPane(2);


        yourWordsTable.add(yourLbl).padLeft(30 + AppInfo.PADDING).padTop(60).left().row();
        yourWordsTable.add(yourWordsScrollPane).width(knownWordsGroup.getWidth() - 60).padBottom(AppInfo.PADDING * 2)
                .padLeft(30 + AppInfo.PADDING).padTop(AppInfo.PADDING).left();

        easyWordsTable.add(easyLbl).padLeft(30 + AppInfo.PADDING).padTop(60).left().row();
        easyWordsTable.add(easyWordsScrollPane).width(knownWordsGroup.getWidth() - 60).padBottom(AppInfo.PADDING * 2)
                .padLeft(30 + AppInfo.PADDING).padTop(AppInfo.PADDING).left();

        easiestWordsTable.add(easiestWords).padLeft(30 + AppInfo.PADDING).padTop(60).left().row();
        easiestWordsTable.add(easiestWordsScrollPane).width(knownWordsGroup.getWidth() - 60).padBottom(AppInfo.PADDING * 2)
                .padLeft(30 + AppInfo.PADDING).padTop(AppInfo.PADDING).left();

        knownWordsGroup.addActor(yourWordsTable);
        knownWordsGroup.addActor(easiestWordsTable);
        knownWordsGroup.addActor(easyWordsTable);

        easiestWordsTable.setVisible(false);
        easyWordsTable.setVisible(false);



        /*
         * TOOOOOP
         */
        knownWordsTopGroup = new Group();

        final Label btnYourWords = new Label("Words you know",labelStyle);
        final Label btnEasiestWords = new Label("Easiest Words",labelStyle);
        final Label btnEasyWords = new Label("Easy Words",labelStyle);

        btnYourWords.setScale(1.2f);
        btnEasyWords.setScale(1.2f);
        btnEasiestWords.setScale(1.2f);

        final Image easiestBg = new Image(Assets.assets.pix);
        easiestBg.setSize(btnEasiestWords.getWidth() + 30,btnEasiestWords.getHeight() + 4);
        easiestBg.setColor( Color.valueOf("4257b2"));

        final Image easyBg = new Image(Assets.assets.pix);
        easyBg.setSize(btnEasyWords.getWidth() + 30,btnEasyWords.getHeight() + 4);
        easyBg.setColor( Color.valueOf("4257b2"));

        final Image yourBg = new Image(Assets.assets.pix);
        yourBg.setSize(btnYourWords.getWidth() + 30,btnYourWords.getHeight() + 4);
        yourBg.setColor( Color.valueOf("4257b2"));

        btnYourWords.setPosition(0,0);
        yourBg.setPosition(btnYourWords.getX() - 15,btnYourWords.getY() - 2);

        btnEasyWords.setPosition(btnYourWords.getWidth() +
                6 * AppInfo.PADDING,0);
        easyBg.setPosition(btnEasyWords.getX() - 15,btnEasyWords.getY() - 2);

        btnEasiestWords.setPosition(btnEasyWords.getX()
                + btnEasyWords.getWidth() + 6 * AppInfo.PADDING,0);
        easiestBg.setPosition(btnEasiestWords.getX() - 15,btnEasiestWords.getY() - 2);

        knownWordsTopGroup.setSize(easiestBg.getX() + easiestBg.getWidth(), easiestBg.getHeight());
        knownWordsTopGroup.addActor(easiestBg);
        knownWordsTopGroup.addActor(easyBg);
        knownWordsTopGroup.addActor(yourBg);
        knownWordsTopGroup.addActor(btnEasiestWords);
        knownWordsTopGroup.addActor(btnEasyWords);
        knownWordsTopGroup.addActor(btnYourWords);


        yourBg.setColor(Color.valueOf("ffcd1f"));
        btnYourWords.setColor(Color.BLACK);

        btnYourWords.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);


                yourBg.setColor(Color.valueOf("ffcd1f"));
                btnYourWords.setColor(Color.BLACK);

                easyBg.setColor(Color.valueOf("4257b2"));
                btnEasyWords.setColor(Color.WHITE);

                easiestBg.setColor(Color.valueOf("4257b2"));
                btnEasiestWords.setColor(Color.WHITE);
                //TODO: card change
                yourWordsTable.setVisible(true);
                easiestWordsTable.setVisible(false);
                easyWordsTable.setVisible(false);
            }
        });

        btnEasyWords.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                easyBg.setColor(Color.valueOf("ffcd1f"));
                btnEasyWords.setColor(Color.BLACK);

                yourBg.setColor(Color.valueOf("4257b2"));
                btnYourWords.setColor(Color.WHITE);

                easiestBg.setColor(Color.valueOf("4257b2"));
                btnEasiestWords.setColor(Color.WHITE);

                //TODO: card change
                yourWordsTable.setVisible(false);
                easiestWordsTable.setVisible(false);
                easyWordsTable.setVisible(true);
            }
        });

        btnEasiestWords.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                easiestBg.setColor(Color.valueOf("ffcd1f"));
                btnEasiestWords.setColor(Color.BLACK);

                easyBg.setColor(Color.valueOf("4257b2"));
                btnEasyWords.setColor(Color.WHITE);

                yourBg.setColor(Color.valueOf("4257b2"));
                btnYourWords.setColor(Color.WHITE);

                //TODO: card change
                yourWordsTable.setVisible(false);
                easiestWordsTable.setVisible(true);
                easyWordsTable.setVisible(false);
            }
        });




    }

    private SlowScrollPane getScrollPane(int id) {
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

        SlowScrollPane slowScrollPane = new SlowScrollPane(null,scrollPaneStyle);
        slowScrollPane.setScrollingDisabled(true,false);
        slowScrollPane.setVariableSizeKnobs(true);
        slowScrollPane.setOverscroll(false, true);
        slowScrollPane.setSize(knownWordsGroup.getWidth() - 60,knownWordsGroup.getHeight());


        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = Fonts.bl_bold_26;

        if (id == 0){

            final Table table = new Table();
            table.setSize(slowScrollPane.getWidth(),slowScrollPane.getHeight());
            table.top().left();

            int i = 0;

            for (final String word: DataHolder.dataholder.yourKnownWords) {
                final Label label  = new Label(word,labelStyle);
                label.setAlignment(Align.center);
                label.setColor(Color.BLACK);

                label.addListener(new ClickListener(){
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        super.clicked(event, x, y);
                        if (DataHolder.dataholder.knownWords.contains(word,false)){
                            label.setColor(Color.RED);
                            DataHolder.dataholder.knownWords.removeValue(word,false);
                            DataHolder.dataholder.yourKnownWords.removeValue(word,false);
                        }else {
                            label.setColor(Color.BLACK);
                            DataHolder.dataholder.knownWords.add(word);
                            DataHolder.dataholder.yourKnownWords.add(word);
                        }

                        //TODO: save
                        DataHolder.dataholder.saveYourKnownWords();
                    }
                });

                table.add(label).width(table.getWidth() / 5f);
                i++;
                if (i%5 == 0) table.row();
            }
            slowScrollPane.setActor(table);
            return slowScrollPane;



        }else if (id == 2){
            final Table table = new Table();
            table.setSize(slowScrollPane.getWidth(),slowScrollPane.getHeight());
            table.top().left();

            int i = 0;

            for (final String word:DataHolder.dataholder.firstKnownWords) {
                final Label label  = new Label(word,labelStyle);
                label.setAlignment(Align.center);
                label.setColor(Color.BLACK);

                if (DataHolder.dataholder.knownWords.contains(word,false))
                    label.setColor(Color.BLACK);
                else
                    label.setColor(Color.RED);


                label.addListener(new ClickListener(){
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        super.clicked(event, x, y);
                        if (DataHolder.dataholder.knownWords.contains(word,false)){
                            label.setColor(Color.RED);
                            DataHolder.dataholder.knownWords.removeValue(word,false);
                        }else {
                            label.setColor(Color.BLACK);
                            DataHolder.dataholder.knownWords.add(word);
                        }

                        //TODO: save
                        DataHolder.dataholder.saveKnownCustomWords();
                    }
                });

                table.add(label).width(table.getWidth() / 5f);
                i++;
                if (i%5 == 0) table.row();
            }
            slowScrollPane.setActor(table);
            return slowScrollPane;
        }else  if (id == 1){
            final Table table = new Table();
            table.setSize(slowScrollPane.getWidth(),slowScrollPane.getHeight());
            table.top().left();

            int i = 0;

            for (final String word:DataHolder.dataholder.secondKnownWords) {
                final Label label  = new Label(word,labelStyle);
                label.setAlignment(Align.center);
                label.setColor(Color.BLACK);

                if (DataHolder.dataholder.knownWords.contains(word,false))
                    label.setColor(Color.BLACK);
                else
                    label.setColor(Color.RED);


                label.addListener(new ClickListener(){
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        super.clicked(event, x, y);
                        if (DataHolder.dataholder.knownWords.contains(word,false)){
                            label.setColor(Color.RED);
                            DataHolder.dataholder.knownWords.removeValue(word,false);
                        }else {
                            label.setColor(Color.BLACK);
                            DataHolder.dataholder.knownWords.add(word);
                        }

                        //TODO: save
                        DataHolder.dataholder.saveKnownCustomWords();
                    }
                });

                table.add(label).width(table.getWidth() / 5f);
                i++;
                if (i%5 == 0) table.row();
            }
            slowScrollPane.setActor(table);
            return slowScrollPane;
        }
        return null;
    }


}
