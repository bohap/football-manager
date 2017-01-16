package com.android.finki.mpip.footballdreamteam.utility;

import java.util.List;

/**
 * Created by Borce on 16.08.2016.
 */
public class ArrayUtils {

    /**
     * Convert the given array of int values to array of Integer values.
     *
     * @param values array of int values
     * @return array of Integers
     */
    public static Integer[] toInteger(int [] values) {
        Integer []result = new Integer[values.length];
        for (int i = 0; i < values.length; i++)  {
            result[i] = values[i];
        }
        return result;
    }

    /**
     * Convert the List of Integers to array of ints.
     *
     * @param values List of Integers
     * @return array os ints
     */
    public static int[] toInt(List<Integer> values) {
        int[] result = new int[values.size()];
        int i = 0;
        for (Integer value : values) {
            result[i++] = value;
        }
        return result;
    }
}
