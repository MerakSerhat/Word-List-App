package com.serhatmerak.wordlist.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class Fonts {



    public static BitmapFont bl_bold_52;
    public static BitmapFont bl_bold_45;
    public static BitmapFont bl_bold_36;
    public static BitmapFont bl_bold_26;
    public static BitmapFont bl_bold_32;
    public static BitmapFont bl_bold_20;
    public static BitmapFont bl_bold_72;
    public static BitmapFont bl_bold_96;
    public static BitmapFont consola18;

    public static String characters =  ">×abâîcçdefgğhıijklmnoö'><öprsştuüvwyxzqABCÇDEFGĞHIİJKLMNOÖPRSŞTUÜVXWYZQ:,?.=!0123456789:+\"() / -%";


    public static void createFonts(){
        createBlinkerBold();
        createConsole();
        bl_bold_45.getData().markupEnabled = true;
        bl_bold_72.getData().markupEnabled = true;
        bl_bold_96.getData().markupEnabled = true;
        bl_bold_52.getData().markupEnabled = true;
        bl_bold_36.getData().markupEnabled = true;
        bl_bold_26.getData().markupEnabled = true;
        bl_bold_32.getData().markupEnabled = true;
        bl_bold_20.getData().markupEnabled = true;

    }

    private static void createConsole() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/consola.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter16 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter16.size = 24;
        parameter16.characters = characters;
        parameter16.minFilter = Texture.TextureFilter.Linear;
        parameter16.magFilter = Texture.TextureFilter.Linear;
        consola18 = generator.generateFont(parameter16); // font size 12 pixels
    }

    private static void createBlinkerBold() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/OpenSans-SemiBold.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter16 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter16.size = 45;
        characters += parameter16.characters;

        parameter16.characters = characters;
        parameter16.minFilter = Texture.TextureFilter.Linear;
        parameter16.magFilter = Texture.TextureFilter.Linear;
        bl_bold_45 = generator.generateFont(parameter16); // font size 12 pixels

        FreeTypeFontGenerator.FreeTypeFontParameter parameter52 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter52.size = 52;
        parameter52.characters = characters;
        parameter52.minFilter = Texture.TextureFilter.Linear;
        parameter52.magFilter = Texture.TextureFilter.Linear;
        bl_bold_52 = generator.generateFont(parameter52); // font size 12 pixels

        FreeTypeFontGenerator.FreeTypeFontParameter parameter36 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter36.size = 36;
        parameter36.characters = characters;
        parameter36.minFilter = Texture.TextureFilter.Linear;
        parameter36.magFilter = Texture.TextureFilter.Linear;
        bl_bold_36 = generator.generateFont(parameter36); // font size 12 pixels


        FreeTypeFontGenerator.FreeTypeFontParameter parameter20 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter20.size = 26;
        parameter20.characters = characters;
        parameter20.minFilter = Texture.TextureFilter.Linear;
        parameter20.magFilter = Texture.TextureFilter.Linear;
        bl_bold_26 = generator.generateFont(parameter20); // font size 12 pixels

        FreeTypeFontGenerator.FreeTypeFontParameter parameter24 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter24.size = 32;
        parameter24.characters = characters;
        parameter24.minFilter = Texture.TextureFilter.Linear;
        parameter24.magFilter = Texture.TextureFilter.Linear;
        bl_bold_32 = generator.generateFont(parameter24); // font size 12 pixels

        FreeTypeFontGenerator.FreeTypeFontParameter parameter72 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter72.size = 64;
        parameter72.characters = characters;
        parameter72.minFilter = Texture.TextureFilter.Linear;
        parameter72.magFilter = Texture.TextureFilter.Linear;
        bl_bold_72 = generator.generateFont(parameter72); // font size 12 pixels

        FreeTypeFontGenerator.FreeTypeFontParameter parameter80 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter80.size = 96;
        parameter80.characters = characters;
        parameter80.minFilter = Texture.TextureFilter.Linear;
        parameter80.magFilter = Texture.TextureFilter.Linear;
        bl_bold_96 = generator.generateFont(parameter80); // font size 12 pixels


        FreeTypeFontGenerator.FreeTypeFontParameter parameter21 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter21.size = 20;
        parameter21.characters = characters;
        parameter21.minFilter = Texture.TextureFilter.Linear;
        parameter21.magFilter = Texture.TextureFilter.Linear;
        bl_bold_20 = generator.generateFont(parameter21); // font size 12 pixels

        generator.dispose(); // don't forget to dispose to avoid memory leaks!
    }
/*
    private static void createBlinkerRegular() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/OpenSans-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter16 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter16.size = 22;
        parameter16.characters = characters;
        parameter16.minFilter = Texture.TextureFilter.Linear;
        parameter16.magFilter = Texture.TextureFilter.Linear;
        bl_regular_16 = generator.generateFont(parameter16); // font size 12 pixels

        FreeTypeFontGenerator.FreeTypeFontParameter parameter20 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter20.size = 26;
        parameter20.characters = characters;
        parameter20.minFilter = Texture.TextureFilter.Linear;
        parameter20.magFilter = Texture.TextureFilter.Linear;
        bl_regular_20 = generator.generateFont(parameter20); // font size 12 pixels

        FreeTypeFontGenerator.FreeTypeFontParameter parameter24 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter24.size = 32;
        parameter24.characters = characters;
        parameter24.minFilter = Texture.TextureFilter.Linear;
        parameter24.magFilter = Texture.TextureFilter.Linear;
        bl_regular_24 = generator.generateFont(parameter24); // font size 12 pixels

        generator.dispose(); // don't forget to dispose to avoid memory leaks!
    }
*/


}
