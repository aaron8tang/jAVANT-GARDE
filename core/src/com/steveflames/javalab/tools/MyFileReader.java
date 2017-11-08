package com.steveflames.javalab.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Flames on 4/10/2017.
 */

public class MyFileReader {

    public static String readFile(String path) {
        FileHandle file = Gdx.files.internal(path);
        if(exists(path))
            return file.readString();
        else
            return null;
    }

    public static boolean exists(String path) {
        return Gdx.files.internal(path).exists();
    }
}
