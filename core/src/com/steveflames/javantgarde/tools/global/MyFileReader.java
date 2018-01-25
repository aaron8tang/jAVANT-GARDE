package com.steveflames.javantgarde.tools.global;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

/**
 * Global tool for reading files, utilizing the LibGDX
 * class FileHandle.
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
