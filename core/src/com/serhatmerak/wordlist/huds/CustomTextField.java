package com.serhatmerak.wordlist.huds;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.serhatmerak.wordlist.helpers.Assets;
import com.serhatmerak.wordlist.helpers.Fonts;

public class CustomTextField extends Group {

    private Label label,icon,btnClose;
    private RoundedRectangle roundedRectangle;
    public TextField textField;


    public CustomTextField(float width, float height){
        setSize(width,height);
        createBackground();
        createTextField();
        createLabel();
        createIcon();
        createBtnClose();
    }

    private void createBtnClose() {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = Fonts.bl_bold_32;

        btnClose= new Label("x",labelStyle);
        btnClose.setColor(Color.DARK_GRAY);
        btnClose.setPosition(getWidth() - 15 - btnClose.getWidth(),
                getHeight() / 2 - icon.getHeight() / 2);

        btnClose.addListener(new ClickListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("sa");
                if (!textField.getText().equals("")){
                    getStage().setKeyboardFocus(textField);
                    textField.setText("");
                }
                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }

    private void createIcon() {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = Fonts.bl_bold_32;

        icon= new Label(">",labelStyle);

        icon.setColor(Color.WHITE);
        icon.setPosition(15,getHeight() / 2 - icon.getHeight() / 2);
        addActor(icon);
    }

    private void createLabel() {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = Fonts.bl_bold_26;

        label = new Label("New List Name",labelStyle);
        label.setPosition(textField.getX() + 5,
                getHeight() / 2 - label.getHeight() / 2);
        label.setTouchable(Touchable.disabled);
        label.setColor(Color.WHITE);
        addActor(label);
    }

    private void createBackground() {
        roundedRectangle = new RoundedRectangle((int)getWidth(),(int)getHeight() , 15);
        roundedRectangle.setColor(Color.valueOf("5568ba"));
        addActor(roundedRectangle);
    }

    private void createTextField() {
        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        Sprite cursorSprite = new Sprite(Assets.assets.pix);
        cursorSprite.setSize(2,30);
        cursorSprite.setColor(Color.DARK_GRAY);
        textFieldStyle.cursor = new SpriteDrawable(cursorSprite);

        textFieldStyle.font = Fonts.bl_bold_32;

        Sprite selectionSprite = new Sprite(Assets.assets.pix);
        selectionSprite.setColor(Color.valueOf("007f7f"));
        textFieldStyle.selection = new SpriteDrawable(selectionSprite);

        Sprite bgSprite = new Sprite(Assets.assets.pix);
        bgSprite.setColor(Color.LIGHT_GRAY);
//        textFieldStyle.background = new SpriteDrawable(bgSprite);
        textFieldStyle.fontColor = Color.BLACK;

        textField = new TextField("",textFieldStyle);
        textField.setSize(getWidth() - 100,getHeight());
        textField.setPosition(50,0);

        addActor(textField);

    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (label.isVisible()){
            if (!textField.getText().equals(""))
                label.setVisible(false);
        }else {
            if (textField.getText().equals(""))
                label.setVisible(true);
        }


        if (getStage().getKeyboardFocus() == textField){
            if (!roundedRectangle.getColor().equals(Color.WHITE)) {
                roundedRectangle.setColor(Color.WHITE);
                addActor(btnClose);
                label.setColor(Color.DARK_GRAY);
                icon.setColor(Color.DARK_GRAY);
                textField.getStyle().fontColor = Color.BLACK;
            }
        }else {
            if (roundedRectangle.getColor().equals(Color.WHITE)) {
                roundedRectangle.setColor(Color.valueOf("5568ba"));
                btnClose.remove();
                label.setColor(Color.WHITE);
                icon.setColor(Color.WHITE);
                textField.getStyle().fontColor = Color.WHITE;
            }
        }
    }
}
