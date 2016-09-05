package com.android.finki.mpip.footballdreamteam.utility;

import com.android.finki.mpip.footballdreamteam.model.Lineup;
import com.android.finki.mpip.footballdreamteam.model.Player;
import com.android.finki.mpip.footballdreamteam.model.User;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;
/**
 * Created by Borce on 07.08.2016.
 */
public class ListUtilsTest {

    Map<String, List<String>> map = new LinkedHashMap<>();

    private final int NUMBER_OF_ITEMS = 9;
    private final String string1 = "Value 1";
    private final String string2 = "Value 2";
    private final String string3 = "Value 3";
    private final String string4 = "Value 4";
    private final String string5 = "Value 5";
    private final String string6 = "Value 6";
    private final String string7 = "Value 7";
    private final String string8 = "Value 8";
    private final String string9 = "Value 9";

    /**
     * Put some test values into the map.
     */
    @Before
    public void setup() {
        List<String> element = new ArrayList<>();
        element.add(string1);
        element.add(string2);
        element.add(string3);
        map.put("key 1", element);
        element = new ArrayList<>();
        element.add(string4);
        element.add(string5);
        element.add(string6);
        map.put("key 2", element);
        element = new ArrayList<>();
        element.add(string7);
        element.add(string8);
        element.add(string9);
        map.put("key 3", element);
    }

    /**
     * Test that asList method will correctly convert Map values into a List
     */
    @Test
    public void testAsList() {
        List<String> list = ListUtils.asList(map);
        assertNotNull(list);
        assertEquals(NUMBER_OF_ITEMS, list.size());
        assertEquals(string1, list.get(0));
        assertEquals(string2, list.get(1));
        assertEquals(string3, list.get(2));
        assertEquals(string4, list.get(3));
        assertEquals(string5, list.get(4));
        assertEquals(string6, list.get(5));
        assertEquals(string7, list.get(6));
        assertEquals(string8, list.get(7));
        assertEquals(string9, list.get(8));
    }

    /**
     * Test that inTheList method will return all records that are in the second list.
     */
    @Test
    public void testInTheList() {
        List<Integer> list1 = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> list2 = Arrays.asList(2, 5, 10, 200, 1412, 3);
        List<Integer> result = ListUtils.inTheList(list1, list2);
        assertEquals(3, result.size());
        assertEquals(2, result.get(0).intValue());
        assertEquals(3, result.get(1).intValue());
        assertEquals(5, result.get(2).intValue());
    }

    /**
     * Test that notInTheList will return all records that are not in the second list.
     */
    @Test
    public void testNotInTheList() {
        List<Integer> list1 = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> list2 = Arrays.asList(2, 5, 10, 200, 1412, 3);
        List<Integer> result = ListUtils.notInTheList(list1, list2);
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).intValue());
        assertEquals(4, result.get(1).intValue());
    }
}
