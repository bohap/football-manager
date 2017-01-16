package com.android.finki.mpip.footballdreamteam.utility;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Borce on 16.08.2016.
 */
public class ArrayUtilsTest {

    /**
     * Test that toInteger method will convert the array of ints to array on Integers.
     */
    @Test
    public void testToInteger() {
        int[] values = {1, 5, 674, 73473, 13, 15, 8, 3256, 263, 8, 75, 4587};
        Integer[] integers = ArrayUtils.toInteger(values);
        assertEquals(values.length, integers.length);
        for (int i = 0; i < values.length; i++) {
            assertEquals(values[i], integers[i].intValue());
        }
    }

    /**
     * Test that toInt method will convert the List of Integers to array of ints.
     */
    @Test
    public void testToIntOnList() {
        List<Integer> values = Arrays.asList(1, 5, 674, 73473, 13, 15, 8, 3256, 263, 8, 75, 4587);
        int[] result = ArrayUtils.toInt(values);
        assertEquals(values.size(), result.length);
        for (int i = 0; i < values.size(); i++) {
            assertEquals(values.get(i).intValue(), result[i]);
        }
    }
}
