package com.serhatmerak.wordlist.huds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.serhatmerak.wordlist.helpers.Assets;
import com.serhatmerak.wordlist.helpers.ColorPalette;

public class BorderImage extends Group {
    Image bg,border;
    public float borderSize;
    public Image rightBorder;

    public BorderImage(Vector3 values){
        createImage(values, ColorPalette.box,ColorPalette.borderBox);
        borderSize = values.z;
    }

    public BorderImage(Vector3 values, Color bgColor, Color borderColor){
        createImage(values,bgColor,borderColor);
        borderSize = values.z;
    }

    private void createImage(Vector3 values, Color bgColor, Color borderColor){
        Texture pix = Assets.assets.pix;
        bg = new Image(pix);
        border = new Image(pix);
        rightBorder = new Image(pix);

        bg.setColor(bgColor);
        border.setColor(borderColor);

        bg.setSize(values.x - values.z * 2,values.y - values.z * 2);
        border.setSize(values.x,values.y);
        rightBorder.setSize(values.z,values.y - values.z * 2);

        bg.setPosition(values.z,values.z);
        border.setPosition(0,0);
        rightBorder.setPosition(0,values.z);

        setSize(values.x,values.y);

        addActor(border);
        addActor(bg);
    }

    @Override
    public void setHeight(float height) {
        super.setHeight(height);
        bg.setHeight(height - 2 * borderSize);
        border.setHeight(height);
        rightBorder.setHeight(height - borderSize * 2);
    }

    @Override
    public void setSize(float width, float height) {
        super.setSize(width, height);
        if (getParent() instanceof WordListTable) {
            bg.setHeight(height - 2 * borderSize);
            border.setHeight(height);
            rightBorder.setHeight(height - borderSize * 2);
        }
    }
}
