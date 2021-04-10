package com.serhatmerak.wordlist.huds;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.serhatmerak.wordlist.helpers.AppInfo;
import com.serhatmerak.wordlist.helpers.Fonts;

public class Alert extends Group {

    private static Group lastAlert;

    public static void show(Stage stage,String text){
        show(stage,text,2);
    }

    public static void show(Stage stage,String text,int duration){

        if (lastAlert != null && lastAlert.getStage() != null){
            lastAlert.remove();
        }

        Group group = new Group();
//        group.setWidth(stage.getViewport().getWorldWidth() * (3f/4f));
//        group.setPosition(stage.getViewport().getWorldWidth() * (1f/8f),100);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = Fonts.bl_bold_32;

        Label label = new Label(text,labelStyle);

        label.setAlignment(Align.center);
        label.setPosition(2 * AppInfo.PADDING,AppInfo.PADDING);

        if (label.getWidth() > stage.getViewport().getWorldWidth() * (3f/4f)){
            label.setWidth(stage.getViewport().getWorldWidth() * (3f/4f));
            label.setWrap(true);
        }

        group.setWidth(label.getWidth() + 4 * AppInfo.PADDING);
        group.setPosition(stage.getViewport().getWorldWidth() / 2 - group.getWidth() / 2
                ,100);

        group.setHeight(label.getHeight() + 2 * AppInfo.PADDING);

        RoundedRectangle roundedRectangle = new RoundedRectangle((int)group.getWidth(),
                (int)group.getHeight(),30);
        roundedRectangle.setColor(Color.GRAY);
        roundedRectangle.setColor(Color.GRAY.r,
                Color.GRAY.g,Color.GRAY.b,0.85f);

        group.addActor(roundedRectangle);
        group.addActor(label);
        group.setColor(1,1,1,0);

        stage.addActor(group);
        group.addAction(Actions.sequence(
                Actions.fadeIn(0.5f, Interpolation.fastSlow),
                Actions.delay(duration),
                Actions.fadeOut(0.5f,Interpolation.slowFast),
                Actions.removeActor()
        ));

        group.setTouchable(Touchable.disabled);

        lastAlert = group;

    }

}
