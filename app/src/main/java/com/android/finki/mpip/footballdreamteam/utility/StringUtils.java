package com.android.finki.mpip.footballdreamteam.utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Borce on 08.08.2016.
 */
public class StringUtils {

    /**
     * Checks if the given string value is a empty string;
     *
     * @param value string to be checked
     * @return whatever the string is empty
     */
    public static boolean isEmpty(String value) {
        return value == null || value.trim().length() == 0;
    }

    /**
     * Check if the given string is a valid email address.
     *
     * @param value string to be checked
     * @return whatever the string is a valid email address
     */
    public static boolean isValidEmail(String value) {
        if (isEmpty(value)) {
            return false;
        }
        String expression = "^[\\w\\.\\-]+@([\\w\\-]+\\.)+[a-zA-Z]{2,4}";
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }

    /**
     * Convert the string first letter to upper case.
     *
     * @param value String to be capitalize
     * @return capitalized String
     */
    public static String capitalize(String value) {
        return value.substring(0, 1).toUpperCase() + value.substring(1 ,value.length());
    }
}