package com.android.finki.mpip.footballdreamteam;


import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Borce on 07.08.2016.
 */
public class TestUtils {

    /**
     * Read the content from a resource file.
     *
     * @param aClass class loader which is calling the method
     * @param file name of the file
     * @return file content
     */
    public static String readFile(ClassLoader aClass, String file) {
        String result = "";
        InputStream stream = aClass.getResourceAsStream(file);
        int i = 0;
        try {
            while ((i = stream.read()) != -1) {
                result += (char)i;
            }
        } catch (IOException e) {
            e.printStackTrace();
            result = null;
        }
        return result;
    }
}
