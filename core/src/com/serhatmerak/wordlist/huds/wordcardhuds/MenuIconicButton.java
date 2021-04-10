package com.serhatmerak.wordlist.huds.wordcardhuds;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.serhatmerak.wordlist.helpers.Assets;
import com.serhatmerak.wordlist.helpers.ColorPalette;
import com.serhatmerak.wordlist.helpers.Fonts;
import com.serhatmerak.wordlist.huds.RoundedRectangle;

import javax.xml.soap.Text;

public class MenuIconicButton extends Group {

    private Image img;
    private RoundedRectangle bgRect;
    private RoundedRectangle smallRect;

    public MenuIconicButton(Texture imgTexture, String text){
        init(imgTexture,text,new Vector2(0,0),40);
    }

    public MenuIconicButton(Texture imgTexture, String text, Vector2 additionalEmpty){
        init(imgTexture,text,additionalEmpty,40);
    }

    public MenuIconicButton(Texture imgTexture, String text, float width){
        init(imgTexture,text,new Vector2(0,0),width);
    }

    public MenuIconicButton(Texture imgTexture, String text,
                            Vector2 additionalEmpty, float width){
        init(imgTexture,text,additionalEmpty,width);
    }

    private void init(Texture imgTexture, String text , Vector2 additionalEmpty,
                      float width){
        img = new Image(imgTexture);
        img.setSize(width,(img.getHeight() * width) / img.getWidth());

        bgRect = new RoundedRectangle(img.getWidth() + 16 + additionalEmpty.x,img.getHeight() + 16 + additionalEmpty.y,10);
        smallRect = new RoundedRectangle(img.getWidth() + 12 + additionalEmpty.x,img.getHeight() + 12 + additionalEmpty.y,10);

        smallRect.setColor(ColorPalette.topMenuBg);

        smallRect.setPosition((bgRect.getWidth() - smallRect.getWidth()) / 2,
                (bgRect.getHeight() - smallRect.getHeight()) / 2);
        img.setPosition((bgRect.getWidth() - img.getWidth()) / 2,
                (bgRect.getHeight() - img.getHeight()) / 2);


        addActor(bgRect);
        addActor(smallRect);
        addActor(img);

        setSize(bgRect.getWidth(),bgRect.getHeight());

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = Fonts.bl_bold_26;

        final Label label = new Label(text,labelStyle);
        label.setColor(Color.BLACK);

        final RoundedRectangle textBg = new RoundedRectangle(label.getWidth() + 24,label.getHeight() + 12,10);
        textBg.setPosition(getWidth() / 2 - textBg.getWidth() / 2,-6 - textBg.getHeight());

        label.setPosition(textBg.getX() + 12,textBg.getY() + 6);

        addActor(textBg);
        addActor(label);

        textBg.setTouchable(Touchable.disabled);
        label.setTouchable(Touchable.disabled);

        textBg.setVisible(false);
        label.setVisible(false);

        addListener(new ClickListener(){
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                textBg.setVisible(true);
                label.setVisible(true);

            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                textBg.setVisible(false);
                label.setVisible(false);
            }
        });

        setOrigin(Align.center);

    }

    @Override
    public void setColor(Color color) {
        super.setColor(color);
        img.setColor(color);
        bgRect.setColor(color);
        smallRect.setColor(ColorPalette.darkBg.r,ColorPalette.darkBg.g,ColorPalette.darkBg.b,color.a);
    }
}

