package com.serhatmerak.wordlist.huds;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.serhatmerak.wordlist.helpers.AppInfo;
import com.serhatmerak.wordlist.helpers.Assets;
import com.serhatmerak.wordlist.helpers.ButtonHoverAnimation;
import com.serhatmerak.wordlist.helpers.ColorPalette;
import com.serhatmerak.wordlist.helpers.Fonts;

public class QuestionPanel extends Group {
    private QuestionPanelStyle questionPanelStyle;


    public QuestionPanel(QuestionPanelStyle questionPanelStyle){
        this.questionPanelStyle = questionPanelStyle;
        createActors();
    }

    private void createActors() {

        Label.LabelStyle biggestStyle = new Label.LabelStyle();
        biggestStyle.font = Fonts.bl_bold_52;

        Label.LabelStyle bigStyle = new Label.LabelStyle();
        bigStyle.font = Fonts.bl_bold_45;

        Label.LabelStyle normalStyle = new Label.LabelStyle();
        normalStyle.font = Fonts.bl_bold_36;

        Image darkBg = new Image(Assets.assets.pix);
        darkBg.setSize(AppInfo.WIDTH,AppInfo.HEIGHT);
        setSize(AppInfo.WIDTH,AppInfo.HEIGHT);
        darkBg.setColor(0,0,0,0.7f);

        Group panelGroup = new Group();
        panelGroup.setWidth(800);

        Image topBg = new Image(Assets.assets.pix);
        topBg.setSize(panelGroup.getWidth(),100);
        topBg.setColor(ColorPalette.TopBlue);

        ///////////////////LABELS\\\\\\\\\\\\\\\\\\\
        Table table = new Table();
        table.setWidth(panelGroup.getWidth());
        table.defaults().pad(2 * AppInfo.PADDING).padBottom(0);

        if (!questionPanelStyle.bigText.equals("")){
            Label bigLabel = new Label(questionPanelStyle.bigText,biggestStyle);
            bigLabel.setWidth(table.getWidth() - 2 * table.defaults().getPadLeft());
            bigLabel.setWrap(true);
            bigLabel.setColor(Color.BLACK);

            table.add(bigLabel).width(bigLabel.getWidth()).row();
        }
        if (!questionPanelStyle.normalText.equals("")){
            Label normalLabel = new Label(questionPanelStyle.normalText,normalStyle);
            normalLabel.setWidth(table.getWidth() - 2 * table.defaults().getPadLeft());
            normalLabel.setWrap(true);
            normalLabel.setColor(Color.BLACK);

            table.add(normalLabel).width(normalLabel.getWidth()).padBottom(AppInfo.PADDING).row();
        }
        table.pack();

        float height = topBg.getHeight() + table.getHeight() + 100 + 4 * AppInfo.PADDING;
        table.setPosition(0,height - topBg.getHeight() - table.getHeight());

        Image wholeBg = new Image(Assets.assets.pix);
        wholeBg.setSize(questionPanelStyle.width,height);

        topBg.setPosition(0,wholeBg.getHeight() - topBg.getHeight());
        panelGroup.setHeight(wholeBg.getHeight());

        panelGroup.addActor(wholeBg);
        panelGroup.addActor(topBg);
        panelGroup.addActor(table);

        panelGroup.setPosition(AppInfo.WIDTH / 2f - panelGroup.getWidth() / 2,
                AppInfo.HEIGHT / 2f - panelGroup.getHeight() / 2 );
        addActor(darkBg);
        addActor(panelGroup);

        Label title = new Label(questionPanelStyle.title,bigStyle);
        title.setPosition(2 * AppInfo.PADDING,topBg.getY() + topBg.getHeight() / 2 - title.getHeight() / 2);

        panelGroup.addActor(title);

        Group closeButton = createCloseButton();
        closeButton.setPosition(topBg.getWidth() - 2 * AppInfo.PADDING - closeButton.getWidth(),
                topBg.getY() + topBg.getHeight() /2 - closeButton.getHeight() / 2);
        panelGroup.addActor(closeButton);
        closeButton.addListener(new ButtonHoverAnimation(closeButton,true));

        RoundedRectangle roundedTexture = new RoundedRectangle(340,100,10);

        Sprite oilOver = new Sprite(roundedTexture.lastTexture);
        oilOver.setColor(ColorPalette.OilBlueHover);

        Sprite oilUp = new Sprite(roundedTexture.lastTexture);
        oilUp.setColor(ColorPalette.OilBlue);

        if (!questionPanelStyle.isWarningPanel) {
            Sprite redUp = new Sprite(roundedTexture.lastTexture);
            redUp.setColor(ColorPalette.ButtonRed);

            Sprite redOver = new Sprite(roundedTexture.lastTexture);
            redOver.setColor(ColorPalette.ButtonRedHover);


            TextButton.TextButtonStyle yesStyle = new TextButton.TextButtonStyle();
            yesStyle.font = Fonts.bl_bold_36;
            yesStyle.up = new SpriteDrawable(redUp);
            yesStyle.over = new SpriteDrawable(redOver);

            TextButton btnYes = new TextButton(questionPanelStyle.yesTitle, yesStyle);
            btnYes.setSize(roundedTexture.getWidth(), roundedTexture.getHeight());

            TextButton.TextButtonStyle noStyle = new TextButton.TextButtonStyle();
            noStyle.font = yesStyle.font;
            noStyle.up = new SpriteDrawable(oilUp);
            noStyle.over = new SpriteDrawable(oilOver);

            TextButton btnNo = new TextButton(questionPanelStyle.noTitle, noStyle);
            btnNo.setSize(roundedTexture.getWidth(), roundedTexture.getHeight());

            btnYes.setPosition(panelGroup.getWidth() - 50 - btnYes.getWidth(), 2 * AppInfo.PADDING);
            btnNo.setPosition(50, 2 * AppInfo.PADDING);

            panelGroup.addActor(btnYes);
            panelGroup.addActor(btnNo);

            btnNo.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    questionPanelStyle.noClicked();
                    remove();
                }
            });

            btnYes.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    questionPanelStyle.yesClicked();
                    remove();
                }
            });

        }else {
            TextButton.TextButtonStyle noStyle = new TextButton.TextButtonStyle();
            noStyle.font = Fonts.bl_bold_36;
            noStyle.up = new SpriteDrawable(oilUp);
            noStyle.over = new SpriteDrawable(oilOver);


            TextButton btnOk = new TextButton(questionPanelStyle.warningTitle, noStyle);
            btnOk.setSize(roundedTexture.getWidth(), roundedTexture.getHeight());

            btnOk.setPosition(panelGroup.getWidth() - 50 - btnOk.getWidth(), 2 * AppInfo.PADDING);

            btnOk.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    questionPanelStyle.panelClosed();
                }
            });

        }



        closeButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                questionPanelStyle.panelClosed();
                remove();
            }
        });
    }

    private Group createCloseButton() {
        Group group = new Group();
        Image circle = new Image(Assets.assets.cancelCircle);
        circle.setColor(ColorPalette.LightTopBlue);
        circle.setSize(64,64);
        group.addActor(circle);

        Image x = new Image(Assets.assets.cancelX);
        x.setSize(64,64);
        x.setPosition(circle.getWidth() / 2 - x.getWidth() / 2,
                circle.getHeight() / 2 - x.getWidth() / 2);
        group.setSize(circle.getWidth(),circle.getHeight());
        group.addActor(x);
        circle.setOrigin(Align.center);
        x.setOrigin(Align.center);
        group.setOrigin(Align.center);
        return group;
    }


    private interface QuestionPanelListener{
        public void yesClicked();
        public void noClicked();
        public void panelClosed();
    }

    public static class QuestionPanelStyle implements QuestionPanelListener{
        public String title = "";
        public String yesTitle = "YES";
        public String noTitle = "NO";
        public String bigText = "";
        public String normalText = "";
        public boolean isWarningPanel = false;
        public String warningTitle = "OK!";
        public float width = 800;
        //height will be found automatically

        @Override
        public void yesClicked() {
        }

        @Override
        public void noClicked() {
        }

        @Override
        public void panelClosed() {
        }

        /*
        -------------
            title
        -------------
        Big
        normal
        dark

        --        ++
         */

    }
}





















