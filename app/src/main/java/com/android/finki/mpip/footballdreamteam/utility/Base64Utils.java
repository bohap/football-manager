package com.android.finki.mpip.footballdreamteam.utility;

import android.util.Base64;

import java.io.UnsupportedEncodingException;

/**
 * Created by Borce on 12.09.2016.
 */
public class Base64Utils {

    /**
     * Encode the given string using Base64 representation.
     *
     * @param data string to be encoded
     * @return encoded string
     */
    public static String encode(String data) {
        byte[] bytes;
        try {
            bytes = data.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            bytes = data.getBytes();
        }
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    /**
     * Decode the given data using Base64 representation.
     *
     * @param data string to be decoded
     * @return decoded string
     */
    public static String decode(String data) {
        byte[] bytes;
        try {
            bytes = data.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            bytes = data.getBytes();
        }
        byte[] result = Base64.decode(bytes, Base64.DEFAULT);
        try {
            return new String(result, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return new String(result);
        }
    }
}
