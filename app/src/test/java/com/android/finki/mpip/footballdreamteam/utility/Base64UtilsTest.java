package com.android.finki.mpip.footballdreamteam.utility;

import android.os.Build;
import android.util.Base64;

import com.android.finki.mpip.footballdreamteam.BuildConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.io.UnsupportedEncodingException;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Borce on 19.09.2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class Base64UtilsTest {

    private Base64Utils utils = new Base64Utils();

    /**
     * Test the behavior on encode called with null param.
     */
    @Test
    public void testEncodeOnNull() {
        assertEquals("", utils.encode(null));
    }

    /**
     * Test that encode method encodes the given string.
     */
    @Test
    public void testSuccessEncode() {
        String test = "Test";
        String encoded = Base64.encodeToString(test.getBytes(), Base64.DEFAULT);
        assertEquals(encoded, utils.encode(test));
    }

    /**
     * Test the behavior on encode called with utf-8 string.
     */
    @Test
    public void testEncodeOnUTF8() throws UnsupportedEncodingException {
        String test = "Тест";
        String encoded = Base64.encodeToString(test.getBytes("UTF8"), Base64.DEFAULT);
        assertEquals(encoded, utils.encode(test));
    }

    /**
     * Test the behavior on decode called with null.
     */
    @Test
    public void testDecodeOnNull() {
        assertEquals("", utils.decode(null));
    }

    /**
     * Test the decode method decodes the given string.
     */
    @Test
    public void testSuccessDecode() {
        String test = "Success Decode";
        String encoded = Base64.encodeToString(test.getBytes(), Base64.DEFAULT);
        String decoded = new String(Base64.decode(encoded.getBytes(), Base64.DEFAULT));
        assertEquals(decoded, test);
        assertEquals(decoded, utils.decode(encoded));
    }
}
