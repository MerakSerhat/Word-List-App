package com.serhatmerak.wordlist.huds;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.serhatmerak.wordlist.helpers.Assets;


public class RoundedRectangle extends Group {

    public Image img;
    public Texture lastTexture;
    Image imTop,imBot,imMid;
    Image cir1,cir2,cir3,cir4;

    public RoundedRectangle(int width,int height,int radius){
        create(width,height,radius);



//
//        imTop = new Image(Assets.assets.pix);
//        imBot = new Image(Assets.assets.pix);
//        imMid = new Image(Assets.assets.pix);
//
//        cir1 = new Image(Assets.assets.circle);
//        cir2 = new Image(Assets.assets.circle);
//        cir3 = new Image(Assets.assets.circle);
//        cir4 = new Image(Assets.assets.circle);
//
//        cir1.setSize(2 * radius,2 * radius);
//        cir2.setSize(2 * radius,2 * radius);
//        cir3.setSize(2 * radius,2 * radius);
//        cir4.setSize(2 * radius,2 * radius);
//
//        cir1.setPosition(0,0);
//        cir2.setPosition(width - radius * 2,0);
//        cir3.setPosition(0,height - radius * 2);
//        cir4.setPosition(width - radius * 2,height - radius * 2);
//
//        imTop.setSize(width - 2 * radius,radius);
//        imBot.setSize(imTop.getWidth(),imTop.getHeight());
//        imMid.setSize(width,height - 2 * radius);
//
//        imBot.setPosition(radius,0);
//        imTop.setPosition(radius,height - radius);
//        imMid.setPosition(0,radius);

//        addActor(imBot);
//        addActor(imMid);
//        addActor(imTop);
//        addActor(cir1);
//        addActor(cir2);
//        addActor(cir3);
//        addActor(cir4);
    }

    public RoundedRectangle(float v, float v1, int radius) {
        create((int)v,(int)v1,radius);
    }

    private void create(int width, int height, int radius) {
        radius = Math.min(Math.min(height,width) / 2,radius);
        setSize(width,height);

        Pixmap newPixmap = new Pixmap(width,height, Pixmap.Format.RGBA8888);
        newPixmap.setColor(Color.WHITE);
        newPixmap.fillCircle(radius,radius,radius);
        newPixmap.fillCircle(radius,height - radius,radius);
        newPixmap.fillCircle(width - radius,radius,radius);
        newPixmap.fillCircle(width - radius,height - radius,radius);

        newPixmap.fillRectangle(radius,0,width - 2 * radius,height);
        newPixmap.fillRectangle(0,radius,width,height - 2 * radius);

        lastTexture = new Texture(newPixmap);
        lastTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        img = new Image(lastTexture);
        addActor(img);
    }

    public RoundedRectangle(int width,int height,int radius,boolean forTop) {
        if (!forTop){
            create(width,height,radius);
            return;
        }

        radius = Math.min(Math.min(height, width) / 2, radius);
        setSize(width, height);

        Pixmap newPixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        newPixmap.setColor(Color.WHITE);
        newPixmap.fillCircle(radius, radius, radius);
//        newPixmap.fillCircle(radius, height - radius, radius);
        newPixmap.fillCircle(width - radius, radius, radius);
//        newPixmap.fillCircle(width - radius, height - radius, radius);

        newPixmap.fillRectangle(radius, 0, width - 2 * radius, height);
        newPixmap.fillRectangle(0, radius, width, height - radius);

        lastTexture = new Texture(newPixmap);
        lastTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        img = new Image(lastTexture);
        addActor(img);
    }

    @Override
    public void setColor(Color color) {
        super.setColor(color);
//        imTop.setColor(color);
//        imBot.setColor(color);
//        imMid.setColor(color);
//
//        cir1.setColor(color);
//        cir2.setColor(color);
//        cir3.setColor(color);
//        cir4.setColor(color);

        img.setColor(color);
    }

    @Override
    public void addAction(Action action) {
        super.addAction(action);
//        imBot.addAction(action);
//        imTop.addAction(action);
//        imMid.addAction(action);
//
//        cir1.addAction(action);
//        cir2.addAction(action);
//        cir3.addAction(action);
//        cir4.addAction(action);
        img.addAction(action);
    }

//    public static Texture combineTextures(Texture texture1, Texture texture2) {
//        texture1.getTextureData().prepare();
//        Pixmap pixmap1 = texture1.getTextureData().consumePixmap();
//
//        texture2.getTextureData().prepare();
//        Pixmap pixmap2 = texture2.getTextureData().consumePixmap();
//
//        pixmap1.drawPixmap(pixmap2, 0, 0);
//        Texture textureResult = new Texture(pixmap1);
//
//        pixmap1.dispose();
//        pixmap2.dispose();
//
//        return textureResult;
//    }
//
//    @Override
//    public void act(float delta) {
//        super.act(delta);
////        imMid.act(delta);
////        imTop.act(delta);
////        imTop.act(delta);
//    }
}
