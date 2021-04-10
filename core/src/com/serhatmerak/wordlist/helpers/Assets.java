package com.serhatmerak.wordlist.helpers;

import com.badlogic.gdx.graphics.Texture;

public class Assets {
    public static final Assets assets = new Assets();

    private Assets(){};

    public Texture pix;
    public Texture remove;
    public Texture star;
    public Texture tick;
    public Texture back;
    public Texture arrow;
    public Texture mix;
    public Texture pause;
    public Texture play;
    public Texture increase;
    public Texture keyboard;
    public Texture circle;
    public Texture save;
    public Texture box;
    public Texture expand;
    public Texture gold;
    public Texture cancelCircle;
    public Texture cancelX;
    public Texture menu;
    public Texture settings;
    public Texture restart;
    public Texture showCard;
    public Texture showWordData;
    public Texture w_letter;


    public void initalize(){
        pix = new Texture("pix.png");
        remove = new Texture("icons/delete.png");
        star = new Texture("icons/star.png");
        tick = new Texture("icons/tick.png");
        back = new Texture("icons/back.png");
        arrow = new Texture("icons/arrow.png");
        mix = new Texture("icons/mix.png");
        play = new Texture("icons/play.png");
        pause = new Texture("icons/pause.png");
        increase = new Texture("icons/increase.png");   
        keyboard = new Texture("icons/keyboard.png");
        circle = new Texture("icons/circle.png");
        save = new Texture("icons/save.png");
        box = new Texture("icons/box.png");
        expand = new Texture("icons/expand.png");
        gold = new Texture("icons/gold.png");
        cancelCircle = new Texture("icons/cancelCircle.png");
        cancelX = new Texture("icons/cancelX.png");
        menu = new Texture("icons/menu.png");
        settings = new Texture("icons/settings.png");
        restart = new Texture("icons/restart.png");
        showCard = new Texture("icons/showCard.png");
        w_letter = new Texture("icons/w_letter.png");
        showWordData = new Texture("icons/showWordData.png");



        remove.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        settings.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        star.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        tick.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        back.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        arrow.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        mix.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        play.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        pause.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        increase.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        keyboard.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        circle.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        save.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        box.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        expand.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        gold.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        cancelCircle.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        cancelX.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        menu.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        restart.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        showCard.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        showWordData.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        w_letter.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

    }
}
