package com.serhatmerak.wordlist.huds;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.serhatmerak.wordlist.helpers.AppInfo;
import com.serhatmerak.wordlist.helpers.Assets;
import com.serhatmerak.wordlist.helpers.Fonts;

public class ColorButton extends Group {

    private Image bg,line;
    private Label label;
    private Image sign;

    private boolean selected = false;


    public ColorButton(Label label,Image sign){
        this.label = label;
        this.sign = sign;


        bg = new Image(Assets.assets.pix);
        line = new Image(Assets.assets.pix);
        line.setColor(Color.valueOf("ffcd1f"));
        line.setVisible(false);


        addActor(bg);
        addActor(sign);
        addActor(label);
        addActor(line);

        addListener(new ClickListener(){
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                bg.setColor(Color.valueOf("ffcd1f"));
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                bg.setColor(Color.WHITE);
            }
        });
    }

    public ColorButton(String text,Image sign){
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = Fonts.bl_bold_32;

        label = new Label(text,labelStyle);
        this.sign = sign;


        bg = new Image(Assets.assets.pix);
        line = new Image(Assets.assets.pix);
        line.setColor(Color.valueOf("ffcd1f"));
        line.setVisible(false);


        addActor(bg);
        addActor(sign);
        addActor(label);
        addActor(line);

        addListener(new ClickListener(){
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                bg.setColor(Color.valueOf("ffcd1f"));
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                bg.setColor(Color.WHITE);
            }
        });
    }

    public ColorButton(String text){
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = Fonts.bl_bold_26;

        label = new Label(text,labelStyle);
        label.setColor(Color.BLACK);

        bg = new Image(Assets.assets.pix);
        line = new Image(Assets.assets.pix);
        line.setColor(Color.valueOf("ffcd1f"));
        line.setVisible(false);

        addActor(bg);
        addActor(label);
        addActor(line);


        addListener(new ClickListener(){
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                bg.setColor(Color.valueOf("ffcd1f"));
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                if (pointer == -1) {
                    if (selected)
                        bg.setColor(Color.valueOf("fff9e3"));
                    else
                        bg.setColor(Color.WHITE);
                }
            }
        });
    }



    @Override
    public void setSize(float width, float height) {
        super.setSize(width, height);
        bg.setSize(width,height);
        line.setSize(10,height);


        if (sign != null) {
            float w = label.getWidth() + sign.getWidth() + 2 * AppInfo.PADDING;
            float h = Math.max(label.getHeight(), sign.getHeight());
            sign.setPosition(width / 2 - w / 2, h / 2 - sign.getWidth() / 2);
            label.setPosition(sign.getX() + 2 * AppInfo.PADDING,
                    h / 2 - label.getHeight() / 2);
        }else {
//            label.setPosition(width / 2 - label.getWidth() / 2,
//                    height / 2 - label.getHeight() / 2);
            label.setPosition(80,
                    height / 2 - label.getHeight() / 2);
        }


    }

    public void select() {
        if (!selected){
            selected = true;
            bg.setColor(Color.valueOf("fff9e3"));
            line.setVisible(true);
        }
    }

    public void unselect() {
        if (selected){
            selected = false;
            bg.setColor(Color.WHITE);
            line.setVisible(false);
        }
    }
}
