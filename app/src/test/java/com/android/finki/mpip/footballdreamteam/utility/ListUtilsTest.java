package com.android.finki.mpip.footballdreamteam.utility;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by Borce on 07.08.2016.
 */
public class ListUtilsTest {

    private Map<String, List<String>> map = new LinkedHashMap<>();
    private List<String> values = new ArrayList<>();

    /**
     * Put some test values into the map.
     */
    @Before
    public void setup() {
        List<String> mapElements = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            String value = String.format("Value %d", i + 1);
            values.add(value);
            mapElements.add(value);
            if ((i + 1) % 3 == 0) {
                map.put(String.format("key %d", i + 1), mapElements);
                mapElements = new ArrayList<>();
            }
        }
    }

    /**
     * Test that asList method called with Map will correctly convert Map values into a List
     */
    @Test
    public void testAsListWithMap() {
        List<String> list = ListUtils.asList(map);
        assertEquals(values.size(), list.size());
        for (int i = 0; i < values.size(); i++) {
            assertEquals(values.get(i), list.get(i));
        }
    }

    /**
     * Test that asList method called with Array will put the content of the array into a List.
     */
    @Test
    public void testAsListWithArray() {
        String[] items = {"Item 1", "Item 2", "Item 3", "Item 3"};
        List<String> result = ListUtils.asList(items);
        assertEquals(result.size(), items.length);
        for (int i = 0; i < result.size(); i++) {
            assertEquals(result.get(i), items[i]);
        }
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

    /**
     * Test the behavior when concat is called with null as the first param.
     */
    @Test
    public void testConcatOnNullFirstParam() {
        List<Integer> list1 = Arrays.asList(0, 2, 3);
        assertEquals(0, ListUtils.concat(null, list1).size());
    }

    /**
     * Test the behavior when concat is called with null as the first param.
     */
    @Test
    public void testConcatOnNullSecondParam() {
        List<Integer> list1 = Arrays.asList(0, 2, 3);
        assertEquals(0, ListUtils.concat(list1, null).size());
    }

    /**
     * Test the result on the concat method.
     */
    @Test
    public void testConcat() {
        List<Integer> list1 = Arrays.asList(0, 2, 3);
        List<Integer> list2 = Arrays.asList(1, 2, 3, 10, 100, 500);
        List<Integer> expected = Arrays.asList(0, 2, 3, 1, 10, 100, 500);
        List<Integer> result = ListUtils.concat(list1, list2);
        assertEquals(expected.size(), result.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), result.get(i));
        }
    }

    /**
     * Test the result on the concat called when the first param is empty list.
     */
    @Test
    public void testConcatOnEmptyFirstList() {
        List<Integer> list = Arrays.asList(1, 2, 3, 10, 100, 500);
        List<Integer> result = ListUtils.concat(new ArrayList<Integer>(), list);
        assertEquals(list.size(), result.size());
        for (int i = 0; i < list.size(); i++) {
            assertEquals(list.get(i), result.get(i));
        }
    }

    /**
     * Test the result on the concat called when the second param is empty list.
     */
    @Test
    public void testConcatOnEmptySecondList() {
        List<Integer> list = Arrays.asList(1, 2, 3, 10, 100, 500);
        List<Integer> result = ListUtils.concat(list, new ArrayList<Integer>());
        assertEquals(list.size(), result.size());
        for (int i = 0; i < list.size(); i++) {
            assertEquals(list.get(i), result.get(i));
        }
    }
}
