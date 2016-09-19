package com.android.finki.mpip.footballdreamteam.utility;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Borce on 07.08.2016.
 */
public class ListUtils {

    /**
     * Map the content of the given Map to a List.
     *
     * @param map map containing the elements
     * @param <T> element type
     * @return List of Map element
     */
    public static <T> List<T> asList(Map<String, List<T>> map) {
        List<T> result = new ArrayList<>();
        for (Map.Entry<String, List<T>> entry : map.entrySet()) {
            List<T> elements = entry.getValue();
            for (T element : elements) {
                result.add(element);
            }
        }
        return result;
    }

    /**
     * Get all record from the list 1 that are in the list 2.
     *
     * @param list1 first List
     * @param list2 second List
     * @param <T>   List type
     * @return all records that are in the second list.
     */
    public static  <T> List<T> inTheList(List<T> list1, List<T> list2) {
        List<T> result = new ArrayList<>();
        for (T record : list1) {
            if (list2.contains(record)) {
                result.add(record);
            }
        }
        return result;
    }

    /**
     * Get all records in the list 1 that are not in the list 2.
     *
     * @param list1 first List
     * @param list2 second List
     * @param <T>   List type
     * @return all records that are no in second list
     */
    public static  <T> List<T> notInTheList(List<T> list1, List<T> list2) {
        List<T> result = new ArrayList<>();
        for (T record : list1) {
            if (!list2.contains(record)) {
                result.add(record);
            }
        }
        return result;
    }

    /**
     * Merge the two lists without adding duplicates.
     *
     * @param list1 first list
     * @param list2 second list
     * @param <T> list type
     * @return merged list
     */
    public static <T> List<T> concat(List<T> list1, List<T> list2) {
        if (list1 == null || list2 == null) {
            return new ArrayList<>();
        }
        Set<T> result = new LinkedHashSet<>(list1);
        result.addAll(list2);
        return new ArrayList<>(result);
    }
}
