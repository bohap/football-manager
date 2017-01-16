package com.android.finki.mpip.footballdreamteam.utility;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by Borce on 08.08.2016.
 */
public class StringUtilsTest {

    /**
     * Test the behavior on the isEmpty method called with null value.
     */
    @Test
    public void testIsEmptyOnNull() {
        assertTrue(StringUtils.isEmpty(null));
    }

    /**
     * Test the behavior on isEmpty method called with zero length string.
     */
    @Test
    public void testIsEmptyOnZeroLengthString() {
        assertTrue(StringUtils.isEmpty(""));
    }

    /**
     * Test the behavior on isEmpty method called with empty spaces string
     */
    @Test
    public void testIsEmptyOnEmptySpacesString() {
        assertTrue(StringUtils.isEmpty("    "));
    }

    /**
     * Test the behavior on isEmpty method called with a regular string.
     */
    @Test
    public void testIsEmptyOnRegularString() {
        assertFalse(StringUtils.isEmpty("test"));
    }

    /**
     * Test the behavior on isEmail method called with a null param.
     */
    @Test
    public void testIsValidEmailOnNull() {
        assertFalse(StringUtils.isValidEmail(null));
    }

    /**
     * Test the behavior on isValidEmail called with a empty string.
     */
    @Test
    public void testIsValidEmailOnEmptyString() {
        assertFalse(StringUtils.isValidEmail("    "));
    }

    /**
     * Test the isValidEmail method works for invalid emails.
     */
    @Test
    public void testIsValidEmailOnInvalidEmails() {
        String email = "user.com";
        assertFalse(StringUtils.isValidEmail(email));
        email = "user@";
        assertFalse(StringUtils.isValidEmail(email));
        email = "user@com";
        assertFalse(StringUtils.isValidEmail(email));
        email = "user@com.";
        assertFalse(StringUtils.isValidEmail(email));
        email = "user@user.c";
        assertFalse(StringUtils.isValidEmail(email));
        email = "user@user.email";
        assertFalse(StringUtils.isValidEmail(email));
    }

    /**
     * Test that isValidEmail method works on valid email.
     */
    @Test
    public void testIsValidEmailOnValidEmails() {
        String email = "user@user.com";
        assertTrue(StringUtils.isValidEmail(email));
        email = "john-doe@email.com";
        assertTrue(StringUtils.isValidEmail(email));
        email = "john.doe@email.com";
        assertTrue(StringUtils.isValidEmail(email));
        email = "john.doe@email.em";
        assertTrue(StringUtils.isValidEmail(email));
    }

    /**
     * Test that capitalize method will convert the String first letter to upper case.
     */
    @Test
    public void testCapitalize() {
        String value = "simpleString";
        assertEquals("SimpleString", StringUtils.capitalize(value));
    }

    /**
     * Test that capitalize method works when the string first letter is upper case.
     */
    @Test
    public void testCapitalizeOnUpperFirstCase() {
        String value = "SimpleString";
        assertEquals(value, StringUtils.capitalize(value));
    }
}
