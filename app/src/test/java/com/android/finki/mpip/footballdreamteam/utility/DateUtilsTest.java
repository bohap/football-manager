package com.android.finki.mpip.footballdreamteam.utility;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by Borce on 30.07.2016.
 */
public class DateUtilsTest {

    private int year = 2016, month = 8, day = 10, hour = 9, minute = 6, second = 3;
    private String sDate = String.format("%02d-%02d-%01d %02d:%02d:%02d",
            year, month, day, hour, minute, second);
    private Calendar calendar = new GregorianCalendar(year, month - 1, day, hour, minute, second);

    /**
     * Test that format method correctly format the date.
     */
    @Test
    public void testFormat() {
        String result = DateUtils.format(calendar.getTime());
        assertEquals(sDate, result);
    }

    /**
     * Test the behavior on format method called with null param.
     */
    @Test
    public void testFormatOnNull() {
        assertNull(DateUtils.format(null));
    }

    /**
     * Test that parse method correctly parse the date.
     */
    @Test
    public void testParse() {
        Date date = DateUtils.parse(sDate);
        assertNotNull(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        assertEquals(calendar.get(Calendar.YEAR), year);
        assertEquals(calendar.get(Calendar.MONTH), month - 1);
        assertEquals(calendar.get(Calendar.DAY_OF_MONTH), day);
        assertEquals(calendar.get(Calendar.HOUR_OF_DAY), hour);
        assertEquals(calendar.get(Calendar.MINUTE), minute);
        assertEquals(calendar.get(Calendar.SECOND), second);
    }

    /**
     * Test the behavior on parse method called with null param.
     */
    @Test
    public void testParseOnNull() {
        assertNull(DateUtils.parse(null));
    }

    /**
     * Test that getYearDiff works when param 1 date year and month is bigger than
     * param 2 date year and month.
     */
    @Test
    public void testGetYearDiffOnDate1YearAndMonthBiggerThanDate2YearAndMonth() {
        Calendar cal1 = new GregorianCalendar(2016, 8, 31);
        Calendar cal2 = new GregorianCalendar(2012, 7, 20);
        int diff = DateUtils.getYearDiff(cal1.getTime(), cal2.getTime());
        assertEquals(4, diff);
    }

    /**
     * Test that getYearDiff works when param 2 date month is bigger than param 1 date month.
     */
    @Test
    public void testGetYearDiffOnDate2MonthBiggerThanDadte1Month() {
        Calendar cal1 = new GregorianCalendar(2016, 8, 31);
        Calendar cal2 = new GregorianCalendar(2012, 9, 20);
        int diff = DateUtils.getYearDiff(cal1.getTime(), cal2.getTime());
        assertEquals(3, diff);
    }

    /**
     * Test the getYearDiff works when dates months are equal
     * and date 2 day is bigger date 1 day.
     */
    @Test
    public void testGetYearDiffOnEqualMonthAndDate2DayBiggerThanDate1Date() {
        Calendar cal1 = new GregorianCalendar(2016, 8, 20);
        Calendar cal2 = new GregorianCalendar(2012, 8, 30);
        int diff = DateUtils.getYearDiff(cal1.getTime(), cal2.getTime());
        assertEquals(3, diff);
    }

    /**
     * Test that getYearDiff works when date 2 year is bigger that date 1 year.
     */
    @Test
    public void testGetYearDIffWhenDate2YearIsBiggerThanDate1Year() {
        Calendar cal1 = new GregorianCalendar(2012, 7, 20);
        Calendar cal2 = new GregorianCalendar(2016, 8, 31);
        int diff = DateUtils.getYearDiff(cal1.getTime(), cal2.getTime());
        assertEquals(-5, diff);
    }

    /**
     * Test that getYearDiff works called with only one data param.
     */
    @Test
    public void testGetYearDiffOnCurrentDate() {
        Calendar cal = new GregorianCalendar(2012, 6, 20);
        int diff = DateUtils.getYearDiff(cal.getTime());
        assertEquals(4, diff);
    }
}
