package com.serhatmerak.wordlist.kumar;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.serhatmerak.wordlist.AppMain;
import com.serhatmerak.wordlist.helpers.AppInfo;
import com.serhatmerak.wordlist.helpers.CustomScreen;
import com.serhatmerak.wordlist.helpers.Fonts;
import com.serhatmerak.wordlist.huds.WordListsMainActor;

public class KumarGraf extends CustomScreen {

    AppMain app;
    SpriteBatch batch;
    OrthographicCamera camera;
    Viewport viewport;
    Stage stage;

    public KumarGraf(AppMain app) {
        this.app = app;
        batch = app.batch;
        camera = new OrthographicCamera(AppInfo.WIDTH, AppInfo.HEIGHT);
        camera.setToOrtho(false, AppInfo.WIDTH, AppInfo.HEIGHT);
        viewport = new FitViewport(AppInfo.WIDTH, AppInfo.HEIGHT, camera);
        stage = new Stage(viewport, batch);


        Kumar kumar = new Kumar();
        setGrap(kumar.karlar);
        setOrtalama(kumar.karlar);

        Gdx.input.setInputProcessor(stage);
    }

    private void setOrtalama(int [] karlar) {
        int top  = 0;
        for(int i = 0 ; i < karlar.length ; i++) {
            top += karlar[i];
        }
        int ortalama = top / 1000;

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = Fonts.bl_bold_45;

        Label ort = new Label("10 sigarada Ortalama kazanç: " + ortalama,labelStyle);
        ort.setPosition(AppInfo.WIDTH / 2f - ort.getPrefWidth(),700);

        stage.addActor(ort);
    }

    private void setGrap(int[] karlar) {
        for(int i = 0 ; i < karlar.length ; i++) {
            Image img = new Image(new Texture("pix.png"));
            img.setSize(30,20);
            img.setColor(new Color(0,0,0,0.02f));
            img.setPosition(200 + karlar[i] * 1.5f,500);
            stage.addActor(img);
        }
    }

    public void render(float delta) {
        Gdx.gl.glClearColor(0.4f, 0.4f, 0.4f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.end();

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);

    }
}
