package com.serhatmerak.wordlist.huds.addwordhuds;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.serhatmerak.wordlist.helpers.AppInfo;
import com.serhatmerak.wordlist.helpers.Assets;
import com.serhatmerak.wordlist.helpers.ColorPalette;
import com.serhatmerak.wordlist.helpers.Fonts;
import com.serhatmerak.wordlist.huds.Alert;
import com.serhatmerak.wordlist.huds.RoundedRectangle;
import com.serhatmerak.wordlist.kumar.SlowScrollPane;
import com.serhatmerak.wordlist.word.WordGenerator;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class AddWordWithTextHud extends Group {

    private AddWordWindow addWordWindow;

    public AddWordWithTextHud(float width,float height,AddWordWindow addWordWindow){
        this.addWordWindow = addWordWindow;
        setSize(width,height);
        createActors();
    }

    private void createActors() {
        createPasteActors();
    }

    private void createPasteActors() {
        RoundedRectangle roundedRectangle = new RoundedRectangle(getWidth() - 2 * AppInfo.PADDING,
                getHeight() - 3 * AppInfo.PADDING - 70,20);
        roundedRectangle.setPosition(AppInfo.PADDING,70 + 2 * AppInfo.PADDING);
        roundedRectangle.setColor(Color.LIGHT_GRAY);

        /////////
        RoundedRectangle buttonTexture = new RoundedRectangle(300,40,30);

        Sprite oilOver = new Sprite(buttonTexture.lastTexture);
        oilOver.setColor(ColorPalette.OilBlueHover);

        Sprite oilUp = new Sprite(buttonTexture.lastTexture);
        oilUp.setColor(ColorPalette.OilBlue);

        TextButton.TextButtonStyle noStyle = new TextButton.TextButtonStyle();
        noStyle.font = Fonts.bl_bold_26;
        noStyle.up = new SpriteDrawable(oilUp);
        noStyle.over = new SpriteDrawable(oilOver);


        final TextButton btnPaste = new TextButton("Paste Text", noStyle);
        btnPaste.setSize(buttonTexture.getWidth(), buttonTexture.getHeight());
        btnPaste.setPosition(roundedRectangle.getX() + roundedRectangle.getWidth() / 2 - btnPaste.getWidth() / 2,
                roundedRectangle.getY() + roundedRectangle.getHeight() / 2 - btnPaste.getHeight() / 2);

        /////////
        Sprite btnBgNorm = new Sprite(Assets.assets.pix);
        btnBgNorm.setSize(400,70);
        btnBgNorm.setColor(ColorPalette.ButtonBlue);

        Sprite btnBgHover = new Sprite(Assets.assets.pix);
        btnBgHover.setSize(400,70);
        btnBgHover.setColor(ColorPalette.ButtonOverBlue);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = Fonts.bl_bold_32;
        textButtonStyle.up = new SpriteDrawable(btnBgHover);
        textButtonStyle.over = new SpriteDrawable(btnBgNorm);
        textButtonStyle.fontColor = Color.WHITE;

        final TextButton btnFindWords = new TextButton("Find Words From Text",textButtonStyle);
        btnFindWords.setPosition(getWidth() - AppInfo.PADDING - btnFindWords.getWidth(),
                AppInfo.PADDING);
        btnFindWords.setSize(400,70);
        btnFindWords.setColor(1,1,1,0.5f);
        btnFindWords.setTouchable(Touchable.disabled);

        //////

        Sprite scroll = new Sprite(Assets.assets.pix);
        Sprite knob = new Sprite(Assets.assets.pix);

        scroll.setSize(20, 100);
        scroll.setColor(Color.LIGHT_GRAY);
        knob.setSize(20, 20);
        knob.setColor(Color.DARK_GRAY);
        ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        scrollPaneStyle.vScroll = new SpriteDrawable(scroll);
        scrollPaneStyle.vScrollKnob = new SpriteDrawable(knob);

        SlowScrollPane slowScrollPane = new SlowScrollPane(null, scrollPaneStyle);
        slowScrollPane.setScrollingDisabled(true, false);
        slowScrollPane.setVariableSizeKnobs(true);
        slowScrollPane.setOverscroll(false, true);
        slowScrollPane.setSize(roundedRectangle.getWidth() - 2 * AppInfo.PADDING,
                roundedRectangle.getHeight() - 2 * AppInfo.PADDING);
        slowScrollPane.setPosition(roundedRectangle.getX() + AppInfo.PADDING,
                roundedRectangle.getY() + AppInfo.PADDING);

        ///////

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = Fonts.bl_bold_26;


        final Label label = new Label("",labelStyle);
        label.setColor(Color.BLACK);
        label.setWidth(slowScrollPane.getWidth());
        label.setWrap(true);
        slowScrollPane.setActor(label);

        //////
        addActor(roundedRectangle);
        addActor(slowScrollPane);
        addActor(btnPaste);
        addActor(btnFindWords);

        btnPaste.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                btnPaste.addAction(Actions.sequence(
                        Actions.fadeOut(0.2f),
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                                final Transferable contents = clipboard.getContents(null);
                                boolean hasTransferableText = (contents != null) && contents.isDataFlavorSupported(DataFlavor.stringFlavor);
                                String copiedText = "";
                                if (hasTransferableText) {
                                    try {
                                        copiedText = (String) contents.getTransferData(DataFlavor.stringFlavor);

                                    } catch (UnsupportedFlavorException | IOException ignored) { }
                                    label.setText(copiedText);
                                    label.addAction(Actions.fadeOut(0));
                                    label.addAction(Actions.fadeIn(0.2f));

                                    btnFindWords.setColor(1,1,1,1);
                                    btnFindWords.setTouchable(Touchable.enabled);
                                }else {
                                    Alert.show(getStage(),"Copied data is not valid!");
                                    btnPaste.addAction(Actions.fadeIn(0.2f));

                                }

                            }
                        })
                ));
            }
        });

        btnFindWords.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                DividedTextGroup dividedTextGroup = new DividedTextGroup(label.getText().toString(),
                        new Vector2(getWidth(),getHeight()),addWordWindow);
                clear();
                addActor(dividedTextGroup);
            }
        });



    }
}
