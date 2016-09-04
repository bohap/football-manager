package com.android.finki.mpip.footballdreamteam.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Borce on 30.07.2016.
 */
public class DateUtils {

    private static String format = "yyyy-MM-dd HH:mm:ss";
    private static SimpleDateFormat formatter = new SimpleDateFormat(format);

    private static String dayNameFormat = "E dd.MM.yy 'at' HH:mm:ss";
    private static SimpleDateFormat dayNameFormatter = new SimpleDateFormat(dayNameFormat);

    /**
     * Format the given date to string.
     *
     * @param date Date to be formatted
     * @return formatted date
     */
    public static String format(Date date) {
        if (date == null) {
            return null;
        }
        return formatter.format(date);
    }

    /**
     * Format the current date to string.
     *
     * @return formatted date
     */
    public static String format() {
        return format(new Date());
    }

    /**
     * Parse the current string to date.
     *
     * @param date string to be parsed
     * @return parsed Date
     */
    public static Date parse(String date) {
        if (date == null) {
            return null;
        }
        Date result = null;
        try {
            result = formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Format the given date to a string containing the data name.
     *
     * @param date Date to be formatted
     * @return formatted date
     */
    public static String dayNameFormat(Date date) {
        if (date == null) {
            return null;
        }
        return dayNameFormatter.format(date);
    }

    /**
     * Get the number of years between the two dates.
     *
     * @param date1 first Date
     * @param date2 second Date
     * @return number of years between dates
     */
    public static int getYearDiff(Date date1, Date date2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);
        int diff = calendar1.get(Calendar.YEAR) - calendar2.get(Calendar.YEAR);
        if (calendar1.get(Calendar.MONTH) < calendar2.get(Calendar.MONTH) ||
                (calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH) &&
                calendar1.get(Calendar.DATE) < calendar2.get(Calendar.DATE))) {
            diff--;
        }
        return diff;
    }

    /**
     * Get the number of years between the current and the given date.
     *
     * @param date Date 2
     * @return number of years between current and given date
     */
    public static int getYearDiff(Date date) {
        return getYearDiff(new Date(), date);
    }
}
