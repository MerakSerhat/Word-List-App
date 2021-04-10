package com.serhatmerak.wordlist.helpers;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class ButtonHoverAnimation extends ClickListener {
    private final boolean smaller;
    private Actor button;


    public ButtonHoverAnimation(Actor button){
        this.button = button;
        this.smaller = false;
    }

    public ButtonHoverAnimation(Actor button,boolean smaller){
        this.button = button;
        this.smaller = smaller;
    }


    @Override
    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        if (!smaller)
            button.addAction(Actions.repeat(3,Actions.scaleBy(0.1f,0.1f)));
        else
            button.addAction(Actions.repeat(3,Actions.scaleBy(0.05f,0.05f)));

    }

    @Override
    public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
        if (!smaller)
            button.addAction(Actions.repeat(3,Actions.scaleBy(-0.1f,-0.1f)));
        else
            button.addAction(Actions.repeat(3,Actions.scaleBy(-0.05f,-0.05f)));
    }
}