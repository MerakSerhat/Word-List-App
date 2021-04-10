package com.serhatmerak.wordlist.huds.wordcardhuds;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.serhatmerak.wordlist.helpers.AppInfo;
import com.serhatmerak.wordlist.helpers.Assets;
import com.serhatmerak.wordlist.helpers.ButtonHoverAnimation;
import com.serhatmerak.wordlist.helpers.ColorPalette;
import com.serhatmerak.wordlist.helpers.DataHolder;
import com.serhatmerak.wordlist.helpers.Fonts;
import com.serhatmerak.wordlist.kumar.SlowScrollPane;
import com.serhatmerak.wordlist.word.Word;
import com.serhatmerak.wordlist.word.WordList;

public class WordListOfWordScreen extends Group {
    private WordCardsHud wordCardsHud;

    public WordListOfWordScreen(WordCardsHud wordCardsHud){
        this.wordCardsHud = wordCardsHud;
        setSize(AppInfo.WIDTH * 0.9f,AppInfo.HEIGHT * 0.9f);
        createActors();
    }

    private void createActors() {
        ///basics
        {
            Image bg = new Image(Assets.assets.pix);
            bg.setSize(getWidth(), getHeight());

            Image topBg = new Image(Assets.assets.pix);
            topBg.setSize(getWidth(), 80);
            topBg.setColor(ColorPalette.TopBlue);
            topBg.setPosition(0, getHeight() - topBg.getHeight());

            addActor(bg);
            addActor(topBg);

            Label.LabelStyle titleStyle = new Label.LabelStyle();
            titleStyle.font = Fonts.bl_bold_36;

            Label title = new Label("Word List", titleStyle);
            title.setPosition(AppInfo.PADDING, topBg.getY() + topBg.getHeight() / 2 - title.getHeight() / 2);
            addActor(title);
        }

        {
            Texture pix = new Texture("pix.png");
            Sprite scroll = new Sprite(pix);
            Sprite knob = new Sprite(pix);

            scroll.setSize(20, 100);
            scroll.setColor(Color.LIGHT_GRAY);
            knob.setSize(20, 20);
            knob.setColor(Color.DARK_GRAY);
            ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
            scrollPaneStyle.vScroll = new SpriteDrawable(scroll);
            scrollPaneStyle.vScrollKnob = new SpriteDrawable(knob);

            ScrollPane scrollPane = new ScrollPane(null, scrollPaneStyle);
            scrollPane.setScrollingDisabled(true, false);
            scrollPane.setVariableSizeKnobs(true);
            scrollPane.setOverscroll(false, true);
            scrollPane.setSize(getWidth(), getHeight() - 80);
            scrollPane.setPosition(0, 0);
            addActor(scrollPane);

            Table table = new Table();
            table.defaults().pad(AppInfo.PADDING);
            table.setWidth(getWidth());
            table.top().left();
            scrollPane.setActor(table);

            Label.LabelStyle hugeStyle = new Label.LabelStyle();
            hugeStyle.font = Fonts.bl_bold_45;

            Label.LabelStyle minStyle = new Label.LabelStyle();
            minStyle.font = Fonts.bl_bold_32;

            WordList sortedList = DataHolder.dataholder.sortAZlist(DataHolder.dataholder.getSelectedList());
            String letter = "";
            int i = 0;
            int index = 0;
            for (Word word:sortedList.words){
                if (!word.name.substring(0,1).toLowerCase().replace("ı","i").equals(letter)){
                    Label label = new Label(word.name.substring(0,1).toLowerCase().replace("ı","i"),
                            hugeStyle);
                    label.setColor(Color.BLACK);
                    table.row();
                    table.add(label).left().colspan(5).row();
                    i = 0;
                    letter = word.name.substring(0,1).toLowerCase().replace("ı","i");
                }

                final Group group = new Group();
                {
                    Label label = new Label(word.name, minStyle);
                    label.setColor(Color.BLACK);
                    label.setAlignment(Align.center);

                    final Image bg = new Image(Assets.assets.pix);
                    bg.setSize((getWidth() - 8 * AppInfo.PADDING - 25) / 4,label.getPrefHeight() + 10);
                    bg.setColor(ColorPalette.Cream);
                    group.addActor(bg);

                    label.setPosition(bg.getWidth() / 2 - label.getWidth() / 2,5);
                    label.setTouchable(Touchable.disabled);


                    group.setSize(bg.getWidth(),bg.getHeight());

                    if (word.star) {
                        Image box = new Image(Assets.assets.pix);
                        box.setSize(10, group.getHeight());
                        box.setColor(Color.GOLD);
                        box.setPosition(group.getWidth() - box.getWidth(), 0);
                        group.addActor(box);
                    }else if (DataHolder.dataholder.knownWords.contains(word.name,false)){
                        Image box = new Image(Assets.assets.pix);
                        box.setSize(10, group.getHeight());
                        box.setColor(ColorPalette.text1);
                        box.setPosition(group.getWidth() - box.getWidth(), 0);
                        group.addActor(box);
                    }

                    ///Close Button
                    Group lblIndex;
                    {
                        lblIndex = new Group();
                        Label.LabelStyle xStyle = new Label.LabelStyle();
                        xStyle.font = Fonts.bl_bold_26;
                        Label xLabel = new Label((index + 1) + ")",xStyle);
                        xLabel.setColor(Color.BLACK);
                        lblIndex.addActor(xLabel);
                        lblIndex.setSize(xLabel.getWidth(),xLabel.getPrefHeight());
                        lblIndex.setPosition(5,bg.getHeight() / 2 - lblIndex.getHeight() / 2);
                        lblIndex.setOrigin(Align.center);
                        lblIndex.setTouchable(Touchable.disabled);

                    }
                    ///
                    final int Index = index;
                    group.addListener(new ClickListener(){
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            super.clicked(event, x, y);
                            wordCardsHud.changeCardWithIndex(Index);
                            WordListOfWordScreen.this.getParent().remove();
                        }

                        @Override
                        public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                            super.enter(event, x, y, pointer, fromActor);
                            bg.setColor(ColorPalette.LightOrange);
                        }

                        @Override
                        public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                            super.exit(event, x, y, pointer, toActor);
                            bg.setColor(ColorPalette.Cream);
                        }
                    });

                    if (label.getPrefWidth() <= group.getWidth() - 90){
                        group.addActor(label);
                    }else {

                        SlowScrollPane wordCardsScrollPane = new SlowScrollPane(null, scrollPaneStyle);
                        wordCardsScrollPane.setScrollingDisabled(false, true);
                        wordCardsScrollPane.setVariableSizeKnobs(true);
                        wordCardsScrollPane.setOverscroll(false, true);
                        wordCardsScrollPane.setSize(group.getWidth() - 90,group.getHeight());
                        wordCardsScrollPane.setPosition(10 + group.getWidth() / 2 - wordCardsScrollPane.getWidth() / 2,0);
                        wordCardsScrollPane.setActor(label);
                        group.addActor(wordCardsScrollPane);
                    }
                    group.addActor(lblIndex);


                }


                if (i != 0 && i%4 == 0)
                    table.row();



                table.add(group);
                i++;
                index++;
            }
        }
    }
}
