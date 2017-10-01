package com.steveflames.javalab.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

/**
 * Created by Flames on 24/9/2017.
 */

public class Fonts {
    public static BitmapFont xsmallMono;
    public static BitmapFont xsmall;
    public static BitmapFont small;
    public static BitmapFont medium;
    public static BitmapFont big;

    public Fonts() {
        xsmallMono = new BitmapFont();
        xsmall = new BitmapFont();
        small = new BitmapFont();
        medium = new BitmapFont();
        big = new BitmapFont();

        FileHandle fontFile = Gdx.files.internal("fonts/mvboli.ttf");
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 20;
        parameter.characters = "1234567890" +
                //"αβγδεζηθικλμνξοπρστυφχψως" +
                //"ΑΒΓΔΕΖΗΘΙΚΛΜΝΞΟΠΡΣΤΥΦΧΨΩ" +
                //"άέήίόύώΆΈΉΊΌΎΏΪ" +
                "+-:/!.*<>^;[](){},'?%" +
                "abcdefghijklmnopqrstuvwxyz" +
                "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        xsmall = generator.generateFont(parameter);
        parameter.size = 36;
        small = generator.generateFont(parameter);
        parameter.size = 60;
        big = generator.generateFont(parameter);
        parameter.size = 50;
        medium = generator.generateFont(parameter);


        fontFile = Gdx.files.internal("fonts/LiberationMono-Regular.ttf");generator = new FreeTypeFontGenerator(fontFile);
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 20;
        parameter.characters = "1234567890" +
                //"αβγδεζηθικλμνξοπρστυφχψως" +
                //"ΑΒΓΔΕΖΗΘΙΚΛΜΝΞΟΠΡΣΤΥΦΧΨΩ" +
                //"άέήίόύώΆΈΉΊΌΎΏΪ" +
                "`~!@#$%^&*()-=_+,<.>/?;:'\"{\\}|[]" +
                "abcdefghijklmnopqrstuvwxyz" +
                "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        xsmallMono = generator.generateFont(parameter);
        generator.dispose();

        Fonts.xsmallMono.setColor(Color.BLACK);
        Fonts.xsmall.setColor(Color.BLACK);
        Fonts.medium.setColor(Color.WHITE);
        Fonts.small.setColor(Color.WHITE);
        Fonts.big.setColor(Color.WHITE);
    }
}
