package com.serhatmerak.wordlist.huds;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.serhatmerak.wordlist.helpers.AppInfo;

public class ShowWindow extends Group {

    public Image img;
    public Group group;

    public ShowWindow (Group windowGroup){
        group = windowGroup;
        Texture pixTexture = new Texture("pix.png");
        img = new Image(pixTexture);
        img.setSize(AppInfo.WIDTH,AppInfo.HEIGHT);
        img.setColor(0.2f,0.2f,0.2f,0.7f);

        addActor(img);
        windowGroup.setPosition(AppInfo.WIDTH / 2f - windowGroup.getWidth() / 2,
                AppInfo.HEIGHT / 2f - windowGroup.getHeight() / 2);
        addActor(windowGroup);

        addListener(new ClickListener(){
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ESCAPE)
                    remove();
                return super.keyDown(event, keycode);

            }
        });

        img.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                remove();
            }
        });
    }
}
