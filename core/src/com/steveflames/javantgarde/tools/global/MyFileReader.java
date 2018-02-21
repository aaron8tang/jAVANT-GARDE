package com.steveflames.javantgarde.tools.global;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Global tool for reading files, utilizing the LibGDX
 * class FileHandle.
 */

public class MyFileReader {

    public static String readFile(String path) {
        InputStream in = new BufferedInputStream(Gdx.files.internal(path).read());
        if(exists(path)) {
            try {
                return new Scanner(in, "UTF-8").useDelimiter("\\A").next();
            }catch (NoSuchElementException e) {
                return "";
            }
        }
        else
            return null;
    }

    public static boolean exists(String path) {
        return Gdx.files.internal(path).exists();
    }
}
