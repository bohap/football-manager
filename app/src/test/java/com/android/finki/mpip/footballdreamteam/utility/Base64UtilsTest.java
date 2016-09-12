package com.android.finki.mpip.footballdreamteam.utility;

import org.junit.Test;

import java.io.UnsupportedEncodingException;

import static junit.framework.Assert.fail;

/**
 * Created by Borce on 12.09.2016.
 */
public class Base64UtilsTest {

    private String text = "simple text";

    @Test
    public void testEncode() {
        try {
            String result = Base64Utils.encode(text);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testDecode() {

    }
}
